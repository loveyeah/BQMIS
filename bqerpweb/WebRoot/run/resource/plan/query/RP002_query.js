Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var workCode = "";
	Ext.override(Ext.grid.GridView, {
		ensureVisible : function(row, col, hscroll) {
			if (typeof row != "number") {
				row = row.rowIndex;
			}
			if (!this.ds) {
				return;
			}
			if (row < 0 || row >= this.ds.getCount()) {
				return;
			}
			col = (col !== undefined ? col : 0);
			var rowEl = this.getRow(row), cellEl;
			if (!(hscroll === false && col === 0)) {
				while (this.cm.isHidden(col)) {
					col++;
				}
				cellEl = this.getCell(row, col);
			}
			if (!rowEl) {
				return;
			}
			var c = this.scroller.dom;
			var ctop = 0;
			var p = rowEl, stop = this.el.dom;
			while (p && p != stop) {
				ctop += p.offsetTop;
				p = p.offsetParent;
			}
			ctop -= this.mainHd.dom.offsetHeight;
			var cbot = ctop + rowEl.offsetHeight;
			var ch = c.clientHeight;
			var stop = parseInt(c.scrollTop, 10);
			var sbot = stop + ch;
			if (ctop < stop) {
				c.scrollTop = ctop;
			} else if (cbot > sbot) {
				c.scrollTop = cbot - ch;
			}
			if (hscroll !== false) {
				var cleft = parseInt(cellEl.offsetLeft, 10);
				var cright = cleft + cellEl.offsetWidth;
				var sleft = parseInt(c.scrollLeft, 10);
				var sright = sleft + c.clientWidth;
				if (cleft < sleft) {
					c.scrollLeft = cleft;
				} else if (cright > sright) {
					c.scrollLeft = cright - c.clientWidth;
				}
			}
			return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
					Ext.fly(rowEl).getY()];
		}
	});

	Ext.QuickTips.init();
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
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
		//modify by fyyang 091109 数量小数点后保留4位
