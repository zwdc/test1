/**
 * 项目文件存档
 */

//囤货
var tunhuoProFile_datagrid;
//补货
var buhuoProFile_datagrid;
//销售
var salesProFile_datagrid;
//囤货和补货的共同
var proFile_form;
var proFile_dialog;




$(function() {
	//初始化囤货列表
	tunhuo();
	$("#type").val("tunhuo");
	$('#tabs').tabs({
	    border:false,
	    onSelect:function(title, index){
			if(title == "囤货") {
				tunhuo();
				$("#type").val("tunhuo");
			} else if(title == "补货") {
				buhuo();
				$("#type").val("buhuo");
			} else if(title == "销售") {
				sales();
				$("#type").val("sales");
			}
	    }
	});
	
});

//囤货
function tunhuo() {
	tunhuoProFile_datagrid = $('#tunhuoProFile_datagrid').treegrid({
        width: 'auto',
		height: fixHeight(0.83),
		rownumbers: true,
		animate: true,
		fitColumns: true,
		url: ctx+"/projectFile/getProcurementList?type=tunhuo",
	    idField:'procurementId',
	    treeField:'procurementNo',
	    pagination: true,
		columns:[[
			         {field:'procurementNo',title:'采购合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
			         {field:'projectName',title:'项目名称',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
			         {title:'文件详情',colspan:4}
		        ],[
		            {field: 'fileName',title: '文件名称',width: fixWidth(0.2),align: 'center'},
		            {field: 'filePath',title: '文件路径',width: fixWidth(0.2),align: 'center'},
					{field: 'uploadUser',title: '上传人',width: fixWidth(0.1),align: 'center'},
					{field: 'uploadDate',title: '上传时间',width: fixWidth(0.1),align: 'center',
						formatter:function(value,row){
		            		  return moment(value).format("YYYY-MM-DD HH:mm");
						 }
					}
			    ]],
    	onDblClickRow: function(index, row) {
    		showDetails(row, 'tunhuo');
        },
        toolbar: "#tunhuoToolbar"
    });
	
	$("#tunhuoSearchbox").searchbox({ 
		menu:"#tunhuoSearchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            tunhuoProFile_datagrid.treegrid('reload',obj); 
	    }
	});
}

//补货列表（补货采购审批表列表）
function buhuo() {
	buhuoProFile_datagrid = $('#buhuoProFile_datagrid').treegrid({
		 width: 'auto',
			height: fixHeight(0.83),
			rownumbers: true,
			animate: true,
			fitColumns: true,
			url: ctx+"/projectFile/getProcurementList?type=buhuo",
		    idField:'procurementId',
		    treeField:'procurementNo',
		    pagination: true,
			columns:[[
				         {field:'procurementNo',title:'采购合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
				         {field:'projectName',title:'项目名称',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
				         {title:'文件详情',colspan:4}
			        ],[
			            {field: 'fileName',title: '文件名称',width: fixWidth(0.2),align: 'center'},
			            {field: 'filePath',title: '文件路径',width: fixWidth(0.2),align: 'center'},
						{field: 'uploadUser',title: '上传人',width: fixWidth(0.1),align: 'center'},
						{field: 'uploadDate',title: '上传时间',width: fixWidth(0.1),align: 'center',
							formatter:function(value,row){
			            		  return moment(value).format("YYYY-MM-DD HH:mm");
							 }
						}
				    ]],
	    	onDblClickRow: function(index, row) {
	    		showDetails(row, 'tunhuo');
	        },
	        toolbar: "#buhuoToolbar"
    });
	
	$("#buhuoSearchbox").searchbox({ 
		menu:"#buhuoSearchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            buhuoProFile_datagrid.treegrid('reload',obj); 
	    }
	});
}

//销售列表（销售审批表列表）
function sales() {
	salesProFile_datagrid = $('#salesProFile_datagrid').treegrid({
		 width: 'auto',
			height: fixHeight(0.83),
			rownumbers: true,
			animate: true,
			fitColumns: true,
			url: ctx+"/projectFile/getSalesList",
		    idField:'salesId',
		    treeField:'salesNo',
		    pagination: true,
			columns:[[
				         {field:'salesNo',title:'销售合同号',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
				         {field:'projectName',title:'项目名称',rowspan:2,width: fixWidth(0.2),align: 'left', halign: 'center'},
				         {title:'文件详情',colspan:4}
			        ],[
			            {field: 'fileName',title: '文件名称',width: fixWidth(0.2),align: 'center'},
			            {field: 'filePath',title: '文件路径',width: fixWidth(0.2),align: 'center'},
						{field: 'uploadUser',title: '上传人',width: fixWidth(0.1),align: 'center'},
						{field: 'uploadDate',title: '上传时间',width: fixWidth(0.1),align: 'center',
							formatter:function(value,row){
			            		  return moment(value).format("YYYY-MM-DD HH:mm");
							 }
						}
				    ]],
	    	onDblClickRow: function(index, row) {
	    		showDetails(row, 'tunhuo');
	        },
	        toolbar: "#salesToolbar"
    });
	
	$("#salesSearchbox").searchbox({ 
		menu:"#salesSearchMenu", 
		prompt:'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            salesProFile_datagrid.treegrid('reload',obj); 
	    }
	});
}

function collapseAll(type){
	var type=$("#type").val();
	if(type == "tunhuo") {
		tunhuoProFile_datagrid.treegrid('collapseAll');
	} else if(type == "buhuo") {
		buhuoProFile_datagrid.treegrid('collapseAll');
	}else if(type == "sales"){
		salesProFile_datagrid.treegrid('collapseAll');
    } 
}
function expandAll(type){
	var type=$("#type").val();
	if(type == "tunhuo") {
		tunhuoProFile_datagrid.treegrid('expandAll');
	} else if(type == "buhuo") {
		buhuoProFile_datagrid.treegrid('expandAll');
	}else if(type == "sales"){
		salesProFile_datagrid.treegrid('expandAll');
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
function formInit(type, row) {
	var url_ = ctx+"/projectFile/saveOrUpdate?type="+type;
    proFile_form = $('#proFile_form').form({
    	url: url_,
        onSubmit: function () {
	        $.messager.progress({
	            title: '提示信息！',
	            text: '数据处理中，请稍后....'
	        });
	        var isValid = $(this).form('validate');
	        if (!isValid) {
	            $.messager.progress('close');
	        }else {
	        	$("#save").linkbutton("disable");
	        }
	        return isValid;
	    },
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	proFile_dialog.dialog('destroy');
	        	if(type == "tunhuo") {
	        		tunhuoProFile_datagrid.treegrid('reload');
	        	} else if(type == "buhuo") {
	        		buhuoProFile_datagrid.treegrid('reload');
	        	} else if(type == "sales") {
	        		salesProFile_datagrid.treegrid('reload');
	        	}
	        	/*if(json.status) {
	        		if(type == "tunhuo") {
	        			pro_dialog.dialog("refresh",ctx+"/tunhuoProcurement/toMain?procurementId="+json.data);
	        		} else if(type == "buhuo") {
	        			pro_dialog.dialog("refresh",ctx+"/buhuoProcurement/toMain?procurementId="+json.data);
	        		}
		        }*/
	        	
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
function showProFile(row) {
	var url_;
	var type=$("#type").val();
	if(row) {
		url_ = ctx+"/projectFile/toMain?id="+row.id+"&type="+type;
	}
	if(row) {
		if(typeof(row.id) == "undefined") {
			//选择的是审批表
			$.messager.alert('提示','请选择相应的【文件信息】表进行编辑操作！','info');
			return;
		}
	} else {
		var procureOrSalesId;
		if(type == "tunhuo") {
			row = tunhuoProFile_datagrid.datagrid('getSelected');
		} else if(type == "buhuo") {
			row = buhuoProFile_datagrid.datagrid('getSelected');
		}else if(type == "sales"){
			row = salesProFile_datagrid.datagrid('getSelected');
	    } 
		if(row){
			if(row.salesId!=null){
				procureOrSalesId=row.salesId;
			}else{
				procureOrSalesId=row.procurementId;
			}
			url_ = ctx+"/projectFile/toMain?type="+type+"&procureOrSalesId="+procureOrSalesId;
		}else{
			url_ = ctx+"/projectFile/toMain?type="+type;
		}
		if(row && typeof(row.id) != "undefined") {
			//选择的是文件信息
			$.messager.alert('提示','请选择相应的【项目】表进行文件存档！','info');
			return;
		}	
	}
	
	proFile_dialog = $('<div/>').dialog({
    	title: "项目文件存档",
		top: 20,
		width: fixWidth(0.8),
		height: 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit(type, row);
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                    proFile_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	proFile_form.form('reset');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	proFile_dialog.dialog('destroy');
                	if(type == "tunhuo") {
                		tunhuoProFile_datagrid.treegrid('reload');
                	} else if(type == "buhuo") {
                		buhuoProFile_datagrid.treegrid('reload');
                	} else if(type == "sales") {
                		salesProFile_datagrid.treegrid('reload');
                	}
                }
            }
        ],
        onClose: function () {
        	proFile_dialog.dialog('destroy');
        	if(type == "tunhuo") {
        		tunhuoProFile_datagrid.treegrid('reload');
        	} else if(type == "buhuo") {
        		buhuoProFile_datagrid.treegrid('reload');
        	} else if(type == "sales") {
        		salesProFile_datagrid.treegrid('reload');
        	}
        }
    });
    
}

//取消保存-删除已保存的产品和配件
function cancelSave() {
	
}

//编辑
function edit() {
	var row;
	var type=$("#type").val();
	if(type == "tunhuo") {
		row = tunhuoProFile_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuoProFile_datagrid.datagrid('getSelected');
	}else if(type == "sales"){
		row = salesProFile_datagrid.datagrid('getSelected');
    } 
    if (row) {
    	showProFile(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {

	var row;
	var type=$("#type").val();
	var contractNo;
	var url_;
	if(type == "tunhuo") {
		row = tunhuoProFile_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuoProFile_datagrid.datagrid('getSelected');
	}else if(type == "sales"){
		row = salesProFile_datagrid.datagrid('getSelected');
    } 
    if (row) {
    	if(type == "tunhuo") {
    		contractNo=row.procurementNo;
    		url_=ctx + '/projectFile/delete?procurementId='+row.procurementId;
    	} else if(type == "buhuo") {
    		contractNo=row.procurementNo;
    		url_=ctx + '/projectFile/delete?procurementId='+row.procurementId;
    	}else if(type == "sales"){
    		contractNo=row.salesNo;
    		url_=ctx + '/projectFile/delete?salesId='+row.salesId;
        } 
    	if(typeof(row.id) == "undefined") {
			//选择的是审批表
    		$.messager.confirm('确认提示！', '您确定要删除合同号为：'+contractNo+'下的所有文件信息?', function (result) {
	            if (result) {
	                $.ajax({
	            		async: false,
	            		cache: false,
	                    url: url_,
	                    type: 'post',
	                    dataType: 'json',
	                    data: {},
	                    success: function (data) {
	                        if (data.status) {
	                        	if(type == "tunhuo") {
	                        		tunhuoProFile_datagrid.treegrid('reload');
	                        	} else if(type == "buhuo") {
	                        		buhuoProFile_datagrid.treegrid('reload');
	                        	} else if(type == "sales") {
	                        		salesProFile_datagrid.treegrid('reload');
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
		}else{
	        $.messager.confirm('确认提示！', '您确定要删除选中的文件信息?', function (result) {
	            if (result) {
	                $.ajax({
	            		async: false,
	            		cache: false,
	                    url: ctx + '/projectFile/delete?id='+row.id,
	                    type: 'post',
	                    dataType: 'json',
	                    data: {},
	                    success: function (data) {
	                        if (data.status) {
	                        	if(type == "tunhuo") {
	                        		tunhuoProFile_datagrid.treegrid('reload');
	                        	} else if(type == "buhuo") {
	                        		buhuoProFile_datagrid.treegrid('reload');
	                        	} else if(type == "sales") {
	                        		salesProFile_datagrid.treegrid('reload');
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
		}
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//详情
function details() {
	var row;
	var type=$("#type").val();
	if(type == "tunhuo") {
		row = tunhuoProFile_datagrid.datagrid('getSelected');
	} else if(type == "buhuo") {
		row = buhuoProFile_datagrid.datagrid('getSelected');
	}else if(type == "sales"){
		row = salesProFile_datagrid.datagrid('getSelected');
    } 
    if (row) {
    	if(typeof(row.id) == "undefined") {
			//选择的是审批表
			$.messager.alert('提示','请选择相应的【文件信息】查看！','info');
			return;
		}
    	showDetails(row,type);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//详情
function showDetails(row, type) {
	var url_ = ctx+"/projectFile/details/"+type+"/"+row.id;
    proFile_dialog = $('<div/>').dialog({
    	title: "文件详情",
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
                	proFile_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	proFile_dialog.dialog('destroy');
        }
    });
}

