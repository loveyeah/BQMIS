Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var operateCode = getParameter("opticketCode");
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var deleteDanger = new Array();
	function resetLine() {
		for (var j = 0; j < measureDs.getCount(); j++) {
			var temp = measureDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
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
	var measuretbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (opticketCode == "") {
					Ext.Msg.alert('提示信息', '请先填写操作票！');
					return;
				}
				var currentRecord = measureGrid.getSelectionModel()
						.getSelected();
				var count = measureDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new conMeasure({
					dangerId : '',
					dangerName : '',
					opticketCode : opticketCode,
					runAddFlag : '',
					measureContent : '',
					displayNo : currentIndex,
					memo : ''
				});
				measureGrid.stopEditing();
				measureDs.insert(currentIndex, o);
				measureSm.selectRow(currentIndex);
				measureGrid.startEditing(currentIndex, 2);
				resetLine();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = measureGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("dangerId") != "") {
							deleteDanger.push(member.get("dangerId"));
						}
						measureGrid.getStore().remove(member);
						measureGrid.getStore().getModifiedRecords()
								.remove(member);
					}
					resetLine();
				}
			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				var modifyRec = measureGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0 || deleteDanger.length>0) {
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("dangerId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'opticket/updateDangerMeasureList.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : deleteDanger.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText+')');
							Ext.MessageBox.alert('提示信息', o.msg);
							measureDs.rejectChanges();
							deleteDanger = [];
							measureDs.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
				} else {
					Ext.MessageBox.alert('提示信息', '没有做任何修改！')
				}

			}
		}, '-', {
			id : 'btnCanse',
			text : "取消",
			iconCls : 'reflesh',
			handler : function() {
				measureDs.reload();
				measureDs.rejectChanges();
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
	var measureSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var measureCm = new Ext.grid.ColumnModel([measureSm,new Ext.grid.RowNumberer({
		header : '',
		width : 30,
		hidden : true,
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
			allowBlank : false
		})
	}, {
		header : '控制措施内容',
		dataIndex : 'measureContent',
		align : 'left',
		width : 200,
		anchor : "90%",
		sortable : false,
		// editor : new Ext.form.TextField({
		// allowBlank : false
		// })
		editor : new Ext.form.TextField({
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = measureGrid.getSelectionModel()
								.getSelected();
						var value = record.get('measureContent');
						measureText.setValue(value);
						measureWin.show();
					})
				}
			// ,
			// "change" : function() {
			// if (this.el.getValue().length > 100) {
			// gridstepCheckTextArea.setValue("");
			// }
			// }

			}
		})
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
		align : 'left',
		sortable : false,
		editor : new Ext.form.TextField({
			allowBlank : false
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
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = measureGrid.getSelectionModel()
								.getSelected();
						var value = record.get('memo');
						memoText.setValue(value);
						win.show();
					})
				}
			// ,
			// "change" : function() {
			// if (this.el.getValue().length > 100) {
			// gridMemoTextArea.setValue("");
			// }
			// }

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
		tbar : measuretbar,
		// bbar : opticketStatus == "3" ? checkSteptbar : bar,
		frame : false,
		border : false,
		width : 680,
		autoScroll : true,
		clicksToEdit : 1
	});
	measureGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = measureGrid.getStore();
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(0, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i - 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i + 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(_store.getCount(), record);
						resetLine();
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
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