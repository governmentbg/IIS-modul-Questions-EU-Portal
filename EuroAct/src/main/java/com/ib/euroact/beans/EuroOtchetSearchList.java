package com.ib.euroact.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.export.PDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.search.EuroOtchetSearch;
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
public class EuroOtchetSearchList extends IndexUIbean {

	/**
	 * Списък отчети на МС
	 * @author s.arnaudova
	 */
	private static final long serialVersionUID = -7011929546695169915L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroOtchetSearchList.class);

	private static final String OTCHET_NEW = "euroOtchetEdit.jsf?faces-redirect=true";

	private SystemData sd;
	private Date decodeDate;
	private EuroOtchetSearch searchEuroOtchet;
	private EuroOtchet euroOtchet;

	/* филтър */
	private Integer periodR;

	private LazyDataModelSQL2Array otchetList; // списък с отчети
	private List<SelectItem> periodList;

	@PostConstruct
	void initData() {

		LOGGER.debug("PostConstruct EuroOtchetSearchList!!!");

		this.decodeDate = new Date();
		this.sd = (SystemData) getSystemData();
		this.searchEuroOtchet = new EuroOtchetSearch();
		this.euroOtchet = new EuroOtchet();

		try {
			this.periodList = createItemsList(false, EuroConstants.CODE_CLASSIF_PERIOD_NOFUTURE, this.decodeDate, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	/*************************
	 * Промяна на период
	 ***********************************/
	public void changePeriodR() {

		if (this.periodR != null) {
			Date[] di;
			di = DateUtils.calculatePeriod(this.periodR);
			this.searchEuroOtchet.setDatOtchetOt(di[0]);
			this.searchEuroOtchet.setDatOtchetDo(di[1]);
		} else {
			this.searchEuroOtchet.setDatOtchetOt(null);
			this.searchEuroOtchet.setDatOtchetDo(null);
		}
	}

	public void changeDate() {
		this.setPeriodR(null);
	}

	/*************************
	 * Зачистване на филтър
	 ***********************************/
	public void actionClear() {

		this.searchEuroOtchet.setDatOtchetOt(null);
		this.searchEuroOtchet.setDatOtchetDo(null);
		periodR = null;
		this.otchetList = null;
		this.searchEuroOtchet = new EuroOtchetSearch();
		this.euroOtchet = new EuroOtchet();

	}

	/*************************
	 * Списък с отчети на МС
	 ***********************************/
	public void actionSearch() {

		try {

			this.searchEuroOtchet.buildQuery(sd);
			this.otchetList = new LazyDataModelSQL2Array(this.searchEuroOtchet, "A01 desc");

		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане списъка с отчети на НС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/************************* Експорти ***********************************/

	// за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	public void postProcessXLS(Object document) {

		String title = getMessageResourceString(LABELS, "euroOtchetSearchList.title");
		new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);

	}

	// за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	public void preProcessPDF(Object document) {

		try {

			String title = getMessageResourceString(LABELS, "euroOtchetSearchList.title");
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
	// подзаглавие??

	/*************************
	 * ПРЕНАСОЧВАНЕ нов/редакция
	 ***********************************/
	public String actionGoto(int i, Object[] row) {

		String idObj = String.valueOf(row[0]);
		String result = "";

		if (i == 1) { // разглеждане
			result = "euroOtchetViewEdit.xhtml?faces-redirect=true&idObj=" + idObj;
		} else {
			return "euroOtchetEdit.jsf?faces-redirect=true&idObj=" + idObj;
		}
		return result;
	}

	public String actionGotoNew() {
		return OTCHET_NEW;
	}

	public Integer getPeriodR() {
		return periodR;
	}

	public void setPeriodR(Integer periodR) {
		this.periodR = periodR;
	}

	public Date getDecodeDate() {
		return new Date(decodeDate.getTime());
	}

	public void setDecodeDate(Date decodeDate) {
		this.decodeDate = decodeDate != null ? new Date(decodeDate.getTime()) : null;
	}

	public LazyDataModelSQL2Array getOtchetList() {
		return otchetList;
	}

	public void setOtchetList(LazyDataModelSQL2Array otchetList) {
		this.otchetList = otchetList;
	}

	public EuroOtchetSearch getSearchEuroOtchet() {
		return searchEuroOtchet;
	}

	public void setSearchEuroOtchet(EuroOtchetSearch searchEuroOtchet) {
		this.searchEuroOtchet = searchEuroOtchet;
	}

	public SystemData getSd() {
		return sd;
	}

	public void setSd(SystemData sd) {
		this.sd = sd;
	}

	public List<SelectItem> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<SelectItem> periodList) {
		this.periodList = periodList;
	}

	public EuroOtchet getEuroOtchet() {
		return euroOtchet;
	}

	public void setEuroOtchet(EuroOtchet euroOtchet) {
		this.euroOtchet = euroOtchet;
	}

}
