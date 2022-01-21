package com.ib.euroact.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.DossierDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.Dossier;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;

public class TestProgramaDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestProgramaDAO.class);

	private static SystemData sd;

	private static ProgramaDAO		dao;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new ProgramaDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testLoad() {
		try {
			
			Programa p = dao.findById(196) ;
			assertNotNull("P is null",p);
			
			System.out.println(p.getId());
			assertNotNull("P.id is null",p.getId());
			//System.out.println(p.getHeader());
			
			List<PointPrograma> points = dao.findProgramaPoints(p.getId());
			assertTrue(points != null && points.size() > 0);
			System.out.println("Points.size = " + points.size());
			
			
			p.setFooter("test");
			dao.save(p);
			
			ProgramaSearch ps = new ProgramaSearch();
			ps.buildQuery(sd);
			
			System.out.println(ps.getSql());
			
			ps.setGodina(2007);
			ps.buildQuery(sd);
			
			System.out.println(ps.getSql());
			
			
			Programa p1 = new Programa();
			p1.setId(11111);
			p1.setGodina(2007);
			
			assertTrue(dao.findDubl(p1));
			
			p.setId(null);
			
			assertTrue(dao.findDubl(p1));
			
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
}
