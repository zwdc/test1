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
	
	function submitForm() {
		$('#feedback_form').form({
		 	url: ctx+"/feedback/saveOrUpdate",
	        onSubmit: function () {
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
	            	message_dialog.dialog('destroy');
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
<form id="feedback_form" method="post">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
	<input type="hidden" name="taskInfo.id" value="${feedback.taskInfo.id }">
	<input type="hidden" name="createUserId" value="${feedback.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${feedback.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${feedback.isDelete }">
    <input type="hidden" name="status" value="${feedback.status }">
    <input type="hidden" name="fileName" id="fileName" value = "${feedback.fileName }"> <!-- id="fileName"不能删 -->
	<input type="hidden" name="filePath" value = "${feedback.filePath }"> 
	<input type="hidden" name="uploadDate" value = "<fmt:formatDate value='${feedback.uploadDate }' type='both'/>">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="6" align="center">反馈信息</td>
		</tr>
		<tr class="active">
		  	<td colspan="8">
		  		<span class="glyphicon glyphicon-link" aria-hidden="true"></span>&nbsp;督查任务信息:
		  	</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">事项标题:</td>
	  		<td colspan="3">${feedback.taskInfo.title }</td>
	  		<td class="text-right">文号:</td>
	  		<td>${feedback.taskInfo.taskNo }</td>
	  	</tr>
	  	<tr class="active">
		  	<td colspan="8">
		  		<span class="glyphicon glyphicon-link" aria-hidden="true"></span>&nbsp;反馈信息:
		  	</td>
	  	</tr>
		<tr>
			<td class="text-right">原始起草人:</td>
			<td><input name="originalPerson" class="easyui-textbox" data-options="prompt:'填写原始起草人'" value="${feedback.originalPerson }" required="required" type="text" style="width: 50%"></td>
			<td class="text-right">手机:</td>
			<td><input name="phone" class="easyui-textbox" data-options="prompt:'填写手机号'"  value="${feedback.phone }" required="required" type="text"></td>
			<td class="text-right">办公电话:</td>
			<td><input name="workPhone" class="easyui-textbox" data-options="prompt:'填写办公电话'"  value="${feedback.workPhone }" required="required" type="text"></td>
		</tr>
		<tr>
			<td class="text-right">处/科室:</td>
			<td><input name="offices" class="easyui-textbox" data-options="prompt:'填写处/科室'"  value="${feedback.offices }" required="required" type="text"></td>
			<td class="text-right">职务:</td>
			<td><input name="dutyOf" class="easyui-textbox" data-options="prompt:'填写职务'"  value="${feedback.dutyOf }" required="required" type="text"></td>
			<td class="text-right">邮箱:</td>
			<td><input name="email" class="easyui-textbox" data-options="prompt:'填写邮箱'"  value="${feedback.email }" required="required" type="text"></td>
		</tr>
		<tr>
			<td class="text-right">联络人:</td>
			<td><input name="contacts" class="easyui-textbox" data-options="prompt:'填写联络人'" value="${feedback.contacts }" required="required" type="text"></td>
			<td class="text-right">联系电话:</td>
			<td><input name="contactsPhone" class="easyui-textbox" data-options="prompt:'填写联系电话'" value="${feedback.contactsPhone }" required="required" type="text"></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td colspan="6">落实情况:<textarea class="easyui-kindeditor" name="content" rows="8">${feedback.content }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">附件:</td>
			<td colspan="5"><input class="easyui-filebox" type="text" id="file" name="file" data-options="prompt:'请选择文件...'" style="width: 90%;height: 25px;" required="required"></td>
		</tr>
	</table>
</form>
</div>