package com.hdc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 代办任务
 * @author zhao
 *
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.ProcessTask;
import com.hdc.service.IProcessTaskService;

@Controller
@RequestMapping("/processTask")
public class ProcessTaskController {

	@Autowired
	private IProcessTaskService processTaskService;
	
	@RequestMapping("/getProcessTask/{id}")
	@ResponseBody
	public ProcessTask getProcessTask(@PathVariable("id") Integer id) throws Exception {
		ProcessTask processTask = new ProcessTask();
		if(id != null) {
			processTask = this.processTaskService.findById(id);
		}
		return processTask;
	}
}
