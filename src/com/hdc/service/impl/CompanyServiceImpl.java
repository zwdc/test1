package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.Company;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IBaseService;
import com.hdc.service.ICompanyService;

@Service
public class CompanyServiceImpl implements ICompanyService{

	@Autowired 
	private IBaseService<Company> baseService;

	@Override
	public List<Company> getListPage(Parameter param, Page<Company> page) throws Exception {
		List<Company> list = this.baseService.findListPage("Company", param, null, page, false);
		return list;
	}

	@Override
	public Company getCompanyById(Integer id) throws Exception {
		return this.baseService.getBean(Company.class, id);
	}
	
	@Override
	public Serializable doAdd(Company company) throws Exception {
		return this.baseService.add(company);
	}

	@Override
	public void doUpdate(Company company) throws Exception {
		this.baseService.update(company);
	}

	@Override
	public void saveOrUpdate(Company company) throws Exception {
		this.baseService.saveOrUpdate(company);
	}

	@Override
	public void doDelete(Company company) throws Exception {
		this.baseService.delete(company);
	}

	@Override
	public List<Company> getList() throws Exception {
		return this.baseService.findByWhere("Company", null, false);
	}

}
