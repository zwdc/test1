package com.hdc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.UserTask;
import com.hdc.service.IResourceService;
import com.hdc.service.IUserTaskService;

/**
 * 设定用户任务页面 相关方法
 * @author ZML
 *
 */
@Controller
@RequestMapping("/userTask")
public class UserTaskController {

    @Autowired
    protected RepositoryService repositoryService;
    
    @Autowired
    protected IUserTaskService userTaskService;
    
    @Autowired
    protected IResourceService resourceService;
    
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toListBpmn(){
		return "taskSetup/list_bpmn";
	}
	
	/**
	 * 读取用户任务列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/getList")
	@ResponseBody
	public Datagrid<Object> listBpmn(
			@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "rows", required = false) Integer rows){
		Page<Object> p = new Page<Object>(page, rows);
		List<Object> jsonList=new ArrayList<Object>(); 
		
		ProcessDefinitionQuery proDefQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
		Integer totalSum = proDefQuery.list().size();
		int[] pageParams = p.getPageParams(totalSum);
		List<ProcessDefinition> processDefinitionList = proDefQuery.listPage(pageParams[0], pageParams[1]);
		for(ProcessDefinition pd : processDefinitionList){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", pd.getId());
			map.put("name", pd.getName());
			map.put("key", pd.getKey());
			map.put("resourceName", pd.getResourceName());
			map.put("diagramResourceName", pd.getDiagramResourceName());
			jsonList.add(map);
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
		
	}
	
	/**
	 * 将节点显示到页面
	 * @param procDefKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listUserTask")
	@ResponseBody
	public List<UserTask> listUserTask(@RequestParam("procDefKey") String procDefKey) throws Exception{
		List<UserTask> list = this.userTaskService.findByWhere(procDefKey);
		return list;
	}
	
	/**
	 * 删除 usertask表中数据，初始化节点信息。
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initialization")
	@ResponseBody
	public Message initialization(HttpServletResponse response) throws Exception {
		Message message = new Message();
		try {
			this.userTaskService.doDeleteAll();
			ProcessDefinitionQuery proDefQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
			List<ProcessDefinition> processDefinitionList = proDefQuery.list();
			if(processDefinitionList.size() != 0){
				for(ProcessDefinition processDefinition : processDefinitionList){
					//读取节点信息保存到usertask表
					setSingleActivitiInfo(processDefinition);
				}
				message.setMessage("初始化成功！");
				message.setStatus(Boolean.TRUE);
			} else {
				message.setMessage("初始化失败！系统中没有部署任何流程文件，请到【流程定义】中部署相应流程！");
				message.setStatus(Boolean.FALSE);
			}
		} catch (Exception e) {
			message.setMessage("初始化失败！");
			message.setStatus(Boolean.FALSE);
			throw e;
		}
		return message;
		
	}
	
	/**
	 * 加载单个bpmn文件到usertask表
	 * @param processDefinitionId
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadSingleBpmn")
	@ResponseBody
	public Message loadSingleBpmn(@RequestParam("processDefinitionId") String processDefinitionId,
								RedirectAttributes redirectAttributes) throws Exception{
		Message message = new Message();
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
		//读取节点信息保存到usertask表
		setSingleActivitiInfo(processDefinition);
		message.setMessage("加载成功！");
		message.setStatus(Boolean.TRUE);
		return message;
	}
	
	/**
	 * 读取节点信息保存到UserTask表中
	 * @param processDefinition
	 * @throws Exception
	 */
	private void setSingleActivitiInfo(ProcessDefinition processDefinition) throws Exception{
		String proDefKey = processDefinition.getKey();
		List<UserTask> list = this.userTaskService.findByWhere(proDefKey);
		ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinition.getId());
		List<ActivityImpl> activitiList = processDef.getActivities();//获得当前任务的所有节点
		for (ActivityImpl activity : activitiList) {
			ActivityBehavior activityBehavior = activity.getActivityBehavior();
			boolean isFound = false;
			//是否为用户任务
			if (activityBehavior instanceof UserTaskActivityBehavior) {
				UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
	            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
	            //任务所属角色
	            String taskDefKey = taskDefinition.getKey();
	            Expression taskName = taskDefinition.getNameExpression();
	            
	            //判断表中是否存在此节点
	            if(list.size() != 0){
					for(UserTask userTask : list){
						if(taskDefKey.equals(userTask.getTaskDefKey()) || taskName.toString().equals(userTask.getTaskName())){
							userTask.setProcDefKey(processDefinition.getKey());
				            userTask.setProcDefName(processDefinition.getName());
				            userTask.setTaskDefKey(taskDefKey);
				            userTask.setTaskName(taskName.toString());
				            this.userTaskService.doUpdate(userTask);
				            isFound = true;
				            break;
						}
					}
					
				}
	            if(!isFound){
	            	UserTask userTask = new UserTask();
		            userTask.setProcDefKey(processDefinition.getKey().trim());
		            userTask.setProcDefName(processDefinition.getName().trim());
		            userTask.setTaskDefKey(taskDefKey.trim());
		            userTask.setTaskName(taskName.toString().trim());
		            userTask.setIsDelete(0);
		            this.userTaskService.doAdd(userTask);
	            }
			}
		}
	}
	
	/**
	 * 保存设定的审批人员
	 * @param procDefKey
	 * @param request
	 * @param redirectAttribute
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setupTask")
	@ResponseBody
	public Message setupTask(@RequestParam("procDefKey") String procDefKey, HttpServletRequest request) throws Exception{
		List<UserTask> list = this.userTaskService.findByWhere(procDefKey);
		for(UserTask userTask : list){
			String taskDefKey = userTask.getTaskDefKey();
			String ids = request.getParameter(taskDefKey+"_id");
			String names = request.getParameter(taskDefKey+"_name");
			String taskType = request.getParameter(taskDefKey+"_taskType");
			userTask.setTaskType(taskType);
			userTask.setCandidate_name(names);
			userTask.setCandidate_ids(ids);
			this.userTaskService.doUpdate(userTask);
		}
		return new Message(Boolean.TRUE, "设置审批人员成功！");
	}

}
