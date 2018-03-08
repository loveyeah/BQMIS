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
	if (month != "") {
		ChangeDateToString(startdate) = month;
	}
	var workFlowNoFact;
	var tecMainId;
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
	// -----------------------------------

	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'print',
				disabled : false,
				handler : printSupply
			});
	// 上报
	var reportBtn = new Ext.Button({
				text : '签字',
				id : 'btnReport',
				iconCls : 'write',
				handler : signRecord
			})

	function signRecord() {
		var url = "sign.jsp";
		var args = new Object();
		args.entryId = workFlowNoFact;
		args.tecMainId = tecMainId;
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
				items : ['月份：', monthDate, btnPrint, '-',
//				btnAdd, '-',
						reportBtn]
			});

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue().substring(0, 4) + "-"
				+ +monthDate.getValue().substring(5, 7);
		url = "/powerrpt/report/webfile/bqmis/fillTecItemFinish.jsp?month="
				+ strMonth;
		document.all.iframe1.src = url;
		init();
	}

	var month = monthDate.getValue().substring(0, 4) + "-"
			+ +monthDate.getValue().substring(5, 7);
	var url = "/powerrpt/report/webfile/bqmis/fillTecItemFinish.jsp?month=" + month;
	function init() {
		Ext.Ajax.request({
					url : 'manageitemplan/getTecDeptItemList.action',
					params : {
						init : 'init',
						status : 'finish',
						month : monthDate.getValue()
					},
					method : 'post',
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// 退回、未上报 上报可用
						if (result[10] == 1) {
							reportBtn.setDisabled(false)
							tecMainId = result[1];
							workFlowNoFact = result[11];
						} else {
							reportBtn.setDisabled(true)
						}

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