<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
$(function(){
	//部门
	$("#group").combobox({
		width:160,
		url:ctx+"/group/getAllGroup",
		valueField: 'id',
		textField: 'name',
		onSelect:function(value){
			$("#group_name").val(value.name);
		},
		required: true,
		onLoadSuccess: function (data) {
			var groupId = $("#groupId").val();
            $("#group").combobox('setValue',groupId);
        }
	});
	//角色
	$("#role").combobox({
		width:160,
		url:ctx+"/role/roleList",
		valueField: 'id',
		textField: 'name',
		onSelect:function(value){
			$("#role_name").val(value.name);
		},
		required: true,
		onLoadSuccess: function (data) {
			debugger;
			var roleId = $("#roleId").val();
            $("#role").combobox('setValue',roleId);
        }
	});
	
	//角色权限
	$('#dataPermission').combobox({
	    valueField:'id',
	    textField:'value',
	    data: [{
			id: '0',
			value: '仅本人'
		},{
			id: '1',
			value: '所在部门'
		},{
			id: '2',
			value: '所属角色'
		},{
			id: '3',
			value: '所在部门且所属角色'
		},{
			id: '4',
			value: '所在部门或所属角色'
		},{
			id: '5',
			value: '所有数据'
		}],
		onSelect: function(record){
			switch(record.id) {
				case '0':
					$("#dataPermission").val(0);
					break;
				case '1':
					$("#dataPermission").val(1);
					break;
				case '2':
					$("#dataPermission").val(2);
					break;
				case '3':
					$("#dataPermission").val(3);
					break;
				case '4':
					$("#dataPermission").val(4);
					break;
				case '5':
					$("#dataPermission").val(5);
					break;
			}
		}
	});
	
	//扩展easyui的validatebox
    $.extend($.fn.validatebox.defaults.rules, {
       //必须和某个字段相等
       equalTo: {
    	   validator: function (value, param) { 
    		   return $(param[0]).val() == value; 
    	   }, 
    	   message: '字段不匹配' 
       },
	   minLength: {
		   validator: function(value, param){
			   return value.length >= param[0];
		   },
		   message: '至少输入 {0} 个字符.'
	   }	
    });
	
	$("#rePassword").qtip({
		style: {
  	       classes: 'qtip-dark qtip-rounded'
  	    },
		position: {
			my: 'left center',
	        at: 'right center',
	        adjust: {
	            x: 10
	        }
	    }
	 });
	
})
</script>
<style type="text/css">
    #fm{
        margin:0;
        padding:10px 30px;
    }
    .ftitle{
        font-size:14px;
        font-weight:bold;
        padding:5px 0;
        margin-bottom:10px;
        border-bottom:1px solid #ccc;
    }
    .fitem{
        margin-bottom:5px;
    }
    .fitem label{
        display:inline-block;
        width:80px;
    }
    .fitem input{
        width:160px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/images/fromedit.png" style="margin-bottom: -3px;"/> 用户信息</div>
    <form id="user_form" method="post" >
		<input id="id" name="id" type="hidden" />
		<input id="groupId" name="groupId" type="hidden" />
		<input id="roleId" name="roleId" type="hidden" />
		
		<input name="registerDate" type="hidden" />
		<input name="isDelete" type="hidden" />
        <div class="fitem">
            <label>用户名:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="required">
        </div>
        <div class="fitem">
            <label>密码:</label>
            <input type="password" id="passwd" required="required"
                   name="passwd" class="easyui-textbox" 
                   missingMessage="请输入新密码." validType="minLength[3]" >
        </div>
        <div class="fitem">
            <label>确认密码:</label>
            <input type="password" id="repasswd"
                   class="easyui-textbox" validType="equalTo['#passwd']"
                   invalidMessage="两次输入密码不匹配.">
            <small><abbr id="rePassword" title="不需要修改原密码时，此项留空!" ><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></abbr></small>
        </div>
      <div class="fitem">
            <label>部门:</label>
			<input id="group" name="group.id" class="easyui-combobox" required="required" />
			<input name="group_name" id="group_name" type="hidden" />
        </div>
         <div class="fitem">
            <label>角色:</label>
			<input id="role" name="role.id" class="easyui-combobox" required="required" />
			<input name="role_name" id="role_name" type="hidden" />
        </div>
        <div class="fitem">
            <label>数据权限:</label>
			<input id="dataPermission" name="dataPermission" class="easyui-combobox" required="required" />
        </div>
        <div class="fitem">
            <label>状态:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="locked" name="locked" style="width: 20px;" value="0" /> 启用
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="locked" name="locked" style="width: 20px;" value="1" /> 停用
            </label>
        </div>
    </form>
</div>
