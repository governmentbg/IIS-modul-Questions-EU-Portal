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
public class EuroBuletinSearch extends SelectMetadata {


	private Date datBuletinOt;
	private Date datBuletinDo;

		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public EuroBuletinSearch() {
		
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
		
		
		select.append("select id a01, NOMER a02, DAT_BULETIN a03, ZAGLAVIE a04 ");				
		from.append(" from EURO_BULETIN ");
		
		
		if (datBuletinOt != null || datBuletinDo != null) {			
			where.append(" where ");
			String andStr = "";
			
			if (datBuletinOt != null) {
				datBuletinOt = DateUtils.startDate(datBuletinOt);
				where.append(" DAT_BULETIN >= :datot ");
				params.put("datot", datBuletinOt);
				andStr = " and ";
			}
			
			if (datBuletinDo != null) {
				datBuletinDo = DateUtils.endDate(datBuletinDo);
				where.append(andStr);
				where.append(" DAT_BULETIN <= :datdo ");
				params.put("datdo", datBuletinDo);
			}
			
			
			
		}
		
		
		
			
		setSqlCount(" select count(id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	public Date getDatBuletinOt() {
		return datBuletinOt;
	}

	public void setDatBuletinOt(Date datBuletinOt) {
		this.datBuletinOt = datBuletinOt;
	}

	public Date getDatBuletinDo() {
		return datBuletinDo;
	}

	public void setDatBuletinDo(Date datBuletinDo) {
		this.datBuletinDo = datBuletinDo;
	}

	
	
	
}