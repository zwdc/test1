package com.hdc.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.ProcessTask;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessTaskService;

@Service
public class ProcessTaskServiceImpl implements IProcessTaskService {

	@Autowired
	private IBaseService<ProcessTask> baseService;
	
	@Override
	public Serializable doAdd(ProcessTask processTask) throws Exception {
		return this.baseService.add(processTask);
	}

	@Override
	public void doUpdate(ProcessTask processTask) throws Exception {
		this.baseService.update(processTask);
	}

	@Override
	public ProcessTask findById(Integer id) throws Exception {
		return this.baseService.getBean(ProcessTask.class, id);
	}

}
