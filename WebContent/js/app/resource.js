/**
 * 资源管理
 */

var resource_treegrid;
var resource_form;
var resource_dialog;

$(function() {
	resource_treegrid = $("#resource").treegrid({
		width : 'auto',
		height : fixHeight(0.93),
		url : ctx+"/resource/listResource",
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
                {field : 'type',title : '资源类型',width : fixWidth(0.1),align : 'center',
            	    formatter:function(value,row){
            		    if("menu"==row.type){
            		    	return "<font color=green>菜单<font>";
            		    }else{
            		    	return "<font color=red>操作<font>";  
            		    }
					}
                },
                {field : 'url',title : '资源路径',width : fixWidth(0.2),align : 'center'},
                {field : 'permission',title : '权限字符串',width : fixWidth(0.2),align : 'left'},
			    {field : 'available',title : '是否启用',width : fixWidth(0.1),align : 'center',
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
            $('#resource_treegrid_menu').menu('show', {
                left: e.pageX,
                top: e.pageY
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
	var node = resource_treegrid.treegrid('getSelected');
	if (node) {
		resource_treegrid.treegrid('collapseAll', node.id);
	} else {
		resource_treegrid.treegrid('collapseAll');
	}
}

//展开
function expandAll(){
	var node = resource_treegrid.treegrid('getSelected');
	if (node) {
		resource_treegrid.treegrid('expandAll', node.id);
	} else {
		resource_treegrid.treegrid('expandAll');
	}
}

//刷新
function refresh(){
	resource_treegrid.treegrid('reload');
}


//----------------Resource-------------------

function resourceFormInit(row) {
	resource_form = $('#resourceForm').form({
        url: ctx+"/resource/saveOrUpdate",
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
            	resource_dialog.dialog('destroy');//销毁对话框
            	resource_treegrid.treegrid('reload');//重新加载列表数据
                
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

function showResource(row){
    //弹出对话窗口
    resource_dialog = $('<div/>').dialog({
    	title : "资源信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/resource/toMain",
        onLoad: function () {
            resourceFormInit(row);
            if (row) {
            	resource_form.form('load', row);
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	resource_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	resource_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            resource_dialog.dialog('destroy');
        }
    });
}

//添加修改操作
function openResource() {
    var row = resource_treegrid.treegrid('getSelected');
    if (row) {
    	showResource(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//停用/启用
function lock( flag ) {
	var row = resource_treegrid.treegrid('getSelected');
    if (row) {
    	var url_;
    	if(flag) {
    		url_ = ctx+"/resource/whetherLock/lock";
    	} else {
    		url_ = ctx+"/resource/whetherLock/unlock";
    	}
        $.ajax({
        	async: false,
    		cache: false,
            url: url_,
            type: 'post',
            dataType: 'json',
            data: {id : row.id},
            success: function (data) {
                if (data.status) {
                    resource_treegrid.treegrid('reload'); //reload the resource data
                } 
	            $.messager.show({
					title : data.title,
					msg : data.message,
					timeout : 1000 * 2
				});
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除组
function delRows() {
	var row = resource_treegrid.treegrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中数据? 同时也会删除所有部门所对应的此权限信息！', function (result) {
            if (result) {
                $.ajax({
                	async: false,
            		cache: false,
                    url: ctx + '/resource/doDelete',
                    type: 'post',
                    dataType: 'json',
                    data: {id : row.id},
                    success: function (data) {
                        if (data.status) {
                            resource_treegrid.treegrid('reload'); //reload the resource data
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
