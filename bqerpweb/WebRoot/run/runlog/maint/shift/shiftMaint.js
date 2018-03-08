Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var method;
	var id;
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
	unitStore.on("load", function(xx, records, o) {
		unitBox.setValue(records[0].data.specialityCode);
		ds.load({
			params : {
				specialCode : records[0].data.specialityCode
			}
		});
	});

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		id : 'unitBox',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'specialityCode',
		width : 150
	});

	var tbar = new Ext.Toolbar({
		items : ['专业：', unitBox, '-', {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.load({
					params : {
						specialCode : unitBox.value
					}
				})
			}
		}, '-', {
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				nameStore.load({
					params : {
						specialCode : unitBox.value
					}
				})
				addwin.setTitle("新增");
				form.getForm().reset();
				addwin.show('btnAdd');
				method = 'add';
			}
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				nameStore.load({
					params : {
						specialCode : unitBox.value
					}
				});
				method = 'update';
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					id = selrows[0].data.shiftId;
					Ext.Ajax.request({
						url : 'runlog/findShiftModel.action',
						params : {
							id : id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							// form.getForm().load({
							// url : 'runlog/findShiftModel.action?id=' + id
							// });
							Ext.get('shift.shiftName').dom.value = o.shiftName;
							Ext.get('shift.shiftSequence').dom.value = o.shiftSequence;
							shiftBox.setValue(o.isShift);
							nameBox.setValue(o.initialNo);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
					addwin.setTitle("修改");
					addwin.show(Ext.get('btnUpdate'));
				} else {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
				}
			}
		}, '-', {
			id : 'btnDel',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							ids = selrows[0].data.shiftId;
							Ext.Ajax.request({
								url : 'runlog/deleteShift.action',
								params : {
									id : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									for (i = 0; i < selrows.length; i++) {
										ds.remove(selrows[i]);
									}
									ds.commitChanges();
									Grid.getView().refresh();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录!');
				}
			}
		}, '-', {
			id : 'btnAdd',
			text : "设置值班人员",
			iconCls : 'add',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var id = "";
					id = selrows[0].data.shiftId;
					var url = "shiftWorker.jsp?id=" + id;
					window.showModalDialog(url, '',
							'dialogWidth=650px;dialogHeight=500px;status=no');

				} else {
					Ext.Msg.alert('提示', '请选择您设置人员的班组!');
				}

			}
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'shiftId'
	}, {
		name : 'initialNo'
	}, {
		name : 'initialName'
	}, {
		name : 'shiftName'
	}, {
		name : 'shiftSequence'
	}, {
		name : 'isShift'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '设置名称',
		dataIndex : 'initialName',
		align : 'center'
	}, {
		header : '班组名称',
		dataIndex : 'shiftName',
		align : 'center'
	}, {
		header : '初始次序',
		dataIndex : 'shiftSequence',
		align : 'center'
	}, {
		header : '是否值班班组',
		dataIndex : 'isShift',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});

	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoWidth : true,
		autoScroll : true,
		tbar : tbar,
		border : false
			// ,
			// viewConfig : {
			// forceFit : false
			// }
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		nameStore.load({
			params : {
				specialCode : unitBox.value
			}
		});
		method = 'update';
		var record = Grid.getSelectionModel().getSelected()
		id = record.data.shiftId;
		Ext.Ajax.request({
			url : 'runlog/findShiftModel.action',
			params : {
				id : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				// form.getForm().load({
				// url : 'runlog/findShiftModel.action?id=' + id
				// });
				Ext.get('shift.shiftName').dom.value = o.shiftName;
				Ext.get('shift.shiftSequence').dom.value = o.shiftSequence;
				shiftBox.setValue(o.isShift);
				nameBox.setValue(o.initialNo);
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
		addwin.setTitle("修改");
		addwin.show(Ext.get('btnUpdate'));
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : Grid
		}]
	});
	// --------------------------------------------------------------------------------------------------------------------------------------------------------
	var shiftBox = new Ext.form.ComboBox({
		fieldLabel : '是否值班班组',
		store : [['1', '是'], ['0', '否'],['2','空']],
		// id : 'shift.isShift',
		name : 'shift.isShift',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'shift.isShift',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		width : 200
	});
	var nameStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findInitialBySpecial.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "name"
		}, [{
			name : 'initialNo'
		}, {
			name : 'initialName'
		}])
	});
	nameStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			specialCode : unitBox.value
		});
	});
	nameStore.load();
	var nameBox = new Ext.form.ComboBox({
		fieldLabel : '初始设置名称',
		store : nameStore,
		valueField : "initialNo",
		displayField : "initialName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'shift.initialNo',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'shift.initialNo',
		width : 200
	});
	var content = new Ext.form.FieldSet({
		title : '初始班组设置',
		height : '100%',
		layout : 'form',
		items : [nameBox, {
			name : 'shift.shiftName',
			xtype : 'textfield',
			fieldLabel : '班组名称',
			readOnly : false,
			allowBlank : false,
			width : 200
		}, {
			name : 'shift.shiftSequence',
			xtype : 'numberfield',
			fieldLabel : '初始次序',
			readOnly : false,
			width : 200
		}, shiftBox],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if(!form.getForm().isValid())
				{
					return false;
				}
				form.getForm().submit({
					url : 'runlog/shiftMaint.action',
					method : 'POST',
					params : {
						method : method,
						id : id
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						 Ext.Msg.alert("成功", message.data);
						ds.reload();
						addwin.hide();
					},
					failure : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
			}

		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				addwin.hide();
			}
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 100,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var addwin = new Ext.Window({
		title : '新增',
		el : 'win',
		modal:true,
		autoHeight : true,
		width : 400,
		closeAction : 'hide',
		items : [form]
	})
});