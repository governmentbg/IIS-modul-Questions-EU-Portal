package com.ib.euroact.migration;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Query;

import org.junit.Test;

import com.aspose.words.Document;
import com.ib.euroact.db.dao.DossierDAO;
import com.ib.euroact.db.dao.EuroActNewConnDAO;
import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dao.EuroActNewSectionEDAO;
import com.ib.euroact.db.dao.EuroActNewSectionZDAO;
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
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.FileUtils;

public class TestWordParser {
	
	public static HashMap<String, Integer> actNames = new HashMap<String, Integer>();
	
	
	@Test
	public void testParse() {
		
		
		//String dirName = "D:\\_HARMONIZACIA\\Last\\ЗУСЕСИФ ДВ_101_2015 P 1303-2013.docx";
		String dirName = "D:\\_HARMONIZACIA\\Last\\";
		
		
		
		try {
			
			SystemData sd = new SystemData();
			List<SystemClassif> vids = sd.getSysClassification(200, new Date(), EuroConstants.CODE_DEFAULT_LANG);
			//System.out.println(vids.size());
			
			
			ArrayList<String> vidove = new ArrayList<String>();
			HashMap<String, Integer> precod = new HashMap<String, Integer>();
			TreeMap<String, ArrayList<Dosie>> dMap = new TreeMap<String, ArrayList<Dosie>>();
			
			initLists(vidove, precod, sd);
			
			
			
			
			
			
			
			WordHarmParser wp = new WordHarmParser(vidove);
			byte[] byteLic = FileUtils.getBytesFromFile(new File("D:\\Bullshit.iv"));
			ByteArrayInputStream bisL = new ByteArrayInputStream(byteLic);
				
			com.aspose.words.License license = new com.aspose.words.License();
			license.setLicense(bisL);
					
			
			
			
			
			int brErrors = 0;
		    int brOk = 0;
		    ArrayList<Dosie> dosieta = new ArrayList<Dosie>();
			
			File f = new File(dirName);
		    if (f.exists()) {
		    	if (f.isDirectory()) {
		    		File[] listOfFiles = f.listFiles();
		    		for (File tekF : listOfFiles) {
		    			if (!tekF.isDirectory()) {		    				
		    				try {
		    					
		    					System.out.print(fillGap(tekF.getName(), 50));
		    					byte[] bytes = FileUtils.getBytesFromFile(new File(tekF.getCanonicalPath()));
		    					ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		    					Document doc = new Document(bis);
		    					Dosie dosie = wp.parseFile(doc);
		    					dosie.setFilename(tekF.getName());
		    					dosieta.add(dosie);
		    					
		    					ArrayList<Dosie> dArray = dMap.get(dosie.getNomer());
		    					if (dArray == null) {
		    						dArray = new ArrayList<Dosie>();
		    					}
		    					dArray.add(dosie);
		    					dMap.put(dosie.getNomer(), dArray);
		    					//		    					
		    					brOk ++;
		    					System.out.println("\tOK");
		    				} catch (Exception e) {
		    					brErrors++;
		    					e.printStackTrace();
		    					if (e.getMessage() == null) {
		    						System.err.println("\tERROR: " + e.getClass().getSimpleName());
		    						e.printStackTrace();
		    					}else {
		    						System.err.println("\tERROR: " + e.getMessage());
		    					}
		    					Thread.sleep(1000);
		    				}
		    			}
		    		}
		    	}else {
		    		try {
		    			
		    			//Единичен файл за проба
		    			
    					//System.out.println(f.getName());
    					byte[] bytes = FileUtils.getBytesFromFile(new File(f.getCanonicalPath()));
    					ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    					Document doc = new Document(bis);
    					Dosie dosie = new WordHarmParser(vidove).parseFile(doc);
    					dosie.setFilename(f.getName());
    					dosieta.add(dosie);
    				
    					brOk ++;
    				} catch (Exception e) {
    					brErrors++;
    					e.printStackTrace();
    					Thread.sleep(1000);
    				}
		    	}
		    }else {
		    	System.out.println("Къдееее сииииии ?????!!!!!!");
		    }
			
		    
		   
		    System.out.println();
		    System.out.println("Брой обработени коректно: " + brOk);
		    System.out.println("Брой обработени с грешка: " + brErrors);
		    
		    
		    System.out.println();
		    System.out.println("30250 --> " + sd.getOtherRole(30250));
		    System.out.println("30287 --> " + sd.getOtherRole(30287));
		    System.out.println("3028711 --> " + sd.getOtherRole(3028711));
		    
//		    if (brOk > 0) {
//		    	return;
//		    }
		    
		    List<SystemClassif> pravi = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_PRAVI_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());
		    System.out.println("ПРАВИ:");
		    for (SystemClassif item : pravi) {
		    	System.out.println("\t" + item.getTekst());
		    }
		    
		    List<SystemClassif> obratni = sd.getClassifByListVod(EuroConstants.CODE_LOGIC_LIST_VRAZKI, EuroConstants.DEF_OBRATNI_VRAZKI, EuroConstants.CODE_DEFAULT_LANG, new Date());
		    System.out.println("ОБРАТНИ:");
		    for (SystemClassif item : obratni) {
		    	System.out.println("\t" + item.getTekst());
		    }
		    
		   proccessLinks(dosieta, vidove, precod, vids );
		   
		   saveData(dosieta, pravi, sd);
		    
		   System.out.println("----------------- END -------------------");
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

	}
	
	
	public static String fillGap(String s, int len){
		int l = s.length();
		for (int i = l; i <= len; i++) {
			s = s + " ";
		}
		return s;
	}
	
	
	
