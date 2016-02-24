package com.hdc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.RoleAndResource;
import com.hdc.service.IBaseService;
import com.hdc.service.IRoleAndResourceService;

@Service
public class RoleAndResourceServiceImpl implements IRoleAndResourceService {
	
	@Autowired 
	private IBaseService<RoleAndResource> baseService;
	
	@Override
	public List<RoleAndResource> getResource(Integer roleId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		String hql = "from RoleAndResource a where a.roleId = :roleId";
		List<RoleAndResource> list = this.baseService.find(hql, map);
		return list;
	}

	@Override
	public void doAdd(RoleAndResource rar) throws Exception {
		this.baseService.add(rar);
		
	}

	@Override
	public void doDelete(RoleAndResource rar) throws Exception {
		this.baseService.delete(rar);
		
	}

	@Override
	public Integer doDelByRole(Integer roleId) throws Exception {
		String hql = "delete from RoleAndResource where roleId = " + roleId;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doDelByResource(Integer resourceId) throws Exception {
		String hql = "delete from RoleAndResource where resourceId = :resourceId";
		Map<String, Object> params = new HashMap<String, Object>();  
		params.put("resourceId", resourceId);  
		return this.baseService.executeHql(hql, params);
	}

}
