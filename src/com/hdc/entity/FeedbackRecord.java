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
 * 反馈记录
 * @author zhao
 *
 */
@Entity
@Table(name = "FEEDBACK_RECORD")
@DynamicUpdate(true)
@DynamicInsert(true)
public class FeedbackRecord extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4197246964734590658L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TASK_INFO")
	@JsonIgnore
	private TaskInfo taskInfo;
	
	@Column(name = "ORIGINAL_PERSON", length = 30)
	private String originalPerson;		//原始起草人
	
	@Column(name = "PHONE", length = 30)
	private String phone;				//手机
	
	@Column(name = "WORK_PHONE", length = 30)
	private String workPhone;			//办公电话
	
	@Column(name = "OFFICES", length = 100)
	private String offices;				//处/科室
	
	@Column(name = "DUTY_OF", length = 50)
	private String dutyOf;				//职务
	
	@Column(name = "EMAIL", length = 100)
	private String email;				//邮箱
	
	@Column(name = "CONTACTS", length = 100)
	private String contacts;			//联系人
	
	@Column(name = "CONTACTS_PHONE", length = 100)
	private String contactsPhone;		//联系电话
	
	@Column(name = "CONTENT", length = 2000)
	private String content;				//落实情况
	
	@Column(name = "REPLENISH", length = 2000)
	private String replenish;			//补充建议（续报）
	
	@Column(name = "TYPE", length = 1)
	private Integer type;				//反馈类型（0.正常反馈  1.续报）
	
	@Column(name = "FILE_NAME", length = 500)
	private String fileName;			//附件名称
	
	@Column(name = "FILE_PATH", length = 1000)
	private String filePath;			//附件路径
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPLOAD_DATE")
	private Date uploadDate ;		//上传时间
	
	@Column(name = "STATUS", length = 30)
	private String status;				//反馈情况（反馈中、已退回、已采用）
	
	@Column(name = "IS_DELAY", length = 1)
	private Integer isDelay;				//是否迟报（1.是、0.否）

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getOriginalPerson() {
		return originalPerson;
	}

	public void setOriginalPerson(String originalPerson) {
		this.originalPerson = originalPerson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getOffices() {
		return offices;
	}

	public void setOffices(String offices) {
		this.offices = offices;
	}

	public String getDutyOf() {
		return dutyOf;
	}

	public void setDutyOf(String dutyOf) {
		this.dutyOf = dutyOf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(Integer isDelay) {
		this.isDelay = isDelay;
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

	public String getReplenish() {
		return replenish;
	}

	public void setReplenish(String replenish) {
		this.replenish = replenish;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

}
