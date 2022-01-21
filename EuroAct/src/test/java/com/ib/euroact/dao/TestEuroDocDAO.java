package com.ib.euroact.dao;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroDocDAO;
import com.ib.euroact.db.dto.EuroDoc;
import com.ib.euroact.db.dto.EuroDocTemaNS;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;

public class TestEuroDocDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroDocDAO.class);

	private static SystemData sd;

	private static EuroDocDAO		dao;
	private static EuroDoc doc;
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new EuroDocDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	
	
	
	
	@Test
	public void testLoad() {
		try {
			
			doc = dao.findById(5258);
			//JPA.getUtil().getEntityManager().close();
			JPA.getUtil().getEntityManager().clear();
			
			System.out.println(doc.getZaglBg());
			System.out.println(doc.getZaglEn());
			System.out.println(doc.getSign());
			System.out.println(doc.getTemaNS().size());
			System.out.println(doc.getTemaEK().size());
			System.out.println(doc.getProcedures().size());
			System.out.println(doc.getStatusi().size());
			System.out.println("V->" + doc.getVraz().size());
			for (EuroDocTemaNS tema : doc.getTemaNS()) {
				System.out.println("\t---> " + tema.getTema());
			}
			
			
			
			
			doc.setZaglBg(doc.getZaglBg() + "  eeeeeeeeeeeeeeeeeuuuuuuuuuuuuuubg");
			
			List<EuroDocTemaNS> temi = new ArrayList<EuroDocTemaNS>(); 
			doc.setTemaNS(temi);
			
			EuroDocTemaNS t = new EuroDocTemaNS();
			t.setTema(7130);
			
			EuroDocTemaNS t1 = new EuroDocTemaNS();
			t1.setTema(7131);
			doc.getTemaNS().add(t);
			doc.getTemaNS().add(t1);
			
			JPA.getUtil().runInTransaction(() -> doc = dao.save(doc));
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
		
	}
	
	
	@Test
	public void testSearchByDosie() {
		try {
			
			ArrayList<EuroDoc> docs = new EuroDocDAO(ActiveUser.DEFAULT).filterEuroDocByProc(7092, 2011, 194);
			System.out.println(docs.size());
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
		
		
		
		
		
	}
	
}
