package com.ib.euroact.system;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;

public class TestSystemData {
	
	
	@Test
	public void testChildren() {
		
		try {
			SystemData sd = new SystemData();
			List<SystemClassif> children = sd.getChildren(2, 30345, new Date(), EuroConstants.CODE_DEFAULT_LANG);
			for (SystemClassif tek : children) {
				System.out.println(tek.getCode() + " " + tek.getTekst() );
			}
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
