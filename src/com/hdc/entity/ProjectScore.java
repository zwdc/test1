package com.hdc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * 项目分数表
 * 记录每个项目每次减分的原因和时间，每次加分的原因和时间
 * @author Administrator
 *
 */
@Entity
@Table(name="project_score")
public class ProjectScore {
	
	public ProjectScore(Project project,String reason,Integer score) {
		this.project=project;
		this.reason=reason;
		this.score=score;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	//不需要通过分数查项目信息，所以这里不做ManyToOne的映射
	@JoinColumn(name="project_id")
	private Project project;
	
	@Column(name = "score", length = 1)
	private Integer score;
	
	@Column(name = "reason", length = 50)
	private String reason;

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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
