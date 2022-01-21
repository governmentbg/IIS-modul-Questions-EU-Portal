package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.TrackableEntity;


/**Това е клас, описващ класа "Годишна програма на Народно събрание"
 * 
 */
@Entity
@Table(name = "EURO_PROGRAMA")
public class Programa extends TrackableEntity implements Cloneable {

       
	/**
	 * 
	 */
	private static final long serialVersionUID = -4464400736204929387L;


	@SequenceGenerator(name = "Programa", sequenceName = "SEQ_PROGRAMA", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "Programa")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
     
    
   

	/** Година  */ 
	@Column(name = "GODINA")
    private Integer godina ;
    
    
    
    /** Заглавна част на програма */ 
	@Column(name = "HEADER")
    private String header;   
   
    
    /** Крайна част на програмата */
	@Column(name = "FOOTER")
    private String footer;
    
  
	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_PROGRAMA;
	}

	

	public Integer getGodina() {
		return godina;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	 public Integer getId() {
			return id;
		}
   
}
