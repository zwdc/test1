package com.hdc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.Menus;
import com.hdc.entity.Resource;
import com.hdc.entity.User;
import com.hdc.service.IResourceService;
import com.hdc.util.UserUtil;


/**
 * 首页控制器
 *
 * @author zml
 */
@Controller
public class MainController {

    @Autowired
    private IResourceService resourceService;
	
    @RequestMapping(value = "/north")
    public String north() {
        return "layout/north";
    }

    @RequestMapping(value = "/main")
    public String main() {
        return "layout/main";
    }
    
    @RequestMapping(value = "/center")
    public String center() {
    	return "layout/center";
    }
    
    @RequestMapping(value = "/south")
    public String south() throws Exception {
    	return "layout/south";
    }
    
    /**
     * admin角色的菜单
     * @return
     * @throws Exception
     */
    @RequestMapping("/menuAdmin")
    @ResponseBody
    public List<Resource> getMenuAdmin() throws Exception {
    	User u = UserUtil.getUserFromSession();
    	List<Resource> menuList = this.resourceService.getTree(u.getRole().getId());
    	return menuList;
    }
    
    /**
     * 其他角色的菜单
     * @return
     * @throws Exception
     */
    @RequestMapping("/menu")
    @ResponseBody
    public List<Menus> getMenu() throws Exception{
    	User u = UserUtil.getUserFromSession();
    	List<Resource> menuList = this.resourceService.getTree(u.getRole().getId());
    	List<Menus> menusList = new ArrayList<Menus>();
    	List<Resource> firstMenu = new ArrayList<Resource>();
    	for(Resource resource : menuList) {
    		if(resource.getParentId() == 1) {	// 一级菜单
    			firstMenu.add(resource);
    		}
    	}
    	
    	for(Resource resource : firstMenu) {
    		Menus menus = new Menus();
    		List<Menus> secondMenu = new ArrayList<Menus>();
    		menus.setId(resource.getId());
			menus.setName(resource.getName());
			for(Resource res : menuList) {
				if(res.getParentId() != 0 && res.getId() != resource.getId() && res.getParentId() == resource.getId())	{	// 0 = root
					Menus menus2 = new Menus();
					menus2.setId(res.getId());
					menus2.setName(res.getName());
					menus2.setUrl(res.getUrl());
					menus2.setParentId(res.getParentId());
					// menus2.setIconCls(iconCls);
					secondMenu.add(menus2);
				}
			}
			menus.setChildren(secondMenu);
			menusList.add(menus);
    	}
    	return menusList;
    }
    
    @RequestMapping("/index")
    public String index() throws Exception {
        return "index";
    }
    
    @RequestMapping("/kickout")
    public String kickout() throws Exception {
    	return "error/kickout";
    }
    
}
