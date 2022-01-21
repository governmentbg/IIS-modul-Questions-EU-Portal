package com.ib.euroact.migration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Query;



import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Run;
import com.ib.euroact.db.dto.EuroActNewSectionZ;
import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.SearchUtils;

@SuppressWarnings("unchecked")
public class WordHarmParser {
	
	//public static ArrayList<String> vidove = new ArrayList<String>();
	

	public WordHarmParser(ArrayList<String> vidove) {

		super();
		//this.vidove = vidove;
	}


	//public ArrayList<Container> global = new ArrayList<Container>();
	
	
	
	public  Dosie parseFile(Document doc) throws InvalidParameterException {
		
		
		
		
		
			Dosie dosie = new Dosie();
			
			
			
			
			//Извличаме час от основните данни
			findMainDataFromText(doc, dosie);
			
						
			//Обработка на таблицата
			NodeCollection<Node> tables = doc.getChildNodes(NodeType.TABLE, true);
			//System.out.println("Tables: " + tables.getCount());
			if (tables.getCount() != 1) {
				
				//System.out.println("1***************" + tables.get(0).getText());
				//System.out.println("2***************" + tables.get(1).getText());
				 
				throw new InvalidParameterException("Броят на таблиците във файла е  " + tables.getCount() );
			}
			
			Node table = tables.get(0);
			
			//Анализиране на таблицата и редовете
			HashMap<String, Node> rowsMap  = processTable(table, dosie ); 
			if (rowsMap.size() != 9) {
				throw new InvalidParameterException("Броят на секциите във файла е различен от 9 -> " + rowsMap.size() );
			}
			
			
			recognizeNumber(dosie);
			
			
			
			
			//HOT FIXES
			if (dosie.getNomer() != null) {
				dosie.setNomer(dosie.getNomer().trim());
//				if (dosie.getNomer().contains("за изпълнение ")) {
//					dosie.setNomer(dosie.getNomer().replace("за изпълнение ", ""));
//				}
				
				//HOT FIXES
				if (dosie.getNomer().contains("на Комисията ")) {
					dosie.setNomer(dosie.getNomer().replace("на Комисията ", ""));
				}
			}
			
			
//			if (dosie.getNomer().equals("92/83/ЕИО")) {
//				System.out.println();
//			}
			
			return dosie;
			
		
		
		
	}
	
	
	
	
	
	
	private HashMap<String, Node> processTable( Node table, Dosie dosie) throws InvalidParameterException {
		
		HashMap<String, Node> rowsMap = new HashMap<String, Node>();
		
		NodeCollection<Node> rows = ((CompositeNode<Node>) table).getChildNodes(NodeType.ROW, true);
		
		//System.out.println("Rows: " + rows.getCount());
		String section = null;
		for (int r = 0; r < rows.getCount(); r++) {
			Node row = rows.get(r);
			NodeCollection<Node> cells = ((CompositeNode<Node>) row).getChildNodes(NodeType.CELL, true);
						
			if (cells.getCount() != 2) {
				throw new InvalidParameterException("Броя на колоните в ред на таблица е  " + cells.getCount() );
			}
			
			String cell1 = cells.get(0).getText(); //Номер на секция
			String cell2 = cells.get(1).getText(); //Съдържание
			
			//Чистим боклуци
			if (cell1 != null && !cell1.trim().isEmpty()) {
				cell1 = cell1.replace("", "");
				section = cell1.trim();
				if (section.length() == 1) {
					section += "."; 
				}
				//Малко корекции - кирилица/латиница
				if (section.equals("A.")) {					
					section = "А.";
				}	
				if (section.equals("B.")) {					
					section = "В.";
				}
				if (section.equals("E.")) {					
					section = "Е.";
				}
				if (section.equals("3.")) {					
					section = "З.";
				}
			}
			
			if (cell2 != null && !cell2.trim().isEmpty()) {
				cell2 = cell2.replace("", "");
			}
			
			
			if (section.equals("А.")) {
				parseSectionA(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
						
			if (section.equals("Б.")) {
				parseSectionB(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("В.")) {
				parseSectionV(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("Г.")) {
				parseSectionG(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("Д.")) {
				parseSectionD(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("Е.")) {
				parseSectionE(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("Ж.")) {
				parseSectionJ(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("З.")) {
				parseSectionZ(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
			
			if (section.equals("И.")) {
				parseSectionI(cells.get(1), dosie);
				rowsMap.put(section, cells.get(1));
			}
		}
		
		return rowsMap;
	}


	
	private void parseSectionA(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'А' НА ФАЙЛА !" );	
		}
		
		
		if (dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE || dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA || dosie.getTipL() == EuroConstants.TIP_ACT_RAMKOVO_RESHENIE ) {
			parseSectionABLong(cell, dosie);
		}else {
			parseSectionABShort(cell, dosie);
		}
	}


	private void parseSectionB(Node cell, Dosie dosie) throws InvalidParameterException {	
		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'Б' НА ФАЙЛА !" );	
		}
		
		
		if (dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE || dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA || dosie.getTipL() == EuroConstants.TIP_ACT_RAMKOVO_RESHENIE ) {
			parseSectionABShort(cell, dosie);
		}else {
			parseSectionABLong(cell, dosie);
		}
	}
	

	private void parseSectionABShort(Node cell, Dosie dosie) throws InvalidParameterException {
		
				
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		String fullPoint = "";
		for (int p = 0; p < pars.getCount(); p++) {
			Node par = pars.get(p);
			fullPoint += par.getText() + "\r\n"; 
		}
		fullPoint = fullPoint.replace("  ", " ");
		//System.out.println(fullPoint);
		boolean var1 = false;
		boolean var2 = false;
		 
		if (dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE || dosie.getTipL() == EuroConstants.TIP_ACT_DIREKTIVA || dosie.getTipL() == EuroConstants.TIP_ACT_RAMKOVO_RESHENIE ) {
			var1 = fullPoint.contains("не предвижда мерки ") || fullPoint.contains(" не се предвиждат мерки ") || fullPoint.contains(" не предвиждат мерки ");
			if (!var1) {
				var2 = fullPoint.contains("предвижда мерки") || fullPoint.contains(" се предвиждат мерки ") || fullPoint.contains(" предвижда мярка ") || fullPoint.contains(" предвижда и мерки по ");
			}
					
		}else {
			var1 = fullPoint.contains("не се въвеждат изисквания") || fullPoint.contains("не въвежда изисквания");
			if (!var1) {
				var2 = fullPoint.contains("въвежда изисквания на") || fullPoint.contains("въвежда изискванията на") || fullPoint.contains("се въвеждат изисквания на ");
			}
		}
		
		if (! var1 && !var2) {
			
			throw new InvalidParameterException("Не може да се разпознае информацията късата версия на секция А/Б " );	
		}
		
		
//		if (var2) {
//			System.out.println();
//			System.out.println("Имааааа");
//		}
		
		
		dosie.setHasMerkiIziskvania(var2); 
	}





	
	private void parseSectionABLong(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		
		HashMap<String, ArrayList<Node>> splittedInfo = splitCellByNumbers((CompositeNode<Node>)cell);
		
		//////////////////////////////////////////// 	ТОЧКА 1		//////////////////////////////////////////// 
		
		ArrayList<Node> point1 = splittedInfo.get("1.");
		if (point1 == null || point1.size() == 0) {
			throw new InvalidParameterException("Не се намира точка АБ.1" );	
		}
		
		//От точка 1 извличаме URL и номер на акт. После от URL-то ще се опитам да взема името
		String firstLine = null;
		String nomer = null;
		
		char fakeSpace = (char)160;
		
		for (int i = 0; i < point1.size(); i++) {
			
			Node curNode = point1.get(i);
			
			String tekstP = curNode.getText().trim().toUpperCase();
			tekstP = tekstP.replace("  ", " ");
						
			tekstP = tekstP.replace("EИО", "ЕИО");
			
			tekstP = tekstP.replace("EO", "ЕО");
			tekstP = tekstP.replace("EО", "ЕО");
			tekstP = tekstP.replace("ЕO", "ЕО");
			
			tekstP = tekstP.replace("EC", "ЕС");
			tekstP = tekstP.replace("ЕC", "ЕС");
			tekstP = tekstP.replace("EС", "ЕС");
			
			
			if (tekstP.startsWith("1.")) {				
				firstLine = curNode.getText();
				
				String secondLine = point1.get(i+1).getText().trim();
				if (!secondLine.isEmpty() && !secondLine.startsWith("Текст на ")) {
//					System.out.println();
//					System.out.println("*********************************************************************" + secondLine);
					firstLine += " " + secondLine;
				}
				
				
				firstLine = firstLine.replace("EC", "ЕС");
				firstLine = firstLine.replace("ЕC", "ЕС");
				firstLine = firstLine.replace("EС", "ЕС");
				
				firstLine = firstLine.replace("EИО", "ЕИО");
				
				firstLine = firstLine.replace("EO", "ЕО");
				firstLine = firstLine.replace("EО", "ЕО");
				firstLine = firstLine.replace("ЕO", "ЕО");
				
				
				//Още малко странности
				firstLine = firstLine.replace(""+fakeSpace, " ");  //Тук  160 --> 32
				firstLine = firstLine.replace("  ", " ");
				
			}
			
			if (tekstP.startsWith("Текст на директива за изпълнение".toUpperCase())) {	
				tekstP = tekstP.replace("Текст на директива за изпълнение".toUpperCase(), "").trim();
				nomer = tekstP.replace(":","").trim();
			}else {
				if (tekstP.startsWith("Текст на решение за изпълнение".toUpperCase())) {	
					tekstP = tekstP.replace("Текст на решение за изпълнение".toUpperCase(), "").trim();
					nomer = tekstP.replace(":","").trim();
				}else {
					if (tekstP.startsWith("Текст на регламент за изпълнение".toUpperCase())) {	
						tekstP = tekstP.replace("Текст на регламент за изпълнение".toUpperCase(), "").trim();
						nomer = tekstP.replace(":","").trim();
					}else {
						if (tekstP.startsWith("Текст на Директива".toUpperCase())) {				
							tekstP = tekstP.replace("Текст на Директива".toUpperCase(), "").trim();
							nomer = tekstP.replace(":","").trim();
						}else {
							if (tekstP.startsWith("Текст на Рамково решение".toUpperCase())) {
								tekstP = tekstP.replace("Текст на Рамково решение".toUpperCase(), "").trim();
								nomer = tekstP.replace(":","").trim();
							}else {
								if (tekstP.startsWith("Текст на Регламент".toUpperCase())) {				
									tekstP = tekstP.replace("Текст на Регламент".toUpperCase(), "").trim();
									nomer = tekstP.replace(":","").trim();
								}else {							
									if (tekstP.startsWith("Текст на Делегиран регламент".toUpperCase())) {				
										tekstP = tekstP.replace("Текст на Делегиран регламент".toUpperCase(), "").trim();
										nomer = tekstP.replace(":","").trim();
									}else {
										if (tekstP.startsWith("Текст на Решение".toUpperCase())) {				
											tekstP = tekstP.replace("Текст на Решение".toUpperCase(), "").trim();
											nomer = tekstP.replace(":","").trim();
										}else {
											if (isHyperLink(tekstP)) {
												dosie.setUrl(parseHyperLink(curNode.getText().trim()));
											}
										}
									}							
								}
							}
						}
					}
				}
				
			}
		}
		
		String dop = "";
		if (nomer == null) {				
			throw new InvalidParameterException("Не се намира номер на акт !!!");
		}else {
			nomer = nomer.replace("EС", "ЕС");
			nomer = nomer.replace("EO", "ЕО");
			nomer = nomer.replace("EО", "ЕО");
			nomer = nomer.replace("ЕO", "ЕО");
			nomer = nomer.replace("EИО", "ЕИО");
			nomer = nomer.replace(""+fakeSpace, " ");
			nomer = nomer.replace("ЕВРАТОМ", "Евратом");
			
			if (nomer.contains("НА КОМИСИЯТА ")) {
				nomer = nomer.replace("НА КОМИСИЯТА ", "");
				dop = " на Комисията";
			}
			
			
			dosie.setNomer(nomer);
					
		}
		
		String searchText = dosie.getTip() + dop + " " + dosie.getNomer();
		
		int begName = firstLine.indexOf(searchText);
//		System.out.println((int)firstLine.charAt(109));
//		
//		String t1 = firstLine;
//		String t2 = dosie.getTip() + " " + dosie.getNomer();
//		for (int i = 93; i < 119; i ++) {
//			System.out.println(t1.charAt(i) + "\t" + t2.charAt(i-93) + "\t" + (int)t1.charAt(i)+ "\t" + (int)t2.charAt(i-93));
//		}
		
		
		if (begName == -1) {
			throw new InvalidParameterException("Не се намира начало на наименование на акт !!!");
		}else {
			dosie.setNameAct(firstLine.substring(begName).replace("\r", ""));
		}
		
		
		
		if (dosie.getUrl() == null) {				
			throw new InvalidParameterException("Не се намира URL на досие !!!");
		}else {
			//proccessUrl(dosie);
		}
		
		if (dosie.getNameAct() == null) {				
			throw new InvalidParameterException("Не се намира наименование на акт от euro-lex !!!");
		}
		
//		if (hasPrint) {
//			System.out.println("Име на акт: " + dosie.getNameAct());
//			System.out.println("URL " + dosie.getUrl());
//			System.out.println("Номер на акт: " + dosie.getNomer());
//		}
		
		
		//Правим една проверка дали урл-то не е объркано. Ще потърсим номера в наименованието
//			if (!dosie.getNameAct().contains(dosie.getNomer()) ) {
//				throw new InvalidParameterException(fileName + "Номерът на акта не се среща в извлеченото име на акт от euro-lex !!!");
//				
//			}
		
		//////////////////////////////////////////// 	ТОЧКА 2		////////////////////////////////////////////
		
		ArrayList<Node> point2 =  splittedInfo.get("2.");
		
//			for (Node p : point2) {
//				System.out.println("->" + p.getText());
//			}
		
		if (point2 == null || point2.size() == 0) {
			throw new InvalidParameterException("Не се намира точка АБ.2" );	
		}
		
		//не отменя, заменя или изменя
		//не изменя, заменя или отменя
		//не се отменя, заменя или изменя 
		boolean foundNotText = false;
		boolean hasRemark = false;
		boolean hasHyperLinks = false;
		ArrayList<Node> remarks = new ArrayList<Node>();
		//Търсим стандартните текстове за НЕ
		for (Node node : point2) {
			String textNode = node.getText();
			textNode = textNode.replace("  ", " ");
			
			//Трябва да е първо заради хиперлинковете
			if (hasRemark) {
				remarks.add(node);
				continue;
			}
			
			if (textNode.toUpperCase().contains("не отменя, заменя или изменя".toUpperCase() )) {
				foundNotText = true;
				continue;
			}
			if (textNode.toUpperCase().contains("не се отменя, заменя или изменя".toUpperCase() )) {
				foundNotText = true;	
				continue;
			}
			if (textNode.toUpperCase().contains("не изменя, заменя или отменя".toUpperCase() )) {
				foundNotText = true;	
				continue;
			}
			
			if (textNode.toUpperCase().contains("не заменя, отменя или отменя".toUpperCase() )) {
				foundNotText = true;	
				continue;
			}

			if (textNode.toUpperCase().contains("забележка:".toUpperCase() )) {
				hasRemark = true;
				continue;
			}
			
			if (isHyperLink(textNode)) {
				hasHyperLinks = true;
				continue;
			}
			
			
			
		}
		
		if (hasHyperLinks && foundNotText) {
			//открити са думичките че няма, но има линкове извън забележката
			throw new InvalidParameterException("в точка АБ.2 са открити думичките че няма връзки, но има линкове извън забележката !" );
		}
		
		if (hasRemark) {
			//System.out.println("Found remark in A2");
			if (remarks.size() == 0) {
				throw new InvalidParameterException("в точка АБ.2 има проблем в забележката !" );
			}
			String remark = "";
			for (Node node : remarks) {
				if (isHyperLink(node.getText())) {
					remark += parseHyperLink(node.getText());
				}else {
					remark += node.getText();
				}
			}
			dosie.setRemarkab2(remark);
		}
		
		
		if (hasHyperLinks) {
			Integer action = null;
			
			Container curContainer = null;
			
			
			ArrayList<String> skipped = new ArrayList<String>();
			for (Node curNode : point2) {
				
				
				
				if(curNode.getText().trim().isEmpty()) {
					continue;
				}
				
				String textNode = curNode.getText();
				if (textNode.trim().isEmpty()) {
					continue;
				}
				textNode = textNode.replace(" :", ":");
				
				if (textNode.toUpperCase().contains("забележка:".toUpperCase() )) {
					//Очакваме да е на края, имаме я извлечена и спираме
					break;
				}
				
				
				//отменя, заменя или изменя 
				if (textNode.toLowerCase().contains("отменя:") ) {
					action = EuroConstants.VID_VRAZ_OTMENIA; //ОТМЕНЯ
					curContainer= new Container(action);						
					continue;
				}
				
				if (textNode.toLowerCase().contains("заменя")) {
					action = EuroConstants.VID_VRAZ_ZAMENIA; //ЗАМЕНЯ
					curContainer= new Container(action);
					continue;
				}
				
				if (textNode.toLowerCase().contains("изменя")) {
					action = EuroConstants.VID_VRAZ_IZMENIA; //ИЗМЕНЯ
					curContainer= new Container(action);
					continue;
				}
				
				if (action == null) {
					skipped.add(textNode);
					continue;
				}
				
				if (isHyperLink(textNode)) {
					curContainer.setUrl(parseHyperLink(textNode));					
					
					if (curContainer.isReady()) {						
						dosie.getAb2list().add(curContainer);
//						if (hasPrint) {
//							System.out.println("A.2 " + curContainer.getAction() + "\t" + curContainer.getName());
//						}
						curContainer= new Container(action);
						
					}else {
						throw new InvalidParameterException("в точка АБ.2 има грешка в подредбата на актовете (Middle)" );	
					}
				}else {
					curContainer.addToName(textNode);
				}
				
				
			}
			if (curContainer.getName() != null || curContainer.getUrl() != null) {
				throw new InvalidParameterException("в точка АБ.2  има грешка в подредбата на актовете (End) " );
			}
			
			if (skipped.size() > 1) {
				throw new InvalidParameterException("Необичайно много пропуснати редове в АБ.2" );
//				for (String s : skipped) {
//					System.out.println("SKIPPED:\t" + s);
//				}
			}
			
			
			
		}else {
			
			
//			if (findNotNullRows(point2) > 2) {
//				//System.out.println("a2list = " + findNotNullRows(point2));
//				throw new InvalidParameterException("в точка А.2 няма линкове към актове, но има повече от 2 параграфа" );
//			}
			
			
		}
		
		
		
		//////////////////////////////////////////// ТОЧКА 3		////////////////////////////////////////////
			
		
		ArrayList<Node> point3 =  splittedInfo.get("3.");
		
		
		
		if (point3 == null || point3.size() == 0) {
			throw new InvalidParameterException("Не се намира точка АБ.3" );	
		}
		
		
				
		foundNotText = false;
		hasRemark = false;
		hasHyperLinks = false;
		remarks = new ArrayList<Node>();
		//Търсим стандартните текстове за НЕ
		for (Node node : point3) {
			String textNode = node.getText();
			
			//Трябва да е първо заради хиперлинковете
			if (hasRemark) {
				remarks.add(node);
				continue;
			}
			
			if (textNode.toUpperCase().contains("не е изменена или отменена".toUpperCase() )) {
				foundNotText = true;
				break;
			}
			if (textNode.toUpperCase().contains("не е изменено или отменено".toUpperCase() )) {
				foundNotText = true;
				break;
			}
			if (textNode.toUpperCase().contains("не е отменена, заменена или изменена ".toUpperCase() )) {
				foundNotText = true;
				break;
			}
			
			if (textNode.toUpperCase().contains("не заменя, отменя или отменя".toUpperCase() )) {
				foundNotText = true;
				continue;
			}

			if (textNode.toUpperCase().contains("забележка:".toUpperCase() )) {
				hasRemark = true;
				continue;
			}
			
			if (isHyperLink(textNode)) {
				hasHyperLinks = true;
				continue;
			}
			
			
			
		}


		if (hasHyperLinks && foundNotText) {
			//открити са думичките че няма, но има линкове извън забележката
			throw new InvalidParameterException("в точка АБ.3 са открити са думичките че няма, но има линкове извън забележката !" );
		}
		
		if (hasRemark) {
			//System.out.println("Found remark in A3");
			if (remarks.size() == 0) {
				throw new InvalidParameterException("в точка АБ.3 за има проблем в забележката !" );
			}
			String remark = "";
			for (Node node : remarks) {
				if (isHyperLink(node.getText())) {
					remark += parseHyperLink(node.getText());
				}else {
					remark += node.getText();
				}
			}
			dosie.setRemarkab3(remark);
		}
		
		
		
		if (hasHyperLinks) {
			Integer action = null;
			
			Container curContainer = null;			
			ArrayList<String> skipped = new ArrayList<String>();
			for (Node curNode : point3) {
				
				//System.out.println("" + action + " --> " + curNode.getText() );
				
				if(curNode.getText().trim().isEmpty()) {
					continue;
				}
				
				
				
				String textNode = curNode.getText();
				if (textNode.contains("e отменена с")) {
					textNode = textNode.replace("e отменена с", "е отменена с");
				}
				
				textNode = textNode.replace("  ", " ");
				
				if (textNode.toUpperCase().contains("забележка:".toUpperCase() )) {
					//System.out.println("ИМААААААААААААААААААААААА в А.3.");
					break;
				}
				
				
				if (textNode.toLowerCase().contains("се отменя с") || textNode.toLowerCase().contains("се отменена с") || textNode.toLowerCase().contains("е отменена с") || textNode.toLowerCase().contains("е отменен с") || textNode.toLowerCase().contains("е отменено с")) {
					action = EuroConstants.VID_VRAZ_OTMENEN; //ОТМЕНЕН
					curContainer= new Container(action);						
					continue;
				}
				
								
				if (textNode.toLowerCase().contains("е заменена с") || textNode.toLowerCase().contains("е заменен с")  || textNode.toLowerCase().contains("е заменено с")) {
					action = EuroConstants.VID_VRAZ_ZAMENEN; //ЗАМЕНЕН
					curContainer= new Container(action);
					continue;
				}
				
				if (textNode.toLowerCase().contains("е изменена с") || textNode.toLowerCase().contains("изменен с") || textNode.toLowerCase().contains("е изменен с") || textNode.toLowerCase().contains("е изменено с")) {
					action = EuroConstants.VID_VRAZ_IZMENEN; //ИЗМЕНЕН
					curContainer= new Container(action);
					continue;
				}
				
				if (textNode.toLowerCase().contains("е поправена с") || textNode.toLowerCase().contains("е проправен с") || textNode.toLowerCase().contains("е поправено с")) {
					action = EuroConstants.VID_VRAZ_POPRAVEN; //ПОПРАВЕН
					curContainer= new Container(action);
					continue;
				}
				
				if (textNode.toLowerCase().contains("е допълнена с") || textNode.toLowerCase().contains("е допълнен с") || textNode.toLowerCase().contains("е допълнено с") || textNode.toLowerCase().contains(" допълнен с") || textNode.toLowerCase().contains(" допълнена с")) {
					action = EuroConstants.VID_VRAZ_DOPALNEN; //ДОПЪЛНЕН
					curContainer= new Container(action);
					continue;
				}
				
				if (textNode.toLowerCase().contains("частично отменена с") || textNode.toLowerCase().contains("частично отменен с") || textNode.toLowerCase().contains("частично отменено с")) {
					action = EuroConstants.VID_VRAZ_CH_OTMENEN; //ЧАСТИЧНО ОТМЕНЕН
					curContainer= new Container(action);						
					continue;
				}
				
				
				
				if (action == null) {
					skipped.add(textNode);
					continue;
				}
				
				if (isHyperLink(textNode)) {
					curContainer.setUrl(parseHyperLink(textNode));
					if (curContainer.isReady()) {						
						dosie.getAb3list().add(curContainer);
						//System.out.println("******************" + curContainer.getName());
//						if (hasPrint) {
//							System.out.println("A.3 " + curContainer.getAction() + "\t" + curContainer.getName());
//						}
						curContainer= new Container(action);
						//System.out.println("ADING .....");
					}else {
						throw new InvalidParameterException( "в точка АБ.3 има Грешка в подредбата на актовете (Middle)" );	
					}
				}else {
					curContainer.addToName(extractActName(textNode));
				}
				
				
			}
			if (curContainer.getName() != null || curContainer.getUrl() != null) {
				//System.out.println(curContainer.getName());
				throw new InvalidParameterException("в точка АБ.3  има Грешка в подредбата на актовете (End)" );
			}
		}else {
			
//			if (findNotNullRows(point3) > 2) {					
//				//System.out.println("a3list = " + findNotNullRows(point3));
//				throw new InvalidParameterException("в точка А.3 няма линкове към актове, но има повече от 2 параграфа" );
//			}
			
			
			
			
		}
		
			
		
		//////////////////////////////////////////// ТОЧКА 4		////////////////////////////////////////////
		ArrayList<Node> point4 =  splittedInfo.get("4.");
				
		
		if (point4 == null || point4.size() == 0) {
			throw new InvalidParameterException("Не се намира точка АБ.4" );	
		}
		
		
		String actoveZaIzpalnenie = null;
		String delegiraniActove = null;
		hasRemark = false;
		hasHyperLinks = true;
		
		ArrayList<Node> remarksDel = new ArrayList<Node>();
		ArrayList<Node> remarksIzp = new ArrayList<Node>();
		
		
		int action = 0;
		
		Container curContainer = null;
		for (Node curNode : point4) {
			
						
				
			if(curNode.getText().trim().isEmpty()) {
				continue;
			}
			
			String textNode = curNode.getText().trim().replace("  ", " ");
			textNode = textNode.replace("  ", " ");
			
			char c2 = 8211;
			textNode = textNode.replace(""+c2, "-");
			textNode = textNode.replace("актове- не се приемат", "актове - не се приемат");
			textNode = textNode.replace("изпълнение- не се приемат", "изпълнение - не се приемат");
			textNode = textNode.replace("изпълнение -не се приемат", "изпълнение - не се приемат");
			
			if (textNode.toUpperCase().contains("делегирани актове - не се приемат".toUpperCase())) {
				dosie.setDelNeSePriemat4(true);
				delegiraniActove = textNode;
				action = EuroConstants.VID_VRAZ_OSN_DEL;
				continue;
			}
			
			if (textNode.toUpperCase().contains("актове за изпълнение - не се приемат".toUpperCase())) {
				dosie.setIzpNeSePriemat4(true);				
				actoveZaIzpalnenie = textNode;
				action = EuroConstants.VID_VRAZ_OSN_IZP;
				continue;
			}
			
			if (textNode.toUpperCase().contains("забележка:".toUpperCase() )) {
				hasRemark = true;				
				continue;
			}
			
						
			if (textNode.startsWith("- актове за изпълнение") || textNode.startsWith("-актове за изпълнение") || textNode.startsWith("-  актове за изпълнение") || textNode.startsWith("- актове по изпълнение") || textNode.startsWith("-актове по изпълнение")) {
				actoveZaIzpalnenie = textNode;
				action = EuroConstants.VID_VRAZ_OSN_IZP;
				hasRemark = false;
				curContainer = new Container(action);
				continue;
			}
					
			
			if (textNode.startsWith("- делегирани актове") || textNode.startsWith("-делегирани актове")) {
				delegiraniActove = textNode;
				action = EuroConstants.VID_VRAZ_OSN_DEL;
				hasRemark = false;
				curContainer = new Container(action);
				continue;
			}
			
			if (hasRemark) {
				if (action == EuroConstants.VID_VRAZ_OSN_IZP) {
					remarksIzp.add(curNode);				
					continue;
				}else {
					remarksDel.add(curNode);
					continue;
				}
			}	
			
			if (action == 0) {
				continue;
			}
			
			if (curContainer == null) {
				curContainer = new Container(action);
			}
			
			
			
			if (isHyperLink(textNode)) {
				curContainer.setUrl(parseHyperLink(textNode));
				if (curContainer.isReady()) {
//					System.out.println();
//					System.out.println("***" + curContainer.getName());
//					System.out.println("***" +  curContainer.getUrl());
					dosie.getAb4list().add(curContainer);
					curContainer = new Container(action);
					
				}else {
					throw new InvalidParameterException( "в точка АБ.4  има Грешка в подредбата на актовете (Middle)" );	
				}
			}else{
				curContainer.addToName(textNode);
			}
		}
		
		if (curContainer != null) {
			if (curContainer.isReady()) {
	//			System.out.println();
	//			System.out.println("***" + curContainer.getName());
	//			System.out.println("***" +  curContainer.getUrl());
				dosie.getAb4list().add(curContainer);
			}else {
				if (curContainer.getName() != null || curContainer.getUrl() != null) {
					throw new InvalidParameterException("в точка АБ.4 има Грешка в подредбата на актовете (End)" );
				}
			}
		}
		
		if (actoveZaIzpalnenie == null) {
			throw new InvalidParameterException("Не се намира информация за актове за изпълнение в точка АБ.4" );
		}
		
		if (delegiraniActove == null) {
			throw new InvalidParameterException("Не се намира информация за делегирани актове в точка АБ.4" );
		}
		
		
		if (remarksDel.size() > 0) {			
			String remark = "";
			for (Node node : remarksDel) {
				if (isHyperLink(node.getText())) {
					remark += parseHyperLink(node.getText());
				}else {
					remark += node.getText();
				}
			}
			dosie.setRemarkab4D(remark);
			//System.out.println("REMARKD:" + remark);
		} 
				
		if (remarksIzp.size() > 0) {			
			String remark = "";
			for (Node node : remarksIzp) {
				if (isHyperLink(node.getText())) {
					remark += parseHyperLink(node.getText());
				}else {
					remark += node.getText();
				}
			}
			dosie.setRemarkab4I( remark);
			//System.out.println("REMARKI:" +remark);
		}
		
		
		
		
		
		
		
		
		
		
		////////////////////////////////////////////ТОЧКА 5		////////////////////////////////////////////
		
		ArrayList<Node> point5 =  splittedInfo.get("5.");
		
		
		if (point5 == null || point5.size() == 0) {
			//throw new InvalidParameterException(fileName + " - Не се намира точка А.5 за директива/рамково решение" );	
		}else {
			
			
			
//			String firstPoint = point5.get(0).getText();
//			point5.remove(0);
//			action = null;
//			boolean neVavejda = false;
//			
//			// 
//			firstPoint = firstPoint.replace("  ", " ");
//			if (firstPoint.indexOf("въвежда и изискванията и на") != -1 ||
//				firstPoint.indexOf("въвежда изисквания и на") != -1 ||
//				firstPoint.indexOf("въвежда и изискванията на") != -1 ||
//				firstPoint.indexOf("се въвеждат изискванията и на") != -1 ||
//				firstPoint.indexOf("въвежда изискванията и на") != -1 ||
//				firstPoint.indexOf("въвежда изисквания на") != -1 ||
//				firstPoint.indexOf("се въвеждат изисквания и на") != -1) {
//				action = "ВЪВЕЖДА ИЗИСКВАНИЯ";
//				
//			}
//			
//			if (firstPoint.indexOf("предвижда изменения в разпоредби, въвеждащи") != -1) {
//				action = "предвижда изменения в разпоредби".toUpperCase();
//			}	
//			
//			if (firstPoint.indexOf("се предвижда основание за въвеждане и на") != -1 ||
//				firstPoint.indexOf("се предвижда основание за въвеждане на") != -1) {
//				action = "предвижда основание за въвеждане".toUpperCase();
//			}
//			
//			if (firstPoint.indexOf(" не въвежда") != -1) {
//				action = "НЕ ВЪВЕЖДА".toUpperCase();
//				neVavejda = true;
//			}
//			 
//			if (firstPoint.indexOf(" предвижда мерки по ") != -1 ||
//				firstPoint.indexOf(" предвиждат мерки по ") != -1 ||
//				firstPoint.indexOf(" предвижда мярка по ") != -1) {
//				action = "предвижда мерки".toUpperCase();
//			}
//			
//			
//			
//			if (action == null) {
//				throw new InvalidParameterException("в точка АБ.5 не се намира действие (action) !" );
//			}
//			
//			
//			
//			for (Node curNode :  point5) {
//				
//				String textNode = curNode.getText();
//				textNode = textNode.replace("  ", " ");
//				
//				if (textNode.trim().isEmpty()) {
//					continue;
//				}
//				
//				if (textNode.contains("информационни досиета")) {
//					continue;
//				}
//						
//				
//				curContainer = new Container(action);
//				curContainer.addToName(textNode.trim());
//				curContainer.setUrl("N/А");
//				dosie.getAb5list().add(curContainer);
//					
//				
//			}
//			
//			
//			if (!neVavejda && dosie.getAb5list().size() == 0) {
//				//throw new InvalidParameterException("в точка АБ.5 не са намерени изброени актове !" );
//			}
//			
		}

	}
	
	



	
	
	

	private void parseSectionV(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'В' НА ФАЙЛА !" );	
		}
		
		//не въвежда административно регулиране
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		
		
		boolean vavejda = true;
		String remark = "";
		for (Node curNode : pars) {
			
			String textNode = curNode.getText();
			if (textNode.trim().isEmpty()) {
				continue;
			}
			
			if (textNode.toUpperCase().contains("не предвижда административно регулиране".toUpperCase()) || 
				textNode.toUpperCase().contains("не се предвижда административно регулиране".toUpperCase()) ||
				textNode.toUpperCase().contains("не въвежда административно регулиране".toUpperCase()) ||
			    textNode.toUpperCase().contains("не се въвежда административно регулиране".toUpperCase())) {
				vavejda = false;				
			}
			
			
			remark += textNode;
			
		}
		
		dosie.setRemarkV(remark);
		dosie.setHasAdmReguliraneV(vavejda);
		
		
//		if (! remark.isEmpty()) {
//			System.out.println();
//			System.out.println(remark);
//		}
		
		//TODO da se setnat neVavejda i remark
//		System.out.println("-->" + remark);
//		System.out.println("vavejda -->" + vavejda);
		
//		if (!vavejda && pars.getCount() > 2) {
//			//Може би трябва да отиде в забележка останалото
//			throw new InvalidParameterException("В секция В се намира индикатор за НЕ, но има много параграфи (" + pars.getCount() + ")");
//		}
		
		
//		if (vavejda && pars.getCount() <= 2) {
//			//Може би трябва да отиде в забележка останалото
//			throw new InvalidParameterException("В секция В не се намира индикатор за НЕ, но има много малко параграфи (" + pars.getCount() + ")");
//		}
		
	}
	
	private void parseSectionG(Node cell, Dosie dosie) throws InvalidParameterException {
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'Г' НА ФАЙЛА !" );	
		}
		
		char c = 8209;
		String smallt = ""+c;
		
		char c1 = 30;
		String rs = ""+c1;
		
		char c2 = 160;
		String fakesp = ""+c2;
		
		//System.out.println();
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		Container curContainer = new Container(null);
		int tek = 0;
		for (int p = 0; p < pars.getCount(); p++) {
			String textNode = pars.get(p).getText().trim();
			//System.out.println(pointN + "---> " + textPar);
			
			if (textNode.isEmpty()) {
				continue;
			}
			
			textNode = textNode.replace(rs, "-");
			textNode = textNode.replace(smallt, "-");
			textNode = textNode.replace("  ", " ");
		    textNode = textNode.replace("  ", " ");
		    textNode = textNode.replace(fakesp, " ");
			
			//Оправяне на глупости 
			textNode = textNode.replace(".Решение по дело", ". Решение по дело");
			textNode = textNode.replace(".Определение по ", ". Определение по ");
			textNode = textNode.replace(".Съединени дела", ". Съединени дела");
			
			textNode = textNode.replace("  ", " ");
			textNode = textNode.replace("Решение. по дело", "Решение по дело");
			textNode = textNode.replace("Решение на Съда по дело", "Решение по дело");
			textNode = textNode.replace("Решение на съда по дело ", "Решение по дело");
			textNode = textNode.replace("Решение на по дело", "Решение по дело");
			textNode = textNode.replace("Решение. по съединени дела", "Решение по съединени дела");
			textNode = textNode.replace(". Съединени дела", ". Решение по съединени дела");
			
			//ДАЛИ ????
			textNode = textNode.replace(". Решение по дела", ". Решение по съединени дела");
			textNode = textNode.replace("Определение на съединени дела", "Определение по съединени дела");
			
			
			String nomer = null;
			String vid = null;
			String dopInfo = null;
			for (int i = 1; i <= 100; i ++) {
//				String test = i +". Решение по дело";
//				System.out.println(test);
//				System.out.println(textNode);
//				
//				String t1 = test;
//				String t2 = textNode;
//				byte[] tb1 = test.getBytes();
//				byte[] tb2 = textNode.getBytes();
//				for (int ii = 0; ii < t1.length(); ii++) {
//					System.out.println(t1.charAt(ii) + "\t" + t2.charAt(ii) + "\t" + (int)t1.charAt(ii)+ "\t" + (int)t2.charAt(ii));
//				}
				
				
				if (textNode.toUpperCase().startsWith(i +". Решение по дело".toUpperCase())) {
					nomer = textNode.replace(i +". Решение по дело", "");
					vid = "Решение по дело";
					dopInfo = textNode;
					
					dopInfo = dopInfo.replace("Решение по Дело ", "Решение по дело "); //хотфикс
					
					//System.out.println(textNode);
					if (tek + 1 != i) {
						throw new InvalidParameterException("Изпуснат номер В СЕКЦИЯ 'Г' НА ФАЙЛА !"  + (tek+1) + " -> " + textNode);
					}
					tek++;
					break;
				}
				if (textNode.toUpperCase().startsWith(i +". Решение по съединени дела ".toUpperCase())) {
					nomer = textNode.replace(i +". Решение по съединени дела", "");
					vid = "Решение по съединени дела";
					dopInfo = textNode;
					//System.out.println(textNode);
					if (tek + 1 != i) {
						throw new InvalidParameterException("Изпуснат номер В СЕКЦИЯ 'Г' НА ФАЙЛА !"  + tek + " -> " + textNode);
					}
					tek++;
					break;
				}
				if (textNode.toUpperCase().startsWith(i +". Определение по дело".toUpperCase())) {
					nomer = textNode.replace(i +". Определение по дело", "");
					vid = "Определение по дело";
					dopInfo = textNode;
					//System.out.println(textNode);
					if (tek + 1 != i) {
						throw new InvalidParameterException("Изпуснат номер В СЕКЦИЯ 'Г' НА ФАЙЛА !"  + tek + " -> " + textNode);
					}
					tek++;
					break;
				}
				
				//Определение на съединени дела 
				
				if (textNode.toUpperCase().startsWith(i +". Определение по съединени дела".toUpperCase())) {
					nomer = textNode.replace(i +". Определение по съединени дела", "");
					vid = "Определение по съединени дела";
					dopInfo = textNode;
					//System.out.println(textNode);
					if (tek + 1 != i) {
						throw new InvalidParameterException("Изпуснат номер В СЕКЦИЯ 'Г' НА ФАЙЛА !"  + tek + " -> " + textNode);
					}
					tek++;
					break;
				}
				
			}
			
			if (nomer == null) {
				
				if (isHyperLink(textNode)) {
					if (curContainer.getUrl() == null) {
						curContainer.setUrl(parseHyperLink(textNode));
					}else {
						//throw new InvalidParameterException("НEПРАВИЛНА ПОДРЕДБА В СЕКЦИЯ 'Г' НА ФАЙЛА ! Опит за добавяне на второ URL" );
					}
				}else {
					curContainer.addToName(converttoHTML((Paragraph)pars.get(p)));
				}
				
				
//				if ("0123456789".contains(textNode.subSequence(0, 1))   && textNode.contains(".") && (  textNode.toLowerCase().contains("решение по") || textNode.toLowerCase().contains("съединени дела") || textNode.toLowerCase().contains("пределение по "))) {
//					throw new InvalidParameterException("Неразпознато начало на група В СЕКЦИЯ 'Г' НА ФАЙЛА !"  + " -> " + textNode);
//				}
			}else {
				
				String oldNom = nomer;
				nomer = nomer.replace("\r", "");
				nomer = nomer.replace("\n", "");
				nomer = nomer.replace(rs, "-");
				nomer = nomer.replace(smallt, "-");
				nomer = nomer.replace(":", "");			   
				nomer = nomer.trim();
				nomer = nomer.replace(" ", "-");
				nomer = nomer.replace("--", "-");
				nomer = nomer.replace("С", "C");
				nomer = nomer.replace("С-C", "C");
				
				if (curContainer.isReady()) {
					dosie.getGlist().add(curContainer);
					curContainer = new Container(EuroConstants.VID_VRAZ_OSN_G);
					curContainer.setNomer(nomer);
					curContainer.setDopInfo(dopInfo);
					curContainer.setVid(vid);
				}else {
					if (curContainer.getAction() != null ) {
						throw new InvalidParameterException("НEПРАВИЛНА ПОДРЕДБА В СЕКЦИЯ 'Г' НА ФАЙЛА !" );
					}
					curContainer = new Container(EuroConstants.VID_VRAZ_OSN_G);
					curContainer.setNomer(nomer);
					curContainer.setDopInfo(dopInfo);
					curContainer.setVid(vid);
				}
				
			}

		}	
		
		if (curContainer.isReady()) {
			//System.out.println(nomer);
			dosie.getGlist().add(curContainer);			
		}
		
		
	}
	
	private void parseSectionD(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'Д' НА ФАЙЛА !" );	
		}
		
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		boolean foundNo = false;
		for (Node node : pars) {
			
			String textNode =node.getText(); 
			 
			if (textNode.toUpperCase().contains("Не са установен процедури".toUpperCase()) ||
				textNode.toUpperCase().contains("Не са установени процедури".toUpperCase()) ||
				textNode.toUpperCase().contains("Не е установена процедура".toUpperCase()) ||
				textNode.toUpperCase().contains("няма открита процедура".toUpperCase()) ||
				textNode.toUpperCase().contains("Не е установено наличие".toUpperCase())) {
				foundNo = true;
			}
			
			 
		}
		
		//System.out.println();
		//System.out.println("---> " + foundNo + "\t" + pars.getCount());
		
		if (foundNo && pars.getCount() > 2) {
			throw new InvalidParameterException("В СЕКЦИЯ 'Д' НА ФАЙЛА e намерено 'Не е установено наличие', а има повече от 2 параграфа!" );	
		}
		
		if (!foundNo && pars.getCount() <= 2) {
			throw new InvalidParameterException("В СЕКЦИЯ 'Д' НА ФАЙЛА не e намерено 'Не е установено наличие', а има малко параграфи!" );	
		}
		
		if (! foundNo) {
			String urlSectionD = "";
			String textSectionD = "";
			for (Node node : pars) {
				
				String textNode =node.getText(); 
				if (isHyperLink(textNode)) {
					urlSectionD = parseHyperLink(textNode) ;					
				}else {
					textSectionD += textNode ; //+ "\r\n";
				}
			}
			
			dosie.setTextSectionD(textSectionD);
			dosie.setUrlSectionD(urlSectionD);
		}
		
		dosie.setHasProcedureD(! foundNo);
		
		
	}
	
	private void parseSectionE(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'Е' НА ФАЙЛА !" );	
		}
		
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		
		String noteE = "";
		boolean globalStart = false;
		Integer action = null;
		Container curContainer = new Container(action);
		boolean inResume = false;
		for (int p = 0; p < pars.getCount(); p++) {
			String textNode = pars.get(p).getText().trim();
			
			if (textNode.trim().isEmpty()) {
				continue;
			}
			
			boolean foundStart = false;
			for (int i = 1; i <= 100; i ++) {
				if (textNode.startsWith(i +". Доклад ") || textNode.startsWith(i +". Мотивирано ") || textNode.startsWith(i +". Становище ")) {
					foundStart = true;
					textNode = textNode.substring((i +".").length()).trim();
					break;
				}
			}
			
			if (foundStart) {
				globalStart = true;	
				inResume = false;
				action = -1;
				if (curContainer.getAction() != null) {
					dosie.getElist().add(curContainer);
				}
				curContainer = new Container(action);
				curContainer.setDopInfo(textNode);
				
			}else {
				
				if (!globalStart) {
					noteE += textNode;
					continue;
				}
				
				if (textNode.contains("Резюме")) {
					inResume = true;
					continue;
				}
				
				if (inResume) {
					curContainer.addToName(textNode);
					continue;
				}
				
				if (isHyperLink(textNode)) {
					curContainer.setUrl(parseHyperLink(textNode));
				}else {
					throw new InvalidParameterException("НEПРАВИЛНА ПОДРЕДБА В СЕКЦИЯ 'Е' НА ФАЙЛА !" );
				}
			}
		}
		
		if (curContainer.getAction() != null) {
		
			dosie.getElist().add(curContainer);
		}
		
		if (noteE != null && !noteE.isEmpty()) {
			dosie.setRemarkE(noteE);
			
			getCOMFromTekst(dosie);
		}
			
		
		
		
	}
	
	


	private void parseSectionJ(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'Ж' НА ФАЙЛА !" );	
		}
		
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		
		String url = null;		
		for (Node node : pars) {
			String textNode = node.getText();
			if (isHyperLink(textNode)) {
				url = parseHyperLink(textNode);
			}
		}
		
		if (url != null) {
			
			dosie.setTehReglURLJ(url);
			
			//дата на получаване: 
			//нотификационен номер: 
			String searchString = pars.get(0).getText();
			int begIndex = searchString.indexOf("нотификационен номер:");
			
			//ХОТФИКС
			if (searchString.indexOf("нотификационен номер ") != -1) {
				searchString = searchString.replace("нотификационен номер", "нотификационен номер:");
				begIndex = searchString.indexOf("нотификационен номер:");
			}
			
			
			
			if (begIndex == -1) {				
				throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ ЗА НОТИФИКАЦИОНЕН НОМЕР В СЕКЦИЯ 'Ж' НА ФАЙЛА !" );
			}
			
			searchString = searchString.substring(begIndex+22);
			
			int zapIndex = searchString.indexOf(",");
			if (zapIndex == -1) {
				zapIndex = searchString.indexOf("с дата на получаване");				 
			}
			String number = searchString.substring(0, zapIndex);
			
			
			dosie.setTehReglNomJ(number);
			
			begIndex = searchString.indexOf("дата на получаване:");
			//ХОТФИКС
			if (searchString.indexOf("дата на получаване ") != -1) {
				searchString = searchString.replace("дата на получаване", "дата на получаване:");
				begIndex = searchString.indexOf("дата на получаване:");
			}
			
			
			if (begIndex == -1) {
				throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ ЗА ДАТА НА ПОЛУЧАВАНЕ В СЕКЦИЯ 'Ж' НА ФАЙЛА !" );
			}
			searchString = searchString.substring(begIndex+20);
			String dat = searchString.substring(0, searchString.indexOf("."));
			
			Date datP = null;		
			try {
				datP = new SimpleDateFormat("dd/MM/yyyy").parse(dat);
			} catch (ParseException e) {
				throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ ФОРМАТИРА ДАТА НА ПОЛУЧАВАНЕ В СЕКЦИЯ 'Ж' НА ФАЙЛА !" );
			} 
			dosie.setTehReglDatJ(datP);
			//System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee>" + number + "/" +datP);
		}
		
		
		
		
		
		
	}
	
	private void parseSectionZ(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'З' НА ФАЙЛА !" );	
		}
		
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		boolean foundNo = false;
		String fullText = "";
		for (Node node : pars) {
			
			String textNode =node.getText(); 
			fullText += textNode ;
			if (textNode.toUpperCase().contains("Няма друг".toUpperCase() ) || textNode.toUpperCase().contains("Няма актове".toUpperCase() )) {
				foundNo = true;
				//System.out.println("****************************************" + fullText);
			}
			
			 
		}
		
		
		
//		System.out.println();
//		System.out.println("---> " + foundNo + "\t" + pars.getCount());
		
		if (foundNo && pars.getCount() > 2) {
			//System.out.println("--------->" + fullText);
			throw new InvalidParameterException("В СЕКЦИЯ 'З' НА ФАЙЛА e намерено 'Няма други актове', а има повече от 2 параграфа!" );	
		}
		
//		if (!foundNo && pars.getCount() <= 2) {
//			throw new InvalidParameterException("В СЕКЦИЯ 'З' НА ФАЙЛА не e намерено 'Не е установено наличие', а има малко параграфи!" );	
//		}
		
		if (! foundNo) {
//			System.out.println();
//			System.out.println(fullText);
			for (Node node : pars) {
				String textNode =node.getText().trim();
				String startP = null;
				if (textNode.trim().startsWith("-")) {
					startP = "-";
				}else {
					for (int i = 1; i <= 100; i++) {
						if (textNode.trim().startsWith(i+".")){
							startP = i+".";
							break;
						}
					}
				}
				if (startP != null) {
					
					
					if (isHyperLink(textNode)) {
						int indBeg = textNode.indexOf("");
						int indEnd = textNode.lastIndexOf("");
						if (indBeg != -1 && indEnd != -1) {
							String url = textNode.substring(indBeg, indEnd);
							textNode = textNode.replace(url, "");
						}
						textNode = textNode.replace("", "");
						textNode = textNode.replace("", "");
						
						
						
						
//						removeHyperLink(node);
//						textNode = node.getText(); 
					
					}
					textNode  = textNode.trim();
					textNode = textNode.substring(startP.length());
					textNode  = textNode.trim();
					if (textNode.startsWith("Законът")) {
						textNode = textNode.replace("Законът", "Закон");
					}
					Container c = new Container(-2);
					c.setName(textNode);
					//extractDVfromZ(textNode);
					dosie.getZlist().add(c);
				}
			}
			
			if (dosie.getZlist().size() == 0) {
				throw new InvalidParameterException("В СЕКЦИЯ 'З' НА ФАЙЛА списъка на законите не е структуриран правилно !" );	
			}
		}
		
		
		
	}
	
	private void parseSectionI(Node cell, Dosie dosie) throws InvalidParameterException {		
		
		if (cell == null || cell.getText() == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ В СЕКЦИЯ 'И' НА ФАЙЛА !" );	
		}
		
		boolean hasPoint1 = false;
		boolean hasPoint2 = false;
		boolean hasPoint3 = false;
		boolean hasPoint4 = false;
		
		NodeCollection<Node> pars = ((CompositeNode<Node>) cell).getChildNodes(NodeType.PARAGRAPH, true);
		for (Node curNode : pars) {
			
			//String tek = curNode.getText();
			
			//System.out.println(curNode.getText());
			if (curNode.getText().contains("1.") && curNode.getText().toUpperCase().contains("Приети".toUpperCase()) && curNode.getText().contains("текстове") ) {
				hasPoint1 = true;
			}
			if (curNode.getText().contains("2.") && curNode.getText().contains("Анотация") ) {
				hasPoint2 = true;
			}
			if (curNode.getText().contains("3.") && curNode.getText().contains("Анализ") && curNode.getText().contains("въвеждане")) {
				hasPoint3 = true;
			}
			if (curNode.getText().contains("4.") && curNode.getText().contains("ценка") && curNode.getText().contains("въздействие")) {
				hasPoint4 = true;
			}
			
//			System.out.println("" + hasPoint1+"|"+hasPoint2 +"|"+ hasPoint3 +"|"+ hasPoint4);
//			if (tek.contains("apis")) {
//				System.out.println("tek");
//			}
		}
		
		
		if (hasPoint1 && hasPoint2 && hasPoint3 && hasPoint4) {
			
		}else {
			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! " + hasPoint1 + "," + hasPoint2 + "," + hasPoint3 + "," + hasPoint4 );
		}
		
		
		HashMap<Integer, String> sectionI = new HashMap<Integer, String>();
		sectionI.put(0, "");
		sectionI.put(1, "");
		sectionI.put(2, "");
		sectionI.put(3, "");
		sectionI.put(4, "");
		
		Integer num = 0;
		for (Node curNode : pars) {
			String textNode =   curNode.getText();
			if (textNode.contains("1.") && textNode.contains("Приети") && textNode.contains("текстове") ) {
				num = 1;
			}
			if (textNode.contains("2.") && textNode.contains("Анотация") ) {
				num = 2;
			}
			if (textNode.contains("3.") && textNode.contains("Анализ") && textNode.contains("въвеждане")) {
				num = 3;
			}
			if (curNode.getText().contains("4.") && curNode.getText().contains("ценка") && curNode.getText().contains("въздействие")) {
				num = 4;
			}
			
			char fakeSpace = (char)160;
			textNode = textNode.replace(""+fakeSpace, " ");
			textNode = textNode.replace("  ", " ");
			textNode = textNode.replace("1. Приети текстове:", "");
			textNode = textNode.replace("1. Приети текстове", "");
			
			textNode = textNode.replace("2. Анотация на приетите текстове:", "");
			textNode = textNode.replace("2. Анотация на приетите текстове:", "");
			textNode = textNode.replace("2. Анотация на приетите текстове", "");
			
			textNode = textNode.replace("2.Анотация на приетите текстове:", "");
			textNode = textNode.replace("2.Анотация на приетите текстове:", "");
			textNode = textNode.replace("2.Анотация на приетите текстове", "");
			
			textNode = textNode.replace("2.Анотация:", "");
			textNode = textNode.replace("2.Анотация", "");
			textNode = textNode.replace("2. Анотация:", "");
			textNode = textNode.replace("2. Анотация", "");
			
			textNode = textNode.replace("3. Анализ на въвеждането:", "");		
			textNode = textNode.replace("3. Анализ на въвеждането.", "");
			textNode = textNode.replace("3. Анализ на въвеждането - ", "");
			textNode = textNode.replace("3. Анализ на въвеждане:", "");		
			textNode = textNode.replace("3. Анализ на въвеждане.", "");
			textNode = textNode.replace("3. Анализ на въвеждане - ", "");
			textNode = textNode.replace("3. Анализ въвеждане:", "");
			textNode = textNode.replace("3. Анализ въвеждане –", "");
			textNode = textNode.replace("3. Анализ въвеждане.", "");
			textNode = textNode.replace("3. Анализ въвеждането:", "");
			textNode = textNode.replace("3. Анализ на въвеждането –", "");
			textNode = textNode.replace("3. Анализ на въвеждането", "");
			
			textNode = textNode.replace("3.Анализ на въвеждането:", "");		
			textNode = textNode.replace("3.Анализ на въвеждането.", "");
			textNode = textNode.replace("3.Анализ на въвеждането - ", "");
			textNode = textNode.replace("3.Анализ на въвеждане:", "");		
			textNode = textNode.replace("3.Анализ на въвеждане.", "");
			textNode = textNode.replace("3.Анализ на въвеждане - ", "");
			textNode = textNode.replace("3.Анализ въвеждане:", "");
			textNode = textNode.replace("3.Анализ въвеждане –", "");
			textNode = textNode.replace("3.Анализ въвеждане.", "");
			textNode = textNode.replace("3.Анализ въвеждането:", "");
			textNode = textNode.replace("3.Анализ на въвеждането –", "");
			textNode = textNode.replace("3.Анализ на въвеждането", "");
			
			textNode = textNode.replace("3. Анализ въвеждането -", "");
			
				
				
			textNode = textNode.replace("4. Оценка на въздействието.", "");			
			textNode = textNode.replace("4. Оценка на въздействие.", "");
			textNode = textNode.replace("4.Оценка на въздействието.", "");			
			textNode = textNode.replace("4.Оценка на въздействие.", "");
			textNode = textNode.replace("4. Оценка на въздействието", "");
			
			textNode = textNode.replace("4. Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията", "Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията");
			textNode = textNode.replace("4. Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията", "Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията");
			textNode = textNode.replace("4.Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията", "Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията");
			textNode = textNode.replace("4.Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията", "Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията");
			
			
			
			
			textNode = textNode.trim();
			
			if (isHyperLink(textNode)) {
				textNode = parseHyperLink(textNode.trim());
			}
			
			if (! textNode.trim().isEmpty()) {
				String current =  sectionI.get(num);
				sectionI.put(num, current + textNode + "\r\n");
			} 
		}
		
		if (sectionI.get(1).trim().isEmpty()) {
			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! Точка 1 е празна"  );
		}
		
		if (sectionI.get(2).trim().isEmpty()) {
			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! Точка 2 е празна"  );
		}
		
//		if (sectionI.get(3).trim().isEmpty()) {
//			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! Точка 3 е празна"  );
//		}
		
//		if (sectionI.get(4).trim().isEmpty()) {
//			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! Точка 4 е празна"  );
//		}
		
		
//		if (!sectionI.get(0).trim().isEmpty()) {
//			throw new InvalidParameterException("НЕПЪЛНА СТРУКТУРА СЕКЦИЯ 'И' НА ФАЙЛА ! Точка 5 е празна"  );
//		}
		
		dosie.setSectionI(sectionI);
		
//		System.out.println();
//		System.out.println(sectionI.get(4));
	}





	public static  ArrayList<String> getNumbersFromString(String tekst){
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
	
	
	
	private  void findMainDataFromText(Document doc, Dosie dosie) throws InvalidParameterException {
		
		
		//Търсим и взимаме
		NodeCollection<Node> paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);
		//System.out.println("Paragraphs: " + paragraphs.getCount());
		//System.out.println(paragraphs.get(4).getText());
		boolean foundID = false;
		String parText = null;
		for (int p = 0; p < paragraphs.getCount(); p++) {
			//System.out.println(paragraphs.get(p).getText());
			if (paragraphs.get(p).getText().contains("ИНФОРМАЦИОННО ДОСИЕ")) {						
				foundID = true;
				continue;
			}
			if (foundID) {
				parText = paragraphs.get(p).getText().trim();
				if (!parText.isEmpty()) {
					
					//Намерили сме го. Сега трябва да видим дали не е разцепен на 2 параграфа
					if (!paragraphs.get(p+1).getText().trim().isEmpty()  && !paragraphs.get(p+1).getText().trim().startsWith("А") && !paragraphs.get(p+1).getText().trim().startsWith("A")) {
						parText += " " + paragraphs.get(p+1).getText().trim();
						if (!paragraphs.get(p+2).getText().trim().isEmpty() && !paragraphs.get(p+2).getText().trim().startsWith("А")  && !paragraphs.get(p+1).getText().trim().startsWith("A")) {
							parText += " " + paragraphs.get(p+2).getText().trim();						
						}
					}
					
					
					break;
				}
			}
			
		}
		
		parText = parText.replace("EO", "ЕО");
		dosie.setName("Информационно досие " + parText);
//		parText = parText.trim();
//		if (parText.startsWith("на ")) {
//			parText = parText.substring(3);
//			parText = parText.trim();
//		}
//		dosie.setName(parText);
		
		
		
		
		if (parText == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ПАРАГРАФА С ИМЕТО НА ЗАКОНОПРОЕКТА !!!!" );				
		}
		
//		if (parText.contains("(ЕС) 2016/680")) {
//			System.out.println(parText);
//		}
		
		
		
		
		//Оправяме бозите при въвеждане
		parText = parText.replace("„", "\"");
		parText = parText.replace("“", "\"");
		parText = parText.replace("a", "а");
		parText = parText.replace("  ", " ");
		
		
		if (parText.contains("§")) {			
			parseMultiLaws(parText, dosie);
		}else {
			parseSingleLaw(parText, dosie);
		}
		
		
		
		
				
		String restString = parText.substring(parText.indexOf(")")+1);
		String tip = null;
		int tipL = 0;
		
		
		//регламент, директива, решение, рамково решение, Регламент за изпълнение.
		if (restString.toUpperCase().contains("ДИРЕКТИВА ЗА ИЗПЪЛНЕНИЕ")) {
			tip = "Директива за изпълнение";
			tipL = EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE;
		}else {
			if (restString.toUpperCase().contains("РЕШЕНИЕ ЗА ИЗПЪЛНЕНИЕ")) {
				tip = "Решение за изпълнение";
				tipL = EuroConstants.TIP_ACT_RЕSHENIE_ZA_IZPALNENIE;
			}else {	
				if (restString.toUpperCase().contains("РЕГЛАМЕНТ ЗА ИЗПЪЛНЕНИЕ")) {
					tip = "Регламент за изпълнение";
					tipL = EuroConstants.TIP_ACT_REGLAMENT_ZA_IZPALNENIE;
				}else {	
					if (restString.toUpperCase().contains("РАМКОВО РЕШЕНИЕ")) {
						tip = "Рамково решение";
						tipL = EuroConstants.TIP_ACT_RAMKOVO_RESHENIE;
					}else {		
						if (restString.toUpperCase().contains("ДИРЕКТИВА")) {					
							tip = "Директива";
							tipL = EuroConstants.TIP_ACT_DIREKTIVA;
							//System.out.println(parText.substring(endIndex+1));
						}else {	
							
							if (restString.toUpperCase().contains("Делегиран регламент".toUpperCase())) {						
								tip = "Делегиран регламент";
								tipL = EuroConstants.TIP_ACT_DELEGIRAN_REGLAMENT ;
							}else {
								if (restString.toUpperCase().contains("РЕШЕНИЕ")) {
									//System.out.println(restString);
									tip = "Решение";
									tipL = EuroConstants.TIP_ACT_RESHENIE;							
								}else {
									if (restString.toUpperCase().contains("РЕГЛАМЕНТ")) {						
										tip = "Регламент";
										tipL = EuroConstants.TIP_ACT_REGLAMENT ;
									}
								}
							}
							
						}
					}
				}
			}
		}
		
		if (tip == null) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ВИД НА АКТА В ЗАГЛАВИЕТО !!!!" );
		}
		
		
		dosie.setTip(tip);
		dosie.setTipL(tipL);
		
//		if (hasPrint) {
//			System.out.println("Тип на акт : " + tip);
//			System.out.println("Закон : " + dosie.getImeZakon1());
//			System.out.println("Хармонизиран със : " + dosie.getImeZakon2());
//		}
		
		
	
	}
	
	
	private void parseSingleLaw(String parText, Dosie dosie) throws InvalidParameterException {
		// TODO Auto-generated method stub
		
		String part = parText.substring(0, parText.indexOf("("));
		part = formalizeZakon(part);
		String zakon1 = null;
		String zakon2 = null;
		if (parText.contains("за изменение и допълнение на") || parText.contains("за допълнение на")) {
			//ЗИД
			zakon2 = part;
			zakon1 = part.replace("Закон за изменение и допълнение на ", "");
			zakon1 = zakon1.replace("Закон за допълнение на ", "");
			zakon1 = formalizeZakon(zakon1);
			//System.out.println("ЗИД --------------->" + zakon1);
			//System.out.println("ЗИД --------------->" + zakon2);
		}else {
			//System.out.println("НЕ Е ЗИД --------------->" + part);
			zakon1 = formalizeZakon(part);
			zakon2 = formalizeZakon(part);
		}
		
		
		//Опитваме да намерим броя и годината на ДВ
		int beginIndex = parText.indexOf("(");
		int endIndex = parText.indexOf(")");
		if (beginIndex == -1 || endIndex == -1) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ ЗА БРОЯ НА ДВ !!!! --> " + parText );
		}
		String dvString = parText.substring(beginIndex+ 1, endIndex).trim();
		//System.out.println("------------------------------------------------->" + dvString);
		
		ArrayList<String> foundStrings = getNumbersFromString(dvString);
		
		if (foundStrings.size() != 2) {
			throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ ПАРСНЕ БРОЙ И ГОДИНА НА ДВ !!!! --> " + dvString  );				
		}
		
		String broiDV = foundStrings.get(0);
		String yearDv = foundStrings.get(1);
		//System.out.println("D-------------------------------------------------->" + broiDV + " | " + yearDv);
		
		dosie.setBroiDV(broiDV);
		dosie.setYearDv(yearDv);
		
		dosie.setImeZakon1(zakon1);
		dosie.setImeZakon2(zakon2);
		if (zakon1 != null) {
			dosie.setIdZakon1(findZakonInDb(zakon1, null, null));
		}
		if (zakon2 != null) {
			dosie.setIdZakon2(findZakonInDb(zakon2, broiDV, yearDv));
		}
		
		
	}





