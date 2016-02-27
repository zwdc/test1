package com.hdc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IBaseService;
import com.hdc.service.ITaskInfoService;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

	@Autowired
	private IBaseService<TaskInfo> baseService;
	
	@Override
	public List<TaskInfo> getListPage(Parameter param, Page<TaskInfo> page) throws Exception {
		return this.baseService.findListPage("TaskInfo", param, null, page);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update TaskInfo set isDelete = 1 where id = "+id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public void doAdd(TaskInfo taskInfo) throws Exception {
		this.baseService.add(taskInfo);
	}

	@Override
	public void doUpdate(TaskInfo taskInfo) throws Exception {
		this.baseService.update(taskInfo);
	}

	@Override
	public TaskInfo findById(Integer id) throws Exception {
		return this.baseService.getBean(TaskInfo.class, id);
	}

}
