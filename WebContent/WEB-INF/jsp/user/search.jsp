<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form id="gradeSearch" method="post">
<div class="table-responsive">
    <table class="table table-bordered table-hover table-condensed">
    	<tr class="active">
			<td>条件</td>
			<td>字段名</td>
			<td>条件</td>
			<td>值</td>
		</tr>
		<tr>
			<td>
				<select name="searchAnds" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'"> 
					<option value="and">并且</option>
					<option value="or">或者</option>
				</select>
			</td>
			<td>
				<select name="searchColumnNames" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'">
					<option value="name">用户名</option>
					<option value="id">用户编号</option>
					<option value="company.name">所属公司</option>
					<option value="group.name">所属部门</option>
					<option value="role.name">所属职位</option>
					<option value="registerDate">注册日期</option>
				</select>
			</td>
			<td>
				<select name="searchConditions" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'">
					<option value="=">等于</option>
					<option value="<>">不等于</option>
					<option value="<">小于</option>
					<option value=">">大于</option>
					<option value="like">模糊</option>
				</select>
			</td>
			<td><input id="searchVals" name="searchVals" class="easyui-textbox" required="required" style="width: 150px;">
				<button type="button" style="display: none;" class="close" data-dismiss="alert" aria-label="Close" onclick="searchRemove(this);"><spans aria-hidden="true">&times;</spans></button>
			</td>
		</tr>
    </table>
</div>
</form>
    


