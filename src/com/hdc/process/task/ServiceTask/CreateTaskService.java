package com.hdc.process.task.ServiceTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Group;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IFeedbackFrequencyService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProjectService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants.ProjectStatus;

/**
 * 根据taskInfoId生成项目表和反馈表
 * @author zhao
 *
 */

@Component
public class CreateTaskService implements JavaDelegate {

	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IFeedbackFrequencyService fbFrequentyService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String taskInfoId = (String) execution.getVariable("taskInfoId");
		TaskInfo taskInfo = this.taskInfoService.findById(new Integer(taskInfoId));
		taskInfo.setStatus(ProjectStatus.WAIT_FOR_CLAIM.toString());
		this.doAddHostGroup(taskInfo);
		this.doAddFeedback(taskInfo);
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
				Project project = new Project();
				project.setGroup(new Group(new Integer(groupId)));
				project.setTaskInfo(taskInfo);
				project.setStatus(ProjectStatus.WAIT_FOR_CLAIM.toString());
				project.setScore(100.00);
				this.projectService.doAdd(project);
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
						case 1:	//单次任务，单次任务已由单月确定
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
							    			
							    			//延期次数和退回次数置0
						    				w_feedback.setDelayCount(0);
						    				w_feedback.setRefuseCount(0);
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
						    				
						    				//延期次数和退回次数置0
						    				m_feedback.setDelayCount(0);
						    				m_feedback.setRefuseCount(0);
							    			this.feedbackService.doAdd(m_feedback);
							    			break;
						    			}
						    		}
						    		m_begin.add(Calendar.MONTH, 1);
						    	}
						    	
						    	
						    	//上面的循环里，不会包括最后一个月，单独计算
						    	/*for(String month : monthlyTask.split(",")){
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
						    				//延期次数和退回次数置0
						    				d_feedback.setDelayCount(0);
						    				d_feedback.setRefuseCount(0);
						    				this.feedbackService.doAdd(d_feedback);
						    			}
						    		}
						    	}*/
						    }
							break;
						default:
							break;
					}
				}
			}
		}
	}

}
