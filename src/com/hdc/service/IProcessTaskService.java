package com.hdc.service;

import java.io.Serializable;

import com.hdc.entity.ProcessTask;

public interface IProcessTaskService {

	public Serializable doAdd(ProcessTask processTask) throws Exception;
	
	public void doUpdate(ProcessTask processTask) throws Exception;
	
	public ProcessTask findById(Integer id) throws Exception;
}
