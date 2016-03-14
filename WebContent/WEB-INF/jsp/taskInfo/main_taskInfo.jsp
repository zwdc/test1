<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/app/choose/group/choose_group.js?_=${sysInitTime}"></script>
<script type="text/javascript" src="${ctx}/js/app/choose/user/user.js?_=${sysInitTime}"></script>
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
		
		$("#reason").kindeditor({readonlyMode: true});
	});
	
	function upload() {
		$('#uploadForm').form('submit', {
	    	url: ctx+"/taskInfo/uploadFile",
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
		    success: function (result) {
		        $.messager.progress('close');
		        var json = $.parseJSON(result);
		        if (json.status) {
		        	taskInfo_dialog.dialog("refresh",ctx+"/taskInfo/toMain?id="+json.data.toString());
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
<form id="taskInfo_form" method="post">
	<input type="hidden" id="taskInfoId" name="id" value="${taskInfo.id }">
	<input type="hidden" name="createUserId" value="${taskInfo.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${taskInfo.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${taskInfo.isDelete }">
    <input type="hidden" name="status" value="${taskInfo.status }">
    <input type="hidden" name="fileName" id="fileName" value = "${taskInfo.fileName }"> <!-- id="fileName"不能删 -->
	<input type="hidden" name="filePath" value = "${taskInfo.filePath }"> 
	<input type="hidden" name="uploadDate" value = "<fmt:formatDate value='${taskInfo.uploadDate }' type='both'/>">
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
			<td><input id="urgency" name="urgency" value="${taskInfo.urgency }" data-options="prompt:'选择急缓程度'" class="easyui-combobox" required="required"></td>
			<td class="text-right">文件类型:</td>
			<td><input id="taskType" name="taskType" value="${taskInfo.taskType }" data-options="prompt:'选择文件类型'" class="easyui-combobox" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">承办单位:</td>
			<td>
				<input type="text" id="hostGroup_name" name="hostGroup_name" value = "${taskInfo.hostGroup }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('hostGroup');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseGroup(true, 'hostGroup');
								}
							}],editable:false,prompt:'选择承办单位'"/>
				<input id="hostGroup_id" name="hostGroup.id" value = "${taskInfo.hostGroup }" type="hidden"/>
			</td>
			<td class="text-right">协办单位:</td>
			<td>
				<input type="text" id="assistantGroup_name" name="assistantGroup_name" value = "${taskInfo.assistantGroup }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('assistantGroup');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseGroup(true, 'assistantGroup');
								}
							}],editable:false,prompt:'选择协办单位'"/>
				<input id="assistantGroup_id" name="assistantGroup.id" value = "${taskInfo.assistantGroup }" type="hidden"/>
			</td>
		</tr>
		<tr>
			<td class="text-right">承办人员:</td>
			<td>
				<input type="text" id="hostUser_name" name="hostUser_name" value = "${taskInfo.hostUser }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('hostUser');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseUser(true, 'hostUser');
								}
							}],editable:false,prompt:'选择承办人员'"/>
				<input id="hostUser_id" name="hostUser.id" value = "${taskInfo.hostUser }" type="hidden"/>
			</td>
			<td class="text-right">协办人员:</td>
			<td>
				<input type="text" id="assistantUser_name" name="assistantUser_name" value = "${taskInfo.assistantUser }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('assistantUser');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseUser(true, 'assistantUser');
								}
							}],editable:false,prompt:'选择协办人员'"/>
				<input id="assistantUser_id" name="assistantUser.id" value = "${taskInfo.assistantUser }" type="hidden"/>
			</td>
		</tr>
		<tr>
			<td class="text-right">分管领导:</td>
			<td>
				<input type="text" id="leadership_name" name="leadership_name" value = "${taskInfo.leadership }" class="easyui-textbox" required="required"
	  			data-options="icons: [
	  						{
								iconCls:'icon-clear',
								handler: function(e){
									clearChoose('leadership');
								}
							},
	  						{iconCls:'icon-search',
								handler: function(e){
									chooseUser(true, 'leadership');
								}
							}],editable:false,prompt:'选择协办人员'"/>
				<input id="leadership_id" name="leadership.id" value = "${taskInfo.leadership }" type="hidden"/>
			</td>
			<td colspan="2"></td>
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
	<p class="text-danger">请先保存任务信息后，再上传附件！</p>
</form>

<!-- 上传合同 -->
<c:if test="${taskInfo.id != null }">
	<form id="uploadForm" method="post" encType="multipart/form-data">
		<input name="taskInfoId" type="hidden" value="${taskInfo.id }">
	    <table class="table table-bordered table-hover table-condensed">
	    	<tr class="bg-primary">
		  		<td colspan="2" align="center">上传附件</td>
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
	    			<c:if test="${taskInfo.fileName != null }">
	    				<a id="download" title="点击下载" href="${ctx }/taskInfo/downloadFile?id=${taskInfo.id}"><span class="glyphicon glyphicon-download-alt"></span>${taskInfo.fileName }</a>
	    			</c:if>
	    		</td>
	    		<td>${taskInfo.uploadDate }</td>
	    	</tr>
	    </table>
    </form>
</c:if>

<c:if test="${!empty refuseReasonList }">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-warning">
	  		<td colspan="4" align="center">拒绝原因</td>
	  	</tr>
		<c:forEach items="${refuseReasonList }" var="r">
			<tr>
				<td class="text-right">拒绝人:</td>
				<td>${r.createUser.name }</td>
				<td class="text-right">拒绝时间:</td>
				<td><fmt:formatDate value="${r.createDate }" type="both"/></td>
			</tr>
			<tr>
				<td class="text-right">拒绝原因:</td>
				<td colspan="3"><textarea class="easyui-kindeditor" id="reason" rows="3" >${r.reason }</textarea></td>
			</tr>
		</c:forEach>
	</table>
</c:if>
</div>