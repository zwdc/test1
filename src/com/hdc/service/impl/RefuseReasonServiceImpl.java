package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.ProcessTask;
import com.hdc.entity.RefuseReason;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IProcessService;
import com.hdc.service.IRefuseReasonService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants.BusinessType;
import com.hdc.util.Constants.OperationType;
import com.hdc.util.UserUtil;

@Service
public class RefuseReasonServiceImpl implements IRefuseReasonService {

	@Autowired
	private IBaseService<RefuseReason> baseService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IProcessService processService;
	
	@Override
	public Serializable doAdd(RefuseReason refuseReason) throws Exception {
		//更新taskInfo的status 保证 这两个处于同一个事务当中
		TaskInfo taskInfo = refuseReason.getTaskInfo();
		this.taskInfoService.doUpdate(taskInfo);
		
		User user = UserUtil.getUserFromSession();
		//给督察提示待办任务
		ProcessTask processTask = new ProcessTask();
		processTask.setUser_name(user.getName());
		processTask.setUser_id(user.getId());
		processTask.setBusinessType(BusinessType.IMPORTANT_FILE.toString());	//业务类型：重要文件
		processTask.setBusinessKey(taskInfo.getId());
//		processTask.setTaskId(taskInfo.getActTaskId());
		processTask.setTitle("拒绝签收的任务事项！");
		processTask.setUrl("/taskInfo/toMain?id="+taskInfo.getId());
		//processTask.setBusinessForm(BusinessForm.QT_FILE.toString());			//业务表单：省政府文件
		processTask.setBusinessOperation(OperationType.MODIFY.toString());
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("claim", false);
		variables.put("processTask", processTask);
		this.processService.complete(processTask.getTaskId(), null, variables);
		
		return this.baseService.add(refuseReason);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "delete from RefuseReason where id = " + id;
		this.baseService.executeHql(hql);
	}

	@Override
	public List<RefuseReason> findByTaskId(Integer taskInfoId) throws Exception {
		String hql = "from RefuseReason where taskInfo.id = " + taskInfoId + " order by createDate desc";
		return this.baseService.find(hql);
	}

}
