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
		<c:choose>
		<c:when test="${feedback==null}">
			<tr>
		  		<td class="text-right">佐证材料上传:</td>
		  		<td colspan="5">
		  		    <a id="filefield" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加附件</a> 
		  		    <input class='easyui-filebox' type='hidden' name='file'>
		  		    <div id="fileZone"> </div>    	
		  		</td>
		  	</tr>
		</c:when>
		<c:otherwise>
			<tr>
		  		<td>附件材料：</td>
		   		<td colspan="5">
			   		 <c:forEach items="${feedback.fdaList}" var="fda"> 
			   		        上传时间:${fda.uploadDate } - 
			   		   <a id="download" title="点击下载" href="${ctx}${fda.url}"><span class="glyphicon glyphicon-download-alt"></span>${fda.name }</a>
			   		 </c:forEach> 
		   		</td>
		   	</tr>	   
		</c:otherwise>
		</c:choose>  	   	
	</table>    
</form>
</div>