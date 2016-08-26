package com.hdc.controller;

import org.activiti.engine.ActivitiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.FeedbackDelay;
import com.hdc.entity.Message;
import com.hdc.service.IFeedbackDelayService;

@Controller
@RequestMapping("/feedbackDelay")
public class FeedbackDelayController {

	@Autowired
	private IFeedbackDelayService feedbackDelayService;
	
	/**
	 * 申请延期反馈页面
	 * @return
	 */
	@RequestMapping("/toAdd")
	public ModelAndView toAdd() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("feedbackDelay/add_feedback");
		return mv;
	}
	
    /**
     * 跳转到审批页面
     * @param taskInfoId
     * @return
     * @throws Exception
     */
    @RequestMapping("/toApproval")
    public ModelAndView toApproval(@RequestParam(value = "feedbackId", required = false) Integer feedbackId) throws Exception {
    	ModelAndView mv = new ModelAndView("feedbackDelay/approval_delay");
    	FeedbackDelay feedbackDelay = this.feedbackDelayService.findById(feedbackId);
    	mv.addObject("delay", feedbackDelay);
    	return mv;
    }
    
	/**
     * 申请延期反馈
     * @return
     * @throws Exception
     */
    @RequestMapping("/callApproval")
    @ResponseBody
    public Message callApproval(FeedbackDelay feedbackDelay) throws Exception {
    	Message message = new Message();
    	try {
    		this.feedbackDelayService.doStartProcess(feedbackDelay);
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
     * 审批延迟反馈操作
     * @param delayId
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
			@RequestParam("delayId") Integer delayId, 
			@RequestParam("isPass") boolean isPass,
			@RequestParam("taskId") String taskId, 
			@RequestParam("comment") String comment)
					throws Exception {
		Message message = new Message();
		try {
			this.feedbackDelayService.doApproval(delayId, isPass, taskId, comment);
			message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("审批延迟反馈出错！");
		}
		return message;
    }
}
