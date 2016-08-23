package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProjectScore;

public interface IProjectScoreService {
	
	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<ProjectScore> getListPage(Parameter param, Page<ProjectScore> page) throws Exception;
	
	/**
	 * 保存
	 * @param projectScore
	 * @throws Exception
	 */
	public Serializable doAdd(ProjectScore projectScore) throws Exception;
	
}
