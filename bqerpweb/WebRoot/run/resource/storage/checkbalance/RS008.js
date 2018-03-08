Ext.onReady(function() {

	//	var fuzzy = new Ext.form.TextField({
	//		codeField : "yes"
	//			});
	//			

	var fuzzy = new Ext.form.TextField({
		maxLength : 30,
		readOnly : true,
		listeners : {
			focus : selectBookNo
		}
	});
	function selectBookNo() {
		this.blur();
		var mate = window.showModalDialog('../checkbook/bookNoSelect.jsp',
				window, 'dialogWidth=200px;dialogHeight=300px;status=no');
		if (typeof(mate) != "undefined") {
			fuzzy.setValue(mate.bookNo);
		}
	}

	// head工具栏
	var headTbar = new Ext.Toolbar({
		items : ['请输入盘点单号<font color="red">*</font>:', fuzzy, '-', {
			id : 'btnReflesh',
			text : "预览",
			handler : btn
		}]
	});

	// 主框架
	var viewport = new Ext.Viewport({
		items : [headTbar]
	});

	function btn() {
		if (null == fuzzy.getValue() || "" == fuzzy.getValue()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
					Constants.COM_E_002, "盘点单号"));
			return;
		} else {
			Ext.Ajax.request({
				method : Constants.POST,
				url : 'resource/checkCheckBalanceNo.action',
				success : function(result, request) {
					var f = eval('(' + result.responseText + ')');
					if (f == "1") {

						window
								.open(application_base_path+"report/webfile/checkBalance.jsp?bookNo="
										+ fuzzy.getValue() + "");
					} else if (f == "0") {
						// 盘点单号不存在
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.RS008_E_002, fuzzy.getValue()));
					}
				},
				failure : function() {
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_014);
				},
				params : {
					bookNo : fuzzy.getValue()
				}
			});
		}
	}

});