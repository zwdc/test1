<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){
		$("#assistantGroup").kindeditor({readonlyMode: true});
		$("#remark").kindeditor({readonlyMode: true});
		//行编辑扩展（单元格编辑）
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					var opts = $(this).datagrid('options');
					var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					$(this).datagrid('beginEdit', param.index);
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
				});
			}
		});
	})
	
	var editIndex = undefined;
	function endEditing(){
		debugger;
		if (editIndex == undefined){return true}
		if ($('#workPlanDatagrid').datagrid('validateRow', editIndex)){
			$('#workPlanDatagrid').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	function onClickCell(index, field){
		debugger;
		if (endEditing()){
			$('#workPlanDatagrid').datagrid('selectRow', index)
					.datagrid('editCell', {index:index,field:field});
			editIndex = index;
		}
	}
	
	//结束编辑后保存
	function onAfterEdit(index, field, changes){
		debugger;
        $.ajax({
    		async: false,
    		cache: false,
            url: ctx + '/feedback/workPlan/'+field.id,
            type: 'post',
            dataType: 'json',
            data: {workPlan: changes.workPlan},
            success: function (result) {
            	if(0 != result.data) {
	                $.messager.show({
						title : result.title,
						msg : result.message,
						timeout : 1000 * 2
					});
            	}
            }
        });
	}
	
	function endEdit(){
		debugger;
		if ($('#workPlanDatagrid').datagrid('validateRow', editIndex)){
			$('#workPlanDatagrid').datagrid('endEdit', editIndex);
			editIndex = undefined;
		}
	}
</script>
<div class="easyui-layout">
<form id="project_form" method="post">
	<input name="projectId" value="${projectId }" type="hidden">
    <table class="table table-bordered table-hover table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务交办信息</td>
		</tr>
		<tr>
			<td class="text-right">任务标题:</td>
			<td colspan="3">${taskInfo.title }</td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td>${taskInfo.title }</td>
			<td class="text-right">急缓程度:</td>
			<td>${zwdc:getUrgencyType(taskInfo.urgency) }</td>

		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td>${taskInfo.taskSource.name }</td>
			<td class="text-right">反馈频度:</td>
			<td>${taskInfo.fbFrequency.name }</td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><fmt:formatDate value="${taskInfo.createTaskDate }" type="both"/></td>
			<td class="text-right">办结时限:</td>
			<td><fmt:formatDate value="${taskInfo.endTaskDate }" type="both"/></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><fmt:formatDate value="${taskInfo.claimLimitDate }" type="both"/></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',fitColumns:true,rownumbers:true,border:true,singleSelect:true">
				    <thead>
						<tr>
							<th data-options="field:'groupName'" width="40%">牵头单位名称</th>
							<th data-options="field:'userNames'" width="50%">联系人</th>
						</tr>
				    </thead>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="4">责任单位:<textarea class="easyui-kindeditor" id="assistantGroup" rows="3" >${taskInfo.assistantGroup }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">备注:<textarea class="easyui-kindeditor" id="remark" rows="3" >${taskInfo.remark }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">拟办意见:<textarea class="easyui-kindeditor" name="suggestion" rows="3" >${suggestion }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">阶段性计划:</td>
			<td colspan="3">
				<table id="workPlanDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/feedback/getFeedbackByProject?projectId=${projectId }',fitColumns:true,rownumbers:true,border:true,toolbar:'#tb',onClickCell:onClickCell,onAfterEdit:onAfterEdit">
				    <thead>
						<tr>
							<th data-options="field:'id',hidden:true">ID</th>
							<th data-options="field:'workPlanDate'" width="40%">阶段日期</th>
							<th data-options="field:'workPlan',editor:'text'" width="50%">阶段计划</th>
						</tr>
				    </thead>
				</table>
			</td>
		</tr>
  	</table>
</form>
</div>
<div id="tb" style="height:auto">
	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="endEdit();">结束编辑</a>
</div>