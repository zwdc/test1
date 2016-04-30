<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <title>代办任务</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link href="${ctx }/css/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx }/js/app/task.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx }/js/app/choose/user/user.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx }/js/app/jump.js" type="text/javascript"></script>
	<script src="${ctx }/js/app/trace.js" type="text/javascript"></script>
	<script src="${ctx }/js/trace/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/trace/jquery.qtip.min.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/js/app/choose/project/choose_project.js"></script> --%>
  </head>
  <body>
	 <div class="well well-small" style="margin-left: 5px;margin-top: 5px;">
		<span class="badge easyui-tooltip" title="提示">提示</span>
		<p>
			在此你可以在<span class="label-info"><strong>待办任务</strong></span>中办理待处理的任务，也可以查看<span class="label-info"><strong>已完成的任务</strong></span>列表。<br/>
			功能说明：<br/>
			&nbsp;&nbsp;1、任务的<span class="label-info"><strong>转办</strong></span>可以把当前已经签收的任务转给其他人办理，任务办理完成后流程会继续向下走。<br/>
			&nbsp;&nbsp;2、任务的<span class="label-info"><strong>委派</strong></span>可以把当前已经签收的任务委派其他人办理，任务办理完成后流程会回到原执行人，由原执行人继续办理。<br/>
			&nbsp;&nbsp;3、任务的<span class="label-info"><strong>跳转</strong></span>可以把当前已经签收的任务跳转（包括回退和向前）至指定活动的节点。
		</p>
	 </div>	
	 <div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="handleTask();">办理</a>
					<!-- <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="claimTask();">签收</a> -->
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="transferTask();">转办</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="delegateTask();">委派</a>
					<!-- <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="jumpTask();">跳转</a> -->
				</td>
				<td style="padding-left:2px;width:100px"></td>
				<td style="padding-left:2px" class="text-right">
					<span style="font-size:9px ">项目名称：</span><input id="projectName" style="width:140px;height:20px" type="text"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="font-size:9px ">标题：</span><input id="title" style="width:140px;height:20px" type="text"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="font-size:9px ">表单类型：</span>
							<select id="businessForm" class="easyui-combobox" style="width:120px;" data-options="editable:false,panelHeight:'auto'">
								<option selected value="">请选择</option>
								<!-- <option value="PROJECT">立项表</option>
								<option value="SALES">销售审批表</option>
								<option value="SALES_CONTRACT">销售合同</option>
								<option value="PROCUREMENT_CONTRACT">采购合同</option>
								<option value="OTHER_CONTRACT">其他待审批文件</option>
								<option value="PAYMENT">付款</option>
								<option value="OUTBOUND">出库单</option>
								<option value="INBOUND">入库单</option>
								<option value="PROCUREMENT_TUNHUO">囤货采购审批表</option>
								<option value="PROCUREMENT_BUHUO">补货采购审批表</option>
								<option value="INVOICE">开票</option>-->
							</select>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="selectMap();">搜索</a>
				</td>
			</tr>
		</table>
	 </div>
	 <!-- <div id="tabs" class="easyui-tabs">
		<div title="待办任务" closable="true" data-options="selected:true" style="padding:5 0 0 0;">
			<table id="todoTask" title="待办任务列表"></table>
		</div>
		<div title="已完成的任务" closable="true" style="padding:5 0 0 0;">
			<table id="endTask" title="已完成任务"></table>
		</div>
	</div> -->
	
	<table id="todoTask" title="待办任务列表"></table>
	
	<div id="task" class="easyui-dialog" closed="true">
		<form id="taskForm" style="margin: 10px 10px" method="post">
			<input id="user_id" name="userId" type="hidden">
			<p><input id="user_name" class="easyui-textbox" prompt="选择委派人" type="text" readonly="readonly">
			<a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="chooseUser(false, 'user');">选择委派人</a></p>
		</form>
	</div>
	
	<div id="jumpTask" class="easyui-dialog" closed="true">
		<form id="jumpForm" style="margin: 10px 10px" method="post">
			当前任务节点：<span class="label-info"><strong><label id="currentTaskName"></label></strong></span><br/>
			选择要跳转的节点：<br/>
			<input id="targetTaskDefinitionKey" name="targetTaskDefinitionKey" class="easyui-textbox" style="width:150px" required="required" type="text"/>
		</form>
	</div>
  </body>
</html>
