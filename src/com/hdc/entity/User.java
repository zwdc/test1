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
public class User implements Serializable{

	private static final long serialVersionUID = -6662232329895785824L;

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
    private Group group;			//所属承办单位
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROLE_ID")
	@JsonIgnore
	private Role role;				//所属角色（职位）
	
	@Column(name="DATE_ALL", length=1)
	private Integer allData ;			//是否有查询所有数据的权限：0无；1有
	
	@Column(name="DATE_GROUP", length=1) 
	private Integer groupData ;			//是否有查询部门数据的权限
	
//	@Column(name="data_role", length=1)
//	private Integer roleData ;			//是否有查询角色数据的权限
	
	@Column(name="DATE_SELF", length=1)
	private Integer selfData ;			//是否有查询自己数据的权限
	
	@Column(name = "IS_DELETE", length = 1)
    private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date")
	private Date createDate ;					//创建时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_date")
	private Date updateDate ;					//修改时间
	
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


	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCredentialsSalt() {
        return name + salt;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Integer getAllData() {
		return allData;
	}

	public void setAllData(Integer allData) {
		this.allData = allData;
	}

	public Integer getGroupData() {
		return groupData;
	}

	public void setGroupData(Integer groupData) {
		this.groupData = groupData;
	}

	public Integer getSelfData() {
		return selfData;
	}

	public void setSelfData(Integer selfData) {
		this.selfData = selfData;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
