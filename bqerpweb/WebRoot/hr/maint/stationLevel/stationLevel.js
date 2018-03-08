Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'stationLevelId'
	}, {
		name : 'stationLevelName'
	}, {
		name : 'stationLevel'
	}, {
		name : 'retrieveCode'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'com/findStationLevelList.action'
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
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'stationLevelId',
			hidden : true
		}, {
			header : "岗位级别编码",
			width : 75,
			//hidden : true,
			sortable : true,
			dataIndex : 'stationLevel'
		}, {
			header : "岗位级别名称",
			width : 75,
			sortable : true,
			dataIndex : 'stationLevelName'
		}, {
			header : "检索码",
			width : 75,
			hidden : true,
			sortable : true,
			dataIndex : 'retrieveCode'
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['岗位级别名称：', fuzzy, {
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
	var wd = 200;

	var stationLevelId = new Ext.form.Hidden({
		id : "stationLevelId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value : '',
		name : 'level.stationLevelId'
	});

	var stationLevel = new Ext.form.NumberField({
		id : "stationLevel",
		fieldLabel : '岗位级别编码',
		allowBlank : false,
		width : wd,
		name : 'level.stationLevel'
	});

	var stationLevelName = new Ext.form.TextField({
		id : "stationLevelName",
		fieldLabel : '岗位级别名称',
		width : wd,
		name : 'level.stationLevelName'
	});

	var retrieveCode = new Ext.form.TextField({
		id : "retrieveCode",
		fieldLabel : '检索码',
		width : wd,
		name : 'level.retrieveCode'
	});
	stationLevelName.on("change", function(filed, newValue, oldValue) {
		Ext.Ajax.request({
			url : 'manager/getRetrieveCode.action',
			params : {
				name : newValue
			},
			method : 'post',
			success : function(result, request) {
				var json = result.responseText;
				var o = eval("(" + json + ")");
				Ext.get("retrieveCode").dom.value = o.substring(0, 8);
			}
		});
	});

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '增加/修改岗位级别信息',
		items : [stationLevelId, stationLevel, stationLevelName, retrieveCode]
	});

	function checkInput() {
		var msg = "";
		if (stationLevel.getValue() == "") {
			msg = "'岗位级别编码'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 320,
		height : 190,
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
				if (stationLevelId.getValue() == "") {
					myurl = "com/addStationLevel.action";
				} else {
					myurl = "com/updateStationLevel.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg.indexOf("成功") != -1) {
							queryRecord();
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
		store.load({
			params : {
				start : 0,
				limit : 18,
				stationLevelName : fuzzy.getValue()
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加岗位级别信息");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				stationLevelId.setValue(record.get("stationLevelId"));
				stationLevel.setValue(record.get("stationLevel"));
				stationLevelName.setValue(record.get("stationLevelName"));
				retrieveCode.setValue(record.get("retrieveCode"));

				myaddpanel.setTitle("修改岗位级别信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var records = grid.selModel.getSelections();
		var ids = [];
		var names = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("stationLevelId")) {
					ids.push(member.get("stationLevelId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteStationLevel.action', {
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