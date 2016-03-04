package com.hdc.service;

import java.util.List;

import com.hdc.entity.FeedbackRecord;

/**
 * 反馈接口
 * @author ZML
 *
 */
public interface IFeedbackRecordService {

	/**
	 * 根据反馈id查询反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FeedbackRecord findById(Integer id) throws Exception;
	
	/**
	 * 根据taskInfoId查询反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception;
}
