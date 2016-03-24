<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
	$(function() {
		$("#parentId").combotree({
			width:180,
			url:ctx+"/taskType/getList",
			idFiled:'id',
		 	textFiled:'name',
		 	parentField:'parentId'
		});
		
		$('#title').tooltip({
			position: 'right',
			content: '<span style="color:#fff">留空为根目录！</span>',
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
			}
		});
	});
</script>
<form id="taskTypeForm" method="post">
    <input type="hidden" name="id">
    <input type="hidden" name="isDelete">
    <table class="table table-bordered table-hover">
	  	<tr class="bg-primary">
	  		<td colspan="2" align="center">任务类型</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">类型名称:</td>
	  		<td>
		    	<input type="text" name="name" class="easyui-textbox" data-option="prompt:'类型名称'" required="required">
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="text-right">父编号:</td>
	  		<td>
	  			<input name="parentId"  class="easyui-combotree" id="parentId" type="text"/>
	  			<small><abbr id="title"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></abbr></small>
	  		</td>
	  	</tr>
  	</table>
</form>
