package com.ib.euroact.migration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.mail.Part;
import javax.persistence.Query;

import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.FileUtils;
import com.ib.system.utils.SearchUtils;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;

public class TestAspose {

//	@SuppressWarnings("rawtypes")
//	public static void main(String[] args) {
//		
//		//https://eur-lex.europa.eu/search.html?DTN=0849&DTA=2015&DB_TYPE_OF_ACT=directive&type=advanced&lang=bg
//		
//		
//		
//		
//		try {
//			
//			byte[] byteLic = FileUtils.getBytesFromFile(new File("D:\\Bullshit.iv"));
//			ByteArrayInputStream bisL = new ByteArrayInputStream(byteLic);
//				
//			com.aspose.words.License license = new com.aspose.words.License();
//			license.setLicense(bisL);
//			
//			
//			//System.out.println("--->:>>>" + JPA.getUtil().getEntityManager().createNativeQuery("select 1 from dual").getResultList().size());
//			
//			ArrayList<String> fileNames = new ArrayList<String>();
//			
//			//String dirName = "D:\\_HARMONIZACIA\\2. ЗДАНС ДВ_27_2018  Д 2015-849.docx";
//			String dirName = "D:\\_HARMONIZACIA\\";
//			
//			
//			File f = new File(dirName);
//		    if (f.exists()) {
//		    	if (f.isDirectory()) {
//		    		File[] listOfFiles = f.listFiles();
//		    		for (File tekF : listOfFiles) {
//		    			if (!tekF.isDirectory()) {
//		    				fileNames.add(tekF.getCanonicalPath());
//		    			}
//		    		}
//		    	}else {
//		    		fileNames.add(dirName);
//		    	}
//		    }
//			
//		    int brErrors = 0;
//		    int brOk = 0;
//		    for (String fileName : fileNames) {
//		    	try {
//					System.out.println(fileName);
//					byte[] bytes = FileUtils.getBytesFromFile(new File(fileName));
//					ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//					Document doc = new Document(bis);
//					parseFile(fileName, doc);
//					brOk ++;
//				} catch (Exception e) {
//					brErrors++;
//					e.printStackTrace();
//				}
//		    }
//		    
//		    System.out.println("Брой обработени коректно: " + brOk);
//		    System.out.println("Брой обработени с грешка: " + brErrors);
//		    
//			
//			
//			
//			
//	
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	public static void parseFile(String fileName, Document doc) throws InvalidParameterException {
//		
//			Dosie dosie = new Dosie();
//			
//			//System.out.println(doc.getText());
//			
//			
//			NodeCollection paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);
//			//System.out.println("Paragraphs: " + paragraphs.getCount());
//			//System.out.println(paragraphs.get(4).getText());
//			boolean foundID = false;
//			String parText = null;
//			for (int p = 0; p < paragraphs.getCount(); p++) {
//				if (paragraphs.get(p).getText().contains("ИНФОРМАЦИОННО ДОСИЕ")) {						
//					foundID = true;
//					continue;
//				}
//				if (foundID) {
//					parText = paragraphs.get(p).getText().trim();
//					if (!parText.isEmpty()) {
//						
//						//Намерили сме го. Сега трябва да видим дали не е разцепен на 2 параграфа
//						if (!paragraphs.get(p+1).getText().trim().isEmpty()) {
//							parText += " " + paragraphs.get(p+1).getText().trim();
//							
//						}
//						
//						
//						break;
//					}
//				}
//				
//			}
//			findMainDataFromText(parText, fileName, dosie);
//			
//			ArrayList<String> sections = new ArrayList<String>();
//			NodeCollection tables = doc.getChildNodes(NodeType.TABLE, true);
//			//System.out.println("Tables: " + tables.getCount());
//			if (tables.getCount() != 1) {
//				throw new InvalidParameterException(fileName + ": Броя на таблиците във файла е  " + tables.getCount() );
//			}
//			
//			Node table = tables.get(0);
//			NodeCollection rows = ((CompositeNode<Node>) table).getChildNodes(NodeType.ROW, true);
//			//System.out.println("Rows: " + rows.getCount());
//			String section = null;
//			for (int r = 0; r < rows.getCount(); r++) {
//				Node row = rows.get(r);
//				NodeCollection cells = ((CompositeNode<Node>) row).getChildNodes(NodeType.CELL, true);
//				//System.out.println("------------------------------");
//				//System.out.println("Cell Size:" + cells.getCount());
//				
//				if (cells.getCount() != 2) {
//					throw new InvalidParameterException(fileName + ": Броя на колоните в ред на таблица е  " + cells.getCount() );
//				}
//				
//				String cell1 = cells.get(0).getText();
//				String cell2 = cells.get(1).getText();
//				cell2 = cell2.replace("", "");
//				cell1 = cell1.replace("", "");
//				//System.out.println(cell1);
//				if (cell1 != null && !cell1.trim().isEmpty()) {
//					section = cell1.trim();
//					
//					//Малко корекции - кирилица/латиница
//					if (section.equals("A.")) {
//						System.out.println("Корекцияяяяяяяяяяяяяяяяяяяяя");
//						section = "А.";
//					}
//					
//				}
//				parseCell(section, (CompositeNode<Node>)cells.get(1), dosie);
//				
//				
////				System.out.println(cell1);
////				System.out.println(cell2);
//				//System.out.println("------------------------------");
//				
//			}
//			
//			//System.out.println("Брой секции: " + sections.size());
//			
//			if (dosie.getCelexNumber() == null && dosie.getUrl() != null) {
//				proccessUrl(dosie);
//			}
//			
//			
//			
//			System.out.println("----------------------------------------------------------------------------------------------------");
//			System.out.println("Тип: " + dosie.getTip());
//			System.out.println("Закон: " + dosie.getImeZakon());
//			System.out.println("Брой ДВ: " + dosie.getBroiDV());
//			System.out.println("Година ДВ: "+ dosie.getYearDv());
//			System.out.println("Закон Ид: "+ dosie.getIdZ());
//			System.out.println("Номер: " + dosie.getNomer());
//			System.out.println("URL: " + dosie.getUrl());
//			System.out.println("CelexNumber: " + dosie.getCelexNumber());
//						
//			if (dosie.getNomer() == null) {				
//				throw new InvalidParameterException("Не се намира номер на досие !!!");
//			}
//			
//			if (dosie.getUrl() == null) {				
//				throw new InvalidParameterException("Не се намира URL на досие !!!");
//			}
//			
//			if (dosie.getCelexNumber() == null) {				
//				throw new InvalidParameterException("Не се намира CelexNumber на досие !!!");
//			}
//			
//			System.out.println("----------------------------------------------------------------------------------------------------");
//			System.out.println();
//			System.out.println();
//			
//			
//			
//			
//			
//		
//		
//		
//	}
//	
//	
//	
//	
//	
//	public static ArrayList<String> getNumbersFromString(String tekst){
//		String numbers = "0123456789";
//		boolean hasQuote = false;
//		
//		ArrayList<String> foundStrings = new ArrayList<String>();
//		
//		String num = "";
//		for(int i = 0; i < tekst.length(); i++) {
//			
//			if ("\"".equals(""+tekst.charAt(i))) {
//				hasQuote = !hasQuote;
//				num = "";
//				continue;
//				
//			}
//			
//			if (!hasQuote) {
//				String tek = ""+tekst.charAt(i);
//			    if (numbers.contains(tek)) {
//			    	num += tek; 
//			    }else {
//			    	if (num.length() > 0) {
//			    		//System.out.println("Adding " + num);
//			    		foundStrings.add(num);
//			    		num = "";				    		
//			    	}
//			    }
//			}
//		}
//		
//		return foundStrings;
//		
//		
//	}
//	
//	
//	public static void findMainDataFromText(String parText, String fileName, Dosie dosie) throws InvalidParameterException {
//		
//		
//		
//		if (parText == null) {
//			throw new InvalidParameterException(fileName + ": НЕ СЕ ОТКРИВА ПАРАГРАФА С ИМЕТО НА ЗАКОНОПРОЕКТА !!!!" );				
//		}
//		
//		
//		//Оправяме бозите при въвеждане
//		parText = parText.replace("„", "\"");
//		parText = parText.replace("“", "\"");
//		parText = parText.replace("a", "а");
//		parText = parText.replace("  ", " ");
//		
//		if (parText.contains("§")) {
//			//Има параграф, който изменя
//			parText = parText.substring(parText.indexOf("§"));
//			int ind = parText.toUpperCase().indexOf("ЗАКОН");
//			if (ind!= -1) {
//				parText = parText.substring(ind);				
//			}
//			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>" + parText);
//		}
//		
//		
//		
//		
//		//Опитваме да намерим броя и годината на ДВ
//		int beginIndex = parText.indexOf("(");
//		int endIndex = parText.indexOf(")");
//		if (beginIndex == -1 || endIndex == -1) {
//			throw new InvalidParameterException(fileName + ": НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ ЗА БРОЯ НА ДВ !!!! --> " + parText );
//		}
//		
//		String restString = parText.substring(endIndex);
//		String tip = null;
//		//регламент, директива, решение, рамково решение.
//		if (restString.toUpperCase().contains("РАМКОВО РЕШЕНИЕ")) {
//			tip = "Рамково решение";
//		}else {		
//			if (restString.toUpperCase().contains("ДИРЕКТИВА")) {
//				tip = "Директива";
//			}else {
//				if (restString.toUpperCase().contains("РЕГЛАМЕНТ")) {
//					tip = "Регламент";
//				}else {
//					if (restString.toUpperCase().contains("РЕШЕНИЕ")) {
//						tip = "Решение";
//					}
//				}
//			}
//		}
//		
//		
//		
//		
//		String dvString = parText.substring(beginIndex+ 1, endIndex).trim();
//		//System.out.println("------------------------------------------------->" + dvString);
//		
//		ArrayList<String> foundStrings = getNumbersFromString(dvString);
//		
//		if (foundStrings.size() != 2) {
//			throw new InvalidParameterException(fileName + ": НЕ МОЖЕ ДА СЕ ПАРСНЕ БРОЙ И ГОДИНА НА ДВ !!!! --> " + dvString  );				
//		}
//		
//		String broiDV = foundStrings.get(0);
//		String yearDv = foundStrings.get(1);
//		
//		//System.out.println("P--->" + parText);
//		String imeZakon = parText.substring(0, parText.indexOf("("));
//		
//		
//		//Понеже някой започват с "на Закона", а други с "на Закон", ще трябва да го направим така че да е само "Закон"
//		if (imeZakon.startsWith("на Закона")) {
//			imeZakon = imeZakon.replaceFirst("на Закона", "Закон");
//		}else {					
//			if (imeZakon.startsWith("на Закон")) {
//				imeZakon = imeZakon.replaceFirst("на Закон", "Закон");
//			}else {
//				if (imeZakon.startsWith("на Гражданския")) {
//					imeZakon = imeZakon.replaceFirst("на Гражданския", "Граждански");
//				}else {
//					if (imeZakon.startsWith("за Закона")) {
//						imeZakon = imeZakon.replaceFirst("за Закона", "Закон");
//					}else {
//						if (imeZakon.startsWith("Закона")) {
//							imeZakon = imeZakon.replaceFirst("Закона", "Закон");
//						}
//					}
//				}
//			}
//		}
//		imeZakon = imeZakon.trim();
//		
//		
//		//ТЪРСЕНЕ НА ЗАКОНА В БД
////		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select id, BROI_DV from ZD_ZAKONOPROEKTI where upper(IME_ZAKON) = :IME");
////		q.setParameter("IME", imeZakon.toUpperCase());
////		
////		ArrayList<Object[]> results = (ArrayList<Object[]>) q.getResultList();
////		if (results.size() == 0) {
////			throw new InvalidParameterException(fileName + ": НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ ! --> |" + imeZakon + "|"  );
////		}
////		Long idZ = null;
////		for (Object[] row : results) {
////			Long id = SearchUtils.asLong(row[0]);
////			String dvStr = SearchUtils.asString(row[1]);
////			
////			if (dvStr != null) {
////				ArrayList<String> foundStrings2 = getNumbersFromString(dvString);					
////				if (foundStrings2.size() != 2) {
////					continue;				
////				}
////				String broiDV_DB = foundStrings2.get(0);
////				String yearDv_DB = foundStrings2.get(1);
////				if (broiDV_DB.equals(broiDV) && yearDv_DB.equals(yearDv)) {
////					idZ = id;
////					break;
////				}
////				
////			}else {
////				continue;
////			}			
////		}
//		
////		if (idZ == null) {
////			throw new InvalidParameterException(fileName + ": НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ И ДВ ! --> " + parText );
////		}
//		
//		
//		
//		
//		
//		dosie.setBroiDV(broiDV);
//		//dosie.setIdZ(idZ);
//		dosie.setImeZakon(imeZakon);
//		dosie.setTip(tip);
//		dosie.setYearDv(yearDv);
//		
//		
//		
//	
//	}
//	
//	
//	public static ArrayList<String> parseCell(String section, CompositeNode<Node> node, Dosie dosie) throws InvalidParameterException {
//		
//		ArrayList<String> result = new ArrayList<String>();
//		String current = "";
//		String point = "";
//		NodeCollection pars = node.getChildNodes(NodeType.PARAGRAPH, true);
//		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>" + pars.getCount());
//		for (int p = 0; p < pars.getCount(); p++) {
//			Node par = pars.get(p);
//			String textPar = par.getText().trim();
//			//System.out.println(section + " " +  textPar);
//			
//			if (textPar.isEmpty()) {
//				continue;
//			}
//			
//			
//			if ("123456789".contains(textPar.substring(0,1))) {
//				if (!current.isEmpty()) {
//					//System.out.println("Adding " + section + " " + point);
//					result.add(current);
//				}
//				current = textPar;
//				int pointIndex = textPar.indexOf(".");
//				if (pointIndex != -1 && pointIndex < 4) {
//					point = textPar.substring(0,pointIndex+1);
//				}
//				
//			}else {
//				current+="\r\n" + textPar;
//			}
//			
//			
//			//Малко разпознавания
//			if ((section.equals("А.") || section.equals("Б."))  && point.equals("1.") && textPar.trim().toUpperCase().startsWith("HYPERLINK")) {
//				String url = parseHyperLink(textPar);
//				dosie.setCelexNumber(getCelexFromUrl(url));
//				dosie.setUrl(url);
//			}
//			
//			if ((section.equals("А.") || section.equals("Б."))  && point.equals("1.") && textPar.trim().toUpperCase().startsWith("ТЕКСТ НА ")) {
//				//System.out.println("======================>>>>>>>>>>" + textPar);
//				
//				if (dosie.getTip() == null) {
//					//Ще се опитаме да поправим стари пропуски
//					if (textPar.toUpperCase().contains("ТЕКСТ НА ДИРЕКТИВА")) {
//						dosie.setTip("Директива");
//					}else {
//						if (textPar.toUpperCase().contains("ТЕКСТ НА РАМКОВО РЕШЕНИЕ")) {
//							dosie.setTip("Рамково решение");
//						}else {
//							if (textPar.toUpperCase().contains("ТЕКСТ НА РЕГЛАМЕНТ")) {
//								dosie.setTip("Регламент");
//							}else {
//								if (textPar.toUpperCase().contains("ТЕКСТ НА РЕШЕНИЕ")) {
//									dosie.setTip("Решение");
//								}else {
//									throw new InvalidParameterException("Неразпознат тип на досие !!!!!");
//								}
//							}
//						}
//					}
//					
//				}
//				
//				
//				String searhText = "ТЕКСТ НА " + dosie.getTip().trim(); 
//				String num = textPar.trim().substring(searhText.length()+1);
//				if (num.endsWith(":")) {
//					num = num.substring(0,num.length()-1);
//				}
//				dosie.setNomer(num);
//			}
//			
//			
//			
//		}
//		if (!current.isEmpty()) {
//			result.add(current);
//		}
//		
////		for (String s : result) {
////			System.out.println(section + " " +  s);
////		}
//		
//		return result;
//	}
//	
//	
//	public static String parseHyperLink(String hyperLink) {
//		if (hyperLink == null) {
//			return null;
//		}
//		
//		
//		int i1 = hyperLink.indexOf("\"");
//		int i2 = hyperLink.substring(i1+1).indexOf("\"");
//		
//		if (i1 == -1 || i2 == -1 || i1==i2 ) {
//			return null;
//		}
//		
//		return hyperLink.substring(i1+1, i1+i2+1);
//	}
//	
//	
//	public static String getCelexFromUrl(String url) {
//		
//		if (url == null) {
//			return null;
//		}
//		
//		url = url.replace("%", ":");
//		
//		String celex = null;
//		if (!url.contains("CELEX:")) {
//			return null;
//		}
//		
//		celex =  url.substring(url.indexOf("CELEX:")+6);
//		int specIndex = celex.indexOf("&");
//		if (specIndex != -1) {
//			celex = celex.substring(0,specIndex);
//		}
//		
//		return celex;
//	}
//	
//	
//public static String proccessCelexNumber(String celexNumber) {
//		
//		String baseURL = "https://eur-lex.europa.eu/legal-content/BG/TXT/?uri=CELEX:";	
//		String url = baseURL + celexNumber;
//		
//		com.itextpdf.styledxmlparser.jsoup.nodes.Document doc;
//		try {
//			doc = Jsoup.parse(readHttpInfo(url));
//		} catch (IOException e) {
//			return "Не може да се открие страница за този CELEX номер !";
//		}
//		
//		//<div class="panel-body" lang="BG">
//		
//		Element title = doc.getElementById("translatedTitle");
//		
//		if (title != null) {
//			System.out.println("CELEX Номер:\t" + celexNumber);
//			System.out.println("CELEX URL:\t" + url);
//			System.out.println("Наименование:\t" + title.text());
//			return title.text();
//		}else {
//			return "Няма намерено наименование !";
//		}
//		
//	}
//	
//	
//	public static void proccessUrl(Dosie dosie) throws InvalidParameterException {
//		
//		System.out.println(dosie.getUrl());
//		com.itextpdf.styledxmlparser.jsoup.nodes.Document doc;
//		try {
//			doc = Jsoup.parse(readHttpInfo(dosie.getUrl()));
//		} catch (IOException e) {
//			throw new InvalidParameterException("Не може да се открие страница за този CELEX номер !");
//		}
//		
//		//<div class="panel-body" lang="BG">
//		
//		Element title = doc.getElementById("translatedTitle");
//		Elements noms = doc.getElementsByClass("DocumentTitle");
//		System.out.println("Size:" + noms.size());
//		String celexNumber = noms.get(0).text();
//		if (celexNumber.startsWith("Document ")) {
//			celexNumber = celexNumber.substring(9);
//		}
//		if (celexNumber.startsWith("Документ ")) {
//			celexNumber = celexNumber.substring(9);
//		}
//		
//		dosie.setCelexNumber(celexNumber);
//		
////		if (title != null) {
////			System.out.println("CELEX Номер:\t" + celexNumber);
////			System.out.println("CELEX URL:\t" + dosie.getUrl());
////			System.out.println("Наименование:\t" + title.text());
////			return title.text();
////		}else {
////			return "Няма намерено наименование !";
////		}
//		
//	}
//	
//	
//	
//	
//	public static String readHttpInfo(String address) throws IOException{
//		
//		String result = "";
//		URL url = new URL(address);
//		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//		connection.setRequestMethod("GET");
//		connection.connect();
//		
//		InputStream is = connection.getInputStream();
//		byte[] buffer = new byte[1024];
//		int read;
//		while ((read = is.read(buffer)) != -1) {
//			String tek = new String(buffer, 0, read);
//			result += tek;
//			
//		}		
//		
//		is.close();
//		connection.disconnect();
//		
//		return result;
//	
//	}

}
