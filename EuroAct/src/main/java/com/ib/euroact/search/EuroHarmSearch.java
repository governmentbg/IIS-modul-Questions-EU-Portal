package com.ib.euroact.search;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ib.euroact.system.SystemData;
import com.ib.system.db.SelectMetadata;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.SearchUtils;

/**
 * Търсене на хармонизирани законопроекти
 *
 * @author mamun
 */
public class EuroHarmSearch extends SelectMetadata {

	/** Деловоден номер */
	String rnDoc;
	
	/** Дата на входиране */
	private Date datZakonOt;
	private Date datZakonDo;
	
	/** Търсене в наименованието */
	private String naim;
	
	/** Състояние */
	private Integer sastoianie;
	
	/** Състояние на етапа */
	private Integer sastoianieEtap;
	
	/** Глава */
	private Integer glava;
	
	/** Отговорник */
	private Integer otgovariast;
	
	/** Приоритетен */
	private boolean priority;

	/** За хармонизация */
	private Integer harm;
	
	/** Само приети */
	private boolean prietiOnly;
	
	
	
		
	
	/**  */
	private static final long serialVersionUID = 6199203775464191186L;

	/**  */
	public EuroHarmSearch() {
		
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
		
		
		select.append("select ZD_ZAKONOPROEKTI.id, ZD_ZAKONOPROEKTI.PRIORITET,  ZD_ZAKONOPROEKTI.NOMER_DOC, ZD_ZAKONOPROEKTI.DAT_DOC, nvl(IME_ZAKON, IME) ime, SASTOIANIE, BROI_DV");				
		from.append(" from ZD_ZAKONOPROEKTI ");
		where.append(" where 1 = 1 ");
		
		if (harm != null) {			
			where.append(" and HARM = :harm ");
			params.put("harm", harm);
		}
		
		if (prietiOnly) {
			where.append(" and SASTOIANIE = :sast ");
			params.put("sast", 6161);
		}
		
		
		if (!SearchUtils.isEmpty(rnDoc)) {			
			where.append(" and NOMER_DOC = :rn ");
			params.put("rn", rnDoc);
		}
		
			
		if (datZakonOt != null) {
			datZakonOt = DateUtils.startDate(datZakonOt);
			where.append(" AND DAT_DOC >= :datot ");
			params.put("datot", datZakonOt);
		}
		
		if (datZakonDo != null) {
			datZakonDo = DateUtils.endDate(datZakonDo);			
			where.append(" AND DAT_DOC <= :datdo ");
			params.put("datdo", datZakonDo);
		}
			
			
		if (sastoianie != null) {			
			where.append(" and SASTOIANIE = :sast ");
			params.put("sast", sastoianie);
		}	
		
		if (sastoianieEtap != null) {			
			where.append(" and ETAP_STATUS = :saste ");
			params.put("saste", sastoianieEtap);
		}
		
		if (otgovariast != null) {			
			where.append(" and HARMLICE = :lice ");
			params.put("lice", otgovariast);
		}	
		
		
		if (glava != null) {
			from.append(", ZD_ZAKON_TEMA");
			where.append(" and ZD_ZAKONOPROEKTI.id = ZD_ZAKON_TEMA.ID_ZAKON ");
			where.append(" and TEMA = :tema ");
			params.put("tema", glava);
		}
		
		
		if (priority) {
			where.append(" and PRIORITET = :pr ");
			params.put("pr", 1);
		}
		
		if (naim != null && ! naim.trim().isEmpty()) {		
			String[] words = naim.split(" ");
			int wordN = 1;
			for (String tek : words) {
				if (tek.trim().isEmpty()) {
					continue;
				}
				where.append(" and upper(ime) like :ime" + wordN + " ");
				params.put("ime"+ wordN, "%"+tek.toUpperCase().trim() + "%");
				wordN++;
			}
		}
		
		
		setSqlCount(" select count(ZD_ZAKONOPROEKTI.id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	public String getRnDoc() {
		return rnDoc;
	}

	public void setRnDoc(String rnDoc) {
		this.rnDoc = rnDoc;
	}

	public Date getDatZakonOt() {
		return datZakonOt;
	}

	public void setDatZakonOt(Date datZakonOt) {
		this.datZakonOt = datZakonOt;
	}

	public Date getDatZakonDo() {
		return datZakonDo;
	}

	public void setDatZakonDo(Date datZakonDo) {
		this.datZakonDo = datZakonDo;
	}

	public String getNaim() {
		return naim;
	}

	public void setNaim(String naim) {
		this.naim = naim;
	}

	public Integer getSastoianie() {
		return sastoianie;
	}

	public void setSastoianie(Integer sastoianie) {
		this.sastoianie = sastoianie;
	}

	public Integer getSastoianieEtap() {
		return sastoianieEtap;
	}

	public void setSastoianieEtap(Integer sastoianieEtap) {
		this.sastoianieEtap = sastoianieEtap;
	}

	public Integer getGlava() {
		return glava;
	}

	public void setGlava(Integer glava) {
		this.glava = glava;
	}

	public Integer getOtgovariast() {
		return otgovariast;
	}

	public void setOtgovariast(Integer otgovariast) {
		this.otgovariast = otgovariast;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public Integer getHarm() {
		return harm;
	}

	public void setHarm(Integer harm) {
		this.harm = harm;
	}

	public boolean isPrietiOnly() {
		return prietiOnly;
	}

	public void setPrietiOnly(boolean prietiOnly) {
		this.prietiOnly = prietiOnly;
	}

	

	
	
	
	
}