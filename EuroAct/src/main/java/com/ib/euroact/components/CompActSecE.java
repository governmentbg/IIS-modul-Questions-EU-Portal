package com.ib.euroact.components;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewSectionEDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewSectionE;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.utils.SearchUtils;

/** */
@FacesComponent(value = "compActSecE", createTag = true)
public class CompActSecE extends UINamingContainer {
	
	private enum PropertyKeys {
		ACT, SECLIST, SECENEW ,SHOWME ,INDEXEDIT
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CompActSecE.class);
	
	public static final String	UIBEANMESSAGES = "ui_beanMessages";
	public static final String	BEANMESSAGES = "beanMessages";
	public static final String  MSGPLSINS = "general.pleaseInsert";
	public static final String  ERRDATABASEMSG = "general.errDataBaseMsg";
	public static final String  SUCCESSAVEMSG = "general.succesSaveMsg";
	public static final String	OBJECTINUSE		 = "general.objectInUse";
	public static final String	LABELS = "labels";
	
	
	
	private String errMsg = null;
	private SystemData systemData = null; // може би ще ми трябва, за да извиквам метод за презареждане на класификацията след промяна или изтриване
	private UserData userData = null;
	
	private EuroActNew tmpAct;
	private EuroActNewSectionE tmpSecENew;
	
	
	/**
	 * Данни 
	 * @return
	 */
	public void init() {		
		
		tmpAct = (EuroActNew) getAttributes().get("act"); 
		
		setAct(tmpAct);
		
		if(tmpAct.getSectionE()==null) {
			setSecEList(new ArrayList<EuroActNewSectionE>());
		} else {
			setSecEList(tmpAct.getSectionE());
		}
		
		setShowMe(true);
		setErrMsg(null);
		
		
		actionNewSecE();
		
		LOGGER.debug("initSecE");
	}

	
	public void actionNewSecE() {
		setIndexEdit(-1);
		setSecENew(new EuroActNewSectionE());
	}
	
	
	public void actionSaveSecE() {
		
		boolean flagSave = true;
		tmpSecENew = getSecENew();
		
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = null;
		
		
		if (context != null && tmpSecENew != null) {
			clientId = this.getClientId(context);
			
			if (SearchUtils.isEmpty(tmpSecENew.getName())) {
				JSFUtils.addMessage(clientId + ":nameSecE", FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, MSGPLSINS ,IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim")));
				flagSave = false;
				PrimeFaces.current().executeScript("scrollToErrors()");
			}
			
		} else {
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, "general.exception"));
			flagSave = false;
		}
		
		if(flagSave) {
			try {
				
				tmpSecENew.setChanged(true);
				tmpSecENew.setEuroActNewId(getAct().getId());
				
				JPA.getUtil().runInTransaction(() -> tmpSecENew = new EuroActNewSectionEDAO(getUserData()).save(tmpSecENew));	
				
				List<EuroActNewSectionE> tmpList = getSecEList();
				if(tmpList==null) { //нищо че в инита го сетвам но пак връща нулл
					tmpList= new ArrayList<EuroActNewSectionE>();
				}
		//		System.out.println("indexEdit--> "+getIndexEdit());
				
				if(getIndexEdit()!=-1) {
					tmpList.set(getIndexEdit(), tmpSecENew);
				} else {
					tmpList.add(tmpSecENew); 
				}
				setSecEList(tmpList);
				
				actionNewSecE();
			} catch (BaseException e) {
				LOGGER.error("Грешка при запис на секция Е към акта на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
			}	
		}
	}
	
	public void actionEditSecE(EuroActNewSectionE conn ,int indx){
		setIndexEdit(indx);
		setSecENew(conn);
		
	}
	
	public void actionDeleteSecE(EuroActNewSectionE conn) {
		//TODO
		try {
			
			JPA.getUtil().begin();
			
			new EuroActNewSectionEDAO(getUserData()).delete(conn);
		
			JPA.getUtil().commit();
			
			List<EuroActNewSectionE> tmpList = getSecEList();
			tmpList.remove(conn);
			setSecEList(tmpList);
			
		} catch (Exception e) {
			JPA.getUtil().rollback();
			LOGGER.error("Грешка при изтриване на секция Е към акта на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
		} finally {
			JPA.getUtil().closeConnection();
		}	
			
		
	}
	
	
	public EuroActNew getAct() {
		return (EuroActNew) getStateHelper().eval(PropertyKeys.ACT, null);
	}

	public void setAct(EuroActNew eau) {
		getStateHelper().put(PropertyKeys.ACT, eau);
	}
	
	
	public EuroActNewSectionE getSecENew() {
		return (EuroActNewSectionE) getStateHelper().eval(PropertyKeys.SECENEW, null);
	}

	public void setSecENew(EuroActNewSectionE eau) {
		getStateHelper().put(PropertyKeys.SECENEW, eau);
	}
	
	/** @return */
	public boolean isShowMe() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWME, false);
	}
	
	/** @param showMe */
	public void setShowMe(boolean showMe) {
		getStateHelper().put(PropertyKeys.SHOWME, showMe);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	private SystemData getSystemData() {
		if (this.systemData == null) {
			this.systemData =  (SystemData) JSFUtils.getManagedBean("systemData");
		}
		return this.systemData;
	}
	
	/** @return the userData */
	private UserData getUserData() {
		if (this.userData == null) {
			this.userData = (UserData) JSFUtils.getManagedBean("userData");
		}
		return this.userData;
	}
	
	/** @return */
	public Integer getLang() {
		return getUserData().getCurrentLang();
	}
	
	@SuppressWarnings("unchecked")
	public List<EuroActNewSectionE> getSecEList() {
		return (List<EuroActNewSectionE>) getStateHelper().eval(PropertyKeys.SECLIST, null);
		
	}

	public void setSecEList(List<EuroActNewSectionE> secEList) {
		getStateHelper().put(PropertyKeys.SECLIST, secEList);
		
	}
	
	public EuroActNewSectionE getTmpSecENew() {
		if(tmpSecENew==null) { tmpSecENew= new EuroActNewSectionE();}
		return tmpSecENew;
	}

	public void setTmpSecENew(EuroActNewSectionE tmpSecENew) {
		//System.out.println("setTmpSecENew-=> "+tmpSecENew.getNote());
		this.tmpSecENew = tmpSecENew;
	}


	public int getIndexEdit() {
		return ((Integer) getStateHelper().eval(PropertyKeys.INDEXEDIT, false)).intValue();
	}

	public void setIndexEdit(int indexEdit) {
		getStateHelper().put(PropertyKeys.INDEXEDIT, indexEdit);
	}
	

}