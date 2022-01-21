package com.ib.euroact.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewConnDAO;
import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewConn;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.indexui.system.Constants;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;

/** */
@FacesComponent(value = "compActConn", createTag = true)
public class CompActConn extends UINamingContainer {
	
	private enum PropertyKeys {
		  CONACT, CONNLIST, CONNNEW, CONNEDIT, SCLIST, SHOWME, COLUMNTABLE, SHOWPANELDATAACT
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CompActConn.class);
	
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
	private EuroActNew actView;
	private EuroActNewConn tmpConnNew;
	
	//private List<ColumnModel> columns;
	
	//private List<SystemClassif> scList;
	
	/**
	 * Данни 
	 * @return
	 */
	public void init() { 		
		
		tmpAct = (EuroActNew) getAttributes().get("compAct"); 
		
		setCompAct(tmpAct);
		
	//	System.out.println("typeConnAct ---> " + getAttributes().get("typeConn"));
		
		setShowPanelDataActs(false); //po podrazbirane e skrit panelas dannite
		
		List<ColumnModel> columns = new ArrayList<>();  
		
		setCompConnList(new ArrayList<EuroActNewConn>());
		
		switch (getTypeConnAct().intValue()) {
		
			case 1:
				if(tmpAct.getSectionAB2()!=null) {
					setCompConnList(tmpAct.getSectionAB2());
				}
				try {
					setScList(getSystemData().getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB2_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date()));
				} catch (DbErrorException e) {
					LOGGER.error("Грешка при извличане на класификация към EuroConstants.DEF_AB2_VRAZKI акта на ЕС!", e);
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
				}
				
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.role"),"roleText1","150" ,"roleText1" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim"),"nameAct2","*" ,"nameAct2" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroActEdit.actUrl"),"url2","35" ,"" ,1));
				setShowPanelDataActs(true);
			break;
			case 2:
				if(tmpAct.getSectionAB3()!=null) {
					setCompConnList(tmpAct.getSectionAB3());
				}
				
