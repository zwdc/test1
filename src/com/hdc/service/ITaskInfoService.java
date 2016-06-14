package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskStatics;

public interface ITaskInfoService {

	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> getListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception;
	
	/**
	 * 获取本年的任务列表
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> getThisYearListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception;
	/**
	 * 获取往年的任务列表
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> getPastYearListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception;
	
	/**
	 * 通过id获取TaskInfo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskInfo findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param taskInfo
	 * @throws Exception
	 */
	public Serializable doAdd(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 修改
	 * @param taskInfo
	 * @throws Exception
	 */
	public void doUpdate(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 更改任务状态为-办理中
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Integer doUpdateStatus(String id, String status) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 启动任务分配流程 
	 * @param taskInfo
	 * @throws Exception
	 */
	public void doStartProcess(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 审批
	 * @param salesId
	 * @param isPass
	 * @param taskId
	 * @param processInstanceId
	 * @param comment
	 * @throws Exception
	 */
	public void doApproval(Integer salesId, boolean isPass, String taskId, String comment) throws Exception;
	
	/**
	 * 完成任务
	 * @param taskInfo
	 * @param taskId
	 * @throws Exception
	 */
	public void doCompleteTask(TaskInfo taskInfo, String taskId) throws Exception;

	List<Map<String, Object>> getApprovalProcess(Integer taskInfo_id) throws Exception;
	
	/**
	 * 统计当年的分类型的完成任务数和未完成任务数
	 */
	public List<TaskStatics> statisticsThisYear() throws Exception;
}
