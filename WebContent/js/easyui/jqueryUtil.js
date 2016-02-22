(function ($)
{
    //全局系统对象
    window['jqueryUtil'] = {};
		//修改ajax默认设置
		$.ajaxSetup({
			type : 'POST',
		    complete: function(XMLHttpRequest, textStatus) {
		        if (XMLHttpRequest.status == "401") {
		            $.messager.alert('提示信息', "由于您长时间未操作，请重新登陆！", 'info',
		            function() {
		            	window.location.href= ctx+"/login"; 
		            });
		        } else if(XMLHttpRequest.status == "405") {
		        	//$.messager.alert('提示信息', "您没有权限操作!", 'info');
		        	alert(XMLHttpRequest.status);
		        } else if (textStatus == "error") {
		            $.messager.alert('提示信息', "请求超时！请稍后再试！", 'info');
		        }
		    },
		    error : function(XMLHttpRequest, textStatus, errorThrown) {
				$.messager.progress('close');
				if (XMLHttpRequest.status == "401") {
					$.messager.alert('提示信息', "登陆超时！请重新登陆！", 'info', function(){
						window.location.href= ctx+"/login";  
					});
				} else {
					$.messager.alert('错误', XMLHttpRequest.responseText);
				}
			}
		});
		
		
		var easyuiErrorFunction = function(XMLHttpRequest) {
			$.messager.progress('close');
			$.messager.alert('错误', XMLHttpRequest.responseText);
		};
		
		$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
		$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
		$.fn.form.defaults.onLoadError = easyuiErrorFunction;
		/**
		 * 取消easyui默认开启的parser
		 * 在页面加载之前，先开启一个进度条
		 * 然后在页面所有easyui组件渲染完毕后，关闭进度条
		 * @requires jQuery,EasyUI
		 */
		$.parser.auto = false;
		$(function() {
			$.messager.progress({
				text : '加载中....',
				interval : 100
			});
			$.parser.parse(window.document);
			window.setTimeout(function() {
				$.messager.progress('close');
				if (self != parent) {
					window.setTimeout(function() {
						try {
							parent.$.messager.progress('close');
						} catch (e) {
						}
					}, 500);
				}
			}, 1);
			$.parser.auto = true;
		});
		
		//IE检测
		jqueryUtil.isLessThanIe8 = function() {
			//leadingWhitespace在IE 6-8中返回false
			return !$.support.leadingWhitespace;
//			return ($.browser.msie && $.browser.version < 8);
		};
		
		//添加千位符
		jqueryUtil.formatNumber = function(money) {  
			if(/[^0-9\.]/.test(money)) return "invalid Number.";  
			var s = money; //获取小数型数据
		    s += "";
		    if (s.indexOf(".") == -1) s += ".0"; //如果没有小数点，在后面补个小数点和0
		    if (/\.\d$/.test(s)) s += "0"; 		 //正则判断
		    while (/\d{4}(\.|,)/.test(s)) 		 //符合条件则进行替换
		        s = s.replace(/(\d)(\d{3}(\.|,))/, "$1,$2"); //每隔3位添加一个
		    return s;
		}  

		//去掉千位符
		jqueryUtil.reFormat = function(money) {
			return money.replace(/,|\s/g, '');
		}
		
		//金钱转换为大写
		jqueryUtil.formatCapital = function(num){     
			  var strOutput = "";  
			  var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';  
			  num += "00";  
			  var intPos = num.indexOf('.');  
			  if (intPos >= 0)  
			    num = num.substring(0, intPos) + num.substr(intPos + 1, 2);  
			  strUnit = strUnit.substr(strUnit.length - num.length);  
			  for (var i=0; i < num.length; i++)  
			    strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i,1),1) + strUnit.substr(i,1);  
			  return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");    
		}  
		
		/**
		 * 使panel和datagrid在加载时提示
		 * @requires jQuery,EasyUI
		 */
		$.fn.panel.defaults.loadingMessage = '加载中....';
		$.fn.datagrid.defaults.loadMsg = '加载中....';
		
		
		/**
		 * @requires jQuery,EasyUI
		 * 防止panel/window/dialog组件超出浏览器边界
		 * @param left
		 * @param top
		 */
		var easyuiPanelOnMove = function(left, top) {
			debugger;
			var l = left;
			var t = top;
			if (l < 1) {
				l = 1;
			}
			if (t < 1) {
				t = 1;
			}
			var width = parseInt($(this).parent().css('width')) + 14;
			var height = parseInt($(this).parent().css('height')) + 14;
			var right = l + width;
			var buttom = t + height;
			var browserWidth = $(window).width();
			var browserHeight = $(window).height();
			if (right > browserWidth) {
				l = browserWidth - width;
			}
			if (buttom > browserHeight) {
				t = browserHeight - height;
				if(t <= 0) {
					t = 20;
				}
			}
			$(this).parent().css({/* 修正面板位置 */
				left : l,
				top : t
			});
		};
		
		/*$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
		$.fn.window.defaults.onMove = easyuiPanelOnMove;
		$.fn.panel.defaults.onMove = easyuiPanelOnMove;*/
		
		/**
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 为datagrid、treegrid增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
		 */
		var createGridHeaderContextMenu = function(e, field) {
			e.preventDefault();
			var grid = $(this);/* grid本身 */
			var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
			if (!headerContextMenu) {
				var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
				var fields = grid.datagrid('getColumnFields');
				for ( var i = 0; i < fields.length; i++) {
					var fildOption = grid.datagrid('getColumnOption', fields[i]);
					if (!fildOption.hidden) {
						$('<div iconCls="icon-ok" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
					} else {
						$('<div iconCls="icon-empty" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
					}
				}
				headerContextMenu = this.headerContextMenu = tmenu.menu({
					onClick : function(item) {
						var field = $(item.target).attr('field');
						if (item.iconCls == 'icon-ok') {
							grid.datagrid('hideColumn', field);
							$(this).menu('setIcon', {
								target : item.target,
								iconCls : 'icon-empty'
							});
						} else {
							grid.datagrid('showColumn', field);
							$(this).menu('setIcon', {
								target : item.target,
								iconCls : 'icon-ok'
							});
						}
					}
				});
			}
			headerContextMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		};
		$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
		$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
		
		
		/**
		 * @requires jQuery,EasyUI
		 * panel关闭时回收内存
		 */
		$.fn.panel.defaults.onBeforeDestroy = function() {
			var frame = $('iframe', this);
			try {
				if (frame.length > 0) {
					frame[0].contentWindow.document.write('');
					frame[0].contentWindow.close();
					frame.remove();
					if ($.browser.msie) {
						CollectGarbage();
					}
				}
			} catch (e) {
			}
		};
		/**
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展treegrid，使其支持平滑数据格式
		 */
		$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
			var opt = $(this).data().treegrid.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		
		/**
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展tree，使其支持平滑数据格式
		 */
		$.fn.tree.defaults.loadFilter = function(data, parent) {
			var opt = $(this).data().tree.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		/**
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展combotree，使其支持平滑数据格式
		 */
		$.fn.combotree.defaults.loadFilter = function(data, parent) {
			var opt = $(this).data().tree.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		//序列化表单到对象
		jqueryUtil.serializeObject = function(form) {
//			console.dir(form.serializeArray());
			var o = {};
			$.each(form.serializeArray(), function(index, field) {
//				console.dir(field.name + " : " + field.value + "  ");
				if (o[this['name']]) {
					o[this['name']] = o[this['name']] + "," + (this['value']==''?' ':this['value']);
				} else {
					o[this['name']] = this['value']==''?' ':this['value'];
				}
			});
//			console.dir(o);
			return o;
		};
		/*$.extend($.fn.datagrid.defaults.editors, {
	        combotree: {
	            init: function(container, options){
	                var editor = jQuery('<input type="text">').appendTo(container);
	                editor.combotree(options);
	                return editor;
	            },
	            destroy: function(target){
	                jQuery(target).combotree('destroy');
	            },
	            getValue: function(target){
	                var temp = jQuery(target).combotree('getValue');
	                var temp2 = jQuery(target).combotree('getText');
	                alert("getValue=>"+temp+"===>"+temp2);
	                return temp+","+temp2;
	            },
	            setValue: function(target, value){
	               //var temp = value.toString.split(",");
	                alert("setValue=11>"+value);
	               // jQuery(target).combotree('setValue', temp[0]);
	            },
	            resize: function(target, width){
	                jQuery(target).combotree('resize', width);
	            }
	        }
		});*/

		//自定义验证-扩展验证电话号
		$.extend($.fn.validatebox.defaults.rules, {
		  phoneRex: {
		    validator: function(value){
		    var rex=/^1[3-8]+\d{9}$/;
		    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
		    //电话号码：7-8位数字： \d{7,8
		    //分机号：一般都是3位数字： \d{3,}
		    //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
		    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    if(rex.test(value)||rex2.test(value))
		    {
		    	// alert('t'+value);
		    	return true;
		    }else
		    {
		    	//alert('false '+value);
		        return false;
		    }
		      
		    },
		    message: '请输入正确电话或手机格式'
		  }
		});
		
		
		/**
		 * 扩展树表格级联选择（点击checkbox才生效）：
		 * 		自定义两个属性：
		 * 		cascadeCheck ：普通级联（不包括未加载的子节点）
		 * 		deepCascadeCheck ：深度级联（包括未加载的子节点）
		 */
		$.extend($.fn.treegrid.defaults,{
			onLoadSuccess : function() {
				var target = $(this);
				var opts = $.data(this, "treegrid").options;
				var panel = $(this).datagrid("getPanel");
				var gridBody = panel.find("div.datagrid-body");
				var idField = opts.idField;//这里的idField其实就是API里方法的id参数
				gridBody.find("div.datagrid-cell-check input[type=checkbox]").unbind(".treegrid").click(function(e){
					if(opts.singleSelect) return;//单选不管
					if(opts.cascadeCheck||opts.deepCascadeCheck){
						var id=$(this).parent().parent().parent().attr("node-id");
						var status = false;
						if($(this).attr("checked")){
							target.treegrid('select',id);
							status = true;
						}else{
							target.treegrid('unselect',id);
						}
						//级联选择父节点
						selectParent(target,id,idField,status);
						selectChildren(target,id,idField,opts.deepCascadeCheck,status);
					}
					e.stopPropagation();//停止事件传播
				});
			}
		});
		
		/**
		 * 扩展树表格级联勾选方法：
		 * @param {Object} container
		 * @param {Object} options
		 * @return {TypeName} 
		 */
		$.extend($.fn.treegrid.methods,{
			/**
			 * 级联选择
		     * @param {Object} target
		     * @param {Object} param 
			 *		param包括两个参数:
		     *			id:勾选的节点ID
		     *			deepCascade:是否深度级联
		     * @return {TypeName} 
			 */
			cascadeCheck : function(target,param){
				var opts = $.data(target[0], "treegrid").options;
				if(opts.singleSelect)
					return;
				var idField = opts.idField;//这里的idField其实就是API里方法的id参数
				var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选
				var selectNodes = $(target).treegrid('getSelections');//获取当前选中项
				for(var i=0;i<selectNodes.length;i++){
					if(selectNodes[i][idField]==param.id)
						status = true;
				}
				//级联选择父节点
				selectParent(target,param.id,idField,status);
				selectChildren(target,param.id,idField,param.deepCascade,status);
			}
		});
		
		/**
		 * 级联选择父节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectParent(target,id,idField,status){
			var parent = target.treegrid('getParent',id);
			if(parent){
				var parentId = parent[idField];
				if(status)
					target.treegrid('select',parentId);
				else
					target.treegrid('unselect',id);
				selectParent(target,parentId,idField,status);
			}
		}
		/**
		 * 级联选择子节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} deepCascade 是否深度级联
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectChildren(target,id,idField,deepCascade,status){
			//深度级联时先展开节点
			if(status&&deepCascade)
				target.treegrid('expand',id);
			//根据ID获取下层孩子节点
			var children = target.treegrid('getChildren',id);
			for(var i=0;i<children.length;i++){
				var childId = children[i][idField];
				if(status)
					target.treegrid('select',childId);
				else
					target.treegrid('unselect',childId);
				selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点
			}
		}
	//cookies
	 jqueryUtil.cookies = (function ()
    {
        var fn = function ()
        {
        };
        fn.prototype.get = function (name)
        {
            var cookieValue = "";
            var search = name + "=";
            if (document.cookie.length > 0)
            {
                offset = document.cookie.indexOf(search);
                if (offset != -1)
                {
                    offset += search.length;
                    end = document.cookie.indexOf(";", offset);
                    if (end == -1) end = document.cookie.length;
                    cookieValue = decodeURIComponent(document.cookie.substring(offset, end))
                }
            }
            return cookieValue;
        };
        fn.prototype.set = function (cookieName, cookieValue, DayValue)
        {
            var expire = "";
            var day_value = 1;
            if (DayValue != null)
            {
                day_value = DayValue;
            }
            expire = new Date((new Date()).getTime() + day_value * 86400000);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + encodeURIComponent(cookieValue) + ";path=/" + expire;
        }
        fn.prototype.remvoe = function (cookieName)
        {
            var expire = "";
            expire = new Date((new Date()).getTime() - 1);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + escape("") + ";path=/" + expire;
            /*path=/*/
        };

        return new fn();
    })();
	//获取随机时间
	 jqueryUtil.getRandTime=function(){
		 	var nowDate=new Date();
		 	var str="";
			var hour=nowDate.getHours();//HH
			str+=((hour<10)?"0":"")+hour;
			var min=nowDate.getMinutes();//MM
			str+=((min<10)?"0":"")+min;
			var sec=nowDate.getSeconds(); //SS
			str+=((sec<10)?"0":"")+sec;
			return Number(str);
	 };
	//切换皮肤
	jqueryUtil.chgSkin=function(selectId,cookiesColor){
        	 docchgskin(document,selectId,cookiesColor);
            $("iframe").each(function (){   
                var dc=this.contentWindow.document;
                docchgskin(dc,selectId,cookiesColor);
            });
            function docchgskin(dc,selectId,cookiesColor){
						removejscssfile(dc,ctx+"/css/themes/"+cookiesColor+"/easyui.css", "css");
                        createLink(dc,ctx+"/css/themes/"+selectId+"/easyui.css");
        	}
            function createLink(dc,url){
		    	var urls = url.replace(/[,]\s*$/ig,"").split(",");
		    	var links = [];
		    	for( var i = 0; i < urls.length; i++ ){
			    links[i] = dc.createElement("link");
			    links[i].rel = "stylesheet";
			    links[i].href = urls[i];
			    dc.getElementsByTagName("head")[0].appendChild(links[i]);
		     	}
	    	}
            function removejscssfile(dc,filename, filetype){
	            var targetelement=(filetype=="js")? "script" : (filetype=="css")? "link" : "none"
	            var targetattr=(filetype=="js")? "src" : (filetype=="css")? "href" : "none"
	            var allsuspects=dc.getElementsByTagName(targetelement)
	            for (var i=allsuspects.length; i>=0; i--)
	            {
	                if (allsuspects[i] && allsuspects[i].getAttribute(targetattr)!=null && allsuspects[i].getAttribute(targetattr).indexOf(filename)!=-1)
	                allsuspects[i].parentNode.removeChild(allsuspects[i])
	            }
        	}  
        };
        /* 
    	 * 格式化字符串
    	 */
        $.formatString = function(str) {
        	for ( var i = 0; i < arguments.length - 1; i++) {
        		str = str.replace("{" + i + "}", arguments[i + 1]);
        	}
        	return str;
        };
        
        //高级查询
        jqueryUtil.gradeSearch=function($dg,formId,url) {
        	var search_dialog = $("<div/>").dialog({
				href : ctx+url,
				modal : true,
				title : '高级查询',
				top : 120,
				width : 600,
				cache: false,
				buttons : [ {
					text : '增加一行',
					iconCls : 'icon-add',
					handler : function() {
						var currObj = $(this).closest('.panel').find('table');
						var targetObj  = currObj.find('tr:last').clone().appendTo(currObj);
						targetObj.find('td:last button').show();
						targetObj.find('td span').remove();
						$.parser.parse(targetObj);
						targetObj.find('td:last span input[class=textbox-value]').attr("name","searchVals");
						
						/*var targetObj = $("<input name='projectDate' class='easyui-datebox' required='required'>").appendTo(currObj);
					    --解析目标为指定DOM的所有子孙元素，不包含这个DOM自身,所以要用targetObj.parent()
					    $.parser.parse(targetObj.parent());*/
						     
						/*console.dir(targetObj[0].cells);
						$.each(targetObj[0].cells, function(index, field) {
							debugger;
							
							//此处有个问题，当input的class为easyui-textbox或easyui-numberbox(可能不止这两个)时
							//$.parser.parse(field)渲染后，新增加的input没有name,导致后台不能取到值。
							//只能渲染完成后，手动给加入一个name属性了
							
							$(this).find('span').remove();
							$.parser.parse(field);//渲染
							
							console.dir($(this).find('span input[class=textbox-value]').val());
							var searchVals = $(this).find('span input[class=textbox-value]');
							$(this).find(searchVals).attr("name","searchVals");
							
							
						});*/
					}
				}, {
					text : '确定',
					iconCls : 'icon-ok',
					handler : function() {
						$.messager.progress({
			                title: '提示信息！',
			                text: '数据处理中，请稍后....'
			            });
						var isValid = $(formId).form('validate');
						if(isValid){
							$dg.datagrid('reload',jqueryUtil.serializeObject($(formId)));
							search_dialog.dialog('destroy');
							$.messager.progress('close');
						} else {
							$.messager.progress('close');
						}
					}
				}, {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function() {
						search_dialog.dialog('close')
					}
				} ]
			});
		};
		
		/**
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 创建一个模式化的dialog
		 * 
		 * @returns $.modalDialog.handler 这个handler代表弹出的dialog句柄
		 * 
		 * @returns $.modalDialog.xxx 这个xxx是可以自己定义名称，主要用在弹窗关闭时，刷新某些对象的操作，可以将xxx这个对象预定义好
		 */
		$.modalDialog = function(options) {
			if ($.modalDialog.handler == undefined) {// 避免重复弹出
				var opts = $.extend({
					title : '',
					width : 840,
					height : 680,
					modal : true,
					onClose : function() {
						$.modalDialog.handler = undefined;
						$(this).dialog('destroy');
					}
					/*onOpen : function() {
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
					}*/
				}, options);
				opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
				return $.modalDialog.handler = $('<div/>').dialog(opts);
			}
		};
		
	/* 
	 * 定义图标样式的数组
	 */
	$.iconData = [ {
		value : '',
		text : '默认'
	},{
		value : 'icon-adds',
		text : 'icon-adds'
	}
	,{
		value : 'icon-ok',
		text : 'icon-ok'
	},{
		value : 'icon-edit',
		text : 'icon-edit'
	} ,{
		value : 'icon-tip',
		text : 'icon-tip'
	},{
		value : 'icon-back',
		text : 'icon-back'
	},{
		value : 'icon-remove',
		text : 'icon-remove'
	},{
		value : 'icon-undo',
		text : 'icon-undo'
	},{
		value : 'icon-cancel',
		text : 'icon-cancel'
	}
	,{
		value : 'icon-save',
		text : 'icon-save'
	}
	,{
		value : 'icon-config',
		text : 'icon-config'
	},{
		value : 'icon-comp',
		text : 'icon-comp'
	},{
		value : 'icon-sys',
		text : 'icon-sys'
	},{
		value : 'icon-db',
		text : 'icon-db'
	},{
		value : 'icon-pro',
		text : 'icon-pro'
	},{
		value : 'icon-role',
		text : 'icon-role'
	},{
		value : 'icon-end',
		text : 'icon-end'
	},{
		value : 'icon-bug',
		text : 'icon-bug'
	},{
		value : 'icon-badd',
		text : 'icon-badd'
	},{
		value : 'icon-bedit',
		text : 'icon-bedit'
	},{
		value : 'icon-bdel',
		text : 'icon-bdel'
	},{
		value : 'icon-item',
		text : 'icon-item'
	},{
		value : 'icon-excel',
		text : 'icon-excel'
	},{
		value : 'icon-auto',
		text : 'icon-auto'
	},{
		value : 'icon-time',
		text : 'icon-time'
	}
	];
})(jQuery);