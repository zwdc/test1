<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
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
	
	function submitForm(obj, taskId) {
		$('#feedback_form').form('submit', {
		 	url: ctx+"/feedback/saveOrUpdate",
	        onSubmit: function () {
	        	if(taskId != null) {
		        	param.taskId = taskId;
	        	}
		        $.messager.progress({
		            title: '提示信息！',
		            text: '数据处理中，请稍后....'
		        });
		        var isValid = $(this).form('validate');
		        if (!isValid) {
		            $.messager.progress('close');
		        }
		        return isValid;
		    },
		    success: function (data) {
	            $.messager.progress('close');
	            var json = $.parseJSON(data);
	            if (json.status) {
	            	obj.dialog('destroy');
	            } 
	            $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
	            
	        }
	    });
	}
</script>
<div class="easyui-layout">
<form id="feedback_form" method="post" encType="multipart/form-data">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
	<input type="hidden" name="createUser.id" value="${feedback.createUser.id }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${feedback.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${feedback.isDelete }">
    <input type="hidden" name="status" value="${feedback.status }">
	<table class="table table-bordered table-hover" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">反馈信息</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" name="originalPerson" class="easyui-textbox"
				value="${user.name }" data-option="prompt:'起草人'"
				required="required" readonly="readonly" ></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" name="ManagerName"
				class="easyui-textbox" value="${feedback.project.group.name }"
				data-option="prompt:'牵头部门'"  required="required" readonly="readonly">
			</td>
			<td class="text-right">反馈时限:</td>
			<td><input name="feedbaceEndDateString" class="easyui-textbox"
				data-options="prompt:'反馈时限',editable:false"
				value="<fmt:formatDate value='${feedback.feedbackEndDate }' type='both'/>" required="required" readonly="readonly"></td>
			
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" name="statusString" class="easyui-textbox"
				value="${(feedback.status eq 'FEEDBACKING' ) ? '反馈中' : ((feedback_noTaskId.status eq 'RETURNED') ? '已退回' : '已采用') }" data-option="prompt:'来源名称'"
				 required="required" readonly="readonly"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" name="delayCountString" class="easyui-textbox"
				value="${feedback.delayCount>0?'延期':'未延期' }" data-option="prompt:'来源名称'"
				 required="required" readonly="readonly"></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input name="feedbackStartDateString" class="easyui-textbox"
				data-options="prompt:'反馈开始时间',editable:false"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate }' type='both'/>" required="required" readonly="readonly"></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					data-options="readonlyMode: true" name="workPlanString" rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					name="situation" rows="3">${feedback.situation }</textarea></td>
		</tr>
		
		<tr>
			<td class="text-right">存在问题/困难:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					name="problems" rows="3">${feedback.problems }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">解决措施/建议:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					name="solutions" rows="3">${feedback.solutions }</textarea></td>
		</tr>
		<tr>
	  		<td class="text-right">佐证材料上传:</td>
	  		<td colspan="3">
		    	<input class="easyui-filebox" type="text" id="file" name="file" data-options="prompt:'请选择文件...'" style="width: 65%;height: 25px;">
		    	<c:if test="${feedback.id != null }">
		    		<small><abbr id="title"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></abbr></small>
		    	</c:if>
	  		</td>
	  	</tr>
		<c:if test="${feedback.id != null }">
		  	<tr>
		  		<td class="text-right">佐证材料下载：</td>
		   		<td>
		   			<c:if test="${feedback.fileName != null }">
		   				<a id="download" title="点击下载" href="${ctx }/taskSource/downloadFile?id=${source.id}"><span class="glyphicon glyphicon-download-alt"></span>${source.fileName }</a>
		   			</c:if>
		   		</td>
		   		<td class="text-right">上传时间:</td>
		   		<td>${source.uploadDate }</td>
		   	</tr>
    	</c:if>
	</table>
</form>
</div>