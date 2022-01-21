package com.ib.euroact.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActNewSectionE;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;

public class EuroActNewSectionEDAO extends EuroDAO<EuroActNewSectionE> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroActNewSectionEDAO.class);
	
	
	
	public EuroActNewSectionEDAO(ActiveUser user){
		super(user);		
	}
	
	
	
	
	
}
