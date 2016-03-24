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
	
	@Column(name = "STATUS", length = 30)
	private String status;			//状态(待签收  办理中  已办结) TaskInfoStatus

	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_task_date")
	private Date createTaskDate;	//立项日期
	
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
	
/*	@Column(name = "FEEDBACK_CYCLE", length = 1)
	private Integer feedbackCycle;	//反馈周期（0.默认一次  1.每周一次  2.每月一次）
*/	
/*	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FEEDBACE_DATE")
	private Date feedbaceDate;		//反馈时限
*/	
/*	@Column(name = "CONTACTS", length = 30)
	private String contacts;		//联系人
	
	@Column(name = "CONTACTS_PHONE", length = 30)
	private String contactsPhone;	//联系电话
	
	@Column(name = "LEADER_SHIP", length = 20)
	private String leadership;		//领导
	
	
	@Column(name = "HOST_USER_ID", length = 50)
	private String hostUser;		//主办人（多个）
	
	@Column(name = "ASSISTANT_USER_ID", length = 50)
	private String assistantUser;	//协办人
	
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
	
	@Column(name = "ACT_TASK_ID", length = 64)
	private String actTaskId;		//activiti中任务的id  完成任务用(监听器每到到一个用户任务，则更新此id)

	@OneToMany(mappedBy="taskInfo")
	@JsonIgnore
    private Set<FeedbackRecord> feedBack = new HashSet<FeedbackRecord>();
    
	@OneToMany(mappedBy="taskInfo")
	@JsonIgnore
	private Set<Urge> urge = new HashSet<Urge>();*/
	
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

}
