Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

// 对应变更
function numberFormat(value) {
	if (value === "") {
		return value
	}
	value = String(value);
	// 整数部分
	var whole = value;
	// 小数部分
	var sub = ".00";
	// 如果有小数
	if (value.indexOf(".") > 0) {
		whole = value.substring(0, value.indexOf("."));
		sub = value.substring(value.indexOf("."), value.length);
		sub = sub + "00";
		if (sub.length > 3) {
			sub = sub.substring(0, 3);
		}
	}
	var r = /(\d+)(\d{3})/;
	while (r.test(whole)) {
		whole = whole.replace(r, '$1' + ',' + '$2');
	}
	v = whole + sub;
	return v;
}
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
		},
		doRender : function(cs, rs, ds, startRow, colCount, stripe) {
			var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
					- 1;
			var tstyle = 'width:' + this.getTotalWidth() + ';';
			// buffers
			var buf = [], cb, c, p = {}, rp = {
				tstyle : tstyle
			}, r;
			for (var j = 0, len = rs.length; j < len; j++) {
				r = rs[j];
				cb = [];
				var rowIndex = (j + startRow);
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					// 判断是否是最后行
					if (r.data["isNewRecord"] == "total") {
						// 替换掉其中的背景颜色
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else {
						// 引用原样式
						p.style = c.style;
					};
					if (p.value == undefined || p.value === "")
						p.value = "&#160;";
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
				var alt = [];
				if (stripe && ((rowIndex + 1) % 2 == 0)) {
					alt[0] = "x-grid3-row-alt";
				}
				if (r.dirty) {
					alt[1] = " x-grid3-dirty-row";
				}
				rp.cols = colCount;
				if (this.getRowClass) {
					alt[2] = this.getRowClass(r, rowIndex, rp, ds);
				}
				rp.alt = alt.join(" ");
				rp.cells = cb.join("");
				buf[buf.length] = rt.apply(rp);
			}
			return buf.join("");
		}
	});

	Ext.QuickTips.init();

	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
	if (v == null || v == "") {
			v = 0;
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
	var objFormDatas;
	// ↓↓*******************采购入库管理**************************************

	// 到货单grid的store
	var arrivalOrderStore = new Ext.data.JsonStore({
		url : 'resource/getArrivalBillList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 到货登记/验收表的流水号
				{
					name : 'id'
				},
				// 到货登记/验收表.单据编号，
				{
					name : 'arrivalNo'
				},
				// 到货登记/验收表.订单编号
				{
					name : 'purNo'
				},
				// 到货登记/验收表.合同编号
				{
					name : 'contractNo'
				},
				// 供应商表.供应商编号
				{
					name : 'supplier'
				},
				// 供应商表.供应商全称
				{
					name : 'supplyName'
				},
				// 到货登记/验收表.操作日期
				{
					name : 'operateDate'
				},// 到货登记/验收表备注
				{
					name : 'memo'
				},// 登陆者名字
				{
					name : 'loginName'
				}, {
					name : 'operateDate2'
				}, {
					name : 'invoiceNo'
				}, {
					name : 'rcvQty'
				}// add by ywliu 20090204
				, {
					name : 'relationArrivalNo'
				}]
			// add by ywliu 20090204
	});

	var confirmBtn = new Ext.Button({
		text : '确认',
		id : 'confirmBtn',
		iconCls : Constants.CLS_OK,
		name : 'confirmBtn',
		// disabled : true,
		handler : function() {
			showRightPurchaseWarehouse();
		}
	});

	var checkBtn = new Ext.Button({
		text : '审核',
		id : 'checkBtn',
		iconCls : Constants.CLS_OK,
		name : 'checkBtn',
		// disabled : true,
		handler : function() {
			checkPurchaseWarehouseInfo();
		}
	});

	// add by drdu 091103
	var cancelBtn = new Ext.Button({
		text : '取消审核',
		id : 'cancelBtn',
		iconCls : Constants.CLS_CANCEL,
		name : 'cancelBtn',
		// disabled : true,
		handler : function() {
			btnCheckCancel();
		}
	});
	arrivalOrderStore.setDefaultSort('arrivalNo', 'DESC');

	arrivalOrderStore.on("load", function() {
		if (arrivalOrderStore.getCount() > 0) {
			confirmBtn.setDisabled(false);
			checkBtn.setDisabled(false);
		} else {
			confirmBtn.setDisabled(true);
			checkBtn.setDisabled(true);
		}

	});
	var smAnnex = new Ext.grid.CheckboxSelectionModel({});

	// add by drdu 091103
	var checkStatus = new Ext.form.ComboBox({
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '未审核'], ['2', '已审核']]
		}),
		id : 'checkStatus',
		name : 'checkStatus',
		valueField : "value",
		displayField : "text",
		value : '1',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		// hiddenName : 'staInfo.checkStatus',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : 100
	});
	// ---------add by fyyang 091215--------------------

	var txtPurNo = new Ext.form.TextField({
		id : 'txtPurNo',
		name : 'txtPurNo',
		fieldLabel : '采购单号',
		width : 80
	});
	var txtClient = new Ext.form.TextField({
		id : 'txtClient',
		name : 'txtClient',
		fieldLabel : '供应商',
		width : 80
	});

	// -------------------------------------------------

	// ---------------add by fyyang ---------------
	var txtConNo = new Ext.form.TextField({
		id : 'txtConNo',
		name : 'txtConNo',
		fieldLabel : '合同号',
		width : 80
	});
	var txtInvNo = new Ext.form.TextField({
		id : 'txtInvNo',
		name : 'txtInvNo',
		fieldLabel : '发票号',
		width : 80
	});
	function queryArrivalOrder() {
		arrivalOrderStore.baseParams = {
			contractNo : txtConNo.getValue(),
			invoiceNo : txtInvNo.getValue(),
			purNo : txtPurNo.getValue(), // add by fyyang 091215
			client : txtClient.getValue(),
			// modified by liuyi 091126 解决刷新数据错误问题
			checkStatus : checkStatus.getValue()
		};
		arrivalOrderStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			// ,
			// checkStatus : checkStatus.getValue()
			}
		});
		arrivalOrderStore.on("load", check);
		detailPanel.getForm().reset();
		rightStore.removeAll();
	}
	// ------------------------------------------------

	// 到货单grid
	var arrivalOrderGrid = new Ext.grid.GridPanel({
		// renderTo : 'left-div',
		border : true,
		// height:700,
		// width:300,
		sm : new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		}),

		fitToFrame : true,
		store : arrivalOrderStore,
		columns : [smAnnex, new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 到货单号
				{
					header : "采购单号",
					sortable : true,
					width : 120,
					dataIndex : 'purNo'
				},
				// 供应商
				{
					header : "供应商",
					sortable : true,
					width : 100,
					dataIndex : 'supplyName'
				},
				// 日期
				{
					header : "日期",
					sortable : true,
					width : 100,
					dataIndex : 'operateDate'
				}],
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.rcvQty < 0
						&& record.data.relationArrivalNo != ""
						&& record.data.relationArrivalNo != null) {
					return 'x-grid-record-red';
				} else if (record.data.rcvQty > 0
						&& record.data.relationArrivalNo != ""
						&& record.data.relationArrivalNo != null) {
					return 'x-grid-record-blue';
				} else {
					return '';
				}
			}
		},
		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : arrivalOrderStore,
			displayInfo : true,
			displayMsg : '共 {2} 条',
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	// arrivalOrderGrid.render();
	arrivalOrderGrid.on("rowdblclick", showRightPurchaseWarehouse);

	// 事务作用码
	var bussinessCode = new Ext.form.TextField({
		id : "bussinessCode",
		xtype : "textfield",
		fieldLabel : '事务作用码',
		readOnly : true,
		anchor : "100%",
		name : ''
	});

	// 事务作用名称
	var bussinessName = new Ext.form.TextField({
		id : "bussinessName",
		xtype : "textfield",
		fieldLabel : '事务作用名称',
		readOnly : true,
		anchor : "100%",
		name : ''
	});
	// 到货单号
	var arrivalOrderCode = new Ext.form.TextField({
		id : "arrivalOrderCode",
		xtype : "textfield",
		fieldLabel : '到货单号',
		readOnly : true,
		anchor : "100%",
		name : ''
	});
	// 采购单号
	var purchaseOrderCode = new Ext.form.TextField({
		id : "purchaseOrderCode",
		xtype : "textfield",
		fieldLabel : '采购单号',
		readOnly : true,
		anchor : "100%",
		name : ''
	});
	// 合同号
	var contactNo = new Ext.form.TextField({
		id : "contactNo",
		xtype : "textfield",
		fieldLabel : '合同号',
		readOnly : true,
		anchor : "100%",
		name : ''
	});

	// 供应商
	var supplyName = new Ext.form.TextField({
		id : "supplyName",
		xtype : "textfield",
		fieldLabel : '供应商',
		readOnly : true,
		anchor : "94.5%",
		name : ''
	});
	// 日期
	var operateDate = new Ext.form.TextField({
		id : "operateDate",
		xtype : "textfield",
		fieldLabel : '日期',
		readOnly : true,
		anchor : "100%",
		name : ''
	});

	// 操作员
	var operator = new Ext.form.TextField({
		id : "operator",
		xtype : "textfield",
		fieldLabel : '采购员',
		readOnly : true,
		anchor : "100%",
		name : ''
	});
	// --------add by fyyang 发票号--------
	var invoiceNo = new Ext.form.TextField({
		id : "invoiceNo",
		xtype : "textfield",
		fieldLabel : '发票号',
		readOnly : true,
		anchor : "94.5%",
		name : ''
	});
	// -----------------------------------

	// 备注
	var detailMemo = new Ext.form.TextArea({
		id : "detailMemo",
		fieldLabel : '备注',
		height : Constants.TEXTAREA_HEIGHT,
		maxLength : 128,
		readOnly : true,
		anchor : "94.5%",
		name : ''
	});
	// 备注
	var detailMemoHidden = new Ext.form.Hidden({
		id : "detailMemoHidden",
		name : 'detailMemoHidden'
	});
	var id = new Ext.form.Hidden({
		id : "id",
		name : 'id',
		value : ''
	});

	var rightData = Ext.data.Record.create([
			// 到货登记/验收表流水号
			{
				name : 'id'
			},
			// 到货登记/验收表.单据编号
			{
				name : 'arrivalNo'
			},
			// 到货登记/验收明细表.备注
			{
				name : 'memo'
			},
			// 到货登记/验收明细表.批次
			{
				name : 'lotCode'
			},
			// 到货登记/验收明细表.已收数量
			{
				name : 'rcvQty'
			},
			// 到货登记/验收明细表.操作人
			{
				name : 'operateBy'
			},
			// 物料主文件.流水号
			{
				name : 'materialId'
			},
			// 物料主文件.编码
			{
				name : 'materialNo'
			},
			// 物料主文件.名称
			{
				name : 'materialName'
			},
			// 物料主文件.是否批控制
			{
				name : 'isLot'
			},
			// 物料主文件.物料分类
			{
				name : 'materialClassId'
			},
			// 物料主文件.计价方式
			{
				name : 'costMethod'
			},
			// 物料主文件.标准成本
			{
				name : 'stdCost'
			},
			// 物料主文件.缺省仓库编码
			{
				name : 'defaultWhsNo'
			},
			// 物料主文件.缺省库位编码
			{
				name : 'defaultLocationNo'
			},// 物料主文件.规格型号
			{
				name : 'specNo'
			},// 物料主文件.存货计量单位
			{
				name : 'stockUmId'
			},// 物料主文件.计划价格
			{
				name : 'frozenCost'
			},// 采购订单明细表.流水号
			{
				name : 'purOrderDetailsId'
			},// 采购订单明细表.采购数量
			{
				name : 'purQty'
			},// 采购订单明细表.已收数
			{
				name : 'purOrderDetailsRcvQty'
			},// 待入库数
			{
				name : 'waitQty'
			},// 本次入库数
			{
				name : 'thisQty'
			},// 采购订单明细表.单价
			{
				name : 'unitPrice'
			},// 采购订单明细表.交期
			{
				name : 'dueDate'
			},// 采购订单明细表.税率
			{
				name : 'taxRate'
			},// 采购订单明细表.币别
			{
				name : 'currencyType'
			},// 采购订单明细表.生产厂家
			{
				name : 'factory'
			},// 保管员
			{
				name : 'saveName'
			},// 仓库编码
			{
				name : 'whsNo'
			},// 仓位编码
			{
				name : 'locationNo'
			},// 备注
			{
				name : 'gridMemo'
			},// 暂收数量
			{
				name : 'insqty'
			}, {
				name : 'arrivalDetailID'
			},// 
			{
				name : 'recQty'
			},// 
			{
				name : 'arrivalDetailModifiedDate'
			},// 
			{
				name : 'orderDetailModifiedDate'
			},// 
			{
				name : 'materialModifiedDate'
			}, {
				name : 'canQty'
			}
			// add by liuyi 091126 单价
			, {
				name : 'unitCost'
			}, {
				// 金额
				name : 'accoutPrice'
			}, {
				// 税额add by drdu 20100407
				name : 'taxCount'
			}]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
		url : 'resource/getArrivalBillDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rightData
	});
	rightStore.setDefaultSort('materialNo', 'ASC');
	rightStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			id : id.getValue()
		});
	});

	// 采购详细列表的store
	var rightStoreCopy = new Ext.data.JsonStore({
		url : 'resource/getArrivalBillDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rightData
	});
	rightStoreCopy.setDefaultSort('materialNo', 'ASC');
	var warehouseStore = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/getWarehouseList.action",
		fields : ['whsNo', 'whsName']
	});
	warehouseStore.load({
		params : {
			flag : 1
		}
	});
	var locationStore = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/getLocationList.action",
		fields : ['locationNo', 'locationName']
	})

	var tempCopy = new Ext.form.Hidden({
		id : "tempCopy",
		name : "tempCopy",
		value : ""
	})

	var mySm = new Ext.grid.CheckboxSelectionModel({});
	// 采购详细列表grid
	var rightGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		style : "border-top:solid 1px",
		// height : 275,
		// anchor : "100%",
		autoScroll : true,
		enableColumnMove : false,
		clicksToEdit : 1,
		border : false,
		store : rightStore,
		sm : mySm,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),	// 物料编码
				{
					header : "物料编码",
					width : 75,
					sortable : true,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					width : 75,
					sortable : true,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					width : 75,
					sortable : true,
					dataIndex : 'specNo'
				},// 单位
				{
					header : "单位",
					width : 50,
					sortable : true,
					renderer : unitName,
					dataIndex : 'stockUmId'
				},
				// 采购数
				{
					header : "采购数",
					width : 50,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
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
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('purQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					align : 'right',
					dataIndex : 'purQty'
				},
				// 已入库数
				{
					header : "已入库数",
					width : 75,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('purOrderDetailsRcvQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'purOrderDetailsRcvQty', totalSum);
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('purOrderDetailsRcvQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					align : 'right',
					dataIndex : 'purOrderDetailsRcvQty'
				},
				// 待入库数 canQty
				{
					header : "待入库数",
					width : 75,
					hidden : true,
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'waitQty', totalSum);
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					align : 'right',
					dataIndex : 'waitQty'
				// },{
				// header : "可入库数",
				// width : 75,
				// sortable : true,
				// renderer : numberFormat,
				// align : 'right',
				// dataIndex : 'canQty'
				},
				// 本单入库数
				{
					header : "本单入库数",
					width : 85,
					sortable : true,
					hidden : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('rcvQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('rcvQty',
										totalSum);
							}
							return moneyFormat(value);
						} else {
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('rcvQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'rcvQty'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					sortable : true,
					align : 'right',
					dataIndex : 'unitCost'
				},// 金额
				{
					header : "金额",
					width : 75,
					align : 'right',
					sortable : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('accoutPrice');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'accoutPrice', totalSum);
							}
							return moneyFormat(value);
						} else {
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('accoutPrice');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'accoutPrice'
				}, mySm, {//----------add by drdu 20100408-----------------
					header : '税额',
					width : 75,
					sorttable : true,
					dataIndex : 'taxCount',
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
						maxValue : 99999999999.9999,
						minValue : 0,
						decimalPrecision : 4
					}),
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var taxCount = record.data.taxCount;
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('taxCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('taxCount', totalSum);
							}
							
							return moneyFormat(taxCount);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('taxCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					}
				},//------------------------------end ----------------------
				// 批号
				{
					header : "批号",
					width : 50,
					hidden : true,
					sortable : true,
					dataIndex : 'lotCode'
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'whsNo'
				},
				// 库位
				{
					header : "库位",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'locationNo'
				},
				// 交期
				{
					header : "交期",
					width : 80,
					sortable : true,
					dataIndex : 'dueDate'
				},
				// 保管员
				{
					header : "保管员",
					width : 50,
					hidden : true,
					sortable : true,
					dataIndex : 'saveName'
				},
				// 验收员
				{
					header : "验收员",
					width : 50,
					hidden : true,
					sortable : true,
					dataIndex : 'operateBy'
				},
				// 备注
				{
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'gridMemo'
				}],
		autoSizeColumns : true

	});
	rightGrid.on('beforeedit', checkIsEdit);

	// rightGrid.on("cellclick", cellClickHandler);

	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		maxLength : 128,
		width : 180
	});

	// 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var rec = rightGrid.selModel.getSelectedCell();
				var record = rightGrid.getStore().getAt(rec[0]);
				// BG eBT_PowrERP_UTBUG_RS001_026
				if (Ext.get("memoText").dom.value.length <= 128) {

					record.set("gridMemo", Ext.get("memoText").dom.value);
					win.hide();
				}
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
	// 		
	var fieldFlag = new Ext.form.Hidden({
		id : "fieldFlag",
		name : "fieldFlag",
		value : ""
	});
	var whsNoFlag = new Ext.form.Hidden({
		id : "whsNoFlag",
		name : "whsNoFlag"
	});
	var locationNoFlag = new Ext.form.Hidden({
		id : "locationNoFlag",
		name : "locationNoFlag"
	});
	var arrivalDetailIDFlag = new Ext.form.Hidden({
		id : "arrivalDetailIDFlag",
		name : "arrivalDetailIDFlag"
	});
	rightGrid.on("afteredit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		fieldFlag.setValue(field);
		whsNoFlag.setValue(record.get('whsNo'));
		locationNoFlag.setValue(record.get("locationNo"));
		arrivalDetailIDFlag.setValue(record.get("arrivalDetailID"));
		if (field == 'whsNo') {
			var tempStr = tempCopy.getValue();

			var materialId = record.get('materialId');

			var column = record.get('whsNo');
			record.set("saveName", getSaveName(column));
			locationStore.load({
				params : {
					whsNo : column
				}
			});
			locationStore.on("load", function() {

				if (fieldFlag.getValue() == "whsNo") {
					if (locationStore.getCount() > 0) {

						locationNoFlag
								.setValue(locationStore.getAt(0).data.locationNo);
					}
				}
				tempSaveValue(tempCopy.getValue(), arrivalDetailIDFlag
						.getValue(), whsNoFlag.getValue(), locationNoFlag
						.getValue());

				var tempArr = tempCopy.getValue().split(",");
				for (var i = 0; i < rightStore.getCount() - 1; i++) {// 统计行
																		// add
																		// by
																		// ywliu
																		// 090723

					var array = isExist(tempCopy.getValue(), rightStore
							.getAt(i).data.arrivalDetailID);

					if (array.length == 2) {
						if (array[0]) {

							rightStore.getAt(i).set("whsNo",
									tempArr[array[1] + 1]);
							rightStore.getAt(i).set("locationNo",
									tempArr[array[1] + 2]);
						}
					}
				}

			});

		}
		if (field == "locationNo") {

			tempSaveValue(tempCopy.getValue(), arrivalDetailIDFlag.getValue(),
					whsNoFlag.getValue(), locationNoFlag.getValue());

			var tempArr = tempCopy.getValue().split(",");
			for (var i = 0; i < rightStore.getCount() - 1; i++) {// 统计行 add
																	// by ywliu
																	// 090723

				var array = isExist(tempCopy.getValue(),
						rightStore.getAt(i).data.arrivalDetailID);

				if (array.length == 2) {
					if (array[0]) {

						rightStore.getAt(i).set("whsNo", tempArr[array[1] + 1]);
						rightStore.getAt(i).set("locationNo",
								tempArr[array[1] + 2]);
					}
				}
			}
		}
		// 入库数的check
		if (field == "thisQty") {/*
									 * var MESSAGE1 ="本次入库数不能为负数"; var MESSAGE2
									 * ="入库数量大于已到货数量。"; var thisQty =
									 * record.get("thisQty"); var materialName =
									 * record.get("materialName"); // 暂入库数 var
									 * rcvQty = record.get("rcvQty");
									 * 
									 * var recQty = record.get("recQty");
									 * 
									 * if(parseFloat(thisQty)<0){
									 * 
									 * Ext.Msg.alert(Constants.NOTICE,MESSAGE1);
									 * record.set("thisQty", "0"); } if
									 * (parseFloat(thisQty) >
									 * parseFloat(rcvQty)-parseFloat(recQty)) {
									 * 
									 * Ext.Msg.alert(Constants.NOTICE,materialName+MESSAGE2);
									 * record.set("thisQty", "0");
									 *  }
									 */
		}

	});
	// 编辑前的操作，仓库编辑前combox数据应该重新加载
	rightGrid.on("beforeedit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		var column = record.get('whsNo');

		fieldFlag.setValue(field);
		whsNoFlag.setValue(record.get('whsNo'));
		locationNoFlag.setValue(record.get("locationNo"));
		arrivalDetailIDFlag.setValue(record.get("arrivalDetailID"));
		if (field == "locationNo") {
			if (column == '') {
				obj.cancel = true;
			} else {
				locationStore.load({
					params : {
						whsNo : column
					}
				});

			}

		}

	});

	// 物资详细grid双击处理
	function cellClickHandler(/* grid, rowIndex, columnIndex, e */) {
		/*
		 * var record = grid.getStore().getAt(rowIndex); var fieldName =
		 * grid.getColumnModel().getDataIndex(columnIndex); if (fieldName ==
		 * "gridMemo"){ win.show(); memoText.setValue(record.get("gridMemo")); };
		 */
		var rec = rightGrid.selModel.getSelectedCell();
		var record = rightGrid.getStore().getAt(rec[0]);
		win.show();
		memoText.setValue(record.get("gridMemo"));
	}
	/**
	 * 把修改的值保存起来
	 */
	function tempSaveValue(tempStr, materialId, whsNo, locationNo) {
		var tempArr = tempStr.split(",");
		var tempFlag = false;
		var k = 0;
		if (tempArr.length > 1) {
			for (var i = 0; i < tempArr.length; i++) {
				if (tempArr[i] == materialId) {
					k = i;
					tempFlag = true;
					break;

				} else {
					i = i + 2;
				}
			}

			if (tempFlag) {
				// 修改已经存在的数据
				tempArr[k] = materialId;
				tempArr[k + 1] = whsNo;
				tempArr[k + 2] = locationNo;
				tempCopy.setValue(tempArr.toString());

			} else {
				// 添加数据
				tempStr = tempStr + "," + materialId + "," + whsNo + ","
						+ locationNo;
				tempCopy.setValue(tempStr);

			}
		} else {

			tempCopy.setValue(materialId + "," + whsNo + "," + locationNo);

		}

	}
	/**
	 * 判断这条记录是否存在
	 */
	function isExist(tempStr, arrivalDetailID) {
		var tempArr = tempStr.split(",");
		var tempFlag = false;
		var k = 0;
		if (tempArr.length > 1) {

			for (var i = 0; i < tempArr.length; i++) {

				if (tempArr[i] == arrivalDetailID) {
					k = i;
					tempFlag = true;
					break;

				} else {
					i = i + 2;
				}
			}
			if (tempFlag) {
				return [true, k];
			} else {
				return [false, 0];
			}

		} else {
			return [false, 0];
		}

	}

	/**
	 * 获取保管员
	 */
	function getSaveName(whsNo) {
		var url = "resource/getWarehouseSaveName.action?whsNo=" + whsNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
	}

	/**
	 * 格式化数据
	 */
	function unitName(value) {
		if (value !== null && value !== "") {
			var url = "resource/getRS001UnitName.action?unitCode=" + value;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			return conn.responseText;
		} else {
			return "";
		}

	};

	var headForm = new Ext.form.FieldSet({
		border : false,
		labelAlign : 'right',
		style : {
			'border' : 0
		},
		layout : 'form',
		labelWidth : 90,
		width : 550,
		autoHeight : true,
		// BG RS001-002
		items : [{
			layout : "column",
			style : "padding-top:20px",
			border : false,
			items : [
					// {
					// columnWidth : 0.45,
					// layout : "form",
					// border : false,
					// height : 30,
					// labelAlign : "right",
					// items : [arrivalOrderCode]
					// },
					{
						columnWidth : 0.45,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [purchaseOrderCode]
					}, {
						columnWidth : 0.45,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [contactNo]
					}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [operateDate]
			}, {
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [operator]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.947,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [supplyName]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.947,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [invoiceNo]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.947,
				layout : "form",
				border : false,
				height : 80,
				labelAlign : "right",
				items : [detailMemo]
			}]
		}]

	});

	var headPanel = new Ext.Panel({
		region : 'north',
		bodyStyle : 'display:none',
		tbar : ['到货单号', txtConNo, '-', '发票号', txtInvNo, '-', '采购单号', txtPurNo,
				'-', '供应商', txtClient, '-', '审核状态：', checkStatus, '-', {
					text : '查询',
					iconCls : 'query',
					handler : queryArrivalOrder
				}, "-", checkBtn, "-", cancelBtn, '-', {
					text : '查看需求计划物资信息',
					handler : function() {
						var sm = arrivalOrderGrid.getSelectionModel();
						var selected = sm.getSelections();
						var ids = [];
						if (selected.length == 0) {
							Ext.Msg.alert("提示", "请选择要查看的记录！");
						} else if (selected.length > 1) {
							Ext.Msg.alert("提示", "请选择一条记录！");
						} else {

							var url = "../issueCheck/planMaterialQuery.jsp";
							var args = new Object();
							args.orderNo = selected[0].data.purNo;
							args.flag = "1";
							var location = window
									.showModalDialog(
											url,
											args,
											'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

						}

					}
				}],
		height : 20
	});
	var detailPanel = new Ext.FormPanel({
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});
	var right = new Ext.Panel({
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'border',
		items : [{
			region : 'north',
			height : 250,
			autoScroll : true,
			items : [detailPanel]
		}, {
			region : 'center',
			layout : 'fit',
			autoScroll : true,
			items : [rightGrid]
		}]
	});

	new Ext.Viewport({
		layout : "border",
		items : [headPanel, {
			region : 'west',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			collapsible : true,
			split : true,
			width : 400,
			autoScroll : true,
			items : [{
				border : false,
				autoScroll : true,
				layout : 'border',
				items : [{
					region : 'center',
					layout : 'fit',
					items : [arrivalOrderGrid]
				}]
			}]
		}, right]
	});

	// ↓↓*******************************处理****************************************
	function addLine() {
		// 统计行
		var record = new rightData({
			id : "",
			arrivalNo : "",
			memo : "",
			lotCode : "",
			rcvQty : "",
			operateBy : "",
			materialId : "",
			materialName : "",
			isLot : "",
			materialClassId : "",
			costMethod : "",
			stdCost : "",
			defaultWhsNo : "",
			purQty : "",
			purOrderDetailsRcvQty : "",
			waitQty : "",
			thisQty : "",
			unitPrice : "",
			dueDate : "",
			taxRate : "",
			stockUmId : "",
			canQty : "",
			isNewRecord : "total"
		});
		// 原数据个数
		var count = rightStore.getCount();
		// 停止原来编辑
		rightGrid.stopEditing();
		// 插入统计行
		rightStore.insert(count, record);
		rightGrid.getView().refresh();
		totalCount = rightStore.getCount() - 1;

	};

	/**
	 * 检查数据是否改变
	 */
	function checkIsChanged() {
		return false;
	}
	/**
	 * 查询操作
	 */
	function queryRecord() {
		arrivalOrderStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		tempCopy.setValue("");
		/*
		 * id.setValue(""); detailPanel.getForm().reset();
		 * rightStore.removeAll(); rightGrid.getBottomToolbar().updateInfo();
		 */
	}
	/**
	 * 获取当前日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;

	}
	/**
	 * 获取事务名称
	 */
	function getTranctionName() {
		Ext.lib.Ajax.request('POST', 'resource/getTransName.action', {
			success : function(action) {

				bussinessName.setValue(action.responseText);
			}
		}, "transCode=" + 'P');
	}

	/**
	 * 双击或点击确认按钮处理
	 */
	function showRightPurchaseWarehouse() {
		if (rightGridDetailIsChanged()) {

			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							showRightGrid();
						}
					});
		} else {
			showRightGrid();
		}
		// 显示详细信息
		function showRightGrid() {
			Ext.Msg.wait("正在查询数据，请等待...");
			if (arrivalOrderGrid.selModel.hasSelection()) {

				var record = arrivalOrderGrid.getSelectionModel().getSelected();
				// 事务作用码
				bussinessCode.setValue('P');
				// 事务作用名称
				getTranctionName();
				// 到货单号
				// arrivalOrderCode.setValue(record.data.arrivalNo);
				// 采购单号
				purchaseOrderCode.setValue(record.data.purNo);
				// 合同号
				contactNo.setValue(record.data.contractNo);
				// 供应商
				supplyName.setValue(record.data.supplyName);
				// 日期
				operateDate.setValue(getCurrentDate());
				// 操作员
				operator.setValue(record.data.loginName);
				// 发票号
				invoiceNo.setValue(record.data.invoiceNo);
				if (record.data.memo == null) {
					detailMemo.setValue("");
					detailMemoHidden.setValue("");
				} else {
					// 备注
					detailMemo.setValue(record.data.memo);
					// 备注保存
					detailMemoHidden.setValue(record.data.memo);
				}
				// 流水号
				id.setValue(record.data.id);
				purchaseOrderDetail(record.data.id);
				tempCopy.setValue("");

			} else {

				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			}
		}

	}
	/**
	 * 显示详细入库信息
	 */
	function purchaseOrderDetail(id) {
		rightStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE,
				id : id
			},
			callback : addLine
		});

	}

	rightStore.on("load", function() {
		Ext.Msg.hide();
	});

	/**
	 * 详细列表的grid内容是否有变更
	 */
	function rightGridDetailIsChanged() {

		if (id.getValue() === "") {
			return false;
		}
		if (rightStore.getCount() - 1 == 0 && detailMemo.getValue() == "") {// 统计行
																			// add
																			// by
																			// ywliu
																			// 090723
			return false;
		} else {
		}

	}

	/**
	 * 备注是否被修改
	 */
	function detailMemoIsChanged() {
		if (detailMemoHidden.getValue() == detailMemo.getValue()) {
			return 0;
		} else {
			return 1;
		}
	}

	function checkPurchaseWarehouseInfo() {
		rightGrid.stopEditing();
		var sm = arrivalOrderGrid.getSelectionModel();
		var selected = sm.getSelections();
		var modifyRec = arrivalOrderGrid.getStore().getModifiedRecords();
		var updateData = new Array();
		var updateDetailData = new Array();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要审核的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member) {
					var data = [];
					updateData.push(member.purNo);
				}
			}
			// add by drdu 20100408 修改税额-------------
			for (var i = 0; i < rightGrid.getStore().getCount() - 1; i++) {
				var rec = rightGrid.getStore().getAt(i);
				if (rightStore.getAt(rightStore.getCount() - 1) != 'isNewRecord') {
					updateDetailData.push({
						id : rec.get("id"),
						tc : rec.get("taxCount")
					});
				}
			}
			// ----------------------------------
			Ext.Msg.confirm("审核", "是否确定审核？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'resource/checkArrivalBillList.action',
						method : 'post',
						params : {
							checkData : Ext.util.JSON.encode(updateData),
							updateDetailData : Ext.util.JSON.encode(updateDetailData)// add by drdu 20100408
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', "审核成功！");
							// add by fyyang 090706 审核成功后刷新
							// queryArrivalOrder();
							arrivalOrderStore.reload();
							detailPanel.getForm().reset();
							rightStore.removeAll();

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
				}
			});
		}
	}

	// add by drdu 091103
	function btnCheckCancel() {
		var sm = arrivalOrderGrid.getSelectionModel();
		var selected = sm.getSelections();
		var modifyRec = arrivalOrderGrid.getStore().getModifiedRecords();
		var updateData = new Array();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要取消审核的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member) {
					var data = [];
					updateData.push(member.purNo);
				}
			}
			Ext.Msg.confirm("取消审核", "是否确定取消审核？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'resource/cancelCheckArrivalBillList.action',
						method : 'post',
						params : {
							checkData : Ext.util.JSON.encode(updateData)
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', "取消审核成功！");
							// 取消审核成功后刷新
							// queryArrivalOrder();
							arrivalOrderStore.reload();
							detailPanel.getForm().reset();
							rightStore.removeAll();

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
				}
			});
		}
	}
	// add by drdu 091103
	function check() {
		var status;
		status = checkStatus.getValue();
		if (status == '1') {
			Ext.get('checkBtn').dom.disabled = false
			Ext.get('cancelBtn').dom.disabled = true
		} else if (status == '2') {
			Ext.get('checkBtn').dom.disabled = true
			Ext.get('cancelBtn').dom.disabled = false
		}
	}

	/**
	 * 判断是否是最后行，如果是则不能编辑
	 */
	function checkIsEdit(obj) {

		if (obj.row == rightStore.getCount() - 1) {

			obj.cancel = true;
		}
	}

	queryArrivalOrder();

})
