/**
 * 项目进程相关
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
        onClickRow: function(index, row) {
        	 $("#projectId").val(row.PROJECT_ID);
        	 $("#procInstId").val(row.PROC_INST_ID);
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

//编辑
function edit() {
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	showProject(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//项目详情
function details(){
    //var row = project_datagrid.datagrid('getSelected');
    //if (row) {
    //	showDetailsProject(row);
    //} else {
     //   $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    //}
	var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	var name="项目进度详情";
    	window.parent.addTab(name,  ctx+"/projectProcess/toDetails?projectId="+$("#projectId").val()+"&procInstId="+$("#procInstId").val());	//easyui添加Tab
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//显示项目详情窗口
function showDetailsProject(row) {
    //弹出对话窗口
	var id = row.projectId;
    project_dialog = $('<div/>').dialog({
    	title : "项目详情",
		top: 20,
		width : fixWidth(0.95),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/projectProcess/toDetails",
        onLoad: function () {
        	$("#detailsRemark").kindeditor({readonlyMode : true});
        },
        buttons:[
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


