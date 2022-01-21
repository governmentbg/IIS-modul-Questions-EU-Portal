package com.ib.euroact.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.DossierDAO;
import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.Dossier;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.search.EuroHarmSearch;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.navigation.Navigation;
import com.ib.indexui.navigation.NavigationData;
import com.ib.indexui.navigation.NavigationDataHolder;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.Constants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.SearchUtils;

@Named
@ViewScoped
public class DossierBean extends IndexUIbean {

	/**
	 * Списък с актове на ЕС по зададени критерии
	 * 
	 */
	private static final long serialVersionUID = -8021542004444743608L;
	static final Logger LOGGER = LoggerFactory.getLogger(DossierBean.class);
	
	
	private SystemData sd;
	
	private Dossier dossier;
	private EuroActNew act;
	private Date decodeDate;
	
	
	private Boolean onsiteBollean;
	
	private SystemClassif zakonNameSc;
//	private SystemClassif zakonNameVSc;
	
	private transient EuroActNewDAO actDAO;
	private transient DossierDAO  dossierDAO;
	
	private ArrayList<Integer> vidDirektivi = new ArrayList<Integer>();
	
	private boolean direktiva = false;
	
	private boolean oldLawHarm = false;
	
	private List<SelectItem> vidAct;
	
	
	private Integer periodSel;
	private EuroHarmSearch euroHarmSearch;
	private LazyDataModelSQL2Array euroHarmList;
	
	private boolean hasAccessPublishOnSite;
	
	private boolean showNumYearAct = false;
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		sd = (SystemData) getSystemData();
		
		actDAO = new EuroActNewDAO(getUserData());
		dossierDAO = new DossierDAO(getUserData());
		
		hasAccessPublishOnSite = getUserData().hasAccess(EuroConstants.CODE_SYSCLASS_MENU, 30445);
		
		zakonNameSc = new SystemClassif();
//		zakonNameVSc  = new SystemClassif();
		
		//------------ vidove aktowe ------------
		try {
		SelectItemGroup g1 = new SelectItemGroup(sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_DIRECTIVI, getCurrentLang(), new Date()));
		
