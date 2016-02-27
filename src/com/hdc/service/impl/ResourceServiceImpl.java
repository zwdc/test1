package com.hdc.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.dao.IJdbcDao;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Resource;
import com.hdc.service.IBaseService;
import com.hdc.service.IResourceService;
import com.hdc.util.BeanUtils;

@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired 
	private IBaseService<Resource> baseService;
	
	@Autowired
	protected IJdbcDao jdbcDao;
    
	@Override
	public Resource getPermissions(Integer id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Resource res = this.baseService.findUnique("Resource", map);
		return res;
	}
	
	@Override
	public List<Resource> getTree(Integer roleId) throws Exception {
		if(!BeanUtils.isBlank(roleId)){
			String hql = "select r from Resource r, RoleAndResource rar where " +
					     "r.id = rar.resourceId and r.isDelete = 0 and r.type = 'menu' and rar.roleId = "+roleId +
					     " order by r.parentId, r.sort";
			return this.baseService.find(hql);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	public List<Resource> getAllResource() throws Exception {
		String hql = "from Resource order by parentId, sort";
		return this.baseService.find(hql);
	}

	@Override
	public Serializable  doAdd(Resource entity) throws Exception {
		Serializable id = this.baseService.add(entity);
		return id;
	}
	
	@Override
	public Resource getResource(String id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.baseService.findUnique("Resource", map);
	}

	@Override
	public void doUpdate(Resource entity) throws Exception {
		this.baseService.update(entity);
		
	}

	@Override
	public void doDelete(Resource entity) throws Exception {
		this.baseService.delete(entity);
		
	}

	@Override
	public List<Resource> getResourceList(Parameter param, Page<Resource> page) throws Exception {
		return this.baseService.findListPage("Resource", param, null, page);
	}

	@Override
	public Integer doDelete(Integer id) throws Exception {
		String sql = "delete from RESOURCES where id=:id ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("id", id);  
		return this.jdbcDao.delete(sql, paramMap);
		
	}

	@Override
	public Integer doUpdateName(String id, String name) throws Exception {
		String hql = "update Resource set name='"+name+"' where id="+id;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doUpdateIsDelete(String id, Integer isDelete) throws Exception {
		String hql = "update Resource set isDelete=" + isDelete + " where id="+id;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doUpdateSort(String id, String pId, String sort) throws Exception {
		if(!BeanUtils.isBlank(sort)){
			String hql = "update Resource set parentId="+ pId +", sort="+ sort +" where id=" + id;
			return this.baseService.executeHql(hql);
		}else{
			return 0;
		}
	}

	@Override
	public void saveOrUpdate(Resource entity) throws Exception {
		this.baseService.saveOrUpdate(entity);
		
	}

	@Override
	public List<Resource> getTreeMenu(String type) throws Exception {
		String hql = "from Resource where type='"+type+"' order by id, sort";
		return this.baseService.find(hql);
	}

	@Override
	public List<Resource> getResourceByPid(Integer parentId) throws Exception {
	    String hql = "from Resource where isDelete = 0 and parentId = "+parentId.toString();
		return this.baseService.find(hql);
	}

}
