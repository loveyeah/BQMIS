Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var planId = null;
	// 定义状态数据
	var stateData = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/findPlanBusiStatusForApprove.action",
		fields : ['statusCode', 'statusName']
	});

	stateData.on('load', function(e, records) {
		stateComboBox.setValue(records[0].data.statusCode);
	});
	stateData.load();
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		fieldLabel : '状态',
		id : "stateCob",
		store : stateData,
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		anchor : '100%',
		triggerAction : 'all',
		readOnly : true
	});

	// -----add by fyyang 090618-----
	// 计划种类
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
		fieldLabel : '计划种类',

		// width : 108,
		anchor : '100%',
		displayField : 'text',
		valueField : 'id',
		// allowBlank : false,
		hiddenName : 'mr.planOriginalId',
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'resource/getPlanOriginalNode.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '合肥电厂',
				text : '合肥电厂'
			}),
			listeners : {
				click : setTxtMrCostSpecial
			}
		},
		selectNodeModel : 'leaf'
	});
	var txtMrOriginalH = new Ext.form.Hidden();

	function setTxtMrCostSpecial(node) {
		if (node.id == "6") {
			Ext.get("quarter").dom.checked = true;
			quarterPanel.show();
			timePanel.hide();
			monthPanel.hide();
		} else if (node.id == "4" || node.id == "5" || node.id == "13") {
			Ext.get("month").dom.checked = true;
			monthPanel.show();
			quarterPanel.hide();
			timePanel.hide();
		} else {
			Ext.get("time").dom.checked = true;
			timePanel.show();
			quarterPanel.hide();
			monthPanel.hide();
		}
	}
	// -------------------------------
	// ---------询价按钮 add by fyyang 100114-------------------
	var btnInquire = new Ext.Toolbar.Button({
		text : "查看询价信息",
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			if (materialGrid.selModel.hasSelection()) {

				var records = materialGrid.selModel.getSelections();
				var record = materialGrid.getSelectionModel().getSelected();

				var args = new Object();
				args.materialName = record.get("materialName");
				args.requirementDetailId = record.get("requirementDetailId");
				var object = window
						.showModalDialog(
								'../../query/priceQueryForSelect.jsp',
								args,
								'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
			} else {
				Ext.Msg.alert("提示", "请先选择物资!");
			}
		}
	});

	var txtAdviceItem = new Ext.form.TextField({
		name : 'txtAdviceItem',
		fieldLabel : "本年预算费用",
		readOnly : true,
		anchor : '100%'
	});

	var txtFactItem = new Ext.form.TextField({
		name : 'txtFactItem',
		fieldLabel : "本年已领费用",
		readOnly : true,
		anchor : '100%'
	});
	// ---------------------------------------------------------------------

	// ----add by sychen 20100419------------
	var txtCheckPrice = new Ext.form.TextField({
		name : 'txtCheckPrice',
		fieldLabel : "本年财务审核领料费用:",
		readOnly : true,
		width : 100
	});
	var txtCheckPriceRate = new Ext.form.TextField({
		name : 'txtCheckPriceRate',
		fieldLabel : "本年财务审核领用费用完成率",
		readOnly : true,
		width : 100
	});
	var txtFactItemRate = new Ext.form.TextField({
		name : 'txtFactItemRate',
		fieldLabel : "本年仓库发出费用完成率",
		readOnly : true,
		width : 100
	});
	// ---------------------------------------------

	/**
	 * 获取当前月的日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	/**
	 * 获取当前前一月的日期
	 */
	function getCurrentDateFrom() {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() - 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 获取当前后一月的日期
	 */
	function getCurrentDateTo() {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() + 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 去掉时间中T
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}

	/**
	 * 需求时间与当前时间比较
	 */
	function checkTime() {
		var startDate = txtMrDateFrom.getValue();
		var endDate = txtMrDateTo.getValue();
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

			}
		}
		return true;
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}

	/**
	 * 赋值主grid中物料类别
	 */
	function getClassName() {
		return txtMaterialClass.getValue();// modify by ywliu , 2009/7/7
	}

	/**
	 * 显示详细 add by liuyi 091111
	 */
	function showDetails() {
		if (headGrid.selModel.hasSelection()) {
			var record = headGrid.getSelectionModel().getSelected();
			planId = record.get('requirementHeadId');

			var start = record.get('mrDept').indexOf('/');
			var deptCode = record.get('mrDept').substring(start + 1,
					record.get('mrDept').length);
			txtAdviceItem.setValue(getBudgetName(record.data['itemCode'], 1,
					deptCode));
			txtFactItem.setValue(getBudgetName(record.data['itemCode'], 2,
					deptCode));
			// --------add by sychen 20100419---------------
			var data = getBudgetName(record.data['itemCode'], 3, deptCode);
			var mydata = data.split(",");
			txtCheckPrice.setValue(mydata[1]);
			var data1 = (parseFloat(mydata[1]) / parseFloat(txtAdviceItem
					.getValue()))
					* 100;
			var data2 = (parseFloat(mydata[0]) / parseFloat(txtAdviceItem
					.getValue()))
					* 100;
			txtCheckPriceRate
					.setValue(parseFloat(txtAdviceItem.getValue()) == 0
							? 0
							: numberFormat(data1));
			txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue()) == 0
					? 0
					: numberFormat(data2));
			// ----------------------------------------------------
			queryRecord();
		}
	}
	/**
	 * 当双击主Grid中的一行时
	 */
	function showEdit() {
		// 判断是否有选中
		if (headGrid.selModel.hasSelection()) {
			var record = headGrid.getSelectionModel().getSelected();
			planId = record.get('requirementHeadId');
			var status = record.get('mrStatus').substring(
					record.get('mrStatus').length - 1,
					record.get('mrStatus').length);
			var workFlowNo = record.get("wfNo");
			var planSource = record.get("planOriginalId");
			var start = record.get("mrDept").indexOf('/');
			var applyDept = record.get("mrDept").substring(start + 1,
					record.get("mrDept").length);
					
			var actionType = "";
			// ---------------
			Ext.Ajax.request({
				url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
				method : 'POST',
				params : {
					entryId : workFlowNo
				},
				success : function(result, request) {
					var url = "";
					var obj = eval("(" + result.responseText + ")");
					if (obj[0].url == null || obj[0].url == "") {
						// Ext.Msg.alert("提示","无地址");
						url = "../planSign/planSign.jsp";
					} else {
						url = "../../../../../" + obj[0].url;
					}
					var args = new Object();

					var title = "";
					if (status == "1") {
						title = "本部门领导审批";
						var conn = Ext.lib.Ajax.getConnectionObject().conn;
						conn.open("POST",
								'resource/getDeptTypeForPlanApprove.action?deptCode='
										+ applyDept, false);
						conn.send(null);
						if (conn.status == "200") {
							actionType = conn.responseText;
						}

					}

					// else if (status == "3") {
					// if (planSource == 4 || planSource == 10)
					// title = "检修安生部领导审批";
					// else if (planSource == 5 || planSource == 11)
					// title = "实业安生部领导审批";
					// else if (planSource == 3)
					// title = "安生部领导审批";
					// else if (planSource == 6 || planSource == 7) {
					// var mydept = applyDept.substring(0, 2);
					// if (mydept == "01")
					// title = "发电综合部领导审批";
					// if (mydept == "02")
					// title = "检修综合部领导审批";
					// if (mydept == "03")
					// title = "实业综合部领导审批";
					// }
					// } else if (status == "4" || status == "5")
					// title = "发电安生部领导审批";
					// else if (status == "6")
					// title = "生产副总审批";
					// else if (status == "8")
					// title = "分管发电副总审批";
					// else if (status == "A")
					// title = "分管实业副总审批";
					// else if (status == "B")
					// title = "分管检修副总审批";
					// else if (status == "F")
					// title = "商务部审批";
					// else if (status == "G")
					// title = "物管中心审批";
					// else if (status == "H")
					// title = "财务部审批";
					// else if (status == "I")
					// title = "监察审计科审批";
					// else if (status == "J")
					// title = "分管副总审批";
					// else if (status == "K")
					// title = "总经理审批";

					// args.title = title;
					args.actionType = actionType;
					args.busiNo = record.get("mrNo");
					args.entryId = workFlowNo;
					args.flowCode = "";
					args.busiStatus = status;
					args.id = record.get("requirementHeadId");
					args.itemCode = record.get("itemCode");
					args.itemName = getBudgetName(record.get("itemCode"));
					
					args.mrDept = record.get("mrDept").substring(start + 1,record.get("mrDept").length);//add by sychen 20100511
					args.flag="1";//add by sychen 20100511
				

					var obj = window
							.showModalDialog(
									url,
									args,
									'dialogWidth:750px;dialogHeight:500px;;directories:yes;help:no;status:no;resizable:yes;scrollbars:yes;');
					if (obj) {
						findFuzzy();
						if (status == "1") {
							queryRecord();
						}
					}
				},
				failure : function(result, request) {
					Ext.Msg.alert("提示", "错误");
				}

			});
			// ----------------

		} else {
			Ext.MessageBox.alert("提示", "请选择一条记录！")
		}

	}

	/**
	 * 弹出物料选择窗口
	 */
	// function selectMaterial() {
	// var mate = window.showModalDialog('../../RP001.jsp', window,
	// 'dialogWidth=800px;dialogHeight=550px;status=no');
	// if (typeof(mate) != "undefined") {
	// // 设置物料名
	// txtMaterialName.setValue(mate.materialName);
	// }
	// }
	/**
	 * 查询函数
	 */
	function findFuzzy(start) {

		if (start > 0) {
			start = start;
		} else {
			start = 0;
		}
		if (txtMrOriginal.getValue() == "6") {
			var quarterYear = "";
			var quarter = "";
			quarterYear = quarterDate.getValue();
			quarter = quarterBox.getValue();
			headStore.baseParams = {
				quarterYear : quarterYear,
				quarter : quarter,
				mrDept : txtMrDeptH.getValue(),
				mrBy : txtMrByH.getValue(),
				maName : txtMaterialName.getValue(),
				maClassName : txtMaterialClass.getValue(),// modify by ywliu ,
				// 2009/7/7
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue()
			};
			headStore.load({
				params : {
					start : start,
					limit : Constants.PAGE_SIZE
				}
			});

		} else if (txtMrOriginal.getValue() == "4"
				|| txtMrOriginal.getValue() == "5") {
			var monthPlan = "";
			monthPlan = monthDate.getValue();
			headStore.baseParams = {
				monthPlan : monthPlan,
				mrDept : txtMrDeptH.getValue(),
				mrBy : txtMrByH.getValue(),
				maName : txtMaterialName.getValue(),
				maClassName : txtMaterialClass.getValue(),// modify by ywliu ,
				// 2009/7/7
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue()
			};
			headStore.load({
				params : {
					start : start,
					limit : Constants.PAGE_SIZE
				}
			});

		} else {

			var startDate = timefromDate.getValue();
			var endDate = timetoDate.getValue();
			if (startDate != "" && endDate != "") {

				var res = compareDateStr(startDate, endDate);

				if (res) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
							Constants.COM_E_006, "计划时间（开始时间）", "结束（终止时间）"));

				} else {
					headStore.baseParams = {
						dateFrom : startDate,
						dateTo : endDate,
						mrDept : txtMrDeptH.getValue(),
						mrBy : txtMrByH.getValue(),
						maName : txtMaterialName.getValue(),
						maClassName : txtMaterialClass.getValue(),// modify by
						// ywliu ,
						// 2009/7/7
						status : stateComboBox.getValue(),
						mrOriginal : txtMrOriginal.getValue()
					};
					headStore.load({
						params : {
							start : start,
							limit : Constants.PAGE_SIZE
						}
					});

				}
			}
		}
		// if (startDate != "" && endDate != "") {
		// var res = compareDateStr(startDate, endDate);
		// if (res) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
		// Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));
		//
		// } else {
		// headStore.baseParams = {
		// dateFrom : txtMrDateFrom.getValue(),
		// dateTo : txtMrDateTo.getValue(),
		// mrDept : txtMrDeptH.getValue(),
		// mrBy : txtMrByH.getValue(),
		// maName : txtMaterialName.getValue(),
		// maClassName : txtMaterialClass.getRawValue(),
		// status:stateComboBox.getValue(),
		// mrOriginal : txtMrOriginal.getValue()
		// };
		// headStore.load({
		// params : {
		// start : start,
		// limit : Constants.PAGE_SIZE
		// }
		// });
		//			
		// }
		// } else {
		// headStore.baseParams = {
		// dateFrom : txtMrDateFrom.getValue(),
		// dateTo : txtMrDateTo.getValue(),
		// mrDept : txtMrDept.getValue(),
		// mrBy : txtMrByH.getValue(),
		// maName : txtMaterialName.getValue(),
		// maClassName : txtMaterialClass.getRawValue(),
		// status:stateComboBox.getValue()
		// };
		// headStore.load({
		// params : {
		// start : 0,
		// limit : Constants.PAGE_SIZE
		// }
		// });
		//		
		// }
	}
	/**
	 * 获取申请人的姓名和工号
	 */
	function getUserName() {
		if (txtMrBy.getValue() == "" && txtMrByH.getValue() == "") {
			Ext.Ajax.request({
				url : 'resource/getInfo.action',
				method : 'post',
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
					if (result != "" && result != null) {
						// txtMrBy.setValue(result.workerName); modify
						// by ywliu 09/07/15 出示时将申请人置空，查询所有需求计划单
						// txtMrByH.setValue(result.workerCode);
					}
					firstLoad();
				}
			});
		}
	}
	/**
	 * 人员选择画面处理
	 */
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			txtMrBy.setValue(person.workerName);
			txtMrByH.setValue(person.workerCode);
		}
	}

	function autoFind() {
		findFuzzy(pageBar.cursor);
	}

	// -------工具栏按钮-----开始

	// --------工具栏按钮-----结束
	// --------第一行textbox--开始
	// 计划月份
	var txtMrDateFrom = new Ext.form.TextField({
		fieldLabel : '计划月份',
		readOnly : true,
		width : 108,
		id : "mrDateFrom",
		name : "mrDateFrom",
		anchor : '100%',
		style : 'cursor:pointer',
		value : getCurrentDateFrom(),
		listeners : {
			focus : function() {
				WdatePicker({
					// 时间格式
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : false
				});

			}

		}

	});
	function choseDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtMrDept.setValue(dept.names);
			txtMrDeptH.setValue(dept.codes);
		}
	}
	// 部门
	var txtMrDept = new Ext.form.TriggerField({
		fieldLabel : '部门',
		width : 108,
		id : "mrDeptId",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'mrDept',
		blankText : '请选择',
		emptyText : '请选择',
		maxLength : 100,
		anchor : '100%',
		readOnly : true
	});
	txtMrDept.onTriggerClick = choseDept;
	var txtMrDeptH = new Ext.form.Hidden({
		hiddenName : 'mrDept'
	})

	// 申请人
	var txtMrBy = new Ext.form.TextField({
		fieldLabel : '申请人员',
		readOnly : true,
		maxLength : 100,
		width : 108,
		id : "mrBy",
		name : "mrBy",
		anchor : '100%'
	});
	txtMrBy.onClick(selectPersonWin);
	var txtMrByH = new Ext.form.Hidden();
	txtMrByH.on('render', getUserName)

	// 物料类别
	var txtMaterialClass = new Ext.ux.ComboBoxTree({
		fieldLabel : '物料类别',
		allowBlank : true,
		width : 108,
		id : "materialClassId",
		anchor : '100%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'materialClass',
		blankText : '请选择',
		emptyText : '请选择',
		// readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'resource/getMaterialClassList.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				name : '合肥电厂',
				text : '合肥电厂'
			})
		},
		selectNodeModel : 'all'
	});

	var btnView = new Ext.Toolbar.Button({
		text : "流程",
		// iconCls :'view',
		handler : function() {

			if (headGrid.selModel.hasSelection()) {
				var record = headGrid.getSelectionModel().getSelected();
				var entryId = record.get("wfNo");
				var planSource = record.get("planOriginalId");

				url = "/power/"
						+ "workflow/manager/show/show.jsp?entryId=" + entryId;

				window.open(url);
			} else {
				Ext.MessageBox.alert("提示", "请选择要查看的记录");
			}

		}
	});

	var firstLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMrOriginal]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMaterialClass]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMrBy, txtMrByH]
		}, {
			columnWidth : 0.04,
			border : false
		}, {
			width : 50,
			border : false,
			items : [{
				width : 50,
				layout : "form",
				border : false,
				items : [btnView]
			}]

		}]
	});
	// --------第一行textbox--结束
	// --------第二行textbox--开始
	// 计划日期至
	var txtMrDateTo = new Ext.form.TextField({
		format : 'Y-m',
		fieldLabel : "至",
		itemCls : 'sex-left',
		readOnly : true,
		clearCls : 'allow-float',
		checked : true,
		width : 108,
		value : getCurrentDateTo(),
		id : "mrDateTo",
		name : "mrDateTo",
		anchor : '100%',
		style : 'cursor:pointer',
		listeners : {
			focus : function() {
				WdatePicker({
					// 时间格式
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : false
				});

			}

		}

	});
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		readOnly : false,
		width : 108,
		id : "materialName",
		anchor : '100%'
	});
	// txtMaterialName.onClick(selectMaterial);

	// -----------add by
	// fyyang-----090618---------------------------------------------
	// -----------最上面行开始-----------------
	// ///////////////////////////////////////////////////////////////////////
	// 时间段Field
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.YEAR, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var timeRadio = new Ext.form.Radio({
		id : 'time',
		name : 'queryWayRadio',
		boxLabel : '按时间段',
		hideLabel : true,
		checked : true,
		disabled : true
	});

	var quarterRadio = new Ext.form.Radio({
		id : 'quarter',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : ' 按季度',
		disabled : true
	});

	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '按月份',
		disabled : true
	});

	var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});

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
	var monthDate = new Ext.form.TextField({
		name : '_monthDate',
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
					alwaysUseStartDate : true
				});
			}
		}
	});

	var quarterDate = new Ext.form.TextField({
		id : 'quarterDate',
		name : '_quarterDate',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : new Date().getFullYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var quarterBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'quarterBox',
		name : 'quarterBoxName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'quarterBoxName',
		editable : false,
		triggerAction : 'all',
		width : 75,
		selectOnFocus : true,
		value : 1
	});

	var timePanel = new Ext.Panel({
		id : 'timeAreaPanel',
		border : false,
		layout : 'form',
		labelWidth : 32,
		items : [{
			layout : 'column',
			bodyStyle : "padding:0 0 0 38",
			border : false,
			items : [{
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [timefromDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.5,
				items : [timetoDate]
			}]

		}]
	});

	/**
	 * 季度Panel
	 */
	var quarterPanel = new Ext.Panel({
		id : 'quarterPanel',
		labelWidth : 32,
		layout : 'form',
		border : false,
		// hidden : true,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [quarterDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.5,
				items : [quarterBox]
			}]

		}]
	});
	/**
	 * 月份Panel
	 */
	var monthPanel = new Ext.Panel({
		id : 'monthPanel',
		labelWidth : 32,
		border : false,
		hidden : true,
		layout : 'form',
		items : [monthDate]
	});

	var queryTypeRadioPanel = new Ext.Panel({
		achor : '100%',
		border : false,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.4,
				align : 'center',
				layout : 'form',
				items : [timeRadio]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.3,
				items : [quarterRadio]
			}, {
				border : false,
				columnWidth : 0.3,
				align : 'center',
				layout : 'form',
				items : [monthRadio]
			}]
		}]
	});

	var Query = new Ext.Panel({
		border : false,
		height : 30,
		anchor : '100%',
		layout : 'column',
		items : [{
			id : 'query',
			border : false,
			columnWidth : 0.55,
			anchor : '100%',
			items : [timePanel, quarterPanel, monthPanel]
		}, {
			border : false,
			columnWidth : 0.45,
			anchor : '100%',
			items : [queryTypeRadioPanel]
		}]
	});

	var fiveLine = new Ext.Panel({
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		layout : "fit",
		anchor : '100%',
		// autoHeight : true,
		height : 30,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 1,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [Query]
			}]
		}]
	});
	// -----------结束--------------------------

	// var fourLine = new Ext.Panel({
	// border : false,
	// height : 30,
	// layout : "column",
	// style :
	// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
	// anchor : '100%',
	// items : [{
	// columnWidth : 0.32,
	// layout : "form",
	// border : false,
	// items : [stateComboBox]
	// },{
	// columnWidth : 0.32,
	// layout : "form",
	// border : false,
	// items : [txtMrOriginal]
	// },{
	// columnWidth : 0.32,
	// layout : "form",
	// border : false,
	// items : [txtMrOriginalH]
	// }, {
	// columnWidth : 0.04,
	// border : false
	// }, {
	// width : 125,
	// border : false
	// }]
	// });
	// ---------------add end
	// --------------------------------------------------------------------
	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
		text : "查询",
		handler : findFuzzy
	});

	var secondLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMaterialName]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMrDept]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [stateComboBox]
		}, {
			columnWidth : 0.04,
			border : false
		}, {
			width : 50,
			layout : "column",
			border : false,
			items : [{
				width : 50,
				layout : "form",
				border : false,
				items : [btnFind]
			}]
		}]
	});
	// --------第二行textbox--结束
	// 主表一览行数据定义
	var headInfo = Ext.data.Record.create([{
		// ID
		name : 'requirementHeadId'
	}, {	// 工作流状态
		name : 'mrStatus'
	}, {	// 申请单编号
		name : 'mrNo'
	}, {	// 申请部门
		name : 'mrDept'
	}, {	// 申请人
		name : 'mrBy'
	}, {	// 申请理由
		name : 'mrReason'
	}, {	// 申请日期
		name : 'mrDate'
	}, {	// 物料类别
		name : 'materialClass'
	}, {	// 费用来源
		name : 'itemCode'
	}, {
		name : 'wfNo'
	}, {
		name : 'planOriginalId'
	},{
	   name:'prjNo'
	}
	])
	// --------第三行grid--开始
	// 主表一览的store
	var headStore = new Ext.data.JsonStore({
		url : 'resource/planApproveQuery.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : headInfo
	});
	// 物料grid
	var pageBar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : headStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
	});
	var headGrid = new Ext.grid.GridPanel({
		// region : "center",
		// layout : 'fit',
		// height : 200,
		renderTo : "mygrid",// add by sychen 20100419
		autoSizeColumns : true,
		border : false,
		enableColumnMove : false,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		store : headStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// ID
				{
					header : "ID",
					width : 60,
					sortable : true,
					hidden : true,
					dataIndex : 'requirementHeadId'
				},
				// 状态
				{
					header : "状态",
					width : 120,
					sortable : true,
					renderer : function(val) {

						if (val != null && val != "") {
							return val.substring(0, val.length - 1);
						}
					},
					dataIndex : 'mrStatus'
				},
				// 申请编号
				{
					header : "申请编号",
					dataIndex : "mrNo",
					sortable : true,
					width : 80
				},
				// 申请单位
				{
					header : "申请单位",
					width : 120,
					sortable : true,
					dataIndex : 'mrDept',
					renderer : function(val) {
						if (val != null && val != "") {
							var start = val.indexOf('/');
							return val.substring(0, start);
						}
					}
				},
				// 申请人
				{
					header : "申请人",
					width : 100,
					sortable : true,
					dataIndex : 'mrBy'
				},
				// 申请理由
				{
					header : "申请理由",
					width : 300,
					sortable : true,
					dataIndex : 'mrReason',
					renderer : function(value, metadata, record) {
						metadata.attr = 'style="white-space:normal;"';
						return value;
					}
				},
				// 申请日期
				{
					header : "申请日期",
					width : 100,
					sortable : true,
					renderer : renderDate,
					dataIndex : 'mrDate'
				}, {
					header : "计划时间",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex) {
						return getPlanDateInfoByDate(record
								.get("planOriginalId"), value);
					},
					dataIndex : 'mrDate'
				}, {
					header : "计划种类",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex) {
						return planOriginalName(record.get("planOriginalId"),
								value);
					},
					dataIndex : 'planOriginalId'
				},
				// 增加“计划时间”,“计划种类”列 ywliu 090720
				// 物料类别
				{
					header : "物料类别",
					width : 100,
					sortable : true,
					renderer : getClassName,
					dataIndex : 'materialClass',
					hidden : true
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
					renderer : function(val) {
						if (val == "" || val == null)// modify by wpzhu
						// 100408
						{
							return "";
						}

						if (val == "zzfy") {
							return "生产类费用";
						} else if (val == "sclfy") {
							return "生产类费用";
						} else {
							return getBudgetName(val);
						}
					},
					dataIndex : 'itemCode'
				}],
		// 分页
		bbar : pageBar
	});

	headGrid.on('rowdblclick', showEdit);
	headGrid.on('rowclick', showDetails);

	
	 headGrid.on("rowcontextmenu", function(g, i, e) { 
				e.stopEvent();
				var record = headGrid.getStore().getAt(i);
				// 右键菜单
				var menu = new Ext.menu.Menu({
					id : 'mainMenu',
					items : [new Ext.menu.Item({
						text : '查看项目详细信息',
						iconCls : 'view',
						handler : function() { 
							
							if(record.get("prjNo")==""||record.get("prjNo")==null)
							{
								Ext.Msg.alert("提示","无项目!");
								return;
							}
							else
							{
							  var url = "../../../../../manage/project/query/lookProjectForResource.jsp";
										
							  var obj=new Object();
							  obj.prjNo=record.get("prjNo")
								window.showModalDialog(
												url,
												obj,
												"dialogWidth:650px;dialogHeight:550px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
							}
						}
					})]
				});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			});
	
	
	/**
	 * 初始化数据load
	 */
	function firstLoad() {
		headStore.baseParams = {
			dateFrom : timefromDate.getValue(),// txtMrDateFrom.getValue(),
			dateTo : timetoDate.getValue(),// txtMrDateTo.getValue(),
			mrDept : txtMrDeptH.getValue(),
			mrBy : txtMrByH.getValue(),
			maName : txtMaterialName.getValue(),
			maClassName : txtMaterialClass.getValue(),// modify by ywliu ,
			// 2009/7/7
			status : stateComboBox.getValue()
		};
		headStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	var thirdLine = new Ext.Panel({
		border : false,
		layout : "column",
		anchor : '100%',
		items : [{
			columnWidth : 1,
			layout : "fit",
			border : false,
			autoScroll : false,
			items : [headGrid]
		}]
	});
	// --------第三行grid--结束

	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		width : 180,
		readOnly : true
	});
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息查看窗口',
		buttons : [{
			text : "返回",
			handler : function() {
				win.hide();
			}
		}]
	});

	// 表单panel
	var formPanel = new Ext.FormPanel({
		region : 'north',
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		border : false,
		height : 90,
		items : [fiveLine, firstLine, secondLine]
	});
	var queryPanel = new Ext.Panel({
		frame : false,
		border : false,
		region : "center",
		layout : 'fit',
		items : [headGrid]
	});
	// liuyi
	var northPanel = new Ext.Panel({
		region : 'north',
		frame : false,
		border : false,
		layout : 'border',
		height : 400,
		items : [formPanel, queryPanel]
	})
	// var layout = new Ext.Viewport({
	// layout : 'border',
	// margins : '0 0 0 0',
	// border : false,
	// items : [formPanel, queryPanel]
	// });

	// add by liuyi 091111 开始

	// Ext.override(Ext.grid.GridView, {
	// ensureVisible : function(row, col, hscroll) {
	// if (typeof row != "number") {
	// row = row.rowIndex;
	// }
	// if (!this.ds) {
	// return;
	// }
	// if (row < 0 || row >= this.ds.getCount()) {
	// return;
	// }
	// col = (col !== undefined ? col : 0);
	// var rowEl = this.getRow(row), cellEl;
	// if (!(hscroll === false && col === 0)) {
	// while (this.cm.isHidden(col)) {
	// col++;
	// }
	// cellEl = this.getCell(row, col);
	// }
	// if (!rowEl) {
	// return;
	// }
	// var c = this.scroller.dom;
	// var ctop = 0;
	// var p = rowEl, stop = this.el.dom;
	// while (p && p != stop) {
	// ctop += p.offsetTop;
	// p = p.offsetParent;
	// }
	// ctop -= this.mainHd.dom.offsetHeight;
	// var cbot = ctop + rowEl.offsetHeight;
	// var ch = c.clientHeight;
	// var stop = parseInt(c.scrollTop, 10);
	// var sbot = stop + ch;
	// if (ctop < stop) {
	// c.scrollTop = ctop;
	// } else if (cbot > sbot) {
	// c.scrollTop = cbot - ch;
	// }
	// if (hscroll !== false) {
	// var cleft = parseInt(cellEl.offsetLeft, 10);
	// var cright = cleft + cellEl.offsetWidth;
	// var sleft = parseInt(c.scrollLeft, 10);
	// var sright = sleft + c.clientWidth;
	// if (cleft < sleft) {
	// c.scrollLeft = cleft;
	// } else if (cright > sright) {
	// c.scrollLeft = cright - c.clientWidth;
	// }
	// }
	// return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
	// Ext.fly(rowEl).getY()];
	// },
	// doRender : function(cs, rs, ds, startRow, colCount, stripe) {
	// var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
	// - 1;
	// var tstyle = 'width:' + this.getTotalWidth() + ';';
	// // buffers
	// var buf = [], cb, c, p = {}, rp = {
	// tstyle : tstyle
	// }, r;
	// for (var j = 0, len = rs.length; j < len; j++) {
	// r = rs[j];
	// cb = [];
	// var rowIndex = (j + startRow);
	// for (var i = 0; i < colCount; i++) {
	// c = cs[i];
	// p.id = c.id;
	// p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
	// ? 'x-grid3-cell-last '
	// : '');
	// p.attr = p.cellAttr = "";
	// p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
	// // 判断是否是最后行
	// if (r.data["isNewRecord"] == "total") {
	// // 替换掉其中的背景颜色
	// p.style = c.style
	// .replace(/background\s*:\s*[^;]*;/, '');
	// } else {
	// // 引用原样式
	// p.style = c.style;
	// };
	// if (p.value == undefined || p.value === "")
	// p.value = "&#160;";
	// if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
	// p.css += ' x-grid3-dirty-cell';
	// }
	// cb[cb.length] = ct.apply(p);
	// }
	// var alt = [];
	// if (stripe && ((rowIndex + 1) % 2 == 0)) {
	// alt[0] = "x-grid3-row-alt";
	// }
	// if (r.dirty) {
	// alt[1] = " x-grid3-dirty-row";
	// }
	// rp.cols = colCount;
	// if (this.getRowClass) {
	// alt[2] = this.getRowClass(r, rowIndex, rp, ds);
	// }
	// rp.alt = alt.join(" ");
	// rp.cells = cb.join("");
	// buf[buf.length] = rt.apply(rp);
	// }
	// return buf.join("");
	// }
	// });
	// 09/07/21 ywliu
	Ext.QuickTips.init();
	/**
	 * 金钱格式化
	 */

	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}

	// 物料详细记录
	var material = Ext.data.Record.create([{
		name : 'requirementDetailId'
	}, {
		name : 'materialNo'
	}, {
		name : 'materialName'
	}, {
		name : 'materSize'
	}, {
		name : 'appliedQty'
	}, {
		name : 'approvedQty'
	}, {
		name : 'issQty'
	}, {
		name : 'estimatedPrice'
	}, {
		name : 'estimatedSum'
	}, {
		name : 'purQty'
	}, {
		name : 'stockUmName'
	}, {
		name : 'usage'
	}, {
		name : 'memo'
	}, {
		name : 'needDate'
	}, {
		name : 'parameter'
	}, {
		name : 'docNo'
	}, {
		name : 'whsName'
	}, {
		name : 'qualityClass'
	}, {
		name : 'left'
	}, {
		name : 'tempNum'
	}, {
		name : 'itemId'
	}, {
		name : 'modifyMemo'
	}]);

	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
		url : 'resource/getMaterialDetail.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});

	var editorApprovedQty = new Ext.form.TextField({
		fieldLabel : '设备名称内容<font color="red">*</font>',
		id : "approvedQty",
		name : "approvedQty",
		allowBlank : false,
		readOnly : false,
		decimalPrecision : 4
	});

	// 物料grid
	var materialGrid = new Ext.grid.EditorGridPanel({
		// layout : 'fit',
		// height : 200,
		// anchor : "100%",

		region : "center",
		border : false,
		autoScroll : true,
		enableColumnMove : false,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		// 单击修改
		store : materialStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 项次号
				{
					header : "项次号",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'requirementDetailId'
				},
				// 物料编码
				{
					header : "物料编码",
					width : 100,
					sortable : true,
					dataIndex : 'materialNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},// 物料名称
				{
					header : "物料名称",
					width : 100,
					sortable : true,
					dataIndex : 'materialName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},// 物料规格
				{
					header : "物料规格",
					width : 100,
					sortable : true,
					dataIndex : 'materSize',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},// 申请数量
				{
					header : "申请数量",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'appliedQty', totalSum);
							}

							// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来

							var appQty = record.get('approvedQty');
							if (appQty != value) {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'appliedQty'
				},// 核准数量
				{
					header : "核准数量",
					width : 100,
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'approvedQty', totalSum);
							}
							// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来

							var appliedQty = record.get('appliedQty');
							if (appliedQty != value) {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							} else {
								return moneyFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedQty')
										- 0;
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'approvedQty',
					editor : editorApprovedQty
				// renderer:function(value){
				// alert(value);
				// return "<span style='color:blue;'>"+value+"</span>";
				//
				// }
				},// 已领数量
				{
					header : "已领数量",
					width : 100,
					sortable : true,
					hidden : true,
					renderer : moneyFormat,
					dataIndex : 'issQty',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 单位
				{
					header : "单位",
					width : 100,
					sortable : true,
					dataIndex : 'stockUmName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 需求日期
				{
					header : "需求日期",
					width : 100,
					sortable : true,
					dataIndex : 'needDate',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 估计单价
				{
					header : "估计单价",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'estimatedPrice', "");
							}
							// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					},
					dataIndex : 'estimatedPrice'
				},// 估计金额
				{
					header : "估计金额",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'estimatedSum', totalSum);
							}
							// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return moneyFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum')
										- 0;
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'estimatedSum'
				},
				// 采购数量
				{
					header : "采购数量",
					width : 100,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'purQty',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},

				// 物料材质
				{
					header : "物料材质",
					width : 100,
					sortable : true,
					dataIndex : 'parameter',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 物料图号
				{
					header : "物料图号",
					width : 100,
					sortable : true,
					dataIndex : 'docNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					sortable : true,
					dataIndex : 'whsName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},// 质量等级
				{
					header : "质量等级",
					width : 100,
					sortable : true,
					dataIndex : 'qualityClass',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 当前库存
				{
					header : "当前库存",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('left',
										"");
							}
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return numberFormat(value);
							}
						}
					},
					dataIndex : 'left'
				},
				// 暂收数量
				{
					header : "暂收数量",
					width : 100,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'tempNum',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
					hidden : true,
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return numberFormat(v);
						}
					},
					dataIndex : 'itemId'
				},
				// 用途
				{
					header : "用途",
					width : 100,
					sortable : true,
					dataIndex : 'usage',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				}, {
					header : "核准信息",
					width : 100,
					sortable : true,
					dataIndex : 'modifyMemo',
					renderer : function(value, metadata, record) {
						// metadata.attr = 'style="white-space:normal;"';
						// return value;
						if (value != null)
							return "<span title='" + value + "' >" + value
									+ "</span>";
					}
				},
				// 备注
				{
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'memo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							} else
								return v;
						}
					}
				}],
		tbar : ['本年财务审核领料费用:', txtCheckPrice, '-', '本年财务审核领用费用完成率(%):',
				txtCheckPriceRate, '-', '本年仓库发出费用完成率(%):', txtFactItemRate],

		// tbar : [
		// '请确认核准数量',
		// "<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>",
		// '颜色说明：',
		// "<div>&nbsp;&nbsp;</div>",
		// "<div style='width:40; color:white;
		// background:green'>申请数量与审核数量不等</div>",
		// '-', {
		// text : '保存',
		// iconCls : 'save',
		// handler : saveRecord
		// }, '-', {
		// text : '取消',
		// iconCls : 'cancer',
		// handler : function() {
		// queryRecord();
		// }
		// }, '-', btnInquire, '-', '本年预算费用:', txtAdviceItem, '-',
		// '本年已领费用:', txtFactItem],
		clicksToEdit : 1,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false
	});

	// ---------add by sychen 20100419---------------------
	var headTbar = new Ext.Toolbar({
		renderTo : headGrid.bbar,
		items : [
				'请确认核准数量',
				"<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>",
				'颜色说明：',
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:40; color:white; background:green'>申请数量与审核数量不等</div>",
				'-', {
					text : '保存',
					iconCls : 'save',
					handler : saveRecord
				}, '-', {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						queryRecord();
					}
				}, '-', btnInquire, '-', '本年预算费用:', txtAdviceItem, '-',
				'本年已领费用:', txtFactItem]
	});
	// //-----------------------------------------------------------
	// materialGrid.on("celldblclick", cellClickHandler);

	materialGrid.on("beforeedit", function contorlEdit(e) {
		var record = e.record.get('isNewRecord');
		if (record == "total") {
			return false;
		}

	});

	function saveRecord() {
		var record = materialGrid.getStore().getModifiedRecords();

		var modifyRecords = new Array();
		for (var i = 0; i < materialStore.getTotalCount(); i++) {

			// if(materialStore.getAt(i).data == null ||
			// materialStore.getAt(i).data.approvedQty == null
			// || materialStore.getAt(i).data.approvedQty == '' ||
			// materialStore.getAt(i).data.approvedQty == 0)
			// {
			// Ext.Msg.alert('提示','核准数量不能为0！');
			// return;
			// }
			modifyRecords.push(materialStore.getAt(i).data);
		}
		// for (var i = 0; i < record.length; i++) {
		// modifyRecords.push(record[i].data);
		// }

		if (record.length > 0) {
			// Ext.Msg.confirm('提示','确认要保存吗？',function(button){
			// if(button == 'yes'){}
			// })
			Ext.Ajax.request({
				url : 'resource/updatePlanDetailForApprove.action',
				params : {
					// data : str
					data : Ext.util.JSON.encode(modifyRecords)
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					if (json.msg == "保存成功！") {
						showDetails();
						// materialStore.load({
						// params : {
						// start : 0,
						// limit : 10
						// },
						// callback : addLine
						// });// 加入统计行 ywliu 090723
						// materialStore.rejectChanges();
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		}

	}

	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		width : 180,
		readOnly : true
	});

	// var layout = new Ext.Viewport({
	// layout : 'fit',
	// margins : '0 0 0 0',
	// border : false,
	// items : [materialGrid]
	// });
	// 查询
	function queryRecord() {
		materialStore.baseParams = {
			headId : planId
		};
		materialStore.load({
			params : {
				start : 0,
				limit : 10
			},
			callback : addLine
		});
	}

	function addLine() {
		// 统计行
		var record = new material({
			requirementDetailId : "",
			materialId : "",
			materialName : "",
			equCode : "",
			materSize : "",
			parameter : "",
			stockUmName : "",
			appliedQty : "",
			estimatedPrice : "",
			left : "",
			maxStock : "",
			usage : "",
			factory : "",
			needDate : "",
			supplier : "",
			memo : "",
			itemId : "",
			planOriginalId : "",
			planOriginalIdHId : "",
			planOriginalIdHName : "",
			lastModifiedDate : "",
			isNewRecord : "total"
		});
		// 原数据个数
		var count = materialStore.getCount();
		// 停止原来编辑
		materialGrid.stopEditing();
		// 插入统计行
		materialStore.insert(count, record);
		materialGrid.getView().refresh();
		totalCount = materialStore.getCount() - 1;

	};
	// add by liuyi 091111 结束
	var layout = new Ext.Viewport({
		layout : 'border',
		margins : '0 0 0 0',
		border : false,
		items : [northPanel, materialGrid]
	});
});