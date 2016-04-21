package com.hdc.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.Message;
import com.hdc.entity.Resource;
import com.hdc.entity.RoleAndResource;
import com.hdc.entity.User;
import com.hdc.service.IResourceService;
import com.hdc.service.IRoleAndResourceService;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.UserUtil;

/**
 * 资源控制器
 * @author ZML
 *
 */
@Controller
@RequiresPermissions("admin:*")
@RequestMapping(value = "/resource")
public class ResourceController {
	@Autowired
    private IResourceService resourceService;
	
	@Autowired
	private IRoleAndResourceService rarService;
	
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toListResource(){
		return "resource/list_resource";
	}
	
	/**
	 * 获取所有resource数据
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listResource")
	@ResponseBody
	public List<Resource> listResource() throws Exception{
		List<Resource> list = this.resourceService.getAllResource();
		return list;
	}
	
	/**
	 * 跳转添加或更新页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMain")
	public String toMain(Model model) throws Exception {
		return "resource/main_resource";
	}	
	
	/**
	 * 保存或更新
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(Resource resource) throws Exception {
		Message message = new Message();
		message.setStatus(Boolean.TRUE);
		Integer id = resource.getId();
		if(BeanUtilsExt.isBlank(id)){
			Integer parentId = resource.getParentId();
			List<Resource> list = this.resourceService.getResourceByPid(parentId);
			resource.setSort(list.size()+1);
			this.resourceService.doAdd(resource);
		} else {
			this.resourceService.doUpdate(resource);
		}
		
		message.setMessage("操作成功！");
		return message;
	}
	
	/**
	 * 跳转到查询页面
	 * @return
	 */
	@RequestMapping(value = "/resourceSearch")
	public String projectSearch(){
		return "resource/search";
	}
	
	/**
	 * 获取菜单树
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getMenuList")
	@ResponseBody
	public List<Resource> getMenuList() throws Exception{
		List<Resource> menuList = this.resourceService.getTreeMenu("menu");
		return menuList;
	} 
	
	/**
	 * 添加资源
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd")
	@ResponseBody
	public Message doAdd(Resource resource) throws Exception{
		resource.setIsDelete(0);
		Message message = new Message();
		try {
			this.resourceService.doAdd(resource);
			message.setStatus(Boolean.TRUE);
			message.setMessage("添加成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("添加失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 在zTree上添加节点
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTree")
	@ResponseBody
	public Message addTree(Resource resource) throws Exception{
		resource.setType("menu");
		resource.setPermission("admin:tree:*");
		resource.setIsDelete(0);
		Message message = new Message();
		User user = UserUtil.getUserFromSession();
		Serializable resourceId = this.resourceService.doAdd(resource);
		Integer roleId = user.getRole().getId();
		this.rarService.doAdd(new RoleAndResource(roleId, (Integer) resourceId));
		try {
			message.setStatus(Boolean.TRUE);
			message.setMessage("添加成功！");
			message.setData(resourceId);
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("添加失败！");
			throw e;
		}
		return message;
		
	}

	/**
	 * 更新
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(Resource resource) throws Exception{
		Message message = new Message();
		try {
			this.resourceService.doUpdate(resource);
			message.setStatus(Boolean.TRUE);
			message.setMessage("保存成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}

	/**
	 * 在zTree上更新节点
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateTreeName", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@RequestParam(value = "id") String id, @RequestParam("name") String name) throws Exception {
		Message message = new Message();
		try {
			Integer result = this.resourceService.doUpdateName(id, name);
			if(result > 0){
				message.setStatus(Boolean.TRUE);
				message.setMessage("保存成功！");
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("您所修改的内容不存在！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping(value = "/updateTreeSort", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdateSort(
			@RequestParam(value = "sortArr", required = false) String sortArr, 
			@RequestParam(value = "nodeArr", required = false) String nodeArr,
			@RequestParam(value = "parentArr", required = false) String parentArr) throws Exception {
		Message message = new Message();
		try {
			String[] nodes = nodeArr.split(",");
			String[] sorts = sortArr.split(",");
			String[] parents = parentArr.split(",");
			for(int i=0;i<nodes.length;i++){
				String id = nodes[i];
				String sortNum = sorts[i];
				String parent = parents[i];
				this.resourceService.doUpdateSort(id, parent, sortNum);
			}
			message.setStatus(Boolean.TRUE);
			message.setMessage("顺序已调整！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doDelete")
	@ResponseBody
	public Message doDelete(@RequestParam("id") Integer id) throws Exception {
		Message message = new Message();
		try {
			Integer result_r = this.resourceService.doDelete(id);
			if(result_r > 0){
				this.rarService.doDeleteByResource(id);
				message.setStatus(Boolean.TRUE);
				message.setMessage("成功删除 "+result_r+" 条数据,");
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("您所删除的内容不存在！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 锁定
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/whetherLock/{flag}")
	@ResponseBody
	public Message lock(@PathVariable("flag") String flag, @RequestParam("id") String id) throws Exception {
		Message message = new Message();
		message.setStatus(Boolean.TRUE);
		if( "lock".equals(flag) ) {
			this.resourceService.doUpdateIsDelete(id, 1);
			message.setMessage("锁定成功！");
		}else if( "unlock".equals(flag) ) {
			this.resourceService.doUpdateIsDelete(id, 0);
			message.setMessage("解锁成功！");
		}
		return message;
	}
}
