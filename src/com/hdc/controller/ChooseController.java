package com.hdc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.User;
import com.hdc.service.IGroupService;
import com.hdc.service.IUserService;

@Controller
@RequestMapping(value = "/choose")
public class ChooseController {
	
	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 跳转到选择页面
	 * @param taskDefKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toChooseGroup")
	public ModelAndView toChooseGroup(@RequestParam("taskDefKey") String taskDefKey) throws Exception{
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
	
	/**
	 * 跳转选择候选人页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/toChooseUser")
	public ModelAndView toChooseUser(@RequestParam("multiSelect") boolean multiSelect, @RequestParam("key") String key) throws Exception{
		ModelAndView mv = new ModelAndView("choose/user/choose_user");
		List<Group> groupList = this.groupService.getGroupList();
		mv.addObject("key", key);
		mv.addObject("multiSelect", multiSelect);
		mv.addObject("groupList", groupList);
		return mv;
	}
	
	
	/**
	 * 在tabs中根据groupId显示用户列表
	 * @return
	 */
	@RequestMapping(value = "/toShowUser")
	public ModelAndView toShowUser(
			@RequestParam("groupId") String groupId,
			@RequestParam("multiSelect") boolean multiSelect, 
			@RequestParam("key") String key){
		ModelAndView mv = new ModelAndView("choose/user/show_user");
		mv.addObject("groupId", groupId);
		mv.addObject("multiSelect", multiSelect);
		mv.addObject("key", key);
		return mv;
	}
	
	/**
	 * 根据groupId显示人员列表
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chooseUser")
	@ResponseBody
	public Datagrid<Object> chooseUser(Parameter param, @RequestParam(value = "groupId", required = false) String groupId) throws Exception{
		Page<User> page = new Page<User>(param.getPage(), param.getRows());
		this.userService.getUserByGroupId(groupId, param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		
		for(User user: page.getResult()){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("group", user.getGroup().getName());
			map.put("role", user.getRole().getName());
			map.put("registerDate", user.getRegisterDate());
			jsonList.add(map);
		}
		Datagrid<Object> dataGrid = new Datagrid<Object>(page.getTotal(), jsonList);
		return dataGrid;
		
	}
}
