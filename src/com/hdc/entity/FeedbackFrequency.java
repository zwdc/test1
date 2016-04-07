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
 * 反馈频度
 * @author ZML
 *
 */
@Entity
@Table(name = "FEEDBACK_FREQUENCY")
@DynamicUpdate(true)
@DynamicInsert(true)
public class FeedbackFrequency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2538831055961042276L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "name", length = 200)
	private String name;			//频度名称
	
	@Column(name = "type", length = 1)
	private Integer type;			// 1.单次任务  2.每周任务  3.每月任务
		
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "single_task")
	private Date singleTask;		//单次任务日期
	
	@Column(name = "weekly_task", length = 20)
	private String weeklyTask;		//每周任务(0周日 1周一 2周二...6周六)
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_start_time")
	private Date weeklyStartTime;	//每周开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_end_time")
	private Date weeklyEndTime;		//每周结束时间
	
	@Column(name = "monthly_task", length = 25)
	private String monthlyTask;		//每月任务
	
	@Column(name = "monthly_start_day", length = 2)
	private Integer monthlyStartDay;	//每月开始日期(天)
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_start_date")
	private Date monthlyStartTime;	//每周开始时间
	
	@Column(name = "monthly_end_day", length = 2)
	private Integer monthlyEndDay;	//每月结束日期(天)
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_end_date")
	private Date monthlyEndTime;		//每周结束时间
	
	@Column(name = "is_delete", length = 1)
	private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getSingleTask() {
		return singleTask;
	}

	public void setSingleTask(Date singleTask) {
		this.singleTask = singleTask;
	}

	public String getWeeklyTask() {
		return weeklyTask;
	}

	public void setWeeklyTask(String weeklyTask) {
		this.weeklyTask = weeklyTask;
	}

	public String getMonthlyTask() {
		return monthlyTask;
	}

	public void setMonthlyTask(String monthlyTask) {
		this.monthlyTask = monthlyTask;
	}
	
	public Integer getMonthlyStartDay() {
		return monthlyStartDay;
	}

	public void setMonthlyStartDay(Integer monthlyStartDay) {
		this.monthlyStartDay = monthlyStartDay;
	}

	public Integer getMonthlyEndDay() {
		return monthlyEndDay;
	}

	public void setMonthlyEndDay(Integer monthlyEndDay) {
		this.monthlyEndDay = monthlyEndDay;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getWeeklyStartTime() {
		return weeklyStartTime;
	}

	public void setWeeklyStartTime(Date weeklyStartTime) {
		this.weeklyStartTime = weeklyStartTime;
	}

	public Date getWeeklyEndTime() {
		return weeklyEndTime;
	}

	public void setWeeklyEndTime(Date weeklyEndTime) {
		this.weeklyEndTime = weeklyEndTime;
	}

	public Date getMonthlyStartTime() {
		return monthlyStartTime;
	}

	public void setMonthlyStartTime(Date monthlyStartTime) {
		this.monthlyStartTime = monthlyStartTime;
	}

	public Date getMonthlyEndTime() {
		return monthlyEndTime;
	}

	public void setMonthlyEndTime(Date monthlyEndTime) {
		this.monthlyEndTime = monthlyEndTime;
	}
	
}
