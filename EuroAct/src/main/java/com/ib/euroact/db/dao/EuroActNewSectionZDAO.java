package com.ib.euroact.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActNewSectionE;
import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;

public class EuroActNewSectionZDAO extends EuroDAO<EuroActNewSectionZ> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroActNewSectionZDAO.class);
	
	
	
	public EuroActNewSectionZDAO(ActiveUser user){
		super(user);		
	}
	
	
	
	
	
}
