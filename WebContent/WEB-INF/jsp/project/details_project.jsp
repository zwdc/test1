<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){
		$("#assistantGroup").kindeditor({readonlyMode: true});
		$("#remark").kindeditor({readonlyMode: true});
		$("#suggestion").kindeditor({readonlyMode: true});
	})
</script>
<div class="easyui-layout">
    <table class="table table-bordered table-hover table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务交办信息</td>
		</tr>
		<tr>
			<td class="text-right">任务标题:</td>
			<td colspan="3">${taskInfo.title }</td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td>${taskInfo.title }</td>
			<td class="text-right">急缓程度:</td>
			<td>${zwdc:getUrgencyType(taskInfo.urgency) }</td>

		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td>${taskInfo.taskSource.name }</td>
			<td class="text-right">反馈频度:</td>
			<td>${taskInfo.fbFrequency.name }</td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><fmt:formatDate value="${taskInfo.createTaskDate }" type="both"/></td>
			<td class="text-right">办结时限:</td>
			<td><fmt:formatDate value="${taskInfo.endTaskDate }" type="both"/></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><fmt:formatDate value="${taskInfo.claimLimitDate }" type="both"/></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="text-right">承办单位:</td>
			<td>${project.group.name }</td>
			<td class="text-right">承办人:</td>
			<td>${project.user.name }</td>
		</tr>
		<tr>
			<td colspan="4">责任单位:<textarea class="easyui-kindeditor" id="assistantGroup" rows="3" >${taskInfo.assistantGroup }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">备注:<textarea class="easyui-kindeditor" id="remark" rows="3" >${taskInfo.remark }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">拟办意见:<textarea class="easyui-kindeditor" id="suggestion" rows="3" >${project.suggestion }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">阶段性计划:</td>
			<td colspan="3">
				<table id="workPlanDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/feedback/getFeedbackByProject?projectId=${project.id }',fitColumns:true,rownumbers:true,border:true">
				    <thead>
						<tr>
							<th data-options="field:'id',hidden:true">ID</th>
							<th data-options="field:'workPlanDate'" width="40%">阶段日期</th>
							<th data-options="field:'workPlan',editor:'text'" width="50%">阶段计划</th>
						</tr>
				    </thead>
				</table>
			</td>
		</tr>
  	</table>
</div>
