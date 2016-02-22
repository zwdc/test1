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
 * 角色（职位）管理
 * @author ZML
 *
 */
@Entity
@Table(name = "ROLE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Role extends BaseCommonEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6442701951520505529L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_ROLE_ID", allocationSize = 1)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;			//职位名称（采购经理、采购员、商务总监等）
	
	@Column(name = "TYPE", length = 50, nullable = false)
	private String type;			//职位标识（admin、CEO、CBO 等）
	
	@Column(name = "REMARK", length = 500)
	private String remark;			//备注
	
	@OneToMany(mappedBy="role")
	@JsonIgnore
    private Set<User> user = new HashSet<User>();
	
	public Role() {
		
	}
	
	public Role(Integer id) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
