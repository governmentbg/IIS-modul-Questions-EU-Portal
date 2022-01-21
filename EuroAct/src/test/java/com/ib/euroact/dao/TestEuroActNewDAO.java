package com.ib.euroact.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class TestEuroActNewDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroActNewDAO.class);

	private static SystemData sd;

	private static EuroActNewDAO		dao;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroActNewDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	private EuroActNew act;
	
	@Test
	public void testSaveAct() {
		try {
			act = new EuroActNew();
			act.setRnFull("1111/222 (EC)");
			act.setRn(111);
			act.setCelex("ALABAAAAA123");
			act.setGodina(2020);
			act.setIme("Aktaaaa");
			act.setUrl("http://uril.url");
			act.setDelegiraniActsYesNo(1);
			act.setIzpalnenieActYesNo(1);
			act.setNoteAB2("ab2");
			act.setNoteAB3("ab3");
			act.setNoteAB4D("ab4");
			act.setNoteSr("sr");
			act.setSectionDText("dddd");
			act.setSectionDYesNo(1);
			act.setSectionEText("e");
			act.setSectionEYesNo(1);
			act.setSectionGYesNo(1);
			act.setSectionZText("z");
			act.setSectionZYesNo(1);
			act.setVidAct(111);
			
			
			
			
			
			
			
			JPA.getUtil().runInTransaction(() -> act = dao.save(act));
			LOGGER.info("--OK-- dao.save(this.doc, true, null, sd)");
			assertNotNull(act.getId()); 
		} catch (BaseException e) {			
			e.printStackTrace();
		}
	}
	
}
