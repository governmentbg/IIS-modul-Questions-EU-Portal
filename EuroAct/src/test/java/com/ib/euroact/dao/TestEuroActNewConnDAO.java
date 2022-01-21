package com.ib.euroact.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewConnDAO;
import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewConn;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;

public class TestEuroActNewConnDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroActNewConnDAO.class);

	private static SystemData sd;

	private static EuroActNewConnDAO		dao;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroActNewConnDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	private EuroActNewConn conn;
	
	@Test
	public void testSaveVraz() {
		try {
			
			conn = new EuroActNewConn();
			conn.setEuroActNewId1(1);
			conn.setEuroActNewId2(2);
			
			conn.setRoleAct1(11);
			conn.setRoleAct2(22);
			
			conn.setNote("333");
			
			
			JPA.getUtil().runInTransaction(() -> conn = dao.save(conn));
			LOGGER.info("--OK-- ");
			assertNotNull(conn.getId()); 
		} catch (BaseException e) {			
			e.printStackTrace();
			fail();
		}
	}
	
}
