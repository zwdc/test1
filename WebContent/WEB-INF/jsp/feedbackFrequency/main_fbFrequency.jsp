<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
		
		$("#monthlyStartDay, #monthlyEndDay").val('');

	});
	
	function checkType(type) {
		if(type == 1) {
			//打开一次
			$("#singleTask").datetimebox("enable");
			//$("#singleTask").validatebox({required: true});
			//禁用每周
			$("#weekly").addClass("disabled");
			$("input[name='weeklyTask']").attr("disabled","disabled");
			$("#weeklyStartDate,#weeklyEndDate").timespinner("disable");
			//$("#weeklyStartDate,#weeklyEndDate").validatebox({required: false});
			//禁用每月
			$("#monthly").addClass("disabled");
			$("input[name='monthlyTask']").attr("disabled","disabled");
			$("#monthlyStartDay,#monthlyEndDay").combobox("disable");
			//$("#monthlyStartDay,#monthlyEndDay").validatebox({required: false});
		} else if(type == 2) {
			//打开每周
			$("#weekly").removeClass("disabled");
			$("input[name='weeklyTask']").removeAttr("disabled");
			$("#weeklyStartDate,#weeklyEndDate").timespinner("enable");
			//$("#weeklyStartDate,#weeklyEndDate").validatebox({required: true});
			//禁用一次
			$("#singleTask").datetimebox("disable");
			//$("#singleTask").validatebox({required: false});
			//禁用每月
			$("#monthly").addClass("disabled");
			$("input[name='monthlyTask']").attr("disabled","disabled");
			$("#monthlyStartDay,#monthlyEndDay").combobox("disable");
			//$("#monthlyStartDay,#monthlyEndDay").validatebox({required: false});
		} else if(type == 3) {
			//打开每月
			$("#monthly").removeClass("disabled");
			$("input[name='monthlyTask']").removeAttr("disabled");
			$("#monthlyStartDay,#monthlyEndDay").combobox("enable");
			//$("#monthlyStartDay,#monthlyEndDay").validatebox({required: true});
			//禁用一次
			$("#singleTask").datetimebox("disable");
			//$("#singleTask").validatebox({required: false});
			//禁用每周
			$("#weekly").addClass("disabled");
			$("input[name='weeklyTask']").attr("disabled","disabled");
			$("#weeklyStartDate,#weeklyEndDate").timespinner("disable");
			//$("#weeklyStartDate,#weeklyEndDate").validatebox({required: false});
		}
	}
</script>
<div class="easyui-layout">
<form id="fbFrequencyForm" method="post">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${feedback.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${feedback.isDelete }">
	<div class="table-responsive">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">反馈信息</td>
		</tr>
		<tr>
	  		<td class="text-right" style="width: 205px;">频度名称:</td>
	  		<td style="padding: 8px;">
	  			<input name="name" class="easyui-textbox" data-options="prompt:'填写频度名称'" value="${feedback.name }" required="required" type="text">
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
	  						<div class="radio">
		  						<label><input type="radio" name="type" value="1" ${feedback.type == 1?"checked":"" } onclick="checkType(1);">默认一次</label>
	  						</div>
	  					</td>
	  					<td style="vertical-align:middle;">
	  						<input id="singleTask" name="singleTask" class="easyui-datetimebox" data-options="prompt:'选择日期',editable:false" value="${feedback.singleTask }">
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
	  						<div class="radio">
	  							<label><input type="radio" name="type" value="2" ${feedback.type == 2?"checked":"" } onclick="checkType(2);">每周一次</label>
	  						</div>
	  					</td>
	  					<td>
	  						<div id="weekly" class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '7')?'checked':''} value="7">周日
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '1')?'checked':''} value="1">周一
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '2')?'checked':''} value="2">周二
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '3')?'checked':''} value="3">周三
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '4')?'checked':''} value="4">周四
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '5')?'checked':''} value="5">周五
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="weeklyTask" ${fn:contains(feedback.weeklyTask, '6')?'checked':''} value="6">周六
		  						</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<input id="weeklyStartDate" name="weeklyStartDate" class="easyui-timespinner" data-options="prompt:'设置开始时间',showSeconds:true" style="width: 100px" value="<fmt:formatDate value='${feedback.weeklyStartDate }' type='time'/>">
	  						-
	  						<input id="weeklyEndDate" name="weeklyEndDate" class="easyui-timespinner" data-options="prompt:'设置结束时间',showSeconds:true" style="width: 100px" value="<fmt:formatDate value='${feedback.weeklyEndDate }' type='time'/>">
	  						
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
	  						<div class="radio">
	  							<label><input type="radio" name="type" value="3" ${feedback.type == 3?"checked":"" } onclick="checkType(3);">每月一次</label>
	  						</div>
	  					</td>
	  					<td>
	  						<div id="monthly" class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '1,')?'checked':''} value="1">一月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '2,')?'checked':''} value="2">二月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '3')?'checked':''} value="3">三月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '4')?'checked':''} value="4">四月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '5')?'checked':''} value="5">五月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '6')?'checked':''} value="6">六月
		  						</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<div class="checkbox">
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '7')?'checked':''} value="7">七月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '8')?'checked':''} value="8">八月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '9')?'checked':''} value="9">九月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '10')?'checked':''} value="10">十月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '11')?'checked':''} value="11">十一月
		  						</label>
		  						<label class="checkbox-inline">
		  							<input type="checkbox" name="monthlyTask" ${fn:contains(feedback.monthlyTask, '12')?'checked':''} value="12">十二月
	  							</label>
	  						</div>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>
	  						<select name="monthlyStartDay"  class="easyui-combobox"  data-options="prompt:'选择一天',editable:false"  style="width: 50px">
	  							<c:forEach begin="1" end="31" step="1" var="day">
	  								<c:choose>
	  									<c:when test="${feedback.monthlyStartDay == day}">
	  										<option selected="selected" value="${day }">${day }</option>
	  									</c:when>
	  									<c:otherwise>
			  								<option value="${day }">${day }</option>
	  									</c:otherwise>
	  								</c:choose>
	  							</c:forEach>
							</select> -
	  						<select  name="monthlyEndDay"  class="easyui-combobox"  data-options="prompt:'选择一天',editable:false"  style="width: 50px">
	  							<c:forEach begin="1" end="31" step="1" var="day">
	  								<c:choose>
	  									<c:when test="${feedback.monthlyEndDay == day}">
	  										<option selected="selected" value="${day }">${day }</option>
	  									</c:when>
	  									<c:otherwise>
			  								<option value="${day }">${day }</option>
	  									</c:otherwise>
	  								</c:choose>
	  							</c:forEach>
							</select>
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