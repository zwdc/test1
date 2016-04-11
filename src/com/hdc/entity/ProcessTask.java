package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.activiti.engine.history.HistoricTaskInstance;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 待办任务
 * @author ZML
 *
 */

@Entity
@Table(name = "PROCESS_TASK")
@DynamicUpdate(true)
@DynamicInsert(true)
public class ProcessTask extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3175127942643941761L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "apply_user_id", length = 5)
	private Integer applyUserId;			//申请人id
	
	@Column(name = "apply_user_name", length = 50)
	private String applyUserName;			//申请人姓名
	
	@Column(name = "title", length = 500)
	private String title;				//申请标题
	
	@Column(name = "url", length = 300)
	private String url;					//待处理任务的url
	
	@Column(name = "task_info_type", length = 150)
	private String taskInfoType;
	
	/*@Column(name = "BUSINESS_TYPE", length = 50)
	private String businessType;		//业务类型 从
	
	@Column(name = "BUSINESS_FORM", length = 50)
	private String businessForm;		//业务表单类型(1国务院文件、省政府文件、市政府文件. 2会议下分类 3工作报告分类...) {@see Constants - BusinessForm}
	
	@Column(name = "BUSINESS_OPERATION", length = 50)
	private String businessOperation;	//业务类型操作（添加、修改、审批）	
	
	@Column(name = "business_key", length = 15)
	private Integer businessKey;		//业务id
*/	
	@Column(name = "task_info_id", length = 10)
	private Integer taskInfoId;			//任务id
	
	@Column(name = "project_id", length = 10)
	private Integer projectId;			//项目id（以后应该会用到，根据项目id查出所有的流程）
	
	@Column(name = "task_id", length = 64)
	private String taskId;				//任务id（activiti task id）
	
	@Column(name = "execution_id", length = 64)
	private String executionId;			//执行流id
	
	@Column(name = "pro_inst_id", length = 64)
	private String processInstanceId;//流程实例id
	
	@Column(name = "task_name", length = 300)
	private String taskName;
	
	@Column(name = "assign", length = 5)
	private String assign;				//任务处理人
	
	@Column(name = "owner", length = 5)
	private String owner;				//任务执行人
	
	@Transient
	private String taskDefinitionKey;
	
	@Transient
	private String processDefinitionId;
	
	@Transient
	private String processDefinitionKey;
	
	@Transient
	private HistoricTaskInstance historicTaskInstance;
	
	@Transient
	private Boolean supended;			//是否挂起
	
	@Transient
	private Integer version;			//流程版本号
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Transient
	private Date taskCreateDate;
	
    public ProcessTask() {
    	
    }
    
    public ProcessTask(Integer userId, String taskId) {
    	this.applyUserId = userId;
    	this.taskId = taskId;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
	public Integer getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Integer applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getTaskInfoType() {
		return taskInfoType;
	}

	public void setTaskInfoType(String taskInfoType) {
		this.taskInfoType = taskInfoType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}

	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	
	
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}
	
	public Boolean getSupended() {
		return supended;
	}

	public void setSupended(Boolean supended) {
		this.supended = supended;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public Date getTaskCreateDate() {
		return taskCreateDate;
	}

	public void setTaskCreateDate(Date taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}
	
	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public HistoricTaskInstance getHistoricTaskInstance() {
		return historicTaskInstance;
	}

	public void setHistoricTaskInstance(HistoricTaskInstance historicTaskInstance) {
		this.historicTaskInstance = historicTaskInstance;
	}

	public Integer getTaskInfoId() {
		return taskInfoId;
	}

	public void setTaskInfoId(Integer taskInfoId) {
		this.taskInfoId = taskInfoId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
