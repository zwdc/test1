/**
 * 任务来源
 */

var taskSource_datagrid;
var taskSource_form;
var taskSource_dialog;

$(function() {
	//数据列表
	taskSource_datagrid = $('#taskSource_datagrid').datagrid({
        url: ctx+"/taskSource/getList",
        width : 'auto',
		height : fixHeight(1),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field: 'name', title: '任务来源名称', width: fixWidth(0.3), align: 'left', halign: 'center', sortable: true},
              {field: 'info', title: '任务来源简介', width: fixWidth(0.4), align: 'left', halign: 'center'},
              {field: 'taskTypeName', title: '所属分类', width: fixWidth(0.2), align: 'center', sortable: true},
              {field: 'createDate', title: '创建日期', width: fixWidth(0.1), align: 'center', sortable: true,
		     		formatter:function(value,row){
		     			return moment(value).format("YYYY-MM-DD HH:mm:ss");
		     		}
              }
    	    ] 
        ],
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            taskSource_datagrid.datagrid('reload',obj); 
	    }
	});
});

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row) {
	taskSource_form = $('#taskSourceForm').form({
        url: ctx+"/taskSource/saveOrUpdate",
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
	        	taskSource_dialog.dialog('destroy');
	        	taskSource_datagrid.datagrid('load');
	        	
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
function add(row) {
	var _url = ctx+"/taskSource/toMain";
	if(row) {
		_url = ctx+"/taskSource/toMain?id="+row.id;
	}
	taskSource_dialog = $('<div/>').dialog({
    	title : "来源信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	taskSource_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	taskSource_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	taskSource_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	taskSource_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = taskSource_datagrid.datagrid('getSelected');
    if (row) {
    	add(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {
    var row = taskSource_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/taskSource/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	taskSource_datagrid.datagrid('load');
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

function details(){
    var row = taskSource_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row) {
	source_dialog = $('<div/>').dialog({
    	title : "来源详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/taskSource/details/"+row.id,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	source_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	source_dialog.dialog('destroy');
        }
    });
}

