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
	var month = getParameter("month");
	var fmonth;
	if (month != "") {
		fmonth = month;
	} else {
		fmonth = ChangeDateToString(startdate);
	}

	var workFlowNoFact;
	var tecMainId;
	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : fmonth,
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
				iconCls : 'print',
				disabled : false,
				handler : printSupply
			});
	// 上报
	var reportBtn = new Ext.Button({
				text : '上报',
				id : 'btnReport',
				iconCls : 'write',
				handler : signRecord
			})
	// 审批查询
	var btnMeetQuery = new Ext.Button({
				id : 'btnMeetQuery',
				text : " 审批查询",
				iconCls : 'view',
				handler : function() {
					var url;
					if (workFlowNoFact == null || workFlowNoFact == "") {
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "bpTecItemFact";
					} else {
						url = "/power/workflow/manager/show/show.jsp?entryId="
								+ workFlowNoFact;
					}
					window.open(url);
				}

			})
	function signRecord() {
		var url = "reportsign.jsp";
		var args = new Object();
		args.entryId = workFlowNoFact;
		args.tecMainId = tecMainId;
		args.workflowType = 'bpTecItemFact';
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		//
		if (obj)
			init();
	}

	function addHandler() {
		window.location.replace("fillTecItemFinishValue.jsp?month="
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
				items : ['月份：', monthDate, btnPrint, '-', btnAdd, '-',
						reportBtn, '-', btnMeetQuery]
			});

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		init();
		var strMonth = monthDate.getValue().substring(0, 4) + "-"
				+ +monthDate.getValue().substring(5, 7);
		url = "/powerrpt/report/webfile/bqmis/fillTecItemFinish.jsp?month="
				+ strMonth;
		document.all.iframe1.src = url;
	}

	var month = monthDate.getValue().substring(0, 4) + "-"
			+ +monthDate.getValue().substring(5, 7);
	var url = "/powerrpt/report/webfile/bqmis/fillTecItemFinish.jsp?month="
			+ month;
	function init() {
		Ext.Ajax.request({
					url : 'manageitemplan/getTecDeptItemList.action',
					params : {
						init : 'init',
						month : monthDate.getValue()
					},
					method : 'post',
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// 退回、未上报 上报可用
						if (result[10] == 0 || result[10] == 3) {
							reportBtn.setDisabled(false);
						} else {
							reportBtn.setDisabled(true);
						}
						if (result[10] != 1 && result[10] != 2) {
							btnAdd.setDisabled(false)
						} else {
							btnAdd.setDisabled(true)
						}
						tecMainId = result[1];
						workFlowNoFact = result[11];
					}
				})
	}
	init()
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