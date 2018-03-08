Ext.onReady(function() {
	var delayTime;// 延时时间
	var delayUnit;// 延时单位
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
	function saveData() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
				if (b == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'manager/saveReportEntryValue.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							timeType : dataTimeTypeBox.getValue(),
							entryDate : getTime(dataTimeTypeBox.getValue())
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							store.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
						}
					})
				}
			})

			// if (!confirm("确定要保存修改吗?"))
			// return;
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
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
		delayTime = records[0].data.timeDelay;
		delayUnit = records[0].data.timeUnit;
		var reportType = records[0].data.reportType;
		setValue(reportType);
		store.load({
			params : {
				reportCode : reportNameBox.getValue(),
				dateType : dataTimeTypeBox.getValue(),
				date : getTime(dataTimeTypeBox.getValue()),
				delayTime : delayTime,
				delayUnit : delayUnit
			}
		});
	});
	reportNameStore.load({
		params : {
			start : 0,
			limit : 99999999
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
				delayTime = record.data.timeDelay;
				delayUnit = record.data.timeUnit;
				setValue(record.data.reportType);
				store.load({
					params : {
						reportCode : reportNameBox.getValue(),
						dateType : dataTimeTypeBox.getValue(),
						date : getTime(dataTimeTypeBox.getValue()),
						delayTime : delayTime,
						delayUnit : delayUnit
					}
				});
			}
		}
	});

	// 时间类型
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : false,
		fieldLabel : '时间点',
		// value : getDate(),
		width : 100,
		listeners : {
			focus : function() {
				var format = "";
				if (dataTimeTypeBox.getValue() == 1)
					format = "yyyy-MM-dd HH";
				if (dataTimeTypeBox.getValue() == 3)
					format = "yyyy-MM-dd";
				if (dataTimeTypeBox.getValue() == 4)
					format = "yyyy-MM";
				if (dataTimeTypeBox.getValue() == 5)
					format = "yyyy";
				if (dataTimeTypeBox.getValue() == 6)
					format = "yyyy";
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true
				});
			}
		}
	});
	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			store.load({
				params : {
					reportCode : reportNameBox.getValue(),
					dateType : dataTimeTypeBox.getValue(),
					date : getTime(dataTimeTypeBox.getValue()),
					delayTime : delayTime,
					delayUnit : delayUnit
				}
			});
		}
	});
	var btnSave = new Ext.Button({
		text : '数据保存',
		iconCls : 'save',
		handler : saveData
	});
	var btnCompute = new Ext.Button({
		text : '数据计算',
		iconCls : 'update',
		handler : function() {
			// Ext.Ajax.request({
			// url : 'manager/itemCollectCompute.action',
			// method : 'post',
			// params : {
			// // isUpdate : Ext.util.JSON.encode(updateData),
			// // timeType : dataTimeTypeBox.getValue(),
			// // entryDate : time.getValue()
			// },
			// success : function(result, request) {
			// var o = eval('(' + result.responseText + ')');
			// Ext.MessageBox.alert('提示信息', o.msg);
			// },
			// failure : function(result, request) {
			// Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
			// }
			// })
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
	}, {
		name : 'dataType'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'manager/findReportEntryList.action'
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
		autoHeight : true,
		autoWidth : true,
		autoScroll : true,
		border : false,
		// style : "padding:0px 0px 0px 0px",
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
					dataIndex : 'dataValue',
					editor : new Ext.form.NumberField({
						allowDecimals : true,
						decimalPrecision : 0
					})
				}],
		tbar : [btnQuery, '-', btnSave, '-', btnCompute],
		sm : sm,
		clicksToEdit : 1,
		listeners : {
			'beforeedit' : function(e) {
				if (e.field == "dataValue") {
					var type = e.record.get('dataType');
					grid.getColumnModel().setEditor(5,
							new Ext.grid.GridEditor(new Ext.form.NumberField({
								allowDecimals : true,
								decimalPrecision : type
							})));
				}
			}
		}
			// 分页
			// bbar : new Ext.PagingToolbar({
			// pageSize : 18,
			// store : store,
			// displayInfo : true,
			// displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			// emptyMsg : "没有记录"
			// })
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		collapsible : true,
		split : true,
		border : false,
		items : [new Ext.Panel({
			border : false,
			height : '100%',
			autoScroll : true,
			title : '运行报表数据录入',
			tbar : ['运行报表名称：', reportNameBox, '-', '时间类型：', dataTimeTypeBox,
					'-', '时间点：', time,' ', quarterBox],
			items : [grid]
		})]
	});
});
