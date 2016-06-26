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
    <link rel="stylesheet" href="${ctx}/css/messenger.css" type="text/css" />
    <link rel="stylesheet" href="${ctx}/css/messenger-theme-flat.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/js/tree_admin.js"></script>
	<script type="text/javascript" src="${ctx}/js/tree_user.js"></script>
    <script type="text/javascript" src="${ctx}/js/messenger.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/messenger-theme-flat.js"></script>
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
				async: false,
        		cache: false,
				type: 'post',
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
			
			Messenger.options = {
   			    extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
   			    theme: 'flat'
   			}
			
			//待签收任务数量
			$.ajax({
				async: false,
        		cache: false,
				type: 'post',
				dataType : "json",
				url: ctx+"/project/getProjectCount?type=1",
				error: function () {
					Messenger().post({
           			  message: "获取待签收任务数量失败！",
           		 	  hideAfter: 5,
           		 	  showCloseButton: true,
           		 	});
				},
				success:function(data){ 
					Messenger().post({
          			  message: "您当前有 " + data + " 条待签收交办事项！",
          		 	  hideAfter: 5,
          		 	  showCloseButton: true,
          		 	});
				}
			});
	});
	</script>
 </head>
 <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:50px;background-color:#E5EFFF;padding:0px;overflow: hidden;" href="${ctx }/north"></div>
	<div data-options="region:'west',split:true,title:'主要菜单'" style="width:200px;background:#EEE;">
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
	<div data-options="region:'south',border:false" style="height:25px;background:#E6F0FF;padding:3px;" href="${ctx }/south"></div>
	<div data-options="region:'center',plain:false" style="overflow: hidden;"  href="${ctx }/center"></div>
</body>
</html>
