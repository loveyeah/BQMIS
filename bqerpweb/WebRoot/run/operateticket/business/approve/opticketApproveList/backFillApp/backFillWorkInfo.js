Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var operateCode = getParameter("opticketCode");
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var deleteWork = new Array();
	var formatData = new Ext.data.Record.create([{
		name : 'checkStatueId'
	}, {
		name : 'checkStatue'
	}]);
	var checkStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/findCheckStatus.action?checkBefFlag=A&isRunning=Y'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalCount : 'total'
		}, formatData)
	});
	checkStore.load();
	function resetLine() {
		for (var j = 0; j < workDs.getCount(); j++) {
			var temp = workDs.getAt(j);
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
	var worktbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (opticketCode == "") {
					Ext.Msg.alert('提示信息', '请先填写操作票！');
					return;
				}
				var currentRecord = workGrid.getSelectionModel().getSelected();
				var count = workDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new finishWork({
					finishWorkId : '',
					opticketCode : opticketCode,
					finishWorkName : '',
					displayNo : currentIndex,
					checkStatus : '',
					runAddFlag : '',
					memo : ''
				});
				workGrid.stopEditing();
				workDs.insert(currentIndex, o);
				workSm.selectRow(currentIndex);
				workGrid.startEditing(currentIndex, 2);
				resetLine();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = workGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("finishWorkId") != "") {
							deleteWork.push(member.get("finishWorkId"));
						}
						workGrid.getStore().remove(member);
						workGrid.getStore().getModifiedRecords().remove(member);
					}
					resetLine();
				}

			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				var modifyRec = workGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0 || deleteWork.length>0) {
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("finishWorkId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'opticket/updateFinishWorkList.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : deleteWork.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText+')');
							Ext.MessageBox.alert('提示信息', o.msg);
							workDs.rejectChanges();
							deleteWork = [];
							workDs.reload();
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
				workDs.reload();
				workDs.rejectChanges();
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
	}, {
		name : 'runAddFlag'
	}, {
		name : 'memo'
	}]);
	var workSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var workCm = new Ext.grid.ColumnModel([workSm,new Ext.grid.RowNumberer({
		header : '',
		width : 30,
		hidden : true,
		sortable : false,
		align : 'left'
	}), {
		header : '内容',
		dataIndex : 'finishWorkName',
		align : 'left',
		width : 300,
		anchor : "90%",
		sortable : false,
		// editor : new Ext.form.TextField({
		// allowBlank : false
		// })
		editor : new Ext.form.TextField({
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = workGrid.getSelectionModel().getSelected();
						var value = record.get('finishWorkName');
						workText.setValue(value);
						workWin.show();
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
		header : '落实情况',
		dataIndex : 'checkStatus',
		align : 'left',
		width : 240,
		anchor : "90%",
		sortable : false,
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			id : 'checkStatus',
			name : 'checkStatus',
			store : checkStore,
//			valueField : "checkStatue",
			mode : 'local',
			displayField : "checkStatue",
			selectOnFocus : true,
			listeners : {
				render : function(f) {
					f.el.on('change', function(e) {
						f.setValue(Ext.get("checkStatus").dom.value);
					});
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
		hidden : false,
		editor : new Ext.form.NumberField({
			allowBlank : false
		})
	}
//	, {
//		header : '备注',
//		dataIndex : 'memo',
//		align : 'left',
//		sortable : false,
//		// editor : new Ext.form.TextField({
//		// allowBlank : false
//		// })
//		editor : new Ext.form.TextArea({
//			listeners : {
//				"render" : function() {
//					this.el.on("dblclick", function() {
//						var record = workGrid.getSelectionModel().getSelected();
//						var value = record.get('memo');
//						memoText.setValue(value);
//						win.show();
//					})
//				}
//			// ,
//			// "change" : function() {
//			// if (this.el.getValue().length > 100) {
//			// gridMemoTextArea.setValue("");
//			// }
//			// }
//
//			}
//		})
//	}
	]);
	// workCm.defaultSortable = true;
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
		tbar : worktbar,
		// bbar : opticketStatus == "3" ? checkSteptbar : bar,
		frame : false,
		border : false,
		width : 680,
		autoScroll : true,
		clicksToEdit : 1
	});
	workGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = workGrid.getStore();
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
	if (opticketCode != "") {
		workDs.load({
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
		items : [workGrid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})