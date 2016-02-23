package com.hdc.service;

import java.io.Serializable;
import java.util.List;

import com.hdc.entity.Company;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;

public interface ICompanyService {

	public List<Company> getListPage(Parameter param, Page<Company> page) throws Exception;
	
	public List<Company> getList() throws Exception;
	
	public Company getCompanyById(Integer id) throws Exception;
	
	public Serializable doAdd(Company company) throws Exception;
	
	public void doUpdate(Company company) throws Exception;
	
	public void saveOrUpdate(Company company) throws Exception;
	
	public void doDelete(Company company) throws Exception;
}
