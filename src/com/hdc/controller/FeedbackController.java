package com.hdc.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.FeedbackAtt;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.util.Constants;
import com.hdc.util.UserUtil;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;
/**
 * 反馈控制器
 * @author zhao
 *
 */

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

	@Autowired
	private IFeedbackRecordService feedbackService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "feedback/list_feedback";
	}
	
	/**
	 * 获取分页数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(Parameter param) throws Exception {
		Page<FeedbackRecord> page = new Page<FeedbackRecord>(param.getPage(), param.getRows());		
		List<FeedbackRecord> fbList=this.feedbackService.getListPage(param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		int fbWL=0;
		Date currentDate=new Date();
		for(FeedbackRecord fb:fbList){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", fb.getId());
			
			if(currentDate.before(fb.getFeedbackStartDate())){
				fbWL=0;//未到反馈期
			}else if(currentDate.after(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null){
				fbWL=2;//红色警告
			}else if(currentDate.after(fb.getFeedbackStartDate())&&currentDate.before(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null){
				fbWL=1;//黄色警告
			}
			map.put("warningLevel", fbWL);
			map.put("feedbackStartDate", fb.getFeedbackStartDate());
			map.put("feedbackEndDate", fb.getFeedbackEndDate());
			map.put("groupName", fb.getProject().getGroup().getName());
			map.put("createUser", fb.getCreateUser().getName());
			map.put("feedbackDate", fb.getFeedbackDate());
			map.put("status", fb.getStatus());
			map.put("refuseCount", fb.getRefuseCount());
			map.put("delayCount", fb.getDelayCount());
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(),jsonList);
	}
	
	/**
	 * 跳转添加或修改页面
	 * @param id
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(
		     	@RequestParam(value = "action", required = false) String action,
				@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mv = null;
		if("edit".equals(action)){
			mv = new ModelAndView("feedback/main_feedback");			
		}else if("check".equals(action)){
			mv = new ModelAndView("feedback/approval_feedback");			
		}else if("detail".equals(action)){
			mv = new ModelAndView("feedback/details_feedback");			
		}else if("feedback".equals(action)){
			mv = new ModelAndView("feedback/do_feedback");			
		}else if("add".equals(action)){
			mv = new ModelAndView("feedback/main_feedback");
		}	
		if(id!=null){
			mv.addObject("feedback", this.feedbackService.findById(id));
		}		
		return mv;
	}
	/**
	 * 审核反馈 -- 暂时没用了
	 * @param feedback
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkFeedback")
	@ResponseBody
	public Message checkFeedback(
				FeedbackRecord feedback, 
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		Integer id = feedback.getId();
		try {
			if(id == null) {
				message.setMessage("获取反馈对象失败");
			} else {
				FeedbackRecord fbr=this.feedbackService.findById(id);
			    fbr.setStatus(feedback.getStatus()); //获取反馈状态，（反馈中 RUNNING、已退回 FAIL、已采用 SUCCESS）
			    //fbr.setSuggestion(feedback.getSuggestion());//获取建议意见
			    this.feedbackService.doUpdate(fbr);
				message.setData(id);
				message.setMessage("审核完成！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败!");
			throw e;
		}
		
		return message;
	}
	 /**
     * 审批反馈操作
     * @param taskInfoId
     * @param isPass
     * @param taskId
     * @param processInstanceId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approval")
	@ResponseBody
	public Message approval(
			@RequestParam("feedbackId") Integer feedbackId, 
			@RequestParam("isPass") boolean isPass,
			@RequestParam("taskId") String taskId, 
			@RequestParam("comment") String comment)
					throws Exception {
		Message message = new Message();
		try {
			this.feedbackService.doApproval(feedbackId, isPass, taskId, comment);
			message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("审批反馈时出错！");
		}
		return message;
    }
	/**
	 * 实施反馈
	 * @param feedback
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveFeedback")
	@ResponseBody
	public Message saveFeedback(
				FeedbackRecord feedback, 
				@RequestParam(value = "file", required = false) MultipartFile[] file,
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		int count=0;//上传文件计数
		Integer id = feedback.getId();
		Set<FeedbackAtt> fbaList=new HashSet<FeedbackAtt>();
		FeedbackRecord fbr=this.feedbackService.findById(id);
		String path1=fbr.getProject().getGroup().getId().toString();
		String path2=fbr.getProject().getId().toString();
		if(path1==null || path2==null){
			message.setMessage("上传路径错误，上传失败");
			return message;
		}
		try {
			if(file!=null){							
				for(int i=0;i<file.length;i++){
					try {
						if(!file[i].isEmpty()){
							String filePath = FileUploadUtils.upload(request, file[i], 
									Constants.FILE_PATH
									        +File.separator+path1
											+File.separator+path2
											+File.separator+id);
							FeedbackAtt fba=new FeedbackAtt();
							String[] fileExName=file[i].getOriginalFilename().split("\\.");
							fba.setUrl(filePath);
							fba.setName(file[i].getOriginalFilename());;
							fba.setUploadDate(new Date());
							fba.setType(fileExName[fileExName.length-1]);
							//子类把主类加一下，子类中才会有主类的ID外键；
							fba.setFdRecord(feedback);
							fbaList.add(fba);
							count++;
						}	
					} catch (Exception e) {
						message.setStatus(Boolean.FALSE);
						message.setTitle("操作失败！");
						if(e instanceof FileSizeLimitExceededException){
							Long actual = ((FileSizeLimitExceededException) e).getActualSize();
							Long permitted = ((FileSizeLimitExceededException) e).getPermittedSize();
							message.setMessage("上传失败！文件大小超过限制，最大上传："+getFileMB(permitted)+",实际大小："+getFileMB(actual));
						} else if (e instanceof InvalidExtensionException){
							message.setMessage("不能上传此文件类型,请重新选择文件上传！");
						}
					}	
				}
			}else{				
				//this.feedbackService.doCompleteTask(feedback, taskId, null, request);
			}
			if(id == null) {
				message.setMessage("获取反馈对象失败");
			} else {
			
			    fbr.setSolutions(feedback.getSituation());
			    fbr.setProblems(feedback.getProblems());
			    fbr.setSituation(feedback.getSituation());
			    fbr.setFdaList(fbaList);
				this.feedbackService.doUpdate(fbr);
				message.setData(id);
				message.setMessage("上传了"+count+"附件，反馈成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败!");
			throw e;
		}
		
		return message;
	}
	/**
	 * 添加或修改
	 * @param feedback
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(
				FeedbackRecord feedback, 
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		Integer id = feedback.getId();
		try {
			if(id == null) {
				this.feedbackService.doAdd(feedback);
				message.setMessage("添加成功！");
			} else {
				this.feedbackService.doUpdate(feedback);;
				message.setData(id);
				message.setMessage("反馈成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败!");
			throw e;
		}
		
		return message;
	}
    
	
	
	
	private String getFileMB(long byteFile){  
	   if(byteFile==0)  
	       return "0MB";  
	   long mb=1024*1024;  
	   return ""+byteFile/mb+"MB";  
	} 
	  /**
		 * 删除
		 * @param id
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/delete/{id}")
		@ResponseBody
		public Message delete(@PathVariable("id") Integer id) throws Exception {
			Message message = new Message();
			this.feedbackService.doDelete(id);
			message.setMessage("删除成功！");
			return message;
		}
	    
	/**
	 * 查看taskInfoId下的所有反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/details/{taskInfoId}")
	public ModelAndView detailsTab(@PathVariable("taskInfoId") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("feedback/list_feedback");
		List<FeedbackRecord> list = this.feedbackService.findByTaskId(id);
		mv.addObject("list", list);		
		return mv;
	} 
	
	/**
	 * 根据projectId查询反馈列表
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFeedbackByProject")
	@ResponseBody
	public List<Map<String, Object>> getList(@RequestParam("projectId") Integer projectId) throws Exception {
		List<FeedbackRecord> list = this.feedbackService.findByProjectId(projectId);
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		for(FeedbackRecord feedback : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String feedbackStartDate = sf.format(feedback.getFeedbackStartDate());
			String feedbackEndDate = sf.format(feedback.getFeedbackEndDate());
			map.put("id", feedback.getId());
			map.put("workPlanDate", feedbackStartDate+" - "+feedbackEndDate);
			map.put("workPlan", feedback.getWorkPlan());
			jsonList.add(map);
		}
		return jsonList;
	}
	
	/**
	 * 添加阶段性任务计划
	 * @param feedbackId
	 * @param workPlan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/workPlan/{feedbackId}")
	@ResponseBody
	public Message workPlan(@PathVariable("feedbackId") Integer feedbackId, @RequestParam(value = "workPlan", required = false) String workPlan) throws Exception {
		Message message = new Message();
		try {
			if(workPlan != null) {
				FeedbackRecord feedback = this.feedbackService.findById(feedbackId);
				feedback.setWorkPlan(workPlan);
				this.feedbackService.doUpdate(feedback);
				message.setMessage("修改成功！");
			} else {
				message.setData(0);
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("修改失败！");
		}
		return message;
	}
	/**
     * 申请反馈审核，可以采用也可以退回。
     * @return
     * @throws Exception
     */
    @RequestMapping("/callApproval")
    @ResponseBody
    public Message callApproval(FeedbackRecord feedback) throws Exception {
    	Message message = new Message();
    	try {
    		this.feedbackService.doStartProcess(feedback);
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
     * 跳转到反馈页面
     * @param taskInfoId
     * @return
     * @throws Exception
     */
    @RequestMapping("/toApproval")
    public ModelAndView toApproval(@RequestParam(value = "feedbackId", required = false) Integer feedbackId) throws Exception {
    	ModelAndView mv = new ModelAndView("feedback/approval_feedback");
    	FeedbackRecord feedback = this.feedbackService.findById(feedbackId);
    	mv.addObject("feedback", feedback);
    	return mv;
    }
    
//    /**
//	 * 签收任务交办表
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping("/claimProject/{projectId}")
//	@ResponseBody
//	public Message claimProject(@PathVariable("projectId") String projectId) throws Exception {
//		Message message = new Message();
//		try {
//			if(StringUtils.isNotBlank(projectId)) {
//				Boolean flag = this.projectService.doClaimProject(projectId);
//				if(flag) {
//					message.setMessage("签收成功！");
//				} else {
//					message.setMessage("该任务已被其他人签收！");
//				}
//			}
//		} catch (Exception e) {
//			message.setStatus(Boolean.FALSE);
//			message.setMessage("签收失败！");
//			throw e;
//		}
//		return message;
//	}
	
    
   
    
    /**
     * 完成任务
     * @param taskInfo
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping("/completeTask")
    @ResponseBody
    public Message completeTask(FeedbackRecord feedback, @RequestParam(value = "feedbackId", required = false) String feedbackId) throws Exception {
    	Message message = new Message();
    	try {
    		this.feedbackService.doCompleteTask(feedback, feedbackId);
    		message.setMessage("提交成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("提交失败!");
			throw e;
		}
    	return message;
    }
}
