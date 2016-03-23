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
@Table(name = "FEEDBACK_RECORD")
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
		
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "single_task")
	private Date singleTask;		//单次任务日期
	
	@Column(name = "name", length = 20)
	private String weeklyTask;		//每周任务(0周日 1周一 2周二...6周六)
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_start_date")
	private Date weeklyStartDate;	//每周开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@Column(name = "weekly_end_date")
	private Date weeklyEndDate;		//每周结束时间
	
	@Column(name = "name", length = 25)
	private String monthlyTask;		//每月任务
	
	@Column(name = "monthly_start_date", length = 2)
	private Integer monthlyStartDate;	//每月开始日期(天)
	
	@Column(name = "monthly_end_date", length = 2)
	private Integer monthlyEndDate;	//每月结束日期(天)

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

	public Date getWeeklyStartDate() {
		return weeklyStartDate;
	}

	public void setWeeklyStartDate(Date weeklyStartDate) {
		this.weeklyStartDate = weeklyStartDate;
	}

	public Date getWeeklyEndDate() {
		return weeklyEndDate;
	}

	public void setWeeklyEndDate(Date weeklyEndDate) {
		this.weeklyEndDate = weeklyEndDate;
	}

	public String getMonthlyTask() {
		return monthlyTask;
	}

	public void setMonthlyTask(String monthlyTask) {
		this.monthlyTask = monthlyTask;
	}

	public Integer getMonthlyStartDate() {
		return monthlyStartDate;
	}

	public void setMonthlyStartDate(Integer monthlyStartDate) {
		this.monthlyStartDate = monthlyStartDate;
	}

	public Integer getMonthlyEndDate() {
		return monthlyEndDate;
	}

	public void setMonthlyEndDate(Integer monthlyEndDate) {
		this.monthlyEndDate = monthlyEndDate;
	}
	
}
