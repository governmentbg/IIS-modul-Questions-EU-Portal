package com.ib.euroact.db.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.AdmUsers;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.EuroDAO;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.system.Constants;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.AuthenticationException;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.PasswordUtils;
import com.ib.system.utils.SearchUtils;




public class AdmUsersDAO extends EuroDAO<AdmUsers> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdmUsersDAO.class);
	
	public AdmUsersDAO(ActiveUser user){
		super(user);		
	}
	
	
	
//	/**Търси потребител по зададен логин и парола
//	 * @param login 
//	 * @param pass
//	 * @param active 1-активен, 2 - неактивен
//	 * @return NULL ако не е намерен такъв потребител
//	 * @throws DbErrorException 
//	 */
//	public AdmUsers findByLoginNameAndPass(String login, String pass,long active) throws DbErrorException {
//		AdmUsers user = null;
//		if (LOGGER.isDebugEnabled()){
//			LOGGER.debug("findByLoginNameAndPass(login="+login+",pass="+pass+",active="+active);
//		}
//		String sql="FROM AdmUsers WHERE login_name=:loginName "
//				+((pass!=null && !pass.trim().isEmpty())?" AND pass=:pass ":"")
//				+ " AND status=:active";
//		
//		try {
//			Query query = createQuery(sql);
//
//			query.setParameter("loginName", login);
//			if (pass!=null && !pass.trim().isEmpty()){
//				query.setParameter("pass", pass);
//			}
//			query.setParameter("active", active);
//			user=(AdmUsers) query.getSingleResult();
//
//		}catch (NoResultException e) {
//			
//		} catch (Exception e) {
//			LOGGER.error("Error loading user from DB", e);
//			throw new DbErrorException("Error loading user from DB ", e);
//		}
//		
//		return user;
//
//	}
	
	
	
	/** Meтод, който зарежда ролите на потребител
	 * @param idUser - системен идентификатор на потребител
	 * @return списък от роли
	 * @throws DbErrorException - грешка при работа с базата данни
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public ArrayList<Long> loadUserRoles(Integer idUser) throws DbErrorException {
		ArrayList<Long> roles = new ArrayList<Long>();
		try {
			
			Query q = JPA.getUtil().getEntityManager().createNativeQuery("select CODE_ROLE from ADM_USER_ROLES where ID_USER = :IDUSER");
			q.setParameter("IDUSER", idUser);
			List<Object> allroles = q.getResultList();
			for (Object role : allroles) {
				roles.add(SearchUtils.asLong(role));
			}
			
		} catch (Exception e) {
			LOGGER.error("Error loading user roles from DB", e);
			throw new DbErrorException("Error loading user roles from DB", e);
		}
		
		return roles;
	}
	
	
	/** Meтод, който зарежда ролите на потребител, идващи от групите, в които участва
	 * @param idUser - системен идентификатор на потребител
	 * @return списък от роли
	 * @throws DbErrorException - грешка при работа с базата данни
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public ArrayList<Long> loadUserGroupRoles(Integer idUser) throws DbErrorException {
		ArrayList<Long> roles = new ArrayList<Long>();
		try {
			
			Query q = JPA.getUtil().getEntityManager().createNativeQuery("select distinct ADM_GROUP_ROLES.CODE_ROLE from ADM_GROUP_ROLES join ADM_USERS_GROUP on ADM_GROUP_ROLES.ID_GROUP = ADM_USERS_GROUP.ID_GROUP  where ADM_USERS_GROUP.ID_USER = :IDUSER");
			q.setParameter("IDUSER", idUser);
			List<Object> allroles = q.getResultList();
			for (Object role : allroles) {
				roles.add(SearchUtils.asLong(role));
			}
			
		} catch (Exception e) {
			LOGGER.error("Error loading group roles from DB", e);
			throw new DbErrorException("Error loading group roles from DB", e);
		}
		
		return roles;
	}
	
	
	/** Meтод, който проверява участието на потребител в групата за селектирен контрол
	 * @param idUser - системен идентификатор на потребител
	 * @return true/false - участие в групата
	 * @throws DbErrorException - грешка при работа с базата данни
	 */
	
	/**
	 * 
	 * @param idLice
	 * @return
	 * @throws DbErrorException
	 */
	public AdmUsers findByIdLice(Long idLice) throws DbErrorException {
		AdmUsers user = null;
	
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM AdmUsers WHERE id_lice="+idLice);
		
				
		Query q = JPA.getUtil().getEntityManager().createQuery(sql.toString());
		ArrayList<AdmUsers> rez =(ArrayList<AdmUsers>) q.getResultList();
		
		if (rez != null && rez.size()>0) {
			user=rez.get(0);
		}
		
		
		return user;
		
	}
	
	
	/** Meтод, който зарежда ролите на потребител
	 * @param idUser - системен идентификатор на потребител
	 * @return списък от роли
	 * @throws DbErrorException - грешка при работа с базата данни
	 */
	@SuppressWarnings("unchecked")
	public TreeSet<Integer> loadUserRolesNew(Integer idUser, SystemData sd) throws DbErrorException {
		TreeSet<Integer> roles = new TreeSet<Integer>();
		try {
			
			String sql = "select CODE_ROLE from ADM_USER_ROLES where ID_USER = :IDUSER "
					+ " UNION "  
			        + " select ADM_GROUP_ROLES.CODE_ROLE from ADM_GROUP_ROLES join ADM_USERS_GROUP on ADM_GROUP_ROLES.ID_GROUP = ADM_USERS_GROUP.ID_GROUP  where ADM_USERS_GROUP.ID_USER = :IDUSER";
			
			Query q = JPA.getUtil().getEntityManager().createNativeQuery(sql);
			q.setParameter("IDUSER", idUser);
			List<Object> allroles = q.getResultList();
			for (Object role : allroles) {
				Integer roleInt = SearchUtils.asInteger(role);
				roles.add(roleInt);
				List<SystemClassif> children = sd.getChildren(EuroConstants.CODE_SYSCLASS_MENU, roleInt, new Date(), EuroConstants.CODE_DEFAULT_LANG);
				for (SystemClassif tek : children) {
					roles.add(tek.getCode());
				}
			}
			
			
			
		} catch (Exception e) {
			LOGGER.error("Error loading user roles from DB", e);
			throw new DbErrorException("Error loading user roles from DB", e);
		}
		
		return roles;
	}
	
	
	
	/**
	 * По данните прави проверка дали има такъв потребител и може ли да се логне в системата
	 *
	 * @param systemData
	 * @param username
	 * @param password
	 * @param externalMode              при <code>true</code> външна система, <code>false</code> вътрешна
	 * @param checkInternalExternalMode при <code>true</code> се прави проврка дали потреботеля има достъп до съответния вид
	 *                                  система
	 * @return
	 * @throws AuthenticationException
	 * @throws BaseException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public AdmUsers validateUser(String username, String password)
		throws NoSuchAlgorithmException, BaseException, InvalidKeySpecException {
		
		AdmUsers user = findByUsername(username);

		if (user == null) {
			throw new AuthenticationException(AuthenticationException.CODE_USER_UNKNOWN, null);
		}

		

		boolean valid = PasswordUtils.validatePasswordBase64(password, user.getPass());
		if (!valid) {			
			throw new AuthenticationException(AuthenticationException.CODE_WRONG_PASSWORD, user.getId());
		}

		if (user.getStatus() == null || user.getStatus().intValue() != Constants.CODE_ZNACHENIE_STATUS_ACTIVE) { // проверка на статус
			throw new AuthenticationException(AuthenticationException.CODE_UNAUTHORIZED_STATUS, user.getId());
		}

		

		//Map<Integer, Map<Integer, Boolean>> accessValues = findUserAccessMap(user.getId());
		//user.setAccessValues(accessValues); // задавам ги на ентитито, за да може после да ги дам на усердатата

		

		return user;
	}
	
	/**Търси потребител по зададен логин
	 * @param login - потребителско име	
	 * @return NULL ако не е намерен такъв потребител
	 * @throws DbErrorException 
	 */
	public AdmUsers findByUsername(String login) throws DbErrorException {
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("findByUsername(login="+login);
		}
		String sql="FROM AdmUsers WHERE login_name=:loginName ";
		
		try {
			Query query = createQuery(sql);

			query.setParameter("loginName", login);
			ArrayList<AdmUsers> result = (ArrayList<AdmUsers>) query.getResultList();
			
			if (result.size() > 0) {
				return result.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Error loading user from DB", e);
			throw new DbErrorException("Error loading user from DB ", e);
		}
		
		

	}
	
	
}
