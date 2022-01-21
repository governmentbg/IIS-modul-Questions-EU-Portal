package com.ib.euroact.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.system.BaseUserData;
import com.ib.system.db.dto.SystemClassif;


/**
 * Каша от наследени от старата система атрибути
 * 
 * @author krasi
 *
 */
public class UserData extends BaseUserData {

	

	static final Logger LOGGER = LoggerFactory.getLogger(UserData.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2989532705334584877L;
	
	

	
    /** Код на лице от регистъра на лицата"  */
  	private Long  idLice;
    
    /** Езика на който иска да работи потребителя */
	private Long lang = 1L;// Constants.CODE_DEFAULT_LANG;
	
	/** Списък на достъпните модули на ИИС */
	private List<SystemClassif> modules = new ArrayList<SystemClassif>();
	
	
	/** криптирана парола на потребитела*/
    private String criptPass;
    
    /** Iме на активния потребител  */
    private String userNames;
    
    
	
	public UserData() {
		super(-1,"SupeUser","The Great SuperUser");
		setIdLice(-1L);
		LOGGER.debug("Initialize UserData");
	}
	
	public UserData(Integer userId, String loginName, String liceNames) {
		super(userId,loginName, liceNames);
		
		LOGGER.debug("Initialize UserData");
	}
	

	public boolean hasAccess(Integer codeAccess) {
		return super.hasAccess(EuroConstants.CODE_SYSCLASS_MENU, codeAccess);
	}
	
	
	public String checkPageAccess(Integer codePage) {
		
		if(!hasAccess(codePage)) {
			return "index";
		} 

		return null;
	}
	
	public Long getIdLice() {
		return idLice;
	}
	

	public Long getLang() {
		return lang;
	}

	public void setIdLice(Long idLice) {
		this.idLice = idLice;
	}

	public List<SystemClassif> getModules() {
		return modules;
	}

	public void setModules(List<SystemClassif> modules) {
		this.modules = modules;
	}

	public String getCriptPass() {
		return criptPass;
	}

	public void setCriptPass(String criptPass) {
		this.criptPass = criptPass;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}



	
	
	
	
}
