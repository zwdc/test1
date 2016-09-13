package com.hdc.service;

import com.hdc.entity.FeedbackDelay;

/**
 * 申请反馈延期接口
 * @author zhao
 *
 */
public interface IFeedbackDelayService {
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FeedbackDelay findById(Integer id) throws Exception;

	/**
	 * 启动反馈审批流程 
	 * @param feedbackDelay
	 * @throws Exception
	 */
	public void doStartProcess(FeedbackDelay feedbackDelay) throws Exception;
	
	/**
	 * 审批
	 * @param delayId
	 * @param isPass
	 * @param taskId
	 * @param processInstanceId
	 * @param comment
	 * @throws Exception
	 */
	public void doApproval(Integer delayId, boolean isPass, String taskId, String comment) throws Exception;
	
}
