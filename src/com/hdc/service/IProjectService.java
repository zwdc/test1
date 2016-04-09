package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Project;

public interface IProjectService {
	/**
	 * 通过id获取Project
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Project findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param project
	 * @throws Exception
	 */
	public Serializable doAdd(Project project) throws Exception;
	
	/**
	 * 修改
	 * @param project
	 * @throws Exception
	 */
	public void doUpdate(Project project) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 根据taskInfoId查询
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByTaskInfo(Integer taskInfoId) throws Exception;
}
