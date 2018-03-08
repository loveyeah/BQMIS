Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	
	function StringToDate(DateStr) {
		var converted = Date.parse(DateStr);
		var myDate = new Date(converted);
		if (isNaN(myDate)) {
			var arys = DateStr.split('-');
			myDate = new Date(arys[0], arys[1]);
		}
		return myDate;
	}
	var fuzzy = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "fuzzy",
		name : 'fuzzy',
		readOnly : true,
		anchor : "80%",
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
					},
					onclearing : function() {
						fuzzy.markInvalid();
					}
				});
			}
		}
	});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'attendanceDaysId'
	}, {
		name : 'month'
	}, {
		name : 'attendanceDays'
	}, {
		name : 'memo'
	}, {
		name : 'startDate'//add by sychen 20100806
	}, {
		name : 'endDate'//add by sychen 20100806
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'com/findAttendanceDaysList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	// 月出勤天数grid
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'attendanceDaysId',
			hidden : true
		}, {
			header : "执行开始时间",
			width : 40,
			sortable : true,
			dataIndex : 'startDate'
		},  {
			header : "执行结束时间",
			width : 40,
			sortable : true,
			dataIndex : 'endDate'
		}, 
//		{
//			header : "月份",
//			width : 40,
//			sortable : true,
//			dataIndex : 'month'
//		}, 
			{
			header : "出勤天数",
			width : 40,
			sortable : false,
			dataIndex : 'attendanceDays'
		}, {
			header : "备注",
			width : 40,
			sortable : true,
			dataIndex : 'memo'
		}],
		viewConfig : {
			forceFit : true
		},
		tbar : ['月份：', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		sm : sm,
		frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	var wd = 240;
	var month = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "month",
		fieldLabel : '月份',
		width : wd,
//		anchor : "80%",
		name : 'month',
		allowBlank : false,
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
					},
					onclearing : function() {
						month.markInvalid();
					}
				});
			}
		}
	});
	
	// add by sychen 20100806
		var startDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "startDate",
		name : 'startDate',
		readOnly : true,
		width : wd,
		fieldLabel : '执行开始时间',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : " ",
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
						if (endDate.getValue() != "") {
							if (startDate.getValue() == ""
									|| startDate.getValue() > endDate
											.getValue()
									|| endDate.getValue() == startDate
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于执行结束时间!");
								startDate.setValue("");
								return;
							}
						}
					},
					onclearing : function() {
						startDate.markInvalid();
					}
				});
				this.blur();
			}
		}
	});

	var endDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "endDate",
		name : 'endDate',
		readOnly : true,
		width : wd,
		fieldLabel : '执行结束时间',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : " ",
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
						if (startDate.getValue() == ""
								|| startDate.getValue() > endDate
										.getValue()
								|| endDate.getValue() == startDate
										.getValue()) {
							Ext.Msg.alert("提示", "必须大于执行开始时间!");
							endDate.setValue("")
							return;
						}
					},
					onclearing : function() {
						endDate.markInvalid();
					}
				});
				this.blur();
			}
		}
	})
	
	

	var attendanceDaysId = new Ext.form.Hidden({
		id : "attendanceDaysId",
		width : wd,
		name : 'attendDays.attendanceDaysId'
	});

	var attendanceDays = new Ext.form.NumberField({
		id : "attendanceDays",
		fieldLabel : '出勤天数',
		width : wd,
		allowBlank : false,
//		anchor : "80%",
		name : 'attendDays.attendanceDays'
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		width : wd,
		name : 'attendDays.memo',
		height : 120
	});

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 110,
		closeAction : 'hide',
		title : '增加/修改月出勤天数',
		items : [attendanceDaysId, startDate,endDate, attendanceDays, memo]
	});

	var win = new Ext.Window({
		width : 400,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (startDate.getValue() == "") {
					Ext.Msg.alert("提示", "<center>请输入执行开始时间</center>");
					return;
				}
				if (endDate.getValue() == "") {
					Ext.Msg.alert("提示", "<center>请输入执行结束时间</center>");
					return;
				}
				if (attendanceDays.getValue() == "") {
					Ext.Msg.alert("提示", "<center>请输入出勤天数</center>");
					return;
				}
				var myurl = "";
				if (attendanceDaysId.getValue() == "") {
					myurl = "com/addAttendanceDays.action";
//					Ext.lib.Ajax.request('POST',
//						'com/findByMonth.action', {
//							success : function(action) {
//								var result = eval(action.responseText);
//								if (!result) {
//									myaddpanel.getForm().submit({
//										method : 'POST',
//										url : myurl,
//										params : {
////											month : month.getValue()
//											startDate : startDate.getValue(),
//											endDate : endDate.getValue()
//										},
//										success : function(form, action) {
//											var o = eval("(" + action.response.responseText + ")");
//											Ext.Msg.alert("注意", "<center>" + o.msg + "</center>");
//											win.hide();
//											queryRecord();
//										},
//										faliue : function() {
//											Ext.Msg.alert('错误', '出现未知错误.');
//										}
//									});
//							    } else {
//							    	Ext.Msg.alert("提示", "<center>该月出勤天数已经维护。</center>");
//							    	return;
//							    }
//							},
//							failure : function() {
//							}
//						},
//						month : month.getValue());
				} else {
					myurl = "com/updateAttendanceDays.action";
					
				}
					    myaddpanel.getForm().submit({
						method : 'POST',
						url : myurl,
						params : {
//							month : month.getValue()
							startDate : startDate.getValue(),
							endDate : endDate.getValue()
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert("注意", "<center>" + o.msg + "</center>");
							win.hide();
							queryRecord();
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	function queryRecord() {
		store.load({
			params : {
				month : fuzzy.getValue(),
				start : 0,
				limit : 18
			}
		});
	}
//update by sychen 20100806
	function addRecord() {
		myaddpanel.getForm().reset();
//        month.setDisabled(false);
		startDate.setValue("");
		win.show();
		var count = store.getCount();
		if (store.getCount() > 0) {
			date = store.getAt(count - 1).get("endDate");

			var dd = StringToDate(date);
			var startdate = ChangeDateToString(dd);

			startDate.setValue(startdate);
			startDate.setDisabled(true);
		} else {
			startDate.setDisabled(false);
		}
		myaddpanel.setTitle("月出勤天数增加");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				myaddpanel.form.loadRecord(record);
//				month.setDisabled(true);
				startDate.setDisabled(true);
				startDate.setValue(record.get("startDate"));
				endDate.setValue(record.get("endDate"));
				attendanceDays.setValue(record.get("attendanceDays"));
				myaddpanel.setTitle("月出勤天数修改");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.attendanceDaysId) {
					ids.push(member.attendanceDaysId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteAttendanceDays.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "<center>删除成功！</center>")
									queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}

	queryRecord();
});