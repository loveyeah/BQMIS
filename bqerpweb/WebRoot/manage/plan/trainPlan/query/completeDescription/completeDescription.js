Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	/**
	 * 月份Field
	 */
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
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();

	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : ChangeDateToString(startdate),
				id : 'monthDate',
				fieldLabel : "月份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});

	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'print',
				disabled : false,
				handler : printSupply
			});
	// 审批信息
	var btnapprove = new Ext.Button({
				text : "审批信息",
				iconCls : 'approve',
				disabled : false,
				handler : approveQuery
			});

	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['月份：', monthDate, btnPrint, '-', btnapprove, '-',
				new Ext.Button({
					text : '导出',
					iconCls : 'print',
					handler : function() {
						if (monthDate.getValue() == null
								|| monthDate.getValue() == "") {
							Ext.Msg.alert('提示信息', '请先选择月份！');
							return;
						}
						var strMonth = monthDate.getValue().substring(0, 4)
								+ monthDate.getValue().substring(5, 7);
						var url = "/powerrpt/frameset?__report=bqmis/bqplancompleteQuery.rptdesign";
						url += "&__action=print&yearmonth=" + strMonth+"&__format=doc";
						window.open(url);
					}
				})]
	});

	function approveQuery() {
		var para = monthDate.getValue();
		Ext.Ajax.request({
			url : 'manageplan/getCompleteDescription.action',
			method : 'post',
			params : {
				month : monthDate.getValue()
			},
			success : function(response, action) {
				var obj = Ext.util.JSON.decode(response.responseText);
				var url = '';
				if (obj) {

					if (obj.entryId == null || obj.entryId == 'null') {
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "bpTrainPlanBackGather";
						window.open(url);;
					} else {
						var url = "/power/workflow/manager/show/show.jsp?entryId="
								+ obj.entryId;
						window.open(url);
					}
				}

			},
			failure : function(response, action) {
			}
		})

	}

	/**
	 * 物料供应打印按钮按下时
	 */
	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue().substring(0, 4)
				+ monthDate.getValue().substring(5, 7);
		url = "/powerrpt/report/webfile/bqmis/bqplancompleteQuery.jsp?yearmonth="
				+ strMonth;
		document.all.iframe1.src = url;
	}
	var month = monthDate.getValue().substring(0, 4)
			+ monthDate.getValue().substring(5, 7);
	var url = "/powerrpt/report/webfile/bqmis/bqplancompleteQuery.jsp?yearmonth="
			+ month;
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