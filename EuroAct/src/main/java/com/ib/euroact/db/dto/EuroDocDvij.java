package com.ib.euroact.db.dto;

import java.io.Serializable;
import java.util.Date;


/**Това е клас, описващ движенията на акт
 * 
 */
public class EuroDocDvij implements  Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 7601659528462726953L;

	   
   
    // *** АТРИБУТИ НА ОБЕКТА ***
    
    
    /** Дата на изслушването */
    private Date datSabitie;    
    
    /** Aкт  */  
    private Integer idAct ;
    private String nameAct;    
    
    /** Забележка */ 
    private String comment;   
    
    /** Наименование */ 
    private String ime;   
    
    
    private boolean forDelete = false;
    
    private Long idKomisia;
    
    private Long vidDoc;
    
    
    private String rnDoc;
    
    private Date datDoc;
    
        
   


   

    

   

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public boolean isForDelete() {
		return forDelete;
	}

	public void setForDelete(boolean forDelete) {
		this.forDelete = forDelete;
	}

	public Date getDatSabitie() {
		return datSabitie;
	}

	public void setDatSabitie(Date datSabitie) {
		this.datSabitie = datSabitie;
	}

	public Integer getIdAct() {
		return idAct;
	}

	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}

	public String getNameAct() {
		return nameAct;
	}

	public void setNameAct(String nameAct) {
		this.nameAct = nameAct;
	}

	public Long getIdKomisia() {
		return idKomisia;
	}

	public void setIdKomisia(Long idKomisia) {
		this.idKomisia = idKomisia;
	}

	public Long getVidDoc() {
		return vidDoc;
	}

	public void setVidDoc(Long vidDoc) {
		this.vidDoc = vidDoc;
	}

	public String getRnDoc() {
		return rnDoc;
	}

	public void setRnDoc(String rnDoc) {
		this.rnDoc = rnDoc;
	}

	public Date getDatDoc() {
		return datDoc;
	}

	public void setDatDoc(Date datDoc) {
		this.datDoc = datDoc;
	}
    
    
    
}
