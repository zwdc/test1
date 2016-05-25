<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="zwdc" uri="http://zwdc.com/zwdc/tags/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
<script type="text/javascript" src="${ctx}/js/app/feedback.js"></script>
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
              {field: 'warningLevel', title: '反馈状态', width: fixWidth(0.08), align: 'center', halign: 'center', sortable: true,
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
        }
	  },
    });
	
	$("#assistantGroup").kindeditor({readonlyMode: true});
	$("#remark").kindeditor({readonlyMode: true});
	$("#suggestion").kindeditor({readonlyMode: true});
	
	$("blockquote").css({
		"border-left": "5px solid #4EA76E",
		"font-size": "15px",
		"padding": "5px 10px",
		"margin": "0px 0px 10px"
	})
});

</script>
<div class="easyui-layout">
<form id="form" action="${ctx }/project/completeApprovalFailed" method="post">
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
		<c:if test="${!empty commentsList }">
	  	<tr>
	  		<td colspan="4">
				<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>&nbsp;
	  			<a data-toggle="collapse" href="#comments" aria-expanded="false" aria-controls="comments" title="点击查看审批意见">意见列表:</a>
	  			<div class="collapse" id="comments">
		  			<c:forEach var="comments" items="${commentsList }">
			  			<blockquote>
							<p class="text-primary">${comments.userName } : ${comments.content }</p>
	  						<footer><fmt:formatDate value="${comments.time }" type="both"/></footer>
					    </blockquote> 
					</c:forEach>
				</div>
	  		</td>
	  	</tr>
	  	</c:if>
  	</table>
  	<table class="table table-bordered table-condensed">
  		<tr class="bg-primary">
			<td colspan="4" align="center">反馈列表</td>
		</tr>
  		<tr>
  			<td>
				<table id="feedback_datagrid"></table>
  			</td>
  		</tr>
  	</table>
</form>
<hr style="margin-top: -5px ">
<div class="pull-right" style="margin: -15px 5px 5px 0px">
 	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="submit(true);" data-options="iconCls:'icon-ok'">确认</a>
</div>
</div>
