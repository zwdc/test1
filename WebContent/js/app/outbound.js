/**
 * 出库单
 */
var outbound_datagrid;
var outbound_form;
var outbound_dialog;
$(function(){
	/*outbound_datagrid=$('#outbound_datagrid').datagrid({
		url:ctx+"/outBound/getOutboundList",			//路径访问后台方法获取数据
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
		     	{field:'salesNo',title:'销售合同号',width : fixWidth(0.08),align:'center',halign:'center'},
		     	{field:'shippingAddress',title:'收货地址',width : fixWidth(0.09),align:'center',halign:'center'},
		     	{field:'outboundNumber',title:'出库数量',width : fixWidth(0.05),align:'center',halign:'center',sortable:true},
		     	{field:'orderNumber',title:'订单号码',width : fixWidth(0.08),align:'center',halign:'center'},
		     	{field:'outboundDate',title:'出库日期',width : fixWidth(0.1),align:'center',halign:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD HH:mm");
					 }
		     	},
		     	{field:'shippingUnit',title:'收货单位',width : fixWidth(0.1),align:'center',halign:'center'},
		     	{field:'linkMan',title:'联系人',width : fixWidth(0.1),align:'center'},
		     	{field:'mailingPhone',title:'联系电话',width : fixWidth(0.1),align:'center',halign:'center'},
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
        	showDetailsOutbound(row);
        },
		toolbar:"#toolbar"
	});*/
	
	outbound_datagrid=$('#outbound_datagrid').treegrid({
		width: 'auto',
		height: fixHeight(0.83),
		rownumbers: true,
		animate: true,
		fitColumns: true,
	    url:ctx+"/outBound/getList",
	    idField:'salesId',
	    treeField:'projectName',
	    pagination: true,
	    columns:[[
	         {field:'projectName',title:'项目名称',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {field:'salesNo',title:'销售合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {title:'入库详情',colspan:6}
        ],[
            {field: 'shippingUnit',title: '收货单位',width: fixWidth(0.2),align: 'center'},
            {field: 'linkMan',title: '联系人',width: fixWidth(0.1),align: 'center'},
            {field: 'cargoStore',title: '货物存放地',width: fixWidth(0.1),align: 'center'},
            {field: 'transportWay',title: '运输方式',width: fixWidth(0.1),align: 'center'},
			{field: 'outboundDate',title: '出库库时间',width: fixWidth(0.1),align: 'center'},
            {field: 'status',title: '状态',width: fixWidth(0.05),align: 'center',
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
	    ]],
	    onDblClickRow: function(row) {
    		showDetails(row, 'tunhuo');
        },
	    toolbar: "#toolbar"
	});
	
	//搜索框
	$("#searchbox").searchbox({
		menu:"#searchMenu",			//搜索类型的菜单
		prompt:'模糊查询',				//显示在输入框里的信息
		//函数当用户点击搜索按钮时调用
		searcher:function(value,name){
			var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
			var obj=eval('('+str+')');
			outbound_datagrid.treegrid('reload',obj);
		}
	});
});
function collapseAll(){
	outbound_datagrid.treegrid('collapseAll');
}
function expandAll(){
	outbound_datagrid.treegrid('expandAll');
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
	outbound_form = $('#outbound_form').form({
		 	url:  ctx+"/outBound/saveOrUpdate",
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
	            	outbound_datagrid.treegrid('reload');
	            	outbound_dialog.dialog("refresh",ctx+"/outBound/toMain?id="+json.data);
	            } 
	            $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
	        }
	    });
}


function showOutbound(row) {
	
	var _url;
	if(row) {
		if(typeof(row.id) == "undefined") {
			//选择的是采购审批表
			$.messager.alert('提示','请选择相应的【出库信息】进行编辑操作！','info');
			return;
		}
		_url = ctx+"/outBound/toMain?id="+row.id
	} else {
		_url = ctx+"/outBound/toMain";
		row = outbound_datagrid.treegrid('getSelected');
		if(row && typeof(row.id) != "undefined") {
			//选择的是入库信息
			$.messager.alert('提示','请选择相应的【销售订单】进行出库操作！','info');
			return;
		} else if(row && typeof(row.id) == "undefined") {
			_url = ctx+"/outBound/toMain?salesId="+row.salesId;
		}
	}
	
	var op;
    //弹出对话窗口
	outbound_dialog = $('<div/>').dialog({
    	title : "出库申请单",
    	top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            collection();
            if($("#outboundId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            if(row) {
            	if(row.status == "APPROVAL_SUCCESS") {
            		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
		        	$("#ok").linkbutton("disable");
		        	op = "reapproval";
		        	$('#save').attr("title", "保存后需重新审批");
            	} else if(row.status == "PENDING") {
            		//审批中，不允许修改或提交审批
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').addClass("easyui-tooltip");
    	        	$('#save,#ok').attr("title", "审批中");
    	        	
            	} else if(row.status == "APPROVAL_FAILED") {
            		//审批失败时，要在待办事项中去操作。
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').attr("title", "请在待办事项中处理");
            	} else if(row.status == "REAPPROVAL") {
            		$("#ok").linkbutton("enable");
            	}
            }
            if($('#isDisable').val()=="true"){
            	$("#ok").linkbutton("disable");
            	$('#ok').attr("title", "请添加出库产品或配件");
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
                    				 outbound_form.submit();
                    			 }
                    		});
                    	} else {
                    		outbound_form.submit();
                    	}
                    } else {
                    	outbound_form.submit();
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
                			outbound_form.form('submit',{
    	            		 	url: ctx+"/outBound/callApproval",
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
    	            	            	outbound_dialog.dialog('destroy');//销毁对话框
    	            	            	outbound_datagrid.treegrid('reload');//重新加载列表数据
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
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	outbound_dialog.dialog('destroy');
                	outbound_datagrid.treegrid('reload');
                }
            }
        ],
        onClose: function () {
        	outbound_dialog.dialog('destroy');
        	outbound_datagrid.treegrid('reload');
        }
    });
}
//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = outbound_datagrid.treegrid('getSelected');
    if (row) {
    	showOutbound(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//删除出库单
function del() {

    var row = outbound_datagrid.treegrid('getSelected');
    if (row) {
    	if(row.status == "WAITING_FOR_APPROVAL") {
    		$.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
    			if (result) {
    				$.ajax({
    					async: false,
    					cache: false,
    					url: ctx + '/outBound/delete/'+row.id,
    					type: 'post',
    					dataType: 'json',
    					data: {},
    					success: function (data) {
    						if (data.status) {
    							outbound_datagrid.treegrid('load');	
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
    		$.messager.alert("提示", "此数据当前的状态不准许被删除！", "warning");
    	}
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//出库单详情
function detailsOutbound(){
    var row = outbound_datagrid.treegrid('getSelected');
    if (row) {
    	if(typeof(row.id) == "undefined") {
			//选择的是采购审批表
			$.messager.alert('提示','请选择相应的【出库信息】进行查看详情操作！','info');
			return;
		}
    	showDetailsOutbound(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示出库单详情窗口
function showDetailsOutbound(row) {
    //弹出对话窗口
	var id = row.id;
	outbound_dialog = $('<div/>').dialog({
    	title : "出库单详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/outBound/toDetails/"+id,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	outbound_dialog.dialog('destroy');
                }
            }
        ],
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        onClose: function () {
        	outbound_dialog.dialog('destroy');
        }
    });
}