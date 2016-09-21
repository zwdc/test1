package com.hdc.util;

import org.apache.commons.lang3.StringUtils;

import com.hdc.entity.User;
import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;

/**
 * 自定义标签
 * @author ZML
 *
 */
public class Functions {

	//返回审批状态
	public static String approvalStatus(String status) {
		if(StringUtils.isNotBlank(status)) {
			switch (ApprovalStatus.getValue(status)) {
			case APPROVAL_SUCCESS:
				return "<span class='text-success'>通过</span>";
			case APPROVAL_FAILED:
				return "<span class='text-danger'>不通过</span>";
			case PENDING:
				return "<span class='text-warning'>审批中</span>";
			case WAITING_FOR_APPROVAL:
				return "<span class='text-muted'>待审批</span>";
			case REAPPROVAL:
				return "<span class='text-danger'>需要重新审批</span>";
			default:
				return "";
			}
		} else {
			return "";
		}
	}
	
	//获取审批状态(审批成功和审批中 返回true, 待审批、审批失败、需要重新审批返回false)
	public static Boolean statusPermission(String status) {
		if(StringUtils.isNotBlank(status)) {
			switch (ApprovalStatus.getValue(status)) {
				case APPROVAL_SUCCESS:
				case PENDING:
				case REAPPROVAL:
					return true;
				case APPROVAL_FAILED:
				case WAITING_FOR_APPROVAL:
					return false;
				default:
					return false;
			}
		} else {
			return false;
		}
	}
	
	//获取审批状态(返回class样式)
	public static String statusClass(String status) {
		if(StringUtils.isNotBlank(status)) {
			switch (ApprovalStatus.getValue(status)) {
			case APPROVAL_SUCCESS:
				return "bg-success";
			case PENDING:
				return "bg-primary";
			case APPROVAL_FAILED:
				return "bg-danger";
			case WAITING_FOR_APPROVAL:
				return "bg-warning";
			case REAPPROVAL:
				return "bg-danger";
			default:
				return "bg-primary";
			}
		} else {
			return "bg-primary";
		}
	}
	
	//返回业务表单类型
	public static String businessForm(String businessForm) {
		String value = "";
		switch (BusinessForm.getValue(businessForm)) {
			case TASK_FORM:
				value = "任务表";
				break;
			case PROJECT_FORM:
				value = "任务交办表";
				break;
			case FEEDBACK_FORM:
				value = "任务反馈表";
				break;
			default:
				break;
		}
		return value;
	}
	
	public static String getUrgencyType(Integer type) {
		String value = "";
		switch( type ){
			case 1:
			  	value = "特提" ;
			  	break;
		  	case 2:
				value = "特急";
				break;
		  	case 3:
				value = "加急";
				break;
		  	case 4:
				value = "平急";
				break;
		  	default:
				break;
		}
		return value;
	}
	
	public static String getUserName() {
		User user = UserUtil.getUserFromSession();
		return user.getName();
	}
}
