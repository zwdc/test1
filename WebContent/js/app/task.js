/**
 * 任务管理（待办任务、已完成任务）
 */

var todoTask_datagrid;
var endTask_datagrid;
var task_form;

var task_dialog;

//委派
var delegate_dialog;
var user_dialog;

//转办
var transfer_dialog;

$(function() {
	showToDoTask();
});

//修正宽高
function fixHeight(percent)   
{   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent)   
{   
	return parseInt(($(this).width() - 80) * percent);
}

//待办任务列表
function showToDoTask(map){
	var _url;
	if(map==null){
		_url=ctx+"/process/todoTask";
	}else{
		_url=ctx+"/process/todoTask?parameterMap="+map;
	}
	todoTask_datagrid = $("#todoTask").datagrid({
        url: _url,
        width : 'auto',
		height :  fixHeight(0.8),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap:false,
		columns : [ 
		    [ 
				{field : 'assign',title : '任务状态',width : fixWidth(0.05),align : 'center',
					formatter:function(value, row){
						if(value == null){
							return "待签收";
						}else{
							return "待办理";  
						}
					}
				},
                {field : 'taskTitle',title : '任务内容',width : fixWidth(0.35),align : 'center'},
                {field : 'taskInfoType',title : '任务类型',width : fixWidth(0.1),align : 'center'},
                {field : 'applyUserName',title : '申请人',width : fixWidth(0.06),align : 'center'},                
                {field : 'title',title : '任务描述',width : fixWidth(0.18),align : 'center'},
                {field : 'taskName',title : '当前节点',width : fixWidth(0.1),align : 'center',
                	formatter:function(value, row){
                		return "<a class='trace' onclick=\"graphTrace('"+row.processInstanceId+"')\" id='diagram' href='javascript:void(0)' title='see'>"+value+"</a>";
                	}
                },
                {field : 'owner',title : '执行人',width : fixWidth(0.07),align : 'center',
                	formatter:function(value, row){
                		if(value != null && value != row.assign){
                			return row.assign+" (原执行人："+value+")";
                		}else{
                			return row.assign;
                		}
					}
                },  
                {field : 'createDate',title : '任务创建时间',width : fixWidth(0.1),align : 'center',
					formatter:function(value,row){
						return moment(value).format("YYYY-MM-DD HH:mm:ss");
					}
                },
                {field : 'suspended',title : '流程状态',width : fixWidth(0.07),align : 'center',
                	formatter:function(value, row){
                		if(value){
                			return "已挂起";
                		}else{
                			return "正常";  
                		}
                	}
                }
            ] 
		],
		toolbar: "#toolbar"
	});
}


//已完成的任务列表
/*function showEndTask(){
	endTask_datagrid = $("#endTask").datagrid({
        url: ctx+"/process/endTask",
        width : 'auto',
		height :  $(this).height()-135,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		columns : [ 
		    [ 
                {field : 'title',title : '标题',width : fixWidth(0.2),align : 'center'},
                {field : 'startTime',title : '任务开始时间',width : fixWidth(0.1),align : 'center',
					formatter:function(value,row){
						return moment(value).format("YYYY-MM-DD HH:mm:ss");
					}
                },
                {field : 'claimTime',title : '任务签收时间',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		if(value != null){
                			return moment(value).format("YYYY-MM-DD HH:mm:ss");
                		}else{
                			return "无需签收"
                		}
                	}
                },
                {field : 'endTime',title : '任务结束时间',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		return moment(value).format("YYYY-MM-DD HH:mm:ss");
                	}
                },
                {field : 'deleteReason',title : '流程结束原因',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		*//** The reason why this task was deleted {'completed' | 'deleted' | any other user defined string }. *//*
                		//本系统中涉及到jump、revoke（跳转、回退）
                		return value;
                	}
                },
                {field : 'version',title : '流程版本号',width : fixWidth(0.1),align : 'center'},
                {field : 'revoke',title : '操作',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		return "<a href='javascript:void(0);' onclick=\"revoke('"+row.taskId+"','"+row.processInstanceId+"')\">撤回</a>";
                	}
                }
                
            ] 
		]
	});
	
}*/

//初始化审批表单
/*function formInit() {
	task_form = $('#form').form({
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
            	audit_dialog.dialog('destroy');//销毁对话框
            	todoTask_datagrid.datagrid('reload');//重新加载列表数据
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}*/

