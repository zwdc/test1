<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
  <head>
    <title>资源管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/resource.js"></script>
  </head>
  <body>
  	<div class="easyui-layout" data-options="fit:true">
	  	<div data-options="region:'north',border:false" title="" style="overflow: hidden; padding: 5px;">
			<div class="well well-small">
				<span class="badge">提示</span>
				<p>
					在此你可以对<span class="label-info"><strong>菜单功能</strong></span>进行编辑!  &nbsp;<span class="label-info"><strong>注意</strong></span>权限字符串不可重复！
					在任意菜单上点击<span class="label-info"><strong>右键</strong></span>，可显示相应操作。<br/><br/>
					新添加的<span class="label-info"><strong>菜单</strong></span>型资源，要在<span class="label-info"><strong>角色权限管理</strong></span>中分配相应权限才能显示到菜单栏中。
				</p>
			</div>
		</div>
		<div data-options="region:'center',border:true">
			<div id="toolbar" style="padding:2px 0">
				<table>
					<tr>
						<td style="padding-left:2px;">
							<shiro:hasRole name="admin">
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showResource();">添加</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="openResource();">编辑</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delRows();">删除</a>|
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="expandAll();">展开</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="collapseAll();">收缩</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="refresh();">刷新</a>
							</shiro:hasRole>
						</td>
					</tr>
				</table>
			</div>
			
			<%-- 列表右键 --%>
			<div id="resource_treegrid_menu" class="easyui-menu" style="width:120px;display: none;">
			    <div onclick="showResource();" data-options="iconCls:'icon-add'">新增</div>
			    <div onclick="openResource();" data-options="iconCls:'icon-edit'">编辑</div>
			    <div onclick="delRows();" data-options="iconCls:'icon-remove'">删除</div>
			    <div onclick="lock(false);" data-options="iconCls:'icon-ok'">启用</div>
			    <div onclick="lock(true);" data-options="iconCls:'icon-lock'">停用</div>
			</div>
			
			<table id="resource" title="权限"></table>
		</div>
	</div>
  </body>
</html>
