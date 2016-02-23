package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Company;
import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.ICompanyService;
import com.hdc.util.BeanUtils;

/**
 * 公司控制类
 * @author ZML
 *
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private ICompanyService companyService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList() {
		return "company/list_company";
	}
	
	/**
	 * 加载列表数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCompany")
	@ResponseBody
	public Datagrid<Company> listCompany(Parameter param) throws Exception {
		Page<Company> page = new Page<Company>(param.getPage(), param.getRows());
		this.companyService.getListPage(param, page);
		Datagrid<Company> dataGrid = new Datagrid<Company>(page.getTotal(), page.getResult());
		return dataGrid;
	}
	
	/**
	 * 保存或更新
	 * @param company
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(Company company) throws Exception {
		Message message = new Message();
		try {
			this.companyService.saveOrUpdate(company);
			message.setStatus(Boolean.TRUE);
			message.setMessage("操作成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 添加页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(){
		return "company/main_company";
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Message delete(@RequestParam("id") Integer id) throws Exception{
		Message message = new Message();
		if(!BeanUtils.isBlank(id)){
			Company company = this.companyService.getCompanyById(id);
			company.setIsDelete(1);
			this.companyService.doUpdate(company);
			message.setStatus(Boolean.TRUE);
			message.setMessage("删除成功！");
		} else {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败，您所删除的内容不存在！");
		}
		return message;
	}
	
	/**
	 * 返回公司下拉列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/companyList")
	@ResponseBody
	public List<Company> companyName() throws Exception{
		List<Company> list=this.companyService.getList();
		return list;
	}
	
	/**
	 * 跳转到详情页面
	 * @return
	 */
	@RequestMapping(value = "/toDetails/{id}")
	public ModelAndView toDetails(@PathVariable("id") Integer id) throws Exception{
		ModelAndView mv = new ModelAndView("company/details_company");
		Company company=this.companyService.getCompanyById(id);
		mv.addObject("company",company);
		return mv;
	}
	
	/**
	 * 跳转到查询页面
	 * @return
	 */
	@RequestMapping(value = "/companySearch")
	public String companySearch(){
		return "company/search";
	}
}
