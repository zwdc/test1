package com.hdc.process.task.ServiceTask;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.ITaskInfoService;
/**
 * 检查反馈情况
 * @author ZML
 *
 */
@Component
public class CheckFeedback implements JavaDelegate {

	@Autowired
	private ITaskInfoService taskInfoService;
	
	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String taskInfoId = (String) execution.getVariable("taskInfoId");
		TaskInfo taskInfo = this.taskInfoService.findById(new Integer(taskInfoId));
		List<FeedbackRecord> list =  this.feedbackService.findByTaskId(new Integer(taskInfoId));
		Integer feedbackCycle = taskInfo.getFeedbackCycle();	//获取反馈周期
		switch (feedbackCycle) {
			case 0:	//默认一次
				if(list.size() > 0){
					execution.setVariable("needFeedback", false);	//无需提示填写反馈信息
				} else {
					Date beginDate = new Date();
			  	    Date endDate = taskInfo.getEndTaskDate();
			  	    long betweenDays = betweenDays(endDate, beginDate); 
			  	    if(betweenDays <= 2) {	//提前两天提示
			  	    	execution.setVariable("needFeedback", true);
			  	    }
				}
				break;
			case 1: //每周一次
				Date assignDate = taskInfo.getAssignDate();		//交办时间
				Date endTaskDate = taskInfo.getEndTaskDate();	//办结日期
				Date nowDate = new Date();
				
				GregorianCalendar gc=new GregorianCalendar(); 
			  	gc.setTime(assignDate); 
			  	Date tempDate = gc.getTime();
			  	while(tempDate.getTime() <= endTaskDate.getTime()) {	//动态日期 < 办结日期
			  		gc.add(3, 1);	//加一周
			  		if(tempDate.getTime() < nowDate.getTime() && nowDate.getTime() < gc.getTime().getTime()){
			  			List<FeedbackRecord> fList = this.feedbackService.findByDate(tempDate, gc.getTime());
			  			long betweenDays = betweenDays(gc.getTime(), nowDate);
			  			if(fList.size() == 0 && betweenDays >= 0 && betweenDays <= 2) {	//提前两天提示
			  				execution.setVariable("needFeedback", true);
			  			} else {
			  				execution.setVariable("needFeedback", false);
			  			}
			  			break;
			  		}
			  		tempDate = gc.getTime(); //加一周后的时间
			  	}
				
				break;
			case 2: //每月一次(和每周一次一样，不同的是 月份加一)
				Date assignDate2 = taskInfo.getAssignDate();		//交办时间
				Date endTaskDate2 = taskInfo.getEndTaskDate();	//办结日期
				Date nowDate2 = new Date();
				
				GregorianCalendar gc2=new GregorianCalendar(); 
			  	gc2.setTime(assignDate2); 
			  	Date tempDate2 = gc2.getTime();
			  	while(tempDate2.getTime() <= endTaskDate2.getTime()) {	//动态日期 < 办结日期
			  		gc2.add(2, 1);	//月份加一
			  		if(tempDate2.getTime() < nowDate2.getTime() && nowDate2.getTime() < gc2.getTime().getTime()){
			  			List<FeedbackRecord> fList = this.feedbackService.findByDate(tempDate2, gc2.getTime());
			  			long betweenDays = betweenDays(gc2.getTime(), nowDate2);
			  			if(fList.size() == 0 && betweenDays >= 0 && betweenDays <= 2) {
			  				execution.setVariable("needFeedback", true);
			  			} else {
			  				execution.setVariable("needFeedback", false);
			  			}
			  			break;
			  		}
			  		tempDate = gc2.getTime(); //加一月后的时间
			  	}
				break;
			default:
				break;
		}
		
	}
	
	//计算相差天数
	private Long betweenDays(Date beginDate, Date endDate) {
		return (long)((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 *24) + 0.5); 
	}

}
