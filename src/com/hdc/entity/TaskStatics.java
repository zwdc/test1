package com.hdc.entity;

import java.io.Serializable;

/**
 * 统计完成数实体类
 * @author Qdj
 *
 */
public class TaskStatics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  name;  //任务分类名称
	private Long endNum; //完成的任务数         必须是Long类型。select count()返回的是Long类型
	private Long unendNum; //未完成的任务数
	
	
	
	public TaskStatics() {

	}

	public TaskStatics(String name, Long endNum, Long unendNum) {
		this.name = name;
		this.endNum = endNum;
		this.unendNum = unendNum;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getEndNum() {
		return endNum;
	}
	public void setEndNum(Long endNum) {
		this.endNum = endNum;
	}
	public Long getUnendNum() {
		return unendNum;
	}
	public void setUnendNum(Long unendNum) {
		this.unendNum = unendNum;
	}

	
}
