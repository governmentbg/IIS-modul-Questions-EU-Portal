package com.ib.euroact.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.PointProgramaDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.navigation.Navigation;
import com.ib.indexui.navigation.NavigationData;
import com.ib.indexui.navigation.NavigationDataHolder;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;

@Named
@ViewScoped
public class ProgramEdit extends IndexUIbean implements Serializable {

	/**
	 *  Въвеждане / актуализация на програма на НС
	 */
	private static final long serialVersionUID = -2456957751192428546L;
	static final Logger LOGGER = LoggerFactory.getLogger(ProgramEdit.class);
	
	private static final String FORM_PROGRAM = "formProgram";
	
	private Date decodeDate;
	private SystemData sd;
	
	private transient ProgramaDAO progDAO;
	private transient PointProgramaDAO pointDAO;
	
	private Programa program;
	private PointPrograma point;
	private List<PointPrograma> pointsList;
	
	/** 
	 * 
	 * 
	 **/
	@PostConstruct
	public void initData() {
		
		LOGGER.debug("PostConstruct!!!");
		
		try {
		
			this.decodeDate = new Date();
			this.sd = (SystemData) getSystemData();
			this.progDAO = new ProgramaDAO(getUserData());
			this.pointDAO = new PointProgramaDAO(getUserData());
			this.program = new Programa();
			this.point = new PointPrograma();
			this.pointsList = new ArrayList<>();
			
			if (JSFUtils.getRequestParameter("idObj") != null && !"".equals(JSFUtils.getRequestParameter("idObj"))) {

				Integer idObj = Integer.valueOf(JSFUtils.getRequestParameter("idObj"));

				if (idObj != null) {
					
					JPA.getUtil().runWithClose(() -> { 
						
						this.program = this.progDAO.findById(idObj);
						this.pointsList = this.progDAO.findProgramaPoints(idObj);
					}); 
					
				}
				
				if (JSFUtils.getRequestParameter("view") != null && !"".equals(JSFUtils.getRequestParameter("view"))) {
					
					for (PointPrograma point : pointsList) {
						
						Integer pored = point.getPored();
						String text = point.getTekst();
						
						List<Object[]> actsList = this.pointDAO.filterActs(SearchUtils.asLong(this.program.getGodina()), SearchUtils.asLong(pored));
						
						if (actsList.size() == 0) {
							continue;
						}
						
						Object[] ea = actsList.get(0);
						
						String sign = (String) ea[4];
						BigDecimal idAct = (BigDecimal) ea[0];
						String link = "<a href='http://iis.parliament.bg/Euro/actECView.faces?idObj="+SearchUtils.asLong(idAct)+"' target='_blank'>"+sign+"</a>";
						
						for (Object obj : actsList) {
							
							ea = (Object[]) obj;
							idAct = (BigDecimal) ea[0];
							sign = "; " + (String) ea[4];
							link += "<a href='http://iis.parliament.bg/Euro/actECView.faces?idObj="+SearchUtils.asLong(idAct)+"' target='_blank'>"+sign+"</a>";
						}
						
						text += " - " + link;
						point.setTekst(text);
					}
					
					if (this.pointsList != null) {
						doSort(this.pointsList); 
					}					
				}				
			}
		
		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане данните на програма на НС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	private boolean checkData() {
		
		boolean save = false;		

		if(this.program.getGodina() == null) {
			JSFUtils.addMessage(FORM_PROGRAM + ":godina", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "programEdit.godina")));
			save = true;
		
		} else {	
			
			if (actionCheckForExistProgram()) {
				save = true;				
			}						
		}
		
		return save;		
	}
	
	public void actionSave() {
		
		if(checkData()) {
			return;
		}
		
		try {			
					
			JPA.getUtil().runInTransaction(() -> this.program = this.progDAO.save(this.program));
		
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при запис на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}
	}
	
	public void actionDelete() {
		
		try {
			
			JPA.getUtil().runInTransaction(() -> {
				
				if (this.pointsList.size() > 0) {
					for (PointPrograma point : pointsList) {
						this.pointDAO.delete(point);
					}
				}				
			
				this.progDAO.delete(this.program);
			
			});	

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));			
			
			this.program = new Programa();
			
			Navigation navHolder = new Navigation();			
		    int i = navHolder.getNavPath().size();	
		   
		    NavigationDataHolder dataHoslder = (NavigationDataHolder) JSFUtils.getManagedBean("navigationSessionDataHolder");
		    Stack<NavigationData> stackPath = dataHoslder.getPageList();
		    NavigationData nd = stackPath.get(i-2);
		    Map<String, Object> mapV = nd.getViewMap();
		    
		    ProgramsList programsList = (ProgramsList) mapV.get("programsList");		   
		    programsList.actionSearch();
		    
