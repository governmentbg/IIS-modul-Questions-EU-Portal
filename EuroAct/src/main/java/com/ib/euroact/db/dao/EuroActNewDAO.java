package com.ib.euroact.db.dao;

import static com.ib.system.utils.SearchUtils.asDate;
import static com.ib.system.utils.SearchUtils.asInteger;
import static com.ib.system.utils.SearchUtils.asString;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewConn;
import com.ib.euroact.db.dto.EuroActNewSectionE;
import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.EuroDAO;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.exceptions.ObjectInUseException;
import com.ib.system.utils.SearchUtils;

public class EuroActNewDAO extends EuroDAO<EuroActNew> {
//alabala
	private static final Logger LOGGER = LoggerFactory.getLogger(EuroActNewDAO.class);
	
	
	
	public EuroActNewDAO(ActiveUser user){
		super(user);		
	}
	
	public void recognizeNumber(EuroActNew dosie) throws InvalidParameterException {
		
		Integer nomerAct = null;
		Integer yearAct = null;		
		
		if (dosie.getRnFull() == null) {
			return;
		}
		 
		ArrayList<String> splitted = getNumbersFromString(dosie.getRnFull());
		if (splitted.size() != 2) {
			LOGGER.error("НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getRnFull() + " --> (" + splitted.size() + ")");
			throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getRnFull() + "  (Номерът трябва да съдържа номер и година разделени с '/')");
		}
		
		String s1 = splitted.get(0);
		String s2 = splitted.get(1);
		int num1 = Integer.parseInt(s1);
		int num2 = Integer.parseInt(s2);
		
		if (s1.length() != 2 && s1.length() != 4 ) {
			//Сигурни сме че не е година
			nomerAct = num1;
			yearAct = num2;
		}else {		
			if (s2.length() != 2 && s2.length() != 4 ) {
				//Сигурни сме че не е година
				nomerAct = num2;
				yearAct = num1;
			}
		}		
		
		if (yearAct == null) {
			//Почваме анализи
			
			if (num1 > 1990 && num1 < 2021) {
				//Вероятно е година
				if (num2 > 1990 && num2 < 2021) {
					// и второто изглежда като година - не знам какво правим
					LOGGER.error("НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ ГОДИНАТА В НОМЕРА : " + dosie.getRnFull());
					throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ ГОДИНАТА В НОМЕРА: " + dosie.getRnFull() + ". Моля, определете ги ръчно!");
				}else {
					nomerAct = num2;
					yearAct = num1;
				}
			}else {
				if (num2 > 1990 && num2 < 2021) {					
						nomerAct = num1;
						yearAct = num2;					
				}else {
					//и двете не мязат на година с 4 цифри - трябва да търсим такива с 98-ма например
					
				}
			}
		}
		
		if (yearAct == null) {
			//Ако годината все още е null
			//имаме просто 2 числа
			if (s1.length() == 2 && s2.length() != 2) {
				yearAct = num1;
				nomerAct = num2;
			}
			
			if (s2.length() == 2 && s1.length() != 2) {
				yearAct = num2;
				nomerAct = num1;
			}
			
			yearAct = num1;
			nomerAct = num2;
		}
		
		
		//Анализ на годината
		if (yearAct < 100 && yearAct > 50) {
			yearAct += 1900;
		}else {
			if (yearAct < 22) {
				yearAct += 2000;
			}
		}			
		
		dosie.setGodina(yearAct);
		dosie.setRn(nomerAct);
	}
	
	
	private  ArrayList<String> getNumbersFromString(String tekst){
		String numbers = "0123456789";
		boolean hasQuote = false;
		
		ArrayList<String> foundStrings = new ArrayList<String>();
		
		if (tekst == null) {
			return foundStrings;
		}else {
			//Това е когато завършва на цифра да не се налага след цикъла да правя анализ ;)
			tekst += ";"; 
		}
		
		
		String num = "";
		for(int i = 0; i < tekst.length(); i++) {
			
			if ("\"".equals(""+tekst.charAt(i))) {
				hasQuote = !hasQuote;
				num = "";
				continue;
				
			}
			
			if (!hasQuote) {
				String tek = ""+tekst.charAt(i);
			    if (numbers.contains(tek)) {
			    	num += tek; 
			    }else {
			    	if (num.length() > 0) {
			    		//System.out.println("Adding " + num);
			    		foundStrings.add(num);
			    		num = "";				    		
			    	}
			    }
			}
		}
		
		return foundStrings;		
		
	}
	