//		v = (Math.round((v - 0) * 100)) / 100;
//		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
//				? v + "0"
//				: v);
//		v = String(v);
//		var ps = v.split('.');
//		var whole = ps[0];
//		var sub = ps[1] ? '.' + ps[1] : '.00';
//		var r = /(\d+)(\d{3})/;
//		while (r.test(whole)) {
//			whole = whole.replace(r, '$1' + ',' + '$2');
//		}
//		v = whole + sub;
//		if (v.charAt(0) == '-') {
//			return '-' + v.substr(1);
//		}
//		return v;
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
	// function checkTime() {
	// var startDate = txtMrDateFrom.getValue();
	// var endDate = txtMrDateTo.getValue();
	// if (startDate != "" && endDate != "") {
	// var res = compareDateStr(startDate, endDate);
	// if (res) {
	// Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
	// Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));
	//
	// }
	// }
	// return true;
	// }
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
	 * 物资详细grid双击处理
	 */
	function cellClickHandler(grid, rowIndex, columnIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "memo") {
			win.show();
			memoText.setValue(record.get("memo"));
			memoText.focus();
		}
	}
	/**
	 * 赋值主grid中物料类别
	 */
	function getClassName() {
		return txtMaterialClass.getRawValue();
	}
	/**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (headGrid.getSelectionModel().getSelected() != null) {
			var record = headGrid.getSelectionModel().getSelected();
			materialStore.load({
				params : {
					headId : record.get('requirementHeadId'),
					flag:"1"
				},
				callback : addLine 
			});
		}

	}
	/**
	 * 当双击主Grid中的一行时，判断是否可以编辑
	 */
	function showEdit() {
		// 判断是否有选中
		if (headGrid.selModel.hasSelection()) {
			var record = headGrid.getSelectionModel().getSelected();
			if (record.get('mrStatus') == "0" || record.get('mrStatus') == "9") {
				// TODO tab跳转操作
				var planId = record.get('requirementHeadId');
				register.edit(planId);
			}
		} else {
			Ext.MessageBox.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001)
		}

	}
	function view() {
		if (headGrid.selModel.hasSelection()) {
			var record = headGrid.getSelectionModel().getSelected();
			if (record.get('wfNo') == "" || record.get('wfNo') == "null"
					|| record.get('wfNo') == null) {
				Ext.Msg.alert('提示', '流程尚未启动！');
			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ record.get('wfNo');
				self.open(url);
			}
		} else {
			Ext.MessageBox.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001)
		}
	}
	/**
	 * 打印按钮触发事件
	 */
	function print() {
		if (headStore.getTotalCount() == 0 || headStore == null
				|| !headGrid.selModel.hasSelection()) {
			Ext.MessageBox.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_011)
		} else {
			if (headGrid.selModel.hasSelection()) {
				var record = headGrid.getSelectionModel().getSelected();
				var planId = record.get('requirementHeadId');
				window
						.open("power/report/webfile/materialRequestReport.jsp?headId="
								+ planId);

			}

		}
	}
	/**
	 * 弹出物料选择窗口
	 */
	// function selectMaterial() {
	// var mate = window.showModalDialog('../RP001.jsp', window,
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
				//maClassName : txtMaterialClass.getRawValue(),
				maClassNo : txtMaterialClassHid.getValue(),
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue()
			};
			headStore.load({
				params : {
					start : start,
					limit : Constants.PAGE_SIZE
				}
			});
			// 清空明细部分gird
			materialStore.removeAll();
		} else if (txtMrOriginal.getValue() == "4"
				|| txtMrOriginal.getValue() == "5") {
			var monthPlan = "";
			monthPlan = monthDate.getValue();
			headStore.baseParams = {
				monthPlan : monthPlan,
				mrDept : txtMrDeptH.getValue(),
				mrBy : txtMrByH.getValue(),
				maName : txtMaterialName.getValue(),
				//maClassName : txtMaterialClass.getRawValue(),
				maClassNo : txtMaterialClassHid.getValue(),
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue()
			};
			headStore.load({
				params : {
					start : start,
					limit : Constants.PAGE_SIZE
				}
			});
			// 清空明细部分gird
			materialStore.removeAll();
		} else {

			var startDate = timefromDate.getValue();
			var endDate = timetoDate.getValue();
			if (startDate != "" && endDate != "") {

				var res = compareDateStr(startDate, endDate);

				if (res) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
							Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

				} else {
					headStore.baseParams = {
						dateFrom : startDate,
						dateTo : endDate,
						mrDept : txtMrDeptH.getValue(),
						mrBy : txtMrByH.getValue(),
						maName : txtMaterialName.getValue(),
						maClassNo : txtMaterialClassHid.getValue(),
					//	maClassName : txtMaterialClass.getRawValue(),
						status : stateComboBox.getValue(),
						mrOriginal : txtMrOriginal.getValue()
					};
					headStore.load({
						params : {
							start : start,
							limit : Constants.PAGE_SIZE
						}
					});
					// 清空明细部分gird
					materialStore.removeAll();
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
		// status:stateComboBox.getValue()
		// };
		// headStore.load({
		// params : {
		// start : start,
		// limit : Constants.PAGE_SIZE
		// }
		// });
		// // 清空明细部分gird
		// materialStore.removeAll();
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
		// // 清空明细部分gird
		// materialStore.removeAll();
		// }
	}
	/**
	 * 获取申请人的姓名和工号
	 */
	
	// 从session取登录人编码姓名部门相关信息
	function getUserName() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					txtMrBy.setValue(result.workerName);
					txtMrByH.setValue(result.workerCode);
					workCode = result.workerCode;
				}
				findFuzzy();// add by ywliu 200911003
			}
		});
	}
	
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		if( workCode == '999999') {
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
							'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(person) != "undefined") {
				txtMrBy.setValue(person.workerName);
				txtMrByH.setValue(person.workerCode);
			}
		}	
	}

	function autoFind() {
		findFuzzy(pageBar.cursor);
	}
	var register = parent.Ext.getCmp('tabPanel').register;
	register.findRecord = autoFind;
	// -------工具栏按钮-----开始

	// 打印按钮
	var btnPrint = new Ext.Toolbar.Button({
		text : "打印",
		iconCls : 'print',
		handler : print
	});
	var btnView = new Ext.Toolbar.Button({
		text : "图形展示",
		handler : view,
		iconCls : 'view'
	});
	var toolBarTop = new Ext.Toolbar({
		region : 'north',
		items : [{
			xtype : 'tbfill',
			width : '80%'
		}, btnView, '-', btnPrint]
	});
	// --------工具栏按钮-----结束
	// --------第一行textbox--开始
	// 计划月份
	// var txtMrDateFrom = new Ext.form.TextField({
	// fieldLabel : '计划月份',
	// readOnly : true,
	// width : 108,
	// id : "mrDateFrom",
	// name : "mrDateFrom",
	// anchor : '100%',
	// style : 'cursor:pointer',
	// value : getCurrentDateFrom(),
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// // 时间格式
	// startDate : '%y-%M',
	// dateFmt : 'yyyy-MM',
	// alwaysUseStartDate : false
	// });
	//
	// }
	//
	// }
	//
	// });
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
						'../../../../comm/jsp/hr/dept/dept.jsp',
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
		id : 'mrBy',
		fieldLabel : '申请人员',
		name : 'mrByName',
		anchor : '100%',
		width : 108,
		readOnly : true
	});
	txtMrBy.onClick(selectPersonWin);
	var txtMrByH = new Ext.form.Hidden();
	
