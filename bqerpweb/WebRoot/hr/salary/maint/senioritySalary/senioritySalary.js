Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id = "";
	
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
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'senioritySalaryId'
	}, {
		name : 'senioritySalary'
	}, {
		name : 'effectStartTime'
	}, {
		name : 'effectEndTime'
	}, {
		name : 'memo'
	}, {
		name : 'status'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'com/findSenioritySalaryList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

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

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

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
			dataIndex : 'senioritySalaryId',
			hidden : true
		}, {
			header : "工龄工资系数",
			width : 75,
			sortable : true,
			dataIndex : 'senioritySalary'
		}, {
			header : "执行开始时间",
			width : 75,
			sortable : true,
			dataIndex : 'effectStartTime'
		}, {
			header : "执行结束时间",
			width : 75,
			sortable : true,
			dataIndex : 'effectEndTime'
		}, {
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['执行时间：', fuzzy, {
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
		// 分页
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

	// -------------------
	var wd = 240;

	var senioritySalaryId = new Ext.form.Hidden({
		id : "senioritySalaryId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value : '',
		name : 'salary.senioritySalaryId'
	});

	var senioritySalary = new Ext.form.NumberField({
		id : "senioritySalary",
		fieldLabel : '工龄工资系数',
		width : wd,
		name : 'salary.senioritySalary'
	});

	var effectStartTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "effectStartTime",
		name : 'effectStartTime',
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
						if (effectEndTime.getValue() != "") {
							if (effectStartTime.getValue() == ""
									|| effectStartTime.getValue() > effectEndTime
											.getValue()
									|| effectEndTime.getValue() == effectStartTime
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于执行结束时间!");
								effectStartTime.setValue("");
								return;
							}
						}
					},
					onclearing : function() {
						effectStartTime.markInvalid();
					}
				});
				this.blur();
			}
		}
	});

	var effectEndTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "effectEndTime",
		name : 'effectEndTime',
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
						if (effectStartTime.getValue() == ""
								|| effectStartTime.getValue() > effectEndTime
										.getValue()
								|| effectEndTime.getValue() == effectStartTime
										.getValue()) {
							Ext.Msg.alert("提示", "必须大于执行开始时间!");
							effectEndTime.setValue("")
							return;
						}
					},
					onclearing : function() {
						effectEndTime.markInvalid();
					}
				});
				this.blur();
			}
		}
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		width : wd,
		name : 'salary.memo',
		height : 80
	});

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 110,
		closeAction : 'hide',
		title : '增加/修改基础工资维护',
		items : [senioritySalaryId, senioritySalary, effectStartTime,
				effectEndTime, memo]
	});

	function checkInput() {
		var msg = "";
		if (senioritySalary.getValue() == "") {
			msg = "'工龄工资系数'";
		}
		if (effectStartTime.getValue() == "") {
			if (msg == "")
				msg = "'执行开始时间'";
			else
				msg = msg + ",'执行开始时间'";
		}
		if (Ext.get("effectEndTime").dom.value == "") {
			if (msg == "")
				msg = "'执行结束时间'";
			else
				msg = msg + ",'执行结束时间'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 400,
		height : 280,
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
				var myurl = "";
				if (senioritySalaryId.getValue() == "") {
					myurl = "com/addSenioritySalary.action";
				} else {
					myurl = "com/updateSenioritySalary.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						"effectStartTime" : effectStartTime.getValue(),
						"effectEndTime" : effectEndTime.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg.indexOf("成功") != -1) {
							queryRecord();
							id = "";
							win.hide();
						}
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
		store.baseParams = {
			sDate : fuzzy.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		var count = store.getCount();
		if (store.getCount() > 0) {
			effectStartTime.setDisabled(true);
			var date = store.getAt(count - 1).get("effectEndTime");
			var dd = StringToDate(date);
			//dd.setMonth(dd.getMonth()+1) 
			var startdate = ChangeDateToString(dd);
			effectStartTime.setValue(startdate);
			effectStartTime.setDisabled(true);
		} else {
			effectStartTime.setDisabled(false);
		}
		myaddpanel.setTitle("增加工龄工资信息");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.getSelectionModel().getSelections();
			id = records[0].data.basisSalaryId;
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				if (records[0].data.status == 1) {
					win.show();
					var record = grid.getSelectionModel().getSelected();
					myaddpanel.getForm().reset();
					myaddpanel.form.loadRecord(record);
					effectStartTime.setDisabled(true);
					effectStartTime.setValue(record.get("effectStartTime"));
					effectEndTime.setValue(record.get("effectEndTime"));

					myaddpanel.setTitle("修改工龄工资信息");
				} else {
					Ext.Msg.alert("提示", "该行不可编辑!");
					return false;
				}
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
				if (member.status == 0) {
					Ext.Msg.alert('提示', '该行不可删除!');
					return;
				} else if (member.senioritySalaryId) {
					ids.push(member.senioritySalaryId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteSenioritySalary.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
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