	public Integer findIdActByVidRnAndGodina(Integer vid, Integer rn, Integer godina) throws DbErrorException {
		
		try {
			
			StringBuilder sql = new StringBuilder();
			sql.append(" select ID");
			sql.append(" from EURO_ACT_NEW ");
			sql.append(" where VID_ACT = :vid  and RN = :rn  and GODINA = :godina ");

			Query query = createNativeQuery(sql.toString())
				.setParameter("vid", vid)
				.setParameter("rn", rn)
				.setParameter("godina", godina);			

			@SuppressWarnings("unchecked")
			List<Integer> result = query.getResultList();
			
			if(result != null && !result.isEmpty()) {
				return asInteger(result.get(0));
			}else {
				return 0;
			}			
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на ид на акт по вид, номер и година!", e);
		}
	
	}	
	
	@SuppressWarnings("unchecked")
	public Integer findIdActByNomer(String rnFull, Integer rn, Integer year, Integer vid, String url) throws DbErrorException {
		
		List<Integer> result = null;
		try {
			if (rn != null ) {
				StringBuilder sql = new StringBuilder();
				sql.append(" select ID");
				sql.append(" from EURO_ACT_NEW ");
				sql.append(" where RN = :rn and GODINA = :GOD and  VID_ACT = :VID");
				if (url != null) {
					sql.append(" and url = :URL");
				}
	
				Query query = createNativeQuery(sql.toString())				
				.setParameter("rn", rn)
				.setParameter("GOD", year)
				.setParameter("VID", vid);
				if (url != null) {
					query.setParameter("URL", url);
				}
								
				result = query.getResultList();
			}else {
				StringBuilder sql = new StringBuilder();
				sql.append(" select ID");
				sql.append(" from EURO_ACT_NEW ");
				sql.append(" where RN_FULL = :rn and  VID_ACT = :VID");
				if (url != null) {
					sql.append(" and url = :URL");
				}
	
				Query query = createNativeQuery(sql.toString())				
				.setParameter("rn", rnFull)				
				.setParameter("VID", vid);
				if (url != null) {
					query.setParameter("URL", url);
				}
								
				result = query.getResultList();
			}
			
			if(result != null && !result.isEmpty()) {
				return asInteger(result.get(0));
			}else {
				return 0;
			}			
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на ид на акт по вид, номер и година!", e);
		}
	
	}	
	
	public Integer findIdActByUpperName(String name) throws DbErrorException {
		
		try {
			
			StringBuilder sql = new StringBuilder();
			sql.append(" select ID");
			sql.append(" from EURO_ACT_NEW ");
			sql.append(" where upper(ime) = :UN");

			Query query = createNativeQuery(sql.toString())				
			.setParameter("UN", name.toUpperCase());
							

			@SuppressWarnings("unchecked")
			List<Integer> result = query.getResultList();
			
			if(result != null && !result.isEmpty()) {
				return asInteger(result.get(0));
			}else {
				return 0;
			}			
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на ид на акт по вид, номер и година!", e);
		}
	
	}	
	
