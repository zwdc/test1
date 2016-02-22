/**
 * 销售订单
 */

var salesOrder_datagrid;
var salesOrder_form;
var salesOrder_dialog;


$(function() {
    salesOrder_datagrid = $('#salesOrder_datagrid').datagrid({
        url: ctx+"/salesOrder/getList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		pageSize: 20,
		rownumbers:true,
		fitColumns:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap: false,
        columns : [ 
            [ 
              {field : 'projectName',title : '项目名称',width : fixWidth(0.3),align : 'left', halign: 'center',sortable: true},
              {field : 'salesNo',title : '销售合同号',width : fixWidth(0.2),align : 'center',sortable: true},
              {field : 'salesPrice',title : '销售价格',width : fixWidth(0.1),align : 'right', halign : 'center',
                	formatter:function(value, row){
                		return jqueryUtil.formatNumber(value);
    				}
              },
              {field : 'productPrice',title : '销售指导价',width : fixWidth(0.1),align : 'right', halign : 'center',
	              	formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
					}
              }
              /*{field : 'status',title : '状态',width : fixWidth(0.1),align : 'center',sortable: true,
	              	formatter:function(value, row){
	              		switch (value) {
							case "PROCUREMENT":
								return "<span class='text-warning'>采购中</span>";
							case "IN_BOUNDABLE":
								return "<span class='text-success'>采购完毕，可入库。</span>";
							case "IN_BOUNDED":
								return "<span class='text-success'>已入库</span>";
							case "OUT_BOUNDABLE":
								return "<span class='text-success'>可出库</span>";
							case "WAIT_FOR_OPERATION":
								return "<span class='text-danger'>订单待检验</span>";
							case "OUT_BOUNDED":
								return "<span class='text-success'>已出库</span>";
							default:
								break;
						}
	              		
	            		if(value == "PROCUREMENT"){
	            			return "采购中";
	            		} else {
	            			return "<span class='text-danger'>未出库</span>";
	            		}
					}
              }*/
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
            salesOrder_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(salesOrder_datagrid, "#gradeSearch", "/sales/salesSearch");
}

//修正宽高
function fixHeight(percent) {  
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//详情
function details(){
    var row = salesOrder_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示详情窗口
function showDetails(row) {
    salesOrder_dialog = $('<div/>').dialog({
    	title : "销售订单详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        inline: false,
        zIndex: 9999,
        href: ctx+"/salesOrder/toMain?salesId="+row.salesId+"&projectId="+row.projectId,
        onLoad: function () {
        	//$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    salesOrder_dialog.dialog('destroy');
                    salesOrder_datagrid.datagrid('reload');
                }
            }
        ],
        onClose: function () {
            salesOrder_dialog.dialog('destroy');
            salesOrder_datagrid.datagrid('reload');
        }
    });
}


