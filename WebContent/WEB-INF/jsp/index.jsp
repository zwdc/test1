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
    <script type="text/javascript" src="${ctx}/js/messenger.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/messenger-theme-flat.js"></script>
	<style type="text/css">
		.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
 		#tree_user a.l-btn span.l-btn-text {
		    display: inline-block;
		    height: 24px;
		    line-height: 24px;
		    margin: 0px 4px 0px 10px;
		    padding: 0px 0px 0px 0px;
		    vertical-align: baseline;
		    /* width: 128px; */
		}
		#tree_user a.l-btn span.l-btn-icon-left {
		    background-position: left center;
		    padding: 0px 4px 0px 24px;
		}
		#tree_user .panel-body {
			padding:5px;
		}
		#tree_user span:focus{
			outline: none;
		}
	</style>
	<script type="text/javascript">
		$(function(){
			initMenu();
			
			if (jqueryUtil.isLessThanIe8()) {
				$.messager.alert('警告','<br/>您使用的浏览器版本太低！<br/><br/>建议您使用【谷歌浏览器】或【火狐浏览器】或【360极速浏览器】来获得更好的页面体验效果！','warning');
			}
			
			Messenger.options = {
   			    extraClasses: 'messenger-fixed messenger-on-top messenger-on-right',
   			    theme: 'flat'
   			}
			
			//待签收任务数量
			$.ajax({
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
	
	var zTree;
	function initMenu(){
		var role = $("#role").val();
		if(role == 'admin'){
			$.post(ctx+"/menuAdmin", function(treeNodes) {
				zTree = $.fn.zTree.init($("#tree_admin"), setting_admin, treeNodes);
				var nodes = zTree.getNodes();
				for (var i=0, l=nodes.length; i<l; i++) {
					zTree.expandNode(nodes[i], true, false, false, false);
				}
			}, "JSON").error(function() {
				$.messager.alert("提示", "获取菜单出错,请重新登陆!");
			});
			
		} else {
			var $ml=$("#tree_user");
			$ml.accordion({animate:true,fit:true,border:false});
			$.post(ctx+"/menu", function(rsp) {
				$.each(rsp,function(i,e) {
					var menulist = "<div class=\"well well-small\">";
					if(e.children && e.children.length>0){
						$.each(e.children,function(ci,ce){
							menulist += "<a href=\"javascript:void(0);\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-add'\" onclick=\"addTab('"+ce.name+"','"+ce.url+"');\">"+ce.name+"</a><br/>";
						});
					}
					menulist += "</div>";
					$ml.accordion('add', {
			            title: e.name,
			            content: menulist,
						border:false,
			            selected: false
			        });
				});
			}, "JSON").error(function() {
				$.messager.alert("提示", "获取菜单出错,请重新登陆!");
			});
		}
	}
	</script>
 </head>
 <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:50px;padding:0px;overflow: hidden;" href="${ctx }/north"></div>
	<div data-options="region:'west',split:true,title:'主要菜单'" style="width:200px;">
 		<!-- <div class="well well-small"> -->
			<shiro:hasRole name="admin">
				<span></span>
				<input value="admin" id="role" type="hidden">
				<ul id="tree_admin" class="ztree"></ul>
			</shiro:hasRole>
			<shiro:lacksRole name="admin">
				<div id="tree_user"></div>
			</shiro:lacksRole>
		<!-- </div> -->
	</div> 
	<div data-options="region:'south',border:false" style="height:25px;padding:3px;" href="${ctx }/south"></div>
	<div data-options="region:'center',plain:false" style="overflow: hidden;"  href="${ctx }/center"></div>
</body>
</html>
