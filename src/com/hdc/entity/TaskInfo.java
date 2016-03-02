package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
public class TaskInfo extends BaseCommonEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1621968964712893873L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="URGENCY_ID")
	@JsonIgnore
	private Urgency urgency;		//急缓程度
	
	@Column(name = "TITLE", length = 200)
	private String title;			//标题
	
	@Column(name = "TASK_NO", length = 200)
	private String taskNo;			//文号
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_TASK_DATE")
	private Date createTaskDate;	//立项日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "ASSIGN_DATE")
	private Date assignDate;		//交办日期
	
	@Column(name = "FEEDBACK_CYCLE", length = 1)
	private Integer feedbackCycle;	//反馈周期（0.默认一次  1.每周一次  2.每月一次）
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FEEDBACE_DATE")
	private Date feedbaceDate;		//反馈时限
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "END_TASK_DATE")
	private Date endTaskDate;		//办结时限
	
	@Column(name = "CONTACTS", length = 30)
	private String contacts;		//联系人
	
	@Column(name = "CONTACTS_PHONE", length = 30)
	private String contactsPhone;	//联系电话
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="HOST_GROUP_ID")
	@JsonIgnore
	private Group hostGroup;		//主办单位
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="HOST_USER_ID")
	@JsonIgnore
	private User hostUser;			//主办人
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSISTANT_GROUP_ID")
	@JsonIgnore
	private Group assistantGroup;	//协办单位
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSISTANT_USER_ID")
	@JsonIgnore
	private User assistantUser;		//协办人
	
	@Column(name = "TASK_CONTENT", length = 2000)
	private String taskContent;		//事项内容
	
	@Column(name = "LEAD_COMMENTS", length = 2000)
	private String leadComments;	//领导批示
	
	@Column(name = "SUGGESTION", length = 2000)
	private String suggestion;		//拟办意见
	
	@Column(name = "FILE_NAME", length = 500)
	private String fileName;		//文件名称
	
	@Column(name = "FILE_PATH", length = 1000)
	private String filePath;		//文件路径
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPLOAD_DATE")
	private Date uploadDate ;		//上传时间
	
	@Column(name = "SUPERVISOR", length = 5)
	private Integer supervisor;		//督办专员 id
	
	@Column(name = "TASK_TYPE", length = 1)
	private Integer taskType;		//文件类型 （1.省政府文件  2.国务院文件  3.其他文件）
	
	@Column(name = "STATUS", length = 30)
	private String status;			//状态(0.待签收  1.办理中  2.已办结) TaskInfoStatus
	
	@Column(name = "ACT_TASK_ID", length = 64)
	private String actTaskId;		//activiti中任务的id  完成任务用(监听器每到到一个用户任务，则更新此id)

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

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public Date getCreateTaskDate() {
		return createTaskDate;
	}

	public void setCreateTaskDate(Date createTaskDate) {
		this.createTaskDate = createTaskDate;
	}

	public Urgency getUrgency() {
		return urgency;
	}

	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}

	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	public Integer getFeedbackCycle() {
		return feedbackCycle;
	}

	public void setFeedbackCycle(Integer feedbackCycle) {
		this.feedbackCycle = feedbackCycle;
	}

	public Date getFeedbaceDate() {
		return feedbaceDate;
	}

	public void setFeedbaceDate(Date feedbaceDate) {
		this.feedbaceDate = feedbaceDate;
	}

	public Date getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate(Date endTaskDate) {
		this.endTaskDate = endTaskDate;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public Group getHostGroup() {
		return hostGroup;
	}

	public void setHostGroup(Group hostGroup) {
		this.hostGroup = hostGroup;
	}

	public Group getAssistantGroup() {
		return assistantGroup;
	}

	public void setAssistantGroup(Group assistantGroup) {
		this.assistantGroup = assistantGroup;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public String getLeadComments() {
		return leadComments;
	}

	public void setLeadComments(String leadComments) {
		this.leadComments = leadComments;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Integer supervisor) {
		this.supervisor = supervisor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public User getHostUser() {
		return hostUser;
	}

	public void setHostUser(User hostUser) {
		this.hostUser = hostUser;
	}

	public User getAssistantUser() {
		return assistantUser;
	}

	public void setAssistantUser(User assistantUser) {
		this.assistantUser = assistantUser;
	}

	public String getActTaskId() {
		return actTaskId;
	}

	public void setActTaskId(String actTaskId) {
		this.actTaskId = actTaskId;
	}
	
}
