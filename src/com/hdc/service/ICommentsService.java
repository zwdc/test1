package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Comments;

public interface ICommentsService {

	public Serializable doAdd(Comments comments) throws Exception;
	
	public List<Comments> findComments(Integer businessKey, String businessForm) throws Exception;
}
