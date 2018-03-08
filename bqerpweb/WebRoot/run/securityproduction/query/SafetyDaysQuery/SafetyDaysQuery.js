Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'safetyDaysRecord.recordId'
	}, {
		name : 'safetyDaysRecord.ifBreak'
	}, {
		name : 'safetyDaysRecord.safetyDays'
	}, {
		name : 'safetyDaysRecord.memo'
	}, {
		name : 'safetyDaysRecord.recordBy'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'recordTime'
	}, {
		name : 'recorderName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/getSafetyDaysRecordList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [new Ext.grid.RowNumberer(), {
			header : "",
			dataIndex : 'safetyDaysRecord.recordId',
			hidden : true
		}, {
			header : "安全记录开始日期",
			width : 75,
			sortable : true,
			align : 'center',
			dataIndex : 'startDate'
		}, {
			header : "安全记录结束日期",
			width : 75,
			sortable : true,
			align : 'center',
			dataIndex : 'endDate'
		}, {
			header : "安全天数",
			width : 50,
			sortable : true,
			align : 'center',
			dataIndex : 'safetyDaysRecord.safetyDays'
		}, {
			header : "备注",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'safetyDaysRecord.memo'
		}, {
			header : "记录人",
			width : 75,
			sortable : true,
			align : 'center',
			dataIndex : 'recorderName'
		}, {
			header : "",
			dataIndex : 'safetyDaysRecord.recordBy',
			hidden : true
		}, {
			header : "记录日期",
			width : 75,
			sortable : true,
			align : 'center',
			dataIndex : 'recordTime'
		}],
		sm : sm,
		stripeRows : true,
		autoSizeColumns : true,
		autoScroll : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	//	grid.on("rowdblclick", updateRecord);

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

});