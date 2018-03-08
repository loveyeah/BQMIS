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
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -12);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});

	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'repairId',
		mapping : 0
	}, {
		name : 'carId',
		mapping : 1
	}, {
		name : 'carNo',
		mapping : 2
	}, {
		name : 'nowKmNum',
		mapping : 3
	}, {
		name : 'sendPerson',
		mapping : 4
	}, {
		name : 'sendPersonName',
		mapping : 5
	}, {
		name : 'repairDate',
		mapping : 6
	}, {
		name : 'repairContend',
		mapping : 7
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/findCarRepairList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var carNo = new Ext.form.TextField({
		id : 'carNo',
		name : 'carNo',
		anchor : "75%"
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 110,
			hidden : true,
			align : 'left',
			dataIndex : 'repairId'
		}, {
			header : "牌照",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'carNo'
		}, {
			header : "维修日期",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'repairDate'
		}, {
			header : "当前公里数",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'nowKmNum'
		}, {
			header : "送修人",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'sendPersonName'
		}, {
			header : "维修内容",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'repairContend'
		}],
		sm : sm,
		tbar : ["维修日期:", fromDate, "~", toDate, "-", "牌照:", carNo, {
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

	// 定义FORM
	var repairId = new Ext.form.Hidden({
		id : "repairId",
		name : 'repair.repairId',
		anchor : "85%"
	});

	var carId = new Ext.form.Hidden({
		id : "carId",
		fieldLabel : 'carId',
		readOnly : true,
		name : 'repair.carId',
		anchor : "85%"
	});
	
	var carName = new Ext.form.TextField({
		id : 'carName',
		fieldLabel : "牌照",
		name : 'repair.carName',
		anchor : "85%",
		readOnly : true,
		allowBlank : false,
        listeners : {
            focus : selectCarNo
        }
	});
	
	var nowKmNum = new Ext.form.NumberField({
		id : 'nowKmNum',
		xtype : "NumberField",
		fieldLabel : "当前公里数",
		name : 'repair.nowKmNum',
		allowBlank : false,
		anchor : "85%"
	});

	var sendPerson = new Ext.form.TextField({
		id : "sendPerson",
		fieldLabel : '送修人',
		readOnly : true,
		anchor : "85%"
	})
	var hiddsendPerson = new Ext.form.Hidden({
		id : 'hiddsendPerson',
		name : 'repair.sendPerson'
	});

	var repairDate = new Ext.form.TextField({
		id : 'repairDate',
		fieldLabel : "维修时间",
		name : 'repair.repairDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : edate
	});

	var repairContend = new Ext.form.TextArea({
		id : "repairContend",
		height : 100,
		fieldLabel : '维修内容',
		name : 'repair.repairContend',
		anchor : "92.3%"
	});

	var myaddpanel = new Ext.FormPanel({
		title : '车辆维修增加/修改',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [repairId, carId,carName, sendPerson, hiddsendPerson]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [nowKmNum, repairDate]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [repairContend]
		}]
	});
	var win = new Ext.Window({
		width : 450,
		height : 250,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (Ext.get("repairId").dom.value == ""
						|| Ext.get("repairId").dom.value == null) {
					myurl = "security/addCarRepair.action";
				} else {
					myurl = "security/updateCarRepair.action";
				}
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						// workerName.setValue("");
						queryRecord();
						win.hide();
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

	// 查询
	function queryRecord() {
		var ftime = Ext.get('fromDate').dom.value;
		var ttime = Ext.get('toDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		store.baseParams = {
				sDate : Ext.get('fromDate').dom.value,
				eDate : Ext.get('toDate').dom.value,
				carNo : Ext.get('carNo').dom.value,
				flag : 'G'
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
		win.setPosition(100, 50);
		win.show();
		myaddpanel.setTitle("增加车辆维修登记");

	}
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				getWorkCode();
				myaddpanel.getForm().loadRecord(record);
				Ext.get('carName').dom.value = record.get('carNo');
				myaddpanel.setTitle("修改车辆维修登记");
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
				if (member.repairId) {
					ids.push(member.repairId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'security/deleteCarRepair.action', {
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

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sendPerson.setValue(result.workerName);
					hiddsendPerson.setValue(result.workerCode);
				}
			}
		});
	}

	  function selectCarNo() {
        this.blur();
        var mate = window.showModalDialog('carSelect.jsp', window,'dialogWidth=200px;dialogHeight=300px;status=no');
        if (typeof(mate) != "undefined") {
            carId.setValue(mate.carId);
            carName.setValue(mate.carNo);
        }
    }

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	getWorkCode();
	queryRecord();
});