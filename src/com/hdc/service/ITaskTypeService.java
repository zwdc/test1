package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfoType;

public interface ITaskTypeService {

	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfoType> getListPage(Parameter param, Page<TaskInfoType> page) throws Exception;
	
	/**
	 * 获取所有列表
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfoType> getAllList() throws Exception;
	
	/**
	 * 通过id获取TaskInfoType
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskInfoType findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param taskType
	 * @throws Exception
	 */
	public Serializable doAdd(TaskInfoType taskType) throws Exception;
	
	/**
	 * 修改
	 * @param taskType
	 * @throws Exception
	 */
	public void doUpdate(TaskInfoType taskType) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
}
