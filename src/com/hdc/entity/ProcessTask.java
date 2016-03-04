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
public class ProcessTask extends BaseCommonEntity implements Serializable {

	private static final long serialVersionUID = -3175127942643941761L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "USER_ID", length = 5)
	private Integer user_id;			//申请人id
	
	@Column(name = "USER_NAME", length = 50)
	private String user_name;			//申请人姓名
	
	@Column(name = "TITLE", length = 500)
	private String title;				//申请标题
	
	@Column(name = "URL", length = 300)
	private String url;					//待处理任务的url
	
	@Column(name = "BUSINESS_TYPE", length = 50)
	private String businessType;		//业务类型（1重要文件、2重要会议、3政府工作报告、4领导批示）
	
	@Column(name = "BUSINESS_FORM", length = 50)
	private String businessForm;		//业务表单类型(1国务院文件、省政府文件、市政府文件. 2会议下分类 3工作报告分类...) {@see Constants - BusinessForm}
	
	@Column(name = "BUSINESS_OPERATION", length = 50)
	private String businessOperation;	//业务类型操作（添加、修改、审批）	
	
	@Column(name = "BUSINESS_KEY", length = 15)
	private Integer businessKey;		//业务id
	
	@Column(name = "PROJECT_ID", length = 15)
	private Long projectId;				//项目id（以后应该会用到，根据项目id查出所有的流程）
	
	@Column(name = "PROJECT_NAME", length = 50)
	private String projectName;			//项目名称
	
	@Column(name = "TASK_ID", length = 64)
	private String taskId;				//任务id
	
	@Column(name = "EXECUTION_ID", length = 64)
	private String executionId;			//执行流id
	
	@Column(name = "PROC_INST_ID", length = 64)
	private String processInstanceId;//流程实例id
	
	@Column(name = "TASK_NAME", length = 300)
	private String taskName;
	
	@Column(name = "ASSIGN", length = 50)
	private String assign;				//任务处理人
	
	@Column(name = "OWNER", length = 50)
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
    	this.user_id = userId;
    	this.taskId = taskId;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getBusinessOperation() {
		return businessOperation;
	}

	public void setBusinessOperation(String businessOperation) {
		this.businessOperation = businessOperation;
	}

	public Integer getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(Integer businessKey) {
		this.businessKey = businessKey;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getBusinessForm() {
		return businessForm;
	}

	public void setBusinessForm(String businessForm) {
		this.businessForm = businessForm;
	}

	public HistoricTaskInstance getHistoricTaskInstance() {
		return historicTaskInstance;
	}

	public void setHistoricTaskInstance(HistoricTaskInstance historicTaskInstance) {
		this.historicTaskInstance = historicTaskInstance;
	}

}
