<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
  <head>
    <title>权限编辑</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/role.js"></script>
	</head>
  <body>
   <div id="panel" data-options="border:false">
        <div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" title="" style="padding: 5px 5px 1px;">
			<div class="well well-small">
				<span class="badge">提示</span>
				<p>
					请<span class="label-info"><strong>双击用户组</strong></span>查看当前组所拥有的资源！
					超级管理员默认拥有<span class="label-info"><strong>所有权限！</strong></span>
					更改相应的组权限后，要点击<span class="label-info"><strong>保存设置</strong></span>来保存更改。
				</p>
			</div>
		</div>
		<div data-options="region:'west',split:true,border:true" style="width:500px;">
			<div id="toolbar" style="padding:2px 0">
				<table>
					<tr>
						<td style="padding-left:4px;padding-bottom:4px;">
							<shiro:hasRole name="admin">
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="savePermission();">保存设置</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showRole();">添加</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="operation();">编辑</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delRows();">删除</a>
							</shiro:hasRole>
						</td>
					</tr>
				</table>
			</div>
			<table id="role" title="用户角色"></table>
		</div>
		<div data-options="region:'center',border:true">
			<div id="tb" style="padding:2px 0">
				<table>
					<tr>
						<td style="padding-left:4px;padding-bottom:4px;">
							<shiro:hasRole name="admin">
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="expandAll();">展开</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="collapseAll();">收缩</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="refresh();">刷新</a>
							</shiro:hasRole>
						</td>
					</tr>
				</table>
			</div>
			<table id="resource" title="权限"></table>
		</div>
		</div>
	</div>
  </body>
</html>
