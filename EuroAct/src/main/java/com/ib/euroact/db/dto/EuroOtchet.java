package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.TrackableEntity;


/**Това е клас, описващ класа "Отчет на МС пред Народно събрание"
 * 
 */
@Entity
@Table(name = "EURO_OTCHET")
public class EuroOtchet extends TrackableEntity implements Cloneable {

       
    /**
	 * 
	 */
	private static final long serialVersionUID = 2126616302085261221L;

	
    
	@SequenceGenerator(name = "EuroOtchet", sequenceName = "SEQ_IZSLUSHVANE", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroOtchet")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
    
    
    
    /** Адресат  */
	@Column(name = "ADRESAT")
    private Integer adresat ;
    
    
    
    /** Относно */ 
	@Column(name = "ANOT")
    private String anot;   
   
    
    /** Дата на изслушването */
	@Column(name = "DAT_OTCHET")
    private Date datOtchet;
    
   	/** Статус на изслушването */
	@Column(name = "STATUS")
    private Integer status;   
    
       
    /** Бележка */    
	@Column(name = "ZABELEJKA")
    private String comment;   
    
    /** Вид*/
	@Column(name = "VID")
    private Integer vid;   
    
    
   
        
    /** Конструктор на обекта */
    public EuroOtchet() {
       
        
    }
   

    
   
    public void setAnot(String anot) {
        this.anot = anot;        
    }

    public String getAnot() {
        return anot;
    }

   
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

   


    public void setComment(String comment) {
        this.comment = comment;       
    }

    public String getComment() {
        return comment;
    }

   


    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getVid() {
        return vid;
    }


    public void setAdresat(Integer adresat) {
        this.adresat = adresat;
    }

    public Integer getAdresat() {
        return adresat;
    }

    
    
    public Date getDatOtchet() {
		return datOtchet;
	}


	public void setDatOtchet(Date datOtchet) {
		this.datOtchet = datOtchet;
	}


	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_OTCHET;
	}


	@Override
	public Object getId() {		
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
