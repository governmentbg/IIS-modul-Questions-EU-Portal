package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**Това е клас, описващ класа "Дати на становища по процедури"
 * 
 */
@Entity
@Table(name = "EURO_STANOVISTA")
public class EuroDocProcedureStan  implements  Serializable{

       
    
    
   
    
    
   // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4111929126290135299L;

	/** ид  */  
	@SequenceGenerator(name = "EuroDocProcedureStan", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocProcedureStan")
	@Column(name = "ID", unique = true, nullable = false)
    private Integer id ;
    
    /** Процедура  */  
	@Column(name = "ID_PROCEDURE", insertable = false, updatable = false)
    private Integer idProcedure ;
    
   
   
    /** Дата на становище */ 
	 @Column(name = "STANOVISTE")
    private Date stanoviste;
    
    /** Номер на становище */
	 @Column(name = "RN_DOC")
    private String rnDoc;
   
   
    
   
        
    /** Конструктор на обекта */
    public EuroDocProcedureStan() {
       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setIdProcedure(Integer idProcedure) {
        this.idProcedure = idProcedure;
    }

    public Integer getIdProcedure() {
        return idProcedure;
    }


    public void setStanoviste(Date stanoviste) {
        this.stanoviste = stanoviste;
    }

    public Date getStanoviste() {
        return stanoviste;
    }

    public void setRnDoc(String rnDoc) {
        this.rnDoc = rnDoc;
    }

    public String getRnDoc() {
        return rnDoc;
    }
}
