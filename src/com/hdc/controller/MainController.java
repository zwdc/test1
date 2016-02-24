package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @RequestMapping("/menu")
    @ResponseBody
    public List<Resource> getMenu() throws Exception{
    	User u = UserUtil.getUserFromSession();
    	List<Resource> menus = this.resourceService.getTree(u.getGroup().getId());
    	return menus;
    }
    
    @RequestMapping("/")
    public String index() throws Exception {
        return "index";
    }
    
    @RequestMapping("/kickout")
    public String kickout() throws Exception {
    	return "error/kickout";
    }
    
}
