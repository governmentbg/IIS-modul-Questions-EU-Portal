package com.ib.euroact.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroHarmDAO;
import com.ib.euroact.db.dto.EuroHarm;
import com.ib.euroact.system.EuroConstants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;

@Named("euroHarmView")
@ViewScoped
public class ЕuroHarmView extends IndexUIbean {

	/**
	 * Хармонизация на закони/разглеждане/
	 * 
	 * @author s.arnaudova
	 */
	private static final long serialVersionUID = 2555363961995455218L;
	static final Logger LOGGER = LoggerFactory.getLogger(ЕuroHarmView.class);

	private static final String ID_OBJ = "idObj";

	private transient EuroHarmDAO harmDao;
	private transient EuroHarm harm;
	
	private boolean priority;
	

	@PostConstruct
	void initData() {

		LOGGER.debug("PostConstruct EuroHarmSearchBean!!!");

		this.harmDao = new EuroHarmDAO();
		this.harm = new EuroHarm();

		if (JSFUtils.getRequestParameter(ID_OBJ) != null && !"".equals(JSFUtils.getRequestParameter(ID_OBJ))) {

			Integer idObj = Integer.valueOf(JSFUtils.getRequestParameter(ID_OBJ));

			if (idObj != null) {
				loadHarm(idObj);
			}
		}

	}

	private void loadHarm(Integer idHarm) {

		try {

			JPA.getUtil().runWithClose(() -> this.harm = this.harmDao.findById(idHarm));
			
			if (this.harm.getPrioritet() == null || this.harm.getPrioritet() == EuroConstants.CODE_ZNACHENIE_NE) {
				this.priority = false;
			} else {
				this.priority = true;
			}
		
		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане данните на отчета! ", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	public EuroHarmDAO getHarmDao() {
		return harmDao;
	}

	public void setHarmDao(EuroHarmDAO harmDao) {
		this.harmDao = harmDao;
	}

	public EuroHarm getHarm() {
		return harm;
	}

	public void setHarm(EuroHarm harm) {
		this.harm = harm;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

}
