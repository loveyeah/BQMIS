Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'assistantManagerUnitsId'
	}, {
		name : 'assistantManagerUnitsName'
	}, {
		name : 'retrieveCode'
	}, {
		name : 'isUse'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'getAssistantUnits.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
	//分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, {

			header : "ID",
			sortable : true,
			dataIndex : 'assistantManagerUnitsId',
			hidden : true
		}, {
			header : "协理单位名称",
			width : 250,
			sortable : true,
			dataIndex : 'assistantManagerUnitsName',
			align : 'center'
		}, {
			header : "检索码",
			width : 250,
			sortable : true,
			dataIndex : 'retrieveCode',
			align : 'center'
		}, {
			header : "使用状态",
			width : 150,
			sortable : true,
			dataIndex : 'isUse',
			align : 'center'
		}],
		sm : sm,
		tbar : [{
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
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("dblclick", updateRecord);
	//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 200;

	var assistantManagerUnitsId = {
		id : "assistantManagerUnitsId",
		xtype : "hidden",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		name : 'unit.assistantManagerUnitsId'

	}
	var assistantManagerUnitsName = new Ext.form.TextField({
		id : "assistantManagerUnitsName",
		xtype : "textfield",
		fieldLabel : '协理单位名称',
		allowBlank : false,
		width : wd,
		name : 'unit.assistantManagerUnitsName'

	});

	var retrieveCode = {
		id : "retrieveCode",
		xtype : "textfield",
		fieldLabel : '检索码',
		name : 'unit.retrieveCode',
		width : wd
	}

	var isUse = {
		id : "isUse",
		xtype : "combo",
		name : 'unit.isUse',
		allowBlank : false,
		triggerAction : 'all',
//		store : new Ext.data.SimpleStore({
//			fields : ['text', 'value'],
//			data : [["启用", "U"], ["停用", "L"]]
//		}),
//		value : 'U',
		store : new Ext.data.SimpleStore({
			fields : ['text', 'value'],
			data : [["启用", "Y"], ["停用", "N"]]
		}),
		value : 'Y',
		hiddenName : 'unit.isUse',
		displayField : 'text',
		valueField : 'value',
		fieldLabel : "状态",
		mode : 'local',
		emptyText : '请选择状态...',
		blankText : '请选择状态',
		readOnly : true,
		width : wd
	}

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		title : '协理单位增加/修改',
		items : [assistantManagerUnitsId, assistantManagerUnitsName,
				retrieveCode, isUse]

	});

	var win = new Ext.Window({
		width : 400,
		height : 200,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			handler : function() {
				var myurl = "";
				if (Ext.get("assistantManagerUnitsId").dom.value == "") {
					myurl = "addAssistantUnits.action";

				} else {
					myurl = "updateAssistantUnits.action";

				}

				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
						win.hide();
					},//modify by drdu 091027
					failure : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert('错误', o.errMsg);
					}
						//					faliue : function() {
						//						Ext.Msg.alert('错误', '出现未知错误.');
						//					}
						});
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
			}
		}]

	});

	function addRecord() {
		myaddpanel.getForm().reset();
		win.setPosition(200, 100);
		win.show();
		assistantManagerUnitsName.on("change", function(filed, newValue,
				oldValue) {

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
		myaddpanel.setTitle("增加协理单位");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				assistantManagerUnitsName.on("change", function(filed,
						newValue, oldValue) {

					Ext.Ajax.request({
						url : 'manager/getRetrieveCode.action',
						params : {
							name : newValue
						},
						method : 'post',
						success : function(result, request) {
							var json = result.responseText;
							var o = eval("(" + json + ")");
							Ext.get("retrieveCode").dom.value = o.substring(0,
									8);
						}
					});
				});

				myaddpanel.getForm().load({
					url : "getSelectedUnit.action",
					params : {
						id : record.get("assistantManagerUnitsId")
					}

				});
				//myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改协理单位");
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
				if (member.assistantManagerUnitsId) {
					ids.push(member.assistantManagerUnitsId);
					names.push(member.assistantManagerUnitsName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST', 'deleteAssistantUnits.action',
							{
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'str=' + ids);
				}
			});
		}

	}

});