	@SuppressWarnings("unchecked")
	public EuroActNew findByIdFull(Integer id, SystemData sd) throws DbErrorException {
		
		EuroActNew act = findById(id);
		
		
		String sqlString = "SELECT " + 
				"	eac.ID, " + 
				"	eac.EURO_ACT_NEW_ID1, " + 
				"	eac.ROLE_ACT1, " + 
				"	eac.EURO_ACT_NEW_ID2, " + 
				"	eac.ROLE_ACT2, " + 
				"	eac.NOTE, " + 
				"	eac.USER_REG, " + 
				"	eac.DATE_REG, " + 
				"	eac.USER_LAST_MOD, " + 
				"	eac.DATE_LAST_MOD, " + 
				"	ea1.IME IME1, " + 
				"	ea1.URL URL1, " + 
				"	ea1.RN_FULL RNFULL1, " +
				"	ea1.GODINA GOD1," +
				"	ea2.IME IME2, " + 
				"	ea2.URL URL2, " + 
				"	ea2.RN_FULL RNFULL2, " +
				"	ea2.GODINA GOD2" +
				" FROM " + 
				"	EURO_ACTS_CONN eac " + 
				"		JOIN EURO_ACT_NEW ea1 ON eac.EURO_ACT_NEW_ID1 = ea1.id " + 
				"		JOIN EURO_ACT_NEW ea2 ON eac.EURO_ACT_NEW_ID2 = ea2.id " + 
				" where EURO_ACT_NEW_ID1 = :idd or EURO_ACT_NEW_ID2 = :idd " + 
				" order by eac.ROLE_ACT1, eac.ROLE_ACT2, ea1.GODINA, ea2.GODINA, ea1.RN, ea2.rn";
		
		ArrayList<Object[]> results = (ArrayList<Object[]>) JPA.getUtil().getEntityManager().createNativeQuery(sqlString).setParameter("idd", act.getId()).getResultList();
		
		
		
		
		//ArrayList<EuroActNewConn> conns = (ArrayList<EuroActNewConn>) JPA.getUtil().getEntityManager().createQuery("from EuroActNewConn where euroActNewId1 = :idd or euroActNewId2 = :idd order by id").setParameter("idd", act.getId()).getResultList();
		ArrayList<EuroActNewSectionE> secE = (ArrayList<EuroActNewSectionE>) JPA.getUtil().getEntityManager().createQuery("from EuroActNewSectionE where euroActNewId = :idd order by id").setParameter("idd", act.getId()).getResultList();
		ArrayList<EuroActNewSectionZ> secZ = (ArrayList<EuroActNewSectionZ>) JPA.getUtil().getEntityManager().createQuery("from EuroActNewSectionZ where euroActNewId = :idd order by id").setParameter("idd", act.getId()).getResultList();
		
		act.setSectionZ(secZ);
		act.setSectionE(secE);
		
		List<SystemClassif> temp = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB2_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());		
		List<Integer> ab2Roles = new ArrayList<Integer>();
		for (SystemClassif item : temp) {
			ab2Roles.add(item.getCode());
		}
		
