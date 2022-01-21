package com.ib.euroact.beans;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActJournal;
import com.ib.euroact.system.EuroConstants;
import com.ib.indexui.system.IndexHttpSessionListener;
import com.ib.indexui.system.IndexLoginBean;
import com.ib.indexui.utils.ClientInfo;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.ActiveUser;
import com.ib.system.BaseUserData;
import com.ib.system.IBUserPrincipal;
import com.ib.system.auth.DBCredential;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.AuthenticationException;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.RestClientException;
import com.ib.system.utils.Base64;



@Named("loginBean")
@RequestScoped
public class LoginBean extends IndexLoginBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -401728067319558567L;
	static final Logger LOGGER = LoggerFactory.getLogger(LoginBean.class);
//	@ManagedProperty(value="#{userData}")
//	@Inject
//	private UserData cc;

	@Inject
	private SecurityContext securityContext;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ExternalContext externalContext;

	public LoginBean() {

	}

//	@Inject 
//	SystemData sd;

	@PostConstruct
	public void init() {
		//sistemId = Long.valueOf(71);
		//setUserUtils(new UserUtils());
		LOGGER.debug("initialize LoginBean");
		
		
		 if(JSFUtils.getRequestParameter("alg:fastlogin")!=null && JSFUtils.getRequestParameter("alg:fastlogin").equals("1")) {
	            
	        	
	        	setUsername(JSFUtils.getRequestParameter("alg:loginName"));
	        	
	            String pass = JSFUtils.getRequestParameter("alg:password");
	            try {
					pass= new String(Base64.decode(pass), "utf-8");
					
					 PrimeFaces.current().executeScript(" document.getElementById('lfrm:password').value='"+pass+"'");
			         PrimeFaces.current().executeScript(" gogoLogin_() ");
				} catch (UnsupportedEncodingException e) {
					//e.printStackTrace();
					LOGGER.debug("greshno dekodirane na parola" ,e);
				}
	           
	            
	            
	        }
		
	}

	