		    navHolder.goBack();
		    
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при изтриване на програма на НС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}		
		
	}	
	
	public boolean actionCheckForExistProgram() {
		
		if (this.program.getGodina() != null) {			
			
			try {
				
				boolean existProg = this.progDAO.findDubl(this.program);
				
				if (existProg) {
					JSFUtils.addMessage(FORM_PROGRAM + ":godina", FacesMessage.SEVERITY_ERROR, getMessageResourceString (beanMessages, "programEdit.existGodina"));
					return true;
				}
			
			} catch (DbErrorException e) {
				LOGGER.error("Грешка при зареждане данните на програми на НС!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
			}
		}
		
		return false; 		
	}
	
	public void actionEditPoint(Integer idObj) {

		try {

			if (idObj != null) {				
				JPA.getUtil().runWithClose(() -> this.point = this.pointDAO.findById(idObj));
			}

		} catch (BaseException e) {
			LOGGER.error("Грешка при зареждане данните от точка към програма на НС! ", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public boolean actionCheckForPoredenNum() {
		
		if (this.point.getPored() != null) {

			boolean existPorNum = false;

			if (this.pointsList.size() > 0) {
				for (PointPrograma point : pointsList) {
					if (this.point.getId() == null) {
						if (this.point.getPored().equals(point.getPored())) {
							existPorNum = true;
							break;
						}
					} else if (!this.point.getId().equals(point.getId())){
						if (this.point.getPored().equals(point.getPored())) {
							existPorNum = true;
							break;
						}
					}
				}
			}
			
			if (existPorNum) {
				JSFUtils.addMessage(FORM_PROGRAM + ":porNum", FacesMessage.SEVERITY_ERROR, getMessageResourceString(beanMessages, "programEdit.existPoredNum"));
				PrimeFaces.current().executeScript("scrollToErrors()");
				return true;
			}
		}
		 return false;
	}
	
	private boolean checkDataPoint() {
		
		boolean save = false;		

		if(this.point.getPored() == null) {
			JSFUtils.addMessage(FORM_PROGRAM + ":porNum", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "programEdit.poredNum")));
			save = true;
			PrimeFaces.current().executeScript("scrollToErrors()");
		
		} else {
			
			if(actionCheckForPoredenNum()) {
				save = true;
				PrimeFaces.current().executeScript("scrollToErrors()");
			}
		}
		
		if(SearchUtils.isEmpty(this.point.getTekst())) {
			JSFUtils.addMessage(FORM_PROGRAM + ":text", FacesMessage.SEVERITY_ERROR, getMessageResourceString (UI_beanMessages, MSGPLSINS, getMessageResourceString(LABELS, "programEdit.text")));
			save = true;
			PrimeFaces.current().executeScript("scrollToErrors()");
		}
		
		return save;		
	}
	
	public void actionSavePoint() {
		
		if(checkDataPoint()) {
			return;
		}
		
		try {	
			
			this.point.setIdPrograma(this.program.getId()); 
			
			JPA.getUtil().runInTransaction(() -> this.point = this.pointDAO.save(this.point));
						
//			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, SUCCESSAVEMSG));
//			
//			PrimeFaces.current().executeScript("scrollToErrors()");
			
			this.point = new PointPrograma();	
			
			JPA.getUtil().runWithClose(() -> this.pointsList = this.progDAO.findProgramaPoints(this.program.getId()));		
			
		} catch (BaseException e) {			
			LOGGER.error("Грешка при запис на точка към програма на НС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}		
		
	}	
	
	public void actionNewPoint() {
		
		if (this.point.getId() == null) {
			this.point = new PointPrograma();
		}		
	}
	
	public void actionNewPointPlus() {
		
		this.point = new PointPrograma();				
	}
	
	public void actionDeletePoint(PointPrograma point) {
		
		try {	
				
			JPA.getUtil().runInTransaction(() -> this.pointDAO.delete(point));
			
//			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString(UI_beanMessages, "general.successDeleteMsg"));
//			
//			PrimeFaces.current().executeScript("scrollToErrors()");
			
			this.point = new PointPrograma();	
			
			JPA.getUtil().runWithClose(() -> this.pointsList = this.progDAO.findProgramaPoints(this.program.getId()));		
		
		} catch (BaseException e) {			
			LOGGER.error("Грешка при изтриване на връзка към акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}	
	}
	
	protected void doSort(List<PointPrograma> ppAl) { 
        
		Comparator<Object> c = new Comparator<Object>() {
            
			public int compare (Object o1, Object o2)
            {
                Integer p1 = ((PointPrograma) o1).getPored();
                Integer p2 = ((PointPrograma) o2).getPored();
                if((null == p1) ||
                   (null == p2) )
                {
                    return 0;
                }
                
                int iR = p1.compareTo(p2);
                return iR;
                
            }
        };
        
        Collections.sort(ppAl, c);
    }
	
	public String actionGotoView() {
		
		return "programView.xhtml?faces-redirect=true&idObj=" + this.program.getId() + "&view=1";
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

	public Programa getProgram() {
		return program;
	}

	public void setProgram(Programa program) {
		this.program = program;
	}

	public PointPrograma getPoint() {
		return point;
	}

	public void setPoint(PointPrograma point) {
		this.point = point;
	}

	public List<PointPrograma> getPointsList() {
		return pointsList;
	}

	public void setPointsList(List<PointPrograma> pointsList) {
		this.pointsList = pointsList;
	}
	
}
