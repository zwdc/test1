package com.hdc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants;
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
			mv.addObject("taskInfo", this.taskInfoService.findById(id));
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
	public Datagrid<Object> getList(Parameter param) throws Exception{
		Page<TaskInfo> page = new Page<TaskInfo>(param.getPage(), param.getRows());
		List<TaskInfo> list = this.taskInfoService.getListPage(param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskInfo task : list) {
			Map<String, Object> map=new HashMap<String, Object>();
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
	public Message saveOrUpdate(
				TaskInfo taskInfo, 
				@RequestParam("file") MultipartFile file,
				HttpServletRequest request) {
		Message message = new Message();
		Integer id = taskInfo.getId();
		try {
			if(!BeanUtils.isBlank(file)) {
				String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				taskInfo.setFilePath(filePath);
				taskInfo.setFileName(file.getOriginalFilename());
				taskInfo.setUploadDate(new Date());
			}
			if(BeanUtils.isBlank(id)) {
				this.taskInfoService.doAdd(taskInfo);
				message.setMessage("添加成功！");
			} else {
				this.taskInfoService.doUpdate(taskInfo);
				message.setMessage("修改成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setTitle("出错了！");
			if(e instanceof FileSizeLimitExceededException){
				Long actual = ((FileSizeLimitExceededException) e).getActualSize();
				Long permitted = ((FileSizeLimitExceededException) e).getPermittedSize();
				message.setMessage("上传失败！文件大小超过限制，最大："+getFileMB(permitted)+",实际大小："+getFileMB(actual));
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
}
