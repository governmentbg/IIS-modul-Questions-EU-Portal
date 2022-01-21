package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**Това е клас, описващ класа "Процедура на акт"
 * 
 */
@Entity
@Table(name = "EURO_PROCEDURE_ACT")
public class EuroDocProcedure  implements  Serializable{

       
    
    
   
    
    
   // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1059040406900193476L;

	/** ид  */  
	@SequenceGenerator(name = "EuroDocProcedure", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocProcedure")
	@Column(name = "ID", unique = true, nullable = false)
    private Integer id ;
    
    /** Акт  */  
	@Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct ;
    
    /** Процедура */ 
	 @Column(name = "PROCEDURE")
    private Integer procedure;   
      
   
    /** Срок */ 
    @Column(name = "SROK")
    private Date srok;
   
   
    @LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true)
	@JoinColumn(name = "ID_PROCEDURE", referencedColumnName = "ID", nullable = false)   
    private List<EuroDocProcedureStan> stanovista = new ArrayList<EuroDocProcedureStan>();
    
   
        
    /** Конструктор на обекта */
    public EuroDocProcedure() {
       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setIdAct(Integer idAct) {
        this.idAct = idAct;
    }

    public Integer getIdAct() {
        return idAct;
    }

    public void setProcedure(Integer procedure) {
        this.procedure = procedure;
    }

    public Integer getProcedure() {
        return procedure;
    }

    

    public void setSrok(Date srok) {
        this.srok = srok;
    }

    public Date getSrok() {
        return srok;
    }


    

    public void setStanovista(List<EuroDocProcedureStan> stanovista) {
        this.stanovista = stanovista;
    }

    public List<EuroDocProcedureStan> getStanovista() {
        return stanovista;
    }
}
