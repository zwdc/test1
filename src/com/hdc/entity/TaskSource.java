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
 * 项目来源
 * @author ZML
 *
 */
@Entity
@Table(name = "task_source")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9183072888064840224L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "info", length = 500)
	private String info;			//来源简介
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "source_date")
	private Date sourceDate ;		//来源时间
	
	@Column(name = "file_name", length = 500)
	private String fileName;		//文件名称
	
	@Column(name = "file_path", length = 1000)
	private String filePath;		//文件路径
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "upload_date")
	private Date uploadDate ;		//上传时间
	
	@Column(name = "leader_comments", length = 2000)
	private String leaderComments;	//领导批示
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	@JsonIgnore
	private TaskInfoType taskInfoType;
	
	@Column(name = "is_delete", length = 1)
    private Integer isDelete;

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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getSourceDate() {
		return sourceDate;
	}

	public void setSourceDate(Date sourceDate) {
		this.sourceDate = sourceDate;
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

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getLeaderComments() {
		return leaderComments;
	}

	public void setLeaderComments(String leaderComments) {
		this.leaderComments = leaderComments;
	}

	public TaskInfoType getTaskInfoType() {
		return taskInfoType;
	}

	public void setTaskInfoType(TaskInfoType taskInfoType) {
		this.taskInfoType = taskInfoType;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
