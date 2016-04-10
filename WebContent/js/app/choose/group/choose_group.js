/**
 * 设定审批候选组
 */

//选择候选组
function chooseGroup(taskDefKey){
	bpmn_dialog = $('<div/>').dialog({
    	title : "设定部门",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/choose/toChooseGroup?taskDefKey="+taskDefKey,
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
                	getValue($("#taskDefKey").val());
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

//取出候选组的值
function getValue(taskDefKey){
    var ids='';
    var names='';
    var checked = $("input[name=groupIds]:checked");//获取所有被选中的标签元素
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
    $("#"+taskDefKey+"_id").val(ids);
	$("#"+taskDefKey+"_name").val(names); 
}
