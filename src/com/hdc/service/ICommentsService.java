package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Comments;

public interface ICommentsService {

	public List<Comments> getList(Comments comments) throws Exception;
	
	public Serializable doAdd(Comments comments) throws Exception;
}