	private void parseMultiLaws(String parText, Dosie dosie) throws InvalidParameterException {
		
		
		String paragraph = "";
		int indPar = parText.indexOf(", изменен и допълнен с ");
		int lengthS = 23;
		paragraph = "изменен и допълнен с ";
		if (indPar == -1) {
			indPar = parText.indexOf(", изменен с ");
			paragraph = "изменен с";
			lengthS = 12;
		}
		if (indPar == -1) {
			indPar = parText.indexOf(", допълнен с ");
			paragraph = "допълнен с ";
			lengthS = 13;
		}
		if (indPar == -1) {
			throw new InvalidParameterException("Не може да се намери разделител на § частта !" + parText);
		}
		
		
		String firstPart = parText.substring(0, indPar);
		String zakon1 = formalizeZakon(firstPart);
		
		//System.out.println("F-------------------------------------------------->" + zakon1);
		String secondPart = parText.substring(indPar+ lengthS);
		
		
		int indOt = secondPart.indexOf(" от ");
		if (indOt == -1) {
			throw new InvalidParameterException("Не може да се намери началото на втория закон !" + parText);
		}
		
		String parInfo = paragraph + secondPart.substring(0, indOt).trim();
		dosie.setParagraph(paragraph);
		//System.out.println("P-------------------------------------------------->" + parInfo);
				
		String zakon2 = secondPart.substring(indOt+3, secondPart.indexOf("("));
		zakon2 = formalizeZakon(zakon2);
		//System.out.println("S-------------------------------------------------->" + zakon2);
		
		
		//Опитваме да намерим броя и годината на ДВ
		int beginIndex = parText.indexOf("(");
		int endIndex = parText.indexOf(")");
		if (beginIndex == -1 || endIndex == -1) {
			throw new InvalidParameterException("НЕ СЕ ОТКРИВА ИНФОРМАЦИЯ ЗА БРОЯ НА ДВ !!!! --> " + parText );
		}
		String dvString = parText.substring(beginIndex+ 1, endIndex).trim();
		//System.out.println("------------------------------------------------->" + dvString);
		
		ArrayList<String> foundStrings = getNumbersFromString(dvString);
		
		if (foundStrings.size() != 2) {
			throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ ПАРСНЕ БРОЙ И ГОДИНА НА ДВ !!!! --> " + dvString  );				
		}
		
		String broiDV = foundStrings.get(0);
		String yearDv = foundStrings.get(1);
		//System.out.println("D-------------------------------------------------->" + broiDV + " | " + yearDv);
		
		dosie.setBroiDV(broiDV);
		dosie.setYearDv(yearDv);
		
		
		dosie.setImeZakon1(zakon1);
		dosie.setImeZakon2(zakon2);
		if (zakon1 != null) {
			dosie.setIdZakon1(findZakonInDb(zakon1, null, null));
		}
		if (zakon2 != null) {
			dosie.setIdZakon2(findZakonInDb(zakon2, broiDV, yearDv));
		}
		dosie.setParagraph(parInfo);
		
		
	}