//	txtMrByH.on('render', getUserName) modify by ywliu 09/07/16 默认查所有需求计划单
	
	
	
	// 登记按钮
	var btnAdd = new Ext.Toolbar.Button({
		text : "物料需求计划登记",
		iconCls : Constants.CLS_WRITE,
		handler : function() {
			register.edit();
		}
	});

	// add by fyyang 090618---------------
	// -----add by fyyang -----------
	// 定义状态数据
//	var stateData = new Ext.data.JsonStore({
//		root : 'list',
//		url : "resource/findAllPlanBusiStatus.action",
//		fields : ['statusCode', 'statusName']
//	});
//
//	stateData.on('load', function(e, records) {
//		stateComboBox.setValue(records[0].data.statusCode);
//	});
//	stateData.load(); modify by ywliu 20091103
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		fieldLabel : '状态',
		id : "stateCob",
		store : [['ALL','所有'],['0','未上报'],['9','已退回']], // modify by ywliu 20091103
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		anchor : '100%',
		// width : 40,
		triggerAction : 'all',
		readOnly : true,
		value : 'ALL' // modify by ywliu 20091103
	});

	// 计划来源
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
		fieldLabel : '计划种类',

		// width : 108,
		anchor : '100%',
		displayField : 'text',
		valueField : 'id',
