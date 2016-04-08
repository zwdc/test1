package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskSource;
import com.hdc.service.IBaseService;
import com.hdc.service.ITaskSourceService;

@Service
public class TaskSourceServiceImpl implements ITaskSourceService {

	@Autowired
	private IBaseService<TaskSource> baseService;
	
	@Override
	public List<TaskSource> getListPage(Parameter param, Page<TaskSource> page) throws Exception {
		return this.baseService.findListPage("TaskSource", param, null, page, true);
	}

	@Override
	public List<TaskSource> getAllList() throws Exception {
		String hql = "from TaskSource where isDelete = 0 order by createDate desc";
		return this.baseService.find(hql);
	}

	@Override
	public TaskSource findById(Integer id) throws Exception {
		return this.baseService.getBean(TaskSource.class, id);
	}

	@Override
	public Serializable doAdd(TaskSource taskSource) throws Exception {
		return this.baseService.add(taskSource);
	}

	@Override
	public void doUpdate(TaskSource taskSource) throws Exception {
		this.baseService.update(taskSource);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update TaskSource set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

}
