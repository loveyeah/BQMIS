Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
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
	var date = new Date();
	var startdate = date.add(Date.DAY, -60);
	var enddate = date;
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value : sdate,
		emptyText : '请选择',
		width : 110
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		emptyText : '请选择',
		width : 110
	});
	var fuzzytext = new Ext.form.TextField({
		id : 'fuzzy',
		name : 'fuzzy',
		width : '150'
	})
	var contbar = new Ext.Toolbar({
		width : Ext.get('div_lay').getWidth(),
		items : ['开始执行时间从：', fromDate, '至:', toDate, '-', fuzzytext, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				con_ds.load({
					params : {
						fuzzy : Ext.get('fuzzy').dom.value,
						startdate : Ext.get('fromDate').dom.value,
						enddate : Ext.get('toDate').dom.value,
						start : 0,
						limit : 18
					}
				});
			}
		}, '-', {
			id : 'btnPaySet',
			text : "设置付款计划",
			iconCls : 'add',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var record = Grid.getSelectionModel().getSelected();
					id = record.data.conId;
					var currencyType = record.data.currencyType;
					var actAmount = record.data.actAmount;
					parent.Ext.getCmp("maintab").setActiveTab(1);
					var url2 = "manage/projectFact/business/contractPayPlan/payPlan/payPlan.jsp";
					parent.document.all.iframe2.src = url2 + "?id=" + id
							+ "&currencyType=" + currencyType + "&actAmount="
							+ actAmount;
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录！');
				}
			}
		}, '-', {
			id : 'btnInfo',
			text : "合同信息",
			iconCls : 'update',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var record = Grid.getSelectionModel().getSelected();
					id = record.data.conId;
					var url = "../../../../../manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
							+ id;
					var o = window.showModalDialog(url, '',
							'dialogWidth=800px;dialogHeight=600px;status=no');
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录！');
				}

			}
		}]
	});
	var con_item = Ext.data.Record.create([{
		name : 'conId'
	}, {
		name : 'workflowStatus'
	}, {
		name : 'conttreesNo'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyType'
	}, {
		name : 'operateName'
	}, {
		name : 'operateDeptName'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				// if (rec.data.workflowStatus != '1') {
				// Ext.get('btnSave').dom.disabled = false;
				// } else {
				// Ext.get('btnSave').dom.disabled = true;
				// }
			}
		}
	});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '供应商',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '金额',
				dataIndex : 'actAmount',
				width : 50,
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyType',
				width : 50,
				align : 'center',
				renderer : function(v) {

					if (v == 1) {
						return "RMB";
					}
					if (v == 2) {
						return "USD";
					} else {
						return "异常";
					}

				}
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center'
			}, {
				header : '履行开始时间',
				dataIndex : 'startDate',
				width : 120,
				align : 'center'
			}, {
				header : '履行结束时间',
				dataIndex : 'endDate',
				width : 120,
				align : 'center'
			}]);

	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/getExecConList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	con_ds.load({
		params : {
			startdate : sdate,
			enddate : edate,
			start : 0,
			limit : 18
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		title : '执行中列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
		id = record.data.conId;
		var currencyType = record.data.currencyType;
		var actAmount = record.data.actAmount;
		parent.Ext.getCmp("maintab").setActiveTab(1);
		var url2 = "manage/projectFact/business/contractPayPlan/payPlan/payPlan.jsp";
		parent.document.all.iframe2.src = url2 + "?id=" + id + "&currencyType="
				+ currencyType + "&actAmount=" + actAmount;
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [Grid]
	});
})
