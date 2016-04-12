package com.hdc.service;

import java.io.Serializable;

import com.hdc.entity.Comments;

public interface ICommentsService {

	public Serializable doAdd(Comments comments) throws Exception;
}
