package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackFrequencyService;

@Service
public class FeedbackFrequencyServiceImpl implements IFeedbackFrequencyService {

	@Autowired
	private IBaseService<FeedbackFrequency> baseService;
	
	@Override
	public List<FeedbackFrequency> getListPage(Parameter param,
			Page<FeedbackFrequency> page) throws Exception {
		return this.baseService.findListPage("FeedbackFrequency", param, null, page, false);
	}

	@Override
	public List<FeedbackFrequency> getAllList() throws Exception {
		String hql = "from FeedbackFrequency where isDelete = 0 order by createDate desc";
		return this.baseService.find(hql);
	}

	@Override
	public FeedbackFrequency findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackFrequency.class, id);
	}

	@Override
	public Serializable doAdd(FeedbackFrequency feedbackFrequency)
			throws Exception {
		return this.baseService.add(feedbackFrequency);
	}

	@Override
	public void doUpdate(FeedbackFrequency feedbackFrequency) throws Exception {
		this.baseService.update(feedbackFrequency);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update FeedbackFrequency set isDelete = 1 where id = " + id;
		this.baseService.executeHql(hql);
	}

}
