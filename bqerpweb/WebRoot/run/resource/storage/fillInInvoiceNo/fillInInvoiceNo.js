Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

// 对应变更
function numberFormat(v) {
	// modified by liuyi 091209
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

	var objFormDatas;
	// ↓↓*******************补录发票管理**************************************
	// 起始时间选择器
	// var fromDate = new Ext.form.DateField({
	// id : 'beginDate',
	// name : 'beginDate',
	// // format : 'Y-m-d',
	// width : 85,
	// allowBlank : false,
	// readOnly : true,
	// value : getCurrentDateFrom(),
	// fieldLabel : '起始时间'
	// });
	//
	//    
	// //结束时间选择器
	// var toDate = new Ext.form.DateField({
	// id : 'endDate',
	// name : 'endDate',
	// format : 'Y-m-d',
	// width : 85,
	// allowBlank : false,
	// readOnly : true,
	// value : getCurrentDateTo(),
	// fieldLabel : '结束时间'
	// });

	// =============采购员 add by drdu 091124=====================
	var txtBuyer = new Ext.form.TextField({
				name : 'txtBuyer',
				fieldLabel : '采购员'
			});
	function chosebuyer() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../comm/purchaserForSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtBuyer.setValue(dept.names);
			txtBuyerHide.setValue(dept.codes);

			queryRecord();
		}
	}
	
	

	var txtBuyer = new Ext.form.TriggerField({
				fieldLabel : '采购员',
				id : "mytxtBuyer",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'txtBuyer',
				blankText : '请选择',
				emptyText : '请选择',
				width : 80,
				onTriggerClick : function(e) {
					if(currentCode=="999999")
						{
					if (!this.disabled) {
						
						chosebuyer();
						
					}
					this.blur();
				}
				}
			});
	var txtBuyerHide = new Ext.form.Hidden({
				hiddenName : 'txtBuyerHide'
			})

	// ================ end =====================

	// 到货单grid的store
	var arrivalOrderStore = new Ext.data.JsonStore({
				url : 'resource/getArrivalListByNoInvoiceNo.action',
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
						}, {
							name : 'buyerName'
						} // 采购员 add by fyyang 091123
				]
			});

	arrivalOrderStore.setDefaultSort('arrivalNo', 'DESC');// 将升序显示ASC改为降序DESC显示
	// modify by ywliu
	// 2009/7/2

	// --------add by fyyang 20100113-------------------------------
	var txtPurNo = new Ext.form.TextField({
				name : 'purNo',
				fieldLabel : '采购单号',
				width : 80
			});

	var txtSupplyName = new Ext.form.TextField({
				name : 'supplyName',
				fieldLabel : '供应商'
			});

	var txtMaterialName = new Ext.form.TextField({
				name : 'materialName',
				fieldLabel : '物资名称'
			});
	// --------------------------------------------------------------------

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
							hidden : true,
							dataIndex : 'arrivalNo'
						}, {
							header : "采购单号",
							sortable : true,
							width : 100,
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
							width : 130,
							dataIndex : 'operateDate'
						}, {
							header : "采购员",
							sortable : true,
							width : 100,
							hidden : true,
							dataIndex : 'buyerName'

						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [
						// {text : '起始时间'},
						// fromDate,
						// { text : '结束时间'},
						// toDate,'-',
						'采购单号：', txtPurNo, '-', '供应商：', txtSupplyName, '-',
						'采购员', txtBuyer],
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

	// // 事务作用码
	// var bussinessCode = new Ext.form.TextField({
	// id : "bussinessCode",
	// xtype : "textfield",
	// fieldLabel : '事务作用码',
	// readOnly : true,
	// anchor : "100%",
	// hidden:true,
	// hideLabel:true,
	// name : ''
	// });

	// 事务作用名称
	// var bussinessName = new Ext.form.TextField({
	// id : "bussinessName",
	// xtype : "textfield",
	// fieldLabel : '事务作用名称',
	// readOnly : true,
	// anchor : "100%",
	// hidden:true,
	// hideLabel:true,
	// name : ''
	// });
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
				maxLength : 200, // modify by drdu 091124
				anchor : "94.5%",
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
				fieldLabel : '采购员',
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
			// {
			// name : 'locationNo'
			// },// 备注
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
			}]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
				// modified by liuyi 091209
				// url : 'resource/getArrivalDetailListByNoInvoiceNo.action',
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
				// modified by liuyi 091209
				// url : 'resource/getArrivalDetailListByNoInvoiceNo.action',
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
							dataIndex : 'materialNo',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						},
						// 物料名称
						{
							header : "物料名称",
							width : 75,
							sortable : true,
							dataIndex : 'materialName',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						},
						// 规格型号
						{
							header : "规格型号",
							width : 75,
							sortable : true,
							dataIndex : 'specNo',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						},// 单位
						{
							header : "单位",
							width : 50,
							sortable : true,
							// renderer : unitName,//modify by drdu 20100428
							dataIndex : 'stockUmId',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + unitName(v) + "</font>";// modify by drdu 20100428
								else
									return unitName(v);// modify by drdu 20100428
							}
						},
						// 采购数
						{
							header : "采购数",
							width : 50,
							sortable : true,
							editor : new Ext.form.NumberField({
										maxValue : 99999999999.9999,
										minValue : 0,
										decimalPrecision : 4
									}),
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('purQty');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'purQty', totalSum);
									}
									// update by sychen 20100427
									if (record.get('purQty') != record
											.get('purOrderDetailsRcvQty')
											&& value != null) {

										return "<font color=blue>"
												+ numberFormat(value)
												+ "</font>";
									} else
										return numberFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('purQty');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
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
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
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
												'purOrderDetailsRcvQty',
												totalSum);
									}
									// update by sychen 20100427
									if (record.get('purQty') != record
											.get('purOrderDetailsRcvQty')
											&& value != null)
										return "<font color=blue>"
												+ numberFormat(value)
												+ "</font>";
									else
										return numberFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('purOrderDetailsRcvQty');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
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
							sortable : true,
							hidden : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('waitQty');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'waitQty', totalSum);
									}
									return numberFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('waitQty');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
											+ "</font>";
								}
							},
							align : 'right',
							dataIndex : 'waitQty'
						}, {
							header : "可入库数",
							width : 75,
							sortable : true,
							hidden : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('canQty');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'canQty', totalSum);
									}
									return numberFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('canQty');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
											+ "</font>";
								}
							},
							align : 'right',
							dataIndex : 'canQty'
						},
						// 本次入库数
						{
							header : "本次入库数<font color='red'>*</font>",
							width : 85,
							sortable : true,
							align : 'right',
							hidden : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('thisQty');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'thisQty', totalSum);
									}
									return numberFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('thisQty');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
											+ "</font>";
								}
							},
							dataIndex : 'thisQty'
						},
						// 单价
						{
							header : "单价",
							width : 75,
							sortable : true,
							align : 'right',
							dataIndex : 'unitCost',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						},// 金额
						{
							header : "金额",
							width : 75,
							align : 'right',
							sortable : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('accoutPrice');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'accoutPrice', totalSum);
									}
									// update by sychen 20100427
									if (record.get('purQty') != record
											.get('purOrderDetailsRcvQty')
											&& value != null)
										return "<font color=blue>"
												+ numberFormat(value)
												+ "</font>";
									else
										return numberFormat(value);
								} else {
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('accoutPrice');
									}
									return "<font color='red'>"
											+ numberFormat(totalSum)
											+ "</font>";
								}
							},
							dataIndex : 'accoutPrice'
						},
						// 批号
						{
							header : "批号",
							width : 50,
							sortable : true,
							hidden : true,
							dataIndex : 'lotCode'
						},
						// 仓库
						{
							header : "仓库",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'whsNo'
						},
						// // 库位
						// {
						// header : "库位",
						// width : 100,
						// sortable : true,
						// dataIndex : 'locationNo'
						// },
						// 交期
						{
							header : "交期",
							width : 80,
							sortable : true,
							dataIndex : 'dueDate',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						},
						// 保管员
						{
							header : "保管员",
							width : 50,
							sortable : true,
							hidden : true,
							dataIndex : 'saveName'
						},
						// 验收员
						{
							header : "验收员",
							width : 50,
							sortable : true,
							hidden : true,
							renderer : getOperateByName,
							dataIndex : 'operateBy'
						},
						// 备注
						{
							header : "备注",
							width : 100,
							sortable : true,
							dataIndex : 'memo',
							// add by sychen 20100427
							renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.get('purQty') != record
										.get('purOrderDetailsRcvQty')
										&& v != null)
									return "<font color=blue>" + v + "</font>";
								else
									return v;
							}
						}],
					tbar : [{//add by drdu 20100428
					text : '刷新',
					iconCls : Constants.CLS_QUERY,
					handler : freshRecord
					}],
				// bbar : new Ext.PagingToolbar({
				// pageSize : Constants.PAGE_SIZE,
				// store : rightStore,
				// displayInfo : true,
				// displayMsg : Constants.DISPLAY_MSG,
				// emptyMsg : Constants.EMPTY_MSG
				// }),
				autoSizeColumns : true

			});
			
	//add by drdu 20100428		
	function freshRecord() {
		showRightPurchaseWarehouse();
	}
	// rightGrid.on("cellclick", cellClickHandler);

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
								.getValue(), whsNoFlag.getValue(),
						locationNoFlag.getValue());

				var tempArr = tempCopy.getValue().split(",");
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
		// -----add by sychen 20100427------------
		if (field == "purQty") {
			if (record.get("purQty") == record.get("purOrderDetailsRcvQty")) {
				return false;
			}
		}
			// -----add by end--------------------
		});

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
				items : [
						// {
						// layout : "column",
						// style : "padding-top:5px",
						// border : false,
						// items : [
						// {
						// columnWidth : 0.45,
						// layout : "form",
						// border : false,
						// height : 30,
						// labelAlign : "right",
						// items : [bussinessCode]
						// }, {
						// columnWidth : 0.45,
						// layout : "form",
						// border : false,
						// height : 30,
						// labelAlign : "right",
						// items : [bussinessName]
						// }]
						// },
						{
					layout : "column",
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
				width : 465,
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
				autoScroll : true,
				layout : 'border',
				border : true,
				items : [detailPanel, rightGrid],
				tbar : ['物资名称：', txtMaterialName, '-',

				{
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : function() {
								queryRecord();
							}
						}, '-', {
							text : "补录发票",
							iconCls : Constants.CLS_SAVE,
							handler : function() {
								confirmPurchaseWarehouse();
							}
						}]

			});
	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [leftPanel, rightPanel]
			})


	/**
	 * 检查数据是否改变
	 */
	function checkIsChanged() {
		return false;
	}

	/**
	 * 获取当前前一月的日期
	 */
	function getCurrentDateFrom() {
		var d, s, t;
		d = new Date();
		var day = d.getDate();
		d.setDate(d.getDate() - 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s += '-';
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * 获取当前后一月的日期
	 */
	function getCurrentDateTo() {
		var d, s, t;
		d = new Date();
		var day = d.getDate();
		d.setDate(d.getDate() + 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s += '-';
		s += (day > 9 ? "" : "0") + day;
		return s;
	}

	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}

	/**
	 * 查询操作
	 */
	function queryRecord() {
		// var startDate = fromDate.getValue();
		// var endDate = toDate.getValue();
		// if (startDate != "" && endDate != "") {
		// var res = compareDate(startDate, endDate);
		// if (res) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
		// Constants.COM_E_006, "起始时间", "结束时间"));
		//
		// }
		// else {
		arrivalOrderStore.baseParams = {
			// startDate : Ext.get("beginDate").dom.value,
			// endDate : Ext.get("endDate").dom.value,
			buyer : txtBuyer.getValue(),// add by drdu 091124
			purNo : txtPurNo.getValue(),
			supplyName : txtSupplyName.getValue(),
			materialName : txtMaterialName.getValue()
		};
		arrivalOrderStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		tempCopy.setValue("");
		// }
		// }
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
	// function getTranctionName() {
	// Ext.lib.Ajax.request('POST', 'resource/getTransName.action', {
	// success : function(action) {
	//						
	// bussinessName.setValue(action.responseText);
	// }
	// }, "transCode=" + 'P');
	// }
	/**
	 * 双击或点击确认按钮处理
	 */
	function showRightPurchaseWarehouse() {
		if (invoiceNo.getValue() != null && invoiceNo.getValue() != "") {
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
				// bussinessCode.setValue('P');
				// 事务作用名称
				// getTranctionName();
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
					},
					callback : addLine
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
			});

	function confirmPurchaseWarehouse() {
		var updateData = new Array();
		// add by sychen 20100427 修改采购数量-------------
		// update by ltong 20100428----------
		var modi = rightStore.getModifiedRecords();
		if (modi.length == 0
				|| (modi.length == 1 && (modi[0].get('purOrderDetailsId') == null || modi[0]
						.get('purOrderDetailsId') == ''))) {
			Ext.Msg.alert('提示', "没有数据修改！");
			return;
		}

		for (var i = 0; i < modi.length; i++) {
			var rec = modi[i];
			if (rec.get('purOrderDetailsId') != null
					&& rec.get('purOrderDetailsId') != '') {
				updateData.push({
							id : rec.get("purOrderDetailsId"),
							purQty : rec.get("purQty")
						});
			}
		}
		// --------------update by ltong-------end------
// alert(Ext.util.JSON.encode(updateData))
// return;
		// --------------------------------------------
		if (invoiceNo.getValue() != null && invoiceNo.getValue() != "") {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								method : 'POST',
								url : 'resource/updateArrivalHead.action',
								params : {
									id : id.getValue(),
									invoiceNo : Ext.get("invoiceNo").dom.value,
									updateData : Ext.util.JSON
											.encode(updateData)
									// add by sychen 20100427
								},
								success : function(action) {
									var o = eval('(' + action.responseText
											+ ')');
									if (o.flag == "1") {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												"补录成功！");
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
										tempCopy.setValue("");
										id.setValue("");
									}
								}
							});
						}

					});
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, "请填写发票号！");
		}
	}
	
	//add by fyyang 20100430
	txtBuyer.setValue(currentName);
	txtBuyerHide.setValue(currentCode);
	
		queryRecord();

})