//	public String logMeIn() {
//		
//		LOGGER.debug("Try to logMeIn");
//
//		/******************** Check **********************************/
//		if (!isValidInput()) {
//			JSFUtils.addMessage("login-form:password", FacesMessage.SEVERITY_WARN,
//					getMessageResourceString(beanMessages, "Невалидни символи."));
//			return "";
//		}
//
//		
//		
//		
//		/********************* MAIN LOGIN ****************************/
//		
//		
//		
//		try {
//			AuthenticationStatus continueAuthentication = continueAuthentication();
//			switch (continueAuthentication) {
//			case SEND_CONTINUE:
//				LOGGER.debug("SEND_CONTINUE");
//				facesContext.responseComplete();
//				break;
//			case SEND_FAILURE:
//				LOGGER.debug("SEND_FAILURE");
//				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
//				JSFUtils.addGlobalMessage( FacesMessage.SEVERITY_WARN,
//						getMessageResourceString(beanMessages, "loginBean.invalidUserPass"));
//
//				
//				return "";
//
//			case SUCCESS:
//				LOGGER.debug("SUCCESS");
//				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login succeed", null));
//				
//				return "/pages/index?faces-redirect=true";
//
//			case NOT_DONE:
//				LOGGER.debug("NOT_DONE");
//
//			}
//		} catch (Exception e) {
//			LOGGER.error("-----------------------------",e);
//			JSFUtils.addMessage("login-form:login", FacesMessage.SEVERITY_ERROR,"SystemError",e.getMessage());
//			return "";
//		}
//
//		return "";
//	}

	private AuthenticationStatus continueAuthentication() {
		DBCredential credential = new DBCredential(getUsername(), getPassword());
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		AuthenticationParameters authParms = AuthenticationParameters.withParams().credential(credential);
		AuthenticationStatus authenticate = securityContext.authenticate(request,response, authParms);
		return authenticate;
	}

	



	public boolean hasMessages(String clientId) {
		return FacesContext.getCurrentInstance().getMessages(clientId).hasNext();
	}

	
	@Override
	protected BaseUserData login() {
		LOGGER.debug("Try to login");

	/******************** MAIN LOGIN ***************************/
		
			AuthenticationStatus continueAuthentication = continueAuthentication();
			switch (continueAuthentication) {
			case SEND_CONTINUE:
				LOGGER.debug("SEND_CONTINUE");
				facesContext.responseComplete();
				break;
//			case SEND_FAILURE:
//				LOGGER.debug("SEND_FAILURE");
//				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
//				JSFUtils.addGlobalMessage( FacesMessage.SEVERITY_WARN,
//						getMessageResourceString(beanMessages, "loginBean.invalidUserPass"));
//
//				
//				break;

			case SUCCESS:
				LOGGER.debug("SUCCESS");
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login succeed", null));
				try {
					this.externalContext.redirect(this.externalContext.getRequestContextPath() + "/pages/index.xhtml");
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}

				break;
				

			case NOT_DONE:
				LOGGER.debug("NOT_DONE");

			}
		

			Set<IBUserPrincipal> principals = this.securityContext.getPrincipalsByType(IBUserPrincipal.class);

			if (principals.isEmpty()) {
				return null; // явно не се е логнал успешно
			}
			return principals.iterator().next().getUserData(); // ето го усера
	}

	public String proba() {
		return "test";
	}
	
	
	/**
	 * Метод за запис в журнала на успешен логин.
	 *
	 * @param request
	 * @param userIP
	 * @param userData
	 * @see IndexHttpSessionListener
	 */
	@Override
	protected void journalLoginSuccess(HttpServletRequest request, String userIP, BaseUserData userData) {
		
		String sessionId = ((HttpSession) JSFUtils.getExternalContext().getSession(false)).getId();
		String clientBrowser = ClientInfo.getClientBrowser(request);
		String clientOS = ClientInfo.getClientOS(request);
		
		String identObject = "Username=" + userData.getLoginName() + "; IP=" + userIP + "; Browser=" + clientBrowser + "; OS=" + clientOS + ";</br>SESSID=" + sessionId;
		
		EuroActJournal journal = new EuroActJournal(new Long(userData.getUserId()), new Long(EuroConstants.CODE_DEIN_LOGIN), new Long(EuroConstants.CODE_ZNACHENIE_JOURNAL_USER), new Long(userData.getUserId()), identObject, null);
		saveAudit(journal);		
	}
	
	
	/**
	 * Метод за запис в журнала на неуспешен логин.
	 *
	 * @param errorMessage
	 * @param request
	 * @param userIP
	 * @param ae
	 */
	@Override
	protected void journalLoginFail(String errorMessage, HttpServletRequest request, String userIP, AuthenticationException ae) {
		String sessionId = ((HttpSession) JSFUtils.getExternalContext().getSession(false)).getId();
		String clientBrowser = ClientInfo.getClientBrowser(request);
		String clientOS = ClientInfo.getClientOS(request);

		String identObject = errorMessage + "</br>IP=" + userIP + "; Browser=" + clientBrowser + "; OS=" + clientOS + "; SESSID=" + sessionId;

		
	
		EuroActJournal journal = new EuroActJournal(new Long(ActiveUser.DEFAULT.getUserId()), new Long(EuroConstants.CODE_DEIN_LOGIN_FAILED), new Long(EuroConstants.CODE_ZNACHENIE_JOURNAL_USER), new Long(getUserData().getUserId()), identObject, null);
		saveAudit(journal);		
		
	}
	
	@Override
	public void journalLogout() throws ServletException, BaseException, RestClientException, IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();

		String userIP = ClientInfo.getClientIpAddr(request);
		String sessionId = ((HttpSession) JSFUtils.getExternalContext().getSession(false)).getId();
		String clientBrowser = ClientInfo.getClientBrowser(request);
		String clientOS = ClientInfo.getClientOS(request);

		String identObject = "Username=" + getUserData().getLoginName() + "; IP=" + userIP + "; Browser=" + clientBrowser + "; OS=" + clientOS + ";</br>SESSID=" + sessionId;

		EuroActJournal journal = new EuroActJournal(new Long(getUserData().getUserId()), new Long(EuroConstants.CODE_DEIN_LOGOUT), new Long(EuroConstants.CODE_ZNACHENIE_JOURNAL_USER), null, identObject, null);
		saveAudit(journal);	

		
	}
	
	
	
	
	private void saveAudit(EuroActJournal journal) {
		
		try {
			//В тази системе винаги имаме достъп до базата и не ползваме isDatabaseMode()				
			JPA.getUtil().runInTransaction(() -> JPA.getUtil().getEntityManager().persist(journal));	

		} catch (BaseException e) {
			LOGGER.error("Грешка при журналиране на login", e);
		}
		
	}
	
	
	

}
