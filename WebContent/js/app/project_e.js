/**
 * 办理人 待签收列表
 */
var project_datagrid;
var project_form;
var project_dialog;
$(function(){
	project_datagrid=$('#project_datagrid').datagrid({
		url:ctx+"/project/getList?type=3",	
		width:'auto',					
		height:fixHeight(1),			
		pagination:true,				
		rownumbers:true,				
		border:false,					
		singleSelect:true,				
		striped:true,					
		columns:[
		    [
		     	{field:'URGENCY',title:'急缓程度',width:fixWidth(0.1),align:'center',sortable:true,
					formatter:function(value,row){
					  switch (value) {
					  	case 0: return "特提";
					  	case 1: return "特急";
					  	case 2: return "加急";
					  	case 3: return "平急";
					  } 
				 }
				},
		     	{field:'TITLE',title:'任务标题',width:fixWidth(0.2),align:'left',halign:'center'},
		     	{field:'SOURCE_NAME',title:'任务来源',width:fixWidth(0.2),align:'center'},
		     	{field:'GROUP_NAME',title:'承办单位',width:fixWidth(0.1),align:'center'},
		     	{field:'USER_NAME',title:'办理人',width:fixWidth(0.1),align:'center'},
		     	{field:'END_TASK_DATE',title:'办结时限',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
		     			return moment(value).format("YYYY-MM-DD HH:mm:ss");
		     		}
		     	},
		     	{field:'FREQUENCY_NAME',title:'反馈频度',width:fixWidth(0.1),align:'center'},
		     	{field: 'STATUS',title: '状态',width:fixWidth(0.1),align:'center', halign:'center',sortable:true,
	            	  formatter:function(value, row){
	            		  switch (value) {
							case "REFUSE_CLAIM":
								return "<span class='text-danger'>拒绝签收</span>";
							case "WAIT_FOR_CLAIM":
								return "<span class='text-warning'>待签收</span>";
							case "CLAIMED":
								return "<span class='text-success'>已签收</span>";
							case "APPLY_FINISHED":
								return "<span class='text-primary'>申请办结</span>";
							case "APPROVAL_SUCCESS":
								return "<span class='text-success'>审批通过</span>";
							case "APPROVAL_FAILED":
								return "<span class='text-danger'>审批失败</span>";
							case "REFUSE_FAILED":
								return "<span class='text-primary'>拒签失败</span>";
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
	              }
		    ]
		],
        onDblClickRow: function(index, row) {
        	showDetails(row);
        },
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
			project_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function searchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function gradeSearch() {
	jqueryUtil.gradeSearch(project_datagrid, "#invoiceSearch", "/invoice/invoiceSearch");
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
	 project_form = $('#project_form').form({
		 	url: ctx+"/project/update",
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
	            	project_dialog.dialog("refresh",ctx+"/project/toProject/claim?projectId="+json.data);
	            	project_datagrid.datagrid('reload');//重新加载列表数据
	            } 
	            $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
	        }
	    });
}


function details(){
    var row = project_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row) {
	project_dialog = $('<div/>').dialog({
    	title : "任务详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/project/toProject/details?projectId="+row.ID,
        buttons: [
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
