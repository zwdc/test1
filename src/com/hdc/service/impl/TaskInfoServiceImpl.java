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
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
		//启动审批流程
		String processInstanceId = this.processService.startApproval("ApprovalTaskInfo", taskInfo.getId().toString(), vars);	
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
