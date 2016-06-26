<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>首页</title>
</head>
<body>
	<div data-options="region:'north',border:false" title="" style="overflow: hidden; padding: 5px;">
		<div class="well well-small">
			<span class="badge" plain="true" >使用说明</span>
			<p>
				<shiro:hasRole name="admin">
					<br/><br/>
					1、在<span class="label-info"><strong>用户管理</strong></span>界面点击<span class="label-info"><strong>同步用户</strong></span>按钮，把系统用户和组关系同步到activiti默认的用户表和组表中。<br/><br/>
					
					2、在<span class="label-info"><strong>流程定义管理</strong></span>界面点击<span class="label-info"><strong>重新部署全部流程</strong></span>按钮，系统可以把resources/deploy目录下得所有以.zip或.bar结尾的流程文件部署到系统当中。<br/>
					   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;只有完成了流程的部署，才可以发布任务和完成任务。如果某一个流程描述文件改变了，也可以单个部署这个文件或者点击加载。<br/><br/>
					
					3、在<span class="label-info"><strong>审批设定</strong></span>界面点击<span class="label-info"><strong>初始化</strong></span>按钮，将每个流程描述文件中的用户任务节点初始化到t_user_task表中，此表是用来保存流程文件中设定好的<br/>
					   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审批人员。初始化完成后，逐个设定审批人员（点击设定人员按钮即可）。如果某个流程描述文件新增或减少了用户任务，则点击设定人员后面的加载按钮即可同步节点。
				</shiro:hasRole>
				<shiro:user>
					<br/><br/>
					
					1、所有在待办事项中提示的操作任务，不能在菜单中单独操作。例如：待办任务中提示填写销售审批表，那么就要在代办任务中<span class="label-info"><strong>选中此条待办任务</strong></span>点击<span class="label-info"><strong>办理</strong></span>按钮，
					而不能在菜单中的销售审批列表中去添加此审批表信息。<br/><br/>
					
					2、代办任务中的<span class="label-info"><strong>暂存</strong></span>功能只会暂时存储数据，不会完成此项待办任务，点击<span class="label-info"><strong>提交任务</strong></span>后才会继续向下走流程。<br/><br/>
					
					3、所有<span class="label-info"><strong>审批失败</strong></span>的业务表单，要在<span class="label-info"><strong>待办任务</strong></span>中办理修改后<span class="label-info"><strong>重新提交审批</strong></span>，而不能删除后重新添加。&nbsp;<span class="label label-danger">重要</span><br/><br/>
					
					4、所有<span class="label-info"><strong>审批通过</strong></span>的业务表单，如果需要修改，则修改后需要进行<span class="label-info"><strong>重新审批</strong></span>，并且<span class="label-info"><strong>一定要在备注中以红色的字体写明</strong></span>本次修改了什么内容，以供审批人员进行查阅和确认。 &nbsp;<span class="label label-danger">重要</span>
					
				</shiro:user>
			</p>
		</div>
	</div>
    
</body>
</html>
