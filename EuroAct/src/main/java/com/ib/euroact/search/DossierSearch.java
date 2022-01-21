package com.ib.euroact.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.system.db.DialectConstructor;
import com.ib.system.db.JPA;
import com.ib.system.db.SelectMetadata;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;

/**
 * Търсене на инф. досиета
 *
 * @author mamun
 */
public class DossierSearch extends SelectMetadata {

	private Integer vidAct;	
	private String  partIme;
	private String  nomerFull;
		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public DossierSearch() {
		
	}

	/**
	 * Използва се от основния екран за търсене на dosieta <br>
	 * @throws DbErrorException 
	 * 
	 */
	public void buildQuery(SystemData sd) throws DbErrorException {
				

		Map<String, Object> params = new HashMap<>();

		StringBuilder select = new StringBuilder();
		StringBuilder from = new StringBuilder();
		StringBuilder where = new StringBuilder();
		
		String dialect = JPA.getUtil().getDbVendorName();
		
		select.append("select ed.id A01, "+DialectConstructor.limitBigString(dialect, "ed.NАМЕ", 300)+" A02, ean.RN_FULL A03, ean.VID_ACT A04");				
		from.append("  from EURO_DOSSIER ed join EURO_ACT_NEW ean on ed.EURO_ACT_NEW_ID = ean.ID ");
		where.append(" where 1=1 ");
		
		
		
		if (vidAct != null) {
			
			ArrayList<Integer> codes = new ArrayList<Integer>();
			codes.add(vidAct);
			List<SystemClassif> classif = sd.getSysClassification(EuroConstants.CODE_SYSCLASS_VID_ACT, new Date(), EuroConstants.CODE_DEFAULT_LANG);
			for (SystemClassif item : classif) {
				if (item.getCodeParent() == vidAct.intValue()) {
					codes.add(item.getCode());
				}
			}
			
			if (codes.size() == 1) {
				where.append(" and VID_ACT = :vidact ");
				params.put("vidact", vidAct);
			}else {
				where.append(" and VID_ACT in  :vidact ");
				params.put("vidact", codes);
			}
			
		}
		
		
		  
		if (nomerFull != null && !nomerFull.trim().isEmpty()) {		
			System.out.println(nomerFull);
			where.append(" and RN_FULL like :rnFull ");
			params.put("rnFull", "%"+nomerFull.trim() + "%");
		}
		
		
		
		if (partIme != null && ! partIme.trim().isEmpty()) {			
			where.append(" and upper(ed.NАМЕ) like :ime ");
			params.put("ime", "%"+partIme.toUpperCase().trim() + "%");
		}
		
			
		setSqlCount(" select count(ed.id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	
	public Integer getVidAct() {
		return vidAct;
	}

	public void setVidAct(Integer vidAct) {
		this.vidAct = vidAct;
	}

	

	public String getPartIme() {
		return partIme;
	}

	public void setPartIme(String partIme) {
		this.partIme = partIme;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNomerFull() {
		return nomerFull;
	}

	public void setNomerFull(String nomerFull) {
		this.nomerFull = nomerFull;
	}

	
}