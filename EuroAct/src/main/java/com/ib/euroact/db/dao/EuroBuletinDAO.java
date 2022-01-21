package com.ib.euroact.db.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroBuletin;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.SearchUtils;




public class EuroBuletinDAO extends EuroDAO<EuroBuletin> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroBuletinDAO.class);
	
	
	
	public EuroBuletinDAO(ActiveUser user){
		super(user);		
	}
	
	
	public boolean checkBuletinNumberForDubl(EuroBuletin buletin) throws DbErrorException{
		   
			
			if (buletin == null || buletin.getNomer() == null) {
				return false;
			}
		
	       StringBuilder sql = new StringBuilder();
	       
	       sql.append("SELECT count(id) CNT  FROM EURO_BULETIN  WHERE NOMER = :NM") ;
	       
	       if (buletin.getDatBuletin() != null){	    	   
	    	   sql.append(" and DAT_BULETIN >= :datb and DAT_BULETIN <= :datе");
	       }
	       
	       					
	       if (buletin.getId() != null) {
	    	   sql.append(" and EURO_BULETIN.ID <> :bid") ;
	       }
	     
	       
	       
		   try{
			  Query query = JPA.getUtil().getEntityManager().createNativeQuery(sql.toString());
			  query.setParameter("NM", buletin.getNomer());
			  
			  if (buletin.getDatBuletin() != null){
				  Calendar calendar = new GregorianCalendar();
				  calendar.setTime(buletin.getDatBuletin());
				  int currentYear = calendar.get(Calendar.YEAR);
				  Date dateOt = DateUtils.startDate(new GregorianCalendar(currentYear, 0, 1).getTime());
		    	  Date dateDo = DateUtils.endDate(new GregorianCalendar(currentYear, 11, 31).getTime());
				  query.setParameter("datb", dateOt);
				  query.setParameter("datе",dateDo);
			  }
			  
			  if (buletin.getId() != null) {
				  query.setParameter("bid", buletin.getId());
			  }
			  
			  
			  int size = SearchUtils.asInteger(query.getSingleResult());
			  if (size > 0) {
				  return true;
			  }else {
				  return false;
			  }
			   
		   } catch (Exception e) {
			   LOGGER.error("Грешка при търсене на бюлетини!", e);
	           throw new DbErrorException("Грешка при търсене на бюлетини!", e);       
		   }
		  
	   }
}
