package com.hdc.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hdc.entity.Comments;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.Constants.FeedbackStatus;
import com.hdc.util.UserUtil;

@Service
public class FeedbackRecordServiceImpl implements IFeedbackRecordService {

	@Autowired
	private IBaseService<FeedbackRecord> baseService;
	
	@Autowired
	private IProcessService processService;
	
	@Autowired
    private ITaskSourceService taskResourceService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Override
	public List<FeedbackRecord> getListPage(Parameter param,
			Page<FeedbackRecord> page) throws Exception {
		return this.baseService.findListPage("FeedbackRecord", param, null, page, true);
	}
	
	@Override
	public List<FeedbackRecord> getAllList() throws Exception {
		String hql = "from FeedbackRecord where isDelete = 0 order by createDate desc";
		return this.baseService.find(hql);
	}
	
	@Override
	public FeedbackRecord findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackRecord.class, id);
	}

	@Override
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception {
		String hql = "from FeedbackRecord where taskInfo.id = " + id +" order by createDate ASC";
		return this.baseService.find(hql);
	}

	@Override
	public Serializable doAdd(FeedbackRecord feedback) throws Exception {
		return this.baseService.add(feedback);
	}

	@Override
	public void doUpdate(FeedbackRecord feedback) throws Exception {
		this.baseService.update(feedback);
	}
	
//	@Override
//	public void doUpdate(FeedbackRecord feedback) throws Exception {
//		String situation=feedback.getSituation();
//		String problem=feedback.getProblems();
//		String hql="Update FeedbackRecord fbr set "
//				+ "fbr.situation=:situation,"
//				+ "fbr.problem=:problem"
//				+ "fbr.solutions=:solutions"
//				+ "fbr.fdaList=:fdaList"
//				+ "where fbr.id=:id";
//		this.baseService.executeHql(hql, params);
//	}

	@Override
	public List<FeedbackRecord> findByDate(Date beginDate, Date endDate)
			throws Exception {
		String hql = "from FeedbackRecord where createDate between :begin and :end";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("begin", beginDate);
		params.put("end", endDate);
		return this.baseService.find(hql);
	}

	@Override
	public void doCompleteTask(FeedbackRecord feedback, String taskId,
			MultipartFile file, HttpServletRequest request) throws Exception {
		Integer id = feedback.getId();
		if(id == null) {
			if(!BeanUtils.isBlank(file)) {
				/*String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				feedback.setFilePath(filePath);
				feedback.setFileName(file.getOriginalFilename());
				feedback.setUploadDate(new Date());*/
			}
			feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
			feedback.setIsDelay(0);		//是否迟报，得根据时间判断
			this.baseService.add(feedback);
			
		} else {
			this.baseService.update(feedback);
		}
		
		//如果有提示的代办任务，则完成任务。
		if(StringUtils.isNotBlank(taskId)) {
			this.processService.complete(taskId, null, null);
		}
		
	}
	
	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update FeedbackRecord set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public List<FeedbackRecord> findByProjectId(Integer projectId)
			throws Exception {
		String hql = "from FeedbackRecord where project.id = " + projectId +" order by createDate ASC";
		return this.baseService.find(hql);
	}

	@Override
	public void doStartProcess(FeedbackRecord feedback) throws Exception {
		feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
		this.baseService.update(feedback);
		TaskInfo taskInfo=feedback.getProject().getTaskInfo();
		//给用户提示任务
		User user=UserUtil.getUserFromSession();
		ProcessTask processTask=new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("反馈已提交，需要审核");
		processTask.setUrl("/feedback/toApproval?fbId="+feedback.getId().toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("taskInfoId", taskInfo.getId().toString());
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalFeedback", taskInfo.getId().toString(), vars);	
				
	}

	@Override
	public void doApproval(Integer feedbackId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		FeedbackRecord fbr = this.findById(feedbackId);
		if(isPass) {
			fbr.setStatus(FeedbackStatus.ACCEPT.toString()); //审批成功
		} else {
			fbr.setStatus(FeedbackStatus.RETURNED.toString()); //审批失败
			TaskInfo taskInfo=fbr.getProject().getTaskInfo();
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("反馈已被退回，请修改后重新提交！");
			processTask.setUrl("/feedback/toMain?fbId="+fbr.getId().toString()+"&action=feedback");
			Serializable id = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", id);
		}
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
	public void doCompleteTask(FeedbackRecord feedback, String taskId) throws Exception {
		//给秘书长提示代办任务
				feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
				this.baseService.update(feedback);
				TaskInfo taskInfo=feedback.getProject().getTaskInfo();
				Map<String, Object> variables = new HashMap<String, Object>();		
				User user = UserUtil.getUserFromSession();
				ProcessTask processTask = new ProcessTask();
				processTask.setTaskTitle(taskInfo.getTitle());
				processTask.setApplyUserId(user.getId());
				processTask.setApplyUserName(user.getName());
				processTask.setTaskInfoId(taskInfo.getId());
				TaskSource taskSource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
				processTask.setTaskInfoType(taskSource.getTaskInfoType().getName());
				processTask.setTitle("反馈修改完成！需要重新审批!");
				processTask.setUrl("/feedback/toApproval?fbId="+feedback.getId().toString());
				Serializable processTaskId = this.processTaskService.doAdd(processTask);
				//初始化任务参数
				variables.put("processTaskId", processTaskId.toString());
				this.processService.complete(taskId, null, variables);
		
	}

}
