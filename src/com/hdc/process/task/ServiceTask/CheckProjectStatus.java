package com.hdc.process.task.ServiceTask;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.Project;
import com.hdc.service.IProjectService;
import com.hdc.service.ITaskInfoService;

@Component
public class CheckProjectStatus implements JavaDelegate {

	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String taskInfoId = (String) execution.getVariable("taskInfoId");
		if(StringUtils.isNotBlank(taskInfoId)) {
			List<Project> list = this.projectService.findByTaskInfo(new Integer(taskInfoId));
			Boolean flag = true;
			for(Project project : list) {
				if(!"APPROVAL_SUCCESS".equals(project.getStatus())) {
					flag =false;
					break;
				}
			}
			if(flag) {
				this.taskInfoService.doUpdateStatus(taskInfoId); //更改任务状体为办理中
				this.projectService.doUpdateStatus(taskInfoId);  //更改此任务下所有项目表的状态为-办理中
			}
		}
		
	}

}
