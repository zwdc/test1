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
	public static final String FILE_PATH  = File.separator + "file-store";		//审批合同上传路径
	public static final String FINAL_FILE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "finalFile"; 	//正本文件上传路径
	public static final String BT_NOTICE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "BTNoticeFile"; 	//中标通知书上传路径
	public static final String APTITUDE_PATH = File.separator + "file-store" + File.separator + "zwdc" + File.separator+ "aptitudeFile"; 	//合作伙伴资质文件上传路径
	
    /**
     * 系统初始化时间
     */
    public static long SYS_INIT_TIME = System.currentTimeMillis();
    
	/**
	 * 审批状态枚举
	 * 审批中、待审批、审批成功(通过)、拒签收失败、审批失败(不通过)、通过后重新提交审批(发现业务表单填写错了，但是审批已经通过了)
	 */
	public enum ApprovalStatus {
		PENDING("PENDING"), WAITING_FOR_APPROVAL("WAITING_FOR_APPROVAL"), APPROVAL_SUCCESS("APPROVAL_SUCCESS"), REFUSE_FAILED("REFUSE_FAILED"), APPROVAL_FAILED("APPROVAL_FAILED"), REAPPROVAL("REAPPROVAL") ;
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
				case "REFUSE_FAILED":
					as = REFUSE_FAILED;
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
	 * 任务状态 待签收、已签收、拒签收、办理中、可以办结、申请办结中、已办结
	 * @author ZML
	 *
	 */
	public enum ProjectStatus {
		WAIT_FOR_CLAIM("WAIT_FOR_CLAIM"), CLAIMED("CLAIMED"), REFUSE_CLAIM("REFUSE_CLAIM"), IN_HANDLING("IN_HANDLING"), CAN_BE_FINISHED("CAN_BE_FINISHED"), APPLY_FINISHED("APPLY_FINISHED"), FINISHED("FINISHED");
		
		private final String value;
		private ProjectStatus(String value) {
			this.value = value;
		}
		
		@Override
        public String toString() {
            return this.value;
        }
		
		public static ProjectStatus getValue(String value) {
			ProjectStatus tis = null;
			switch(value) {
				case "WAIT_FOR_CLAIM":
					tis = WAIT_FOR_CLAIM;//待签收
					break;
				case "CLAIMED":
					tis = CLAIMED;//已签收
					break;
				case "REFUSE_CLAIM":
					tis = REFUSE_CLAIM;//拒签收
					break;
				case "IN_HANDLING":
					tis = IN_HANDLING;//办理中
					break;
				case "CAN_BE_FINISHED":
					tis = CAN_BE_FINISHED;  //可以办结
					break;
				case "APPLY_FINISHED":
					tis = APPLY_FINISHED;//申请办结
					break;
				case "FINISHED":
					tis = FINISHED;//已办结
					break;
				default:
					break;
			}
			return tis;
		}
	}
	
	/**
	 * 反馈处理中、已退回、已采用
	 * @author ZML
	 *
	 */
	public enum FeedbackStatus {
		FEEDBACKING("FEEDBACKING"), RETURNED("RETURNED"), ACCEPT("ACCEPT");
		private final String value;
		private FeedbackStatus(String value) {
			this.value = value;
		}
		@Override
        public String toString() {
            return this.value;
        }
		
		public static FeedbackStatus getValue(String value) {
			FeedbackStatus feedback = null;
			
			switch(value) {
				case "FEEDBACKING":
					feedback = FEEDBACKING;//反馈中
					break;
				case "RETURNED":
					feedback = RETURNED;//已退回
					break;
				case "ACCEPT":
					feedback = ACCEPT;//已采用
					break;
				default:
					break;
			}
			return feedback;
		}
		
	}
	
	/**
	 * 业务类型枚举（1.市长办公会议纪要、2.市政府常务会议纪要、3.市承担省政府重点工作目标任务、4.政府工作报告 5.领导交办事项 6.上级督办事项 7.省市县重点项目）
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
	 * 业务表单类型(任务表单(TaskInfo)、任务交办表(Project)、交办表办结(ProjectComplete)、任务反馈表(Feedback))
	 * 用途：审批评论时，来标记此评论所属类型
	 */
	public enum  BusinessForm {
		TASK_FORM("TASK_FORM"), PROJECT_FORM("PROJECT_FORM"), PROJECT_COMPLETE("PROJECT_COMPLETE"), FEEDBACK_FORM("FEEDBACK_FORM"), FEEDBACK_DELAY_FORM("FEEDBACK_DELAY_FORM");
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
			case "TASK_FORM":
				bf = TASK_FORM;
				break;
			case "PROJECT_FORM":
				bf = PROJECT_FORM;
				break;
			case "FEEDBACK_FORM":
				bf = FEEDBACK_FORM;
				break;
			case "PROJECT_COMPLETE":
				bf = PROJECT_COMPLETE;
				break;
			case "FEEDBACK_DELAY_FORM":
				bf = FEEDBACK_DELAY_FORM;
				break;
			default:
				break;
			}
			return bf;
		}
	}
	
}