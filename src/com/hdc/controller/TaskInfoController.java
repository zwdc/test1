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
import com.hdc.entity.TaskInfo;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.BeanUtils;

/**
 * 督察处对任务进行管理
 * @author zhao
 *
 */

@Controller
@RequestMapping("/taskInfo")
public class TaskInfoController {

	@Autowired
	private ITaskInfoService taskInfoService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "taskInfo/list_taskInfo";
	}
	
	/**
	 * 跳转添加修改页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(@RequestParam(value="id", required=false) Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("taskInfo/main_taskInfo");
		if(!BeanUtils.isBlank(id)) {
			mv.addObject("taskInfo", this.taskInfoService.findById(id));
		}
		return mv;
	}
	
	/**
	 * 获取列表分页数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(Parameter param) throws Exception{
		Page<TaskInfo> page = new Page<TaskInfo>(param.getPage(), param.getRows());
		List<TaskInfo> list = this.taskInfoService.getListPage(param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskInfo task : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("title", task.getTitle());
			map.put("taskNo", task.getTaskNo());
			map.put("createTaskDate", task.getCreateTaskDate());
			map.put("assignDate", task.getAssignDate());
			map.put("endTaskDate", task.getEndTaskDate());
			map.put("feedbackCycle", task.getFeedbackCycle());
			map.put("feedbaceDate", task.getFeedbaceDate());
			map.put("hostGroup", task.getHostGroup().getId());				//主办单位
			map.put("assistantGroup", task.getAssistantGroup().getId());	//协办单位
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
}
