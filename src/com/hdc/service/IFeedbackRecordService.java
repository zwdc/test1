package com.hdc.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;

/**
 * 反馈接口
 * @author ZML
 *
 */
public interface IFeedbackRecordService {

	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> getListPage(Parameter param, Page<FeedbackRecord> page) throws Exception;
	/**
	 * 获取所有列表
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> getAllList() throws Exception;
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
	
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 保存反馈 完成任务
	 * @param feedback
	 * @param taskId
	 * @param file
	 * @param request
	 * @throws Exception
	 */
	public void doCompleteTask(
			FeedbackRecord feedback, 
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws Exception;
	
}
