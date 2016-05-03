<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div id="combo_toolbar" style="padding:2px 0; display: none;">
	<table>
		<tr>
			<td style="padding-left:5px">
				<input id="combo_paramBox" style="width: 200px" class="easyui-searchbox" type="text"/>
			</td>
		</tr>
	</table>
</div>
<div class="easyui-layout">
<form id="taskInfo_form" method="post" encType="multipart/form-data">
	<table class="table table-bordered table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">批量上传任务</td>
		</tr>
		<tr>
			<td class="text-right">Excel文件:</td>
			<td colspan="3">
			    <input class="easyui-filebox" type="text" id="file" name="file" data-options="prompt:'请选择文件...'" style="width: 65%;height: 25px;">
		    	<c:if test="${source.id != null }">
		    		<small><abbr id="title"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></abbr></small>
		    	</c:if>
			</td>
		</tr>		
	</table>
</form>
</div>
