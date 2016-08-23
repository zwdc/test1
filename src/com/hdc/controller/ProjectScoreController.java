package com.hdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProjectScore;
import com.hdc.service.IProjectScoreService;

/**
 * 项目评分表
 * @author ZML
 *
 */
@Controller
@RequestMapping("/projectScore")
public class ProjectScoreController {

	@Autowired
	private IProjectScoreService projectScoreService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "projectSource/list_projectScore";
	}
	
	/**
	 * 获取列表分页数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<ProjectScore> getList(Parameter param) throws Exception {
		Page<ProjectScore> page = new Page<ProjectScore>(param.getPage(), param.getRows());
		List<ProjectScore> list = this.projectScoreService.getListPage(param, page);
		return new Datagrid<ProjectScore>(page.getTotal(), list);
	}
	
}
