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
 * 用户任务表(动态分配审批人员)
 * @author ZML
 *
 */
@Entity
@Table(name = "USER_TASK")
@DynamicUpdate(true)
@DynamicInsert(true)
public class UserTask implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2100799566256858467L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "PROC_DEF_KEY", length = 100)
	private String procDefKey;		//com.yzky.vacation
	
	@Column(name = "PROC_DEF_NAME", length = 100)
	private String procDefName;		//请假申请
	
	@Column(name = "TASK_DEF_KEY", length = 100)
	private String taskDefKey;		//hrAudti
	
	@Column(name = "TASK_NAME", length = 100)
	private String taskName;		//人事审批
	
	@Column(name = "TASK_TYPE", length = 50)
	private String taskType;		//1.assignee.受理人(唯一) 1.candidateUser候选人(多个) 2.candidateGroup候选组（多个）
	
	@Column(name = "CANDIDATE_NAME", length = 200)
	private String candidate_name; 	//人或候选人或组的名称
	
	@Column(name = "CANDIDATE_IDS", length = 100)
	private String candidate_ids;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_DATE")
	private Date createDate ;		//创建时间
	
	@Column(name = "IS_DELETE", length = 2)
    private Integer isDelete;
	
	public String getProcDefKey() {
		return procDefKey;
	}
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	public String getProcDefName() {
		return procDefName;
	}
	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCandidate_name() {
		return candidate_name;
	}
	public void setCandidate_name(String candidate_name) {
		this.candidate_name = candidate_name;
	}
	public String getCandidate_ids() {
		return candidate_ids;
	}
	public void setCandidate_ids(String candidate_ids) {
		this.candidate_ids = candidate_ids;
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
	
}
