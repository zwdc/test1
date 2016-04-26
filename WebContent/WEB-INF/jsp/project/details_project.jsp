<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
<script type="text/javascript">
var feedback_datagrid;
var feedback_form;
var feedback_dialog;
var fb=${feedback};
$(function() {
	//数据列表
	feedback_datagrid = $('#feedback_datagrid').datagrid({
        //url: ctx+"/feedback/getList",
        width : 'auto',
		height : 'auto',
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [
             [
              {field: 'warningLevel', title: '预警', width: fixWidth(0.08), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value=="1") {           			
                      	return "开始反馈";
                      }else if(value=="2"){
                    	  return "逾期反馈"; 
                      }else{
                    	  return "未到时间"; 
                      }
            	  },
            	  styler:function(value){
            		  if (value=="1") {           			
                          return 'background-color:yellow;color:white';
                        }else if(value=="2"){
                      	  return 'background-color:red;color:white';; 
                        }
              	  }
			},
			  {field: 'feedbackStartDate', title: '反馈期间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
					  formatter:function(value,row){
						  return moment(value).format("MM月DD日")+"-"+moment(row.feedbackEndDate).format("MM月DD日");
					  }
				},
              {field: 'groupName', title: '牵头单位', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'createUser', title: '填报人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'feedbackDate', title: '反馈时间', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row){
            		  if(value==null){
            			  return "--"
            		  }else{
            			  return moment(value).format("YYYY-MM-DD HH:mm:ss");
            		  }
            		 
				  }
              },
              {field: 'status', title: '状态', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  switch (value) {
						case "FEEDBACKING":
							return "<span class='text-primary'>反馈处理中</span>";
						case "RETURNED":
							return "<span class='text-danger'>已退回</span>";
						case "ACCEPT":
							return "<span class='text-success'>已采纳</span>";
						default:
							return "<span class='text-warning'>未反馈</span>";
					  }
            	  },
            	  styler:function(value){
            		  if (value=="SUCCESS") {           			
                          return 'background-color:green;color:white';
                        }else if(value=="FAIL"){
                      	  return 'background-color:orange;color:white';; 
                        }
              	  }
              },
              
              {field: 'delayCount', title: '延期次数', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true},
              {field: 'refuseCount', title: '退回次数', width: fixWidth(0.05), align: 'center', halign: 'center', sortable: true}  
        ]
     ],
     toolbar: "#toolbar1"
  /*   onLoadSuccess:function(fb){
    	 feedback_datagrid.datagrid("loadData",fb);
     }*/
    });
	 feedback_datagrid.datagrid("loadData",fb);
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            feedback_datagrid.datagrid('reload',obj); 
	    }
	});
	$("#assistantGroup").kindeditor({readonlyMode: true});
	$("#remark").kindeditor({readonlyMode: true});
	$("#suggestion").kindeditor({readonlyMode: true});
});

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row,url) {
	feedback_form = $('#feedback_form').form({
        url: url,
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
	        	feedback_dialog.dialog('destroy');
	        	feedback_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

//反馈审核—— 督查人员
function check(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息审核",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=check&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/checkFeedback");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//实施反馈—— 承办单位
function feedback(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "进行反馈",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=feedback&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveFeedback");
	        },
	        buttons: [
	            {
	                text: '暂存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	            	text: '申请审核',
	            	iconCls: 'icon-ok',
	            	id: 'ok',
	            	handler: function () {
	                	$.messager.confirm('确认提示！','确认提交表单进入反馈审核流程吗？',function(result){
	                		if(result){
	                			feedback_form.form('submit',{
	    	            		 	url: ctx+"/feedback/callApproval",
	    	            	        onSubmit: function () {
	    	            		        $.messager.progress({
	    	            		            title: '提示信息！',
	    	            		            text: '数据处理中，请稍后....'
	    	            		        });
	    	            		        var isValid = $(this).form('validate');
	    	            		        if (!isValid) {
	    	            		            $.messager.progress('close');
	    	            		        } else {
	    	            		        	$("#save").linkbutton("disable");
	    	            		        	$("#ok").linkbutton("disable");
	    	            		        }
	    	            		        return isValid;
	    	            		    },
	    	            		    success: function (data) {
	    	            	            $.messager.progress('close');
	    	            	            var json = $.parseJSON(data);
	    	            	            if (json.status) {
	    	            	            	feedback_dialog.dialog('destroy');//销毁对话框
	    	            	            	feedback_datagrid.datagrid('reload');//重新加载列表数据
	    	            	            } 
	    	            	            $.messager.show({
	    	            					title : json.title,
	    	            					msg : json.message,
	    	            					timeout : 1000 * 2
	    	            				});
	    	            	        }
	    	            	    });
	                		}
	                	});
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//编辑
function editFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈信息编辑",
			top: 20,
			width : fixWidth(0.9),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=edit&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/saveOrUpdate");
	        },
	        buttons: [
	            {
	                text: '保存',
	                iconCls: 'icon-save',
	                handler: function () {
	                	feedback_form.submit();
	                }
	            },
	            {
	                text: '重置',
	                iconCls: 'icon-reload',
	                handler: function () {
	                	feedback_form.form('clear');
	                }
	            },
	            {
	                text: '关闭',
	                iconCls: 'icon-cancel',
	                handler: function () {
	                	feedback_dialog.dialog('destroy');
	                }
	            }
	        ],
	        onClose: function () {
	        	feedback_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//查看反馈详情
function detailsFeedback(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	feedback_dialog = $('<div/>').dialog({
	    	title : "反馈记录详情",
			top: 20,
			width : fixWidth(0.8),
			height : 'auto',
	        modal: true,
	        minimizable: true,
	        maximizable: true,
	        href: ctx+"/feedback/toMain?action=detail&id="+row.id,
	        onLoad: function () {
	            formInit(row,ctx+"/feedback/detail");
	        },
	        buttons: [
	  	          {
	  	                text: '关闭',
	  	                iconCls: 'icon-cancel',
	  	                handler: function () {
	  	                	feedback_dialog.dialog('destroy');
	  	                }
	  	            }
	  	        ],
	        onClose: function () {
	        	source_dialog.dialog('destroy');
	        }
	    });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
//添加
function addFeedback(){
	feedback_dialog = $('<div/>').dialog({
    	title : "反馈信息添加",
		top: 20,
		width : fixWidth(0.9),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/feedback/toMain?action=add",
        onLoad: function () {
            formInit(null,ctx+"/feedback/saveOrUpdate");
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	feedback_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	feedback_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	feedback_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	feedback_dialog.dialog('destroy');
        }
    });
}


//删除
function delFeedback() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/feedback/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	feedback_datagrid.datagrid('load');
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
</script>
<div class="easyui-layout">
   <table class="table table-bordered table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务交办信息</td>
		</tr>
		<tr>
			<td class="text-right">任务标题:</td>
			<td colspan="3">${taskInfo.title }</td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td>${taskInfo.title }</td>
			<td class="text-right">急缓程度:</td>
			<td>${zwdc:getUrgencyType(taskInfo.urgency) }</td>

		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td>${taskInfo.taskSource.name }</td>
			<td class="text-right">反馈频度:</td>
			<td>${taskInfo.fbFrequency.name }</td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><fmt:formatDate value="${taskInfo.createTaskDate }" type="both"/></td>
			<td class="text-right">办结时限:</td>
			<td><fmt:formatDate value="${taskInfo.endTaskDate }" type="both"/></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><fmt:formatDate value="${taskInfo.claimLimitDate }" type="both"/></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="text-right">牵头单位:</td>
			<td colspan="3">
				<table id="hostGroupDatagrid" class="easyui-datagrid" data-options="url:'${ctx }/group/getHostGroupList?groupIds=${taskInfo.hostGroup }',fitColumns:true,rownumbers:true,border:true,singleSelect:true">
				    <thead>
						<tr>
							<th data-options="field:'groupName'" width="25%">牵头单位名称</th>
							<th data-options="field:'userNames0'" width="15%">联系人A</th>
							<th data-options="field:'linkway0'" width="20%">联系方式</th>
							<th data-options="field:'userNames1'" width="15%">联系人B</th>
							<th data-options="field:'linkway1'" width="20%">联系方式</th>
						</tr>
				    </thead>
				</table>
			</td>
		</tr>
		<tr>
			<td class="text-right">责任单位:</td>
			<td colspan="3">
				<textarea name="assistantGroup" rows="1" cols="80" style="width: 100%">${taskInfo.assistantGroup }</textarea>
			</td>
		</tr>
		 <tr class="bg-primary">
			<td colspan="4" align="center">反馈列表</td>
		</tr>
  	</table>
  	<div id="toolbar1" style="padding:2px 0">
	<table>   
		<tr>
		<td style="padding-left:2px">
			<!--  <shiro:hasRole name="admin">-->
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
			<!--</shiro:hasRole>-->
		</td>
		</tr>
	</table>
		</div>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'name'">任务名称</div>
		<div data-options="name:'createDate'">反馈时间</div>
	</div>
	<table id="feedback_datagrid" title=""></table>
</div>
