package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProjectScore;
import com.hdc.service.IBaseService;
import com.hdc.service.IProjectScoreService;

@Service
public class ProjectScoreServiceImpl implements IProjectScoreService {

	@Autowired
	private IBaseService<ProjectScore> baseService;
	
	@Override
	public List<ProjectScore> getListPage(Parameter param, Page<ProjectScore> page)
			throws Exception {
		return this.baseService.findListPage("ProjectScore", param, null,page,true);
	}
	@Override
	public Serializable doAdd(ProjectScore projectScore) throws Exception {
		// TODO Auto-generated method stub
		return this.baseService.add(projectScore);
	}

}