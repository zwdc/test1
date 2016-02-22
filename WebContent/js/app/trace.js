/**
 * 流程跟踪
 * @param id
 */
function graphTrace(id) {
    // 获取图片资源
    var imageUrl = ctx + "/process/process-instance?pid=" + id + "&type=png";
    
	$.ajax({
		async: false,
		cache: false,
        type: "POST",
        url: ctx+"/process/trace/" + id,
        dataType: 'json',
        data: {},
        success: function (data) {
        	$.messager.progress("close");
        	if(data == null) {
        		 $.messager.alert("提示", "此流程已结束或不存在！", "info");
        	} else {
        		var positionHtml = "";
        		// 生成图片
        		var varsArray = new Array();
        		$.each(data, function(i, v) {
        			var $positionDiv = $('<div/>', {
        				'class': 'activity-attr'
        			}).css({
        				position: 'absolute',
        				left: (v.x + 5),
        				top: (v.y + 25),
        				width: (v.width +2),
        				height: (v.height + 5),
        				backgroundColor: '#00FF00',
        				opacity: 0,
        				zIndex: $.fn.qtip.zindex - 1
        			});
        			
        			// 跟踪节点边框
        			var $border = $('<div/>', {
        				'class': 'activity-attr-border'
        			}).css({
        				position: 'absolute',
        				left: (v.x + 5),
        				top: (v.y + 25),
        				width: (v.width + 2),
        				height: (v.height + 5),
        				zIndex: $.fn.qtip.zindex - 2
        			});
        			
        			if (v.currentActiviti) {
        				$border.addClass('ui-corner-all-12').css({
        					border: '3px solid red'
        				});
        			}
        			positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
        			varsArray[varsArray.length] = v.vars;
        		});
        		
        		if ($('#workflowTraceDialog').length == 0) {
        			$('<div/>', {
        				id: 'workflowTraceDialog',
        				html: "<div class='easyui-layout'><img src='" + imageUrl + "'style='left:0px; top:0px;' />" +
        				"<div id='processImageBorder'>" +
        				positionHtml +
        				"</div>" +
        				"</div>"
        			}).appendTo('body');
        		} else {
        			$('#workflowTraceDialog img').attr('src', imageUrl);
        			$('#workflowTraceDialog #processImageBorder').html(positionHtml);
        		}
        		
        		// 设置每个节点的data
        		$('#workflowTraceDialog .activity-attr').each(function(i, v) {
        			$(this).data('vars', varsArray[i]);
        		});
        		
        		$("<img/>").attr("src", imageUrl).load(function() {
        			//弹出对话窗口
        			var workflowTraceDialog = $('#workflowTraceDialog').dialog({
        				title : "查看流程跟踪",
        				top: 10,
        				width : this.width+20,
        				height : 'auto',
        				modal: true,
        				minimizable: true,
        				maximizable: true,
        				resizable: true,
        				buttons: [
        				          {
        				        	  text: '关闭',
        				        	  iconCls: 'icon-cancel',
        				        	  handler: function () {
        				        		  workflowTraceDialog.dialog('destroy');
        				        	  }
        				          }
        				          ],
        				          onClose: function () {
        				        	  workflowTraceDialog.dialog('destroy');
        				          }
        			});
        			// 此处用于显示每个节点的信息，如果不需要可以删除
        			$('.activity-attr').qtip({
        				content: {
        					title: '节点说明',
        					text: function() {
        						var vars = $(this).data('vars');
        						var tipContent = "";
        						$.each(vars, function(varKey, varValue) {
        							if (varValue) {
        								tipContent += varKey + "：" + varValue + "<br/><br/>";
        							}
        						});
        						return tipContent;
        					}
        				},
        				style: {
        					classes: 'qtip-dark qtip-rounded'
        				},
        				position: {
        					at: 'bottom left',
        					adjust: {
        						x: 60
        					}
        				}
        			});
        		});
        	}
            
        },
        beforeSend:function(){
	        $.messager.progress({
	            title: '提示信息！',
	            text: '数据处理中，请稍后....'
	        });
    	},
    	complete: function(){
    		$.messager.progress("close");
   		}
	});
    

}
