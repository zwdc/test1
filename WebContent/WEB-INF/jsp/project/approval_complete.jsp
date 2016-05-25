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
var pid=${project.id};
$(function() {
	//数据列表
	feedback_datagrid = $('#feedback_datagrid').datagrid({
		url: ctx+'/feedback/getFeedbackByProject?projectId='+pid,
        width : 'auto',
		height : 'auto',
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		nowrap:false,
        columns : [
             [
              {field: 'status', title: '反馈状态', width: fixWidth(0.08), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value==null) {           			
                      	  return "未反馈";
                      }else if(value=="FEEDBACKING"){
                    	  return "反馈中"; 
                      }else if(value=="ACCEPT"){
                    	  return "已采用"; 
                      }else if(value=="RETURNED"){
                    	  return "被退回"; 
                      }
            	  }
			},
			  {field: 'feedbackStartDate', title: '反馈期间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
					  formatter:function(value,row){
						  return moment(value).format("MM月DD日")+"-"+moment(row.feedbackEndDate).format("MM月DD日");
					  }
				},
              {field: 'groupName', title: '牵头单位', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'feedbackUser', title: '填报人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'feedbackDate', title: '反馈时间', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row){
            		  if(value==null){
            			  return "--"
            		  }else{
            			  return moment(value).format("YYYY-MM-DD HH:mm:ss");
            		  }
            		 
				  }
              },
              
              {field: 'delayCount', title: '延期次数', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'refuseCount', title: '退回次数', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true}  
        ]
     ],
		  rowStyler:function(index,row){
			if (row.warningLevel=="1") {           			
	           return 'background-color:yellow;color:black';
	        }else if(row.warningLevel=="2"){
	      	    return 'background-color:red;color:white';
	        }else if(row.status=="ACCEPT"){
	        	return 'background-color:green;color:white';
	        }
		  },
    });
	
	$("#assistantGroup").kindeditor({readonlyMode: true});
	$("#remark").kindeditor({readonlyMode: true});
	$("#suggestion").kindeditor({readonlyMode: true});
});
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
</script>
<div class="easyui-layout">
<form id="form" action="${ctx }/project/approvalComplete" method="post">
	<input name="projectId" value="${project.id }" type="hidden">
	<input id="taskId" name="taskId" type="hidden">
    <table id="sales" class="table table-bordered table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">任务信息</td>
		</tr>
		<tr>
			<td class="text-right">任务内容:</td>
			<td colspan="3"><textarea class="easyui-kindeditor" 
					data-options="readonlyMode:true"  rows="2">${taskInfo.title }</textarea></td>
		</tr>
		<tr>
			<td class="text-right">任务简称:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.title }"
				data-option="prompt:'牵头部门'"  disabled="disabled" ></td>
			<td class="text-right">急缓程度:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${zwdc:getUrgencyType(taskInfo.urgency) }"
				data-option="prompt:'急缓程度'"  disabled="disabled" ></td>

		</tr>
		<tr>
			<td class="text-right">任务来源:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.taskSource.name }"
				data-option="prompt:'任务来源'"  disabled="disabled" ></td>
			<td class="text-right">反馈频度:</td>
			<td><input type="text" class="easyui-textbox" 
			    value="${taskInfo.fbFrequency.name }"
				data-option="prompt:'反馈频度'"  disabled="disabled" ></td>
		</tr>
		<tr>
			<td class="text-right">开始时间:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.createTaskDate }' type='both'/>"></td>
			<td class="text-right">办结时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.endTaskDate }' type='both'/>"></td>
		</tr>
		<tr>
			<td class="text-right">签收时限:</td>
			<td><input class="easyui-datetimebox"
				data-options="prompt:'反馈时限'" disabled="disabled"
				value="<fmt:formatDate value='${taskInfo.claimLimitDate }' type='both'/>"></td>
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
		<tr><td colspan="4">
		  <div id="feedbacktoolbar" style="padding:2px 0">
			<table>   
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
					</td>
				</tr>
			</table>
		   </div>
	    </td></tr>
  		<tr>
  			<td colspan="4">
				<table id="feedback_datagrid"></table>
  			</td>
  		</tr>
  		<tr>
	  		<td colspan="4">
	  			<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;审批意见:
	  			<textarea class="easyui-kindeditor" name="comment" rows="3"></textarea>
	  		</td>
	  	</tr>
  	</table>
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(true);" data-options="iconCls:'icon-ok'">同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(false);" data-options="iconCls:'icon-remove'">不同意</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="closeDialog();" data-options="iconCls:'icon-cancel'">关闭</a>
</div>
</div>
