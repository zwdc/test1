package com.hdc.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.RefuseReason;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IRefuseReasonService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants;
import com.hdc.util.Constants.TaskInfoStatus;
import com.hdc.util.FileDownloadUtils;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;

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
	private IRefuseReasonService rrefuseReasonService;
	
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
		if(!BeanUtils.isBlank(id)) {
			TaskInfo taskInfo = this.taskInfoService.findById(id);
			mv.addObject("taskInfo", taskInfo);
		}
		return mv;
	}
	
	/**
	 * 获取列表分页数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(
				Parameter param, 
				@RequestParam(value = "status", required = false) String status,
				@RequestParam(value = "taskType", required = false) Integer taskType) throws Exception{
		Page<TaskInfo> page = new Page<TaskInfo>(param.getPage(), param.getRows());
		Map<String, Object> m = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(status)) {
			m.put("status", status);
		}
		if(taskType != null) {
			m.put("taskType", taskType);
		}
		
		List<TaskInfo> list = this.taskInfoService.getListPage(param, page, m);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskInfo task : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", task.getId());
			map.put("title", task.getTitle());
			map.put("taskNo", task.getTaskNo());
			map.put("createTaskDate", task.getCreateTaskDate());
			map.put("assignDate", task.getAssignDate());
			map.put("endTaskDate", task.getEndTaskDate());
			map.put("feedbackCycle", task.getFeedbackCycle());
			map.put("feedbaceDate", task.getFeedbaceDate());
			map.put("hostGroup", task.getHostGroup().getId());				//主办单位
			map.put("assistantGroup", task.getAssistantGroup().getId());	//协办单位
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
			if(BeanUtils.isBlank(id)) {
				taskInfo.setStatus(TaskInfoStatus.WAIT_FOR_CLAIM.toString());
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
	
	/**
	 * 上传附件
	 * @param file
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadFile")
	@ResponseBody
	public Message uploadFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "taskInfoId") Integer id,
			HttpServletRequest request) throws Exception {
		Message message = new Message();
		try {
			if(!BeanUtils.isBlank(file)) {
				String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				TaskInfo taskInfo = this.taskInfoService.findById(id);
				taskInfo.setFilePath(filePath);
				taskInfo.setFileName(file.getOriginalFilename());
				taskInfo.setUploadDate(new Date());
				this.taskInfoService.doUpdate(taskInfo);
				message.setMessage("文件上传成功!");
				message.setData(id.toString());
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
     * 附件下载
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/downloadFile")
    public void downloadFile(
    		@RequestParam("id") Integer id,
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!BeanUtils.isBlank(id)){
    		TaskInfo taskInfo = this.taskInfoService.findById(id);
    		if(StringUtils.isBlank(taskInfo.getFileName()) || StringUtils.isBlank(taskInfo.getFilePath())){
    			response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("您下载的文件不存在！");
    		} else {
    			FileDownloadUtils.download(request, response, taskInfo.getFilePath());
    		}
    	} else {
    		response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("您下载的文件不存在！");
    	}
    }
    /**
     * 分配任务
     * @return
     * @throws Exception
     */
    @RequestMapping("/assignTask")
    @ResponseBody
    public Message assignTask(TaskInfo taskInfo) throws Exception {
    	Message message = new Message();
    	try {
    		this.taskInfoService.doStartProcess(taskInfo);
    		message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
    	return message;
    }
    
    /**
     * 签收
     * @return
     * @throws Exception 
     */
    @RequestMapping("/claim/{id}")
    @ResponseBody
    public Message claim(@PathVariable("id") Integer id) throws Exception{
    	Message message = new Message();
    	TaskInfo taskInfo = this.taskInfoService.findById(id);
    	taskInfo.setStatus(TaskInfoStatus.IN_HANDLING.toString());
    	this.taskInfoService.doUpdate(taskInfo);
    	message.setMessage("签收成功！");
    	return message;
    }
    
    /**
     * 拒绝签收
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/refuse/{id}")
    @ResponseBody
    public Message refuseClaim(@PathVariable("id") Integer id, RefuseReason refuseReason) throws Exception {
    	Message message = new Message();
    	TaskInfo taskInfo = this.taskInfoService.findById(id);
    	taskInfo.setStatus(TaskInfoStatus.REFUSE_CLAIM.toString());
    	refuseReason.setTaskInfo(taskInfo);
    	this.rrefuseReasonService.doAdd(refuseReason);
    	message.setMessage("操作成功！");
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
		mv.addObject("taskInfo", taskInfo);
		return mv;
	}
    
    public static int dayForWeek(String pTime) throws Exception {
    	  DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	  Calendar c = Calendar.getInstance();
    	  c.setTime(format.parse(pTime));
    	  int dayForWeek = 0;
    	  if(c.get(Calendar.DAY_OF_WEEK) == 1){
    	   dayForWeek = 7;
    	  }else{
    	   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    	  }
    	  return dayForWeek;
	 }
    
    public static void main(String[] args) throws Exception {
		System.out.println("当前日期是星期几："+dayForWeek("2016-03-06"));
		
		//一个月有几天
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		System.out.println("本月有多少天："+maxDay);
		
		Calendar c = Calendar.getInstance();
  	    System.out.println("今天是这个月的第几个星期几："+c.get(Calendar.DAY_OF_WEEK_IN_MONTH));
  	    System.out.println("本月的第几周："+c.get(Calendar.WEEK_OF_MONTH));	
		
    }
}
