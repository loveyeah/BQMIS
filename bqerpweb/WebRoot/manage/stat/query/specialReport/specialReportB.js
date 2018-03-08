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
					reportType : '4'
				}
			});
			
	var monthsDate = new Ext.form.TextField({
				name : 'monthsDate',
				value : ChangeDateToString(new Date()),
				id : 'monthsDate',
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
			
		var montheDate = new Ext.form.TextField({
				name : 'montheDate',
				value : ChangeDateToString(new Date()),
				id : 'montheDate',
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
		if (monthsDate.getValue() == null || monthsDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		Ext.Ajax.request({
			method : 'post',
			url : 'manageplan/getPlanBackGetherWo.action',
			params : {
				planTime : monthsDate.getValue()
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
				+ monthsDate.getValue());
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
		items : ['报表名:', reportNameBox, '开始时间：', monthsDate,"结束时间",montheDate,btnPrint,
				new Ext.Button({
					text : '导出',
					iconCls : 'print',
					handler : function() {
					var startDate = monthsDate.getValue();
					var endDate = montheDate.getValue();
					var month = monthsDate.getValue().substring(0,7)+"-01";
					var url;
						if (reportCode == 34) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportBZ.rptdesign";
							url += "&__action=print&startDate="
				     	+ startDate+"&endDate="+endDate+"&month="+month+"&reportCode="+reportCode+"&__format=xls";
						}
						//modify by wpzhu
						if (reportCode == 35 ) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportBZ10.rptdesign";
							url += "&__action=print&startDate="
				     	  + startDate+"&endDate="+endDate+"&month="+month+"&reportCode="+reportCode+"&__format=xls";
						}
						window.open(url);
					}
				})
		]
	});

	function printSupply() {
		if (monthsDate.getValue() == null || monthsDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var startDate = monthsDate.getValue();
		var endDate = montheDate.getValue();
		if(monthsDate.getValue().substring(0,7)!= montheDate.getValue().substring(0,7)){
			  Ext.Msg.alert("提示","请选择同一月份");
			  return;
		}
		var month = monthsDate.getValue().substring(0,7)+"-01";
		var url;
		if (reportCode == 34) {
			url = "/powerrpt/report/webfile/bqmis/specialReportBZ.jsp?startDate="
					+ startDate+"&endDate="+endDate+"&month="+month+"&reportCode="+reportCode;
		}
		if (reportCode == 35 ) {
			url = "/powerrpt/report/webfile/bqmis/specialReportBZ10.jsp?startDate="
					+ startDate+"&endDate="+endDate+"&month="+month+"&reportCode="+reportCode;
		}
		document.all.iframe1.src = url;
	}
	var startDate = monthsDate.getValue();
	var endDate = montheDate.getValue();
    var month = monthsDate.getValue().substring(0,7)+"-01";
	var url  = "/powerrpt/report/webfile/bqmis/specialReportBZ.jsp?startDate="
					+ startDate+"&endDate="+endDate+"&month="+month+"&reportCode=34";
					
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