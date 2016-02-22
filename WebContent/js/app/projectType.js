/**
 * 项目类型
 */

var projectType_datagrid;
var projectType_form;
var projectType_dialog;


$(function() {
	//数据列表
	projectType_datagrid = $('#projectType_datagrid').datagrid({
        url: ctx+"/projectType/listProjectType",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '项目类型名称',width : fixWidth(0.2),align:'left',halign:'center'},
              {field : 'remark', title : '备注说明', width : fixWidth(0.8),align : 'center'}
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetailsProjectType(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            projectType_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(projectType_datagrid, "#gradeSearch", "/projectType/projectTypeSearch");
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
    projectType_form = $('#projectTypeForm').form({
        url: ctx+"/projectType/saveOrUpdate",
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
	        }
	        return isValid;
	    },
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	projectType_dialog.dialog('destroy');
	        	projectType_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加项目类型信息
function showProjectType(row) {
	projectType_dialog = $('<div/>').dialog({
    	title : "项目类型信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/projectType/toMain",
        onLoad: function () {
            formInit(row);
            if(row){
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	projectType_form.form('load', row);  //通过row初始化表单中的数据
            }
            $("input[name=isCon]:eq(0)").attr("checked", 'checked');
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	projectType_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	projectType_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	projectType_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	projectType_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = projectType_datagrid.datagrid('getSelected');
    if (row) {
    	showProjectType(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除项目类型信息
function del() {

    var row = projectType_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/projectType/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	projectType_datagrid.datagrid('load');	// reload the project data
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

//项目详情
function details(){
    var row = projectType_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsProjectType(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示项目类型详情窗口
function showDetailsProjectType(row) {
    //弹出对话窗口
	var id = row.id;
	projectType_dialog = $('<div/>').dialog({
    	title : "项目类型详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/projectType/toDetails?id="+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	projectType_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	projectType_dialog.dialog('destroy');
        }
    });
}

