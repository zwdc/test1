<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
$(function(){
	//角色（职位）
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
			var roleId = $("#roleId").val();
            $("#role").combobox('setValue',roleId);
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
		<input type="hidden" name="id" id="id"/>
		<input type="hidden" name="groupId" id="groupId"/>
		<input type="hidden" name="roleId" id="roleId"/>
		<input type="hidden" name="companyId" id="companyId"/>
		<!-- <input type="hidden" name="salt" id="salt"/> -->
		<input type="hidden" name="registerDate"/>
		<input type="hidden" name="isDelete"/>
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
            <label>角色:</label>
			<input id="role" name="role.id" class="easyui-textbox" required="required" />
			<input name="role_name" id="role_name" type="hidden" />
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
