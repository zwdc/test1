/**
 * 销售合同
 */

var contract_datagrid;
var contract_form;
var contract_dialog;

$(function() {
    contract_datagrid = $('#contract_datagrid').datagrid({
        url: ctx+"/salesContract/getList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'projectName',title : '项目名称',width : fixWidth(0.2),align : 'left', halign: 'center'},
              {field : 'salesNo',title : '销售合同号',width : fixWidth(0.1),align : 'center',sortable: true},
              {field : 'contractAmount',title : '销售合同额',width : fixWidth(0.1),align : 'center',sortable: true},
              {field : 'fileName',title : '文件名称',width : fixWidth(0.3),align : 'left', halign: 'center'},
	     	  {field : 'status',title: '状态',width: fixWidth(0.1),align: 'center', halign: 'center',sortable: true,
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
              		return "<a class='trace' onclick=\"graphTrace('"+value+"')\" id='diagram' href='#' title='点击查看'>"+row.taskName+"</a>";
				}
              }
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetails(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            contract_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(contract_datagrid, "#gradeSearch", "/salesContract/gradeSearch");
}

//修正宽高
function fixHeight(percent) {  
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}



//初始化表单
function formInit() {
    contract_form = $('#form').form({
    	url: ctx+"/salesContract/saveOrUpdate",
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
	        	contract_dialog.dialog("refresh",ctx+"/salesContract/toMain?id="+json.data.toString());
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加修改窗口
function showContract(row) {
	var url_ = ctx+"/salesContract/toMain";
	var op;
	if(row){
		url_ = ctx+"/salesContract/toMain?id="+row.id;
	}
    contract_dialog = $('<div/>').dialog({
    	title : "合同上传",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit();
            if($("#contractId").val() == '') {
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
                    				 contract_form.submit();
                    			 }
                    		});
                    	} else {
                    		contract_form.submit();
                    	}
                    } else {
                    	contract_form.submit();
                    }
                }
            },
            {
            	text: '提交审批',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
            		var url_ = ctx+"/salesContract/callApproval";
            		$.messager.confirm('确认提示！', '确认提交表单进入审批流程吗？', function (result) {
                        if (result) {
                        	contract_form.form('submit', {
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
    	            		        	$("#ok").linkbutton("disable");
    	            		        }
    	            		        return isValid;
    	            		    },
    	            		    success: function (data) {
    	            	            $.messager.progress('close');
    	            	            var json = $.parseJSON(data);
    	            	            if (json.status) {
    	            	            	contract_dialog.dialog('destroy');
    	            	            	contract_datagrid.datagrid('reload');
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
                	contract_form.form('reset');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    contract_dialog.dialog('destroy');
                    contract_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
            contract_dialog.dialog('destroy');
            contract_datagrid.datagrid('reload');
        }
    });
}

//编辑
function edit() {
    var row = contract_datagrid.datagrid('getSelected');
    if (row) {
    	showContract(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {

    var row = contract_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/salesContract/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id: id},
                    success: function (result) {
                        if (result.status) {
                            contract_datagrid.datagrid('load');
                        }
                        $.messager.show({
        					title : result.title,
        					msg : result.message,
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

//详情
function details(){
    var row = contract_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示详情窗口
function showDetails(row) {
    contract_dialog = $('<div/>').dialog({
    	title : "合同详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/salesContract/details/"+row.id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        	$("#paymentType").kindeditor({readonlyMode : true});
        },
        buttons: [
			{
			    text: '打印预览',
			    iconCls: 'icon-search',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPagetwo('details_contract');
			    	LODOP.PREVIEW();
			    }
			},
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
