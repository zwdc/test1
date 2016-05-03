package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;

public interface IGroupService {

	public List<Group> getGroupListPage(Parameter param, Page<Group> page) throws Exception;
	
	public List<Group> getGroupList() throws Exception;
	
	public Group getGroupById(Integer id) throws Exception;
	
	public Serializable doAdd(Group group) throws Exception;

	public void doUpdate(Group group) throws Exception;
	
	public void saveOrUpdate(Group group) throws Exception;
	
	public void doDelete(Group group) throws Exception;

	public Group getGroupByName(Map<String, Object> param) throws Exception;
	
}
