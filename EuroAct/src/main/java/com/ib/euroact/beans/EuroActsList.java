package com.ib.euroact.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.export.PDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.search.EuroActNewSearch;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.customexporter.CustomExpPreProcess;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.exceptions.DbErrorException;

@Named
@ViewScoped
public class EuroActsList extends IndexUIbean {

	/**
	 * Списък с актове на ЕС по зададени критерии
	 * 
	 */
	private static final long serialVersionUID = -8021542004444743608L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroActsList.class);
	
	private Date decodeDate;
	private SystemData sd;
	
	private EuroActNewSearch searchEuroAct;
	private LazyDataModelSQL2Array actsList; // списък с актове 
	
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		this.decodeDate = new Date();
		this.sd = (SystemData) getSystemData();
		this.searchEuroAct = new EuroActNewSearch();
	}
	
	/** 
	 *  Търсене на актове на ЕС по зададени критерии
	 * @throws DbErrorException 
	 * 
	 */
	public void actionSearch() {
		
		try {
		
			this.searchEuroAct.buildQuery(sd);
			this.actsList = new LazyDataModelSQL2Array(this.searchEuroAct, "A01 desc");
		
		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане списъка с актове на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	public void actionClear() {
		
		this.searchEuroAct = new EuroActNewSearch();
		this.actsList = null;		
	}
	
	public String actionGoto(Integer idObj) {
		
		return "euroActEdit.jsf?faces-redirect=true&idObj=" + idObj;
	}
	
	public String actionGotoNew() {
		
		return "euroActEdit.jsf?faces-redirect=true";	
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

	public EuroActNewSearch getSearchEuroAct() {
		return searchEuroAct;
	}

	public void setSearchEuroAct(EuroActNewSearch searchEuroAct) {
		this.searchEuroAct = searchEuroAct;
	}

	public LazyDataModelSQL2Array getActsList() {
		return actsList;
	}

	public void setActsList(LazyDataModelSQL2Array actsList) {
		this.actsList = actsList;
	}
	
/******************************** EXPORTS **********************************/
	
	/**
	 * за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	 */
	public void postProcessXLS(Object document) {
		
		String title = getMessageResourceString(LABELS, "euroActsList.title");		  
    	new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);		
     
	}

	/**
	 * за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	 */
	public void preProcessPDF(Object document)  {
		
		try {
			
			String title = getMessageResourceString(LABELS, "euroActsList.title");		
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
