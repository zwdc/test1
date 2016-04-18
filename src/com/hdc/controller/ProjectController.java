package com.hdc.controller;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.service.IProjectService;

/**
 * 任务交办管理器
 * @author ZML
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;
	
	
	/**
	 * 根据标识跳转到不同页面
	 * @param type
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(@RequestParam("type") Integer type) {
		if(type == 1) {
			//待签收
			return "project/list_c_project";
		} else if(type == 2) {
			//办理中
			return "project/list_h_project";
		} else if(type == 3) {
			//申请办结
			return "project/list_ae_project";
		} else if(type == 4) {
			//已办结
			return "project/list_e_project";
		}
		return null;
	}
	
	/**
	 * 根据type判断页面 签收页面、审批页面、修改页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/toProject/{type}")
	public ModelAndView toProject(@PathVariable("type") String type, @RequestParam("projectId") Integer projectId) throws Exception {
		ModelAndView mv = new ModelAndView();
		if("modify".equals(type)) {
			mv.setViewName("project/modify_project");
		} else if("claim".equals(type)) {
			mv.setViewName("project/claim_project");
		} else if("approval".equals(type)) {
			mv.setViewName("project/approval_project");
		} else if("details".equals(type)) {
			mv.setViewName("project/details_project");
		}
    	Project project = this.projectService.findById(projectId);
    	if(project != null) {
			mv.addObject("taskInfo", project.getTaskInfo());
			mv.addObject("project", project);
		}
    	return mv;
	}
	
	/**
	 * 更新操作
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Message update(@RequestParam("projectId") String projectId, @RequestParam("suggestion") String suggestion) throws Exception {
		Message message = new Message();
		try {
			this.projectService.doUpdateById(projectId, suggestion);
			message.setMessage("更新成功！");
			message.setData(projectId);
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("更新失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 根据type获取不同数据
	 * @param param
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Map<String, Object>> getList(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(param.getPage(), param.getRows());
		List<Map<String, Object>> list = this.projectService.getProjectList(param, type, page);
		return new Datagrid<Map<String, Object>>(page.getTotal(), list);
	}
	
	/**
	 * 签收任务交办表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/claimProject/{projectId}")
	@ResponseBody
	public Message claimProject(@PathVariable("projectId") String projectId) throws Exception {
		Message message = new Message();
		try {
			if(StringUtils.isNotBlank(projectId)) {
				Boolean flag = this.projectService.doClaimProject(projectId);
				if(flag) {
					message.setMessage("签收成功！");
				} else {
					message.setMessage("该任务已被其他人签收！");
				}
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("签收失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping("/callApproval")
	@ResponseBody
	public Message callApproval(@RequestParam("projectId") Integer projectId, @RequestParam("suggestion") String suggestion) throws Exception {
		Message message = new Message();
		try {
			this.projectService.doStartProcess(projectId, suggestion);
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
     * 审批
     * @param projectId
     * @param isPass
     * @param taskId
     * @param comment
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approval")
   	@ResponseBody
   	public Message approval(
   			@RequestParam("projectId") Integer projectId, 
   			@RequestParam("isPass") boolean isPass,
   			@RequestParam("taskId") String taskId, 
   			@RequestParam("comment") String comment)
   					throws Exception {
   		Message message = new Message();
   		try {
   			this.projectService.doApproval(projectId, isPass, taskId, comment);
   			message.setMessage("审批完成！");
   		} catch (Exception e) {
   			message.setStatus(Boolean.FALSE);
   			message.setMessage("审批失败！");
   		}
   		return message;
   }
    
   /**
    * 完成任务
    * @param projectId
    * @param suggestion
    * @param taskId
    * @return
    * @throws Exception
    */
    @RequestMapping("/completeTask")
    @ResponseBody
    public Message completeTask(
    			@RequestParam("projectId") Integer projectId, 
    			@RequestParam("suggestion") String suggestion, 
    			@RequestParam(value = "taskId", required = false) String taskId) throws Exception {
    	Message message = new Message();
    	try {
    		this.projectService.doCompleteTask(projectId, suggestion, taskId);
    		message.setMessage("申请成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("申请失败!");
			throw e;
		}
    	return message;
    }
    
    /**
     * 跳转到拒签页面
     * @return
     */
    @RequestMapping("/toRefuse")
    @ResponseBody
    public ModelAndView toRefuse() {
    	ModelAndView mv = new ModelAndView("project/refuse_project");
    	return mv;
    }
    
    /**
     * 拒签收
     * @param projectId
     * @param refuseReason
     * @return
     */
    @RequestMapping("/refuse")
    @ResponseBody
    public Message refuse(@RequestParam("projectId") Integer projectId, @RequestParam("refuseReason") String refuseReason) {
    	Message message = new Message();
    	
    	return message;
    }
    
	
}
