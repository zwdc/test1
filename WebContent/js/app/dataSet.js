/**
 * 销售审批表
 */

var dataSet_datagrid;

$(function() {
	dataSet_datagrid = $('#dataSet_datagrid').datagrid({
        url: ctx+"/dataSet/getList",
        width : 'auto',
		height : fixHeight(0.89),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		checkOnSelect:true,
        columns : [ 
            [ 
              {field : 'user.name',title : '用户名称',width : fixWidth(0.2),align : 'center',sortable: true},
              {field : 'all',title : '全部',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value, row){
            		  if(value){
            			  return '<input type="radio" name="'+row.userId+'" checked="checked" onclick="unchooseValue(\''+row.userId+'\',\'all\');">';
            		  } else {
            			  return '<input type="radio" name="'+row.userId+'" onclick="chooseValue(\''+row.userId+'\',\'all\');">';
            		  }
              	  }
              },
              {field : 'company',title : '所在公司',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value, row){
            		  if(value){
              			  return '<input type="radio" name="'+row.userId+'" checked="checked" onclick="unchooseValue(\''+row.userId+'\',\'company\');">';
              		  } else {
              			  return '<input type="radio" name="'+row.userId+'" onclick="chooseValue(\''+row.userId+'\',\'company\');">';
              		  }
            	  }
              },
              {field : 'group',title : '所在部门',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value, row){
            		  if(value){
            			  return '<input type="radio" name="'+row.userId+'" checked="checked" onclick="unchooseValue(\''+row.userId+'\',\'group\');">';
            		  } else {
            			  return '<input type="radio" name="'+row.userId+'" onclick="chooseValue(\''+row.userId+'\',\'group\');">';
            		  }
            	  }  
              },
              {field : 'role',title : '所在职务',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value, row){
            		  if(value){
              			  return '<input type="radio" name="'+row.userId+'" checked="checked" onclick="unchooseValue(\''+row.userId+'\',\'role\');">';
              		  } else {
              			  return '<input type="radio" name="'+row.userId+'" onclick="chooseValue(\''+row.userId+'\',\'role\');">';
              		  }
            	  }
              },
              {field : 'self',title : '仅本人',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value, row){
          			  if(value){
            			  return '<input type="radio" name="'+row.userId+'" checked="checked" onclick="unchooseValue(\''+row.userId+'\',\'self\');">';
            		  } else {
            			  return '<input type="radio" name="'+row.userId+'" onclick="chooseValue(\''+row.userId+'\',\'self\');">';
            		  }
            	  }
              }
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
            dataSet_datagrid.datagrid('reload',obj); 
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

//保存数据集权限
function chooseValue(userId, dataType) {
	/*if($("#"+dataType).is(':checked')) {
		
	}*/
	$.ajax({
		async: false,
		cache: false,
		type: "POST",
		url: ctx+"/dataSet/chooseValue",
		data: {userId: userId, dataType: dataType, choose:true},
		success: function (data) {
			$.messager.progress("close");
			if (data.status) {
				dataSet_datagrid.datagrid("reload"); //reload the process data
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

//取消数据权限
function unchooseValue(userId, dataType) {
	$.ajax({
		async: false,
		cache: false,
		type: "POST",
		url: ctx+"/dataSet/chooseValue",
		data: {userId: userId, dataType: dataType, choose:false},
		success: function (data) {
			$.messager.progress("close");
			if (data.status) {
				dataSet_datagrid.datagrid("reload"); //reload the process data
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


