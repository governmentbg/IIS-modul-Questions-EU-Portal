package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

// Generated 2009-12-2 11:10:13 by Hibernate Tools 3.2.5.Beta

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ib.system.db.dto.SystemJournal;

/**
 * BabhJournal е създаден специално, за да записва данни във формата на таблицата JOURNAL от старата база на ВЕТИС
 */
@Entity
@Table(name = "ADM_JOURNAL")
public class EuroActJournal implements java.io.Serializable {

	
	
	
	private static final long serialVersionUID = 1753489188512533516L;
	
//	Генератор, когато ползваме sid таблица
//	@TableGenerator(table = "sid", name = "BabhJournal",  allocationSize = 1, initialValue = 0, pkColumnName = "OBJECT",   valueColumnName = "NEXT_VAL", pkColumnValue = "jur")
//	@GeneratedValue(strategy = TABLE, generator="BabhJournal")
//	@Column(name = "ID", unique = true, nullable = false)
//	@Id
	
	
//	Генератор, когато ползваме sequence
	@SequenceGenerator(name = "EuroActJournal", sequenceName = "SEQ_JOURNAL", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroActJournal")
	@Column(name = "ID", unique = true, nullable = false)
	
	private Long id;
	
	@Column(name = "DATE_ACTION")
	private Date dateAction;
	
	@Column(name = "ID_USER")
	private Long idUser;
	
	@Column(name = "CODE_ACTION")
	private Long codeAction;
	
	@Column(name = "CODE_OBJECT")
	private Long codeObject;
	
	@Column(name = "ID_OBJECT")
	private Long idObject;

	@Column(name = "IDENT_OBJECT")
	private String identObject;
	
	@Column(name = "OBJECT_CONTENT")
	private byte[] objectContent;
	
	
	
	public EuroActJournal() {
	}
	
	public EuroActJournal(SystemJournal journal) {
		
		this.codeAction = new Long(journal.getCodeAction());
		this.codeObject = new Long(journal.getCodeObject());
		this.dateAction = journal.getDateAction();
		this.identObject = journal.getIdentObject();
		this.idObject = new Long(journal.getIdObject());
		this.objectContent = journal.getObjectContent();
	}
	
	/**
	 * @param idUser
	 * @param codeAction
	 * @param codeObject
	 * @param idObject
	 * @param identObject
	 * @param objectContent
	 */
	public EuroActJournal(Long idUser, Long codeAction, Long codeObject, Long idObject, String identObject, byte[] objectContent) {
		this.idUser = idUser;
		this.dateAction = new Date();
		this.codeAction = codeAction;
		this.codeObject = codeObject;
		this.idObject =  idObject;
		this.identObject = identObject;
		this.objectContent = objectContent;
	}
	

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateAction() {
		return this.dateAction;
	}

	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	public Long getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getCodeAction() {
		return this.codeAction;
	}

	public void setCodeAction(Long codeAction) {
		this.codeAction = codeAction;
	}

	public Long getCodeObject() {
		return this.codeObject;
	}

	public void setCodeObject(Long codeObject) {
		this.codeObject = codeObject;
	}

	public Long getIdObject() {
		return this.idObject;
	}

	public void setIdObject(Long idObject) {
		this.idObject = idObject;
	}

	public String getIdentObject() {
		return this.identObject;
	}

	public void setIdentObject(String identObject) {
		this.identObject = identObject;
	}

	public void setObjectContent(byte[] objectContent) {
		this.objectContent = objectContent ==null?null:objectContent .clone();
	}

	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Journal [id=");
		builder.append(id);
		builder.append(", codeAction=");
		builder.append(codeAction);
		builder.append(", codeObject=");
		builder.append(codeObject);
		builder.append(", dateAction=");
		builder.append(dateAction);
		builder.append(", idObject=");
		builder.append(idObject);
		builder.append(", idUser=");
		builder.append(idUser);
		builder.append(", identObject=");
		builder.append(identObject);
		builder.append(", objectContent=");
		builder.append(Arrays.toString(objectContent));
		builder.append("]");
		return builder.toString();
	}

	

	

}
