<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="easyui-layout">
    <table class="table table-bordered table-hover">
	  	<tr class="active">
	  		<td colspan="2" align="center">个人信息</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">用户名:</td>
	  		<td>${user.name}</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">所属公司:</td>
	  		<td>${user.company.name}</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">所属部门:</td>
	  		<td>${user.group.name}</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">所属角色:</td>
	  		<td>${user.role.name}</td>
	  	</tr>
  	</table>
</div>
