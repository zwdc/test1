<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="templatemo-content">
    <blockquote>
		<div id="add"><span class="glyphicon glyphicon-th-list"></span>&nbsp;添加公司&nbsp;</div>
		<div id="update"><span class="glyphicon glyphicon-th-list"></span>&nbsp;修改公司信息&nbsp;</div>
    </blockquote>
    <form id="companyForm" method="post">
	    <input type="hidden" name="id">
	    <input type="hidden" name="createUserId">
	    <input type="hidden" name="createDate">
	    <input type="hidden" name="isDelete">
	        
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
	  	<tr class="active">
	  		<td colspan="4" align="center"><div id="tdAdd">添加公司信息</div><div id="tdUpdate">修改公司信息</div></td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">公司名称:</td>
	  		<td>
		    	<input type="text" name="name" class="easyui-textbox" prompt="填写公司名称" required="required">
	  		</td>
	  		<td class="text-right">公司地址:</td>
	  		<td>
		    	<input type="text" name="address" class="easyui-textbox" required="required">
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">联系电话:</td>
	  		<td>
				<input name="phone" class="easyui-textbox" required="required">
	  		</td>
	  		<td class="text-right">说明:</td>
	  		<td>
				<input name="note"  class="easyui-textbox" type="text"/>
	  		</td>
	  	</tr>
	  	</table>
	    </div>
	  	<!-- <button type="button" id="save" class="btn btn-primary btn-sm" onclick="submitForm()">添加</button>
	  	<button type="button" id="clear" class="btn btn-warning btn-sm" onclick="clearForm()">重置</button>
	  	<button type="button" id="delete" class="btn btn-danger btn-sm" onclick="deleteForm()">删除</button> -->
	</form>
</div>
