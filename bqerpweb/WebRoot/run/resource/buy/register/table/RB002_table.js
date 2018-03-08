Ext.onReady(function() {
	// 页面跳转时使用
	var register = parent.Ext.getCmp('tabPanel').register;
	// 设置刷新采购单列表的监听器
	register.refreshListHandler = refreshAllData;

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;

	// ********** 主画面******* //
	var wd = 180;

	// 模糊查询输入框
	var tfFuuzy = new Ext.form.TextField({
				id : "tfFuuzy",
				width : wd + 200,
				hideLabel : true,
				emptyText : '采购订单号/合同编号/供应商编号/供应商名称/采购员编号/采购员姓名'
			});

	// head工具栏
	var headTbar = new Ext.Toolbar({
				region : 'north',
				items : [tfFuuzy, '-', {
							id : 'btnQuery',
							text : "模糊查询",
							iconCls : Constants.CLS_QUERY,
							handler : gridFresh
						}, '-', {
							id : 'btnSubmit',
							text : "确认",
							iconCls : Constants.CLS_OK,
							handler : editOrder
						}]
			});

	// 是否显示所有采购单
	var ckbShowAll = new Ext.form.Checkbox({
				id : 'ckbShowAll',
				boxLabel : "显示所有采购单",
				width : wd,
				hideLabel : true
			})

	// grid工具栏
	var gridTbar = new Ext.Toolbar({
				items : [ckbShowAll]
			});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// grid中的数据Record
	var gridRecord = new Ext.data.Record.create([{
				// 采购单流水号
				name : 'orderId'
			}, {
				// 采购单编号
				name : 'purNo'
			}, {
				// 采购单状态
				name : 'purStatus'
			}, {
				// 人员名称
				name : 'buyerName'
			}, {
				// 合同号
				name : 'contractNo'
			}, {
				// 供应商ID
				name : 'supplier'
			}, {
				// 供应商编号
				name : 'supplierNo'
			}, {
				// 供应商名称
				name : 'supplyName'
			}, {
				// 采购员
				name : 'buyer'
			}, {
				// 备注
				name : 'orderMemo'
			}, {
				// 税率
				name : 'orderTaxRate'
			}, {
				// 汇率
				name : 'rate'
			}, {
				// 交期
				name : 'orderDueDate'
			}, {
				// 币别
				name : 'currencyId'
			}, {
				// 采购订单表.上次修改日期
				name : 'orderModifyDate'
			}, {
				//发票号
				name : 'invoiceNo'
			}]);

	// grid的store
	var queryStore = new Ext.data.JsonStore({
				url : 'resource/getRegisterOrderList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : gridRecord
			});
	queryStore.on('load', function(store, records) {
				var diabledFlag = !records || records.length < 1;
				Ext.getCmp('btnSubmit').setDisabled(diabledFlag);
			});

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : PAGE_SIZE,
				store : queryStore,
				displayInfo : true,
				displayMsg : Constants.DISPLAY_MSG,
				emptyMsg : Constants.EMPTY_MSG
			});

	// 页面的Grid主体
	var gridOrder = new Ext.grid.GridPanel({
				region : 'center',
				store : queryStore,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "采购单编号",
							sortable : true,
							width : 100,
							dataIndex : 'purNo'
						}, {
							header : "合同号",
							sortable : true,
							width : 100,
							dataIndex : 'contractNo'
						}, {
							header : "供应商编号",
							sortable : true,
							width : 90,
							dataIndex : 'supplierNo'
						}, {
							header : "供应商名称",
							sortable : true,
							width : 100,
							dataIndex : 'supplyName'
						}, {
							header : "采购员",
							sortable : true,
							width : 80,
							dataIndex : 'buyerName'
						}, {
							header : "备注",
							sortable : true,
							width : 270,
							dataIndex : 'orderMemo'
						}, {
							// 税率
							hidden : true,
							dataIndex : 'orderTaxRate'
						}, {
							// 汇率
							hidden : true,
							dataIndex : 'rate'
						}, {
							// 交期
							hidden : true,
							dataIndex : 'orderDueDate'
						}, {
							// 币别
							hidden : true,
							dataIndex : 'currencyId'
						}, {
							// 采购单流水号
							header : "采购单流水号",
							hidden : true,
							dataIndex : 'orderId'
						}, {
							// 采购单状态
							header : "状态",
							// hidden : true,
							dataIndex : 'purStatus',
							renderer : function(value) {
								if (value == "0")
									return "未发送";
								else if (value == "2")
									return "已发送";
								else
									return value;
							}
						}, {
							// 人员Id
							hidden : true,
							dataIndex : 'buyer'
						}, {
							// 供应商Id
							hidden : true,
							dataIndex : 'supplier'
						}, {
							// 采购订单表.上次修改日期
							hidden : true,
							dataIndex : 'orderModifyDate'
						}],
				tbar : gridTbar,
				bbar : pagebar,
				sm : sm,
				frame : false,
				border : false,
				enableColumnMove : false
			});
	gridOrder.on('rowdblclick', editOrder);
	gridOrder.on('render', function() {
				// 初始化Grid
				gridFresh();
			});

	// 主框架
	new Ext.Viewport({
				layout : "border",
				autoHeight : true,
				items : [headTbar, gridOrder]
			});
	// ↑↑********** 主画面*******↑↑//

	// ↓↓*********处理***********↓↓//
	// 编辑Grid
	function editOrder(grid, rowIndex) {
		var records = gridOrder.getSelectionModel().getSelections();
		if (!records || records.length < 1) {
			// 如果没有选择显示提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return;
		}
		var record = records[0];
		if (typeof rowIndex == 'number') {
			record = grid.getStore().getAt(rowIndex);
		}
		// 编辑采购单记录
		register.edit(record.data);
	}

	function tempRefreshFun() {
		queryStore.un('load', tempRefreshFun);

		var record = null;
		for (var i = queryStore.getCount() - 1; i >= 0; i--) {
			record = queryStore.getAt(i).data;
			if (record.orderId == register.orderId) {
				break;
			}
		}

		if (record && record.orderId == register.orderId) {
			// 编辑采购单记录
			register.edit(record, true);
		} else {
			// 通过采购单流水号得到采购单
			Ext.Ajax.request({
						method : Constants.POST,
						url : 'resource/getRegisterOrderById.action',
						success : function(result, request) {
							if (result.responseText) {
								record = eval('(' + result.responseText + ')');
								// 编辑采购单记录
								register.edit(record, true);
							}
						},
						params : {
							orderId : register.orderId
						}
					});
		}
	}

	// 刷新画面数据
	function refreshAllData() {
		if (register.orderId) {
			queryStore.on('load', tempRefreshFun);
		}
		// 重新加载Grid中的数据
		queryStore.reload();
	}

	// 根据条件加载Grid
	function gridFresh() {
		queryStore.baseParams = {
			fuzzy : tfFuuzy.getValue(),
			isShowAll : ckbShowAll.checked
		};
		queryStore.load({
					params : {
						start : 0,
						limit : PAGE_SIZE
					}
				});
	}

	// 重新加载Grid中的数据
	function gridReload() {
		queryStore.reload();
	}
		// ↑↑*********处理***********↑↑//
	});