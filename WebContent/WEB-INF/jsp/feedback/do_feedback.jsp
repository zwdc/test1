<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
<script type="text/javascript">
	$(function() {
		$('#download').tooltip({
			position: 'right',
			content: '<span style="color:#fff">点击下载</span>',
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
			}
		});	 
		var count = 0; 
		 $("#filefield").click(function(){  
			var input = $("<input class='easyui-filebox' type='text' name='file' data-options=\""+"prompt:'请选择文件...'\""+" style='width: 40%;height: 25px;'>");  	
			var button = $("<input type='button' class='easyui-linkbutton' value='移除 '></div>");  
			var pre=$("<div>").append(button).append(input);		
			$("#fileZone").append(pre);
			$.parser.parse(pre);  //为了刷新页面
			button.click(function() {  
				pre.remove();
			});
		});
		 
		 $(".del").click(function() {
             var $p = $(this).parent();
             var $this = $(this);
             $.ajax({
            	 url:ctx+'/feedback/removeAttr/'+$this.attr("id"),
			  	 type:'post',
			  	 dataType:'json',
                 beforeSend: function() {
                     //发送请求前改变背景色
                     $p.css("backgroundColor", "#FB6C6C");
                 },
                 success: function() {
                     //删除成功
                     $p.slideUp(300, function() {
                         //移除父级div
                         $p.remove();
                     });
                 }
             });
		     //阻止浏览器默认事件
		     return false;
         });
		 
		  var situation = KindEditor.create('textarea[name="situation"]',{
			   readonlyMode : false, //只读模式 默认为false
			   width:'100%',
			   themeType : 'default', 			   
			   afterChange : function() {
			      //限制字数
			      var limitNum = ${limitSituation};  //设定限制字数
			      var pattern = '可以输入' + limitNum + '字'; 
			      $('.situation_surplus').html(pattern); //输入显示
			      if(this.count('text') > limitNum) {
			       pattern = ('字数超过限制，请适当删除部分内容');
			       //超过字数限制自动截取
			       var strValue = situation.text();
			       strValue = strValue.substring(0,limitNum);
			       situation.text(strValue);      
			       } else {
			       //计算剩余字数
			       var result = limitNum - this.count('text'); 
			       pattern = '你已输入'+this.count()+'字符（字数统计包含HTML代码。），还可以输入' +  result + '字'; 
			       }
			       $('.situation_surplus').html(pattern); //输入显示
			     },
			     items : ['fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist','file'],
		});
		  
		  var problems = KindEditor.create('textarea[name="problems"]',{
			   readonlyMode : false, //只读模式 默认为false
			   width:'100%',
			   themeType : 'default', 			   
			   afterChange : function() {
			      //限制字数
			      var limitNum = ${limitProblem};  //设定限制字数
			      var pattern = '可以输入' + limitNum + '字'; 
			      $('.problems_surplus').html(pattern); //输入显示
			      if(this.count('text') > limitNum) {
			       pattern = ('字数超过限制，请适当删除部分内容');
			       //超过字数限制自动截取
			       var strValue = problems.text();
			       strValue = strValue.substring(0,limitNum);
			       problems.text(strValue);      
			       } else {
			       //计算剩余字数
			       var result = limitNum - this.count('text'); 
			       pattern = '你已输入'+this.count('text')+'字符（字数统计包含HTML代码。），还可以输入' +  result + '字'; 
			       }
			       $('.problems_surplus').html(pattern); //输入显示
			     },
			     items : ['fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist','file'], 
		});
		  var solutions = KindEditor.create('textarea[name="solutions"]',{
			   readonlyMode : false, //只读模式 默认为false
			   width:'100%',
			   themeType : 'default', 			   
			   afterChange : function() {
			      //限制字数
			      var limitNum = ${limitSolution};  //设定限制字数
			      var pattern = '可以输入' + limitNum + '字'; 
			      $('.solutions_surplus').html(pattern); //输入显示
			      if(this.count('text') > limitNum) {
			       pattern = ('字数超过限制，请适当删除部分内容');
			       //超过字数限制自动截取
			       var strValue = solutions.text();
			       strValue = strValue.substring(0,limitNum);
			       solutions.text(strValue);      
			       } else {
			       //计算剩余字数
			       var result = limitNum - this.count('text'); 
			       pattern = '你已输入'+this.count('text')+'字符（字数统计包含HTML代码。），还可以输入' +  result + '字'; 
			       }
			       $('.solutions_surplus').html(pattern); //输入显示
			     },
			     items : ['fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist','file'], 
		});
	});
	
