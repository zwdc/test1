package com.hdc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Group;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.User;
import com.hdc.service.IGroupService;
import com.hdc.util.BeanUtils;

@Controller
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private IGroupService groupService;
	
	/**
	 * 获取所有组信息
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAll")
	public String toList(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
	
	/**
	 * 跳转到添加或修改组页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMain")
	public String toAdd() throws Exception{
		return "group/main_group";
	}
	
	/**
	 * 添加或更新
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(Group group) throws Exception {
		Message message = new Message();
		Integer id = group.getId();
		try {
			if(BeanUtils.isBlank(id)) {
				group.setIsDelete(0);
				group.setCreateDate(new Date());
				this.groupService.doAdd(group);
			} else {
				group.setUpdateDate(new Date());
				this.groupService.doUpdate(group);
			}
			message.setMessage("操作成功！");
			message.setStatus(Boolean.TRUE);
		} catch (Exception e) {
			message.setMessage("操作失败！");
			message.setStatus(Boolean.FALSE);
		}
		return message;
	}
	
	/**
	 * 候选人页面、添加用户页面使用
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllGroup")
	@ResponseBody
	public List<Group> getGroupList() throws Exception{
		List<Group> list = this.groupService.getGroupList();
		return list;
	}
	/**
	 * 加载列表数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listGroup")
	@ResponseBody
	public Datagrid<Group> listGroup(Parameter param) throws Exception{
		Page<Group> page = new Page<Group>(param.getPage(), param.getRows());
		this.groupService.getGroupListPage(param, page);
		return new Datagrid<Group>(page.getTotal(), page.getResult());
	}
	/**
	 * 删除组信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Message delete(@RequestParam("id") Integer id) throws Exception{
		Message message = new Message();
		if(!BeanUtils.isBlank(id)){
			Group group = this.groupService.getGroupById(id);
			group.setIsDelete(1);
			this.groupService.doUpdate(group);
			message.setStatus(Boolean.TRUE);
			message.setMessage("删除成功！");
		} else {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败，您所删除的内容不存在！");
		}
		return message;
	}
	/**
	 * 跳转到详情页面
	 * @return
	 */
	@RequestMapping(value = "/toDetails/{id}")
	public ModelAndView toDetails(@PathVariable("id") Integer id) throws Exception{
		ModelAndView mv = new ModelAndView("group/details_group");
		Group group=this.groupService.getGroupById(id);
		mv.addObject("group",group);
		return mv;
	}
	/**
	 * 跳转到查询页面
	 * @return
	 */
	@RequestMapping(value = "/groupSearch")
	public String groupSearch(){
		return "group/search";
	}
	
	/**
	 * 立项页面获取 牵头单位列表及人员信息
	 * @param groupIds
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("/getHostGroupList")
	@ResponseBody
	public List<Object> getList(@RequestParam("groupIds") String groupIds) throws NumberFormatException, Exception {
		List<Object> jsonList = new ArrayList<Object>();
		for(String groupId : groupIds.split(",")) {
			if(StringUtils.isNotBlank(groupId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				Group group = this.groupService.getGroupById(new Integer(groupId));
				String userNames = "";
				for(User user : group.getUser()) {
					userNames += user.getName();
				}
				map.put("groupId", group.getId());
				map.put("groupName", group.getName());
				map.put("userNames", userNames);
				jsonList.add(map);
			}
		}
		return jsonList;
	}
}
