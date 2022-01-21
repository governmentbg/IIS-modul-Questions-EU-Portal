package com.ib.euroact.migration;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;

public class FixSectionZ {
	
	
	@Test
	public void fixIt() {
		
		try {
			WordHarmParser parser = new WordHarmParser(new ArrayList<String>());
			JPA.getUtil().begin();
			
			ArrayList<EuroActNewSectionZ> all = (ArrayList<EuroActNewSectionZ>) JPA.getUtil().getEntityManager().createQuery("from EuroActNewSectionZ").getResultList();
			System.out.println(all.size());
			int cnt = 0;
			for (EuroActNewSectionZ tek : all) {
				String name = tek.getNameBgAct();
				int indDv = name.lastIndexOf("(");
				String end = null;
				if (indDv > 0) {
					end = name.substring(indDv);
					//System.out.println(++cnt + ". " + end);
					ArrayList<String> nums = parser.getNumbersFromString(end);
					if (nums.size() != 2) {
						System.out.println(tek.getDvBroi() + "|" + tek.getDvGodina() + "| ----> " +   name);
						continue;
					}else {
						if (tek.getDvBroi() == null) {
							System.out.println(tek.getDvBroi() + "|" + tek.getDvGodina() + "| ----> " +   name);
							continue;
						}
					}
					
				}else {
					System.out.println(tek.getDvBroi() + "|" + tek.getDvGodina() + "| ----> " +   name);
					continue;
				}
				
				ArrayList<String> nums = parser.getNumbersFromString(end);
				
				if (! tek.getDvBroi().equals(nums.get(0)) || ! tek.getDvGodina().equals(Integer.parseInt(nums.get(1)))) {
					System.out.println("~~~~~~~~~~~~~> " + tek.getDvBroi() + "|" + tek.getDvGodina() + " != " + nums + "   id = " + tek.getId());
				}else {
					tek.setNameBgAct(name.substring(0,name.lastIndexOf("(")-1).trim());
					System.out.println(tek.getNameBgAct());
					JPA.getUtil().getEntityManager().persist(tek);
				}
				
				
			}
			
			JPA.getUtil().commit();
		} catch (Exception e) {
			JPA.getUtil().rollback();
			e.printStackTrace();
			fail();
		}
		
	}

}
