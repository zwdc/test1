package com.hdc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProjectScore;
import com.hdc.entity.TaskSource;
import com.hdc.service.IProjectScoreService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.Constants;
import com.hdc.util.FileDownloadUtils;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;

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
