package com.hdc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 职位权限
 * @author ZML
 *
 */
@Entity
@Table(name = "GROUP_RESOURCE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class GroupAndResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8494475256149728305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
    private Integer id;

    @Column(name = "group_id", length = 5, nullable = false)
    private Integer groupId;

    @Column(name = "resource_id", length = 5, nullable = false)
    private Integer resourceId;

    public GroupAndResource() {
    	
    }
    
    public GroupAndResource(Integer groupId, Integer resourceId) {
    	this.groupId = groupId;
    	this.resourceId = resourceId;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
    
}
