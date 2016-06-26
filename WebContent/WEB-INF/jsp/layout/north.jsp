<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" charset="utf-8">
	function logout() {
		$.messager.confirm("提示", "确认退出吗?",function(r){
			if(r){
				window.location.href="${ctx}/logout";
			}
		});
		
	}

	var userInfoWindow;
	function showUserInfo() {
		userInfoWindow = $('<div/>').window({
			modal : true,
			title : '当前用户信息',
			width : 380,
			height : 260,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			href : ctx+'/user/userInfo',
			onClose : function() {
				$(this).window('destroy');
			}
		});
	}
    function getDateDemo(){  
        /*  
          
            //声明时间  
            var date = new Date();  
            alert(date);//当前时间  
            alert(date.toLocaleString());//转化为本地时间  
            alert(date.getFullYear());//显示年份  
            alert(date.getMonth() + 1);//显示月份 0-11，需要加1  
            alert(date.getDate());//显示一月中的日期  
            alert(date.getDay());//显示一周的日期，星期几  
            alert(date.getHours());//获取小时时间  
            alert(date.getMinutes());//获取当前分钟  
            alert(date.getSeconds());//获取当前秒数  
            alert(date.getMilliseconds());//获取当前的毫秒数  
            alert(date.getTime());//获取从1970年1月1日午夜零时，到当前时间的毫秒值  
        */    
            //分别获取年、月、日、时、分、秒  
            var myDate = new Date();  
            var year = myDate.getFullYear();  
            var month = myDate.getMonth() + 1;  
            var date = myDate.getDate();  
            var hours = myDate.getHours();  
            var minutes = myDate.getMinutes();  
            var seconds = myDate.getSeconds();  
              
            //月份的显示为两位数字如09月  
            if(month < 10 ){  
                month = "0" + month;  
            }  
            if(date < 10 ){  
                date = "0" + date;  
            }  
              
            //时间拼接  
            var dateTime = year + "年" + month + "月" + date + "日" + hours + "时" + minutes + "分" + seconds + "秒";  
              
            //document.write(dateTime);//打印当前时间  
              
            var divNode = document.getElementById("time");  
            divNode.innerHTML = dateTime;  
          
    }     
    window.setInterval("getDateDemo()",1000);//每隔1秒，调用一次getDateDemo()   
	 
</script>
<div style="magin:0px;">
	<img src='${ctx }/images/solg_login.png'/ height=40>
	 
	<div style="position: absolute; right: 10px; bottom: 5px; ">
	    <ul>
	    	<li>
	    	<shiro:principal/>&nbsp;&nbsp;你好，欢迎登录！&nbsp;&nbsp;
	    	</li>
	        <li>
	    	 <div id="time" class="easyui-linkbutton"></div>
	    	</li>
	    	 <li>
	    	 <a href="javascript:void(0);" class="easyui-linkbutton" ><i class="fa fa-comments fa-large"></i>消息</a>
	    	</li>
	    	 <li>
	    	<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu"><i class="fa fa-cogs fa-large"/> 控制面板</a> 
	    	</li>
	    </ul>	
	</div>
	<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	    <div onclick="showUserInfo();"><i class="fa fa-lock fa-large"/> 修改密码</div>
		<div onclick="showUserInfo();"><i class="fa fa-user fa-large"/> 个人信息</div>
		<div class="menu-sep"></div>
		<div onclick="logout();"><i class="fa fa-sign-out fa-large"/> 退出系统</div>
	</div>
</div>