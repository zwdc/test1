/**
 * 开票申请
 */
var invoice_datagrid;
var invoice_form;
var invoice_dialog;
$(function(){
	invoice_datagrid=$('#invoice_datagrid').datagrid({
		url:ctx+"/invoice/getInvoiceList",			//路径访问后台方法获取数据
		width:'auto',					//宽度自适应
		height:fixHeight(0.89),			//高度自适应
		pagination:true,				//显示底部分页栏
		rownumbers:true,				//显示行号
		border:false,					//边框
		singleSelect:true,				//只允许选中一行
		striped:true,					//隔行变色
		//列的集合
		columns:[
		    [
		     	{field:'projectName',title:'项目名称',width : fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'makeInvoiceName',title:'开票单位名称',width : fixWidth(0.09),align:'center',halign:'center'},
		     	{field:'customerName',title:'客户名称',width : fixWidth(0.17),align:'center',halign:'center'},
		     	{field:'totalPrice',title:'总价',width : fixWidth(0.08),align:'right',halign:'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
		     	},
		     	{field:'invoiceType',title:'发票类型',width : fixWidth(0.05),align:'center',halign:'center'},
		     	{field:'currentPrice',title:'开票金额',width : fixWidth(0.08),align:'right',halign:'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
		     	},
		     	{field:'apply_date',title:'申请时间',width : fixWidth(0.05),align:'center',halign:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD");
					 }
		     	},
		     	{field: 'status',title: '状态',width: fixWidth(0.05),align: 'center', halign: 'center',sortable: true,
	            	  formatter:function(value, row){
	            		  switch (value) {
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
	              },
	              {field : 'processInstanceId',title : '当前节点',width : fixWidth(0.1),align : 'center',
	                	formatter:function(value, row){
	                		if(row.status == "WAITING_FOR_APPROVAL") {
	                			return "-";
	                		} else {
	                			return "<a class='trace' onclick=\"graphTrace('"+value+"')\" id='diagram' href='#' title='点击查看'>"+row.taskName+"</a>";
	                		}
	  				}
	              }
		    ]
		],
        onDblClickRow: function(index, row) {
        	showDetailsInvoice(row);
        },
		//页面项
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
			invoice_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function invoiceSearchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function  invoiceSearch() {
	jqueryUtil.gradeSearch(invoice_datagrid, "#invoiceSearch", "/invoice/invoiceSearch");
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
	 invoice_form = $('#invoice_form').form({
		 	url: ctx+"/invoice/saveOrUpdate",
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
	            	invoice_dialog.dialog("refresh",ctx+"/invoice/toMain?id="+json.data);
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
function showInvoice(row) {
	var _url = ctx+"/invoice/toMain";
	var op;
	if (row) {
		_url = ctx+"/invoice/toMain?id="+row.id;
		
	}
	invoice_dialog = $('<div/>').dialog({
    	title : "开票申请信息",
    	top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if($("#invoiceId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            if(row) {
            	if(row.status == "APPROVAL_SUCCESS") {
            		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
		        	$("#ok").linkbutton("disable");
		        	op = "reapproval";
		        	$('#save').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">保存后需重新审批</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "PENDING") {
            		//审批中，不允许修改或提交审批
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">审批中</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "APPROVAL_FAILED") {
            		//审批失败时，要在待办事项中去操作。
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">请在待办事项中处理</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "REAPPROVAL") {
            		$("#ok").linkbutton("enable");
            	}
            }
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	if(row) {
                    	if(row.status == "APPROVAL_SUCCESS") {
                    		$.messager.confirm('确认提示！', '此表单已经审批通过，您确定要保存后重新申请审批吗？', function (result) {
                    			 if (result) {
                    				 if(op == 'reapproval') {
                             			row.status = 'REAPPROVAL'
                             		 }
                    				 invoice_form.submit();
                    			 }
                    		});
                    	} else {
                    		invoice_form.submit();
                    	}
                    } else {
                    	invoice_form.submit();
                    }
                }
            },
            {
            	text: '提交审批',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
                	$.messager.confirm('确认提示！','确认提交表单进入审批流程吗？',function(result){
                		if(result){
                			invoice_form.form('submit',{
    	            		 	url: ctx+"/invoice/submitApprove",
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
    	            	            	invoice_dialog.dialog('destroy');//销毁对话框
    	            	            	invoice_datagrid.datagrid('reload');//重新加载列表数据
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
                	invoice_form.form('reset');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	invoice_dialog.dialog('destroy');
                	invoice_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
        	invoice_dialog.dialog('destroy');
        	invoice_datagrid.datagrid('reload');
        }
    });
}
//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = invoice_datagrid.datagrid('getSelected');
    if (row) {
        showInvoice(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//删除项目
function del() {

    var row = invoice_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/invoice/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	invoice_datagrid.datagrid('load');
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
//发票详情
function detailsInvoice(){
    var row = invoice_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsInvoice(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示发票详情窗口
function showDetailsInvoice(row) {
    //弹出对话窗口
	var id = row.id;
	invoice_dialog = $('<div/>').dialog({
    	title : "开票详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/invoice/toDetails/"+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	invoice_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	invoice_dialog.dialog('destroy');
        }
    });
}