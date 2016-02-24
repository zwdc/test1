<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<script type="text/javascript" src="${ctx}/js/app/model.js"></script>
	<title>流程设计模型</title>
</head>

<body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>流程模型</strong></span>进行编辑!&nbsp; 新的流程模型需要进行<span class="label-info"><strong>部署</strong></span>，才能在系统中使用！
		</p>
	</div>
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="add();">创建</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="deploy();">部署</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>|
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="exportmodel();">导出</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
					</shiro:hasRole>
				</td>
			</tr>
		</table>
	</div>
	
	<table id="model_datagrid" title="用户管理"></table>
</body>

</html>
