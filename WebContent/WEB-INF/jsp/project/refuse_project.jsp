<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-layout">
<form id="refuse_form" method="post">
	<input id="projectId" name="projectId" type="hidden">
    <table class="table table-bordered table-hover">
		<tr>
			<td colspan="4">拒绝原因:<textarea class="easyui-kindeditor" id="refuseReason" name="refuseReason" rows="3" ></textarea></td>
		</tr>
  	</table>
</form>
</div>