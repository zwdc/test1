<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="templatemo-content">
    <blockquote>
		<span class="glyphicon glyphicon-th-list"></span>&nbsp;部门信息&nbsp;
    </blockquote>
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
		  		<td class="text-right">部门类型:</td>
		  		<td>
					<input id="type" name="type" class="easyui-textbox" required="required">
		  		</td>
		  	</tr>
	  	</table>
	    </div>
	</form>
</div>
