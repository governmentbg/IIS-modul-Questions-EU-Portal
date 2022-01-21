package com.ib.euroact.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ib.euroact.db.dao.DossierDAO;
import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.Dossier;
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

public class PrintDosie {

	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		try {
			
			//String fileName = "ЗЗБУТ ДВ_ 108_2008 Д 89-391.docx";
			//String fileName = "ЗВПКИИП ДВ_62_2015 Р 575-2013.docx";  //Регламент
			String fileName = "НК ДВ_7_2019 Д 2014-42.docx";
			
			
			ArrayList<Integer> vidDirektivi = new ArrayList<Integer>();
			
			vidDirektivi.add(EuroConstants.TIP_ACT_DIREKTIVA); 
			vidDirektivi.add(EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE);
			vidDirektivi.add(EuroConstants.TIP_ACT_POPRAVKA_NA_DIREKTIVA);
			vidDirektivi.add(EuroConstants.TIP_ACT_RAMKOVO_RESHENIE);
			vidDirektivi.add(EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA);
		
			
			
			EuroActNewDAO adao = new EuroActNewDAO(ActiveUser.DEFAULT);
			DossierDAO ddao = new DossierDAO(ActiveUser.DEFAULT);
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
			
					
			// ---- Start -----
			
			ArrayList<Dossier> all = (ArrayList<Dossier>) JPA.getUtil().getEntityManager().createQuery("from Dossier where fileName = :fn").setParameter("fn", fileName).getResultList();
			//ArrayList<Dossier> all = (ArrayList<Dossier>) JPA.getUtil().getEntityManager().createQuery("from Dossier where id = :idd").setParameter("idd", 6524).getResultList();
			if (all.size() == 0) {
				System.out.println("НЕ СЕ ОТКРИВА ДОСИЕ С ТОВА ИМЕ НА ФАЙЛ!!!");
				return;
			}
				
			//Dossier dossier = all.get(0);
			
			Dossier dossier = ddao.findById(6532);
			ddao.fillDossierConnections(dossier);
			EuroActNew act = adao.findByIdFull(dossier.getEuroActNewId(), sd);
			
			
			System.out.println("Досие на:  " + dossier.getName() );
			System.out.println("Закон: " + dossier.getZakonName());
			System.out.println("Хармонизиращ закон: " + dossier.getZakonName());
			System.out.println("Параграф: " + dossier.getNoteV());
			
			String vid = sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT, act.getVidAct(), EuroConstants.CODE_DEFAULT_LANG, new Date());
			System.out.print("Вид акт:  " + vid)  ;
			System.out.print("\tНомер:  " + act.getRnFull());
			System.out.print("\tCELEX:  " + act.getCelex());
			System.out.println();
			System.out.println();
			
			
			