	private String formalizeZakon(String imeZakon) {
		
		imeZakon = imeZakon.trim();
		
		imeZakon = imeZakon.replace("държавния бюджет", "държавния бюджет на Република България");
		imeZakon = imeZakon.replace("на Търговския закон", "Търговски закон");
		imeZakon = imeZakon.replace("Търговския закон", "Търговски закон");
		
		
		
		//Понеже някой започват с "на Закона", а други с "на Закон", ще трябва да го направим така че да е само "Закон"
		if (imeZakon.startsWith("на Закона")) {
			imeZakon = imeZakon.replaceFirst("на Закона", "Закон");
		}else {					
			if (imeZakon.startsWith("на Закон")) {
				imeZakon = imeZakon.replaceFirst("на Закон", "Закон");
			}else {
				if (imeZakon.startsWith("на Гражданския")) {
					imeZakon = imeZakon.replaceFirst("на Гражданския", "Граждански");
				}else {
					if (imeZakon.startsWith("на Наказателно-процесуалния")) {
						imeZakon = imeZakon.replaceFirst("на Наказателно-процесуалния", "Наказателно-процесуален");
					}else {
						if (imeZakon.startsWith("за Закона")) {
							imeZakon = imeZakon.replaceFirst("за Закона", "Закон");
						}else {
							if (imeZakon.startsWith("Закона")) {
								imeZakon = imeZakon.replaceFirst("Закона", "Закон");
							}
						}
					}
				}
			}
		}
		imeZakon = imeZakon.trim();
		if (imeZakon.equals("Наказателно-процесуалния кодекс")) {
			imeZakon = "Наказателно-процесуален кодекс";
		}
		if (imeZakon.equals("Наказателния кодекс")) {
			imeZakon = "Наказателен кодекс";
		}
		
		
		if (imeZakon.equals("на Наказателния кодекс")) {
			imeZakon = "Наказателен кодекс";
		}
		
		if (imeZakon.equals("на Наказателния кодекс")) {
			imeZakon = "Наказателен кодекс";
		}
		
		if (imeZakon.equals("Гражданския процесуален кодекс")) {
			imeZakon = "Граждански процесуален кодекс";
		}
		
		if (imeZakon.equals("на Данъчно-осигурителния процесуален кодекс")) {
			imeZakon = "Данъчно-осигурителен процесуален кодекс";
		}
		
		if (imeZakon.equals("Данъчно-осигурителния процесуален кодекс")) {
			imeZakon = "Данъчно-осигурителен процесуален кодекс";
		}
		
		
		
		
		
		
		
		
		
		return imeZakon;
	}





	
	private  HashMap<String, ArrayList<Node>> splitCellByNumbers (CompositeNode<Node> cell) throws InvalidParameterException {
		
		HashMap<String, ArrayList<Node>> result = new HashMap<String, ArrayList<Node>>();
		
		String currentPoint = "0";
		ArrayList<Node> currentSelection = new ArrayList<Node>();
		NodeCollection<Node> pars = cell.getChildNodes(NodeType.PARAGRAPH, true);
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>" + pars.getCount());
		for (int p = 0; p < pars.getCount(); p++) {
			Node par = pars.get(p);
			String textParagraph = par.getText().trim(); 
			if (!textParagraph.isEmpty() && "123456789".contains(textParagraph.substring(0,1)) && !textParagraph.startsWith(currentPoint)) {
				if (! currentPoint.equals("0")) {
					//System.out.println("Adding point " + currentPoint);
					result.put(currentPoint, currentSelection);
				}
				currentSelection = new ArrayList<Node>();
				currentPoint = getPointFromText(textParagraph);
			}
			currentSelection.add(par);
		}
		result.put(currentPoint, currentSelection);	
		//System.out.println("Adding point " + currentPoint);
		
		return result;
	}
			
			
			
			
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
			
			
			
			
	private String getPointFromText(String textParagraph) throws InvalidParameterException {
		
		String point = "";
		for (int i = 0; i < 10; i ++) {
			String symbol = textParagraph.substring(i, i+1);
			if (!"0123456789.".contains(symbol)) {
				//throw new InvalidParameterException("Не може да се извлече номер от началото на параграф !!" + textParagraph);
				break;
			}			
			point += symbol;
		}
		
		return point;
	}


	
	
