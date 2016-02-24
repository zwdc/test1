package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @ClassName: User
 * @Description:用户实体类
 * @author: zml
 *
 */

@Entity
@Table(name = "USERS")
@DynamicUpdate(true)
@DynamicInsert(true)
public class User extends BaseCommonEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1884568758139444465L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "USER_NAME", length = 50, nullable = false, unique = true)
	private String name;
	
	@Column(name = "USER_PWD", length = 50, nullable = false)
	private String passwd;
	
	@Column(name = "USER_SALT", length = 100)
	private String salt; 			//加密密码的盐
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "REG_DATE")
	private Date registerDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="GROUP_ID")
	@JsonIgnore
    private Group group;			//所属部门

	
	public User(){
		
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public Date getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCredentialsSalt() {
        return name + salt;
    }
	
}