			if (! vidDirektivi.contains(act.getVidAct())){
				//Регламент - къса
				System.out.println("Секция \"А. Въеждане изисквания на директива или рамково решение, за което има друго информационно досие\"");
				System.out.println();
			}else {
				System.out.println("Секция \"А. Информация за транспонираната директива или рамково решение\"");
				System.out.println();
				//Директиви дълга
				System.out.println("Подсекция \"1. Информация за транспонираната директива или рамково решение\"");
				System.out.println();
				System.out.println("Наименование на " + vid + ":\t"  + act.getIme());
				System.out.println("Текст на " + vid + ":\t" + act.getUrl());
				System.out.println();
				System.out.println();
				
				System.out.println("Подсекция \"2. Актове, които се отменят/заменят/изменят от " + vid);
				System.out.println();
				if (act.getSectionAB2().size() == 0) {
					System.out.println(vid + " " + act.getRnFull() + " не отменя, заменя или изменя друг акт на Европейския съюз.");
				}else{				
					for (EuroActNewConn con : act.getSectionAB2()) {				
						//System.out.println("\t\t"+sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct1(), EuroConstants.CODE_DEFAULT_LANG, new Date()) + "\t" + dao.findById(con.getEuroActNewId2()).getIme());
						System.out.println("\t\t"+con.getRoleText1() + "\t" + con.getNameAct2());	
					}					
				}
				if (act.getNoteAB2() != null) {
					System.out.println("ЗАБЕЛЕЖКА:");
					System.out.println(act.getNoteAB2());					
				}
				System.out.println();
				System.out.println();
				
				System.out.println("Подсекция \"3. Актове, които изменят " + vid + " " + act.getRnFull() + "\"");
				System.out.println();
				if (act.getSectionAB3().size() == 0) {
					System.out.println(vid + " " + act.getRnFull() + " не е изменен/a/o или отменен/a/o.");
				}else{				
					for (EuroActNewConn con : act.getSectionAB3()) {				
						//System.out.println("\t\t"+sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_VRAZ, con.getRoleAct1(), EuroConstants.CODE_DEFAULT_LANG, new Date()) + "\t" + dao.findById(con.getEuroActNewId2()).getIme());
						System.out.println("\t\t"+con.getRoleText1() + "\t" + con.getNameAct2());	
					}					
				}
				if (act.getNoteAB2() != null) {
					System.out.println("ЗАБЕЛЕЖКА:");
					System.out.println(act.getNoteAB2());					
				}
				System.out.println();
				System.out.println();
				
				
				
				System.out.println("Подсекция \"4. Актове за изпълнение или делегирани актове по прилагане на " + vid + " " + act.getRnFull() + "\"");
				System.out.println();
				System.out.println("- актове за изпълнение: " + sd.decodeItem(EuroConstants.CODE_SYSCLASS_AB4, act.getIzpalnenieActYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
				for (EuroActNewConn con : act.getSectionAB4i()) {				
					//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
					System.out.println("\t\t"+ con.getNameAct2());
				}
				if (act.getNoteAB4I() != null) {
					System.out.println("ЗАБЕЛЕЖКА:");
					System.out.println(act.getNoteAB4I());					
				}
				
				System.out.println("- делегирани актове: " + sd.decodeItem(EuroConstants.CODE_SYSCLASS_AB4, act.getDelegiraniActsYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
				for (EuroActNewConn con : act.getSectionAB4d()) {				
					//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
					System.out.println("\t\t"+ con.getNameAct2());
				}
				if (act.getNoteAB4D() != null) {
					System.out.println("ЗАБЕЛЕЖКА:");
					System.out.println(act.getNoteAB4D());					
				}
				System.out.println();
				System.out.println();
				
				System.out.println("Подсекция \"5. Други директиви и/или рамкови решения\"");
				System.out.println();
				if (dossier.getLinksDir().size() == 0) {
					//System.out.println(dossier.getZakonNameV() + "въвежда изискванията и на : ");
					System.out.println("\t*** НЯМА НАМЕРЕНИ ДАННИ ***" );
				}else {
					System.out.println(dossier.getZakonNameV() + " въвежда изискванията и на : ");
					for (Dossier d: dossier.getLinksDir()) {
						System.out.println("\t" + d.getNameAct());
					}
					System.out.println("за които има други информационни досиета");
				}
				
				
			}
			
			
			
			if (vidDirektivi.contains(act.getVidAct())){
				//Directive
				System.out.println();
				System.out.println();
				System.out.println("Секция \"Б. Мерки по прилагане на регламент или решение на ЕС, за което има друго информационно досие\"");
				System.out.println();
				if (dossier.getReqMeasuredYesNo().equals(1)) {
					for (Dossier d: dossier.getLinksReg()) {
						System.out.println("\t" + d.getNameAct());
					}
				}else {
					System.out.println(dossier.getZakonNameV() + " не предвижда мерки по прилагането на регламент или решение на Европейския съюз.");
				}
				
				
				
			}else {
				System.out.println("Секция \"Б. Информация за регламент или решение на ЕС, за които са предвидени мерки по прилагане\"");
				System.out.println();
				
			}
			
			
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"В. Административно регулиране - въвеждане на лицензионен или регулаторен режим, издаване на удостоверение\"");
			System.out.println();
			System.out.println("\tЗаконът въвежда административно регулиране, което да е въз основа на акта на ЕС: " + sd.decodeItem(EuroConstants.CODE_CLASSIF_DANE, dossier.getSectionVYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			if (dossier.getSectionVText() != null) {
				System.out.println("\t" + dossier.getSectionVText());
			}
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"Г. Тълкувателни решения на Съда на ЕС\"");
			System.out.println();	
			if (act.getSectionG().size() > 0) {
				for (EuroActNewConn con : act.getSectionG()) {
					EuroActNew gact = adao.findById(con.getEuroActNewId2());
					//System.out.println("\t\t"+ dao.findById(con.getEuroActNewId2()).getIme());
					String gvid = sd.decodeItem(EuroConstants.CODE_SYSCLASS_VID_ACT, gact.getVidAct(), EuroConstants.CODE_DEFAULT_LANG, new Date());
					System.out.println("\t"+ gvid + " " + gact.getRnFull());
					System.out.println("\tURL: " + gact.getUrl());
					System.out.println("\t" + gact.getNoteSr());
					//System.out.println("\t--------"+gact.getRnFull()+"----------");
					System.out.println();
					
				}
			}else {
				System.out.println("\tНяма постановени решения на Съда на Европейския съюз, които да тълкуват разпоредби на " + vid + act.getRnFull());
			}
			
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"Д. Процедура за нарушение за непълно или неточно транспониране\"");
			System.out.println();			
			System.out.println("\tСъществува процедура за нарушение за непълно или неточно транспониране: " + sd.decodeItem(EuroConstants.CODE_CLASSIF_DANE, act.getSectionDYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			if (act.getSectionDText() != null) {
				System.out.println("\t" + act.getSectionDText());
			}
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"Е. Доклад или мотивирано становище по глава единадесета от ПОДНС\"");	
			System.out.println();
			
			if (act.getSectionEText() != null) {
				System.out.println("\t" + act.getSectionEText());
			}
			
			if (act.getSectionE().size() == 0) {
				//System.out.println("\tНяма приет доклад или мотивирано становище по процедурата за парламентарно наблюдение и контрол по въпросите на Европейския съюз.");
			}else {
				for (EuroActNewSectionE con : act.getSectionE()) {				
					System.out.println("\t"+ con.getName());
					System.out.println("\tURL: "+ con.getUrl());
					if (con.getNote() != null) {
						System.out.println("\tРезюме:");
						System.out.println(con.getNote());
						System.out.println("-------------------------------------------------------------------------------");
					}
					
				}
			}
			
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"Ж. Нотифициране на технически регламент\"");	
			System.out.println();
			System.out.println("\tЗаконът е нотифициран като технически регламент: " + sd.decodeItem(EuroConstants.CODE_CLASSIF_DANE, dossier.getNotificationsYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			if (dossier.getNotificationsYesNo() == 1) {
				System.out.println("\tНотификационен номер: "+ dossier.getNotificationsNumber());
				System.out.println("\tДата на получаване: "+ dossier.getNotificationsDate());
				System.out.println("\tURL: "+ dossier.getNotificationsURL());
			}
			
			
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"З. Другите актове, с които е въведен съответния акт на ЕС\"");	
			System.out.println();
			System.out.println("Има Другите актове: " + sd.decodeItem(EuroConstants.CODE_CLASSIF_DANE, act.getSectionZYesNo(), EuroConstants.CODE_DEFAULT_LANG, new Date()));
			System.out.println();			
			System.out.println();
			int porN = 1;
			for (EuroActNewSectionZ con : act.getSectionZ()) {				
				System.out.println("\t"+ porN + ". " + con.getNameBgAct());
				porN ++;
			}
			
			
			System.out.println();
			System.out.println();
			System.out.println("Секция \"И. Други\"");			
			System.out.println();
			System.out.println("\tПодсекция \"1. Приети текстове\"");
			System.out.println(dossier.getSectionI1());
			System.out.println();
			System.out.println("\tПодсекция \"2. Анотация на приетите текстове\"");
			System.out.println(dossier.getSectionI2());
			System.out.println();
			System.out.println("\tПодсекция \"3. Анализ на въвеждането - неточности/практики\"");
			System.out.println(dossier.getSectionI3());
			System.out.println();
			System.out.println("\tПодсекция \"4. Частична или цялостна предварителна оценка на въздействието\"");
			System.out.println(dossier.getSectionI4());
			System.out.println();
	
			
			
			
			
		} catch (DbErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
