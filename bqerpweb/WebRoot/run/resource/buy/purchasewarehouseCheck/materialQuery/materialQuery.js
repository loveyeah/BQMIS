Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
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
		y = d.getDate();
		s += (t > 9 ? "" : "0") + t + "-" + (y > 9 ? "" : "0") + y;
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
		y = d.getDate();
		s += (t > 9 ? "" : "0") + t + "-" + (y > 9 ? "" : "0") + y;
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
			materialStore.load({
						params : {
							headId : record.get('requirementHeadId')
						}
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
				url = application_base_path+"workflow/manager/show/show.jsp?entryId="
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
						.open(application_base_path+"report/webfile/materialRequestReport.jsp?headId="
								+ planId);

			}

		}
	}
	/**
	 * 弹出物料选择窗口
	 */
	function selectMaterial() {
		var mate = window.showModalDialog('../../../plan/RP001.jsp', window,
				'dialogWidth=800px;dialogHeight=550px;status=no');
		if (typeof(mate) != "undefined") {
			// 设置物料名
			materialName.setValue(mate.materialName);
			materialH.setValue(mate.materialNo);
		}
	}
	/**
	 * 查询函数
	 */
	function findFuzzy(start) {
		var startDate = txtMrDateFrom.getValue();
		var endDate = txtMrDateTo.getValue();
		if (start > 0) {
			start = start;
		} else {
			start = 0;
		}
		if (startDate != "" && endDate != "") {
			var startDateInput = startDate.substring(0, 4) + "-"
					+ startDate.substring(5, 7) + "-"
					+ startDate.substring(8, 10);
			var endDateInput = endDate.substring(0, 4) + "-"
					+ endDate.substring(5, 7) + "-" + endDate.substring(8, 10);
			var res = compareDateStr(startDateInput, endDateInput);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

			} else {
				materialStore.baseParams = {
					dateFrom : txtMrDateFrom.getValue(),
					dateTo : txtMrDateTo.getValue(),
					materialNo : materialH.getValue(),
					whsNo : cboDelayStore.getValue(),
					IsPurchasehouse : warehouse.getValue(),
					checkPerson : checkPersonH.getRawValue()
				};
				materialStore.load({
							params : {
								start : start,
								limit : Constants.PAGE_SIZE
							}
						});
			}
		} else {
			materialStore.baseParams = {
				dateFrom : txtMrDateFrom.getValue(),
				dateTo : txtMrDateTo.getValue(),
				materialNo : materialH.getValue(),
				whsNo : cboDelayStore.getValue(),
				IsPurchasehouse : warehouse.getValue(),
				checkPerson : checkPersonH.getValue()
			};
			materialStore.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
		}
	}
	/**
	 * 获取申请人的姓名和工号
	 */
	function getUserName() {
		if (purchaser.getValue() == "" && txtMrByH.getValue() == "") {
			Ext.Ajax.request({
						url : 'resource/getInfo.action',
						method : 'post',
						success : function(action) {
							var result = eval("(" + action.responseText + ")");
							if (result != "" && result != null) {
								purchaser.setValue(result.workerName);
								txtMrByH.setValue(result.workerCode);
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
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			purchaser.setValue(person.workerName);
			txtMrByH.setValue(person.workerCode);
		}
	}
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		Ext.Ajax.request({
			params : {
				dateFrom : txtMrDateFrom.getValue(),
				dateTo : txtMrDateTo.getValue(),
				materialNo : materialH.getValue(),
				whsNo : cboDelayStore.getValue(),
				IsPurchasehouse : warehouse.getValue(),
				checkPerson : checkPersonH.getRawValue()
			},
			url : 'resource/getArrivalCheckDetailListByMaterial.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				var html = ['<table border=1><tr><th>合同号</th><th>填单日期</th><th>物料编码</th><th>物料名称</th><th>物料规格</th><th>单位</th><th>验收员</th><th>验收时间</th><th>验收员验收意见</th><th>仓库</th><th>供应商</th><th>入库数量</th><th>采购人</th><th>发票号</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					var contractNo = "";
					var entryDate = "";
					var materialNo = "";
					var materialName = "";
					var specNo = "";
					var stockUmIdName = "";
					var operateByName = "";
					var arrivalDetailModifiedDate = "";
					var docNo = "";
					var whsName = "";
					var supplyName = "";
					var recQty = "";
					var buyerName = "";
					var invoiceNo = "";
					if (rc.contractNo != null) {
						contractNo = rc.contractNo;
					}
					if (rc.entryDate != null) {
						entryDate = rc.entryDate;
					}
					if (rc.materialNo != null) {
						materialNo = rc.materialNo;
					}
					if (rc.materialName != null) {
						materialName = rc.materialName;
					}
					if (rc.specNo != null) {
						specNo = rc.specNo;
					}
					if (rc.stockUmIdName != null) {
						stockUmIdName = rc.stockUmIdName;
					}
					if (rc.operateByName != null) {
						operateByName = rc.operateByName;
					}
					if (rc.arrivalDetailModifiedDate != null) {
						arrivalDetailModifiedDate = rc.arrivalDetailModifiedDate;
					}
					if (rc.docNo != null) {
						docNo = rc.docNo;
					}
					if (rc.whsName != null) {
						whsName = rc.whsName;
					}
					if (rc.supplyName != null) {
						supplyName = rc.supplyName;
					}
					if (rc.recQty != null) {
						recQty = rc.recQty;
					}
					if (rc.buyerName != null) {
						buyerName = rc.buyerName;
					}
					if (rc.invoiceNo != null) {
						invoiceNo = rc.invoiceNo;
					}
					html.push('<tr><td>' + contractNo + '</td><td>' + entryDate
							+ '</td><td>' + materialNo + '</td><td>'
							+ materialName + '</td><td>' + specNo + '</td><td>'
							+ stockUmIdName + '</td><td>' + operateByName
							+ '</td><td>' + arrivalDetailModifiedDate
							+ '</td><td>' + docNo + '</td><td>' + whsName
							+ '</td><td>' + supplyName + '</td><td>' + recQty
							+ '</td><td>' + buyerName + '</td><td>' + invoiceNo
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		})
	}

	var toolBarTop = new Ext.Toolbar({
				region : 'north',
				items : [{
							xtype : 'tbfill',
							width : '80%'
						}, new Ext.Toolbar.Button({})]
			});
	// --------工具栏按钮-----结束
	// --------第一行textbox--开始
	// 计划月份
	var txtMrDateFrom = new Ext.form.TextField({
				fieldLabel : '起始日期',
				readOnly : true,
				width : 108,
				id : "mrDateFrom",
				name : "mrDateFrom",
				anchor : '90%',
				style : 'cursor:pointer',
				value : getCurrentDateFrom(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d ',
									dateFmt : 'yyyy-MM-dd ',
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
						'../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			checkPerson.setValue(dept.workerName);
			checkPersonH.setValue(dept.workerCode);
		}
	}
	// 仓库 DataStore
	var dsDelayStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getWarehouseList.action",
				fields : ['whsNo', 'whsName']
			});
	// 仓库
	dsDelayStore.load();
	dsDelayStore.on('load', function() {
				if (dsDelayStore.getTotalCount() > 0) {
					var recordLocation = dsDelayStore.getAt(0);
					dsDelayStore.remove(recordLocation);
					// var record = new Ext.data.Record({
					// whsNo:"",
					// whsName:"&nbsp"
					// })
					// dsDelayStore.insert(0, record);
				}
			})
	// 仓库组合框
	var cboDelayStore = new Ext.form.ComboBox({
				fieldLabel : "仓库",
				name : "delayStore",
				// width : 100,
				anchor : '90%',
				store : dsDelayStore,
				displayField : "whsName",
				valueField : "whsNo",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			});

	var checkPerson = new Ext.form.TriggerField({
				fieldLabel : '验收员',
				// width : 108,
				id : "checkPrson",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'checkPrson',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '90%',
				readOnly : true
			});
	checkPerson.onTriggerClick = choseDept;
	var checkPersonH = new Ext.form.Hidden({
				hiddenName : 'checkPersonH'
			})

	// 查询按钮
	var btnAdd = new Ext.Toolbar.Button({
				text : "查询",
				iconCls : Constants.CLS_QUERY,
				handler : findFuzzy
			});

	var firstLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [txtMrDateFrom]
				},
				// {
				// columnWidth : 0.25,
				// layout : "form",
				// border : false,
				// items : []
				// },
				{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [cboDelayStore]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [checkPerson, checkPersonH]
				}, {
					// width : 125,
					columnWidth : 0.1,
					layout : "form",
					border : false,
					items : [btnAdd]
				}]
	});
	// --------第一行textbox--结束
	// --------第二行textbox--开始
	// 计划日期至
	var txtMrDateTo = new Ext.form.TextField({
				format : 'Y-m',
				fieldLabel : "截止日期",
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 108,
				value : getCurrentDateTo(),
				id : "txtMrDateTo",
				name : "mrDateTo",
				anchor : '90%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});

					}

				}

			});

	var materialName = new Ext.form.TextField({
				fieldLabel : '物料名称',
				readOnly : true,
				maxLength : 100,
				// width : 108,
				id : "materialName",
				name : "materialName",
				anchor : '90%',
				allowBlank : false
			});
	materialName.onClick(selectMaterial);
	var materialH = new Ext.form.Hidden({
				hiddenName : 'materialH'
			})

	var dsDelayStore = new Ext.data.SimpleStore({
				root : 'list',
				// url : "resource/getWarehouseList.action",
				data : [[], ['0', '所有'], ['1', '未入库'], ['2', '已入库']],
				fields : ['whsNo', 'whsName']
			});

	// 入库情况
	var warehouse = new Ext.form.ComboBox({
				fieldLabel : "入库情况",
				name : "warehouse",
				width : 200,
				store : dsDelayStore,
				displayField : "whsName",
				valueField : "whsNo",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			});

	// 导出按钮
	var btnexport = new Ext.Toolbar.Button({
				text : "导出",
				iconCls : 'export',
				handler : myExport
			});

	// 确认按钮
	var btnSure = new Ext.Toolbar.Button({
				text : "确认",
				iconCls : Constants.CLS_OK,
				handler : showEdit
			});

	var secondLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [txtMrDateTo]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [materialName]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false
					// items : [materSize]
			}	,
				// {
				// columnWidth : 0.3,
				// layout : "form",
				// border : false,
				// items : [warehouse]
				// },
				{
					// width : 125,
					columnWidth : 0.1,
					layout : "form",
					border : false,
					items : [btnexport]
				}

		]
	});

	// 物料详细记录
	var material = Ext.data.Record.create([{
				name : 'arrivalNo'
			}, {
				name : 'supplyName'
			}, {
				name : 'purNo'
			}, {
				name : 'contractNo'
			}, {
				name : 'buyerName'
			}, {
				name : 'entryDate'
			}, {
				name : 'invoiceNo'
			}, {
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'specNo'
			}, {
				name : 'recQty'
			}, {
				name : 'className'
			}, {
				name : 'whsName'
			}, {
				name : 'arrivalDetailModifiedDate'
			}, {
				name : 'operateByName'
			}, {
				name : 'stockUmIdName'
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
				url : 'resource/getArrivalCheckDetailListByMaterial.action',
				root : 'root',
				totalProperty : 'total',
				fields : material
			});
	var pageBar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : materialStore,
				displayInfo : true,
				displayMsg : Constants.DISPLAY_MSG,
				emptyMsg : Constants.EMPTY_MSG
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
							header : "合同号",
							width : 100,
							sortable : true,
							dataIndex : 'contractNo'
						}, {
							header : "填单日期",
							width : 100,
							sortable : true,
							dataIndex : 'entryDate'
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
							dataIndex : 'specNo'
						},// 申请数量
						// {
						// header : "申请数量",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : numberFormat,
						// dataIndex : 'appliedQty'
						// },// 核准数量
						// {
						// header : "核准数量",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : numberFormat,
						// dataIndex : 'approvedQty'
						// },// 已领数量
						// {
						// header : "已领数量",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : numberFormat,
						// dataIndex : 'issQty'
						// },// 估计单价
						// {
						// header : "估计单价",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : moneyFormat,
						// dataIndex : 'estimatedPrice'
						// },// 估计金额
						// {
						// header : "估计金额",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : moneyFormat,
						// dataIndex : 'estimatedSum'
						// },
						// // 采购数量
						// {
						// header : "采购数量",
						// width : 100,
						// sortable : true,
						// align : 'right',
						// renderer : numberFormat,
						// dataIndex : 'purQty'
						// },
						// 单位
						{
							header : "单位",
							width : 100,
							sortable : true,
							dataIndex : 'stockUmIdName'
						},
						// 需求日期
						{
							header : "验收员",
							width : 100,
							sortable : true,
							dataIndex : 'operateByName'
						},

						// 物料材质
						{
							header : "验收时间",
							width : 100,
							sortable : true,
							dataIndex : 'arrivalDetailModifiedDate'
						},
						// 物料图号
						{
							header : "验收员验收意见",
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
							header : "供应商",
							width : 100,
							sortable : true,
							dataIndex : 'supplyName'
						},
						// 当前库存
						{
							header : "入库数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'recQty'
						},
						// 暂收数量
						{
							header : "采购人",
							width : 100,
							sortable : true,
							align : 'right',
							dataIndex : 'buyerName'
						},
						// 费用来源
						{
							header : "发票号",
							width : 100,
							sortable : true,
							dataIndex : 'invoiceNo'
						}],
				bbar : pageBar
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

	var tableField = new Ext.form.FieldSet({
				autoWidth : false,
				height : 100,
				border : true,
				title : '填写日期',
				layout : "column",
				anchor : '100%',
				style : "padding-top:10;",
				items : [{
							border : false,
							layout : "column",
							columnWidth : 1,
							items : [firstLine]
						}, {
							border : false,
							layout : "column",
							columnWidth : 1,
							items : [secondLine]
						}
				// ,{ border : false,
				// layout : "column", items : [{
				// // columnWidth : 0.145,
				// layout : "column",
				// // hideLabels : true,
				// border : false,
				// items : [secondLine2]}]
				//							
				// }
				]

			});

	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		border : false,
		// autoHeight : true,
		height : 150,
		region : 'north',
		items : [toolBarTop, {
			border : false,
			style : "padding-top:;padding-bottom:;padding-right:20;padding-left:20;",
			items : [tableField]
		}]
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

	materialStore.baseParams = {
		dateFrom : txtMrDateFrom.getValue(),
		dateTo : txtMrDateTo.getValue(),
		materialNo : materialH.getValue(),
		whsNo : cboDelayStore.getValue(),
		IsPurchasehouse : warehouse.getValue(),
		checkPerson : checkPersonH.getValue()
	};
	materialStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

});