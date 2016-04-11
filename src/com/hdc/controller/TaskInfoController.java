package com.hdc.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Group;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.User;
import com.hdc.service.IGroupService;
import com.hdc.service.ITaskInfoService;
import com.hdc.service.IUserService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.TaskInfoStatus;
import com.hdc.util.UserUtil;
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
			map.put("info", task.getInfo());
			map.put("createTaskDate", task.getCreateTaskDate());
			map.put("endTaskDate", task.getEndTaskDate());
			map.put("taskSourceName", task.getTaskSource().getName()); 	//任务来源
			map.put("fbFrequencyName", task.getFbFrequency().getName());//反馈频度
			map.put("urgency", task.getUrgency());	//急缓程度
			map.put("status", task.getStatus());
			jsonList.add(map);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	/*private String getUserName(String userIds) throws NumberFormatException, Exception {
		if( null == userIds ){
			return "" ;
		} else {
			String[] ids = userIds.split(",");
			String names = "";
			for(String id : ids){
				User user = this.userService.getUserById(new Integer(id));
				names += user.getName()+",";
			}
			return names.substring(0, names.length()-1);
		}
	}
	
	private String getGroupName(String groupIds) throws Exception {
		if( null == groupIds ){
			return "" ;
		} else {
			String[] ids = groupIds.split(",");
			String names = "";
			for(String id : ids) {
				Group group = this.groupService.getGroupById(new Integer(id));
				names += group.getName()+",";
			}
			return names.substring(0, names.length()-1);
		}
	}*/
 	
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
	
    /**
     * 申请审批，由秘书长先进行审批，通过后承办单位可签收。
     * @return
     * @throws Exception
     */
    @RequestMapping("/approvalTask")
    @ResponseBody
    public Message approvalTask(TaskInfo taskInfo) throws Exception {
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
//    	taskInfo.setClaimDate(new Date());
    	this.taskInfoService.doClaimTask(taskInfo);
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
    public Message refuseClaim(@PathVariable("id") Integer id) throws Exception {
    	Message message = new Message();
    	User user = UserUtil.getUserFromSession();
    	TaskInfo taskInfo = this.taskInfoService.findById(id);
    	taskInfo.setStatus(TaskInfoStatus.REFUSE_CLAIM.toString());
    	message.setMessage("操作成功！");
    	return message;
    }
    
    /**
     * 申请办结
     * @param taskInfoId
     * @return
     * @throws Exception
     */
    @RequestMapping("/applyForEnd/{taskInfoId}")
    @ResponseBody
    public Message applyForEnd(@PathVariable("taskInfoId") Integer id) throws Exception {
    	Message message = new Message();
    	try {
    		this.taskInfoService.doCompleteTask(id);
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
  	    
  	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	    Date beginDate = format.parse("2016-03-02 15:27:30");
  	    Date endDate = format.parse("2016-03-08 11:27:30");
  	    long beginTime = beginDate.getTime(); 
  	    long endTime = endDate.getTime(); 
  	    long betweenDays = (long)((beginTime - endTime) / (1000 * 60 * 60 *24) + 0.5); 
  	    System.out.println("相差天数："+betweenDays);
  	    
  	    GregorianCalendar gc=new GregorianCalendar(); 
  	    gc.setTime(beginDate); 
  	    gc.add(3, 1);	//加一周
  	    //gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
  	    System.out.println("日期计算："+format.format(gc.getTime()));
  	    
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
