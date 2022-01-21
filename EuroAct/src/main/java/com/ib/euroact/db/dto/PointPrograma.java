package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.TrackableEntity;


/**Това е клас, описващ класа "Точка от годишната програма на Народно събрание"
 * 
 */
@Entity
@Table(name = "EURO_POINTS_PR")
public class PointPrograma  extends TrackableEntity implements Cloneable {


     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5585524099544375669L;

	

	@SequenceGenerator(name = "PointPrograma", sequenceName = "SEQ_PROGRAMA", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "PointPrograma")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
    
    /** Програма  */  
	@Column(name = "ID_PROGRAMA")
    private Integer idPrograma ;
    
    /** Точка */ 
	@Column(name = "TEKST")
    private String tekst;   
   
   
    /** Номер на точка */ 
	@Column(name = "PORED")
    private Integer pored;
   
   
   
       
    
    
   
        
    /** Конструктор на обекта */
    public PointPrograma() {
       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getTekst() {
        return tekst;
    }

    public void setPored(Integer pored) {
        this.pored = pored;
    }

    public Integer getPored() {
        return pored;
    }


	public Integer getIdPrograma() {
		return idPrograma;
	}


	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}


	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_PROGRAMA_POINT;
	}
    
    
}
