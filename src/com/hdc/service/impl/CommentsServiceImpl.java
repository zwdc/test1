package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Comments;
import com.hdc.service.IBaseService;
import com.hdc.service.ICommentsService;

@Service
public class CommentsServiceImpl implements ICommentsService {

	@Autowired
	private IBaseService<Comments> baseService;
	
	@Override
	public List<Comments> getList(Comments comments) throws Exception {
		String hql = "from Comments where businessKey=:businessKey and businessType=:businessType and businessForm=:businessForm order by time desc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("businessKey", comments.getBusinessKey());
		paramMap.put("businessType", comments.getBusinessType());
		paramMap.put("businessForm", comments.getBusinessForm());
		return this.baseService.find(hql, paramMap);
	}

	@Override
	public Serializable doAdd(Comments comments) throws Exception {
		return this.baseService.add(comments);
	}

}