        g1.setSelectItems(new SelectItem[] {
        		new SelectItem(EuroConstants.TIP_ACT_DIREKTIVA, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_DIREKTIVA, getCurrentLang(), new Date())), 
        		new SelectItem(EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE, getCurrentLang(), new Date())), 
        		new SelectItem(EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA, getCurrentLang(), new Date()))
        });
         
        SelectItemGroup g2 = new SelectItemGroup(sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_REGLAMENTI, getCurrentLang(), new Date()));
        g2.setSelectItems(new SelectItem[] {
        		new SelectItem(EuroConstants.TIP_ACT_REGLAMENT, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_REGLAMENT, getCurrentLang(), new Date())), 
        		new SelectItem(EuroConstants.TIP_ACT_DELEGIRAN_REGLAMENT, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_DELEGIRAN_REGLAMENT, getCurrentLang(), new Date())), 
        		new SelectItem(EuroConstants.TIP_ACT_REGLAMENT_ZA_IZPALNENIE, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_REGLAMENT_ZA_IZPALNENIE, getCurrentLang(), new Date()))
        });
        
        SelectItemGroup g3 = new SelectItemGroup(sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_RESHENIA, getCurrentLang(), new Date()));
        g3.setSelectItems(new SelectItem[] {
        		new SelectItem(EuroConstants.TIP_ACT_RESHENIE, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_RESHENIE, getCurrentLang(), new Date())), 
        		new SelectItem(EuroConstants.TIP_ACT_RAMKOVO_RESHENIE, sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT,EuroConstants.TIP_ACT_RAMKOVO_RESHENIE, getCurrentLang(), new Date()))
        });
         
        vidAct = new ArrayList<SelectItem>();
        vidAct.add(g1);
        vidAct.add(g2);
        vidAct.add(g3);
        
		} catch (DbErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//---------------------------------------
		
		
		//---------------------------------------
		vidDirektivi.add(EuroConstants.TIP_ACT_DIREKTIVA); 
		vidDirektivi.add(EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE);
		vidDirektivi.add(EuroConstants.TIP_ACT_POPRAVKA_NA_DIREKTIVA);
		vidDirektivi.add(EuroConstants.TIP_ACT_RAMKOVO_RESHENIE);
		vidDirektivi.add(EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA);
		//---------------------------------------
		
		if (JSFUtils.getRequestParameter("idObj") != null) {
			
			try {
			
				Integer idObj = Integer.valueOf(JSFUtils.getRequestParameter("idObj"));
		
		
				try {
					JPA.getUtil().runWithClose(() -> {
						
						dossier = dossierDAO.findById(idObj);
						dossierDAO.fillDossierConnections(dossier);
						act =  actDAO.findByIdFull(dossier.getEuroActNewId(), sd);
					});
					
					
					decodeDate = new Date(dossier.getDateReg().getTime());
					
					onsiteBollean = (dossier.getOnSiteYesNo() !=null && dossier.getOnSiteYesNo().intValue() == Constants.CODE_ZNACHENIE_DA ? Boolean.TRUE:Boolean.FALSE);
					
					if(dossier.getZakonName()!=null) {
						zakonNameSc.setTekst(dossier.getZakonName());
						
					}
					
//					if(dossier.getZakonNameV()!=null) {
//						zakonNameVSc.setTekst(dossier.getZakonNameV());
//					}
					
					if(dossier.getZakonIdV()==null) {
						oldLawHarm = true;
					} else {
						oldLawHarm = false;
					}
					
					direktiva =  vidDirektivi.contains(act.getVidAct());
					
				} catch (BaseException e) {
					LOGGER.error("Грешка при зареждане данните на досие и акт на ЕС!", e);
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
				}
		
			} catch (NumberFormatException e) {
				LOGGER.error("dossierBean праща се  стринг в idObj!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} 
		
		} else {
			actionNew();
		}
		
		euroHarmSearch = new EuroHarmSearch();
		euroHarmSearch.setPrietiOnly(true);
		
		showNumYearAct = false;
	}
	
	public void actionNew() {
		
		decodeDate = new Date();
		
		onsiteBollean = Boolean.FALSE;
		
		zakonNameSc.setTekst("");
	//	zakonNameVSc.setTekst("");
		
		direktiva = false;
		
		dossier = new Dossier();
		act = new EuroActNew();
		
		dossier.setSectionVYesNo(Constants.CODE_ZNACHENIE_NE);
		
		oldLawHarm = false;
		
	}
	
	/**
	 * autocomplete - търсене на значение
	 *
	 * @param query
	 * @return
	 */
	public List<SystemClassif> actionComplete(String query) {
		List<SystemClassif> result = null;
		
		if (query != null && !query.trim().isEmpty()) {
			try {
				
				result =  getSystemData().queryClassification(Integer.valueOf(EuroConstants.CODE_SYSCLASS_ZAKONI), query, new Date(), Integer.valueOf(getCurrentLang()), new ArrayList<Integer>());

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return result != null ? result : new ArrayList<>();
	}
	
	public void onItemSelectLaw(SelectEvent<SystemClassif> event) {
		SystemClassif item = event.getObject();
		if(item!=null) {
			
			//System.out.println(item);
			dossier.setZakonName(item.getTekst());
			try {
				Object[] spec = (Object[]) sd.getItemSpecifics(EuroConstants.CODE_SYSCLASS_ZAKONI, item.getCode(), EuroConstants.CODE_DEFAULT_LANG, new Date());
				if(spec!=null) {
				//	System.out.println(spec[0].toString());
					dossier.setZakonName(spec[0].toString());
					zakonNameSc.setTekst(spec[0].toString());
				}
			} catch (DbErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public void onItemUnselectLow(UnselectEvent<SystemClassif> event)  {
		SystemClassif item =  event.getObject();
		//System.out.println(item);
		dossier.setZakonName(null);
	}
	
	
//	public void onItemSelectLawHarm(SelectEvent<SystemClassif> event) {
//		SystemClassif item = event.getObject();
//		if(item!=null) {
//			
//			dossier.setZakonNameV(item.getTekst());
//			try {
//				Object[] spec = (Object[]) sd.getItemSpecifics(EuroConstants.CODE_SYSCLASS_ZAKONI, item.getCode(), EuroConstants.CODE_DEFAULT_LANG, new Date());
//				if(spec!=null) {
//				//	System.out.println(spec[0].toString());
//					dossier.setZakonNameV(spec[0].toString());
//					zakonNameVSc.setTekst(spec[0].toString());
//					dossier.setZakonDvBrV(SearchUtils.asInteger(spec[1]));
//					dossier.setZakonDvGodV(SearchUtils.asInteger(spec[2]));
//				}
//			} catch (DbErrorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		}
//		
//	}

	public void onItemUnselectLowHarm(UnselectEvent<SystemClassif> event)  {
//		SystemClassif item =  event.getObject();
//		System.out.println(item);
		dossier.setZakonNameV(null);
		dossier.setZakonDvBrV(null);
		dossier.setZakonDvGodV(null);
	}
	
	
	
	private boolean checkData() {
		
		boolean save = true;	
		
		
		if(SearchUtils.isEmpty(dossier.getName())) {
			JSFUtils.addMessage("formDossier:dossierName", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "dossier.name")));
			save = false;
		}
		
		if(SearchUtils.isEmpty(zakonNameSc.getTekst())) {
			JSFUtils.addMessage("formDossier:lawSearch", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "general.law")));
			save = false;
		}
		
//		if(SearchUtils.isEmpty(zakonNameVSc.getTekst())) {
//			JSFUtils.addMessage("formDossier:lawHarmSearch", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "general.lawHarm")));
//			save = false;
//		}
		
		if(SearchUtils.isEmpty(dossier.getZakonNameV())) {
			JSFUtils.addMessage("formDossier:lawHarmSearch", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "general.lawHarm")));
			save = false;
		}
		
		
		if(act.getId()!=null && SearchUtils.isEmpty(act.getIme())) {
			JSFUtils.addMessage("formDossier:tabSections:actName", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.naim")));
			save = false;
		}
		
		
		if(SearchUtils.isEmpty(act.getRnFull())) {
			JSFUtils.addMessage("formDossier:nomerAct", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.nomer")));
			save = false;
		}
		
		if(act.getVidAct() == null) {
			//JSFUtils.addMessage("formDossier:vidAct:аutoCompl", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.vid")));
			JSFUtils.addMessage("formDossier:vidAct", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.vid")));
			save = false;
		}
		
		if(showNumYearAct) {
			if(act.getRn() == null) {
				JSFUtils.addMessage("formDossier:nomAct", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.nomer")));
				save = false;
			}
			if(act.getGodina() == null) {
				JSFUtils.addMessage("formDossier:godAct", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "euroActsList.year")));
				save = false;
			}
			
		}
		
		
		if(dossier.getZakonDvBrV() == null) {
			JSFUtils.addMessage("formDossier:dvBrV", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "general.broj")));
			save = false;
		}
		
		if(dossier.getZakonDvGodV() == null) {
			JSFUtils.addMessage("formDossier:dvGodV", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "general.godina")));
			save = false;
		}
		return save;		
	}
	
	// izpolzwa se s butona za publikuwane na sajta (butona e заменен с чекбокс за да е как то в актовете и не им харесваше сивото)
	public void publishOnSite() {
		
		if(onsiteBollean) {
			dossier.setOnSiteYesNo(Constants.CODE_ZNACHENIE_DA) ;
		} else {
			dossier.setOnSiteYesNo(Constants.CODE_ZNACHENIE_NE) ;
		}
		
		actionSave();
	}
	
	
	public void actionSave() {
		
		if(onsiteBollean) {
			dossier.setOnSiteYesNo(Constants.CODE_ZNACHENIE_DA) ;
		} else {
			dossier.setOnSiteYesNo(Constants.CODE_ZNACHENIE_NE) ;
		}
		
		//System.out.println("zakonNameSc.getTekst-> "+zakonNameSc.getTekst());
		//System.out.println("zakonNameSc.getTekst-> "+zakonNameVSc.getTekst());
		
		//tova pri nov bez da e izbrano ot auto compliita
		if(zakonNameSc.getCode()==0) {
			dossier.setZakonName(zakonNameSc.getTekst());
		}
		
//		if(zakonNameVSc.getCode()==0) {
//			dossier.setZakonNameV(zakonNameVSc.getTekst());
//		}
		
		
		
		if(checkData()) {
			
			try {
				
				JPA.getUtil().runInTransaction(() -> {
					
					boolean newAct = false;
					
					if(act.getId()==null) {
						newAct = true;
					}
					
					act = actDAO.save(act);
					
					if(newAct) {
						dossier.setEuroActNewId(act.getId());
					}
					
					
					if(dossier.getLinksReg()!=null && dossier.getLinksReg().size()>0) {
						dossier.setReqMeasuredYesNo(Constants.CODE_ZNACHENIE_DA);
					} else {
						dossier.setReqMeasuredYesNo(Constants.CODE_ZNACHENIE_NE);
					}
					
					dossier = dossierDAO.save(dossier);
					
					
				});
				
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));
				
				getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);
				getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_ZAKONI, false, true);
				
				Navigation navHolder = new Navigation();			
			    int i = navHolder.getNavPath().size();	
			   
			    NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils.getManagedBean("navigationSessionDataHolder");
			    Stack<NavigationData> stackPath = dataHoslder.getPageList();
			    NavigationData nd = stackPath.get(i-2);
			    Map<String, Object> mapV = nd.getViewMap();
			    
			    DossierList dossierList = (DossierList) mapV.get("dossierList");		   
			    dossierList.actionSearch();
				
			} catch (BaseException e) {			
				LOGGER.error("Грешка при запис на досие или акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			}
		}
		
	}
	
	public void actionDelete() {
		//TODO
		
		try {
			
			JPA.getUtil().runInTransaction(() ->  
				dossierDAO.deleteById(dossier.getId())
			); 
		
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO,  IndexUIbean.getMessageResourceString(UI_beanMessages, "general.successDeleteMsg") );
			actionNew();
			
			Navigation navHolder = new Navigation();			
		    int i = navHolder.getNavPath().size();	
		   
		    NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils.getManagedBean("navigationSessionDataHolder");
		    Stack<NavigationData> stackPath = dataHoslder.getPageList();
		    NavigationData nd = stackPath.get(i-2);
		    Map<String, Object> mapV = nd.getViewMap();
		    
		    DossierList dossierList = (DossierList) mapV.get("dossierList");		   
		    dossierList.actionSearch();
			
		} catch (BaseException e) {			
			LOGGER.error("Грешка при изтриване на досие!", e);			
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR,getMessageResourceString(UI_beanMessages, ERRDATABASEMSG), e.getMessage());
		} 
	}
	 
	public void actionRemoveAct() {
		//TODO da zaìstwa akta kojto e bil izbran
		
		act= new EuroActNew();
		dossier.setEuroActNewId(null);
		
		dossier.setNameAct(null);
		
		showNumYearAct = false;
	}
	
	
	public void actionCheckForExistAct() {
		
		if (act.getVidAct() != null && !act.getRnFull().isEmpty()) {
		
			try {
				showNumYearAct = false;
				
				actDAO.recognizeNumber(act);
				
				
				actionCheckForExistAct(act.getRn()  ,act.getGodina());
				
			} catch (InvalidParameterException e) {
				//LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
				//act.setRnFull("");
				dossier.setEuroActNewId(null);
				dossier.setNameAct(null);
				
				showNumYearAct = true;
				
				PrimeFaces.current().executeScript("document.getElementById('formDossier:nomAct').focus()");	
				
			} catch (Exception e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			}  finally {
				JPA.getUtil().closeConnection();
			}
		
		} else {
			dossier.setEuroActNewId(null);
			dossier.setNameAct(null);
		}
		
	}
	
	public void actionCheckForExistActByNumYear() {
		if (act.getVidAct() != null && !act.getRnFull().isEmpty() && act.getRn() != null && act.getGodina() != null) {
			try {
				actionCheckForExistAct(act.getRn()  ,act.getGodina());
			} catch (Exception e) {
				LOGGER.error("Грешка при зареждане данните на акт на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
				
				act.setRnFull("");
				dossier.setEuroActNewId(null);
				dossier.setNameAct(null);
				
			}  finally {
				JPA.getUtil().closeConnection();
			}
		} else {
			dossier.setEuroActNewId(null);
			dossier.setNameAct(null);
		}
	}
	
	private void actionCheckForExistAct(Integer rn  ,Integer godina) throws DbErrorException {
		
		Integer vid = act.getVidAct();
		
		Integer idAct = actDAO.findIdActByVidRnAndGodina(vid, rn, godina);
		
		if (idAct != 0) {	
			
			//act = actDAO.findById(idAct);
			act =  actDAO.findByIdFull(idAct, sd);
				
			//ako намери акт го прекачаме
			dossier.setEuroActNewId(act.getId());
			dossier.setNameAct(act.getIme());
		} 
			
//					if (this.act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
//							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
//							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
//							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
//							|| this.act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA) ) {
//							
//						hideAccordion = true;
//					
//					} else {
//						hideAccordion = false;
//					}
				
	}
	
	public void actionChangeVidAct() {
		
		if(!SearchUtils.isEmpty(act.getRnFull())) {
			actionCheckForExistAct();
		}
		
		direktiva =  vidDirektivi.contains(act.getVidAct());
		
//		if ((act.getVidAct() != null) && (act.getVidAct().equals(EuroConstants.TIP_ACT_SADEVNI_AKTOVE) 
//				|| act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_DELO)
//				|| act.getVidAct().equals(EuroConstants.TIP_ACT_RESHENIE_PO_SAEDINENI_DELA)
//				|| act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_DELO)
//				|| act.getVidAct().equals(EuroConstants.TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA)) ) {
//		} 
		
	}
	
	public Dossier getDossier() {
		return dossier;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	public EuroActNew getAct() {
		return act;
	}

	public void setAct(EuroActNew act) {
		this.act = act;
	}

	public Date getDecodeDate() {
		return decodeDate;
	}

	public void setDecodeDate(Date decodeDate) {
		this.decodeDate = decodeDate;
	}

	public Boolean getOnsiteBollean() {
		return onsiteBollean;
	}

	public void setOnsiteBollean(Boolean onsiteBollean) {
		this.onsiteBollean = onsiteBollean;
	}

	public SystemClassif getZakonNameSc() {
		return zakonNameSc;
	}

	public void setZakonNameSc(SystemClassif zakonNameSc) {
		this.zakonNameSc = zakonNameSc;
	}

//	public SystemClassif getZakonNameVSc() {
//		return zakonNameVSc;
//	}
//
//	public void setZakonNameVSc(SystemClassif zakonNameVSc) {
//		this.zakonNameVSc = zakonNameVSc;
//	}

	public ArrayList<Integer> getVidDirektivi() {
		return vidDirektivi;
	}

	public void setVidDirektivi(ArrayList<Integer> vidDirektivi) {
		this.vidDirektivi = vidDirektivi;
	}

	public boolean isDirektiva() {
		return direktiva;
	}

	public void setDirektiva(boolean direktiva) {
		this.direktiva = direktiva;
	}

	public List<SelectItem> getVidAct() {
		return vidAct;
	}

	public void setVidAct(List<SelectItem> vidAct) {
		this.vidAct = vidAct;
	}

	public boolean isOldLawHarm() {
		return oldLawHarm;
	}

	public void setOldLawHarm(boolean oldLawHarm) {
		this.oldLawHarm = oldLawHarm;
	}

	public EuroHarmSearch getEuroHarmSearch() {
		return euroHarmSearch;
	}

	public void setEuroHarmSearch(EuroHarmSearch euroHarmSearch) {
		this.euroHarmSearch = euroHarmSearch;
	}

	public Integer getPeriodSel() {
		return periodSel;
	}

	public void setPeriodSel(Integer periodSel) {
		this.periodSel = periodSel;
	}

	public LazyDataModelSQL2Array getEuroHarmList() {
		return euroHarmList;
	}

	public void setEuroHarmList(LazyDataModelSQL2Array euroHarmList) {
		this.euroHarmList = euroHarmList;
	}
	
	public void changePeriod() {

		if (periodSel != null) {
			Date[] di = DateUtils.calculatePeriod(periodSel);
			euroHarmSearch.setDatZakonOt(di[0]);
			euroHarmSearch.setDatZakonDo(di[1]);
		} else {
			euroHarmSearch.setDatZakonOt(null);
			euroHarmSearch.setDatZakonDo(null);
		}
	}

	public void changeDate() {
		periodSel = null;
	}
	
	public void actionSearchLawHarm() {

		try {

			euroHarmSearch.buildQuery(sd);
			euroHarmList = new LazyDataModelSQL2Array(this.euroHarmSearch, null);

		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане на хармонизация!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	public void actionClearLawHarm() {

		euroHarmSearch = new EuroHarmSearch();
		euroHarmSearch.setPrietiOnly(true);
		euroHarmList = null;
		periodSel = null;

	}
	
	public int getPeriodNoFuture() {
		return EuroConstants.CODE_CLASSIF_PERIOD_NOFUTURE;
	}
	
	public void actionSelectLawHarm(Object []item) {
		
		if(item!=null) {
			clearHarmLaw();
			
			dossier.setZakonNameV( (String)item[4]);
			dossier.setZakonIdV(SearchUtils.asInteger(item[0]));
						
			String brojDv = (String)item[6];
			
			if(brojDv!=null) {
				
				ArrayList<String> rez = getNumbersFromString(brojDv);
				
				if(rez!=null && rez.size() == 2) {
					dossier.setZakonDvBrV( Integer.valueOf(rez.get(0)));
					dossier.setZakonDvGodV(Integer.valueOf(rez.get(1)));
					
				}
			}
		}
		
	}
	
	public void clearHarmLaw() {
		dossier.setZakonNameV(null);
		dossier.setZakonIdV(null);
		dossier.setZakonDvBrV(null);
		dossier.setZakonDvGodV(null);
	}
	
	//Изкопиран от WordHarmParser защото не го достъпвам
	private  ArrayList<String> getNumbersFromString(String tekst){
		String numbers = "0123456789";
		boolean hasQuote = false;
		
		ArrayList<String> foundStrings = new ArrayList<String>();
		
		if (tekst == null) {
			return foundStrings;
		}else {
			//Това е когато завършва на цифра да не се налага след цикъла да правя анализ ;)
			tekst += ";"; 
		}
		
		
		String num = "";
		for(int i = 0; i < tekst.length(); i++) {
			
			if ("\"".equals(""+tekst.charAt(i))) {
				hasQuote = !hasQuote;
				num = "";
				continue;
				
			}
			
			if (!hasQuote) {
				String tek = ""+tekst.charAt(i);
			    if (numbers.contains(tek)) {
			    	num += tek; 
			    }else {
			    	if (num.length() > 0) {
			    		//System.out.println("Adding " + num);
			    		foundStrings.add(num);
			    		num = "";				    		
			    	}
			    }
			}
		}
		
		return foundStrings;
		
		
	}

	public boolean isHasAccessPublishOnSite() {
		return hasAccessPublishOnSite;
	}

	public void setHasAccessPublishOnSite(boolean hasAccessPublishOnSite) {
		this.hasAccessPublishOnSite = hasAccessPublishOnSite;
	}

	public boolean isShowNumYearAct() {
		return showNumYearAct;
	}

	public void setShowNumYearAct(boolean showNumYearAct) {
		this.showNumYearAct = showNumYearAct;
	}
	
}
