<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
<script type="text/javascript" src="${ctx}/js/app/feedback.js?_=${sysInitTime}"></script>
<script type="text/javascript">
	$(function(){
		$("#assistantGroup").kindeditor({readonlyMode: true});
		$("#remark").kindeditor({readonlyMode: true});
	})
</script>
<div class="easyui-layout">
    <table id="sales" class="table table-bordered table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务信息</td>
		</tr>
		<tr>
			<td class="text-right">任务内容:</td>
			<td colspan="3"><textarea class="easyui-kindeditor" 
					data-options="readonlyMode:true"  rows="2">${taskInfo.title }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.title }"
				data-option="prompt:'牵头部门'"  disabled="disabled" ></td>
			<td class="text-right">急缓程度:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${zwdc:getUrgencyType(taskInfo.urgency) }"
				data-option="prompt:'急缓程度'"  disabled="disabled" ></td>

		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.taskSource.name }"
				data-option="prompt:'任务来源'"  disabled="disabled" ></td>
			<td class="text-right">反馈频度:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.fbFrequency.name }"
				data-option="prompt:'反馈频度'"  disabled="disabled" ></td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.createTaskDate }' type='both'/>"></td>
			<td class="text-right">办结时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.endTaskDate }' type='both'/>"></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.claimLimitDate }' type='both'/>"></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',fitColumns:true,rownumbers:true,border:true,singleSelect:true">
				    <thead>
						<tr>
							<th data-options="field:'groupName'" width="25%">牵头单位名称</th>
							<th data-options="field:'userNames0'" width="15%">联系人A</th>
							<th data-options="field:'linkway0'" width="20%">联系方式</th>
							<th data-options="field:'userNames1'" width="15%">联系人B</th>
							<th data-options="field:'linkway1'" width="20%">联系方式</th>
						</tr>
				    </thead>
				</table>
			</td>
		</tr>
		<tr>
			<td class="text-right">责任单位:</td>
			<td colspan="3"><textarea class="easyui-kindeditor" id="assistantGroup" rows="1" >${taskInfo.assistantGroup }</textarea></td>
		</tr>
		 <tr class="bg-primary">
			<td colspan="4" align="center">反馈列表</td>
		</tr>
  	</table>
	<table>   
		<tr>
		<td style="padding-left:2px">
			<shiro:hasRole name="admin">
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addFeedback();">添加反馈</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="feedback();">承办反馈</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="check();">反馈审核</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editFeedback();">编辑</a>
		    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delFeedback();">删除</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
			</shiro:hasRole>
		</td>
		<td style="padding-left:5px">
			<input id="searchbox" type="text"/>
		</td>
		</tr>
	</table>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'name'">任务名称</div>
		<div data-options="name:'createDate'">反馈时间</div>
	</div>
	<table id="feedback_datagrid" title=" "></table>
</div>