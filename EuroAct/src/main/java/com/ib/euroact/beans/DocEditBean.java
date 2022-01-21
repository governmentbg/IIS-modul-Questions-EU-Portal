package com.ib.euroact.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroDocDAO;
import com.ib.euroact.db.dao.FilesDAO;
import com.ib.euroact.db.dto.EuroDoc;
import com.ib.euroact.db.dto.EuroDocDosie;
import com.ib.euroact.db.dto.EuroDocLang;
import com.ib.euroact.db.dto.EuroDocLink;
import com.ib.euroact.db.dto.EuroDocProcedure;
import com.ib.euroact.db.dto.EuroDocProcedureStan;
import com.ib.euroact.db.dto.EuroDocStatus;
import com.ib.euroact.db.dto.EuroDocTemaEK;
import com.ib.euroact.db.dto.EuroDocTemaNS;
import com.ib.euroact.db.dto.EuroDocVraz;
import com.ib.euroact.db.dto.Files;
import com.ib.euroact.export.EuroReportCardExport;
import com.ib.euroact.parsers.ActParser;
import com.ib.euroact.parsers.ZipUtils;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.system.IndexUIbean;
import com.ib.indexui.utils.JSFUtils;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.BaseException;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;

import eu2.TransmissionRequestType;

/**
 * Екран 'Добавяне на нов пакет/документ'
 * @author n.kanev
 *
 */
@Named("docEdit")
@ViewScoped
public class DocEditBean extends IndexUIbean {
	
