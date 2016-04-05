<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/app/choose/group/choose_group.js?_=${sysInitTime}"></script>
<script type="text/javascript" src="${ctx}/js/app/choose/user/user.js?_=${sysInitTime}"></script>
<script type="text/javascript">
	$(function() {
		$.getJSON(ctx+"/js/app/app.json",function(data){
			$('#feedback').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].feedback
			});
			
			$('#urgency').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].urgencyType
			});
			
			$('#taskType').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].taskType
			});
		});
		
		/* $('#urgency').combobox({
			url:ctx+"/urgency/urgencyList",
			valueField: "id",
		    textField: "name"
		});  */
		
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
		
		$("#reason").kindeditor({readonlyMode: true});
		
		$('#hostGroupDatagrid').datagrid({
			toolbar: [{
				text:'添加牵头单位',
				iconCls: 'icon-add',
				handler: function(){
					chooseHostGroup();
				}
			},'-',{
				text:'删除单位',
				iconCls: 'icon-remove',
				handler: function(){alert('help')}
			}]
		});
	});
	
	function upload() {
		$('#uploadForm').form('submit', {
	    	url: ctx+"/taskInfo/uploadFile",
	        onSubmit: function () {
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
		        	taskInfo_dialog.dialog("refresh",ctx+"/taskInfo/toMain?id="+json.data.toString());
		        } 
		        $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
		    }
	    });
	}
	
	var group_datagrid, group_dialog;
	function chooseHostGroup() {
		group_dialog = $('<div/>').dialog({
	    	title : "选择候选单位",
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
							name: field.name,
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
<form id="taskInfo_form" method="post">
	<input type="hidden" id="taskInfoId" name="id" value="${taskInfo.id }">
	<input type="hidden" name="createUserId" value="${taskInfo.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${taskInfo.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${taskInfo.isDelete }">
    <input type="hidden" name="status" value="${taskInfo.status }">
    <input type="hidden" name="fileName" id="fileName" value = "${taskInfo.fileName }"> <!-- id="fileName"不能删 -->
	<input type="hidden" name="filePath" value = "${taskInfo.filePath }"> 
	<input type="hidden" name="uploadDate" value = "<fmt:formatDate value='${taskInfo.uploadDate }' type='both'/>">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">任务信息</td>
		</tr>
		<tr>
			<td class="text-right">任务标题:</td>
			<td colspan="3"><input name="title" class="easyui-textbox" data-options="prompt:'填写事项标题'" value="${taskInfo.title }" required="required" type="text" style="width: 50%"></td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td><input name="info" value="${taskInfo.info }" data-options="prompt:'任务简称'" class="easyui-textbox" required="required"></td>
			<td class="text-right">急缓程度:</td>
			<td><input id="urgency" name="urgency" value="${taskInfo.urgency }" data-options="prompt:'选择急缓程度'" class="easyui-combobox" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td><input name="taskNo" class="easyui-textbox" data-options="prompt:'填写文号'"  value="${taskInfo.taskNo }" required="required" type="text"></td>
			<td class="text-right">开始时间:</td>
			<td><input name="createTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.createTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">反馈周期:</td>
			<td><input id="feedback" name="feedbackCycle" class="easyui-combobox" data-options="prompt:'选择反馈周期'" value="${taskInfo.feedbackCycle }" required="required"></td>
			<td class="text-right">办结时限:</td>
			<td><input name="endTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.endTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true,rownumbers:true,border:true">
				    <thead>
						<tr>
							<th data-options="field:'name'" width="40%">牵头单位名称</th>
							<th data-options="field:'userNames'" width="50%">联系人</th>
						</tr>
				    </thead>
				</table>
				<!-- <button type="button" class="btn btn-primary btn-xs" onclick="chooseHostGroup();">添加</button> -->
				<input id="hostGroupId" name="hostGroup" value = "${taskInfo.hostGroup }" type="hidden"/>
			</td>
		</tr>
		<%-- <tr>
			<td colspan="4">事项内容:<textarea class="easyui-kindeditor" name="taskContent" rows="3" >${taskInfo.taskContent }</textarea></td>
		</tr>
		<tr>
			<td colspan="4">领导批示:<textarea class="easyui-kindeditor" name="leadComments" rows="3" >${taskInfo.leadComments }</textarea></td>
		</tr>  --%>
		<tr>
			<td class="text-right">责任单位:</td>
			<td colspan="3">
				<textarea name="assistantGroup" rows="3" cols="80" style="width: 100%">${taskInfo.assistantGroup }</textarea>
			</td>
		</tr>
	</table>
</form>

<c:if test="${!empty refuseReasonList }">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-warning">
	  		<td colspan="4" align="center">拒绝原因</td>
	  	</tr>
		<c:forEach items="${refuseReasonList }" var="r">
			<tr>
				<td class="text-right">拒绝人:</td>
				<td>${r.createUser.name }</td>
				<td class="text-right">拒绝时间:</td>
				<td><fmt:formatDate value="${r.createDate }" type="both"/></td>
			</tr>
			<tr>
				<td class="text-right">拒绝原因:</td>
				<td colspan="3"><textarea class="easyui-kindeditor" id="reason" rows="3" >${r.reason }</textarea></td>
			</tr>
		</c:forEach>
	</table>
</c:if>
</div>
