package com.hdc.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hdc.entity.FeedbackRecord;

/**
 * 反馈接口
 * @author ZML
 *
 */
public interface IFeedbackRecordService {

	/**
	 * 添加
	 * @param feedback
	 * @return
	 * @throws Exception
	 */
	public Serializable doAdd(FeedbackRecord feedback) throws Exception;
	
	/**
	 * 更新
	 * @param feedback
	 * @throws Exception
	 */
	public void doUpdate(FeedbackRecord feedback) throws Exception;
	
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
	
	/**
	 * 通过指定时间区间查询
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findByDate(Date beginDate, Date endDate) throws Exception;
	
}
