package com.ib.euroact.db.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroHarm;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;




public class EuroHarmDAO  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroHarmDAO.class);
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public EuroHarm findById(Integer id) throws DbErrorException{
		   
			
	       
		   try{
			   
			 String sql = "SELECT " + 
			 		"	NOMER_DOC, " + 
			 		"	DAT_DOC, " + 
			 		"	DATE_PRIEMANE, " + 
			 		"	GETTEXTATTR(105, SASTOIANIE, DAT_DOC) sast, " + 
			 		"	ZD_ZAKONOPROEKTI.ime, " + 
			 		"	IME_ZAKON, " + 
			 		"	GETCLOBATTR(42, ZD_ZAKONOPROEKTI.id, 3) , " + 
			 		"	PS_PARLAMENT_KOMISII.IME KOMISIA, " + 
			 		"	ZD_PARVO_CHETENE.DATE_do, " + 
			 		"	PRIORITET, " + 
			 		"	PAGES, " + 
			 		"	GETCLOBATTR(42, ZD_ZAKONOPROEKTI.id, 31), " + 
			 		"	GETCLOBATTR(42, ZD_ZAKONOPROEKTI.id, 32)  " + 
			 		" FROM " + 
			 		"	ZD_ZAKONOPROEKTI  " + 
			 		"		LEFT OUTER JOIN ZD_ZAKON_KOMISII  " + 
			 		"		ON ZD_ZAKONOPROEKTI.id = ZD_ZAKON_KOMISII.ID_ZAKONOPROEKT AND " + 
			 		"		ZD_ZAKON_KOMISII.VID = 681  " + 
			 		"			LEFT OUTER JOIN PS_PARLAMENT_KOMISII  " + 
			 		"			ON ZD_ZAKON_KOMISII.ID_KOMISIA = PS_PARLAMENT_KOMISII.ID  " + 
			 		"				LEFT OUTER JOIN ADM_OBJECT_RELATIONS  " + 
			 		"				ON ADM_OBJECT_RELATIONS.CODE_OBJECT1 = 42 AND " + 
			 		"				ADM_OBJECT_RELATIONS.ID_OBJECT1 = ZD_ZAKONOPROEKTI.id AND " + 
			 		"				ADM_OBJECT_RELATIONS.CODE_OBJECT2 = 47  " + 
			 		"					LEFT OUTER JOIN ZD_PARVO_CHETENE  " + 
			 		"					ON ADM_OBJECT_RELATIONS.ID_OBJECT2 = ZD_PARVO_CHETENE.ID  " + 
			 		" WHERE " + 
			 		"	ZD_ZAKONOPROEKTI.id = :IDD";  
			   
			   
			  Query query = JPA.getUtil().getEntityManager().createNativeQuery(sql.toString());
			  query.setParameter("IDD", id);
			  
			  Object[] result = (Object[]) query.getSingleResult();
			  if (result != null) {
				  EuroHarm harm = new EuroHarm();
				  harm.setNomerDoc(SearchUtils.asString(result[0]));
				  harm.setDatDoc(SearchUtils.asDate(result[1]));
				  harm.setDatPriemane(SearchUtils.asDate(result[2]));
				  harm.setSastoianie(SearchUtils.asString(result[3]));
				  harm.setIme(SearchUtils.asString(result[4]));
				  harm.setImeZakon(SearchUtils.asString(result[5]));
				  harm.setVnositel(SearchUtils.asString(result[6]));
				  harm.setVodesta(SearchUtils.asString(result[7]));
				  harm.setDatParvo(SearchUtils.asDate(result[8]));
				  harm.setPrioritet(SearchUtils.asInteger(result[9]));
				  harm.setPages(SearchUtils.asInteger(result[10]));
				  harm.setBelejkiEs(SearchUtils.asString(result[11]));
				  harm.setBelejkiPraven(SearchUtils.asString(result[12]));
				  
				  sql = "select tekst from ZD_ZAKON_TEMA left outer join ADM_SYSTEM_CLASSIF on CODE_CLASSIF = 132 and code = tema and DATE_DO is null where ID_ZAKON = :IDD";
				  query = JPA.getUtil().getEntityManager().createNativeQuery(sql.toString());
				  query.setParameter("IDD", id);
				  
				  ArrayList<Object> temi = (ArrayList<Object>) query.getResultList();
				  for (Object o : temi) {
					  harm.getGlavi().add(SearchUtils.asString(o));
				  }
				  
				  return harm;
			  }
			  
			   
		   } catch (Exception e) {
			   LOGGER.error("Грешка при търсене на хармонизация!", e);
	           throw new DbErrorException("Грешка при търсене на хармонизация!", e);       
		   }
		   
		   return null;
		  
	   }
}
