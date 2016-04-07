package com.hdc.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.IProjectService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessType;
import com.hdc.util.Constants.OperationType;
import com.hdc.util.Constants.TaskInfoStatus;
import com.hdc.util.UserUtil;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

	@Autowired
	private IBaseService<TaskInfo> baseService;
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IProcessService processService;
	
    @Autowired
    private TaskService taskService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
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
		this.baseService.update(taskInfo);
		
		//初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
//		vars.put("hostUser", taskInfo.getHostUser());
		//启动流程
		String processInstanceId = this.processService.startApproval("TaskInfo", taskInfo.getId().toString(), vars);	
		// 根据processInstanceId查询第一个任务，即“录入任务”
		Task firstTask = this.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		// 完成第一个任务，任务继续向下流
		this.processService.complete(firstTask.getId().toString(), null, null);
		
	}
	
	/**
	 * 根据主办单位，生成项目表
	 * @param groupIds
	 * @param taskInfoId
	 * @throws Exception
	 */
	private void saveHostGroup(String groupIds, Integer taskInfoId) throws Exception {
		if(StringUtils.isNotBlank(groupIds)) {
			for(String groupId : groupIds.split(",")){
				//List<Object[]> paramList = new ArrayList<Object[]>();
				Project project = new Project();
				project.setGroup(new Group(new Integer(groupId)));
				project.setTaskInfo(new TaskInfo(taskInfoId));
				project.setStatus(ApprovalStatus.PENDING.toString());
				this.projectService.doAdd(project);
			}
		}
	}
	
	@Override
	public void doClaimTask(TaskInfo taskInfo) throws Exception {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("claim", true);
		
//		this.processService.complete(taskInfo.getActTaskId(), null, variables);
		this.baseService.update(taskInfo);
		
	}

	@Override
	public void doCompleteTask(Integer id) throws Exception {
		//给督察处提示代办任务
		TaskInfo taskInfo = this.findById(id);
		taskInfo.setStatus(TaskInfoStatus.APPLY_FINISHED.toString());
		this.baseService.update(taskInfo);
		
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setUser_name(user.getName());
		processTask.setUser_id(user.getId());
		processTask.setBusinessType(BusinessType.IMPORTANT_FILE.toString());	//业务类型：重要文件
		processTask.setBusinessKey(id);
		//processTask.setTaskId(taskInfo.getActTaskId());  //不用设置，跳转到下一节点时会根据listener自动赋值
		processTask.setTitle("任务《"+taskInfo.getTitle()+"》申请办结！");
		processTask.setUrl("/taskInfo/toApproval?id="+id);
		//processTask.setBusinessForm(BusinessForm.QT_FILE.toString());			//业务表单：省政府文件
		processTask.setBusinessOperation(OperationType.APPROVAL.toString());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		//初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("processTaskId", processTaskId.toString());
//		this.processService.complete(taskInfo.getActTaskId(), null, vars);		//完成“办理中” 节点任务
		
	}

}
