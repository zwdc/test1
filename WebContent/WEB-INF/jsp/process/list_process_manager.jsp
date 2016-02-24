<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>流程定义</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/process.js"></script>
	<script type="text/javascript">
		$(function(){
			var message = '${message }';
			if(message != ''){
				$.messager.show({
					title : "提示",
					msg : message,
					timeout : 1000 * 2
				});
			}
		});
	</script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>流程定义</strong></span>进行部署!  &nbsp;<span class="label-info"><strong>提示</strong></span>可以单个文件加载，
			也可以多个文件同时加载！点击xml或者png链接可以查看具体内容！<br/><br/>
			<span class="label-info"><strong>加载</strong></span>: 将重新加载指定流程，如果流程文件没有变化则不会重复部署，如果流程发生变化则会部署新的流程。
			<span class="label-info"><strong>重新部署全部流程</strong></span>: 将重新扫描所有流程资源包，如果部署资源没有变化则不会重复部署，如果资源发生变化则会部署新的流程。
		</p>
	</div>
	<div id="tb" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="redeploy();">加载</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="convert_to_model();">转换为模型</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"  onclick="delRows();">删除</a>&nbsp;|&nbsp;
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="deploy();">部署流程</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="deployAll();">重新部署全部流程</a>
				</td>
			</tr>
		</table>
	</div>
	<fieldset id="deployFieldset" style="display: none">
		<legend style="margin-left: 10px" align="left">部署新流程</legend>
		<div align="left">
		<b>支持文件格式：</b>zip、bar、bpmn、bpmn20.xml<br /><br />
		<form action="${ctx }/process/deploy" method="post" enctype="multipart/form-data">
			<input type="file" name="file" />
			<input type="submit" class="input_button4" value="Submit" />
		</form>
		</div>
	</fieldset>
	<table id="process" title="流程定义">
	</table>
  </body>
</html>
