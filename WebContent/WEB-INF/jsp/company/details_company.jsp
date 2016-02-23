<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="company" class="templatemo-content">
    <blockquote>
		<span class="glyphicon glyphicon-th-list"></span>&nbsp;公司详情&nbsp;
    </blockquote>
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
	  	<tr class="active">
	  		<td colspan="4" align="center">公司详细信息</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">公司名称:</td>
	  		<td>
	  			${company.name}
	  		</td>
	  		<td class="text-right">公司地址:</td>
	  		<td>
	  			${company.address}
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">联系电话:</td>
	  		<td>
	  			${company.phone}
	  		</td>
	  		<td class="text-right">说明:</td>
	  		<td>
	  			${company.note}
	  		</td>
	  	</tr>
	  	</table>
	    </div>
	  	<!-- <button type="button" id="save" class="btn btn-primary btn-sm" onclick="submitForm()">添加</button>
	  	<button type="button" id="clear" class="btn btn-warning btn-sm" onclick="clearForm()">重置</button>
	  	<button type="button" id="delete" class="btn btn-danger btn-sm" onclick="deleteForm()">删除</button> -->
</div>
