package com.ib.euroact.components;

import java.util.ArrayList;
import java.util.List;

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
import com.ib.euroact.db.dao.EuroActNewSectionZDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.indexui.system.Constants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.utils.SearchUtils;

/** */
@FacesComponent(value = "compActSecZ", createTag = true)
public class CompActSecZ extends UINamingContainer {

	private enum PropertyKeys {
		ACT, SECLIST, SECZNEW, SHOWME, SHOWPANELDATAACT
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CompActSecZ.class);

	public static final String UIBEANMESSAGES = "ui_beanMessages";
	public static final String BEANMESSAGES = "beanMessages";
	public static final String MSGPLSINS = "general.pleaseInsert";
	public static final String ERRDATABASEMSG = "general.errDataBaseMsg";
	public static final String SUCCESSAVEMSG = "general.succesSaveMsg";
	public static final String OBJECTINUSE = "general.objectInUse";
	public static final String LABELS = "labels";

	private String errMsg = null;
	private SystemData systemData = null; // може би ще ми трябва, за да извиквам метод за презареждане на класификацията след промяна или изтриване
	private UserData userData = null;

	private EuroActNew tmpAct;
	private EuroActNewSectionZ tmpSecZNew;

	
	
	/**
	 * Данни
	 * 
	 * @return
	 */
	public void init() {

		tmpAct = (EuroActNew) getAttributes().get("act");

		setAct(tmpAct);

		setShowPanelDataActs(false); // po podrazbirane e skrit panelas dannite
		
		if(tmpAct.getSectionZ()==null) {
			setSecZList(new ArrayList<EuroActNewSectionZ>());
		} else {
			setSecZList(tmpAct.getSectionZ()); 
		}
		
		if (tmpAct.getSectionZYesNo().intValue() == EuroConstants.CODE_ZNACHENIE_DA) {
			setShowPanelDataActs(true);
		}

		setShowMe(true);
		setErrMsg(null);

		setSecZNew(new EuroActNewSectionZ());

		LOGGER.debug("initSecZ");
	}

	public void actionNewSecZ() {

		setSecZNew(new EuroActNewSectionZ());
	}

