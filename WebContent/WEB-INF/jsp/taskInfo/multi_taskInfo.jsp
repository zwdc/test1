<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="well well-small" style="background-image:url(${ctx}/images/multiInsert.jpeg)">
   <span class="badge" iconCls="icon-save" plain="true" >使用说明</span>
	  <p>
		<shiro:user>
		1、通过Excel导入，对Excel表格制作的要求比较高。所以请谨慎使用。<br/>
		2、任务的数据表格应该放在最左边的工作簿中，一般是 Sheet1。表格中的数据不能有字段（列）名称。<br/>
		3、数据从最左边依次是：<br/>
		<span class="label label-danger">任务内容</span>——<span class="label-info"><strong>（文本型，1000字）</strong></span><br/>
		<span class="label label-danger">任务开始时间</span>——<span class="label-info"><strong>（日期时间型）</strong></span><br/>
		<span class="label label-danger">任务结束时限</span>——<span class="label-info"><strong>（日期时间型）</strong></span><br/>
		<span class="label label-danger">任务签收时限</span>——<span class="label-info"><strong>（日期时间型）</strong></span><br/>
		<span class="label label-danger">牵头单位列表</span>——<span class="label-info"><strong>（字符类型）</strong></span><br/>
		<span class="label label-danger">协办单位列表</span>——<span class="label-info"><strong>（文本类型，单位名称要正确，中间用西文逗号隔开）</strong></span><br/>
		<span class="label label-danger">急缓程度ID</span>——<span class="label-info"><strong>（数值型，0为特提，1为特急 ，2为加急，3为平急）</strong></span><br/>
		<span class="label label-danger">反馈频度ID</span>——<span class="label-info"><strong>（数值型，通过反馈频度列表查询）</strong></span><br/>
		<span class="label label-danger">任务来源ID</span>——<span class="label-info"><strong>（数值型，通过任务来源列表查询）</strong></span><br/>
		</shiro:user>
	    </p>
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
			</td>
		</tr>		
	</table>
</form>

</div>
