<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
	$(function() {
		$('#taskInfoType').combobox({
		    url: ctx+'/taskType/getAll',
		    valueField:'id',
		    textField:'name'
		});
		
		$('#title').tooltip({
			position: 'right',
			content: '<span style="color:#fff">重新上传文件将会覆盖以下源文件，无需修改文件时请留空！</span>',
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
			}
		});
		
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
<form id="taskSourceForm" method="post" encType="multipart/form-data">
    <input type="hidden" name="id" value="${source.id }">
    <input type="hidden" name="isDelete" value="${source.isDelete }">
    <input type="hidden" name="createUser.id" value="${source.createUser.id}">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${source.createDate }' type='both'/>">
    <input type="hidden" name="fileName" id="fileName" value = "${source.fileName }"> <!-- id="fileName"不能删 -->
	<input type="hidden" name="filePath" value = "${source.filePath }"> 
    <table class="table table-bordered table-hover">
	  	<tr class="bg-primary">
	  		<td colspan="4" align="center">任务来源</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务来源全称:</td>
	  		<td colspan="3">
		    	<input type="text" name="name" class="easyui-textbox" value="${source.name }" data-option="prompt:'来源名称'" style="width: 70%" required="required">
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务来源简介:</td>
	  		<td colspan="3">
	  			<textarea class="easyui-kindeditor" name="info" rows="3">${source.info }</textarea>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务类型:</td>
	  		<td>
		    	<input type="text" id="taskInfoType" name="taskInfoType.id" value="${source.taskInfoType.id }" class="easyui-combobox" data-option="prompt:'任务类型'" required="required">
	  		</td>
	  		<td class="text-right">来源时间:</td>
	  		<td>
	  			<input name="sourceDate" class="easyui-datetimebox" data-options="prompt:'来源时间',editable:false" value="${source.sourceDate }" required="required">
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">领导批示:</td>
	  		<td colspan="3">
	  			<textarea class="easyui-kindeditor" name="leaderComments" rows="3">${source.leaderComments }</textarea>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">扫描件上传:</td>
	  		<td colspan="3">
		    	<input class="easyui-filebox" type="text" id="file" name="file" data-options="prompt:'请选择文件...'" style="width: 65%;height: 25px;">
		    	<c:if test="${source.id != null }">
		    		<small><abbr id="title"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></abbr></small>
		    	</c:if>
	  		</td>
	  	</tr>
	  	<c:if test="${source.id != null }">
		  	<tr>
		  		<td></td>
		   		<td>
		   			<c:if test="${source.fileName != null }">
		   				<a id="download" title="点击下载" href="${ctx }/taskSource/downloadFile?id=${source.id}"><span class="glyphicon glyphicon-download-alt"></span>${source.fileName }</a>
		   			</c:if>
		   		</td>
		   		<td class="text-right">上传时间:</td>
		   		<td>${source.uploadDate }</td>
		   	</tr>
    	</c:if>
  	</table>
</form>
