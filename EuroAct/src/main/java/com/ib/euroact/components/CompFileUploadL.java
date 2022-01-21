package com.ib.euroact.components;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.FilesDAO;
import com.ib.euroact.db.dto.Files;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.BaseUserData;
import com.ib.system.SysConstants;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;
import com.ib.system.utils.X;


@FacesComponent(value="compFileUploadL")
public class CompFileUploadL<T> extends UINamingContainer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4374808903453485891L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompFileUploadL.class);
	private enum PropertyKeys {
		LSTOBJTMP,
		LISTDELOBJ,
		EDITINDEX
	}
	
	public  static final  String AUTOSAVE 		= "autoSave";
	public  static final  String LISTOBJ 		= "listObj";

	
//	public void initRenderComp() {
//		//Само при подаден параметър!!
//		
//	
//	}

	
	@SuppressWarnings("unchecked")
	protected void returnValues(List <T> tmpArr, Object hc, String list ){
		if(tmpArr == null){
			tmpArr = new ArrayList<>();
		}
		
		if(getEditIndex() == null){
			tmpArr.add((T) hc);			
		}
		
		setLstObjTmp(tmpArr);	
		ValueExpression expr = getValueExpression(list);
	    ELContext ctx = getFacesContext().getELContext();
	    if(expr != null){
	    	expr.setValue(ctx, tmpArr);
	    }	
	    
	    executeRemoteCmd();
	    initComp();	 
		    
	}
	
	/**
	 *  извиква remoteCommnad - ако има такава....
	 */
	private void executeRemoteCmd() {
		String remoteCommnad = (String) getAttributes().get("onComplete");
		if (remoteCommnad != null && !remoteCommnad.equals("")) {
			PrimeFaces.current().executeScript(remoteCommnad);
		}
	}
	
	
	
	public void initComp(){
		setLstObjTmp(null);
		setLstDelObjTmp(null);
	}
	

	public void listenerPrime(FileUploadEvent event) {
		UploadedFile item = event.getFile();
		String filename = item.getFileName();
		
		if(isISO88591Encoded(filename)){
			filename = new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		}
		
		X<Files> x = X.empty();
		
		Files files = new Files();
		files.setFilename(filename);
		files.setContentType(item.getContentType());
		files.setContent(item.getContent());
		
		Boolean autoSave = (Boolean) getAttributes().get(AUTOSAVE);  
		
		if (Boolean.TRUE.equals(autoSave)) {
			try {
				FilesDAO dao = new FilesDAO(getUserData());

				Integer idObj = (Integer) getAttributes().get("idObj");
				Integer codeObj = (Integer) getAttributes().get("codeObj");
				
				files.setCodeObject(codeObj);
				files.setIdObject(idObj);
				
				JPA.getUtil().runInTransaction(() -> x.set(dao.saveFile(files)));
				
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString("ui_beanMessages", "general.succesSaveFileMsg") );
				
			} catch (Exception e) {
				LOGGER.error("Exception: " + e.getMessage(), e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR,  "Грешка при прикачване на файл!", e.getMessage());
			}
			
		} else {
			x.set(files);
		}

		if (x.isPresent()) {
			returnValues (getLstObjTmp(), x.get(), LISTOBJ);	
		}
	}
    
    public void clear(){
    	setLstObjTmp(null);
    }
		
    
	/**
	 * Download selected file
	 *
	 * @param files
	 */
	public void download(Files files) {
		try {
			if (files.getId() != null){
			
				FilesDAO dao = new FilesDAO(getUserData());					
			
				try {
					files = dao.findById(files.getId());	
				} finally {
					JPA.getUtil().closeConnection();
				}
				
				if(files.getContent() == null){					
					files.setContent(new byte[0]);
				}
			}
			

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();

			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			String agent = request.getHeader("user-agent");

			String codedfilename = "";

			if (null != agent && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("Mozilla") && -1 != agent.indexOf("rv:11") || -1 != agent.indexOf("Edge"))) {
				codedfilename = URLEncoder.encode(files.getFilename(), "UTF8");
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
				codedfilename = MimeUtility.encodeText(files.getFilename(), "UTF8", "B");
			} else {
				codedfilename = URLEncoder.encode(files.getFilename(), "UTF8");
			}

			externalContext.setResponseHeader("Content-Type", "application/x-download");
			externalContext.setResponseHeader("Content-Length", files.getContent().length + "");
			externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + codedfilename + "\"");
			externalContext.getResponseOutputStream().write(files.getContent());

			facesContext.responseComplete();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	

	
    protected String getMessageResourceString(String bundleName,String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		String text = null;
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, bundleName);
		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = key;
		}
		return text;
	}
 

    public void actionDelete(Object key) {
		List<T> tmpArr = this.getLstObjTmp();
		if(key != null && tmpArr.contains(key)){
			tmpArr.remove(key);
			setLstObjTmp(tmpArr);
			
			ValueExpression expr = getValueExpression(LISTOBJ);
		    ELContext ctx = getFacesContext().getELContext();
		    if(expr != null){
		    	expr.setValue(ctx, tmpArr);
		    }

			if(((Files) key).getId() != null){
		    	if(getAttributes().get(AUTOSAVE) != null && (Boolean)getAttributes().get(AUTOSAVE)){
		    		removeFile((Files)key);
		    		executeRemoteCmd();
		    	} else if(this.getLstDelObjTmp() != null) {
			    	returnValues(this.getLstDelObjTmp(),key,"listDelObj");		
		    	}
		    }
			initComp();
		}
	}

    private void removeFile(Files file){
    	try {
						
			FilesDAO dao = new FilesDAO(getUserData());	
						
			JPA.getUtil().runInTransaction(() -> dao.deleteFile(file));
			

			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, getMessageResourceString("ui_beanMessages", "general.succesDeleteFileMsg") );
			
		} catch (BaseException e) {
			LOGGER.error( e.getMessage(), e);
		}
    }

	
   
	
	
	/**
	 * Само за wildfly за да не изкарва името на файла на маймуница
	 * @param text
	 * @return
	 */
	private boolean isISO88591Encoded(String text) {
	    String checked = "";
		
		checked = new String(text.getBytes( StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
		
	 //   boolean isEnc = checked.equals(text);
	    return checked.equals(text);

	}
	
	 
    public Date getToday(){
		return new Date();
	}
    
    public int getCodeDa() {
    	return SysConstants.CODE_ZNACHENIE_DA;
    }
    
//	@SuppressWarnings("unchecked")
	public List<SelectItem> fileTypes(){		
		return new ArrayList<>();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<T> getLstObjTmp() {	
		return (List<T>) getStateHelper().eval(PropertyKeys.LSTOBJTMP,getAttributes().get(LISTOBJ));		
	}
	
	public void setLstObjTmp(List<T> lstObjTmp) {
		getStateHelper().put(PropertyKeys.LSTOBJTMP, lstObjTmp);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getLstDelObjTmp() {	
		return (List<T>) getStateHelper().eval(PropertyKeys.LISTDELOBJ,getAttributes().get("listDelObj"));		
	}
	
	public void setLstDelObjTmp(List<T> lstObjTmp) {
		getStateHelper().put(PropertyKeys.LISTDELOBJ, lstObjTmp);
	}

	
	
	public void setEditIndex(Integer editIndex){
		getStateHelper().put(PropertyKeys.EDITINDEX, editIndex);
	}
	
	public Integer getEditIndex(){
		return (Integer) getStateHelper().eval(PropertyKeys.EDITINDEX,null);
	}
	
	
	
	/** @return the userData */
	private BaseUserData	userData	= null;
	private BaseUserData getUserData() {
		if (this.userData == null) {
			this.userData = (BaseUserData) JSFUtils.getManagedBean("userData");
		}
		return this.userData;
	}
	
	public Integer getLang() {
		return getUserData().getCurrentLang();
	}
	
	

	
}
