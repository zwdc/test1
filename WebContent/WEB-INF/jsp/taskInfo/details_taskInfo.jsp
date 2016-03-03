<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
	$(function(){
		$("#taskContent").kindeditor({readonlyMode: true});
		$("#leadComments").kindeditor({readonlyMode: true});
		$("#suggestion").kindeditor({readonlyMode: true});
	})
</script>
<div class="easyui-layout">
    <table id="sales" class="table table-bordered table-hover table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">事项信息</td>
		</tr>
		<tr>
			<td class="text-right">事项标题:</td>
			<td colspan="3">${taskInfo.title }</td>
		</tr>
		<tr>
			<td class="text-right">文号:</td>
			<td>${taskInfo.taskNo }</td>
			<td class="text-right">立项时间:</td>
			<td><fmt:formatDate value="${taskInfo.createTaskDate }" type="both"/></td>
		</tr>
		<tr>
			<td class="text-right">反馈周期:</td>
			<td>${taskInfo.feedbackCycle }</td>
			<td class="text-right">办结时限:</td>
			<td><fmt:formatDate value="${taskInfo.endTaskDate }" type="both"/></td>
		</tr>
		<tr>
			<td class="text-right">联系人:</td>
			<td>${taskInfo.contacts }</td>
			<td class="text-right">联系电话:</td>
			<td>${taskInfo.contactsPhone }</td>
		</tr>
		<tr>
			<td class="text-right">急缓程度:</td>
			<td>${taskInfo.urgency }</td>
			<td class="text-right">文件类型:</td>
			<td>${taskInfo.taskType }</td>
		</tr>
		<tr>
			<td class="text-right">主办单位:</td>
			<td>${taskInfo.hostGroup.name }</td>
			<td class="text-right">协办单位:</td>
			<td>${taskInfo.assistantGroup.name }</td>
		</tr>
		<tr>
			<td colspan="4">事项内容:<textarea class="easyui-kindeditor" id="taskContent" rows="3" >${taskInfo.taskContent }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">领导批示:<textarea class="easyui-kindeditor" id="leadComments" rows="3" >${taskInfo.leadComments }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">拟办意见:<textarea class="easyui-kindeditor" id="suggestion" rows="3" >${taskInfo.suggestion }</textarea></td>
		</tr>
  	</table>
  	<c:if test="${contract.fileName != null }">
    	<div id="upload" class="easyui-layout">
	    <table class="table table-bordered table-hover table-condensed">
	    	<tr class="bg-primary">
		  		<td colspan="2" align="center">附件</td>
		  	</tr>
	    	<tr>
	    		<td><a id="download" title="点击下载" href="${ctx }/taskInfo/downloadFile?id=${taskInfo.id}"><span class="glyphicon glyphicon-download-alt"></span>&nbsp;${taskInfo.fileName }</a></td>
	    		<td>${taskInfo.uploadDate }</td>
	    	</tr>
	    </table>
	    </div>
    </c:if>
</div>
