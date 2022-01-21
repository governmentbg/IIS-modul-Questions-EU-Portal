package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;

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
@Table(name = "EURO_ACT_NEW")
public class EuroActNew extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314332234092965330L;

	@SequenceGenerator(name = "EuroActNew", sequenceName = "SEQ_EURO_ACT_NEW", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroActNew")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	

	@Column(name = "RN")
	private Integer rn;
	
	@Column(name = "GODINA")
	private Integer godina;
	
	@Column(name = "RN_FULL")
	private String rnFull;
	
	@Column(name = "IME")
	private String ime;
	
	@Column(name = "CELEX")
	private String celex;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "VID_ACT")
	private Integer vidAct;
	
	@Column(name = "NOTE_SR")
	private String noteSr;
	
	@Column(name = "NOTE_AB2")
	private String noteAB2;
	
	@Column(name = "NOTE_AB3")
	private String noteAB3;
	
	@Column(name = "NOTE_AB4D")
	private String noteAB4D;
	
	@Column(name = "NOTE_AB4I")
	private String noteAB4I;
	
	@Column(name = "NOTE_G")
	private String noteG;
	
	@Column(name = "IZPALNENIE_ACTS_YESNO")
	private Integer izpalnenieActYesNo = EuroConstants.CODE_OTG_NIAMA_PRIETI;
	
	@Column(name = "DELEGIRANI_ACTS_YESNO")
	private Integer delegiraniActsYesNo = EuroConstants.CODE_OTG_NIAMA_PRIETI;
	
	@Column(name = "SECTION_G_YESNO")
	private Integer sectionGYesNo = EuroConstants.CODE_ZNACHENIE_NE;
	
	@Column(name = "SECTION_D_YESNO")
	private Integer sectionDYesNo = EuroConstants.CODE_ZNACHENIE_NE;
	@Column(name = "SECTION_D_TEXT")
	private String sectionDText;
	
	@Column(name = "SECTION_D_URL")
	private String sectionDUrl;
	
	
	

	@Column(name = "SECTION_E_YESNO")
	private Integer sectionEYesNo = EuroConstants.CODE_ZNACHENIE_NE;	
	@Column(name = "SECTION_E_TEXT")
	private String sectionEText;
	
	@Column(name = "SECTION_Z_YESNO")
	private Integer sectionZYesNo = EuroConstants.CODE_ZNACHENIE_NE;	
	@Column(name = "SECTION_Z_TEXT")
	private String sectionZText;
	
	@Column(name = "FILENAME")
	private String fileName;
	
	@Column(name = "COM_VID")
	private Integer comVid;
	
	@Column(name = "COM_YEAR")
	private Integer comYear;
	
	@Column(name = "COM_NUMBER")
	private Integer comNumber;
	

	

	
	
	
	

	@Transient
	private ArrayList<EuroActNewConn> sectionAB2   = new ArrayList<EuroActNewConn>();
	@Transient
	private ArrayList<EuroActNewConn> sectionAB3   = new ArrayList<EuroActNewConn>();
	@Transient
	private ArrayList<EuroActNewConn> sectionAB4i  = new ArrayList<EuroActNewConn>();
	@Transient
	private ArrayList<EuroActNewConn> sectionAB4d  = new ArrayList<EuroActNewConn>();
	@Transient
	private ArrayList<EuroActNewConn> sectionG = new ArrayList<EuroActNewConn>();
	@Transient
	private ArrayList<EuroActNewSectionE> sectionE = new ArrayList<EuroActNewSectionE>();
	@Transient
	private ArrayList<EuroActNewSectionZ> sectionZ = new ArrayList<EuroActNewSectionZ>();
				         
		   
		
	public String getNoteSr() {
		return noteSr;
	}

	public void setNoteSr(String noteSr) {
		this.noteSr = noteSr;
	}

	public String getNoteAB2() {
		return noteAB2;
	}

	public void setNoteAB2(String noteAB2) {
		this.noteAB2 = noteAB2;
	}

	public String getNoteAB3() {
		return noteAB3;
	}

	public void setNoteAB3(String noteAB3) {
		this.noteAB3 = noteAB3;
	}

	

	public Integer getIzpalnenieActYesNo() {
		return izpalnenieActYesNo;
	}

	public void setIzpalnenieActYesNo(Integer izpalnenieActYesNo) {
		this.izpalnenieActYesNo = izpalnenieActYesNo;
	}

	public Integer getDelegiraniActsYesNo() {
		return delegiraniActsYesNo;
	}

	public void setDelegiraniActsYesNo(Integer delegiraniActsYesNo) {
		this.delegiraniActsYesNo = delegiraniActsYesNo;
	}

	public Integer getSectionGYesNo() {
		return sectionGYesNo;
	}

	public void setSectionGYesNo(Integer sectionGYesNo) {
		this.sectionGYesNo = sectionGYesNo;
	}
	
	public boolean isSectionGYesNoBool() {
		return (sectionGYesNo==null?false:(sectionGYesNo.longValue() == EuroConstants.CODE_ZNACHENIE_DA?true:false));
	}

	public void setSectionGYesNoBool(boolean sectionGYesNoBool) {
		
		setSectionGYesNo(EuroConstants.CODE_ZNACHENIE_NE);
		
		if(sectionGYesNoBool) {
			setSectionGYesNo(EuroConstants.CODE_ZNACHENIE_DA);
		}
	
	}

	public Integer getSectionDYesNo() {
		return sectionDYesNo;
	}

	public void setSectionDYesNo(Integer sectionDYesNo) {
		this.sectionDYesNo = sectionDYesNo;
	}

	public Integer getSectionEYesNo() {
		return sectionEYesNo;
	}

	public void setSectionEYesNo(Integer sectionEYesNo) {
		this.sectionEYesNo = sectionEYesNo;
	}

	public Integer getSectionZYesNo() {
		return sectionZYesNo;
	}

	public void setSectionZYesNo(Integer sectionZYesNo) {
		this.sectionZYesNo = sectionZYesNo;
	}
	
	public boolean isSectionZYesNoBool() {
		return (sectionZYesNo==null?false:(sectionZYesNo.longValue() == EuroConstants.CODE_ZNACHENIE_DA?true:false));
	}

	public void setSectionZYesNoBool(boolean sectionZYesNoBool) {
		
		setSectionZYesNo(EuroConstants.CODE_ZNACHENIE_NE);
		
		if(sectionZYesNoBool) {
			setSectionZYesNo(EuroConstants.CODE_ZNACHENIE_DA);
		}
	
	}
	
	
	public String getSectionDText() {
		return sectionDText;
	}

	public void setSectionDText(String sectionDText) {
		this.sectionDText = sectionDText;
	}

	public String getSectionEText() {
		return sectionEText;
	}

	public void setSectionEText(String sectionEText) {
		this.sectionEText = sectionEText;
	}

	public String getSectionZText() {
		return sectionZText;
	}

	public void setSectionZText(String sectionZText) {
		this.sectionZText = sectionZText;
	}

	public Integer getVidAct() {
		return vidAct;
	}

	public void setVidAct(Integer vidAct) {
		this.vidAct = vidAct;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}

	public Integer getGodina() {
		return godina;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
	}

	public String getRnFull() {
		return rnFull;
	}

	public void setRnFull(String rnFull) {
		this.rnFull = rnFull;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getCelex() {
		return celex;
	}

	public void setCelex(String celex) {
		this.celex = celex;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_ACT;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ArrayList<EuroActNewConn> getSectionAB2() {
		return sectionAB2;
	}

	public void setSectionAB2(ArrayList<EuroActNewConn> sectionAB2) {
		this.sectionAB2 = sectionAB2;
	}

	public ArrayList<EuroActNewConn> getSectionAB3() {
		return sectionAB3;
	}

	public void setSectionAB3(ArrayList<EuroActNewConn> sectionAB3) {
		this.sectionAB3 = sectionAB3;
	}

	public ArrayList<EuroActNewConn> getSectionAB4i() {
		return sectionAB4i;
	}

	public void setSectionAB4i(ArrayList<EuroActNewConn> sectionAB4i) {
		this.sectionAB4i = sectionAB4i;
	}

	public ArrayList<EuroActNewConn> getSectionAB4d() {
		return sectionAB4d;
	}

	public void setSectionAB4d(ArrayList<EuroActNewConn> sectionAB4d) {
		this.sectionAB4d = sectionAB4d;
	}

	public ArrayList<EuroActNewSectionE> getSectionE() {
		return sectionE;
	}

	public void setSectionE(ArrayList<EuroActNewSectionE> sectionE) {
		this.sectionE = sectionE;
	}

	public ArrayList<EuroActNewSectionZ> getSectionZ() {
		return sectionZ;
	}

	public void setSectionZ(ArrayList<EuroActNewSectionZ> sectionZ) {
		this.sectionZ = sectionZ;
	}

	public ArrayList<EuroActNewConn> getSectionG() {
		return sectionG;
	}

	public void setSectionG(ArrayList<EuroActNewConn> sectionG) {
		this.sectionG = sectionG;
	}

	public String getNoteAB4D() {
		return noteAB4D;
	}

	public void setNoteAB4D(String noteAB4D) {
		this.noteAB4D = noteAB4D;
	}

	public String getNoteAB4I() {
		return noteAB4I;
	}

	public void setNoteAB4I(String noteAB4I) {
		this.noteAB4I = noteAB4I;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getComVid() {
		return comVid;
	}

	public void setComVid(Integer comVid) {
		this.comVid = comVid;
	}

	public Integer getComYear() {
		return comYear;
	}

	public void setComYear(Integer comYear) {
		this.comYear = comYear;
	}

	public Integer getComNumber() {
		return comNumber;
	}

	public void setComNumber(Integer comNumber) {
		this.comNumber = comNumber;
	}
	
	public String getSectionDUrl() {
		return sectionDUrl;
	}

	public void setSectionDUrl(String sectionDUrl) {
		this.sectionDUrl = sectionDUrl;
	}

	public String getNoteG() {
		return noteG;
	}

	public void setNoteG(String noteG) {
		this.noteG = noteG;
	}
	
	

}
