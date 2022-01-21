package com.ib.euroact.db.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**Това е клас, описващ класа "Процедура на акт"
 * 
 */
@Entity
@Table(name = "EURO_DOSIE_ACT")
public class EuroDocDosie  implements  Serializable{

       
    
    
   
    
    
   // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3861633586725553070L;

	/** ид  */
	@SequenceGenerator(name = "EuroDocDosie", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocDosie")
	@Column(name = "ID", unique = true, nullable = false)
    private Integer id ;
    
    /** Акт  */  
	@Column(name = "ID_ACT", insertable = false, updatable = false)
    private Integer idAct ;
    
   
    /** Сигнатура - процедура */ 
	@Column(name = "SIGN_PROC")
    private Integer signProc;
	@Transient
	private String signProcText;
    
        
	/**  Сигнатура - номер */
	@Column(name = "SIGN_NOMER")
    private Integer signNomer;
    
    /**  Сигнатура - година */
	@Column(name = "SIGN_GODINA")
    private Integer signGodina;
    
    /**  Сигнатура - обединен (цял номер) */ 
	@Column(name = "SIGN")
    private String sign;
   
   
        
    /** Конструктор на обекта */
    public EuroDocDosie() {
       
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



	public Integer getSignProc() {
		return signProc;
	}



	public void setSignProc(Integer signProc) {
		this.signProc = signProc;
	}



	public Integer getSignNomer() {
		return signNomer;
	}



	public void setSignNomer(Integer signNomer) {
		this.signNomer = signNomer;
	}



	public Integer getSignGodina() {
		return signGodina;
	}



	public void setSignGodina(Integer signGodina) {
		this.signGodina = signGodina;
	}



	public String getSign() {
		return sign;
	}



	public void setSign(String sign) {
		this.sign = sign;
	}


    
	public String getSignProcText() {
		return signProcText;
	}



	public void setSignProcText(String signProcText) {
		this.signProcText = signProcText;
	}


    
}
