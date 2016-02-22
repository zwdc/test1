/**
 * 合作伙伴
 */

var partner_datagrid;
var partner_form;
var partner_dialog;


$(function() {
	//数据列表
	partner_datagrid = $('#partner_datagrid').datagrid({
        url: ctx+"/partner/getList?tType=1",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field: 'code', title: '编码', width: fixWidth(0.1), align: 'center', sortable: true},
              {field: 'cType', title: '公司类型', width: fixWidth(0.1), align: 'center', sortable: true,
            	  formatter: function(value, row) {
            		  switch( value ){
	      				case 1 :
	      					return "供应商" ;
	      				case 2:
	      					return "外贸公司" ;
	      				case 3:
	      					return "厂家" ;
	      				case 4 :
	      					return "银行" ;
	      				case 5:
	      					return "物流" ;
	      				case 6 :
	      					return "商检" ;
	      				case 7:
	      					return "海关" ;
	      				case 0:
	      					return "其他" ;
	      			}
            	  }  
              },
              {field: 'name', title: '公司名称', width: fixWidth(0.3), align: 'center', sortable: true},
              {field: 'address', title: '公司地址', width: fixWidth(0.3), align: 'center'},
              {field: 'contactName', title: '联系人', width: fixWidth(0.1), align: 'center',
            	  formatter: function(value, row) {
            		  if(value.indexOf(',') == -1) {
            			  return value;
            		  } else {
            			  return value.substring(0, value.indexOf(','));
            		  }
            	  }
              },
              {field: 'phone', title: '联系电话', width: fixWidth(0.1), align: 'center',
            	  formatter: function(value, row) {
            		  if(value.indexOf(',') == -1) {
            			  return value;
            		  } else {
            			  return value.substring(0, value.indexOf(','));
            		  }
            	  }
              }
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
            partner_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(partner_datagrid, "#gradeSearch", "/partner/gradeSearch");
}

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit() {
	partner_form = $('#partnerForm').form({
        url: ctx+"/partner/saveOrUpdate",
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
	        	partner_dialog.dialog('destroy');
	        	partner_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


function showPartner(row) {
	var url_ = ctx+"/partner/toMain?tType=1";
	if(row) {
		url_ = ctx+"/partner/toMain?tType=1&infoId="+row.infoId;
	}
	partner_dialog = $('<div/>').dialog({
    	title : "合作伙伴信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit();
        },
        buttons: [
            {
                text: '保存',
                is: 'save',
                iconCls: 'icon-save',
                handler: function () {
                	partner_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	partner_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	partner_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	partner_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = partner_datagrid.datagrid('getSelected');
    if (row) {
    	showPartner(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除部门信息
function del() {
    var row = partner_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/partner/delete?infoId='+row.infoId,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	partner_datagrid.datagrid('load');	// reload the project data
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
    var row = partner_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row){
	partner_dialog = $('<div/>').dialog({
    	title : "合作伙伴详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/partner/details?tType=1&infoId="+row.infoId,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	partner_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	partner_dialog.dialog('destroy');
        }
    });
}

