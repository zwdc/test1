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
			  {field: 'feedbackStartDate', title: '反馈期间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
					  formatter:function(value,row){
						  return moment(value).format("MM月DD日")+"-"+moment(row.feedbackEndDate).format("MM月DD日");
					  }
				},
              {field: 'project', title: '牵头单位', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
					formatter:function(value,row,index){
	            		  return row.project.group.name;
					}
              },
              {field: 'createUser', title: '填报人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row,index){
            		  return value.name;
				}
              },
              {field: 'feedbackDate', title: '反馈时间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
				  }
              },
              {field: 'status', title: '状态', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value=="SUCCESS") {           			
                      	return "采用";
                      }else if(value=="FAIL"){
                    	  return "退回"; 
                      }else{
                    	  return "反馈中"; 
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
              
              {field: 'isDelay', title: '是否延期', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true},
              {field: 'refuseCount', title: '退回次数', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true},
              {field: 'type', title: '操作', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true}             
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
function formInit(row) {
	feedback_form = $('#feedback_form').form({
        url: ctx+"/feedback/saveOrUpdate",
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
    	showPage(row,"check");
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//编辑
function edit() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	showPage(row,"edit");
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//查看反馈详情
function details(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	showPage(row,"detail");
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//添加
function add(){
    showPage(null,"add");
}

//显示页面
function showPage(row,action) {
	var _url = ctx+"/feedback/toMain?action="+action;
	if(row) {
		_url = ctx+"/feedback/toMain?action="+action+"&id="+row.id;
	}
	if(action=="detail"){
		feedback_dialog = $('<div/>').dialog({
	    	title : "反馈记录详情",
			top: 20,
			width : fixWidth(0.8),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: _url,
	        onLoad: function () {
	            formInit(row);
	        },
	        buttons: [
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	source_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	source_dialog.dialog('destroy');
	        }
	    });
	}else{
		feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: _url,
	        onLoad: function () {
	            formInit(row);
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
	
}

//删除
function del() {
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


