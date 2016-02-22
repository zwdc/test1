/**
 * 开票申请
 */
var biddingMoney_datagrid;
var biddingMoney_form;
var biddingMoney_dialog;
$(function(){
	biddingMoney_datagrid=$('#biddingMoney_datagrid').datagrid({
		url:ctx+"/biddingMoney/listBiddingMoney",			//路径访问后台方法获取数据
		width:'auto',					//宽度自适应
		height:fixHeight(0.89),			//高度自适应
		pagination:true,				//显示底部分页栏
		rownumbers:true,				//显示行号
		border:false,					//边框
		singleSelect:true,				//只允许选中一行
		striped:true,					//隔行变色
		//列的集合
		columns:[
		    [
		     	{field:'projectName',title:'项目名称',width : fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'companyName',title:'付款单位',width : fixWidth(0.1),align:'center',halign:'center'},
		     	{field:'paymentMoney',title:'付款金额',width : fixWidth(0.1),align:'right',halign:'center',
		     		formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
		     	},
		     	{field:'btNo',title:'招标编号',width : fixWidth(0.1),align:'center',halign:'center'},
		     	{field:'collectionUnit',title:'收款单位',width : fixWidth(0.1),align:'center',halign:'center'},
		     	{field:'collectionAccountName',title:'收款账户名称',width : fixWidth(0.1),align:'center',halign:'center'}
		    ]
		],
        onDblClickRow: function(index, row) {
        	showDetailsBiddingMoney(row);
        },
		//页面项
		toolbar:"#toolbar"
	});
	//搜索框
	$("#searchbox").searchbox({
		menu:"#searchMenu",			//搜索类型的菜单
		prompt:'模糊查询',			//显示在输入框里的信息
		//函数当用户点击搜索按钮时调用
		searcher:function(value,name){
			var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
			var obj=eval('('+str+')');
			biddingMoney_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function biddingMoneySearchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function  biddingMoneySearch() {
	jqueryUtil.gradeSearch(biddingMoney_datagrid, "#biddingMoneySearch", "/biddingMoney/biddingMoneySearch");
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
	 biddingMoney_form = $('#biddingMoney_form').form({
		 	url: ctx+"/biddingMoney/saveOrUpdate",
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
	            	biddingMoney_dialog.dialog('destroy');//销毁对话框
	            	biddingMoney_datagrid.datagrid('reload');//重新加载列表数据
	            } 
	            $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
	        }
	    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showBiddingMoney(row) {
	var _url = ctx+"/biddingMoney/toMain";
	if (row) {
		_url = ctx+"/biddingMoney/toMain?id="+row.id;
		
	}
    //弹出对话窗口
	biddingMoney_dialog = $('<div/>').dialog({
    	title : "招投标款项信息",
    	top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            conversion();	//加载页面是转化大小写
            if (row) {
            	biddingMoney_form.form('load', row);  //通过row初始化表单中的数据
            } else {
            	$("input[name=locked]:eq(0)").attr("checked", 'checked');//状态 初始化值
            }
        },
        buttons: [
            {
                text: '保存',
                id: 'save',
                iconCls: 'icon-save',
                handler: function () {
                	biddingMoney_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	biddingMoney_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	biddingMoney_dialog.dialog('destroy');
        }
    });
}
//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = biddingMoney_datagrid.datagrid('getSelected');
    if (row) {
        showBiddingMoney(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//删除项目
function del() {

    var row = biddingMoney_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/biddingMoney/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	biddingMoney_datagrid.datagrid('load');
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
//发票详情
function detailsBiddingMoney(){
    var row = biddingMoney_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsBiddingMoney(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示发票详情窗口
function showDetailsBiddingMoney(row) {
    //弹出对话窗口
	var id = row.id;
	biddingMoney_dialog = $('<div/>').dialog({
    	title : "招投标款项详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/biddingMoney/toDetails/"+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode: true});
        },
        buttons: [
			{
			    text: '打印预览',
			    iconCls: 'icon-print',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPagetwo('details_biddingMoney');
			    	LODOP.PREVIEW();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	biddingMoney_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	biddingMoney_dialog.dialog('destroy');
        }
    });
}