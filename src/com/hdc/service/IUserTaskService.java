package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.UserTask;

public interface IUserTaskService {

	public Serializable doAdd(UserTask userTask) throws Exception;
	
	public void doUpdate(UserTask userTask) throws Exception;
	
	public void doDelete(UserTask userTask) throws Exception;
	
	public List<UserTask> toList(String procDefKey, Parameter param, Page<UserTask> page) throws Exception;
	
	public Integer doDeleteAll() throws Exception; 
	
	public UserTask findById(Integer id) throws Exception;
	
	public List<UserTask> findByWhere(String procDefKey) throws Exception;
	
	public List<UserTask> getAll() throws Exception;
	
}
