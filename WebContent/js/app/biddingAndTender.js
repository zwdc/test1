/**
 * 招投标
 */

var bt_datagrid;
var bt_form;
var bt_dialog;


$(function() {
	//数据列表
    bt_datagrid = $('#bt_datagrid').datagrid({
        url: ctx+"/biddingAndTender/getList",
        width: 'auto',
		height: fixHeight(0.89),
		pagination: true,
		rownumbers: true,
		border: false,
		singleSelect: true,
		striped: true,
        columns: [ 
            [ 
              {field : 'projectName',title : '项目名称',width : fixWidth(0.2),align:'left',halign:'center'},
              {field : 'btNO',title : '招标编号',width : fixWidth(0.08),align : 'center',sortable: true},
              {field : 'btType',title : '招标类型',width : fixWidth(0.08),align : 'center',sortable: true},
              {field : 'biddingCompany',title : '招标公司',width : fixWidth(0.08),align : 'center'},
              {field : 'bidOpeningDate', title : '开标日期', width : fixWidth(0.1),align : 'center',
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD HH:mm");
					 }
	     	  },
              {field : 'acceptedMoney', title : '中标金额', width : fixWidth(0.1), align : 'right', halign: 'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
		      },
              {field : 'acceptedServiceMoney', title : '中标服务费', width : fixWidth(0.1),align : 'right', halign: 'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
		      },
              {field : 'isEnd', title : '招投标状态', width : fixWidth(0.1),align : 'center',
		     		formatter:function(value, row){
	            		if(value == 0) {
	            			return "<span class='text-success'>未结束</span>";
	            		} else if(value == 1){
	            			return "<span class='text-danger'>已结束</span>";
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
            bt_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(bt_datagrid, "#gradeSearch", "/biddingAndTender/gradeSearch");
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
    bt_form = $('#btForm').form({
        url: ctx+"/biddingAndTender/saveOrUpdate",
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
	    success: function (result) {
	        $.messager.progress('close');
//	        var json = $.parseJSON(result);	//$.parseJSON()只支持标准的JSON,key和value都带引号的就是标准的;而且必须是双引号，单引号也不行. jsonStr = {"name": "CodePlayer"};
	        var json = eval("("+result+")");
	        if (json.status) {
	        	bt_dialog.dialog('destroy');
	        	bt_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


function showBT(row) {
	var url_ = ctx+"/biddingAndTender/toMain";
	if(row) {
		url_ = ctx+"/biddingAndTender/toMain?id="+row.id;
	}
    bt_dialog = $('<div/>').dialog({
    	title : "招投标信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit();
            if(row){
            	if(row.isEnd == 1){
            		$("#save").linkbutton("disable");
            		$("#ok").linkbutton("disable");
            	}
            } else {
            	$("input[name=isReceivedInvoice]:eq(0)").attr("checked", 'checked');
            	$("input[name=tenderBookInvoice]:eq(0)").attr("checked", 'checked');
            }
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                    bt_form.submit();
                }
            },
            {
            	text: '招投标项目结束',
            	iconCls: 'icon-ok',
            	id: 'ok',
            	handler: function () {
            		$.messager.confirm('确认提示！', '结束后此招投标信息将不能修改，您确认此操作吗?', function (result) {
                        if (result) {
                        	$("#isEnd").val(1);	//结束招投标
                        	bt_form.submit();
                        }
                    });
            	}
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	bt_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                    bt_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            bt_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = bt_datagrid.datagrid('getSelected');
    if (row) {
    	showBT(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {

    var row = bt_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/biddingAndTender/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id: row.id},
                    success: function (data) {
                        if (data.status) {
                            bt_datagrid.datagrid('load');	// reload the project data
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
    var row = bt_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示项目详情窗口
function showDetails(row) {
    //弹出对话窗口
    bt_dialog = $('<div/>').dialog({
    	title : "招投标详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/biddingAndTender/details?id="+row.id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    bt_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            bt_dialog.dialog('destroy');
        }
    });
}

