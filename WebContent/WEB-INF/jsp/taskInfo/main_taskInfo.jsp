<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/app/choose/group/choose_group.js?_=${sysInitTime}"></script>
<script type="text/javascript" src="${ctx}/js/app/choose/user/user.js?_=${sysInitTime}"></script>
<script type="text/javascript">
	$(function() {
		$.getJSON(ctx+"/js/app/app.json",function(data){
			$('#urgency').combobox({
				valueField: "id",
			    textField: "name",
			    data: data[0].urgencyType
			});
		});
		
		//反馈频度
		$('#fbFrequency').combobox({
			url:ctx+"/feedbackFrequency/getAllList",
			valueField: "id",
		    textField: "name"
		});
		
		//反馈来源
		var taskSourceGrid=$("#taskSource").combogrid({
		 	panelWidth: 500,
			idField: 'id',
			textField: 'name',
			url: ctx+'/taskSource/getAllList',
			method: 'post',
			pagination:true,
			rownumbers:true,
			fitColumns: true,
			striped:true,
			columns: [[
				{field: 'name', title: '来源名称', width: fixWidth(0.1), align: 'center', sortable: true},
	            {field: 'sourceDate', title: '来源时间', width: fixWidth(0.1), align: 'center', sortable: true,
	            	  formatter: function(value, row) {
	            		  moment(value).format("YYYY-MM-DD HH:mm:ss");
	            	  }  
	            },
	            {field: 'taskType', title: '任务类型', width: fixWidth(0.3), align: 'left',halign: 'center'}
			]],
			onSelect: function(index, row) {
				$("#bankName").textbox("setValue", row.bank);
				$("#telephone").textbox("setValue",row.companyPhone);
				$("#bankAccount").textbox("setValue",row.bankAccount);
				$("#mailingAddress").textbox("setValue",row.address);
			},
	        toolbar: "#combo_toolbar"
 		});
 		 
 		$("#combo_paramBox").searchbox({ 
 			menu:"#combo_searchMenu", 
 			prompt :'模糊查询',
 		    searcher:function(value, name){   
 		    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
 	            var obj = eval('('+str+')');
 	           taskSourceGrid.combogrid('grid').datagrid('reload',obj); 
 		    }
 		});
		
		
		$("#reason").kindeditor({readonlyMode: true});
		
		var hostGroup = $('#hostGroupDatagrid').datagrid({
			toolbar: [{
				text:'添加牵头单位',
				iconCls: 'icon-add',
				handler: function(){
					chooseHostGroup();
				}
			},'-',{
				text:'删除单位',
				iconCls: 'icon-remove',
				handler: function(){
					debugger;
					var allCheckedRows = hostGroup.datagrid('getChecked');
				    if (allCheckedRows.length > 0) {
			            var checkedRowLength = allCheckedRows.length;
			            for (var i = 0; i < checkedRowLength; i++) {
			                var checkedRow = allCheckedRows[i];
			                var checkedRowIndex = hostGroup.datagrid('getRowIndex', checkedRow);
			                hostGroup.datagrid('deleteRow', checkedRowIndex);
			            }
				    	
				    } else {
				    	$.messager.alert("提示", "您未勾选任何操作对象，请勾选一行数据！");
				    }
				}
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
<div id="combo_toolbar" style="padding:2px 0; display: none;">
	<table>
		<tr>
			<td style="padding-left:5px">
				<input id="combo_paramBox" style="width: 200px" class="easyui-searchbox" type="text"/>
			</td>
		</tr>
	</table>
</div>
<div id="combo_searchMenu" style="display: none;">
	<div data-options="name:'name'">来源名称</div>
	<div data-options="name:'code'">来源时间</div>
</div>
<div class="easyui-layout">
<form id="taskInfo_form" method="post">
	<input type="hidden" id="taskInfoId" name="id" value="${taskInfo.id }">
	<input type="hidden" name="createUserId" value="${taskInfo.createUserId }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${taskInfo.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${taskInfo.isDelete }">
    <input type="hidden" name="status" value="${taskInfo.status }">
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
			<td><input id="taskSource" name="taskSource.id" class="easyui-combogrid" data-options="prompt:'选择来源'"  value="${taskInfo.taskSource.id }" required="required"></td>
			<td class="text-right">开始时间:</td>
			<td><input name="createTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.createTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">反馈频度:</td>
			<td><input id="fbFrequency" name="fbFrequency.id" class="easyui-combobox" data-options="prompt:'选择反馈频度'" value="${taskInfo.fbFrequency.id }" required="required"></td>
			<td class="text-right">办结时限:</td>
			<td><input name="endTaskDate" class="easyui-datetimebox" data-options="prompt:'选择立项时间',editable:false" value="${taskInfo.endTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="fitColumns:true,rownumbers:true,border:true">
				    <thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
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
