Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	//	var operateCode = getParameter("opticketCode");
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
	var finishWork = Ext.data.Record.create([{
		name : 'finishWorkId'
	}, {
		name : 'opticketCode'
	}, {
		name : 'finishWorkName'
	}, {
		name : 'displayNo'
	}, {
		name : 'checkStatus'
	},{
		name : 'runAddFlag'
	}, {
		name : 'memo'
	}]);
	var workSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var workCm = new Ext.grid.ColumnModel([{
		header : 'finishWorkId',
		dataIndex : 'finishWorkId',
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
		dataIndex : 'finishWorkName',
		align : 'left',
		width : 300,
		sortable : false,
		anchor : "90%",
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = workGrid.getSelectionModel()
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
		align : 'left',
		width : 250,
		anchor : "90%",
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
		hidden : false,
		editor : new Ext.form.TextField({
			allowBlank : false,
			listeners : {
				focus : function() {
					var record = workGrid.getSelectionModel()
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
		sortable : false,
		hidden : true,
		editor : new Ext.form.TextArea({
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = workGrid.getSelectionModel().getSelected();
						var value = record.get('memo');
						memoText.setValue(value);
						win.show();
					})
				}
			}
		})
	}]);
	var workDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getFinishWorkList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, finishWork)
	});
	
	// workDs.load();
	workbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : workDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var workGrid = new Ext.grid.EditorGridPanel({
		store : workDs,
		cm : workCm,
		sm : workSm,
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
	var workText = new Ext.form.TextArea({
		id : "workText",
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
					var record = workGrid.selModel.getSelected()
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
	var workWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [workText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("workText").dom.value.length <= 100) {
					var record = workGrid.selModel.getSelected()
					record.set("finishWorkName", Ext.get("workText").dom.value);
					workWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				workWin.hide();
			}
		}]
	});
	if(opticketCode!=""){
		workDs.load({params : {
		opticketCode : opticketCode
	}})
	}else{
		Ext.Msg.alert("提示信息","请先选择一个操作票！");
	}
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [workGrid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})