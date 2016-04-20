<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>

<script type="text/javascript">
	$(function() {
		$('#download').tooltip({
			position: 'right',
			content: '<span style="color:#fff">点击下载</span>',
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
			}
		});	 
	});

</script>
<div class="easyui-layout">
<form id="feedback_form" method="post" action="${ctx }/feedback/approval" encType="multipart/form-data">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
	<input id="taskId" name="taskId" type="hidden">
	<table class="table table-bordered" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">反馈信息审核</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" class="easyui-textbox"
				value="${user.name}" data-option="prompt:'起草人'" disabled="disabled"></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" class="easyui-textbox" 
			    value="${feedback.project.group.name }"
				data-option="prompt:'牵头部门'"  disabled="disabled" >
			</td>
			<td class="text-right">反馈时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${feedback.feedbackEndDate }' type='both'/>"></td>	
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" class="easyui-textbox"
				value="${(feedback.status eq 'FEEDBACKING' ) ? '反馈中' : ((feedback.status eq 'RETURNED') ? '已退回' : '已采用') }" data-option="prompt:'来源名称'"
				 disabled="disabled"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" class="easyui-textbox"
				value="${feedback.delayCount>0?'延期':'未延期' }" data-option="prompt:'来源名称'"
				 disabled="disabled" ></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input class="easyui-datetimebox"
				data-options="prompt:'反馈开始时间'" disabled="disabled"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate}' type='both'/>"></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor" 
					data-options="readonlyMode:true"  rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" 	 rows="3">${feedback.situation }</textarea></td>
		</tr>
		
		<tr>
			<td class="text-right">存在问题/困难:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" 	rows="3">${feedback.problems }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">解决措施/建议:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" rows="3">${feedback.solutions }</textarea></td>
		</tr>
        <tr>
		  		<td class="text-right">附件材料：</td>
		   		<td colspan="5" style="8px">
		   		     <c:forEach items="${feedback.fdaList}" var="fda"> 
					 <div class="layout_default">
    					<p class="info"><span class="date"></span> <span class="author"></span></p>
    					<div class="image_container"> <a href="${ctx }${fda.url}" rel="lightbox[ostec]"> 
    						<img src="${ctx }${fda.url}"> </a> </div>
  					</div>
					 </c:forEach> 

		   		</td>
		   	</tr>	   
	  	<tr>
	  		<td colspan="6">
	  			<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;审批意见:
	  			<textarea class="easyui-kindeditor" name="comment" rows="3"></textarea>
	  		</td>
	  	</tr>
    	
	</table>
    
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(true);" data-options="iconCls:'icon-ok'">同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(false);" data-options="iconCls:'icon-remove'">不同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="closeDialog();" data-options="iconCls:'icon-cancel'">关闭</a>
</div>
</div>