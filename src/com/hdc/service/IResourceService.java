package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Resource;

public interface IResourceService {

	public Resource getPermissions(Integer id) throws Exception;
	
	public Resource getResource(String id) throws Exception;
	
	public List<Resource> getTree(Integer roleId) throws Exception;
	
	public List<Resource> getTreeMenu(String type) throws Exception;
	
	public List<Resource> getAllResource() throws Exception;
	
	public List<Resource> getResourceByPid(Integer parentId) throws Exception;
	
	public List<Resource> getResourceList(Parameter param, Page<Resource> page) throws Exception;
	
	public Serializable  doAdd(Resource entity) throws Exception;
	
	public void doUpdate(Resource entity) throws Exception;
	
	public void saveOrUpdate(Resource entity) throws Exception;
	
	public Integer doUpdateIsDelete(String id, Integer isDelete) throws Exception;
	
	public Integer doUpdateName(String id, String name) throws Exception;
	
	public Integer doUpdateSort(String id, String pId, String sort) throws Exception;
	
	public void doDelete(Resource entity) throws Exception;
	
	public Integer doDelete(Integer id) throws Exception;
	
}
