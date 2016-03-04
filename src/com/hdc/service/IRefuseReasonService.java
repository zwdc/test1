package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.RefuseReason;

/**
 * 拒绝原因service
 * @author zhao
 *
 */
public interface IRefuseReasonService {

	/**
	 * 添加
	 * @param refuseReason
	 * @return
	 * @throws Exception
	 */
	public Serializable doAdd(RefuseReason refuseReason) throws Exception;
	
	/**
	 * 物理删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 通过taskInfoId查找拒绝原因
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	public List<RefuseReason> findByTaskId(Integer taskInfoId) throws Exception;
}
