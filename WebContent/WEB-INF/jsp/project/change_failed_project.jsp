<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){
		$("#assistantGroup").kindeditor({readonlyMode: true});
		$("#remark").kindeditor({readonlyMode: true});
		$("#refuseReason").kindeditor({readonlyMode: true});
	})
</script>
<div class="easyui-layout">
<form id="form" action="${ctx }/project/completeApprovalFailed" method="post">
	<input id="taskId" name="taskId" type="hidden">
    <table class="table table-bordered table-hover table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务信息</td>
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
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',fitColumns:true,rownumbers:true,border:true">
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
			<td colspan="4">责任单位:<textarea class="easyui-kindeditor" id="assistantGroup" rows="3" >${taskInfo.assistantGroup }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">备注:<textarea class="easyui-kindeditor" id="remark" rows="3" >${taskInfo.remark }</textarea></td>
		</tr>
		<tr class="bg-primary">
			<td colspan="4" align="center">拒签交办表信息</td>
		</tr>
		<tr>
			<td class="text-right">拒签单位:</td>
			<td>
				${project.group.name }
			</td>
			<td class="text-right">拒签人:</td>
			<td>${project.refuseUser.name }</td>
		</tr>
		<tr>
			<td colspan="4">拒签原因:<textarea class="easyui-kindeditor" id="refuseReason" rows="3" >${project.refuseReason }</textarea></td>
		</tr>
		<c:if test="${!empty commentsList }">
	  	<tr>
	  		<td colspan="4">
				<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>&nbsp;
	  			<a data-toggle="collapse" href="#comments" aria-expanded="false" aria-controls="comments" title="点击查看审批意见">意见列表:</a>
	  			<div class="collapse" id="comments">
		  			<c:forEach var="comments" items="${commentsList }">
			  			<blockquote>
							<p class="text-primary">${comments.userName } : ${comments.content }</p>
	  						<footer><fmt:formatDate value="${comments.time }" type="both"/></footer>
					    </blockquote> 
					</c:forEach>
				</div>
	  		</td>
	  	</tr>
	  	</c:if>
  	</table>
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(true);" data-options="iconCls:'icon-ok'">确认</a>
</div>
</div>
