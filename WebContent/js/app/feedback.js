/**
 * 反馈记录
 */

var feedback_datagrid;
var feedback_form;
var feedback_dialog;

$(function() {
	//数据列表
	feedback_datagrid = $('#feedback_datagrid').datagrid({
        url: ctx+"/feedback/getList",
        width : 'auto',
		height : fixHeight(1),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [
             [
              {field: 'warningLevel', title: '预警', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value=="1") {           			
                      	return "<img style=\"height: 15px;width: 15px;\" src=\"../images/yellow.png\"/>";
                      }else if(value=="2"){
                    	  return "<img style=\"height: 15px;width: 15px;\" src=\"../images/red.png\"/>"; 
                      }else{
                    	  return "无色"; 
                      }
            	  }
			},
			  {field: 'feedbackStartDate', title: '反馈期间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
					  formatter:function(value,row){
						  return moment(value).format("MM月DD日")+"-"+moment(row.feedbackEndDate).format("MM月DD日");
					  }
				},
              {field: 'groupName', title: '牵头单位', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'createUser', title: '填报人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'feedbackDate', title: '反馈时间', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row){
            		  if(value==null){
            			  return "--"
            		  }else{
            			  return moment(value).format("YYYY-MM-DD HH:mm:ss");
            		  }
            		 
				  }
              },
              {field: 'status', title: '状态', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value=="SUCCESS") {           			
                      	return "采用";
                      }else if(value=="FAIL"){
                    	  return "退回"; 
                      }else if(value=="RUNNING"){
                    	  return "反馈中"; 
                      }else{
                    	  return "未反馈";
                      }
            	  },
            	  styler:function(value){
            		  if (value=="SUCCESS") {           			
                          return 'background-color:green;color:white';
                        }else if(value=="FAIL"){
                      	  return 'background-color:orange;color:white';; 
                        }
              	  }
              },
              
              {field: 'delayCount', title: '延期次数', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true},
              {field: 'refuseCount', title: '退回次数', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true}  
        ]
             ]
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            feedback_datagrid.datagrid('reload',obj); 
	    }
	});
});

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row,url) {
	feedback_form = $('#feedback_form').form({
        url: url,
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
	        	feedback_dialog.dialog('destroy');
	        	feedback_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

//反馈审核—— 督查人员
function check(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息审核",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=check&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/checkFeedback");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//实施反馈—— 承办单位
function feedback(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "进行反馈",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=feedback&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveFeedback");
	        },
	        buttons: [
	            {
	                text: '暂存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	            	text: '申请审核',
	            	iconCls: 'icon-ok',
	            	id: 'ok',
	            	handler: function () {
	                	$.messager.confirm('确认提示！','确认提交表单进入反馈审核流程吗？',function(result){
	                		if(result){
	                			taskInfo_form.form('submit',{
	    	            		 	url: ctx+"/feedback/callApproval",
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
	    	            	            	taskInfo_dialog.dialog('destroy');//销毁对话框
	    	            	            	taskInfo_datagrid.datagrid('reload');//重新加载列表数据
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
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//编辑
function editFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息编辑",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=edit&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveOrUpdate");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//查看反馈详情
function detailsFeedback(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈记录详情",
			top: 20,
			width : fixWidth(0.8),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=detail&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/detail");
	        },
	        buttons: [
	  	          {
	  	                text: '关闭',
	  	                iconCls: 'icon-cancel',
	  	                handler: function () {
	  	                	feedback_dialog.dialog('destroy');
	  	                }
	  	            }
	  	        ],
	        onClose: function () {
	        	source_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//添加
function addFeedback(){
	feedback_dialog = $('<div/>').dialog({
    	title : "反馈信息添加",
		top: 20,
		width : fixWidth(0.9),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/feedback/toMain?action=add",
        onLoad: function () {
            formInit(null,ctx+"/feedback/saveOrUpdate");
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	feedback_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	feedback_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	feedback_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	feedback_dialog.dialog('destroy');
        }
    });
}


//删除
function delFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/feedback/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	feedback_datagrid.datagrid('load');
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


