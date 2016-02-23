<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
	$(function() {
		$("#parentId").combotree({
			width:171,
			url:ctx+"/resource/getMenuList",
			idFiled:'id',
		 	textFiled:'name',
		 	parentField:'parentId'
		});
	});
</script>
<div class="templatemo-content">
    <blockquote>
		<span class="glyphicon glyphicon-th-list"></span>&nbsp;资源信息&nbsp;
    </blockquote>
    <form id="resourceForm" method="post">
	    <input type="hidden" name="id">
	    <input type="hidden" name="sort">   
	    
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
		  	<tr class="active">
		  		<td colspan="4" align="center">资源信息</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">资源名称:</td>
		  		<td>
			    	<input id="name" name="name" class="easyui-textbox easyui-validatebox" required="required">
		  		</td>
		  		<td class="text-right">权限字符串:</td>
		  		<td>
		  			<input id="url" name="permission" class="easyui-textbox easyui-validatebox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">资源路径:</td>
		  		<td>
					<input id="url" name="url" class="easyui-textbox easyui-validatebox">
		  		</td>
		  		<td class="text-right">资源类型:</td>
		  		<td>
			    	<select id="type" class="easyui-combobox" name="type" style="width:171px;" data-options="required:true">
						<option value="menu">菜单</option>
						<option value="button">操作</option>
					</select>
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">父编号:</td>
		  		<td>
		  			<input name="parentId"  class="easyui-combotree" id="parentId" type="text"/>
		  		</td>
		  		<td class="text-right">是否启用:</td>
		  		<td>
		  			<select id="available" class="easyui-combobox" name="isDelete" style="width:171px;" data-options="required:true">
						<option value="0">是</option>
						<option value="1">否</option>
					</select>
		  		</td>
		  	</tr>
  		  	<tr>
		  		<td colspan="4">
		  			<textarea class="form-control" name="note" rows="3"></textarea>
		  		</td>
	  		</tr>
	  	</table>
	    </div>
	</form>
</div>
