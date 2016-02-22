/**
 * 设备类型
 */

var productType_datagrid;
var productType_form;
var productType_dialog;


$(function() {
	var name='';
	if(GetQueryString("type")==1)
		name="设备类型名称";
	else
		name="产品类型名称";
	//数据列表
	productType_datagrid = $('#productType_datagrid').datagrid({
        url: ctx+"/productType/listProductType?type="+GetQueryString("type"),
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : ''+name+'',width : fixWidth(0.2),align:'left',halign:'center'},
              {field : 'remark', title : '备注说明', width : fixWidth(0.8),align : 'center'}
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetailsProductType(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            productType_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(productType_datagrid, "#gradeSearch", "/productType/productTypeSearch?type="+GetQueryString("type"));
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
    productType_form = $('#productTypeForm').form({
        url: ctx+"/productType/saveOrUpdate",
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
	        	productType_dialog.dialog('destroy');
	        	productType_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

var title='';
if(GetQueryString("type")==1)
	title="设备类型";
else
	title="产品类型";
//显示添加设备类型信息
function showProductType(row) {
	productType_dialog = $('<div/>').dialog({
    	title : ""+title+"信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/productType/toMain?type="+GetQueryString("type"),
        onLoad: function () {
            formInit(row);
            if(row){
            	row.createDate = moment(row.createDate).format("YYYY-MM-DD HH:mm:ss");
            	productType_form.form('load', row);  //通过row初始化表单中的数据
            }
            $("input[name=isCon]:eq(0)").attr("checked", 'checked');
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                	productType_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	productType_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                	productType_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	productType_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = productType_datagrid.datagrid('getSelected');
    if (row) {
    	showProductType(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除设备类型信息
function del() {

    var row = productType_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/productType/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	productType_datagrid.datagrid('load');	// reload the project data
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

//设备详情
function details(){
    var row = productType_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsProductType(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示设备类型详情窗口
function showDetailsProductType(row) {
    //弹出对话窗口
	var id = row.id;
	productType_dialog = $('<div/>').dialog({
    	title : ""+title+"详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/productType/toDetails?id="+id+"&type="+GetQueryString("type"),
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	productType_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	productType_dialog.dialog('destroy');
        }
    });
}
//获取url参数方法
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

