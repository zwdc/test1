<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="templatemo-content">
    <blockquote>
		<span class="glyphicon glyphicon-th-list"></span>&nbsp;部门信息&nbsp;
    </blockquote>
	    <div class="table-responsive">
	    <table class="table table-bordered table-hover">
		  	<tr class="active">
		  		<td colspan="2" align="center">部门信息</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">部门名称:</td>
		  		<td>
			    	${group.name}
		  		</td>
		  	</tr>
		  	<tr>
		  		<td class="text-right">部门类型:</td>
		  		<td>
		  			${group.type}
		  		</td>
		  	</tr>
	  	</table>
	    </div>
</div>
