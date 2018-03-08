Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'lbWorkId'
	}, {
		name : 'lbWorkName'
	}, {
		name : 'ifLbSpecialKind'
	}, {
		name : 'retrieveCode'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'com/findLaborWorkTypeList.action'
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
		columns : [sm, new Ext.grid.RowNumberer(), {
			header : "劳保工种ID",
			width : 75,
			sortable : true,
			hidden : true,
			dataIndex : 'lbWorkId'
		}, {
			header : "劳保工种名称",
			width : 75,
			sortable : true,
			dataIndex : 'lbWorkName'
		}, {
			header : "是否特殊劳保工种",
			width : 75,
			sortable : true,
			dataIndex : 'ifLbSpecialKind',
			renderer : function(v) {
				if (v == 'Y') {
					return "是";
				}
				if (v == 'N') {
					return "否";
				}
			}
		}, {
			header : "检索码",
			width : 75,
			sortable : true,
			dataIndex : 'retrieveCode'
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['劳保工种名称：', fuzzy, {
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

	var lbWorkId = new Ext.form.Hidden({
		id : "lbWorkId",
		fieldLabel : 'ID',
		anchor : '95%',
		readOnly : true,
		value : '',
		name : 'type.lbWorkId'

	});

	var lbWorkName = new Ext.form.TextField({
		id : "lbWorkName",
		fieldLabel : '劳保工种名称',
		allowBlank : false,
		anchor : '95%',
		name : 'type.lbWorkName'
	});

	var ifLbSpecialKind = new Ext.form.ComboBox({
		fieldLabel : '特殊劳保工种',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['Y', '是'], ['N', '否']]
		}),
		id : 'ifLbSpecialKind',
		name : 'ifLbSpecialKind',
		valueField : "value",
		displayField : "text",
		value : 'N',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'type.ifLbSpecialKind',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%'
	});

	var retrieveCode = new Ext.form.TextField({
		id : "retrieveCode",
		fieldLabel : '检索码',
		anchor : '95%',
		name : 'type.retrieveCode'
	});

	lbWorkName.on("change", function(filed, newValue, oldValue) {
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
		title : '增加/修改劳保工种信息',
		items : [lbWorkId, lbWorkName, ifLbSpecialKind, retrieveCode]
	});

	function checkInput() {
		var msg = "";
		if (lbWorkName.getValue() == "") {
			msg = "'劳保工种名称'";
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
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (Ext.get("lbWorkId").dom.value == ""
						|| Ext.get("lbWorkId").dom.value == null) {
					myurl = "com/addLaborWorkType.action";
				} else {
					myurl = "com/updateLaborWorkType.action";
				}
				if (!checkInput())
					return;
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
		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzytext : fuzzy.getValue()
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加劳保工种信息");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改劳保工种信息");
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
				if (member.lbWorkId) {
					ids.push(member.lbWorkId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteLaborWorkType.action', {
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