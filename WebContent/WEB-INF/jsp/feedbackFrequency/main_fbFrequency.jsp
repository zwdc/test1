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
		 	url: ctx+"/feedback/completeTask",
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
	<input type="hidden" name="taskInfo.id" value="${feedback.taskInfo.id }">
	<input type="hidden" name="createUserId" value="${feedback.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${feedback.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${feedback.isDelete }">
    <input type="hidden" name="status" value="${feedback.status }">
    <input type="hidden" name="fileName" id="fileName" value = "${feedback.fileName }">
	<input type="hidden" name="filePath" value = "${feedback.filePath }"> 
	<input type="hidden" name="uploadDate" value = "<fmt:formatDate value='${feedback.uploadDate }' type='both'/>">
	<div class="table-responsive">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">反馈信息</td>
		</tr>
		<tr>
	  		<td class="text-right" style="width: 205px;">频度名称:</td>
	  		<td style="padding: 8px;">
	  			<input name="phone" class="easyui-textbox" data-options="prompt:'填写频度名称'"  value="${feedback.phone }" required="required" type="text">
	  		</td>
	  	</tr>
		<tr class="active">
		  	<td colspan="2">
		  		<span class="glyphicon glyphicon-link" aria-hidden="true"></span>&nbsp;默认一次:
		  	</td>
	  	</tr>
	  	<tr>
	  		<td colspan="2">
	  			<div class="table-responsive">
		  		<table class="table table-bordered">
	  				<tr>
	  					<td rowspan="2" class="text-center" style="width: 200px; vertical-align:middle;">
	  						<input type="radio" name="type" value="0">默认一次
	  					</td>
	  					<td>
	  						<input name="sourceDate" class="easyui-datetimebox" data-options="prompt:'选择日期',editable:false" value="${source.sourceDate }" required="required">
	  					</td>
	  				</tr>
		  		</table>
		  		</div>
	  		</td>
	  	</tr>
	  	<tr class="active">
		  	<td colspan="2">
		  		<span class="glyphicon glyphicon-link" aria-hidden="true"></span>&nbsp;每周一次:
		  	</td>
	  	</tr>
		<tr>
	  		<td colspan="2">
	  			<div class="table-responsive">
	  			<table class="table table-bordered">
	  				<tr>
	  					<td rowspan="2" class="text-center" style="width: 200px; vertical-align:middle;">
	  						<input type="radio" name="type" value="0">每周一次
	  					</td>
	  					<td>
	  						<div class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周日
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周一
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周二
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周三
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周四
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周五
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">周六
		  						</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<input name="sourceDate" class="easyui-timespinner" data-options="prompt:'设置时间',showSeconds:true" value="${source.sourceDate }" required="required">
	  					</td>
	  				</tr>
	  			</table>
	  			</div>
	  		</td>
		</tr>
	  	<tr class="active">
		  	<td colspan="2">
		  		<span class="glyphicon glyphicon-link" aria-hidden="true"></span>&nbsp;每月一次:
		  	</td>
	  	</tr>
		<tr>
			<td colspan="2">
				<div class="table-responsive">
				<table class="table table-bordered">
	  				<tr>
	  					<td rowspan="3" class="text-center" style="width: 200px; vertical-align:middle;">
	  						<input type="radio" name="type" value="0">每月一次
	  					</td>
	  					<td>
	  						<div class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox">一月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">二月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">三月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">四月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">五月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">六月
		  						</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<div class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox">七月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">八月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">九月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">十月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">十一月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox">十二月
	  							</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<input name="sourceDate" class="easyui-datetimebox" data-options="prompt:'反馈时间',editable:false" value="${source.sourceDate }" required="required"> - 
	  						<input name="sourceDate" class="easyui-datetimebox" data-options="prompt:'反馈时间',editable:false" value="${source.sourceDate }" required="required">
	  					</td>
	  				</tr>
	  			</table>
	  			</div>
	  		</td>
		</tr>
	</table>
	</div>
</form>
</div>