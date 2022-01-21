package com.ib.euroact.eauth;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActJournal;
import com.ib.system.BaseSystemData;
import com.ib.system.auth.EAuthCredential;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemJournal;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.saml.SamlConstants;

@Path("/eauth")
public class SamlRestEndpoint extends com.ib.system.rest.SamlRestEndpoint {
	
	   @Inject
	    private ServletContext context;

	    @Inject
	    private SecurityContext securityContext;

	    @Context
	    private HttpServletRequest request;

	    @Context
	    private HttpServletResponse response;

	    @Context
	    private UriInfo info;
	    
	    static final Logger LOGGER = LoggerFactory.getLogger(SamlRestEndpoint.class);

	    @Override
	    protected Properties loadSamlProviderPropeties() throws IllegalAccessException {
	        Properties properties = new Properties();
	        for(Field field : SamlConstants.class.getFields()) {
	            String fieldValue = field.get(null).toString();// static access
	            try {
	                String settingValue = getSystemData().getSettingsValue(fieldValue);
	                if(settingValue != null) {
	                    properties.put(fieldValue, settingValue);
	                }
	            } catch (DbErrorException ignored) {}

	        }
	        return properties;
	    }

	    @Override
	    public void logOperation(Long codeAction, Long codeObject, Long idObject, Long idUser, byte[] objectContent, String objectXml) {
	    	EuroActJournal systemJournal = new EuroActJournal();
	        if(codeAction != null) {
	            systemJournal.setCodeAction(codeAction);
	        }
	        if(codeObject != null) {
	            systemJournal.setCodeObject(codeObject);
	        }
	        systemJournal.setDateAction(new Date());
	        if(idObject != null) {
	            systemJournal.setIdObject(idObject);
	        }
	        if(idUser != null) {
	            systemJournal.setIdUser(idUser);
	        }
	        //systemJournal.setObjectContent(objectContent);
	        //systemJournal.setObjectXml(objectXml);

	        EntityManager entityManager = JPA.getUtil().getEntityManager();
	        try {
	            JPA.getUtil().begin();
	            entityManager.persist(systemJournal);
	            JPA.getUtil().commit();
	        } catch (Exception e) {
	        	LOGGER.error("Грешка при запис за логване през eАуторизация ! ", e);
	            JPA.getUtil().rollback();
	            throw new RuntimeException("Грешка при запис за логване през eАуторизация !");
	        }finally {
	        	JPA.getUtil().closeConnection();
	        }
	        
	    }

	    @Override
	    protected BaseSystemData getSystemData() {
	        return (BaseSystemData) this.context.getAttribute("systemData");
	    }

	    protected SecurityContext getSecurityContext(){
	        return securityContext;
	    }

	    @Override
	    protected HttpServletRequest getHttpServletRequest() {
	        return request;
	    }

	    @Override
	    protected HttpServletResponse getHttpServletResponse() {
	        return response;
	    }

	    /*
	     * Method had to be overridden due to different column names into adm_user table
	     */
	    @Override
	    protected Credential findUserByEmail(EntityManager manager, String email) throws DbErrorException {
	        Query q = manager.createQuery("select u.id from AdmUsers u where u.email=?1").setParameter(1, email);
	        Number userIdNumber = (Number) q.getSingleResult();
	        if(userIdNumber != null) {
	            return new EAuthCredential(userIdNumber.intValue());
	        }else{
	            throw new DbErrorException("Няма намерен потребител!");
	        }
	    }

	    @Override
	    protected String getSuccessPage() {
	        return "/pages/index.xhtml";
	    }

	    @Override
	    protected String getErrorPage() {
	        return "/pages/login.xhtml";
	    }
}
