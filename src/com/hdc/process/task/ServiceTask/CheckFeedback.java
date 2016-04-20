package com.hdc.process.task.ServiceTask;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.FeedbackRecord;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProjectService;
import com.hdc.util.Constants.ProjectStatus;

/**
 * 检查此projectId下的反馈是否都被采纳，如果是则允许申请办结。
 * @author ZML
 *
 */
@Component
public class CheckFeedback implements JavaDelegate {

	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private IProjectService projectService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String projectId = (String) execution.getVariable("projectId");
		if(StringUtils.isNotBlank(projectId)) {
			List<FeedbackRecord> list = this.feedbackService.findNoAccept(projectId);
			if(list.size() == 0) {
				this.projectService.doUpdateProjectStatus(projectId, ProjectStatus.CAN_BE_FINISHED.toString());
			}
		}
	}

}
