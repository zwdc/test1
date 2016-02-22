package com.hdc.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 公司
 * @author ZML
 *
 */
@Entity
@Table(name = "COMPANY")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Company extends BaseCommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5648218392015027877L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_COMPANY_ID", allocationSize = 1)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;			
	
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;		//公司名称
	
	@Column(name = "ADDRESS", length = 200)
	private String address;		//地址
	
	@Column(name = "PHONE", length = 15)
	private String phone;		//联系电话
	
	@Column(name = "NOTE", length = 500)
	private String note;		//说明
	
	@OneToMany(mappedBy="company")	//mappedBy="company": 指明User类为双向关系维护端，负责外键的更新。即要将company的数据,赋给user
	@JsonIgnore
    private Set<User> user = new HashSet<User>();
	
	public Company() {
		
	}
	
	public Company(Integer id) {
		this.id = id;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
