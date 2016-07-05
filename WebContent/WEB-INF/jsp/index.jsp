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
    <script type="text/javascript" src="${ctx}/js/messenger.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/messenger-theme-flat.js"></script>
	<style type="text/css">
		#menuList a.l-btn span span.l-btn-text {
		    display: inline-block;
		    height: 14px;
		    line-height: 14px;
		    margin: 0px 0px 0px 10px;
		    padding: 0px 0px 0px 10px;
		    vertical-align: baseline;
		    width: 128px;
		}
		#menuList 	a.l-btn span span.l-btn-icon-left {
		    background-position: left center;
		    padding: 0px 0px 0px 20px;
		}
		#menuList .panel-body {
			padding:5px;
		}
		#menuList span:focus{
			outline: none;
		}
	</style>
	<script type="text/javascript">
		$(function(){
			if (jqueryUtil.isLessThanIe8()) {
				$.messager.alert('警告','<br/>您使用的浏览器版本太低！<br/><br/>建议您使用【谷歌浏览器】或【火狐浏览器】或【360极速浏览器】来获得更好的页面体验效果！','warning');
			}
			
			initMenu();
			
			Messenger.options = {
   			    extraClasses: 'messenger-fixed messenger-on-top messenger-on-right',
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
		
	function initMenu(){
		debugger;
		var $ml=$("#menuList");
		$ml.accordion({animate:true,fit:true,border:false});
		$.post(ctx+"/menu", function(rsp) {
			$.each(rsp,function(i,e){
				var menulist ="<div class=\"well well-small\">";
				if(e.child && e.child.length>0){
					$.each(e.child,function(ci,ce){
						//var effort=ce.name+"||"+ce.iconCls+"||"+ce.url;
						console.dir(ce.name+"--"+ce.url);
						menulist+="<a href=\"javascript:void(0);\" class=\"easyui-linkbutton\" data-options=\"plain:true\" onclick=\"addTab('"+ce.name+"','"+ce.url+"');\">"+ce.name+"</a><br/>";
					});
				}
				menulist+="</div>";
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
	</script>
 </head>
 <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:50px;padding:0px;overflow: hidden;" href="${ctx }/north"></div>
	<div data-options="region:'west',split:true,title:'主要菜单'" style="width:200px;">
<%-- 		<div class="well well-small">
			<shiro:lacksRole name="admin">
				<span></span>
				<input value="user" id="role" type="hidden">
				<ul id="tree_user" class="ztree"></ul>
			</shiro:lacksRole>
			<shiro:hasRole name="admin">
				<span></span>
				<input value="admin" id="role" type="hidden">
				<ul id="tree_admin" class="ztree"></ul>
			</shiro:hasRole>
		</div> --%>
		<div id="menuList"></div>
	</div> 
	<div data-options="region:'south',border:false" style="height:25px;padding:3px;" href="${ctx }/south"></div>
	<div data-options="region:'center',plain:false" style="overflow: hidden;"  href="${ctx }/center"></div>
</body>
</html>
