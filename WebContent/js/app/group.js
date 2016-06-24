/**
 * 部门页面相关
 */

var group_datagrid;
var group_form;
var group_dialog;


$(function() {
	//数据列表
	group_datagrid = $('#group_datagrid').datagrid({
        url: ctx+"/group/listGroup",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '部门名称',width : fixWidth(0.15),align : 'center',sortable: true},
              {field : 'major_name',title : '主管市长',width : fixWidth(0.15),align : 'center',sortable: true},
              {field : 'major_phone',title : '联系方式',width : fixWidth(0.15),align : 'center',sortable: true},
              {field : 'leader_name',title : '办公室主任',width : fixWidth(0.15),align : 'center',sortable: true},
              {field : 'leader_phone',title : '联系方式',width : fixWidth(0.15),align : 'center',sortable: true},
              {field : 'type',title : '部门类型',width : fixWidth(0.15),align : 'center',sortable: true}
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
            group_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(group_datagrid, "#gradeSearch", "/group/groupSearch");
}

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row) {
	group_form = $('#groupForm').form({
        url: ctx+"/group/saveOrUpdate",
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
	        	group_dialog.dialog('destroy');
	        	group_datagrid.datagrid('load');
	        	
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
function showGroup(row) {
    //弹出对话窗口
	group_dialog = $('<div/>').dialog({
    	title : "部门信息",
		top: 20,
		width : fixWidth(0.6),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/group/toMain",
        onLoad: function () {
            formInit(row);
            if (row) {
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	group_form.form('load', row);  //通过row初始化表单中的数据
            } else {
            	$("input[name=isCon]:eq(0)").attr("checked", 'checked');
            }
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	group_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	group_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	group_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	group_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = group_datagrid.datagrid('getSelected');
    if (row) {
    	showGroup(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除部门信息
function del() {

    var row = group_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/group/delete?id='+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	group_datagrid.datagrid('load');	// reload the project data
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

//组详情
function details(){
    var row = group_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsGroup(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示组详情窗口
function showDetailsGroup(row) {
    //弹出对话窗口
	var id = row.id;
	group_dialog = $('<div/>').dialog({
    	title : "部门详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/group/toDetails/"+id,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	group_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	group_dialog.dialog('destroy');
        }
    });
}