	private  String parseHyperLink(String hyperLink) {
		if (hyperLink == null) {
			return null;
		}
		
		if (hyperLink.toUpperCase().startsWith("http")) {
			return hyperLink;
		}
		
		
		hyperLink = hyperLink.replace("", "");
		
		
		int i1 = hyperLink.indexOf("\"");
		int i2 = hyperLink.substring(i1+1).indexOf("\"");
		
		if (i1 == -1 || i2 == -1 || i1==i2 ) {
			
			int i = hyperLink.indexOf("http");
			if (i != -1) {				
				return hyperLink.substring(i);
			}else {			
				return null;
			}
		}
		
		return hyperLink.substring(i1+1, i1+i2+1);
	}
	
	
	
	
	
	
	
	
//	private  void proccessUrl(Dosie dosie) throws InvalidParameterException {
//		
//		//System.out.println(dosie.getUrl());
//		com.itextpdf.styledxmlparser.jsoup.nodes.Document doc;
//		try {
//			doc = Jsoup.parse(readHttpInfo(dosie.getUrl()));
//		} catch (IOException e) {
//			throw new InvalidParameterException("Не може да се открие страница за този CELEX номер !");
//		}
//		
//		//<div class="panel-body" lang="BG">
//		
//		try {
//			Element title = doc.getElementById("translatedTitle");
//			dosie.setNameAct(title.text());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			doc.text();
//		}
//		
////		Elements noms = doc.getElementsByClass("DocumentTitle");
//		//System.out.println("Size:" + noms.size());
////		String celexNumber = noms.get(0).text();
////		if (celexNumber.startsWith("Document ")) {
////			celexNumber = celexNumber.substring(9);
////		}
////		if (celexNumber.startsWith("Документ ")) {
////			celexNumber = celexNumber.substring(9);
////		}	
//	}
	
	
	
	
//	private  String readHttpInfo(String address) throws IOException{
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
	
