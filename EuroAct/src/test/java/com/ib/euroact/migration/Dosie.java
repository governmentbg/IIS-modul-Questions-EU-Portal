package com.ib.euroact.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.ib.euroact.db.dto.Dossier;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.euroact.system.EuroConstants;

public class Dosie {
	
	
	private String filename;
	
	private String tip;
	private Integer tipL = 0;
	
	//От заглавието на досието
	private String imeZakon1; //Закон
	private Integer idZakon1;
	private String paragraph; //параграф 
	private String imeZakon2; //Хармонизиран със 
	private Integer idZakon2;
	private String broiDV;
	private String yearDv;
	
	private String nomer;
	private String url;
	private String celexNumber;
	
	private String nameAct;	
	private Integer nomerAct;
	private Integer yearAct;
	
	private String name;	
	
	private Integer comYear;
	private Integer comNom;
	private String  comVid; 
	
	

	//За късата версия на АБ секцията - предвижда мерки / въвежда изисквания
	private boolean hasMerkiIziskvania;
	
	//За дългата версия
	private ArrayList<Container> ab2list = new ArrayList<Container>();
	private ArrayList<Container> ab3list = new ArrayList<Container>();
	private ArrayList<Container> ab4list = new ArrayList<Container>();
	private ArrayList<Container> ab5list = new ArrayList<Container>();
	
	private String remarkab2;
	private String remarkab3;
	private String remarkab4D;
	private String remarkab4I;
		
	private boolean delNeSePriemat4 = false;
	private boolean izpNeSePriemat4 = false;
	
	//Сеекция В 
	private boolean hasAdmReguliraneV;
	private String remarkV;
	
	//Секция Г
	private String remarkSR;
	private ArrayList<Container> glist = new ArrayList<Container>();
	
	//Секция Д
	private String textSectionD;
	private String urlSectionD;
	private boolean hasProcedureD;
	
	
	//Секция Е
	private String remarkE;
	private ArrayList<Container> elist = new ArrayList<Container>();
	
	//Секция Ж
	private String tehReglNomJ;
	private Date tehReglDatJ;
	private String tehReglURLJ;
	
	//Секция З
	private ArrayList<Container> zlist = new ArrayList<Container>();
	
