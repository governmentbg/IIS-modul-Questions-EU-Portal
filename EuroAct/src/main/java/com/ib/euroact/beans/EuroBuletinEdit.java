package com.ib.euroact.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ib.euroact.db.dao.EuroBuletinDAO;
import com.ib.euroact.db.dao.FilesDAO;
import com.ib.euroact.db.dto.EuroBuletin;
import com.ib.euroact.db.dto.Files;
import com.ib.euroact.system.EuroConstants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.X;

@Named
@ViewScoped
public class EuroBuletinEdit extends IndexUIbean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3834862456069538279L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroBuletinEdit.class);

	private EuroBuletin buletin;
	private transient EuroBuletinDAO buletinDAO;
	private static final String FORM_EURO_BULETIN = "formEuroBuletin";
	private List<Files> filesList;

	@PostConstruct
	private void init() {
		this.buletin = new EuroBuletin();
		this.buletinDAO = new EuroBuletinDAO(getUserData());
		FilesDAO daoF = new FilesDAO(getUserData());
		String objId = JSFUtils.getRequestParameter("idObj");
		filesList = new ArrayList<>();
		if (objId != null && !"".equals(objId)) {

			Integer idObj = Integer.valueOf(objId);

			if (idObj != null) {
				try {
					JPA.getUtil().runWithClose(() -> this.buletin = this.buletinDAO.findById(idObj));
					filesList = daoF.selectByFileObject(idObj, EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_BULETIN);
				} catch (BaseException e) {
					LOGGER.error("Грешка при търсене на бюлетинн по Id!", e);
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				}

			}
		}

	}

	private boolean checkData() throws DbErrorException {

		boolean save = false;

//		if (this.buletin.getNomer() == null) {
//			JSFUtils.addMessage(FORM_EURO_BULETIN + ":nomerBuletin", FacesMessage.SEVERITY_ERROR,
//					getMessageResourceString(UI_beanMessages, MSGPLSINS,
//							getMessageResourceString(LABELS, "euroBuletinSearch.nomer")));
//			save = true;
//		}

		if (this.buletin.getDatBuletin() == null) {
			JSFUtils.addMessage(FORM_EURO_BULETIN + ":dateBuletin", FacesMessage.SEVERITY_ERROR,
					getMessageResourceString(UI_beanMessages, MSGPLSINS,
							getMessageResourceString(LABELS, "euroBuletinSearch.date")));
			save = true;
		}
		if (this.buletin.getNomer() != null) {

			boolean chekBuletin = this.buletinDAO.checkBuletinNumberForDubl(this.buletin);
			if (chekBuletin) {
				JSFUtils.addMessage(FORM_EURO_BULETIN + ":nomerBuletin", FacesMessage.SEVERITY_ERROR,
						"Вече съществува бюлетин със същия номер за тази година!");
				save = true;

			}
		}

		Date currentDate = new Date();
		Date picked = this.buletin.getDatBuletin();
		if (picked != null && DateUtils.actionCountDays(picked, currentDate) == -1) {
			JSFUtils.addMessage(FORM_EURO_BULETIN + ":dateBuletin", FacesMessage.SEVERITY_ERROR,
					"Дата на бюлетин не може да бъде бъдеща дата!");
			save = true;
		}

		return save;
	}

	public void actionSave() throws DbErrorException {
		if (checkData()) {
			return;
		}
	
		
		
		
		
		boolean newBuletin = buletin.getId() == null;
		try {
			JPA.getUtil().runInTransaction(() ->{
					buletin = this.buletinDAO.save(buletin);
					if (newBuletin) {
						buletin.setNomer(buletin.getId());
					
						if(filesList != null && !filesList.isEmpty()) {
							FilesDAO filesDao = new FilesDAO(getUserData());
							for (Files f : filesList) {
								f.setCodeObject(EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_BULETIN);
								f.setIdObject(buletin.getId());
								filesDao.saveFile(f);
							}
						}
					}
				}
			);
			
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO,getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));
			getSystemData().reloadClassif(EuroConstants.CODE_SYSCLASS_EURO_ACT, false, true);

		} catch (BaseException e) {
			LOGGER.error("Грешка при запис на бюлетинн!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	private void autoSave(Files files, X<Files> x) throws BaseException {
		
		
	}
	
	
	
	public void actionDelete() {
		try {

			JPA.getUtil().runInTransaction(() -> {
				this.buletinDAO.delete(this.buletin);
				if (filesList != null && !filesList.isEmpty()) {

					FilesDAO filesDao = new FilesDAO(getUserData());
					for (Files f : filesList) {
						filesDao.deleteFile(f);
					}
				}

			});

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO,
					getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));

			this.buletin = new EuroBuletin();
			this.filesList = new ArrayList<>();

		} catch (BaseException e) {
			LOGGER.error("Грешка при изтриване на бюлетин!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}

	}

	public EuroBuletin getBuletin() {
		return this.buletin;
	}

	public void setBuletin(EuroBuletin buletin) {
		this.buletin = buletin;
	}

	public List<Files> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<Files> filesList) {
		this.filesList = filesList;
	}
}
