package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Comments;
import com.hdc.entity.FeedbackDelay;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackDelayService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.UserUtil;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;

@Service
public class FeedbackDelayServiceImpl implements IFeedbackDelayService {

	@Autowired
	private IBaseService<FeedbackDelay> baseService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private ITaskSourceService taskResourceService;
	
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Autowired
	private IProcessService processService;
	
	@Override
	public void doStartProcess(FeedbackDelay feedbackDelay) throws Exception {
		feedbackDelay.setStatus(ApprovalStatus.PENDING.toString());
		Serializable id = this.baseService.add(feedbackDelay);
		FeedbackRecord feedback = this.feedbackService.findById(feedbackDelay.getFeedback().getId());
		
		TaskInfo taskInfo=feedback.getProject().getTaskInfo();
		//给用户提示任务
		User user=UserUtil.getUserFromSession();
		ProcessTask processTask=new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("反馈延期申请已提交，等待审核！");
		processTask.setUrl("/feedbackDelay/toApproval?delayId="+id.toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("projectId", feedback.getProject().getId().toString());
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalFeedback", taskInfo.getId().toString(), vars);	

	}

	@Override
	public void doApproval(Integer feedbackId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		FeedbackDelay feedbackDelay = this.findById(feedbackId);
		if(isPass) {
			feedbackDelay.setStatus(ApprovalStatus.APPROVAL_SUCCESS.toString());
		} else {
			feedbackDelay.setStatus(ApprovalStatus.APPROVAL_FAILED.toString());
		}
		this.baseService.update(feedbackDelay);
		
		// 评论,可记录每次审核意见
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(feedbackId);
		comments.setBusinessForm(BusinessForm.FEEDBACK_FORM.toString());
		variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
	}

	@Override
	public FeedbackDelay findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackDelay.class, id);
	}

}
