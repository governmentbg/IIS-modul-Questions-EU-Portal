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
@Table(name = "EURO_SECTION_Z")
public class EuroActNewSectionZ extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314332256092965330L;

	@SequenceGenerator(name = "EuroActNewSectionZ", sequenceName = "SEQ_EURO_SECTION_Z", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroActNewSectionZ")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	

	public EuroActNewSectionZ() {
		super();		
	}
	
	      
	

	
	@Column(name = "EURO_ACT_NEW_ID")
	private Integer euroActNewId;
		
	@Column(name = "NAME_BG_ACT")
	private String nameBgAct;
	
	@Column(name = "BG_ACT_ID")
	private Integer bgActId;
	
	@Column(name = "FILENAME")
	private String fileName;
	
	
	@Column(name = "DV_BROI")
	private String dvBroi;

	@Column(name = "DV_GODINA")
	private Integer dvGodina;


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
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_ACT_SECTION_Z;
	}

	@Override
	public Integer getId() {
		return id;
	}

	
	

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getNameBgAct() {
		return nameBgAct;
	}

	public void setNameBgAct(String nameBgAct) {
		this.nameBgAct = nameBgAct;
	}

	public Integer getBgActId() {
		return bgActId;
	}

	public void setBgActId(Integer bgActId) {
		this.bgActId = bgActId;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getEuroActNewId() {
		return euroActNewId;
	}

	public void setEuroActNewId(Integer euroActNewId) {
		this.euroActNewId = euroActNewId;
	}

	public String getDvBroi() {
		return dvBroi;
	}

	public void setDvBroi(String dvBroi) {
		this.dvBroi = dvBroi;
	}

	public Integer getDvGodina() {
		return dvGodina;
	}

	public void setDvGodina(Integer dvGodina) {
		this.dvGodina = dvGodina;
	}

	
    public EuroActNewSectionZ clone() {
    	EuroActNewSectionZ cloned = new EuroActNewSectionZ();
    	
    	cloned.setBgActId(bgActId);
    	cloned.setChanged(changed);
    	cloned.setDateLastMod(super.getDateLastMod());
    	cloned.setDateReg(super.getDateReg());
    	cloned.setDvBroi(dvBroi);
    	cloned.setDvGodina(dvGodina);
    	cloned.setEuroActNewId(euroActNewId);
    	cloned.setFileName(fileName);
    	cloned.setNameBgAct(nameBgAct);
    	cloned.setUserLastMod(super.getUserLastMod());
    	cloned.setUserReg(super.getUserReg());
    	
    	return cloned;
    	
    }
	
	
}
