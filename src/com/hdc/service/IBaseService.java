package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;

public interface IBaseService<T> {
	
	public Serializable add(T bean) throws Exception;
	 
	public void saveOrUpdate(T bean) throws Exception;
	
	public void delete(T bean) throws Exception;
	
	public void update(T bean) throws Exception;
	 
	public T getBean(final Class<T> obj,final Serializable id) throws Exception;
	 
	/**
	 * 条件查询分页
	 * @param tableSimpleName	 实体类名字
	 * @param param				 分页、模糊查询、高级查询条件
	 * @param map				 用户自定义查询条件
	 * @param page				 分页
	 * @param dataSetPermission 查询时是否需要数据集权限
	 * @return
	 * @throws Exception
	 */
	public List<T> findListPage(String tableSimpleName, Parameter param, Map<String, Object> params, Page<T> page, Boolean dataSetPermission) throws Exception;
	
	/**
	 * 获取数量
	 * @param tableSimpleName	 实体类名字
	 * @param param				 分页、模糊查询、高级查询条件
	 * @param map				 用户自定义查询条件
	 * @param dataSetPermission 查询时是否需要数据集权限
	 * @return
	 * @throws Exception
	 */
	public Integer getCount(String tableSimpleName, Parameter param, Map<String, Object> params, Boolean dataSetPermission) throws Exception;
	
	/**
	 * 条件查询
	 * @param tableSimpleName	 实体类名字
	 * @param param				 查询条件
	 * @param dataSetPermission 查询时是否需要数据集权限(true/false)
	 * @return
	 * @throws Exception
	 */
	public List<T> findByWhere(String tableSimpleName, Map<String, Object> params, Boolean dataSetPermission) throws Exception;

	/**
	 * 获取唯一数据
	 * @param tableSimpleName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public T findUnique(String tableSimpleName, Map<String, Object> params) throws Exception;
	
	/**
	 * 批量执行HQL 响应数目
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public Integer executeHql(final String hql) throws Exception;
	
	/**
	 * 批量执行HQL (更新) 响应数目
	 * @param hql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer executeHql(String hql, Map<String, Object> params) throws Exception;
	
	/**
	 * 查询
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public List<T> find(final String hql) throws Exception;
	
	/**
	 * 按条件查询hql
	 * @param hql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<T> find(String hql, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取数据权限
	 * @return
	 * @throws Exception
	 */
	public StringBuffer getDataSetPermission() throws Exception;
	
}
