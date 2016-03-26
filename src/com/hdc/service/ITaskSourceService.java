package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskSource;

public interface ITaskSourceService {
	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskSource> getListPage(Parameter param, Page<TaskSource> page) throws Exception;
	
	/**
	 * 获取所有列表
	 * @return
	 * @throws Exception
	 */
	public List<TaskSource> getAllList() throws Exception;
	
	/**
	 * 通过id获取TaskSource
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskSource findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param taskSource
	 * @throws Exception
	 */
	public Serializable doAdd(TaskSource taskSource) throws Exception;
	
	/**
	 * 修改
	 * @param taskSource
	 * @throws Exception
	 */
	public void doUpdate(TaskSource taskSource) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
}
