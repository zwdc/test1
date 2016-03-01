package com.hdc.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Comments;
import com.hdc.entity.Page;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.User;
import com.hdc.process.task.TaskCommand.DeleteActiveTaskCmd;
import com.hdc.process.task.TaskCommand.RevokeTaskCmd;
import com.hdc.process.task.TaskCommand.StartActivityCmd;
import com.hdc.service.ICommentsService;
import com.hdc.service.IProcessService;
import com.hdc.service.IUserService;
import com.hdc.util.BeanUtils;
import com.hdc.util.UserUtil;
import com.hdc.workflow.WorkflowService;

/**
 * 流程相关Service
 * @author zml
 *
 */
@Service
public class ProcessServiceImpl implements IProcessService{

	private static final Logger logger = Logger.getLogger(ProcessServiceImpl.class);
	
	@Autowired
	protected RuntimeService runtimeService;
	
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected TaskService taskService;
    
    @Autowired
    protected RepositoryService repositoryService;
    
    @Autowired
    protected HistoryService historyService;
    
	@Autowired
	protected IUserService userService;
	
    @Autowired
    ProcessEngineFactoryBean processEngineFactory;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    
    @Autowired
    protected WorkflowService workflowService;
    
	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private ICommentsService commentsService;
	
    /**
     * 用户查询代办任务
     * @param user
     * @param model
     * @return
     */
	@Override
    public List<ProcessTask> findTodoTask(User user, Page<ProcessTask> page, ProcessTask processTask) throws Exception{
		//taskCandidateOrAssigned查询某个人的待办任务，包含已签收、候选任务<候选人范围和候选组范围>
 		TaskQuery taskQuery = this.taskService.createTaskQuery().taskCandidateOrAssigned(user.getId().toString());
		Integer totalSum = taskQuery.list().size();
		
		int[] pageParams = page.getPageParams(totalSum);
		//查询代办
		List<Task> tasks;
		boolean flag=processTask!=null&&((processTask.getProjectName()!=null&&!processTask.getProjectName().trim().equals(""))||(processTask.getTitle()!=null&&!processTask.getTitle().trim().equals(""))||(processTask.getBusinessForm()!=null&&!processTask.getBusinessForm().trim().equals("")));
		if(flag){
			if(processTask.getBusinessForm()!=null&&!processTask.getBusinessForm().trim().equals("")){
				tasks = taskQuery.processVariableValueLike("projectName", "%"+processTask.getProjectName()+"%").processVariableValueLike("title", "%"+processTask.getTitle()+"%").processVariableValueEquals("businessForm", processTask.getBusinessForm()).orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
			}else{
				tasks = taskQuery.processVariableValueLike("projectName", "%"+processTask.getProjectName()+"%").processVariableValueLike("title", "%"+processTask.getTitle()+"%").orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
			}
		}else{
			tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
		}
		List<ProcessTask> taskList = getUserTaskList(tasks);
		return taskList;
    } 

    /**
     * 读取已结束中的流程(admin流程管理中查看)
     *
     * @return
     */
    @Override
    public List<ProcessTask> findFinishedProcessInstances(Page<ProcessTask> page) {
        HistoricProcessInstanceQuery historQuery = historyService.createHistoricProcessInstanceQuery().finished();
    	
        Integer totalSum = historQuery.list().size();
        int[] pageParams = page.getPageParams(totalSum);
		List<HistoricProcessInstance> list = historQuery.orderByProcessInstanceEndTime().desc().listPage(pageParams[0], pageParams[1]);
		List<ProcessTask> processList = new ArrayList<ProcessTask>();
		
		for (HistoricProcessInstance historicProcessInstance : list) {
			String processInstanceId = historicProcessInstance.getId();
			List<HistoricVariableInstance> listVar = this.historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
			for(HistoricVariableInstance var : listVar){
				if("serializable".equals(var.getVariableTypeName()) && "processTask".equals(var.getVariableName())){
					ProcessTask base = (ProcessTask) var.getValue();
//					base.setHistoricProcessInstance(historicProcessInstance);
//					base.setProcessDefinition(getProcessDefinitionById(historicProcessInstance.getProcessDefinitionId()));
					processList.add(base);
					break;
				}
			}
		}
		
        return processList;
    }
	
