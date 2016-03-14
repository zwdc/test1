/**
 * 设定审批人员
 */

var user_datagrid;
var group_combobox;


$(function() {
	//数据列表
	var groupId = $("#groupId").val();
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/choose/chooseUser?groupId="+groupId,
        width : 'auto',
		height : $(".easyui-layout").height()-1,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field:'ck', title : '#',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value,row){
            		  var multiSelect = $("#multiSelect").val();
            		  if(multiSelect == "true"){
            			  return '<input type="checkbox" id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="ids" onclick="selectUsers();" />';
            		  }else{
            			  return '<input type="radio" name="id" value="'+row.id+'" onclick="selectUser(\''+row.id+'\',\''+row.name+'\');" />';
            		  }
				  }
              },
              {field : 'name',title : '用户名',width : fixWidth(0.3),align : 'center'},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.3),align : 'center',
            	  formatter:function(value, row){
              		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
              	  }
              },
              {field : 'group',title : '用户组',width : fixWidth(0.3),align : 'center'},
              {field : 'role',title : '角色',width : fixWidth(0.3),align : 'center'}
    	    ] 
        ]
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
});

//选择单个用户用户，对父页面赋值
function selectUser( userId, userName ){
	var key = $("#key").val();
	$("#"+key+"_id", window.parent.document).val(userId);
	try {
		window.parent.setValueUserName(key, userName);
	} catch (e) {
		$("#"+key+"_name", window.parent.document).val(userName);
	}
	setTimeout(function () { 
		window.parent.set_chooseUser();
    }, 100);
}

//选择候选人，对父页面赋值
function selectUsers(){
	var key = $("#key").val();
    var ids='';
    var names='';
    var checked = $("input:checked");//获取所有被选中的标签元素
    for(i=0;i<checked.length;i++){
     	//将所有被选中的标签元素的值保存成一个字符串，以逗号隔开
   	 	var obj = checked[i].value.split("_");
        if(i<checked.length-1){
           ids+=obj[0]+',';
           names+=obj[1]+',';
        }else{
           ids+=obj[0];
           names+=obj[1];
        }
    }
    $("#"+key+"_id", window.parent.document).val(ids);
    try {
        //easyui中的testBox赋值比较蛋疼。上面用jquery 可以直接给父页面赋值，前提是没有用easyui的easyui-textbox样式
    	window.parent.setValue(key, names);
	} catch (e) {
		$("#"+key+"_name", window.parent.document).val(names);
	}
}
