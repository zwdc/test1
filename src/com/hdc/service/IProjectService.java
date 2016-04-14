package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;

public interface IProjectService {
	
	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Project> getListPage(Parameter param, Page<Project> page, Map<String, Object> map) throws Exception;
	
	/**
	 * 针对办理中或已签收的查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getHProject(Parameter param, Page<Map<String, Object>> page) throws Exception;
	
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
	 * 签收(线程安全)
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public boolean doClaimProject(String projectId) throws Exception;
	
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
