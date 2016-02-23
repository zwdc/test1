<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
  <head>
    <title>部门列表管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/group.js"></script>
  </head>
  <body>
  <div class="easyui-layout" data-options="fit:true">
  <div data-options="region:'north',border:false" title="" style="overflow: hidden; padding: 5px;">
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>部门</strong></span>进行管理!<br/><br/>
		</p>
	</div>
  </div>
  <div data-options="region:'center',border:true">
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showGroup();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details();">详情</a>
					</shiro:hasRole>
				</td>
				<td style="padding-left:5px">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:5px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="gradeSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'name'">部门名称</div>
		<div data-options="name:'type'">部门类型</div>
	</div>
	<table id="group_datagrid" title="部门管理"></table>
  </div>
  </div>
  </body>
</html>
