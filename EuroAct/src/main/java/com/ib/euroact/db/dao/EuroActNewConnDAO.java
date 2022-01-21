package com.ib.euroact.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActNewConn;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;

public class EuroActNewConnDAO extends EuroDAO<EuroActNewConn> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroActNewConnDAO.class);
	
	
	
	public EuroActNewConnDAO(ActiveUser user){
		super(user);		
	}
	
	
	
	
	
}
