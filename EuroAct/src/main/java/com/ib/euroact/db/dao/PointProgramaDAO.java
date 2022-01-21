package com.ib.euroact.db.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.exceptions.DbErrorException;


public class PointProgramaDAO extends EuroDAO<PointPrograma> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointProgramaDAO.class);
	
	
	
	public PointProgramaDAO(ActiveUser user){
		super(user);		
	}
	
	
	 /** Връща списък от актове
     * @return списък от актове като списък от EuroAct като Object[]
     * @throws DbErrorException грешка при работа с БД    
   */
   public ArrayList<Object[]> filterActs( Long gpYear, Long gpPoint) throws DbErrorException  {
       
       
       try {
       
            String selectClause = "SELECT EURO_ACT.ID, " +
            "                          EURO_ACT.SIGN_SERIA, " +
            "                          EURO_ACT.SIGN_GODINA, " +
            "                          EURO_ACT.SIGN_NOMER, " +
            "                          EURO_ACT.SIGN, " +           
            "                          EURO_ACT.ZAGL_BG, " +
            "                          EURO_ACT.ZAGL_EN  "    ;   
            
           ArrayList<String> tablici = new ArrayList<String>();
           ArrayList<String> uslovia = new ArrayList<String>();
           String fromClause = "" ;
           String whereClause = "" ;
           String orderClause = " ORDER BY EURO_ACT.SIGN_SERIA, EURO_ACT.SIGN_GODINA, EURO_ACT.SIGN_NOMER" ;
           
           tablici.add("EURO_ACT");
           
           if (gpYear != null){
               uslovia.add("EURO_ACT.NSPROG_GODINA = "+ gpYear);
           }
                     
           if (gpPoint != null)
               uslovia.add("EURO_ACT.NSPROG_NOMER = "+ gpPoint);
             

           if (tablici.size() > 0){
               fromClause += " FROM " ;
               for (int i = 0; i < tablici.size(); i++)  {
                   fromClause += tablici.get(i);
                   if (i != (tablici.size() - 1))
                       fromClause += ", ";
               }
           }
           
           
           if (uslovia.size() > 0){
               whereClause += " WHERE " ;
               for (int i = 0; i < uslovia.size(); i++)  {
                   whereClause += uslovia.get(i);
                   if (i != (uslovia.size() - 1))
                       whereClause += " AND ";
               }
           }
      
      
           //System.out.println(selectClause + fromClause + whereClause + orderClause);
           
          
           Query query = createNativeQuery(selectClause + fromClause + whereClause + orderClause);

           @SuppressWarnings("unchecked")
		   ArrayList<Object[]>  rez = (ArrayList<Object[]>) query.getResultList();

           return rez;
             
             

       } catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на EuroAct -> filterActs!", e);
	   }
            
   }
	
	
}
