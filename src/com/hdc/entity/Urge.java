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
 * 催办
 * @author zhao
 *
 */
@Entity
@Table(name = "URGE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Urge extends BaseCommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8530589958991100023L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 6, nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TASK_INFO")
	@JsonIgnore
	private TaskInfo taskInfo;
	
	@Column(name = "URGE_CONTENT", length = 1000)
	private String urgeContent;		//催办内容
	
	@Column(name = "CONTACTS", length = 30)
	private String contacts;		//联系人
	
	@Column(name = "CONTACTS_PHONE", length = 20)
	private String contactsPhone;	//联系电话
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "URGE_DATE")
	private Date urgeDate;			//催办日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "HANDLE_DATE")
	private Date handleDate;		//办理时限

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

	public String getUrgeContent() {
		return urgeContent;
	}

	public void setUrgeContent(String urgeContent) {
		this.urgeContent = urgeContent;
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

	public Date getUrgeDate() {
		return urgeDate;
	}

	public void setUrgeDate(Date urgeDate) {
		this.urgeDate = urgeDate;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}
	
}
