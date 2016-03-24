package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Message;
import com.hdc.entity.TaskInfoType;
import com.hdc.service.ITaskTypeService;

/**
 * 任务类型控制器
 * @author zhao
 *
 */
@Controller
@RequestMapping("/taskType")
public class TaskTypeController {

	@Autowired
	private ITaskTypeService taskTypeService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "taskType/list_taskType";
	}
	
	/**
	 * 跳转添加或修改页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public String toMain() throws Exception {
		return "taskType/main_taskType";
	}
	
	/**
	 * 获取所有类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public List<TaskInfoType> getList() throws Exception {
		return this.taskTypeService.getAllList();
	}
	
	/**
	 * 添加或更新
	 * @param taskType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(TaskInfoType taskType) throws Exception {
		Message message = new Message();
		Integer id = taskType.getId();
		if(id == null) {
			if(taskType.getParentId() == null) {
				taskType.setParentId(0);
			}
			this.taskTypeService.doAdd(taskType);
			message.setMessage("添加成功！");
		} else {
			this.taskTypeService.doUpdate(taskType);
			message.setMessage("修改成功！");
		}
		return message;
	}
	
	/**
	 * 详细页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/details/{id}")
	public ModelAndView details(@PathVariable("id") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView();
		TaskInfoType taskType = this.taskTypeService.findById(id);
		mv.addObject("TaskInfoType", taskType);
		return mv;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception {
		Message message = new Message();
		this.taskTypeService.doDelete(id);
		message.setMessage("删除成功！");
		return message;
	}
}
