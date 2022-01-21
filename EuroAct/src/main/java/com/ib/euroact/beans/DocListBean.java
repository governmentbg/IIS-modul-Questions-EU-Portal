package com.ib.euroact.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.export.PDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.search.EuroDocSearch;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.customexporter.CustomExpPreProcess;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;

/**
 * Екран 'Актуализация на документи'
 * @author n.kanev
 *
 */
@Named("docList")
@ViewScoped
public class DocListBean extends IndexUIbean {

	private static final long serialVersionUID = 5577600110347943488L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DocListBean.class);
	
	public static final int CODE_CLASSIF_PERIOD = EuroConstants.CODE_CLASSIF_PERIOD_NOFUTURE;
	
	private boolean signaturaChecked;
	private List<SystemClassif> signaturaOptions;
	private Object signaturaSeria;
	private List<SystemClassif> procOptions;
	private Object procSeria;
	private Integer dokumentPeriod;
	private Integer poluchavanePeriod;
	
	private EuroDocSearch searchEuroDoc;
	private LazyDataModelSQL2Array docsList;

	@PostConstruct
	public void initData() {
		initFilter();
		
		this.signaturaOptions = new ArrayList<>();
		this.procOptions = new ArrayList<>();
		try {
			this.signaturaOptions.addAll(getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_SIGNATURA_SERIA, new Date(), this.getCurrentLang()));
			this.procOptions.addAll(getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_PROC_CODE, new Date(), this.getCurrentLang()));
		} catch (DbErrorException e) {
			LOGGER.error(e.toString());
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, getMessageResourceString(beanMessages, "general.errorClassif"));
		}
	
	}
	
	/**
	 * Начални стойности на филтъра
	 */
	private void initFilter() {
		this.searchEuroDoc = new EuroDocSearch();
		this.docsList = null;
		this.signaturaChecked = false;
		this.signaturaSeria = null;
		this.procSeria = null;
		this.dokumentPeriod = null;
		this.poluchavanePeriod = null;
	}
	
	/**
	 * Щрака се чекбоксът до сигнатурата
	 */
	public void actionToggleSignatura() {
		if(this.signaturaChecked) {
			this.signaturaSeria = null;
			this.searchEuroDoc.setSignSeria(null);
			this.searchEuroDoc.setSignGodina(null);
			this.searchEuroDoc.setSignNomer(null);
		}
		else {
			this.searchEuroDoc.setFreeNom(null);
		}
	}
	
	/**
	 * Направен е избор в падащото меню за сигнатура
	 */
	public void actionSelectSignatura() {
		this.searchEuroDoc.setSignSeria(this.getSeriaCode(this.signaturaSeria));
	}
	
	/**
	 * Направен е избор в падащото меню за междуинст... долу
	 */
	public void actionSelectProc() {
		this.searchEuroDoc.setProcSeria(this.getSeriaCode(this.procSeria));
	}
	
	/**
	 * Класификацията се получава като стринг, а тук се взима само code
	 * @param chosenValue обект от тип SystemClassif, който идва от екрана като стринг
	 * @return стйността на code полето на избрания обект
	 */
	private Integer getSeriaCode(Object chosenValue) {
		if(chosenValue == null) {
			return null;
		}
		else {
			String regex = "(code=\\d*)";
			Pattern pattern = Pattern.compile(regex);
			Matcher m = pattern.matcher((String) chosenValue);
			if(m.find()) {
				String code = m.group().split("=")[1];
				return Integer.parseInt(code);
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Направен е избор в падащото меню за период 'Дата на документа'
	 */
	public void actionChangePeriodDokument() {
		if (this.dokumentPeriod != null) {
			Date[] period;
			period = DateUtils.calculatePeriod(this.dokumentPeriod);
			this.searchEuroDoc.setDateDocOt(period[0]);
			this.searchEuroDoc.setDateDocDo(period[1]);
    	} else {
    		this.searchEuroDoc.setDateDocOt(null);
			this.searchEuroDoc.setDateDocDo(null);
		}
	}
	
	/**
	 * Направен е избор в падащото меню за период 'Дата на получаване'
	 */
	public void actionChangePeriodPoluchavane() {
		if (this.poluchavanePeriod != null) {
			Date[] period;
			period = DateUtils.calculatePeriod(this.poluchavanePeriod);
			this.searchEuroDoc.setDatePoluchOt(period[0]);
			this.searchEuroDoc.setDatePoluchDo(period[1]);
    	} else {
    		this.searchEuroDoc.setDatePoluchOt(null);
			this.searchEuroDoc.setDatePoluchDo(null);
		}
	}
	
	/**
	 * Натиснат е бутонът 'Търсене' на филтъра
	 */
	public void actionSearch() {
		try {
			this.searchEuroDoc.buildQuery(getSystemData(SystemData.class));
		} catch (DbErrorException e) {
			e.printStackTrace();
		}
		this.docsList = new LazyDataModelSQL2Array(this.searchEuroDoc, null); 
	}
	
	/**
	 * Натиснат е бутонът 'Изчистване' на филтъра
	 */
	public void actionClear() {
		initFilter();
	}
	
	/**
	 * за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	 */
	public void postProcessXLS(Object document) {
		
		String title = getMessageResourceString(LABELS, "docList.title");		  
    	new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);		
     
	}

	/**
	 * за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	 */
	public void preProcessPDF(Object document)  {
		
		try {
			
			String title = getMessageResourceString(LABELS, "docList.title");		
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
	
	public String actionGotoNew() {
		return "docNew.jsf?faces-redirect=true";
	}
	
	public String actionGotoEdit(Integer docId) {
		return "docEdit.jsf?faces-redirect=true&idObj=" + docId;
	}
	
	public String actionGotoView(Integer docId) {
		return "docEditView.xhtml?faces-redirect=true&idObj=" + docId;
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public boolean isSignaturaChecked() {
		return signaturaChecked;
	}

	public void setSignaturaChecked(boolean signaturaChecked) {
		this.signaturaChecked = signaturaChecked;
	}

	public List<SystemClassif> getSignaturaOptions() {
		return signaturaOptions;
	}

	public void setSignaturaOptions(List<SystemClassif> signaturaOptions) {
		this.signaturaOptions = signaturaOptions;
	}

	public Object getSignaturaSeria() {
		return signaturaSeria;
	}

	public void setSignaturaSeria(Object signaturaSeria) {
		this.signaturaSeria = signaturaSeria;
	}

	public List<SystemClassif> getProcOptions() {
		return procOptions;
	}

	public void setProcOptions(List<SystemClassif> procOptions) {
		this.procOptions = procOptions;
	}

	public Object getProcSeria() {
		return procSeria;
	}

	public void setProcSeria(Object procSeria) {
		this.procSeria = procSeria;
	}

	public Integer getDokumentPeriod() {
		return dokumentPeriod;
	}

	public void setDokumentPeriod(Integer dokumentPeriod) {
		this.dokumentPeriod = dokumentPeriod;
	}

	public Integer getPoluchavanePeriod() {
		return poluchavanePeriod;
	}

	public void setPoluchavanePeriod(Integer poluchavanePeriod) {
		this.poluchavanePeriod = poluchavanePeriod;
	}

	public EuroDocSearch getSearchEuroDoc() {
		return searchEuroDoc;
	}

	public void setSearchEuroDoc(EuroDocSearch searchEuroDoc) {
		this.searchEuroDoc = searchEuroDoc;
	}

	public LazyDataModelSQL2Array getDocsList() {
		return docsList;
	}

	public void setDocsList(LazyDataModelSQL2Array docsList) {
		this.docsList = docsList;
	}
}
