package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;

public interface IProjectService {
	
	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Project> getListPage(Parameter param, Page<Project> page, Map<String, Object> map) throws Exception;
	
	/**
	 * 针对办理中或已签收的查询
	 * @param param
	 * @param type
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getProjectList(Parameter param, Integer type, Page<Map<String, Object>> page) throws Exception;
	
	/**
	 * 审批
	 * @param salesId
	 * @param isPass
	 * @param taskId
	 * @param processInstanceId
	 * @param comment
	 * @throws Exception
	 */
	public void doApproval(Integer projectId, boolean isPass, String taskId, String comment) throws Exception;
	
	/**
	 * 审批拒绝签收
	 * @param projectId
	 * @param oldGroupId
	 * @param groupId
	 * @param isPass
	 * @param taskId
	 * @param comment
	 * @throws Exception
	 */
	public void doApprovalRefuse(Integer projectId, Integer oldGroupId, Integer groupId, boolean isPass, String taskId, String comment) throws Exception;
	
	/**
	 * 通过id获取Project
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Project findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param project
	 * @throws Exception
	 */
	public Serializable doAdd(Project project) throws Exception;
	
	/**
	 * 修改
	 * @param project
	 * @throws Exception
	 */
	public void doUpdate(Project project) throws Exception;
	
	/**
	 * 根据taskInfoId批量更新项目状态
	 * @param taskInfoId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Integer doUpdateStatus(String taskInfoId, String status) throws Exception;
	
	/**
	 * 根据projectId更新状态
	 * @param projectId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Integer doUpdateProjectStatus(String projectId, String status) throws Exception;
	
	/**
	 * 更新拟办意见
	 * @param projectId
	 * @param suggestion
	 * @throws Exception
	 */
	public void doUpdateById(String projectId, String suggestion) throws Exception;
	
	/**
	 * 签收(线程安全)
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public boolean doClaimProject(String projectId) throws Exception;
	
	/**
	 * 申请审批
	 * @param projectId
	 * @param suggestion
	 * @throws Exception
	 */
	public void doStartProcess(Integer projectId, String suggestion) throws Exception;
	
	/**
	 * 拒签收审批
	 * @param projectId
	 * @param reason
	 * @throws Exception
	 */
	public void doRefuseProject(Integer projectId, String reason) throws Exception;
	
	/**
	 * 完成任务
	 * @param projectId
	 * @param suggestion
	 * @param taskId
	 * @throws Exception
	 */
	public void doCompleteTask(Integer projectId, String suggestion, String taskId) throws Exception;
	
	/**
	 * 查看驳回申请时，点确认完成任务
	 * @param projectId
	 * @param taskId
	 * @throws Exception
	 */
	public void doCompleteApprovalFailed(String taskId) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 根据taskInfoId查询
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByTaskInfo(Integer taskInfoId) throws Exception;
	
	
}
