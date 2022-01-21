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
import com.ib.euroact.db.dao.EuroHarmDAO;
import com.ib.euroact.db.dao.EuroOtchetDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.EuroBuletin;
import com.ib.euroact.db.dto.EuroHarm;
import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;

public class TestEuroHarmDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroHarmDAO.class);

	
	@Test
	public void tesFindById() {
		try {

			EuroHarmDAO dao = new EuroHarmDAO();
			EuroHarm harm = dao.findById(3888);
			if (harm != null) {
				System.out.println("Дeловоден номер: " + harm.getNomerDoc());
				System.out.println("Дата: " + harm.getDatDoc());
				System.out.println("Датa на приемане: " + harm.getDatPriemane());
				System.out.println("Законопроект: " + harm.getIme());
				System.out.println("Закон: " + harm.getImeZakon());
				System.out.println("Вносители: " + harm.getVnositel());
				System.out.println("Водеща комисия: " + harm.getVodesta());
				System.out.println("Първо четене: " + harm.getDatParvo());
				System.out.println("Приоритетен: " + harm.getPrioritet());
				System.out.println("Страници: " + harm.getPages());
				System.out.println("Бележки (ЕC): " + harm.getBelejkiEs());
				System.out.println("Бележки (Правен): " + harm.getBelejkiPraven());
				
				System.out.println("Глави:");
				for (String s : harm.getGlavi()) {
					System.out.println("\t\t" + s);
				}
			}
			
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
	
	
}
