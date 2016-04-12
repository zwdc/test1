package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Comments;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskInfoService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.Constants.TaskInfoStatus;
import com.hdc.util.UserUtil;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

	@Autowired
	private IBaseService<TaskInfo> baseService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private IProcessService processService;
	
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ITaskSourceService taskResourceService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Override
	public List<TaskInfo> getListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception {
		return this.baseService.findListPage("TaskInfo", param, map, page, true);
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
		taskInfo.setStatus(ApprovalStatus.PENDING.toString());
		this.baseService.update(taskInfo);
		
		//给用户提示任务
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("任务录入完成，需要审批!");
		processTask.setUrl("/taskInfo/toApproval?taskInfoId="+taskInfo.getId().toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalTaskInfo", taskInfo.getId().toString(), vars);	
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
		/*ProcessTask processTask = new ProcessTask();
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
		vars.put("processTaskId", processTaskId.toString());*/
//		this.processService.complete(taskInfo.getActTaskId(), null, vars);		//完成“办理中” 节点任务
		
	}

	@Override
	public void doApproval(Integer taskInfoId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		TaskInfo taskInfo = this.findById(taskInfoId);
		if(isPass) {
			taskInfo.setStatus(ApprovalStatus.APPROVAL_SUCCESS.toString()); //审批成功
		} else {
			taskInfo.setStatus(ApprovalStatus.APPROVAL_FAILED.toString()); //审批失败
			ProcessTask processTask = new ProcessTask();
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("任务审批不通过，请修改后重新审批！");
			processTask.setUrl("/taskInfo/toApproval?taskInfoId="+taskInfo.getId().toString());
			this.processTaskService.doAdd(processTask);
		}
		// 评论
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName());
		comments.setContent(comment);
		comments.setBusinessKey(taskInfoId);
		comments.setBusinessForm(BusinessForm.TASK_FORM.toString());
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
	}

}
