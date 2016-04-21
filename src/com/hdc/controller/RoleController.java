package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Role;
import com.hdc.entity.RoleAndResource;
import com.hdc.service.IRoleAndResourceService;
import com.hdc.service.IRoleService;
import com.hdc.util.BeanUtilsExt;

/**
 * 角色(职位)控制器
 * @author ZML
 *
 */

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IRoleAndResourceService rarService;
	
	
	/**
	 * 跳转到添加或修改组页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMain")
	public String toMain() throws Exception{
		return "roleResource/main_role";
	}
	
	/**
	 * 跳转角色权限管理页面
	 * @return
	 */
	@RequestMapping(value ="/permissionAssignment")
	public String permissionAssignment() throws Exception{
		return "roleResource/role_and_resource";
	}
	
	/**
	 * 添加或更新
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(Role role) throws Exception {
		Message message = new Message();
		try {
			this.roleService.saveOrUpdate(role);
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
	@RequestMapping("/getRoleList")
	@ResponseBody
	public Datagrid<Role> getList(Parameter param) throws Exception{
		Page<Role> page = new Page<Role>(param.getPage(), param.getRows());
		this.roleService.getRoleListPage(param, page);
		Datagrid<Role> dataGrid = new Datagrid<Role>(page.getTotal(), page.getResult());
		return dataGrid;
	}
	
	/**
	 * 关联角色权限
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getRolePermission")
	@ResponseBody
	public List<RoleAndResource> getRolePermission(@RequestParam("roleId") Integer roleId) throws Exception{
		List<RoleAndResource> garList = this.rarService.getResource(roleId);
		return garList;
	}
	
	/**
	 * 保存权限信息
	 * @param roleId
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/savePermission")
	@ResponseBody
	public Message savePermission(@RequestParam("roleId") Integer roleId, @RequestParam("resourceIds[]") String[] resourceIds) throws Exception {
		if(!BeanUtilsExt.isBlank(roleId)){
			this.rarService.doDeleteByRole(roleId);
			for(String resourceId: resourceIds){
				RoleAndResource rar = new RoleAndResource();
				rar.setRoleId(roleId);
				rar.setResourceId(new Integer(resourceId));
				this.rarService.doAdd(rar);
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
		if(!BeanUtilsExt.isBlank(id)) {
			this.rarService.doDeleteByRole(id);
			this.roleService.doDelete(id.toString());
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
	@RequestMapping(value = "/roleList")
	@ResponseBody
	public List<Role> roleList() throws Exception{
		List<Role> list=this.roleService.getRoleList();
		return list;
	}
}
