package com.ib.euroact.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;

public class EuroOtchetDAO extends EuroDAO<EuroOtchet> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroOtchetDAO.class);
	
	
	
	public EuroOtchetDAO(ActiveUser user){
		super(user);		
	}
	
	
	
	
	
	
	
}
