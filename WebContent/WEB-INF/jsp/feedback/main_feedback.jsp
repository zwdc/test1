<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
<script type="text/javascript">
</script>
<div class="easyui-layout">
<form id="feedback_form" method="post" encType="multipart/form-data">
    <input type="hidden" id="feedback" name="project.id" value="1">
	<table class="table table-bordered" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">添加反馈信息</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" name="createUser.id" class="easyui-textbox"
				value="${user.id}" data-option="prompt:'起草人'"
				required="required" ></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" name="project.group.id"
				class="easyui-textbox" value="1"
				data-option="prompt:'牵头部门'"  required="required" >
			</td>
			<td class="text-right">反馈时限:</td>
			<td><input name="feedbackEndDate" class="easyui-datetimebox"
				data-options="prompt:'反馈时限'"
				value="<fmt:formatDate value='${feedback.feedbackEndDate}' type='both'/>" required="required" ></td>	
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" name="status" class="easyui-textbox"
				value="" data-option="prompt:'记录状态'"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" name="delayCount" class="easyui-textbox"
				value="0" data-option="prompt:'来源名称'"
				 required="required" ></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input name="feedbackStartDate" class="easyui-datetimebox"
				data-options="prompt:'反馈开始时间'"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate}' type='both'/>" required="required" ></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					data-options="readonlyMode: false,prompt:'阶段工作计划'" name="workPlan" rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>   	
	</table>    
</form>
</div>