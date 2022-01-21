package com.ib.euroact.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroBuletinDAO;
import com.ib.euroact.db.dao.EuroOtchetDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.EuroBuletin;
import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;

public class TestBuletinDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestBuletinDAO.class);

	private static SystemData sd;

	private static EuroBuletinDAO		dao;
	
	EuroBuletin buletin = null;
	
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroBuletinDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testSave() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			buletin = new EuroBuletin();
			
			buletin.setDatBuletin(new Date());
			buletin.setDonInfo("ааа");
			buletin.setNomer(1);
			buletin.setZaglavie("ддддд");
			
			
			JPA.getUtil().runInTransaction(() -> buletin = dao.save(buletin));
			
			assertNotNull(buletin.getId());
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
	
	
	@Test
	public void testDubl() {
		try {
			buletin = new EuroBuletin();		
			//buletin.setDatBuletin(new Date());
			buletin.setNomer(1);
			//buletin.setId(1111);
		
			System.out.println(dao.checkBuletinNumberForDubl(buletin));
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
}
