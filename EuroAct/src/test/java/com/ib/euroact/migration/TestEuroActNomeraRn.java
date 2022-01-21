package com.ib.euroact.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.BaseException;

public class TestEuroActNomeraRn {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroActNomeraRn.class);

	private static SystemData sd;

	private static EuroActNewDAO		dao;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroActNewDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testFillRn() {
		try {
			
			ArrayList<EuroActNew> acts = (ArrayList<EuroActNew>) JPA.getUtil().getEntityManager().createQuery("from EuroActNew where rn is null").getResultList();
			System.out.println(acts.size());
			for (EuroActNew act : acts) {
				try {
					dao.recognizeNumber(act);
					JPA.getUtil().runInTransaction(() -> dao.save(act));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
}
