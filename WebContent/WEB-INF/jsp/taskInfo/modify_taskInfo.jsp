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
		
		$('#createTaskDate').datetimebox({
			onSelect: function(startDate){
				//根据开始时间，限制结束时间的范围，不能小于开始时间。
				$('#endTaskDate').datetimebox().datetimebox('calendar').calendar({
					validator: function(endDate){
						return endDate >= startDate;
					},
					onSelect: function(endDate){
						//根据开始时间和结束时限，限制签收时间。
						$('#claimLimitDate').datetimebox().datetimebox('calendar').calendar({
							validator: function(date){
								return startDate <= date && date <= endDate;
							}
						});
					}
				});
				
			}
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
			url: ctx+'/taskSource/getList',
			method: 'post',
			pagination:true,
			rownumbers:true,
			fitColumns: true,
			striped:true,
			columns: [[
				{field: 'name', title: '来源名称', width: 200, align: 'left', halign: 'center', sortable: true},
	            {field: 'sourceDate', title: '来源时间', width: 100, align: 'center', sortable: true,
	            	  formatter: function(value, row) {
	            		 return moment(value).format("YYYY-MM-DD HH:mm:ss");
	            	  }  
	            },
	            {field: 'taskTypeName', title: '任务类型', width: 100, align: 'center'}
			]],
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
			            //重新设置hostGroupId
			            var groupIds = "";
			            var rows = hostGroup.datagrid('getRows');
			            if(rows.length > 0) {
			            	for(var j = 0; j < rows.length; j++){
			            		var row = rows[j];
			            		groupIds += row.groupId + ',';
			            	}
			            	$("#hostGroupId").val(groupIds.substring(0, groupIds.length-1));
			            } else {
			            	$("#hostGroupId").val("");
			            }
				    } else {
				    	$.messager.alert("提示", "您未勾选任何操作对象，请勾选一行数据！");
				    }
				}
			}]
		});
	});
	
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
		$('#hostGroupDatagrid').datagrid('loadData', { total: 0, rows: [] }); //清空已生成的牵头单位
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
	
	//暂存
	function saveTemporary() {
		$('#taskInfo_form').form('submit', {
	    	url: ctx+"/taskInfo/saveOrUpdate",
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
		        	task_dialog.dialog("refresh",ctx+"/taskInfo/toModify?id="+json.data);
		        } 
		        $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
		    }
	    });
	}
	
	//完成任务
	function completeTask() {
		$('#taskInfo_form').form('submit', {
	    	url: ctx+"/taskInfo/completeTask",
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
	
	//重置表单
	function reloadForm() {
		$('#taskInfo_form').form('clear');
	}
	
	//关闭dialog
	function closeDialog() {
		task_dialog.dialog('destroy');
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
	<!-- <input type="hidden" id="processTaskId" name="processTaskId"> -->	<!-- 通过此id暂存时修改processTask的url -->
	<input type="hidden" id="taskId" name="taskId">
	<input type="hidden" id="taskInfoId" name="id" value="${taskInfo.id }">
	<input type="hidden" name="createUser.id" value="${taskInfo.createUser.id }">
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${taskInfo.createDate }' type='both'/>">
    <input type="hidden" name="isDelete" value="${taskInfo.isDelete }">
    <input type="hidden" name="status" value="${taskInfo.status }">
	<table class="table table-bordered table-hover table-condensed">
		<tr class="bg-primary">
			<td colspan="4" align="center">修改任务信息</td>
		</tr>
		<tr>
			<td class="text-right">任务内容:</td>
			<td colspan="3"><input name="title" class="easyui-textbox" data-options="prompt:'填写任务内容'" value="${taskInfo.title }" required="required" type="text" style="width: 50%"></td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><input id="createTaskDate" name="createTaskDate" class="easyui-datetimebox" data-options="prompt:'选择开始时间',editable:false" value="${taskInfo.createTaskDate }" required="required"></td>
			<td class="text-right">急缓程度:</td>
			<td><input id="urgency" name="urgency" value="${taskInfo.urgency }" data-options="prompt:'选择急缓程度'" class="easyui-combobox" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td><input id="taskSource" name="taskSource.id" class="easyui-combogrid" data-options="prompt:'选择来源'"  value="${taskInfo.taskSource.id }" required="required"></td>
			<td class="text-right">反馈频度:</td>
			<td><input id="fbFrequency" name="fbFrequency.id" class="easyui-combobox" data-options="prompt:'选择反馈频度'" value="${taskInfo.fbFrequency.id }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><input id="claimLimitDate" name="claimLimitDate" class="easyui-datetimebox" data-options="prompt:'选择签收时限',editable:false" value="${taskInfo.claimLimitDate }" required="required"></td>
			<td class="text-right">办结时限:</td>
			<td><input id="endTaskDate" name="endTaskDate" class="easyui-datetimebox" data-options="prompt:'选择办结时间',editable:false" value="${taskInfo.endTaskDate }" required="required"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',align:'center',fitColumns:true,rownumbers:true,border:true,singleSelect:true">
				    <thead>
						<tr>
							<th data-options="field:'groupName'" align='center' width="25%">牵头单位名称</th>
							<th data-options="field:'userNames0'" align='center' width="15%">联系人A</th>
							<th data-options="field:'linkway0'" align='center' width="20%">联系方式</th>
							<th data-options="field:'userNames1'" align='center' width="15%">联系人B</th>
							<th data-options="field:'linkway1'" align='center' width="20%">联系方式</th>
						</tr>
				    </thead>
				</table>
				<!-- <button type="button" class="btn btn-primary btn-xs" onclick="chooseHostGroup();">添加</button> -->
				<input id="hostGroupId" name="hostGroup" value = "${taskInfo.hostGroup }" type="hidden" required="required"/>
			</td>
		</tr>
		<tr>
			<td class="text-right">责任单位:</td>
			<td colspan="3">
				<textarea name="assistantGroup" rows="3" cols="80" style="width: 100%">${taskInfo.assistantGroup }</textarea>
			</td>
		</tr>
	</table>
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveTemporary();" data-options="iconCls:'icon-save'">暂存</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="completeTask();" data-options="iconCls:'icon-ok'">提交任务</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="reloadForm();" data-options="iconCls:'icon-reload'">重置</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="closeDialog();" data-options="iconCls:'icon-cancel'">关闭</a>
</div>
</div>
