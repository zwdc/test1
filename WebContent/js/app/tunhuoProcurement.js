/**
 * 采购审批表
 */

//囤货
var pro_datagrid;
var pro_form;
var pro_dialog;

$(function() {
	//初始化囤货列表
	tunhuo();
	
	$('#tabs').tabs({
	    border:false,
	    onSelect:function(title, index){
			if(title == "囤货") {
				tunhuo();
			} else if(title == "补货") {
				buhuo();
			} else if(title == "采购计划") {
				caigoujihua();
			}
	    }
	});
	
});

//囤货
function tunhuo() {
	pro_datagrid = $('#pro_datagrid').datagrid({
        url: ctx+"/tunhuoProcurement/getList",
        width: 'auto',
		height: fixHeight(0.83),
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap: false,
        columns: [ 
            [ 
              {field: 'procurementNo',title: '采购合同号',width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
              {field: 'procurementPrice',title: '采购价格',width: fixWidth(0.1),align: 'right', halign: 'center',sortable: true,
            	  formatter:function(value, row){
            		  return jqueryUtil.formatNumber(value);
    			  }
              },
              {field: 'procurementCompany',title: '采购方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'salesCompany',title: '销售方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'status',title: '状态',width: fixWidth(0.2),align: 'center', halign: 'center',
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
						default:
							return "";
					}
    			  }
              }
    	    ] 
        ],
        view: detailview,
    	detailFormatter:function(index,row){
    		return '<div class="ddv" style="padding:5px 0"></div>';
    	},
    	onExpandRow: function(index, row){
    		var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
    		ddv.panel({
    			border: false,
    			cache: false,
    			href: ctx+'/tunhuoProcurementProduct/getProcurementProduct?procurementId='+row.procurementId,
    			onLoad: function(){
    				$('#pro_datagrid').datagrid('fixDetailRowHeight',index);
    			}
    		});
    		$('#pro_datagrid').datagrid('fixDetailRowHeight',index);
    	},
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'tunhuo');
        },
        toolbar: "#toolbar"
    });
	
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            pro_datagrid.datagrid('reload',obj); 
	    }
	});
}

//补货列表（补货采购审批表列表）
function buhuo() {
	buhuo_datagrid = $('#buhuo_datagrid').datagrid({
        url: ctx+"/buhuoProcurement/getList",
        width: 'auto',
		height: fixHeight(0.83),
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap: false,
		columns: [ 
            [ 
              {field: 'projectName',title: '项目名称',width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
              {field: 'salesNo',title: '销售合同号',width: fixWidth(0.1),align: 'center', sortable: true},
              {field: 'procurementNo',title: '采购合同号',width: fixWidth(0.1),align: 'center',sortable: true},
              {field: 'procurementPrice',title: '采购价格',width: fixWidth(0.1),align: 'right', halign: 'center',sortable: true,
                	formatter:function(value, row){
                		return jqueryUtil.formatNumber(value);
    				}
              },
              {field: 'procurementCompany',title: '采购方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'salesCompany',title: '销售方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'status',title: '状态',width: fixWidth(0.2),align: 'center', halign: 'center',
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
						default:
							return "";
					}
    			  }
              }
    	    ] 
        ],
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'buhuo');
        },
        toolbar: "#buhuoToolbar"
    });
	
	$("#buhuoSearchbox").searchbox({ 
		menu:"#buhuoSearchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            buhuo_datagrid.datagrid('reload',obj); 
	    }
	});
}

//采购计划
function caigoujihua(){
	jihua_datagrid = $('#jihua_datagrid').datagrid({
        url: ctx+"/procurementOrder/getList",
        width: 'auto',
		height: fixHeight(0.83),
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap: false,
        columns: [ 
            [ 
              {field: 'projectName',title: '项目名称',width: fixWidth(0.2),align: 'left', halign: 'center', sortable: true},
              {field: 'salesNo',title: '销售合同号',width: fixWidth(0.1),align: 'center', halign: 'center'},
              {field: 'orderNo',title: '销售订单编号',width: fixWidth(0.1),align: 'center', halign: 'center'},
              {field: 'createDate',title: '申请日期',width: fixWidth(0.1),align: 'center', halign: 'center', sortable: true,
            	  formatter: function(value, row) {
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
            	  }
              },
              {field: 'createUser',title: '申请人',width: fixWidth(0.1),align: 'center', halign: 'center'},
              {field: 'status',title: '状态',width: fixWidth(0.1),align: 'center', halign: 'center',
            	  formatter: function(value, row) {
            		  switch (value) {
						case "WAIT_FOR_PROCUREMENT":
							return "待采购";
						case "PROCUREMENT":
							return "采购中";
						case "END_PROCUREMENT":
							return "采购完毕";
						default:
							return "";
						}
            	  	}
              }
    	    ] 
        ],
    	onDblClickRow: function(index, row) {
        	showDetailsOrder(row);
        },
        toolbar: "#jihuaToolbar"
    });
	
	$("#jihuaSearchbox").searchbox({ 
		menu:"#jihuaSearchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            jihua_datagrid.datagrid('reload',obj); 
	    }
	});
}

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch(type) {
	if(type == "tunhuo") {
		jqueryUtil.gradeSearch(pro_datagrid, "#tunhuoGradeSearch", "/tunhuoProcurement/gradeSearch");
	} else if(type == "buhuo") {
		jqueryUtil.gradeSearch(buhuo_datagrid, "#buhuoGradeSearch", "/buhuoProcurement/gradeSearch");
	}
}

