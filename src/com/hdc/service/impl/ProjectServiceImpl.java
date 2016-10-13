package com.hdc.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.dao.IJdbcDao;
import com.hdc.entity.Comments;
import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.Project;
import com.hdc.entity.ProjectScore;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.IProjectScoreService;
import com.hdc.service.IProjectService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.Constants.ProjectStatus;
import com.hdc.util.UserUtil;

@Service
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private IBaseService<Project> baseService;
	
	@Autowired
	private IJdbcDao jdbcDao;
	
	@Autowired
	private IProcessService processService;
	
	@Autowired
	private IProjectScoreService projectScoreService;
	
    @Autowired
    private ITaskSourceService taskResourceService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Override
	public Project findById(Integer id) throws Exception {
		return this.baseService.getBean(Project.class, id);
	}

	@Override
	public Serializable doAdd(Project project) throws Exception {
		return this.baseService.add(project);
	}

	@Override
	public void doUpdate(Project project) throws Exception {
		this.baseService.update(project);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update Project set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public List<Project> findByTaskInfo(Integer taskInfoId) throws Exception {
		String hql = "from Project where taskInfo.id = " + taskInfoId.toString() + " and isDelete = 0";
		return this.baseService.find(hql);
	}

	@Override
	public List<Project> getListPage(Parameter param, Page<Project> page,
			Map<String, Object> map) throws Exception {
		return this.baseService.findListPage("Project", param, map, page, true);
	}

	@Override
	public List<Map<String, Object>> getProjectList(Parameter param, Integer type , Page<Map<String, Object>> page)
			throws Exception {
		StringBuffer sb = new StringBuffer();  
		User user = UserUtil.getUserFromSession();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupId", user.getGroup().getId());
		paramMap.put("userId", user.getId());
		sb.append("select a.id, g.name group_name, u.user_name user_name, t.id task_id, t.title, t.urgency, s.name source_name, t.end_task_date, fb.name frequency_name, a.status from project a ");
		sb.append("left join groups g on (g.group_id = a.group_id) ");
		sb.append("left join users u on (u.user_id = a.user_id) ");
		sb.append("left join role r on(r.id=u.role_id)");
		sb.append("left join task_info t on (t.id = a.task_info_id and t.is_delete = 0) ");
		sb.append("left join task_source s on (s.id = t.task_source) ");
		sb.append("left join feedback_frequency fb on (fb.id = t.fb_frequency and fb.is_delete = 0) ");
		
		if(type == 1) {
			//待签收/已签收/审批中/审批通过/审批失败     一个单位只有两个人可以签收，其他人都可以查看
			sb.append("where a.is_delete = 0 and (a.status = 'WAIT_FOR_CLAIM' or a.status = 'CLAIMED' or a.status = 'PENDING' or a.status = 'APPROVAL_SUCCESS' or a.status = 'APPROVAL_FAILED' or a.status = 'REFUSE_FAILED')");
		} else if(type == 2) {
			//办理中/申请办结   
			sb.append("where a.is_delete = 0 and (a.status = 'IN_HANDLING' or a.status='CAN_BE_FINISHED' or a.status = 'APPLY_FINISHED' or a.status = 'APPROVAL_FAILED')");
		} else if(type == 3) {
			//已办结     同单位的人可以查看
			sb.append("where a.is_delete = 0 and a.status = 'FINISHED'");
		}
		//数据权限（主要是为了签收用）与所有人的数据权限不同
		if(user.getDataPermission() == 0) {
			sb.append("and a.user_id = " + user.getId().toString());			
		} else if(user.getDataPermission() == 1) {
			sb.append("and a.group_id = " + user.getGroup().getId().toString());
		}else if(user.getDataPermission()==2){
			sb.append("and a.role_id="+user.getRole().getId().toString());
		}else if(user.getDataPermission()==3){
			sb.append("and (a.role_id="+user.getRole().getId().toString()+" or a.role_id is null) and a.group_id="+ user.getGroup().getId().toString());
		}else if(user.getDataPermission()==4){
			sb.append("and (a.role_id="+user.getRole().getId().toString()+" or a.group_id="+ user.getGroup().getId().toString()+")");
		}
		//模糊查询
		if(param != null && StringUtils.isNotBlank(param.getSearchValue())){
        	//如果查询的字段中有日期
        	if(param.getSearchName().toLowerCase().indexOf("date") >= 0){
        		sb.append(" and to_char(a." + param.getSearchName() + ", 'yyyy-MM-dd') like '%" + param.getSearchValue() + "%' ");
        	}else{
        		sb.append(" and a." + param.getSearchName() + " like '%" + param.getSearchValue() + "%' ");
        	}
        }
		
		//高级查询
        if(StringUtils.isNotBlank(param.getSearchColumnNames())){
	        String[] searchColumnNameArray=param.getSearchColumnNames().split(",");
			String[] searchAndsArray=param.getSearchAnds().split(",");
			String[] searchConditionsArray=param.getSearchConditions().split(",");
			String[] searchValsArray=param.getSearchVals().split(",");
			StringBuffer gradeSearch = new StringBuffer();  
			
			if(searchColumnNameArray.length >0 ){
				for (int i = 0; i < searchColumnNameArray.length; i++) {
					if (StringUtils.isNotBlank(searchColumnNameArray[i])) {
						String value=searchValsArray[i].trim().replaceAll("\'", "");
						if ("like".equals(searchConditionsArray[i].trim())){
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								gradeSearch.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}else{
								gradeSearch.append(" " + searchAndsArray[i].trim() + " a." + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}
						}else {
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								gradeSearch.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}else{
								gradeSearch.append(" " + searchAndsArray[i].trim() + " a." + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}
						}
					}
				}
				String gs = gradeSearch.toString();
				sb.append(" and (" + gs.substring(StringUtils.indexOfIgnoreCase(gs, " ",1), gs.length()) + " )");			
			}
        }     
        if(param != null && StringUtils.isNotBlank(param.getSort())) {
			sb.append(" order by a." + param.getSort() + " " + param.getOrder());
		} else {
			sb.append(" order by a.create_date desc");
		}
        
        return this.jdbcDao.find(sb.toString(), paramMap, page);
	}

	@Override
	public synchronized boolean doClaimProject(String projectId) throws Exception {
		User user = UserUtil.getUserFromSession();
		String hql1 = "from Project where isDelete = 0 and id = "+projectId+" and user.id is null";
		List<Project> list = this.baseService.find(hql1);
		if(list.size() > 0) {
			String hql2 = "update Project set user.id = :userId, claimDate = :claimDate, status = :status where id = " + projectId;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", user.getId());
			Date claimDate = new Date();
			params.put("claimDate", claimDate);
			params.put("status", "CLAIMED");
			this.baseService.executeHql(hql2, params);
			
			Project project = list.get(0);
			TaskInfo taskInfo = project.getTaskInfo();
			Date claimLimitDate = taskInfo.getClaimLimitDate();
			if(claimDate.after(claimLimitDate)){
				ProjectScore projectScore=new ProjectScore(project,null,"未按时签收任务",-10);
				this.projectScoreService.doAdd(projectScore);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doUpdateById(String projectId, String suggestion)
			throws Exception {
		String hql = "update Project set suggestion = '" + suggestion + "' where id = " + projectId;
		this.baseService.executeHql(hql);
	}

	@Override
	public void doStartProcess(Integer projectId, String suggestion)
			throws Exception {
		Project  project = this.findById(projectId);
		project.setSuggestion(suggestion);
		project.setStatus(ApprovalStatus.PENDING.toString());
		this.doUpdate(project);
		TaskInfo taskInfo = project.getTaskInfo();
		//给用户提示任务
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("任务交办表需要审批!");
		processTask.setUrl("/project/toProject/approval?projectId="+projectId.toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		// 此处业务修改为：谁创建的任务谁去审批，原来是秘书长审批拟办意见和阶段计划
		vars.put("createTaskUser", taskInfo.getCreateUser().getId().toString());
		vars.put("taskInfoId", taskInfo.getId().toString());	//根据taskInfoId查询所有project,判断项目状态
		vars.put("projectId", projectId.toString());			//根据projectId查询审批流程
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalProject", projectId.toString(), vars);	
		
	}

	@Override
	public void doApproval(Integer projectId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		Project project = this.findById(projectId);
		if(isPass) {
			project.setStatus(ApprovalStatus.APPROVAL_SUCCESS.toString()); 	  //审批通过，所有班里人审批都通过才是办理中状态
		} else {
			project.setStatus(ApprovalStatus.APPROVAL_FAILED.toString()); //审批失败
			TaskInfo taskInfo = project.getTaskInfo();
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("任务交办表审批不通过，请修改后重新审批！");
			processTask.setUrl("/project/toProject/modify?projectId="+projectId.toString());
			Serializable processTaskId = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", processTaskId.toString());
		}
		this.doUpdate(project);
		// 评论
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(projectId);
		comments.setBusinessForm(BusinessForm.PROJECT_FORM.toString());
		
		variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
		
	}

	@Override
	public void doCompleteTask(Integer projectId, String suggestion, String taskId) throws Exception {
		//给秘书长提示代办任务
		Project  project = this.findById(projectId);
		project.setSuggestion(suggestion);
		project.setStatus(ApprovalStatus.PENDING.toString());
		this.doUpdate(project);
		TaskInfo taskInfo = project.getTaskInfo();
		Map<String, Object> variables = new HashMap<String, Object>();
		
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		processTask.setTaskInfoId(taskInfo.getId());
		TaskSource taskSource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskSource.getTaskInfoType().getName());
		processTask.setTitle("任务交办表修改完成！需要重新审批!");
		processTask.setUrl("/project/toProject/approval?projectId="+projectId.toString());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		//初始化任务参数
		variables.put("processTaskId", processTaskId.toString());
		this.processService.complete(taskId, null, variables);
		
	}
	
	@Override
	public void doCompleteCompletionTask(Integer projectId, String taskId)
			throws Exception {
		//给秘书长提示代办任务
		Project  project = this.findById(projectId);
		project.setStatus(ApprovalStatus.PENDING.toString());
		this.doUpdate(project);
		TaskInfo taskInfo = project.getTaskInfo();
		Map<String, Object> variables = new HashMap<String, Object>();
		
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		processTask.setTaskInfoId(taskInfo.getId());
		TaskSource taskSource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskSource.getTaskInfoType().getName());
		processTask.setTitle("办结申请修改完成！需要重新审批!");
		processTask.setUrl("/project/toProject/modifyComplete?projectId="+projectId.toString());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		//初始化任务参数
		variables.put("processTaskId", processTaskId.toString());
		this.processService.complete(taskId, null, variables);
		
	}

	@Override
	public Integer doUpdateStatus(String taskInfoId, String status) throws Exception {
		String hql = "update Project set status = '" + status + "' where taskInfo.id = " + taskInfoId;
		return this.baseService.executeHql(hql);
	}

	@Override
	public void doRefuseProject(Integer projectId, String reason)
			throws Exception {
		User user = UserUtil.getUserFromSession();
		Project  project = this.findById(projectId);
		project.setRefuseReason(reason);
		project.setStatus(ApprovalStatus.PENDING.toString());
		project.setRefuseUser(user);
		this.doUpdate(project);
		TaskInfo taskInfo = project.getTaskInfo();
		//给用户提示任务
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("任务交办表拒签收!");
		processTask.setUrl("/project/toProject/approval_refuse?projectId="+projectId.toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("censur", taskInfo.getCreateUser().getId().toString());	//督查室审批
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("RefuseClaim", projectId.toString(), vars);	
		
	}

	@Override
	public void doApprovalRefuse(Integer projectId, Integer oldGroupId, Integer groupId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		Project project = this.findById(projectId);
		if(isPass) {
			project.setStatus(ProjectStatus.WAIT_FOR_CLAIM.toString()); 	//同意修改承办单位，新的单位待签收
			project.setGroup(new Group(groupId));
			project.setRefuseReason("");
			project.setRefuseUser(null);
			TaskInfo taskInfo = project.getTaskInfo();
			String hostGroup = taskInfo.getHostGroup();
			taskInfo.setHostGroup(hostGroup.replace(","+oldGroupId, ","+groupId));	//替换单位
			//给秘书长提示审批 任务信息（因为承担单位有变化了）
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("主办单位发生改变，请重新审批！");
			processTask.setUrl("/taskInfo/toApproval?taskInfoId="+taskInfo.getId());
			Serializable processTaskId = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", processTaskId.toString());
		} else {
			project.setStatus(ApprovalStatus.REFUSE_FAILED.toString()); 	//不同意修改承办单位
			TaskInfo taskInfo = project.getTaskInfo();
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("您拒绝签收的操作被驳回，查看驳回原因！");
			processTask.setUrl("/project/toProject/change_failed?projectId="+project.getId());
			Serializable processTaskId = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", processTaskId.toString());
		}
		this.doUpdate(project);
		// 评论
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(projectId);
		comments.setBusinessForm(BusinessForm.PROJECT_FORM.toString());
		
		variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
		
	}

	@Override
	public void doCompleteApprovalFailed(String taskId) throws Exception {
		this.processService.complete(taskId, null, null);
	}

	@Override
	public Integer doUpdateProjectStatus(String projectId, String status) throws Exception {
		String hql = "update Project set status = '" + status + "' where id = " + projectId;
		return this.baseService.executeHql(hql);
	}

	@Override
	public void doStartComplete(Integer projectId) throws Exception {
		Project  project = this.findById(projectId);
		project.setStatus(ProjectStatus.APPLY_FINISHED.toString());	//申请办结中
		this.doUpdate(project);
		TaskInfo taskInfo = project.getTaskInfo();
		//给用户提示任务
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("任务交办表申请办结!");
		processTask.setUrl("/project/toProject/complete?projectId="+projectId.toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());	//根据taskInfoId查询所有project,判断项目状态
		vars.put("createTaskUser", taskInfo.getCreateUser().getId().toString());
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalComplete", projectId.toString(), vars);	
		
	}

	@Override
	public void doApprovalComplete(Integer projectId, boolean isPass,
			String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		Project project = this.findById(projectId);
		if(isPass) {
			project.setStatus(ProjectStatus.FINISHED.toString()); 	  //此任务交办表为已办结，当所有交办表都为已办结时，TaskInfo才为已办结
		} else {
			project.setStatus(ApprovalStatus.APPROVAL_FAILED.toString()); //审批失败
			TaskInfo taskInfo = project.getTaskInfo();
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("申请办结不通过，请查看审批意见！");
			processTask.setUrl("/project/toProject/modifyComplete?projectId="+projectId.toString());
			Serializable processTaskId = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", processTaskId.toString());
		}
		this.doUpdate(project);
		// 评论
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(projectId);
		comments.setBusinessForm(BusinessForm.PROJECT_COMPLETE.toString());
		
		variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
		
	}

}
