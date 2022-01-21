package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.PersistentEntity;
import com.ib.system.db.TrackableEntity;

/** Информация за конкретен файл в системата */
@Entity
@Table(name = "ADM_FILES")
public class Files extends TrackableEntity {

	/**  */
	private static final long serialVersionUID = -625136388299076952L;

	@SequenceGenerator(name = "Files", sequenceName = "SEQ_FILES", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "Files")
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "CODE_OBJECT")
	private Integer codeObject;
	
	@Column(name = "ID_OBJECT")
	private Integer idObject;

	@Column(name = "FILENAME")
	private String filename;

	@Column(name = "TIP")
	private String contentType;
	
	@Column(name = "BLOBCONTENT")
	private byte[] content;
	
	@Column(name = "ROLIA")
	private Integer rolia;
	
	@Column(name = "VID_DOC")
	private Integer vidDoc;
	

	/**  */
	public Files() {
		super();
	}

	public Files(Integer id, Integer codeObject, Integer idObject, String filename, String contentType, Integer rolia, 	Integer vidDoc) {
		super();
		this.id = id;
		this.codeObject = codeObject;
		this.idObject = idObject;
		this.filename = filename;
		this.contentType = contentType;
		this.rolia = rolia;
		this.vidDoc = vidDoc;
	}
	
	

	/** */
	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_FILE;
	}

	/** @return the content */
	public byte[] getContent() {
		return this.content;
	}

	/** @return the contentType */
	public String getContentType() {
		return this.contentType;
	}

	

	/** @return the filename */
	public String getFilename() {
		return this.filename;
	}

	

	

	/** @see PersistentEntity#getId() */
	@Override
	public Integer getId() {
		return this.id;
	}

	

	

	

	

	/** @param content the content to set */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/** @param contentType the contentType to set */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	

	/** @param filename the filename to set */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	

	/** @param id the id to set */
	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getCodeObject() {
		return codeObject;
	}



	public void setCodeObject(Integer codeObject) {
		this.codeObject = codeObject;
	}



	public Integer getIdObject() {
		return idObject;
	}



	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	public Integer getRolia() {
		return rolia;
	}



	public void setRolia(Integer rolia) {
		this.rolia = rolia;
	}



	public Integer getVidDoc() {
		return vidDoc;
	}



	public void setVidDoc(Integer vidDoc) {
		this.vidDoc = vidDoc;
	}



	

	
	
	
	
	
}