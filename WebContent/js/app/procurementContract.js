/**
 * 采购合同
 */

//囤货
var tunhuo_datagrid;
//补货
var buhuo_datagrid;

var pro_form;
var contract_dialog;


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
	tunhuo_datagrid = $('#tunhuo_datagrid').datagrid({
        url: ctx+"/procurementContract/getList/tunhuo",
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
              {field: 'contractAmount',title: '合同金额',width: fixWidth(0.1),align: 'right', halign: 'center',sortable: true,
                	formatter:function(value, row){
                		return jqueryUtil.formatNumber(value);
    				}
              },
              {field: 'companyName',title: '采购方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'partnerInfoName',title: '销售方',width: fixWidth(0.2),align: 'center', halign: 'center'},
              {field: 'fileName',title : '文件名称',width : fixWidth(0.2),align : 'left', halign: 'center'},
              {field: 'status',title: '状态',width: fixWidth(0.1),align: 'center', halign: 'center',sortable: true,
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
    		showDetails(row, 'tunhuo');
        },
        toolbar: "#tunhuo_toolbar"
    });
	
	$("#tunhuo_searchbox").searchbox({ 
		menu:"#tunhuo_searchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            tunhuo_datagrid.datagrid('reload',obj); 
	    }
	});
}

//补货列表（补货采购审批表列表）
function buhuo() {
	buhuo_datagrid = $('#buhuo_datagrid').datagrid({
        url: ctx+"/procurementContract/getList/buhuo",
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
              {field: 'projectName',title: '采购合同号',width: fixWidth(0.2),align: 'left', halign: 'center'},
              {field: 'salesNo',title: '销售合同号',width: fixWidth(0.1),align: 'center'},
              {field: 'procurementNo',title: '采购合同号',width: fixWidth(0.1),align: 'center',sortable: true},
              {field: 'contractAmount',title: '采购价格',width: fixWidth(0.1),align: 'right', halign: 'center',sortable: true,
                	formatter:function(value, row){
                		return jqueryUtil.formatNumber(value);
    				}
              },
              {field: 'companyName',title: '采购方',width: fixWidth(0.1),align: 'center', halign: 'center'},
              {field: 'partnerInfoName',title: '销售方',width: fixWidth(0.1),align: 'center', halign: 'center'},
              {field : 'fileName',title : '文件名称',width : fixWidth(0.2),align : 'left', halign: 'center'},
              {field: 'status',title: '状态',width: fixWidth(0.1),align: 'center', halign: 'center',sortable: true,
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
              }
    	    ] 
        ],
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'buhuo');
        },
        toolbar: "#buhuo_toolbar"
    });
	
	$("#buhuo_searchbox").searchbox({ 
		menu:"#buhuo_searchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            buhuo_datagrid.datagrid('reload',obj); 
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
		jqueryUtil.gradeSearch(tunhuo_datagrid, "#tunhuoGradeSearch", "/procurementContract/gradeSearch/tunhuo");
	} else if(type == "buhuo") {
		jqueryUtil.gradeSearch(buhuo_datagrid, "#buhuoGradeSearch", "/procurementContract/gradeSearch/buhuo");
	}
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
    pro_form = $('#form').form({
    	url: ctx+"/procurementContract/saveOrUpdate",
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
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	if(type == "tunhuo") {
	        		contract_dialog.dialog("refresh",ctx+"/procurementContract/toMain?id="+json.data.toString()+"&type=tunhuo");
	        	} else if(type == "buhuo") {
	        		contract_dialog.dialog("refresh",ctx+"/procurementContract/toMain?id="+json.data.toString()+"&type=buhuo");
	        	}
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
function showContract(type, row) {
	var url_;
	var op;
	if(type == "tunhuo") {
		url_ = ctx+"/procurementContract/toMain?type=tunhuo";
		if(row) {
			url_ = ctx+"/procurementContract/toMain?type=tunhuo&id="+row.id;
		}
	} else if(type == "buhuo") {
		url_ = ctx+"/procurementContract/toMain?type=buhuo";
		if(row) {
			url_ = ctx+"/procurementContract/toMain?type=buhuo&id="+row.id;
		}
	}
    contract_dialog = $('<div/>').dialog({
    	title: "采购合同",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit(type);
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
    	        	$("#uploadButton").attr("disabled", "disabled");
    	        	
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
    	        	$("#uploadButton").attr("disabled", "disabled");
    	        	
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
            var fileName = $("#fileName").val();
            if(fileName == '' || fileName == null) {
            	$("#ok").linkbutton("disable");	  //还没上传文件时是不准许完成任务的
            	$('#ok').tooltip({
					position: 'top',
					content: '<span style="color:#fff">请先上传合同文件</span>',
					onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: '#666',
							borderColor: '#565656'
						});
					}
				});
            } else {
            	$("#ok").linkbutton("enable");
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
                    				 pro_form.submit();
                    			 }
                    		});
                    	} else {
                    		pro_form.submit();
                    	}
                    } else {
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
    	            		 	url: ctx+"/procurementContract/callApproval",
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
    	            	            	contract_dialog.dialog('destroy');
    	            	            	if(type == "tunhuo") {
    	            	            		tunhuo_datagrid.datagrid('reload');
    	            	            	} else if(type == "buhuo") {
    	            	            		buhuo_datagrid.datagrid('reload');
    	            	            	}
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
                	clearChoose();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	contract_dialog.dialog('destroy');
                	if(type == "tunhuo") {
                		tunhuo_datagrid.datagrid('reload');
                	} else if(type == "buhuo") {
                		buhuo_datagrid.datagrid('reload');
                	}
                }
            }
        ],
        onClose: function () {
        	contract_dialog.dialog('destroy');
        	if(type == "tunhuo") {
        		tunhuo_datagrid.datagrid('reload');
        	} else if(type == "buhuo") {
        		buhuo_datagrid.datagrid('reload');
        	}
        }
    });
}

