/**
 * 设定审批人员
 */

var bpmn_datagrid;
var bpmn_dialog;
var model_dialog;
var model_form;
var model_width = 0;

var group_datagrid;


$(function() {
	//数据列表
    bpmn_datagrid = $('#bpmn_datagrid').datagrid({
        url: ctx+"/userTask/getList",
        width : 'auto',
		height :  fixHeight(0.91),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'id',title : 'ID',width : fixWidth(0.2),align : 'cneter',},
              {field : 'name', title : '名称', width : fixWidth(0.2), align : 'left',},
              {field : 'key',title : 'KEY',width : fixWidth(0.2),align : 'left'},
              {field : 'resourceName',title : 'XML',width : fixWidth(0.2),align : 'center',
			    	formatter:function(value, row){
			    		return "<a id='tip' target='_blank' title='点击查看' href='../process/process-definition?processDefinitionId="+row.id+"&resourceType=xml'>"+row.resourceName+"</a>"
			    	}
              },
              {field : 'diagramResourceName',title : '图片',width : fixWidth(0.2),align : 'center',
			    	formatter:function(value, row){
			    		return "<a id='tip' target='_blank' title='点击查看' href='../process/process-definition?processDefinitionId="+row.id+"&resourceType=image'>"+row.diagramResourceName+"</a>"
			    	}
              }
    	    ] 
        ],
        toolbar: "#toolbar"
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

//加载单个文件
function loadSingle(){
	var row = bpmn_datagrid.datagrid('getSelected');
    if (row) {
    	$.ajax({
			async: false,
			cache: false,
			type: "POST",
			url: ctx+"/userTask/loadSingleBpmn",
			data: {processDefinitionId : row.id},
			success: function (data) {
				$.messager.progress("close");
    			if (data.status) {
    				bpmn_datagrid.datagrid("reload"); 
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
    }else{
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！", "info");
    }
}

//初始化所有
function initialization(){
	$.messager.confirm('提示','此操作将会删除所有已设定的审批人员，确定初始化所有流程定义的审批人员吗？', function (result) {
		if(result){
			$.ajax({
				async: false,
				cache: false,
				type: "POST",
				url: ctx+"/userTask/initialization",
				data: {},
				success: function (data) {
					$.messager.progress("close");
	    			if (data.status) {
	    				bpmn_datagrid.datagrid("reload"); 
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
	});
	
}


//组合流程节点
function outputData( obj, index, dataLength ){
	var taskDefKey = obj.taskDefKey;
	var taskType = obj.taskType;
	var br = '<br/>'
	//普通用户节点
	var modal = 
	'<div class="input-group" class="table-responsive" style="border: 1px solid;margin: 5px;">\
		<table class="table table-bordered well" style="margin-bottom: 0px">\
		<tr>\
			<td>名称:</td>\
			<td>'+obj.taskName+'</td>\
		</tr>\
		<tr>\
			<td>类型:</td>\
			<td>\
				<input type="radio" name="'+taskDefKey+'_taskType" value="assignee" id="assignee" onclick="chooseUser(false,\''+taskDefKey+'\');" />人员\
		        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateUser" id="candidateUser" onclick="chooseUser(true,\''+taskDefKey+'\');" />候选人\
		        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateGroup" id="candidateGroup" onclick="chooseGroup(\''+taskDefKey+'\');" />候选组\
		        <input type="radio" name="'+taskDefKey+'_taskType" value="custom" id="custom" onclick="clearChoose(\''+taskDefKey+'\');" />由项目决定\
			</td>\
		</tr>\
		<tr>\
			<td>选择:</td>\
			<td>\
				<input type="text" id="'+taskDefKey+'_name" name="'+taskDefKey+'_name" class="easyui-textbox easyui-validatebox" />\
				<a href="#" onclick="clearChoose(\''+taskDefKey+'\');" class="easyui-linkbutton">清空</a>\
				<input type="hidden" id="'+taskDefKey+'_id" name="'+taskDefKey+'_id" />\
			</td>\
		</tr>\
		</table>\
	</div>\
	';
	//修改任务的节点已经在配置文件的 initiator 中设置，此处不用选择任务办理人。
	var modify = 
		'<div class="input-group" class="table-responsive" style="border: 1px solid;margin: 5px;">\
		<table class="table table-bordered well" style="margin-bottom: 0px">\
		<tr>\
			<td>名称:</td>\
			<td>'+obj.taskName+'</td>\
		</tr>\
		<tr>\
			<td>类型:</td>\
			<td>\
				任务发起人\
				<input type="hidden" value="modify" name="'+taskDefKey+'_taskType" />\
			</td>\
		</tr>\
		<tr>\
			<td>选择:</td>\
			<td>\
				<input type="text" id="'+taskDefKey+'_name" value="任务发起人" name="'+taskDefKey+'_name" readonly="readonly" class="easyui-textbox"/>\
				<input type="hidden" id="'+taskDefKey+'_id" value="0" name="'+taskDefKey+'_id" class="easyui-textbox"/>\
			</td>\
		</tr>\
		</table>\
		</div>\
	    ';
	if(taskDefKey == "modifyApply"){
		$(modify).appendTo($("#modelTable"));
	}else{
		var modal = $(modal).appendTo($("#modelTable"));
/*		if(index % 4 == 0 ){
			$(br).appendTo($("#modelTable"));
		}
		*/
		if(taskType == "assignee") {
    		modal.find("table input[id=assignee]").attr("checked","checked");
		} else if (taskType == "candidateUser") {
			modal.find("table input[id=candidateUser]").attr("checked","checked");
		} else if (taskType == "candidateGroup") {
			modal.find("table input[id=candidateGroup]").attr("checked","checked");
		} else if (taskType == "custom") {
			modal.find("table input[id=custom]").attr("checked","checked");
		}
		modal.find("table input[id="+taskDefKey+"_name]").attr("value",obj.candidate_name);
		modal.find("table input[id="+taskDefKey+"_id]").attr("value",obj.candidate_ids);
	}
}

//初始化表单
function formInit_bpmn( procDefKey ) {
    model_form = $('#model_form').form({
    	url: ctx+"/userTask/setupTask?procDefKey="+procDefKey,
        onSubmit: function (param) {
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
            	model_dialog.dialog('close');//销毁对话框
                bpmn_datagrid.datagrid('reload');//重新加载列表数据
                
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

function initModelTable( procDefKey ){
    //显示节点信息
	model_dialog = $('#dialog-form').dialog({
    	top: ($(window).height()-450) * 0.5,
        left: ($(window).width()-650) * 0.5,
		width : 650,
		height : 'auto',
		closed: false,
        modal: true,
        shadow: true,
        iconCls: 'icon-save',
        minimizable: false,
        maximizable: false,
        onLoad: function () {
			//formInit_bpmn( procDefKey );
		},
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    model_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	model_dialog.dialog('close');
                }
            }
        ],
        onClose: function () {
        	$("#modelTable").html("");
        	model_width = 0;
        }
    });
}

//设定审批人员
function setAuthor(){
	var row = bpmn_datagrid.datagrid('getSelected');
	if (row) {
		$.ajax({
			async: false,
			cache: false,
			type: "POST", 
			url: ctx+"/userTask/listUserTask",
			data: {procDefKey: row.key},
			success: function (data) {
				if(data.length == 0){
					$.messager.show({
						title:'提示',
						msg:'请先【加载】所选流程定义文件，再设定审批人员！',
						showType:'fade',
						style:{
							right:'',
							bottom:''
						}
					});
				}else{
					formInit_bpmn( row.key );
					for(var i=1;i<=data.length;i++) {  
						//逐个显示审批人员
						outputData(data[i-1], i, data.length);
						model_width += 300;	//每个节点的宽度
					}
					//显示model
					initModelTable(row.key);
//					$("#model_form").attr("action",ctx+"/userTask/setPermission?procDefKey="+row.key);
				}
			}
		});
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}


