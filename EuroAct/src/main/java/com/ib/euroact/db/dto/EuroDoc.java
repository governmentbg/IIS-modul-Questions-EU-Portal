package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.TrackableEntity;

/**
 * Това е клас, описващ обекта акт на ЕС
 * 
 */

@Entity
@Table(name = "EURO_ACT")
public class EuroDoc extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3353059055597400363L;

	@SequenceGenerator(name = "EuroDoc", sequenceName = "SEQ_EURO_ACT", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDoc")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	// *** АТРИБУТИ НА ОБЕКТА ***

	/** Сигнатура - серия */
	@Column(name = "SIGN_SERIA")
	private Integer signSeria;
	@Transient
	private String signSeriaText;

	/** Сигнатура - номер */
	@Column(name = "SIGN_NOMER")
	private Integer signNomer;

	/** Сигнатура - година */
	@Column(name = "SIGN_GODINA")
	private Integer signGodina;

	/** Сигнатура - обединен (цял номер) */
	@Column(name = "SIGN")
	private String sign;

	/** Дата на документа */
	@Column(name = "DAT_DOC")
	private Date datDoc;

	/** Дата на изпращане */
	@Column(name = "DAT_IZPR")
	private Date datIzpr;

	/** Дата на получаване */
	@Column(name = "DAT_POLUCH")
	private Date datPoluch;

	/** Получен от */
	@Column(name = "POLUCHEN_OT")
	private Integer poluchenOt;

	/** Автор */
	@Column(name = "AVTOR")
	private Integer avtor;

	/** Служител (отговарящ) */
	@Column(name = "SLUJ")
	private Integer sluj;

	/** Вид досие */
	@Column(name = "VID_DOSIE")
	private Integer vidDosie;

	/** Заглавие - български */
	@Column(name = "ZAGL_BG")
	private String zaglBg;

	/** Заглавие - английски */
	@Column(name = "ZAGL_EN")
	private String zaglEn;

	/** УИД на документа */
	@Column(name = "UNID")
	private String uid;

	/** Език на документа */
	@Column(name = "LANG")
	private Integer lang;

	/** Включен в Годишната работна програма НС - da/ne */
	@Column(name = "NSPROG")
	private Integer nsProg;
	/** Включен в Годишната работна програма НС - дата */
	@Column(name = "NSPROG_GODINA")
	private Integer nsProgGodina;
	/** Включен в Годишната работна програма НС - номер */
	@Column(name = "NSPROG_NOMER")
	private Integer nsProgNomer;

	/** Включен в Годишната работна програма MС */
	@Column(name = "MSPROG")
	private Integer msProg;
	/** Включен в Годишната работна програма МС - дата */
	@Column(name = "MSPROG_GODINA")
	private Integer msProgGodina;
	/** Включен в Годишната работна програма МС - номер */
	@Column(name = "MSPROG_NOMER")
	private Integer msProgNomer;

	/** Статус */
	@Column(name = "STATUS")
	private Integer status;
	@Column(name = "STATUS_DATE")
	private Date statusDate;

	/** Етап на приемане на проекта в Европейския парламент */
	@Column(name = "ETAPEP")
	private String etapEP;

	/** Етап на приемане на проекта в Съвета на ЕС */
	@Column(name = "ETAPES")
	private String etapES;

	/** Предстоящи събития */
	@Column(name = "PREDSTOIASTI")
	private String predstoiasti;

	/** Приемане на акта от европейските институции - наименование */
	@Column(name = "PRIEMANE_IME")
	private String priemaneIme;

	/** Приемане на акта от европейските институции - номер CELEX */
	@Column(name = "PRIEMANE_CELEX")
	private String priemaneCelex;

	/**
	 * Приемане на акта от европейските институции - обнародван в Официален вестник
	 * брой
	 */
	@Column(name = "PRIEMANE_BROI")
	private String priemaneBroi;

	/**
	 * Приемане на акта от европейските институции - обнародван в Официален вестник
	 * дата
	 */
	@Column(name = "PRIEMANE_DATE")
	private Date priemaneDate;

	/** Приемане на акта от европейските институции - URL ЕurLex */
	@Column(name = "PRIEMANE_URL")
	private String priemaneUrl;

	
	@Column(name = "ZABELEJKA")
	private String comment;
	

    /** Процедури и срокове */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocProcedure> procedures = new ArrayList<EuroDocProcedure>();
    
    /** Междуинстуционално досие 1:М */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocDosie> dosieta = new ArrayList<EuroDocDosie>();
    
    /** Тематика НС 1:М */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocTemaNS> temaNS = new ArrayList<EuroDocTemaNS>();
    
    /** Тематика EK 1:М */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocTemaEK> temaEK = new ArrayList<EuroDocTemaEK>();
    
    /** URL връзки към документи */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocLink> links = new ArrayList<EuroDocLink>();
	
	/** Езици*/
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocLang> langs = new ArrayList<EuroDocLang>();
    
//    /** Връзки към документи (актове)*/
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
//	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
	@Transient
    private List<EuroDocVraz> vraz = new ArrayList<EuroDocVraz>();
    
//    /** Комисии , в които е разпределен акта*/
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
//	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
	@Transient
    private List<EuroDocKomisia> komisii = new ArrayList<EuroDocKomisia>();
    
    /** Статуси*/
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_ACT", referencedColumnName = "ID", nullable = false)
    private List<EuroDocStatus> statusi = new ArrayList<EuroDocStatus>();

	@Column(name = "BGFILES")
	private Integer bgFiles;
	@Column(name = "ENFILES")
	private Integer enFiles;

	@Column(name = "IPEX_STATUS")
	private Integer ipexStatus;
	@Column(name = "IPEX_SPISSUES")
	private Integer ipexSpIssues;
	@Column(name = "IPEX_IMPINFO")
	private Integer ipexImpInfo;
	@Column(name = "IPEX_REASONEDOP")
	private Integer ipexReasonedOp;

	// pole dostupno za saita
	@Column(name = "DOST")
	private Integer dost;

	/** Конструктор на обекта */
	public EuroDoc() {

	}

	public Integer getSignSeria() {
		return signSeria;
	}

	public void setSignSeria(Integer signSeria) {
		this.signSeria = signSeria;
	}

	public Integer getSignNomer() {
		return signNomer;
	}

	public void setSignNomer(Integer signNomer) {
		this.signNomer = signNomer;
	}

	public Integer getSignGodina() {
		return signGodina;
	}

	public void setSignGodina(Integer signGodina) {
		this.signGodina = signGodina;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Date getDatDoc() {
		return datDoc;
	}

	public void setDatDoc(Date datDoc) {
		this.datDoc = datDoc;
	}

	public Date getDatIzpr() {
		return datIzpr;
	}

	public void setDatIzpr(Date datIzpr) {
		this.datIzpr = datIzpr;
	}

	public Date getDatPoluch() {
		return datPoluch;
	}

	public void setDatPoluch(Date datPoluch) {
		this.datPoluch = datPoluch;
	}

	public Integer getPoluchenOt() {
		return poluchenOt;
	}

	public void setPoluchenOt(Integer poluchenOt) {
		this.poluchenOt = poluchenOt;
	}

	public Integer getAvtor() {
		return avtor;
	}

	public void setAvtor(Integer avtor) {
		this.avtor = avtor;
	}

	public Integer getSluj() {
		return sluj;
	}

	public void setSluj(Integer sluj) {
		this.sluj = sluj;
	}

	public Integer getVidDosie() {
		return vidDosie;
	}

	public void setVidDosie(Integer vidDosie) {
		this.vidDosie = vidDosie;
	}

	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public Integer getNsProg() {
		return nsProg;
	}

	public void setNsProg(Integer nsProg) {
		this.nsProg = nsProg;
	}

	public Integer getNsProgGodina() {
		return nsProgGodina;
	}

	public void setNsProgGodina(Integer nsProgGodina) {
		this.nsProgGodina = nsProgGodina;
	}

	public Integer getNsProgNomer() {
		return nsProgNomer;
	}

	public void setNsProgNomer(Integer nsProgNomer) {
		this.nsProgNomer = nsProgNomer;
	}

	public Integer getMsProg() {
		return msProg;
	}

	public void setMsProg(Integer msProg) {
		this.msProg = msProg;
	}

	public Integer getMsProgGodina() {
		return msProgGodina;
	}

	public void setMsProgGodina(Integer msProgGodina) {
		this.msProgGodina = msProgGodina;
	}

	public Integer getMsProgNomer() {
		return msProgNomer;
	}

	public void setMsProgNomer(Integer msProgNomer) {
		this.msProgNomer = msProgNomer;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getEtapEP() {
		return etapEP;
	}

	public void setEtapEP(String etapEP) {
		this.etapEP = etapEP;
	}

	public String getEtapES() {
		return etapES;
	}

	public void setEtapES(String etapES) {
		this.etapES = etapES;
	}

	public String getPredstoiasti() {
		return predstoiasti;
	}

	public void setPredstoiasti(String predstoiasti) {
		this.predstoiasti = predstoiasti;
	}

	public String getPriemaneIme() {
		return priemaneIme;
	}

	public void setPriemaneIme(String priemaneIme) {
		this.priemaneIme = priemaneIme;
	}

	public String getPriemaneCelex() {
		return priemaneCelex;
	}

	public void setPriemaneCelex(String priemaneCelex) {
		this.priemaneCelex = priemaneCelex;
	}

	public String getPriemaneBroi() {
		return priemaneBroi;
	}

	public void setPriemaneBroi(String priemaneBroi) {
		this.priemaneBroi = priemaneBroi;
	}

	public Date getPriemaneDate() {
		return priemaneDate;
	}

	public void setPriemaneDate(Date priemaneDate) {
		this.priemaneDate = priemaneDate;
	}

	public String getPriemaneUrl() {
		return priemaneUrl;
	}

	public void setPriemaneUrl(String priemaneUrl) {
		this.priemaneUrl = priemaneUrl;
	}

	
	public Integer getBgFiles() {
		return bgFiles;
	}

	public void setBgFiles(Integer bgFiles) {
		this.bgFiles = bgFiles;
	}

	public Integer getEnFiles() {
		return enFiles;
	}

	public void setEnFiles(Integer enFiles) {
		this.enFiles = enFiles;
	}

	public Integer getIpexStatus() {
		return ipexStatus;
	}

	public void setIpexStatus(Integer ipexStatus) {
		this.ipexStatus = ipexStatus;
	}

	public Integer getIpexSpIssues() {
		return ipexSpIssues;
	}

	public void setIpexSpIssues(Integer ipexSpIssues) {
		this.ipexSpIssues = ipexSpIssues;
	}

	public Integer getIpexImpInfo() {
		return ipexImpInfo;
	}

	public void setIpexImpInfo(Integer ipexImpInfo) {
		this.ipexImpInfo = ipexImpInfo;
	}

	public Integer getIpexReasonedOp() {
		return ipexReasonedOp;
	}

	public void setIpexReasonedOp(Integer ipexReasonedOp) {
		this.ipexReasonedOp = ipexReasonedOp;
	}

	public Integer getDost() {
		return dost;
	}

	public void setDost(Integer dost) {
		this.dost = dost;
	}

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_DOC;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<EuroDocProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<EuroDocProcedure> procedures) {
		this.procedures = procedures;
	}

	public List<EuroDocDosie> getDosieta() {
		return dosieta;
	}

	public void setDosieta(List<EuroDocDosie> dosieta) {
		this.dosieta = dosieta;
	}

	public List<EuroDocTemaNS> getTemaNS() {
		return temaNS;
	}

	public void setTemaNS(List<EuroDocTemaNS> temaNS) {
		this.temaNS = temaNS;
	}

	public List<EuroDocTemaEK> getTemaEK() {
		return temaEK;
	}

	public void setTemaEK(List<EuroDocTemaEK> temaEK) {
		this.temaEK = temaEK;
	}

	public List<EuroDocLink> getLinks() {
		return links;
	}

	public void setLinks(List<EuroDocLink> links) {
		this.links = links;
	}

	public List<EuroDocVraz> getVraz() {
		return vraz;
	}

	public void setVraz(List<EuroDocVraz> vraz) {
		this.vraz = vraz;
	}

	public List<EuroDocKomisia> getKomisii() {
		return komisii;
	}

	public void setKomisii(List<EuroDocKomisia> komisii) {
		this.komisii = komisii;
	}

	public List<EuroDocLang> getLangs() {
		return langs;
	}

	public void setLangs(List<EuroDocLang> langs) {
		this.langs = langs;
	}

	public List<EuroDocStatus> getStatusi() {
		return statusi;
	}

	public void setStatusi(List<EuroDocStatus> statusi) {
		this.statusi = statusi;
	}

	public String getZaglBg() {
		return zaglBg;
	}

	public void setZaglBg(String zaglBg) {
		this.zaglBg = zaglBg;
	}

	public String getZaglEn() {
		return zaglEn;
	}

	public void setZaglEn(String zaglEn) {
		this.zaglEn = zaglEn;
	}
	
	public String getZagl() {
		if (zaglBg != null && !zaglBg.trim().isEmpty()) {
			return zaglBg;
		}else {
			return zaglEn;
		}	
	}

	public String getSignSeriaText() {
		return signSeriaText;
	}

	public void setSignSeriaText(String signSeriaText) {
		this.signSeriaText = signSeriaText;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	

}
