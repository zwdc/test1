<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/css/bootstrap-table.min.css" type="text/css" />
<script type="text/javascript" src="${ctx}/js/bootstrap-table.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript">
	$(function() {
		$.getJSON(ctx+"/js/app/app.json",function(data){
			$('#feedback').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].feedback
			});
			
			$('#urgency').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].urgencyType
			});
			
			$('#taskType').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].taskType
			});
		});
		
		/* $('#urgency').combobox({
			url:ctx+"/urgency/urgencyList",
			valueField: "id",
		    textField: "name"
		});  */
	});
</script>
<div class="easyui-layout">
<form id="taskInfo_form" method="post">
	<input type="hidden" id="taskInfoId" name="id" value="${taskInfo.id }">
	<input type="hidden" name="createUserId" value="${taskInfo.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${taskInfo.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${taskInfo.isDelete }">
    <input type="hidden" name="status" value="${taskInfo.status }">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">事项信息</td>
		</tr>
		<tr>
			<td class="text-right">事项标题:</td>
			<td colspan="3"><input name="title" class="easyui-textbox" data-options="prompt:'填写事项标题'" value="${taskInfo.title }" required="required" type="text" style="width: 50%"></td>
		</tr>
		<tr>
			<td class="text-right">文号:</td>
			<td><input name="taskNo" class="easyui-textbox" data-options="prompt:'填写文号'"  value="${taskInfo.taskNo }" required="required" type="text"></td>
			<td class="text-right">立项时间:</td>
			<td><input name="createTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.createTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">反馈周期:</td>
			<td><input id="feedback" name="feedbackCycle" class="easyui-combobox" data-options="prompt:'选择反馈周期'" value="${taskInfo.feedbackCycle }" required="required"></td>
			<td class="text-right">办结时限:</td>
			<td><input name="endTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.endTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">联系人:</td>
			<td><input name="contacts" class="easyui-textbox" data-options="prompt:'填写联系人'" value="${taskInfo.contacts }" required="required" type="text"></td>
			<td class="text-right">联系电话:</td>
			<td><input name="contactsPhone" class="easyui-textbox" data-options="prompt:'填写联系电话'" value="${taskInfo.contactsPhone }" required="required" type="text"></td>
		</tr>
		<tr>
			<td class="text-right">急缓程度:</td>
			<td><input id="urgency" name="urgency.id" value="${taskInfo.urgency.id }" data-options="prompt:'选择急缓程度'" class="easyui-combobox" required="required"></td>
			<td class="text-right">文件类型:</td>
			<td><input id="taskType" name="taskType" value="${taskInfo.taskType }" data-options="prompt:'选择文件类型'" class="easyui-combobox" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">主办单位:</td>
			<td>
				<input type="text" id="hostGroup_name" name="hostGroup_name" value = "${hostGroup_name }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('hostGroup');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseGroup(false, 'hostGroup');
								}
							}],editable:false,prompt:'选择主办单位'"/>
				<input id="hostGroup_id" name="hostGroup" value = "${taskInfo.hostGroup }" type="hidden"/>
			</td>
			<td class="text-right">协办单位:</td>
			<td>
				<input type="text" id="assistantGroup_name" name="assistantGroup_name" value = "${assistantGroup_name }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('assistantGroup');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseGroup(false, 'assistantGroup');
								}
							}],editable:false,prompt:'选择主办单位'"/>
				<input id="assistantGroup_id" name="assistantGroup" value = "${taskInfo.assistantGroup }" type="hidden"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">事项内容:<textarea class="easyui-kindeditor" name="taskContent" rows="3" >${taskInfo.taskContent }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">领导批示:<textarea class="easyui-kindeditor" name="leadComments" rows="3" >${taskInfo.leadComments }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">拟办意见:<textarea class="easyui-kindeditor" name="suggestion" rows="3" >${taskInfo.suggestion }</textarea></td>
		</tr>
	</table>
	<p class="text-danger">请先保存任务信息后，再上传合同文件！</p>
</form>

<!-- 上传合同 -->
<c:if test="${taskInfo.id != null }">
	<form id="uploadForm" method="post" encType="multipart/form-data">
		<input name="taskInfoId" type="hidden" value="${taskInfo.id }">
	    <table class="table table-bordered table-hover table-condensed">
	    	<tr class="bg-primary">
		  		<td colspan="2" align="center">上传合同</td>
		  	</tr>
	    	<tr>
	    		<td>
	    			<input class="easyui-filebox" type="text" id="file" name="file" data-options="prompt:'请选择文件...'" style="width: 90%;height: 25px;" required="required">
	    			<!-- <input type="file" id="file" name="file" style="width: 800px;height: 30px;"> -->
	    		</td>
	    		<td align="center"><button type="button" onclick="upload();" class="btn btn-primary btn-sm">&nbsp;上传&nbsp;</button></td>
	    	</tr>
	    	<tr>
	    		<td>
	    			<c:if test="${contract.fileName != null }">
	    				<a id="download" title="点击下载" href="${ctx }/taskInfo/downloadFile?id=${taskInfoId.id}"><span class="glyphicon glyphicon-download-alt"></span>${contract.fileName }</a>
	    			</c:if>
	    		</td>
	    		<td>${taskInfoId.uploadDate }</td>
	    	</tr>
	    </table>
    </form>
</c:if>
</div>