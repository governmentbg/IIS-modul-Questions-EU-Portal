package com.ib.euroact.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroOtchetDAO;
import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.navigation.Navigation;
import com.ib.indexui.navigation.NavigationData;
import com.ib.indexui.navigation.NavigationDataHolder;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.SearchUtils;

@Named
@ViewScoped
public class EuroOtchetEdit extends IndexUIbean implements Serializable {

	/**
	 * Въвеждане/актуализация на отчет на МС
	 * 
	 * @author s.arnaudova
	 */
	private static final long serialVersionUID = 3116611860713714620L;
	static final Logger LOGGER = LoggerFactory.getLogger(EuroOtchetEdit.class);

	private static final String ID_OBJ = "idObj";
	private static final String FORM_EURO_OTCHET = "formEuroOtchetEdit";

	private Date decodeDate;
	private SystemData sd;

	private transient EuroOtchetDAO dao;
	private EuroOtchet euroOtchet;


	@PostConstruct
	public void initData() {

		LOGGER.debug("PostConstruct EuroOtchetEdit!!!");

		this.decodeDate = new Date();
		this.sd = (SystemData) getSystemData();

		this.dao = new EuroOtchetDAO(getUserData());
		this.euroOtchet = new EuroOtchet();

		if (JSFUtils.getRequestParameter(ID_OBJ) != null && !"".equals(JSFUtils.getRequestParameter(ID_OBJ))) {

			Integer idObj = Integer.valueOf(JSFUtils.getRequestParameter(ID_OBJ));

			if (idObj != null) {
				loadOtchet(idObj);
			}
		}

	}

	/*************************
	 * Зареждане отчет
	 ***********************************/
	private void loadOtchet(Integer idObj) {

		try {

			JPA.getUtil().runWithClose(() -> this.euroOtchet = this.dao.findById(idObj));

		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане данните на отчета! ", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/*************************
	 * Проверка за въведени данни
	 ***********************************/
	private boolean checkData() {

		boolean save = true;

		if (this.euroOtchet.getDatOtchet() == null) {
			JSFUtils.addMessage(FORM_EURO_OTCHET + ":dateReg", FacesMessage.SEVERITY_ERROR, getMessageResourceString(
					UI_beanMessages, MSGPLSINS, getMessageResourceString(UI_LABELS, "general.date")));
			save = false;
		}

		if (SearchUtils.isEmpty(this.euroOtchet.getAnot())) {
			JSFUtils.addMessage(FORM_EURO_OTCHET + ":otchetOtnosno", FacesMessage.SEVERITY_ERROR,
					getMessageResourceString(UI_beanMessages, MSGPLSINS,
							getMessageResourceString(LABELS, "euroOtchetSearchList.otnosno")));
			save = false;
		}

		if (this.euroOtchet.getAdresat() == null) {
			JSFUtils.addMessage(FORM_EURO_OTCHET + ":otchetAdresat", FacesMessage.SEVERITY_ERROR,
					getMessageResourceString(UI_beanMessages, MSGPLSINS,
							getMessageResourceString(LABELS, "euroOtchetSearchList.adresat")));
			save = false;
		}

		if (this.euroOtchet.getStatus() == null) {
			JSFUtils.addMessage(FORM_EURO_OTCHET + ":otchetStatus", FacesMessage.SEVERITY_ERROR,
					getMessageResourceString(UI_beanMessages, MSGPLSINS,
							getMessageResourceString(LABELS, "euroOtchetSearchList.status")));
			save = false;
			
		} else {
			// проверка за бъдещи дати
			if (DateUtils.startDate(this.euroOtchet.getDatOtchet()).after(DateUtils.startDate(new Date()))) {
				JSFUtils.addMessage(FORM_EURO_OTCHET + ":dateReg", FacesMessage.SEVERITY_ERROR,
						getMessageResourceString(beanMessages, "euroOtchetEdit.dateAfter"));

				save = false;
			}

		}

		return save;
	}

	/*************************
	 * Запис
	 ***********************************/
	public void actionSave() {

		if (checkData()) {

			try {

				JPA.getUtil().runInTransaction(() -> this.euroOtchet = this.dao.save(euroOtchet));

				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO,
						getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));

			} catch (Exception e) {
				LOGGER.error("Грешка при запис на отчет на МС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}

		}

	}

	/*************************
	 * Изтриване
	 ***********************************/
	public void actionDelete() {

		try {

			JPA.getUtil().runInTransaction(() -> this.dao.delete(euroOtchet));

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO,
					getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));

			this.euroOtchet = new EuroOtchet();

			Navigation navHolder = new Navigation();
			int i = navHolder.getNavPath().size();

			NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils
					.getManagedBean("navigationSessionDataHolder");
			Stack<NavigationData> stackPath = dataHoslder.getPageList();
			NavigationData nd = stackPath.get(i - 2);
			Map<String, Object> mapV = nd.getViewMap();

			EuroOtchetSearchList euroOtchetSearchList = (EuroOtchetSearchList) mapV.get("euroOtchetSearchList");
			euroOtchetSearchList.actionSearch();

			navHolder.goBack();

		} catch (Exception e) {
			LOGGER.error("Грешка при изтриване на отчет на МС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}

	}

	public Date getDecodeDate() {
		return decodeDate;
	}

	public void setDecodeDate(Date decodeDate) {
		this.decodeDate = decodeDate;
	}

	public SystemData getSd() {
		return sd;
	}

	public void setSd(SystemData sd) {
		this.sd = sd;
	}

	public EuroOtchetDAO getDao() {
		return dao;
	}

	public void setDao(EuroOtchetDAO dao) {
		this.dao = dao;
	}

	public EuroOtchet getEuroOtchet() {
		return euroOtchet;
	}

	public void setEuroOtchet(EuroOtchet euroOtchet) {
		this.euroOtchet = euroOtchet;
	}

}
