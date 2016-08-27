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
 * 反馈延期表
 * @author zhao
 *
 */
@Entity
@Table(name = "FEEDBACK_DELAY")
@DynamicUpdate(true)
@DynamicInsert(true)
public class FeedbackDelay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6691487802724748192L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="feedback_id")
	@JsonIgnore
	private FeedbackRecord feedback;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "delay_date")
	private Date delayDate;					// 延期结束时间
	
	@Column(name = "delay_reason", length = 4000)
	private String delayReason;				// 申请延期的原因                    
	
	@Column(name = "status", length = 30)
	private String status;					// 审批状态

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FeedbackRecord getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedbackRecord feedback) {
		this.feedback = feedback;
	}

	public Date getDelayDate() {
		return delayDate;
	}

	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
