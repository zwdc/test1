package com.hdc.process.task.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.ProcessTask;
import com.hdc.service.IProcessTaskService;

/**
 * 动态更新事项表(ProcessTask)中的 taskId等信息
 * @author ZML
 *
 */
@Component
public class ProcessTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2200736398161972540L;
	
	@Autowired
	private IProcessTaskService processTaskService;

	@Override
	public void notify(DelegateTask delegateTask) {
		String processTaskId = (String) delegateTask.getVariable("processTaskId");
		try {
			if(StringUtils.isNotBlank(processTaskId)){
				ProcessTask processTask = this.processTaskService.findById(new Integer(processTaskId));
				processTask.setTaskId(delegateTask.getId());
				processTask.setExecutionId(delegateTask.getExecutionId());
				processTask.setProcessInstanceId(delegateTask.getProcessInstanceId());
				this.processTaskService.doUpdate(processTask);
				delegateTask.setVariable("taskTitle", processTask.getTaskTitle());
				delegateTask.setVariable("title", processTask.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
