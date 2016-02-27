<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>任务列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/bootstrap-table.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/bootstrap-table-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/taskInfo.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>开票申请</strong></span>进行管理!<br/><br/>
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showTaskInfo();">添加</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">编辑</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsInvoice();">详情</a>
				</td>
				<td style="padding-left:5px">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:5px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="invoiceSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'invoiceType'">发票类型</div>
		<div data-options="name:'receiveInvoiceName'">收票公司名称</div>
		<div data-options="name:'currentPrice'">本次开票金额</div>
	</div>
	<table id="taskInfo_datagrid" title="任务"></table>
  </body>
</html>
