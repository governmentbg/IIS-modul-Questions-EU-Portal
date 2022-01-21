package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EURO_ACT_LANG")
public class EuroDocLang implements  Serializable{

    
    
       
     // *** АТРИБУТИ НА ОБЕКТА ***
     
    /**
	 * 
	 */
	private static final long serialVersionUID = -7876221082653351612L;
	
	@SequenceGenerator(name = "EuroDocLang", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocLang")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	 @Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct;
    
	 @Column(name = "LANG")
    private Integer lang;
    
   
   
    
    /** Конструктор на обекта */
    public EuroDocLang() {
       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Integer getLang() {
        return lang;
    }


	public Integer getIdAct() {
		return idAct;
	}


	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}

    
    

}
