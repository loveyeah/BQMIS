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
				text : '签字',
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
		items : ['月份：', monthDate, btnPrint, new Ext.Button({
			text : '导出',
			iconCls : 'print',
			handler : function() {
				var strMonth = monthDate.getValue().substring(0, 4) + "-"
						+ +monthDate.getValue().substring(5, 7);
				var url = "/powerrpt/frameset?__report=bqmis/fillTecItemFinish.rptdesign";
				url += "&__action=print&month=" + strMonth + "&__format=xls";
				window.open(url);
			}
		})]
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
	var url = "/powerrpt/report/webfile/bqmis/fillTecItemFinish.jsp?month="
			+ month;
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
						if (result[11] == 1) {
							reportBtn.setDisabled(false)
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