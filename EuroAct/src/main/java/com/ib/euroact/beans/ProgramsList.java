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

import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.customexporter.CustomExpPreProcess;
import com.ib.indexui.pagination.LazyDataModelSQL2Array;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.exceptions.DbErrorException;

@Named
@ViewScoped
public class ProgramsList extends IndexUIbean {

	/**
	 * Списък с програми на НС
	 * 
	 */
	private static final long serialVersionUID = -8021542004444743608L;
	static final Logger LOGGER = LoggerFactory.getLogger(ProgramsList.class);
	
	private Date decodeDate;
	private SystemData sd;
	
	private ProgramaSearch searchProgram;
	private LazyDataModelSQL2Array programsList; // списък с програми 
	
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		this.decodeDate = new Date();
		this.sd = (SystemData) getSystemData();
		this.searchProgram = new ProgramaSearch();
		
		actionSearch();
	}
	
	/** 
	 *  Търсене на програми на НС
	 * @throws DbErrorException 
	 * 
	 */
	public void actionSearch() {
		
		try {
			
			this.searchProgram.buildQuery(sd);
			this.programsList = new LazyDataModelSQL2Array(this.searchProgram, "A01 desc");

		} catch (DbErrorException e) {
			LOGGER.error("Грешка при зареждане данните на програми на НС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	public String actionGoto(Integer idObj) {
		
		return "programEdit.jsf?faces-redirect=true&idObj=" + idObj;
	}
	
	public String actionGotoNew() {
		
		return "programEdit.jsf?faces-redirect=true";	
	}
	
	public String actionGotoView(Integer idObj) {
		
		return "programView.xhtml?faces-redirect=true&idObj=" + idObj + "&view=1";
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

	public ProgramaSearch getSearchProgram() {
		return searchProgram;
	}

	public void setSearchProgram(ProgramaSearch searchProgram) {
		this.searchProgram = searchProgram;
	}

	public LazyDataModelSQL2Array getProgramsList() {
		return programsList;
	}

	public void setProgramsList(LazyDataModelSQL2Array programsList) {
		this.programsList = programsList;
	}
	
/******************************** EXPORTS **********************************/	

	/**
	 * за експорт в excel - добавя заглавие и дата на изготвяне на справката и др.
	 */
	public void postProcessXLS(Object document) {
		
		String title = getMessageResourceString(LABELS, "programsList.title");		  
    	new CustomExpPreProcess().postProcessXLS(document, title, null, null, null);		
     
	}

	/**
	 * за експорт в pdf - добавя заглавие и дата на изготвяне на справката
	 */
	public void preProcessPDF(Object document)  {
		
		try {
			
			String title = getMessageResourceString(LABELS, "programsList.title");		
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
	/******************************** END EXPORTS **********************************/
	
}
