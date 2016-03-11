<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <title>督察接收</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js?_=${sysInitTime}"></script>
  	<%-- <script type="text/javascript" src="${ctx}/js/app/choose/user/user.js?_=${sysInitTime}"></script> --%>
  	<script type="text/javascript">
  		var s_datagrid;
  		var g_datagrid;
  		var o_datagrid;
  		var tabsTitle = "省政府文件";
	  	$(function() {
	  		//初始化省政府文件
	  		shengzhengfu("WAIT_FOR_CLAIM", "dqs");
	  		$('#tabs').tabs({
	  		    border:false,
	  		    onSelect:function(title, index){
	  		    	tabsTitle = title;
	  				if(title == "省政府文件") {
	  					shengzhengfu("WAIT_FOR_CLAIM", "dqs");
	  				} else if(title == "国务院文件") {
	  					guowuyuan();
	  				} else if(title == "其他文件") {
	  					other();
	  				}
	  		    }
	  		});
	  		
	  		$("#bt_dqs").bind("click", function(event) { $("#blz").hide(); $("#ybl").hide(); $("#dqs").show();});
	  		$("#bt_blz").bind("click", function(event) { $("#dqs").hide(); $("#ybl").hide(); $("#blz").show();});
	  		$("#bt_ybl").bind("click", function(event) { $("#blz").hide(); $("#dqs").hide(); $("#ybl").show();});
	  	});
	  	
	    //修正宽高
		function fixHeight(percent)   
		{   
			return parseInt($(this).height() * percent);
		}

		function fixWidth(percent)   
		{   
			return parseInt(($(this).width() - 30) * percent);
		}
		
		function statusName(flag, toolbar) {
			if(tabsTitle == "省政府文件") {
				shengzhengfu(flag, toolbar);
			} else if(tabsTitle == "国务院文件") {
				guowuyuan(flag, toolbar);
			} else if(tabsTitle == "其他文件") {
				other(flag, toolbar);
			}
		}
	  	
	  	function shengzhengfu(status, toolbarId) {
	  		s_datagrid = $('#s_datagrid').datagrid({
	  	        url: ctx+"/taskInfo/getList?status="+status+"&taskType=1",
	  	        width: 'auto',
	  			height: fixHeight(0.8),
	  			pagination:true,
	  			rownumbers:true,
	  			//fitColumns:true,
	  			border:false,
	  			singleSelect:true,
	  			striped:true,
	  			nowrap: false,
	  			columns: [ 
	  	            [ 
					    {field:'title',title:'标题',width:fixWidth(0.2),align:'left',halign:'center'},
				     	{field:'taskNo',title:'文号',width:fixWidth(0.1),align:'center',halign:'center'},
				     	{field:'createTaskDate',title:'立项时间',width:fixWidth(0.1),align:'center',halign:'center',sortable:true,
				     		formatter:function(value,row){
			            		return moment(value).format("YYYY-MM-DD HH:mm:ss");
							}
				     	},
				     	{field:'assignDate',title:'交办时间',width:fixWidth(0.1),align:'center',halign:'center',sortable:true,
			     			formatter:function(value,row){
			            	  	return moment(value).format("YYYY-MM-DD HH:mm:ss");
							}
				     	},
				     	{field:'claimDate',title:'签收时间',width:fixWidth(0.1),align:'center',halign:'center',sortable:true,
			     			formatter:function(value,row){
		            		  	return moment(value).format("YYYY-MM-DD HH:mm:ss");
						 	}
				     	},
				     	{field:'urgeCount',title:'催办情况',width:fixWidth(0.05),align:'center',
				     		formatter:function(value,row){
		            		  	return '<a href="javascript:void(0);" onclick="showUrge('+row.id+');">'+value+'</a>';
						 	}	
				     	},
				     	{field:'feedbackCycle',title:'反馈周期',width:fixWidth(0.1),align:'center'},
				     	{field:'feedbackCount',title:'反馈记录',width:fixWidth(0.05),align:'center',
				     		formatter:function(value,row){
		            		  	return '<a href="javascript:void(0);" onclick="showFeedback('+row.id+');">'+value+'</a>';
						 	}	
				     	},
				     	{field:'hostGroup',title:'主板单位',width:fixWidth(0.1),align:'center'},
				     	{field:'assistantGroup',title:'协办单位',width:fixWidth(0.1),align:'center'},
				     	{field:'endTaskDate',title:'办结时限',width:fixWidth(0.1),align:'center',sortable:true,
				     		formatter:function(value,row){
		            			return moment(value).format("YYYY-MM-DD");
							}
				     	},
				     	{field:'status',title:'办理状态',width:fixWidth(0.1),align:'center',sortable:true,
				     		formatter:function(value,row){
				     			switch (value) {
									case "IN_HANDLING":
										return "<span class='text-success'>办理中</span>";
									case "APPLY_FINISHED":
										return "<span class='text-warning'>申请办结</span>";
									case "FINISHED":
										return "<span class='text-danger'>已办结</span>";
									default:
										return "";
								}
							}
				     	}
	  	    	    ] 
	  	        ],
		        onDblClickRow: function(index, row) {
		        	//双击一行时触发
		        	showDetails(row);	//显示详细信息页面
		        },
	  	        toolbar: "#"+toolbarId
	  	    });
	  		
	  		if(status == "WAIT_FOR_CLAIM") {
	  			s_datagrid.datagrid('hideColumn', 'urgeCount');
	  			s_datagrid.datagrid('hideColumn', 'feedbackCount');
	  			s_datagrid.datagrid('hideColumn', 'claimDate');
	  			s_datagrid.datagrid('hideColumn', 'status');
	  		}
	  		
	  	}
	  	
	  	//显示催办情况
	  	function showUrge(taskInfoId) {
	  		var urge_dialog = $('<div/>').dialog({
	  	    	title : "催办记录",
	  			top: 20,
	  			width : fixWidth(0.8),
	  			height: fixHeight(0.9),
	  	        modal: true,
	  	        minimizable: true,
	  	        maximizable: true,
	  	        href: ctx+"/urge/details/"+taskInfoId,
	  	        buttons: [
	  	            {
	  	                text: '关闭',
	  	                iconCls: 'icon-cancel',
	  	                handler: function () {
	  	                	urge_dialog.dialog('destroy');
	  	                }
	  	            }
	  	        ],
	  	        onClose: function () {
	  	        	urge_dialog.dialog('destroy');
	  	        }
	  	    });
	  	}
	  	
	  	//显示反馈记录
	  	function showFeedback(taskInfoId) {
	  		var feedback_dialog = $('<div/>').dialog({
	  	    	title : "反馈记录",
	  			top: 20,
	  			width : fixWidth(0.8),
	  			height: fixHeight(0.9),
	  	        modal: true,
	  	        minimizable: true,
	  	        maximizable: true,
	  	        href: ctx+"/feedback/detailsTab/"+taskInfoId,
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
	  	        	feedback_dialog.dialog('destroy');
	  	        }
	  	    });
	  	}
	  	
	  	function details(){
	  	    var row = s_datagrid.datagrid('getSelected');
	  	    if (row) {
	  	    	showDetails(row);
	  	    } else {
	  	        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
	  	    }
	  	}

	  	function showDetails(row) {
	  	    //弹出对话窗口
	  		var taskInfo_dialog = $('<div/>').dialog({
	  	    	title : "任务详情",
	  			top: 20,
	  			width : fixWidth(0.8),
	  			height: fixHeight(0.9),
	  	        modal: true,
	  	        minimizable: true,
	  	        maximizable: true,
	  	        href: ctx+"/taskInfo/details/"+row.id,
	  	        buttons: [
	  	            {
	  	                text: '关闭',
	  	                iconCls: 'icon-cancel',
	  	                handler: function () {
	  	                	taskInfo_dialog.dialog('destroy');
	  	                }
	  	            }
	  	        ],
	  	        onClose: function () {
	  	        	taskInfo_dialog.dialog('destroy');
	  	        }
	  	    });
	  	}
	  	
	  	//签收
	  	function claim() {
	  		var row = s_datagrid.datagrid('getSelected');
	  	    if (row) {
	  	    	$.messager.confirm('温馨提示！', '是否确认签收?', function (result) {
		            if (result) {
	  	                $.ajax({
	  	            		async: false,
	  	            		cache: false,
	  	                    url: ctx + '/taskInfo/claim/'+row.id,
	  	                    type: 'post',
	  	                    dataType: 'json',
	  	                    success: function (data) {
	  	                        if (data.status) {
	  	                        	s_datagrid.datagrid('load');
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
	  	
	    //拒绝
	  	function refuse() {
		  	var row = s_datagrid.datagrid('getSelected');
	  	    if (row) {
			  	var refuse_form;
	  	    	var refuse_dialog = $('<div/>').dialog({
		  	    	title : "拒绝原因",
		  			top: 20,
		  			width : fixWidth(0.8),
		  			height: 'auto',
		  	        modal: true,
		  	        minimizable: true,
		  	        maximizable: true,
		  	      	href: ctx+"/superviserReveive/toRefuse",
		  	        buttons: [
		  	            {
		  	                text: '拒绝',
		  	                iconCls: 'icon-remove',
		  	                handler: function () {
		  	                	$.messager.confirm('确认提示！','是否拒绝签收此任务？',function(result){
		  	                		if(result){
		  	                			refuse_form = $("#refuseForm").form('submit',{
		  	    	            		 	url: ctx+"/taskInfo/refuse/"+row.id,
		  	    	            	        onSubmit: function () {
		  	    	            		        $.messager.progress({
		  	    	            		            title: '提示信息！',
		  	    	            		            text: '数据处理中，请稍后....'
		  	    	            		        });
		  	    	            		        if(KindEditor.instances[0].html() == "") {
		  	    	            		        	$.messager.progress('close');
		  	    	            		        	$.messager.alert("温馨提示", "请输入拒绝签收的原因！");
		  	    	            		        	return false;
		  	    	            		        }
		  	    	            		        return true;
		  	    	            		    },
		  	    	            		    success: function (data) {
		  	    	            	            $.messager.progress('close');
		  	    	            	            var json = $.parseJSON(data);
		  	    	            	            if (json.status) {
		  	    	            	            	refuse_dialog.dialog('destroy');//销毁对话框
		  	    	            	            	s_datagrid.datagrid('reload');  //重新加载列表数据
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
		  	                	//refuse_form.form('reset');
		  	                	KindEditor.instances[0].html("");
		  	                }
		  	            },
		  	            {
		  	                text: '关闭',
		  	                iconCls: 'icon-cancel',
		  	                handler: function () {
		  	                	refuse_dialog.dialog('destroy');
		  	                }
		  	            }
		  	        ],
		  	        onClose: function () {
		  	        	refuse_dialog.dialog('destroy');
		  	        }
		  	    });
	  	    } else {
	  	        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
	  	    }
	  	}
	  
	  	//反馈
	  	function feedback() {
	  		var row = s_datagrid.datagrid('getSelected');
	  	    if (row) {
	  	    	var feedback_dialog = $('<div/>').dialog({
		  	    	title : "定期反馈",
		  			top: 20,
		  			width : fixWidth(0.8),
		  			height: 'auto',
		  	        modal: true,
		  	        minimizable: true,
		  	        maximizable: true,
		  	      	href: ctx+"/feedback/toMain?taskInfoId="+row.id,
		  	        buttons: [
		  	            {
		  	                text: '提交反馈',
		  	                iconCls: 'icon-ok',
		  	                handler: function () {
		  	                	$.messager.confirm('温馨提示！','确认提交此反馈信息？',function(result){
		  	                		if(result){
				  	                	submitForm(feedback_dialog, null);
				  	                	s_datagrid.datagrid('reload');
		  	                		}
		  	                	});
		  	                }
		  	            },
		  	            {
		  	            	text: '重置',
		  	                iconCls: 'icon-reload',
		  	                handler: function () {
		  	                	$('#feedback_form').form('reset');
		  	                	KindEditor.instances[0].html("");
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
	  	
	  	//申请办结
	  	function applyForEnd() {
	  		var row = s_datagrid.datagrid('getSelected');
	  	    if (row) {
		  		$.messager.confirm('温馨提示！','确认申请办结此事项？',function(result){
	          		if(result){
	          			$.ajax({
	  	            		async: false,
	  	            		cache: false,
	  	                    url: ctx + '/taskInfo/applyForEnd/'+row.id,
	  	                    type: 'post',
	  	                    dataType: 'json',
	  	                    success: function (data) {
	  	                        if (data.status) {
	  	                        	s_datagrid.datagrid('load');
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
  </head>
  <body class="easyui-layout">
  <div data-options="region:'west',border:false" style="width: 30px; height: auto;">
	<a href="javascript:void(0);" id="bt_dqs" class="easyui-linkbutton" data-options="toggle:true,group:'west',selected:true" style="height: 25%" onclick="statusName('WAIT_FOR_CLAIM', 'dqs');">待签收</a>
	<a href="javascript:void(0);" id="bt_blz" class="easyui-linkbutton" data-options="toggle:true,group:'west'" style="height: 25%" onclick="statusName('IN_HANDLING', 'blz');">办理中</a>
	<a href="javascript:void(0);" id="bt_ybl" class="easyui-linkbutton" data-options="toggle:true,group:'west'" style="height: 25%" onclick="statusName('FINISHED', 'ybl');">已办理</a>
  </div>
  <div data-options="region:'center',border:true">
	<div id="dqs" style="padding:2px 0; display: none;">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="details();">查看</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="claim();">签收</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="refuse();">拒绝</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="blz" style="padding:2px 0; display: none;">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="details();">查看</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="applyForEnd();">申请办结</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="feedback();">反馈</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="ybl" style="padding:2px 0; display: none;">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="details();">查看</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="feedback();">续报</a>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="tabs" class="easyui-tabs">
		<div title="省政府文件" data-options="selected:true, closable:false" style="padding:5 0 0 0;">
			<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
				<table style="width: 100%; border: dashed; border-color: highlight;">
					<tr align="center">
						<td>立项时间：</td>
						<td><input type="text" class="easyui-datebox" > - <input type="text" class="easyui-datebox" ></td>
						<td>办结时间：</td>
						<td><input type="text" class="easyui-datebox" > - <input type="text" class="easyui-datebox" ></td>
					</tr>
					<tr align="center">
						<td colspan="4">
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >搜索</a>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >重置</a>
						</td>
					</tr>
				</table>
			</div>
			<table id="s_datagrid"></table>
		</div>
		<div title="国务院文件" data-options="closable:false" style="padding:5 0 0 0;">
			<table id="g_datagrid"></table>
		</div>
		<div title="其他文件" data-options="closable:false" style="padding:5 0 0 0;">
			<table id="o_datagrid"></table>
		</div>
	</div>
  </div>
  </body>
</html>
