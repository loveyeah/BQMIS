Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// ----------------add-----------------------
Ext.override(Ext.grid.GridView, {
	doRender : function(cs, rs, ds, startRow, colCount, stripe) {
		var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount - 1;
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
				// 判断是否是统计行
				if (r.data["materialNo"] == null
						|| r.data["materialNo"] == "undefined") {
					// 替换掉其中的背景颜色
					p.style = c.style.replace(/background\s*:\s*[^;]*;/, '');
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
// ------------------------------------------
// 对应变更
function numberFormat(v) {
	// modify by fyyang 091109 数量改为小数点后4位
	// if(value===""){
	// return value
	// }
	// value = String(value);
	// // 整数部分
	// var whole = value;
	// // 小数部分
	// var sub = ".00";
	// // 如果有小数
	// if (value.indexOf(".") > 0) {
	// whole = value.substring(0, value.indexOf("."));
	// sub = value.substring(value.indexOf("."), value.length);
	// sub = sub + "00";
	// if(sub.length > 3){
	// sub = sub.substring(0,3);
	// }
	// }
	// var r = /(\d+)(\d{3})/;
	// while (r.test(whole)){
	// whole = whole.replace(r, '$1' + ',' + '$2');
	// }
	// v = whole + sub;
	// return v;
	if (v == null || v == "") {
		return "0.0000";
	}
	v = (Math.round((v - 0) * 10000)) / 10000;
	v = (v == Math.floor(v)) ? v + ".0000" : ((v * 10 == Math.floor(v * 10))
			? v + "000"
			: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v * 1000 == Math
					.floor(v * 1000)) ? v + "0" : v)));
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
Ext.onReady(function() {
	Ext.QuickTips.init();

	// ↓↓*******************收料单报表所需数据**************************************
	var birtReceiveDate = "";
	var birtArrivalNo = "";
	var birtSupplyName = "";
	var birtContractNo = "";
	var birtPurNo = "";
	var birtWhsNo = new Array();

	var objFormDatas;
	// ↓↓*******************采购入库管理**************************************
	// 查询值
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		value : "",
		maxLength : 30,
		name : "fuzzy",
		emptyText : "到货单号/供应商/物料"
	});

	// 到货单grid的store
	var arrivalOrderStore = new Ext.data.JsonStore({
		url : 'resource/getRS001PurchaseList.action',
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
				}, {// 发票号add by drdu 090618
					name : 'invoiceNo'
				}]
	});
	var confirmBtn = new Ext.Button({
		text : '确认',
		id : 'confirmBtn',
		iconCls : Constants.CLS_OK,
		name : 'confirmBtn',
		disabled : true,
		handler : function() {
			showRightPurchaseWarehouse();
		}
	});
	arrivalOrderStore.setDefaultSort('arrivalNo', 'DESC');// 将升序显示ASC改为降序DESC显示
	// modify by ywliu
	// 2009/7/2
	arrivalOrderStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			fuzzy : fuzzy.getValue()
		});
	});
	arrivalOrderStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	arrivalOrderStore.on("load", function() {
		if (arrivalOrderStore.getCount() > 0) {
			confirmBtn.setDisabled(false);
		} else {
			confirmBtn.setDisabled(true);

		}

	});

	// 到货单grid
	var arrivalOrderGrid = new Ext.grid.GridPanel({
		// renderTo : 'left-div',
		border : true,
		// height:700,
		// width:300,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),

		fitToFrame : true,
		store : arrivalOrderStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 到货单号
				{
					header : "到货单号",
					sortable : true,
					width : 120,
					dataIndex : 'arrivalNo'
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
			forceFit : true
		},
		tbar : [fuzzy, "-", {
			text : "模糊查询",
			iconCls : Constants.CLS_QUERY,
			handler : function() {/*
			 * if (rightGridDetailIsChanged()) { //
			 * Ext.Msg.confirm("提示信息", "放弃已修改的内容吗？",
			 * function(buttonobj) { if (buttonobj ==
			 * "yes") { queryRecord(); } }); } else {
			 * queryRecord(); }
			 * 
			 */
				queryRecord();
			}
		}],// , "-",confirmBtn 隐藏页面的“确认”按钮 modify by ywliu 090717
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

	// 发票号 add by drdu 090618
	var invoiceNo = new Ext.form.TextField({
		id : "invoiceNo",
		fieldLabel : '发票号',
		readOnly : false,
		anchor : "100%",
		name : ''
	});
	// 发票号 add by drdu 090618
	var invoiceNoHidden = new Ext.form.Hidden({
		id : "invoiceNoHidden",
		name : 'invoiceNoHidden'
	});

	// 供应商
	var supplyName = new Ext.form.TextField({
		id : "supplyName",
		xtype : "textfield",
		fieldLabel : '供应商',
		readOnly : true,
		anchor : "100%",
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
		fieldLabel : '操作员',
		readOnly : true,
		anchor : "100%",
		name : ''
	});

	// 备注
	var detailMemo = new Ext.form.TextArea({
		id : "detailMemo",
		fieldLabel : '备注',
		height : Constants.TEXTAREA_HEIGHT,
		maxLength : 128,
		readOnly : false,
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
			},{
				name : 'sbMemo' // add by liuyi 20100406 需求备注
			},{
				name : 'sbDeptName' // add by liuyi 20100406 申报部门
			},{
				name : 'planOriginalId' // add by liuyi 20100430 需求计划来源
			}
			]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
		url : 'resource/getRS001PurchaseDetailList.action',
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
		url : 'resource/getRS001PurchaseDetailList.action',
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
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 物料编码
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
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'purQty'
				},
				// 已入库数
				{
					header : "已入库数",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'purOrderDetailsRcvQty'
				},
				// 待入库数 canQty
				{
					header : "待入库数",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'waitQty'
				}, {
					header : "可入库数",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'canQty'
				},
				// 本次入库数
				{
					header : "本次入库数<font color='red'>*</font>",
					width : 85,
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					align : 'right',
					editor : new Ext.form.NumberField({
						decimalPrecision : 4

					}),
					renderer : numberFormat,
					dataIndex : 'thisQty'
				},
				// 批号
				{
					header : "批号",
					width : 50,
					sortable : true,
					dataIndex : 'lotCode'
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					renderer : comboBoxWarehouseRenderer,
					editor : new Ext.form.ComboBox({
						store : warehouseStore,
						displayField : "whsName",
						valueField : "whsNo",
						mode : 'local',

						triggerAction : 'all',
						readOnly : true

					}),
					dataIndex : 'whsNo'
				},
				// 库位
				{
					header : "库位",
					width : 100,
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					renderer : comboBoxLocationRenderer,
					editor : new Ext.form.ComboBox({
						store : locationStore,
						displayField : "locationName",
						valueField : "locationNo",
						mode : 'local',

						triggerAction : 'all',

						readOnly : true
					// width : 80
					}),
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
					sortable : true,
					dataIndex : 'saveName'
				},
				// 验收员
				{
					header : "验收员",
					width : 50,
					sortable : true,
					renderer : getOperateByName,
					dataIndex : 'operateBy'
				},
				// 备注
				{
					header : "备注",
					width : 100,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.TextArea({
						maxLength : 128,
						listeners : {
							"render" : function() {
								this.el.on("dblclick", function() {
									var rec = rightGrid.selModel
											.getSelectedCell();
									var record = rightGrid.getStore()
											.getAt(rec[0]);
									var value = record.get('gridMemo');
									memoText.setValue(value);
									win.show();
								})
							}
						}
					}),
					sortable : true,
					dataIndex : 'gridMemo'
				}
				// add by liuyi 20100406 增加需求备注和申报部门列
				,{
					header : '需求备注',
					dataIndex : 'sbMemo'
				},{
					header : '申报部门',
					dataIndex : 'sbDeptName'
				}
				],
		// bbar : new Ext.PagingToolbar({
		// pageSize : Constants.PAGE_SIZE,
		// store : rightStore,
		// displayInfo : true,
		// displayMsg : Constants.DISPLAY_MSG,
		// emptyMsg : Constants.EMPTY_MSG
		// }),
		autoSizeColumns : true

	});
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
				// for (var i = 0; i < rightStore.getCount(); i++) { modify by
				// fyyang 090724 去掉总计行
				for (var i = 0; i < rightStore.getCount() - 1; i++) {

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
			// for (var i = 0; i < rightStore.getCount(); i++) { modify by
			// fyyang 090724 去掉总计行
			for (var i = 0; i < rightStore.getCount() - 1; i++) {

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
		 * record.set("thisQty", "0"); }
		 */
		}
		// ------------add by fyyang 090723 合计行计算------
		if (field == "thisQty") {
			var column = record.get('materialNo');
			if (column != null && column != "undefined") {
				var sumdata = 0;
				for (var j = 0; j < rightStore.getCount() - 1; j++) {
					var temp = rightStore.getAt(j);
					if (temp.get("thisQty") != null) {
						sumdata = parseFloat(sumdata)
								+ parseFloat(temp.get("thisQty"));
					}
				}
				rightStore.getAt(rightStore.getCount() - 1).set('thisQty',
						sumdata);
			}
		}
			// -----------------------------------------------

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
		// ---------add by fyyang 090723 合计行不能编辑----------

		if (field == "thisQty" || field == "whsNo" || field == "locationNo"
				|| field == "gridMemo") {
			if (record.get("materialNo") == null
					|| record.get("materialNo") == "undefined") {
				return false;
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
		if (value != null && value != "" && value != "undefined") {
			var url = "resource/getRS001UnitName.action?unitCode=" + value;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			return conn.responseText;
		} else {
			return "";
		}

	};

	function getOperateByName(value) {
		if (value !== null && value !== "") {
			var url = "resource/getRS001OperaterByName.action?operateBy="
					+ value;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			return conn.responseText;
		} else {
			return "";
		}
	}
	/**
	 * 格式化数据
	 */
	function comboBoxWarehouseRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];

		var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	};
	/**
	 * 格式化数据
	 */
	function comboBoxLocationRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];
		var locationNo = record.data["locationNo"];
		var url = "resource/getLocationName.action?whsNo=" + whsNo
				+ "&locationNo=" + locationNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	}

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
			style : "padding-top:5px",
			border : false,
			items : [{
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [bussinessCode]
			}, {
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [bussinessName]
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
				items : [arrivalOrderCode]
			}, {
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [purchaseOrderCode]
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
				items : [contactNo]
			}, {
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [supplyName]
			}]
		}, {	// add by drdu 090618
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [invoiceNo]
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
				columnWidth : 0.45,
				layout : "form",
				border : false,
				height : 30,
				labelAlign : "right",
				items : [operateDate]
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

	// 明细panel
	var detailPanel = new Ext.FormPanel({
		border : false,
		region : 'north',
		height : 220,
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});

	var leftPanel = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : 300,
		autoScroll : true,
		border : false,
		containerScroll : true,
		collapsible : true,
		split : false,
		items : [arrivalOrderGrid]
	});
	// 右边的panel
	var rightPanel = new Ext.Panel({
		region : "center",
		// autoScroll:true,
		layout : 'border',
		border : true,
		items : [detailPanel, rightGrid],
		tbar : [{
			text : "确认入库",
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				confirmPurchaseWarehouse();
			}
		}, {
			text : '入库回滚',
			iconCls : Constants.CLS_SAVE,
			handler : cancelPurchaseWarehouse
		}, {	// add by drdu 091120
			text : '补打印',
			iconCls : 'print',
			handler : printReport
		}]

	});
	// 显示区域
	var view = new Ext.Viewport({
		enableTabScroll : true,
		autoScroll : true,
		layout : "border",
		items : [leftPanel, rightPanel]
	})

	// ↓↓*******************************处理****************************************

	// add by drdu 091120
	function printReport() {
		window.location
				.replace("../purchasewarehouse/afterPrintPurchasewarehouse.jsp");
	}

	function cancelPurchaseWarehouse() {

		// 确认是否正在结帐
		Ext.lib.Ajax.request('POST', 'resource/isIssueBalanceNow.action', {
			success : function(action) {
				if ('true' == action.responseText) {
					// 正在结帐,弹出信息无法进行业务
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_001);
					return;
				} else {
					window.showModalDialog('cancelPurchasewarehouseQuery.jsp',
							null,
							'status:no;dialogWidth=750px;dialogHeight=450px');
					arrivalOrderStore.reload();

				}
			}
		}, "transCode=P");

		// }
	}
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
				arrivalOrderCode.setValue(record.data.arrivalNo);
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

				// 发票号 add by drdu 090618
				if (record.data.invoiceNo == null) {
					invoiceNo.setValue("");
					invoiceNoHidden.setValue("");
				} else {
					invoiceNo.setValue(record.data.invoiceNo);
					invoiceNoHidden.setValue(record.data.invoiceNo);
				}

				// 报表显示数据 addBy ywliu 2009/6/18
				birtReceiveDate = record.data.operateDate;
				birtArrivalNo = record.data.arrivalNo;
				birtPurNo = record.data.purNo;
				birtSupplyName = record.data.supplyName;
				birtContractNo = record.data.contractNo;
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
			}
		});
		rightStoreCopy.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE,
				id : id
			}
		});

	}

	rightStore.on("load", function() {
		Ext.Msg.hide();
		// ----------add by fyyang 090723------加合计行--------
		var totalpurQty = 0; // 采购数
		var totalpurOrderDetailsRcvQty = 0; // 已入库数
		var totalwaitQty = 0; // 待入库数
		var totalcanQty = 0;// 可入库数
		var totalthisQty = 0;// 本次入库数

		for (var j = 0; j < rightStore.getCount(); j++) {
			var temp = rightStore.getAt(j);
			if (temp.get("purQty") != null) {
				totalpurQty = parseFloat(totalpurQty)
						+ parseFloat(temp.get("purQty"));
			}
			if (temp.get("purOrderDetailsRcvQty") != null) {
				totalpurOrderDetailsRcvQty = parseFloat(totalpurOrderDetailsRcvQty)
						+ parseFloat(temp.get("purOrderDetailsRcvQty"));
			}
			if (temp.get("waitQty") != null) {
				totalwaitQty = parseFloat(totalwaitQty)
						+ parseFloat(temp.get("waitQty"));
			}
			if (temp.get("canQty") != null) {
				totalcanQty = parseFloat(totalcanQty)
						+ parseFloat(temp.get("canQty"));
			}

			if (temp.get("thisQty") != null) {
				totalthisQty = parseFloat(totalthisQty)
						+ parseFloat(temp.get("thisQty"));
			}
		}
		var mydata = new rightData({
			purQty : totalpurQty,
			purOrderDetailsRcvQty : totalpurOrderDetailsRcvQty,
			waitQty : totalwaitQty,
			canQty : totalcanQty,
			thisQty : totalthisQty
		});
		if (rightStore.getCount() > 0) {
			rightStore.add(mydata);
		}

			// ---------------------------------------
	});

	function confirmPurchaseWarehouse() {
		var MESSAGE1 = "物料编码";
		var MESSAGE2 = "所在行的仓库不能为空，请选择。";
		var MESSAGE3 = "所在行的库位不能为空，请选择。";
		var MESSAGE4 = "本次入库数不能为空，请输入。";
		var MESSAGE5 = "本次入库数不能为负数。";
		var MESSAGE6 = "所在行的入库数量大于已到货数量。";
		if (id.getValue() === "") {
			return;
		}
		// if(!rightGridDetailIsChanged()){
		//			
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_006);
		// }else{}
		// 将明细是否改变的判断注释掉 modify by ywliu 090721

		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == "yes") {
						// 对应式样变更
						var returnValue = new Array();
						var array = getPurchaseWarehousDetailList();

						if (array.length != 0) {
							returnValue = warehouseAndLocationJudge();

							if (returnValue.length != 0) {

								/*
								 * if(returnValue[0]=="1"){ // COM_E_017
								 * Ext.Msg.alert(Constants.NOTICE,MESSAGE1+returnValue[1]+MESSAGE2);
								 * return ; } if(returnValue[0]=="2"){
								 * Ext.Msg.alert(Constants.NOTICE,MESSAGE1+returnValue[1]+MESSAGE2);
								 * return ; } if(returnValue[0]=="4"){
								 * Ext.Msg.alert(Constants.NOTICE,MESSAGE1+returnValue[1]+MESSAGE3);
								 * return ; }
								 */
								var msg = "";
								for (var i = 0; i < returnValue.length; i++) {
									if (returnValue[i] == '0') {
										msg = msg + MESSAGE4;
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												msg);
										return;
									}
									if (returnValue[i] == '5') {
										msg = msg + MESSAGE5;
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												msg);
										return;
									}
									/*
									 * if(returnValue[i]=='6'){
									 * msg=msg+MESSAGE6;
									 * Ext.Msg.alert(Constants.NOTICE,msg);
									 * return; }
									 */
									i++;
								}
								/*
								 * for(var i = 0; i<returnValue.length;i++){
								 * 
								 * if(returnValue[i]=='5'){ msg = msg +
								 * MESSAGE1+returnValue[i+1]+MESSAGE5+"<br>"; }
								 * i++; } if(msg!==''){
								 * Ext.Msg.alert(Constants.NOTICE,msg); return; }
								 */

								for (var i = 0; i < returnValue.length; i++) {

									if (returnValue[i] == '6') {
										msg = msg + MESSAGE1
												+ returnValue[i + 1] + MESSAGE6
												+ "<br>";
									}
									i++;
								}
								if (msg !== '') {
									Ext.Msg
											.alert(Constants.SYS_REMIND_MSG,
													msg);
									return;
								}
								for (var i = 0; i < returnValue.length; i++) {

									if (returnValue[i] == '1') {
										msg = msg + MESSAGE1
												+ returnValue[i + 1] + MESSAGE2
												+ "<br>";
									}
									if (returnValue[i] == '2') {
										msg = msg + MESSAGE1
												+ returnValue[i + 1] + MESSAGE2
												+ "<br>";
									}
									if (returnValue[i] == "4") {
										msg = msg + MESSAGE1
												+ returnValue[i + 1] + MESSAGE3
												+ "<br>";
									}

									i++;
								}
								if (msg !== '') {
									Ext.Msg
											.alert(Constants.SYS_REMIND_MSG,
													msg);
									return;
								}
							}
						}
						// var array = getPurchaseWarehousDetailList();

						if (array.length != 0 || detailMemoIsChanged() === 1) {
							// BG RS001-027
							if (detailMemo.getValue().length > 128) {
								return;
							}

							// for (var i = 0; i < rightStore.getCount(); i++) {
							// modify by fyyang 090723 去掉合计行
							for (var i = 0; i < rightStore.getCount() - 1; i++) {
								if (rightStore.getAt(i).get("thisQty") == null
										|| rightStore.getAt(i).get("thisQty") == '0') {
									continue;
								}
								var temp = rightStore.getAt(i).data.whsNo;
								var k = 0;
								for (var j = 0; j < birtWhsNo.length; j++) {
									if (temp == birtWhsNo[j]) {
										k = 1;
									}
								}
								if (k == 0) {
									birtWhsNo
											.push(rightStore.getAt(i).data.whsNo);
								}

							}

							Ext.Ajax.request({
								method : 'POST',
								url : 'resource/updatePurchaseWarehouse.action',
								params : {
									data : Ext.util.JSON
											.encode(getPurchaseWarehousDetailList()),
									detailMemo : detailMemo.getValue(),
									memoFlag : detailMemoIsChanged(),
									id : id.getValue(),
									data1 : Ext.util.JSON
											.encode(getPurchaseWarehouseList()),
									invoiceNo : Ext.get("invoiceNo").dom.value
								// add by drdu 090618
								},
								success : function(action) {

									var o = eval('(' + action.responseText
											+ ')');
									if (o.flag == "1") {
										// add by liuyi 20100429
										// 需求计划来源为固定资产类时，有几条物资，打印几条单子
										// modify by liuyi 20100429 去掉合计行

										// add by liuyi 20100430 对应仓库数组下所有的物资
										// 数组中元素为数据
										var allMetailIds = new Array();
										// add by liuyi 20100430
										// 对应仓库数组下所有来源为固定资产类的物资 数组元素为数组
										var gdMetailIds = new Array();
										for (var j = 0; j < birtWhsNo.length; j++) {
											// 一仓库中的所有物资
											var allArr = new Array();
											// 一仓库中的来源为固定资产的物资
											var gdArr = new Array();
											for (var i = 0; i < rightStore
													.getCount()
													- 1; i++) {
												if (rightStore.getAt(i)
														.get("thisQty") == null
														|| rightStore.getAt(i)
																.get("thisQty") == '0') {
													continue;
												}
												var tempWhoNo = rightStore
														.getAt(i).data.whsNo;
												var tempMaterialId = rightStore
														.getAt(i).data.materialId;
												var tempPlanOriginalId = rightStore
														.getAt(i).data.planOriginalId;
												// 右边列表中的store的仓库与 仓库数组中相同
												if(tempWhoNo == birtWhsNo[j]){
													allArr.push(tempMaterialId);
													if(tempPlanOriginalId == 3){
														gdArr.push(tempMaterialId);
													}
												}
											}
											allMetailIds.push(allArr);
											gdMetailIds.push(gdArr);

										}

//										for (var j = 0; j < birtWhsNo.length; j++) {
//											alert(birtWhsNo[j])
//											alert(allMetailIds[j].join(','))
//											alert(gdMetailIds[j].join(','))
//										}
										
										for (var i = 0; i < rightStore
												.getCount()
												- 1; i++) {
											if (rightStore.getAt(i)
													.get("thisQty") == null
													|| rightStore.getAt(i)
															.get("thisQty") == '0') {
												continue;
											}
											var tempPlanOriginalId = rightStore
														.getAt(i).data.planOriginalId;
											if (tempPlanOriginalId == 3) {
												var tempWhoNo = rightStore
														.getAt(i).data.whsNo;
												var tempMaterialId = rightStore
														.getAt(i).data.materialId;
												strReportAdds = "/powerrpt/report/webfile/receiptBillAddOfGd.jsp?purNo="
														+ birtPurNo
														+ "&whsName="
														+ tempWhoNo
														+ "&receiveDate="
														+ birtReceiveDate
														+ "&arrivalNo="
														+ birtArrivalNo
														+ "&materialId="
														+ tempMaterialId;
												if (birtContractNo != null) {
													strReportAdds += "&contractNo="
															+ birtContractNo;
												}
												window
														.open(encodeURI(strReportAdds));
											}
											

										}

										for (var i = 0; i < birtWhsNo.length; i++) {
											var whsNoName = birtWhsNo[i];
											// modified by liuyi 20100430 判断明细物资来源为固定资产类
											var all = allMetailIds[i];
											var gd = gdMetailIds[i];
											if(all.length == gd.length){
												;
											}else{
											strReportAdds = "/powerrpt/report/webfile/receiptBill.jsp?purNo="
													+ birtPurNo
													+ "&whsName="
													+ whsNoName
													+ "&receiveDate="
													+ birtReceiveDate
													+ "&arrivalNo="
													+ birtArrivalNo
													// add by liuyi 20100430 来源为固定资产的类的物资
													+ "&metailIdNotIn=" + gd.join(',')
													;
											// + "&supplier="
											// + birtSupplyName;
											if (birtContractNo != null) {
												strReportAdds += "&contractNo="
														+ birtContractNo;
											}
											window
													.open(encodeURI(strReportAdds));
										}

										}
										birtReceiveDate = "";
										birtArrivalNo = "";
										birtSupplyName = "";
										birtContractNo = "";
										birtPurNo = "";
										birtWhsNo = [];
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												"入库成功！");
										arrivalOrderStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
										detailPanel.getForm().reset();
										rightStore.removeAll();
										// 05/09/09 15:43 分页栏已被注释掉 yiliu
										// rightGrid.getBottomToolbar().updateInfo();
										tempCopy.setValue("");
										id.setValue("");

									} else if (o.flag == "0") {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												o.msg);
									} else {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												o.msg);
										arrivalOrderStore.load({
											params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
										detailPanel.getForm().reset();
										rightStore.removeAll();
										rightGrid.getBottomToolbar()
												.updateInfo();
										tempCopy.setValue("");
										id.setValue("");
									}
								}
								
								
							});
						}
					}

				});

	}
	function getPurchaseWarehousDetailList() {
		var records = new Array();
		// for (var i = 0; i < rightStore.getCount(); i++) { modify by fyyang
		// 090723 去掉统计行
		for (var i = 0; i < rightStore.getCount() - 1; i++) {

			if (rightStore.getAt(i).get("thisQty") == null
					|| rightStore.getAt(i).get("thisQty") == '0') {
				continue;
			}
			records.push(rightStore.getAt(i).data);
			// alert(rightStore.getAt(i).data.whsNo)

		}
		return records;
	}
	function getPurchaseWarehouseList() {
		var records = new Array();
		for (var i = 0; i < arrivalOrderStore.getCount(); i++) {

			if (arrivalOrderStore.getAt(i).get("id") == id.getValue()) {
				records.push(arrivalOrderStore.getAt(i).data);
				break;
			}

		}
		return records;
	}
	/**
	 * 备注,发票号是否被修改 modify by drdu 090618
	 */
	function detailMemoIsChanged() {
		if ((detailMemoHidden.getValue() == detailMemo.getValue())
				&& (invoiceNoHidden.getValue() == invoiceNo.getValue())) {
			return 0;
		} else {
			return 1;
		}
	}
	/**
	 * 详细列表的grid内容是否有变更
	 */
	function rightGridDetailIsChanged() {

		if (id.getValue() === "") {
			return false;
		}
		if (rightStore.getCount() == 0 && detailMemo.getValue() == ""
				&& invoiceNo.getValue() == "") {
			return false;
		} else {

			if ((detailMemo.getValue() != detailMemoHidden.getValue())
					|| (invoiceNo.getValue() != detailMemoHidden.getValue())
					|| ((detailMemo.getValue() != detailMemoHidden.getValue()) && (invoiceNo
							.getValue() != detailMemoHidden.getValue()))) {

				return true;
			} else {
				var flag = false;
				for (var i = 0; i < rightStore.getCount() - 1; i++) { // add
					// by
					// ywliu
					// 090723
					/*
					 * // 判断本次入库数有没有改变 if (!(rightStore.getAt(i).get("thisQty") == "" &&
					 * rightStoreCopy .getAt(i).get("thisQty")==0)) {
					 */

					if (rightStore.getAt(i).get("thisQty") !== rightStoreCopy
							.getAt(i).get("thisQty")) {

						flag = true;
						break;
					}
					/* } */
					// 仓库改变判断
					if (!(rightStore.getAt(i).get("whsNo") == "" && rightStoreCopy
							.getAt(i).get("whsNo") == "")) {
						if (rightStore.getAt(i).get("whsNo") != rightStoreCopy
								.getAt(i).get("whsNo")) {

							flag = true;
							break;
						}
						// alert(rightStore.getAt(i).get("whsNo"));
					}
					// 库位改变判断
					if (!(rightStore.getAt(i).get("locationNo") == "" && rightStoreCopy
							.getAt(i).get("locationNo") == "")) {
						if (rightStore.getAt(i).get("locationNo") != rightStoreCopy
								.getAt(i).get("locationNo")) {

							flag = true;
							break;
						}
					}
					// 备注改变判断
					if (!(rightStore.getAt(i).get("gridMemo") === "" && rightStoreCopy
							.getAt(i).get("gridMemo") === null)) {

						if (rightStore.getAt(i).get("gridMemo") != rightStoreCopy
								.getAt(i).get("gridMemo")) {
							flag = true;
							break;
						}
					}

				}
				return flag;
			}

		}

	}
	/**
	 * 仓库库位的判断
	 */
	function warehouseAndLocationJudge() {
		var whsNo;
		var locationNo;
		var defaultWhsNo;
		var defaultLocationNo;
		var array = [];
		var thisQty;
		// for (var i = 0; i < rightStore.getCount(); i++) { modify by fyyang
		// 090724 去掉总计行
		for (var i = 0; i < rightStore.getCount() - 1; i++) {
			whsNo = rightStore.getAt(i).get("whsNo");
			locationNo = rightStore.getAt(i).get("locationNo");
			defaultWhsNo = rightStore.getAt(i).get("defaultWhsNo");
			defaultLocationNo = rightStore.getAt(i).get("defaultLocationNo");
			thisQty = rightStore.getAt(i).get("thisQty");
			var rcvQty = rightStore.getAt(i).get("rcvQty");

			var recQty = rightStore.getAt(i).get("recQty");
			var materialNo = rightStore.getAt(i).get("materialNo");
			// TODO
			if (thisQty === "") {
				array.push("0");
				array.push(materialNo);
			}

			if (parseFloat(thisQty) < 0) {

				array.push("5");
				array.push(materialNo);
			}
			if (parseFloat(thisQty) > parseFloat(rcvQty) - parseFloat(recQty)) {

				array.push("6");
				array.push(materialNo);

			}

			if (thisQty != "0") {
				if (whsNo === "" && locationNo === "") {

					if (defaultWhsNo == null || defaultWhsNo == '') {
						// 仓库库位都没有选，默认仓库为空
						// return ["1",i+1];
						array.push("1");
						array.push(materialNo);

					} else {
						if (defaultLocationNo == null
								|| defaultLocationNo == '') {
							/*
							 * var url =
							 * "resource/getLocationAllListCount.action?whsNo=" +
							 * whsNo; var conn =
							 * Ext.lib.Ajax.getConnectionObject().conn;
							 * conn.open("POST", url, false); conn.send(null);
							 * var count = conn.responseText;
							 */

							// 对应bug
							var url = "resource/getLocationList.action?whsNo="
									+ defaultWhsNo;
							var conn = Ext.lib.Ajax.getConnectionObject().conn;
							conn.open("POST", url, false);
							conn.send(null);
							var count = eval('(' + conn.responseText + ")");

							if (count.list.length != 0) {
								// 仓库库位都没有选，默认仓库不为空，库位为空
								// return ["2",i+1]
								array.push("2");
								array.push(materialNo);
							}
						}
					}
				}
				if (whsNo !== "") {

					var url = "resource/getLocationList.action?whsNo=" + whsNo;
					var conn = Ext.lib.Ajax.getConnectionObject().conn;
					conn.open("POST", url, false);
					conn.send(null);
					var count = eval('(' + conn.responseText + ')');

					if (count.list.length == 0) {
						// 仓库有值，下拉框没值
						// return ["3",i+1]
						array.push("3");
						array.push(materialNo);
					} else {

						if (locationNo == "" && defaultLocationNo == null) {

							// 仓库有值，下拉框有值，选定的是空，而且默认库位为空
							// return ["4",i+1]
							array.push("4");
							array.push(materialNo);
						}

						if (locationNo == "" && defaultLocationNo != null) {
							if (whsNo != defaultWhsNo) {
								array.push("4");
								array.push(materialNo);
							}
						}
					}
				}
			}
		}

		return array;
	}
})