	//Секция И
	private HashMap<Integer, String> sectionI = new HashMap<Integer, String>();
	
	
	
	
	
	
	private Integer idAct;
	
	
	
	
	
	
	public Integer getTipL() {
		return tipL;
	}
	public void setTipL(Integer tipL) {
		this.tipL = tipL;
	}
	public Integer getIdZakon1() {
		return idZakon1;
	}
	public void setIdZakon1(Integer idZakon1) {
		this.idZakon1 = idZakon1;
	}
	public Integer getIdZakon2() {
		return idZakon2;
	}
	public void setIdZakon2(Integer idZakon2) {
		this.idZakon2 = idZakon2;
	}
	public String getParagraph() {
		return paragraph;
	}
	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}
	public String getCelexNumber() {
		return celexNumber;
	}
	public void setCelexNumber(String celexNumber) {
		this.celexNumber = celexNumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getImeZakon2() {
		return imeZakon2;
	}
	public void setImeZakon2(String imeZakon2) {
		this.imeZakon2 = imeZakon2;
	}
	public String getBroiDV() {
		return broiDV;
	}
	public void setBroiDV(String broiDV) {
		this.broiDV = broiDV;
	}
	public String getYearDv() {
		return yearDv;
	}
	public void setYearDv(String yearDv) {
		this.yearDv = yearDv;
	}
	
	public String getNomer() {
		return nomer.trim();
	}
	public void setNomer(String nomer) {
		this.nomer = nomer;
	}
	public String getImeZakon1() {
		return imeZakon1;
	}
	public void setImeZakon1(String imeZakon1) {
		this.imeZakon1 = imeZakon1;
	}
	public String getNameAct() {
		return nameAct;
	}
	public void setNameAct(String nameAct) {
		this.nameAct = nameAct;
	}
	public Integer getNomerAct() {
		return nomerAct;
	}
	public void setNomerAct(Integer nomerAct) {
		this.nomerAct = nomerAct;
	}
	public Integer getYearAct() {
		return yearAct;
	}
	public void setYearAct(Integer yearAct) {
		this.yearAct = yearAct;
	}
	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	public ArrayList<Container> getGlist() {
		return glist;
	}
	public void setGlist(ArrayList<Container> glist) {
		this.glist = glist;
	}
	public ArrayList<Container> getElist() {
		return elist;
	}
	public void setElist(ArrayList<Container> elist) {
		this.elist = elist;
	}
	
	public boolean isHasMerkiIziskvania() {
		return hasMerkiIziskvania;
	}
	public void setHasMerkiIziskvania(boolean hasMerkiIziskvania) {
		this.hasMerkiIziskvania = hasMerkiIziskvania;
	}
	public ArrayList<Container> getAb2list() {
		return ab2list;
	}
	public void setAb2list(ArrayList<Container> ab2list) {
		this.ab2list = ab2list;
	}
	public ArrayList<Container> getAb3list() {
		return ab3list;
	}
	public void setAb3list(ArrayList<Container> ab3list) {
		this.ab3list = ab3list;
	}
	public ArrayList<Container> getAb4list() {
		return ab4list;
	}
	public void setAb4list(ArrayList<Container> ab4list) {
		this.ab4list = ab4list;
	}
	public ArrayList<Container> getAb5list() {
		return ab5list;
	}
	public void setAb5list(ArrayList<Container> ab5list) {
		this.ab5list = ab5list;
	}
	public String getRemarkab2() {
		return remarkab2;
	}
	public void setRemarkab2(String remarkab2) {
		this.remarkab2 = remarkab2;
	}
	public String getRemarkab3() {
		return remarkab3;
	}
	public void setRemarkab3(String remarkab3) {
		this.remarkab3 = remarkab3;
	}
	
	public boolean isDelNeSePriemat4() {
		return delNeSePriemat4;
	}
	public void setDelNeSePriemat4(boolean delNeSePriemat4) {
		this.delNeSePriemat4 = delNeSePriemat4;
	}
	public boolean isIzpNeSePriemat4() {
		return izpNeSePriemat4;
	}
	public void setIzpNeSePriemat4(boolean izpNeSePriemat4) {
		this.izpNeSePriemat4 = izpNeSePriemat4;
	}

	public String getRemarkV() {
		return remarkV;
	}
	public void setRemarkV(String remarkV) {
		this.remarkV = remarkV;
	}
	
	public String getTextSectionD() {
		return textSectionD;
	}
	public void setTextSectionD(String textSectionD) {
		this.textSectionD = textSectionD;
	}
	public boolean isHasProcedureD() {
		return hasProcedureD;
	}
	public void setHasProcedureD(boolean hasProcedureD) {
		this.hasProcedureD = hasProcedureD;
	}
	public boolean isHasAdmReguliraneV() {
		return hasAdmReguliraneV;
	}
	public void setHasAdmReguliraneV(boolean hasAdmReguliraneV) {
		this.hasAdmReguliraneV = hasAdmReguliraneV;
	}
	public String getTehReglNomJ() {
		return tehReglNomJ;
	}
	public void setTehReglNomJ(String tehReglNomJ) {
		this.tehReglNomJ = tehReglNomJ;
	}
	public Date getTehReglDatJ() {
		return tehReglDatJ;
	}
	public void setTehReglDatJ(Date tehReglDatJ) {
		this.tehReglDatJ = tehReglDatJ;
	}
	public String getTehReglURLJ() {
		return tehReglURLJ;
	}
	public void setTehReglURLJ(String tehReglURLJ) {
		this.tehReglURLJ = tehReglURLJ;
	}
	public ArrayList<Container> getZlist() {
		return zlist;
	}
	public void setZlist(ArrayList<Container> zlist) {
		this.zlist = zlist;
	}
	public HashMap<Integer, String> getSectionI() {
		return sectionI;
	}
	public void setSectionI(HashMap<Integer, String> sectionI) {
		this.sectionI = sectionI;
	}
	
	public Integer getIdAct() {
		return idAct;
	}
	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}
	public String getRemarkSR() {
		return remarkSR;
	}
	public void setRemarkSR(String remarkSR) {
		this.remarkSR = remarkSR;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	
	
	public String getRemarkE() {
		return remarkE;
	}
	public void setRemarkE(String remarkE) {
		this.remarkE = remarkE;
	}
	public EuroActNew toEuroActNew() {
		EuroActNew act = new EuroActNew();
		
		act = new EuroActNew();
		act.setRnFull(nomer);
		act.setRn(nomerAct);
		act.setCelex(celexNumber);
		act.setGodina(yearAct);
		act.setIme(nameAct);
		act.setUrl(url);
		
		if (comYear != null) {
			act.setComVid(7060);
			act.setComYear(comYear);
			act.setComNumber(comNom);
		}
		
		
		
		if (isIzpNeSePriemat4()) {
			act.setIzpalnenieActYesNo(EuroConstants.CODE_OTG_NE_SE_PRIEMAT);
		}else {
			act.setIzpalnenieActYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI);
		}
		
		if (isDelNeSePriemat4()) {
			act.setDelegiraniActsYesNo(EuroConstants.CODE_OTG_NE_SE_PRIEMAT);
		}else {
			act.setDelegiraniActsYesNo(EuroConstants.CODE_OTG_NIAMA_PRIETI);
		}
				
		for ( Container con: ab4list) {
			if (con.getAction().equals(EuroConstants.VID_VRAZ_OSN_DEL)) {
				act.setDelegiraniActsYesNo(EuroConstants.CODE_OTG_IMA_PRIETI);
			}
			
			if (con.getAction().equals(EuroConstants.VID_VRAZ_OSN_IZP)) {
				act.setIzpalnenieActYesNo(EuroConstants.CODE_OTG_IMA_PRIETI);
			}
		}
			
		act.setNoteAB2(remarkab2);
		act.setNoteAB3(remarkab3);
		act.setNoteAB4D(remarkab4D);
		act.setNoteAB4I(remarkab4I);
		act.setNoteSr(null);   //????
		act.setSectionDText(textSectionD);
		act.setSectionDUrl(urlSectionD);
		if (textSectionD != null) {
			act.setSectionDYesNo(1);
		}else {
			act.setSectionDYesNo(2);
		}
		//act.setSectionEText("e");
		//act.setSectionEYesNo(1);
		if (glist.size() > 0) {
			act.setSectionGYesNo(1);
		}else {
			act.setSectionGYesNo(2);
		}
		
		
		if (elist.size() > 0) {
			act.setSectionEYesNo(1);
		}else {
			act.setSectionEYesNo(2);
		}
		
		if (remarkE != null) {
			act.setSectionEText(remarkE);
		}
		
		
		if (zlist.size() > 0) {
			act.setSectionZYesNo(1);
		}else {
			act.setSectionZYesNo(2);
		}
		
		act.setVidAct(tipL);
		//act.setSectionZText("z");
		
		if (remarkSR != null) {
			act.setNoteSr(remarkSR);
		}
		
		
		
		//act.setVidAct(111);
		
		act.setFileName(filename);
		
		return act;
	}
	
	
	public Dossier toDossier() {
		
		Dossier d  = new Dossier();
		
		d.setName(name);
		d.setZakonName(imeZakon1);
		d.setZakonNameV(imeZakon2);
		d.setZakonId(idZakon1);
		d.setZakonIdV(idZakon2);
		d.setNoteV(paragraph);
		d.setEuroActNewId(idAct);
		
		d.setFileName(filename);
		
		
//		Секция А/Б къса част
		if (hasMerkiIziskvania) {
			d.setReqMeasuredYesNo(1);
		}else {
			d.setReqMeasuredYesNo(2);
		}
				
		// Секция В
		if (hasAdmReguliraneV) {
			d.setSectionVYesNo(1);
			d.setSectionVText(remarkV);
		}else {
			d.setSectionVYesNo(2);	
		}
		
		
		
		//Секция Ж				
		d.setNotificationsNumber(tehReglNomJ);		     	
		d.setNotificationsDate(tehReglDatJ);
		d.setNotificationsURL(tehReglURLJ);
		if (tehReglDatJ!= null) {
			d.setNotificationsYesNo(1); 
		}else {
			d.setNotificationsYesNo(2); 
		}
		
		
		//Секция И
		
		d.setSectionI1(sectionI.get(1));
		d.setSectionI2(sectionI.get(2));	
		d.setSectionI3(sectionI.get(3));	
		d.setSectionI4(sectionI.get(4));
		
		
		if (yearDv != null) {
			d.setZakonDvGodV(Integer.parseInt(yearDv));
		}
		
		if (broiDV != null) {
			d.setZakonDvBrV(Integer.parseInt(broiDV));
		}
		
		return d;
		
		
	}
	public String getRemarkab4D() {
		return remarkab4D;
	}
	public void setRemarkab4D(String remarkab4d) {
		remarkab4D = remarkab4d;
	}
	public String getRemarkab4I() {
		return remarkab4I;
	}
	public void setRemarkab4I(String remarkab4i) {
		remarkab4I = remarkab4i;
	}
	public Integer getComNom() {
		return comNom;
	}
	public void setComNom(Integer comNom) {
		this.comNom = comNom;
	}
	public Integer getComYear() {
		return comYear;
	}
	public void setComYear(Integer comYear) {
		this.comYear = comYear;
	}
	public String getComVid() {
		return comVid;
	}
	public void setComVid(String comVid) {
		this.comVid = comVid;
	}
	public String getUrlSectionD() {
		return urlSectionD;
	}
	public void setUrlSectionD(String urlSectionD) {
		this.urlSectionD = urlSectionD;
	}
	
	
	
	
	

}
