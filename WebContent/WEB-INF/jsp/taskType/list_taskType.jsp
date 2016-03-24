<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
	<title>项目类型管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/taskType.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="show();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="expandAll();">展开</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="collapseAll();">收缩</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="refresh();">刷新</a>
					</shiro:hasRole>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="treegrid_menu" class="easyui-menu" style="width:120px;display: none;">
	    <div onclick="showResource();" data-options="iconCls:'icon-add'">新增</div>
	    <div onclick="openResource();" data-options="iconCls:'icon-edit'">编辑</div>
	    <div onclick="delRows();" data-options="iconCls:'icon-remove'">删除</div>
	</div>
	
	<table id="taskType_treegrid" title="类型管理"></table>
  </body>
</html>