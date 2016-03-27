package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.FeedbackFrequency;

public interface IFeedbackFrequencyService {
	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackFrequency> getListPage(Parameter param, Page<FeedbackFrequency> page) throws Exception;
	
	/**
	 * 获取所有列表
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackFrequency> getAllList() throws Exception;
	
	/**
	 * 通过id获取FeedbackFrequency
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FeedbackFrequency findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param feedbackFrequency
	 * @throws Exception
	 */
	public Serializable doAdd(FeedbackFrequency feedbackFrequency) throws Exception;
	
	/**
	 * 修改
	 * @param feedbackFrequency
	 * @throws Exception
	 */
	public void doUpdate(FeedbackFrequency feedbackFrequency) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
}
