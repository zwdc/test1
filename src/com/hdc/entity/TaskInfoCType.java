package com.hdc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 事项子分类
 * @author ZML
 *
 */
@Entity
@Table(name = "TASK_INFO_CTYPE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskInfoCType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9183072888064840224L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TYPE_ID")
	@JsonIgnore
	private TaskInfoType taskInfoType;

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

	public TaskInfoType getTaskInfoType() {
		return taskInfoType;
	}

	public void setTaskInfoType(TaskInfoType taskInfoType) {
		this.taskInfoType = taskInfoType;
	}		
	
}
