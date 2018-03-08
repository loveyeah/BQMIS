Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var operateCode = getParameter("opticketCode");
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var bar = new Ext.Toolbar({
		items : [{
			id : 'btnRefesh',
			text : "刷新",
			iconCls : 'refesh',
			handler : function() {
				checkStepDs.reload();
			}
		}]
	})
	var checkStep = Ext.data.Record.create([{
		name : 'stepCheckId'
	}, {
		name : 'opticketCode'
	}, {
		name : 'stepCheckName'
	}, {
		name : 'displayNo'
	}, {
		name : 'checkStatus'
	}, {
		name : 'runAddFlag'
	}, {
		name : 'memo'
	}]);
	var checkStepSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var checkStepCm = new Ext.grid.ColumnModel([{
		header : 'stepCheckId',
		dataIndex : 'stepCheckId',
		hidden : true
	}, {
		header : 'opticketCode',
		dataIndex : 'opticketCode',
		hidden : true
	}, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		sortable : false,
		align : 'left'
	}), {
		header : '内容',
		dataIndex : 'stepCheckName',
		align : 'left',
		width : 300,
		sortable : false,
		anchor : "90%",
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = checkStepGrid.getSelectionModel()
							.getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	}, {
		header : '落实情况',
		dataIndex : 'checkStatus',
		width : 250,
		align : 'left',
		sortable : false
	}, {
		header : '是否运行时补充',
		dataIndex : 'runAddFlag',
		align : 'left',
		hidden : true,
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			id : 'runAddFlag',
			name : 'runAddFlag',
			store : [['Y', '是'], ['N', '非']],
			valueField : "value",
			displayField : "text",
			mode : 'local',
			selectOnFocus : true
		})
	}, {
		header : '顺序号',
		dataIndex : 'displayNo',
		width : 30,
		align : 'left',
		sortable : false,
		editor : new Ext.form.NumberField({
			allowBlank : false,
			listeners : {
				focus : function() {
					var record = checkStepGrid.getSelectionModel()
							.getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'left',
		hidden : true,
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = checkStepGrid.getSelectionModel()
							.getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	}]);
	// checkStepCm.defaultSortable = true;
	var checkStepDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getCheckStepList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, checkStep)
	});
	// workDs.load();
	checkStepbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : checkStepDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var checkStepGrid = new Ext.grid.EditorGridPanel({
		store : checkStepDs,
		cm : checkStepCm,
		sm : checkStepSm,
		frame : false,
		border : false,
		width : 680,
		autoScroll : true,
		clicksToEdit : 1
	});
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		maxLength : 100,
		width : 180
	});
	var stepCheckText = new Ext.form.TextArea({
		id : "stepCheckText",
		maxLength : 100,
		width : 180
	});
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("memoText").dom.value.length <= 100) {
					var record = checkStepGrid.selModel.getSelected()
					record.set("memo", Ext.get("memoText").dom.value);
					win.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				win.hide();
			}
		}]
	});
	var stepCheckWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [stepCheckText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("stepCheckText").dom.value.length <= 100) {
					var record = checkStepGrid.selModel.getSelected()
					record.set("stepCheckName",
							Ext.get("stepCheckText").dom.value);
					stepCheckWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				stepCheckWin.hide();
			}
		}]
	});
	if (opticketCode != "") {
		checkStepDs.load({
			params : {
				opticketCode : opticketCode
			}
		})
	} else {
		Ext.Msg.alert("提示信息", "请先选择一个操作票！");
	}
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [checkStepGrid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})