	private static final long serialVersionUID = 151016670598809145L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DocEditBean.class);
	private static final String SIGNATURA_REGEX = "^(\\w+)\\((\\d+)\\) (\\d+)$";
	
	private EuroDoc euroDoc;				// основният обект, с който се работи на екрана
	private boolean documentIsLoaded;		// ако сме отворили съществуващ документ за редакция, стойността е true; ако създаваме нов, значи е false;
	private transient EuroDocDAO docDao;
	private transient FilesDAO filesDao;
	private List<SystemClassif> procOptions;
	private transient Object procSeria;
	private boolean publish;
	private boolean signaturaChecked;
	private boolean nsProgChecked;
	private boolean msProgChecked;
	private EuroDocStatus lastStatus;
	
	private List<SystemClassif> signaturaOptions;
	private transient Object signaturaSeriaObj;
	// Сигнатурата се пипа в тези допълнителни полета, защото иначе стават грешки 
	// при зареждането на документ, промяната, отварянето на друг и връщането към предишния 
	private Integer signSeria;
	private Integer signGodina;
	private Integer signNomer;
	private String sign;
	
	private EuroDocDosie tempDosie;		// въведените стойности в полетата за ново досие
	
	private List<Integer> langList = new ArrayList<>();		// списъкът с избраните нови езици
	private List<SystemClassif> langClassifs;				// избраните езици като обекти класификация
	
	private EuroDocLink tempEuroLink;	// въведените стойности в полетата за нов линк
	
	private List<SystemClassif> proceduresOptions;			// класификация с видовете процедури
	private Map<Integer, EuroDocProcedure> proceduresMap;	// мап, който съдържа процедурите на документа, ключ е кодът на процедурата
	private Map<Integer, Boolean> proceduresChecked;		// мап, в който има за ключ код на процедура и стойност true/false, ако чекбоксът е чекнат
	private Map<Integer, String> tempStanNomer;				// инпут полето за номер на становище, ключ е кодът на процедурата
	private Map<Integer, LocalDate> tempStanDate;			// инпут полето за дата на становище, ключ е кодът на процедурата
	
	private List<Files> filesList;		// списъкът с качените файлове
	private List<Files> filesXList;		// списъкът с изтритите файлове	
	
	private List<Integer> temaNsList;				// избрани тематики за НС
	private List<SystemClassif> temaNsClassifs;		// избрани тематики като обект класификация
	
	private List<Integer> temaEkList;				// избрани тематики за ЕК
	private List<SystemClassif> temaEkClassifs;		// избрани тематики като обект класификация
	
	// Връзки с други документи на ЕС
	private boolean signaturaVrazChecked;			// чекбоксът
	private String signaturaVraz;					// полето за свободно писане
	private transient Object signaturaVrazSeria;	// падащо меню за серия
	private Integer signaturaVrazGodina;			// година
	private Integer signaturaVrazNomer;				// номер
	
	@PostConstruct
	public void initData() {
			
		try {
			this.signaturaOptions = new ArrayList<>();
			this.signaturaOptions.addAll(getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_SIGNATURA_SERIA, new Date(), this.getCurrentLang()));
			this.procOptions = new ArrayList<>();
			this.procOptions.addAll(getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_PROC_CODE, new Date(), this.getCurrentLang()));
			this.proceduresOptions = new ArrayList<>();
			this.proceduresOptions.addAll(getSystemData().getSysClassification(EuroConstants.CODE_SYSCLASS_PROCEDURE_BG, new Date(), this.getCurrentLang()));
		} catch (DbErrorException e) {
			LOGGER.error(getMessageResourceString(beanMessages, "general.errorClassif"), e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, getMessageResourceString(beanMessages, "general.errorClassif"));
			this.scrollToMessages();
		}	
		
		this.initFields();
		this.docDao = new EuroDocDAO(getUserData());
		this.filesDao = new FilesDAO(getUserData());
		
		String paramId = JSFUtils.getRequestParameter("idObj");
		
		if(paramId != null) {
			try {
				Integer id = Integer.parseInt(paramId);
				this.getEuroDocById(id);
			}
			catch(NumberFormatException e) {
				LOGGER.error("Подаден е грешен номер на обекта!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Подаден е грешен номер на обекта!");
				this.scrollToMessages();
				this.euroDoc = new EuroDoc();
			}
		}
	}
	
	private void initFields() {
		this.lastStatus = new EuroDocStatus();
		this.tempDosie = new EuroDocDosie();
		this.tempEuroLink = new EuroDocLink();
		this.euroDoc = new EuroDoc();
		this.euroDoc.setDatPoluch(new Date());
		this.euroDoc.setStatusDate(DateUtils.startDate(new Date()));
		this.documentIsLoaded = false;
		this.procSeria = null;
		this.publish = true;
		this.signaturaChecked = false;
		this.nsProgChecked = false;
		this.msProgChecked = false;
		this.tempDosie = new EuroDocDosie();
		this.langList = new ArrayList<>();
		this.langClassifs = new ArrayList<>();
		this.tempEuroLink = new EuroDocLink();
		this.filesList = new ArrayList<>();
		this.filesXList = new ArrayList<>();
		this.temaNsList = new ArrayList<>();
		this.temaNsClassifs = new ArrayList<>();
		this.temaEkList = new ArrayList<>();
		this.temaEkClassifs = new ArrayList<>();
		this.proceduresMap = new HashMap<>();
		this.tempStanNomer = new HashMap<>();
		this.tempStanDate = new HashMap<>();
		this.proceduresChecked = new HashMap<>();
		for(SystemClassif p : this.proceduresOptions) {
			this.proceduresChecked.put(p.getCode(), false);
		}
	}
	
	/**
	 * Намира в базата EuroDoc обекта по извлеченото ID от параметъра.
	 * @param euroDocId
	 */
	private void getEuroDocById(Integer euroDocId)	{
		EuroDoc loadedDoc = null;
		try {
			loadedDoc = this.docDao.findById(euroDocId);
		} catch (DbErrorException e) {
			LOGGER.error(getMessageResourceString(UI_beanMessages, "general.errDataBaseMsg"), e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на документа!");
			this.scrollToMessages();
		}
		catch (NullPointerException e) {
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Документът не е намерен!");
			scrollToMessages();
		}
		this.loadEuroDoc(loadedDoc, true);
	}
	
	/**
	 * Зарежда EuroDоc обекта на екрана
	 * @param loaded
	 * @param loadFiles дали да зареди и файловете от базата. Ако се зарежда от Zip, няма смисъл да е true
	 */
	private void loadEuroDoc(EuroDoc loaded, boolean loadFiles) {
		
		this.euroDoc = loaded;
		
		if(loaded == null) {
			return;
		}
		
		this.documentIsLoaded = true;
		
		// сигнатура
		// пази се в отделни променливи, за да не стане объркване 
		if(loaded.getSignSeria()!=null) {
			signaturaSeriaObj = this.signaturaOptions.stream().filter(o -> o.getCode() == loaded.getSignSeria()).findFirst().orElse(null);
			signSeria = loaded.getSignSeria();
			signGodina = loaded.getSignGodina();
			signNomer = loaded.getSignNomer();
			sign = loaded.getSign();
		} else {
			signaturaChecked = true;
			sign = loaded.getSign();
		}
		
		// Включен в Годишната работна програма на НС
		this.nsProgChecked = loaded.getNsProg() != null && loaded.getNsProg().equals(EuroConstants.CODE_ZNACHENIE_DA);
		// Включен в Годишната работна програма на МС
		this.msProgChecked = loaded.getMsProg() != null && loaded.getMsProg().equals(EuroConstants.CODE_ZNACHENIE_DA);
		
		// Публикуване на сайта
		if(!publish) { //ако е казано да се публикува не го променяме 07,05,21
			this.publish = loaded.getDost() != null && loaded.getDost().equals(EuroConstants.CODE_ZNACHENIE_DA);
		}
		// Статуси
		this.euroDoc.getStatusi().sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
		this.lastStatus.setStatus(this.euroDoc.getStatus());
		this.lastStatus.setDatStatus(this.euroDoc.getStatusDate());
		
		// Работа по процедури
		this.proceduresMap.clear();
		this.proceduresChecked.clear();
		this.tempStanDate.clear();
		this.tempStanNomer.clear();
		for(EuroDocProcedure p : loaded.getProcedures()) {
			this.proceduresMap.put(p.getProcedure(), p);
			this.proceduresChecked.put(p.getProcedure(), true);
		}
		
		// Език на документа 
		if(loaded.getLangs() != null) {
			this.langList = loaded.getLangs().stream().map(EuroDocLang::getLang).collect(Collectors.toList());
			try {
				this.langClassifs = 
						getSystemData()
						.getSysClassification(EuroConstants.CODE_SYSCLASS_EZIK, new Date(), this.getCurrentLang())
						.stream()
						.filter(e -> this.langList.contains(e.getCode()))
						.collect(Collectors.toList());
			} catch (DbErrorException e) {
				JSFUtils.addMessage("langsInput", FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на езика на документа!");
				scrollToMessages();
				LOGGER.error(getMessageResourceString(beanMessages, "general.errorClassif"), e);
			}
		}
		
		// Тематична област на НС
		if(loaded.getTemaNS() != null) {
			this.temaNsList = loaded.getTemaNS().stream().map(EuroDocTemaNS::getTema).collect(Collectors.toList());
			try {
				this.temaNsClassifs = 
						getSystemData()
						.getSysClassification(EuroConstants.CODE_SYSCLASS_TEMATIKA_NS, new Date(), this.getCurrentLang())
						.stream()
						.filter(t -> this.temaNsList.contains(t.getCode()))
						.collect(Collectors.toList());
			} catch (DbErrorException e) {
				JSFUtils.addMessage("temaNsInput", FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на Тематична област на НС!");
				scrollToMessages();
				LOGGER.error(getMessageResourceString(beanMessages, "general.errorClassif"), e);
			}
		}
		
		// Тематична област на ЕК
		if(loaded.getTemaEK() != null) {
			this.temaEkList = loaded.getTemaEK().stream().map(EuroDocTemaEK::getTema).collect(Collectors.toList());
			try {
				this.temaEkClassifs = 
						getSystemData()
						.getSysClassification(EuroConstants.CODE_SYSCLASS_TEMATIKA_EK, new Date(), this.getCurrentLang())
						.stream()
						.filter(t -> this.temaEkList.contains(t.getCode()))
						.collect(Collectors.toList());
			} catch (DbErrorException e) {
				JSFUtils.addMessage("temaEkInput", FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на Тематична област на ЕК!");
				scrollToMessages();
				LOGGER.error(getMessageResourceString(beanMessages, "general.errorClassif"), e);
			}
		}
		
		if(loadFiles) {
			this.loadCurrentFiles();
		}
	}
	
	/**
	 * Зарежда файловете към отворения в момента документ.
	 */
	private void loadCurrentFiles() {
		// Файлове
		if(this.euroDoc != null) {
			try {
				this.filesList = this.filesDao.selectByFileObject(this.euroDoc.getId(), this.euroDoc.getCodeMainObject());
				this.filesXList = new ArrayList<>();
			} catch (DbErrorException e) {
				LOGGER.error("Грешка при зареждането на файлове!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на файлове!");
				scrollToMessages();
			}
		}
	}
	
	/**
	 * Натиснат е бутонът 'Запис'
	 */
	public void actionSave() {
		// валидаиця
		if(!validateOnSave()) {
			this.scrollToMessages();
			return;
		}
		
		// Публикуване на сайта
		this.euroDoc.setDost(this.publish ? EuroConstants.CODE_ZNACHENIE_DA : EuroConstants.CODE_ZNACHENIE_NE);
		
		// Сигнатура
		if(!signaturaChecked) {
			euroDoc.setSignSeria(signSeria);
			euroDoc.setSignGodina(signGodina);
			euroDoc.setSignNomer(signNomer);
			SystemClassif selected = signaturaOptions.stream().filter(o -> o.getCode() == euroDoc.getSignSeria()).findFirst().orElse(null);
			euroDoc.setSign(String.format("%s(%d) %d", selected.getTekst(), euroDoc.getSignGodina(), euroDoc.getSignNomer()));
		} else {
			euroDoc.setSign(this.sign.trim());
		}
		
		// Статуси
		boolean add = false;
		
		if(this.lastStatus.getStatus() == null && this.euroDoc.getStatus() != null 				// било е празно и сме въвели нещо
				|| this.lastStatus.getStatus() != null && this.euroDoc.getStatus() == null) {	// било е попълнено и сме го оставили празно
			add = true;
		}
		if(this.lastStatus.getStatus() != null && this.euroDoc.getStatus() != null 				// било е попълнено и сме попълнили нещо друго
				&& !this.euroDoc.getStatus().equals(this.lastStatus.getStatus())) {
			add = true;
		}
		
		if(add) {
			EuroDocStatus newStatus = new EuroDocStatus();
			newStatus.setStatus(this.lastStatus.getStatus());
			newStatus.setDatStatus(this.lastStatus.getDatStatus());
			
			this.euroDoc.getStatusi().add(newStatus);
			
			this.lastStatus = new EuroDocStatus();
			this.lastStatus.setStatus(this.euroDoc.getStatus());
			this.lastStatus.setDatStatus(this.euroDoc.getStatusDate());
		}
		
		// Език на документа		
		// трябва да се обиколи колекцията и да се провери кое го има/няма в списъка this.langList
		this.euroDoc.getLangs().removeIf(l -> !this.langList.contains(l.getLang())); // махат се изтритите в екрана
		
		for(Integer l : this.langList) { // добавят се добавените от екрана
			if(this.euroDoc.getLangs().stream().noneMatch(lang -> lang.getLang().equals(l))) {
				EuroDocLang newLang = new EuroDocLang();
				newLang.setLang(l);
				this.euroDoc.getLangs().add(newLang);
			}
		}
		
		// Включен в Годишната работна програма на НС
		this.euroDoc.setNsProg(this.nsProgChecked ? EuroConstants.CODE_ZNACHENIE_DA : EuroConstants.CODE_ZNACHENIE_NE);
		
		// Включен в Годишната работна програма на MС
		this.euroDoc.setMsProg(this.msProgChecked ? EuroConstants.CODE_ZNACHENIE_DA : EuroConstants.CODE_ZNACHENIE_NE);
		
		// Работа по процедури
		this.euroDoc.getProcedures().clear();
		this.proceduresMap.forEach((c, p) -> { 
			if(this.proceduresChecked.get(c) && p.getSrok() != null && !p.getStanovista().isEmpty()) {
				this.euroDoc.getProcedures().add(p);
			}
		});
		
		// Тематична област на НС
		this.euroDoc.getTemaNS().removeIf(t -> !this.temaNsList.contains(t.getTema())); // махат се изтритите в екрана
		for(Integer t : this.temaNsList) { // добавят се добавените от екрана
			if(this.euroDoc.getTemaNS().stream().noneMatch(tema -> tema.getTema().equals(t))) {
				EuroDocTemaNS newTema = new EuroDocTemaNS();
				newTema.setTema(t);
				this.euroDoc.getTemaNS().add(newTema);
			}
		}
		
		// Тематична област на EK
		this.euroDoc.getTemaEK().removeIf(t -> !this.temaEkList.contains(t.getTema())); // махат се изтритите в екрана
		for(Integer t : this.temaEkList) { // добавят се добавените от екрана
			if(this.euroDoc.getTemaEK().stream().noneMatch(tema -> tema.getTema().equals(t))) {
				EuroDocTemaEK newTema = new EuroDocTemaEK();
				newTema.setTema(t);
				this.euroDoc.getTemaEK().add(newTema);
			}
		}
		
		try {
			JPA.getUtil().runInTransaction(() -> {
				
				this.euroDoc = this.docDao.save(this.euroDoc);
				
				// Файлове
				if(!this.filesList.isEmpty()) {
					for(Files f : this.filesList) {
						f.setIdObject(this.euroDoc.getId());
						f.setCodeObject(this.euroDoc.getCodeMainObject()); // трябва да се слага ръчно, ако compFileUploadL.autoSave = false;
						if(f.getId() == null) { // да не се прави запис, ако вече е в базата
							this.filesDao.save(f);
						}
					}
				}
				
				if(!this.filesXList.isEmpty()) {
					for(Files f : this.filesXList) {
						this.filesDao.delete(f);
					}
				}
				
				//this.loadEuroDoc(this.euroDoc, true);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, "Документът е записан успешно.");
			});
		} catch (BaseException e) {
			LOGGER.error("Грешка при запис на документ!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Неуспешен запис!");
			scrollToMessages();
		}	
		
	}
	
	/**
	 * Вика се при натискане на Запис. Валидира задължителните полета.
	 */
	private boolean validateOnSave() {
		boolean valid = true;
		
		// сигнатура
		if(!signaturaChecked) {
			if(signSeria == null) {
				valid = false;
				JSFUtils.addMessage("inputSignaturaSeria", FacesMessage.SEVERITY_ERROR, "Серията е задължителна!");
			}
			if(signGodina == null) {
				valid = false;
				JSFUtils.addMessage("inputSignaturaGodina", FacesMessage.SEVERITY_ERROR, "Годината е задължителна!");
			}
			if(signNomer == null) {
				valid = false;
				JSFUtils.addMessage("inputSignaturaNomer", FacesMessage.SEVERITY_ERROR, "Номерът е задължителен!");
			}
		}
		else {
			if(sign == null || sign.trim().isEmpty()) {
				valid = false;
				JSFUtils.addMessage("inputSignatura", FacesMessage.SEVERITY_ERROR, "Сигнатурата е задължителна!");
			}
		}
		
		// дата на изпращане
		if(euroDoc.getDatIzpr() == null) {
			valid = false;
			JSFUtils.addMessage("inputDataIzpr", FacesMessage.SEVERITY_ERROR, "Дата на изпращане е задължителна!");
		}
		
		return valid;
	}
	
	/**
	 * Проверява дали полето за ръчно написана сигнатура е валидно.
	 * @return
	 */
	private boolean checkSignaturaRegex() {
		Pattern pattern = Pattern.compile(SIGNATURA_REGEX);
		Matcher m = pattern.matcher((String) this.sign.trim());
		if(m.find()) {

			SystemClassif classif = this.signaturaOptions.stream().filter(o -> o.getTekst().equals(m.group(1))).findFirst().orElse(null);
			if(classif == null) {
				return false;
			}
			
			try {
				Integer.parseInt(m.group(2));
				Integer.parseInt(m.group(3));
			}
			catch(NumberFormatException e) {
				return false;
			}

		}
		else {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Натиснат е бутонът 'Нов документ'
	 */
	public void actionNew() {
		this.initFields();
		this.signaturaSeriaObj = null;
		this.signSeria = null;
		this.signGodina = null;
		this.signNomer = null;
		this.sign = null;
	}
	
	/**
	 * Натиснат е бутонът 'Изтриване'
	 */
	public void actionDelete() {

		if(this.euroDoc.getId() != null) {
			try {
				JPA.getUtil().runInTransaction(() -> {
					if(!this.euroDoc.getVraz().isEmpty()) {
						Query query = JPA.getUtil().getEntityManager().createQuery("delete from EuroDocVraz v where v.idAct1 = :id1 or v.idAct2 = :id2 ");
						query.setParameter("id1", this.euroDoc.getId());
						query.setParameter("id2", this.euroDoc.getId());
						query.executeUpdate();
					}
					
					if(this.filesList != null) {
						for(Files f : this.filesList) {
							if (f.getId() != null) {
								this.filesDao.delete(f);
							}
						}
					}
					
					this.docDao.delete(this.euroDoc);
					
					this.actionNew();
				});
			} catch (BaseException e) {
				LOGGER.error("Грешка при изтриване на документ!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при изтриване на документ!");
				scrollToMessages();
			}
		}
		
	}	
	
	/**
	 * Натиснат е бутонът 'Зареждане на ZIP-файл'
	 */
	public void actionLoadZip(FileUploadEvent event)  {
		UploadedFile zip = event.getFile();
		List<Files> unzippedFiles = new ArrayList<>();
		
		if(zip != null) {
			EuroDoc parsedAct = null;
			
			Map<String, byte[]> tmpHash = ZipUtils.unZipIt(zip.getContent());
			
			try {
				for (String element : tmpHash.keySet()) {
					if (element.contains(".xml")) {
						InputStream stream = new ByteArrayInputStream(tmpHash.get(element));
						JAXBContext jaxbContext = JAXBContext.newInstance(TransmissionRequestType.class);
		     			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	
		     			JAXBElement<TransmissionRequestType> root = jaxbUnmarshaller.unmarshal(new StreamSource(stream), TransmissionRequestType.class);
		     			TransmissionRequestType trans = root.getValue();
		                 
						parsedAct = new ActParser().parseAct(trans, (SystemData) getSystemData());
					}
					else {
						Files file = new Files();
						file.setContent(tmpHash.get(element));
						file.setFilename(element);
		                
						unzippedFiles.add(file);
					}
				}
				
				if(parsedAct != null) {
					
					this.loadEuroDoc(parsedAct, true);
					// ако в базата документът вече е записан и има прикачен файл,
					// но сега от зип се зареди същият документ и в него има файл със същото име,
					// изтрива досегашния файл и слага този от зипа
					List<String> unzippedFilesNames = unzippedFiles.stream().map(f -> f.getFilename()).collect(Collectors.toList());
					List<Files> containedFiles = this.filesList.stream().filter(f -> unzippedFilesNames.contains(f.getFilename())).collect(Collectors.toList());
					this.filesXList.addAll(containedFiles);
					this.filesList.removeAll(containedFiles);
					this.filesList.addAll(unzippedFiles);
					
					JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, "Файлът е зареден.");
					this.scrollToMessages();
				}
				else {
					this.filesList.addAll(unzippedFiles);
					//JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_INFO, "ZIP файлът не съдържа документ.");
					//this.scrollToMessages();
				}
			}
			catch(BaseException | JAXBException e) {
				LOGGER.error("Грешка при зареждането на ZIP файл!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на ZIP файл!");
				this.scrollToMessages();
			}
			catch(Exception e) {
				LOGGER.error("Грешка при зареждането на ZIP файл!", e);
				JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при зареждането на ZIP файл!");
				this.scrollToMessages();
			}
			
		}

	}
	
	/**
	 * Избрана е опция в падащото меню 'Сигнатура на документа'
	 */
	public void actionSelectSignatura() {
		this.signSeria = this.getSeriaCode(this.signaturaSeriaObj);
		this.actionSearchOnSignaturaBlur();
	}
	
	/**
	 * Вика се, когато се промени стойността в някое от полетата за сигнатура на документа.
	 * Ако намери документ с дадената сигнатура, моментално го зарежда.
	 */
	public void actionSearchOnSignaturaBlur() {
		
		try {
			List<EuroDoc> foundDocs = new ArrayList<EuroDoc>();
			if(signaturaChecked) {
				foundDocs = docDao.filterEuroDoc(sign);
			} else {
				foundDocs = docDao.filterEuroDoc(this.signSeria, this.signGodina, this.signNomer);
			}
			
			if(foundDocs.size() > 0) {			
				EuroDoc found = foundDocs.get(0);	
				loadEuroDoc(found, true);
			} else {
				// ако не се намери документ, се гледа дали се редактира зареден документ или е отворен празен:
				//    ако е зареден документ, да отвори празен с новата сигнатура;
				//    ако се редактира празен, не се прави нищо.
				if(documentIsLoaded) {			
					initFields();
				}
				
			}	
		
		}catch (Exception e) {
			LOGGER.error("Грешка при търсене на акт на ЕС!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());	
		}  finally {
			JPA.getUtil().closeConnection();
		}
	}
	
	public void actionToggleSignatura() {
		if(signaturaChecked) {
			signSeria=null;
			signGodina=null;
			signNomer=null;
		} else {
			sign= null;
		}
	}
	
	/**
	 * Изтрива статус от таблицата с история на статусите.
	 * @param index
	 */
	public void actionDeleteStatus(int index) {
		this.euroDoc.getStatusi().remove(index);
	}
	
	/**
	 * Избрана е опция на падащото меню 'Междуинституционално досие'
	 */
	public void actionSelectProc() {
		this.tempDosie.setSignProc(this.getSeriaCode(this.procSeria));
		this.actionOnDosieBlur();
	}
	
	/**
	 * Когато се пише в полетата за ново досие, на onchange проверява да добави новото автоматично
	 */
	public void actionOnDosieBlur() {
		if(this.tempDosie.getSignGodina() == null || this.tempDosie.getSignNomer() == null || this.tempDosie.getSignProc() == null) {
			return;
		}
		else {
			actionAddDosie();
		}
	}
	
	/**
	 * Натиснат е бутонът 'Добавяне' в секция Междуинституционално досие
	 */
	public void actionAddDosie() {
		if(this.tempDosie.getSignGodina() == null || this.tempDosie.getSignNomer() == null || this.tempDosie.getSignProc() == null) {
			return;
		}
		
		EuroDocDosie dosie = new EuroDocDosie();
		dosie.setSignGodina(this.tempDosie.getSignGodina());
		dosie.setSignNomer(this.tempDosie.getSignNomer());
		dosie.setSignProc(this.tempDosie.getSignProc());
		SystemClassif proc = this.procOptions.stream().filter(o -> o.getCode() == tempDosie.getSignProc()).findFirst().orElse(null);
		if(proc != null) {
			dosie.setSign(String.format("%d/%d(%s)", this.tempDosie.getSignGodina(), this.tempDosie.getSignNomer(), proc.getTekst()));
		}
		
		if(this.euroDoc.getDosieta().stream().noneMatch(d -> d.getSign().equals(dosie.getSign()))) {
			this.euroDoc.getDosieta().add(dosie);
		}
		
		this.tempDosie = new EuroDocDosie();
		this.procSeria = null;
	}
	
	/**
	 * Изтрито е нещо от chips полето в секцията Междуинституционално досие
	 */
	public void actionDeleteDosie() {
		FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        int index = Integer.valueOf(map.get("index"));
        this.euroDoc.getDosieta().remove(index);
	}
	
	/**
	 * Кликнат е чекбоксът 'Включен в Годишната работна програма на НС'
	 */
	public void actionNsProgCheck() {
		if(this.nsProgChecked) {
			this.euroDoc.setNsProg(EuroConstants.CODE_ZNACHENIE_DA);
		}
		else {
			this.euroDoc.setNsProg(EuroConstants.CODE_ZNACHENIE_NE);
			this.euroDoc.setNsProgGodina(null);
			this.euroDoc.setNsProgNomer(null);
		}
	}
	
	/**
	 * Кликнат е чекбоксът 'Включен в Годишната работна програма на МС'
	 */
	public void actionMsProgCheck() {
		if(this.msProgChecked) {
			this.euroDoc.setMsProg(EuroConstants.CODE_ZNACHENIE_DA);
		}
		else {
			this.euroDoc.setMsProg(EuroConstants.CODE_ZNACHENIE_NE);
			this.euroDoc.setMsProgGodina(null);
			this.euroDoc.setMsProgNomer(null);
		}
	}
	
	/**
	 * Кликнат е плюсът за добавяне на ново становище към процедура.
	 * @param procCode кодът на процедурата
	 */
	public void actionAddStanoviste(Integer procCode) {
		
		if(this.proceduresMap.get(procCode).getSrok() == null 
				|| this.tempStanNomer.get(procCode).trim().isEmpty() 
				|| this.tempStanDate.get(procCode) == null) {
			return;
		}
		
		EuroDocProcedure procedure = this.proceduresMap.get(procCode);
		
		Date stanDate = Date.from(this.tempStanDate.get(procCode).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		if(procedure.getStanovista().stream().noneMatch(s -> 
				s.getRnDoc().equals(this.tempStanNomer.get(procCode).trim()) 
				&& s.getStanoviste().equals(stanDate))) {
			EuroDocProcedureStan stanovi6te = new EuroDocProcedureStan();
			stanovi6te.setRnDoc(this.tempStanNomer.get(procCode).trim());
			stanovi6te.setStanoviste(stanDate);
				      
			procedure.getStanovista().add(stanovi6te);
		}
		
		this.tempStanNomer.put(procCode, null);
		this.tempStanDate.put(procCode, null);
	}
	
	/**
	 * Кликнат е чекбоксът до някоя процедура. Инициализира нов обект.
	 * @param procCode кодът на процедурата
	 */
	public void procedureChecked(Integer procCode) {
		Boolean checked = this.proceduresChecked.get(procCode);
		if(checked) {
			if(!this.proceduresMap.containsKey(procCode)) {
				EuroDocProcedure procedure = new EuroDocProcedure();
				procedure.setProcedure(procCode);
				this.proceduresMap.put(procCode, procedure);
			}
		}
	}
	
	/**
	 * Кликната е иконката за изтриване в полето с процедури.
	 */
	public void actionDeleteProcedure() {
		FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        int procCode = Integer.valueOf(map.get("procCode"));
        int stanIndex = Integer.valueOf(map.get("stanIndex"));
        
        this.proceduresMap.get(procCode).getStanovista().remove(stanIndex);
	}
	
	/**
	 * Класификацията се получава като стринг, а тук се взима само code
	 * @param chosenValue обект от тип SystemClassif, който идва от екрана като стринг
	 * @return стйността на code полето на избрания обект
	 */
	private Integer getSeriaCode(Object chosenValue) {
		if(chosenValue == null) {
			return null;
		}
		else {
			String regex = "(code=\\d*)";
			Pattern pattern = Pattern.compile(regex);
			Matcher m = pattern.matcher((String) chosenValue);
			if(m.find()) {
				String code = m.group().split("=")[1];
				return Integer.parseInt(code);
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Натиснат е бутонът 'Добавяне' в секция URL - връзки
	 */
	public void actionAddUrl() {
		if(this.tempEuroLink.getOpis() == null || this.tempEuroLink.getUrl() == null 
				|| this.tempEuroLink.getOpis().trim().isEmpty() || this.tempEuroLink.getUrl().trim().isEmpty()) {
			return;
		}
		
		EuroDocLink link = new EuroDocLink();
		link.setOpis(this.tempEuroLink.getOpis().trim());
		link.setUrl(this.tempEuroLink.getUrl().trim());
		
		this.euroDoc.getLinks().add(link);
		
		this.tempEuroLink = new EuroDocLink();
	}
	
	/**
	 * Натиснат е бутонът за изтриване в таблицата с URL - връзки
	 */
	public void actionDeleteUrl(int index) {
		this.euroDoc.getLinks().remove(index);
	}
	
	/**
	 * Кликнат е чекбоксът при попълване на сигнатура в секция Връзки с други документи на ЕС 
	 */
	public void actionCheckVrazSignatura() {
		if(this.signaturaVrazChecked) {
			this.signaturaVrazGodina = null;
			this.signaturaVrazNomer = null;
			this.signaturaVrazSeria = null;
		}
		else {
			this.signaturaVraz = null;
		}
	}
	
	/**
	 * Натиснат е бутонът 'Добавяне' в секция Връзки с други документи на ЕС 
	 */
	public void actionAddVraz() {
		
		if(!this.signaturaVrazChecked && (this.signaturaVrazGodina == null || this.signaturaVrazNomer == null || this.signaturaVrazSeria == null)) {
			return;
		}
		
		if(this.signaturaVrazChecked && (this.signaturaVraz == null || this.signaturaVraz.trim().isEmpty())) {
			return;
		}
					
		List<EuroDoc> foundDocs = new ArrayList<>();
		
		if(signaturaVrazChecked) {
			foundDocs = docDao.filterEuroDoc(signaturaVraz.trim());
		} else {
			Integer seria = this.getSeriaCode(this.signaturaVrazSeria);
			foundDocs = docDao.filterEuroDoc(seria, signaturaVrazGodina, signaturaVrazNomer);
		}
		
		
		if(foundDocs.size() > 0) {
			
			EuroDoc found = foundDocs.get(0);
			EuroDocVraz vrazka = new EuroDocVraz();
			
			vrazka.setIdAct1(this.euroDoc.getId()); // това нaрочно е така, слага се в EuroDocDAO.save
			vrazka.setIdAct2(found.getId());
			vrazka.setSignVraz(found.getSign());
			vrazka.setNameVraz(found.getZagl());
			
			// проверка да не се слагат дубликати
			boolean duplicate = false;
			if(this.euroDoc.getId() == null) {
				// ако id е празно, значи документът още не е записан в базата, 
				// следователно единствените връзки са тези, добавени от настоящия екран;
				// единствено се проверява полето getIdAct2, понеже getIdAct1 е null
				duplicate = this.euroDoc.getVraz().stream().anyMatch(v -> v.getIdAct2().equals(found.getId()));
			}
			else {
				// id не е празно, значи вече е записан в базата,
				// следователно връзките са както добавени от настоящия екран, така и взети от базата;
				// няма опасност getIdAct1 или getIdAct2 да е null, значи се проверяват и двете
				duplicate = this.euroDoc.getVraz().stream().anyMatch(v -> v.getIdAct1().equals(found.getId()) || v.getIdAct2().equals(found.getId()));
			}
			
			if(!duplicate) {
				this.euroDoc.getVraz().add(vrazka);
			}
			
			this.signaturaVraz = null;
			this.signaturaVrazGodina = null;
			this.signaturaVrazNomer = null;
			this.signaturaVrazSeria = null;
		}
		else {
			JSFUtils.addMessage("inputSignaturaVraz", FacesMessage.SEVERITY_INFO, "Не е намерен документ с тази сигнатура");
			scrollToMessages();
		}
	}
	
	/**
	 * Кликната е лупата за разглеждане в chips полето в секцията Връзки с други документи на ЕС
	 */
	public String actionViewVraz(int index) {
        EuroDocVraz vraz = this.euroDoc.getVraz().get(index);
        
        Integer id;
        if(vraz.getIdAct1() == null || vraz.getIdAct1().equals(this.euroDoc.getId())) {
        	id = vraz.getIdAct2();
        }
        else {
        	id = vraz.getIdAct1();
        }
        
		return "docEditView.xhtml?faces-redirect=true&idObj=" + id;

	}
	
	/**
	 * Изтрито е нещо от chips полето в секцията Връзки с други документи на ЕС
	 */
	public void actionDeleteVraz() {
		FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        int index = Integer.valueOf(map.get("index"));
        this.euroDoc.getVraz().remove(index);
	}
	
	/**
	 * Създава експорт и изпраща файла обратно.
	 * @param code 1 - HTML, 2 - DOC
	 */
	public void export(int code) {
		try {
			EuroReportCardExport docExport = new EuroReportCardExport();
			docExport.ReportEUDocExp(this.euroDoc, code);
		}
		catch (Exception e) {
			LOGGER.error("Грешка при експорт на документа!", e);
			JSFUtils.addGlobalMessage(FacesMessage.SEVERITY_ERROR, "Грешка при експорт на документа!");
			this.scrollToMessages();
		}
	}

	/**
	 * Получава сигнатура във вида "COM(2001) 123", парсва я и връща масив с елементи:
	 * [0] - кода на сигнатурата
	 * [1] - година
	 * [2] - номер
	 * @return
	 */
	private Integer[] getSignaturaComponents(String signaturaString) {
		Integer[] components = new Integer[3];
		
		Pattern pattern = Pattern.compile(SIGNATURA_REGEX);
		Matcher m = pattern.matcher(signaturaString.trim());
		m.find();
		SystemClassif classif = this.signaturaOptions.stream().filter(o -> o.getTekst().equals(m.group(1))).findFirst().orElse(null);
		
		if(classif != null) {
			components[0] = classif.getCode();
			components[1] = Integer.parseInt(m.group(2));
			components[2] = Integer.parseInt(m.group(3));
		}
		else {
			// най-вероятно ръчно е написана грешна сигнатура в url-a
			this.actionNew();
		}
		
		return components;
	}
	
	private String getSignaturaTekst(Integer code) {
		return this.signaturaOptions.stream().filter(o -> o.getCode() == code).findFirst().orElse(null).getTekst();
	}
	
	/**
	 * Вика функцията scrollToErrors на страницата, за да се скролне екранът към съобщенията за грешка.
	 * Сложено е, защото иначе съобщенията може да са извън видимия екран и user изобшо да не разбере,
	 * че е излязла грешка, и каква.
	 */
	private void scrollToMessages() {
		PrimeFaces.current().executeScript("scrollToErrors()");
	}
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public EuroDoc getEuroDoc() {
		return euroDoc;
	}

	public void setEuroDoc(EuroDoc euroDoc) {
		this.euroDoc = euroDoc;
	}

	public EuroDocDAO getDocDao() {
		return docDao;
	}

	public void setDocDao(EuroDocDAO docDao) {
		this.docDao = docDao;
	}

	public List<SystemClassif> getSignaturaOptions() {
		return signaturaOptions;
	}

	public void setSignaturaOptions(List<SystemClassif> signaturaOptions) {
		this.signaturaOptions = signaturaOptions;
	}

	public Object getSignaturaSeriaObj() {
		return signaturaSeriaObj;
	}

	public void setSignaturaSeriaObj(Object signaturaSeria) {
		this.signaturaSeriaObj = signaturaSeria;
	}

	public List<SystemClassif> getProcOptions() {
		return procOptions;
	}

	public void setProcOptions(List<SystemClassif> procOptions) {
		this.procOptions = procOptions;
	}

	public Object getProcSeria() {
		return procSeria;
	}

	public void setProcSeria(Object procSeria) {
		this.procSeria = procSeria;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public boolean isSignaturaChecked() {
		return signaturaChecked;
	}

	public void setSignaturaChecked(boolean signaturaChecked) {
		this.signaturaChecked = signaturaChecked;
	}

	public boolean isNsProgChecked() {
		return nsProgChecked;
	}

	public void setNsProgChecked(boolean nsProgChecked) {
		this.nsProgChecked = nsProgChecked;
	}

	public boolean isMsProgChecked() {
		return msProgChecked;
	}

	public void setMsProgChecked(boolean msProgChecked) {
		this.msProgChecked = msProgChecked;
	}
	
	public EuroDocDosie getTempDosie() {
		return tempDosie;
	}

	public void setTempDosie(EuroDocDosie tempDosie) {
		this.tempDosie = tempDosie;
	}

	public List<Integer> getLangList() {
		return langList;
	}

	public void setLangList(List<Integer> langList) {
		this.langList = langList;
	}

	public List<SystemClassif> getLangClassifs() {
		return langClassifs;
	}

	public void setLangClassifs(List<SystemClassif> langClassifs) {
		this.langClassifs = langClassifs;
	}

	public EuroDocLink getTempEuroLink() {
		return tempEuroLink;
	}

	public void setTempEuroLink(EuroDocLink tempEuroLink) {
		this.tempEuroLink = tempEuroLink;
	}
	
	public List<SystemClassif> getProceduresOptions() {
		return proceduresOptions;
	}

	public void setProceduresOptions(List<SystemClassif> proceduresOptions) {
		this.proceduresOptions = proceduresOptions;
	}

	public Map<Integer, EuroDocProcedure> getProceduresMap() {
		return proceduresMap;
	}

	public void setProceduresMap(Map<Integer, EuroDocProcedure> proceduresMap) {
		this.proceduresMap = proceduresMap;
	}

	public Map<Integer, Boolean> getProceduresChecked() {
		return proceduresChecked;
	}

	public void setProceduresChecked(Map<Integer, Boolean> proceduresChecked) {
		this.proceduresChecked = proceduresChecked;
	}

	public Map<Integer, String> getTempStanNomer() {
		return tempStanNomer;
	}

	public void setTempStanNomer(Map<Integer, String> tempStanNomer) {
		this.tempStanNomer = tempStanNomer;
	}

	public Map<Integer, LocalDate> getTempStanDate() {
		return tempStanDate;
	}

	public void setTempStanDate(Map<Integer, LocalDate> tempStanDate) {
		this.tempStanDate = tempStanDate;
	}

	public List<Files> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<Files> filesList) {
		this.filesList = filesList;
	}

	public List<Files> getFilesXList() {
		return filesXList;
	}

	public void setFilesXList(List<Files> filesXList) {
		this.filesXList = filesXList;
	}

	public List<Integer> getTemaNsList() {
		return temaNsList;
	}

	public void setTemaNsList(List<Integer> temaNsList) {
		this.temaNsList = temaNsList;
	}

	public List<SystemClassif> getTemaNsClassifs() {
		return temaNsClassifs;
	}

	public void setTemaNsClassifs(List<SystemClassif> temaNsClassifs) {
		this.temaNsClassifs = temaNsClassifs;
	}

	public List<Integer> getTemaEkList() {
		return temaEkList;
	}

	public void setTemaEkList(List<Integer> temaEkList) {
		this.temaEkList = temaEkList;
	}

	public List<SystemClassif> getTemaEkClassifs() {
		return temaEkClassifs;
	}

	public void setTemaEkClassifs(List<SystemClassif> temaEkClassifs) {
		this.temaEkClassifs = temaEkClassifs;
	}

	public boolean isSignaturaVrazChecked() {
		return signaturaVrazChecked;
	}

	public void setSignaturaVrazChecked(boolean signaturaVrazChecked) {
		this.signaturaVrazChecked = signaturaVrazChecked;
	}

	public String getSignaturaVraz() {
		return signaturaVraz;
	}

	public void setSignaturaVraz(String signaturaVraz) {
		this.signaturaVraz = signaturaVraz;
	}

	public Object getSignaturaVrazSeria() {
		return signaturaVrazSeria;
	}

	public void setSignaturaVrazSeria(Object signaturaVrazSeria) {
		this.signaturaVrazSeria = signaturaVrazSeria;
	}

	public Integer getSignaturaVrazGodina() {
		return signaturaVrazGodina;
	}

	public void setSignaturaVrazGodina(Integer signaturaVrazGodina) {
		this.signaturaVrazGodina = signaturaVrazGodina;
	}

	public Integer getSignaturaVrazNomer() {
		return signaturaVrazNomer;
	}

	public void setSignaturaVrazNomer(Integer signaturaVrazNomer) {
		this.signaturaVrazNomer = signaturaVrazNomer;
	}

	public Integer getSignSeria() {
		return signSeria;
	}

	public void setSignSeria(Integer signSeria) {
		this.signSeria = signSeria;
	}

	public Integer getSignGodina() {
		return signGodina;
	}

	public void setSignGodina(Integer signGodina) {
		this.signGodina = signGodina;
	}

	public Integer getSignNomer() {
		return signNomer;
	}

	public void setSignNomer(Integer signNomer) {
		this.signNomer = signNomer;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
