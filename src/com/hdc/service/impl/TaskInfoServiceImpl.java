package com.hdc.service.impl;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackFrequencyService;
import com.hdc.service.IFeedbackRecordService;
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
	private IFeedbackFrequencyService fbFrequentyService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private IProcessService processService;
	
    @Autowired
    private TaskService taskService;
    
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
		this.doAddHostGroup(taskInfo);
		this.doAddFeedback(taskInfo);
		
		//初始化任务参数
		/*Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
//		vars.put("hostUser", taskInfo.getHostUser());
		//启动流程
		String processInstanceId = this.processService.startApproval("TaskInfo", taskInfo.getId().toString(), vars);	
		// 根据processInstanceId查询第一个任务，即“录入任务”
		Task firstTask = this.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		// 完成第一个任务，任务继续向下流
		this.processService.complete(firstTask.getId().toString(), null, null);*/
		
	}
	
	/**
	 * 根据主办单位，生成项目表
	 * @param groupIds
	 * @param taskInfoId
	 * @throws Exception
	 */
	private void doAddHostGroup(TaskInfo taskInfo) throws Exception {
		String groupIds = taskInfo.getHostGroup(); 
		if(StringUtils.isNotBlank(groupIds)) {
			for(String groupId : groupIds.split(",")){
				//List<Object[]> paramList = new ArrayList<Object[]>();
				Project project = new Project();
				project.setGroup(new Group(new Integer(groupId)));
				project.setTaskInfo(taskInfo);
				project.setStatus(ApprovalStatus.PENDING.toString());
				this.projectService.doAdd(project);
				//生成一个项目表 就要针对此项目表 生成反馈表
				//saveFeedback(project, taskInfo);
			}
		}
	}
	
	/**
	 * 根据反馈频度生产反馈表
	 * @param taskInfo
	 * @throws Exception
	 */
	private void doAddFeedback(TaskInfo taskInfo) throws Exception {
		List<Project> listProject = this.projectService.findByTaskInfo(taskInfo.getId());
		Integer fbFrequencyId = taskInfo.getFbFrequency().getId();
		if(fbFrequencyId != null) {
			FeedbackFrequency fbFrequency = this.fbFrequentyService.findById(fbFrequencyId);
			Date taskBegin = taskInfo.getCreateTaskDate();
			Date taskEnd = taskInfo.getEndTaskDate();
			Integer fbType = fbFrequency.getType();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!taskBegin.after(taskEnd)) {	//判断日期是否正确
				for(Project project : listProject){	//每个项目下，生成反馈表
					switch (fbType) {
						case 1:	//单次任务
							FeedbackRecord feedback = new FeedbackRecord();
							feedback.setProject(project);
							feedback.setIsDelay(0);
							Date singleTaskDate = fbFrequency.getSingleTask();				//此日期前进行反馈
							feedback.setFeedbackStartDate(taskInfo.getCreateTaskDate());	//项目开始时间
							feedback.setFeedbackEndDate(singleTaskDate);
							this.feedbackService.doAdd(feedback);
							break;
						case 2:	//每周任务
							Calendar w_begin = new GregorianCalendar();
						    Calendar w_end = new GregorianCalendar();
						    
						    w_begin.setTime(taskBegin);
						    
						    w_end.setTime(taskEnd);
						    w_end.add(Calendar.DAY_OF_YEAR, 1);  //为了包含结束日期的最后一天
						    
						    String weeklyTask = fbFrequency.getWeeklyTask();
						    Date weeklyStartTime = fbFrequency.getWeeklyStartTime();
						    Date weeklyEndTime = fbFrequency.getWeeklyEndTime();
						    if(StringUtils.isNotBlank(weeklyTask)) {
							    while(w_begin.before(w_end)){	//循环任务的开始日期到结束日期，生成反馈表
							    	for(String week : weeklyTask.split(",")) {
							    		if(Integer.parseInt(week) == w_begin.get(Calendar.DAY_OF_WEEK)) {
							    			FeedbackRecord w_feedback = new FeedbackRecord();
											w_feedback.setProject(project);
											w_feedback.setIsDelay(0);
							    			Date currDate = new java.sql.Date(w_begin.getTime().getTime());
							    			StringBuffer sb_begin = new StringBuffer();
							    			sb_begin.append(currDate).append(" ").append(weeklyStartTime);
							    			
							    			StringBuffer sb_end = new StringBuffer();
							    			sb_end.append(currDate).append(" ").append(weeklyEndTime);
							    			
							    			w_feedback.setFeedbackStartDate(formatter.parse(sb_begin.toString()));
							    			w_feedback.setFeedbackEndDate(formatter.parse(sb_end.toString()));
							    			this.feedbackService.doAdd(w_feedback);
							    			break;
							    		}
							    	}
							       w_begin.add(Calendar.DAY_OF_YEAR, 1);
							    }
						    }
							break;
						case 3:	//每月任务
							Calendar m_begin = new GregorianCalendar();
						    Calendar m_end = new GregorianCalendar();
						    m_begin.setTime(taskBegin);
						    m_end.setTime(taskEnd);
							
						    String monthlyTask = fbFrequency.getMonthlyTask();
						    Date monthlyStartTime = fbFrequency.getMonthlyStartTime();
						    Date monthlyEndTime = fbFrequency.getMonthlyEndTime();
						    
						    Integer monthlyStartDay = fbFrequency.getMonthlyStartDay();
						    Integer monthlyEndDay = fbFrequency.getMonthlyEndDay(); 
						    
						    if(StringUtils.isNotBlank(monthlyTask)) {
						    	while(m_begin.before(m_end)){	//循环任务的开始日期到结束日期，生成反馈表
						    		for(String month : monthlyTask.split(",")) {
						    			if(Integer.parseInt(month) == m_begin.get(Calendar.MONTH)+1) {	//月份是从0开始，因此要+1
						    				FeedbackRecord m_feedback = new FeedbackRecord();
						    				m_feedback.setProject(project);
											m_feedback.setIsDelay(0);
						    				//反馈开始日期
						    				StringBuffer sb_begin = new StringBuffer();
						    				sb_begin.append(m_begin.get(Calendar.YEAR)).append("-").append(m_begin.get(Calendar.MONTH)+1).append("-").append(monthlyStartDay+" ");
						    				sb_begin.append(monthlyStartTime);
						    				m_feedback.setFeedbackStartDate(formatter.parse(sb_begin.toString()));
						    				//反馈结束时限
						    				StringBuffer sb_end = new StringBuffer();
						    				sb_end.append(m_begin.get(Calendar.YEAR)).append("-").append(m_begin.get(Calendar.MONTH)+1).append("-").append(monthlyEndDay+" ");
						    				sb_end.append(monthlyEndTime);
						    				m_feedback.setFeedbackEndDate(formatter.parse(sb_end.toString()));
							    			this.feedbackService.doAdd(m_feedback);
							    			break;
						    			}
						    		}
						    		m_begin.add(Calendar.MONTH, 1);
						    	}
						    	
						    	//上面的循环里，不会包括最后一个月，单独计算
						    	for(String month : monthlyTask.split(",")){
						    		if(Integer.parseInt(month) == m_end.get(Calendar.MONTH)+1){
						    			Integer day = m_end.get(Calendar.DAY_OF_MONTH);
						    			if(day >= monthlyEndDay) { //如过项目的结束时限所规定最后的一天 >= 每月任务选择的 天和日期，则生成反馈表
						    				FeedbackRecord d_feedback = new FeedbackRecord();
						    				d_feedback.setProject(project);
						    				d_feedback.setIsDelay(0);
						    				StringBuffer sb_begin = new StringBuffer();
						    				sb_begin.append(m_end.get(Calendar.YEAR)).append("-").append(m_end.get(Calendar.MONTH)+1).append("-").append(monthlyStartDay+" ");
						    				sb_begin.append(monthlyStartTime);
						    				d_feedback.setFeedbackStartDate(formatter.parse(sb_begin.toString()));
						    				
						    				StringBuffer sb_end = new StringBuffer();
						    				sb_end.append(m_end.get(Calendar.YEAR)).append("-").append(m_end.get(Calendar.MONTH)+1).append("-").append(monthlyEndDay+" ");
						    				sb_end.append(monthlyEndTime);
						    				d_feedback.setFeedbackEndDate(formatter.parse(sb_end.toString()));
						    				this.feedbackService.doAdd(d_feedback);
						    			}
						    		}
						    	}
						    }
							break;
						default:
							break;
					}
				}
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
	
	public static void main(String[] args) throws ParseException {
		Calendar c_begin = new GregorianCalendar();
	     Calendar c_end = new GregorianCalendar();
	     DateFormatSymbols dfs = new DateFormatSymbols();
	     String[] weeks = dfs.getWeekdays();
	     c_begin.set(2016, 3, 06); //Calendar的月从0-11，所以4月是3.
	     c_end.set(2016, 11, 22); //Calendar的月从0-11，所以5月是4.
	     //c_begin.set(2016, 3, 2, 0, 0, 0);
	     int count = 1;
	     //c_end.add(Calendar.MONTH, -1);  //结束日期下滚一天是为了包含最后一天
	     
	     while(c_begin.before(c_end)){
	       System.out.println("第"+count+"周  日期："+new java.sql.Date(c_begin.getTime().getTime()).toString()+","+weeks[c_begin.get(Calendar.DAY_OF_WEEK)]+"---"+c_begin.get(Calendar.DAY_OF_WEEK)+" -- "+c_begin.get(Calendar.MONTH));
	 
	      if(c_begin.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
	          count++;
	      }
	      c_begin.add(Calendar.DAY_OF_YEAR, 1);
	     }
	     
	     /*Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-1-22");
	     Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-9-30");
	     Calendar c1 = new GregorianCalendar();
	     Calendar c2 = new GregorianCalendar();
	     c1.setTime(d1);
	     c2.setTime(d2);
	     //c2.add(Calendar.MONTH, 1);
	     while(c1.before(c2)){
	    	 System.out.println(c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONTH)+1)+"--"+(c1.get(Calendar.MONTH)+1));
	    	 c1.add(Calendar.MONTH, 1);
	     }
	     System.out.println(c2.get(Calendar.DAY_OF_MONTH)+"-"+c2.get(Calendar.MONTH));*/
	}

}
