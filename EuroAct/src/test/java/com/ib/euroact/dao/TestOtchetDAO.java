package com.ib.euroact.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroOtchetDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;

public class TestOtchetDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestOtchetDAO.class);

	private static SystemData sd;

	private static EuroOtchetDAO		dao;
	
	EuroOtchet otchet = null;
	
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroOtchetDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testSave() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			otchet = new EuroOtchet();
			
			otchet.setDatOtchet(sdf.parse("11.11.2021"));
			otchet.setAnot("2");
			otchet.setComment("3");
			otchet.setAdresat(4);
			otchet.setStatus(5);
			otchet.setVid(6);
			
			JPA.getUtil().runInTransaction(() -> otchet = dao.save(otchet));
			
			assertNotNull(otchet.getId());
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
}
