package com.hdc.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.RefuseReason;
import com.hdc.service.IBaseService;
import com.hdc.service.IRefuseReasonService;
import com.hdc.service.ITaskInfoService;

@Service
public class RefuseReasonServiceImpl implements IRefuseReasonService {

	@Autowired
	private IBaseService<RefuseReason> baseService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Override
	public Serializable doAdd(RefuseReason refuseReason) throws Exception {
		//更新taskInfo的status 保证 这两个处于同一个事务当中
		this.taskInfoService.doUpdate(refuseReason.getTaskInfo());
		return this.baseService.add(refuseReason);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "delete from RefuseReason where id = " + id;
		this.baseService.executeHql(hql);
	}

}
