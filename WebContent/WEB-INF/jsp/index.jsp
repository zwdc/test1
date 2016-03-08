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
    <script type="text/javascript" src="${ctx}/js/goeasy.js"></script>
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
   			    extraClasses: 'messenger-fixed messenger-on-top messenger-on-right',
   			    theme: 'flat'
   			}
			 var goEasy = new GoEasy({
		         appkey: '0cf326d6-621b-495a-991e-a7681bcccf6a',
		         onConnected: function() {
		        	alert("成功连接 GoEasy。");
	        	 },
	        	 onDisconnected: function() {
	        	 	alert("与 GoEasy 连接断开。");
	        	 },
	        	 onConnectFailed: function(error) {
	        	 	alert("与 GoEasy 连接失败，错误编码："+error.code+"错误信息："+error.content);
	        	 }
		     });
			//获取消息
			 goEasy.subscribe({
	             channel: "zwdc_user_${user.id}",
	             onMessage: function(message){  //1.获取消息
	            	 alert(message.content);
	            	 $.ajax({	//2.去任务表拿提示的任务
	                     url: ctx + '/processTask/getProcessTask/'+message.content,		
	                     type: 'post',
	                     dataType: 'json',
	                     success: function (data) {
	                         if(data != null) {
	                        	 var msg = Messenger().post({	//3.右上角显示消息弹窗
		       	                	  message: data.title,
		       	                	  hideAfter: 15,
		       	         		 	  showCloseButton: true,
		       	                	  actions: {
		       	                	    retry: {
		       	                	       label: '查看',
		       	                	       phrase: 'Retrying TIME',
		       	                	       auto: false,
		       	                	       delay: 15,
		       	                	       action: function() {
		       	                	    	 showMessage(data);	//4.点击查看后，显示要办理的任务
		       	                	       }
		       	                	    },
		       	                	    cancel: {
		       	                	       label: '取消',
		       	                	       action: function() {
		       	                	         return msg.cancel();
		       	                	       }
		       	                	    }
		       	                	  }
		                       	 });
	                         } else {
	                        	 Messenger().post({
	                   			  message: "未找到相关数据！",
	                   		 	  hideAfter: 3,
	                   		 	  showCloseButton: true,
	                   		 	});
	                         }
	                     }
	                 });
	             },
	             onSuccess: function () {
            	 	alert("Channel 订阅成功。");
            	 },
            	 onFailed: function (error) {
            	 	alert("Channel 订阅失败, 错误编码：" + error.code + " 错误信息：" + error.content)
            	 }
	         });
		});
		
		var message_dialog;
		function showMessage(data) {	//5.弹窗显示任务页面
			message_dialog = $('<div/>').dialog({
		    	title : "任务详情",
				top: 20,
				width : ($(this).width() - 50) * 0.7,
				height : 'auto',
		        modal: true,
		        minimizable: true,
		        maximizable: true,
		        href: ctx + data.url,
		        buttons: [
					{
					    text: '提交',
					    iconCls: 'icon-save',
					    id: 'save',
					    handler: function () {
					    	submitForm(message_dialog);
					    }
					},
		            {
		                text: '关闭',
		                iconCls: 'icon-cancel',
		                handler: function () {
		                	message_dialog.dialog('destroy');
		                }
		            }
		        ],
		        onClose: function () {
		        	message_dialog.dialog('destroy');
		        }
		    });
		}
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
