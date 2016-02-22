/**
 * 立项相关
 */

var project_datagrid;
var project_form;
var project_dialog;


$(function() {
	//数据列表
    project_datagrid = $('#project_datagrid').datagrid({
        url: ctx+"/project/getList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		fitColumns:true,
		sortName: 'PROJECT_DATE',
		sortOrder: 'desc',
        columns : [ 
            [ 
              {field : 'PROJECT_NAME',title : '项目名称',width : fixWidth(0.3),align:'left',halign:'center',sortable: true},
              {field : 'PROJECT_DATE',title : '立项日期',width : fixWidth(0.1),align : 'center',sortable: true,
            	  formatter: function(value, row) {
            		  return moment(value).format("YYYY-MM-DD");
            	  }
              },
              {field : 'PROJECT_TYPE_NAME', title : '项目类型', width : fixWidth(0.1),align : 'center',sortable: true},
              {field : 'PROVINCE',title : '所属省份',width : fixWidth(0.1),align : 'center',sortable: true},
              {field : 'COMPANY_NAME', title : '签约公司', width : fixWidth(0.1),align : 'center',sortable: true},
              {field : 'IS_CONTRACT', title : '已签合同', width : fixWidth(0.1),align : 'center',sortable: true,
            	  formatter: function(value, row) {
            		  if(value == 1) {
            			  return "是";
            		  } else {
            			  return "否";
            		  }
            	  }
              },
              {field : 'PROC_INST_ID',title : '当前节点',width : fixWidth(0.1),align : 'center',
              	formatter:function(value, row){
              		return "<a class='trace' onclick=\"graphTrace('"+value+"')\" id='diagram' href='#' title='点击查看'>"+row.TASK_NAME+"</a>";
				}
              }
    	    ] 
        ],
        onDblClickRow: function(index, row) {
        	showDetailsProject(row);
        },
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            project_datagrid.datagrid('reload',obj); 
	    }
	});
});
//高级查询 delRow
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(project_datagrid, "#gradeSearch", "/project/projectSearch");
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
    project_form = $('#projectForm').form({
        url: ctx+"/project/saveOrUpdate",
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
	    success: function (result) {
	        $.messager.progress('close');
	        var json = $.parseJSON(result);
	        if (json.status) {
	        	project_dialog.dialog("refresh",ctx+"/project/toMain?projectId="+json.data);
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}


//显示添加立项信息
function showProject(row) {
	var url_ = ctx+"/project/toMain";
	if(row) {
		url_ = ctx+"/project/toMain?projectId="+row.PROJECT_ID;
	}
    project_dialog = $('<div/>').dialog({
    	title : "立项信息",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: url_,
        onLoad: function () {
            formInit(row);
            if($("#projectId").val() == '') {
            	$("#ok").linkbutton("disable");
            } else {
            	$("#ok").linkbutton("enable");
            }
            if($("#isComplete").val() == 1) {
            	$("#ok").linkbutton("disable");
            }
            var projectType = $('#productTypeName').val();
            if(projectType.indexOf("百胜") != -1) {
	    		$("#projectManager_name, #areaManager_name").textbox('disable');
	    		$("#projectManager_name, #areaManager_name").textbox({prompt: '无需选择'});
	    	} else {
	    		$("#projectManager_name,#areaManager_name").textbox('enable');
	    		$("#projectManager_name").textbox({prompt: '选择项目经理'});
	    		$("#areaManager_name").textbox({prompt: '选择区域总经理'});
	    	}
            if(row) {
            	if(row.IS_COMPLETE==1){
            		$("#ok").linkbutton("disable");
            		$('#ok').tooltip({
    					position: 'top',
    					content: '<span style="color:#fff">立项已完成，不可重复完成</span>',
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
                text: '暂存',
                iconCls: 'icon-save',
                id: 'save',
                handler: function () {
                    project_form.submit();
                }
            },
            {
            	text: '立项完成',
            	iconCls: 'icon-save',
            	id: 'ok',
            	handler: function () {
            		$.messager.confirm('确认提示！', '确认完成此立项任务并进入销售流程吗？', function (result) {
                        if (result) {
                        	project_form.form('submit', {
    	            		 	url: ctx+"/project/complete",
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
    	            	            	project_dialog.dialog('destroy');
    	            		        	project_datagrid.datagrid('reload');
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
                	project_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	KindEditor.remove('#remark');
                    project_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            project_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	showProject(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除项目
function del() {

    var row = project_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                var id = row.PROJECT_ID;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/project/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                            project_datagrid.datagrid('load');	// reload the project data
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
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	showDetailsProject(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示项目详情窗口
function showDetailsProject(row) {
    //弹出对话窗口
	var id = row.PROJECT_ID;
    project_dialog = $('<div/>').dialog({
    	title : "项目详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/project/toDetails?id="+id,
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
			{
			    text: '打印预览',
			    iconCls: 'icon-print',
			    handler: function(){
			    	LODOP=getLodop();
			    	createPrintPagetwo('details_project');
			    	LODOP.PREVIEW();
			    }
			},
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    project_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            project_dialog.dialog('destroy');
        }
    });
}

