/**
 * 督察处发布任务
 */
var taskInfo_datagrid;
var taskInfo_form;
var taskInfo_dialog;
$(function(){
	taskInfo_datagrid=$('#taskInfo_datagrid').datagrid({
		url:ctx+"/taskInfo/getList/pastYear",	//路径访问后台方法获取数据
		width:'auto',					//宽度自适应
		height:fixHeight(1),			//高度自适应
		fitColumns:true,
		pagination:true,				//显示底部分页栏
		rownumbers:true,				//显示行号
		border:false,					//边框
		singleSelect:true,				//只允许选中一行
		striped:true,					//隔行变色
		nowrap:false,
		columns:[
		    [
				{field:'endYear',title:'完成年份',width:fixWidth(0.08),align:'center',sortable:true,
		     		formatter:function(value,row){
		     			return moment(value).format("YYYY");
		     		}
		     	},{field:'urgency',title:'急缓程度',width:fixWidth(0.08),align:'center',sortable:true,
						formatter:function(value,row){
						  switch (value) {
						  	case 0: return "特提";
						  	case 1: return "特急";
						  	case 2: return "加急";
						  	case 3: return "平急";
						  } 
					 }
					},
		     	{field:'title',title:'任务内容',width:fixWidth(0.5),align:'left',halign:'center',multiline:true},		     	
		     	{field:'groupName',title:'牵头单位',width:fixWidth(0.1),align:'left',halign:'center',multiline:true},
		     	{field:'major',title:'主管市长',width:fixWidth(0.1),align:'left',halign:'center',multiline:true},
		     	{field:'createTaskDate',title:'开始时间',width:fixWidth(0.1),align:'center',sortable:true,
		     		formatter:function(value,row){
	            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
					 }
		     	},
		     	{field:'fbFrequencyName',title:'反馈频度',width:fixWidth(0.1),align:'center'},
		     	{field:'taskSourceName',title:'任务来源',width:fixWidth(0.1),align:'center'}
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
			taskInfo_datagrid.datagrid('reload',obj);
		}
	});
});
//高级搜索 删除一行
function searchRemove(curr) {
	$(curr).closest('tr').remove();
} 
//高级查询
function gradeSearch() {
	jqueryUtil.gradeSearch(taskInfo_datagrid, "#taskInfoSearch", "/taskInfo/taskInfoSearch");
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
	 taskInfo_form = $('#taskInfo_form').form({
		 	url: ctx+"/taskInfo/saveOrUpdate",
	        onSubmit: function () {
		        $.messager.progress({
		            title: '提示信息！',
		            text: '数据处理中，请稍后....'
		        });
		        var isValid = $(this).form('validate');
		        if (!isValid) {
		            $.messager.progress('close');
		        }
		        var hostGroupId = $("#hostGroupId").val();
		        if(hostGroupId == "") {
		        	$.messager.progress('close');
		        	$.messager.alert("提示","至少选择一个牵头单位！");
		        	return false;
		        }
		        return isValid;
		    },
		    success: function (data) {
	            $.messager.progress('close');
	            var json = $.parseJSON(data);
	            if (json.status) {
	            	taskInfo_dialog.dialog("refresh",ctx+"/taskInfo/toMain?id="+json.data);//刷新更新
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