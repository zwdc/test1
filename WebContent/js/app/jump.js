/**
 * 任务跳转
 * 此功能只适合，不同人员去干同一个事情的情况。例如：n个人去审批一个审批表，可以在这些人员中随意跳转。
 * 但不适用于，n个人去干m个任务，这样跳转的业务就会乱套，也不符合实际要求。
 * 页面上的此功能需要加权限过滤
 */

var jump_dialog;

//任务跳转（包括回退和向前）至指定活动节点
function jumpTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再处理任务！");
    	}else{
    		$('#targetTaskDefinitionKey').combobox({
			    url: ctx+"/bpmn/listUserTask?procDefKey="+row.processDefinitionKey,
			    valueField:'taskDefKey',
			    textField:'taskName',
			    panelWidth: 200,
				panelHeight: 'auto',
				panelHeight: 'auto',
				groupField: 'procDefName',
				formatter: function(row){
					var s = '<span style="font-weight:bold">' + row.taskName + '</span><br/>' +
							'<span style="color:#888"> TaskDefKey: ' + row.taskDefKey + '</span>';
					return s;
				}
			});
    		$("#currentTaskName").html(row.taskName);
    		jump_dialog = $('#jumpTask').dialog({
    			title : "任务跳转",
    			top: 20,
    			width : 300,
    			height : 160,
    			closed: false,
    			cache: false,
    			modal: true,
    			buttons: [
			          {
			        	  text: '确认',
			        	  iconCls: 'icon-ok',
			        	  handler: function () {
			        		  var targetTaskDefinitionKey = $('#targetTaskDefinitionKey').combobox('getValue');
			        		  if(targetTaskDefinitionKey == ""){
			        			  //$.messager.alert("提示", "您未选择任何跳转节点，不能确认！"); 
			        			  $('#targetTaskDefinitionKey').combobox().next('span').find('input').focus()
			        			  return false;
			        		  }
			        		  
			        		  $.ajax({
		        				  type: "POST",
		        				  url: ctx+"/process/jumpTask",
		        				  data: {taskId : row.taskId, taskDefinitionKey : targetTaskDefinitionKey},
		        				  success: function (data) {
		        					  $.messager.progress("close");
		        					  if (data.status) {
		        						  $("#targetTaskDefinitionKey").combobox("setValue", "");
		        						  jump_dialog.dialog('close');
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
			          },
			          {
			        	  text: '关闭',
			        	  iconCls: 'icon-cancel',
			        	  handler: function () {
			        		  //$("#jump").val("");
			        		  jump_dialog.dialog('close');
			        	  }
			          }
		          ]
    		});
    	}
    }else{
    	 $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}



