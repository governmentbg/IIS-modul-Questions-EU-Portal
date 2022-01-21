package com.ib.euroact.search;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ib.euroact.system.SystemData;
import com.ib.system.db.SelectMetadata;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;

/**
 * Търсене на отчети
 *
 * @author mamun
 */
public class EuroOtchetSearch extends SelectMetadata {


	private Date datOtchetOt;
	private Date datOtchetDo;

		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public EuroOtchetSearch() {
		
	}

	/**
	 * Използва се от основния екран за търсене на oтчети <br>
	 * @throws DbErrorException 
	 * 
	 */
	public void buildQuery(SystemData sd) throws DbErrorException {
				

		Map<String, Object> params = new HashMap<>();

		StringBuilder select = new StringBuilder();
		StringBuilder from = new StringBuilder();
		StringBuilder where = new StringBuilder();
		
		
		select.append("select id a01, DAT_OTCHET a02, ADRESAT a03, ANOT ao4, STATUS ao5");				
		from.append(" from EURO_OTCHET ");
		
		
		if (datOtchetOt != null || datOtchetDo != null) {			
			where.append(" where ");
			String andStr = "";
			
			if (datOtchetOt != null) {
				datOtchetOt = DateUtils.startDate(datOtchetOt);
				where.append(" dat_otchet >= :datot ");
				params.put("datot", datOtchetOt);
				andStr = " and ";
			}
			
			if (datOtchetDo != null) {
				datOtchetDo = DateUtils.endDate(datOtchetDo);
				where.append(andStr);
				where.append(" dat_otchet <= :datdo ");
				params.put("datdo", datOtchetDo);
			}
			
			
			
		}
		
		
		
			
		setSqlCount(" select count(id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	public Date getDatOtchetOt() {
		return datOtchetOt;
	}

	public void setDatOtchetOt(Date datOtchetOt) {
		this.datOtchetOt = datOtchetOt;
	}

	public Date getDatOtchetDo() {
		return datOtchetDo;
	}

	public void setDatOtchetDo(Date datOtchetDo) {
		this.datOtchetDo = datOtchetDo;
	}

	

	
	
	
	
}