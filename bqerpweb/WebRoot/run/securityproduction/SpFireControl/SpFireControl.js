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
	var edate = ChangeDateToString(enddate);
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'id',
		mapping : 0
	}, {
		name : 'deployPart',
		mapping : 1
	}, {
		name : 'type',
		mapping : 2
	}, {
		name : 'param',
		mapping : 3
	}, {
		name : 'controlNumber',
		mapping : 4
	}, {
		name : 'serialCode',
		mapping : 5
	}, {
		name : 'validDate',
		mapping : 6
	}, {
		name : 'checkDate',
		mapping : 7
	}, {
		name : 'changeDate',
		mapping : 8
	}, {
		name : 'checkBy',
		mapping : 9
	}, {
		name : 'checkName',
		mapping : 10
	}, {
		name : 'memo',
		mapping : 11
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/findFireControlList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var deployPartQuery = new Ext.form.TextField({
		id : 'deployPartQuery',
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
			dataIndex : 'id'
		}, {
			header : "配置部位",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'deployPart'
		}, {
			header : "型号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'type'
		}, {
			header : "规格",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'param'
		}, {
			header : "数量",
			width : 70,
			sortable : true,
			align : 'left',
			dataIndex : 'controlNumber'
		}, {
			header : "编号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'serialCode'
		}, {
			header : "有效日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'validDate'
		}, {
			header : "检测日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'checkDate'
		}, {
			header : "更换日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'changeDate'
		}, {
			header : "检测人",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'checkName'
		}, {
			header : "备注",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'memo'
		}],
		sm : sm,
		tbar : ["配置部位:", deployPartQuery, {
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
	var id = new Ext.form.Hidden({
		id : "id",
		name : 'control.id',
		anchor : "85%"
	});

	var deployPart = new Ext.form.TextField({
		id : 'deployPart',
		fieldLabel : "配置部位",
		name : 'control.deployPart',
		allowBlank : false,
		anchor : "85%"
	});
	
	var type = new Ext.form.TextField({
		id : 'type',
		fieldLabel : "型号",
		name : 'control.type',
		allowBlank : false,
		anchor : "85%"
	});
	
	var param = new Ext.form.TextField({
		id : 'param',
		fieldLabel : "规格",
		name : 'control.param',
		allowBlank : false,
		anchor : "85%"
	});
	
	var controlNumber = new Ext.form.NumberField({
		id : 'controlNumber',
		fieldLabel : "数量",
		name : 'control.controlNumber',
		allowBlank : false,
		anchor : "85%"
	});
	
	var serialCode = new Ext.form.TextField({
		id : 'serialCode',
		fieldLabel : "编号",
		name : 'control.serialCode',
		allowBlank : false,
		anchor : "85%"
	});
	
	var validDate = new Ext.form.DateField({
		format : 'Y-m-d',
		fieldLabel : '有效日期',
		name : 'control.validDate',
		value : edate,
		id : 'validDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '85%'
	});
	
	var checkDate = new Ext.form.DateField({
		format : 'Y-m-d',
		fieldLabel : '检测日期',
		name : 'control.checkDate',
		value : edate,
		id : 'checkDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '85%'
	});
	
	var changeDate = new Ext.form.DateField({
		format : 'Y-m-d',
		fieldLabel : '更换日期',
		name : 'control.changeDate',
		value : edate,
		id : 'changeDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '85%'
	});
	
	var checkBy = {
		fieldLabel : '检测人',
		name : 'checkBy',
		xtype : 'combo',
		id : 'checkBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'control.checkBy',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('checkBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('checkBy'), emp.workerName);
			}
		}
	};
	
	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 80,
		fieldLabel : '备注',
		name : 'control.memo',
		anchor : "92.3%"
	});

	var myaddpanel = new Ext.FormPanel({
		title : '消防器材配置增加/修改',
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
				labelWidth : 60,
				items : [id,deployPart,type,controlNumber,validDate,changeDate]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 60,
				border : false,
				items : [param, serialCode,checkBy,checkDate]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 60,
			items : [memo]
		}]
	});
	var win = new Ext.Window({
		width : 500,
		height : 320,
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
				if (Ext.get("id").dom.value == ""
						|| Ext.get("id").dom.value == null) {
					myurl = "security/saveFireControlRecord.action";
				} else {
					myurl = "security/updateFireControlRecord.action";
				}
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
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
		store.baseParams = {
				start : 0,
				limit : 18,
				deployPart : deployPartQuery.getValue()
		}
		store.load();
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.setPosition(100, 50);
		win.show();
		myaddpanel.setTitle("增加消防器材配置");

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
				myaddpanel.getForm().loadRecord(record);
				Ext.get('checkBy').dom.value = record.get('checkName');
				myaddpanel.setTitle("修消防器材配置");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.id) {
					ids.push(member.id);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'security/deleteFireControlRecord.action', {
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

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();
});