package com.hdc.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Project;
import com.hdc.service.IBaseService;
import com.hdc.service.IProjectService;

@Service
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private IBaseService<Project> baseService;
	
	@Override
	public Project findById(Integer id) throws Exception {
		return this.baseService.getBean(Project.class, id);
	}

	@Override
	public Serializable doAdd(Project project) throws Exception {
		return this.baseService.add(project);
	}

	@Override
	public void doUpdate(Project project) throws Exception {
		this.baseService.update(project);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update Project set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

}
