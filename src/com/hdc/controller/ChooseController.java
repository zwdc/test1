package com.hdc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IGroupService;

@Controller
@RequestMapping(value = "/toChoose")
public class ChooseController {
	
	@Autowired
	private IGroupService groupService;
	
	/**
	 * 跳转到选择页面
	 * @param taskDefKey
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toChooseGroup")
	public ModelAndView toChooseGroup(@RequestParam("key") String taskDefKey, Model model) throws Exception{
		ModelAndView mv = new ModelAndView("choose/group/choose_group");
		mv.addObject("taskDefKey", taskDefKey);
		return mv;
	}
	
	/**
	 * 选择组
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chooseGroup")
	@ResponseBody
	public Datagrid<Group> chooseGroup(Parameter param) throws Exception{
		Page<Group> page = new Page<Group>(param.getPage(), param.getRows());
		this.groupService.getGroupListPage(param, page);
		return new Datagrid<Group>(page.getTotal(), page.getResult());
		
	}
}
