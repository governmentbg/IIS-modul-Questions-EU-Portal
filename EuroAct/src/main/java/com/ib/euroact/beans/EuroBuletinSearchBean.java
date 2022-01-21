package com.ib.euroact.beans;

import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.export.PDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.search.EuroBuletinSearch;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.customexporter.CustomExpPreProcess;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;

@Named
@ViewScoped
public class EuroBuletinSearchBean extends IndexUIbean{
	
	/**
	 * 
	 */
	private EuroBuletinSearch buletinSearch;
	private Integer period;
	private SystemData sd;
	private LazyDataModelSQL2Array buletinList;

	private static final long serialVersionUID = -8576974177747176673L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroBuletinSearchBean.class);
	
	@PostConstruct
	public void initData() {
		this.sd = (SystemData) getSystemData();
		this.buletinSearch = new EuroBuletinSearch();
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}
	
	public EuroBuletinSearch getBuletinSearch() {
		return buletinSearch;
	}

	public void setBuletinSearch(EuroBuletinSearch buletinSearch) {
		this.buletinSearch = buletinSearch;
	}

	public LazyDataModelSQL2Array getBuletinList() {
		return buletinList;
	}

	public void setBuletinList(LazyDataModelSQL2Array buletinList) {
		this.buletinList = buletinList;
	}

	public void changeDate() {
		this.setPeriod(null);
		
	}
	public void changePeriod () {
		
    	if (this.period != null) {
			Date[] di;
			di = DateUtils.calculatePeriod(this.period);
			buletinSearch.setDatBuletinOt(di[0]);
			buletinSearch.setDatBuletinDo(di[1]);		
    	} else {
    		buletinSearch.setDatBuletinOt(null);
    		buletinSearch.setDatBuletinDo(null);
		}
    }
	
	public void actionSearch() {
		try {
			
			this.buletinSearch.buildQuery(sd);
			this.buletinList = new LazyDataModelSQL2Array(this.buletinSearch, "A01 desc");
		
		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане списъка с Евровести!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	public void actionClear() {
		this.buletinSearch = new EuroBuletinSearch();
		this.buletinList = null;
		this.period = null;
	}
	
	public String actionGotoNew() {
		
		return "euroBuletinEdit.jsf?faces-redirect=true";	
	
	}
	
	public String actionGoto(int i, Object[] row) {
		
		String idObj = String.valueOf(row[0]);
		String result = "";

		if (i == 1) { // разглеждане
			result = "euroBuletinViewEdit.xhtml?faces-redirect=true&idObj=" + idObj;
		} else {
			return "euroBuletinEdit.jsf?faces-redirect=true&idObj=" + idObj;
		}
		return result;
	}
	
	public int getClassifPreiod(){
		return EuroConstants.CODE_CLASSIF_PERIOD_NOFUTURE;
	}
	
/******************************** EXPORTS **********************************/
	
	/**
	 * за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	 */
	public void postProcessXLS(Object document) {
		
		String title = getMessageResourceString(LABELS, "euroBuletinList.title");		  
    	new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);		
     
	}

	/**
	 * за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	 */
	public void preProcessPDF(Object document)  {
		
		try {
			
			String title = getMessageResourceString(LABELS, "euroBuletinList.title");		
			new CustomExpPreProcess().preProcessPDF(document, title, null, null, null);		
						
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
	
}
