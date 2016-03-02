/**
 * 选择人员
 */

//----------------------------------------------------------
var bpmn_dialog;
//选择人或候选人
function chooseUser( multiSelect, key ){
	//弹出对话窗口
	bpmn_dialog = $('<div/>').dialog({
    	title : "设定人员",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/user/toChooseUser?multiSelect="+multiSelect+"&key="+key,
        onClose: function () {
        	//clearChoose(key);
        	bpmn_dialog.dialog('destroy');
        }
    });
}

//根据groupId显示人员列表的标签--choose_user.jsp
function addTab(title, groupId, key, multiSelect){
	if ($('#userTabs').tabs('exists', title)){
		$('#userTabs').tabs('select', title);
	} else {
		var url = ctx+"/user/toShowUser?groupId="+groupId+"&key="+key+"&multiSelect="+multiSelect;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		$('#userTabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}

//showUser.js调用，给父页面赋值
function setValueUserName(key, value){
	$("#"+key+"_name").textbox("setValue",value);
}

//取消选择--choose_user.jsp
function destroy_chooseUser(key){
	//clearChoose(key);
	bpmn_dialog.dialog('destroy');
}

//选择人时，同时也对父页面赋值了。所以，确认键就只关闭页面就好--choose_user.jsp
function set_chooseUser(){
	bpmn_dialog.dialog('destroy');
}

//"清除"按钮
function clearChoose(key){
	$("#"+key+"_id").val("");
	try {
    	$("#"+key+"_name").textbox("setValue","");
	} catch (e) {
		$("#"+key+"_name").val("");
	}
}