package com.hdc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Role;
import com.hdc.entity.User;
import com.hdc.service.IRoleService;
import com.hdc.service.IUserService;
import com.hdc.service.impl.PasswordHelper;
import com.hdc.shiro.realm.UserRealm;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.Constants;
import com.hdc.util.UserUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@Autowired
    protected IRoleService roleService;
	
    @Autowired
    private PasswordHelper passwordHelper;
	
	@Autowired
    private SessionDAO sessionDAO;
	
	/**
	 * 跳转到用户列表页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toList")
	public String toList() throws Exception{
		return "user/list_user";
	}
	
	/**
	 * 用户列表
	 * @param page 当前第几页
	 * @param rows 每页显示记录数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> userList(Parameter param) throws Exception{
		Page<User> page = new Page<User>(param.getPage(), param.getRows());
		List<User> userList = this.userService.getUserList(param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(User user : userList){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("registerDate", user.getRegisterDate());
			map.put("locked", user.getIsDelete());
			map.put("staffId", user.getStaffId());
			map.put("groupId", user.getGroup().getId());
			map.put("group_name", user.getGroup().getName());
			map.put("roleId", user.getRole().getId());
			map.put("role_name", user.getRole().getName());
			map.put("dataPermission", user.getDataPermission());
			map.put("dataPermission", user.getDataPermission());
			map.put("passwd", user.getPasswd());
			map.put("salt", user.getSalt());
			map.put("isDelete", user.getIsDelete());
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	/**
	 * 跳转到添加用户页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
		return "user/add_user";
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @param synToActiviti  是否同步用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute("user") User user,
						@Value("#{APP_PROPERTIES['account.user.add.syntoactiviti']}") Boolean synToActiviti) throws Exception{
		Message message = new Message();
		user.setRegisterDate(new Date());
		user.setCreateDate(new Date());
		user.setIsDelete(0);
		String staffId = user.getStaffId();
		if(StringUtils.isNoneBlank(staffId)) {
			User u = this.userService.getUserByStaffId(staffId);
			if(u == null) {
				this.userService.doAdd(user, synToActiviti);
				message.setStatus(Boolean.TRUE);
				message.setMessage("添加成功！");
			} else {
				message.setStatus(Boolean.FALSE);
				message.setMessage("添加失败，此编制号已存在！");
			}
		} else {
			message.setStatus(Boolean.FALSE);
			message.setMessage("添加失败！");
		}
		return message;
		
	}
	
	/**
	 * 跳转到添加页面
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") Integer id) throws Exception{
		return "user/update_user";
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/doUpdate")
    @ResponseBody
	public Message doUpdate(
				User user, 
				@RequestParam("rePasswd") String rePasswd,
				@RequestParam("password") String password,
				@RequestParam("salt") String salt) throws Exception{
		Message message = new Message();
		Integer id = user.getId();
		if(!BeanUtilsExt.isBlank(id)){
			String staffId = user.getStaffId();
			if(StringUtils.isNoneBlank(staffId)) {
				User u = this.userService.getUserByStaffId(staffId);
				if(u == null) {
					if(StringUtils.isBlank(rePasswd)) {
						user.setPasswd(password);
						user.setSalt(salt);
						user.setUpdateDate(new Date());
						this.userService.doUpdate(user, false);
					} else {
						this.userService.doUpdate(user, true);
					}
					//清空认证缓存
					Subject currentUser = SecurityUtils.getSubject();
					UserRealm ur = new UserRealm();
					ur.clearCachedAuthenticationInfo(currentUser.getPrincipals());
					
					message.setStatus(Boolean.TRUE);
					message.setMessage("修改成功！");
				} else {
					message.setStatus(Boolean.FALSE);
					message.setMessage("修改失败，此编制号已存在！");
				}
			}
		} else {
			message.setStatus(Boolean.FALSE);
			message.setMessage("修改失败，用户不存在！");
		}
		return message;
	}
	
    /**
     * 删除用户和关联的activiti用户信息
     * @param id
     * @param synToActiviti
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id,
						@Value("#{APP_PROPERTIES['account.user.delete.syntoactiviti']}") Boolean synToActiviti) throws Exception{
		if(!BeanUtilsExt.isBlank(id)){
			User user = new User();
			user.setId(id);
			this.userService.doDelete(user, synToActiviti);
			//清空认证和权限缓存
			Subject currentUser = SecurityUtils.getSubject();
			UserRealm ur = new UserRealm();
			ur.clearCache(currentUser.getPrincipals());
			
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
	
	/**
	 * 跳转到在线用户页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toListOnlineUser")
	public String toOnlineUser() throws Exception{
		return "user/online_user";
	}
	
	/**
	 * 显示在线用户列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listOnlineUser")
	@ResponseBody
    public List<Object> list(Model model) {
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        List<Object> jsonList=new ArrayList<Object>(); 
        for(Session session : sessions){
        	Map<String, Object> map=new HashMap<String, Object>();
        	PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        	if(principalCollection == null) {
        		continue;
        	}
        	String userName = (String)principalCollection.getPrimaryPrincipal();
        	Boolean forceLogout = session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null;
        	map.put("id", session.getId());
        	map.put("userName", userName);
        	map.put("host", session.getHost());
        	map.put("lastAccessTime", session.getLastAccessTime());
        	map.put("forceLogout", forceLogout);
        	jsonList.add(map);
        }
        return jsonList;
    }

	/**
	 * 强制退出用户
	 * @param sessionId
	 * @return
	 */
    @RequestMapping("/forceLogout/{sessionId}")
	@ResponseBody
    public Message forceLogout(@PathVariable("sessionId") String sessionId) {
		Message message = new Message();
        try {
            Session session = sessionDAO.readSession(sessionId);
            if(session != null) {
                session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
            }
            message.setStatus(Boolean.TRUE);
            message.setMessage("强制退出成功！");
        } catch (Exception e) {
        	message.setStatus(Boolean.FALSE);
            message.setMessage("强制退出失败！");
        }
        return message;
    }
	
	/**
	 * 同步所有用户到activiti表
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("admin:user:syncUser")
	@RequestMapping("/syncUserToActiviti")
	@ResponseBody
	public Message syncUserToActiviti() throws Exception {
		Message message = new Message();
		try {
			this.userService.doAddAllUserAndRoleToActiviti();
			message.setStatus(Boolean.TRUE);
            message.setMessage("同步用户信息成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
            message.setMessage("同步用户信息失败！");
		}
		return message;
	}
	
	//如果执行删除，工作流审批中的代办任务和待签收任务将无法使用。（在act_ru_identitylink将查不到act_id_user、act_id_group和act_id_membership的信息）
	@RequiresPermissions("admin:user:delAllIdentifyData")
	@RequestMapping("/delAllIdentifyData")
	public String delAllIdentifyData(RedirectAttributes redirectAttributes) throws Exception {
		this.userService.doDeleteAllActivitiIdentifyData();
		redirectAttributes.addFlashAttribute("msg", "成功删除工作流引擎Activiti的用户、角色以及关系！");
		return "redirect:/userAction/toList_page";
	}  
	
	/**
	 * 跳转选择任务委派人页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/toChooseDelegateUser")
	public ModelAndView toChooseDelegateUser() throws Exception{
		ModelAndView mv = new ModelAndView("task/delegate_user");
		List<Role> groupList = this.roleService.getRoleList();
		mv.addObject("groupList", groupList);
		return mv;
	}
	
	/**
	 * 在tabs中根据groupId显示用户列表
	 * @return
	 */
	@RequestMapping(value = "/toShowDelegateUser")
	public ModelAndView toShowDelegateUser(
			@RequestParam("groupId") String groupId){
		ModelAndView mv = new ModelAndView("task/show_user");
		mv.addObject("groupId", groupId);
		return mv;
	}
	
	/**
	 * 获取候选人列表
	 * @param page
	 * @param rows
	 * @param groupId
	 * @param flag
	 * @param key
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
			map.put("group", user.getRole().getName());
			map.put("registerDate", user.getRegisterDate());
			jsonList.add(map);
		}
		Datagrid<Object> dataGrid = new Datagrid<Object>(page.getTotal(), jsonList);
		return dataGrid;
		
	}
	
	/**
	 * 修改密码时验证原密码是否输入正确
	 * @param userId
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validPasswd")
	@ResponseBody
	public Message validPasswd(@RequestParam("userId") Integer userId, @RequestParam("passwd") String passwd) throws Exception {
		Message message = new Message();
		User u = this.userService.getUserById(userId);
		if(!BeanUtilsExt.isBlank(u)){
			String oldPass = u.getPasswd();
			
			u.setPasswd(passwd);
			this.passwordHelper.encryptPassword(u);
			String newPass = u.getPasswd();
			
			if(newPass.equals(oldPass)){
				message.setStatus(Boolean.TRUE);
				message.setMessage("原密码与用户输入密码一致！");
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("原密码与用户输入密码不一致！");
			}
		}else{
			message.setStatus(Boolean.FALSE);
			message.setMessage("用户不存在！");
		}
		
		return message;
	}
	
	/**
	 * 跳转到查询页面
	 * @return
	 */
	@RequestMapping(value = "/userSearch", method = RequestMethod.GET)
	public String userSearch(){
		return "user/search";
	}
	
	/**
	 * 初始化密码123
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initPassword")
	@ResponseBody
	public Message initPassword(@RequestParam("userId") Integer userId) throws Exception {
		User user = this.userService.getUserById(userId);
		user.setPasswd(Constants.DEFAULT_PASSWORD);
		this.userService.doUpdate(user, true);
		return new Message(Boolean.TRUE, "初始化成功！");
	}
	
	/**
	 * 用户个人信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userInfo")
	public ModelAndView userInfo() throws Exception {
		ModelAndView mv = new ModelAndView("user/user_info");
		User user = UserUtil.getUserFromSession();
		mv.addObject("user", user);
		return mv;
	}
}