				try {
					setScList(getSystemData().getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB3_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date()));
				} catch (DbErrorException e) {
					LOGGER.error("Грешка при извличане на класификация към EuroConstants.DEF_AB3_VRAZKI акта на ЕС!", e);
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
				}
				
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.role"),"roleText2","150" ,"roleText2" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim"),"nameAct1","*" ,"nameAct1",0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroActEdit.actUrl"),"url1","35" ,"" ,1));
				setShowPanelDataActs(true);
			break;
			case 3:
				if(tmpAct.getSectionAB4i()!=null) {
					setCompConnList(tmpAct.getSectionAB4i());
				}
				
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim"),"nameAct2","*" ,"nameAct2" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroActEdit.actUrl"),"url2","35" ,"" ,1));
				
				if(tmpAct.getIzpalnenieActYesNo().intValue() == EuroConstants.CODE_OTG_IMA_PRIETI) {
					setShowPanelDataActs(true);
				}
			break;
			case 4:
				if(tmpAct.getSectionAB4d()!=null) {
					setCompConnList(tmpAct.getSectionAB4d());
				}
				
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim"),"nameAct2","*" ,"nameAct2" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroActEdit.actUrl"),"url2","35" ,"" ,1));
				
				if(tmpAct.getDelegiraniActsYesNo().intValue() == EuroConstants.CODE_OTG_IMA_PRIETI) {
					setShowPanelDataActs(true);
				}
			break;
			case 5:
				if(tmpAct.getSectionG()!=null) {
					setCompConnList(tmpAct.getSectionG());
				}
				
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroAct.naim"),"nameAct2","*" ,"nameAct2" ,0));
				columns.add(new ColumnModel(IndexUIbean.getMessageResourceString(LABELS, "euroActEdit.actUrl"),"url2","35" ,"" ,1));
				
				if(tmpAct.getSectionGYesNo().intValue() == EuroConstants.CODE_ZNACHENIE_DA){
					setShowPanelDataActs(true);
				}
			break;

			default:
				setCompConnList(new ArrayList<EuroActNewConn>());
			
		
		}
		setColumns(columns);
		
		setShowMe(true);
		setErrMsg(null);
		
		setEditConn(new EditConn());
		setConnNew(new EuroActNewConn());
		
		LOGGER.debug("initAct");
		actView = new EuroActNew();
	}

	
	public void actionNewConn() {
		setEditConn(new EditConn());
		setConnNew(new EuroActNewConn());
	}
	
	
	private boolean checkData(EditConn data) {
		
		boolean save = true;
		
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = null;
		
		
		if (context != null && data != null) {
			clientId = this.getClientId(context);
		
			switch (getTypeConnAct().intValue()) {
			
				case 1:
				case 2:
					if (data.getRole() == null ) {
						JSFUtils.addMessage(clientId + ":actionSec", FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, MSGPLSINS ,IndexUIbean.getMessageResourceString(LABELS, "euroAct.role")));
						save = false;
						
					}
				break;
			}
			
			
			if(data.getActId()==null) {
				JSFUtils.addMessage(clientId + ":actForSec:аutoCompl", FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, MSGPLSINS ,IndexUIbean.getMessageResourceString(LABELS, "euroAct.selectAct")));
				save = false;
			}
			
		} else {
			
			 JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, "general.exception"));
			 save = false;
		}	
		
		return  save;
	}
	
	public void actionSaveConn() {
		
		
		
		EditConn connEdit = getEditConn();
		
		if(checkData(connEdit)) {
			
			tmpAct = getCompAct();
			tmpConnNew = getConnNew(); 
			
			EuroActNewDAO     actDAO  = new EuroActNewDAO(getUserData());
			EuroActNewConnDAO connDAO = new EuroActNewConnDAO(getUserData());
			
			tmpConnNew.setChanged(true);
			tmpConnNew.setEuroActNewId1(tmpAct.getId()); //във всички без ключ 2 там го подменям 
			
			try {
			
				switch (getTypeConnAct().intValue()) {
				
					case 1:
							
						tmpConnNew.setRoleAct2(getSystemData().getOtherRole(connEdit.getRole()));
						
						tmpConnNew.setRoleAct1(connEdit.getRole());
						tmpConnNew.setEuroActNewId2(connEdit.getActId());
						tmpConnNew.setNameAct2(connEdit.getNameAct());
						tmpConnNew.setNote(connEdit.getNote());
					break;
					case 2:
						tmpConnNew.setEuroActNewId2(tmpAct.getId());
						
						tmpConnNew.setRoleAct1(getSystemData().getOtherRole(connEdit.getRole()));
						
						tmpConnNew.setRoleAct2(connEdit.getRole()); 
						tmpConnNew.setEuroActNewId1(connEdit.getActId());
						tmpConnNew.setNameAct1(connEdit.getNameAct());
						tmpConnNew.setNote(connEdit.getNote());
					break;
					case 3:
						tmpConnNew.setRoleAct1(EuroConstants.VID_VRAZ_OSN_IZP); 
						tmpConnNew.setRoleAct2(getSystemData().getOtherRole(EuroConstants.VID_VRAZ_OSN_IZP));
						
						
						tmpConnNew.setEuroActNewId2(connEdit.getActId());
						tmpConnNew.setNameAct2(connEdit.getNameAct());
						tmpConnNew.setNote(connEdit.getNote());
					break;
					case 4:
						tmpConnNew.setRoleAct1(EuroConstants.VID_VRAZ_OSN_DEL); 
						tmpConnNew.setRoleAct2(getSystemData().getOtherRole(EuroConstants.VID_VRAZ_OSN_DEL));	
						
						tmpConnNew.setEuroActNewId2(connEdit.getActId());
						tmpConnNew.setNameAct2(connEdit.getNameAct());
						tmpConnNew.setNote(connEdit.getNote());
					break;
					case 5:
						tmpConnNew.setRoleAct1(EuroConstants.VID_VRAZ_OSN_G); 
						tmpConnNew.setRoleAct2(getSystemData().getOtherRole(EuroConstants.VID_VRAZ_OSN_G));
						
						tmpConnNew.setEuroActNewId2(connEdit.getActId());
						tmpConnNew.setNameAct2(connEdit.getNameAct());
						tmpConnNew.setNote(connEdit.getNote());
					break;
			
				
				}
			
			
				try {
					JPA.getUtil().runInTransaction(() -> {
					
						tmpConnNew = connDAO.save(tmpConnNew);	
						JPA.getUtil().flush();
						tmpAct = actDAO.save(tmpAct);
						 
					});
					
					//towa mi se iskashe da go izbergna i prezarejdaneto
					JPA.getUtil().runWithClose(() -> tmpAct = actDAO.findByIdFull(tmpAct.getId(), getSystemData()));

// така не върши работа защото няма разкодирани  транзиент полетата в обекта EuroActNewConn при редакция а при ново не са били и попълвани
//					switch (getTypeConnAct().intValue()) {	
//						case 1:
//							tmpAct.getSectionAB2().add(tmpConnNew);
//							setCompConnList(tmpAct.getSectionAB2());
//						break;
//						case 2:
//							tmpAct.getSectionAB3().add(tmpConnNew);
//							setCompConnList(tmpAct.getSectionAB3());
//						break;
//						case 3:
//							tmpAct.getSectionAB4i().add(tmpConnNew);
//							setCompConnList(tmpAct.getSectionAB4i());
//						break;
//						case 4:
//							tmpAct.getSectionAB4d().add(tmpConnNew);
//							setCompConnList(tmpAct.getSectionAB4d());
//						break;
//						case 5:
//							tmpAct.getSectionG().add(tmpConnNew);
//							setCompConnList(tmpAct.getSectionG());
//						break;
//					}
					
				} catch (BaseException e) {
					LOGGER.error("Грешка при запис секция"+getTypeConnAct().intValue()+" акта на ЕС!", e);
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
				}
				
				setCompAct(tmpAct);
				
				setConnNew(new EuroActNewConn());
				setEditConn(new EditConn());
				
				switch (getTypeConnAct().intValue()) {	//towa mi se iskashe da go izbergna i prezarejdaneto
					case 1:
						setCompConnList(tmpAct.getSectionAB2());
					break;
					case 2:
						setCompConnList(tmpAct.getSectionAB3());
					break;
					case 3:
						setCompConnList(tmpAct.getSectionAB4i());
					break;
					case 4:
						setCompConnList(tmpAct.getSectionAB4d());
					break;
					case 5:
						setCompConnList(tmpAct.getSectionG());
					break;
				}
				
				ValueExpression expr2 = getValueExpression("compAct");
				ELContext ctx2 = getFacesContext().getELContext();
				if (expr2 != null) {
					expr2.setValue(ctx2, tmpAct);
				}
				
			} catch (DbErrorException e) {
				LOGGER.error("Грешка при запис секция"+getTypeConnAct().intValue()+" акта на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
			}
		} else {
			PrimeFaces.current().executeScript("scrollToErrors()");
		}
	}
	
	public void actionEditConn(EuroActNewConn conn){
		
		setConnNew(conn);
		
		switch (getTypeConnAct().intValue()) {
		
			case 1:
				setEditConn(new EditConn(conn.getRoleAct1() ,conn.getEuroActNewId2() ,conn.getNameAct2() ,conn.getNote()));
			break;
			case 2:
				setEditConn(new EditConn(conn.getRoleAct2() ,conn.getEuroActNewId1() ,conn.getNameAct1() ,conn.getNote()));
			break;
			case 3:
				setEditConn(new EditConn(null ,conn.getEuroActNewId2() ,conn.getNameAct2() ,conn.getNote()));
			break;
			case 4:
				setEditConn(new EditConn(null ,conn.getEuroActNewId2() ,conn.getNameAct2() ,conn.getNote()));
			break;
			case 5:
				setEditConn(new EditConn(null ,conn.getEuroActNewId2() ,conn.getNameAct2() ,conn.getNote()));
			break;

		
		}
	
	}
	
	public void actionViewAct(EuroActNewConn conn){
				
		actView = new EuroActNew();
		
		try {
			
			switch (getTypeConnAct().intValue()) {
			
			case 1:
				setActView(new EuroActNewDAO(getUserData()).findById(conn.getEuroActNewId2()));				
			break;
			case 2:
				setActView(new EuroActNewDAO(getUserData()).findById(conn.getEuroActNewId1()));
			break;
			case 3:
				setActView(new EuroActNewDAO(getUserData()).findById(conn.getEuroActNewId2()));	
			break;
			case 4:
				setActView(new EuroActNewDAO(getUserData()).findById(conn.getEuroActNewId2()));	
			break;
			case 5:
				setActView(new EuroActNewDAO(getUserData()).findById(conn.getEuroActNewId2()));	
			break;		
		}
			

		
		} catch (DbErrorException e) {
			LOGGER.error("Грешка при търсене на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
		}	
	
	}
	
	public void actionDeleteConn(EuroActNewConn conn) {
		
		EuroActNewDAO     actDAO  = new EuroActNewDAO(getUserData());
		EuroActNewConnDAO connDAO = new EuroActNewConnDAO(getUserData());
		
		try {
			tmpAct = getCompAct();
			
			List<EuroActNewConn> tmpList = getCompConnList();
			tmpList.remove(conn);
			
			setCompConnList(tmpList);
			
			boolean saveAct=false;
			
			switch (getTypeConnAct().intValue()) {
			
				case 1:
					tmpAct.getSectionAB2().remove(conn);
				break;
				case 2:
					tmpAct.getSectionAB3().remove(conn);
				break;
				case 3:
					tmpAct.getSectionAB4i().remove(conn);
					
					if(tmpAct.getSectionAB4i().size()==0) {
						tmpAct.setIzpalnenieActYesNo(null); 
						saveAct=true;
					}
				break;
				case 4:
					tmpAct.getSectionAB4d().remove(conn);
					
					if(tmpAct.getSectionAB4d().size()==0) {
						tmpAct.setDelegiraniActsYesNo(null);
						saveAct=true;
					}
				break;
				case 5:
					tmpAct.getSectionG().remove(conn);
					
					if(tmpAct.getSectionG().size()==0) {
						tmpAct.setSectionGYesNo(Constants.CODE_ZNACHENIE_NE);
						saveAct=true;
					}
				break;
	
			}
		
			
			try {
				JPA.getUtil().begin();
				
				connDAO.delete(conn);
			
			
				if(saveAct) {
					actDAO.save(tmpAct);
				}
				
				JPA.getUtil().commit();
				
				setCompAct(tmpAct);
				setConnNew(new EuroActNewConn());
				setEditConn(new EditConn());
				
				ValueExpression expr2 = getValueExpression("compAct");
				ELContext ctx2 = getFacesContext().getELContext();
				if (expr2 != null) {
					expr2.setValue(ctx2, tmpAct);
				}
			} catch (Exception e) {
				JPA.getUtil().rollback();
				LOGGER.error("Грешка при изтриване в секция"+getTypeConnAct().intValue()+" акта на ЕС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
			} finally {
				JPA.getUtil().closeConnection();
			}	
			
			
		} catch (Exception e) {
			LOGGER.error("Грешка при изтриване в секция"+getTypeConnAct().intValue()+" акта на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, IndexUIbean.getMessageResourceString(UIBEANMESSAGES, ERRDATABASEMSG));
		}
	}
	
	
	public void actionShowPanel() {
		
		tmpAct = getCompAct();
		
		setShowPanelDataActs(false);
		
		switch (getTypeConnAct().intValue()) {
		
			case 3:
				if(tmpAct.getIzpalnenieActYesNo().intValue() == EuroConstants.CODE_OTG_IMA_PRIETI) {
					setShowPanelDataActs(true);
				}
			break;
			case 4:
				if(tmpAct.getDelegiraniActsYesNo().intValue() == EuroConstants.CODE_OTG_IMA_PRIETI) {
					setShowPanelDataActs(true);
				}
			break;
			case 5:
				if(tmpAct.getSectionGYesNo().intValue() == EuroConstants.CODE_ZNACHENIE_DA){
					setShowPanelDataActs(true);
				}
			break;
	
		
		}
	}
	/** @return */
	private Integer getTypeConnAct() {
		return  (Integer)getAttributes().get("typeConn");
	}
	
	
	public EuroActNew getCompAct() {
		return (EuroActNew) getStateHelper().eval(PropertyKeys.CONACT, null);
	}

	public void setCompAct(EuroActNew eau) {
		getStateHelper().put(PropertyKeys.CONACT, eau);
	}
	
	
	/** @return */
	public boolean isShowMe() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWME, false);
	}
	
	/** @param showMe */
	public void setShowMe(boolean showMe) {
		getStateHelper().put(PropertyKeys.SHOWME, showMe);
	}
	
	/** @return */
	public boolean isShowPanelDataActs() {
		return (Boolean) getStateHelper().eval(PropertyKeys.SHOWPANELDATAACT, false);
	}
	
	/** @param showMe */
	public void setShowPanelDataActs(boolean showPanelDataActs) {
		getStateHelper().put(PropertyKeys.SHOWPANELDATAACT, showPanelDataActs);
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
	public List<EuroActNewConn> getCompConnList() {
		return (List<EuroActNewConn>) getStateHelper().eval(PropertyKeys.CONNLIST, null);
		
	}


	public void setCompConnList(List<EuroActNewConn> compConnList) {
		getStateHelper().put(PropertyKeys.CONNLIST, compConnList);
		
	}


//	public EuroActNewConn getConnNew() {
//		return (EuroActNewConn) getStateHelper().eval(PropertyKeys.CONNNEW, null);
//	}
//
//
//	public void setConnNew(EuroActNewConn connNew) {
//		getStateHelper().put(PropertyKeys.CONNNEW, connNew);
//	}


	@SuppressWarnings("unchecked")
	public List<SystemClassif> getScList() {
		return (List<SystemClassif>) getStateHelper().eval(PropertyKeys.SCLIST, null);
	}


	public void setScList(List<SystemClassif> scList) {
		getStateHelper().put(PropertyKeys.SCLIST, scList);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ColumnModel> getColumns() {
		return (List<ColumnModel>) getStateHelper().eval(PropertyKeys.COLUMNTABLE, null);
	}

	public void setColumns(List<ColumnModel> columns) {
		getStateHelper().put(PropertyKeys.COLUMNTABLE, columns);
		
	}
	
	static public class ColumnModel implements Serializable {
		 
        private static final long serialVersionUID = 4314195886286451513L;
		private String  header;
        private String  property;
        private String  width;
        private String  sortColumn;
        // 0-текст, 1-link
        private Integer columnTypeValue;
        
        public ColumnModel(String header, String property ,String width, String sortColumn ,Integer columnTypeValue) {
            this.header = header;
            this.property = property;
            this.width = width;
            this.sortColumn = sortColumn;
            this.columnTypeValue = columnTypeValue;
            
        }
        
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}
		
		public String getSortColumn() {
			return sortColumn;
		}

		public void setSortColumn(String sortColumn) {
			this.sortColumn = sortColumn;
		}

		public Integer getColumnTypeValue() {
			return columnTypeValue;
		}

		public void setColumnTypeValue(Integer columnTypeValue) {
			this.columnTypeValue = columnTypeValue;
		}
	
		
    }
	
	public EuroActNewConn getConnNew() {
		return (EuroActNewConn) getStateHelper().eval(PropertyKeys.CONNNEW, null);
	}

	public void setConnNew(EuroActNewConn connNew) {
		getStateHelper().put(PropertyKeys.CONNNEW, connNew);
	}	
	
	public EditConn getEditConn() {
		return (EditConn) getStateHelper().eval(PropertyKeys.CONNEDIT, null);
	}

	public void setEditConn(EditConn connNew) {
		getStateHelper().put(PropertyKeys.CONNEDIT, connNew);
	}

	public EuroActNew getActView() {
		return actView;
	}


	public void setActView(EuroActNew actView) {
		this.actView = actView;
	}

	public class EditConn implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5018687184792949245L;
		
		private Integer role;
		private Integer actId;
		private String  nameAct;
		private String  note;
		
		public EditConn() {};
		
		public EditConn(Integer role ,Integer actId ,String  nameAct ,String  note) {
			this.role = role;
			this.actId = actId;
			this.nameAct = nameAct;
			this.note = note;
		}
		
		public Integer getRole() {
			return role;
		}
		
		public void setRole(Integer role) {
			this.role = role;
		}
		
		public Integer getActId() {
			return actId;
		}
		
		public void setActId(Integer actId) {
			this.actId = actId;
		}
		
		public String getNameAct() {
			return nameAct;
		}
		
		public void setNameAct(String nameAct) {
			this.nameAct = nameAct;
		}
		
		public String getNote() {
			return note;
		}
		
		public void setNote(String note) {
			this.note = note;
		}
		
	}
}