	private Integer  findZakonInDb(String imeZakon, String broiDV, String yearDv) throws InvalidParameterException {
		
//		if ("СПИРАМЕ ПРОВЕРКАТА".length() > 3) {
//			return -1L;
//		}
		
		
		
		Integer idZakon = null;
		//ТЪРСЕНЕ НА ЗАКОНА В БД
		Query q = JPA.getUtil().getEntityManager().createNativeQuery("select id, BROI_DV from ZD_ZAKONOPROEKTI where upper(IME_ZAKON) = :IME");
		q.setParameter("IME", imeZakon.toUpperCase());
		
		ArrayList<Object[]> results = (ArrayList<Object[]>) q.getResultList();
		if (results.size() == 0) {
			//throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ ! --> |" + imeZakon + "|"  );
//			System.out.println();
//			System.out.println("НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ ! --> |" + imeZakon + "|");
		}
		
		if (broiDV == null ) {
			if(results.size() != 1) {
				//throw new InvalidParameterException("Нe може да се определи еднозначно закон по име ! --> |" + imeZakon + "|"  );
				//System.out.println("Нe може да се определи еднозначно закон по име ! --> |" + imeZakon + "|");
			}else {
				idZakon = SearchUtils.asInteger(results.get(0)[0]);
			}
		}else {
			for (Object[] row : results) {
				Integer id = SearchUtils.asInteger(row[0]);
				String dvStr = SearchUtils.asString(row[1]);
				
				if (dvStr != null) {
					ArrayList<String> foundStrings2 = getNumbersFromString(dvStr);					
					if (foundStrings2.size() != 2) {
						continue;				
					}
					String broiDV_DB = foundStrings2.get(0);
					String yearDv_DB = foundStrings2.get(1);
					if (broiDV_DB.equals(broiDV) && yearDv_DB.equals(yearDv)) {
						idZakon = id;
						break;
					}
					
				}else {
					continue;
				}			
			}
			
		}
		
				
		if (idZakon == null) {
//			System.out.println();
//			System.out.println("НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ И ДВ ! --> " + imeZakon + " | " + broiDV + " | " + yearDv);
			//throw new InvalidParameterException("НЕ МОЖЕ ДА СЕ НАМЕРИ ЗАКОН ПО ИМЕ И ДВ ! --> " + imeZakon + " | " + broiDV + " | " + yearDv );
			
		}
		
		return idZakon;
	}
	
	
//	@Deprecated
//	private ArrayList<Node> extractRows(HashMap<String, ArrayList<Node>> source, String startingWith) {
//		ArrayList<Node> result = new ArrayList<Node>();
//		Iterator<Entry<String, ArrayList<Node>>> it = source.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<String, ArrayList<Node>> entry = it.next();
//			if (entry.getKey().startsWith(startingWith)) {
//				result.addAll(entry.getValue());
//			}
//		}
//		
//		
//		return result;
//		
//	}
	