function  jihuaGradeSearch() {
	jqueryUtil.gradeSearch(pro_datagrid, "#gradeSearch", "/procurementOrder/gradeSearch");
}

//修正宽高
function fixHeight(percent) {  
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}



//初始化表单
function formInit(type) {
	var url_;
	if(type == "tunhuo") {
		url_ = ctx+"/tunhuoProcurement/saveOrUpdate";
	} else if(type == "buhuo") {
		url_ = ctx+"/buhuoProcurement/saveOrUpdate";
	}
    pro_form = $('#form').form({
    	url: url_,
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
	        }
	        return isValid;
	    },
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	pro_dialog.dialog('destroy');
	        	pro_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title: json.title,
				msg: json.message,
				timeout: 1000 * 2
			});
	    }
    });
}


//显示添加修改窗口
function showPro(type, row) {
	var url_;
	if(type == "tunhuo") {
		url_ = ctx+"/tunhuoProcurement/toMain";
		if(row) {
			url_ = ctx+"/tunhuoProcurement/toMain?procurementId="+row.procurementId;
		}
	} else if(type == "buhuo") {
		url_ = ctx+"/buhuoProcurement/toMain";
		if(row) {
			url_ = ctx+"/buhuoProcurement/toMain?procurementId="+row.procurementId;
		}
	}
    pro_dialog = $('<div/>').dialog({
    	title: "采购审批表",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit(type);
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	if(type == "tunhuo") {
                		saveTunhuoProcurement();
                	} else if(type == "buhuo") {
                		pro_form.submit();
                	}
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	pro_form.form('reset');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	pro_dialog.dialog('destroy');
            		pro_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
        	pro_dialog.dialog('destroy');
    		pro_datagrid.datagrid('reload');
        }
    });
    
    //显示提交审批按钮
    if(type == "tunhuo") {
    	pro_dialog.dialog({
    		onLoad: function () {
                formInit(type);
                if(row) {
                	if(row.status == "APPROVAL_SUCCESS") {
                		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
    		        	$("#ok").linkbutton("disable");
                	} else if(row.status == "PENDING") {
                		//审批中，不允许修改或提交审批
                		$("#save").linkbutton("disable");
        	        	$("#ok").linkbutton("disable");
        	        	
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
                	}
                }
            },
    		buttons: [
    		            {
    		                text: '保存',
    		                iconCls: 'icon-save',
    		                id: 'save',
    		                handler: function () {
    		                	if(type == "tunhuo") {
    		                		saveTunhuoProcurement();
    		                	} else if(type == "buhuo") {
    		                		pro_form.submit();
    		                	}
    		                }
    		            },
    		            {
    		            	text: '提交审批',
    		            	iconCls: 'icon-ok',
    		            	id: 'ok',
    		            	handler: function () {
    		            		$.messager.confirm('确认提示！', '确认提交表单进入审批流程吗？', function (result) {
    		                        if (result) {
    		                        	pro_form.form('submit', {
    		    	            		 	url: ctx+"/tunhuoProcurement/callApproval",
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
    		    	            	            	pro_dialog.dialog('destroy');//销毁对话框
    		    	            	            	pro_datagrid.datagrid('reload');//重新加载列表数据
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
    		                	pro_form.form('reset');
    		                }
    		            },
    		            {
    		                text: '关闭',
    		                iconCls: 'icon-cancel',
    		                handler: function () {
    		                	pro_dialog.dialog('destroy');
    		            		pro_datagrid.datagrid('reload');
    		                }
    		            }
    		        ]
    	})
    }
}

//取消保存-删除已保存的产品和配件
function cancelSave() {
	
}

//编辑
function edit(type) {
	var row;
	if(type == "tunhuo") {
		row = pro_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.datagrid('getSelected');
	}
    if (row) {
    	showPro(type, row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {

    var row = pro_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/procurement/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id: row.salesId},
                    success: function (data) {
                        if (data.status) {
                            pro_datagrid.datagrid('load');	// reload the sales data
                        }
                        $.messager.show({
        					title: data.title,
        					msg: data.message,
        					timeout: 1000 * 2
        				});
                    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//详情
function details(type) {
	var row;
	if(type == "tunhuo") {
		row = pro_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.datagrid('getSelected');
	} else if(type == "jihua") {
		row = jihua_datagrid.datagrid('getSelected');
	}
    if (row) {
    	if(type == "tunhuo" || type == "buhuo") {
    		showDetails(row, type);
    	} else if(type == "jihua") {
    		showDetailsOrder(row);
    	}
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//囤货/补货 详情
function showDetails(row, type) {
	
	var url_ = ctx+"/tunhuoProcurement/details/"+row.procurementId;
	if(type == "buhuo") {
		url_ = ctx+"/buhuoProcurement/details/"+row.procurementId;
	}
    pro_dialog = $('<div/>').dialog({
    	title: "采购详情",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode: true});
        },
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    pro_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            pro_dialog.dialog('destroy');
        }
    });
}

//采购计划详情
function showDetailsOrder(row) {
	pro_dialog = $('<div/>').dialog({
        	title: "采购计划详情",
    		top: 20,
    		width: fixWidth(0.8),
    		height: 'auto',
            modal: true,
            minimizable: true,
            maximizable: true,
            href: ctx+"/procurementOrder/details/"+row.id,
            onLoad: function () {
            	$("#detailsRemark").kindeditor({readonlyMode: true});
            },
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        pro_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                pro_dialog.dialog('destroy');
            }
        });
}

