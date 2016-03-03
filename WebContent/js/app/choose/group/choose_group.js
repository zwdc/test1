/**
 * 设定审批候选组
 */

//选择候选组
function chooseGroup(  multiSelect, key ){
	bpmn_dialog = $('<div/>').dialog({
    	title : "设定部门",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/choose/toChooseGroup?multiSelect="+multiSelect+"&key="+key,
        onLoad: function () {
            //显示候选组
            group_datagrid = $('#group_datagrid').datagrid({
                url: ctx+"/choose/chooseGroup",
                width : 'auto',
        		height :  $(this).height()-40,
        		pagination:true,
        		rownumbers:true,
        		border:false,
        		singleSelect:true,
        		striped:true,
                columns : [ 
                    [ 
                      {field:'ck', title : '#',width : ($(this).width() - 50) * 0.1,align : 'center',
                    	  formatter:function(value,row){
                    		  var multiSelect = $("#multiSelect").val();
                    		  if(multiSelect == "true"){
                    			  return '<input type="checkbox" id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="groupIds" onclick="selectGroups();" />';
                    		  }else{
                    			  return '<input type="radio" name="id" value="'+row.id+'" onclick="selectGroup(\''+row.id+'\',\''+row.name+'\');" />';
                    		  }
                    		  
                    		  return '<input type="checkbox" id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="groupIds" />';
        				  }
                      },
                      {field : 'name',title : '用户名',width : ($(this).width() - 50) * 0.45,align : 'center'},
                      {field : 'type',title : '用户组',width : ($(this).width() - 50) * 0.45,align : 'center'}
            	    ] 
                ]
            });
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	bpmn_dialog.dialog('destroy');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	bpmn_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	bpmn_dialog.dialog('destroy');
        }
    });
}

//选择单个用户用户，对父页面赋值
function selectGroup( groupId, groupName ){
	var key = $("#key").val();
	$("#"+key+"_id").val(groupId);
	try {
		$("#"+key+"_name").textbox("setValue",groupName);
	} catch (e) {
		$("#"+key+"_name").val(groupName);
	}
	setTimeout(function () { 
		bpmn_dialog.dialog('destroy');
    }, 100);
}
