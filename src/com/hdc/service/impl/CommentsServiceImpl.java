package com.hdc.service.impl;

import java.io.Serializable;

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
	public Serializable doAdd(Comments comments) throws Exception {
		return this.baseService.add(comments);
	}

}
