/**
 * 任务类型
 */

var taskType_datagrid;
var taskType_form;
var taskType_dialog;

$(function() {
	//数据列表
	taskType_datagrid = $('#taskType_datagrid').datagrid({
        url: ctx+"/taskType/getList",
        width : 'auto',
		height : fixHeight(1),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '类型名称',width : fixWidth(0.4),align : 'left',sortable: true},
              {field : 'id',title : '类型ID',width : fixWidth(0.4),align : 'left',sortable: true},
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
            taskType_datagrid.datagrid('reload',obj); 
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
	taskType_form = $('#taskTypeForm').form({
        url: ctx+"/taskType/saveOrUpdate",
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
	        	taskType_dialog.dialog('destroy');
	        	taskType_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

//显示添加部门信息窗口
function add(row) {
    //弹出对话窗口
	taskType_dialog = $('<div/>').dialog({
    	title : "部门信息",
		top: 20,
		width : fixWidth(0.5),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/taskType/toMain",
        onLoad: function () {
            formInit(row);
            if (row) {
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	taskType_form.form('load', row);  //通过row初始化表单中的数据
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
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	taskType_form.form('clear');
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

//编辑
function edit() {
    var row = taskType_datagrid.datagrid('getSelected');
    if (row) {
    	add(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除部门信息
function del() {
    var row = taskType_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/taskType/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	taskType_datagrid.datagrid('load');	// reload the project data
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

