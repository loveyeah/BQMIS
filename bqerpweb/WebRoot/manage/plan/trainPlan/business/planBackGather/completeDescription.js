Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	var month = getParameter("month");
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
	// -----------------------------------

	var btnPrint = new Ext.Button({
		text : "查询",
		iconCls : 'print',
		disabled : false,
		handler : printSupply
	});
	// 修改
	var editBtu = new Ext.Button({
		text : '修改',
		id : 'btnUpdate',
		iconCls : 'update',
		handler : editFun
	})
	function editFun() {
		var month = monthDate.getValue();
		window.location
				.replace("../../business/planBackGather/planBackGather.jsp?month="
						+ month);

		// window.location.replace("../../business/planBackGather/planBackGather.jsp");
	}
	// 上报
	var reportBtn = new Ext.Button({
		text : '上报',
		id : 'btnReport',
		iconCls : 'upcommit',
		handler : reportFun
	})

	var returnBtn = new Ext.Button({
		text : '退回',
		id : 'btnReturn',
		iconCls : 'untread',
		handler : returnFun
	})

	function reportFun() {
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
						if (result.model.workflowStatus == 0
								|| result.model.workflowStatus == 3) {
							var url = "reportSign.jsp";
							approveId = result.model.approvalId;
							workFlowNo = result.model.workflowNo;
							var args = new Object();
							args.entryId = workFlowNo;
							args.approveId = approveId;
							args.planTime = monthDate.getValue();// add by ywliu 20100610
							args.flowCode = "bpTrainPlanBackGather";
							var obj = window
									.showModalDialog(url, args,
											'status:no;dialogWidth=770px;dialogHeight=550px');
						}
						// else if (result.model.workflowStatus == 1
						// || result.model.workflowStatus == 2) {
						// alert("该月份数据已上报!");
						// return false;
						// }
					} else {
						Ext.Ajax.request({
							method : 'post',
							url : 'manageplan/getTrainingSumIdForAdd.action',
							params : {
								planTime : monthDate.getValue()
							},
							success : function(action) {
								var results = eval("(" + action.responseText
										+ ")");
								if (results != null) {
									if (results.model != null) {
										var trainingMainId = results.model.trainingMainId;
										Ext.Ajax.request({
											url : 'manageplan/addSumApproveRecord.action',
											method : 'post',
											params : {
												trainingMainId : trainingMainId,
												month : monthDate.getValue()
											},
											success : function(result, request) {

												Ext.Ajax.request({
													method : 'post',
													url : 'manageplan/getPlanBackGetherWo.action',
													params : {
														planTime : monthDate
																.getValue()
													},
													success : function(action) {
														var result = eval("("
																+ action.responseText
																+ ")");
														var url = "reportSign.jsp";
														approveId = result.model.approvalId;
														workFlowNo = result.model.workflowNo;
														var args = new Object();
														args.entryId = workFlowNo;
														args.approveId = approveId;
														args.flowCode = "bpTrainPlanBackGather";
														var obj = window
																.showModalDialog(
																		url,
																		args,
																		'status:no;dialogWidth=770px;dialogHeight=550px');
													}
												})
											},
											failure : function(result, request) {
												Ext.MessageBox.alert('提示信息',
														'未知错误！')
											}
										})
									}
									// else{
									// alert("该月份没有可上报的数据！");
									// return false;
									// }
								}
							}
						})

					}
				}
			}
		});
	}

	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['月份：', monthDate, btnPrint, '-', editBtu, '-', reportBtn, "-",
				returnBtn]
	});

	function returnFun() {
		this.blur();
		var month = monthDate.getValue();
		var deptcodes = [];
		var mate = window.showModalDialog('reportSelectDep.jsp?month=' + month,
				window, 'dialogWidth=400px;dialogHeight=350px;status=no');
		if (typeof(mate) != "undefined") {
			Ext.Ajax.request({
				method : 'post',
				url : 'manageplan/backGatherReturnSelectMethod.action',
				params : {
					month : month,
					deptCode : mate.code
				},
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
//					alert(result.msg);
				}
			});
		} else {
			return;
		}
	}

	function init() {
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
//						alert(result.model.workflowStatus)
						if (result.model.workflowStatus == 1
								|| result.model.workflowStatus == 2) {
							Ext.getCmp("btnUpdate").setDisabled(true);
							Ext.getCmp("btnReport").setDisabled(true);
							Ext.getCmp("btnReturn").setDisabled(true);
						} else if (result.model.workflowStatus == 0
								|| result.model.workflowStatus == null) {
							Ext.getCmp("btnUpdate").setDisabled(false);
							Ext.getCmp("btnReport").setDisabled(false);
							Ext.getCmp("btnReturn").setDisabled(false);
						}
					}
					// else{
					// Ext.getCmp("btnReport").setDisabled(true);
					// Ext.getCmp("btnUpdate").setDisabled(true);
					// }
				}

			}
		});
	}
	
	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue().substring(0, 4)
				+ monthDate.getValue().substring(5, 7);
		url = "/powerrpt/report/webfile/bqmis/bqplancomplete.jsp?yearmonth="
				+ strMonth;
		document.all.iframe1.src = url;
		init();
	}

	// var month = monthDate.getValue().substring(0, 4)
	// + monthDate.getValue().substring(5, 7);
	var url = "/powerrpt/report/webfile/bqmis/bqplancomplete.jsp?yearmonth="
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
			html : '<iframe id="iframe1" name="iframe1"  src="' + url
					+ '"  frameborder="0"  width="100%" height="100%"  />'
		}]
	});

	printSupply();
})