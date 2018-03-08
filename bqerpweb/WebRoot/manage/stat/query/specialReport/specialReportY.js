Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	/**
	 * 月份Field
	 */
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
	var month = getParameter("month");
	if (month != "") {
		exdate = month;
	}

	var reportCode;

	var rec = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCReportNewList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	var reportNameBox = new Ext.form.ComboBox({
				fieldLabel : '报表名称',
				store : ds,
				id : 'template',
				name : 'template',
				valueField : "reportCode",
				displayField : "reportName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				width : 250,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				resizable : true,
				listeners : {
					select : function(index, record) {
						reportCode = record.data.reportCode;
						reportNameBox.setValue(reportCode);
					}
				}
			});
	ds.on('load', function(e, records) {
				reportCode = records[1].data.reportCode;
				reportNameBox.setValue(reportCode)
			});

	ds.load({
				params : {
					reportType : '2'
				}
			});

	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : exdate,
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
	// -----------------------------------

	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'query',
				disabled : false,
				handler : printSupply
			});
	// 上报
	var reportBtn = new Ext.Button({
				text : '审批',
				id : 'btnReport',
				iconCls : 'write',
				handler : signRecord
			})

	function signRecord() {
		var workFlowNo;
		var approveId;
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		Ext.Ajax.request({
			method : 'post',
			url : 'manageplan/getPlanBackGetherWo.action',
			params : {
				planTime : monthDate.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result != null) {
					if (result.model != null) {
						if (result.model.workflowStatus == 1) {
							var url = "sign.jsp";
							approveId = result.model.approvalId;
							workFlowNo = result.model.workflowNo;
							var args = new Object();
							args.entryId = workFlowNo;
							args.approveId = approveId;
							var obj = window
									.showModalDialog(url, args,
											'status:no;dialogWidth=770px;dialogHeight=550px');
						} else if (result.model.workflowStatus == 2) {
							alert("该月份数据已审批!");
							return false;
						} else if (result.model.workflowStatus == 3) {
							alert("该月份数据已退回,请重新填写上报");
							return false;
						}
					} else {
						alert("该月份没有上报数据!");
						return false;
					}
				}
			}
		});
	}

	function addHandler() {
		window.location.replace("fillTecItemValue.jsp?month="
				+ monthDate.getValue());
	}

	var btnAdd = new Ext.Button({
				text : "填写",
				id : "btnAdd",
				iconCls : 'add',
				disabled : false,
				handler : addHandler
			});
	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['报表名:', reportNameBox, '月份：', monthDate, btnPrint,
				new Ext.Button({
					text : '导出',
					iconCls : 'print',
					handler : function() {
						var strMonth = monthDate.getValue().substring(0, 4)
								+ monthDate.getValue().substring(5, 7);
						var url;

						if (reportCode == 13 || reportCode == 18
								|| reportCode == 21) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 6 || reportCode == 7
								|| reportCode == 8) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA678.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 40) {
							url = "/powerrpt/preview?__report=bqmis/specialReportMore.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 26) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA26.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 9 || reportCode == 10) {
							url = "/powerrpt/preview?__report=bqmis/specialReportB.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 20 || reportCode == 15) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA10.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 22) {
							url = "/powerrpt/preview?__report=bqmis/specialReportTest.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 16 || reportCode == 27) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA1.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 12 || reportCode == 14) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA11.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						/*
						 * if(reportCode == 14){ url =
						 * "/powerrpt/preview?__report=bqmis/specialReportA14.rptdesign";
						 * url += "&__action=print&month=" + strMonth +
						 * "&reportCode=" + reportCode + "&__format=xls"; }
						 */
						if (reportCode == 19 || reportCode == 33) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA12.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 17) {
							url = "/powerrpt/preview?__report=bqmis/specialReportA300gl.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}

						window.open(url);
					}
				})
		// ,
		// '-', btnAdd,

		// '-',reportBtn
		]
	});

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue().substring(0, 4)
				+ monthDate.getValue().substring(5, 7);
		var url;
		if (reportCode == 13 || reportCode == 18 || reportCode == 21) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 6 || reportCode == 7 || reportCode == 8|| reportCode == 40) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA678.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 40) {
			url = "/powerrpt/report/webfile/bqmis/specialReportMore.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 26) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA26.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 9 || reportCode == 10) {
			url = "/powerrpt/report/webfile/bqmis/specialReportB.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 20 || reportCode == 15) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA10.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 22) {
			url = "/powerrpt/report/webfile/bqmis/specialReportTest.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 16 || reportCode == 27) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA1.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 12) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA11.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 14) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA14.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 33 || reportCode == 19) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA12.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 17) {
			url = "/powerrpt/report/webfile/bqmis/specialReportA300gl.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}

		document.all.iframe1.src = url;
	}

	var month = monthDate.getValue().substring(0, 4)
			+ monthDate.getValue().substring(5, 7);

	var url = "/powerrpt/report/webfile/bqmis/specialReportA11.jsp?month="
			+ month + "&reportCode=12";

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