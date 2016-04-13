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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 反馈表
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
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_id")
	@JsonIgnore
	private Project project;
	
	@OneToMany(mappedBy = "fdRecord",fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	@JsonIgnore
	private Set<FeedbackAtt> fdaList=new HashSet<FeedbackAtt>();
	
	@Column(name = "work_plan", length = 2000)
	private String workPlan;				//阶段工作计划
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "feedback_start_date")
	private Date feedbackStartDate;			//反馈开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "feedback_end_date")
	private Date feedbackEndDate;			//反馈结束时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "feedback_date")
	private Date feedbackDate;				//反馈日期
	
	@Column(name = "situation", length = 4000)
	private String situation ;				//落实情况
	
	@Column(name = "problems", length = 4000)
	private String problems ;				//存在问题
	
	@Column(name = "solutiona", length = 4000)
	private String solutions;				//解决措施
	
	@Column(name = "type", length = 1)
	private Integer type;					//反馈类型（0.正常反馈  1.续报）
	
	@Column(name = "task_schedule", length = 3)
	private Integer taskSchedule;			//任务进度
	
	@Column(name = "suggestion", length = 1000)
	private String suggestion;				//督导意见
	
	@Column(name = "status", length = 30)
	private String status;					//反馈情况（反馈中 RUNNING、已退回 FAIL、已采用 SUCCESS）
	
	@Column(name = "refuse_count", length = 2)
	private Integer refuseCount;			//退回次数
	
	@Column(name = "warning_level", length = 2)
	private Integer warningLevel;			//预警级别
	
	@Column(name = "delay_count", length = 2)
	private Integer delayCount;				//延期次数
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "delay_date")
	private Date delayDate;					//延期结束时间
	
	@Column(name = "is_delay", length = 1)
	private Integer isDelay;				//是否迟报（1.是、0.否）

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getWorkPlan() {
		return workPlan;
	}

	public void setWorkPlan(String workPlan) {
		this.workPlan = workPlan;
	}

	public Date getFeedbackStartDate() {
		return feedbackStartDate;
	}

	public void setFeedbackStartDate(Date feedbackStartDate) {
		this.feedbackStartDate = feedbackStartDate;
	}

	public Set<FeedbackAtt> getFdaList() {
		return fdaList;
	}

	public void setFdaList(Set<FeedbackAtt> fdaList) {
		this.fdaList = fdaList;
	}

	public Date getFeedbackEndDate() {
		return feedbackEndDate;
	}

	public void setFeedbackEndDate(Date feedbackEndDate) {
		this.feedbackEndDate = feedbackEndDate;
	}

	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}

	public String getSolutions() {
		return solutions;
	}

	public void setSolutions(String solutions) {
		this.solutions = solutions;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTaskSchedule() {
		return taskSchedule;
	}

	public void setTaskSchedule(Integer taskSchedule) {
		this.taskSchedule = taskSchedule;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getRefuseCount() {
		return refuseCount;
	}

	public void setRefuseCount(Integer refuseCount) {
		this.refuseCount = refuseCount;
	}

	public Integer getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(Integer warningLevel) {
		this.warningLevel = warningLevel;
	}

	public Integer getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(Integer delayCount) {
		this.delayCount = delayCount;
	}

	public Date getDelayDate() {
		return delayDate;
	}

	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}

	public Integer getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(Integer isDelay) {
		this.isDelay = isDelay;
	}

}
