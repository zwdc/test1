/**
 * 入库
 */

var inbound_form;
var inbound_dialog;
var inbound_tunhuo_dialog;
//囤货
var tunhuo_datagrid;
//补货
var buhuo_datagrid;



$(function() {
	//初始化囤货列表
	tunhuo();
	
	$('#tabs').tabs({
	    border:false,
	    onSelect:function(title, index){
			if(title == "囤货入库单") {
				tunhuo();
			} else if(title == "补货入库单") {
				buhuo();
			}
	    }
	});
	
});

//囤货入库列表
function tunhuo() {
	//datagrid的方式加载数据
	/*tunhuo_datagrid = $('#tunhuo_datagrid1').datagrid({
        url: ctx+"/inBoundTunhuo/getList",
        width: 'auto',
		height: fixHeight(0.83),
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns: [ 
            [ 
              {field: 'procurementNo',title: '采购合同号',width: fixWidth(0.2),align: 'left', halign: 'center'},
              {field: 'storeHouse',title: '库房名',width: fixWidth(0.2),align: 'center',sortable: true},
              {field: 'receiveUserName',title: '接收人',width: fixWidth(0.2),align: 'center',sortable: true},
              {field: 'inboundDate',title: '入库时间',width: fixWidth(0.1),align: 'center',sortable: true,
            	  formatter:function(value, row){
            		  return moment(value).format("YYYY-MM-DD");
            	  }
              }
    	    ] 
        ],
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'tunhuo');
        },
        toolbar: "#toolbar"
    });*/
	
	//treegrid的方式加载数据
	tunhuo_datagrid = $('#tunhuo_datagrid').treegrid({
		width: 'auto',
		height: fixHeight(0.83),
		rownumbers: true,
		animate: true,
		fitColumns: true,
	    url:ctx+"/inBoundTunhuo/getInboundList",
	    idField:'procurementId',
	    treeField:'procurementNo',
	    pagination: true,
	    columns:[[
	         {field:'procurementNo',title:'采购合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {title:'入库详情',colspan:4}
        ],[
            {field: 'storeHouse',title: '库房名',width: fixWidth(0.2),align: 'center'},
            {field: 'receiveUserName',title: '接收人',width: fixWidth(0.2),align: 'center'},
			{field: 'inboundDate',title: '入库时间',width: fixWidth(0.1),align: 'center'},
			{field: 'completeInbound',title: '入库状态',width: fixWidth(0.1),align: 'center',
				formatter: function(value, row) {
					if(value == 0) {
						return "<span class='text-danger'>待入库</span>";
					} else if(value == 1){
						return "<span class='text-success'>已入库</span>";
					}
				}
			}
	    ]],
	    onDblClickRow: function(row) {
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
            tunhuo_datagrid.treegrid('reload',obj); 
	    }
	});
}

function collapseAll(type){
	if(type == "tunhuo") {
		tunhuo_datagrid.treegrid('collapseAll');
	} else if(type == "buhuo") {
		buhuo_datagrid.treegrid('collapseAll');
	}
}
function expandAll(type){
	if(type == "tunhuo") {
		tunhuo_datagrid.treegrid('expandAll');
	} else if(type == "buhuo") {
		buhuo_datagrid.treegrid('expandAll');
	}
}

//补货入库列表
function buhuo() {
	/*buhuo_datagrid = $('#buhuo_datagrid').datagrid({
        url: ctx+"/inBoundBuhuo/getList",
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
              {field: 'salesNo',title: '销售合同号',width: fixWidth(0.1),align: 'center',sortable: true},
              {field: 'procurementNo',title: '采购合同号',width: fixWidth(0.1),align: 'center',sortable: true},
              {field: 'storeHouse',title: '库房名',width: fixWidth(0.2),align: 'center',sortable: true},
              {field: 'receiveUserName',title: '接收人',width: fixWidth(0.2),align: 'center'},
              {field: 'inboundDate',title: '入库时间',width: fixWidth(0.1),align: 'center', sortable: true,
            	  formatter:function(value, row){
            		  return moment(value).format("YYYY-MM-DD");
            	  }
              }
    	    ] 
        ],
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'buhuo');
        },
        toolbar: "#buhuoToolbar"
    });*/
	
	//treegrid的方式加载数据
	buhuo_datagrid = $('#buhuo_datagrid').treegrid({
		width: 'auto',
		height: fixHeight(0.83),
		rownumbers: true,
		animate: true,
		fitColumns: true,
	    url:ctx+"/inBoundBuhuo/getInboundList",
	    idField:'procurementId',
	    treeField:'procurementNo',
	    pagination: true,
	    columns:[[
             {field:'procurementNo',title:'采购合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {field:'projectName',title:'项目名称',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {field:'salesNo',title:'销售合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center',sortable: true},
	         {title:'入库详情',colspan:4}
        ],[
            {field: 'storeHouse',title: '库房名',width: fixWidth(0.2),align: 'center'},
            {field: 'receiveUserName',title: '接收人',width: fixWidth(0.2),align: 'center'},
			{field: 'inboundDate',title: '入库时间',width: fixWidth(0.1),align: 'center'},
			{field: 'completeInbound',title: '入库状态',width: fixWidth(0.1),align: 'center',
				formatter: function(value, row) {
					if(value == 0) {
						return "<span class='text-danger'>待入库</span>";
					} else if(value == 1){
						return "<span class='text-success'>已入库</span>";
					}
				}
			}
	    ]],
	    onDblClickRow: function(row) {
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
            buhuo_datagrid.treegrid('reload',obj); 
	    }
	});
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
		url_ = ctx+"/inBoundTunhuo/saveOrUpdate";
	} else if(type == "buhuo") {
		url_ = ctx+"/inBoundBuhuo/saveOrUpdate";
	}
    inbound_form = $('#form').form({
    	url: url_,
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
	        	if(json.status) {
	        		if(type == "tunhuo") {
                		inbound_tunhuo_dialog.dialog("refresh",ctx+"/inBoundTunhuo/toMain?id="+json.data);
                	} else if(type == "buhuo") {
                		inbound_dialog.dialog("refresh",ctx+"/inBoundBuhuo/toMain?id="+json.data);
                	}
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
function showBuhuo(row) {
	var url_;
	if(row) {
		if(typeof(row.id) == "undefined") {
			//选择的是采购审批表
			$.messager.alert('提示','请选择相应的【入库信息】表进行编辑操作！','info');
			return;
		}
		url_ = ctx+"/inBoundBuhuo/toMain?id="+row.id
	} else {
		url_ = ctx+"/inBoundBuhuo/toMain";
		row = buhuo_datagrid.treegrid('getSelected');
		if(row && typeof(row.id) != "undefined") {
			//选择的是入库信息
			$.messager.alert('提示','请选择相应的【采购审批】表进行入库操作！','info');
			return;
		} else if(row && typeof(row.id) == "undefined") {
			url_ = ctx+"/inBoundBuhuo/toMain?procurementId="+row.procurementId;
		}
		
	}
	
	
    inbound_dialog = $('<div/>').dialog({
    	title: "入库单",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit("buhuo");
            if($("#inboundId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            if(row && row.completeInbound == 1) {
            	$("#ok").linkbutton("disable");
            } else if(row && row.completeInbound == 0) {
            	$("#ok").linkbutton("enable");
            }
        },
        buttons: [
			{
			    text: '保存',
			    iconCls: 'icon-save',
			    id: 'save',
			    handler: function () {
			    	inbound_form.submit();
			    }
			},
            {
                text: '确认入库',
                iconCls: 'icon-save',
                id: 'ok',
                handler: function () {
                	$.messager.confirm('确认提示！', '此操作将会直接进行入库操作，增加相应的库存数量！您确认执行入库操作吗？', function (result) {
            			if(result) {
            				inbound_form.form('submit',{
    	            		 	url: ctx+"/inBoundBuhuo/completeTask",
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
    	            	            	inbound_dialog.dialog('destroy');
    	            	            	buhuo_datagrid.treegrid('reload');
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
                	inbound_dialog.dialog('destroy');
                	buhuo_datagrid.treegrid('reload');
                }
            }
        ],
        onClose: function () {
        	inbound_dialog.dialog('destroy');
        	buhuo_datagrid.treegrid('reload');
        }
    });
    
}

function showTunhuo(row) {
	var url_;
	if(row) {
		if(typeof(row.id) == "undefined") {
			//选择的是采购审批表
			$.messager.alert('提示','请选择相应的【入库信息】表进行编辑操作！','info');
			return;
		}
		url_ = ctx+"/inBoundTunhuo/toMain?id="+row.id
	} else {
		url_ = ctx+"/inBoundTunhuo/toMain";
		row = tunhuo_datagrid.treegrid('getSelected');
		if(row && typeof(row.id) != "undefined") {
			//选择的是入库信息
			$.messager.alert('提示','请选择相应的【采购审批】表进行入库操作！','info');
			return;
		} else if(row && typeof(row.id) == "undefined") {
			url_ = ctx+"/inBoundTunhuo/toMain?procurementId="+row.procurementId;
		}
		
	}
	inbound_tunhuo_dialog = $('<div/>').dialog({
    	title: "入库单",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit("tunhuo");
            if($("#inboundId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            if(row && row.completeInbound == 1) {
            	$("#ok").linkbutton("disable");
            } else if(row && row.completeInbound == 0) {
            	$("#ok").linkbutton("enable");
            }
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	inbound_form.submit();
                }
            },
            {
            	text: '确认入库',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
            		$.messager.confirm('确认提示！', '您确定要完成此次入库操作吗? 下次仍可以继续将没有入库的产品或配件进行入库操作！', function (result) {
                        if (result) {
                        	inbound_form.form('submit',{
    	            		 	url: ctx+"/inBoundTunhuo/completeInbound",
    	            	        onSubmit: function (param) {
    	            		        $.messager.progress({
    	            		            title: '提示信息！',
    	            		            text: '数据处理中，请稍后....'
    	            		        });
    	            		        var isValid = $(this).form('validate');
    	            		        if (!isValid) {
    	            		            $.messager.progress('close');
    	            		        } else {
    	            		        	param.inboundId = $("#inboundId").val();
    	            		        	param.procurementId = $("#procurementId").val();
    	            		        	$("#save").linkbutton("disable");
    	            		        	$("#ok").linkbutton("disable");
    	            		        }
    	            		        return isValid;
    	            		    },
    	            		    success: function (data) {
    	            	            $.messager.progress('close');
    	            	            var json = $.parseJSON(data);
    	            	            if (json.status) {
    	            	            	inbound_tunhuo_dialog.dialog('destroy');	
    	            	            	tunhuo_datagrid.treegrid('reload');
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
                	inbound_tunhuo_dialog.dialog('destroy');
                	tunhuo_datagrid.treegrid('reload');
                }
            }
        ],
        onClose: function () {
        	inbound_tunhuo_dialog.dialog('destroy');
        	tunhuo_datagrid.treegrid('reload');
        }
    });
    
}

//编辑
function edit(type) {
	var row;
	if(type == "tunhuo") {
		row = tunhuo_datagrid.treegrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.treegrid('getSelected');
	}
    if (row) {
    	if(type == "tunhuo") {
    		showTunhuo(row);
    	} else if(type == "buhuo") {
    		showBuhuo(row);
    	}
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del(type) {
	var row;
	if(type == "tunhuo") {
		row = tunhuo_datagrid.treegrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.treegrid('getSelected');
	}
    
    if (row) {
    	var _url;
    	if(type == "tunhuo") {
    		_url = ctx+"/inBoundTunhuo/delete?id="+row.id;
    	} else if(type == "buhuo") {
    		_url = ctx+"/inBoundBuhuo/delete?id="+row.id;
    	}
    	if(row.completeInbound == 0) {
    		$.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
    			if (result) {
    				$.ajax({
    					async: false,
    					cache: false,
    					url: _url,
    					type: 'post',
    					dataType: 'json',
    					success: function (data) {
    						if (data.status) {
    							if(type == "tunhuo") {
    								tunhuo_datagrid.treegrid('reload');
    							} else if(type == "buhuo") {
    								buhuo_datagrid.treegrid('reload');
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
    		$.messager.alert("提示", "此数据当前的状态不准许被删除！", "warning");
    	}
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//详情
function details(type) {
	var row;
	if(type == "tunhuo") {
		row = tunhuo_datagrid.treegrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuo_datagrid.treegrid('getSelected');
	}
    if (row) {
    	showDetails(row, type);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//囤货/补货 详情
function showDetails(row, type) {
	var url_;
	if(type == "tunhuo") {
		if(typeof(row.id) == "undefined") {
			$.messager.alert('提示','只能查看【入库信息】的详细信息！','info');
			return;
		} else {
			url_ = ctx+"/inBoundTunhuo/details?inboundId="+row.id;
		}
	} else if(type == "buhuo") {
		if(typeof(row.id) == "undefined") {
			$.messager.alert('提示','只能查看【入库信息】的详细信息！','info');
			return;
		} else {
			url_ = ctx+"/inBoundBuhuo/details?inboundId="+row.id;
		}
	}
    inbound_dialog = $('<div/>').dialog({
    	title: "入库详情",
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
                    inbound_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            inbound_dialog.dialog('destroy');
        }
    });
}

