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
var fb=${feedback};
$(function() {
	//数据列表
	feedback_datagrid = $('#feedback_datagrid').datagrid({
        //url: ctx+"/feedback/getList",
        width : 'auto',
		height : 'auto',
		rownumbers:true,
		border:false,
		singleSelect:true,
		data:fb,
		striped:true,
		nowrap:false,
        columns : [
             [
              {field: 'warningLevel', title: '反馈状态', width: fixWidth(0.08), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value){
            		  if (value=="1") {           			
                      	  return "开始反馈";
                      }else if(value=="2"){
                    	  return "逾期反馈"; 
                      }else if(value=='3'){
                    	  return "反馈被退回"; 
                      }else if(value=='4'){
                    	  return "反馈被采纳"; 
                      }else if(value=='5'){
                    	  return "反馈中";
                      }else{
                    	  return "未到反馈期";
                      }
            	  },
            	  styler:function(index,row){
            		  if (row.warningLevel=="1") {           			
                         return 'background-color:yellow;color:white';
                       }else if(row.warningLevel=="2"){
                     	  return 'background-color:red;color:white';
                       }else if(row.warningLevel=='3'){
                     	  return 'background-color:red;color:white';
                       }else if(row.warningLevel=='4'){
                     	  return 'background-color:green;color:white';
                       }else if(row.warningLevel=='5'){
                     	  return 'background-color:blue;color:white';
                       }else{
                     	  return ;
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
     toolbar: "#feedbacktoolbar"
    });
	//feedback_datagrid.datagrid("loadData",fb);
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
</script>
<div class="easyui-layout">
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
  	</table>
  	<div id="feedbacktoolbar" style="padding:2px 0">
	<table>   
		<tr>
		<td style="padding-left:2px">
			<!--  <shiro:hasRole name="admin">
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addFeedback();">添加反馈</a>-->
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="feedback();">承办反馈</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="check();">反馈审核</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
				<!--<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editFeedback();">编辑</a>
		    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delFeedback();">删除</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="detailsFeedback();">详情</a>
			</shiro:hasRole>-->
		</td>
		<td style="padding-left:5px">
			<input id="searchbox" type="text"/>
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
