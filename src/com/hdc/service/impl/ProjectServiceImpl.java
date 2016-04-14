package com.hdc.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.dao.IJdbcDao;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IProjectService;
import com.hdc.util.UserUtil;

@Service
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private IBaseService<Project> baseService;
	
	@Autowired
	private IJdbcDao jdbcDao;
	
	@Override
	public Project findById(Integer id) throws Exception {
		return this.baseService.getBean(Project.class, id);
	}

	@Override
	public Serializable doAdd(Project project) throws Exception {
		return this.baseService.add(project);
	}

	@Override
	public void doUpdate(Project project) throws Exception {
		this.baseService.update(project);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update Project set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public List<Project> findByTaskInfo(Integer taskInfoId) throws Exception {
		String hql = "from Project where taskInfo.id = " + taskInfoId.toString();
		return this.baseService.find(hql);
	}

	@Override
	public List<Project> getListPage(Parameter param, Page<Project> page,
			Map<String, Object> map) throws Exception {
		return this.baseService.findListPage("Project", param, map, page, true);
	}

	@Override
	public List<Map<String, Object>> getHProject(Parameter param, Page<Map<String, Object>> page)
			throws Exception {
		StringBuffer sb = new StringBuffer();  
		sb.append("select * from project a where is_delete = 0 and (status = 'IN_HANDLING' or status = 'APPLY_FINISHED')");
		sb.append(this.baseService.getDataSetPermission());
		//模糊查询
		if(param != null && StringUtils.isNotBlank(param.getSearchValue())){
        	//如果查询的字段中有日期
        	if(param.getSearchName().toLowerCase().indexOf("date") >= 0){
        		sb.append(" and to_char(a." + param.getSearchName() + ", 'yyyy-MM-dd') like '%" + param.getSearchValue() + "%' ");
        	}else{
        		sb.append(" and a." + param.getSearchName() + " like '%" + param.getSearchValue() + "%' ");
        	}
        }
		
		//高级查询
        if(StringUtils.isNotBlank(param.getSearchColumnNames())){
	        String[] searchColumnNameArray=param.getSearchColumnNames().split(",");
			String[] searchAndsArray=param.getSearchAnds().split(",");
			String[] searchConditionsArray=param.getSearchConditions().split(",");
			String[] searchValsArray=param.getSearchVals().split(",");
			StringBuffer gradeSearch = new StringBuffer();  
			
			if(searchColumnNameArray.length >0 ){
				for (int i = 0; i < searchColumnNameArray.length; i++) {
					if (StringUtils.isNotBlank(searchColumnNameArray[i])) {
						String value=searchValsArray[i].trim().replaceAll("\'", "");
						if ("like".equals(searchConditionsArray[i].trim())){
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								gradeSearch.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}else{
								gradeSearch.append(" " + searchAndsArray[i].trim() + " a." + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}
						}else {
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								gradeSearch.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}else{
								gradeSearch.append(" " + searchAndsArray[i].trim() + " a." + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}
						}
					}
				}
				String gs = gradeSearch.toString();
				sb.append(" and (" + gs.substring(StringUtils.indexOfIgnoreCase(gs, " ",1), gs.length()) + " )");
			
			}
        }
        
        if(param != null && StringUtils.isNotBlank(param.getSort())) {
			sb.append(" order by a." + param.getSort() + " " + param.getOrder());
		} else {
			sb.append(" order by a.create_date desc");
		}
        
        this.jdbcDao.find(sb.toString(), null, page);
        
		return null;
	}

	@Override
	public synchronized boolean doClaimProject(String projectId) throws Exception {
		User user = UserUtil.getUserFromSession();
		String hql1 = "from Project where isDelete = 0 and id = "+projectId+" and user.id is null";
		Integer count = this.baseService.executeHql(hql1);
		if(count > 0) {
			String hql2 = "update Project set user.id = " + user.getId() + " where id = " + projectId;
			this.baseService.executeHql(hql2);
			return true;
		} else {
			return false;
		}
	}

}
