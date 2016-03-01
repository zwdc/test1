package com.hdc.util;

import java.io.File;


/**
 * 常量类
 * @author ZML
 *
 */
public class Constants {
	
	/***************** system ******************/
	public static final String DB_NAME = "mysql";
	public static final String MESSAGE = "message";
	public static final String DEFAULT_PASSWORD = "123";
	
	/***************** session key *****************/
    public static final String CURRENT_USER = "user";
    //public static final String GROUP_ID = "groupId";
    public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    
	/***************** activiti *****************/
	public static final String ASSIGNEE = "assignee";
	public static final String CANDIDATE_USER = "candidateUser";
	public static final String CANDIDATE_GROUP = "candidateGroup";
	
	/***************** shiro *******************/
	public static final Integer PASSWORD_RETRY_COUNT = 5;	//登录次数超过此数时锁定
	public static final Integer PASSWORD_SHOW_JCAPTCHA = 3;	//登录次超过此数时数显示验证码
	
	
	/***************** service *******************/
	public static final String FILE_PATH  = File.separator + "file-store" + File.separator + "zwdc" + File.separator + "attachmentFile";		//审批合同上传路径
	public static final String FINAL_FILE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "finalFile"; 	//正本文件上传路径
	public static final String BT_NOTICE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "BTNoticeFile"; 	//中标通知书上传路径
	public static final String APTITUDE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "aptitudeFile"; 	//合作伙伴资质文件上传路径
	
    /**
     * 系统初始化时间
     */
    public static long SYS_INIT_TIME = System.currentTimeMillis();
    
	/**
	 * 审批状态枚举
	 * 审批中、待审批、审批成功(通过)、审批失败(不通过)、通过后重新提交审批(发现业务表单填写错了，但是审批已经通过了)
	 */
	public enum ApprovalStatus {
		PENDING("PENDING"), WAITING_FOR_APPROVAL("WAITING_FOR_APPROVAL"), APPROVAL_SUCCESS("APPROVAL_SUCCESS"), APPROVAL_FAILED("APPROVAL_FAILED"), REAPPROVAL("REAPPROVAL") ;
		private final String value;
		private ApprovalStatus(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
		
		public static ApprovalStatus getValue( String value ) {
			ApprovalStatus as = null;
			switch (value) {
				case "PENDING":
					as = PENDING;
					break;
				case "WAITING_FOR_APPROVAL":
					as = WAITING_FOR_APPROVAL;
					break;
				case "APPROVAL_SUCCESS":
					as = APPROVAL_SUCCESS;
					break;
				case "APPROVAL_FAILED":
					as = APPROVAL_FAILED;
					break;
				case "REAPPROVAL":
					as = REAPPROVAL;
					break;
				default:
					break;
			}
			return as;
		}
	}
	
	/**
	 * 业务类型枚举（重要文件、重要会议、政府工作报告、领导批示）
	 *
	 */
	public enum BusinessType {
		IMPORTANT_FILE("IMPORTANT_FILE"), IMPORTANT_MEETING("IMPORTANT_MEETING"), GOV_WORK_REPORT("GOV_WORK_REPORT"), LEAD_COMMENTS("LEAD_COMMENTS");
		private final String value;
		
		private BusinessType(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
		
		public static BusinessType getValue( String value) {
			BusinessType bt = null;
			switch (value) {
				case "IMPORTANT_FILE":	
					bt = IMPORTANT_FILE;
					break;
				case "IMPORTANT_MEETING":	
					bt = IMPORTANT_MEETING;
					break;
				case "GOV_WORK_REPORT":	
					bt = GOV_WORK_REPORT;
					break;
				default:
					break;
			}
			return bt;
		}
	}
	
	/**
	 * 业务表单类型（立项表、销售审批表、采购审批表、合同、开票、出库单等）-要与controller的ReequestMapping对应
	 * 可用来完成任务时根据表单类型判断去调用controller。也用在评论中，显示是哪个业务表单的评论(审批失败 修改时显示评论内容用)。
	 */
	public enum  BusinessForm {
		PROJECT("PROJECT"), SALES("SALES"), PROCUREMENT_TUNHUO("PROCUREMENT_TUNHUO"), PROCUREMENT_BUHUO("PROCUREMENT_BUHUO"), SALES_CONTRACT("SALES_CONTRACT"), PROCUREMENT_CONTRACT("PROCUREMENT_CONTRACT"), OTHER_CONTRACT("OTHER_CONTRACT"), OUTBOUND ("OUTBOUND"), INBOUND("INBOUND"), PAYMENT("PAYMENT"), INVOICE("INVOICE") ;
		private final String value;
		
		private BusinessForm(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
		
		public static BusinessForm getValue( String value ) {
			BusinessForm bf = null;
			switch (value) {
			case "PROJECT":
				bf = PROJECT;
				break;
			case "SALES":
				bf = SALES;
				break;
			case "PROCUREMENT_TUNHUO":
				bf = PROCUREMENT_TUNHUO;
				break;
			case "PROCUREMENT_BUHUO":
				bf = PROCUREMENT_BUHUO;
				break;
			case "SALES_CONTRACT":
				bf = SALES_CONTRACT;
				break;
			case "PROCUREMENT_CONTRACT":
				bf = PROCUREMENT_CONTRACT;
				break;
			case "OTHER_CONTRACT":
				bf = OTHER_CONTRACT;
				break;
			case "OUTBOUND":
				bf = OUTBOUND;
				break;
			case "INBOUND":
				bf = INBOUND;
				break;
			case "PAYMENT":
				bf = PAYMENT;
				break;
			case "INVOICE":
				bf=INVOICE;
			default:
				break;
			}
			return bf;
		}
	}
	
	
	/**
	 * 业务操作类型（针对代办任务中使用）
	 *
	 */
	public enum OperationType {
		ADD("ADD"), UPLOAD("UPLOAD"), APPROVAL("APPROVAL"), MODIFY("MODIFY"), PRINT("PRINT"), IN_BOUND("IN_BOUND"),STAMP("STAMP");
		private final String value;
		
		private OperationType(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
	}
	
	/**
	 * 订单状态枚举
	 * 待采购、采购中、可入库、已入库、可出库、待操作、已出库
	 */
	public enum OrderStatus {
		WAIT_FOR_PROCUREMENT("WAIT_FOR_PROCUREMENT"), PROCUREMENT("PROCUREMENT"), IN_BOUNDABLE("IN_BOUNDABLE"), IN_BOUNDED("IN_BOUNDED"), OUT_BOUNDABLE("OUT_BOUNDABLE"), WAIT_FOR_OPERATION("WAIT_FOR_OPERATION"), OUT_BOUNDED("OUT_BOUNDED");
		private final String value;
		private OrderStatus(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
		
		public static OrderStatus getValue( String value ) {
			OrderStatus as = null;
			switch (value) {
				case "WAIT_FOR_PROCUREMENT":
					as = WAIT_FOR_PROCUREMENT;
					break;
				case "PROCUREMENT":
					as = PROCUREMENT;
					break;
				case "IN_BOUNDABLE":
					as = IN_BOUNDABLE;
					break;
				case "IN_BOUNDED":
					as = IN_BOUNDED;
					break;
				case "OUT_BOUNDABLE":
					as = OUT_BOUNDABLE;
					break;
				case "WAIT_FOR_OPERATION":
					as = WAIT_FOR_OPERATION;
					break;
				case "OUT_BOUNDED":
					as = OUT_BOUNDED;
					break;
				default:
					break;
			}
			return as;
		}
	}
	
}