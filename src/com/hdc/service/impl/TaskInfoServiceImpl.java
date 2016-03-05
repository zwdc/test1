package com.hdc.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants.TaskInfoStatus;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

	@Autowired
	private IBaseService<TaskInfo> baseService;
	
	@Autowired
	private IProcessService processService;
	
    @Autowired
    private TaskService taskService;
	
	@Override
	public List<TaskInfo> getListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception {
		return this.baseService.findListPage("TaskInfo", param, map, page);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update TaskInfo set isDelete = 1 where id = "+id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public Serializable doAdd(TaskInfo taskInfo) throws Exception {
		return this.baseService.add(taskInfo);
	}

	@Override
	public void doUpdate(TaskInfo taskInfo) throws Exception {
		this.baseService.update(taskInfo);
	}

	@Override
	public TaskInfo findById(Integer id) throws Exception {
		return this.baseService.getBean(TaskInfo.class, id);
	}

	@Override
	public void doStartProcess(TaskInfo taskInfo) throws Exception {
		taskInfo.setStatus(TaskInfoStatus.WAIT_FOR_CLAIM.toString());
		taskInfo.setAssignDate(new Date());
		this.baseService.update(taskInfo);
		
		//初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
		vars.put("hostUser", taskInfo.getHostUser().getId().toString());
		//启动流程
		String processInstanceId = this.processService.startApproval("TaskInfo", taskInfo.getId().toString(), vars);	
		// 根据processInstanceId查询第一个任务，即“录入任务”
		Task firstTask = this.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		// 完成第一个任务，任务继续向下流
		this.processService.complete(firstTask.getId().toString(), null, null);
		
	}

	@Override
	public void doClaimTask(TaskInfo taskInfo) throws Exception {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("claim", true);
		
		this.processService.complete(taskInfo.getActTaskId(), null, variables);
		this.baseService.update(taskInfo);
		
	}

}
