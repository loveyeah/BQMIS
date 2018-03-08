Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'planTypeId'
	}, {
		name : 'planTypeName'
	}, {
		name : 'memo'
	}, {
		name : 'isUse'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'equplan/findEquPlanTypeList.action'
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
			dataIndex : 'planTypeId',
			hidden : true
		}, {
			header : "类型名称",
			width : 75,
			sortable : true,
			dataIndex : 'planTypeName'
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
		tbar : ['类型名称：', fuzzy, {
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
	var wd = 220;

	var planTypeId = new Ext.form.Hidden({
		id : "planTypeId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value : '',
		name : 'type.planTypeId'
	});

	var planTypeName = new Ext.form.TextField({
		id : "planTypeName",
		fieldLabel : '类型名称',
		allowBlank : false,
		width : wd,
		name : 'type.planTypeName'
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		width : wd,
		name : 'type.memo',
		height : 80

	});
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '增加/修改计划类型信息',
		items : [planTypeId, planTypeName, memo]
	});

	function checkInput() {
		var msg = "";
		if (planTypeName.getValue() == "") {
			msg = "'类型名称'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 350,
		height : 200,
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
				if (planTypeId.getValue() == "") {
					myurl = "equplan/addEquPlanType.action";
				} else {
					myurl = "equplan/updateEquPlanType.action";
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
				planTypeName : fuzzy.getValue()
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加计划类型信息");
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
				planTypeId.setValue(record.get("planTypeId"));
				planTypeName.setValue(record.get("planTypeName"));
				memo.setValue(record.get("memo"));

				myaddpanel.setTitle("修改计划类型信息");
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
				if (member.get("planTypeId")) {
					ids.push(member.get("planTypeId"));
					names.push(member.get("planTypeName"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'equplan/deleteEquPlanType.action', {
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