//取消保存-删除已保存的产品和配件
function cancelSave() {
	
}

//编辑
function edit(type) {
	var row;
	if(type == "tunhuo") {
		row = tunhuo_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.datagrid('getSelected');
	}
    if (row) {
    	showContract(type, row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del(type) {
	var row;
	if(type == "tunhuo") {
		row = tunhuo_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.datagrid('getSelected');
	}
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/procurementContract/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id: row.id},
                    success: function (data) {
                        if (data.status) {
                            if(type == "tunhuo") {
                        		tunhuo_datagrid.datagrid('load');
                        	} else if(type == "buhuo") {
                        		buhuo_datagrid.datagrid('load');
                        	}
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
		row = tunhuo_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.datagrid('getSelected');
	}
    if (row) {
    	showDetails(row, type);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//囤货/补货 详情
function showDetails(row, type) {
	var url_ = ctx+"/procurementContract/details/tunhuo/"+row.id;
	if(type == "buhuo") {
		url_ = ctx+"/procurementContract/details/buhuo/"+row.id;
	}
    contract_dialog = $('<div/>').dialog({
    	title: "采购合同详情",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
        	$("#paymentType").kindeditor({readonlyMode: true});
        	$("#detailsRemark").kindeditor({readonlyMode: true});
        },
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    contract_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            contract_dialog.dialog('destroy');
        }
    });
}

function clearChoose() {
	$("#procurementId").val("");
	$("#procurementNo").textbox("clear");
	$("#contractAmount").numberbox("clear");
	
	if($("#projectName").length){
		$("#projectName").textbox("clear");
		$("#salesNo").textbox("clear");
		//清除价钱
		$("#productTotalPrice").html("0");
		$("#partTotalPrice").html("0");
		$("#procurementPrice").numberbox("setValue", 0);
	}
	
	$("#productInfo,#partInfo").bootstrapTable('removeAll');
}
