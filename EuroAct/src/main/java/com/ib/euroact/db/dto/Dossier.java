package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.TrackableEntity;

@Entity
@Table(name = "EURO_DOSSIER")
public class Dossier extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314332234092965330L;

	@SequenceGenerator(name = "Dossier", sequenceName = "SEQ_EURO_DOSSIER", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "Dossier")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	       
	//Основни данни 
	@Column(name = "NАМЕ")
	private String name;
	
	@Column(name = "EURO_ACT_NEW_ID")
	private Integer euroActNewId;
	
	@Column(name = "ZAKON_ID")
	private Integer zakonId;
		
	@Column(name = "ZAKON_NAME")
	private String zakonName;
		
	@Column(name = "ZAKON_ID_V")
	private Integer zakonIdV;
		
	@Column(name = "ZAKON_NAME_V")
	private String zakonNameV;
	
	@Column(name = "NOTE_V")
	private String noteV;
	
	@Column(name = "ON_SITE_YESNO")
	private Integer onSiteYesNo = 2;
	
	
	@Column(name = "FILENAME")
	private String fileName;
	
	
	@Column(name = "ZAKON_DV_BR_V")
	private Integer zakonDvBrV;
	
	@Column(name = "ZAKON_DV_GOD_V")
	private Integer zakonDvGodV;
	
	
	///////////////////////////////
	
	

	//	Секция А/Б къса част
	@Column(name = "REQ_MEASURE_YESNO")
	private Integer reqMeasuredYesNo;
	
	
	
	
	
	//Секция В
	@Column(name = "SECTION_V_YESNO")
	private Integer sectionVYesNo; 
	
	@Column(name = "SECTION_V_TEXT")
	private String sectionVText;
	
		
	
	//Секция Ж
	@Column(name = "NOTIFICATIONS_YESNO")
	private Integer notificationsYesNo; 
	
	@Column(name = "NOTIFICATIONS_NUMBER")
	private String notificationsNumber;
	     	
	@Column(name = "NOTIFICATIONS_DATE")
	private Date notificationsDate;
	
	@Column(name = "NOTIFICATIONS_URL")
	private String notificationsURL;	
	
	
	//Секция И
	
	@Column(name = "SECTION_I1")
	private String sectionI1;
	
	@Column(name = "SECTION_I2")
	private String sectionI2;
	
	@Column(name = "SECTION_I3")
	private String sectionI3;
	
	@Column(name = "SECTION_I4")
	private String sectionI4;
	
	
	@Transient
	private Integer vidAct; 
	
	@Transient
	private String nameAct; 
	
	@Transient
	ArrayList<Dossier> linksDir = new ArrayList<Dossier>();
	
	@Transient
	ArrayList<Dossier> linksReg = new ArrayList<Dossier>();
	
	@Column(name = "TEMA")
	private Integer tema;
	
	
	
//	@Transient
//	private ArrayList<EuroActNewConn> sectionAB2   = new ArrayList<EuroActNewConn>();
	
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_DOSSIER;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEuroActNewId() {
		return euroActNewId;
	}

	public void setEuroActNewId(Integer euroActNewId) {
		this.euroActNewId = euroActNewId;
	}

	public Integer getZakonId() {
		return zakonId;
	}

	public void setZakonId(Integer zakonId) {
		this.zakonId = zakonId;
	}

	public String getZakonName() {
		return zakonName;
	}

	public void setZakonName(String zakonName) {
		this.zakonName = zakonName;
	}

	public Integer getZakonIdV() {
		return zakonIdV;
	}

	public void setZakonIdV(Integer zakonIdV) {
		this.zakonIdV = zakonIdV;
	}

	public String getZakonNameV() {
		return zakonNameV;
	}

	public void setZakonNameV(String zakonNameV) {
		this.zakonNameV = zakonNameV;
	}

	public String getNoteV() {
		return noteV;
	}

	public void setNoteV(String noteV) {
		this.noteV = noteV;
	}

	

	public Integer getOnSiteYesNo() {
		return onSiteYesNo;
	}

	public void setOnSiteYesNo(Integer onSiteYesNo) {
		this.onSiteYesNo = onSiteYesNo;
	}

	
	public Integer getSectionVYesNo() {
		return sectionVYesNo;
	}

	public void setSectionVYesNo(Integer sectionVYesNo) {
		this.sectionVYesNo = sectionVYesNo;
	}

	public String getSectionVText() {
		return sectionVText;
	}

	public void setSectionVText(String sectionVText) {
		this.sectionVText = sectionVText;
	}

	public Integer getNotificationsYesNo() {
		return notificationsYesNo;
	}

	public void setNotificationsYesNo(Integer notificationsYesNo) {
		this.notificationsYesNo = notificationsYesNo;
	}

	public String getNotificationsNumber() {
		return notificationsNumber;
	}

	public void setNotificationsNumber(String notificationsNumber) {
		this.notificationsNumber = notificationsNumber;
	}

	public Date getNotificationsDate() {
		return notificationsDate;
	}

	public void setNotificationsDate(Date notificationsDate) {
		this.notificationsDate = notificationsDate;
	}

	public String getNotificationsURL() {
		return notificationsURL;
	}

	public void setNotificationsURL(String notificationsURL) {
		this.notificationsURL = notificationsURL;
	}

	public String getSectionI1() {
		return sectionI1;
	}

	public void setSectionI1(String sectionI1) {
		this.sectionI1 = sectionI1;
	}

	public String getSectionI2() {
		return sectionI2;
	}

	public void setSectionI2(String sectionI2) {
		this.sectionI2 = sectionI2;
	}

	public String getSectionI3() {
		return sectionI3;
	}

	public void setSectionI3(String sectionI3) {
		this.sectionI3 = sectionI3;
	}

	public String getSectionI4() {
		return sectionI4;
	}

	public void setSectionI4(String sectionI4) {
		this.sectionI4 = sectionI4;
	}

	public Integer getReqMeasuredYesNo() {
		return reqMeasuredYesNo;
	}

	public void setReqMeasuredYesNo(Integer reqMeasuredYesNo) {
		this.reqMeasuredYesNo = reqMeasuredYesNo;
	}

	public Integer getZakonDvBrV() {
		return zakonDvBrV;
	}

	public void setZakonDvBrV(Integer zakonDvBrV) {
		this.zakonDvBrV = zakonDvBrV;
	}

	public Integer getZakonDvGodV() {
		return zakonDvGodV;
	}

	public void setZakonDvGodV(Integer zakonDvGodV) {
		this.zakonDvGodV = zakonDvGodV;
	}

	public Integer getVidAct() {
		return vidAct;
	}

	public void setVidAct(Integer vidAct) {
		this.vidAct = vidAct;
	}

	public String getNameAct() {
		return nameAct;
	}

	public void setNameAct(String nameAct) {
		this.nameAct = nameAct;
	}

	public ArrayList<Dossier> getLinksDir() {
		return linksDir;
	}

	public void setLinksDir(ArrayList<Dossier> linksDir) {
		this.linksDir = linksDir;
	}

	public ArrayList<Dossier> getLinksReg() {
		return linksReg;
	}

	public void setLinksReg(ArrayList<Dossier> linksReg) {
		this.linksReg = linksReg;
	}

	public Integer getTema() {
		return tema;
	}

	public void setTema(Integer tema) {
		this.tema = tema;
	}
	

}
