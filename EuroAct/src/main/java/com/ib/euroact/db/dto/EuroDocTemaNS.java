package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**Това е клас, описващ класа "Тематика НС"
 * 
 */
@Entity
@Table(name = "EURO_TEMA_ACT_NS")
public class EuroDocTemaNS  implements  Serializable{

       
    
    
   
    
    
   // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5492464692170649476L;

	/** ид  */  
	@SequenceGenerator(name = "EuroDocTemaNS", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocTemaNS")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
    
    /** Акт  */  
    @Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct ;
    
   
    /** Тематика */ 
    @Column(name = "TEMA")
    private Integer tema;    
    
   
    /** Конструктор на обекта */
    public EuroDocTemaNS() {       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


   


    public void setTema(Integer tema) {
        this.tema = tema;
    }

    public Integer getTema() {
        return tema;
    }

   

	public Integer getIdAct() {
		return idAct;
	}


	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}
}
