package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.UserTask;
import com.hdc.service.IBaseService;
import com.hdc.service.IUserTaskService;

@Service
public class UserTaskServiceImpl implements IUserTaskService {

	@Autowired 
	private IBaseService<UserTask> baseService;
	
	@Override
	public Serializable doAdd(UserTask userTask) throws Exception {
		return baseService.add(userTask);
	}

	@Override
	public void doUpdate(UserTask userTask) throws Exception {
		this.baseService.update(userTask);
	}

	@Override
	public void doDelete(UserTask userTask) throws Exception {
		this.baseService.delete(userTask);
	}

	@Override
	public List<UserTask> toList(String procDefKey, Parameter param, Page<UserTask> page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("procDefKey", procDefKey);
		List<UserTask> list = this.baseService.findListPage("UserTask", param, map, page, false);
		return list;
	}

	@Override
	public UserTask findById(Integer id) throws Exception {
		return this.baseService.getBean(UserTask.class, id);
	}

	@Override
	public Integer doDeleteAll() throws Exception {
		String hql = "delete UserTask";
		return this.baseService.executeHql(hql);
	}

	@Override
	public List<UserTask> findByWhere(String procDefKey) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("procDefKey", procDefKey);
		return this.baseService.findByWhere("UserTask", map, false);
	}

	@Override
	public List<UserTask> getAll() throws Exception {
		return this.baseService.findByWhere("UserTask", null, false);
	}

}
