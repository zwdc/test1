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

import com.hdc.util.Constants.BusinessForm;

/**
 * 评论
 * @author ZML
 *
 */

@Entity
@Table(name = "COMMENTS")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Comments implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1831626180362549079L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "user_id", length = 5)
	private String userId;		//评论人id
	
	@Column(name = "user_name", length = 50)
	private String userName;	// 评论人
	
	@Column(name = "content", length = 1000)
	private String content;		// 评论内容
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "time")
	private Date time;			// 评论时间
	
	@Column(name = "businessKey", length = 10)
	private Integer businessKey;		//业务id
	
	/**
	 * @see BusinessForm
	 */
	@Column(name="business_form", length = 20)
	private String businessForm; 		//业务表单
	
	public Comments() {
		
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(Integer businessKey) {
		this.businessKey = businessKey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBusinessForm() {
		return businessForm;
	}
	public void setBusinessForm(String businessForm) {
		this.businessForm = businessForm;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
