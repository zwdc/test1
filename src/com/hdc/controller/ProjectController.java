package com.hdc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IProjectService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants.ProjectStatus;
import com.hdc.util.UserUtil;

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
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	
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
	
	/**
	 * 跳转签收页面
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toClaim")
	public ModelAndView toClaim(@RequestParam("projectId") Integer projectId) throws Exception {
		ModelAndView mv = new ModelAndView("project/claim_project");
		Project project = this.projectService.findById(projectId);
		if(project != null) {
			//TaskInfo taskInfo = this.taskInfoService.findById(project.getTaskInfo().getId());
			mv.addObject("taskInfo", project.getTaskInfo());
			mv.addObject("projectId", projectId);
			mv.addObject("suggestion", project.getSuggestion());
		}
		return mv;
	}
	
	/**
	 * 根据type获取不同数据
	 * @param param
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Project> page = new Page<Project>(param.getPage(), param.getRows());
		User user = UserUtil.getUserFromSession();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group.id", user.getGroup().getId());
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
			m.put("taskInfoId", taskInfo.getId());
			m.put("taskTitle", taskInfo.getTitle());
			m.put("urgency", taskInfo.getUrgency());
			m.put("sourceName", taskInfo.getTaskSource().getName());
			m.put("hostGroup", project.getGroup().getName());
			if(project.getUser() != null) {
				m.put("hostUser", project.getUser().getName());
			}
			m.put("endTaskDate", taskInfo.getEndTaskDate());
			m.put("fbFrequencyName", taskInfo.getFbFrequency().getName());
			jsonList.add(m);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	//应该卸载feedbackController方法中
	/*@RequestMapping("/workPlan/{projectId}")
	public Message workPlan(@PathVariable("projectId") Integer projectId, @RequestParam("workPlan") String workPlan) {
	}*/
}