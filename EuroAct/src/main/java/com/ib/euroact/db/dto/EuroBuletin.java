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


/**Това е клас, описващ обекта акт на ЕС
 * 
 */
@Entity
@Table(name = "EURO_BULETIN")
public class  EuroBuletin extends TrackableEntity implements Cloneable {

    
   
   
    
     // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1635522455320664063L;
	
	
	@SequenceGenerator(name = "EuroBuletin", sequenceName = "SEQ_BULETIN", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroBuletin")
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	

	/**  Номер */  
	@Column(name = "NOMER")
    private Integer nomer;
    
    /**  Дата на бюлетина */ 
	@Column(name = "DAT_BULETIN")
    private Date datBuletin;
    
    /**  Информация */ 
	@Column(name = "DOP_INFO")
    private String donInfo;
    
    /**  Заглавие */ 
	@Column(name = "ZAGLAVIE")
    private String zaglavie;
    
    
    
    /** Конструктор на обекта */
    public EuroBuletin() {
       
    }
   

    


	public Integer getNomer() {
		return nomer;
	}


	public void setNomer(Integer nomer) {
		this.nomer = nomer;
	}


	public Date getDatBuletin() {
		return datBuletin;
	}


	public void setDatBuletin(Date datBuletin) {
		this.datBuletin = datBuletin;
	}


	public String getDonInfo() {
		return donInfo;
	}


	public void setDonInfo(String donInfo) {
		this.donInfo = donInfo;
	}


	public String getZaglavie() {
		return zaglavie;
	}


	public void setZaglavie(String zaglavie) {
		this.zaglavie = zaglavie;
	}


	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_EURO_BULETIN;
	}


	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

   
}
