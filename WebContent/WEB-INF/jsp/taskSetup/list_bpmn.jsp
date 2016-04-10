<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>设定审批人员</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/userTask.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/choose/user/user.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/choose/group/choose_group.js"></script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此您可以<span class="label-info"><strong>动态的</strong></span>为每个<span class="label-info"><strong>流程定义</strong></span>文件设定审批人员。
			<span class="label-info"><strong>提示：</strong></span>如果bpmn文件结构没有改变，则不需要<span class="label-info"><strong>初始化所有</strong></span>。
			如果哪个文件发生变化，则重新<span class="label-info"><strong>加载</strong></span>此文件即可，单个加载是不会清除原有数据的。
		</p>
	</div>
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="setAuthor();">设定人员</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="loadSingle();">加载</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="initialization();">初始化所有</a>
				</td>
			</tr>
		</table>
	</div>
	<table id="bpmn_datagrid" title="流程任务列表"></table>
	
    <div id="dialog-form" title="设定审批人员" class="easyui-dialog" closed="true" style="margin-top: 5px;">
		<div data-options="region:'north',border:false" title="" style="overflow: hidden; padding: 5px;">
			<div class="well well-small">
				<span class="badge" iconCls="icon-save" plain="true" id="tishi" title="提示">提示</span>
				<p>
					请选择各个节点需要审批的<span class="label-info"><strong>人员</strong></span>、<span class="label-info"><strong>候选人</strong></span>和<span class="label-info"><strong>候选组</strong></span>
				</p>
			</div>
		</div>
		<form id="model_form" class="form-inline" method="post">
			<div class="form-group" id="modelTable"></div>
		</form>
	</div>

	</div>
  </body>
</html>
