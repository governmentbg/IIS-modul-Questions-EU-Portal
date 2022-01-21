package com.ib.euroact.components;

import java.util.Date;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.search.EuroActNewSearch;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.exceptions.DbErrorException;

/** */
@FacesComponent(value = "compActSearch", createTag = true)
public class CompActSearch extends UINamingContainer {
	
	private enum PropertyKeys {
		ACTSEARCH, CODEACT, SHOWME, ACTSLIST
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CompActSearch.class);
	
	private UserData userData	= null;
	private SystemData systemData	= null;
	private Date dateClassif = null;
	
	/**
	 * Разширеното търсене - инциализира компонентата   <f:event type="preRenderComponent" listener="#{cc.initEuroActS(true)}" />
	 */	
	public void initActSearch(boolean bb) {

		boolean modal = (Boolean) getAttributes().get("modal"); // обработката е в модален диалог (true) или не (false)
		boolean onlySadAct = (Boolean) getAttributes().get("onlySadAct");
		
		EuroActNewSearch tmpActS1 = new EuroActNewSearch();

		if (!modal) {
			tmpActS1 = new EuroActNewSearch();
		}

		EuroActNewSearch tmpActS = new EuroActNewSearch();
		
		if (onlySadAct) {
			tmpActS.setVidAct(EuroConstants.TIP_ACT_SADEVNI_AKTOVE);
		}		
		
		if (bb) { // при отваряне на модалния
			setShowMe(true);
			String srchTxt = (String) getAttributes().get("searchTxt");
			if (srchTxt != null) {
				tmpActS.setPartIme(srchTxt);
			}
		
		} else { // при изчистване...
			ValueExpression expr2 = getValueExpression("searchTxt");
			ELContext ctx2 = getFacesContext().getELContext();
			if (expr2 != null) {
				expr2.setValue(ctx2, null);
			}

			tmpActS1 = null;
		}

		if (tmpActS1 == null || tmpActS1.getPartIme() == null) {

			setActSearch(tmpActS);
			setActsList(null);
		}
		
		LOGGER.debug("initEuroActS>>>> ");
	}
	
	 /** 
	    * търсене на акт - изп. се само, ако е в модален прозорец
	    * изивква се при затваряне на модалния прозореца (onhide) 
	    * 
	    */
	   public void actionHideModal() {		
		   setActSearch(null);
		   setActsList(null);
		   setShowMe(false);
		   LOGGER.debug("actionHideModal>>>> ");
		}
	   
	   /** 
	    * търсене на акт - бутон "Търсене"
	 * @throws DbErrorException 
	    */
	   public void actionSearchAct() throws DbErrorException {		
		   getActSearch().buildQuery(getSystemData());
		   setActsList(new LazyDataModelSQL2Array(getActSearch(),"A01 desc")); 
		   LOGGER.debug("actionSearchAct >>>> {}", getActsList().getRowCount());
		}
	   
	   /** 
	    * търсене на акт - бутон "Изчисти"
	    */
	   public void actionClearAct() {		
		   initActSearch(false); 
		   LOGGER.debug("actionClearAct >>>> ");
		}	  
	   
	   /**
	    * търсене на акт - избор на акт
	    */
	   public void actionModalSelectAct(Object[] row) {	  
		   
		    if( row != null && row[0] != null) {
		    	 LOGGER.debug("actionModalSelectAct >>>> {}", row[0]);
			   //връща id на избрания кореспондент
			    ValueExpression expr2 = getValueExpression("codeAct");
				ELContext ctx2 = getFacesContext().getELContext();
				if (expr2 != null) {
					expr2.setValue(ctx2, Integer.valueOf(row[0].toString()));
				}	
		    }
		// извиква remoteCommnad - ако има такава....
			String remoteCommnad = (String) getAttributes().get("onComplete");
			if (remoteCommnad != null && !"".equals(remoteCommnad)) {
				PrimeFaces.current().executeScript(remoteCommnad);
			}		
	   }
	
	public EuroActNewSearch getActSearch() {
		EuroActNewSearch eval = (EuroActNewSearch) getStateHelper().eval(PropertyKeys.ACTSEARCH, null);
		return eval != null ? eval : new EuroActNewSearch();
	}

	public void setActSearch(EuroActNewSearch actSearch) {
		getStateHelper().put(PropertyKeys.ACTSEARCH, actSearch);
	}
	
	public Integer getCodeAct() {
		return (Integer) getStateHelper().eval(PropertyKeys.CODEACT, null);
	}

	public void setCodeAct(Integer codeAct) {
		getStateHelper().put(PropertyKeys.CODEACT, codeAct);
	}
	

	public LazyDataModelSQL2Array getActsList() {
		return (LazyDataModelSQL2Array) getStateHelper().eval(PropertyKeys.ACTSLIST, null);
	}


	public void setActsList(LazyDataModelSQL2Array actsList) {
		getStateHelper().put(PropertyKeys.ACTSLIST, actsList);
	}
	
	/** @return */
	public boolean isShowMe() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWME, false);
	}
	/** @param showMe */
	public void setShowMe(boolean showMe) {
		getStateHelper().put(PropertyKeys.SHOWME, showMe);
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
	
	/** @return the systemData */
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