	private String extractActName(String textNode) {
		
		int indBegAct = textNode.toUpperCase().indexOf("РАМКОВО РЕШЕНИЕ");
		if (indBegAct != -1 && indBegAct < 10) {
			return textNode.substring(indBegAct);
		}
		
		indBegAct = textNode.toUpperCase().indexOf("ДИРЕКТИВА");
		if (indBegAct != -1 && indBegAct < 10) {
			return textNode.substring(indBegAct);
		}
		
		indBegAct = textNode.toUpperCase().indexOf("РЕШЕНИЕ");
		if (indBegAct != -1 && indBegAct < 10) {
			return textNode.substring(indBegAct);
		}
		
		indBegAct = textNode.toUpperCase().indexOf("РЕГЛАМЕНТ");
		if (indBegAct != -1 && indBegAct < 10) {
			return textNode.substring(indBegAct);
		}
		
		indBegAct = textNode.toUpperCase().indexOf("Council Directive".toUpperCase());
		if (indBegAct != -1 && indBegAct < 10) {
			return textNode.substring(indBegAct);
		}
		 
		
		return textNode.trim();
	}





//	private boolean checkForHyperlinks(ArrayList<Node> point) {
//		
//		//System.out.println("Point PSize="+ point.size());
//		for (Node curNode : point) {
//			String textNode = curNode.getText();			
//			if (isHyperLink(textNode)) {
//				//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + textNode);
//				return true;
//			}
//		}
//		return false;
//	}
	
	
	public void recognizeNumber(Dosie dosie) throws InvalidParameterException {
		
		if ("(ЕО) № 1998/2006".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(1998);
			return;
		}
		
		if ("(ЕО) № 1993/2004".equals(dosie.getNomer())) {
			dosie.setYearAct(2004);
			dosie.setNomerAct(1993);
			return;
		}
		
		if ("(ЕО) № 2006/2004".equals(dosie.getNomer())) {
			dosie.setYearAct(2004);
			dosie.setNomerAct(2006);
			return;
		}
		
		if ("(ЕС) 2019/2020".equals(dosie.getNomer())) {
			dosie.setYearAct(2019);
			dosie.setNomerAct(2020);
			return;
		}
		
		if ("(ЕС) 2019/2019".equals(dosie.getNomer())) {
			dosie.setYearAct(2019);
			dosie.setNomerAct(2019);
			return;
		}
		
		if ("2010/C 186/1".equals(dosie.getNomer())) {
			dosie.setYearAct(2010);
			dosie.setNomerAct(186);
			return;
		}
		
		if ("2010/С 186/1".equals(dosie.getNomer())) {
			dosie.setYearAct(2010);
			dosie.setNomerAct(186);
			return;
		}
		
		if ("(ЕО) № 1192".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(1192);
			return;
		}
		
		if ("(ЕО) № 2007/2006".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(2007);
			return;
		}
		
		if ("(ЕС) 2019/1995".equals(dosie.getNomer())) {
			dosie.setYearAct(2019);
			dosie.setNomerAct(1995);
			return;
		}
		
		if ("(ЕС) 2018/1999".equals(dosie.getNomer())) {
			dosie.setYearAct(2018);
			dosie.setNomerAct(1999);
			return;
		}
		
		if ("(ЕС) 2016/2020".equals(dosie.getNomer())) {
			dosie.setYearAct(2016);
			dosie.setNomerAct(2020);
			return;
		}
		
		if ("(ЕС) 2017/1991".equals(dosie.getNomer())) {
			dosie.setYearAct(2017);
			dosie.setNomerAct(1991);
			return;
		}
		
		if ("(ЕО) № 2011/2006".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(2011);
			return;
		}
		
		if ("(ЕО) № 2012/2006".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(2012);
			return;
		}
		
		if ("(ЕО) № 2013/2006".equals(dosie.getNomer())) {
			dosie.setYearAct(2006);
			dosie.setNomerAct(2013);
			return;
		}
		
		if ("(ЕС) 2015/1991".equals(dosie.getNomer())) {
			dosie.setYearAct(2015);
			dosie.setNomerAct(1991);
			return;
		}
		
		if ("(ЕО) № 1991/2004".equals(dosie.getNomer())) {
			dosie.setYearAct(2004);
			dosie.setNomerAct(1991);
			return;
		}
		
		
		
		
		Integer nomerAct = null;
		Integer yearAct = null;
		
		
		if (dosie.getNomer() == null) {
			return;
		}
		
		ArrayList<String> splitted = getNumbersFromString(dosie.getNomer());
		if (splitted.size() != 2) {
			throw new InvalidParameterException(splitted.size() + "--------------------------------------------> НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getNomer());
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
					throw new InvalidParameterException("ГОДИНИ--------------------------------------------> НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getNomer());
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
		
		
//		if (yearAct == null) {
//			//Ако и сега не сме я разпознали ..... гръмим
//			throw new InvalidParameterException("ГОДИНИ--------------------------------------------> НЕ МОЖЕ ДА СЕ РАЗПОЗНАЕ НОМЕР: " + dosie.getNomer());
//		}
		
		
		//Анализ на годината
		if (yearAct < 100 && yearAct > 50) {
			yearAct += 1900;
		}else {
			if (yearAct < 22) {
				yearAct += 2000;
			}
		}
		
		
		
		dosie.setYearAct(yearAct);
		dosie.setNomerAct(nomerAct);
	}
	
	
//	private int findNotNullRows(ArrayList<Node> nodes) {
//		int result = 0;
//		for (Node node : nodes) {
//			if (!node.getText().trim().isEmpty()) {
//				result++;
//			}
//		}
//		
//		
//		return result;
//	}
	
	
//	private String mergePoint(ArrayList<Node> nodes) {
//		String s = "";
//		for (Node node : nodes) {
//			if (!node.getText().trim().isEmpty()) {
//				s += "\r\n" + node.getText();
//			}
//		}
//		
//		
//		return s;
//	}
	
	
//	public void createDosieFromContainer(Container c) {
		
//		Dosie d = new Dosie();
//		d.setNameAct(c.getName());
//		d.setUrl(c.getUrl());
//		
//		String foundVid = null;
//		
//		for (String vid : vidove) {
//			if (c.getName().toUpperCase().trim().startsWith(vid.toUpperCase())) {
//				foundVid = vid;
//				break;
//			}
//		}
//		
//		d.setTip(foundVid);
//		
//		
//		
//		try {
//			if (c.getUrl() != null) {
//				proccessUrl(d);
//			}
//		} catch (InvalidParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
	
	
//	private void removeHyperLink(Node node) {
//		for (com.aspose.words.Field field : node.getRange().getFields())	{
//
//			if (field.getType() == FieldType.FIELD_HYPERLINK)	{
//				try {
//					field.remove();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	
	private boolean isHyperLink(String link) {
		if (link == null) {
			return false;
		}else {
			if (link.toUpperCase().startsWith("HTTP")) {
				return true;
			}else {
				if (link.contains("HYPERLINK")){
					return true;
				}else {
					return false;
				}
			}
		}
	}
	
	
	private String converttoHTML(Paragraph curNode) {
		
		String html = "";
		
		NodeCollection<Node> nodes = curNode.getChildNodes();
		for (Node n : nodes) {
			if (n.getNodeType() == NodeType.RUN ) {						
				Run r = (Run)n;
				//System.out.println(r.getText() + "\t-->\t BOLD:" + r.getFont().getBold() + "\tITALIC:" + r.getFont().getItalic() + "\t UNDERLINE:" + r.getFont().getUnderline());
				String temp = n.getText();
				if (r.getFont().getBold()) {
					temp = "<b>" + temp + "</b>";
				}
				if (r.getFont().getItalic()) {
					temp = "<i>" + temp + "</i>";
				}
				if (r.getFont().getUnderline() == 1) {
					temp = "<u>" + temp + "</u>";
				}
				html += temp;
				
			}else {
				html += n.getText(); 
			}
		}
		
		
		return html + "<br>";
	}
	
	
	private void getCOMFromTekst(Dosie dosie) {
		
		String note = dosie.getRemarkE();
		String vid = null;
		int index = note.indexOf("COM ");
		if (index != -1) {
			vid = "COM";
		}else {
			index = note.indexOf("COM");
			if (index != -1) {
				vid = "COM";
			}else {
				index = note.indexOf("COM");
				if (index != -1) {
					vid = "COM";
				}
			}
			
		}
		
		if (index != -1) {
			
			String pattern = "COM(1234567890)/ ";
			String nom = "";
			String tek = note.substring(index, index+1);
			while (pattern.contains(tek)) {
				nom += tek;
				index++;
				tek = note.substring(index, index+1);
			}
			
			
			
			ArrayList<String> noms = getNumbersFromString(nom);
			
			dosie.setComYear(Integer.parseInt(noms.get(0)));
			dosie.setComNom(Integer.parseInt(noms.get(1)));
			
			//System.out.println("COOOOOOOOOOOMMMMMMMMMMMMMMMMM -----------------------------------> " + nom + " -> " + dosie.getComYear() + "\t" + dosie.getComNom());
			
			
		}else {
			return ;
		}
	}
	
	
	public static void extractDVfromZ(EuroActNewSectionZ sec) throws InvalidParameterException {
		
		String s = sec.getNameBgAct();
		if (s == null) {
			return;
		}
		
		
		int beginIndex = s.lastIndexOf("(");
		int endIndex = s.lastIndexOf(")");
		
		if (beginIndex == -1 || endIndex == -1 || endIndex < beginIndex) {
			//throw new InvalidParameterException("Неправилно форматиран акт в секция 'З' -> " + beginIndex + "|" + endIndex );
			return;
		}
		
		String dvInfo = s.substring(beginIndex+1, endIndex);
		//System.out.println(dvInfo);
		
		int brIndex = dvInfo.indexOf("бр.");
		if (brIndex == -1) {
			//throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва бр.) -> " + dvInfo );
			return;
		}
		brIndex+=3;
		int otIndex = dvInfo.indexOf("от");
		if (otIndex == -1) {
			//throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва от) -> " + dvInfo );
			return;
		}
		
		int godIndex = dvInfo.indexOf("г");
		if (godIndex == -1) {
			//throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва г.) -> " + dvInfo );
			return;
		}
		
		String brStr = dvInfo.substring(brIndex,otIndex).trim();
		String godStr = dvInfo.substring(otIndex+2,godIndex).trim();
		//System.out.println("|"+brStr+"|"+godStr+"|");
		
		sec.setDvBroi(brStr);
		try {
			sec.setDvGodina(Integer.parseInt(godStr));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	
}