</script>
<div class="easyui-layout">
<form id="feedback_form" method="post" encType="multipart/form-data">
	<input type="hidden" id="feedbackId" name="id" value="${feedback.id }">
	<%-- <input type="hidden" name="createUserId" value="${feedback.createUserId }"/>
    <input type="hidden" name="createDate" value="<fmt:formatDate value='${feedback.createDate }' type='both'/>"/>
    <input type="hidden" name="isDelete" value="${feedback.isDelete }"/>
    <input type="hidden" name="status" value="${feedback.status }"/> --%>
	<table class="table table-bordered" style="width: 100%;">
		<tr class="bg-primary">
			<td colspan="6" align="center">填报反馈信息</td>
		</tr>
		<tr>
			<td class="text-right">起草人:</td>
			<td><input type="text" class="easyui-textbox"
				value="${user.name }" data-option="prompt:'起草人'" disabled="disabled"></td>
			<td class="text-right">牵头部门:</td>
			<td colspan="1"><input type="text" class="easyui-textbox" value="${feedback.project.group.name }"
				data-option="prompt:'牵头部门'" disabled="disabled"></td>
			<td class="text-right">反馈时限:</td>
			<td><input name="feedback.feedbackEndDate" class="easyui-datetimebox"
				data-options="prompt:'反馈时限'"
				value="<fmt:formatDate value='${feedback.feedbackEndDate}' type='both'/>" disabled="disabled"></td>	
		</tr>
		
		<tr>
			<td class="text-right">记录状态:</td>
			<td><input type="text" name="status" class="easyui-textbox"
				value="${(feedback.status eq 'FEEDBACKING' ) ? '反馈中' : ((feedback.status eq 'RETURNED') ? '已退回' : ((feedback.status eq 'ACCEPT') ? '已采用' : '未反馈')) }" data-option="prompt:'来源名称'"
				disabled="disabled"></td>
			<td class="text-right">是否延期:</td>
			<td><input type="text" name="delayCount" class="easyui-textbox"
				value="${feedback.delayCount>0?'延期':'未延期' }" data-option="prompt:'来源名称'"
				disabled="disabled"></td>
			<td class="text-right">反馈开始时间:</td>
			<td ><input name="feedback.feedbackStartDate" class="easyui-datetimebox"
				data-options="prompt:'反馈开始时间'"
				 value="<fmt:formatDate value='${feedback.feedbackStartDate}' type='both'/>" disabled="disabled"></td>
		</tr>
		<tr>
			<td class="text-right">阶段工作计划:</td>
			<td colspan="5"><textarea class="easyui-kindeditor"
					data-options="readonlyMode:true,prompt:'阶段工作计划'" rows="3">${feedback.workPlan }</textarea>
					
			</td>
		</tr>
		<tr>
			<td class="text-right">落实情况:</td>
			<td colspan="5">
			<span class="situation_surplus"></span>			
			<textarea name="situation" rows="3" class="easyui-kindeditor"  data-options="readonlyMode:false,prompt:'落实情况，最多不可超过100字'"  required="required" >${feedback.situation }</textarea>							
			</td>
		</tr>
		
		<tr>
			<td class="text-right">存在问题/困难:</td>
			<td colspan="5">
			<span class="problems_surplus"></span>
			<textarea class="easyui-kindeditor" name="problems" rows="3">${feedback.problems }</textarea>			
			</td>
		</tr>
		<tr>
			<td class="text-right">解决措施/建议:</td>
			<td colspan="5">
			<span class="solutions_surplus"></span>
			<textarea class="easyui-kindeditor" data-options="readonlyMode:false,prompt:'落实情况，最多不可超过100字'"
					name="solutions" rows="3">${feedback.solutions }</textarea>	
			</td>	
		</tr>
		<tr>
		  	<td class="text-right">佐证材料上传:</td>
		  	<td colspan="5">
		  		  <a id="filefield" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加附件</a> 
		  		   <input class='easyui-filebox' type='hidden' name='file'>
		  		   <div id="fileZone"> </div> 
		  		  <c:forEach items="${feedback.fdaList}" var="fda"> 
	   		     	<div class="well well-small">
   					    <a href="${ctx }${fda.url}" target=blank> 
   						<img src="${ctx }/images/icon/${fda.type}.gif" title="" >${fda.uploadDate} - ${fda.name}   
 					    </a> - <input id="${fda.id}" type='button' class='easyui-linkbutton del' value='移除 '>
 					</div>
				 </c:forEach>    	
		   </td>
		</tr>   	
	</table>    
</form>

</div>