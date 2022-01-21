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


import com.ib.euroact.search.EuroHarmSearch;
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
public class EuroHarmSearchBean extends IndexUIbean {

	/**
	 * Хармонизация на закони
	 * 
	 * @author s.arnaudova
	 */
	private static final long serialVersionUID = -8785152431283632429L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroHarmSearchBean.class);

	private SystemData sd;
	private EuroHarmSearch euroHarmSearch;

	private Integer periodR;
	private LazyDataModelSQL2Array euroHarmList;


	@PostConstruct
	void initData() {

		LOGGER.debug("PostConstruct EuroHarmSearchBean!!!");

		this.sd = (SystemData) getSystemData();
		this.euroHarmSearch = new EuroHarmSearch();
		

	}

	public void actionSearch() {

		try {

			this.euroHarmSearch.setHarm(1); //Тук твърдо само за хармонизация, понеже е справка
			this.euroHarmSearch.buildQuery(sd);
			this.euroHarmList = new LazyDataModelSQL2Array(this.euroHarmSearch, null);

		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане на хармонизация!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	public void actionClear() {

		this.euroHarmSearch = new EuroHarmSearch();
		this.euroHarmList = null;
		this.periodR = null;

	}

	public void changePeriodR() {

		if (this.periodR != null) {
			Date[] di;
			di = DateUtils.calculatePeriod(this.periodR);
			this.euroHarmSearch.setDatZakonOt(di[0]);
			this.euroHarmSearch.setDatZakonDo(di[1]);
		} else {
			this.euroHarmSearch.setDatZakonOt(null);
			this.euroHarmSearch.setDatZakonDo(null);
		}
	}

	public void changeDate() {
		this.setPeriodR(null);
	}

	/************************* Експорти ***********************************/

	public void postProcessXLS(Object document) {
		// за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
		String title = getMessageResourceString(LABELS, "euroHarm.title");
		new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);

	}

	public void preProcessPDF(Object document) {
		// за експорт в pdf - добавя заглавие и дата на изготвяне на справката
		try {

			String title = getMessageResourceString(LABELS, "euroHarm.title");
			new CustomExpPreProcess().preProcessPDF(document, title, null, null, null);

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
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
	

	public String actionGoto(Integer idObj) {
		return "euroHarmView.xhtml?faces-redirect=true&idObj=" + idObj;
	}
	
	/********************/

	public int getPeriodNoFuture() {
		return EuroConstants.CODE_CLASSIF_PERIOD_NOFUTURE;
	}
	
	public int getSysClassifDaNe() {
		return EuroConstants.CODE_CLASSIF_DANE;
	}

	public SystemData getSd() {
		return sd;
	}

	public void setSd(SystemData sd) {
		this.sd = sd;
	}

	public EuroHarmSearch getEuroHarmSearch() {
		return euroHarmSearch;
	}

	public void setEuroHarmSearch(EuroHarmSearch euroHarmSearch) {
		this.euroHarmSearch = euroHarmSearch;
	}

	public LazyDataModelSQL2Array getEuroHarmList() {
		return euroHarmList;
	}

	public void setEuroHarmList(LazyDataModelSQL2Array euroHarmList) {
		this.euroHarmList = euroHarmList;
	}

	public Integer getPeriodR() {
		return periodR;
	}

	public void setPeriodR(Integer periodR) {
		this.periodR = periodR;
	}


}
