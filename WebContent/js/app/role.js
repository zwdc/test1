/**
 * 角色（职位）、权限控制
 */

var role_datagrid;
var role_form;
var role_dialog;

var resource_datagrid;
var resource_form;
var resource_dialog;

$(function() {
	$("#panel").panel({   
		   width:'auto',   
		   height:$(this).height(),   
		   title: '权限编辑',   
	});
	
	role_datagrid = $("#role").datagrid({
		url : ctx+"/role/getRoleList",
		width : 'auto',
		height : fixHeight(0.85),
		pagination:true,
		border:false,
		rownumbers:true,
		singleSelect:true,
		striped:true,
		idField: 'id',
		sortName: 'type',//默认排序字段
        sortOrder: 'desc',//默认排序方式 'desc' 'asc'
        pageSize: 20,
		columns : [ 
		    [ 
		        {field : 'name',title : '角色名称',width : fixWidth(0.1),sortable: true,align : 'center'},
		        {field : 'type',title : '角色标识',width : fixWidth(0.1),sortable: true,align : 'center'},
		        {field : 'remark',title : '说明',width : fixWidth(0.1),sortable: true,align : 'left', halign: 'center'}
            ] 
		],
		toolbar:'#toolbar',
		onDblClickRow:getPermission
	})
	
	resource_datagrid = $("#resource").treegrid({
		width : 'auto',
		height : $(this).height()-120,
		url : ctx+"/resource/listResource",
		rownumbers:true,
		animate: true,
		collapsible: true,
		fitColumns: true,
		border:false,
		striped:true,
		singleSelect:false,
		cascadeCheck:true,
		deepCascadeCheck:true,
		idField: 'id',
		treeField: 'name',
		parentField : 'parentId',
		columns : [ 
		    [ 
                {field:'ck', checkbox:true},
                {field : 'name',title : '资源名称',width : fixWidth(0.2)},
                {field : 'type',title : '资源类型',width : fixWidth(0.1),align : 'center',sortable: true,
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
		            	} else if ("1"==row.isDelete) {
		            		return "<font color=red>否<font>";  
		            	}
					}
                }
                
            ] 
		],
		toolbar:'#tb',
		onClickRow:function(row){   
           //级联选择   
			resource_datagrid.treegrid('cascadeCheck',{   
                  id:row.id, //节点ID   
                  deepCascade:true //深度级联   
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
	var node = resource_datagrid.treegrid('getSelected');
	if (node) {
		resource_datagrid.treegrid('collapseAll', node.id);
	} else {
		resource_datagrid.treegrid('collapseAll');
	}
}

//展开
function expandAll(){
	var node = resource_datagrid.treegrid('getSelected');
	if (node) {
		resource_datagrid.treegrid('expandAll', node.id);
	} else {
		resource_datagrid.treegrid('expandAll');
	}
}

//刷新
function refresh(){
	resource_datagrid.treegrid('reload');
}

//双击职位时，关联权限
function getPermission(rowIndex, rowData){ 
    $.ajax({
    	async: false,
		cache: false,
        url: ctx + '/role/getRolePermission',
        type: 'post',
        dataType: 'json',
        data: {roleId: rowData.id},
        success: function (data) {
        	resource_datagrid.treegrid('reload');
        	setTimeout(function () { 
        		if(data.length!=0){
        			resource_datagrid.treegrid('unselectAll');
      	    	    $.each(data,function(i,e){
      	    		    resource_datagrid.treegrid('select',e.resourceId);
      	    	    });
      			}else{
      				$.messager.show({
      					title :"提示",
      					msg :"该角色下暂无权限信息!",
      					timeout : 1000 * 2
      				});
      				resource_datagrid.treegrid('unselectAll');
      			}
            }, 100);
        },
        beforeSend:function(){
		    $.messager.progress({
			    title: '提示信息！',
			    text: '数据处理中，请稍后....'
		    });
	    },
	    complete: function(){
		    $.messager.progress("close");
	    }
    });
} 


//初始化表单
function formInit(row) {
    role_form = $('#roleForm').form({
        url: ctx+"/role/saveOrUpdate",
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
                role_dialog.dialog('destroy');//销毁对话框
                role_datagrid.datagrid('reload');//重新加载列表数据
                
            }
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

//显示添加或修改窗口
function showRole(row) {
    //弹出对话窗口
    role_dialog = $('<div/>').dialog({
    	title : "角色信息",
		top: 20,
		width : 500,
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/role/toMain",
        onLoad: function () {
            formInit(row);
            if (row) {
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	role_form.form('load', row);
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    role_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    role_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            role_dialog.dialog('destroy');
        }
    });
}

//添加修改操作
function operation() {
    var row = role_datagrid.datagrid('getSelected');
    if (row) {
    	showRole(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除角色
function delRows() {
    var row = role_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中数据? 同时也会删除此角色所对应的权限信息！', function (result) {
            if (result) {
                $.ajax({
                	async: false,
            		cache: false,
                    url: ctx + '/role/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id : row.id},
                    success: function (data) {
                    	$.messager.progress("close");
                        if (data.status) {
                            role_datagrid.datagrid('load');
                            resource_datagrid.treegrid('reload');
                        }
                        $.messager.show({
        					title : data.title,
        					msg : data.message,
        					timeout : 1000 * 2
        				});
                    },
                    beforeSend:function(){
    				    $.messager.progress({
    					    title: '提示信息！',
    					    text: '数据处理中，请稍后....'
    				    });
    			    },
    			    complete: function(){
    				    $.messager.progress("close");
    			    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//保存权限设置
function savePermission(){
	var selections=resource_datagrid.treegrid('getSelections');
	var selectionRole=role_datagrid.datagrid('getSelected');
	var checkedIds=[];
	if(selections.length!=0){
		$.each(selections,function(i,e){
			checkedIds.push(e.id);
		});
		if(selectionRole){
			$.ajax({
				url: ctx + '/role/savePermission',
				type: 'post',
				dataType: 'json',
				data: {roleId:selectionRole.id, resourceIds:checkedIds},
                success: function (data) {
                	$.messager.progress("close");
                	resource_datagrid.datagrid("reload");
                	$.messager.show({
    					title : data.title,
    					msg : data.message,
    					timeout : 1000 * 2
    				});
                },
			    beforeSend:function(){
				    $.messager.progress({
					    title: '提示信息！',
					    text: '数据处理中，请稍后....'
				    });
			    },
			    complete: function(){
				    $.messager.progress("close");
			    },
				error:function(){
					$.messager.show({
						title :"提示",
						msg : "分配失败！",
						timeout : 1000 * 2
					});
				}
				
			});
		}else{
			$.messager.show({
				title :"提示",
				msg : "请选择角色！",
				timeout : 1000 * 2
			});
		}
	}else{
		$.messager.show({
			title :"提示",
			msg : "请选择资源信息！",
			timeout : 1000 * 2
		});
		expandAll();
	}
}

