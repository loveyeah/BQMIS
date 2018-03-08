Ext.onReady(function() {
	function getDate(v) {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		if (v <= 3) {
			t = d.getDate();
			s += "-" + (t > 9 ? "" : "0") + t + " ";
		}
		if (v == 1) {
			t = d.getHours();
			s += (t > 9 ? "" : "0") + t;
			// t = d.getMinutes();
			// s += (t > 9 ? "" : "0") + t + ":";
			// t = d.getSeconds();
			// s += (t > 9 ? "" : "0") + t;
			// s += "00:00";
		}
		return s;
	}
	function setValue(reportType) {
		var d = new Date();
		if (reportType == 1)
			time.setValue(getDate(1)), quarterBox.hide();
		if (reportType == 3)
			time.setValue(getDate(3)), quarterBox.hide();
		if (reportType == 4)
			time.setValue(getDate(4)), quarterBox.hide();
		if (reportType == 5)
			time.setValue(d.getFullYear()), quarterBox.show();
		if (reportType == 6)
			time.setValue(d.getFullYear()), quarterBox.hide();
	}
	function getTime(reportType) {
		var returnTime;
		if (reportType == 1)
			returnTime = time.getValue();
		if (reportType == 3)
			returnTime = time.getValue();
		if (reportType == 4)
			returnTime = time.getValue() + "-01";
		if (reportType == 5) {
			returnTime = time.getValue();
			var quarter = quarterBox.getValue();
			if (quarter == 1)
				returnTime += "-01-01";
			if (quarter == 2)
				returnTime += "-04-01";
			if (quarter == 3)
				returnTime += "-07-01";
			if (quarter == 4)
				returnTime += "-10-01";
		}
		if (reportType == 6)
			returnTime = time.getValue() + "-01-01";
		return returnTime;
	}
	var dataTimeTypeData = Ext.data.Record.create([{
		name : 'value'
	}, {
		name : 'key'
	}]);
	var dataTimeTypeStore = new Ext.data.JsonStore({
		url : 'comm/getBpBasicDataByType.action?type=DATA_TIME_TYPE',
		fields : dataTimeTypeData
	});
	dataTimeTypeStore.load();
	var dataTimeTypeBox = new Ext.form.ComboBox({
		fieldLabel : '时间类型',
		store : dataTimeTypeStore,
		id : 'dataTimeType',
		name : 'dataTimeType',
		valueField : "key",
		displayField : "value",
		mode : 'local',
		typeAhead : true,
		width : 100,
		forceSelection : true,
		hiddenName : 'statItem.dataTimeType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择'
	});
	dataTimeTypeBox.on('beforequery', function() {
		return false
	});
	var reportNameData = Ext.data.Record.create([{
		name : 'reportName'
	}, {
		name : 'reportCode'
	}, {
		name : 'reportType'
	}, {
		name : 'timeDelay'
	}, {
		name : 'timeUnit'
	}]);
	var reportReader = new Ext.data.JsonReader({
		root : "list"
	}, reportNameData);
	var reportNameStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/getBpCInputReportList.action'
		}),
		reader : reportReader
	});
	reportNameStore.on('load', function(e, records) {
		reportNameBox.setValue(records[0].data.reportCode)
		dataTimeTypeBox.setValue(records[0].data.reportType);
		var reportType = records[0].data.reportType;
		setValue(reportType);
		// store.on('beforeload', function() {
		// Ext.apply(this.baseParams, {
		// reportCode : reportNameBox.getValue(),
		// startDate : getTime(dataTimeTypeBox.getValue())
		// });
		// });
		store.load({
			params : {
				reportCode : reportNameBox.getValue(),
				startDate : getTime(dataTimeTypeBox.getValue()),
				dateType : dataTimeTypeBox.getValue()
			}
		});
	});
	reportNameStore.load({
		params : {
			start : 0,
			limit : 99999999
		}
	});
	var reportNameBox = new Ext.form.ComboBox({
		fieldLabel : '运行报表名称',
		store : reportNameStore,
		id : 'reportNameBox',
		name : 'reportName',
		valueField : "reportCode",
		displayField : "reportName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		listeners : {
			select : function(index, record) {
				dataTimeTypeBox.setValue(record.data.reportType);
				var reportType = record.data.reportType;
				setValue(reportType);
				store.load({
					params : {
						reportCode : reportNameBox.getValue(),
						startDate : getTime(dataTimeTypeBox.getValue()),
						dateType : dataTimeTypeBox.getValue()
					}
				});
			}
		}
	});
	var quarterBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'quarterBox',
		name : 'quarterBoxName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		hidden : true,
		forceSelection : true,
		hiddenName : 'quarterBoxName',
		editable : false,
		triggerAction : 'all',
		width : 110,
		selectOnFocus : true,
		value : 1
	});
	// 时间类型
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : false,
		fieldLabel : '时间点',
		// value : getDate(),
		listeners : {
			focus : function() {
				var format = "yyyy-MM-dd";
				if (dataTimeTypeBox.getValue() == 1) {
					format = "yyyy-MM-dd HH";
				}
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true
				});
			}
		}
	});
	// var endtime = new Ext.form.TextField({
	// id : 'endtime',
	// allowBlank : false,
	// fieldLabel : '时间点',
	// // value : getDate(),
	// listeners : {
	// focus : function() {
	// var format = "yyyy-MM-dd";
	// if (dataTimeTypeBox.getValue() == 1) {
	// format = "yyyy-MM-dd HH";
	// }
	// WdatePicker({
	// startDate : '%y-%M-%d',
	// dateFmt : format,
	// alwaysUseStartDate : true
	// });
	// }
	// }
	// });
	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			store.load({
				params : {
					reportCode : reportNameBox.getValue(),
					startDate : getTime(dataTimeTypeBox.getValue()),
					dateType : dataTimeTypeBox.getValue()
				}
			});
		}
	});

	var MyRecord = Ext.data.Record.create([{
		name : 'itemCode'
	}, {
		name : 'itemName'
	}, {
		name : 'unitCode'
	}, {
		name : 'unitName'
	}, {
		name : 'dataValue'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'manager/findReportList.action'
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	number = new Ext.grid.RowNumberer({
		header : "",
		align : 'left'
	})
	var theReader = new Ext.data.JsonReader({
			// root : "list",
			// totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	var grid = new Ext.grid.EditorGridPanel({
		height : "100%",
		title : "运行报表数据查询",
		// style : "padding:5px 5px 5px 5px",
		border : false,
		store : store,
		viewConfig : {
			forcefit : true
		},
		columns : [sm, // 选择框
				number, {
					header : "指标编码",
					width : 100,
					sortable : false,
					dataIndex : 'itemCode'
				}, {
					header : "指标名称",
					width : 100,
					sortable : false,
					dataIndex : 'itemName'
				}, {
					header : "计量单位",
					width : 100,
					sortable : false,
					dataIndex : 'unitName'
				}, {
					header : "指标值",
					width : 100,
					sortable : false,
					dataIndex : 'dataValue'
				}],
		tbar : ['运行报表名称：', reportNameBox, '-', '时间类型：', dataTimeTypeBox, '-',
				'时间点：', time, ' ',quarterBox, '-', btnQuery],
		sm : sm
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [grid]
	});
});