		temp = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB3_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());
		List<Integer> ab3Roles = new ArrayList<Integer>();
		for (SystemClassif item : temp) {
			ab3Roles.add(item.getCode());
		}
		
		
		for (Object[] row : results) {
			EuroActNewConn con = new EuroActNewConn();
			con.setId(asInteger(row[0]));
			con.setEuroActNewId1(asInteger(row[1]));
			con.setRoleAct1(asInteger(row[2]));
			con.setEuroActNewId2(asInteger(row[3]));
			con.setRoleAct2(asInteger(row[4]));
			if (row[5] != null) {
				con.setNote(SearchUtils.clobToString((Clob) row[5]));
			}
			con.setUserReg(asInteger(row[6]));
			con.setDateReg(asDate(row[7]));
			con.setUserLastMod(asInteger(row[8]));
			con.setDateLastMod(asDate(row[9]));
			con.setNameAct1(asString(row[10]));
			con.setUrl1(asString(row[11]));
			con.setRnFull1(asString(row[12]));
			con.setGodina1(asInteger(row[13]));
			con.setNameAct2(asString(row[14]));
			con.setUrl2(asString(row[15]));
			con.setRnFull2(asString(row[16]));
			con.setGodina2(asInteger(row[17]));
			
			con.setChanged(false);
			
			con.setRoleText1(sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct1(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			con.setRoleText2(sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct2(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			
			
			if (con.getEuroActNewId1().equals(act.getId()) && ab2Roles.contains(con.getRoleAct1())) {
				act.getSectionAB2().add(con);
			}
			
			if (con.getEuroActNewId2().equals(act.getId()) && ab3Roles.contains(con.getRoleAct2())) {
				act.getSectionAB3().add(con);
			}
			
			if (con.getRoleAct1().equals(EuroConstants.VID_VRAZ_OSN_G)) {
				act.getSectionG().add(con);
			}
			
			if (con.getRoleAct1().equals(EuroConstants.VID_VRAZ_OSN_IZP)) {
				act.getSectionAB4i().add(con);
			}
			
			if (con.getRoleAct1().equals(EuroConstants.VID_VRAZ_OSN_DEL)) {
				act.getSectionAB4d().add(con);
			}
			
			
		}
		
		
		
		
		return act;
	}
	
	
	
	public EuroActNew save(EuroActNew act) throws DbErrorException {
		
		
		if (act.getRnFull() != null) {
			act.setRnFull(act.getRnFull().trim());
		}
		
		if (act.getIme() != null) {
			act.setIme(act.getIme().trim());
		}
		
		EuroActNew saved = super.save(act);
		saved.setSectionAB2(act.getSectionAB2());
		saved.setSectionAB3(act.getSectionAB3());
		saved.setSectionAB4i(act.getSectionAB4i());
		saved.setSectionAB4d(act.getSectionAB4d());
		saved.setSectionG(act.getSectionG());
		saved.setSectionE(act.getSectionE());
		saved.setSectionZ(act.getSectionZ());
		
		return saved;
	}
	
	
	/** Изписват се валидации преди реално да се извика изтриванто. Ако не е позволено да се трие се дава ObjectInUseException */
	@Override
	public void delete(EuroActNew entity) throws DbErrorException, ObjectInUseException {
		try {
			Integer cnt;

			cnt = asInteger( // EURO_DOSSIER --> EURO_ACT_NEW_ID (NUMBER(10))
				createNativeQuery("select count (*) as cnt from EURO_DOSSIER where EURO_ACT_NEW_ID = ?1") //
					.setParameter(1, entity.getId()) //
					.getResultList().get(0));
			if (cnt != null && cnt.intValue() > 0) {
				throw new ObjectInUseException("Актът се използва в досие и не може да бъде изтрит!");
			}

			cnt = asInteger( // EURO_ACTS_CONN --> EURO_ACT_NEW_ID1 (NUMBER(10)), EURO_ACT_NEW_ID2 (NUMBER(10))
				createNativeQuery("select count (*) as cnt from EURO_ACTS_CONN where EURO_ACT_NEW_ID1 = ?1") //
					.setParameter(1, entity.getId()) //
					.getResultList().get(0));
			if (cnt != null && cnt.intValue() > 0) {
				throw new ObjectInUseException("Актът участва във връзки с други актове и не може да бъде изтрит!");
			}
			
			cnt = asInteger( // EURO_ACTS_CONN --> EURO_ACT_NEW_ID1 (NUMBER(10)), EURO_ACT_NEW_ID2 (NUMBER(10))
					createNativeQuery("select count (*) as cnt from EURO_ACTS_CONN where EURO_ACT_NEW_ID2 = ?1") //
						.setParameter(1, entity.getId()) //
						.getResultList().get(0));
				if (cnt != null && cnt.intValue() > 0) {
					throw new ObjectInUseException("Актът участва във връзки с други актове и не може да бъде изтрит!");
				}

			cnt = asInteger( // EURO_SECTION_E --> EURO_ACT_NEW_ID (NUMBER(10))
				createNativeQuery("select count (*) as cnt from EURO_SECTION_E where EURO_ACT_NEW_ID = ?1") //
					.setParameter(1, entity.getId()) //
					.getResultList().get(0));
			if (cnt != null && cnt.intValue() > 0) {
				throw new ObjectInUseException("Актът имам въведени секция Е и не може да бъде изтрит!");
			}
			
			cnt = asInteger( // EURO_SECTION_Z --> EURO_ACT_NEW_ID (NUMBER(10))
					createNativeQuery("select count (*) as cnt from EURO_SECTION_Z where EURO_ACT_NEW_ID = ?1") //
						.setParameter(1, entity.getId()) //
						.getResultList().get(0));
				if (cnt != null && cnt.intValue() > 0) {
					throw new ObjectInUseException("Актът имам въведени секция З и не може да бъде изтрит!");
				}
			

		} catch (ObjectInUseException e) {
			throw e; // за да не се преопакова

		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на свързани обекти към документа!", e);
		}

		super.delete(entity);
	}
	
	
	
	
}
