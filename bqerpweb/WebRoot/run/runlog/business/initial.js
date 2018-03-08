Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var shiftId;
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
	var currentdate = ChangeDateToString(date);

	var unitStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findUintProfessionList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	unitStore.load();

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'remote',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'specialityCode',
		id : 'unitBox',
		width : 200,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					var plcode = record.get('specialityCode');
					var timestore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							method : 'GET',
							url : 'runlog/findShfitTimeBySpecial.action?specialityCode='
									+ plcode + ''
						}),
						reader : new Ext.data.JsonReader({
							id : "time"
						}, [{
							name : 'shiftTimeId'
						}, {
							name : 'shiftTimeName'
						}]),
						autoLoad : true
					});
					timestore.load();
					Ext.getCmp('timeBox').clearValue();

					Ext.getCmp('timeBox').store = timestore;
					if (Ext.getCmp('timeBox').view) {

						Ext.getCmp('timeBox').view.setStore(timestore);
					}
					Ext.getCmp('timeBox').enable();

				}
			}
		}
	});
	var timeBox = new Ext.form.ComboBox({
		fieldLabel : '值班班次',
		id : 'timeBox',
		name : 'shiftTimeId',
		valueField : 'shiftTimeId',
		displayField : 'shiftTimeName',
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'shiftTimeId',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		disabled : true,
		width : 200,
		listeners : {
			select : {
				fn : function(combo, record, index) {
					var shifttimeid = record.get('shiftTimeId');
					var special = Ext.get("specialityCode").dom.value;
					var date = Ext.get("date").dom.value;
					Ext.Ajax.request({
						url : 'runlog/getInitialShift.action',
						params : {
							specialcode : special,
							shifttimeId : shifttimeid,
							nowdate : date
						},
						method : 'post',
						success : function(result, request) {
							var json = result.responseText;
							var o = eval("(" + json + ")");
							Ext.get("shiftName").dom.value = o[1]
							shiftId = o[0];
							var workerstore = new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									method : 'GET',
									url : 'runlog/getInitialWorker.action?shiftId='
											+ shiftId + ''
								}),
								reader : new Ext.data.JsonReader({
									id : "worker"
								}, [{
									name : 'emp_code'
								}, {
									name : 'emp_name'
								}]),
								autoLoad : true
							});
							workerstore.load();
							Ext.getCmp('workerBox').clearValue();
							Ext.getCmp('workerBox').store = workerstore;
							if (Ext.getCmp('workerBox').view) {

								Ext.getCmp('workerBox').view
										.setStore(workerstore);
							}
							Ext.getCmp('workerBox').enable();
						}
					});
				}
			}
		}
	});
	var shiftName = new Ext.form.TextField({
		name : 'shiftName',
		xtype : 'textfield',
		fieldLabel : '值班班组',
		readOnly : true,
		allowBlank : false,
		width : 200
	});
	var workerBox = new Ext.form.ComboBox({
		fieldLabel : '值班负责人',
		id : 'workerBox',
		name : 'emp_code',
		valueField : 'emp_code',
		displayField : 'emp_name',
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'emp_code',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		disabled : true,
		width : 200
	});
	var weather = new Ext.data.Record.create([{
		name : 'weatherKeyId'
	}, {
		name : 'weatherName'
	}]);
	var weatherStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findWeatherList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, weather)
	});
	weatherStore.load();
	weatherStore.on("load", function(ds, records, o) {
		weatherBox.setValue(records[0].data.weatherKeyId);
	});
	var weatherBox = new Ext.form.ComboBox({
		fieldLabel : '值班天气',
		store : weatherStore,
		valueField : "weatherKeyId",
		displayField : "weatherName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'weatherKeyId',
		editable : false,
		selectOnFocus : true,
		name : 'weatherKeyId',
		id : 'weatherBox',
		width : 200
	});
	var content = new Ext.form.FieldSet({
		title : '请选择初始化信息！',
		height : '100%',
		layout : 'form',
		items : [{
			xtype : 'datefield',
			format : 'Y-m-d',// 此处换为'Y'即可
			fieldLabel : '值班日期',
			name : 'date',
			value : currentdate,
			id : 'date',
			itemCls : 'sex-left',
			clearCls : 'allow-float',
			checked : true,
			emptyText : '请选择',
			width : 200
		}, unitBox, timeBox, shiftName, workerBox, weatherBox]
	});
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnReflesh',
			text : "初始化",
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				Ext.Msg.confirm('提示', '是否确认初始化' + Ext.get("unitBox").dom.value
						+ '专业' + Ext.get("workerBox").dom.value + '的值班记录？',
						function(b) {
							if (b == 'yes') {
								form.getForm().submit({
									url : 'runlog/runlogInitial.action',
									method : 'post',
									params : {
										nowdate : Ext.get("date").dom.value,
										specialcode : Ext.get("specialityCode").dom.value,
										shiftId : shiftId,
										shifttimeId : Ext.get("shiftTimeId").dom.value,
										weatherid : Ext.get("weatherKeyId").dom.value,
										workercode : Ext.get("emp_code").dom.value
									},
									success : function(form, action) {
										var message = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("成功", message.data);
									},
									failure : function(form, action) {
										var error = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.show({
											title : '错误',
											msg : error.data,
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.ERROR
										});
									}
								})
							}
						})
			}
		}]
	})
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 100,
		autoHeight : true,
		region : 'center',
		border : false,
		tbar : tbar,
		items : [content]
	});
	var panel = new Ext.Panel({
		border : false,
		collapsible : true,
		items : [form]
	});

	var layout = new Ext.Viewport({
		layout : "fit",
		border : false,
		items : [panel]
	})
});
