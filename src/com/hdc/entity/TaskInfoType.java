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
 * 任务类型
 * @author ZML
 *
 */
@Entity
@Table(name = "task_info_type")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskInfoType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2253823390134237808L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "parentId", length = 5, nullable = false)
    private Integer parentId;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "is_delete", length = 1)
    private Integer isDelete;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
}
