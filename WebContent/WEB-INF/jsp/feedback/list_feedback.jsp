<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
	<title>反馈记录管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/feedback.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div id="feedbacktoolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="feedback();">承办反馈</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="check();">反馈审核</a>
					<!-- <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addFeedback();">添加反馈</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editFeedback();">编辑</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delFeedback();">删除</a> -->
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
				</td>
				<td style="padding-left:5px">
					<input id="searchbox" type="text"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'name'">任务名称</div>
		<div data-options="name:'createDate'">反馈时间</div>
	</div>
	
	<table id="feedback_datagrid" title="反馈记录管理"></table>
  </body>
</html>