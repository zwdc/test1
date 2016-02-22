/**
 * 管理运行中的流程
 */

var process_datagrid_runing;
var process_form;
var process_dialog;

$(function() {
	process_datagrid_runing = $("#process_running").datagrid({
        url: ctx+"/process/runningProcess",
        width : 'auto',
        height : fixHeight(0.85),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
//      pageList : [2, 5, 10, 15, 20 ],
//      pageSize:2,
		columns : [ 
		    [ 
                {field : 'id',title : '执行ID',width : fixWidth(0.2),align : 'center'},
                {field : 'processInstanceId',title : '流程实例ID',width : fixWidth(0.1),align : 'center'},
                {field : 'processDefinitionId',title : '流程定义ID',width : fixWidth(0.2),align : 'center'},
                {field : 'activityId',title : 'activityId',width : fixWidth(0.1),align : 'center'},
                {field : 'taskName',title : '当前节点',width : fixWidth(0.3),align : 'center',
                	formatter:function(value, row){
                		return "<a class='trace' onclick=\"graphTrace('"+row.processInstanceId+"')\" id='diagram' href='#' pid='"+row.id+"' pdid='"+row.processDefinitionId+"' title='点击查看'>"+value+"</a>";
					}
                },
                {field : 'suspended',title : '挂起/激活',width : fixWidth(0.1),align : 'center',
		          	formatter:function(value, row){
		        		if(value){
		        			return "<a href='javascript:void(0);' onclick=\"suspended('active','"+row.processInstanceId.toString()+"')\">激活</a>";
		        		}else{
		        			return "<a href='javascript:void(0);' onclick=\"suspended('suspend','"+row.processInstanceId.toString()+"')\">挂起</a>";  
		        		}
					}
                }
            ] 
		],
		onLoadSuccess: function(){
			//Tooltip - 如果有多个，需要分配不同的id来分别显示，此处只做个演示
			$(".datagrid-btable").find("#diagram").qtip({
				style: {
         	       classes: 'qtip-dark qtip-rounded'
         	    },
				position: {
			        at: 'right bottom',
			        adjust: {
			            x: 10
			        }
			    }
			});
			//此处可修改 对其方式
			$(".datagrid-btable").find(".datagrid-cell-c1-taskName").css('text-align', 'left');
		}
	});
});

//修正宽高
function fixHeight(percent)   
{   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent)   
{   
	return parseInt(($(this).width() - 50) * percent);
}

//挂起、激活
function suspended( status, id ){
	var url_ = ctx+"/process/updateProcessStatusByProInstanceId/active/"+id;
	if(status == "suspend"){
		url_ = ctx+"/process/updateProcessStatusByProInstanceId/suspend/"+id;
	}
	
	$.ajax({
		async: false,
		cache: false,
		type: "POST",
		url: url_,
		data: {},
		success: function (data) {
			$.messager.progress("close");
			if (data.status) {
				process_datagrid_runing.datagrid("reload"); //reload the process data
			} 
			$.messager.show({
				title : data.title,
				msg : data.message,
				timeout : 1000 * 2
			});
		},
		beforeSend:function(){
			$.messager.progress({
				title: '提示信息！',
				text: '数据处理中，请稍后....'
			});
		},
		complete: function(){
			$.messager.progress("close");
		}
	});
}


