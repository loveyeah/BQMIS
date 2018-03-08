Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var method;
	var id;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnReflesh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : function() {
				ds.load();
			}
		}, '-', {
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
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
				method = 'update';
				var selrows = shiftInitialGrid.getSelectionModel()
						.getSelections();
				if (selrows.length > 0) {
					id = selrows[0].data.initialNo;
					Ext.Ajax.request({
						url : 'runlog/findInitialModel.action',
						params : {
							id : id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.get('shiftInitial.initialName').dom.value = o[1];
							Ext.get('shiftInitial.shiftAmount').dom.value = o[3];
							Ext.get("shiftInitial.timeAmount").dom.value = o[4];
							Ext.get("shiftInitial.activeDate").dom.value = o[5];
							Ext.get("shiftInitial.disconnectDate").dom.value = o[6];
							unitBox.setValue(o[2]);
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
				var selectedRows = shiftInitialGrid.getSelectionModel()
						.getSelections();
				if (selectedRows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							ids = selectedRows[0].data.initialNo;
							Ext.Ajax.request({
								url : 'runlog/deleteInitial.action',
								params : {
									id : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									for (i = 0; i < selectedRows.length; i++) {
										ds.remove(selectedRows[i]);
									}
									ds.commitChanges();
									shiftInitialGrid.getView().refresh();
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
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'initialNo'
	}, {
		name : 'initialName'
	}, {
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}, {
		name : 'shiftAmount'
	}, {
		name : 'timeAmount'
	}, {
		name : 'activeDate'
	}, {
		name : 'disconnectDate'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
			// listeners : {
			// rowselect : function(sm, row, rec) {
			// Ext.getCmp("bolock-form").getForm().loadRecord(rec);
			// }
			// }
	});
	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '设置名称',
		dataIndex : 'initialName',
		align : 'center'
	}, {
		header : '专业',
		dataIndex : 'specialityName',
		width : 150,
		align : 'center'
	}, {
		header : '班组个数',
		dataIndex : 'shiftAmount',
		width : 80,
		align : 'center'
	}, {
		header : '班次个数',
		dataIndex : 'timeAmount',
		width : 80,
		align : 'center'
	}, {
		header : '生效时间',
		dataIndex : 'activeDate',
		width : 120,
		align : 'center'
	}, {
		header : '失效时间',
		dataIndex : 'disconnectDate',
		width : 120,
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findInitialList.action'
		}),
		reader : new Ext.data.JsonReader({
				// root : 'root'
				}, item)
	});
	ds.load();
	var shiftInitialGrid = new Ext.grid.GridPanel({
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

	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : shiftInitialGrid
		}]
	});
	// ---------------------------------------------------------------------
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

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'shiftInitial.specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'shiftInitial.specialityCode',
		anchor : '90%'
	});

	var content = new Ext.form.FieldSet({
		title : '值班初始化信息',
		height : '100%',
		layout : 'form',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 65,
				items : [{
					name : 'shiftInitial.initialName',
					xtype : 'textfield',
					fieldLabel : '设置名称',
					readOnly : false,
					anchor : '90%',
					allowBlank : false
				}, {
					name : 'shiftInitial.shiftAmount',
					xtype : 'numberfield',
					fieldLabel : '班组个数',
					value : '0',
					readOnly : true,
					anchor : '90%'
				}, {
					xtype : 'datefield',
					format : 'Y-m-d',// 此处换为'Y'即可
					fieldLabel : '生效日期',
					name : 'shiftInitial.activeDate',
					// value : '2008-06',
					id : 'shiftInitial.activeDate',
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					readOnly : true,
					checked : true,
					emptyText : '请选择',
					anchor : '90%',
					allowBlank : false
				}]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 65,
				border : false,
				items : [unitBox, {
					name : 'shiftInitial.timeAmount',
					xtype : 'numberfield',
					fieldLabel : '班次个数',
					value : '0',
					readOnly : true,
					anchor : '90%'
				}, {
					xtype : 'datefield',
					format : 'Y-m-d',// 此处换为'Y'即可
					fieldLabel : '失效日期',
					name : 'shiftInitial.disconnectDate',
					// value : '2008-06',
					id : 'shiftInitial.disconnectDate',
					itemCls : 'sex-left',
					readOnly : true,
					clearCls : 'allow-float',
					checked : true,
					emptyText : '请选择',
					anchor : '90%',
					allowBlank : false
				}]
			}]

		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				var str1=Ext.get('shiftInitial.activeDate').dom.value;
				var str2=Ext.get('shiftInitial.disconnectDate').dom.value;
				str11 = new Date(str1.replace("-", ",")).getTime();
				str22 = new Date(str2.replace("-", ",")).getTime();
				if(!(str22 > str11))
				{
					Ext.Msg.alert('提示', '失效日期必须比生效日期大.');
					return false;
				}
				form.getForm().submit({
					url : 'runlog/initialMaint.action',
					method : 'post',
					params : {
						method : method,
						id : id
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						// Ext.Msg.alert("成功", message.data);
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
	shiftInitialGrid.on('rowdblclick', function(grid, rowIndex, e) {
		method = 'update';
		var record = shiftInitialGrid.getSelectionModel().getSelected()
		id = record.data.initialNo;
		Ext.Ajax.request({
			url : 'runlog/findInitialModel.action',
			params : {
				id : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.get('shiftInitial.initialName').dom.value = o[1];
				Ext.get('shiftInitial.shiftAmount').dom.value = o[3];
				Ext.get("shiftInitial.timeAmount").dom.value = o[4];
				Ext.get("shiftInitial.activeDate").dom.value = o[5];
				Ext.get("shiftInitial.disconnectDate").dom.value = o[6];
				unitBox.setValue(o[2]);
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
		addwin.setTitle("修改");
		addwin.show(Ext.get('btnUpdate'));
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 65,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var addwin = new Ext.Window({
		title : '新增',
		el : 'win',
		autoHeight : true,
		width : 500,
		modal : true,
		closeAction : 'hide',
		items : [form]
	})
});