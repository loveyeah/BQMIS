Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var operateCode = getParameter("opticketCode");
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var methods = "update";
	var url = "opticket/findCheckStatus.action?";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var checkStatusData = eval('(' + conn.responseText + ')').list;
	var formatData = new Ext.data.Record.create([{
		name : 'checkStatueId'
	}, {
		name : 'checkStatue'
	}]);

	var checkStore = new Ext.data.JsonStore({
		data : checkStatusData,
		fields : formatData
	});
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
	var conMeasure = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'opticketCode'
	}, {
		name : 'dangerName'
	}, {
		name : 'measureContent'
	}, {
		name : 'runAddFlag'
	}, {
		name : 'displayNo'
	}, {
		name : 'memo'
	}]);
	var measureSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var measureCm = new Ext.grid.ColumnModel([{
		header : 'dangerId',
		dataIndex : 'dangerId',
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
		header : '危险点',
		dataIndex : 'dangerName',
		align : 'left',
		width : 300,
		anchor : "90%",
		sortable : false,
		editor : new Ext.form.TextField({
			allowBlank : false,
			listeners : {
				focus : function() {
					var record = measureGrid.getSelectionModel().getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	}, {
		header : '控制措施内容',
		dataIndex : 'measureContent',
		align : 'left',
		width : 200,
		anchor : "90%",
		sortable : false,
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = measureGrid.getSelectionModel().getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}

			}
		})
	}, {
		header : '是否运行时补充',
		dataIndex : 'runAddFlag',
		align : 'left',
		hidden : true,
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			// triggerAction : 'all',
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
		align : 'left',
		width : 80,
		sortable : false,
		editor : new Ext.form.TextField({
			allowBlank : false,
			listeners : {
				focus : function() {
					var record = measureGrid.getSelectionModel().getSelected();
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
		// editor : new Ext.form.TextField({
		// allowBlank : false
		// })
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = measureGrid.getSelectionModel().getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	}]);
	// measureCm.defaultSortable = true;
	var measureDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getDangerousList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, conMeasure)
	});
	measurebbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : measureDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	function searchMeasures(argOpticketCode) {
		measureDs.baseParams = {
			opticketCode : argOpticketCode
		};

		measureDs.load();
	}
	var measureGrid = new Ext.grid.EditorGridPanel({
		store : measureDs,
		cm : measureCm,
		sm : measureSm,
		// tbar : measuretbar,
		// bbar : opticketStatus == "3" ? checkSteptbar : bar,
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
	var measureText = new Ext.form.TextArea({
		id : "measureText",
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
					var record = measureGrid.selModel.getSelected()
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
	var measureWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [measureText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("measureText").dom.value.length <= 100) {
					var record = measureGrid.selModel.getSelected()
					record.set("measureContent",
							Ext.get("measureText").dom.value);
					measureWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				measureWin.hide();
			}
		}]
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [measureGrid]
	});
	searchMeasures(opticketCode);
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})