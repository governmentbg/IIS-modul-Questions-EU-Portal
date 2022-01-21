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


/**Това е клас, описващ класа "Връзки между актове"
 * 
 */
@Entity
@Table(name = "EURO_VRAZ_ACT")
public class EuroDocVraz  implements  Serializable{

    
   // *** АТРИБУТИ НА ОБЕКТА ***
     
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1138304269652882969L;

	/** ид  */
	@SequenceGenerator(name = "EuroDocVraz", sequenceName = "SEQ_ACT_DOP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "EuroDocVraz")
	@Column(name = "ID", unique = true, nullable = false)
    private Integer id ;
    
    /** Актове  */  
	@Column(name = "id_act1")
    private Integer idAct1 ;
	@Column(name = "id_act2")
    private Integer idAct2 ;    
    
    
    /** Сигнатура на вързания документ  */
	@Transient
    private String signVraz;
	@Transient
    private String nameVraz;
    
	@Transient
    private Integer seria;
	@Transient
    private Integer godina;
	@Transient
    private Integer nomer;
    


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSignVraz(String signVraz) {
        this.signVraz = signVraz;
    }

    public String getSignVraz() {
        return signVraz;
    }

    public void setNameVraz(String nameVraz) {
        this.nameVraz = nameVraz;
    }

    public String getNameVraz() {
        return nameVraz;
    }

   
    public void setSeria(Integer seria) {
        this.seria = seria;
    }

    public Integer getSeria() {
        return seria;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }

    public Integer getGodina() {
        return godina;
    }

    public void setNomer(Integer nomer) {
        this.nomer = nomer;
    }

    public Integer getNomer() {
        return nomer;
    }

    public int compareTo(Object o) throws ClassCastException {      
        if (o instanceof EuroDocVraz){
            EuroDocVraz compVraz = (EuroDocVraz)o;            
            
            
            if (this.getIdAct1().equals(compVraz.getIdAct1()) && this.getIdAct2().equals(compVraz.getIdAct2()))
                return 0;
            if (this.getIdAct1().equals(compVraz.getIdAct2()) && this.getIdAct2().equals(compVraz.getIdAct1()))
                return 0;
            if (compVraz.getIdAct1().longValue() == 6974 && getIdAct1().longValue() == 6974 && compVraz.getIdAct2().longValue() == 6081 && getIdAct2().longValue() == 6081)
                System.out.println(getIdAct1() + "|" +getIdAct2() + "|" + compVraz.getIdAct1()  + "|" +compVraz.getIdAct2());    
            return -1;
            
        }
        else
            throw new ClassCastException("Not EuroDocVraz");
    }

	public Integer getIdAct1() {
		return idAct1;
	}

	public void setIdAct1(Integer idAct1) {
		this.idAct1 = idAct1;
	}

	public Integer getIdAct2() {
		return idAct2;
	}

	public void setIdAct2(Integer idAct2) {
		this.idAct2 = idAct2;
	}
    
//    public boolean equals(Object o) throws ClassCastException {      
//        if (o instanceof EuroDocVraz){
//            EuroDocVraz compVraz = (EuroDocVraz)o;            
//            
//            
//            if (!this.getId_act1().equals(compVraz.getId_act1()) )
//                return true;
//            if (this.getId_act1().equals(compVraz.getId_act2()) && this.getId_act2().equals(compVraz.getId_act1()))
//                return true;
//            
//            return false;
//            
//        }
//        else
//            throw new ClassCastException("Not EuroDocVraz");
//    }
    
    
}
