package com.ib.euroact.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.AdmUsersDAO;
import com.ib.euroact.db.dto.AdmUsers;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.euroact.system.UserData;
import com.ib.system.ActiveUser;
import com.ib.system.BaseUserData;
import com.ib.system.auth.DBCredential;
import com.ib.system.auth.EAuthCredential;
import com.ib.system.auth.IBIdentityStore;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.AuthenticationException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SysClassifUtils;


/**
 * Основния клас за логване на птребител!!! Цялата идея в в Java Securoty-to JAAS
 * 
 * see com.ib.cc.config.ApplicationConfig
 * 
 * @author krasi
 *
 */
@ApplicationScoped
public class EuroActIdentityStore extends IBIdentityStore {
	static final Logger LOGGER = LoggerFactory.getLogger(EuroActIdentityStore.class);
	
	SystemData systemData=null;
	@Inject
	private ServletContext servletContext;
	
	@Override
	protected Optional<BaseUserData> findUserDB(DBCredential credential) throws AuthenticationException {
		
		 	if (LOGGER.isDebugEnabled()) {
		 		LOGGER.debug("findUser(String userName=" + credential.getCaller() + ", String password=" + credential.getPasswordAsString()
		 				+" - start");
		 	}

		 	// Правим проверка за коректност на входните параметри !
		 	if (credential== null ||credential.getCaller() == null || credential.getPasswordAsString() == null)
		 		throw new AuthenticationException(AuthenticationException.CODE_USER_UNKNOWN,1);

		 	// Временна променлива, с която ще работим
		 	AdmUsers tempUser = null;

		 	String username = credential.getCaller();
			String password = credential.getPasswordAsString();
		 	
		 	
		 	AdmUsersDAO userDAO = new AdmUsersDAO(ActiveUser.DEFAULT);

		 	UserData userData=null ;
		 	try {
		 		
//		 		String isHasheActive = sd.getSettingsValue("isHashingActive");
//		 		LOGGER.info("isHasheActive="+isHasheActive);
		 		
		 		tempUser = userDAO.validateUser(username, password);
		 		
		 		if (tempUser != null) {
			
		 			userData=loadUserData(tempUser, null);
		 		}else {
		 			throw new AuthenticationException(AuthenticationException.CODE_USER_UNKNOWN,1);
		 		}
		 			
		 		
		 	} catch (DbErrorException e) {
		 		LOGGER.error(e.getMessage(), e);
		 		throw new AuthenticationException("Грешка при извличане на обект от БД", -1);
		 	} catch (AuthenticationException e) {
				LOGGER.error(e.getMessage(), e);
		 		throw e;			
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
		 		throw new AuthenticationException("Неочаквана грешка !", -1);
			}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("findUser(String userName=" + credential.getCaller() + ", String password=" + credential.getPasswordAsString()
				+" - end");
		 	}
		 	if (userData!=null) {
				return Optional.of(userData);
			}else {
				return Optional.empty();
			}

	 }

	@Override
	protected Optional<BaseUserData> findUserEAuth(EAuthCredential credential) throws AuthenticationException {
		EAuthCredential eAuthCredential = credential;
		try {

			AdmUsers admUser = JPA.getUtil().getEntityManager().find(AdmUsers.class, eAuthCredential.getUserId());

			return Optional.of(loadUserData(admUser, null));
		} catch (DbErrorException e) {
			throw new AuthenticationException(1, eAuthCredential.getUserId());
		}
	}

	
	public UserData loadUserData(AdmUsers user, Long lang) throws DbErrorException {
		
		if (user == null) {
			return null;
		}
		
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("loadUserData  - start");
		}
		
		AdmUsersDAO dao = new AdmUsersDAO(ActiveUser.DEFAULT);

		
		if (lang == null){
			lang = Long.valueOf(EuroConstants.CODE_DEFAULT_LANG);
		}
		
		String names = "";
		if (user.getIme() !=  null) {
			names += user.getIme();
		} 
		if (user.getPrezime() !=  null) {
			names += " " + user.getPrezime();
		} 
		if (user.getFamilia() !=  null) {
			names += " " + user.getFamilia();
		} 
		names = names.trim();
		
		UserData userData = new UserData(user.getId(), user.getLogin_name(), names);
		
		TreeSet<Integer> tmpRolesAsSet = dao.loadUserRolesNew(user.getId(), getSystemData());
		
		List<SystemClassif> modules = new ArrayList<SystemClassif>();
		
		
		

		/*************************************************
		 * 3.Сега ще добавим всички деца и всички родители на всеки избран код
		 */
		

		TreeSet<Integer> allRolesAsSet = new TreeSet<Integer>();
		List<SystemClassif> classif = getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_MENU, new Date(), lang.intValue());
		
		for (Integer role : tmpRolesAsSet) {
				
			if (allRolesAsSet.contains(role.intValue())) {
				continue;
			}
			
			allRolesAsSet.add(role.intValue());
						
			SystemClassif curItem = null;			
			for (SystemClassif tekItem : classif) {
				if (tekItem.getCode() == role.intValue()) {
					curItem = tekItem;
					break;
				}
			}
			
			if (curItem == null) {
				//Няма го в менюто - прескачаме
				continue;
			}
			
			List<SystemClassif> res = SysClassifUtils.getParents(classif, curItem);
			for (SystemClassif forAdd : res) {
				
				if (forAdd.getCodeParent() == 0) {
					if (!modules.contains(forAdd)) {
						modules.add(forAdd);
					}
				}
				
				allRolesAsSet.add(forAdd.getCode());
			}
			
			res = SysClassifUtils.getChildren(classif, role.intValue(), null);
			for (SystemClassif forAdd : res) {
				allRolesAsSet.add(forAdd.getCode());
			}
			
			
			
		}
		
		
		
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("----------------------------> Брой роли ОБЩО : " + allRolesAsSet.size() );
		}
		
		Map<Integer,Boolean> map = new HashMap<Integer,Boolean>();
		Map<Integer,Map<Integer,Boolean>> fullMap = new HashMap<Integer,Map<Integer,Boolean>>();
		for (Integer role : allRolesAsSet) {
			map.put(role, true);
		}
		
		fullMap.put(EuroConstants.CODE_SYSCLASS_MENU, map);
		
		userData.setAccessValues(fullMap);
		userData.setModules(modules);
		


		try {
			//Long idLice = user.getId_lice();
			
			
		} catch (Exception e) {
			LOGGER.error("loadUserData() : ", e);
			throw new DbErrorException("Грешка при извличане на данни за потребител от БД", e);

		}
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Found User: "+userData.getLiceNames() +" with id="+userData.getUserId());;
			LOGGER.debug("Roles:");
			for (Iterator<Integer> iterator = allRolesAsSet.iterator(); iterator
					.hasNext();) {
				Integer long1 =  iterator.next();
				LOGGER.debug("" + long1);
				
			}
			
		}
		
		userData.setUserNames(user.getLogin_name());
        userData.setCriptPass(user.getPass());
		
		
		return userData;
	}
	
	
	public SystemData getSystemData() {
		if (systemData==null) {
			systemData=(SystemData)servletContext.getAttribute("systemData");
		}
		return systemData;
	}
	
}

