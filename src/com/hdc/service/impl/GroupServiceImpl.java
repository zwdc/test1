package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Group;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IBaseService;
import com.hdc.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired 
	private IBaseService<Group> baseService;
	
    @Autowired
    protected IdentityService identityService;
	
	@Override
	public List<Group> getGroupListPage(Parameter param, Page<Group> page) throws Exception{
		List<Group> list = this.baseService.findListPage("Group", param, null, page);
		return list;
	}

	@Override
	public Serializable doAdd(Group group) throws Exception {
		Serializable groupId = this.baseService.add(group);
        org.activiti.engine.identity.Group identity_group = this.identityService.newGroup(groupId.toString());
        identity_group.setName(group.getName());
        identity_group.setType(group.getType());
        this.identityService.saveGroup(identity_group);
		return groupId;
	}

	@Override
	public void doUpdate(Group group) throws Exception {
		this.baseService.update(group);
	}

	@Override
	public void doDelete(Group group) throws Exception {
		this.identityService.deleteGroup(group.getId().toString());
		this.baseService.delete(group);
	}

	@Override
	public List<Group> getGroupList() throws Exception {
		return this.baseService.findByWhere("Group", null);
	}

	@Override
	public Group getGroupById(Integer id) throws Exception {
		return this.baseService.getBean(Group.class, id);
	}

	@Override
	public void saveOrUpdate(Group group) throws Exception {
		this.baseService.saveOrUpdate(group);
	}

}
