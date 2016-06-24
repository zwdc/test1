<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
var ap_datagrid;
var ap_form;
var ap_dialog;
var ap=${approvalProcess};
$(function() {
	//数据列表
	ap_datagrid = $('#ap_datagrid').datagrid({
        //url: ctx+"/feedback/getList",
        width : 'auto',
		height : 'auto',
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [
             [
              {field: 'CREATEDATE', title: '提交时间', width: fixWidth(0.2), align: 'center', halign: 'center', sortable: true,
            	  formatter:function(value,row){
		     			return moment(value).format("YYYY-MM-DD HH:mm:ss");
		     		}             
              },
			  {field: 'APPLYUSER', title: '提交人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'APPLYCONTENT', title: '提交内容', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'APPROVALUSER', title: '审批人', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true},
              {field: 'APPROVALCONTENT', title: '审批内容', width: fixWidth(0.1), align: 'center', halign: 'center', sortable: true}  
        ]
     ],
	toolbar: "#toolbar1"
    });
	 ap_datagrid.datagrid("loadData",ap);
});
</script>
<table id="ap_datagrid" title="审批流程列表"></table>