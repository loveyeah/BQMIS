Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init(); 
	var itemCode = "";
	var nodeCode = "";
	var apartCode = "";
	var dateType = "";
	var isCorrespond = "";
	 
	var planRecord = Ext.data.Record.create([{
				// 物料编码
				name : 'itemCode'
			}, {
				// 物料ID
				name : 'itemName'
			}, {
				// 计划单号
				name : 'dataTimeType'
			}, {
				// 计划单项次号
				name : 'isCorrespond'
			}, {
				// 核准数量
				name : 'dataTimeType'
			}, {
				// 已采购数
				name : 'nodeCode'
			}, {
				// 待采购数
				name : 'descriptor'
			}, {
				// 是否是其它采购员的需求计划物资
				name : 'nodeName'
			}, {
				// 采购订单与需求计划关联表.上次修改日期
				name : 'apartCode'
			} 
			]);
	// grid列模式
	var planCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '指标编码',
				width : 100,
				dataIndex : 'itemCode',
				sortable : false
			}, {
				header : '指标名称',
				width : 150,
				dataIndex : 'itemName',
				sortable : false
			}, {
				header : '规格型号',
				width : 60,
				hidden : true,
				dataIndex : 'dataTimeType',
				sortable : false
			}, {
				header : '材质/参数',
				width : 60,
				hidden : true,
				dataIndex : 'isCorrespond',
				sortable : false
			}, {
				header : '节点编码',
				dataIndex : 'nodeCode',
				width : 80,
				sortable : true
			}, {
				header : '节点描述',
				dataIndex : 'descriptor',
				width : 100,
				sortable : true
			}, {
				header : '节点名称',
				dataIndex : 'nodeName',
				width : 150,
				sortable : true
			}, {
				header : '单元',
				dataIndex : 'apartCode',
				sortable : true,
				width : 50 
		}]);
	planCM.defaultSortable = true;
	// ↓↓**********************需求计划物资Grid************↓↓//
	// 数据源
	var planStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getStatItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, planRecord)
			}); 
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 20,
				store : planStore,
				displayInfo : true,
				displayMsg : "一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : '共{0}页'
			});

	var argFuzzy = new Ext.form.TextField({
		id : 'argFuzzy',
		fieldLabel : "模糊查询",
		// hideLabel : true,
		emptyText : '',
		name : 'argFuzzy',
		value : '',
		anchor : '100%'
			// width : '100%'
		});

	// grid
	var planGrid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableColumnHide : true,
		ds : planStore,
		cm : planCM,
		height : 600,
		sm : new Ext.grid.RowSelectionModel(),
		border : false,
		// frame : true,
		tbar : ["-", argFuzzy, {
					text : "查询",
					iconCls : 'query',
					handler : queryRecords

				}, {
					text : "对应",
					iconCls : 'save',
					handler : function() {
						if (isCorrespond == "N") {
							var url = "selectDcsNode.jsp?itemCode=" + itemCode;
							var collection = window
									.showModalDialog(
											url,
											'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							Ext.MessageBox.alert('提示信息', "对应成功");
							planStore.reload();
							itemCode = "";
							dateType = "";
							isCorrespond = "";
						} else if (isCorrespond == "Y") {
							Ext.MessageBox.alert('提示信息', '这条记录已经对应过了，不能再进行对应！')
						} else {
							Ext.MessageBox.alert('提示信息', '请选择一行数据！')
						}

					}
				}, {
					text : "取消对应",
					iconCls : 'cancer',
					handler : function() {
						if (isCorrespond == "Y") {
							Ext.Ajax.request({
										url : 'manager/cancelCorrespond.action',
										method : 'post',
										params : {
											itemCode : itemCode
										},
										success : function(result, request) {
											var o = eval('('
													+ result.responseText + ')');
											Ext.MessageBox.alert('提示信息',
													"取消对应成功");
											planStore.reload();
										},
										failure : function(result, request) {
											Ext.MessageBox.alert('提示信息',
													'未知错误！')
										}
									})
						} else if (isCorrespond == "N") {
							Ext.MessageBox.alert('提示信息', '这条记录尚未对应过，不能进行取消对应！')
						} else {
							Ext.MessageBox.alert('提示信息', '请选择一行数据！')
						}
						itemCode = "";
						nodeCode = "";
						apartCode = "";
						dateType = "";
						// 刷新Grid
						planStore.reload();
						// detailStore.reload();
					}
				}, {
					text : "采集点",
					iconCls : 'close',
					handler : function() {
						if (dateType != "") {
							var url = "dateTypeMaint.jsp?dateType=" + dateType
									+ "&itemCode=" + itemCode;
							var collection = window
									.showModalDialog(
											url,
											'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
						} else {
							Ext.MessageBox.alert('提示信息', '请选择一行数据！')
						}
					}
				}, {
					text : "关闭",
					iconCls : 'close',
					handler : function() {
						window.close()
					}
			}],
		bbar : gridbbar,
		autoScroll : true,
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.isCorrespond == "N") {
					return 'x-grid-record-blue';
				} else {
					return '';
				}
			}
		}
	});
	// planGrid.on('mouseover', function() {
	// return false;
	// });
	planGrid.on('rowclick', function() {
		// var records = planGrid.getSelectionModel().getSelections();
		var record = planGrid.getSelectionModel().getSelected();
		itemCode = record.get('itemCode');
		dateType = record.get('dataTimeType');
		isCorrespond = record.get('isCorrespond');
			// Ext.getCmp('btnMeasure').setDisabled(diabledFlag);
		});
	// planGrid.on('rowmousedown', function(grid, rowIndex, e) {
	// if (planStore.getAt(rowIndex).data.isOtherPlan) {
	// return false;
	// }
	// return true;
	// });
	// ↑↑**********************需求计划物资Grid************↑↑//

	// ↓↓**********************采购订单明细************↓↓//
	var detailRecprd = Ext.data.Record.create([{
				// PO单明细项次号
				name : 'nodeCode'
			}, {
				// 物料编码
				name : 'descriptor'
			}, {
				// 物料Id
				name : 'nodeName'
			}, {
				// 数量
				name : 'apartCode'
			}, {
				// 数量
				name : 'isCorrespond'
			}]);

	var detailCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '节点编码',
				dataIndex : 'nodeCode',
				width : 80,
				sortable : true
			}, {
				header : '节点描述',
				dataIndex : 'descriptor',
				width : 100,
				sortable : true
			}, {
				header : '节点名称',
				dataIndex : 'nodeName',
				width : 150,
				sortable : true
			}, {
				header : '单元',
				dataIndex : 'apartCode',
				sortable : true,
				width : 50
			}]);
	detailCM.defaultSortable = true;
	// 数据源
	var detailStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getDcsNodeList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, detailRecprd)
			});

	var bbar = new Ext.PagingToolbar({
				pageSize : 20,
				store : detailStore,
				displayInfo : true,
				displayMsg : "一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : '共{0}页'
			});

	// 待选作业方式grid
	var detailGrid = new Ext.grid.GridPanel({
				enableColumnMove : false,
				enableColumnHide : true,
				ds : detailStore,
				cm : detailCM,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				border : false,
				// frame : true,
				autoScroll : true,
				bbar : bbar,
				viewConfig : {
					forceFit : false,
					getRowClass : function(record, rowIndex, rowParams, store) {
						// 禁用数据显示红色
						if (record.data.isCorrespond == "N") {
							return 'x-grid-record-blue';
						} else {
							return '';
						}
					}
				}
			});

	detailGrid.on('rowclick', function() {
		// var records = planGrid.getSelectionModel().getSelections();
		var record = detailGrid.getSelectionModel().getSelected();
		nodeCode = record.get('nodeCode');
		apartCode = record.get('apartCode');
		isCorrespond = record.get('isCorrespond');
			// Ext.getCmp('btnMeasure').setDisabled(diabledFlag);
		});

	// ↑↑**********************采购订单明细************↑↑//

	// ↓↓**********************中间按钮************↓↓//
	// 对应
	var btnCorrespond = new Ext.Button({
				id : 'btnCorrespond',
				height : 80,
				text : "对应",
				style : "margin-left:22;margin-top:80",
				handler : function() {
					if (isCorrespond == "N") {

					} else if (isCorrespond == "Y") {
						Ext.MessageBox.alert('提示信息', '这条记录已经对应过了，不能再进行对应！');

					}  
					itemCode = "";
					nodeCode = "";
					apartCode = "";
					dateType = ""; 
				}
			});
	// 取消对应
	var cancelCorrespond = new Ext.Button({
				id : 'cancelCorrespond',
				text : "取消对应",
				style : "margin-left:12;margin-top:30;",
				handler : function() {
					if (isCorrespond == "Y") {
						Ext.Ajax.request({
									url : 'manager/cancelCorrespond.action',
									method : 'post',
									params : {
										itemCode : itemCode
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.alert('提示信息', "取消对应成功");
										planStore.reload();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '未知错误！')
									}
								})
					} else if (isCorrespond == "N") {
						Ext.MessageBox.alert('提示信息', '这条记录尚未对应过，不能进行取消对应！')
					}
					itemCode = "";
					nodeCode = "";
					apartCode = "";
					dateType = ""; 
				}
			});

	// 采集点
	var btnCollection = new Ext.Button({
		id : 'btnCollection',
		text : "采集点",
		style : "margin-left:17;margin-top:30;",
		handler : function() {
			var url = "dateTypeMaint.jsp?dateType=" + dateType + "&itemCode="
					+ itemCode;
			var collection = window
					.showModalDialog(
							url,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		}
	});

	// 关闭
	var btnClose = new Ext.Button({
				id : 'btnClose',
				text : "关闭",
				style : "margin-left:20;margin-top:30;",
				handler : function() {
					
				}
			}); 
	function queryRecords() {
		planStore.load({
					params : {
						start : 0,
						limit : 20
					}
				});
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : 'fit',
				margins : '10, 10, 10 ,10',
				items : [planGrid]
			}); 
	queryRecords();
});
