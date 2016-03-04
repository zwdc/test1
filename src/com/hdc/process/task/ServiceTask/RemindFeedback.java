package com.hdc.process.task.ServiceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.ITaskInfoService;
import com.hdc.service.IUserService;
import com.hdc.util.Constants.BusinessType;
import com.hdc.util.Constants.OperationType;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

@Component
public class RemindFeedback implements JavaDelegate {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String userId = (String)execution.getVariable("hostUser");
		Integer taskInfoId = (Integer) execution.getVariable("taskInfoId");
		if(StringUtils.isNotBlank(userId)) {
			GoEasy goEasy = new GoEasy("appkey");
			goEasy.publish("zwdc_user_"+userId, "您有将要到期尚未反馈的督察信息");
			
			//给办理人提示代办事项
			TaskInfo taskInfo = this.taskInfoService.findById(taskInfoId);
			User user = this.userService.getUserById(new Integer(userId));
			ProcessTask processTask = new ProcessTask();
			processTask.setUser_name(user.getName());
			processTask.setUser_id(user.getId());
			processTask.setBusinessType(BusinessType.IMPORTANT_FILE.toString());	//业务类型：重要文件
			processTask.setBusinessKey(taskInfo.getId());
			processTask.setTitle("拒绝签收的任务事项！");
			processTask.setUrl("/feedback/toMain?taskInfoId="+taskInfo.getId());
			//processTask.setBusinessForm(BusinessForm.QT_FILE.toString());			//业务表单：省政府文件
			processTask.setBusinessOperation(OperationType.ADD.toString());
			execution.setVariable("processTask", processTask);
		}
	}
	
}
