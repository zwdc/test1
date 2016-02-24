package com.hdc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 职位权限
 * @author ZML
 *
 */
@Entity
@Table(name = "ROLE_RESOURCE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class RoleAndResource implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7497479604698543018L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_ROLE_RESOURCE_ID", allocationSize = 1)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
    private Integer id;

    @Column(name = "role_id", length = 5, nullable = false)
    private Integer roleId;

    @Column(name = "resource_id", length = 5, nullable = false)
    private Integer resourceId;

    public RoleAndResource() {
    	
    }
    
    public RoleAndResource(Integer roleId, Integer resourceId) {
    	this.roleId = roleId;
    	this.resourceId = resourceId;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
    
}
