package com.ib.euroact.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.system.EuroConstants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.system.exceptions.DbErrorException;


@Named
@ViewScoped
public class TestDesi extends IndexUIbean implements Serializable {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDesi.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 4985013225316883670L;
	
	private Integer codeActSrch;
	private String txtActSrch;
	private Integer codeAct;
	private String actName;
	private Integer codeActEdit;
	private String txtActEdit;
	private Integer codeActSrch1;
	private String txtActSrch1;
	private Integer codeAct1;
	private String actName1;

	/**
	 *прехвърля избраното значение в друго поле и зачиства полето за търсене
	 */
	public void actionSelectAct() {
		codeAct = codeActSrch; 
		try {
			actName = getSystemData().decodeItem(EuroConstants.CODE_SYSCLASS_EURO_ACT, codeActSrch, getUserData().getCurrentLang(), new Date());
		
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		codeActSrch = null;
	}
	
	/**
	 *прехвърля избраното значение в друго поле и зачиства полето за търсене
	 */
	public void actionSelectAct1() {
		codeAct1 = codeActSrch1; 
		try {
			actName1 = getSystemData().decodeItem(EuroConstants.CODE_SYSCLASS_EURO_ACT, codeActSrch1, getUserData().getCurrentLang(), new Date());
		
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		codeActSrch1 = null;
	}
	
	/**
	 *прехвърля избраното значение в друго поле и зачиства полето за търсене
	 */
	public void actionClearAct() {
		codeAct = null;
		actName = null;
	}
	
	/**
	 *прехвърля избраното значение в друго поле и зачиства полето за търсене
	 */
	public void actionClearAct1() {
		codeAct1 = null;
		actName1 = null;
	}


	public Integer getCodeActSrch() {
		return codeActSrch;
	}

	public void setCodeActSrch(Integer codeActSrch) {
		this.codeActSrch = codeActSrch;
	}

	public String getTxtActSrch() {
		return txtActSrch;
	}

	public void setTxtActSrch(String txtActSrch) {
		this.txtActSrch = txtActSrch;
	}

	public Integer getCodeAct() {
		return codeAct;
	}

	public void setCodeAct(Integer codeAct) {
		this.codeAct = codeAct;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public Integer getCodeActEdit() {
		return codeActEdit;
	}

	public void setCodeActEdit(Integer codeActEdit) {
		this.codeActEdit = codeActEdit;
	}

	public String getTxtActEdit() {
		return txtActEdit;
	}

	public void setTxtActEdit(String txtActEdit) {
		this.txtActEdit = txtActEdit;
	}

	public Integer getCodeActSrch1() {
		return codeActSrch1;
	}

	public void setCodeActSrch1(Integer codeActSrch1) {
		this.codeActSrch1 = codeActSrch1;
	}

	public String getTxtActSrch1() {
		return txtActSrch1;
	}

	public void setTxtActSrch1(String txtActSrch1) {
		this.txtActSrch1 = txtActSrch1;
	}

	public Integer getCodeAct1() {
		return codeAct1;
	}

	public void setCodeAct1(Integer codeAct1) {
		this.codeAct1 = codeAct1;
	}

	public String getActName1() {
		return actName1;
	}

	public void setActName1(String actName1) {
		this.actName1 = actName1;
	}
	
	

}