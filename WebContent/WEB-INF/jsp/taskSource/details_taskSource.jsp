<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){
		$("#info").kindeditor({readonlyMode: true});
		$("#leaderComments").kindeditor({readonlyMode: true});
		
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
	})
</script>
<div class="easyui-layout">
    <table class="table table-bordered table-hover">
	  	<tr class="bg-primary">
	  		<td colspan="4" align="center">任务来源信息</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务来源全称:</td>
	  		<td colspan="3">${source.name }</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务来源简介:</td>
	  		<td colspan="3">
	  			<textarea class="easyui-kindeditor" id="info" rows="3">${source.info }</textarea>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">任务类型:</td>
	  		<td>${source.taskInfoType.name }</td>
	  		<td class="text-right">来源时间:</td>
	  		<td><fmt:formatDate value="${source.sourceDate }" type="both"/></td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">领导批示:</td>
	  		<td colspan="3">
	  			<textarea class="easyui-kindeditor" id="leaderComments" rows="3">${source.leaderComments }</textarea>
	  		</td>
	  	</tr>
	  	<c:if test="${source.fileName != null }">
		  	<tr>
		  		<td class="text-right">扫描件:</td>
		   		<td><a id="download" title="点击下载" href="${ctx }/taskSource/downloadFile?id=${source.id}"><span class="glyphicon glyphicon-download-alt"></span>${source.fileName }</a></td>
		   		<td class="text-right">上传时间:</td>
		   		<td>${source.uploadDate }</td>
		   	</tr>
    	</c:if>
  	</table>
</div>
