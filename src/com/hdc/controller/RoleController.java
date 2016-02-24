package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Group;
import com.hdc.entity.GroupAndResource;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IGroupAndResourceService;
import com.hdc.service.IGroupService;
import com.hdc.util.BeanUtils;

/**
 * 角色(职位)控制器
 * @author ZML
 *
 */

@Controller
@RequestMapping("/group")
public class RoleController {

	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IGroupAndResourceService garService;
	
	
	/**
	 * 跳转到添加或修改组页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMain")
	public String toMain() throws Exception{
		return "groupResource/main_group";
	}
	
	/**
	 * 跳转角色权限管理页面
	 * @return
	 */
	@RequestMapping(value ="/permissionAssignment")
	public String permissionAssignment() throws Exception{
		return "groupResource/group_and_resource";
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
		try {
			this.groupService.saveOrUpdate(group);
			message.setStatus(Boolean.TRUE);
			message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
		
		
		return message;
	}
	
	/**
	 * 加载角色信息
	 * @return
	 */
	@RequestMapping("/getGroupList")
	@ResponseBody
	public Datagrid<Group> getList(Parameter param) throws Exception{
		Page<Group> page = new Page<Group>(param.getPage(), param.getRows());
		this.groupService.getGroupListPage(param, page);
		Datagrid<Group> dataGrid = new Datagrid<Group>(page.getTotal(), page.getResult());
		return dataGrid;
	}
	
	/**
	 * 关联角色权限
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getGroupPermission")
	@ResponseBody
	public List<GroupAndResource> getGroupPermission(@RequestParam("groupId") Integer groupId) throws Exception{
		List<GroupAndResource> garList = this.garService.getResource(groupId);
		return garList;
	}
	
	/**
	 * 保存权限信息
	 * @param groupId
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/savePermission")
	@ResponseBody
	public Message savePermission(@RequestParam("groupId") Integer groupId, @RequestParam("resourceIds[]") String[] resourceIds) throws Exception {
		if(!BeanUtils.isBlank(groupId)){
			this.garService.doDelByGroup(groupId);
			for(String resourceId: resourceIds){
				GroupAndResource rar = new GroupAndResource();
				rar.setGroupId(groupId);
				rar.setResourceId(new Integer(resourceId));
				this.garService.doAdd(rar);
			}
		}else{
			return new Message(Boolean.FALSE, "保存失败，请选择用户组！");
		}
		return new Message(Boolean.TRUE, "保存成功！");
	}
	
	/**
	 * 删除角色和相应权限
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Message delete(@RequestParam("id") Integer id) throws Exception{
		if(!BeanUtils.isBlank(id)) {
			this.garService.doDelByGroup(id);
			this.groupService.doDelete(this.groupService.getGroupById(id));
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
	/**
	 * 返回角色下拉列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/groupList")
	@ResponseBody
	public List<Group> groupList() throws Exception{
		List<Group> list=this.groupService.getGroupList();
		return list;
	}
}
