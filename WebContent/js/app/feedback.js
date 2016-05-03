/**
 * 反馈记录
 */
var feedback_datagrid;
var feedback_form;
var feedback_dialog;
//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row,url) {
	feedback_form = $('#feedback_form').form({
        url: url,
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
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	feedback_dialog.dialog("refresh",ctx+"/feedback/toModify?id="+json.data);
	        	feedback_datagrid.datagrid('load');
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

//反馈审核—— 督查人员
function check(){
  var row = feedback_datagrid.datagrid('getSelected');
  if (row) {
  	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息审核",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=check&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/checkFeedback");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
  } else {
      $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
  }
}
//实施反馈—— 承办单位
function feedback(){
  var row = feedback_datagrid.datagrid('getSelected');
  var flag=false;
  if(row){
  	$.ajax({
  		async:false,
  		cache:false,
  		url:ctx+'/feedback/checkFeedbackDate/'+row.id,
  		type:'post',
  		dataType:'json',
  		success:function(data){
  			if(data.status){
  				flag=true;
  			}else{
  				$.messager.show({
  					title:data.title,
  					msg:data.message,
  					timeout:1000*2
  				});
  			}
  		}
  	});
  }else {
      $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
  }
  if (flag) {
  	feedback_dialog = $('<div/>').dialog({
	    	title : "进行反馈",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=feedback&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveFeedback");
	        },
	        buttons: [
	            {
	                text: '暂存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	saveTemporary();
	                }
	            },
	            {
	            	text: '申请审核',
	            	iconCls: 'icon-ok',
	            	id: 'ok',
	            	handler: function () {
	                	$.messager.confirm('确认提示！','确认提交表单进入反馈审核流程吗？',function(result){
	                		if(result){
	                			feedback_form.form('submit',{
	    	            		 	url: ctx+"/feedback/callApproval",
	    	            	        onSubmit: function () {
	    	            		        $.messager.progress({
	    	            		            title: '提示信息！',
	    	            		            text: '数据处理中，请稍后....'
	    	            		        });
	    	            		        var isValid = $(this).form('validate');
	    	            		        if (!isValid) {
	    	            		            $.messager.progress('close');
	    	            		        } else {
	    	            		        	$("#save").linkbutton("disable");
	    	            		        	$("#ok").linkbutton("disable");
	    	            		        }
	    	            		        return isValid;
	    	            		    },
	    	            		    success: function (data) {
	    	            	            $.messager.progress('close');
	    	            	            var json = $.parseJSON(data);
	    	            	            if (json.status) {
	    	            	            	feedback_dialog.dialog('destroy');//销毁对话框
	    	            	            	feedback_datagrid.datagrid('reload');//重新加载列表数据
	    	            	            	$("#feedback_datagrid")datagrid('reload');
	    	            	            } 
	    	            	            $.messager.show({
	    	            					title : json.title,
	    	            					msg : json.message,
	    	            					timeout : 1000 * 2
	    	            				});
	    	            	        }
	    	            	    });
	                		}
	                	});
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
  } 
}
//查看反馈详情
function detailsFeedback(){
  var row = feedback_datagrid.datagrid('getSelected');
  if (row) {
  	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈记录详情",
			top: 20,
			width : fixWidth(0.8),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=detail&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/detail");
	        },
	        buttons: [
	  	          {
	  	                text: '关闭',
	  	                iconCls: 'icon-cancel',
	  	                handler: function () {
	  	                	feedback_dialog.dialog('destroy');
	  	                }
	  	            }
	  	        ],
	        onClose: function () {
	        	source_dialog.dialog('destroy');
	        }
	    });
  } else {
      $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
  }
}
//暂存
function saveTemporary() {
	$('#feedback_form').form('submit', {
    	url: ctx+"/feedback/saveFeedback",
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
	        	$("#feedback_datagrid")datagrid('reload');
	        	feedback_dialog.dialog("refresh",ctx+"/feedback/toMain?action=feedback&id="+json.data);
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
	$('#feedback_form').form('submit', {
    	url: ctx+"/feedback/completeTask",
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
	$('#feedback_form').form('clear');
}

//关闭dialog
function closeDialog() {
	task_dialog.dialog('destroy');
}

//编辑
function editFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息编辑",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=edit&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveOrUpdate");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//添加
function addFeedback(){
	feedback_dialog = $('<div/>').dialog({
    	title : "反馈信息添加",
		top: 20,
		width : fixWidth(0.9),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/feedback/toMain?action=add",
        onLoad: function () {
            formInit(null,ctx+"/feedback/saveOrUpdate");
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	feedback_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	feedback_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	feedback_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	feedback_dialog.dialog('destroy');
        }
    });
}


//删除
function delFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/feedback/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	feedback_datagrid.datagrid('load');
                        }
                        $.messager.show({
        					title : data.title,
        					msg : data.message,
        					timeout : 1000 * 2
        				});
                    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}