	public void actionSaveSecZ() {
		
		boolean flagSave = true;
		
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = null;
		tmpAct = getAct();
		tmpSecZNew = getSecZNew();
		
		if (context != null && tmpSecZNew != null) {
			clientId = this.getClientId(context);
			
			if (SearchUtils.isEmpty(tmpSecZNew.getNameBgAct())) {
				JSFUtils.addMessage(clientId + ":nameSecZ", FacesMessage.SEVERITY_ERROR, 
						IndexUIbean.getMessageResourceString(BEANMESSAGES, "compActSecZ.nameBgAct"));
				flagSave = false;
			}
			
			if (SearchUtils.isEmpty(tmpSecZNew.getDvBroi())) {
				JSFUtils.addMessage(clientId + ":dvNum", FacesMessage.SEVERITY_ERROR, 
						IndexUIbean.getMessageResourceString(BEANMESSAGES, "compActSecZ.dvBroi"));
				flagSave = false;				
			}
			
			if (tmpSecZNew.getDvGodina() == null) {
				JSFUtils.addMessage(clientId + ":dvYear", FacesMessage.SEVERITY_ERROR, 
						IndexUIbean.getMessageResourceString(BEANMESSAGES, "compActSecZ.dvGodina"));
				flagSave = false;				
			}
			
			if (!flagSave) {
				errMsg = "Моля, въведете задължителната информация! ";
				PrimeFaces.current().executeScript("scrollToErrors()");
			}
			
		} else {
			flagSave = false;
		}
		
		if (flagSave) {
			
			EuroActNewDAO actDAO = new EuroActNewDAO(getUserData());
			EuroActNewSectionZDAO secZDAO = new EuroActNewSectionZDAO(getUserData());

			try {
				
				tmpSecZNew.setChanged(true);
				tmpSecZNew.setEuroActNewId(tmpAct.getId());

				JPA.getUtil().runInTransaction(() -> {

					secZDAO.save(tmpSecZNew);
					actDAO.save(tmpAct);
					tmpAct = actDAO.findByIdFull(tmpAct.getId(), getSystemData()); //towa mi se iskashe da go izbergna i prezarejdaneto

				});
				
				setAct(tmpAct);
				
				setSecZNew(new EuroActNewSectionZ());
				
				setSecZList(tmpAct.getSectionZ()); 
				
				ValueExpression expr2 = getValueExpression("act");
				ELContext ctx2 = getFacesContext().getELContext();
				if (expr2 != null) {
					expr2.setValue(ctx2, tmpAct);
				}

			} catch (BaseException e) {
				LOGGER.error("Грешка при запис на секция З към акта на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
			}
			
		}
	}

	public void actionEditSecZ(EuroActNewSectionZ conn) {
		setSecZNew(conn);
	}

	public void actionDeleteSecZ(EuroActNewSectionZ conn) {

		EuroActNewDAO actDAO = new EuroActNewDAO(getUserData());
		EuroActNewSectionZDAO secZDAO = new EuroActNewSectionZDAO(getUserData());

		tmpAct = getAct();

		List<EuroActNewSectionZ> tmpList = getSecZList();
		tmpList.remove(conn);

		setSecZList(tmpList);

		tmpAct.getSectionZ().remove(conn);

		if (tmpAct.getSectionZ().size() == 0) {
			tmpAct.setSectionZYesNo(Constants.CODE_ZNACHENIE_NE);
		}

		try {
			
			JPA.getUtil().begin();

			secZDAO.delete(conn);

			actDAO.save(tmpAct);

			JPA.getUtil().commit();

			setAct(tmpAct);
			setSecZNew(new EuroActNewSectionZ());

			ValueExpression expr2 = getValueExpression("act");
			ELContext ctx2 = getFacesContext().getELContext();
			if (expr2 != null) {
				expr2.setValue(ctx2, tmpAct);
			}
		
		} catch (Exception e) {
			LOGGER.error("Грешка при изтриване на секция З към акта на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));

		} finally {
			JPA.getUtil().closeConnection();
		}
	}

	public void actionShowPanel() {

		tmpAct = getAct();

		setShowPanelDataActs(false);

		if (tmpAct.getSectionZYesNo().intValue() == EuroConstants.CODE_ZNACHENIE_DA) {
			setShowPanelDataActs(true);
		}

	}

	public EuroActNew getAct() {
		return (EuroActNew) getStateHelper().eval(PropertyKeys.ACT, null);
	}

	public void setAct(EuroActNew eau) {
		getStateHelper().put(PropertyKeys.ACT, eau);
	}

	public EuroActNewSectionZ getSecZNew() {
		return (EuroActNewSectionZ) getStateHelper().eval(PropertyKeys.SECZNEW, null);
	}

	public void setSecZNew(EuroActNewSectionZ eau) {
		getStateHelper().put(PropertyKeys.SECZNEW, eau);
	}

	/** @return */
	public boolean isShowMe() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWME, false);
	}

	/** @param showMe */
	public void setShowMe(boolean showMe) {
		getStateHelper().put(PropertyKeys.SHOWME, showMe);
	}

	/** @return */
	public boolean isShowPanelDataActs() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWPANELDATAACT, false);
	}

	/** @param showMe */
	public void setShowPanelDataActs(boolean showPanelDataActs) {
		getStateHelper().put(PropertyKeys.SHOWPANELDATAACT, showPanelDataActs);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private SystemData getSystemData() {
		if (this.systemData == null) {
			this.systemData = (SystemData) JSFUtils.getManagedBean("systemData");
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

	@SuppressWarnings("unchecked")
	public List<EuroActNewSectionZ> getSecZList() {
		return (List<EuroActNewSectionZ>) getStateHelper().eval(PropertyKeys.SECLIST, null);

	}

	public void setSecZList(List<EuroActNewSectionZ> secZList) {
		getStateHelper().put(PropertyKeys.SECLIST, secZList);

	}

}