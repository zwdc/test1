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
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * 组织机构、承办单位
 * @author ZML
 *
 */

@Entity
@Table(name = "GROUPS")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Group extends BaseCommonEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5933624941063844429L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;
	
	@Column(name = "TYPE", length = 50, nullable = false)
	private String type;

	@OneToMany(mappedBy="group")
	@JsonIgnore
    private Set<User> user = new HashSet<User>();
    
	public Group(){
		
	}
	
	public Group(Integer id){
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

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
