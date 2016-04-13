package com.hdc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IProjectService;
import com.hdc.util.Constants.ProjectStatus;

/**
 * 任务交办管理器
 * @author ZML
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;
	
	
	/**
	 * 根据标识跳转到不同页面
	 * @param type
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(@RequestParam("type") Integer type) {
		if(type == 1) {
			//待签收
			return "project/list_c_project";
		} else if(type == 2) {
			//办理中
			return "project/list_h_project";
		} else if(type == 3) {
			//申请办结
			return "project/list_ae_project";
		} else if(type == 4) {
			//已办结
			return "project/list_e_project";
		}
		return null;
	}
	
	@RequestMapping("/getList")
	public Datagrid<Object> getList(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Project> page = new Page<Project>(param.getPage(), param.getRows());
		Map<String, Object> map = new HashMap<String, Object>();
		if(type == 1) {
			//待签收
			map.put("status", ProjectStatus.WAIT_FOR_CLAIM.toString());
		} else if(type == 2) {
			//办理中
			map.put("status", ProjectStatus.IN_HANDLING.toString());
		} else if(type == 3) {
			//已办结
			map.put("status", ProjectStatus.APPLY_FINISHED.toString());
		} else if(type == 4) {
			//已办结
			map.put("status", ProjectStatus.FINISHED.toString());
		}
		List<Object> jsonList=new ArrayList<Object>(); 
		List<Project> list = this.projectService.getListPage(param, page, map);
		for(Project project : list) {
			Map<String, Object> m=new HashMap<String, Object>();
			TaskInfo taskInfo = project.getTaskInfo();
			m.put("id", project.getId());
			m.put("taskTitle", taskInfo.getTitle());
			m.put("sourceName", taskInfo.getTaskSource().getName());
			m.put("hostGroup", project.getGroup().getName());
			m.put("hostUser", project.getUser().getName());
			jsonList.add(m);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
}
