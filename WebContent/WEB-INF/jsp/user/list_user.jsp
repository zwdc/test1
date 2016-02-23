<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <title>用户管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="${ctx}/css/jquery.qtip.min.css" type="text/css" />
  	<script type="text/javascript" src="${ctx}/js/trace/jquery.qtip.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/user.js"></script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>用户</strong></span>进行管理!<br/>
			其中<span class="label-info"><strong>同步用户</strong></span>功能是将用户表的数据同步到工作流(Activiti)中的用户表中，具体
			工作流中的用户表包括： <span class="label-info"><strong>act_id_group</strong></span>、<span class="label-info"><strong>act_id_membership</strong></span>、
			<span class="label-info"><strong>act_id_user</strong></span>。<br/>
			同步用户功能并不影响系统的正常使用。<span class="label-info"><strong>初始化密码</strong></span>可以把当前用户的密码初始化为系统默认密码“123”。
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showUser();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>|
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="initPassword();">初始化密码</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="sync();">同步用户</a>
					</shiro:hasRole>
				</td>
				<td style="padding-left:5px">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="gradeSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'name'">用户名</div>
		<div data-options="name:'id'">用户编号</div>
		<div data-options="name:'company.name'">所属公司</div>
		<div data-options="name:'group.name'">所属部门</div>
		<div data-options="name:'role.name'">所属角色</div>
		<div data-options="name:'registerDate'">注册日期</div>
	</div>
	<table id="user_datagrid" title="用户管理"></table>
  </body>
</html>
