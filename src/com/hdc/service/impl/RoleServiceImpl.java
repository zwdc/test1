package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Role;
import com.hdc.service.IBaseService;
import com.hdc.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private IBaseService<Role> baseService; 
	
	@Override
	public List<Role> getRoleListPage(Parameter param, Page<Role> page)
			throws Exception {
		return this.baseService.findListPage("Role", param, null, page);
	}

	@Override
	public List<Role> getRoleList() throws Exception {
		return this.baseService.findByWhere("Role", null);
	}

	@Override
	public Role getRoleById(String id) throws Exception {
		return this.baseService.getBean(Role.class, id);
	}

	@Override
	public Serializable doAdd(Role role) throws Exception {
		return this.baseService.add(role);
	}

	@Override
	public void doUpdate(Role role) throws Exception {
		this.baseService.update(role);
		
	}

	@Override
	public void saveOrUpdate(Role role) throws Exception {
		this.baseService.saveOrUpdate(role);
		
	}

	@Override
	public void doDelete(String id) throws Exception {
		String hql = "update Role set isDelete = 1 where id = " + id;
		this.baseService.executeHql(hql);
		
	}

}
