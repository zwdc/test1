/**
 * 督察处发布任务
 */
var taskInfo_datagrid;
var taskInfo_form;
var taskInfo_dialog;
$(function(){
	taskInfo_datagrid=$('#taskInfo_datagrid').datagrid({
		url:ctx+"/taskInfo/getList",	//路径访问后台方法获取数据
		width:'auto',					//宽度自适应
		height:fixHeight(0.89),			//高度自适应
		pagination:true,				//显示底部分页栏
		rownumbers:true,				//显示行号
		border:false,					//边框
		singleSelect:true,				//只允许选中一行
		striped:true,					//隔行变色
		columns:[
		    [
		     	{field:'title',title:'标题',width:fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'taskNo',title:'文号',width:fixWidth(0.1),align:'center',halign:'center'},
		     	{field:'createTaskDate',title:'立项时间',width:fixWidth(0.1),align:'center',halign:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
					 }
		     	},
		     	{field:'feedbackCycle',title:'反馈周期',width:fixWidth(0.1),align:'center'},
		     	{field:'hostGroup',title:'主办单位',width:fixWidth(0.1),align:'center'},
		     	{field:'hostUser',title:'主办人',width:fixWidth(0.1),align:'center'},
		     	{field:'endTaskDate',title:'办结时限',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD");
					 }
		     	},
		     	{field: 'status',title: '状态',width:fixWidth(0.05),align:'center', halign:'center',sortable:true,
	            	  formatter:function(value, row){
	            		  switch (value) {
							case "IN_HANDLING":
								return "<span class='text-success'>办理中</span>";
							case "REFUSE_CLAIM":
								return "<span class='text-danger'>拒绝签收</span>";
							case "WAIT_FOR_CLAIM":
								return "<span class='text-warning'>待签收</span>";
							case "APPLY_FINISHED":
								return "<span class='text-primary'>申请办结</span>";
							case "FINISHED":
								return "<span class='text-muted'>已办结</span>";
							default:
								return "";
						  }
	    			  }
	              }
		    ]
		],
        onDblClickRow: function(index, row) {
        	showDetails(row);
        },
		toolbar:"#toolbar"
	});
	//搜索框
	$("#searchbox").searchbox({
		menu:"#searchMenu",			//搜索类型的菜单
		prompt:'模糊查询',			//显示在输入框里的信息
		//函数当用户点击搜索按钮时调用
		searcher:function(value,name){
			var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
			var obj=eval('('+str+')');
			taskInfo_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function searchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function gradeSearch() {
	jqueryUtil.gradeSearch(taskInfo_datagrid, "#invoiceSearch", "/invoice/invoiceSearch");
}

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}


//初始化表单
function formInit(row) {
	 taskInfo_form = $('#taskInfo_form').form({
		 	url: ctx+"/taskInfo/saveOrUpdate",
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
	            	taskInfo_dialog.dialog('destroy');//销毁对话框
	            	taskInfo_datagrid.datagrid('reload');//重新加载列表数据
	            } 
	            $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
	        }
	    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showTaskInfo(row) {
	var _url = ctx+"/taskInfo/toMain";
	if (row) {
		_url = ctx+"/taskInfo/toMain?id="+row.id;
		
	}
	taskInfo_dialog = $('<div/>').dialog({
    	title : "任务信息",
    	top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if($("#taskInfoId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
        },
        buttons: [
            {
                text: '暂存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	taskInfo_form.submit();
                }
            },
            {
            	text: '分配任务',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
                	$.messager.confirm('确认提示！','确认提交表单进入任务办理流程吗？',function(result){
                		if(result){
                			taskInfo_form.form('submit',{
    	            		 	url: ctx+"/taskInfo/assignTask",
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
    	            	            	taskInfo_dialog.dialog('destroy');//销毁对话框
    	            	            	taskInfo_datagrid.datagrid('reload');//重新加载列表数据
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
                	taskInfo_form.form('reset');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	taskInfo_dialog.dialog('destroy');
                	taskInfo_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
        	taskInfo_dialog.dialog('destroy');
        	taskInfo_datagrid.datagrid('reload');
        }
    });
}
//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = taskInfo_datagrid.datagrid('getSelected');
    if (row) {
        showTaskInfo(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function del() {
    var row = taskInfo_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/taskInfo/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	taskInfo_datagrid.datagrid('load');
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

function details(){
    var row = taskInfo_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row) {
    //弹出对话窗口
	taskInfo_dialog = $('<div/>').dialog({
    	title : "任务详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/taskInfo/details/"+row.id,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	taskInfo_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	taskInfo_dialog.dialog('destroy');
        }
    });
}

function publishMessage() {
	var goEasy = new GoEasy({
        appkey: '0cf326d6-621b-495a-991e-a7681bcccf6a'
    });
	goEasy.publish({
        channel: 'zwdc_user_1',
        message: '2',
        onSuccess:function(){
        	alert("消息发布成功。");
    	},
    	onFailed: function (error) {
    		alert("消息发送失败，错误编码："+error.code+" 错误信息："+error.content);
    	}
    });
}