function submit(status) {
	$('#form').form('submit', {
		onSubmit: function (param) {
            $.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
	    	param.isPass = status;	//提交的参数
        },
        success: function (result) {
            $.messager.progress('close');
            var json = $.parseJSON(result);
            if (json.status) {
            	task_dialog.dialog('destroy');//销毁对话框
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

function closeDialog() {
	task_dialog.dialog('destroy');
}

//办理
function handleTask() {
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再处理任务！");
    	} else {
    		task_dialog = $('<div/>').dialog({
    			title : "任务信息",
    			href: ctx+row.url,
    			top: 20,
    			width : fixWidth(0.8),
    			height : 'auto',
    			modal: true,
    			minimizable: true,
    			maximizable: true,
    			onLoad: function () {
    				$("#taskId").val(row.taskId);	//根据taskId完成任务
    			},
	            onClose: function () {
	            	task_dialog.dialog('destroy');
	            }
    		});
        }
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//签收
function claimTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign != null){
    		$.messager.alert("提示", "您已经签收此任务，根据任务状态进行【办理】任务！");
    	}else{
    		$.ajax({
    			type: "POST",
    			url: ctx+"/process/claim/"+row.taskId,
    			data: {},
    			success: function (data) {
    				$.messager.progress("close");
    				if (data.status) {
    					todoTask_datagrid.datagrid("reload"); 
    				} 
    				$.messager.show({
    					title : data.title,
    					msg : data.message,
    					timeout : 1000 * 2
    				});
    			},
    			beforeSend:function(){
    				$.messager.progress({
    					title: '提示信息！',
    					text: '数据处理中，请稍后....'
    				});
    			},
    			complete: function(){
    				$.messager.progress("close");
    			}
    		});
    	}
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//选择委派人窗口
/*function chooseUser(){
	//弹出对话窗口
	user_dialog = $('<div/>').dialog({
    	title : "选择任务委派人",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/user/toChooseDelegateUser",
        onClose: function () {
        	user_dialog.dialog('destroy');
        }
    });
}
*/

//根据groupId显示人员列表的标签--delegate_user.jsp
/*function addTab(title, groupId){
	if ($('#userTabs').tabs('exists', title)){
		$('#userTabs').tabs('select', title);
	} else {
		var url = ctx+"/userAction/toShowDelegateUser?groupId="+groupId;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		$('#userTabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}

//取消选择--delegate_user.jsp
function destroy_chooseUser(){
	$("#userName").val("");
	$("#userId").val("");
	user_dialog.dialog('destroy');
}

//选择人时，同时也对父页面赋值了。所以，确认键就只是关闭页面--delegate_user.jsp
function set_chooseUser(){
	user_dialog.dialog('destroy');
}*/

//委派任务小窗口
function delegateTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再处理任务！");
    	}else{
    		delegate_dialog = $('#task').dialog({
    			title : "委派任务",
    			top: 20,
    			width : 300,
    			height : 150,
    			closed: false,
    			cache: false,
    			modal: true,
    			buttons: [
    			          {
    			        	  text: '确认',
    			        	  iconCls: 'icon-ok',
    			        	  handler: function () {
    			        		  var userName = $("#user_name").textbox("getValue");
    			        		  var userId = $("#user_id").val();
    			        		  if(userName == ""){
    			        			  $.messager.alert("提示", "您未选择任何委派人，不能确认！"); 
    			        			  return false;
    			        		  }else if(userName == row.assign){
    			        			  $.messager.alert("提示", "不能将此任务委派为自己，请重新选择委派人！"); 
    			        			  return false;
    			        		  }
    			        		  
    			        		  $.ajax({
			        				  type: "POST",
			        				  url: ctx+"/process/delegateTask",
			        				  data: {taskId: row.taskId, userId : userId},
			        				  success: function (result) {
			        					  $.messager.progress("close");
			        					  if (result.status) {
			        						  $("#user_name").textbox("setValue", "");
			        						  $("#user_id").val("");
			        						  delegate_dialog.dialog('close');
			        						  todoTask_datagrid.datagrid("reload"); //reload the task data
			        					  } 
			        					  $.messager.show({
			        						  title : result.title,
			        						  msg : result.message,
			        						  timeout : 1000 * 2
			        					  });
			        				  },
			        				  beforeSend:function(){
			        					  $.messager.progress({
			        						  title: '提示信息！',
			        						  text: '数据处理中，请稍后....'
			        					  });
			        				  },
			        				  complete: function(){
			        					  $.messager.progress("close");
			        				  }
			        			  });
    			        		  
    			        	  }
    			          },
    			          {
    			        	  text: '关闭',
    			        	  iconCls: 'icon-cancel',
    			        	  handler: function () {
    			        		  $("#user_name").textbox("setValue", "");
    			        		  $("#user_id").val("");
    			        		  delegate_dialog.dialog('close');
    			        	  }
    			          }
    			          ]
    		});
    	}
    }else{
    	 $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//转办任务小窗口
function transferTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再处理任务！");
    	}else{
    		transfer_dialog = $('#task').dialog({
    			title : "转办任务",
    			top: 20,
    			width : 300,
    			height : 150,
    			closed: false,
    			cache: false,
    			modal: true,
    			buttons: [
			          {
			        	  text: '确认',
			        	  iconCls: 'icon-ok',
			        	  handler: function () {
			        		  var userName = $("#user_name").textbox("getValue");;
			        		  var userId = $("#user_id").val();
			        		  if(userName == ""){
			        			  $.messager.alert("提示", "您未选择任何转办人，不能确认！"); 
			        			  return false;
			        		  }else if(userName == row.assign){
			        			  $.messager.alert("提示", "不能将此任务转办给自己，请重新选择转办人！"); 
			        			  return false;
			        		  }
			        		  
			        		  $.ajax({
		        				  type: "POST",
		        				  url: ctx+"/process/transferTask",
		        				  data: {taskId: row.taskId, userId : userId},
		        				  success: function (result) {
		        					  $.messager.progress("close");
		        					  if (result.status) {
		        						  $("#user_name").textbox("setValue", "");
		        						  $("#user_id").val("");
		        						  transfer_dialog.dialog('close');
		        						  todoTask_datagrid.datagrid("reload"); //reload the process data
		        					  } 
		        					  $.messager.show({
		        						  title : result.title,
		        						  msg : result.message,
		        						  timeout : 1000 * 2
		        					  });
		        				  },
		        				  beforeSend:function(){
		        					  $.messager.progress({
		        						  title: '提示信息！',
		        						  text: '数据处理中，请稍后....'
		        					  });
		        				  },
		        				  complete: function(){
		        					  $.messager.progress("close");
		        				  }
		        			  });
			        		  
			        	  }
			          },
			          {
			        	  text: '关闭',
			        	  iconCls: 'icon-cancel',
			        	  handler: function () {
			        		  $("#user_name").textbox("setValue", "");
			        		  $("#user_id").val("");
			        		  transfer_dialog.dialog('close');
			        	  }
			          }
		          ]
    		});
    	}
    }else{
    	 $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}


//撤回已办任务到代办任务
function revoke(taskId, processInstanceId){
	$.ajax({
		type: "POST",
		url: ctx+"/process/revoke",
		data: {taskId: taskId, processInstanceId: processInstanceId},
		success: function (result) {
			$.messager.progress("close");
			if (result.status) {
				endTask_datagrid.datagrid("reload"); 
			} 
			$.messager.show({
				title : result.title,
				msg : result.message,
				timeout : 1000 * 2
			});
		},
		beforeSend:function(){
			$.messager.progress({
				title: '提示信息！',
				text: '数据处理中，请稍后....'
			});
		},
		complete: function(){
			$.messager.progress("close");
		}
	});
}

function jumpTask1(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再处理任务！");
    	}else{
    		$.ajax({
    			type: "POST",
    			url: ctx+"/process/process/jumpTask",
    			data: {taskId : row.taskId, taskDefinitionKey : "bossAudit"},
    			success: function (result) {
    				$.messager.progress("close");
    				if (result.status) {
    					todoTask_datagrid.datagrid("reload"); //reload the process data
    				} 
    				$.messager.show({
    					title : result.title,
    					msg : result.message,
    					timeout : 1000 * 2
    				});
    			},
    			beforeSend:function(){
    				$.messager.progress({
    					title: '提示信息！',
    					text: '数据处理中，请稍后....'
    				});
    			},
    			complete: function(){
    				$.messager.progress("close");
    			}
    		});
    	}
    }else{
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//查询点击按钮
function selectMap(){
	 var projectName=$('#projectName').val();
	 var title=$('#title').val();
	 var businessForm=$('#businessForm').combobox('getValue');
	 var str="{\"projectName\":\""+projectName+"\",\"title\":\""+title+"\",\"businessForm\":\""+businessForm+"\"}";
     var obj = eval('('+str+')');
	 todoTask_datagrid.datagrid('reload',obj);
}