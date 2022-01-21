package com.ib.euroact.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.DossierDAO;
import com.ib.euroact.db.dto.Dossier;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;

public class TestDossierDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestDossierDAO.class);

	private static SystemData sd;

	private static DossierDAO		dao;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new DossierDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testSaveVraz() {
		try {
			
			Dossier d = dao.findById(6330);
			System.out.println(d.getId());
			assertNotNull("d.id is null",d.getId());
			dao.fillDossierConnections(d);
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
}
