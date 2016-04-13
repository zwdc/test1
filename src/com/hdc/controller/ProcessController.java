package com.hdc.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.User;
import com.hdc.service.IProcessService;
import com.hdc.service.IUserService;
import com.hdc.util.Constants;
import com.hdc.util.ProcessDefinitionCache;
import com.hdc.util.UserUtil;
import com.hdc.util.WorkflowUtils;
import com.hdc.workflow.WorkflowDeployService;
import com.hdc.workflow.WorkflowService;

@Controller
@RequestMapping("/process")
public class ProcessController {
	private static final Logger logger = Logger.getLogger(ProcessController.class);
	@Autowired
	protected IUserService userService;
    
    @Autowired
    protected WorkflowService traceService;

	@Autowired
	private IProcessService processService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private WorkflowDeployService workflowProcessDefinitionService;
	
	
	/**
     * 跳转待办任务、已完成任务页面
     * @return
     */
    @RequestMapping(value = "/todoTaskList")
    public String userTaskList(){
    	return "task/list_task";
    }
    
    /**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/todoTask")
	@ResponseBody
	public Datagrid<ProcessTask> todoTask(Parameter param) throws Exception{
		User user = UserUtil.getUserFromSession();
		Page<ProcessTask> page = new Page<ProcessTask>(param.getPage(), param.getRows());
		List<ProcessTask> taskList = this.processService.findTodoTask(user.getId().toString(), page);
		/*		List<Object> jsonList=new ArrayList<Object>(); 
		for(ProcessTask base : taskList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_name", base.getUser_name());
			map.put("title", base.getTitle());
			map.put("url", base.getUrl());
			map.put("businessKey", base.getBusinessKey());
			map.put("businessForm", base.getBusinessForm());	//待办任务时，根据此字段去执行controller中的方法
			map.put("businessOperation", base.getBusinessOperation());
			map.put("taskId", base.getTaskId());
			map.put("taskName", base.getTaskName());
			map.put("projectName", base.getProjectName());
			map.put("createTime", base.getTaskCreateDate());
			String assign = base.getAssign();
			if(StringUtils.isNotBlank(assign)){
				User u = this.userService.getUserById(new Integer(assign));
				assign = u.getName();
			}
			String owner = base.getOwner();
			if(StringUtils.isNotBlank(owner)){
				User u = this.userService.getUserById(new Integer(owner));
				owner = u.getName();
			}
			
			map.put("assign", assign);
			map.put("owner", owner);
			map.put("executionId", base.getExecutionId());
			map.put("taskDefinitionKey", base.getTaskDefinitionKey());
			map.put("processInstanceId", base.getProcessInstanceId());
			map.put("processDefinitionId", base.getProcessDefinitionId());
			map.put("processDefinitionKey", base.getProcessDefinitionKey());	//任务跳转用
			map.put("supended", base.getSupended());
			map.put("version", base.getVersion());
			
			jsonList.add(map);
		}*/
		return new Datagrid<ProcessTask>(page.getTotal(), taskList);
	}
    
	
	@RequestMapping(value = "/endTask")
	@ResponseBody
	public Datagrid<Object> endTask(Parameter param) throws Exception {
		User user = UserUtil.getUserFromSession();
		Page<ProcessTask> page = new Page<ProcessTask>(param.getPage(), param.getRows());
		List<ProcessTask> taskList = this.processService.findFinishedTaskInstances(user, page);
		
		List<Object> jsonList=new ArrayList<Object>(); 
		for(ProcessTask base : taskList){
			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("user_name", base.getUser_name());
			map.put("title", base.getHistoricTaskInstance().getName());
			
			map.put("taskId", base.getHistoricTaskInstance().getId());
    		map.put("processInstanceId", base.getHistoricTaskInstance().getProcessInstanceId());
    		map.put("startTime", base.getHistoricTaskInstance().getStartTime());
    		map.put("claimTime", base.getHistoricTaskInstance().getClaimTime());
    		map.put("endTime", base.getHistoricTaskInstance().getEndTime());
    		map.put("deleteReason", base.getHistoricTaskInstance().getDeleteReason());
    		ProcessDefinition pd = this.processService.getProcessDefinitionById(base.getProcessDefinitionId());
    		map.put("version", pd.getVersion());
    		jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
    /**
     * 跳转流程定义页面 - admin
     * @return
     * @throws Exception
     */
    @RequestMapping("/toListProcessManager")
    public String toListProcess() throws Exception{
    	return "process/list_process_manager";
    }
    
    /**
     * 跳转流程实例页面 - admin
     * @return
     * @throws Exception
     */
    @RequestMapping("/toListProcessInstance")
    public String toListProcessRunning() throws Exception{
    	return "process/list_process_instance";
    }
    
    /**
     * 流程定义列表 - admin
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
	@RequestMapping("/listProcess")
	@ResponseBody
    public Datagrid<Object> listProcess(Parameter param) throws Exception{
    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
    	Page<Object[]> p = new Page<Object[]>(param.getPage(), param.getRows());
    	int[] pageParams = p.getPageParams(processDefinitionQuery.list().size());
    	if(StringUtils.isNoneEmpty(param.getSort())) {
    		switch (param.getSort()) {
				case "id":
					processDefinitionQuery.orderByProcessDefinitionId();
					break;
				case "name":
					processDefinitionQuery.orderByProcessDefinitionName();
					break;
				case "key":
					processDefinitionQuery.orderByProcessDefinitionKey();
					break;
				default:
					break;
			}
    		switch (param.getOrder()) {
				case "asc":
					processDefinitionQuery.asc();
					break;
				case "desc":
					processDefinitionQuery.desc();
					break;
				default:
					break;
			}
    	}
    	
    	List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
    	List<Object> pdList = new  ArrayList<Object>();
    	for (ProcessDefinition processDefinition : processDefinitionList) {
    		String deploymentId = processDefinition.getDeploymentId();
    		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("id", processDefinition.getId());
    		map.put("name", processDefinition.getName());
    		map.put("key", processDefinition.getKey());
    		map.put("deploymentId", processDefinition.getDeploymentId());
    		map.put("version", processDefinition.getVersion());
    		map.put("resourceName", processDefinition.getResourceName());
    		map.put("diagramResourceName", processDefinition.getDiagramResourceName());
    		map.put("deploymentTime", deployment.getDeploymentTime());
    		map.put("suspended", processDefinition.isSuspended());
            pdList.add(map);
        }
    	Datagrid<Object> dataGrid = new Datagrid<Object>(p.getTotal(), pdList);
    	return dataGrid;
    }
    
	/**
	 * 签收任务
	 * @return
	 */
	@RequestMapping("/claim/{taskId}")
	@ResponseBody
	public Message claim(@PathVariable("taskId") String taskId) {
		Message message = new Message();
		try {
			User user = UserUtil.getUserFromSession();
			this.processService.doClaim(user, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("任务签收成功！");
		}catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！任务签收失败！");
		}catch (ActivitiTaskAlreadyClaimedException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务已被其他组成员签收！请刷新页面重新查看！");
		}catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("任务签收失败！请联系管理员！");
		} 
        return message;
	}
	
    /**
     * 管理运行中的流程 - admin
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/runningProcess")
    @ResponseBody
    public Datagrid<Object> listRuningProcess(Parameter param) throws Exception{
    	Page<ProcessInstance> p = new Page<ProcessInstance>(param.getPage(), param.getRows());
    	List<ProcessInstance> list = this.processService.listRuningProcess(p);
    	List<Object> pieList = new ArrayList<Object>();
    	for(ProcessInstance processInstance : list){
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("id", processInstance.getId());
    		map.put("processInstanceId", processInstance.getProcessInstanceId());
    		map.put("processDefinitionId", processInstance.getProcessDefinitionId());
    		map.put("activityId", processInstance.getActivityId());
    		map.put("suspended", processInstance.isSuspended());
    		
    		ProcessDefinitionCache.setRepositoryService(this.repositoryService);
    		String taskName = ProcessDefinitionCache.getActivityName(processInstance.getProcessDefinitionId(), processInstance.getActivityId());
    		map.put("taskName", taskName);
    		pieList.add(map);
    		
    	}
    	return new Datagrid<Object>(p.getTotal(), pieList);
    }
    
    /**
     * 管理已结束的流程 - admin
     *
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/finishedProcess")
    @ResponseBody
    public Datagrid<Object> findFinishedProcessInstances(Parameter param) throws Exception {
    	Page<ProcessTask> p = new Page<ProcessTask>(param.getPage(), param.getRows());
    	List<Object> jsonList=new ArrayList<Object>(); 
    	List<ProcessTask> processList = this.processService.findFinishedProcessInstances(p);
    	for(ProcessTask base : processList){
    		Map<String, Object> map=new HashMap<String, Object>();
    		/*map.put("businessType", base.getBusinessType());
    		map.put("user_name", base.getUser_name());*/
    		map.put("title", base.getTitle());
//    		map.put("startTime", base.getHistoricProcessInstance().getStartTime());
//    		map.put("endTime", base.getHistoricProcessInstance().getEndTime());
//    		map.put("deleteReason", base.getHistoricProcessInstance().getDeleteReason());
//    		map.put("version", base.getProcessDefinition().getVersion());
    		jsonList.add(map);
    	}
    	return new Datagrid<Object>(p.getTotal(), jsonList);
    }
    
    /**
     * 激活、挂起流程实例-根据processInstanceId - admin
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateProcessStatusByProInstanceId/{status}/{processInstanceId}")
    @ResponseBody
    public Message updateProcessStatusByProInstanceId(
    		@PathVariable("status") String status, 
    		@PathVariable("processInstanceId") String processInstanceId,
            RedirectAttributes redirectAttributes) throws Exception{
    	Message message = new Message();
    	if (status.equals("active")) {
    		this.processService.activateProcessInstance(processInstanceId);
    		message.setStatus(Boolean.TRUE);
            message.setMessage("已激活ID为[" + processInstanceId + "]的流程实例。");
    	} else if (status.equals("suspend")) {
        	this.processService.suspendProcessInstance(processInstanceId);
        	message.setStatus(Boolean.TRUE);
            message.setMessage("已挂起ID为[" + processInstanceId + "]的流程实例。");
    	}
    	return message;
    }
    
    /**
     * 导入部署 - admin
     * --@Value用于将一个SpEL表达式结果映射到到功能处理方法的参数上。
     * @RequestParam(value = "file", required = false) required = false时可以不用传递这个参数，默认为true
     * @param exportDir
     * @param file
     * @return
     */
    @RequestMapping(value = "/deploy")
    public ModelAndView deploy(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir, 
    					  @RequestParam(value = "file", required = false) MultipartFile file) {
    	//@Value("${export.diagram.path}")
    	//@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
    	ModelAndView mav = new ModelAndView("process/list_process_manager");
        String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = this.repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = this.repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            }

            List<ProcessDefinition> list = this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            for (ProcessDefinition processDefinition : list) {
                WorkflowUtils.exportDiagramToFile(this.repositoryService, processDefinition, exportDir);
            }
            mav.addObject(Constants.MESSAGE, "流程部署成功！");
        } catch (Exception e) {
        	mav.addObject(Constants.MESSAGE, "流程部署失败！");
            logger.error("error on deploy process, because of file input stream", e);
        }
        return mav;
    }
    
    /**
     * 加载单个流程 - admin
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/redeploy/single")
    @ResponseBody
    public Message redeploySingle(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
    							@RequestParam("resourceName") String resourceName,
    							@RequestParam(value = "diagramResourceName", required = false) String diagramResourceName,
    							@RequestParam("deploymentId") String deploymentId) throws Exception {
    	Message message = new Message();
        try {
        	//this.repositoryService.deleteDeployment(deploymentId, true);
        	//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
        	String processKey = resourceName.substring(0, resourceName.indexOf('.'))+".zip";
        	this.workflowProcessDefinitionService.redeploySingleFrom(exportDir, processKey);
        	//方法二：通过classpath/bpmn下的流程描述文件部署--流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir, resourceName,diagramResourceName);
        	message.setStatus(Boolean.TRUE);
        	message.setMessage("已重新加载选定流程！");
		} catch (Exception e) {
        	message.setStatus(Boolean.FALSE);
        	if("The classpath:/deploy/ is empty!".equals(e.getMessage())){
        		message.setMessage("加载指定流程失败！classpath:/deploy/ 目录为空！");
        	} else if("can not find .zip or .bar in classpath:/deploy/ !".equals(e.getMessage())){
        		message.setMessage("加载指定流程失败！系统没有找到您部署的文件！");
        	} else {
        		message.setMessage("加载指定流程失败！");
        	}
		}
        return message;
    }
    
    /**
     * 部署全部流程 - admin
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/redeploy/all")
    @ResponseBody
    public Message redeployAll(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir, 
				    		HttpServletResponse response) throws Exception {
    	Message message = new Message();
    	try {
    		/*List<Deployment> deploymentList = this.repositoryService.createDeploymentQuery().list();
    		//删除现有所有流程实例
    		for(Deployment deployment : deploymentList){
    			String deploymentId = deployment.getId();
    			this.repositoryService.deleteDeployment(deploymentId, true);
    		}*/
    		
    		//重新部署全部流程实例(资源没有变化时不会重复部署，只部署流程有变化的资源)
    		//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
    		this.workflowProcessDefinitionService.deployAllFromClasspath(exportDir);
    		
    		//方法二：通过classpath/bpmn下的流程描述文件部署-流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir);
    		message.setStatus(Boolean.TRUE);
        	message.setMessage("已重新部署全部流程！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
        	if("The classpath:/deploy/ is empty!".equals(e.getMessage())){
        		message.setMessage("重新部署流程失败！classpath:/deploy/ 目录为空！");
        	} else if("can not find .zip or .bar in classpath:/deploy/ !".equals(e.getMessage())){
        		message.setMessage("重新部署流程失败！系统没有找到您部署的文件！");
        	} else {
        		message.setMessage("重新部署流程失败！");
        	}
		}
    	return message;
    }
    
    /**
     * 激活、挂起流程实例-根据processDefinitionId - admin
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateProcessStatusByProDefinitionId")
    @ResponseBody
    public Message updateProcessStatusByProDefinitionId(
    		@RequestParam("status") String status,
    		@RequestParam("processDefinitionId") String processDefinitionId) throws Exception{
    	//如果用/{status}/{processDefinitionId} rest风格，@PathVariable获取的processDefinitionId 为com.zml.oa,实际值为com.zml.oa.vacation:1:32529。难道是BUG?
    	Message message = new Message();
    	if (status.equals("active")) {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            message.setStatus(Boolean.TRUE);
            message.setMessage("已激活ID为[" + processDefinitionId + "]的流程定义。");
        } else if (status.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            message.setStatus(Boolean.TRUE);
            message.setMessage("已挂起ID为[" + processDefinitionId + "]的流程定义。");
        }
    	return message;
    }
    
    /**
     * admin
     * 删除部署的流程，级联删除流程实例 true。
     * 不管是否指定级联删除，部署的相关数据均会被删除，这些数据包括流程定义的身份数据（IdentityLink）、流程定义数据（ProcessDefinition）、流程资源（Resource）
     * 部署数据（Deployment）。
     * 如果设置级联(true)，则会删除流程实例数据（ProcessInstance）,其中流程实例也包括流程任务（Task）与流程实例的历史数据；如果设置flase 将不会级联删除。
     * 如果数据库中已经存在流程实例数据，那么将会删除失败，因为在删除流程定义时，流程定义数据的ID已经被流程实例的相关数据所引用。
     * 
     * 删除失败时，是ACT_RU_IDENTITYLINK中TASK_ID有值，ACT_RU_TASK 无法删除
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(@RequestParam("deploymentId") String deploymentId) {
    	this.repositoryService.deleteDeployment(deploymentId, true);
        return new Message(Boolean.TRUE, "删除成功！");
    }
    
    /**
     * 转换为model - admin
     * @param processDefinitionId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @RequestMapping(value = "/convert_to_model")
    @ResponseBody
    public Message convertToModel(@RequestParam("processDefinitionId") String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
    	ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = this.repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        org.activiti.engine.repository.Model modelData = this.repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        this.repositoryService.saveModel(modelData);

        this.repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return new Message(Boolean.TRUE, "转换成功！请到【 流程设计模型 】菜单中查看！");
    }
    
    /**
     * 显示图片通过部署id，不带流程跟踪(没有乱码问题) - admin
     * @param processDefinitionId
     * @param resourceType	资源类型(xml|image)
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process-definition")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
    	InputStream resourceAsStream = this.processService.getDiagramByProDefinitionId_noTrace(resourceType, processDefinitionId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    /**
     * 显示图片通过流程id，不带流程跟踪(没有乱码问题)
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = this.processService.getDiagramByProInstanceId_noTrace(resourceType, processInstanceId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    /**
     * 自定义流程跟踪信息-显示比较灵活
     *
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/trace/{pid}")
    @ResponseBody
    public List<Map<String, Object>> traceProcess(@PathVariable("pid") String processInstanceId) throws Exception {
        List<Map<String, Object>> activityInfos = this.traceService.traceProcess(processInstanceId);
        return activityInfos;
    }
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @param response
     * @throws Exception 
     */
    @RequestMapping(value = "/process/showDiagram/{processInstanceId}", method = RequestMethod.GET)
	public void showDiagram(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) throws Exception {
	        InputStream imageStream = this.processService.getDiagram(processInstanceId);
	        // 输出资源内容到相应对象
	        byte[] b = new byte[1024];
	        int len;
	        while ((len = imageStream.read(b, 0, 1024)) != -1) {
	            response.getOutputStream().write(b, 0, len);
	        }
	}
    
    /**
	 * 委派任务
	 * 委派也是代办、协办，你领导接到一个任务，让你代办，你办理完成后任务还是回归到你的领导，事情是你做的，功劳是你领导的，这就是代办。
	 * 所以代办人完成任务后，任务还会回到原执行人，流程不会发生变化。
	 * @param taskId	代办人
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/delegateTask")
	@ResponseBody
	public Message delegateTask(@RequestParam("taskId") String taskId, @RequestParam("userId") String userId){
		Message message = new Message();
		try {
			this.processService.doDelegateTask(userId, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("委派任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！委派任务失败！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("委派任务失败，系统错误！");
		}
		return message;
	}
    
    /**
	 * 转办任务，办理完成后，流程会继续向下走。
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/transferTask")
	@ResponseBody
	public Message transferTask(@RequestParam("taskId") String taskId, @RequestParam("userId") String userId){
		Message message = new Message();
		try {
			this.processService.doTransferTask(userId, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("转办任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！转办任务失败！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("转办任务失败，系统错误！");
		}
		return message;
	}
	
	/**
	 * 撤销任务
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/revoke")
	@ResponseBody
	public Message revoke(@RequestParam("taskId") String taskId, @RequestParam("processInstanceId") String processInstanceId) throws Exception{
		Message message = new Message();
		
		try {
			Integer revokeFlag = this.processService.revoke(taskId, processInstanceId);
//			Integer revokeFlag = this.revokeTaskService.revoke(taskId, processInstanceId);
//			Command<Integer> cmd = new RevokeTask(taskId, processInstanceId);
//			Integer revokeFlag = this.processEngine.getManagementService().executeCommand(cmd);
			
			
			if(revokeFlag == 0){
				message.setStatus(Boolean.TRUE);
				message.setMessage("撤销任务成功！");
			}else if(revokeFlag == 1){
				message.setStatus(Boolean.FALSE);
				message.setMessage("撤销任务失败 - [ 此任务所在流程已结束! ]");
			}else if(revokeFlag == 2){
				message.setStatus(Boolean.FALSE);
				message.setMessage("撤销任务失败 - [ 下一结点已经通过,不能撤销! ]");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("撤销任务失败 - [ 内部错误！]");
			throw e;
		}
		return message;
	}
	
    /**
     * 任务跳转（包括回退和向前）至指定活动节点
     * @param currentTaskId
     * @param targetTaskDefinitionKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/jumpTask")
    @ResponseBody
    public Message jumpTargetTask(@RequestParam("taskId") String currentTaskId, @RequestParam("taskDefinitionKey") String targetTaskDefinitionKey) throws Exception{
    	Message message = new Message();
    	try {
			this.processService.moveTo(currentTaskId, targetTaskDefinitionKey);
			message.setStatus(Boolean.TRUE);
        	message.setMessage("任务跳转成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
        	message.setMessage("任务跳转失败！");
		}
    	return message;
    }
    
    @RequestMapping(value = "/getTaskName")
    @ResponseBody
    public Map<String, Object> getCurrentTaskName(@RequestParam("taskId") String processInstanceId) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(StringUtils.isNotBlank(processInstanceId)) {
    		Task task = this.processService.getTaskByProcessInstanceId(processInstanceId);
    		map.put("taskName", task.getName());
    	} else {
    		map.put("taskName", "任务不存在");
    	}
    	return map;
    }
}
