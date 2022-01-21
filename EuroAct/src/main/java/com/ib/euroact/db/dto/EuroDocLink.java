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
@Table(name = "EURO_LINKS_ACT")
public class EuroDocLink implements  Serializable{

    
    
       
     // *** АТРИБУТИ НА ОБЕКТА ***
     
    /**
	 * 
	 */
	private static final long serialVersionUID = -9148462312893752645L;
	
	@SequenceGenerator(name = "EuroDocLink", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocLink")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	 @Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct;
	 
	 @Column(name = "URL")
    private String url;
    
	 @Column(name = "OPIS")
    private String opis;
   
   
    
    /** Конструктор на обекта */
    public EuroDocLink() {
       
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }


	public Integer getIdAct() {
		return idAct;
	}


	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}

    
}