	public static void saveMainActFromDosie(Dosie dosie) throws DbErrorException {
		
			EuroActNew act = dosie.toEuroActNew();
		
			act = checkAndSave(act);
			
			dosie.setIdAct(act.getId());
			
			
			
			
		
	}
	
	
	private static EuroActNew checkAndSave(EuroActNew act) throws DbErrorException {
		EuroActNewDAO dao = new EuroActNewDAO(ActiveUser.DEFAULT);
		if (act.getRnFull() != null && act.getRnFull().trim().length() > 0) {
			String url = null;
			if (act.getVidAct().equals(EuroConstants.TIP_ACT_POPRAVKA_NA_DIREKTIVA) || 
				act.getVidAct().equals(EuroConstants.TIP_ACT_POPRAVKA_REGLAMENT) || 
				act.getVidAct().equals(EuroConstants.TIP_ACT_POPRAVKA_DEL_REGLAMENT) || 
				act.getVidAct().equals(EuroConstants.TIP_ACT_POPRAVKA_REGLAMENT_IZP)) {
				
				url = act.getUrl();
			}
			Integer id = dao.findIdActByNomer(act.getRnFull(), act.getRn(), act.getGodina(), act.getVidAct(), url);
			if (id != null && id > 0) {
				act.setId(id);
			}else {
				try {
					
					act = dao.save(act);
					actNames.put(act.getIme(), act.getId());
					
				} catch (Exception e) {					
					throw new DbErrorException("Грешка при запис на act", e);
				}
				
			}
		}else {
			if (act.getIme() != null) {
				
				//System.out.println("----> " + act.getIme());
				Integer idhm = actNames.get(act.getIme());
				//System.out.println("Id from HashMap:" + idhm);
				
				Integer id = dao.findIdActByUpperName(act.getIme());
				//System.out.println("Id from Db:" + id);
				if (id != null && id > 0) {
					act.setId(id);
				}else {
					try {
						
						act = dao.save(act);
						actNames.put(act.getIme(), act.getId());
						
					} catch (Exception e) {					
						throw new DbErrorException("Грешка при запис на act", e);
					}
					
				}
			}
		}
		
		return act;
		
	}


