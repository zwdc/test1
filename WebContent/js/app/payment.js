/**
 * 付款相关
 */

var payment_datagrid;
var payment_form;
var payment_dialog;

$(function() {
	//数据列表
	payment_datagrid = $('#payment_datagrid').datagrid({
        url: ctx+"/payment/getPaymentList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'projectName',title : '项目名称',width : fixWidth(0.2),align:'left',halign:'center',sortable: true},
              {field : 'procurementNo',title : '采购合同号',width : fixWidth(0.1),align : 'center'},
              {field : 'paymentMoney',title : '付款金额',width : fixWidth(0.1),align : 'center',sortable: true,
            	  formatter:function(value, row){
	            		return jqueryUtil.formatNumber(value);
            	  }
              },
              {field : 'paymentDate', title : '付款日期', width : fixWidth(0.15),align : 'center',sortable: true,
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD");
				  }  
              },
              {field : 'partnerInfoName', title : '收款单位', width : fixWidth(0.1),align : 'center'},
              {field : 'companyName', title : '付款单位', width : fixWidth(0.1),align : 'center'},
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
                		if(row.status == "WAITING_FOR_APPROVAL") {
                			return "-";
                		} else {
                			return "<a class='trace' onclick=\"graphTrace('"+value+"')\" id='diagram' href='#' title='点击查看'>"+row.taskName+"</a>";
                		}
  				}
              }
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetailsPayment(row);
        },
        toolbar: "#toolbar"
    });
	
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            payment_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(payment_datagrid, "#paymentSearch", "/payment/paymentSearch");
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
	 payment_form = $('#payment_form').form({
		 	url: ctx+"/payment/saveOrUpdate",
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
	            	payment_dialog.dialog("refresh" ,ctx+"/payment/toMain?id="+json.data.toString());
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
function showPayment(row) {
	var _url = ctx+"/payment/toMain";
	var op;
	if (row) {
		_url = ctx+"/payment/toMain?id="+row.id;
	}
	payment_dialog = $('<div/>').dialog({
    	title : "付款申请信息",
    	top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit();
            if($("#paymentId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            conversion();	//加载页面时转化大小写
            if(row) {
            	if(row.status == "APPROVAL_SUCCESS") {
            		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
		        	$("#ok").linkbutton("disable");
		        	$("#save").linkbutton("disable");
		        	$('#save,#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">审批通过，出纳已确认不可更改</span>',
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
                          				payment_form.submit();
                          			 }
                          		});
                          	} else {
                          		payment_form.submit();
                          	}
                          } else {
                        	  payment_form.submit();
                          }
                      }
                  },
                  {
                  	text: '提交审批',
                  	iconCls: 'icon-ok',
                  	id: 'ok',
                  	handler: function () {
                  		$.messager.confirm('确认提示！', '确认提交表单进入审批流程吗？', function (result) {
                              if (result) {
                            	  payment_form.form('submit', {
          	            		 	url: ctx+"/payment/callApproval",
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
          	            	            	payment_dialog.dialog('destroy');//销毁对话框
          	            	            	payment_datagrid.datagrid('reload');//重新加载列表数据
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
                      	payment_form.form('reset');
                      }
                  },
                  {
                      text: '关闭',
                      iconCls: 'icon-cancel',
                      handler: function () {
                          payment_dialog.dialog('destroy');
                      }
                  }
           ],
           onClose: function () {
               payment_dialog.dialog('destroy');
               payment_datagrid.datagrid('load');
           }
	});
}

//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = payment_datagrid.datagrid('getSelected');
    if (row) {
        showPayment(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//删除项目
function del() {

    var row = payment_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/payment/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	payment_datagrid.datagrid('load');
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
//付款详情
function detailsPayment(){
    var row = payment_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsPayment(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示付款详情窗口
function showDetailsPayment(row) {
    //弹出对话窗口
	var id = row.id;
	payment_dialog = $('<div/>').dialog({
    	title : "付款单详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/payment/toDetails/"+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        	 if(row) {
             	if(row.isCashier != 0) {
             		//审批通过后，可以修改但要重新审批，并且要求用户把修改的内容以红色字体写在备注中。
 		        	$("#print").linkbutton("disable");
 		        	$('#print').tooltip({
     					position: 'top',
     					content: '<span style="color:#fff">出纳确认后才能打印</span>',
     					onShow: function(){
     						$(this).tooltip('tip').css({
     							backgroundColor: '#666',
     							borderColor: '#565656'
     						});
     					}
     				});
             	}
        	 }
        },
        buttons: [
			{
				id:'print',
			    text: '打印',
			    iconCls: 'icon-print',
			    handler: function () {
			    	print();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	payment_dialog.dialog('destroy');
                }
            } 
        ],
        onClose: function () {
        	payment_dialog.dialog('destroy');
        }
    });
}
//打印
function print(){
	LODOP=getLodop();  
	LODOP.PRINT_INITA(-10,-5,763,533,"付款单");
	var imagesUrl =$("#webUrl").val().replace(new RegExp(/(\\)/g),'/');
	LODOP.ADD_PRINT_SETUP_BKIMG(imagesUrl);
	LODOP.SET_SHOW_MODE("BKIMG_WIDTH",767);
	LODOP.SET_SHOW_MODE("BKIMG_HEIGHT",399);
	LODOP.SET_SHOW_MODE("BKIMG_PRINT",true);
	LODOP.ADD_PRINT_TEXT(49,319,100,20,"付款单");
	LODOP.SET_PRINT_STYLEA(0,"FontName","微软雅黑");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,66,100,20,"项目名称");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,187,100,20,"采购合同号");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,279,100,20,"付款单位名称");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,374,100,20,"收款单位名称");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,464,100,20,"付款金额");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(87,601,100,20,"用途");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(120,48,147,42,document.getElementById("paymentProjectName").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(120,192,89,42,document.getElementById("paymentProcurementNo").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(120,285,100,42,document.getElementById("paymentCompanyName").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(120,381,101,42,document.getElementById("paymentPartnerInfoName").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(120,477,100,42,document.getElementById("paymentMoney").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(120,567,167,42,document.getElementById("purpose").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(170,64,100,20,"付款日期");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(171,184,100,20,"支付方式");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(171,281,100,20,"收款方开户行");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(171,373,100,20,"收款方账号");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(171,543,100,20,"大写");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(198,49,141,36,document.getElementById("paymentDate").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(198,194,100,36,document.getElementById("paymentModel").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(198,286,100,36,document.getElementById("bankName").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(198,380,100,36,document.getElementById("collectionAccount").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(198,475,264,37,jqueryUtil.formatCapital(document.getElementById("paymentMoney").value));
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.ADD_PRINT_TEXT(265,59,117,27,"备注");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	LODOP.ADD_PRINT_TEXT(238,193,549,68,document.getElementById("paymentRemark").value);
	LODOP.SET_PRINT_STYLEA(0,"FontSize",8);
	var css="<style>table {width:692px;font-size:12;line-height:24px};table,td { border: 1 solid #000000;border-collapse:collapse;text-align:center };table,th { border: 1 solid #000000;border-collapse:collapse }</style>";
	var approvalTable=document.getElementById("approvalTable").innerHTML;
	LODOP.ADD_PRINT_HTM(320,45,1000,500,css+approvalTable);
	LODOP.PREVIEW();
}