package com.hdc.process.task.ServiceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.service.IProjectService;

/**
 * 检查此projectId下的反馈是否都被采纳，如果是则允许申请办结。
 * @author ZML
 *
 */
@Component
public class CheckFeedback implements JavaDelegate {

	@Autowired
	private IProjectService projectService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String projectId = (String) execution.getVariable("projectId");
		if(StringUtils.isNotBlank(projectId)) {
			
		}
	}

}
