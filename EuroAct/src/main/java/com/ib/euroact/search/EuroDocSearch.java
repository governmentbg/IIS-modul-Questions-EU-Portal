package com.ib.euroact.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ib.euroact.system.SystemData;
import com.ib.system.db.SelectMetadata;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;

/**
 * Търсене на евроактове
 *
 * @author mamun
 */
public class EuroDocSearch extends SelectMetadata {


	Date dateDocOt;
	Date dateDocDo; 
	Date datePoluchOt; 
	Date datePoluchDo; 
	Integer signSeria; 
	Integer signGodina; 
	Integer signNomer;
	Integer vid;
	Integer avtor; 
	Integer poluchenOt;
	String celex;
	String uid;
	Integer procSeria; 
	Integer procGodina; 
	Integer procNomer;
	Integer status;
	Integer temaEK; 
	Integer gpYear; 
	Integer gpPoint; 
	String partZagl; 
	String freeNom;

		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public EuroDocSearch() {
		
	}

	/**
	 * Използва се от основния екран за търсене на актове <br>
	 * @throws DbErrorException 
	 * 
	 */
	public void buildQuery(SystemData sd) throws DbErrorException {
				

		Map<String, Object> params = new HashMap<>();

		StringBuilder select = new StringBuilder();
		StringBuilder from = new StringBuilder();
		StringBuilder where = new StringBuilder();
		
		if (dateDocOt != null) {
			dateDocOt = DateUtils.startDate(dateDocOt);
		}
		if (dateDocDo != null) {
			dateDocDo = DateUtils.endDate(dateDocDo);
		}
		if (datePoluchOt != null) {
			datePoluchOt = DateUtils.startDate(datePoluchOt);
		}
		if (datePoluchDo != null) {
			datePoluchDo = DateUtils.endDate(datePoluchDo);
		}
		
		ArrayList<String> tablici = new ArrayList<String>();
        ArrayList<String> uslovia = new ArrayList<String>();
		
		
		select.append("SELECT EURO_ACT.ID A01, EURO_ACT.SIGN_SERIA A02, EURO_ACT.SIGN_GODINA A03, EURO_ACT.SIGN_NOMER A04, EURO_ACT.SIGN A05, EURO_ACT.ZAGL_BG A06, EURO_ACT.ZAGL_EN A07 ");				
		tablici.add("EURO_ACT");
		
		
		// Търсене в пълен текст на поръчката
		if (partZagl != null) {
	//        int rezult = new UniSearchUtils().addUslovie(partZagl,"EURO_ACT", "DIC_EURO", tablici, uslovia,Porachka.CODE_OBJECT_EURO_ACT,null,1);
	//        if (rezult == -1)
	//            return acts;
		}
        
         
        if (dateDocOt != null) {
            uslovia.add("EURO_ACT.DAT_DOC >= :DAT1 ");
        	params.put("DAT1", dateDocOt);
        }
           
        if (dateDocDo != null) {   
            uslovia.add("EURO_ACT.DAT_DOC <= :DAT2 ");
            params.put("DAT2", dateDocDo);
        }
       
            
        if (datePoluchOt != null) {
            uslovia.add("EURO_ACT.DAT_POLUCH >= :DAT3 ");
            params.put("DAT3", datePoluchOt);
        }
           
        if (datePoluchDo != null) {   
            uslovia.add("EURO_ACT.DAT_POLUCH <= :DAT4 ");
            params.put("DAT4", datePoluchDo);
        }
           
            
            
         if (signSeria != null) {
             uslovia.add("EURO_ACT.SIGN_SERIA = :SSERIA");
             params.put("SSERIA", signSeria);
         }
             
           
         if (signNomer != null) {
            uslovia.add("EURO_ACT.SIGN_NOMER = :SNOMER");
            params.put("SNOMER", signNomer);
         }
            
        if (signGodina != null) {
           uslovia.add("EURO_ACT.SIGN_GODINA = :SGODINA");
           params.put("SGODINA", signGodina);
        }
        
        
        if (freeNom != null && freeNom.trim().length() > 0) {
            uslovia.add("EURO_ACT.SIGN = :FREEN");
            params.put("FREEN", freeNom);
        }
        
            
        if (vid != null) {
            uslovia.add("EURO_ACT.VID_DOSIE = :VID");
            params.put("VID", vid);
        }
         
        if (avtor != null) {
            uslovia.add("EURO_ACT.AVTOR = :AVTOR");
            params.put("AVTOR", avtor);
        }
        
        if(poluchenOt != null) {
        	uslovia.add("EURO_ACT.POLUCHEN_OT = :POLUCHEN");
            params.put("POLUCHEN", poluchenOt);
        }
                     
         if (celex != null && celex.trim().length() > 0) {
            uslovia.add("EURO_ACT.PRIEMANE_CELEX = :CELEX");
            params.put("CELEX", celex);
         }
            
         if (uid != null && uid.trim().length() > 0) {
            uslovia.add("EURO_ACT.UNID = :UID");
            params.put("UID", uid);
         }
         
         if (status != null) {
            uslovia.add("EURO_ACT.STATUS = :STAT");
            params.put("STAT", status);
         }
           
        if (gpYear != null){
            uslovia.add("EURO_ACT.NSPROG_GODINA = :GPY");
            params.put("GPY", gpYear);
        }
                  
        if (gpPoint != null) {
            uslovia.add("EURO_ACT.NSPROG_NOMER = :GPP");
            params.put("GPP", gpPoint);
        }
          

        if (procSeria != null || procNomer != null || procGodina != null){
            tablici.add("EURO_DOSIE_ACT");
            uslovia.add("EURO_ACT.ID = EURO_DOSIE_ACT.ID_ACT");  
            
            if (procSeria != null) {
                uslovia.add("EURO_DOSIE_ACT.SIGN_PROC = :SIGNPS");
                params.put("SIGNPS", procSeria);
            }
            if (procNomer != null) {
                uslovia.add("EURO_DOSIE_ACT.SIGN_NOMER = :SIGNPN");
                params.put("SIGNPN", procNomer);
            }
            if (procGodina != null) {
                uslovia.add("EURO_DOSIE_ACT.SIGN_GODINA = :SIGNPG");
                params.put("SIGNPG", procGodina);
            }
        }
        
        if (temaEK != null ){
            tablici.add("EURO_TEMA_ACT_EK");
            uslovia.add("EURO_ACT.ID = EURO_TEMA_ACT_EK.ID_ACT");    
            uslovia.add("EURO_TEMA_ACT_EK.TEMA = :TEMA");
            params.put("TEMA", temaEK);
        }
		
        if (tablici.size() > 0){
            from.append(" FROM ") ;
            for (int i = 0; i < tablici.size(); i++)  {
            	from.append(tablici.get(i));
                if (i != (tablici.size() - 1))
                	from.append(", ");
            }
        }
        
        
        if (uslovia.size() > 0){
            where.append(" WHERE ") ;
            for (int i = 0; i < uslovia.size(); i++)  {
            	where.append(uslovia.get(i));
                if (i != (uslovia.size() - 1))
                	where.append(" AND ");
            }
        }
		
			
		setSqlCount(" select count(EURO_ACT.id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	public Date getDateDocOt() {
		return dateDocOt;
	}

	public void setDateDocOt(Date dateDocOt) {
		this.dateDocOt = dateDocOt;
	}

	public Date getDateDocDo() {
		return dateDocDo;
	}

	public void setDateDocDo(Date dateDocDo) {
		this.dateDocDo = dateDocDo;
	}

	public Date getDatePoluchOt() {
		return datePoluchOt;
	}

	public void setDatePoluchOt(Date datePoluchOt) {
		this.datePoluchOt = datePoluchOt;
	}

	public Date getDatePoluchDo() {
		return datePoluchDo;
	}

	public void setDatePoluchDo(Date datePoluchDo) {
		this.datePoluchDo = datePoluchDo;
	}

	public Integer getSignSeria() {
		return signSeria;
	}

	public void setSignSeria(Integer signSeria) {
		this.signSeria = signSeria;
	}

	public Integer getSignGodina() {
		return signGodina;
	}

	public void setSignGodina(Integer signGodina) {
		this.signGodina = signGodina;
	}

	public Integer getSignNomer() {
		return signNomer;
	}

	public void setSignNomer(Integer signNomer) {
		this.signNomer = signNomer;
	}

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	public Integer getAvtor() {
		return avtor;
	}

	public void setAvtor(Integer avtor) {
		this.avtor = avtor;
	}

	public Integer getPoluchenOt() {
		return poluchenOt;
	}

	public void setPoluchenOt(Integer poluchenOt) {
		this.poluchenOt = poluchenOt;
	}

	public String getCelex() {
		return celex;
	}

	public void setCelex(String celex) {
		this.celex = celex;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getProcSeria() {
		return procSeria;
	}

	public void setProcSeria(Integer procSeria) {
		this.procSeria = procSeria;
	}

	public Integer getProcGodina() {
		return procGodina;
	}

	public void setProcGodina(Integer procGodina) {
		this.procGodina = procGodina;
	}

	public Integer getProcNomer() {
		return procNomer;
	}

	public void setProcNomer(Integer procNomer) {
		this.procNomer = procNomer;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTemaEK() {
		return temaEK;
	}

	public void setTemaEK(Integer temaEK) {
		this.temaEK = temaEK;
	}

	public Integer getGpYear() {
		return gpYear;
	}

	public void setGpYear(Integer gpYear) {
		this.gpYear = gpYear;
	}

	public Integer getGpPoint() {
		return gpPoint;
	}

	public void setGpPoint(Integer gpPoint) {
		this.gpPoint = gpPoint;
	}

	public String getPartZagl() {
		return partZagl;
	}

	public void setPartZagl(String partZagl) {
		this.partZagl = partZagl;
	}

	public String getFreeNom() {
		return freeNom;
	}

	public void setFreeNom(String freeNom) {
		this.freeNom = freeNom;
	}

	

	
	
	
	
}