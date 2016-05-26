package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 事项
 * @author ZML
 *
 */
@Entity
@Table(name = "TASK_INFO")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskInfo extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1621968964712893873L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "title", length = 200)
	private String title;			//任务标题
	
	@Column(name = "info", length = 200)
	private String info;			//任务简称
	
	@Column(name = "task_info", length = 2000)
	private String taskInfo;		//任务说明
	
	@Column(name = "host_group", length = 50)
	private String hostGroup;		//主办单位（多个）
	
	@Column(name = "assistant_group", length = 1000)
	private String assistantGroup;	//协办单位(手写单位名称，不参与业务)
	
	@Column(name = "urgency", length = 1)
	private Integer urgency;		//急缓程度
	
	@Column(name = "status", length = 30)
	private String status;			//状态(待签收  办理中  已办结) TaskInfoStatus

	@OneToMany(mappedBy = "taskInfo",fetch = FetchType.LAZY)//不做级联删除或者加载
	@JsonIgnore
	@OrderBy(clause="id ASC")
	private Set<Project> projectList=new HashSet<Project>(); //反馈表
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_task_date")
	private Date createTaskDate;	//立项日期（开始日期）
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_task_date")
	private Date endTaskDate;		//结束时限
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_handle_date")
	private Date endHandleDate;		//办结日期(最后一个人办结时间)
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "claim_limit_date")
	private Date claimLimitDate;	//签收时限
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="task_source")
	@JsonIgnore
	private TaskSource taskSource;	//任务来源
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fb_frequency")
	@JsonIgnore
	private FeedbackFrequency fbFrequency; //反馈频度
	
	@Column(name = "remark", length = 2000)
	private String remark;
	
	public TaskInfo() {
		
	}
	
	public TaskInfo(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateTaskDate() {
		return createTaskDate;
	}

	public void setCreateTaskDate(Date createTaskDate) {
		this.createTaskDate = createTaskDate;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public Date getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate(Date endTaskDate) {
		this.endTaskDate = endTaskDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHostGroup() {
		return hostGroup;
	}

	public void setHostGroup(String hostGroup) {
		this.hostGroup = hostGroup;
	}

	public String getAssistantGroup() {
		return assistantGroup;
	}

	public void setAssistantGroup(String assistantGroup) {
		this.assistantGroup = assistantGroup;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}

	public Date getEndHandleDate() {
		return endHandleDate;
	}

	public void setEndHandleDate(Date endHandleDate) {
		this.endHandleDate = endHandleDate;
	}

	public Date getClaimLimitDate() {
		return claimLimitDate;
	}

	public void setClaimLimitDate(Date claimLimitDate) {
		this.claimLimitDate = claimLimitDate;
	}

	public TaskSource getTaskSource() {
		return taskSource;
	}

	public void setTaskSource(TaskSource taskSource) {
		this.taskSource = taskSource;
	}

	public FeedbackFrequency getFbFrequency() {
		return fbFrequency;
	}

	public void setFbFrequency(FeedbackFrequency fbFrequency) {
		this.fbFrequency = fbFrequency;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(Set<Project> projectList) {
		this.projectList = projectList;
	}
	
}
