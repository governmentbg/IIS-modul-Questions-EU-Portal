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


/**Това е клас, описващ класа "Състояние/статус на акта"
 * 
 */
@Entity
@Table(name = "EURO_ACT_STATUS")
public class EuroDocStatus  implements  Serializable{

     
    /**
	 * 
	 */
	private static final long serialVersionUID = -3861633581725553070L;

	/** ид  */  
	@SequenceGenerator(name = "EuroDocStatus", sequenceName = "SEQ_EURO_ACTSTATUS", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocStatus")
	@Column(name = "ID", unique = true, nullable = false)
    private Integer id ;
    
    /** Акт  */  
	 @Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct ;
    
   
    /** Сигнатура - процедура */ 
	 
	@Column(name = "STATUS")
    private Integer status;    
    
    /**  Сигнатура - номер */  
	@Column(name = "STATUS_DATE")
    private Date datStatus;
    
   
   
       
    
    
   
        
    /** Конструктор на обекта */
    public EuroDocStatus() {
       
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








	public void setIdAct(Integer id_act) {
		this.idAct = idAct;
	}








	public Integer getStatus() {
		return status;
	}








	public void setStatus(Integer status) {
		this.status = status;
	}





	public Date getDatStatus() {
		return datStatus;
	}








	public void setDatStatus(Date datStatus) {
		this.datStatus = datStatus;
	}

}
