<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="templatemo-content">
    <form id="roleForm" method="post">
	    <input type="hidden" name="id">
	    <input type="hidden" name="createUserId">
	    <input type="hidden" name="createDate">
	    <input type="hidden" name="isDelete">
	    
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
		  	<tr class="active">
		  		<td colspan="2" align="center">角色信息</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">角色名称:</td>
		  		<td>
			    	<input name="name" class="easyui-textbox easyui-validatebox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">角色标识:</td>
		  		<td>
					<input name="type" class="easyui-textbox easyui-validatebox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">角色说明:</td>
		  		<td>
					<input name="remark" class="easyui-textbox">
		  		</td>
		  	</tr>
	  	</table>
	    </div>
	</form>
</div>