//		allowBlank : false,
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
		} else if (node.id == "4" || node.id == "5"||node.id == "13") {
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
	// add end----------------------------

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
			items : [txtMrOriginal, txtMrOriginalH]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMrDept]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [txtMrBy, txtMrByH]
		}, {
			columnWidth : 0.04,
			border : false
		}, {
			width : 125,
			layout : "form",
			border : false,
			items : [btnAdd]
		}]
	});
	// --------第一行textbox--结束
	// --------第二行textbox--开始
	// // 计划日期至
	// var txtMrDateTo = new Ext.form.TextField({
	// format : 'Y-m',
	// fieldLabel : "至",
	// itemCls : 'sex-left',
	// readOnly : true,
	// clearCls : 'allow-float',
	// checked : true,
	// width : 108,
	// value : getCurrentDateTo(),
	// id : "mrDateTo",
	// name : "mrDateTo",
	// anchor : '100%',
	// style : 'cursor:pointer',
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// // 时间格式
	// startDate : '%y-%M',
	// dateFmt : 'yyyy-MM',
	// alwaysUseStartDate : false
	// });
	//
	// }
	//
	// }
	//
	// });
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		readOnly : false,
		width : 108,
		id : "materialName",
		anchor : '100%'
	});
	// txtMaterialName.onClick(selectMaterial);
	
	
	
	// 物料类别
	var txtMaterialClass = new Ext.ux.ComboBoxTree({
		fieldLabel : '物料类别',
		allowBlank : true,
		width : 108,
		id : "materialClassId",
		anchor : '100%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'txtmaterialClass',
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
			}),
			listeners : {
				click : function(node, e) {
					txtMaterialClassHid.setValue(node.attributes.id);
				}
			}
		},
		selectNodeModel : 'all'
	});
	
	//add by drdu 0900703
	var txtMaterialClassHid = new Ext.form.Hidden();
	
	// -----------add by
	// fyyang-----090617---------------------------------------------
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
	var startdate = enddate.add(Date.MONTH, -1);
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

	// var yearRadio = new Ext.form.Radio({
	// id : 'year',
	// name : 'queryWayRadio',
	// hideLabel : true,
	// boxLabel : '按年份'
	// });

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

	// var yearDate = new Ext.form.TextField({
	// id : 'yearDate',
	// name : '_yearDate',
	// fieldLabel : "年份",
	// style : 'cursor:pointer',
	// cls : 'Wdate',
	// // width : 150,
	// value : new Date().getFullYear(),
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// startDate : '%y',
	// dateFmt : 'yyyy',
	// alwaysUseStartDate : true
	// });
	// }
	// }
	// });

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
	 * 年份Panel
	 */
	// var yearPanel = new Ext.Panel({
	// id : 'yearPanel',
	// hidden : true,
	// border : false,
	// layout : 'form',
	// labelWidth : 32,
	// items : [yearDate]
	// });
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

	// ---------------add end
	// --------------------------------------------------------------------

	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
		text : "查询",
		iconCls : Constants.CLS_QUERY,
		handler : findFuzzy
	});

	// 查看流程按钮
	var btnSure = new Ext.Toolbar.Button({
		text : "流程",
		iconCls : 'view',
		handler : function()
		{
			
			if (headGrid.selModel.hasSelection()) {
			var record = headGrid.getSelectionModel().getSelected();
			var entryId=record.get("wfNo");
			var planSource=record.get("planOriginalId");  
			var flowCode=getFlowCode(planSource);
			if(flowCode=="")
			{
				Ext.Msg.alert("提示","该流程不存在！");
				return ;
			}
			if(entryId==null||entryId=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode;
        	}
        	else
        	{
        		url ="/power/workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open(url);
		} else {
			Ext.MessageBox.alert("提示","请选择要查看的记录");
		}
		}
		
	
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
			items : [txtMaterialClass,txtMaterialClassHid]
		}, {
			columnWidth : 0.32,
			layout : "form",
			border : false,
			items : [stateComboBox]
		}, {
			columnWidth : 0.04,
			border : false
		}, {
			width : 125,
			layout : "column",
			border : false,
			items : [{
				width : 70,
				layout : "form",
				border : false,
				items : [btnFind]
			}, {
				width : 50,
				layout : "form",
				border : false,
				items : [btnSure]
			}]
		}

		]
	});

	// var secondLine2 = new Ext.Panel({
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

	// -----------------------
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
		name : 'itemCode'//modify by ywliu  2009/7/6
	}, {	// 费用来源
		name : 'wfNo'
	}, {
		name : 'planOriginalId'
	},{
	  name:'prjNo'
	}])
	// --------第三行grid--开始
	// 主表一览的store
	var headStore = new Ext.data.JsonStore({
		url : 'resource/getPlanHeadByFuzzy.action',
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
		region : "center",
		layout : 'fit',
		height : 200,
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
					width : 80,
					sortable : true,
					renderer : function(val) {
						return busiStatus(val);
						// if(val == "0"){
						// return "待审批";
						// }else if(val =="1"){
						// return "审批中";
						// }else if(val == "2"){
						// return "审批结束";
						// }else if(val == "0"){
						// return "退回";
						// }else {
						// return "";
						// }
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
					width : 130,
					sortable : true,
					dataIndex : 'mrDept'
				},
				// 申请人
				{
					header : "申请人",
					width : 80,
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
						//return "<span title='"+value+"' >"+value+"</span>";
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
				 },
				{
					header : "计划时间",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex) {
						return getPlanDateInfoByDate(record
								.get("planOriginalId"), value);
					},
					dataIndex : 'mrDate'
				},
				{
					header : "计划种类",
					width : 100,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex) {
						return planOriginalName(record
								.get("planOriginalId"), value);
					},
					dataIndex : 'planOriginalId'
				},
				// 物料类别
				{
					header : "物料类别",
					width : 100,
					sortable : true,
					renderer : getClassName,
					dataIndex : 'materialClass'
					// 05/08/09 15:30 不显示该列 yiliu
					,
					hidden : true
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
					 renderer : function(val) {
					 	if(val==""||val==null)// modify by wpzhu 100408
					 	{
					 	return"";
					 	}

					 	if (val == "zzfy") {
							return "生产类费用";
						} else if (val == "sclfy") {
							return "生产类费用";
						}else {
							return getBudgetName(val);
						}},
					dataIndex : 'itemCode'//modify by ywliu  2009/7/6
				}],
		// 分页
		bbar : pageBar
	});

	headGrid.on('rowclick', showDetail);
	headGrid.on('rowdblclick', showEdit);

	/**
	 * 初始化数据load
	 */
	function firstLoad() {
		headStore.baseParams = {
			dateFrom : timefromDate.getValue(),// txtMrDateFrom.getValue(),
			dateTo : timetoDate.getValue(), // txtMrDateTo.getValue(),
			mrDept : txtMrDeptH.getValue(),
			mrBy : txtMrByH.getValue(),
			maName : txtMaterialName.getValue(),
			maClassName : txtMaterialClass.getRawValue(),
			status : stateComboBox.getValue()
		};
		headStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	headStore.on('load', function() {
		
		if (headStore.getCount() < 1) {
			btnSure.disable();
		} else {
			btnSure.enable();
		}

	})

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
		name : 'itemId'//modify by ywliu  2009/7/6
	},{
	    name:'cancelReason'
	},{
	    name:'useFlag'
	}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
		url : 'resource/getMaterialDetail.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
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
					header : "明细ID",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'requirementDetailId'
				},
				// 物料编码
				{
					header : "物料编码",
					width : 100,
					sortable : true,
					dataIndex : 'materialNo'
				},// 物料名称
				{
					header : "物料名称",
					width : 100,
					sortable : true,
					dataIndex : 'materialName'
				},// 物料规格
				{
					header : "物料规格",
					width : 100,
					sortable : true,
					dataIndex : 'materSize'
				},// 申请数量
				{
					header : "状态",
					width : 100,
					sortable : true,
					dataIndex : 'useFlag',
					renderer:function(value)
					{
						if(value=="C") return "已作废";
						if(value=="Y") return "未作废";
					}
				},
				{
					header : "作废原因",
					width : 100,
					sortable : true,
					dataIndex : 'cancelReason'
				},
				{
					header : "申请数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('appliedQty',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'appliedQty'
				},// 核准数量
				{
					header : "核准数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedQty');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('approvedQty',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedQty');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'approvedQty'
				},// 已领数量
				{
					header : "已领数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('issQty',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'issQty'
				},// 估计单价
				{
					header : "估计单价",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('estimatedPrice',
										"");
							}
							return numberFormat(value);
						}
					},
					dataIndex : 'estimatedPrice'
				},// 估计金额
				{
					header : "估计金额",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('estimatedSum',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							return "<font color='red'>" + numberFormat(totalSum)
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
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('purQty');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('purQty',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('purQty');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'purQty'
				},
				// 单位
				{
					header : "单位",
					width : 100,
					sortable : true,
					dataIndex : 'stockUmName'
				},
				// 需求日期
				{
					header : "需求日期",
					width : 100,
					sortable : true,
					dataIndex : 'needDate'
				},

				// 物料材质
				{
					header : "物料材质",
					width : 100,
					sortable : true,
					dataIndex : 'parameter'
				},
				// 物料图号
				{
					header : "物料图号",
					width : 100,
					sortable : true,
					dataIndex : 'docNo'
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					sortable : true,
					dataIndex : 'whsName'
				},// 质量等级
				{
					header : "质量等级",
					width : 100,
					sortable : true,
					dataIndex : 'qualityClass'
				},
				// 当前库存
				{
					header : "当前库存",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('tempNum',
										"");
							}
							return numberFormat(value);
						}
					},
					dataIndex : 'left'
				},
				// 暂收数量
				{
					header : "暂收数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('tempNum');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('tempNum',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('tempNum');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'tempNum'
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
					renderer : function(val) {
//						if (val == "1") {
//							return "费用来源1";
//						} else if (val == "2") {
//							return "费用来源2";
//						} else if (val == "3") {
//							return "费用来源3";
//						} else if (val == "4") {
//							return "费用来源4";
//						} else {
//							return "";
//						}//modify by ywliu  2009/7/6
//						if (val == "zzfy") {
//							return "制造费用";
//						} else if (val == "lwcb") {
//							return "劳务成本";
//						}else {
//							return "";
//						}
						// modify by ywliu 20100225
						return getBudgetName(val);
					},
					dataIndex : 'itemId'//modify by ywliu  2009/7/6
				},
				// 用途
				{
					header : "用途",
					width : 100,
					sortable : true,
					dataIndex : 'usage'
				},
				// 备注
				{
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'memo'
				}]
	});
	materialGrid.on("celldblclick", cellClickHandler);
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
	var fourthLine = new Ext.Panel({
		// border : false,
		layout : "fit",
		anchor : '100%',
		autoHeight : true,
		items : [materialGrid]
	});
	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		border : false,
		autoHeight : true,
		region : 'north',
		items : [toolBarTop, fiveLine, firstLine, secondLine, thirdLine]
	});
	var queryPanel = new Ext.Panel({
		layout : 'border',
		frame : false,
		border : false,
		items : [formPanel, materialGrid]
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		border : false,
		items : [queryPanel]
	});
	
	// add by ywliu 09/07/16
//	findFuzzy();
	
	/**
	 * 添加统计行 add by ywliu 090722
	 */
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

	
	setTxtMrCostSpecial(0);
	getUserName();

});