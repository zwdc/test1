<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<body>
	<hgroup>
		<h2><b>对不起您没有权限！</b></h2>
		<h2>给您带来的不便我们深表歉意! <a href="${ctx }/" style="color: #08c;">返回网站首页</a>.</h2>
	</hgroup>
</body>
</html>
