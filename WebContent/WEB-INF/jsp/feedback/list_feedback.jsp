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
	
</script>
<div class="easyui-layout">
<div id="tt" class="easyui-tabs" style="width:100%;height:auto">
	<c:choose>
		<c:when test="${!empty list }">
			<c:forEach items="${list }" var="feedback" varStatus="i">
			    <div title="第&nbsp;${i.count }&nbsp;次反馈" style="padding:10px;">
					<table class="table table-bordered table-hover table-condensed">
						<tr class="bg-primary">
							<td colspan="6" align="center">反馈信息</td>
						</tr>
						<tr>
							<td class="text-right">原始起草人:</td>
							<td>${feedback.originalPerson }</td>
							<td class="text-right">手机:</td>
							<td>${feedback.phone }</td>
							<td class="text-right">办公电话:</td>
							<td>${feedback.workPhone }</td>
						</tr>
						<tr>
							<td class="text-right">处/科室:</td>
							<td>${feedback.offices }</td>
							<td class="text-right">职务:</td>
							<td>${feedback.dutyOf }</td>
							<td class="text-right">邮箱:</td>
							<td>${feedback.email }</td>
						</tr>
						<tr>
							<td class="text-right">联络人:</td>
							<td>${feedback.contacts }</td>
							<td class="text-right">联系电话:</td>
							<td>${feedback.contactsPhone }</td>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td colspan="6">落实情况:<textarea class="easyui-kindeditor" id="content${i.count }" rows="8">${feedback.content }</textarea></td>
						</tr>
						<tr class="active">
							<td class="text-right">附件:</td>
							<td colspan="4"><a id="download" title="点击下载" href="${ctx }/feedback/downloadFile?id=${feedback.id}"><span class="glyphicon glyphicon-download-alt"></span>&nbsp;${feedback.fileName }</a></td>
	    					<td>${feedback.uploadDate }</td>
						</tr>
						<tr>
							<td class="text-right">反馈情况:</td>
							<td colspan="2">
								<input type="radio" name="status${i.count }" ${feedback.status == 'FEEDBACKING'?'checked':'' } readonly="readonly"> 反馈中
								<input type="radio" name="status${i.count }" ${feedback.status == 'RETURNED'?'checked':'' } readonly="readonly"> 已退回
								<input type="radio" name="status${i.count }" ${feedback.status == 'ACCEPT'?'checked':'' } readonly="readonly"> 已采用
							</td>
							<td class="text-right">是否迟报:</td>
							<td colspan="2">
								<input type="radio" name="isDelay${i.count }" ${feedback.isDelay == 0?'checked':'' } readonly="readonly"> 否
								<input type="radio" name="isDelay${i.count }" ${feedback.isDelay == 1?'checked':'' } readonly="readonly"> 是
							</td>
						</tr>
					</table>
					<script type="text/javascript">$("#content${i.count}").kindeditor({readonlyMode: true});</script>
			    </div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<div title="提示" style="padding:10px;">
				<div class="alert alert-warning" role="alert">
				  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				      暂无反馈记录！
				</div>
		    </div>
		</c:otherwise>
	</c:choose>
</div>
</div>