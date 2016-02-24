package com.hdc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.GroupAndResource;
import com.hdc.service.IBaseService;
import com.hdc.service.IGroupAndResourceService;

@Service
public class GroupAndResourceServiceImpl implements IGroupAndResourceService {
	
	@Autowired 
	private IBaseService<GroupAndResource> baseService;
	
	@Override
	public List<GroupAndResource> getResource(Integer groupId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		String hql = "from GroupAndResource a where a.groupId = :groupId";
		List<GroupAndResource> list = this.baseService.find(hql, map);
		return list;
	}

	@Override
	public void doAdd(GroupAndResource rar) throws Exception {
		this.baseService.add(rar);
		
	}

	@Override
	public void doDelete(GroupAndResource rar) throws Exception {
		this.baseService.delete(rar);
		
	}

	@Override
	public Integer doDelByGroup(Integer groupId) throws Exception {
		String hql = "delete from GroupAndResource where groupId = " + groupId;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doDelByResource(Integer resourceId) throws Exception {
		String hql = "delete from GroupAndResource where resourceId = :resourceId";
		Map<String, Object> params = new HashMap<String, Object>();  
		params.put("resourceId", resourceId);  
		return this.baseService.executeHql(hql, params);
	}

}
