package com.hdc.process.task.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.BeanUtils;

/**
 * 动态更新事项表(TaskInfo)中的 actTaskId
 * @author ZML
 *
 */
@Component("taskInfoListener")
public class TaskInfoListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2200736398161972540L;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IProcessTaskService processTaskService;

	@Override
	public void notify(DelegateTask delegateTask) {
		Integer taskInfoId = (Integer) delegateTask.getVariable("taskInfoId");
		String processTaskId = (String) delegateTask.getVariable("processTaskId");
		try {
			if(!BeanUtils.isBlank(taskInfoId)){
				TaskInfo taskInfo = this.taskInfoService.findById(taskInfoId);
				taskInfo.setActTaskId(delegateTask.getId());
				this.taskInfoService.doUpdate(taskInfo);
			}
			if(StringUtils.isNotBlank(processTaskId)){
				ProcessTask processTask = this.processTaskService.findById(new Integer(processTaskId));
				processTask.setTaskId(delegateTask.getId());
				processTask.setExecutionId(delegateTask.getExecutionId());
				processTask.setProcessInstanceId(delegateTask.getProcessInstanceId());
				this.processTaskService.doUpdate(processTask);
				delegateTask.setVariable("processTask", processTask);
				delegateTask.setVariable("businessForm", processTask.getBusinessForm());
				delegateTask.setVariable("title", processTask.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
