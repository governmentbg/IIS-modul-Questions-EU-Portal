package com.ib.euroact.db.dto;

import java.io.Serializable;
import java.util.Date;


/**Това е клас, описващ обекта комисия на акт
 * 
 */
public class  EuroDocKomisia  implements  Serializable, Cloneable {

    
    
    
    
    
     //  ********************************************************************
     //  * Кодове на атрибутите на обектите. Освен тук описанието трябва да *
     //  * се направи и в таблицата objects и object_attributes заради      *
     //  * универсалните справки.                                           *
     //  * Поредността на атрибутите започва от 2, като е приемаме че е id  *
     //  * (системният идентификатор на обекта)                             *
     //  ********************************************************************
    
   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3055990632664829268L;



	/** Код на атрибута "Комисия" */
    public static final long CODE_ATTRIB_KOMISIA = 2;
    
  
    
     // *** АТРИБУТИ НА ОБЕКТА ***
    
    
    
    private Integer id;
    
    /** Акт */  
    private Integer idAct;
  
    /** Комисия */  
    private Integer idKomisia  ;    
    private Date dat_doklad  ;
    
    /**Ipex*/
    private String descript;
    private String link_title;
    private String url;
    
    
    
    
    
    /** Конструктор на обекта */
    public EuroDocKomisia() {
       
    }
   
    public EuroDocKomisia clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (EuroDocKomisia) super.clone();
	}
    

    

    public void setDat_doklad(Date dat_doklad) {
        this.dat_doklad = dat_doklad;
    }

    public Date getDat_doklad() {
        return dat_doklad;
    }



	public void setUrl(String url) {
		this.url = url;
	}




	public String getUrl() {
		return url;
	}




	public void setDescript(String descript) {
		this.descript = descript;
	}




	public String getDescript() {
		return descript;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdAct() {
		return idAct;
	}

	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}

	public Integer getIdKomisia() {
		return idKomisia;
	}

	public void setIdKomisia(Integer idKomisia) {
		this.idKomisia = idKomisia;
	}

	public void setLink_title(String link_title) {
		this.link_title = link_title;
	}




	public String getLink_title() {
		return link_title;
	}
}
