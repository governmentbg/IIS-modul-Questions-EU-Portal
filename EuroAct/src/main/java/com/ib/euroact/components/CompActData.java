package com.ib.euroact.components;

import java.util.Date;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.exceptions.ObjectInUseException;
import com.ib.system.utils.SearchUtils;

/** */
@FacesComponent(value = "compActData", createTag = true)
public class CompActData extends UINamingContainer {
	
	private enum PropertyKeys {
		  ACT, SHOWME
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CompActData.class);
	public static final String	UIBEANMESSAGES = "ui_beanMessages";
	public static final String	BEANMESSAGES = "beanMessages";
	public static final String  MSGPLSINS = "general.pleaseInsert";
	public static final String  ERRDATABASEMSG = "general.errDataBaseMsg";
	public static final String  SUCCESSAVEMSG = "general.succesSaveMsg";
	public static final String	OBJECTINUSE		 = "general.objectInUse";
	public static final String	LABELS = "labels";
	private static final String CODEACT = "codeAct";
	
	private String errMsg = null;
	private SystemData systemData = null; // може би ще ми трябва, за да извиквам метод за презареждане на класификацията след промяна или изтриване
	private UserData userData = null;
	private Date dateClassif = null;
	private EuroActNew tmpAct;
	
	private boolean showNoteSr = false;
	

	/**
	 * Данни на акт - актуализация и разглеждане
	 * @return
	 * @throws DbErrorException
	 */
	public void initAct() {		
		
		//boolean modal = (Boolean) getAttributes().get("modal"); // обработката е в модален диалог (true) или не (false)
		
		Integer idAct = (Integer) getAttributes().get(CODEACT); 
		
		if(idAct != null) {
			loadAct(idAct);
		
		} else { // нов
			String srchTxt = (String) getAttributes().get("searchTxt"); 
			clearAct(srchTxt);
			
			ValueExpression expr2 = getValueExpression("searchTxt"); //зачиствам текста - искам да се изпозлва само при първото отваряне
			ELContext ctx2 = getFacesContext().getELContext();
			if (expr2 != null) {
				expr2.setValue(ctx2, null);
			}	
		}
		
		setShowMe(true);
		setErrMsg(null);
		LOGGER.debug("initAct");
	}
	
	/**
	 *  зарежда данни за корепондент по зададени критерии
	 */
	private boolean loadAct(Integer idAct) {
		
		boolean bb = true;		
		
		try {
			
			setErrMsg(null);
			
			if (idAct != null) {
				JPA.getUtil().runWithClose(() -> tmpAct = new EuroActNewDAO(getUserData()).findById(idAct));
				
				if (tmpAct.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
						|| tmpAct.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
						|| tmpAct.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
						|| tmpAct.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
						|| tmpAct.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA) ) {
						
						showNoteSr = true;
					
					} else {
						showNoteSr = false;
					}
				
			} else {					
				bb = false;				
			}			
			
			if(bb) {				
				setAct(tmpAct);
			}
			
			tmpAct = null;
			
			LOGGER.debug("loadAct");
		
		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане на данни за акт! ", e);
		}
		
