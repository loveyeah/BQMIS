Ext.onReady(function() {
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
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	var fromDate = new Ext.form.TextField({
		id : 'fromDate',
		style : 'cursor:pointer',
		readOnly : true,
		width : 80,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					isShowClear : false
				});
				this.blur();
			}
		}
	});

	var toDate = new Ext.form.TextField({
		id : 'toDate',
		style : 'cursor:pointer',
		readOnly : true,
		width : 80,
		value : edate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					isShowClear : false
				});
				this.blur();
			}
		}
	});

	// 定义选择行
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

	// grid中的数据Record
	var gridRecord = new Ext.data.Record.create([{
		name : 'acceptId',
		mapping : 0
	}, {
		name : 'repairProjectName',
		mapping : 1
	}, {
		name : 'workingChargeName',
		mapping : 2
	}, {
		name : 'workingCharge',
		mapping : 3
	}, {
		name : 'workingMenbers',
		mapping : 4
	}, {
		name : 'startTime',
		mapping : 5
	}, {
		name : 'endTime',
		mapping : 6
	}, {
		name : 'startEndTime',
		mapping : 7
	}, {
		name : 'specialityName',
		mapping : 8
	}, {
		name : 'specialityId',
		mapping : 9
	}, {
		name : 'memo',
		mapping : 10
	}, {
		name : 'completion',
		mapping : 11
	}, {
		name : 'acceptanceLevelName',
		mapping : 12
	}, {
		name : 'workflowNo',
		mapping : 13
	}, {
		name : 'workflowStatus',
		mapping : 14
	}, {
		name : 'planStartDate',
		mapping : 15
	}, {
		name : 'planEndDate',
		mapping : 16
	}, {
		name : 'planStartEndDate',
		mapping : 17
	}, {
		name : 'fillBy',
		mapping : 18
	}, {
		name : 'fillName',
		mapping : 19
	}, {
		name : 'fillDate',
		mapping : 20
	}, {
		name : 'acceptanceLevel',
		mapping : 21
	}]);
	// grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'manageplan/getRepairAcceptList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : gridRecord
	});

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
		pageSize : 18,
		store : queryStore,
		displayInfo : true,
		displayMsg : "一共 {2} 条",
		emptyMsg : "没有记录"
	});

	// 页面的Grid主体
	var gridOrder = new Ext.grid.GridPanel({
		id : 'div_grid',
		region : 'north',
		layout : 'column',
		store : queryStore,
		height : 300,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : "ID",
			hidden : true,
			width : 100,
			dataIndex : 'acceptId'
		}, {
			header : "项目名称",
			sortable : true,
			width : 100,
			dataIndex : 'repairProjectName'
		}, {
			header : "工作负责人",
			sortable : true,
			width : 100,
			dataIndex : 'workingChargeName'
		}, {
			header : "工作成员",
			sortable : true,
			width : 100,
			dataIndex : 'workingMenbers'
		}, {
			header : "计划工作时间",
			width : 150,
			dataIndex : 'planStartEndDate',
			renderer : function(v) {
				if (v == '----') {
					return '';
				} else {
					return v;
				}
			}
		}, {
			header : "实际工作时间",
			width : 150,
			dataIndex : 'startEndTime'
		}, {
			header : "填写专业",
			width : 100,
			dataIndex : 'specialityName'
		}, {
			header : "验收级别",
			width : 150,
			hidden : true,
			dataIndex : 'acceptanceLevel'
		}, {
			header : "验收级别",
			width : 150,
			dataIndex : 'acceptanceLevelName'
		}, {
			header : "完成情况",
			width : 150,
			dataIndex : 'completion'
		}, {
			header : "备注",
			width : 150,
			dataIndex : 'memo'
		}],
		tbar : [{
			text : '登记时间:'
		}, fromDate, {
			text : '~'
		}, toDate, '-', {
			id : 'btnSubmit',
			text : "查询",
			iconCls : 'query',
			handler : findFuzzy
		}],
		bbar : pagebar,
		sm : sm,
		frame : false,
		border : false,
		enableColumnMove : false
	});

	var approveList = new repairMaint.approveList();
	gridOrder.on('rowdblclick', function(grid, rowIndex, e) {
		var record = gridOrder.getSelectionModel().getSelected();
		approveList.setFormRec(record);
		tabs.setActiveTab(1);
	});

	function findFuzzy() {
		var ftime = fromDate.getValue();
		var ttime = toDate.getValue();
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		queryStore.baseParams = {
			sd : ftime,
			ed : ttime,
			workflowStatus : "approve"
		};
		queryStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	findFuzzy();
	var tabs = new Ext.TabPanel({
		id : 'div_tabs',
		activeTab : 1,
		tabPosition : 'bottom',
		plain : false,
		defaults : {
			autoScroll : true
		},
		items : [{
			id : 'repairList',
			title : '项目验收列表',
			items : gridOrder,
			autoScroll : true,
			layout : 'fit'
		}, {
			id : 'repairApprove',
			title : '项目验收填写',
			autoScroll : true,
			items : approveList.grid,
			layout : 'fit'
		}]
	});

	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tabs]
	});
	tabs.setActiveTab(0);
});