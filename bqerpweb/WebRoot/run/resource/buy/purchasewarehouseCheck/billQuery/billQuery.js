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
	/**
	 * 获取当前月的日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		y = d.getDay()+1;
		s += (t > 9 ? "" : "0") + t +(y > 9 ? "" : "0")+ y;
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
		y = d.getDay()+1;
		s += (t > 9 ? "" : "0") + t +"-"+(y > 9 ? "" : "0")+ y;
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
		y = d.getDay()+1;
		s += (t > 9 ? "" : "0") + t +"-"+(y > 9 ? "" : "0")+ y;
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
		var date1 = Date.parseDate(argDateStr1.toString(), 'Y-m-d');
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
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (headGrid.getSelectionModel().getSelected() != null) {
			var record = headGrid.getSelectionModel().getSelected();
			materialStore.load({
				params : {
					headId : record.get('arrivalNo')
				},
				callback : addLine
			});
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
					+ endDate.substring(5, 7) + "-"
					+ endDate.substring(8, 10);		
			var res = compareDateStr(startDateInput, endDateInput);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

			} else {
				var IsPurchasehouse;
				if(radio1.getValue()){
					IsPurchasehouse = 1;
				} else if(radio2.getValue()){
					IsPurchasehouse = 0;
				}
				
					headStore.baseParams = {
					dateFrom : txtMrDateFrom.getValue(),
					dateTo : txtMrDateTo.getValue(),
					checkPerson : checkPersonH.getValue(),
					purchaser : purchaserH.getValue(),
					supplierH : supplierH.getValue(),
					IsPurchasehouse : IsPurchasehouse,
					arrialNo:txtArrialNo.getValue()
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
			
		} else {
			headStore.baseParams = {
				dateFrom : txtMrDateFrom.getValue(),
				dateTo : txtMrDateTo.getValue(),
				checkPerson : checkPersonH.getValue(),
				purchaser : purchaserH.getValue(),
				supplierH : supplierH.getValue(),
				arrialNo:txtArrialNo.getValue()
			};
			headStore.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
			// 清空明细部分gird
			materialStore.removeAll();
		}
	}
	
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
						'../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			checkPerson.setValue(person.workerName);
			checkPersonH.setValue(person.workerCode);
		}
	}

	function selectSupplierWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var supplier1 = window
				.showModalDialog(
						'../../../../../comm/jsp/supplierQuery/supplierQuery.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(supplier1) != "undefined") {
			supplier2.setValue(supplier1.supplyName);
			supplierH.setValue(supplier1.supplierId);
		}
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
						'purchaserForSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			purchaser.setValue(dept.names);
			purchaserH.setValue(dept.codes);
		}
	}
	// 采购员
	var purchaser = new Ext.form.TriggerField({
				fieldLabel : '采购员',
				// width : 108,
				id : "purchaser",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '90%',
				readOnly : true
			});
	purchaser.onTriggerClick = choseDept;
	var purchaserH = new Ext.form.Hidden({
				hiddenName : 'mrDept'
			})

	var checkPerson = new Ext.form.TriggerField({
				fieldLabel : '验收员',
				// width : 108,
				id : "checkPerson",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '90%',
				readOnly : true
			});
	checkPerson.onTriggerClick = selectPersonWin;
	var checkPersonH = new Ext.form.Hidden({
				hiddenName : 'checkPersonCode'
			})
//-----------add by fyyang 20100113---------------
				var txtArrialNo = new Ext.form.TextField({
				fieldLabel : '到货单号',
				maxLength : 100,
				id : "txtArrialNo",
				name : "arrialNo",
				anchor : '90%'
			});
//-------------------------------------------------------			
			
	// 申请人
	var supplier2 = new Ext.form.TextField({
				fieldLabel : '供应商',
				readOnly : true,
				maxLength : 100,
				// width : 108,
				id : "supplier2",
				name : "mrBy",
				anchor : '90%'
			});
	supplier2.onClick(selectSupplierWin);
	var supplierH = new Ext.form.Hidden();
	// 查询按钮
	var btnAdd = new Ext.Toolbar.Button({
				text : "查询",
				iconCls : Constants.CLS_QUERY,
				handler : findFuzzy
			});

	var radio1 = new Ext.form.Radio({
				boxLabel : '已入库',
				hideLabel : true,
				id : 'radio1',
				name : 'actionId',
				checked : true,
				listeners : {
					check : function(radio, checked) {
					}
				}
			});

	var radio2 = new Ext.form.Radio({
				boxLabel : '未入库',
				hideLabel : true,
				id : 'radio2',
				name : 'actionId',
				checked : true,
				listeners : {
					check : function(radio, checked) {
					}
				}
			});
	// Ext.getCmp("approve-radio").hidden=true;
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
				}, {
					columnWidth : 0.26,
					layout : "form",
					border : false,
					items : [checkPerson, checkPersonH]
				}, {
					columnWidth : 0.26,
					layout : "form",
					border : false,
					items : [purchaser, purchaserH]
				}, 
					{
					columnWidth : 0.03,
					layout : "form",
					border : false
				}, 
					{
					columnWidth : 0.15,
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
									startDate : '%y-%M-%d ',
									dateFmt : 'yyyy-MM-dd ',
									alwaysUseStartDate : false
								});

					}

				}

			});

	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
				text : "清除条件",
				iconCls : Constants,
				handler : function() {
//					txtMrDateFrom.setValue("");
//					txtMrDateTo.setValue("");
//					checkPerson.setValue("");
//					purchaser.setValue("");
//					supplier2.setValue("");
//					checkPersonH.setValue("");
//					purchaserH.setValue("");
//					supplierH.setValue("");
				}
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
					columnWidth : 0.26,
					layout : "form",
					border : false,
					items : [supplier2,supplierH]
				}, 
				{
					columnWidth : 0.26,
					layout : "form",
					border : false,
					items : [txtArrialNo]
				}, 
	           {
					columnWidth : 0.03,
					layout : "form",
					border : false
				},
					{
					columnWidth : 0.15,
					layout : "form",
					border : false,
					items : [btnFind]
				}

		]
	});

	// -----------------------
	// --------第二行textbox--结束
	// 主表一览行数据定义
	var headInfo = Ext.data.Record.create([{
				// ID
				name : 'arrivalDate'
			}, {// 工作流状态
				name : 'arrivalNo'
			}, {// 申请单编号
				name : 'buyerName'
			}, {// 申请部门
				name : 'contractNo'
			}, {// 申请人
				name : 'entryDate'
			}, {// 申请理由
				name : 'id'
			}, {// 申请日期
				name : 'invoiceNo'
			}, {// 物料类别
				name : 'loginName'
			},{// 物料类别
				name : 'memo'
			},{// 物料类别
				name : 'operateDate'
			},{// 物料类别
				name : 'operateDate2'
			},{// 物料类别
				name : 'purNo'
			},{// 物料类别
				name : 'supplier'
			},{// 物料类别
				name : 'supplyName'
			}])
	// --------第三行grid--开始
	// 主表一览的store
	var headStore = new Ext.data.JsonStore({
				url : 'resource/getArrivalCheckList.action',
				root : 'root',
				totalProperty : 'total',
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
//				region : "center",
//				layout : 'fit',
				height : 200,
//				autoSizeColumns : true,
				border : false,
//				enableColumnMove : false,
				// 单选
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				store : headStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 50
								}),
						{
							header : "填写日期",
							width : 80,
							sortable : true,
							dataIndex : 'entryDate'
						},
						{
							header : "采购员",
							width : 70,
							sortable : true,
							dataIndex : 'buyerName'
						},
						{
							header : "凭证编号",
							dataIndex : "arrivalNo",
							sortable : true,
							width : 150
						},
//						{
//							header : "入库时间",
//							width : 100,
//							sortable : true,
//							dataIndex : 'arrivalDate'
//						},
						{
							header : "供应商名称",
							width : 100,
							sortable : true,
							dataIndex : 'supplyName'
						},
						// 申请日期
						{
							header : "采购单编号",
							width : 100,
							sortable : true,
							dataIndex : 'purNo'
						},
						// 物料类别
						{
							header : "合同凭证",
							width : 100,
							sortable : true,
							dataIndex : 'contractNo'
						},{
							header : "发票号",
							width : 100,
							sortable : true,
							dataIndex : 'invoiceNo'
						}],
				// 分页
				bbar : pageBar
			});

	headGrid.on('rowclick', showDetail);

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

	// 物料详细记录
	var material = Ext.data.Record.create([{
				name : 'requirementDetailId'
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
				url : 'resource/getArrivalCheckDetailList.action',
				root : 'root',
				totalProperty : 'total',
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
//						
//						{
//							header : "估计单价",
//							width : 100,
//							sortable : true,
//							align : 'right',
//							renderer : moneyFormat,
//							dataIndex : 'estimatedPrice'
//						},// 估计金额
//						{
//							header : "估计金额",
//							width : 100,
//							sortable : true,
//							align : 'right',
//							renderer : moneyFormat,
//							dataIndex : 'estimatedSum'
//						},
						// 采购数量
						{
							header : "入库数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('recQty');
									}
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('recQty',
												totalSum);
									}
									return moneyFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('recQty');
									}
									return "<font color='red'>" + moneyFormat(totalSum)
											+ "</font>";
								}
							},
							dataIndex : 'recQty'
						},
						// 单位
						{
							header : "单位",
							width : 100,
							sortable : true,
							dataIndex : 'stockUmIdName'
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
							header : "物料类别",
							width : 100,
							sortable : true,
							dataIndex : 'className'
						},
						
						// 仓库
						{
							header : "仓库",
							width : 100,
							sortable : true,
							dataIndex : 'whsName'
						},// 质量等级
						{
							header : "验收员",
							width : 100,
							sortable : true,
							dataIndex : 'operateByName'
						},
						// 当前库存
						{
							header : "验收员验收核对时间",
							width : 150,
							sortable : true,
							align : 'right',
							dataIndex : 'arrivalDetailModifiedDate'
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
				autoHeight : true
				,
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
		height:350,
//		autoHeight : true,
		region : 'north',
		items : [toolBarTop, {
			border : false,
			style : "padding-top:;padding-bottom:;padding-right:20;padding-left:20;",
			items : [tableField]
		}, thirdLine]
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
	var IsPurchasehouse;
	if(radio1.getValue()){
		IsPurchasehouse = 1;
	} else if(radio2.getValue()){
		IsPurchasehouse = 0;
	}		
	headStore.baseParams = {
			dateFrom : txtMrDateFrom.getValue(),
			dateTo : txtMrDateTo.getValue(),
			checkPerson : checkPersonH.getValue(),
			purchaser : purchaserH.getValue(),
			supplierH : supplierH.getValue(),
			IsPurchasehouse : IsPurchasehouse,
			arrialNo:txtArrialNo.getValue()
	};
	headStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 清空明细部分gird
	materialStore.removeAll();
	
	function addLine() {
		// 统计行
		var record = new material({
			requirementDetailId : "",
			materialNo : "",
			materialName : "",
			specNo : "",
			recQty : "",
			className : "",
			whsName : "",
			arrivalDetailModifiedDate : "",
			operateByName : "",
			stockUmIdName : "",
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

});