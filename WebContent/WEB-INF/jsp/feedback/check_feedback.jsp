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
	<table class="table table-bordered" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">反馈信息审核</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" class="easyui-textbox"
				value="${user.name}" data-option="prompt:'起草人'" disabled="disabled"></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" class="easyui-textbox" 
			    value="${feedback.project.group.name }"
				data-option="prompt:'牵头部门'"  disabled="disabled" >
			</td>
			<td class="text-right">反馈时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${feedback.feedbackEndDate }' type='both'/>"></td>	
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" class="easyui-textbox"
				value="${(feedback.status eq 'FEEDBACKING' ) ? '反馈中' : ((feedback.status eq 'RETURNED') ? '已退回' : '已采用') }" data-option="prompt:'来源名称'"
				 disabled="disabled"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" class="easyui-textbox"
				value="${feedback.delayCount>0?'延期':'未延期' }" data-option="prompt:'来源名称'"
				 disabled="disabled" ></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input class="easyui-datetimebox"
				data-options="prompt:'反馈开始时间'" disabled="disabled"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate}' type='both'/>"></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor" 
					data-options="readonlyMode:true"  rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" 	 rows="3">${feedback.situation }</textarea></td>
		</tr>
		
		<tr>
			<td class="text-right">存在问题/困难:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" 	rows="3">${feedback.problems }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">解决措施/建议:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
				data-options="readonlyMode:true" rows="3">${feedback.solutions }</textarea></td>
		</tr>
        <tr>
		  		<td class="text-right">附件材料：</td>
		   		<td colspan="5" style="8px">
		   		     <c:forEach items="${feedback.fdaList}" var="fda"> 
					 <div class="layout_default">
    					<p class="info"><span class="date"></span> <span class="author"></span></p>
    					<div class="image_container"> <a href="${ctx }${fda.url}" rel="lightbox[ostec]"> 
    						<img src="${ctx }${fda.url}"> </a> </div>
  					</div>
					 </c:forEach> 

		   		</td>
		   	</tr>	   
	  	<tr>
	  		<td class="text-right">反馈材料审核:</td>
	  		<td colspan="5">
	  		   <div class="">
	  		    <pre><input type="radio" class="easyui-radio" name="status" value="SUCCESS" checked="checked" />采用   /  <input type="radio" class="easyui-radio" name="status" value="FAIL"/>退回</pre>
	  		   </div>
	  		   <div>
	  		   <textarea class="easyui-kindeditor"
					data-options="prompt:'反馈开始时间'" name="suggestion" rows="3" required="required"></textarea>	   
	  		   </div>	  		    
	  		</td>
	  	</tr>
    	
	</table>
    
</form>
</div>