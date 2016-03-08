package com.hdc.process.task.TaskListener;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskInfoService;
import com.hdc.service.IUserService;
import com.hdc.util.Constants.BusinessType;
import com.hdc.util.Constants.OperationType;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

@Component
public class RemindFeedback implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 674707000195946993L;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		String userId = (String)delegateTask.getVariable("hostUser");
		String taskInfoId = (String) delegateTask.getVariable("taskInfoId");
		try {
			if(StringUtils.isNotBlank(userId)) {
				//给办理人提示代办事项
				TaskInfo taskInfo = this.taskInfoService.findById(new Integer(taskInfoId));
				taskInfo.setActTaskId(delegateTask.getId());
				this.taskInfoService.doUpdate(taskInfo);
				ProcessTask processTask = new ProcessTask();
				processTask.setUser_name("系统管理");
				//processTask.setUser_id(user.getId());
				processTask.setBusinessType(BusinessType.IMPORTANT_FILE.toString());	//业务类型：重要文件
				processTask.setBusinessKey(taskInfo.getId());
				processTask.setTaskId(delegateTask.getId());
				processTask.setAssign(userId);
				processTask.setTitle("您有将要到期尚未反馈的督察信息");
				processTask.setUrl("/feedback/toMain?taskInfoId="+taskInfo.getId());
				//processTask.setBusinessForm(BusinessForm.QT_FILE.toString());			//业务表单：省政府文件
				processTask.setBusinessOperation(OperationType.ADD.toString());
				Serializable id = this.processTaskService.doAdd(processTask);
				delegateTask.setVariable("processTask", processTask);
				//推送消息
				GoEasy goEasy = new GoEasy("0cf326d6-621b-495a-991e-a7681bcccf6a");
				goEasy.publish("zwdc_user_"+userId, id.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
