<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <title>任务列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/taskInfo.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
				    <shiro:hasRole name="SUPERVISE">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showTaskInfo();">创建任务</a>
                    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="multiInsert();">批量创建任务</a>
					</shiro:hasRole>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details();">详情</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="approvalPrcess();">查看审批流程</a>
				</td>
				<td style="padding-left:5px;">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:5px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-large-chart',plain:true" onclick="invoiceSearch();">本年度完成情况统计</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="gradeSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMenu" style="display:none;">
		<div data-options="name:'title'">任务内容</div>
	</div>
	<table id="taskInfo_datagrid" title="任务列表"></table>
  </body>
</html>
