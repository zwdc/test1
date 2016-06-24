package com.hdc.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.hdc.entity.ApprovalProcess;
import com.hdc.entity.Datagrid;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Group;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IExcel2TaskInfoService;
import com.hdc.service.IGroupService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.Constants;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.FeedbackStatus;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;
import com.uwantsoft.goeasy.client.goeasyclient.listener.GoEasyError;
import com.uwantsoft.goeasy.client.goeasyclient.listener.PublishListener;

/**
 * 督察处对任务进行管理
 * @author zhao
 *
 */

@Controller
@RequestMapping("/taskInfo")
public class TaskInfoController {

	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IExcel2TaskInfoService excel2TaskInfoService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "taskInfo/list_taskInfo";
	}
	
	/**
	 * 跳转添加修改页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(@RequestParam(value="id", required=false) Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("taskInfo/main_taskInfo");
		if(!BeanUtilsExt.isBlank(id)) {
			TaskInfo taskInfo = this.taskInfoService.findById(id);
			mv.addObject("taskInfo", taskInfo);
		}
		return mv;
	}
	
	/**
	 * 跳转到审批不通过的修改页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/toModify")
	public ModelAndView toModify(@RequestParam("id") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("taskInfo/modify_taskInfo");
		TaskInfo taskInfo = this.taskInfoService.findById(id);
		mv.addObject("taskInfo", taskInfo);
		return mv;
	}
	
	@RequestMapping("/toChooseGroup")
	public String toChooseGroup() {
		return "taskInfo/choose_group";
	}
	
	/**
	 * 根据承办单位显示 单位下的联系人
	 * 解决返回值为乱码
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getGroupUser/{groupId}", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getGroupUser(@PathVariable("groupId") Integer groupId) throws Exception {
		Group group = this.groupService.getGroupById(groupId);
		String userNames = "";
		for(User user : group.getUser()) {
			userNames += user.getName()+"、";
		}
		if(StringUtils.isNotBlank(userNames)) {
			return userNames.substring(0, userNames.length()-1);
		} else {
			return "";
		}
	}
	
	/**
	 * 跳转添加修改页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMultiInsert")
	public ModelAndView toMultiInsert() throws Exception {
		ModelAndView mv = new ModelAndView("taskInfo/multi_taskInfo");
		return mv;
	}
	
	/**
	 * 批量添加
	 * @param TaskInfo
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/multiInsert")
	@ResponseBody
	public Message multiInsert( 
				@RequestParam("file") MultipartFile file,
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		message.setStatus(true);
		message.setTitle("提示");
		message.setMessage("这是咋回事");
		try {
			if(!file.isEmpty()) {
				String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH+"\\excel");				
				message=this.excel2TaskInfoService.readXls(request.getSession().getServletContext().getRealPath(filePath));
				if(message.getStatus()==true){
					List<TaskInfo> taskInfoList=(List<TaskInfo>) message.getData();
					for(int i=0;i<taskInfoList.size();i++){
						this.taskInfoService.doAdd(taskInfoList.get(i));
					}				
				}
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setTitle("操作失败！");
			if(e instanceof FileSizeLimitExceededException){
				Long actual = ((FileSizeLimitExceededException) e).getActualSize();
				Long permitted = ((FileSizeLimitExceededException) e).getPermittedSize();
				message.setMessage("上传失败！文件大小超过限制，最大上传："+getFileMB(permitted)+",实际大小："+getFileMB(actual));
			} else if (e instanceof InvalidExtensionException){
				message.setMessage("不能上传此文件类型,请重新选择文件上传！");
			}
		}
		return message;
	}
	
    private String getFileMB(long byteFile){  
        if(byteFile==0)  
           return "0MB";  
        long mb=1024*1024;  
        return ""+byteFile/mb+"MB";  
    } 
	
	/**
	 * 获取列表分页数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(Parameter param) throws Exception{
		Page<TaskInfo> page = new Page<TaskInfo>(param.getPage(), param.getRows());
		Map<String, Object> m = new HashMap<String, Object>();
		List<TaskInfo> list = this.taskInfoService.getListPage(param, page, m);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskInfo task : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", task.getId());
			map.put("title", task.getTitle());
			map.put("info", task.getInfo());
			map.put("createTaskDate", task.getCreateTaskDate());
			map.put("endTaskDate", task.getEndTaskDate());
			map.put("taskSourceName", task.getTaskSource().getName()); 	//任务来源
			map.put("fbFrequencyName", task.getFbFrequency().getName());//反馈频度
			map.put("urgency", task.getUrgency());	//急缓程度
			map.put("status", task.getStatus());
			String[] hostStr=task.getHostGroup().split(",");
			StringBuffer major=new StringBuffer();
			StringBuffer groupName=new StringBuffer();
			for(int i=0;i<hostStr.length;i++){
				Group group=this.groupService.getGroupById(Integer.valueOf(hostStr[i].trim()));
				if(i==0){
					major.append(group.getMajorName());
					groupName.append(group.getName());
				}else{
					major.append(",");
					major.append(group.getMajorName());
					groupName.append(",");
					groupName.append(group.getName());
				}			
			}
			map.put("major", major);
			map.put("groupName",groupName);
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	/**
	 * 添加或修改
	 * @param taskInfo
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(TaskInfo taskInfo) {
		Message message = new Message();
		Integer id = taskInfo.getId();
		try {
			if(BeanUtilsExt.isBlank(id)) {
				taskInfo.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.toString());
				Serializable taskInfoId = this.taskInfoService.doAdd(taskInfo);
				message.setData(taskInfoId.toString());
				message.setMessage("添加成功！");
			} else {
				this.taskInfoService.doUpdate(taskInfo);
				message.setData(id);
				message.setMessage("修改成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setTitle("操作失败！");
		}
		
		return message;
	}
	/*
	 * 
	 */
  	@RequestMapping(value = "/delete/{id}")
   	public Message delete(@PathVariable("id") Integer id) throws Exception {
  		Message message = new Message();
		try {
			if(!BeanUtilsExt.isBlank(id)) {//如果id是空的，没有选择行
				this.taskInfoService.doDelete(id);
				message.setData(id);
				message.setMessage("删除成功！");
			} else {
				message.setData(id);
				message.setMessage("没有选择行，请选择某一行删除！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setTitle("操作失败！");
		}		
		return message;
  	}
	
    /**
     * 申请审批，由秘书长先进行审批，通过后承办单位可签收。
     * @return
     * @throws Exception
     */
    @RequestMapping("/callApproval")
    @ResponseBody
    public Message callApproval(TaskInfo taskInfo) throws Exception {
    	Message message = new Message();
    	try {
    		this.taskInfoService.doStartProcess(taskInfo);
    		message.setMessage("操作成功！");
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
            	message.setMessage("没有部署流程，请联系管理员在[流程定义]中部署相应流程文件！");
            } else {
            	message.setMessage("启动流程失败，系统内部错误！");
            }
            throw e;
        } catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
    	return message;
    }
    
    /**
     * 跳转到审批页面
     * @param taskInfoId
     * @return
     * @throws Exception
     */
    @RequestMapping("/toApproval")
    public ModelAndView toApproval(@RequestParam(value = "taskInfoId", required = false) Integer taskInfoId) throws Exception {
    	ModelAndView mv = new ModelAndView("taskInfo/approval_taskInfo");
    	TaskInfo taskInfo = this.taskInfoService.findById(taskInfoId);
    	mv.addObject("taskInfo", taskInfo);
    	return mv;
    }
    
    /**
     * 审批操作
     * @param taskInfoId
     * @param isPass
     * @param taskId
     * @param processInstanceId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approval")
	@ResponseBody
	public Message approval(
			@RequestParam("taskInfoId") Integer taskInfoId, 
			@RequestParam("isPass") boolean isPass,
			@RequestParam("taskId") String taskId, 
			@RequestParam("comment") String comment)
					throws Exception {
		Message message = new Message();
		try {
			this.taskInfoService.doApproval(taskInfoId, isPass, taskId, comment);
			message.setMessage("审批完成！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("审批失败！");
		}
		return message;
    }
    
    /**
     * 完成任务
     * @param taskInfo
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping("/completeTask")
    @ResponseBody
    public Message completeTask(TaskInfo taskInfo, @RequestParam(value = "taskId", required = false) String taskId) throws Exception {
    	Message message = new Message();
    	try {
    		this.taskInfoService.doCompleteTask(taskInfo, taskId);
    		message.setMessage("申请成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("申请失败!");
			throw e;
		}
    	return message;
    }
    
    /**
   	 * 详情页面
   	 * 
   	 * @param id
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/details/{id}")
   	public ModelAndView details(@PathVariable("id") Integer id) throws Exception {
   		ModelAndView mv = new ModelAndView("taskInfo/details_taskInfo");
   		TaskInfo taskInfo = this.taskInfoService.findById(id);
   		//以下获取任务下的反馈的列表
		List<Map> jsonList=new ArrayList<Map>();
		int fbWL=-1;
		Date currentDate=new Date();
		for(Project project:taskInfo.getProjectList()){
			for(FeedbackRecord fb:project.getFbrList()){
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("id", fb.getId());				
				if(FeedbackStatus.RETURNED.toString().equals(fb.getStatus())){
					fbWL=3;//反馈被退回
				}else if(FeedbackStatus.ACCEPT.toString().equals(fb.getStatus())){
					fbWL=4;//反馈采用fbWL=4,如果反馈采纳，则进入下一个反馈期
				}else if(FeedbackStatus.FEEDBACKING.toString().equals(fb.getStatus())){
					fbWL=5;//反馈中
				}else{
					if(currentDate.before(fb.getFeedbackStartDate()) && fb.getStatus()==null){
						fbWL=0;//未到反馈期
					}else if(currentDate.after(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null&&fb.getStatus()==null){
						fbWL=2;//红色警告
					}else if(currentDate.after(fb.getFeedbackStartDate())&&currentDate.before(fb.getFeedbackEndDate())&&fb.getFeedbackDate()==null){
						fbWL=1;//黄色警告
					}
				}		
				map.put("warningLevel", fbWL);
				map.put("feedbackStartDate", fb.getFeedbackStartDate());
				map.put("feedbackEndDate", fb.getFeedbackEndDate());
				map.put("groupName", fb.getProject().getGroup().getName());
				if(fb.getFeedbackUser()!=null){
					map.put("feedbackUser", fb.getFeedbackUser().getName());
				}else{
					map.put("feedbackUser", "--");
				}	
				map.put("feedbackDate", fb.getFeedbackDate());
				map.put("status", fb.getStatus());
				map.put("refuseCount", fb.getRefuseCount());
				map.put("delayCount", fb.getDelayCount());
				jsonList.add(map);
			}
		}		
		Datagrid fbList=new Datagrid(jsonList.size(),jsonList);
		Gson gson=new Gson();
		//在gson转换ArrayList的时候，不能有懒加载的对象		
		mv.addObject("feedback",gson.toJson(fbList));
   		mv.addObject("taskInfo", taskInfo);
   		return mv;
   	}
   	/**
	 * 获取审批流程列表分页数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
   	@RequestMapping(value = "/getApprovalProcess/{id}")
	public ModelAndView getApprovalProcess(@PathVariable("id") Integer id) throws Exception{
   		ModelAndView mv = new ModelAndView("taskInfo/ap_taskInfo");
		List<Map<String,Object>> list = this.taskInfoService.getApprovalProcess(id);
		System.out.println(list.get(0));;
		Datagrid fbList=new Datagrid(list.size(),list);
		Gson gson=new Gson();
		//在gson转换ArrayList的时候，不能有懒加载的对象		
		mv.addObject("approvalProcess",gson.toJson(fbList));
		return mv;
	}
	   
   	
   	
   	
   	
   	
   	
    public static void main(String[] args) throws Exception {
 	    //测试推送
 	    GoEasy goEasy = new GoEasy("0cf326d6-621b-495a-991e-a7681bcccf6a");
 	    goEasy.publish("zwdc_user_1", "您有将要到期尚未反馈的督察信息", new PublishListener(){
		@Override
		public void onSuccess() {
			System.out.println("消息发布成功。");
		}
		@Override
		public void onFailed(GoEasyError error) {
			System.out.println("消息发布失败, 错误编码：" + error.getCode() + " 错误信息： " +
					error.getContent());
		}
	});
 	    
   }
}
