package com.ib.euroact.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.navigation.Navigation;
import com.ib.indexui.navigation.NavigationData;
import com.ib.indexui.navigation.NavigationDataHolder;
import com.ib.indexui.system.Constants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.SearchUtils;

@Named
@ViewScoped
public class EuroActEdit extends IndexUIbean implements Serializable {

	/**
	 *  Въвеждане / актуализация на акт на ЕС 
	 */
	private static final long serialVersionUID = -1852027387210019578L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroActEdit.class);
	
	private static final String FORM_EURO_ACT = "formEuroAct";
	
	private Date decodeDate;
	private SystemData sd;
	
	private transient EuroActNewDAO actDAO;
	private EuroActNew act;
	private boolean secDYesNo = false;	
	private boolean hideAccordion = false;
	private boolean viewBtnUrl = false;
	private boolean viewDataForNumAndYear = false;
	
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		try {
		
			this.decodeDate = new Date();
			this.sd = (SystemData) getSystemData();
			this.actDAO = new EuroActNewDAO(getUserData());
			this.act = new EuroActNew();

			if (JSFUtils.getRequestParameter("idObj") != null && !"".equals(JSFUtils.getRequestParameter("idObj"))) {

				Integer idObj = Integer.valueOf(JSFUtils.getRequestParameter("idObj"));

				if (idObj != null) {
					
					JPA.getUtil().runWithClose(() -> this.act = this.actDAO.findByIdFull(idObj, this.sd));//					
					
					if (this.act.getUrl() != null && !this.act.getUrl().isEmpty()) {
						this.viewBtnUrl = true;
					} else {
						this.viewBtnUrl = false;
					}					
	
					if (this.act.getSectionDYesNo() != null && this.act.getSectionDYesNo().equals(Constants.CODE_ZNACHENIE_DA)) {
						this.secDYesNo = true;						
					} else {
						this.secDYesNo = false;
					}
				
					if (this.act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
						|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
						|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
						|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
						|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA) ) {
						
						this.hideAccordion = true;
					
					} else {
						this.hideAccordion = false;
					}
					
				}
				else {
					
					this.act.setIzpalnenieActYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI);
					this.act.setDelegiraniActsYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI); 
				}
			}
		
		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	private boolean checkData() {
		
		boolean save = false;	
		
		if(SearchUtils.isEmpty(this.act.getRnFull()) && !this.viewDataForNumAndYear) {
			JSFUtils.addMessage(FORM_EURO_ACT + ":rnFull", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.nomer")));
			save = true;
		}
		
		if (this.viewDataForNumAndYear && this.act.getRn() == null) {
			JSFUtils.addMessage(FORM_EURO_ACT + ":rn", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.nomer")));
			save = true;
		}
		
		if (this.viewDataForNumAndYear && this.act.getGodina() == null) {
			JSFUtils.addMessage(FORM_EURO_ACT + ":godina", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.year")));
			save = true;
		}
		
		if(this.act.getVidAct() == null) {
			JSFUtils.addMessage(FORM_EURO_ACT + ":vidAct:аutoCompl", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.vid")));
			save = true;
		}
		
		return save;		
	}
	
	public void actionSave() {
		
		if(checkData()) {
			return;
		}
		
		try {
			
			if (this.act.getSectionAB4i().size() == 0 && this.act.getIzpalnenieActYesNo().equals(EuroConstants.CODE_OTG_IMA_PRIETI)) { 
				this.act.setIzpalnenieActYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI);
			}
			
			if (this.act.getSectionAB4d().size() == 0 && this.act.getDelegiraniActsYesNo().equals(EuroConstants.CODE_OTG_IMA_PRIETI)) {
				this.act.setDelegiraniActsYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI); 
			}
			
			if (this.act.getSectionG().size() == 0) { 
				this.act.setSectionGYesNo(Constants.CODE_ZNACHENIE_NE);	
			
			} else {
				this.act.setSectionGYesNo(Constants.CODE_ZNACHENIE_DA);
			}
			
			if (this.act.getSectionZ().size() == 0) {
				this.act.setSectionZYesNo(Constants.CODE_ZNACHENIE_NE);		
			
			} else {
				this.act.setSectionZYesNo(Constants.CODE_ZNACHENIE_DA);							
			}	
					
			JPA.getUtil().runInTransaction(() -> this.act = this.actDAO.save(this.act));
		
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));
			
			getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);
			
			Navigation navHolder = new Navigation();			
		    int i = navHolder.getNavPath().size();	
		   
		    NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils.getManagedBean("navigationSessionDataHolder");
		    Stack<NavigationData> stackPath = dataHoslder.getPageList();
		    NavigationData nd = stackPath.get(i-2);
		    Map<String, Object> mapV = nd.getViewMap();
		    
		    EuroActsList actsList = (EuroActsList) mapV.get("euroActsList");		   
		    actsList.actionSearch();
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при запис на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	public void actionDelete() {
		
		try {
			
			JPA.getUtil().runInTransaction(() -> this.actDAO.delete(this.act));

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));
			
			getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);
			
			this.act = new EuroActNew();
			
			Navigation navHolder = new Navigation();			
		    int i = navHolder.getNavPath().size();	
		   
		    NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils.getManagedBean("navigationSessionDataHolder");
		    Stack<NavigationData> stackPath = dataHoslder.getPageList();
		    NavigationData nd = stackPath.get(i-2);
		    Map<String, Object> mapV = nd.getViewMap();
		    
		    EuroActsList actsList = (EuroActsList) mapV.get("euroActsList");		   
		    actsList.actionSearch();
		    
		    navHolder.goBack();		    
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при изтриване на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}		
		
	}	
	
	public void actionCheckForExistAct() {
		
		if (this.act.getVidAct() != null) {
		
			try {
			
				if (!this.act.getRnFull().isEmpty()) { 
					
					this.actDAO.recognizeNumber(this.act);	
					
					Integer vid = this.act.getVidAct();
					String rnFull = this.act.getRnFull();
					Integer rn = this.act.getRn();
					Integer godina = this.act.getGodina();						
						
					JPA.getUtil().runWithClose(() -> { 
						
						Integer idAct = this.actDAO.findIdActByVidRnAndGodina(vid, rn, godina);
						// this.act = new EuroActNew(); // Това се маха, тъй като дублира при редакция на съществуваши актове
						
						
						if (idAct != null && idAct != 0) {
							if (this.act.getId() == null || !this.act.getId().equals(idAct)) {
								this.act = this.actDAO.findById(idAct);
							}else {
								this.act.setVidAct(vid);
								this.act.setRnFull(rnFull);
								this.act.setRn(rn);
								this.act.setGodina(godina);		
							}
						
						} else {							
							this.act.setVidAct(vid);
							this.act.setRnFull(rnFull);
							this.act.setRn(rn);
							this.act.setGodina(godina);						
						}
					});	
					
					if (this.act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA) ) {
							
						this.hideAccordion = true;
					
					} else {
						this.hideAccordion = false;
					}					
				}
			
			} catch (InvalidParameterException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
				this.viewDataForNumAndYear = true;
				String focusJs = "document.getElementById('formEuroAct:rn').focus()";
				PrimeFaces.current().executeScript(focusJs);	
				
			} catch (DbErrorException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			
			} catch (BaseException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			} 
		}
	}
	
	public void actionCheckForExistActByRnAndYear() {
		
		if (this.act.getVidAct() != null) {
		
			try {
			
				if (this.act.getRn() != null && this.act.getGodina() != null) { 					
					
					Integer vid = this.act.getVidAct();
					String rnFull = this.act.getRnFull();
					Integer rn = this.act.getRn();
					Integer godina = this.act.getGodina();						
						
					JPA.getUtil().runWithClose(() -> { 
						
						Integer idAct = this.actDAO.findIdActByVidRnAndGodina(vid, rn, godina);
						
						if (idAct != 0) {
						
							this.act = this.actDAO.findById(idAct);
						
						} else {
							
							this.act.setVidAct(vid);
							this.act.setRnFull(rnFull);
							this.act.setRn(rn);
							this.act.setGodina(godina); 
						}
					});	
					
					if (this.act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA) ) {
							
						this.hideAccordion = true;
					
					} else {
						this.hideAccordion = false;
					}					
				}
			
			} catch (BaseException e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			} 
		}
	}
	
	public void actionChangeVidAct() {
		
		if(!SearchUtils.isEmpty(this.act.getRnFull())) {
			actionCheckForExistAct();
		}
		
		if ((this.act.getVidAct() != null) && (this.act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
				|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
				|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
				|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
				|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA)) ) {
				
			this.hideAccordion = true;
		
		} else {
			this.hideAccordion = false;
		}
	}
	
	public void actionSaveConn() {
		
		try {
			
			JPA.getUtil().runInTransaction(() ->  this.act = this.actDAO.save(this.act));	

//			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));			
//			PrimeFaces.current().executeScript("scrollToErrors()");
			
			JPA.getUtil().runWithClose(() -> this.act = this.actDAO.findByIdFull(this.act.getId(), this.sd));
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при запис на връзка към акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}		
		
	}
	
	public void actionDeleteConn() {
		
		try {
			
			this.act.setSectionDText(null);	
			this.secDYesNo = false;
			this.act.setSectionDYesNo(Constants.CODE_ZNACHENIE_NE);	
				
			JPA.getUtil().runInTransaction(() ->  this.act = this.actDAO.save(this.act));
			
//			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));
//			PrimeFaces.current().executeScript("scrollToErrors()");		
			
			JPA.getUtil().runWithClose(() -> this.act = this.actDAO.findByIdFull(this.act.getId(), this.sd));

		} catch (BaseException e) {			
			LOGGER.error("Грешка при изтриване на връзка към акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}			
	}
	
	public void actionClickDYesNo() {		

		if (this.secDYesNo) {
			this.act.setSectionDYesNo(Constants.CODE_ZNACHENIE_DA);
		} else {
			this.act.setSectionDYesNo(Constants.CODE_ZNACHENIE_NE);					
		}				
	}
	
	public void actionChangeUrl() {
		if (this.act.getUrl() != null && !this.act.getUrl().isEmpty()) {
			this.viewBtnUrl = true;
		} else {
			this.viewBtnUrl = false;
		}
	}

	public Date getDecodeDate() {
		return new Date(decodeDate.getTime()) ;
	}

	public void setDecodeDate(Date decodeDate) {
		this.decodeDate = decodeDate != null ? new Date(decodeDate.getTime()) : null;
	}
	
	public SystemData getSd() {
		return sd;
	}

	public void setSd(SystemData sd) {
		this.sd = sd;
	}	

	public EuroActNew getAct() {
		return act;
	}

	public void setAct(EuroActNew act) {
		this.act = act;
	}

	public boolean isSecDYesNo() {
		return secDYesNo;
	}

	public void setSecDYesNo(boolean secDYesNo) {
		this.secDYesNo = secDYesNo;
	}

	public boolean isHideAccordion() {
		return hideAccordion;
	}

	public void setHideAccordion(boolean hideAccordion) {
		this.hideAccordion = hideAccordion;
	}

	public boolean isViewBtnUrl() {
		return viewBtnUrl;
	}

	public void setViewBtnUrl(boolean viewBtnUrl) {
		this.viewBtnUrl = viewBtnUrl;
	}

	public boolean isViewDataForNumAndYear() {
		return viewDataForNumAndYear;
	}

	public void setViewDataForNumAndYear(boolean viewDataForNumAndYear) {
		this.viewDataForNumAndYear = viewDataForNumAndYear;
	}
	
}
