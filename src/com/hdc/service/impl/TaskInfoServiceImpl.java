package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.dao.IJdbcDao;
import com.hdc.entity.Comments;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskInfoService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.DateUtil;
import com.hdc.util.UserUtil;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

	@Autowired
	private IBaseService<TaskInfo> baseService;
	
	@Autowired
	private IJdbcDao jdbcDao;
	
	@Autowired
	private IProcessService processService;
	
    @Autowired
    private ITaskSourceService taskResourceService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Override
	public List<TaskInfo> getThisYearListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception {
		if(StringUtils.isNotBlank(param.getSearchColumnNames())&&param.getSearchColumnNames().length()>0){
			param.setSearchColumnNames(param.getSearchColumnNames()+",endTaskDate");
			param.setSearchAnds(param.getSearchAnds()+",and");
			param.setSearchConditions(param.getSearchConditions()+",>");
			param.setSearchVals(param.getSearchVals()+",\'"+DateUtil.getCurrYearFirst()+"\'");
		}else{
			param.setSearchColumnNames("endTaskDate");
			param.setSearchAnds("and");
			param.setSearchConditions(">");
			param.setSearchVals("\'"+DateUtil.getCurrYearFirst()+"\'");
		}
		return this.baseService.findListPage("TaskInfo", param, map, page, true);
	}
	@Override
	public List<TaskInfo> getPastYearListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception {
		if(StringUtils.isNotBlank(param.getSearchColumnNames())&&param.getSearchColumnNames().length()>0){
			param.setSearchColumnNames(param.getSearchColumnNames()+",endTaskDate");
			param.setSearchAnds(param.getSearchAnds()+",and");
			param.setSearchConditions(param.getSearchConditions()+",<");
			param.setSearchVals(param.getSearchVals()+",\'"+DateUtil.getCurrYearFirst()+"\'");
		}else{
			param.setSearchColumnNames("endTaskDate");
			param.setSearchAnds("and");
			param.setSearchConditions("<");
			param.setSearchVals("\'"+DateUtil.getCurrYearFirst()+"\'");
		}
		return this.baseService.findListPage("TaskInfo", param, map, page, true);
	}
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
	public void doCompleteTask(TaskInfo taskInfo, String taskId) throws Exception {
		//给秘书长提示代办任务
		taskInfo.setStatus(ApprovalStatus.PENDING.toString());
		this.baseService.update(taskInfo);
		Map<String, Object> variables = new HashMap<String, Object>();		
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		processTask.setTaskInfoId(taskInfo.getId());
		TaskSource taskSource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskSource.getTaskInfoType().getName());
		processTask.setTitle("任务修改完成！需要重新审批!");
		processTask.setUrl("/taskInfo/toApproval?taskInfoId="+taskInfo.getId().toString());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		//初始化任务参数
		variables.put("processTaskId", processTaskId.toString());
		this.processService.complete(taskId, null, variables);
	}

	@Override
	public void doApproval(Integer taskInfoId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		TaskInfo taskInfo = this.findById(taskInfoId);
		if(isPass) {
			taskInfo.setStatus(ApprovalStatus.APPROVAL_SUCCESS.toString()); //审批成功
		} else {
			taskInfo.setStatus(ApprovalStatus.APPROVAL_FAILED.toString()); //审批失败
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("任务审批不通过，请修改后重新审批！");
			processTask.setUrl("/taskInfo/toModify?id="+taskInfo.getId().toString());
			Serializable id = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", id.toString());
		}
		// 评论
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(taskInfoId);
		comments.setBusinessForm(BusinessForm.TASK_FORM.toString());	
		variables.put("isPass", isPass);
		System.out.println(taskId);
		this.processService.complete(taskId, comments, variables);
	}

	@Override
	public Integer doUpdateStatus(String id, String status) throws Exception {
		String hql = "update TaskInfo set status = '" + status + "' where id = " + id;
		return this.baseService.executeHql(hql);
	}
	@Override
	public List<Map<String,Object>> getApprovalProcess(Integer taskInfo_id) throws Exception {
		String sql = "SELECT process_task.create_date as createDate,"
				+ "process_task.apply_user_name as applyUser,"
				+ "act_hi_comment.MESSAGE_ as approvalContent,"
				+ "users.USER_NAME as approvalUser,"
				+ "process_task.title as applyContent"
				+ " FROM "
				+ "process_task "
				+ "Left Join act_hi_comment ON act_hi_comment.PROC_INST_ID_ = process_task.pro_inst_id "
				+ "Inner Join users ON users.USER_ID = process_task.assign "
				+ " WHERE "
				+ "task_info_id="+taskInfo_id+" order by project_id,createDate";
		return this.jdbcDao.findAll(sql,null);
	}

}
