package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

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
	
	@Column(name = "parentId", length = 5)
    private Integer parentId;		//父节点(没用先留着，新需求2016-03-25)
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "is_delete", length = 1)
    private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date")
	private Date createDate ;		//创建时间
	
	@Column(name = "limit_situation") 
	private Integer limitSituation; //落实情况字数限制
	
	@Column(name = "limit_problems")
	private Integer limitProblems;//存在问题字数限制
	
	@Column(name = "limit_solutions")
	private Integer limitSolutions;//解决措施字数限制

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getLimitSituation() {
		return limitSituation;
	}

	public void setLimitSituation(Integer limitSituation) {
		this.limitSituation = limitSituation;
	}

	public Integer getLimitProblems() {
		return limitProblems;
	}

	public void setLimitProblems(Integer limitProblems) {
		this.limitProblems = limitProblems;
	}

	public Integer getLimitSolutions() {
		return limitSolutions;
	}

	public void setLimitSolutions(Integer limitSolutions) {
		this.limitSolutions = limitSolutions;
	}
	
}
