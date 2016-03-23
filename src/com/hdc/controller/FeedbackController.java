package com.hdc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.ITaskInfoService;
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
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	/**
	 * 跳转添加或修改页面
	 * @param id
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(
				@RequestParam(value = "id", required = false) Integer id, 
				@RequestParam(value = "taskInfoId", required = false) Integer taskInfoId) throws Exception {
		ModelAndView mv = new ModelAndView("feedback/main_feedback");
		FeedbackRecord feedback = new FeedbackRecord();
		if(id != null) {
			feedback = this.feedbackService.findById(id);
		}
		if(taskInfoId != null) {
			TaskInfo taskInfo = this.taskInfoService.findById(taskInfoId);
//			feedback.setTaskInfo(taskInfo);
		}
		mv.addObject("feedback", feedback);
		return mv;
	}
	
	/**
	 * 添加或修改
	 * @param feedback
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(
				FeedbackRecord feedback, 
				@RequestParam(value = "taskId", required = false) String taskId,
				@RequestParam("file") MultipartFile file,
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		try {
			this.feedbackService.doCompleteTask(feedback, taskId, file, request);
			message.setMessage("操作成功!");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败!");
			throw e;
		}
		return message;
	}
	
	/**
	 * 查看taskInfoId下的所有反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detailsTab/{taskInfoId}")
	public ModelAndView detailsTab(@PathVariable("taskInfoId") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("feedback/list_feedback");
		List<FeedbackRecord> list = this.feedbackService.findByTaskId(id);
		mv.addObject("list", list);		
		return mv;
	} 
	
}
