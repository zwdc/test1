package com.hdc.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IFeedbackFrequencyService;

/**
 * 反馈频度控制器
 * @author liuyadi
 *
 */

@Controller
@RequestMapping("/feedbackFrequency")
public class FeedbackFrequencyController {
	
	@Autowired
	private IFeedbackFrequencyService feedbackFrequencyService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "feedbackFrequency/list_fbFrequency";
	}
	
	/**
	 * 跳转更新或添加页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("feedbackFrequency/main_fbFrequency");
		if(id != null) {
			FeedbackFrequency f = this.feedbackFrequencyService.findById(id);
			mv.addObject("feedback", f);
		}
		return mv;
	}
	
	/**
	 * 获取分页数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<FeedbackFrequency> getList(Parameter param) throws Exception {
		Page<FeedbackFrequency> page = new Page<FeedbackFrequency>(param.getPage(), param.getRows());
		this.feedbackFrequencyService.getListPage(param, page);
		return new Datagrid<FeedbackFrequency>(page.getTotal(), page.getResult());
	}
	
	/**
	 * 添加或修改
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(FeedbackFrequency f) throws Exception {
		Message message = new Message();
		Integer id = f.getId();
		try {
			if(id == null) {
				f.setIsDelete(0);
				f.setCreateDate(new Date());
				this.feedbackFrequencyService.doAdd(f);
			} else {
				this.feedbackFrequencyService.doUpdate(f);
			}
			message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 详情页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/details/{id}")
	public ModelAndView details(@PathVariable("id") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("feedbackFrequency/details_fbFrequency");
		mv.addObject("f", this.feedbackFrequencyService.findById(id));
		return mv;
	}
	
}
