package com.hdc.service;

import java.io.Serializable;
import java.util.List;



import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.User;

public interface IUserService {

	public List<User> getUserList(Parameter param, Page<User> page) throws Exception;
	
	public User getUserByName(String user_name) throws Exception;

	public User getUserById(Integer id) throws Exception;
	
	public List<User> getUserByGroupId(String groupId, Parameter param, Page<User> page) throws Exception;
	
	public void doUpdate(User user, Boolean updatePasswd) throws Exception;
	
	/**
     * 添加用户并[同步其他数据库]
     * step 1: 保存系统用户，同时设置和部门的关系
     * step 2: 同步用户信息到activiti的identity.User，同时设置角色
     * step 3: 同步用户信息到DATASET_PERMISSION表中，用来设置数据权限
     * 
     * @param user              用户对象
     * @param synToActiviti     是否同步到Activiti数据库，通过配置文件方式设置，使用属性：account.user.add.syntoactiviti
     * @throws  Exception                       其他未知异常
     */
    public Serializable doAdd(User user, boolean synToActiviti) throws Exception;
        
    /**
     * 删除用户
     * @param userId        用户ID
     * @param synToActiviti     是否同步到Activiti数据库，通过配置文件方式设置，使用属性：account.user.add.syntoactiviti
     * @throws Exception
     */
    public void doDelete(User user, boolean synToActiviti) throws Exception;
    
    /**
     * 同步用户、角色数据到工作流
     * @throws Exception
     */
    public void synAllUserAndRoleToActiviti() throws Exception;
 
    /**
     * 删除工作流引擎Activiti的用户、角色以及关系
     * @throws Exception
     */
    public void deleteAllActivitiIdentifyData() throws Exception;
}


