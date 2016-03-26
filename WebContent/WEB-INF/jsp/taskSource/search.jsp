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
					<option value="name">来源名称</option>
					<option value="taskInfoType.name">任务类型</option>
					<option value="sourceDate">来源日期</option>
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
				<!-- <a style="display: none;" href="javascript:void(0);" onclick="searchRemove(this);">删除</a> -->
				<button type="button" style="display: none;" class="close" data-dismiss="alert" aria-label="Close" onclick="searchRemove(this);"><div aria-hidden="true">&times;</div></button>
			</td>
		</tr>
    </table>
</div>
</form>
    
