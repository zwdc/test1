package com.hdc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Comments;
import com.hdc.entity.Datagrid;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfoType;
import com.hdc.service.ICommentsService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProjectService;
import com.hdc.util.Constants;
import com.hdc.util.Constants.BusinessForm;

/**
 * 任务交办管理器
 * @author ZML
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private ICommentsService commentsService;
	
	
	/**
	 * 根据标识跳转到不同页面
	 * @param type
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(@RequestParam("type") Integer type) {
		if(type == 1) {
			//待签收
			return "project/list_c_project";
		} else if(type == 2) {
			//办理中
			return "project/list_h_project";
		} else if(type == 3) {
			//已办结
			return "project/list_e_project";
		}
		return null;
	}
	 /**
     * 检查是否可以签收异议
     * @param feedbackId
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkTaskInfoType/{projectId}")
    @ResponseBody
	public Message checkTaskInfoType(@PathVariable("projectId") Integer projectId)
					throws Exception {
		Message message = new Message();
		if(projectId == null) {
			message.setMessage("获取反馈对象失败");
		} else {
			Project project=this.projectService.findById(projectId);
			TaskInfoType taskInfoType=project.getTaskInfo().getTaskSource().getTaskInfoType();
			if(taskInfoType.getIsRefuse()==0){
				message.setTitle("提示");
				message.setMessage(taskInfoType.getName()+"，不能提出异议！");
				message.setStatus(false);
			}else{
				message.setStatus(true);
			}
		}		
		return message;
    }
	
    /**
     * 检查是否可以申请办结
     * @param feedbackId
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkIfFinished/{projectId}")
    @ResponseBody
	public Message checkIfFinished(@PathVariable("projectId") Integer projectId)
					throws Exception {
		Message message = new Message();
		if(projectId == null) {
			message.setMessage("获取反馈对象失败");
		} else {
			Project project=this.projectService.findById(projectId);
			if(!Constants.ProjectStatus.CAN_BE_FINISHED.toString().equals(project.getStatus())){//如果不是可办结状态，就反馈false
				message.setTitle("提示");
				message.setStatus(false);
				message.setMessage("存在未采用的反馈信息，不能申请办结");
			}
		}
		return message;
//			Set<FeedbackRecord> fbrList=project.getFbrList();
//			Iterator<FeedbackRecord> it=fbrList.iterator();
//			while(it.hasNext()){
//				String status=it.next().getStatus();
//				if(!Constants.FeedbackStatus.ACCEPT.toString().equals(status)){
//					message.setStatus(false);
//					break;
//				}
//			}
    	
    }
    
	/**
	 * 根据type判断页面 签收页面、审批页面、修改页面,显示的是project详情
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/toProject/{type}")
	public ModelAndView toProject(@PathVariable("type") String type, @RequestParam("projectId") Integer projectId) throws Exception {
		ModelAndView mv = new ModelAndView();
		Project project = this.projectService.findById(projectId);
		if(project!=null){
			mv.addObject("taskInfo", project.getTaskInfo());
			mv.addObject("project", project);
			if("modify".equals(type)) {
				mv.setViewName("project/modify_project");
			} else if("claim".equals(type)) {
				//签收任务
				mv.setViewName("project/claim_project");
				//this.putFbListToProject(mv, project);
			} else if("approval".equals(type)) {
				mv.setViewName("project/approval_project");
			} else if("details".equals(type)) {
				//查看任务详情
				mv.setViewName("project/details_project");
				//this.putFbListToProject(mv, project);
			} else if("feedback".equals(type)) {
				//任务反馈
				mv.setViewName("project/feedback_project");
				//this.putFbListToProject(mv, project);
			} else if("approval_refuse".equals(type)) {
				mv.setViewName("project/approval_refuse_project");
			} else if("change_failed".equals(type)) {
				//拒签操作被驳回的页面
				List<Comments> commentsList = this.commentsService.findComments(projectId, BusinessForm.PROJECT_FORM.toString());
				mv.addObject("commentsList", commentsList);
				mv.setViewName("project/change_failed_project");
			} else if("complete".equals(type)) {
				//审批申请办结页面
				mv.setViewName("project/approval_complete");
				//this.putFbListToProject(mv, project);
			} else if("modifyComplete".equals(type)) {
				//申请办结被驳回的页面
				List<Comments> commentsList = this.commentsService.findComments(projectId, BusinessForm.PROJECT_COMPLETE.toString());
				mv.addObject("commentsList", commentsList);
				mv.setViewName("project/failed_complete");
				//this.putFbListToProject(mv, project);
			}
		}		
    	return mv;
	}
//	/*
//	 * 将反馈列表添加到项目内容表  ——feedback的获取还得用url方法，直接显示的不行
//	 * 
	
//	 */
//	private void putFbListToProject(ModelAndView mv,Project project) throws Exception{	
//    	if(project != null) {
//    		//以下开始装载项目下的反馈列表
//			List<Object> jsonList=new ArrayList<Object>();
//			int fbWL=-1;
//			Date currentDate=new Date();
//			for(FeedbackRecord fb:project.getFbrList()){
//				Map<String, Object> map=new HashMap<String, Object>();
//				map.put("id", fb.getId());				
//				if(currentDate.before(fb.getFeedbackStartDate()) && fb.getStatus()==null){
//					fbWL=0;//未到反馈期
//				}else if(currentDate.after(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null&&fb.getStatus()==null){
//					fbWL=2;//红色警告
//				}else if(currentDate.after(fb.getFeedbackStartDate())&&currentDate.before(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null){
//					fbWL=1;//黄色警告
//				}
//				map.put("warningLevel", fbWL);
//				map.put("feedbackStartDate", fb.getFeedbackStartDate());
//				map.put("feedbackEndDate", fb.getFeedbackEndDate());
//				map.put("groupName", fb.getProject().getGroup().getName());
//				if(fb.getFeedbackUser()!=null){
//					map.put("feedbackUser", fb.getFeedbackUser().getName());
//				}else{
//					map.put("feedbackUser", "--");
//				}				
//				map.put("feedbackDate", fb.getFeedbackDate());
//				map.put("status", fb.getStatus());
//				map.put("refuseCount", fb.getRefuseCount());
//				map.put("delayCount", fb.getDelayCount());
//				jsonList.add(map);
//			}
//			Datagrid<Object> fbList=new Datagrid<Object>(jsonList.size(),jsonList);
//			Gson gson=new Gson();
//			//在gson转换ArrayList的时候，不能有懒加载的对象		
//			mv.addObject("feedback",gson.toJson(fbList));
//		}
//	}
	
	/**
	 * 更新操作--添加建议
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Message update(@RequestParam("projectId") String projectId, @RequestParam("suggestion") String suggestion) throws Exception {
		Message message = new Message();
		try {
			this.projectService.doUpdateById(projectId, suggestion);
			message.setMessage("更新成功！");
			message.setData(projectId);
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("更新失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 根据type获取不同数据
	 * @param param
	 * @param type  1 未签收     2办理中    3已办结
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Map<String, Object>> getList(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(param.getPage(), param.getRows());
		List<Map<String, Object>> list = this.projectService.getProjectList(param, type, page);
		List<Map<String,Object>> jsonList=new ArrayList<Map<String,Object>>(); 
		int fbWL=-1;
		Date currentDate=new Date();
		if(type==1){//显示待签收的项目页面
			for(Map<String, Object> map:list){
				Project project=this.projectService.findById(Integer.valueOf(map.get("ID").toString()));
				if(project.getTaskInfo().getClaimLimitDate().before(currentDate)&&"WAIT_FOR_CLAIM".equals(project.getStatus())){
					fbWL=1; //在待签收的状态下，逾期
				}
				map.put("claim_limit_date", project.getTaskInfo().getClaimLimitDate());
				map.put("warningLevel", fbWL);
				jsonList.add(map);
			}
			
		}else if(type==2){//显示办理中的项目页面
			for(Map<String, Object> map:list){
				Project project=this.projectService.findById(Integer.valueOf(map.get("ID").toString()));
				Set<FeedbackRecord> fbSet=project.getFbrList();
			    Iterator<FeedbackRecord> iter = fbSet.iterator();
			    FeedbackRecord fbr=null;
			    boolean flag=false;
				while(iter.hasNext()){
					fbr=(FeedbackRecord)iter.next();
					if(fbr.getFeedbackDate()==null && fbr.getStatus()==null){
						flag=true;
						break;
					}
				 }	
				if(flag==true){
					if(currentDate.before(fbr.getFeedbackStartDate()) && fbr.getStatus()==null){
						fbWL=0;//未到反馈期
					}else if(currentDate.after(fbr.getFeedbackEndDate())&&fbr.getFeedbackDate()==null&&fbr.getStatus()==null){
						fbWL=2;//红色警告
					}else if(currentDate.after(fbr.getFeedbackStartDate())&&currentDate.before(fbr.getFeedbackEndDate())&&fbr.getFeedbackDate()==null){
						fbWL=1;//黄色警告
					}
					map.put("warningLevel", fbWL);			
					map.put("fbStatus", fbr.getStatus());
				}

				jsonList.add(map);
			}
		}else{//显示已办结的项目页面
			jsonList = list;
		}
		
		return new Datagrid<Map<String, Object>>(page.getTotal(), jsonList);
	}
	
	/**
	 * 获取不同类型的project数量
	 * @param type 1 未签收     2办理中    3已办结
	 * @return
	 */
	@RequestMapping("/getProjectCount")
	@ResponseBody
	public Integer getProjectCount(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(param.getPage(), param.getRows());
		this.projectService.getProjectList(param, type, page);
		return page.getTotal();
	}
	
	/**
	 * 签收任务交办表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/claimProject/{projectId}")
	@ResponseBody
	public Message claimProject(@PathVariable("projectId") String projectId) throws Exception {
		Message message = new Message();
		try {
			if(StringUtils.isNotBlank(projectId)) {
				Boolean flag = this.projectService.doClaimProject(projectId);
				if(flag) {
					message.setMessage("签收成功！");		
				} else {
					message.setMessage("该任务已被其他人签收！");
				}
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("签收失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping("/callApproval")
	@ResponseBody
	public Message callApproval(@RequestParam("projectId") Integer projectId, @RequestParam("suggestion") String suggestion) throws Exception {
		Message message = new Message();
		try {
			this.projectService.doStartProcess(projectId, suggestion);
			message.setMessage("操作成功！");
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
            	message.setMessage("没有部署流程，请联系管理员在[流程定义]中部署相应流程文件！");
            } else {
            	message.setMessage("启动流程失败，系统内部错误！");
            }
            throw e;
        } catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
		return message;
	}
	
    /**
     * 审批
     * @param projectId
     * @param isPass
     * @param taskId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approval")
   	@ResponseBody
   	public Message approval(
   			@RequestParam("projectId") Integer projectId, 
   			@RequestParam("isPass") boolean isPass,
   			@RequestParam("taskId") String taskId, 
   			@RequestParam("comment") String comment)
   					throws Exception {
   		Message message = new Message();
   		try {
   			this.projectService.doApproval(projectId, isPass, taskId, comment);
   			message.setMessage("审批完成！");
   		} catch (Exception e) {
   			message.setStatus(Boolean.FALSE);
   			message.setMessage("审批失败！");
   		}
   		return message;
    }
    
    /**
     * 审批拒绝签收
     * @param projectId
     * @param oldGroupId	//旧的单位
     * @param groupId		//新的单位
     * @param isPass
     * @param taskId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping("/approvalRefuse")
    @ResponseBody
    public Message approvalRefuse(
    		@RequestParam("projectId") Integer projectId, 
    		@RequestParam("oldHostGroup") Integer oldGroupId,
    		@RequestParam("hostGroup") Integer groupId,
   			@RequestParam("isPass") boolean isPass,
   			@RequestParam("taskId") String taskId, 
   			@RequestParam("comment") String comment) throws Exception {
   		Message message = new Message();
   		try {
   			this.projectService.doApprovalRefuse(projectId, oldGroupId, groupId, isPass, taskId, comment);
   			message.setMessage("审批完成！");
   		} catch (Exception e) {
   			message.setStatus(Boolean.FALSE);
   			message.setMessage("审批失败！");
   		}
   		return message;
    }
    
   /**
    * 完成任务
    * @param projectId
    * @param suggestion
    * @param taskId
    * @return
    * @throws Exception
    */
    @RequestMapping("/completeTask")
    @ResponseBody
    public Message completeTask(
    			@RequestParam("projectId") Integer projectId, 
    			@RequestParam("suggestion") String suggestion, 
    			@RequestParam(value = "taskId", required = false) String taskId) throws Exception {
    	Message message = new Message();
    	try {
    		this.projectService.doCompleteTask(projectId, suggestion, taskId);
    		message.setMessage("申请成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("申请失败!");
			throw e;
		}
    	return message;
    }
    
    /**
     * 跳转到拒签页面
     * @return
     */
    @RequestMapping("/toRefuse")
    @ResponseBody
    public ModelAndView toRefuse() {
    	ModelAndView mv = new ModelAndView("project/refuse_project");
    	return mv;
    }
    
    /**
     * 拒签收
     * @param projectId
     * @param refuseReason
     * @return
     * @throws Exception 
     */
    @RequestMapping("/refuse")
    @ResponseBody
    public Message refuse(@RequestParam("projectId") Integer projectId, @RequestParam("refuseReason") String refuseReason) throws Exception {
    	Message message = new Message();
    	try {
			this.projectService.doRefuseProject(projectId, refuseReason);
			message.setMessage("反馈成功！");
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
            	message.setMessage("没有部署流程，请联系管理员在[流程定义]中部署相应流程文件！");
            } else {
            	message.setMessage("启动流程失败，系统内部错误！");
            }
            throw e;
        } catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
    	return message;
    }
    
    /**
     * 查看驳回申请点确认完成任务
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping("/completeApprovalFailed")
    @ResponseBody
    public Message completeApprovalFailed(
			@RequestParam(value = "taskId", required = false) String taskId) throws Exception {
    	Message message = new Message();
    	try {
    		this.projectService.doCompleteApprovalFailed(taskId);
    		message.setMessage("已确认");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("确认失败!");
			throw e;
		}
    	return message;
    }
    
    
    /**
     * 查看驳回的办结申请 完成任务重新申请
     */
    public Message completeCompletionTask(
    		@RequestParam("projectId") Integer projectId, 
			@RequestParam("taskId") String taskId) throws Exception {
    	Message message = new Message();
    	try {
    		
    		message.setMessage("已确认");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("确认失败!");
			throw e;
		}
    	return message;
    }
    
    /**
     * 申请办结
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping("/completion/{projectId}")
    @ResponseBody
	public Message applyForCompletion(@PathVariable("projectId") Integer projectId) throws Exception {
    	Message message = new Message();
		List<FeedbackRecord> list = this.feedbackService.findNoAccept(projectId.toString());
		if(list.size() > 0) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("请确保所有反馈信息都被采纳后，才可以申请办结！");
		} else {
			this.projectService.doStartComplete(projectId);
			message.setMessage("申请成功！");
		}
		return message;
	}
    
    /**
     * 审批办结申请
     * @param projectId
     * @param isPass
     * @param taskId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping("/approvalComplete")
    @ResponseBody
    public Message approvalComplete(
    		@RequestParam("projectId") Integer projectId, 
   			@RequestParam("isPass") boolean isPass,
   			@RequestParam("taskId") String taskId, 
   			@RequestParam("comment") String comment) throws Exception {
   		Message message = new Message();
   		try {
   			this.projectService.doApprovalComplete(projectId, isPass, taskId, comment);
   			message.setMessage("审批完成！");
   		} catch (Exception e) {
   			message.setStatus(Boolean.FALSE);
   			message.setMessage("审批失败！");
   		}
   		return message;
    }
    
}
