package com.hdc.util;

import org.apache.commons.lang3.StringUtils;

import com.hdc.util.Constants.ApprovalStatus;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.Constants.OrderStatus;

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
			case PROJECT:
				value = "立项表";
				break;
			case SALES:
				value = "销售审批表";
				break;
			case SALES_CONTRACT:
				value = "销售合同";
				break;
			case PROCUREMENT_CONTRACT:
				value = "采购合同";
				break;
			case PAYMENT:
				value = "开票";
				break;
			case OUTBOUND:
				value = "出库单";
				break;
			case PROCUREMENT_TUNHUO:
				value = "囤货采购审批表";
				break;
			case PROCUREMENT_BUHUO:
				value = "补货采购审批表";
				break;
			default:
				break;
		}
		return value;
	}
	
	public static String productPartType(Integer partType) {
		String value = "";
		switch( partType ){
			case 1:
			  	value = "模块" ;
			  	break;
		  	case 2:
				value = "工作带";
				break;
		  	case 3:
				value = "探头";
				break;
		  	case 4:
				value = "适配器";
				break;
		  	case 5:
				value = "穿刺";
				break;
		  	case 6:
				value = "显示器";
				break;
		  	case 7:
				value = "保护罩";
				break;
		  	case 8:
		  		value = "保修";
		  		break;
		  	case 9:
		  		value = "主机系统/平台";
		  		break;
		  	case 0:
				value = "其他";
				break;
		  	default:
				break;
		}
		return value;
	}
	
	//返回招投标类型
	public static String btType(Integer btType) {
		String value = "";
		switch( btType ){
			case 1:
				value = "国际招投标";
				break;
			case 2:
				value = "国内招投标";
				break;
			case 3:
				value = "政府招投标";
				break;
			case 4:
				value = "院内议标";
				break;
			case 5:
				value = "不参与投标";
				break;
			default:
				break;
		}
		return value;
	}
	
	//返回合作伙伴中公司类型
	public static String partnerCType(Integer cType) {
		switch (cType) {
			case 1:
				return "供应商";
			case 2:
				return "外贸公司";
			case 3:
				return "厂家";
			case 4:
				return "银行";
			case 5:
				return "物流";
			case 6:
				return "商检";
			case 7:
				return "海关";
			case 8 :
				return "经销商" ;
			case 9:
				return "医院" ;
			case 0:
				return "其他";
			default:
				return "";
		}
	}
	
	//返回订单状态
	public static String orderStatus(String status) {
		
		switch (OrderStatus.getValue(status)) {
			case WAIT_FOR_PROCUREMENT:
				return "<span class='text-muted'>待采购</span>";
			case PROCUREMENT:
				return "<span class='text-warning'>采购中</span>";
			case IN_BOUNDABLE:
				return "<span class='text-success'>可入库</span>";
			case IN_BOUNDED:
				return "<span class='text-success'>已入库</span>";
			case OUT_BOUNDABLE:
				return "<span class='text-success'>可出库</span>";
			case OUT_BOUNDED:
				return "<span class='text-success'>已出库</span>";
			case WAIT_FOR_OPERATION:
				return "<span class='text-danger'>订单待检验</span>";
			default:
				return "";
		}
	}
}
