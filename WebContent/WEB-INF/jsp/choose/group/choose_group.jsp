<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<title>选择用户组</title>
</head>

<body>
	<input type="hidden" id="multiSelect" value="${multiSelect }"/>
	<input type="hidden" id="key" value="${key }"/>
	<table id="group_datagrid" title="用户组列表"></table>
    
</body>

</html>
