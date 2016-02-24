package com.hdc.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.hdc.entity.Comments;
import com.hdc.entity.Page;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.User;

public interface IProcessService {
	
	/**
	 * 启动流程
	 * @param startKey	
	 * @param businessKey
	 * @param variables
	 * @throws Exception
	 */
	public String startApproval(String startKey, String businessKey, Map<String, Object> variables) throws Exception;
	
	/**
	 * 查询代办任务
	 * @param user
	 * @param page
	 * @throws Exception
	 */
	public List<ProcessTask> findTodoTask(User user, Page<ProcessTask> page,ProcessTask processTask) throws Exception;
	
    /**
     * 签收任务
     * @param user
     * @param taskId
     * @throws Exception
     */
    public void doClaim(User user, String taskId) throws Exception;
    
    /**
     * 委派任务
     * @param userId
     * @param taskId
     * @throws Exception
     */
    public void doDelegateTask(String userId, String taskId) throws Exception;
    
    /**
     * 转办任务
     * @param userId
     * @param taskId
     * @throws Exception
     */
    public void doTransferTask(String userId, String taskId) throws Exception;
    
    /**
     * 完成任务
     * @param taskId  			任务ID
     * @param comments			评论实体
     * @param variables			任务参数	
     * @throws Exception	
     */
    public String complete(String taskId, Comments comments, Map<String, Object> variables) throws Exception;
    
    /**
     * 撤销任务
     * @param historyTaskId
     * @param processInstanceId
     * @throws Exception
     */
    public Integer revoke(String historyTaskId, String processInstanceId) throws Exception;
    
    /**
     * 获取评论
     * @param processInstanceId
     * @throws Exception
     */
    public List<Comments> getComments(String processInstanceId) throws Exception;
    
    /**
     * 根据executionId获取流程参数 
     * @param executionId
     * @param variableName
     * @throws Exception
     */
    public Object getVariableByExecutionId(String executionId, String variableName) throws Exception;
    
    /**
     * 根据executionId保存流程参数
     * @param executionId
     * @param variableName
     * @param object
     * @throws Exception
     */
    public void saveVariableByExecutionId(String executionId, String variableName, Object object) throws Exception;
    
    /**
     * 根据processInstanceId获取流程实例
     * @param processInstanceId
     * @throws Exception
     */
    public ProcessInstance getProcessInstanceById(String processInstanceId) throws Exception;
    
    /**
     * 根据processDefinitionId获取流程定义实体
     * @param processDefinitionId
     * @return
     * @throws Exception
     */
    public ProcessDefinition getProcessDefinitionById(String processDefinitionId) throws Exception;
    
	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity			当前任务节点
	 * @param targetTaskDefinitionKey	目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void moveTo(String currentTaskId, String targetTaskDefinitionKey) throws Exception;

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * 
	 * @param currentTaskEntity			当前任务节点
	 * @param targetTaskDefinitionKey	目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void moveTo(TaskEntity currentTaskEntity, String targetTaskDefinitionKey) throws Exception;
 
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @throws Exception
     */
    public InputStream getDiagram(String processInstanceId) throws Exception;
    
    /**
     * 显示图片-通过流程ID，不带流程跟踪(没有乱码问题)
     * @param resourceType
     * @param processInstanceId
     * @throws Exception
     */
    public InputStream getDiagramByProInstanceId_noTrace(String resourceType, String processInstanceId) throws Exception;
    
    /**
     * 显示图片-通过部署ID，不带流程跟踪(没有乱码问题)
     * @param resourceType
     * @param processInstanceId
     * @throws Exception
     */
    public InputStream getDiagramByProDefinitionId_noTrace(String resourceType, String processDefinitionId) throws Exception;

    /**
     * 读取已结束中的流程-admin查看
     * @param page
     * @throws Exception
     */
    public List<ProcessTask> findFinishedProcessInstances(Page<ProcessTask> page) throws Exception;
    
    /**
     * 各个审批人员查看自己完成的任务
     * @param user
     * @param page
     * @throws Exception
     */
    public List<ProcessTask> findFinishedTaskInstances(User user, Page<ProcessTask> page) throws Exception;
    
    /**
     * 管理运行中流程
     * @param page
     * @throws Exception
     */
    public List<ProcessInstance> listRuningProcess(Page<ProcessInstance> page) throws Exception;

    /**
     * 激活流程实例
     * @param processInstanceId
     * @throws Exception
     */
    public void activateProcessInstance(String processInstanceId) throws Exception;
    
    /**
     * 挂起流程实例
     * @param processInstanceId
     * @throws Exception
     */
    public void suspendProcessInstance(String processInstanceId) throws Exception;
    
    /**
     * 通过processInstanceId获取当前任务
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    public Task getTaskByProcessInstanceId(String processInstanceId) throws Exception;
    
}
