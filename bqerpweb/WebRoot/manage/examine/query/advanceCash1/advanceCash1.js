// 物料盘点表打印
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
			// + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month;
			// + "-";
		}
		// if (Day >= 10) {
		// CurrentDate = CurrentDate + Day;
		// } else {
		// CurrentDate = CurrentDate + "0" + Day;
		// }
		return CurrentDate;
	}

	var temp = new Date();
	var date = ChangeDateToString(temp);

	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : date,
				id : 'monthDate',
				fieldLabel : "日期",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : false
								});
						this.blur();
					}
				}
			});

	// 打印盘点表按钮
	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'print',
				disabled : false,
				handler : printSupply
			});
	// 导出
	var btnExport = new Ext.Button({
				text : "导出",
				iconCls : 'export',
				disabled : false,
				handler : exportXLS

			})

	var headTbar = new Ext.Toolbar({
				region : 'north',
				items : ['日期：', monthDate, btnPrint, '-', btnExport]
			});

	function exportXLS() {
//		var month = monthDate.getValue().substring(0, 4) + "-"
//				+ monthDate.getValue().substring(5, 7);
		var month = monthDate.getValue();
		var url = "/powerrpt/frameset?__report=bqmis/deptcash1.rptdesign";
		url += "&__action=print&dateTime=" + month + "&__format=doc";
		window.open(url);
	}

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择日期！');
			return;
		}
		var strMonth = monthDate.getValue();
		var url = "/powerrpt/report/webfile/bqmis/deptcash1.jsp?dateTime="
				+ strMonth;
		document.all.iframe1.src = url;
	}
	var url = "/powerrpt/report/webfile/bqmis/deptcash1.jsp?dateTime=" + date;
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
					region : "north",
					layout : 'fit',
					height : 30,
					border : false,
					split : true,
					margins : '0 0 0 0',
					items : [headTbar]
				}, {
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ url
							+ '"  frameborder="0"  width="100%" height="100%"  />'
				}]
	});

})