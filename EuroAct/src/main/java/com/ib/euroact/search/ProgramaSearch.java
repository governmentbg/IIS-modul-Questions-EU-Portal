package com.ib.euroact.search;

import java.util.HashMap;
import java.util.Map;

import com.ib.euroact.system.SystemData;
import com.ib.system.db.SelectMetadata;
import com.ib.system.exceptions.DbErrorException;

/**
 * Търсене на евроактове
 *
 * @author mamun
 */
public class ProgramaSearch extends SelectMetadata {


	private Integer godina;

		
	
	/**  */
	private static final long serialVersionUID = 6198203775464191186L;

	/**  */
	public ProgramaSearch() {
		
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
		
		
		select.append("select id a01, 'Годишна работна програма на Народното събрание по въпросите на Европейския съюз (' || GODINA ||' г.)' a02 ");				
		from.append(" from EURO_PROGRAMA ");
		
		
		if (godina != null) {			
			where.append(" where GODINA = :god ");
			params.put("god", godina);
		}
		
		
		
			
		setSqlCount(" select count(id) " + from.toString() + where.toString()); // на този етап бройката е готова

		
		String queryStr=select.toString() + from.toString() + where.toString();
		setSql(queryStr);
		System.out.println("SQL String: "+ queryStr);

		setSqlParameters(params);
	}

	public Integer getGodina() {
		return godina;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
	}

	
	
	
	
}