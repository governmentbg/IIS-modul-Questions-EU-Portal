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
 * Търсене на евроактове
 *
 * @author mamun
 */
public class EuroActNewSearch extends SelectMetadata {

	private Integer vidAct;	
	private Integer nomer;
	private Integer godina;
	private String  partIme;
	private String  nomerFull;
		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public EuroActNewSearch() {
		
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
		
		String dialect = JPA.getUtil().getDbVendorName();
		
		select.append("select id A01, rn_full A02, godina A03, CELEX A04, VID_ACT A05, ");
		select.append(DialectConstructor.limitBigString(dialect, "IME", 400) + " A06 "); // max 300!		
		from.append(" from EURO_ACT_NEW ");
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
		
		if (nomer != null) {			
			where.append(" and RN = :rn ");
			params.put("rn", nomer);
		}
		
		if (nomerFull != null && !nomerFull.trim().isEmpty()) {		
			System.out.println(nomerFull);
			where.append(" and RN_FULL like :rnFull ");
			params.put("rnFull", "%"+nomerFull.trim() + "%");
		}
		
		if (godina != null) {			
			where.append(" and GODINA = :god ");
			params.put("god", godina);
		}
		
		if (partIme != null && ! partIme.trim().isEmpty()) {			
			where.append(" and upper(ime) like :ime ");
			params.put("ime", "%"+partIme.toUpperCase().trim() + "%");
		}
		
			
		setSqlCount(" select count(id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
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

	public Integer getNomer() {
		return nomer;
	}

	public void setNomer(Integer nomer) {
		this.nomer = nomer;
	}

	public Integer getGodina() {
		return godina;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
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