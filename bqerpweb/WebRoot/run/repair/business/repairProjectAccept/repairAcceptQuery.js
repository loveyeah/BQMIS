Ext.onReady(function() {
	// 项目名称
	var projectName = new Ext.form.TextField({
		id : 'projectName',
		width : 120
	})
	// 实际工期
	var startDate = new Ext.form.TextField({
		id : 'startDate',
		width : 120,
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					isShowClear : true,
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd'
				});
			}
		}
	})
	var endDate = new Ext.form.TextField({
		id : 'endDate',
		width : 120,
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					isShowClear : true,
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd'
				});
			}
		}
	})
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
		name : 'workTime',
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
		name : 'workflowNo',
		mapping : 13
	}, {
		name : 'fillBy',
		mapping : 18
	}, {
		name : 'fillByName',
		mapping : 19
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
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录"
	});

	// 页面的Grid主体
	var gridOrder = new Ext.grid.GridPanel({
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
			width : 150,
			dataIndex : 'repairProjectName'
		}, {
			header : "工作负责人",
			sortable : true,
			width : 150,
			dataIndex : 'workingChargeName'
		}, {
			header : "工作成员",
			sortable : true,
			width : 150,
			dataIndex : 'workingMenbers'
		}, {
			header : "实际工作时间",
			width : 200,
			dataIndex : 'workTime'
		}, {
			header : "填写专业",
			width : 150,
			dataIndex : 'specialityName'
		}, {
			header : "填写人ID",
			hidden : true,
			width : 100,
			dataIndex : 'fillBy'
		}, {
			header : "填写人",
			width : 100,
			dataIndex : 'fillByName'
		}],
		tbar : [{
			text : '实际工期：'
		}, startDate, {
			text : '至'
		}, endDate, {
			text : '项目名称：'
		}, projectName, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : findFuzzy
		}, '-', {
			id : 'btnTable',
			text : "会签表",
			iconCls : 'pdfview',
			handler : Table
		}, '-', {
			id : 'btnApprove',
			text : "审批信息",
			iconCls : 'view',
			handler : Approve
		}],
		bbar : pagebar,
		sm : sm,
		frame : false,
		border : false,
		enableColumnMove : false
	});

	// gridOrder.on('rowdblclick', function(grid, rowIndex, e) {
	// var record = gridOrder.getSelectionModel()
	// .getSelected();
	// approveList.setFormRec(record);
	// tabs.setActiveTab(1);
	// });

	new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		border : false,
		items : [gridOrder]
	});

	function findFuzzy(start) {
		if (startDate.getValue() != "" && endDate.getValue() == "") {
			Ext.Msg.alert('提示', '请选择查询结束时间！');
			return;
		}
		if (startDate.getValue() == "" && endDate.getValue() != "") {
			Ext.Msg.alert('提示', '请选择查询开始时间！');
			return;
		}
		queryStore.baseParams = {
			projectName : projectName.getValue(),
			startDate : startDate.getValue(),
			endDate : endDate.getValue()
		};
		queryStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	function Table() {//add by drdu 20100526
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = sm.getSelected();
			var id = rec.get('acceptId');
			var url = "/powerrpt/report/webfile/bqmis/repairAccept.jsp?acceptId="
					+ id;
			window.open(url);
		}
	};
	function Approve() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = sm.getSelected();
			var workflowType = "bqRepairAcceptApprove";
			var entryId = rec.get('workflowNo');
			var url;
			if (entryId == null || entryId == "")
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ workflowType;
			else
				url = url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			window.open(url);

		}
	};
	findFuzzy();

});