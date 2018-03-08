Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// add by ywliu 20091112
	var queryType = "";
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
		// modify by fyyang 091109 数量小数点后保留4位
		// v = (Math.round((v - 0) * 100)) / 100;
		// v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v *
		// 10))
		// ? v + "0"
		// : v);
		// v = String(v);
		// var ps = v.split('.');
		// var whole = ps[0];
		// var sub = ps[1] ? '.' + ps[1] : '.00';
		// var r = /(\d+)(\d{3})/;
		// while (r.test(whole)) {
		// whole = whole.replace(r, '$1' + ',' + '$2');
		// }
		// v = whole + sub;
		// if (v.charAt(0) == '-') {
		// return '-' + v.substr(1);
		// }
		// return v;
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
	 * 数字格式化 保留两位小数
	 */
	function numberParseTwo(v) {
		if (v == null || v == "") {
			return "0.00";
		}
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
			var start = record.get('mrDept').indexOf('/');
			var deptCode = record.get('mrDept').substring(start + 1,
					record.get('mrDept').length);
			txtAdviceItem.setValue(getBudgetName(record.data['itemCode'], 1,
					deptCode));

			// --------add by fyyang 20100419---------------
			var data = getBudgetName(record.data['itemCode'], 3, deptCode);
			var mydata = data.split(",");
			txtFactItem.setValue(mydata[0]);
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
			materialStore.load({
				params : {
					headId : record.get('requirementHeadId'),
					flag : "1"
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
				url = application_base_path
						+ "workflow/manager/show/show.jsp?entryId="
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
				window.open(application_base_path
						+ "report/webfile/materialRequestReport.jsp?headId="
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
				observer : workCode, // add by ywliu 20091113
				queryType : queryType,// add by ywliu 20091113
				maName : txtMaterialName.getValue(),
				// maClassName : txtMaterialClass.getRawValue(),
				maClassNo : txtMaterialClassHid.getValue(),// modify by ywliu
															// 20091116
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue(),
				mrPlanNo : txtMrPlanNo.getValue()
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
				observer : workCode, // add by ywliu 20091113
				queryType : queryType,// add by ywliu 20091113
				maName : txtMaterialName.getValue(),
				// maClassName : txtMaterialClass.getRawValue(),
				maClassNo : txtMaterialClassHid.getValue(),// modify by ywliu
															// 20091116
				status : stateComboBox.getValue(),
				mrOriginal : txtMrOriginal.getValue(),
				mrPlanNo : txtMrPlanNo.getValue()
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
						observer : workCode, // add by ywliu 20091113
						queryType : queryType,// add by ywliu 20091113
						mrBy : txtMrByH.getValue(),
						maName : txtMaterialName.getValue(),
						// maClassName : txtMaterialClass.getRawValue(),
						maClassNo : txtMaterialClassHid.getValue(),// modify by
																	// ywliu
																	// 20091116
						status : stateComboBox.getValue(),
						mrOriginal : txtMrOriginal.getValue(),
						mrPlanNo : txtMrPlanNo.getValue()
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
		// else {
		// alert();
		// var quarter = "";
		// var monthPlan = "";
		// var quarterYear = "";
		// var quarter = "";
		// if(txtMrOriginal.getValue() == "6") {
		// quarterYear = quarterDate.getValue();
		// quarter = quarterBox.getValue();
		// } else if(txtMrOriginal.getValue() == "4" || txtMrOriginal.getValue()
		// == "5") {
		// monthPlan = monthDate.getValue();
		// }
		// headStore.baseParams = {
		// monthPlan : monthPlan,
		// quarterYear : quarterYear,
		// quarter : quarter ,
		// dateFrom : txtMrDateFrom.getValue(),
		// dateTo : txtMrDateTo.getValue(),
		// mrDept : txtMrDept.getValue(),
		// mrBy : txtMrByH.getValue(),
		// maName : txtMaterialName.getValue(),
		// maClassName : txtMaterialClass.getRawValue(),
		// status : stateComboBox.getValue(),
		// mrOriginal : txtMrOriginal.getValue()
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
	function getUserName(init) {
		// btnInquire.setVisible(false);
		if (init) {
			// add by ywliu 20091113
			btnReturn.setVisible(false);
			Ext.get("all").dom.parentNode.style.display = "none";
			Ext.Ajax.request({
				url : 'resource/getQueryRightByWorkId.action',
				method : 'post',
				params : {
					fileAddr : 'run/resource/plan/query/MRPInformation.jsp'
				},
				success : function(action) {
					var state = action.responseText;
					if (state.indexOf('1') != -1) {
						Ext.get("all").dom.parentNode.style.display = "";
					}
					if (state.indexOf('2') != -1) {
						btnReturn.setVisible(true);
					}
				}
			});
		}
		if (!init) {// add by ywliu 20091113
			Ext.Ajax.request({
				url : 'resource/getInfo.action',
				method : 'post',
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
					if (result != "" && result != null) {
						// txtMrBy.setValue(result.workerName);
						// txtMrByH.setValue(result.workerCode);
						workCode = result.workerCode;
					}
					// firstLoad();
					findFuzzy();
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
		if (queryType != '1') {// add by ywliu 20091113
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

	function setTxtMrCostSpecial(node) {
		if (node.id == "6") {
			Ext.get("quarter").dom.checked = true;
			quarterPanel.show();
			timePanel.hide();
			monthPanel.hide();
		} else if (node.id == "4" || node.id == "5") {
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

	/**
	 * 添加统计行
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
	var totalSum = 0;

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
		width : 90,
		checked : true,
		disabled : true
	});

	var quarterRadio = new Ext.form.Radio({
		id : 'quarter',
		name : 'queryWayRadio',
		hideLabel : true,
		disabled : true,
		width : 90,
		boxLabel : ' 按季度'
	});

	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		disabled : true,
		width : 108,
		boxLabel : '按月份'
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
	// -----add by fyyang 计划单号----100112-------
	var txtMrPlanNo = new Ext.form.TextField({
		fieldLabel : '申请单号',
		maxLength : 100,
		width : 108,
		id : "mrPlanNo",
		name : "mrPlanNo",
		anchor : '90%'
	});

	// -------------------------------------

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
		width : 73,
		selectOnFocus : true,
		value : 1
	});

	var timePanel = new Ext.Panel({
		id : 'timeAreaPanel',
		border : false,
		layout : 'form',
		labelWidth : 60,
		items : [{
			layout : 'column',
			bodyStyle : "padding:0 0 0 38",
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [timefromDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.25,
				items : [timetoDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.25,
				items : [txtMrPlanNo]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				width : 18
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
		// labelWidth : 32,
		layout : 'form',
		border : false,
		// hidden : true,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [quarterDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.3,
				items : [quarterBox]
			}]

		}]
	});
	/**
	 * 月份Panel
	 */
	var monthPanel = new Ext.Panel({
		id : 'monthPanel',
		// labelWidth : 32,
		border : false,
		// hidden : true,
		layout : 'form',
		items : [{
			border : false,
			columnWidth : 0.35,
			align : 'center',
			layout : 'form',
			items : [monthDate]
		}]
	});

	// add by ywliu 20091112
	function getChooseQueryType() {
		var list = document.getElementsByName("rightRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

	// add by ywliu 20091112
	var reportRadio = new Ext.form.Radio({
		id : 'report',
		name : 'rightRadio',
		// modified by liuyi 20100317
		// boxLabel : '我上报的',
		boxLabel : '我的',
		width : 90,
		hideLabel : true,
		checked : true,
		listeners : {
			check : function() {
				var type = getChooseQueryType();
				switch (type) {
					case 'report' : {
						queryType = '1';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
					case 'approve' : {
						queryType = '2';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
					case 'all' : {
						queryType = '3';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						// btnInquire.setVisible(true);
						break;
					}
					case 'dept' : {
						queryType = '4';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
				}
				// findFuzzy();
			}
		}
	});

	var deptRadio = new Ext.form.Radio({
		id : 'dept',
		name : 'rightRadio',
		hideLabel : true,
		// disabled : true,
		width : 90,
		boxLabel : ' 本部门的'
	});

	var approveRadio = new Ext.form.Radio({
		id : 'approve',
		name : 'rightRadio',
		hideLabel : true,
		// disabled : true,
		width : 90,
		boxLabel : ' 我审批的'
	});

	var allRadio = new Ext.form.Radio({
		id : 'all',
		name : 'rightRadio',
		hideLabel : true,
		labelSeparator : '',
		width : 90,
		boxLabel : '所有的'
	});

	// var queryTypeRadioPanel = new Ext.Panel({
	// achor : '100%',
	// border : false,
	// layout : 'form',
	// items : [{
	// layout : 'column',
	// border : false,
	// items : [{
	// border : false,
	// columnWidth : 0.15,
	// align : 'center',
	// layout : 'form',
	// items : [timeRadio]
	// }, {
	// align : 'center',
	// layout : 'form',
	// border : false,
	// columnWidth : 0.12,
	// items : [quarterRadio]
	// }, {
	// border : false,
	// columnWidth : 0.12,
	// align : 'center',
	// layout : 'form',
	// items : [monthRadio]
	// }, {
	// border : false,
	// columnWidth : 0.15,
	// layout : 'form',
	// items : [reportRadio]
	// }, {
	// border : false,
	// columnWidth : 0.15,
	// align : 'center',
	// layout : 'form',
	// items : [approveRadio]
	// }, {
	// border : false,
	// columnWidth : 0.15,
	// align : 'center',
	// layout : 'form',
	// items : [allRadio]
	// }]
	// }]
	// });

	// var allRadioPanel = new Ext.Panel({
	// achor : '100%',
	// border : false,
	// layout : 'form',
	// items : [{
	// layout : 'column',
	// border : false,
	// items : [{
	// border : false,
	// columnWidth : 0.15,
	// align : 'center',
	// layout : 'form',
	// items : [allRadio]
	// }]
	// }]
	// });

	var Query = new Ext.Panel({
		border : false,
		height : 30,
		anchor : '100%',
		layout : 'column',
		items : [{
			id : 'query',
			border : false,
			columnWidth : 1,
			anchor : '100%',
			items : [timePanel, quarterPanel, monthPanel]
		// }, {
		// border : false,
		// columnWidth : 0.5,
		// anchor : '100%',
		// items : [queryTypeRadioPanel]
		// }, {
		// border : false,
		// columnWidth : 0.15,
		// anchor : '100%',
		// items : [allRadioPanel]
		}]
	});

	var toolBarTop = new Ext.Toolbar({
		region : 'north',
		items : [timeRadio, quarterRadio, monthRadio, reportRadio, deptRadio,
				approveRadio, allRadio]
	});
	// --------工具栏按钮-----结束
	// --------第一行textbox--开始
	// 计划月份
	// var txtMrDateFrom = new Ext.form.TextField({
	// fieldLabel : '年份',
	// readOnly : true,
	// width : 108,
	// id : "mrDateFrom",
	// name : "mrDateFrom",
	// anchor : '90%',
	// style : 'cursor:pointer',
	// value : getCurrentDateFrom(),
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// // 时间格式
	// startDate : '%y',
	// dateFmt : 'yyyy',
	// alwaysUseStartDate : false
	// });
	//
	// }
	//
	// }
	//
	// });
	function choseDept() {
		if (queryType != '1' && queryType != '4') {
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
	}
	// 部门
	var txtMrDept = new Ext.form.TriggerField({
		fieldLabel : '申请部门',
		width : 108,
		id : "mrDeptId",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'mrDept',
		blankText : '请选择',
		emptyText : '请选择',
		maxLength : 100,
		anchor : '90%',
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
		anchor : '90%'
	});
	txtMrBy.onClick(selectPersonWin);
	var txtMrByH = new Ext.form.Hidden();

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		readOnly : false,
		width : 200,
		id : "materialName",
		anchor : '90%'
	});
	// txtMaterialName.onClick(selectMaterial);

	// 查看流程按钮
	var btnView = new Ext.Toolbar.Button({
		text : "流程",
		iconCls : 'view',
		handler : function() {

			if (headGrid.selModel.hasSelection()) {
				var record = headGrid.getSelectionModel().getSelected();
				var entryId = record.get("wfNo");
				var planSource = record.get("planOriginalId");
				var flowCode = getFlowCode(planSource);
				if (flowCode == "") {
					Ext.Msg.alert("提示", "该流程不存在！");
					return;
				}
				if (entryId == null || entryId == "") {
					url = application_base_path
							+ "workflow/manager/flowshow/flowshow.jsp?flowCode="
							+ flowCode;
				} else {
					url = application_base_path
							+ "workflow/manager/show/show.jsp?entryId="
							+ entryId;
				}
				window.open(url);
			} else {
				Ext.MessageBox.alert("提示", "请选择要查看的记录");
			}
		}

	});

	// 查询详细信息按钮
	var btnReturn = new Ext.Toolbar.Button({
		text : "查询详细",
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			window.location.replace("../query/MRPMaterialQuery.jsp");
		}
	});

	var firstLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		// style :
		// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrDept]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrBy, txtMrByH]
		}, {
			columnWidth : 0.25,
			border : false,
			layout : "form",
			items : [txtMaterialName]
		}, {
			width : 72,
			columnWidth : 0.1,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [btnView]
			}]
		}, {
			width : 72,
			columnWidth : 0.1,
			layout : "form",
			border : false,
			items : [{
				// width : 72,
				layout : "form",
				border : false,
				items : [btnReturn]
			}]
		}]
	});
	// --------第一行textbox--结束
	// --------第二行textbox--开始
	// 计划日期至
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
	// anchor : '90%',
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

	// 物料类别
	var txtMaterialClass = new Ext.ux.ComboBoxTree({
		fieldLabel : '物料类别',
		allowBlank : true,
		width : 108,
		id : "materialClassId",
		anchor : '90%',
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
			}),
			listeners : { // add by ywliu 20091116
				click : function(node, e) {
					txtMaterialClassHid.setValue(node.attributes.id);
				}
			}
		},
		selectNodeModel : 'all'
	});
	// add by ywliu 20091116
	var txtMaterialClassHid = new Ext.form.Hidden();

	// -----add by fyyang -----------
	// 定义状态数据
	var stateData = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/findAllPlanBusiStatus.action",
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
		anchor : '90%',
		// width : 40,
		triggerAction : 'all',
		readOnly : true
	});

	// 计划种类
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
		fieldLabel : '计划种类',
		anchor : '90%',
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

	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
		text : "查询",
		iconCls : Constants.CLS_QUERY,
		handler : findFuzzy
	});

	var secondLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		// style :
		// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMaterialClass]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [stateComboBox]
		}, {
			columnWidth : 0.25,
			border : false,
			layout : "form",
			items : [txtMrOriginal, txtMrOriginalH]
		}, {
			// width : 125,
			// 05/08/09 13:21 使上下两行数据对齐 yiliu
			width : 72,
			columnWidth : 0.1,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [btnFind]
			}]
		}]
	});

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
		name : 'itemCode'
	}, {	// 费用来源
		name : 'wfNo'
	}, {
		name : 'planOriginalId'
	},
	{	// 工程项目
		name : 'prjNo'
	} ])
	// --------第三行grid--开始
	// 主表一览的store
	var headStore = new Ext.data.JsonStore({
		url : 'resource/getPlanHeadByLogin.action',
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
	// add by fyyang 090728---增加删除------------
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = headGrid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条需求计划单吗?")) {
					var headId = record.get("requirementHeadId");
					var entryId = record.get("wfNo");
					Ext.Ajax.request({
						url : "resource/deletePlanRequirement.action",
						method : 'post',
						params : {
							entryId : entryId,
							headId : headId
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							headGrid.getStore().remove(record);
							materialStore.removeAll();
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}
		}
	});
	// -------------------------------------------

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
					width : 150, // modify by ywliu 20091103
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
					width : 100,
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
						// return "<span title='"+value+"' >"+value+"</span>";
						metadata.attr = 'style="white-space:normal;"';
						return value;
					}
				},
				// 申请时间
				{
					header : "申请时间",
					width : 100,
					sortable : true,
					renderer : renderDate,
					dataIndex : 'mrDate',
					renderer : function(value, cellmeta, record, rowIndex) {
						if (value.length >= 10) {
							value = value.substring(0, 4) + "年"
									+ value.substring(5, 7) + "月"
									+ value.substring(8, 10) + "日";
						}
						// modified by liuyi 091130 不处理日期
						// return getPlanDateInfoByDate(record
						// .get("planOriginalId"), value);
						return value;
					}
				},
				// 物料类别
				{
					header : "物料类别",
					width : 100,
					sortable : true,
					renderer : getClassName,
					dataIndex : 'materialClass'
					// 05/08/09 15:46 隐藏该列 yiliu
					,
					hidden : true
				},//工程项目  add by wpzhu 10/05/11
					{
					header : "工程项目",
					width : 100,
					sortable : true,
					dataIndex : 'prjNo',
					renderer : function(val) {
						if (val == "" || val == null)									
						{
							return "";
						}
						else {
							return getPrjShowNo(val);
						}
					}
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
				}],
		tbar : ['->', btnDelete, '<div id="divManagerDel">需求计划列表</div>'],
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
			dateFrom : timefromDate.getValue(),
			dateTo : timetoDate.getValue(),
			mrDept : txtMrDeptH.getValue(),
			mrBy : txtMrByH.getValue(),
			maName : txtMaterialName.getValue(),
			queryType : queryType,// add by ywliu 20091113
			maClassName : txtMaterialClass.getRawValue(),
			status : stateComboBox.getValue(),
			mrPlanNo : txtMrPlanNo.getValue()
		};
		headStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	headStore.on('load', function() {
		// if(headStore.getCount()<1){
		// btnSure.disable();
		// }else{
		// btnSure.enable();
		// }

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
	var expander = new Ext.grid.RowExpander({
		// tpl : new Ext.Template(
		// // '<p><b>工作票号:</b> </p><br>',
		// '<p><b>工作内容：</b></p>'),
		expandRow : mygridExpend
	});
	function mygridExpend(row) {
		if (typeof row == 'number') {
			row = this.grid.view.getRow(row);
		}
		var record = this.grid.store.getAt(row.rowIndex);
		var id = record.get("requirementDetailId");
		// 增加判断明细ID是否为空 add by ywliu 090722
		if (id != null && id != "") {
			// //-----------------------
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getOrdersByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			// 成功状态码为200
			if (conn.status == "200") {
				var result = conn.responseText;
				// alert(result);
				var record1 = eval('(' + result + ')');
				var purNo = "采购单号：";
				var purchaseQuatity = "采购数量："
				if (record1.materialName != null && record1.materialName != "") {
					purNo += record1.materialName
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					purNo = "";
				}
				if (record1.purchaseQuatity != null
						&& record1.purchaseQuatity != "") {
					purchaseQuatity += record1.purchaseQuatity
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					purchaseQuatity = "";
				}
				var achieveQuantity = "已收数量：";
				if (record1.achieveQuantity != null
						&& record1.achieveQuantity != "") {
					achieveQuantity += record1.achieveQuantity
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					achieveQuantity = "";
				}
				var clientName = "";
				if (record1.meno != null && record1.meno != "") {
					clientName = "供应商：" + record1.meno
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}// modify by drdu 091029
				// var price="";
				// if(record1.quotedPrice!=null&&record1.quotedPrice!="")
				// {
				// price="单价："+record1.quotedPrice;
				// }
			}
			conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getArrivalListByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			if (conn.status == "200") {
				var result = conn.responseText;
				// alert(result);
				var record2 = eval('(' + result + ')');
				var mifNo = "";
				if (record2 != null) {
					for (var i = 0; i < record2.length; i++) {
						var itemStatus = record2[i].itemStatus;
						if (itemStatus == "0") {
							itemStatus = "已到货";
						} else if (itemStatus == "1") {
							itemStatus = "已验收合格";
						} else if (itemStatus == "3") {
							itemStatus = "已验收不合格";
						} else if (itemStatus == "2") {
							itemStatus = "已入库";
						}

						mifNo += "到货单号："
								+ record2[i].mifNo
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+
								// "检验不合格数:"+ record2[i].badQty +
								"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "已收数量：" + record2[i].rcvQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "已入库数量:" + record2[i].recQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "本次到货数量:" + record2[i].theQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "物料状态:" + itemStatus + "<BR>";
					}
				}
			}
			conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getIssueDetailByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			if (conn.status == "200") {
				var result = conn.responseText;
				// alert(result);
				var record3 = eval('(' + result + ')');
				var issueNo = "";
				var appliedCount = "";
				var actIssuedCount = "";
				var approvedCount = "";
				if (record3 != null) {
					if (record3.issueNo != null && record3.issueNo != "") {
						issueNo = "领料单号：" + record3.issueNo
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.appliedCount != null
							&& record3.appliedCount != "") {
						appliedCount = "申请数量：" + record3.appliedCount
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.approvedCount != null
							&& record3.approvedCount != "") {
						approvedCount = "核准数量：" + record3.approvedCount
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.actIssuedCount != null
							&& record3.actIssuedCount != "") {
						actIssuedCount = "实际发货数量：" + record3.actIssuedCount;
					}
				}
			}
			expander.tpl = new Ext.Template(
					// '<p><font color="blue"><b>工作票号：</b>'+no+'</font>
					// </p><br>',
					'<p> <br><font color="blue"><b>采购单内容：</b><br>' + '<span>'
							+ purNo + '</span>' + purchaseQuatity
							+ achieveQuantity + clientName + '</font></p>',
					'<p> <br><font color="blue"><b>到货单内容：</b><br>' + mifNo
							+ '</font></p>',
					'<p> <br><font color="blue"><b>领料单内容：</b><br>' + issueNo
							+ appliedCount + approvedCount + actIssuedCount
							+ '&nbsp;&nbsp;</font></p>');
			var body = Ext.DomQuery.selectNode(
					'tr:nth(2) div.x-grid3-row-body', row);
			if (this.beforeExpand(record, body, row.rowIndex)) {
				this.state[record.id] = true;
				Ext.fly(row).replaceClass('x-grid3-row-collapsed',
						'x-grid3-row-expanded');
				this.fireEvent('expand', this, record, body, row.rowIndex);
			}
		}
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
	}, {	// add by drdu 091109
		name : 'useFlag'
	}, {	// add by drdu 091109
		name : 'cancelReason'
	}, {
		name : 'factory'
	}, {
		name : 'supplier'
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
	// ---------询价按钮 add by fyyang 100112-------------------
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
								'priceQueryForSelect.jsp',
								args,
								'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
			} else {
				Ext.Msg.alert("提示", "请先选择物资!");
			}
		}
	});

	// {text:'查看询价信息',// 隐藏查看询价信息按钮
	// iconCls : Constants.CLS_QUERY,
	// handler:function(){
	//							
	// if (materialGrid.selModel.hasSelection()) {
	//		
	// var records = materialGrid.selModel.getSelections();
	// var record = materialGrid.getSelectionModel().getSelected();
	//														  
	// var args=new Object();
	// args.materialName=record.get("materialName");
	// args.requirementDetailId=record.get("requirementDetailId");
	// var object = window
	// .showModalDialog(
	// 'priceQueryForSelect.jsp',
	// args,
	// 'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
	// } else {
	// Ext.Msg.alert("提示", "请先选择物资!");
	// }
	//								
	// }
	// }
	// --------------------------------------------------------

	// ----add by fyyang 20100312
	var txtAdviceItem = new Ext.form.TextField({
		name : 'txtAdviceItem',
		fieldLabel : "本年预算费用",
		readOnly : true,
		width : 80
	});

	var txtFactItem = new Ext.form.TextField({
		name : 'txtFactItem',
		fieldLabel : "本年已领费用",
		readOnly : true,
		width : 80
	});
	// ------------------------------
	// ----add by fyyang 20100419------------
	var txtCheckPrice = new Ext.form.TextField({
		name : 'txtCheckPrice',
		fieldLabel : "本年财务审核领料费用:",
		readOnly : true,
		width : 80
	});
	var txtCheckPriceRate = new Ext.form.TextField({
		name : 'txtCheckPriceRate',
		fieldLabel : "本年财务审核领用费用完成率",
		readOnly : true,
		width : 60
	});
	var txtFactItemRate = new Ext.form.TextField({
		name : 'txtFactItemRate',
		fieldLabel : "本年仓库发出费用完成率",
		readOnly : true,
		width : 60
	});
	// ---------------------------------------------

	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
		// layout : 'fit',
		// height : 200,
		// anchor : "100%",
		// renderTo : "myGrid",
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
		columns : [expander, new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 项次号
				{
					header : "项次号",
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
					dataIndex : 'materialNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {

						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty
									&& record.get('useFlag') == 'Y'
									&& v != null) {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty
									&& record.get('useFlag') == 'Y'
									&& v != null) {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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

						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if (appliedQty != approvedQty
									&& record.get('useFlag') == 'Y'
									&& v != null) {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
								return v;
						}
					}
				},// 申请数量
				{
					header : "申请数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
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
							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来

							var appQty = record.get('approvedQty');
							if ((appQty != value)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";

						}
					},
					dataIndex : 'appliedQty'

				},// 核准数量
				{
					header : "核准数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
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

							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来

							var appliedQty = record.get('appliedQty');
							if ((appliedQty != value)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedQty');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";
						}
					},
					dataIndex : 'approvedQty'
				},// 已领数量
				{
					header : "已领数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('issQty',
										totalSum);
							}

							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";
						}
					},
					dataIndex : 'issQty'
				},// 估计单价
				{
					header : "估计单价",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'estimatedPrice', "");
							}
							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberParseTwo(value) + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberParseTwo(value) + "</font>";
							} else {
								return numberParseTwo(value);
							}
						}
					},
					dataIndex : 'estimatedPrice'
				},// 估计金额
				{
					header : "估计金额",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
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
							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberParseTwo(value) + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberParseTwo(value) + "</font>";
							} else {
								return numberParseTwo(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							return "<font color='red'>"
									+ numberParseTwo(totalSum) + "</font>";
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
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('purQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('purQty',
										totalSum);
							}
							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}

							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('purQty');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";
						}
					},
					dataIndex : 'purQty'
				},
				// 单位
				{
					header : "单位",
					width : 100,
					sortable : true,
					dataIndex : 'stockUmName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
								return v;
						}
					}
				}, {
					header : "生产厂家",
					width : 100,
					sortable : true,
					dataIndex : 'factory',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}
						}
						// --------------------------------
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}

				}, {
					header : "建议供应商",
					width : 100,
					sortable : true,
					dataIndex : 'supplier',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
								return v;
						}
					}
				},
				// 当前库存
				{
					header : "当前库存",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'tempNum', "");
							}
							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && value != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
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
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('tempNum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'tempNum', totalSum);
							}

							// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来

							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>"
										+ numberFormat(value) + "</font>";
							}
							// --------------------------------

							if (record.get('useFlag') == 'C' && value != null) {
								return "<font color='red'>"
										+ numberFormat(value) + "</font>";
							} else {
								return numberFormat(value);
							}

						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('tempNum');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";
						}
					},
					dataIndex : 'tempNum'

				},
				// // 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
					hidden : true,
					// renderer : function(val) {
					// if (val == "zzfy") {
					// return "生产成本";
					// } else if (val == "lwcb") {
					// return "劳务成本";
					// } else {
					// return "";
					// }// mofify by ywliu 090720
					// },
					dataIndex : 'itemId',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// modify by ywliu 20100225
						// var val = '';
						// if (v == "zzfy") {
						// val = "生产成本";
						// } else if (v == "lwcb") {
						// val = "劳务成本";
						// }
						// if (record.get('useFlag') == 'C' && v != null)
						// return "<font color=red>" + val + "</font>";
						// else
						// return val;
						return getBudgetName(v);
					}
				},
				// 用途
				{
					header : "用途",
					width : 100,
					sortable : true,
					dataIndex : 'usage',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
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
				// }
				},
				// 备注
				{
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'memo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
								return v;
						}
					}
				}, {
					header : "是否作废",
					width : 100,
					sortable : true,
					dataIndex : 'useFlag',
					renderer : function(v) {
						if (v == 'Y')
							return '未作废';
						else if (v == 'C')
							return '已作废';
					},
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty)
									&& record.get('useFlag') == 'Y'
									&& v != null) {
								return "<font color='green'>" + "未作废"
										+ "</font>";
							}
						}
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + "已作废" + "</font>";
						else if (record.get('useFlag') == 'Y' && v != null)
							return "未作废";
					}
				}, {
					header : "作废原因",
					width : 100,
					sortable : true,
					dataIndex : 'cancelReason',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedQty');
							var approvedQty = record.get('approvedQty');
							if ((appliedQty != approvedQty) && v != null
									&& record.get('useFlag') == 'Y') {
								return "<font color='green'>" + v + "</font>";
							}

							// --------------------------------
							if (record.get('useFlag') == 'C' && v != null)
								return "<font color=red>" + v + "</font>";
							else
								return v;
						}
					}
				}],
		tbar : ['本年财务审核领料费用:', txtCheckPrice, '-', '本年财务审核领用费用完成率(%):',
				txtCheckPriceRate, '-', '本年已领费用:', txtFactItem, '-',
				'本年仓库发出费用完成率(%):', txtFactItemRate],
		enableColumnHide : true,
		enableColumnMove : false,
		plugins : expander,
		collapsible : true,
		animCollapse : false
	});
	materialGrid.on("celldblclick", cellClickHandler);
	// ---------add by fyyang 20100419---------------------
	var headTbar = new Ext.Toolbar({
		items : [
				'需求计划物资明细',
				"<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>",
				'颜色说明：',
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:40; color:white; background:red'>已作废</div>",
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:40; color:white; background:green'>申请数量与审核数量不等</div>",
				btnInquire, '-', '本年预算费用:', txtAdviceItem]
	});
	// -----------------------------------------------------------
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

	var sixLine = new Ext.Panel({
		border : false,
		layout : "fit",
		anchor : '100%',
		autoHeight : true,
		items : [firstLine, secondLine]
	});

	var fiveLine = new Ext.Panel({
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		layout : "fit",
		anchor : '100%',
		// autoHeight : true,
		height : 100,
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
			}, {
				border : false,
				columnWidth : 1,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [sixLine]
			}]
		}]
	});

	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 70,
		border : false,
		autoHeight : true,
		region : 'north',
		items : [toolBarTop, fiveLine, thirdLine, headTbar]
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
	txtMrByH.on('render', getUserName(true));

	setTxtMrCostSpecial(0);

	document.getElementById("divManagerDel").onclick = function() {
		if (currentUser == "999999" && event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};

});