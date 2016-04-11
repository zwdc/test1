/**
 * Easyui KindEditor的扩展.
 * 需要在jquery.easyui.min.js和kindeditor-min.js之后导入.
 * 使用 1.class="easyui-kindeditor"
 * 		2.$("#kindeditor").kindeditor({....});
 **/
(function ($, K) {
	if (!K)
		throw "KindEditor未定义!";

	function create(target) {
		var opts = $.data(target, 'kindeditor').options;
		var editor = K.create(target, opts);
		$.data(target, 'kindeditor').options.editor = editor;
	}

	$.fn.kindeditor = function (options, param) {
		if (typeof options == 'string') {
			var method = $.fn.kindeditor.methods[options];
			if (method) {
				return method(this, param);
			}
		}
		options = options || {};
		return this.each(function () {
			var state = $.data(this, 'kindeditor');
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'kindeditor', {
						options : $.extend({}, $.fn.kindeditor.defaults, $.fn.kindeditor.parseOptions(this), options)
					});
			}
			create(this);
		});
	}

	$.fn.kindeditor.parseOptions = function (target) {
		return $.extend({}, $.parser.parseOptions(target, []));
	};

	$.fn.kindeditor.methods = {
		editor : function (jq) {
			return $.data(jq[0], 'kindeditor').options.editor;
		}
	};

	$.fn.kindeditor.defaults = {
		width : "100%",
		//minWidth : "500px",
		langType:"zh_CN",
		resizeType : 1,		//只能改变高度
		allowPreviewEmoticons : false,
		allowImageUpload : false,
		items : [
				'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist','file'],
		afterCreate: function() {
            this.sync();
        },
		afterChange:function() {
			this.sync();
		},
        afterBlur: function() {
            this.sync();
        }
	};
	$.parser.plugins.push("kindeditor");
})(jQuery, KindEditor);