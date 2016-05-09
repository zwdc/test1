<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>

<script type="text/javascript">
	$(function() {
		$("blockquote").css({
			"border-left": "5px solid #4EA76E",
			"font-size": "15px",
			"padding": "5px 10px",
			"margin": "0px 0px 10px"
		})
	});
	//暂存
	function saveTemporary() {
		$('#feedback_form').form('submit', {
	    	url: ctx+"/feedback/saveFeedback",
	        onSubmit: function (data) {
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
		        	task_dialog.dialog("refresh",ctx+"/feedback/toMain?action=modify&id="+json.data);
		        } 
		        $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
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
	}
	
	//完成任务
	function completeTask() {
		$('#feedback_form').form('submit', {
	    	url: ctx+"/feedback/completeTask",
	        onSubmit: function (param) {
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
		        	task_dialog.dialog('destroy');
              	  	todoTask_datagrid.datagrid('reload');//重新加载列表数据
		        } 
		        $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
		    }
	    });
	}
	
	//关闭dialog
	function closeDialog() {
		task_dialog.dialog('destroy');
	}
</script>
<div class="easyui-layout">
<form id="feedback_form" method="post" encType="multipart/form-data">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
	<input id="taskId" name="taskId" type="hidden">
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
				value="${(feedback.status eq 'FEEDBACKING' ) ? '反馈中' : ((feedback.status eq 'RETURNED') ? '已退回' : ((feedback.status eq 'ACCEPT') ? '已采用' : '未反馈')) }" data-option="prompt:'来源名称'"
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
					data-options="readonlyMode:true" rows="3">${feedback.workPlan }</textarea>
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5"><textarea class="easyui-kindeditor" name="situation" rows="3" required="required" >${feedback.situation }</textarea></td>
		</tr>
		
		<tr>
			<td class="text-right">存在问题/困难:</td>
			<td colspan="5"><textarea class="easyui-kindeditor" name="problems" rows="3">${feedback.problems }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">解决措施/建议:</td>
			<td colspan="5"><textarea class="easyui-kindeditor" name="solutions" rows="3">${feedback.solutions }</textarea></td>
		</tr>
        <tr>
	  		<td class="text-right">附件材料：</td>
	   		<td colspan="5" style="8px">		   
	   		     <c:forEach items="${feedback.fdaList}" var="fda"> 
	   		     	<div class="well well-small">
   					    <a href="${ctx }${fda.url}" target=blank> 
   						<img src="${ctx }/images/icon/${fda.type}.gif" title="">${fda.uploadDate} - ${fda.name} 
 					    </a>
 					</div>
				 </c:forEach> 
	   		</td>
	   	</tr>	
	   	<tr>
		  	<td class="text-right">佐证材料上传:</td>
		  	<td colspan="5">
		  		  <a id="filefield" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加附件</a> 
		  		   <input class='easyui-filebox' type='hidden' name='file'>
		  		   <div id="fileZone"> </div>    	
		   </td>
		</tr>   
		<tr>
	  		<td colspan="6">
	  			<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>&nbsp;
	  			<a data-toggle="collapse" href="#comments" aria-expanded="false" aria-controls="comments" title="点击查看审批意见">意见列表:</a>
	  			<div class="collapse" id="comments">
	  			<c:choose>
	  				<c:when test="${!empty commentsList }">
	  					<c:forEach var="comments" items="${commentsList }">
				  			<blockquote>
								<p class="text-primary">${comments.userName } : ${comments.content }</p>
		  						<footer><fmt:formatDate value="${comments.time }" type="both"/></footer>
						    </blockquote> 
						</c:forEach>
	  				</c:when>
	  				<c:otherwise>
	  					<blockquote>
							<p class="text-muted">暂无审批意见！</p>
					    </blockquote> 
	  				</c:otherwise>
	  			</c:choose>
				  	
				</div>
	  		</td>
	  	</tr>	   
	</table>
    <hr style="margin-top: -5px ">
	<div class="pull-right" style="margin: -15px 5px 5px 0px;">
	 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveTemporary();" data-options="iconCls:'icon-save'">暂存</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="completeTask();" data-options="iconCls:'icon-ok'">提交任务</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="closeDialog();" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</form>
</div>