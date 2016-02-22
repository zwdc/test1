/**
 * 收款
 */

var collection_datagrid;
var collection_form;
var collection_dialog;


$(function() {
	//数据列表
	collection_datagrid = $('#collection_datagrid').datagrid({
        url: ctx+"/collection/listCollection",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'projectName',title : '项目名称',width : fixWidth(0.2),align:'left',halign:'center'},
              {field : 'salesNo', title : '销售合同号', width : fixWidth(0.1),align : 'center'},
              {field : 'collectionMoney',title : '收款金额',width : fixWidth(0.1),align:'right',halign:'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
              },
              {field : 'collectionDate', title : '收款时间', width : fixWidth(0.2),align : 'center',
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm");
				 }
              },
              {field : 'companyName',title : '收款单位',width : fixWidth(0.2),align:'center'},
              {field : 'paymentUnit', title : '付款单位', width : fixWidth(0.2),align : 'center'}
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetails(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            collection_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(collection_datagrid, "#gradeSearch", "/collection/collectionSearch");
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
	collection_form = $('#collection_form').form({
        url: ctx+"/collection/saveOrUpdate",
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
	        	collection_dialog.dialog('destroy');
	        	collection_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加收款信息
function showCollection(row) {
	var _url = ctx+"/collection/toMain";
	if (row != undefined && row.id) {
		_url =  ctx+"/collection/toMain?id="+row.id;
	}
	collection_dialog = $('<div/>').dialog({
    	title : "收款信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            $("input[name=isCon]:eq(0)").attr("checked", 'checked');
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	collection_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	collection_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	collection_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	collection_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = collection_datagrid.datagrid('getSelected');
    if (row) {
    	showCollection(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除收款信息
function del() {

    var row = collection_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/collection/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	collection_datagrid.datagrid('load');
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

//收款详情
function details(){
    var row = collection_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示收款详情窗口
function showDetails(row) {
    //弹出对话窗口
	var id = row.id;
	collection_dialog = $('<div/>').dialog({
    	title : "收款详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/collection/toDetails/"+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
			{
			    text: '打印预览',
			    iconCls: 'icon-print',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPagetwo('details_collection');
			    	LODOP.PREVIEW();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	collection_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	collection_dialog.dialog('destroy');
        }
    });
}

