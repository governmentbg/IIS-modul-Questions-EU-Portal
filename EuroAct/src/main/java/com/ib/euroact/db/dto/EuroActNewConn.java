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
@Table(name = "EURO_ACTS_CONN")
public class EuroActNewConn extends TrackableEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314332234092965330L;

	@SequenceGenerator(name = "EuroActNewConn", sequenceName = "SEQ_EURO_ACT_CONN", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroActNewConn")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	

	public EuroActNewConn() {
		super();		
	}
	
	
	public EuroActNewConn(Integer idAct1, Integer idAct2, Integer role1, Integer role2 ) {
		super();		
		this.euroActNewId1 = idAct1;
		this.euroActNewId2 = idAct2;
		this.roleAct1 = role1;
		this.roleAct2 = role2;
	}

	
	@Column(name = "EURO_ACT_NEW_ID1")
	private Integer euroActNewId1;
	
	
	@Column(name = "ROLE_ACT1")
	private Integer roleAct1;
	
	@Column(name = "EURO_ACT_NEW_ID2")
	private Integer euroActNewId2;
	
	
	@Column(name = "ROLE_ACT2")
	private Integer roleAct2;
	
	
	@Column(name = "NOTE")
	private String note;
	
	@Column(name = "FILENAME")
	private String fileName;
	
	
	@Transient
	private boolean changed = false;
	
	
	@Transient
	private String nameAct1;
	@Transient
	private String nameAct2;
	@Transient
	private String url1;
	@Transient
	private String url2;
	@Transient
	private Integer godina1;
	@Transient
	private Integer godina2;
	@Transient
	private String rnFull1;
	@Transient
	private String rnFull2;
	
	@Transient
	private String roleText1;
	@Transient
	private String roleText2;
	
	
	
	public String getRoleText1() {
		return roleText1;
	}


	public void setRoleText1(String roleText1) {
		this.roleText1 = roleText1;
	}


	public String getRoleText2() {
		return roleText2;
	}


	public void setRoleText2(String roleText2) {
		this.roleText2 = roleText2;
	}


	public String getNameAct1() {
		return nameAct1;
	}


	public void setNameAct1(String nameAct1) {
		this.nameAct1 = nameAct1;
	}


	public String getNameAct2() {
		return nameAct2;
	}


	public void setNameAct2(String nameAct2) {
		this.nameAct2 = nameAct2;
	}


	public String getUrl1() {
		return url1;
	}


	public void setUrl1(String url1) {
		this.url1 = url1;
	}


	public String getUrl2() {
		return url2;
	}


	public void setUrl2(String url2) {
		this.url2 = url2;
	}


	public Integer getGodina1() {
		return godina1;
	}


	public void setGodina1(Integer godina1) {
		this.godina1 = godina1;
	}


	public Integer getGodina2() {
		return godina2;
	}


	public void setGodina2(Integer godina2) {
		this.godina2 = godina2;
	}


	public String getRnFull1() {
		return rnFull1;
	}


	public void setRnFull1(String rnFull1) {
		this.rnFull1 = rnFull1;
	}


	public String getRnFull2() {
		return rnFull2;
	}


	public void setRnFull2(String rnFull2) {
		this.rnFull2 = rnFull2;
	}


	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_ACT_CONN;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getEuroActNewId1() {
		return euroActNewId1;
	}

	public void setEuroActNewId1(Integer euroActNewId1) {
		this.euroActNewId1 = euroActNewId1;
	}

	public Integer getRoleAct1() {
		return roleAct1;
	}

	public void setRoleAct1(Integer roleAct1) {
		this.roleAct1 = roleAct1;
	}

	public Integer getEuroActNewId2() {
		return euroActNewId2;
	}

	public void setEuroActNewId2(Integer euroActNewId2) {
		this.euroActNewId2 = euroActNewId2;
	}

	public Integer getRoleAct2() {
		return roleAct2;
	}

	public void setRoleAct2(Integer roleAct2) {
		this.roleAct2 = roleAct2;
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


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
