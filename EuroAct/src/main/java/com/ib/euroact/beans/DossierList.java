package com.ib.euroact.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.export.PDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.search.DossierSearch;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.customexporter.CustomExpPreProcess;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.IndexUIbean;
import com.ib.system.exceptions.DbErrorException;

@Named
@ViewScoped
public class DossierList extends IndexUIbean {

	/**
	 * Списък с актове на ЕС по зададени критерии
	 * 
	 */
	private static final long serialVersionUID = -8021542004444743608L;
	static final Logger LOGGER = LoggerFactory.getLogger(DossierList.class);
	
	
	private SystemData sd;
	
	private DossierSearch searchDossier;
	private LazyDataModelSQL2Array dossierList; // списък с  Информационни досиета 
	
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		sd = (SystemData) getSystemData();
		searchDossier = new DossierSearch();
	}
	
	/** 
	 *  Търсене на актове на ЕС по зададени критерии
	 * @throws DbErrorException 
	 * 
	 */
	public void actionSearch() throws DbErrorException {
		
		searchDossier.buildQuery(sd);
		dossierList = new LazyDataModelSQL2Array(searchDossier, "A01 desc");		
	}
	
	public void actionClear() { 
		
		searchDossier = new DossierSearch();
		dossierList = null;		
	}
	
	public String actionGoto(int view ,Integer idObj) {
		
		if(view==1) {
			return "dossierBeanView.xhtml?faces-redirect=true&idObj=" + idObj;
		} else {
			return "dossierBean.xhtml?faces-redirect=true&idObj=" + idObj;
		}
	}
	 
	public String actionGotoNew() {
		
		return "dossierBean.jsf?faces-redirect=true";	
	}

	public SystemData getSd() {
		return sd;
	}

	public void setSd(SystemData sd) {
		this.sd = sd;
	}
	
	public LazyDataModelSQL2Array getDossierList() {
		return dossierList;
	}

	public void setDossierList(LazyDataModelSQL2Array dossierList) {
		this.dossierList = dossierList;
	}
	
	public DossierSearch getSearchDossier() {
		return searchDossier;
	}

	public void setSearchDossier(DossierSearch searchDossier) {
		this.searchDossier = searchDossier;
	}
	
/******************************** EXPORTS **********************************/
	
	/**
	 * за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	 */
	public void postProcessXLS(Object document) {
		
		String title = getMessageResourceString(LABELS, "dossierList.title");		  
    	new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);		
     
	}

	/**
	 * за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	 */
	public void preProcessPDF(Object document)  {
		
		try {
			
			String title = getMessageResourceString(LABELS, "dossierList.title");		
			new CustomExpPreProcess().preProcessPDF(document, title, null, null, null);		
						
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);			
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);			
		} 
	}
	
	/**
	 * за експорт в pdf 
	 * @return
	 */
	public PDFOptions pdfOptions() {
		PDFOptions pdfOpt = new CustomExpPreProcess().pdfOptions(null, null, null);
        return pdfOpt;
	}
	
	/**
	 * подзаглавие за експорта 
	 */
//	public Object[] dopInfo() {
//		
//		Object[] dopInf = null;
//		dopInf = new Object[2];
//		if(this.dateFrom != null && this.dateTo != null) {
//			dopInf[0] = "период: " + DateUtils.printDate(this.dateFrom) + " - "+ DateUtils.printDate(this.dateTo);
//		} 
//	
//		return dopInf;
//	}
	
	/******************************** END EXPORTS **********************************/
	
}
