/**
 * 公司页面相关
 */

var company_datagrid;
var company_form;
var company_dialog;


$(function() {
	//数据列表
	company_datagrid = $('#company_datagrid').datagrid({
        url: ctx+"/company/listCompany",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '公司名称',width : fixWidth(0.2),align : 'left',sortable: true},
              {field : 'address',title : '地址',width : fixWidth(0.3),align : 'center',sortable: true},
              {field : 'phone',title : '联系电话',width : fixWidth(0.2),align : 'center',sortable: true},
              {field : 'note', title : '说明', width : fixWidth(0.3),align : 'center'}
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
            company_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(company_datagrid, "#gradeSearch", "/company/companySearch");
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
	company_form = $('#companyForm').form({
        url: ctx+"/company/saveOrUpdate",
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
	        	company_dialog.dialog('destroy');
	        	company_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加公司信息窗口
function showCompany(row) {
    //弹出对话窗口
    company_dialog = $('<div/>').dialog({
    	title : "公司信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/company/toAdd",
        onLoad: function () {
            formInit(row);
            if (row) {
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	company_form.form('load', row);  //通过row初始化表单中的数据
            	//添加操作时标题显示添加，隐藏修改
            	$("#add").attr("style", 'display:none;');
            	$("#tdAdd").attr("style", 'display:none;');
            } else {
            	$("input[name=isCon]:eq(0)").attr("checked", 'checked');
            	//修改操作时标题显示修改，隐藏添加
            	$("#update").attr("style", 'display:none;');
            	$("#tdUpdate").attr("style", 'display:none;');
            }
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	company_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	company_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	company_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	company_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = company_datagrid.datagrid('getSelected');
    if (row) {
    	showCompany(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除公司
function del() {

    var row = company_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/company/delete?id='+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	company_datagrid.datagrid('load');	// reload the project data
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

//公司详情
function details(){
    var row = company_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsCompany(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示公司详情窗口
function showDetailsCompany(row) {
    //弹出对话窗口
	var id = row.id;
	company_dialog = $('<div/>').dialog({
    	title : "公司详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/company/toDetails/"+id,
        buttons: [
			{
			    text: '打印预览',
			    iconCls: 'icon-print',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPagetwo('company');
			    	LODOP.PREVIEW();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	company_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	company_dialog.dialog('destroy');
        }
    });
}

