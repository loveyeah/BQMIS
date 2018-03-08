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
			CurrentDate = CurrentDate + Month+'-';
		} else {
			CurrentDate = CurrentDate + "0" + Month+'-';
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	

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
				reportCode = records[0].data.reportCode;
				reportNameBox.setValue(reportCode)
			});

	ds.load({
				params : {
					reportType : '1'
				}
			});

	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : ChangeDateToString(new Date()),
				id : 'monthDate',
				fieldLabel : "月份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
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
						var strMonth = monthDate.getValue();
						var url;
						if (reportCode == 25) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportM.rptdesign";
							url += "&__action=print&date=" + strMonth
									+ "&reportCode="+reportCode+"&__format=xls";
						}
						//modify by wpzhu
						if (reportCode == 23 ) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportM100.rptdesign";
							url += "&__action=print&date=" + strMonth
									+ "&reportCode="+reportCode+"&__format=xls";
						}
						if ( reportCode == 24 ) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportM300.rptdesign";
							url += "&__action=print&date=" + strMonth
									+ "&reportCode="+reportCode+"&__format=xls";
						}
						window.open(url);
					}
				})
		]
	});

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue();
		var url;
		if (reportCode == 25) {
			url = "/powerrpt/report/webfile/bqmis/specialReportM.jsp?date="
					+ strMonth+"&reportCode="+reportCode;
		}
		if (reportCode == 23 ) {
			url = "/powerrpt/report/webfile/bqmis/specialReportM100.jsp?date="
					+ strMonth+"&reportCode="+reportCode;
		}
		if (reportCode == 24 ) {
			url = "/powerrpt/report/webfile/bqmis/specialReportM300.jsp?date="
					+ strMonth+"&reportCode="+reportCode;
		}
		document.all.iframe1.src = url;
	}

	var month = monthDate.getValue();

//	alert(month)
	var url = "/powerrpt/report/webfile/bqmis/specialReportM100.jsp?date="
			+ month+"&reportCode=23";
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