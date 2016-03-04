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
 * 事项分类
 * @author ZML
 *
 */
@Entity
@Table(name = "TASK_INFO_TYPE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskInfoType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2253823390134237808L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;

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
	
}
