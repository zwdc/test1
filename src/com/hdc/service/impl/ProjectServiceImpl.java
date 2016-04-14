package com.hdc.service.impl;

import java.io.Serializable;
import java.util.HashMap;
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
	public List<Map<String, Object>> getProjectList(Parameter param, Integer type , Page<Map<String, Object>> page)
			throws Exception {
		StringBuffer sb = new StringBuffer();  
		User user = UserUtil.getUserFromSession();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupId", user.getGroup().getId());
		paramMap.put("userId", user.getId());
		sb.append("select a.id, g.name group_name, u.user_name user_name, t.id task_id, t.title, t.urgency, s.name source_name, t.end_task_date, t.fb_frequency from project a ");
		sb.append("left join groups g on (g.group_id = a.group_id) ");
		sb.append("left join users u on (u.user_id = a.user_id) ");
		sb.append("left join task_info t on (t.id = a.task_info_id) ");
		sb.append("left join task_source s on (s.id = t.id) ");
		
		if(type == 1) {
			//待签收
			sb.append("where a.is_delete = 0 and a.status = 'WAIT_FOR_CLAIM' and a.group_id = :groupId and (a.user_id = :userId or a.user_id is null)");
		} else if(type == 2) {
			//办理中/申请办结
			sb.append("where a.is_delete = 0 and (a.status = 'IN_HANDLING' or a.status = 'APPLY_FINISHED') and a.group_id = :groupId and a.user_id = :userId ");
		} else if(type == 3) {
			//已办结
			sb.append("where a.is_delete = 0 and a.status = 'FINISHED' and a.group_id = :groupId and a.user_id = :userId ");
		}
		//数据权限
		/*if(user.getGroupData() == 1) {
			sb.append("and a.create_user_id.group = " + user.getGroup().getId().toString());
		} else if(user.getSelfData() == 1) {
			sb.append("and a.createUser = " + user.getId().toString());
		}*/
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
        
        return this.jdbcDao.find(sb.toString(), paramMap, page);
	}

	@Override
	public synchronized boolean doClaimProject(String projectId) throws Exception {
		User user = UserUtil.getUserFromSession();
		String hql1 = "from Project where isDelete = 0 and id = "+projectId+" and user.id is null";
		List<Project> list = this.baseService.find(hql1);
		if(list.size() > 0) {
			String hql2 = "update Project set user.id = " + user.getId() + " where id = " + projectId;
			this.baseService.executeHql(hql2);
			return true;
		} else {
			return false;
		}
	}

}