	public static void proccessLinks (ArrayList<Dosie> dosieta, ArrayList<String> vidove, HashMap<String, Integer> precod,  List<SystemClassif> vids) throws InvalidParameterException {
		
		System.out.println();
	    System.out.println();
	    System.out.println("Обработка на връзки ...");
	    System.out.println();
	    
	    
		
		//TreeSet<String> links = new TreeSet<String > (); 
		
		for (Dosie dosie : dosieta) {
			System.out.print(fillGap(dosie.getFilename(),50));
			//links.add(dosie.getUrl());
			
	    	//System.out.println("--------------> " + dosie.getFilename());
	    	
	    	//System.out.println("--------------> AB2" );
			
	    	for (Container c : dosie.getAb2list()) {		    				    		
	    		if (c.getName() != null) {
	    			//System.out.println(dosie.getFilename() + " --> " + "A.2. " + c.getName());
	    			createDosieFromABContainer(c, vidove, precod, vids );
	    			c.getDosie().setFilename(dosie.getFilename());
	    		}
	    	}
	    	
	    	
			
			//System.out.println("--------------> AB3" );
	    	for (Container c : dosie.getAb3list()) {		    				    		
	    		if (c.getName() != null) {
	    			//System.out.println(dosie.getFilename() + " --> " + "A.3. " + c.getName());
	    			createDosieFromABContainer(c, vidove, precod, vids);
	    			c.getDosie().setFilename(dosie.getFilename());
	    		}
	    	}
	    	
	    	//System.out.println("--------------> AB4" );
	    	for (Container c : dosie.getAb4list()) {	
	    		//System.out.println("0");
	    		if (c.getName() != null) {
	    			//System.out.println(dosie.getFilename() + " --> " + "A.4. " + c.getName());
	    			createDosieFromABContainer(c, vidove, precod, vids);
	    			c.getDosie().setFilename(dosie.getFilename());
	    		}
	    	}
	    	
//	    	for (Container c : dosie.getA5list()) {
//	    		joins.add("A5.\t"+ c.getAction());
//	    		boolean found = false;
//	    		for (String vid : vidove) {
//	    			if (c.getName().toUpperCase().startsWith(vid.toUpperCase())) {
//	    				found = true;
//	    				break;
//	    			}
//	    		}
//	    		
//	    		if (!found) {
//	    			System.out.println(dosie.getFilename() + " --> " + "A.5. " + c.getName());
//	    		}		    	
//	    	}
	    	
	    	for (Container c : dosie.getGlist()) {		    				    		
	    		if (c.getName() != null) {
	    			//System.out.println(dosie.getFilename() + " --> " + "A.4. " + c.getName());
	    			createDosieFromGContainer(c, vidove, precod, vids);
	    			c.getDosie().setFilename(dosie.getFilename());
	    		}
	    	}
	    	
//	    	for (Container c : dosie.getElist()) {		    				    		
//	    		System.out.println(c.getAction());
//	    	}
	    	
//	    	for (Container c : dosie.getJlist()) {		    				    		
//	    		System.out.println(c.getAction());
//	    	}
	    	
	    	System.out.println("\tOK");
	    	
	    }
		
//		String db = "";
//		for (String s : links) {
//			
//			s = s.replace(" ", "");
//			s = s.replace("\r", "");
//			s = s.replace("\n", "");
//			db += s + "\r\n";
//		}
//		
//		try {
//			FileUtils.writeBytesToFile("D:\\_HARMONIZACIA\\help\\link.txt", db.getBytes());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

	
	
	public static void createDosieFromABContainer(Container c, ArrayList<String> vidove, HashMap<String, Integer> precod, List<SystemClassif>  vids) throws InvalidParameterException {
		
		
		Dosie d = new Dosie();
		
		
		String name = c.getName();
		String nomer = null;
		String dopInfo = null;
		
		if (name == null) {
			return;
		}
		
		name = name.replace("  ", " ");
		name = name.replace("Рeгламент", "Регламент");
		
		c.setName(name);
		
		String foundVid = null;
		for (String vid : vidove) {
			if (name.toUpperCase().trim().startsWith(vid.toUpperCase())) {
				foundVid = vid;
				break;
			}
		}
		
		if (foundVid == null) {
			//Ще се опитаме да махнем членовете
			
			int first = 9999;
			String part = null;
			for (String vid : vidove) {
				int ind = name.toUpperCase().trim().indexOf(vid.toUpperCase());
				if (ind != -1 && ind < first) {
					first = ind;
					foundVid = vid;
					dopInfo = name.substring(0,ind);					
					part = c.getName().substring((foundVid+dopInfo).length()+1);
					nomer = getNumberFromName(part);
					if (dopInfo.endsWith(" от ")) {
						dopInfo = dopInfo.substring(0,dopInfo.length()-3).trim();
					}
					c.setDopInfo(dopInfo);
					name = c.getName().substring(ind);
					
				}
			}
			if (foundVid == null) {
				System.out.println("Непознат вид **** " + c.getName() + "|" + foundVid + "|" + dopInfo + "|" + nomer + "|");
			}
			//return;   //TODO и на тях трябва да се опитаме да им извлечем номерата
		}else {
			String part = c.getName().substring(foundVid.length()+1);
			nomer = getNumberFromName(part);
		}
		
		
		//System.out.println("***" + name);
		d.setNameAct(name);
		d.setNomer(nomer);
		d.setTip(foundVid);
		d.setUrl(c.getUrl());
		if (foundVid != null) {
			
			for (SystemClassif v : vids) {
				if (v.getTekst().equalsIgnoreCase(foundVid)) {
					d.setTipL(v.getCode());
					break;
				}
			}
			
			
//			System.out.println(foundVid);
//			System.out.println(precod.size());
//			System.out.println(precod.get(foundVid));
			d.setTipL(precod.get(foundVid));
		}
		if (nomer != null && !nomer.isEmpty()) {
			new WordHarmParser(vidove).recognizeNumber(d);
		}else {
			System.out.println("*** Ненамерен номер на акт: " + foundVid + " --> " + d.getNameAct() );
		}
		
		c.setDosie(d);
	}
	
	
	public static void createDosieFromGContainer(Container c, ArrayList<String> vidove, HashMap<String, Integer> precod, List<SystemClassif>  vids) throws InvalidParameterException {
		
		
		Dosie d = new Dosie();
		
		int begInd = c.getDopInfo().toUpperCase().indexOf(c.getVid().toUpperCase());
		String name = c.getDopInfo().substring(begInd);
		
		
		String nomer = c.getNomer();
		String dopInfo = null;
		
		if (name == null) {
			return;
		}
		
				
		String foundVid = c.getVid();
		
		
		if (foundVid == null) {
				System.out.println("G: Непознат вид **** " + c.getName() + "|" + foundVid + "|" + dopInfo + "|" + nomer + "|");
		}
			
		d.setNameAct(c.getVid() + " " + c.getNomer());
		d.setNomer(nomer);
		d.setTip(foundVid);
		d.setUrl(c.getUrl());
		d.setRemarkSR(c.getName());
		if (foundVid != null) {
			d.setTipL(precod.get(foundVid));
		}
		if (nomer != null && !nomer.isEmpty()) {
			//new WordHarmParser(vidove).recognizeNumber(d);
		}else {
			System.out.println("*** Ненамерен номер на акт: " + foundVid + " --> " + d.getNameAct() );
		}
		
		c.setDosie(d);
	}
	
	
	
	public static String getNumberFromName(String part) {
		
		char fakeSpace = (char)160;
		part = part.replace(""+fakeSpace, " ");
		part = part.replace("(EEC) No", "(ЕС) №");
		part = part.replace("(ЕО) №.", "(ЕО) №");
		
		
		
		//System.out.println("--> " + part);
		
		
		String maska = "0123456789() EOECЕОЕИОЕС/ПВР№";
		 String nomer = "";
		 for (int i = 0; i < part.length(); i++) {
			 String tek = part.substring(i, i+1);
			 //System.out.println(tek + " ---> "  + (int)tek.charAt(0));
			 if (!maska.contains(tek)) {
				 break;
			 }else {
				 nomer += tek;
			 }
		 }
		
		nomer = nomer.trim();
		nomer = nomer.replace("E", "Е");
		nomer = nomer.replace("C", "С");
		nomer = nomer.replace("O", "О");
		nomer = nomer.replace("B", "В");
		nomer = nomer.replace("P", "Р");
		
		
		if (nomer.length()<5) {
			nomer = "";
		}
		//2011/62/ЕС
		if (nomer.endsWith(" Е")) {
			nomer = nomer.substring(0, nomer.length()-2);
		}
		
		return nomer;
	}
	
	public static void initLists(ArrayList<String> vidove, HashMap<String, Integer> precod, SystemData sd ) throws DbErrorException {
		
		
		
		vidove.add("Директива за изпълнение");
		vidove.add("Регламент за изпълнение");
		vidove.add("Решение за изпълнение");
		
		
		vidove.add("Делегиран регламент");			
		vidove.add("Делегирана директива");			
		vidove.add("Делегирано решение");
		
		vidove.add("Поправка на Директива");
		vidove.add("Поправка на Регламент за изпълнение");   ///////
		vidove.add("Поправка на Делегиран регламент на Комисията"); ////////
		
		
		vidove.add("Поправка на Делегиран регламент");
		vidove.add("Поправка на Регламент");
		
		vidove.add("Директива");
		vidove.add("Регламент");
		vidove.add("Решение");
		vidove.add("Рамково решение");
		
		vidove.add("Council Directive");
		vidove.add("Modified proposal");
		vidove.add("ACT");
		vidove.add("Council Regulation");
		vidove.add("Commission Regulation");
		vidove.add("DOCUMENT");
		
		vidove.add("Протокол");
		vidove.add("Конвенция");
		vidove.add("Акт");
		vidove.add("Зелена книга");
		vidove.add("Споразумение");
		
		
		vidove.add("Втори протокол");
		vidove.add("Шеста Директива");
		vidove.add("Единадесета Директива");
		vidove.add("Second Council Directive");
		
		/////////////////////////////////
		List<SystemClassif> classif = sd.getSysClassification(EuroConstants.CODE_SYSCLASS_VID_ACT, new Date(), EuroConstants.CODE_DEFAULT_LANG);
		for (SystemClassif item : classif) {
			precod.put(item.getTekst(), item.getCode());
		}
		
		
		precod.put("Директива", EuroConstants.TIP_ACT_DIREKTIVA);
		precod.put("Регламент", EuroConstants.TIP_ACT_REGLAMENT);
		precod.put("Решение", EuroConstants.TIP_ACT_RESHENIE);
		precod.put("Рамково решение", EuroConstants.TIP_ACT_RAMKOVO_RESHENIE);
		
		precod.put("Директива за изпълнение", EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE);
		precod.put("Регламент за изпълнение", EuroConstants.TIP_ACT_REGLAMENT_ZA_IZPALNENIE);
		precod.put("Решение за изпълнение", EuroConstants.TIP_ACT_RЕSHENIE_ZA_IZPALNENIE);
		
		
		precod.put("Делегиран регламент", EuroConstants.TIP_ACT_DELEGIRAN_REGLAMENT);			
		precod.put("Делегирана директива", EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA);			
		precod.put("Делегирано решение", EuroConstants.TIP_ACT_DELEGIRANO_RESHENIE);
		
		precod.put("Поправка на Директива", EuroConstants.TIP_ACT_POPRAVKA_NA_DIREKTIVA);
		precod.put("Поправка на Регламент", EuroConstants.TIP_ACT_POPRAVKA_REGLAMENT);
		precod.put("Поправка на Делегиран регламент", EuroConstants.TIP_ACT_POPRAVKA_DEL_REGLAMENT);
		
		precod.put("Council Directive", EuroConstants.TIP_ACT_DIREKTIVA);
		precod.put("Modified proposal", EuroConstants.TIP_ACT_PREDLOJENIE);
		precod.put("ACT", EuroConstants.TIP_ACT_DRUG_AKT);
		precod.put("Council Regulation", EuroConstants.TIP_ACT_REGLAMENT);
		precod.put("Commission Regulation", EuroConstants.TIP_ACT_REGLAMENT);
		precod.put("DOCUMENT", EuroConstants.TIP_ACT_DRUG_AKT);
		
		precod.put("Протокол", EuroConstants.TIP_ACT_PROTOKOL);
		precod.put("Конвенция", EuroConstants.TIP_ACT_KONVENCIA);
		precod.put("Акт", EuroConstants.TIP_ACT_DRUG_AKT);
		precod.put("Зелена книга", EuroConstants.TIP_ACT_ZELENA_KNIGA);
		precod.put("Споразумение", EuroConstants.TIP_ACT_SPORAZUMENIE);
		
		
		precod.put("Втори протокол", EuroConstants.TIP_ACT_PROTOKOL);
		precod.put("Шеста Директива", EuroConstants.TIP_ACT_DIREKTIVA);
		precod.put("Единадесета Директива", EuroConstants.TIP_ACT_DIREKTIVA);
		precod.put("Second Council Directive", EuroConstants.TIP_ACT_DIREKTIVA);
		
		precod.put("Поправка на Регламент за изпълнение", EuroConstants.TIP_ACT_POPRAVKA_REGLAMENT_IZP);   ///////
		precod.put("Поправка на Делегиран регламент на Комисията", EuroConstants.TIP_ACT_POPRAVKA_DEL_REGLAMENT); 
	}
	
	
	
	
	public static void saveData(ArrayList<Dosie> dosieta, List<SystemClassif> pravi, SystemData sd) {
		try {
	    	JPA.getUtil().begin();
	    	
	    	JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_ACTS_CONN").executeUpdate();
	    	JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_SECTION_E").executeUpdate();
	    	JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_SECTION_Z").executeUpdate();
	    	JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_ACT_NEW").executeUpdate();
	    	JPA.getUtil().getEntityManager().createNativeQuery("delete from EURO_DOSSIER").executeUpdate();
	    	
	    	System.out.println();
	    	System.out.println("Saving Main Acts... ");
	    	for (Dosie dosie : dosieta) {
	    		System.out.print(fillGap(dosie.getFilename(),50));
				saveMainActFromDosie(dosie);
				System.out.println("\tOK");
	    	}
	    	
	    	
	    	System.out.println();
	    	System.out.println("Saving Vraz & Dossier ... ");
			for (Dosie dosie : dosieta) {
				System.out.print(fillGap(dosie.getFilename(),50));
				
				Integer idActOsn = dosie.getIdAct();
				
				Dossier d = dosie.toDossier();
				d.setEuroActNewId(idActOsn);
				new DossierDAO(ActiveUser.DEFAULT).save(d); 
				
				
				for (Container c : dosie.getAb2list()) {		    				    		
		    		if (c.getDosie() != null) {
		    			c.getDosie().setFilename(dosie.getFilename());
		    			saveMainActFromDosie(c.getDosie());
		    			Integer idActVraz = c.getDosie().getIdAct();
		    			Integer role = c.getAction();
		    			doSaveVraz(idActOsn, idActVraz, role, c.getDopInfo(),pravi, sd);
		    			
		    		}
		    	}
				
				
		    	for (Container c : dosie.getAb3list()) {		    				    		
		    		if (c.getDosie() != null) {
		    			c.getDosie().setFilename(dosie.getFilename());
		    			saveMainActFromDosie(c.getDosie());
		    			Integer idActVraz = c.getDosie().getIdAct();
		    			Integer role = c.getAction();
		    			doSaveVraz(idActOsn, idActVraz, role, c.getDopInfo(),pravi, sd);
		    		}
		    	}
		    	
		    	
		    	for (Container c : dosie.getAb4list()) {	
		    		if (c.getDosie() != null) {
		    			c.getDosie().setFilename(dosie.getFilename());
		    			saveMainActFromDosie(c.getDosie());
		    			Integer idActVraz = c.getDosie().getIdAct();
		    			Integer role = c.getAction();
		    			doSaveVraz(idActOsn, idActVraz, role, c.getDopInfo(),pravi, sd);
		    		}
		    	}
		    	
		    	
		    	for (Container c : dosie.getGlist()) {	
		    		if (c.getDosie() != null) {
		    			//System.out.println("*** Вид:" + c.getVid());
		    			c.getDosie().setFilename(dosie.getFilename());
		    			saveMainActFromDosie(c.getDosie());
		    			Integer idActVraz = c.getDosie().getIdAct();
		    			Integer role = c.getAction();
		    			doSaveVraz(idActOsn, idActVraz, role, c.getDopInfo(),pravi, sd);
		    		}
		    	}
		    	
		    	for (Container c : dosie.getElist()) {		    			
		    		
		    		EuroActNewSectionE secE = new EuroActNewSectionE();
		    		secE.setFileName(dosie.getFilename());
		    		secE.setNote(c.getName());
		    		secE.setName(c.getDopInfo());
		    		secE.setUrl(c.getUrl());
		    		secE.setEuroActNewId(idActOsn);
		    		checkAndSaveSectionE(secE);
		    	}
		    	
		    	for (Container c : dosie.getZlist()) {
		    		//System.out.println(c.getName());
		    		EuroActNewSectionZ secZ = new EuroActNewSectionZ();
		    		secZ.setFileName(dosie.getFilename());
		    		secZ.setEuroActNewId(idActOsn);
		    		secZ.setNameBgAct(c.getName());
		    		//WordHarmParser.extractDVfromZ(secZ);
		    		
		    		if (c.getName().indexOf("(") != -1) {
			    		String dvString = c.getName().substring(c.getName().indexOf("("));
			    		if (dvString.toUpperCase().contains("БР.") && dvString.toUpperCase().contains("ДВ")) {
			    			ArrayList<String> all = WordHarmParser.getNumbersFromString(dvString);
			    			if (all.size() == 2) {
			    				try {
									secZ.setDvBroi(all.get(0));
									secZ.setDvGodina(Integer.parseInt(all.get(1)));
								} catch (Exception e) {
									
								}
			    			}else {
			    				if (all.size() == 3) {
			    					//Връзката най вероятно е от вида бр Х и бр. Y от 20ZZ
			    				}
			    				
			    			}
			    		}
		    		}
		    		
		    		checkAndSaveSectionZ(secZ);
		    		
//		    		if (c.getName().indexOf("(") != -1) {
//			    		ArrayList<String> all = WordHarmParser.getNumbersFromString(c.getName().substring(c.getName().indexOf("(")));
//			    		if (all.size() != 2) {
//			    			System.out.println("");
//			    			System.out.println(c.getName() + "  ----------------------"+all.size()+"---->"  + all);
//			    		}
//		    		}else {
		    			//checkAndSaveSectionZ(secZ);
		    		//}
		    		
		    		
		    	}
		    	
		    	System.out.println("\tOK");
			}
			JPA.getUtil().commit();
		} catch (Exception e) {
			JPA.getUtil().closeConnection();
			e.printStackTrace();
		}
	}


	private static void doSaveVraz(Integer idActOsn, Integer idActVraz, Integer role, String dopInfo, List<SystemClassif> pravi, SystemData sd) throws DbErrorException {

//		System.out.println("idActOsn="+ idActOsn);
//		System.out.println("idActVraz="+ idActVraz);
//		System.out.println("role="+ role);
//		System.out.println();
		
		Integer otherRole = sd.getOtherRole(role);
		if (role != null) { 
			boolean isPrava = false;
			for (SystemClassif tekRole : pravi) {
				if (tekRole.getCode() == role.intValue()) {
					isPrava = true;
					break;
				}
			}
			
			EuroActNewConn conn = null; 
			if (isPrava) {
				conn = new EuroActNewConn(idActOsn, idActVraz, role, otherRole);
			}else {
				//Обръщаме я
				conn = new EuroActNewConn(idActVraz, idActOsn, otherRole, role);
			}
			conn.setNote(dopInfo);
			
			if (conn.getEuroActNewId1().equals(conn.getEuroActNewId2())) {
				System.out.println();
			}
			
			checkAndSaveVraz(conn);
				
			
		}
		
	}


	@SuppressWarnings("unchecked")
	private static void checkAndSaveVraz(EuroActNewConn conn) throws DbErrorException {
		
		if (conn == null) {
			return ;
		}
		
		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select id from EURO_ACTS_CONN where EURO_ACT_NEW_ID1 = :id1 and EURO_ACT_NEW_ID2 = :id2 and ROLE_ACT1 = :r1 and ROLE_ACT2 = :r2");
		q.setParameter("id1", conn.getEuroActNewId1());
		q.setParameter("id2", conn.getEuroActNewId2());
		q.setParameter("r1", conn.getRoleAct1());
		q.setParameter("r2", conn.getRoleAct2());
		
		ArrayList<Object> result = (ArrayList<Object>) q.getResultList();
		if (result.size() == 0) {
			EuroActNewConnDAO dao = new EuroActNewConnDAO(ActiveUser.DEFAULT);
			dao.save(conn);
		}else {
			if (result.size() > 1) {
				throw new DbErrorException("Дублирани връзки в ДБ !!!");
			}
		}
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	private static void checkAndSaveSectionE(EuroActNewSectionE conn) throws DbErrorException {
		
		if (conn == null) {
			return ;
		}
		
		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select id from EURO_SECTION_E where EURO_ACT_NEW_ID = :id and NAME = :IME");
		q.setParameter("id", conn.getEuroActNewId());
		q.setParameter("IME", conn.getName());
		
		
		ArrayList<Object> result = (ArrayList<Object>) q.getResultList();
		if (result.size() == 0) {
			new EuroActNewSectionEDAO(ActiveUser.DEFAULT).save(conn);
		}else {
			if (result.size() > 1) {
				throw new DbErrorException("Дублирани връзки в ДБ за секция Е !!!");
			}
		}
		
		
	}
	
	private static void checkAndSaveSectionZ(EuroActNewSectionZ conn) throws DbErrorException {
		
		if (conn == null) {
			return ;
		}
		
		
		String name = conn.getNameBgAct();
		
		
		name = name.replace("бр.1", "бр. 1");
		name = name.replace("бр.2", "бр. 2");
		name = name.replace("бр.3", "бр. 3");
		name = name.replace("бр.4", "бр. 4");
		name = name.replace("бр.5", "бр. 5");
		name = name.replace("бр.6", "бр. 6");
		name = name.replace("бр.7", "бр. 7");
		name = name.replace("бр.8", "бр. 8");
		name = name.replace("бр.9", "бр. 9");
		name = name.replace("бр.0", "бр. 0");
		
		name = name.replace("1от", "1 от");
		name = name.replace("2от", "2 от");
		name = name.replace("3от", "3 от");
		name = name.replace("4от", "4 от");
		name = name.replace("5от", "5 от");
		name = name.replace("6от", "6 от");
		name = name.replace("7от", "7 от");
		name = name.replace("8от", "8 от");
		name = name.replace("9от", "9 от");
		name = name.replace("0от", "0 от");
		
		name = name.replace("от1", "от 1");
		name = name.replace("от2", "от 2");
		name = name.replace("от3", "от 3");
		name = name.replace("от4", "от 4");
		name = name.replace("от5", "от 5");
		name = name.replace("от6", "от 6");
		name = name.replace("от7", "от 7");
		name = name.replace("от8", "от 8");
		name = name.replace("от9", "от 9");
		name = name.replace("от0", "от 0");
		
		name = name.replace("  ", " ");
		name = name.replace("  ", " ");
		name = name.replace("  ", " ");
		
		name = name.replace("“", "\"");
		
		name = name.replace("ДВ ,", "ДВ,");
		name = name.replace(",бр" ,", бр");
		
		
		name = name.replace("„", "\"");
		name = name.replace(" \"(", "\"(");
		name = name.replace(" \" (", "\" (");
		
		conn.setNameBgAct(name);
		
		
		
		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select id from EURO_SECTION_Z where EURO_ACT_NEW_ID = :id and NAME_BG_ACT = :IME");
		q.setParameter("id", conn.getEuroActNewId());
		q.setParameter("IME", name);
		
		
		ArrayList<Object> result = (ArrayList<Object>) q.getResultList();
		if (result.size() == 0) {
			new EuroActNewSectionZDAO(ActiveUser.DEFAULT).save(conn);
		}else {
			if (result.size() > 1) {
				throw new DbErrorException("Дублирани връзки в ДБ за секция Е !!!");
			}
		}
		
		
	}
	
	

}
