package com.hdc.process.task.ServiceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

public class RemindFeedback implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String userId = (String)execution.getVariable("hostUser");
		GoEasy goEasy = new GoEasy("appkey");
		goEasy.publish("zwdc_user_"+userId, "您有将要到期尚未反馈的督察信息");
	}
	
}
