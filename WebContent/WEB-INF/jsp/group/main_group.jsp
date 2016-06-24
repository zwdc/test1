<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="templatemo-content">
     <form id="groupForm" method="post">
	    <input type="hidden" name="id">
	    <input type="hidden" name="isDelete">   
	    <input type="hidden" name="createUser">
	    <input type="hidden" name="createDate">  
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
		  	<tr class="active">
		  		<td colspan="2" align="center">部门信息</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">部门名称:</td>
		  		<td>
			    	<input id="name" name="name" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">办公室主任:</td>
		  		<td>
			    	<input id="name" name="leader_name" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">联系方式:</td>
		  		<td>
			    	<input id="name" name="leader_phone" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">主管市长:</td>
		  		<td>
			    	<input id="name" name="major_name" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">联系方式:</td>
		  		<td>
			    	<input id="name" name="major_phone" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">部门编号:</td>
		  		<td>
					<input id="type" name="type" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
	  	</table>
	    </div>
	</form>
</div>