		return bb;
	}
	
	/**
	 * Запис на акт
	 */
	public void actionSave() {
		
		errMsg = " Моля, въведете задължителната информация!";
		
		if (checkData()) {
			
			errMsg = null;
			
			try {
				
				LOGGER.debug("actionSave>>>> ");
				
				JPA.getUtil().runInTransaction(() -> this.tmpAct = new EuroActNewDAO(getUserData()).save(getAct()));
				
				getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);
				
				if (tmpAct != null && tmpAct.getId() != null) {
					// връща id на избрания кореспондент
					ValueExpression expr2 = getValueExpression(CODEACT);
					ELContext ctx2 = getFacesContext().getELContext();
					if (expr2 != null) {
						expr2.setValue(ctx2, tmpAct.getId());
					}
				}

				// извиква remoteCommnad - ако има такава....
				String remoteCommnad = (String) getAttributes().get("onComplete");
				if (remoteCommnad != null && !"".equals(remoteCommnad)) {
					PrimeFaces.current().executeScript(remoteCommnad);
				}
				
//				setAct(tmpAct); // излишно е, ако веднага при запис се затваря модалния, иначе не е....

				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, SUCCESSAVEMSG));
			
			} catch (BaseException e) {
				LOGGER.error("Грешка при запис на кореспондент ", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
			}
		}
	}

	/**
	 * Проверка за валидни данни
	 * 
	 * @return
	 */
	public boolean checkData() {
		
		boolean flagSave = true;
		
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = null;
		tmpAct = getAct();
		
		if (context != null && tmpAct != null) {
			clientId = this.getClientId(context);
			
			if (SearchUtils.isEmpty(tmpAct.getRnFull())) {
				JSFUtils.addMessage(clientId + ":nomerAct", FacesMessage.SEVERITY_ERROR, 
						IndexUIbean.getMessageResourceString(BEANMESSAGES, "compActData.nomerAct"));
				flagSave = false;
			}
			
			if(tmpAct.getVidAct() == null) {
				JSFUtils.addMessage(clientId + ":vidAct:аutoCompl", FacesMessage.SEVERITY_ERROR, 
						IndexUIbean.getMessageResourceString(BEANMESSAGES, "compActData.vidAct"));
				flagSave = false;
			}

			if (!flagSave) {
				errMsg = " Моля, въведете задължителната информация! ";
			}

		} else {
			flagSave = false;
		}
		
		return flagSave;
	}

	/**
	 * Зачиства данните на акт - бутон "нов"
	 * 
	 */
	public void clearAct(String srchTxt) {
		
		tmpAct = new EuroActNew();		
		tmpAct.setIme(srchTxt);
		setAct(tmpAct);		
	}
	
	public void actionCheckForExistAct() {

		if (getAct().getVidAct() != null) {

			try {
				
				if (!getAct().getRnFull().isEmpty()) { 
				
					EuroActNewDAO actDao = new EuroActNewDAO(getUserData());
	
					actDao.recognizeNumber(getAct());
	
					Integer vid = getAct().getVidAct();
					String rnFull = getAct().getRnFull();
					Integer rn = getAct().getRn();
					Integer godina = getAct().getGodina();
	
					Integer idAct = actDao.findIdActByVidRnAndGodina(vid, rn, godina);
					tmpAct = new EuroActNew();
	
					if (idAct != 0) {
	
						JPA.getUtil().runWithClose(() -> tmpAct = actDao.findById(idAct));
	
					} else {
	
						tmpAct.setVidAct(vid);
						tmpAct.setRnFull(rnFull);
						tmpAct.setRn(rn);
						tmpAct.setGodina(godina);
					}
					
					setAct(tmpAct);	
					
					if ((getAct().getVidAct() != null) && (getAct().getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
							|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
							|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
							|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
							|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA)) ) {
							
						showNoteSr = true;
					
					} else {
						showNoteSr = false;
					}
					
				}

			} catch (InvalidParameterException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());

			} catch (DbErrorException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			} catch (BaseException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG), e.getMessage());
			}
		}
		
	}

	public void actionChangeVidAct() {

		if (!SearchUtils.isEmpty(getAct().getRnFull())) {
			actionCheckForExistAct();
		}
		
		if ((getAct().getVidAct() != null) && (getAct().getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
				|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
				|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
				|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
				|| getAct().getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA)) ) {
				
			showNoteSr = true;
		
		} else {
			showNoteSr = false;
		}
	}
	
	/**
	 * Изтриване на акт
	 */
	public void actionDelete() {
		
		try {
			
			LOGGER.debug("actionDelete>>>> ");

			JPA.getUtil().runInTransaction(() -> new EuroActNewDAO(getUserData()).delete(getAct()));
			
			getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);

			ValueExpression expr2 = getValueExpression(CODEACT);
			ELContext ctx2 = getFacesContext().getELContext();
			if (expr2 != null) {
				expr2.setValue(ctx2, null);
			}

			// извиква remoteCommnad - ако има такава....
			String remoteCommnad = (String) getAttributes().get("onComplete");
			if (remoteCommnad != null && !"".equals(remoteCommnad)) {
				PrimeFaces.current().executeScript(remoteCommnad);
			}

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, "general.successDeleteMsg"));
		
		} catch (ObjectInUseException e) {
			LOGGER.error("Грешка при изтриване на акт! ObjectInUseException = {}", e.getMessage());
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, OBJECTINUSE), e.getMessage());
		
		} catch (BaseException e) {
			LOGGER.error("Грешка при изтриване на акт", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
		}

	}
	
	/**
	 * коригиране данни на акт - изп. се само, ако е в модален прозорец
	 * изивква се при затваряне на модалния прозореца (onhide)
	 * 
	 */
	public void actionHideModal() {
		
		setAct(null);
		setShowMe(false);
		LOGGER.debug("actionHideModal>>>> ");
	}

	public EuroActNew getAct() {
		return (EuroActNew) getStateHelper().eval(PropertyKeys.ACT, null);
	}

	public void setAct(EuroActNew act) {
		getStateHelper().put(PropertyKeys.ACT, act);
	}
	
	/** @return */
	public boolean isShowMe() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWME, false);
	}
	
	/** @param showMe */
	public void setShowMe(boolean showMe) {
		getStateHelper().put(PropertyKeys.SHOWME, showMe);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public boolean isShowNoteSr() {
		return showNoteSr;
	}

	public void setShowNoteSr(boolean showNoteSr) {
		this.showNoteSr = showNoteSr;
	}

	/** @return the dateClassif */
	private Date getDateClassif() {
		if (this.dateClassif == null) {
			this.dateClassif = (Date) getAttributes().get("dateClassif");
			if (this.dateClassif == null) {
				this.dateClassif = new Date();
			}
		}
		return this.dateClassif;
	}
	
	private SystemData getSystemData() {
		if (this.systemData == null) {
			this.systemData =  (SystemData) JSFUtils.getManagedBean("systemData");
		}
		return this.systemData;
	}
	
	/** @return the userData */
	private UserData getUserData() {
		if (this.userData == null) {
			this.userData = (UserData) JSFUtils.getManagedBean("userData");
		}
		return this.userData;
	}
	
	/** @return */
	public Integer getLang() {
		return getUserData().getCurrentLang();
	}
	
	/** @return */
	public Date getCurrentDate() {
		return getDateClassif();
	}
	
}