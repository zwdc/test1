<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <title>欢迎</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/tree_admin.js"></script>
	<script type="text/javascript" src="${ctx}/js/tree_user.js"></script>
	<style type="text/css">
		.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
	</style>
	<script type="text/javascript">
		var zTree;
		var treeNodes;
	
		$(function(){
			if (jqueryUtil.isLessThanIe8()) {
				$.messager.alert('警告','<br/>您使用的浏览器版本太低！<br/><br/>建议您使用【谷歌浏览器】或【火狐浏览器】或【360极速浏览器】来获得更好的页面体验效果！','warning');
			}
			
			//菜单
			$.ajax({
				async : false,			//同步，等待success完成后继续执行。
				cache : false,
				type: 'POST',
				dataType : "json",
				url: ctx+"/menu",
				error: function () {
					alert('menu请求失败');
				},
				success:function(data){ 
					treeNodes = data;
				}
			});
			
			var role = $("#role").val();
			if(role == 'admin'){
				zTree = $.fn.zTree.init($("#tree_admin"), setting_admin, treeNodes);
			}else{
				zTree = $.fn.zTree.init($("#tree_user"), setting_user, treeNodes);
			}
			var nodes = zTree.getNodes();
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.expandNode(nodes[i], true, false, false, false);
			}
		});
		
	</script>
 </head>
 <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:40px;background:#EEE;padding:10px;overflow: hidden;" href="${ctx }/north"></div>
	<div data-options="region:'west',split:true,title:'主要菜单'" style="width:200px;">
		<div class="well well-small">
			<shiro:lacksRole name="admin">
				<span>用户功能</span>
				<input value="user" id="role" type="hidden">
				<ul id="tree_user" class="ztree"></ul>
			</shiro:lacksRole>
			<shiro:hasRole name="admin">
				<span>管理员功能</span>
				<input value="admin" id="role" type="hidden">
				<ul id="tree_admin" class="ztree"></ul>
			</shiro:hasRole>
		</div>
		<div id="divSortContent"></div> <!-- 调试输出用，暂时先留着 -->
	</div> 
	<div data-options="region:'south',border:false" style="height:25px;background:#EEE;padding:5px;" href="${ctx }/south"></div>
	<div data-options="region:'center',plain:true,title:'欢迎使用'" style="overflow: hidden;"  href="${ctx }/center"></div>
</body>
</html>
