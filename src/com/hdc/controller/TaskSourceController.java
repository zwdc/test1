package com.hdc.controller;

import java.util.ArrayList;
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
import com.hdc.entity.TaskSource;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants;
import com.hdc.util.FileDownloadUtils;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;

/**
 * 任务来源控制器
 * @author ZML
 *
 */
@Controller
@RequestMapping("/taskSource")
public class TaskSourceController {

	@Autowired
	private ITaskSourceService taskSourceService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "taskSource/list_taskSource";
	}
	
	/**
	 * 跳转添加或修改页面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(@RequestParam(value="id", required=false) Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("taskSource/main_taskSource");
		if(id != null) {
			mv.addObject("source", this.taskSourceService.findById(id));
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
	public Datagrid<Object> getList(Parameter param) throws Exception {
		Page<TaskSource> page = new Page<TaskSource>(param.getPage(), param.getRows());
		List<TaskSource> list = this.taskSourceService.getListPage(param, page);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskSource taskSource : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", taskSource.getId());
			map.put("name", taskSource.getName());
			map.put("info", taskSource.getInfo());
			map.put("taskTypeName", taskSource.getTaskInfoType().getName());
			map.put("sourceDate", taskSource.getSourceDate());
			map.put("createDate", taskSource.getCreateDate());
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	/**
	 * 添加或修改
	 * @param TaskSource
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(
				TaskSource taskSource, 
				@RequestParam("file") MultipartFile file,
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		Integer id = taskSource.getId();
		try {
			if(!file.isEmpty()) {
				String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				taskSource.setFilePath(filePath);
				taskSource.setFileName(file.getOriginalFilename());
				taskSource.setUploadDate(new Date());
			}
			if(id == null) {
				this.taskSourceService.doAdd(taskSource);
				message.setMessage("添加成功！");
			} else {
				this.taskSourceService.doUpdate(taskSource);
				message.setData(id);
				message.setMessage("修改成功！");
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
    @RequestMapping("/downloadFile")
    public void downloadFile(
    		@RequestParam("id") Integer id,
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!BeanUtils.isBlank(id)){
    		TaskSource TaskSource = this.taskSourceService.findById(id);
    		if(StringUtils.isBlank(TaskSource.getFileName()) || StringUtils.isBlank(TaskSource.getFilePath())){
    			response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("您下载的文件不存在！");
    		} else {
    			FileDownloadUtils.download(request, response, TaskSource.getFilePath());
    			
    		}
    	} else {
    		response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("您下载的文件不存在！");
    	}
    }
    
    /**
	 * 详情页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/details/{id}")
	public ModelAndView details(@PathVariable("id") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("taskSource/details_taskSource");
		TaskSource taskSource = this.taskSourceService.findById(id);
		mv.addObject("source", taskSource);
		return mv;
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception {
		Message message = new Message();
		this.taskSourceService.doDelete(id);
		message.setMessage("删除成功！");
		return message;
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 * @throws Exception
	 */
/*	@RequestMapping("/getAllList")
	@ResponseBody
	public List<Object> getList() throws Exception {
		List<TaskSource> list = this.taskSourceService.getAllList();
		List<Object> jsonList=new ArrayList<Object>(); 
		for(TaskSource taskSource : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", taskSource.getId());
			map.put("name", taskSource.getName());
			map.put("taskType", taskSource.getTaskInfoType().getName());
			map.put("sourceDate", taskSource.getSourceDate());
			jsonList.add(map);
		}
		return jsonList;
	}*/
}
