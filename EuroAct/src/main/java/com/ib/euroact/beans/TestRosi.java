package com.ib.euroact.beans;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.FilesDAO;
import com.ib.euroact.db.dto.Files;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;



@Named
@ViewScoped
public class TestRosi extends IndexUIbean implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestRosi.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 4985013225316883670L;


	private List<Files> filesList;

	public List<Files> getFilesList() {
		return filesList;
	}


	public void setFilesList(List<Files> filesList) {
		this.filesList = filesList;
	}
	
	@PostConstruct
	void initData() {
		
		try {
			JPA.getUtil().runWithClose(() -> {
				FilesDAO daoF = new FilesDAO(getUserData());		
				filesList = daoF.selectByFileObject(6507,83); 
			 
			});
		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане на файлове ! ", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR,getMessageResourceString(UI_beanMessages, ERRDATABASEMSG), e.getMessage());
		}
		
	}
	
}