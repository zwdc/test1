/**
 * 用户页面相关
 */

var user_datagrid;
var user_form;
var user_dialog;


$(function() {
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/user/getList",
        width : 'auto',
		height : fixHeight(0.85),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '用户名',width : fixWidth(0.1),align : 'left',sortable: true},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.2),sortable: true,
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm");
				  }
              },
              {field : 'group_name',title : '部门',width : fixWidth(0.1),sortable: true},
              {field : 'role_name',title : '角色',width : fixWidth(0.1),sortable: true},
              {field : 'dataPermission',title : '数据权限',width : fixWidth(0.1),sortable: true,
            	  formatter:function(value,row){
            		 if(value==0){
            			 return "仅可见自己数据";
            		 }else if(value==1){
            			 return"可见部门内数据";
            		 }else if(value==2){
            			 return"可见同角色数据";
            		 }else if(value==3){
            			 return"可见本部门且同角色数据";
            		 }else if(value==4){
            			 return"可见本部门或同角色数据";
            		 }else if(value==5){
            			 return"可见所有数据";
            		 }
				  }
              },
              {field : 'locked',title : '状态',width : fixWidth(0.1),
            	  formatter:function(value,row){
            		  if("0"==row.locked){
						return "<font color=green>正常<font>";
            		  }else{
            			return "<font color=red>停用<font>";  
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
            user_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级搜索 del row
function searchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  gradeSearch() {
	jqueryUtil.gradeSearch(user_datagrid, "#gradeSearch", "/user/userSearch");
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
	var _url = ctx+"/user/doAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/user/doUpdate";
	}
    user_form = $('#user_form').form({
        url: _url,
        onSubmit: function (param) {
            $.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
            var isValid = $(this).form('validate');
            
            var repasswd = $("#repasswd").textbox('getValue');
            if(repasswd != '') {
            	 $("#repasswd").validatebox();
            }
            
			if (!isValid) {
                $.messager.progress('close');
            } else {
            	if(row) {
            		param.salt = row.salt;
            		param.password = row.passwd;
            		param.rePasswd = repasswd;
            	}
            }
            return isValid;
        },
        success: function (data) {
            $.messager.progress('close');
            var json = $.parseJSON(data);
            if (json.status) {
                user_dialog.dialog('destroy');//销毁对话框
                user_datagrid.datagrid('reload');//重新加载列表数据
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showUser(row) {
	var _url = ctx+"/user/toAdd";
	if (row) {
		_url = ctx+"/user/toUpdate/"+row.id;
	}
    //弹出对话窗口
    user_dialog = $('<div/>').dialog({
    	title : "用户信息",
		top: 20,
		width : 600,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if (row) {
            	row.registerDate = moment(row.registerDate).format("YYYY-MM-DD HH:mm");
            	user_form.form('load', row);  //通过row初始化表单中的数据
            } else {
            	$("input[name=locked]:eq(0)").attr("checked", 'checked');//状态 初始化值
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    user_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    user_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            user_dialog.dialog('destroy');
        }
    });
}



//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        showUser(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除用户
function del() {
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的所有行?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/user/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                            user_datagrid.datagrid('load');	// reload the user data
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

//同步
function sync(){
    $.messager.confirm('提示', '重新同步所有用户信息到工作流表,确认？', function (result) {
        if (result) {
            $.ajax({
        		async: false,
        		cache: false,
                url: ctx + '/user/syncUserToActiviti',
                type: 'post',
                dataType: 'json',
                data: {},
                success: function (data) {
                    if (data.status) {
                        user_datagrid.datagrid('load');	// reload the user data
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
}

//初始化密码
function initPassword() {
	var row = user_datagrid.datagrid('getSelected');
    if (row) {
    	$.messager.confirm('提示', '初始化用户密码为系统默认密码 "123" ?', function (result) {
    		if (result) {
    			$.ajax({
    	    		async: false,
    	    		cache: false,
    	            url: ctx + '/user/initPassword',
    	            type: 'post',
    	            dataType: 'json',
    	            data: {userId : row.id},
    	            success: function (data) {
    	                if (data.status) {
    	                    user_datagrid.datagrid('load');	// reload the user data
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