    /**
     * 用户查看已完成任务
     */
	@Override
	public List<ProcessTask> findFinishedTaskInstances(User user, Page<ProcessTask> page) throws Exception {
		HistoricTaskInstanceQuery historQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(user.getId().toString()).finished();
		Integer totalSum = historQuery.list().size();
        int[] pageParams = page.getPageParams(totalSum);
    	List<HistoricTaskInstance> list = historQuery.orderByHistoricTaskInstanceEndTime().desc().listPage(pageParams[0], pageParams[1]);
    	List<ProcessTask> taskList = new ArrayList<ProcessTask>();
    	
    	for(HistoricTaskInstance historicTaskInstance : list){
    		ProcessTask base = new ProcessTask();
    		base.setHistoricTaskInstance(historicTaskInstance);
    		base.setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId());
    		taskList.add(base);
    		/*String processInstanceId = historicTaskInstance.getProcessInstanceId();
    		List<HistoricVariableInstance> listVar = this.historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
			for(HistoricVariableInstance var : listVar){
				if("serializable".equals(var.getVariableTypeName()) && "processTask".equals(var.getVariableName())){
					ProcessTask base = (ProcessTask) var.getValue();
					base.setHistoricTaskInstance(historicTaskInstance);
					base.setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId());
					taskList.add(base);
					break;
				}
			}*/
    	}
		return taskList;
	}
    
    /**
     * 将Task集合转为ProcessTask集合
     * @param tasks
     * @return
     */
    protected List<ProcessTask> getUserTaskList(List<Task> tasks) throws Exception{
    	List<ProcessTask> taskList = new ArrayList<ProcessTask>();
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
        	String executionId = task.getExecutionId();
            ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            if(BeanUtils.isBlank(processInstance)){
            	//如果有挂起的流程则continue
            	continue;
            }
            ProcessTask base = (ProcessTask) getVariableByExecutionId(executionId, "processTask");
            base.setTaskId(task.getId());
            base.setTaskName(task.getName());
            base.setTaskCreateDate(task.getCreateTime());
            base.setTaskDefinitionKey(task.getTaskDefinitionKey());
            base.setAssign(task.getAssignee());
            base.setOwner(task.getOwner());
            base.setExecutionId(executionId);
            base.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            base.setProcessInstanceId(processInstanceId);
            ProcessDefinition processDefinition = getProcessDefinitionById(processInstance.getProcessDefinitionId());
            base.setProcessDefinitionKey(processDefinition.getKey());
            base.setSupended(processInstance.isSuspended());
            base.setVersion(processDefinition.getVersion());
            
            taskList.add(base);
        }
    	return taskList;
    }
    
    /**
     * 查询流程定义对象
     *
     * @param processDefinitionId 流程定义ID
     * @return
     */
    @Override
    public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinition;
    }
    
    /**
     * 签收任务
     * @param user
     * @param taskId
     */
	@Override
    public void doClaim(User user, String taskId){
    	this.identityService.setAuthenticatedUserId(user.getId().toString());
        this.taskService.claim(taskId, user.getId().toString());
    }
	
    /**
     * 委派任务
     */
	@Override
	public void doDelegateTask(String userId, String taskId) throws Exception {
		//API: If no owner is set on the task, the owner is set to the current assignee of the task.
		//OWNER_（委托人）：受理人委托其他人操作该TASK的时候，受理人就成了委托人OWNER_，其他人就成了受理人ASSIGNEE_
		//assignee容易理解，主要是owner字段容易误解，owner字段就是用于受理人委托别人操作的时候运用的字段
		this.taskService.delegateTask(taskId, userId);
	}
	
	/**
	 * 转办任务
	 */
	@Override
	public void doTransferTask(String userId, String taskId) throws Exception {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task != null){
			String assign = task.getAssignee();
			this.taskService.setAssignee(taskId, userId);
			this.taskService.setOwner(taskId, assign);
		}else{
			throw new ActivitiObjectNotFoundException("任务不存在！", this.getClass());
		}
	}
	
	/**
	 * 获取评论
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@Override
    public List<Comments> getComments(String processInstanceId) throws Exception{
		// 查询一个任务所在流程的全部评论
		List<Comment> comments = this.taskService.getProcessInstanceComments(processInstanceId);
		List<Comments> commnetList = new ArrayList<Comments>();
		for(Comment comment : comments){
			User user = this.userService.getUserById(new Integer(comment.getUserId()));
			Comments vo = new Comments();
			vo.setContent(comment.getFullMessage());
			vo.setTime(comment.getTime());
			vo.setUserName(user.getName());
			commnetList.add(vo);
		}
    	return commnetList;
    }
    
    /**
     * 根据executionId获取流程参数 
     * @param executionId
     * @param variableName
     * @throws Exception
     */
	@Override
	public Object getVariableByExecutionId(String executionId, String variableName) throws Exception {
		return this.runtimeService.getVariableLocal(executionId, variableName);
	}
	
    /**
     * 根据executionId保存流程参数
     * @param executionId
     * @param variableName
     * @param object
     * @throws Exception
     */
	@Override
	public void saveVariableByExecutionId(String executionId,
			String variableName, Object object) throws Exception {
		this.runtimeService.setVariable(executionId, variableName, object);
	}
	
	/**
	 * 获取流程实例
	 */
	@Override
	public ProcessInstance getProcessInstanceById(String processInstanceId)
			throws Exception {
		return this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @return
     */
    @Override
    public InputStream getDiagram(String processInstanceId){
    	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        // 不使用spring请使用下面的两行代码
//    	ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//    	Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngineFactory.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        //通过引擎生成png图片，并标记当前节点,并把当前节点用红色边框标记出来，弊端和直接部署流程文件生成的图片问题一样-乱码！。
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
    	return imageStream;
    }
    
    /**
     * 显示图片-通过流程ID，，不带流程跟踪(没有乱码问题)
     * @param resourceType
     * @param processInstanceId
     * @return
     */
    @Override
    public InputStream getDiagramByProInstanceId_noTrace(String resourceType, String processInstanceId){
    	
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        String resourceName = "";
        if (resourceType.equals("png") || resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
    }
    
    /**
     * 显示图片-通过部署ID，不带流程跟踪(没有乱码啊问题)
     * @param resourceType
     * @param processInstanceId
     * @return
     * @throws Exception
     */
	@Override
	public InputStream getDiagramByProDefinitionId_noTrace(String resourceType,
			String processDefinitionId) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("png") || resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
	}

    /**
     * 启动流程
     */
	@Override
	public String startApproval(String startKey, String businessKey, Map<String, Object> variables) throws Exception {
		String processInstanceId = null;
		try{
			User user = UserUtil.getUserFromSession();
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中。对应ACT_HI_PROCINST 中的START_USER_ID_
			this.identityService.setAuthenticatedUserId(user.getId().toString());
			ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(startKey, businessKey, variables);
			processInstanceId = processInstance.getId();
		} catch (ActivitiException e) {
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.error("没有部署流程，请在[流程定义]中部署相应流程文件！", e);
            } else {
                logger.error("启动请假流程失败，系统内部错误！", e);
            }
            throw e;
        } catch (Exception e) {
            logger.error("启动请假流程失败：", e);
            throw e;
        }finally{
			this.identityService.setAuthenticatedUserId(null);
		}
        return processInstanceId;
	}

	/**
	 * 完成任务
	 * @param taskId    任务id
	 * @param content   评论内容
	 * @param userid    评论人id
	 * @param variables 参数
	 */
	@Override
	public String complete(String taskId, Comments comments, Map<String, Object> variables) throws Exception {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
		
		// 添加评论--意见为空时，默认“同意”。
    	if(comments != null) {
    		if(StringUtils.isEmpty(comments.getContent())) {
    			Boolean isPass = (Boolean) variables.get("isPass");
    			if(isPass) {
    				comments.setContent("同意");
    			} else {
    				comments.setContent("不同意");
    			}
    		}
    		//评论人的id  一定要写，不然查看的时候会报错，没有用户
        	this.identityService.setAuthenticatedUserId(comments.getUserId());
    		this.taskService.addComment(taskId, processInstanceId, comments.getContent());	//activiti自己的comment
    		comments.setTime(new Date());
    		this.commentsService.doAdd(comments);		
    	}
    	
    	if(DelegationState.PENDING == task.getDelegationState()){
    		// 完成委派任务
    		this.taskService.resolveTask(taskId, variables);
    	} else {
    		//正常完成任务
    		this.taskService.complete(taskId, variables);
    	}
		return processInstanceId;
	}

	@Override
	public List<ProcessInstance> listRuningProcess(Page<ProcessInstance> page) throws Exception {
		
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		int[] pageParams = page.getPageParams(processInstanceQuery.list().size());
		List<ProcessInstance> list = processInstanceQuery.orderByProcessInstanceId().desc().listPage(pageParams[0], pageParams[1]);
		return list;
	}

	@Override
	public void activateProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.activateProcessInstanceById(processInstanceId);
	}

	@Override
	public void suspendProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	/**
	 * 撤回任务
	 */
	@Override
	public Integer revoke(String historyTaskId, String processInstanceId) throws Exception {
		Command<Integer> cmd = new RevokeTaskCmd(historyTaskId, processInstanceId);
		Integer revokeFlag = this.processEngine.getManagementService().executeCommand(cmd);
		return revokeFlag;
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	@Override
	public void moveTo(String currentTaskId, String targetTaskDefinitionKey)
			throws Exception {
		TaskEntity taskEntity = (TaskEntity) this.taskService.createTaskQuery().taskId(currentTaskId).singleResult();
		moveTo(taskEntity, targetTaskDefinitionKey);
		
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	@Override
	public void moveTo(TaskEntity currentTaskEntity,
			String targetTaskDefinitionKey) throws Exception {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ((RepositoryServiceImpl)this.repositoryService).getDeployedProcessDefinition(currentTaskEntity.getProcessDefinitionId()); 
		//PvmActivity是部署时期对象，ActivityImpl是它的实现类
		ActivityImpl activity = (ActivityImpl) pde.findActivity(targetTaskDefinitionKey);

		moveTo(currentTaskEntity, activity);
	}
	
	private void moveTo(TaskEntity currentTaskEntity, ActivityImpl activity)
	{
		Command<Void> deleteCmd = new DeleteActiveTaskCmd(currentTaskEntity, "jump", true);
		Command<Void> StartCmd = new StartActivityCmd(currentTaskEntity.getExecutionId(), activity);
		this.processEngine.getManagementService().executeCommand(deleteCmd);
		this.processEngine.getManagementService().executeCommand(StartCmd);
	}

	@Override
	public Task getTaskByProcessInstanceId(String processInstanceId)
			throws Exception {
		if(StringUtils.isNotBlank(processInstanceId)) {
			return this.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		} else {
			return null;
		}
	}

}
