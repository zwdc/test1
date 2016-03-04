package com.hdc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.FeedbackRecord;
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
	 * 挑战添加或修改页面
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
			mv.addObject("feedback", feedback);
		}
		if(taskInfoId != null) {
			TaskInfo taskInfo = this.taskInfoService.findById(taskInfoId);
			feedback.setTaskInfo(taskInfo);
			mv.addObject("feedback", feedback);
		}
		return mv;
	}

}
