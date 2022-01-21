package com.ib.euroact.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.db.dto.EuroActNewConn;
import com.ib.euroact.db.dto.EuroActNewSectionE;
import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;

public class PrintActInfo {

	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		try {
			
			EuroActNewDAO dao = new EuroActNewDAO(ActiveUser.DEFAULT);
			SystemData sd = new SystemData();
			List<SystemClassif> temp = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB2_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());
			
			List<Integer> ab2Roles = new ArrayList<Integer>();
			for (SystemClassif item : temp) {
				ab2Roles.add(item.getCode());
			}
			
			temp = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_AB3_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());
			List<Integer> ab3Roles = new ArrayList<Integer>();
			for (SystemClassif item : temp) {
				ab3Roles.add(item.getCode());
			}
			
			//String nomerAct = "92/83/ЕИО";
			String nomerAct = "2011/16/ЕС";
			
			
			EuroActNew act = (EuroActNew) JPA.getUtil().getEntityManager().createQuery("from EuroActNew where rnFull = :rn").setParameter("rn", nomerAct).getResultList().get(0);
			act = dao.findByIdFull(act.getId(), sd);
			
			System.out.println("Id:\t" + act.getId());
			System.out.println("Име:\t" + act.getIme());
			System.out.println("Вид:\t" + sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT, act.getVidAct(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			System.out.println("Пълен номер:\t" + act.getRnFull());
			System.out.println("Номер:\t" + act.getRn());
			System.out.println("Година:\t" + act.getGodina());
			System.out.println("URL:\t" + act.getUrl());
			System.out.println("CELEX:\t" + act.getCelex());
			//System.out.println("Брой връзки:\t" + conns.size());
			
			System.out.println("Връзки AB2");
			for (EuroActNewConn con : act.getSectionAB2()) {				
				//System.out.println("\t\t"+sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct1(), EuroConstants.CODE_DEFAULT_LANG, new Date()) + "\t" + dao.findById(con.getEuroActNewId2()).getIme());
				System.out.println("\t\t"+con.getRoleText1() + "\t" + con.getNameAct2());	
			}
			if (act.getNoteAB2() != null) {
				System.out.println("   ЗАБЕЛЕЖКА:");
				System.out.println(act.getNoteAB2());
				System.out.println();
			}
			
			System.out.println("Връзки AB3");
			for (EuroActNewConn con : act.getSectionAB3()) {				
				//System.out.println("\t\t"+sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct2(), EuroConstants.CODE_DEFAULT_LANG, new Date())+ "\t" + dao.findById(con.getEuroActNewId1()).getIme());
				System.out.println("\t\t"+con.getRoleText2() + "\t" + con.getNameAct1());	
			}
			if (act.getNoteAB3() != null) {
				System.out.println("   ЗАБЕЛЕЖКА:");
				System.out.println(act.getNoteAB3());
				System.out.println();
			}
			
			
			System.out.println("Връзки AB4");
			System.out.println("  - Актове за изпълнение");
			for (EuroActNewConn con : act.getSectionAB4i()) {				
				//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
				System.out.println("\t\t"+ con.getNameAct2());
			}
			System.out.println("  - Делегирани актове");
			for (EuroActNewConn con : act.getSectionAB4d()) {			
					//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
					System.out.println("\t\t"+ con.getNameAct2());
			}
			if (act.getNoteAB4D() != null) {
				System.out.println("   ЗАБЕЛЕЖКА:");
				System.out.println(act.getNoteAB4D());
				System.out.println();
			}
			
			
			
			System.out.println("Връзки Секция Г");			
			for (EuroActNewConn con : act.getSectionG()) {
				//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
				System.out.println("\t\t"+ con.getNameAct2());
			}
			
			System.out.println("Д. Съществува процедура за нарушение за непълно или неточно транспониране" + act.getSectionDYesNo());
			System.out.println();
			System.out.println(act.getSectionDText());
			System.out.println();
			
			
			System.out.println("Връзки Секция E");			
			for (EuroActNewSectionE con : act.getSectionE()) {				
				System.out.println("\t\t"+ con.getName() + " " + con.getUrl());								
			}
			
					
			
			
			
			
			System.out.println("З. Другите актове, с които е въведен съответния акт на ЕС" + act.getSectionZYesNo());
			System.out.println();
			System.out.println(act.getSectionZText());
			System.out.println();
			for (EuroActNewSectionZ con : act.getSectionZ()) {				
				System.out.println("\t\t"+ con.getNameBgAct());								
			}
			
			
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
