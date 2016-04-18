<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){
		$("#assistantGroup").kindeditor({readonlyMode: true});
		$("#remark").kindeditor({readonlyMode: true});
		$("#refuseReason").kindeditor({readonlyMode: true});
	})
	
	function changeGroup() {
		group_dialog = $('<div/>').dialog({
	    	title : "选择承办单位",
			top: 20,
			width : 1000,
			height : 400,
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/taskInfo/toChooseGroup",
	        onLoad: function () {
	            //显示候选组
	            group_datagrid = $('#group_datagrid').datagrid({
	                url: ctx+"/choose/chooseGroup",
	                width : 'auto',
	        		height :  $(this).height()-40,
	        		pagination:true,
	        		rownumbers:true,
	        		border:false,
	        		striped:true,
	        		singleSelect:true,
	                columns : [ 
	                    [ 
	                      {field:'ck', title : '#',width : ($(this).width() - 50) * 0.1,align : 'center',checkbox:true},
	                      {field : 'name',title : '单位名称',width : ($(this).width() - 50) * 0.45,align : 'center'},
	                      {field : 'type',title : '单位类型',width : ($(this).width() - 50) * 0.45,align : 'center'}
	            	    ] 
	                ]
	            });
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	selectGroups();
	                	group_dialog.dialog('destroy');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	group_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	group_dialog.dialog('destroy');
	        }
	    });
	}
	
	function selectGroups() {
		$("#hostGroupId").val("");	//清空id
		//重新生成牵头单位
		var rows = group_datagrid.datagrid('getChecked');
		var groupIds = "";
		$.each(rows, function(index, field) { 
    		$.ajax({
                url: ctx + '/taskInfo/getGroupUser/'+field.id,
                type: 'post',
                dataType: 'text',
                success: function (data) {
					$('#hostGroupDatagrid').datagrid('insertRow',{
						row: {
							groupId: field.id,
							groupName: field.name,
							userNames: data
						}
					});
                }
            });
    		groupIds += field.id + ',';
    	});
		$("#hostGroupId").val(groupIds.substring(0, groupIds.length-1));
	}
</script>
<div class="easyui-layout">
<form id="form" action="${ctx }/project/approval" method="post">
	<input name="projectId" value="${project.id }" type="hidden">
	<input id="taskId" name="taskId" type="hidden">
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
			<td class="text-right">承办单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',fitColumns:true,rownumbers:true,border:true">
				    <thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'groupId',hidden:true">ID</th>
							<th data-options="field:'groupName'" width="25%">牵头单位名称</th>
							<th data-options="field:'userNames0'" width="15%">联系人A</th>
							<th data-options="field:'linkway0'" width="20%">联系方式</th>
							<th data-options="field:'userNames1'" width="15%">联系人B</th>
							<th data-options="field:'linkway1'" width="20%">联系方式</th>
						</tr>
				    </thead>
				</table>
				<input id="hostGroupId" name="hostGroup" value = "${taskInfo.hostGroup }" type="hidden"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">责任单位:<textarea class="easyui-kindeditor" id="assistantGroup" rows="3" >${taskInfo.assistantGroup }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">备注:<textarea class="easyui-kindeditor" id="remark" rows="3" >${taskInfo.remark }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">拒签单位:</td>
			<td>${project.group.name }</td>
			<td class="text-right">拒签人:</td>
			<td>${project.refuseUser.name }</td>
		</tr>
		<tr>
			<td colspan="4">拒签原因:<textarea class="easyui-kindeditor" id="refuseReason" rows="3" >${project.refuseReason }</textarea></td>
		</tr>
		<tr>
			<td colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" onclick="changeGroup();" data-options="iconCls:'icon-ok'">替换此单位</a></td>
		</tr>
		<tr>
	  		<td colspan="4">
	  			<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;审批意见:
	  			<textarea class="easyui-kindeditor" name="comment" rows="3"></textarea>
	  		</td>
	  	</tr>
  	</table>
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(true);" data-options="iconCls:'icon-ok'">同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(false);" data-options="iconCls:'icon-remove'">不同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="closeDialog();" data-options="iconCls:'icon-cancel'">关闭</a>
</div>
</div>
