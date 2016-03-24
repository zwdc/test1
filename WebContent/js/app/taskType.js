/**
 * 任务类型
 */

var taskType_treegrid;
var taskType_form;
var taskType_dialog;


$(function() {
	taskType_treegrid = $("#taskType_treegrid").treegrid({
		width : 'auto',
		height : fixHeight(0.93),
		url : ctx+"/taskType/getList",
		rownumbers:true,
		animate: true,
		collapsible: true,
		fitColumns: true,
		border:false,
		striped:true,
		cascadeCheck:true,
		deepCascadeCheck:true,
		idField: 'id',
		treeField: 'name',
		parentField : 'parentId',
		columns : [ 
		    [ 
                {field : 'name',title : '资源名称',width : fixWidth(0.2)},
			    {field : 'isDelete',title : '是否可用',width : fixWidth(0.1),align : 'center',
		            formatter:function(value,row){
		            	if("0"==row.isDelete){
		            		return "<font color=green>是<font>";
		            	}else if("1" == row.isDelete){
		            		return "<font color=red>否<font>";  
		            	}
					}
                }
                
            ] 
		],
		toolbar:'#toolbar',
        onContextMenu: function (e, row) {
            e.preventDefault();
            $('#treegrid_menu').menu('show', {
                left: e.pageX,
                top: e.pageY,
                onClick:function(item){   
                	if("add" == item.id) {
                		row.name = "";
            			row.isDelete = 0;
            			row.parentId = row.id;
            			row.id = "";
            			show(row);
                	} else if("edit" == item.id) {
                		if(row.parentId == 0) {
                			row.parentId = "";
                			show(row);
                		}
                		show(row);
                	} else if("delete" == item.id) {
                		delRows();
                	}
				}
            });
            
        }
	});
});

//修正宽高
function fixHeight(percent)   
{   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent)   
{   
	return parseInt(($(this).width() - 50) * percent);
}

//收缩
function collapseAll(){
	var node = taskType_treegrid.treegrid('getSelected');
	if (node) {
		taskType_treegrid.treegrid('collapseAll', node.id);
	} else {
		taskType_treegrid.treegrid('collapseAll');
	}
}

//展开
function expandAll(){
	var node = taskType_treegrid.treegrid('getSelected');
	if (node) {
		taskType_treegrid.treegrid('expandAll', node.id);
	} else {
		taskType_treegrid.treegrid('expandAll');
	}
}

//刷新
function refresh(){
	taskType_treegrid.treegrid('reload');
}


//----------------Resource-------------------

function formInit(row) {
	taskType_form = $('#taskTypeForm').form({
        url: ctx+"/taskType/saveOrUpdate",
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
            	taskType_dialog.dialog('destroy');//销毁对话框
            	taskType_treegrid.treegrid('reload');//重新加载列表数据
                
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

//添加
function show(row){
    taskType_dialog = $('<div/>').dialog({
    	title : "任务类型",
		top: 20,
		width : 600,
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/taskType/toMain",
        onLoad: function () {
        	formInit(row);
            if (row) {
            	taskType_form.form('load', row);
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	taskType_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	taskType_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            taskType_dialog.dialog('destroy');
        }
    });
}

//修改操作
function edit() {
    var row = taskType_treegrid.treegrid('getSelected');
    if (row) {
    	show(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}


//删除组
function delRows() {
	var row = taskType_treegrid.treegrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中数据? 同时也会删除所有部门所对应的此权限信息！', function (result) {
            if (result) {
                $.ajax({
                	async: false,
            		cache: false,
                    url: ctx + '/taskType/doDelete/' + row.id,
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.status) {
                            taskType_treegrid.treegrid('reload'); 
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

