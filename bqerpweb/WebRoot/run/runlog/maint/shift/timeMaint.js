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
					id = selrows[0].data.shiftTimeId;
					Ext.Ajax.request({
						url : 'runlog/findShiftTimeModel.action',
						params : {
							id : id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.get('shiftTime.shiftSerial').dom.value = o[5];
							Ext.get('shiftTime.shiftTimeName').dom.value = o[2];
							Ext.get('onDutyTime').dom.value = o[3];
							Ext.get('offDutyTime').dom.value = o[4];
							if (o[6] != null) {
								Ext.get('shiftTime.shiftTimeDesc').dom.value = o[6];
							} else {
								Ext.get('shiftTime.shiftTimeDesc').dom.value = "";
							}
							nameBox.setValue(o[1]);
							restBox.setValue(o[7]);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
					addwin.setTitle("修 改");
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
							ids = selrows[0].data.shiftTimeId;
							Ext.Ajax.request({
								url : 'runlog/deleteShiftTime.action',
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
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'shiftTimeId'
	}, {
		name : 'initialNo'
	}, {
		name : 'initialName'
	}, {
		name : 'shiftTimeName'
	}, {
		name : 'onDutyTime'
	}, {
		name : 'offDutyTime'
	}, {
		name : 'shiftSerial'
	}, {
		name : 'shiftTimeDesc'
	}, {
		name : 'isRest'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '设置名称',
		dataIndex : 'initialName',
		align : 'center'
	}, {
		header : '班次名称',
		dataIndex : 'shiftTimeName',
		align : 'center'
	}, {
		header : '开始时间',
		dataIndex : 'onDutyTime',
		align : 'center'
	}, {
		header : '结束时间',
		dataIndex : 'offDutyTime',
		align : 'center'
	}, {
		header : '初始顺序',
		dataIndex : 'shiftSerial',
		align : 'center'
	}, {
		header : '是否休息班',
		dataIndex : 'isRest',
		align : 'center'
	}, {
		header : '说明',
		dataIndex : 'shiftTimeDesc',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftTimeList.action'
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
		id = record.data.shiftTimeId;
		Ext.Ajax.request({
			url : 'runlog/findShiftTimeModel.action',
			params : {
				id : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.get('shiftTime.shiftSerial').dom.value = o[5];
				Ext.get('shiftTime.shiftTimeName').dom.value = o[2];
				Ext.get('onDutyTime').dom.value = o[3];
				Ext.get('offDutyTime').dom.value = o[4];
				if (o[6] != null) {
					Ext.get('shiftTime.shiftTimeDesc').dom.value = o[6];
				} else {
					Ext.get('shiftTime.shiftTimeDesc').dom.value = "";
				}
				nameBox.setValue(o[1]);
				restBox.setValue(o[7]);
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
	// ---------------------------------------------------------------------
	var restBox = new Ext.form.ComboBox({
		fieldLabel : '是否休息班',
		store : [['Y', '是'], ['N', '否']],
		// id : 'shift.isShift',
		name : 'shiftTime.isRest',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'shiftTime.isRest',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '85%'
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
	nameStore.load({
		params : {
			specialCode : unitBox.value
		}
	});
	var nameBox = new Ext.form.ComboBox({
		fieldLabel : '设置名称',
		store : nameStore,
		valueField : "initialNo",
		displayField : "initialName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'shiftTime.initialNo',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'shiftTime.initialNo',
		anchor : '85%'
	});

	var content = new Ext.form.FieldSet({
		title : '班次时间设置',
		height : '100%',
		layout : 'form',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [nameBox, {
					xtype : 'timefield',
					fieldLabel : '开始时间',
					format : 'H:i:s',
					name : 'onDutyTime',
					id : 'onDutyTime',
					mode : 'local',
					increment : 15,
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					allowBlank : false,
					emptyText : '请选择',
					anchor : '85%'
				}, {
					name : 'shiftTime.shiftSerial',
					xtype : 'numberfield',
					fieldLabel : '初始次序',
					readOnly : false,
					anchor : '85%'
				}]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 80,
				border : false,
				items : [{
					name : 'shiftTime.shiftTimeName',
					xtype : 'textfield',
					fieldLabel : '班次名称',
					readOnly : false,
					allowBlank : false,
					anchor : '85%'
				}, {
					xtype : 'timefield',
					fieldLabel : '结束时间',
					format : 'H:i:s',
					name : 'offDutyTime',
					id : 'offDutyTime',
					mode : 'local',
					increment : 15,
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					checked : true,
					allowBlank : false,
					emptyText : '请选择',
					anchor : '85%'
				}, restBox]
			}]

		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [{
				id : 'shiftTime.shiftTimeDesc',
				xtype : 'textfield',
				fieldLabel : '班次说明',
				readOnly : false,
				// height : 120,
				anchor : '92.5%'
			}]
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if(!form.getForm().isValid())
				{
					return false;
				}
				form.getForm().submit({
					url : 'runlog/shiftTimeMaint.action',
					method : 'post',
					params : {
						method : method,
						id : id,
						onDutyTime : Ext.get('onDutyTime').dom.value,
						offDutyTime : Ext.get('offDutyTime').dom.value
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
				addwin.hide();
			}
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var addwin = new Ext.Window({
		title : '新增',
		el : 'win',
		autoHeight : true,
		modal:true,
		width : 600,
		closeAction : 'hide',
		items : [form]
	})
});