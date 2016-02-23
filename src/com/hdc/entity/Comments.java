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
	@Column(name = "ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "USER_ID", length = 5)
	private String userId;		//评论人id
	
	@Column(name = "USER_NAME", length = 50)
	private String userName;	// 评论人
	
	@Column(name = "CONTENT", length = 1000)
	private String content;		// 评论内容
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "TIME")
	private Date time;			// 评论时间
	
	@Column(name = "BUSINESS_KEY", length = 15)
	private Long businessKey;			//业务id
	
	@Column(name="BUSINESS_TYPE", length = 50)
	private String businessType; 		//业务类型
	
	@Column(name = "BUSINESS_FORM", length = 50)
	private String businessForm;	//业务表单类型（立项表、销售审批表、采购审批表、合同、开票、出库单等）,区分业务表单类型
	
	@Column(name = "PROC_INST_ID", length = 64)
	private String processInstanceId;	//流程实例id
	
	public Comments() {
		
	}
	
	public Comments(Long businessKey, String businessType, String businessForm) {
		this.businessKey = businessKey;
		this.businessType = businessType;
		this.businessForm = businessForm;
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
	public Long getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(Long businessKey) {
		this.businessKey = businessKey;
	}
	public String getBusinessForm() {
		return businessForm;
	}
	public void setBusinessForm(String businessForm) {
		this.businessForm = businessForm;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
