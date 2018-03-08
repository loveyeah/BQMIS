Ext.onReady(function() {

	// 系统当前时间
	var enddate = new Date();
	// 系统当前时间前七天
	var startdate = new Date();
	startdate.setDate(enddate.getDate() - 7);
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
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
	};
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "",
		allowBlank : false,
		readOnly : true,
		value : startdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : enddate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});
	var Border = Ext.Viewport;
	var loginSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var loginCm = new Ext.grid.ColumnModel([{
		header : "工号",
		dataIndex : "workerCode",
		sortable : true,
		width : 100
	}, {
		header : "主机名",
		dataIndex : "hostName",
		sortable : true,
		width : 100
	}, {
		header : "主机IP",
		dataIndex : "hostIp",
		width : 100
	}, {
		header : "登陆时间",
		dataIndex : "loginDate",
		width : 100
	}, {
		header : "登陆模块",
		dataIndex : "loginFile",
		width : 100
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findLoginList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "data.total",
			root : "data.root"
		}, [{
			name : "id"
		}, {
			name : "workerCode"
		}, {
			name : "hostName"
		}, {
			name : "hostIp"
		}, {
			name : "loginDate"
		}, {
			name : "loginFile"
		}])
	});
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	ds.load({
		params : {
			beginDate : ChangeDateToString(fromDate.getValue()),
			endDate : ChangeDateToString(toDate.getValue()),
			workerCode : '',
			start : 0,
			limit : 18
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			beginDate : ChangeDateToString(fromDate.getValue()),
			endDate : ChangeDateToString(toDate.getValue()),
			workerCode : Ext.get("workerCode").dom.value
		})
	});
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : loginCm,
		bbar : bbar,
		sm : loginSm,
		viewConfig : {
			forceFit : true
		},
		tbar : ['用户登陆时间:', '-', fromDate, '-', '至', toDate, '-', '工号:', '-', {
			id : "workerCode",
			xtype : 'textfield',
			name : 'workerCode'
		}, '-', {
			id : "query",
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.load({
					params : {
						beginDate : ChangeDateToString(fromDate.getValue()),
						endDate : ChangeDateToString(toDate.getValue()),
						workerCode : Ext.get("workerCode").dom.value,
						start : 0,
						limit : 18
					}
				});
			}
		}]
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
})