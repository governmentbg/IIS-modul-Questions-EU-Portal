package com.ib.euroact.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroActJournal;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.system.ActiveUser;
import com.ib.system.db.AbstractDAO;
import com.ib.system.db.PersistentEntity;
import com.ib.system.db.dto.SystemJournal;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.JAXBHelper;


/**
 * Абстрактно DAO, което да следяват всички даота в проекта на животните, заяради журнала
 */
 public abstract class EuroDAO<E extends PersistentEntity> extends AbstractDAO<E>{

	 private static final Logger LOGGER = LoggerFactory.getLogger(EuroDAO.class);
	 
	protected EuroDAO(Class<E> typeClass, ActiveUser user) {
		super(typeClass, user);
		
	}

	protected EuroDAO(ActiveUser user) {
		super(user);		
	}
	
	
	/**
	 * Реализира писането в таблицата за журнал. Пише такъв обект, какъвто е подаден. Грижа на извикващия е да си го конструира.
	 *
	 * @param journal
	 * @throws DbErrorException
	 */
	@Override
	public void saveAudit(SystemJournal journal) throws DbErrorException {
		
		if (journal == null) {
			return;
		}
		
		EuroActJournal jurBabh = new EuroActJournal(journal);
		
		LOGGER.debug("BEGIN Запис в журнала действие:{},потребител:{}", jurBabh.getCodeAction(), jurBabh.getIdUser());
		try {
			getEntityManager().persist(jurBabh);
		} catch (Exception e) {
			throw new DbErrorException(e);
		}
		LOGGER.debug("SUCCESSFUL Запис в журнала действие:{},потребител:{}", jurBabh.getCodeAction(), jurBabh.getIdUser());
	}
	
	/**
	 * Реализира писането в таблицата за журнал. Използва се при запис,корекция и изтриване.
	 *
	 * @param entity
	 * @param codeAction
	 * @throws DbErrorException
	 */
	public void saveAudit(E entity, int codeAction) throws DbErrorException {
		
		LOGGER.debug("BEGIN Запис в журнала на обект от тип:{}", this.typeClass.getSimpleName());
		try {
			//byte[] objectContent = Serializator.serialize2BArray(entity.toAuditSerializeObject(getUnitName()));

			//SystemJournal journal = new SystemJournal(getUserId(), codeAction, entity.getCodeMainObject(), entity.getId(), entity.getIdentInfo(), objectContent);
			
			String xml = JAXBHelper.objectToXml(entity, false);
			
			EuroActJournal jur = new EuroActJournal();
			jur.setIdUser(new Long(getUserId()));
			jur.setCodeAction(new Long(codeAction));
			jur.setCodeObject(new Long(entity.getCodeMainObject()));
			jur.setIdObject(((Number)entity.getId()).longValue());
			jur.setDateAction(new Date());
			jur.setIdentObject(entity.getIdentInfo());
			
			getEntityManager().persist(jur);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DbErrorException(e);
		}
		LOGGER.debug("SUCCESSFUL Запис в журнала на обект от тип:{}", this.typeClass.getSimpleName());
	}
		
	

	
	
	private static String readHttpInfo(String address) throws IOException{
		
		String result = "";
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		InputStream is = connection.getInputStream();
		byte[] buffer = new byte[1024];
		int read;
		while ((read = is.read(buffer)) != -1) {
			String tek = new String(buffer, 0, read);
			result += tek;
			
		}		
		
		is.close();
		connection.disconnect();
		
		return result;
	
	}
	
	
	private static String getNumberFromName(String part) {
		
		char fakeSpace = (char)160;
		part = part.replace(""+fakeSpace, " ");
		
		
		part = part.replace("(EEC) No", "(ЕС) №");
		
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
	
	
	private void recognizeNumber(EuroActNew act) throws InvalidParameterException {
		
		
		Integer nomerAct = null;
		Integer yearAct = null;
		
		
		if (act == null || act.getRnFull() == null) {
			return;
		}
		
		ArrayList<String> splitted = getNumbersFromString(act.getRnFull());
		if (splitted.size() != 2) {
			throw new InvalidParameterException(splitted.size() + "--------------------------------------------> НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + act.getRnFull());
		}
		
		String s1 = splitted.get(0);
		String s2 = splitted.get(1);
		int num1 = Integer.parseInt(s1);
		int num2 = Integer.parseInt(s2);
		
		if (s1.length() != 2 && s1.length() != 4 ) {
			//Сигурни сме че не е година
			nomerAct = num1;
			yearAct = num2;
		}else {		
			if (s2.length() != 2 && s2.length() != 4 ) {
				//Сигурни сме че не е година
				nomerAct = num2;
				yearAct = num1;
			}
		}
		
		
		
		
		if (yearAct == null) {
			//Почваме анализи
			
			if (num1 > 1990 && num1 < 2021) {
				//Вероятно е година
				if (num2 > 1990 && num2 < 2021) {
					// и второто изглежда като година - не знам какво правим
					throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + act.getRnFull());
				}else {
					nomerAct = num2;
					yearAct = num1;
				}
			}else {
				if (num2 > 1990 && num2 < 2021) {					
						nomerAct = num1;
						yearAct = num2;					
				}else {
					//и двете не мязат на година с 4 цифри - трябва да търсим такива с 98-ма например
					
				}
			}
		}
		
		if (yearAct == null) {
			//Ако годината все още е null
			//имаме просто 2 числа
			if (s1.length() == 2 && s2.length() != 2) {
				yearAct = num1;
				nomerAct = num2;
			}
			
			if (s2.length() == 2 && s1.length() != 2) {
				yearAct = num2;
				nomerAct = num1;
			}
			
			if (yearAct == null) {
				yearAct = num1;
				nomerAct = num2;
			}
		}
		
		
	//	if (yearAct == null) {
	//		//Ако и сега не сме я разпознали ..... гръмим
	//		throw new InvalidParameterException("ГОДИНИ--------------------------------------------> НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getNomer());
	//	}
		
		
		//Анализ на годината
		if (yearAct < 100 && yearAct > 50) {
			yearAct += 1900;
		}else {
			if (yearAct < 22) {
				yearAct += 2000;
			}
		}
		
		
		
		act.setGodina(yearAct);
		act.setRn(nomerAct);
	}
	
	
	private  ArrayList<String> getNumbersFromString(String tekst){
		String numbers = "0123456789";
		boolean hasQuote = false;
		
		ArrayList<String> foundStrings = new ArrayList<String>();
		
		if (tekst == null) {
			return foundStrings;
		}else {
			//Това е когато завършва на цифра да не се налага след цикъла да правя анализ ;)
			tekst += ";"; 
		}
		
		
		String num = "";
		for(int i = 0; i < tekst.length(); i++) {
			
			if ("\"".equals(""+tekst.charAt(i))) {
				hasQuote = !hasQuote;
				num = "";
				continue;
				
			}
			
			if (!hasQuote) {
				String tek = ""+tekst.charAt(i);
			    if (numbers.contains(tek)) {
			    	num += tek; 
			    }else {
			    	if (num.length() > 0) {
			    		//System.out.println("Adding " + num);
			    		foundStrings.add(num);
			    		num = "";				    		
			    	}
			    }
			}
		}
		
		return foundStrings;
		
		
	}
	
}