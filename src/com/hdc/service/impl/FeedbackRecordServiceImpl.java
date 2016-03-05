package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.FeedbackRecord;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackRecordService;

@Service
public class FeedbackRecordServiceImpl implements IFeedbackRecordService {

	@Autowired
	private IBaseService<FeedbackRecord> baseService;
	
	@Override
	public FeedbackRecord findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackRecord.class, id);
	}

	@Override
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception {
		String hql = "from FeedbackRecord where taskInfo.id = " + id;
		return this.baseService.find(hql);
	}

	@Override
	public Serializable doAdd(FeedbackRecord feedback) throws Exception {
		return this.baseService.add(feedback);
	}

	@Override
	public void doUpdate(FeedbackRecord feedback) throws Exception {
		this.baseService.update(feedback);
	}

}
