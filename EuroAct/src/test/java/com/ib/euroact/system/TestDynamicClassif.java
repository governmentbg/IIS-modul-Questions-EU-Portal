package com.ib.euroact.system;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;

public class TestDynamicClassif {

	@Test
	public void testIt() {


		try {
			SystemData sd = new SystemData();
			List<SystemClassif> classif = sd.getSysClassification     (EuroConstants.CODE_SYSCLASS_DIR_ES,new Date(), EuroConstants.CODE_DEFAULT_LANG);
			System.out.println(classif.size());
			
			assertTrue(classif.size() > 0);
			
			
			SystemClassif item = classif.get(0);
			
			
			System.out.println(item.getTekst());
			
//			Object[] spec = (Object[]) sd.getItemSpecifics(206, item.getCode(), EuroConstants.CODE_DEFAULT_LANG, new Date());
//			System.out.println(spec[0].toString());
//			
			
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
