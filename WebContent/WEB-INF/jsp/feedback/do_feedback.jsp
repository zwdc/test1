<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
		var count = 0; 
		 $("#filefield").click(function(){  
			var input = $("<input class='easyui-filebox' type='text' name='file' data-options=\""+"prompt:'请选择文件...'\""+" style='width: 40%;height: 25px;'>");  	
			var button = $("<input type='button' class='easyui-linkbutton' value='移除 '></div>");  
			var pre=$("<div>").append(button).append(input);		
			$("#fileZone").append(pre);
			$.parser.parse(pre);
			  button.click(function() {  
				  pre.remove();
		     });
			 
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
	<table class="table table-bordered table-hover" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">填报反馈信息</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" class="easyui-textbox"
				value="${user.name }" data-option="prompt:'起草人'" disabled="disabled"></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" class="easyui-textbox" value="${feedback.project.group.name }"
				data-option="prompt:'牵头部门'" disabled="disabled"></td>
			<td class="text-right">反馈时限:</td>
			<td><input name="feedback.feedbackEndDate" class="easyui-datetimebox"
				data-options="prompt:'反馈时限'"
				value="<fmt:formatDate value='${feedback.feedbackEndDate}' type='both'/>" disabled="disabled"></td>	
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" name="status" class="easyui-textbox"
				value="${(feedback.status eq 'RUNNING' ) ? '反馈中' : ((feedback.status eq 'FAIL') ? '已退回' : '已采用') }" data-option="prompt:'来源名称'"
				disabled="disabled"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" name="delayCount" class="easyui-textbox"
				value="${feedback.delayCount>0?'延期':'未延期' }" data-option="prompt:'来源名称'"
				disabled="disabled"></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input name="feedback.feedbackStartDate" class="easyui-datetimebox"
				data-options="prompt:'反馈开始时间'"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate}' type='both'/>" disabled="disabled"></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					data-options="readonlyMode:true,prompt:'阶段工作计划'" rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					name="situation" rows="3"  required="required" >${feedback.situation }</textarea></td>
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
		  		<td colspan="5">
		  		    <a id="filefield" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加附件</a> 
		  		    <input class='easyui-filebox' type='hidden' name='file'>
		  		    <div id="fileZone"> </div>    	
		  		</td>
		</tr>   	
	</table>    
</form>
</div>