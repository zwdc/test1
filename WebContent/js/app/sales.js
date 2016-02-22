/**
 * 销售审批表
 */

var sales_datagrid;
var sales_form;
var sales_dialog;


$(function() {
    sales_datagrid = $('#sales_datagrid').datagrid({
        url: ctx+"/sales/getList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		//fitColumns:true,
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
              },
	     	  {field: 'status',title: '状态',width: fixWidth(0.1),align: 'center', halign: 'center',sortable: true,
            	  formatter:function(value, row){
            		  switch (value) {
						case "APPROVAL_SUCCESS":
							return "<span class='text-success'>审批通过</span>";
						case "APPROVAL_FAILED":
							return "<span class='text-danger'>审批失败</span>";
						case "WAITING_FOR_APPROVAL":
							return "<span class='text-warning'>待申请审批</span>";
						case "PENDING":
							return "<span class='text-primary'>审批中</span>";
						case "REAPPROVAL":
							return "<span class='text-danger'>需要重新审批</span>";
						default:
							return "";
					  }
    			  }
              },
              {field : 'processInstanceId',title : '当前节点',width : fixWidth(0.1),align : 'center',
                	formatter:function(value, row){
                		return "<a class='trace' onclick=\"graphTrace('"+value+"')\" id='diagram' href='#' title='点击查看'>"+row.taskName+"</a>";
  				}
              }
    	    ] 
        ],
        view: detailview,
    	detailFormatter:function(index,row){
    		return '<div class="ddv" style="padding:5px 0"></div>';
    	},
    	onExpandRow: function(index, row){
    		var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
    		ddv.panel({
    			border: false,
    			cache: false,
    			href: ctx+'/salesProduct/getSalesProduct?salesId='+row.salesId,
    			onLoad: function(){
    				$('#sales_datagrid').datagrid('fixDetailRowHeight',index);
    			}
    		});
    		$('#sales_datagrid').datagrid('fixDetailRowHeight',index);
    	},
    	onDblClickRow: function(index, row) {
        	showDetailsSales(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            sales_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(sales_datagrid, "#gradeSearch", "/sales/salesSearch");
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
    sales_form = $('#form').form({
    	url: ctx+"/sales/saveOrUpdate",
        onSubmit: function () {
	        $.messager.progress({
	            title: '提示信息！',
	            text: '数据处理中，请稍后....'
	        });
	        debugger;
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
	        	sales_dialog.dialog("refresh",ctx+"/sales/toMain?id="+json.data);
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加修改窗口
function showSales(row) {
	var url_ = ctx+"/sales/toMain";
	if(row) {
		url_ = ctx+"/sales/toMain?id="+row.salesId;
	}
	//修复最小化后无法还原
	/*if(typeof(sales_dialog) != "undefined" && typeof(sales_dialog.dialog('options')) != "undefined"){
		if(sales_dialog.dialog('options').minimized){
			sales_dialog.dialog('open');
		}
	} else {
		
	}*/
	var op;
	sales_dialog = $('<div/>').dialog({
		id: "main_sales",
		title : "销售审批表",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
		modal: true,
		minimizable: true,
		maximizable: true,
		href: url_,
		onLoad: function () {
			formInit();
			if($("#salesId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
			if(row) {
            	if(row.status == "APPROVAL_SUCCESS") {
            		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
		        	$("#ok").linkbutton("disable");
		        	op = "reapproval";
		        	$('#save').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">保存后需重新审批</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "PENDING") {
            		//审批中，不允许修改或提交审批
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">审批中</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "APPROVAL_FAILED") {
            		//审批失败时，要在待办事项中去操作。
            		$("#save").linkbutton("disable");
    	        	$("#ok").linkbutton("disable");
    	        	$("#addProduct").attr("disabled", "disabled");
    	        	
    	        	$('#save,#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">请在待办事项中处理</span>',
    					onShow: function(){
    						$(this).tooltip('tip').css({
    							backgroundColor: '#666',
    							borderColor: '#565656'
    						});
    					}
    				});
            	} else if(row.status == "REAPPROVAL") {
            		$("#ok").linkbutton("enable");
            	}
            }
		},
		buttons: [
		          {
		        	  text: '保存',
		        	  iconCls: 'icon-save',
		        	  id: 'save',
		        	  handler: function () {
		        		  if(row) {
		                    	if(row.status == "APPROVAL_SUCCESS") {
		                    		$.messager.confirm('确认提示！', '此表单已经审批通过，您确定要保存后重新申请审批吗？', function (result) {
		                    			 if (result) {
		                    				 if(op == 'reapproval') {
		                             			row.status = 'REAPPROVAL'
		                             		 }
		                    				 sales_form.submit();
		                    			 }
		                    		});
		                    	} else {
		                    		sales_form.submit();
		                    	}
		                    } else {
		                    	sales_form.submit();
		                    }
		        	  }
		          },
		          {
		            	text: '提交审批',
		            	iconCls: 'icon-ok',
		            	id: 'ok',
		            	handler: function () {
		            		var url_ = ctx+"/sales/callApproval";
		            		$.messager.confirm('确认提示！', '确认提交表单进入审批流程吗？', function (result) {
		                        if (result) {
		                        	sales_form.form('submit', {
		    	            		 	url: url_,
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
		    	            		        	$("#ok").linkbutton("disable");
		    	            		        }
		    	            		        return isValid;
		    	            		    },
		    	            		    success: function (data) {
		    	            	            $.messager.progress('close');
		    	            	            var json = $.parseJSON(data);
		    	            	            if (json.status) {
		    	            	            	sales_dialog.dialog('destroy');
		    	            	            	sales_datagrid.datagrid('reload');
		    	            	            } 
		    	            	            $.messager.show({
		    	            					title : json.title,
		    	            					msg : json.message,
		    	            					timeout : 1000 * 2
		    	            				});
		    	            	        }
		    	            	    });
		                        }
		                    });
		            	}
		            },
		          {
		        	  text: '重置',
		        	  iconCls: 'icon-reload',
		        	  handler: function () {
		        		  sales_form.form('reset');
		        	  }
		          },
		          {
		        	  text: '关闭',
		        	  iconCls: 'icon-cancel',
		        	  handler: function () {
		        		  sales_dialog.dialog('destroy');
		        		  sales_datagrid.datagrid('reload');
		        	  }
		          }
		          ],
		          onClose: function () {
		        	  sales_dialog.dialog('destroy');
		        	  sales_datagrid.datagrid('reload');
		          }
	});
	
}

//取消保存-删除已保存的产品和配件
function cancelSave() {
	
}

//编辑
function edit() {
    var row = sales_datagrid.datagrid('getSelected');
    if (row) {
    	showSales(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {

    var row = sales_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/sales/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {id : row.salesId},
                    success: function (data) {
                        if (data.status) {
                            sales_datagrid.datagrid('load');	// reload the sales data
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

//详情
function details(){
    var row = sales_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsSales(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示详情窗口
function showDetailsSales(row) {
    sales_dialog = $('<div/>').dialog({
    	title : "销售详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/sales/details/"+row.salesId,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons: [
			{
			    text: '打印预览',
			    iconCls: 'icon-print',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPageSales();
			    	LODOP.PREVIEW();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    sales_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            sales_dialog.dialog('destroy');
        }
    });
}
//打印
//将详细页面数据
function createPrintPageSales(){
	refreshData();
    LODOP=getLodop();  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单二");
	LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
	LODOP.SET_PRINT_MODE("FULL_HEIGHT_FOR_OVERFLOW",true);
	var css="<style>table,td { border: 1 solid #000000;border-collapse:collapse };table,th { border: 1 solid #000000;border-collapse:collapse }</style>";
	var tab=document.getElementById('sales');
	var table="<table>";
	for(var i=0 ;i<tab.rows.length;i++)
    {
		table+="<tr>"
        for(var j=0;j<tab.rows[i].cells.length;j++) 
        {
	        if(i==3){
	        	var onetable="<table>";
	        	var onetab=document.getElementById('detailsProduct');
	        	for(var a=0 ;a<onetab.rows.length;a++)
	            {
	        		onetable+="<tr>"
	                for(var b=0;b<onetab.rows[a].cells.length;b++) 
	                {
	                	onetable+="<td>"+onetab.rows[a].cells[b].innerHTML+"</td>";
	                }
	        		onetable+="</tr>"
	            }
	        	onetable+="</table>"
	        	table+="<td colspan='4'>"+onetable+"</td>";
	        }/*else if(i==7){
	        	var onetable="<table>";
	        	var onetab=document.getElementById('probe');
	        	for(var a=0 ;a<onetab.rows.length;a++)
	            {
	        		onetable+="<tr>"
	                for(var b=0;b<onetab.rows[a].cells.length;b++) 
	                {
	                	onetable+="<td>"+onetab.rows[a].cells[b].innerHTML+"</td>";
	                }
	        		onetable+="</tr>"
	            }
	        	onetable+="</table>"
	        	table+="<td colspan='4'>"+onetable+"</td>";
	        }else if(i==9){
	        	var onetable="<table>";
	        	var onetab=document.getElementById('available');
	        	for(var a=0 ;a<onetab.rows.length;a++)
	            {
	        		onetable+="<tr>"
	                for(var b=0;b<onetab.rows[a].cells.length;b++) 
	                {
	                	onetable+="<td>"+onetab.rows[a].cells[b].innerHTML+"</td>";
	                }
	        		onetable+="</tr>"
	            }
	        	onetable+="</table>"
	        	table+="<td colspan='4'>"+onetable+"</td>";
	        }*/else{
	        	if(tab.rows[i].cells.length==4){
	        		table+="<td>"+tab.rows[i].cells[j].innerHTML+"</td>";
	        	}else if(tab.rows[i].cells.length<2){
	        		table+="<td colspan='4'>"+tab.rows[i].cells[j].innerHTML+"</td>";
	        	}else{
	        		table+="<td>"+tab.rows[i].cells[j].innerHTML+"</td>";
	        	}
	        	
	        }
	        
        }
		table+="</tr>"
    }
	table+="</table>"
	LODOP.ADD_PRINT_HTM(88,40,1000,500,css+table);
}

