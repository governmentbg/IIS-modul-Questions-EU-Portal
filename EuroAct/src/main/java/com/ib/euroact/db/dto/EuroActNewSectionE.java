package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

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
@Table(name = "EURO_SECTION_E")
public class EuroActNewSectionE extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314332256092965330L;

	@SequenceGenerator(name = "EuroActNewSectionE", sequenceName = "SEQ_EURO_SECTION_E", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroActNewSectionE")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	

	public EuroActNewSectionE() {
		super();		
	}
	
	
	

	
	@Column(name = "EURO_ACT_NEW_ID")
	private Integer euroActNewId;
		
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "NOTE")
	private String note;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "FILENAME")
	private String fileName;
	
	@Transient
	private boolean changed = false;
	
	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_ACT_SECTION_E;
	}

	@Override
	public Integer getId() {
		return id;
	}

	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEuroActNewId() {
		return euroActNewId;
	}

	public void setEuroActNewId(Integer euroActNewId) {
		this.euroActNewId = euroActNewId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
