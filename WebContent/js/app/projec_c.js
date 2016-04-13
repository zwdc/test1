/**
 * 办理人 待签收列表
 */
var project_datagrid;
var project_form;
var project_dialog;
$(function(){
	project_datagrid=$('#project_datagrid').datagrid({
		url:ctx+"/project/getList?type=1",	
		width:'auto',					
		height:fixHeight(1),			
		pagination:true,				
		rownumbers:true,				
		border:false,					
		singleSelect:true,				
		striped:true,					
		columns:[
		    [
		     	{field:'title',title:'任务标题',width:fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'info',title:'任务简称',width:fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'createTaskDate',title:'开始时间',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
					 }
		     	},
		     	{field:'endTaskDate',title:'结束时限',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
		     			return moment(value).format("YYYY-MM-DD HH:mm:ss");
		     		}
		     	},
		     	{field:'fbFrequencyName',title:'反馈频度',width:fixWidth(0.1),align:'center'},
		     	{field:'taskSourceName',title:'任务来源',width:fixWidth(0.1),align:'center'},
		     	{field:'urgency',title:'急缓程度',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
	            		  switch (value) {
	            		  	case 0: return "特提";
	            		  	case 1: return "特急";
	            		  	case 2: return "加急";
	            		  	case 3: return "平急";
	            		  } 
					 }
		     	},
		     	{field: 'status',title: '状态',width:fixWidth(0.1),align:'center', halign:'center',sortable:true,
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
							case "APPROVAL_SUCCESS":
								return "<span class='text-success'>审批通过</span>";
							case "APPROVAL_FAILED":
								return "<span class='text-danger'>审批失败</span>";
							case "WAITING_FOR_APPROVAL":
								return "<span class='text-warning'>待申请审批</span>";
							case "PENDING":
								return "<span class='text-primary'>审批中</span>";
							case "REAPPROVAL":
								return "<span class='text-danger'>需要重新审批</span>";
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
			project_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function searchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function gradeSearch() {
	jqueryUtil.gradeSearch(project_datagrid, "#invoiceSearch", "/invoice/invoiceSearch");
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
	 project_form = $('#project_form').form({
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
	            	project_dialog.dialog("refresh",ctx+"/taskInfo/toMain?id="+json.data);
	            	project_datagrid.datagrid('reload');//重新加载列表数据
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
	project_dialog = $('<div/>').dialog({
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
                	project_form.submit();
                }
            },
            {
            	text: '申请审批',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
                	$.messager.confirm('确认提示！','确认提交表单进入任务办理流程吗？',function(result){
                		if(result){
                			project_form.form('submit',{
    	            		 	url: ctx+"/taskInfo/approvalTask",
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
    	            	            	project_dialog.dialog('destroy');//销毁对话框
    	            	            	project_datagrid.datagrid('reload');//重新加载列表数据
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
                	project_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	project_dialog.dialog('destroy');
                	project_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
        	project_dialog.dialog('destroy');
        	project_datagrid.datagrid('reload');
        }
    });
}
//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
        showTaskInfo(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function del() {
    var row = project_datagrid.datagrid('getSelected');
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
                        	project_datagrid.datagrid('load');
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
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row) {
    //弹出对话窗口
	project_dialog = $('<div/>').dialog({
    	title : "任务详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/taskInfo/details/"+row.id,
        buttons: "#task_btn",
        onClose: function () {
        	project_dialog.dialog('destroy');
        }
    });
}

/*buttons: [
          {
              text: '关闭',
              iconCls: 'icon-cancel',
              handler: function () {
              	project_dialog.dialog('destroy');
              }
          }
      ],*/

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