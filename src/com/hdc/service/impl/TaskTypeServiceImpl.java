package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfoType;
import com.hdc.service.IBaseService;
import com.hdc.service.ITaskTypeService;

@Service
public class TaskTypeServiceImpl implements ITaskTypeService {

	@Autowired
	private IBaseService<TaskInfoType> baseService;

	@Override
	public List<TaskInfoType> getListPage(Parameter param, Page<TaskInfoType> page)
			throws Exception {
		return this.baseService.findListPage("TaskInfoType", param, null, page);
	}

	@Override
	public TaskInfoType findById(Integer id) throws Exception {
		return this.baseService.getBean(TaskInfoType.class, id);
	}

	@Override
	public Serializable doAdd(TaskInfoType taskType) throws Exception {
		return this.baseService.add(taskType);
	}

	@Override
	public void doUpdate(TaskInfoType taskType) throws Exception {
		this.baseService.update(taskType);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "delete from TaskInfoType where id = " + id;
		this.baseService.executeHql(hql);
	}

	@Override
	public List<TaskInfoType> getAllList() throws Exception {
		String hql = "from TaskInfoType where isDelete = 0";
		return this.baseService.find(hql);
	};
	

}
