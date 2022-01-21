package com.ib.euroact.db.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ib.euroact.system.EuroConstants;
import com.ib.system.db.JournalAttr;
import com.ib.system.db.PersistentEntity;
import com.ib.system.exceptions.DbErrorException;

@Entity(name = "AdmUsers")
@Table(name = "ADM_USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER, name = "SYSTEM_ID")
//@AttributeOverride(name = "dateReg", column = @Column(name="DATE_REG",updatable = false,insertable = false))
public class AdmUsers implements Serializable, PersistentEntity {

	private static final long serialVersionUID = -892569392915416508L;
	@Id
	@Column(name = "ID")
	private Integer id;
	/** "име за регистрация" */
	@Column(name = "LOGIN_NAME")
	private String login_name;

	/** "Парола"" */
	@Column(name = "PASS")
	private String pass;

	/** "Служител" */
	@Column(name = "ID_LICE")
	private Long id_lice;
	@Transient
	private String name_lice;

	/** "Дата на регистрация" */
	@Column(name = "DATE_REG")
	Date date_reg;

	/** "Статус" - 1 - забранен достъп до системата; 2 - разрешен достъп */
	@Column(name = "STATUS")
	private Long status;
	@Transient
	private String statusText;

	/** "Дата на статуса" */
	@Column(name = "DATE_STATUS")
	private Date date_status;

	/* ЕГН */
	@Transient
	private String egn;

	/** "Име" */
	@Column(name = "IME")
	private String ime;

	/** "Презиме" */
	@Column(name = "PREZIME")
	private String prezime;

	/** "Фамилия" */
	@Column(name = "FAMILIA")
	private String familia;

	/** "Забележка" */
	@Transient
	private String comment;
	
	@Column(name = "EMAIL")
	@JournalAttr(label = "email", defaultText = "Емейл")
	private String email;
	
	
	
	

	/**
	 * Конструктор на класа Users. Зарежда кода на типа обект, който създаваме. За
	 * обекта "Потребител" този код е 1
	 */
	public AdmUsers() {

		date_reg = new Date();
		date_status = new Date();
		status = Long.valueOf(1);
	}

	public String getComment() {
		return comment;
	}

	public Date getDate_reg() {
		return date_reg;
	}

	/**
	 * Връща текущата стойност на атрибута "Дата на статуса".
	 * 
	 * @return status_date Дата на статуса
	 */
	public Date getDate_status() {
		return date_status;
	}

	public String getEgn() {
		return egn;
	}

	public String getFamilia() {
		return familia;
	}

	public Integer getId() {
		return id;
	}

	public Long getId_lice() {
		return id_lice;
	}

	public String getIme() {
		return ime;
	}

	/**
	 * Връща текущата стойност на атрибута "Име за регистрация".
	 * 
	 * @return login_name име за регистрация
	 */
	public String getLogin_name() {
		return login_name;
	}

	public String getName_lice() {
		return name_lice;
	}

	/**
	 * Връща текущата стойност на атрибута "Парола".
	 * 
	 * @return names Парола
	 */
	public String getPass() {
		return pass;
	}

	public String getPrezime() {
		return prezime;
	}

	/**
	 * Връща текущата стойност на атрибута "Статус".
	 * 
	 * @return status Статус
	 */
	public Long getStatus() {
		return status;
	}

//    /**Връща текущата стойност на полето "Роли".
//     * @return Роли
//     */
//    public ArrayList<Long> getSelectedRoles() {
//        
//        ArrayList<Long> rez = new ArrayList<Long>();
//        if (userRoles != null) {
//	        for ( AdmUserRoles role : userRoles )  {
//	            rez.add(role.getCode_role());
//	        }
//        }
//        
//         return rez;
//    }
//    
//    /**Връща текущата стойност на полето "Роли".
//     * @return Роли
//     */
//    public void setSelectedRoles(ArrayList<Long> roles) {
//        
//        if (this.getUserRoles() == null)
//            this.setUserRoles(new ArrayList<AdmUserRoles>());
//        
//        this.getUserRoles().clear(); 
//        if (roles != null) {
//	        for ( Long code : roles )  {
//	        	AdmUserRoles ur = new AdmUserRoles();
//	            ur.setCode_role(code);
//	            this.getUserRoles().add(ur);
//	        }
//        }    
//    }
//        
//        
//    /**Връща текущата стойност на полето "Групи на потребителя".
//     * @return Роли
//     */
//    public ArrayList<Long> getSelectedGroups() {
//        
//        ArrayList<Long> rez = new ArrayList<Long>();
//        if (userGroups != null) {
//	        for ( AdmGroups group : userGroups )  {
//	            rez.add(group.getId());
//	        }
//        }    
//        
//         return rez;
//    }
//    
//    /**Връща текущата стойност на полето "Групи на потребителя".
//     * @return Роли
//     */
//    public void setSelectedGroups(ArrayList<Long> groups) {
//        
//        if (this.getUserGroups() == null)
//            this.setUserGroups(new ArrayList<AdmGroups>());
//        
//        this.getUserGroups().clear(); 
//        if (groups != null) {
//	        for ( Long id_group : groups )  {
//	            AdmGroups gr = new AdmGroups();
//	            gr.setId(id_group);
//	            this.getUserGroups().add(gr);
//	        } 
//        }    
//    }

	public String getStatusText() {
		return statusText;
	}

	public void setComment(String comment) {
		this.comment = comment;

	}

	public void setDate_reg(Date date_reg) {
		this.date_reg = date_reg;
	}

	/**
	 * Задава нова стойност на атрибута "Дата на статуса".
	 * 
	 * @param status_date Дата на статуса
	 */
	public void setDate_status(Date status_date) {

		this.date_status = status_date;
	}

	public void setEgn(String egn) {
		this.egn = egn;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Зарежда нова стойност на полето "Роли".
	 * 
	 * @param userObjectRoles роли
	 */
//    public void setUserRoles(List<AdmUserRoles> userObjectRoles) {
//        this.userRoles = userObjectRoles;
//    }
//
//    /**Връща текущата стойност на полето "Роли".
//     * @return Роли
//     */
//    public List<AdmUserRoles> getUserRoles() {
//        return userRoles;
//    }
//    
//    /**Задава групите в които участва потребителя.
//     * @param userGroups групи
//     */
//    public void setUserGroups(List<AdmGroups> userGroups) {
//        this.userGroups = userGroups;
//    }
//
//    /**Връща групите в които участва потребителя.
//     * @return групи
//     */
//    public List<AdmGroups> getUserGroups() {
//        return userGroups;
//    }

	public void setId_lice(Long id_lice) {
		this.id_lice = id_lice;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * Задава нова стойност на атрибута "име за регистрация".
	 * 
	 * @param login_name име за регистрация
	 */
	public void setLogin_name(String login_name) {

		this.login_name = login_name;
	}

	public void setName_lice(String name_lice) {
		this.name_lice = name_lice;
	}

	/**
	 * Задава нова стойност на атрибута "Парола".
	 * 
	 * @param password Парола
	 */
	public void setPass(String password) {

		this.pass = password;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	/**
	 * Задава нова стойност на атрибута "Статус".
	 * 
	 * @param status Статус
	 */
	public void setStatus(Long status) {

		this.status = status;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	/**
	 * Предефиниран метод, използван за извличане на допълнителна информация за
	 * обекта, при записване на данни в журнала.
	 * 
	 * @return Идентификационна информация за обекта
	 *//*
		 * @Override public String toString() { return id+" "+ime+" "+familia;
		 * //this.getNames(); }
		 */

	@Override
	public Integer getCodeMainObject() {
		return EuroConstants.CODE_ZNACHENIE_JOURNAL_USER;
	}

	@Override
	public String getIdentInfo() throws DbErrorException {
		return name_lice;
	}

	@Override
	public String toString() {
		return "AdmUsers [id=" + id + ", login_name=" + login_name + ", pass=" + pass + ", id_lice=" + id_lice
				+ ", name_lice=" + name_lice + ", date_reg=" + date_reg + ", status=" + status + ", statusText="
				+ statusText + ", date_status=" + date_status + ", egn=" + egn + ", ime=" + ime + ", prezime=" + prezime
				+ ", familia=" + familia + ", comment=" + comment + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
