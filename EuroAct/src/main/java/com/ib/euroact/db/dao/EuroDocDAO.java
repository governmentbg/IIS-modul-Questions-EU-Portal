package com.ib.euroact.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroDoc;
import com.ib.euroact.db.dto.EuroDocVraz;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;

public class EuroDocDAO extends EuroDAO<EuroDoc> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroDoc.class);
	
	
	
	public EuroDocDAO(ActiveUser user){
		super(user);		
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<EuroDoc> filterEuroDoc(Integer signSeria, Integer signGodina, Integer signNomer){
		
		if (signSeria == null || signGodina == null || signNomer == null) {
			return new ArrayList<EuroDoc>();
		}
		
		Query q = JPA.getUtil().getEntityManager().createQuery("from EuroDoc where signSeria = :seria and signNomer = :nom and signGodina = :god");
		q.setParameter("seria", signSeria);
		q.setParameter("nom", signNomer);
		q.setParameter("god", signGodina);
		
		return (ArrayList<EuroDoc>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EuroDoc> filterEuroDoc(String freeNom){
		
		if (freeNom == null || freeNom.isEmpty()) {
			return new ArrayList<EuroDoc>();
		}
		
		Query q = JPA.getUtil().getEntityManager().createQuery("from EuroDoc where sign = :freeNom");
		q.setParameter("freeNom", freeNom);
		
		return (ArrayList<EuroDoc>) q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<EuroDoc> filterEuroDocByProc(Integer procSeria, Integer procGodina, Integer procNomer) {
		
		ArrayList<EuroDoc> result = new ArrayList<EuroDoc>();
		
		if (procSeria == null || procGodina == null || procNomer == null) {
			return result;
		}
		
		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select EURO_ACT.id, EURO_ACT.SIGN, nvl(ZAGL_BG, ZAGL_EN) zagl from EURO_ACT join EURO_DOSIE_ACT on EURO_ACT.ID = EURO_DOSIE_ACT.ID_ACT where EURO_DOSIE_ACT.SIGN_PROC = :seria and EURO_DOSIE_ACT.SIGN_NOMER = :nom and EURO_DOSIE_ACT.SIGN_GODINA = :god");
		q.setParameter("seria", procSeria);
		q.setParameter("nom", procNomer);
		q.setParameter("god", procGodina);
		
		ArrayList<Object[]> rows = (ArrayList<Object[]>) q.getResultList();
		for (Object[] row : rows) {
			EuroDoc doc = new EuroDoc();
			doc.setId(SearchUtils.asInteger(row[0]));
			doc.setSign(SearchUtils.asString(row[1]));
			doc.setZaglBg(SearchUtils.asString(row[1]));
			result.add(doc);
			
		}
		
		
		
		
		return result;
		
	}
	
	
	
	public EuroDoc findById(Integer id) throws DbErrorException {
		EuroDoc act = super.findById(id);
		
		act.setVraz(loadVraz(id));
		
		return act;
	}
	
	
	public EuroDoc save (EuroDoc doc) throws DbErrorException {
		
		List<EuroDocVraz> allVraz = doc.getVraz();
		EuroDoc docNew = super.save(doc);
		docNew.setVraz(allVraz);
		docNew.setSignSeriaText(doc.getSignSeriaText());
		
		
		try {
			Query q = JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_VRAZ_ACT where ID_ACT1 = :IDA1 or ID_ACT2 = :IDA2");
			q.setParameter("IDA1", docNew.getId());
			q.setParameter("IDA2", docNew.getId());
			q.executeUpdate();
			
			for (EuroDocVraz vraz : docNew.getVraz()) {
				
				if (vraz.getIdAct1() == null) {
					vraz.setIdAct1(doc.getId());
				}
				
				if (vraz.getIdAct2() == null) {
					//Идва от парсер-а и не сме я идентифицирали
					EuroDoc a = new EuroDoc();
                    a.setSignSeria(vraz.getSeria());
                    a.setSignNomer(vraz.getNomer());
                    a.setSignGodina(vraz.getGodina());
                    a.setSign(vraz.getSignVraz());
                    a.setZaglBg("Не е пристигнал !");
                    a = super.save(a);
                    vraz.setIdAct2(a.getId());
				}
				
				
				q = JPA.getUtil().getEntityManager().createNativeQuery("INSERT INTO EURO_VRAZ_ACT(ID, ID_ACT1, ID_ACT2) 	VALUES(SEQ_ACT_DOP.nextval, :IDA1, :IDA2)");
				q.setParameter("IDA1", vraz.getIdAct1() != null ? vraz.getIdAct1() : docNew.getId());
				q.setParameter("IDA2", vraz.getIdAct2());
				q.executeUpdate();
			}
		} catch (Exception e) {
			LOGGER.error("Грешка при запис на данни за връзки на акт!", e);
			throw new DbErrorException("Грешка при запис на данни за връзки на акт!", e);
		}
		
		
		return docNew;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<EuroDocVraz> loadVraz(Integer EuroDocId) throws DbErrorException {
        ArrayList<EuroDocVraz> vr = new ArrayList<EuroDocVraz>();
        try {
            
            
            String sqlString = " SELECT V.ID, V.ID_ACT1, V.ID_ACT2, E1.ZAGL_BG Z1, E1.ZAGL_EN Z2, E1.SIGN S1, E2.ZAGL_BG Z3, E2.ZAGL_EN Z4, E2.SIGN S2,  " + 
            				   " E1.SIGN_SERIA SS1, E1.SIGN_GODINA SG1, E1.SIGN_NOMER SN1, E2.SIGN_SERIA SS2, E2.SIGN_GODINA SG2, E2.SIGN_NOMER SN2 " +	
                               " FROM   EURO_VRAZ_ACT V, EURO_ACT E1, EURO_ACT E2 " + 
                               " WHERE  (V.ID_ACT1 = :ID_ACT OR V.ID_ACT2 = :ID_ACT) AND V.ID_ACT1 = E1.ID AND V.ID_ACT2 = E2.ID";
            
            Query sqlQuery = JPA.getUtil().getEntityManager().createNativeQuery(sqlString);
            sqlQuery.setParameter("ID_ACT",EuroDocId);
            
//            sqlQuery.addScalar("ID", new LongType());
//            sqlQuery.addScalar("ID_ACT1", new LongType());
//            sqlQuery.addScalar("ID_ACT2", new LongType());
//            
//            sqlQuery.addScalar("Z1", new StringType());
//            sqlQuery.addScalar("Z2", new StringType());
//            sqlQuery.addScalar("S1", new StringType());
//            
//            sqlQuery.addScalar("Z3", new StringType());
//            sqlQuery.addScalar("Z4", new StringType());
//            sqlQuery.addScalar("S2", new StringType());
            
            ArrayList<Object[]> rez = (ArrayList<Object[]>)sqlQuery.getResultList();
            for (int i = 0; i < rez.size(); i++)  {
                Object[] row = (Object[] )rez.get(i);
                EuroDocVraz vraz = new EuroDocVraz();
                vraz.setId(SearchUtils.asInteger(row[0]));
                
                vraz.setIdAct1(SearchUtils.asInteger(row[1]));
                vraz.setIdAct2(SearchUtils.asInteger(row[2]));
                if (EuroDocId.equals(vraz.getIdAct1())){
                    // Връзката е в 2
                    vraz.setSignVraz(SearchUtils.asString(row[8]));  //s2
                    if (row[6] == null) {
                        vraz.setNameVraz(SearchUtils.asString(row[7]));    
                    }else {
                        vraz.setNameVraz(SearchUtils.asString(row[6]));
                    }
                    
                    vraz.setSeria(SearchUtils.asInteger(row[12]));
                    vraz.setGodina(SearchUtils.asInteger(row[13]));
                    vraz.setNomer(SearchUtils.asInteger(row[14]));
                    
                    
                }else{
                    // Връзката е в 1
                     vraz.setSignVraz(SearchUtils.asString(row[5]));  //s2
                      if (row[3] == null) {
                          vraz.setNameVraz(SearchUtils.asString(row[4]));    
                      }else {
                          vraz.setNameVraz(SearchUtils.asString(row[3]));
                      }
                      vraz.setSeria(SearchUtils.asInteger(row[9]));
                      vraz.setGodina(SearchUtils.asInteger(row[10]));
                      vraz.setNomer(SearchUtils.asInteger(row[11]));
                    
                }
               
                vr.add(vraz);
                
                
                
            }
            
            
            
        } catch (Exception e) {
        	LOGGER.error("Грешка при извличане на данни за връзки на акт!", e);
			throw new DbErrorException("Грешка при извличане на данни за връзки на акт!", e);
        }
        
        return vr;
    }
	
	
	
}
