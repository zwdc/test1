package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Role;

public interface IRoleService {

	public List<Role> getRoleListPage(Parameter param, Page<Role> page) throws Exception;
	
	public List<Role> getRoleList() throws Exception;
	
	public Role getRoleById(String id) throws Exception;
	
	public Serializable doAdd(Role role) throws Exception;

	public void doUpdate(Role role) throws Exception;
	
	public void saveOrUpdate(Role role) throws Exception;
	
	public void doDelete(String id) throws Exception;
}
