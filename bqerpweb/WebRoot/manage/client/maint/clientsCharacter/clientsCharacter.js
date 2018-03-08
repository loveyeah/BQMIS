Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'characterId'
	}, {
		name : 'characterName'
	}, {
		name : 'memo'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'clients/getClientsCharacterList.action'
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
		columns : [sm,new Ext.grid.RowNumberer(), {
				header : "性质Id",
				width : 75,
				sortable : true,
				hidden : true,
				dataIndex : 'characterId'
			}, {
				header : "合作伙伴性质名称",
				width : 75,
				sortable : true,
				dataIndex : 'characterName'
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
		tbar : ['合作伙伴性质名称：', fuzzy, {
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

	var characterId = new Ext.form.Hidden({
		id : "characterId",
		fieldLabel : 'ID',
		anchor : '95%',
		readOnly : true,
		value : '',
		name : 'character.characterId'

	});

	var characterName = new Ext.form.TextField({
		id : "characterName",
		fieldLabel : '类型名称',
		allowBlank : false,
		anchor : '95%',
		name : 'character.characterName'
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		anchor : '95%',
		name : 'character.memo',
		height : 80
	});
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '增加/修改合作伙伴性质信息',
		items : [characterId, characterName, memo]
	});

	function checkInput() {
		var msg = "";
		if (characterName.getValue() == "") {
			msg = "'合作伙伴性质名称'";
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
				if (Ext.get("characterId").dom.value == ""
						|| Ext.get("characterId").dom.value == null) {
					myurl = "clients/addCharacter.action";
				} else {
					myurl = "clients/updateCharacter.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
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
		myaddpanel.setTitle("增加合作伙伴性质信息");
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
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改合作伙伴性质信息");
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
				if (member.characterId) {
					ids.push(member.characterId);
					names.push(member.characterName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'clients/deleteCharacter.action', {
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
							}, 'ids=' + ids);
				}
			});
		}
	}
	store.reload();

});