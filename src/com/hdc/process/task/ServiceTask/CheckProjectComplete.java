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
import com.hdc.util.Constants.ProjectStatus;

/**
 * 查看是否所有的project都已办结，都已办结后则置TaskInfo的状态为已办结
 * @author ZML
 *
 */
@Component
public class CheckProjectComplete implements JavaDelegate {

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
				if(!"FINISHED".equals(project.getStatus())) {
					flag =false;
					break;
				}
			}
			if(flag) {
				this.taskInfoService.doUpdateStatus(taskInfoId, ProjectStatus.FINISHED.toString()); 	//更改任务状体为办理中
			}
		}
	}

}
