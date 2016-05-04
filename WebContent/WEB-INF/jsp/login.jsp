<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<title>登录</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="${ctx}/css/htmleaf-demo.css">
	<link rel="stylesheet" href="${ctx}/css/style.css">
	<script src='${ctx}/js/shine.min.js'></script>
    <script src="${ctx}/js/index.js"></script>
</head>
<body>
  <div id="main-wrapper htmleaf-container">
  <div class="cont_principal">
   
    <div class="template-page-wrapper cont_join">
      <div style="background:#FFCC33; filter:alpha(opacity=50);margin-top:2px;padding-top:10px;padding-bottom:10px">
        	<img src='${ctx}/images/solg_login.png'/>
       </div> 
       <br/>
      <form class="form-horizontal" role="form" name="formLogin" action="" id="formLogin" method="post">
        <div id="username" class="form-group">
          <div class="col-sm-12">
              <input type="text" class="input_text" name="name" placeholder="用户名" value="发改委1">
          </div>              
        </div>
        <div id="password" class="form-group">
          <div class="col-sm-12">
              <input type="password" class="input_text" name="passwd" placeholder="密码" value="123">
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-12">
                <div class="form-inline">
	                <c:if test="${jcaptcha}">
						<input class="form-control" style="width: 20%; margin-left: 35%;" name="jcaptchaCode" id="jcaptchaCode" type="text" placeholder="验证码" nullmsg="请输入验证码!" />
						<img style="padding: 2px 2px;width: 20%;" align="absmiddle" id="jcaptcha" src="${ctx }/jcaptcha.jpg"/>
					</c:if>
                </div>
          </div>
        </div>
        <div id="errorMsg" class="form-group">
      		<div class="row">
		        <div id="msg" class="alert alert-danger sr-only" role="alert">
				  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				  <span class="sr-only">Error:</span>
				  ${msg }
 				   <button type="button" class="close" data-dismiss="alert" 
				      aria-hidden="true">
				      &times;
				   </button>
			    </div>
		    </div>
	    </div>
	   
        <div class="cont_btn_join">
             <button type="submit" class="">登录</button>
               
        </div>
        
      </form>
    </div>
   
    </div>
    
  </div>
  
<script>
	$(function() {
	    $("#jcaptcha").click(function() {
	        $("#jcaptcha").attr("src", '${ctx}/jcaptcha.jpg?'+new Date().getTime());
	    });
	    var msg = '${msg}';
	  	if(msg != ''){
	  		$("#msg").removeClass("sr-only");
	  	}
	});
</script>
</body>
</html>