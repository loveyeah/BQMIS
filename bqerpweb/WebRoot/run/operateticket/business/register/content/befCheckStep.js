Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var operateCode = getParameter("opCode");
	var deleteStepCheck = new Array();
	var formatData = new Ext.data.Record.create([{
		name : 'checkStatueId'
	}, {
		name : 'checkStatue'
	}]);
	var checkStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/findCheckStatus.action?checkBefFlag=B&isRunning=N'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalCount : 'total'
		}, formatData)
	});
	checkStore.load();
	function resetLine() {
		for (var j = 0; j < checkStepDs.getCount(); j++) {
			var temp = checkStepDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
	var checkSteptbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (operateCode == "") {
					Ext.Msg.alert('提示信息', '请先填写操作票！');
					return;
				}
				var currentRecord = checkStepGrid.getSelectionModel()
						.getSelected();
				var count = checkStepDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new checkStep({
					stepCheckId : '',
					opticketCode : operateCode,
					stepCheckName : '',
					runAddFlag : 'Y',
					displayNo : currentIndex,
					checkStatus : '',
					memo : ''
				});
				checkStepGrid.stopEditing();
				checkStepDs.insert(currentIndex, o);
				checkStepSm.selectRow(currentIndex);
				checkStepGrid.startEditing(currentIndex, 3);
				resetLine();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = checkStepGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("stepCheckId") != "") {
							deleteStepCheck.push(member.get("stepCheckId"));
						}
						checkStepGrid.getStore().remove(member);
						checkStepGrid.getStore().getModifiedRecords()
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
				var modifyRec = checkStepGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0 || deleteStepCheck.length>0) {
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("stepCheckId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'opticket/updateBefCheckStepList.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : deleteStepCheck.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText+')');
							Ext.MessageBox.alert('提示信息', o.msg)
							checkStepDs.rejectChanges();
							deleteStepCheck = [];
							checkStepDs.reload();
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
				checkStepDs.reload();
				checkStepDs.rejectChanges();
			}
		}, '->', '当前操作票编号:[ ' + getParameter('opCode') + ' ]']
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
	var checkStepSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var checkStepCm = new Ext.grid.ColumnModel([checkStepSm,
	new Ext.grid.RowNumberer({
		header : '',
		width : 30,
		hidden : true,
		sortable : false,
		sortable : false,
		align : 'left'
	}), {
		dataIndex : 'runAddFlag',
		sortable : false,
		width : 30,
		hidden : true,
		renderer : function(val) {
			if (val == 'N') {
				return "<font color='gray'>IIIIIIIII</font>";
			} else {
				return "<font color='green'>IIIIIIIII</font>";
			}
		}

	}, {
		header : '内容',
		dataIndex : 'stepCheckName',
		align : 'left',
		width : 230,
		sortable : false,
		renderer : function(v) {
			return "<div style='white-space:normal;'>" + v + "</div>";
		},
		editor : new Ext.form.TextField({
			listeners : {
				focus : function() {
					var record = checkStepGrid.getSelectionModel()
							.getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					} else {
						this.el.on("dblclick", function() {
							var _record = checkStepGrid.getSelectionModel()
									.getSelected();
							var value = _record.get('stepCheckName');
							stepCheckText.setValue(value);
							stepCheckWin.show();
						})
					}
				}
			}
		})
	}, {
		header : '落实情况',
		dataIndex : 'checkStatus',
		align : 'left',
		width : 280,
		sortable : false,
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			id : 'checkStatus',
			name : 'checkStatus',
			store : checkStore,
//			valueField : "checkStatue",
			displayField : "checkStatue",
			selectOnFocus : true,
			mode : 'local',
			sortable : false,
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
		sortable : false,
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
	}
			// , {
			// header : '备注',
			// dataIndex : 'memo',
			// align : 'left',
			// width : 300,
			// editor : new Ext.form.TextArea({
			// listeners : {
			// focus : function() {
			// var record = checkStepGrid.getSelectionModel().getSelected();
			// if (record.get("runAddFlag") == 'N') {
			// this.triggerAction='query';
			// this.blur();
			// } else {
			// this.el.on("dblclick", function() {
			// this.triggerAction='all';
			// var value = record.get('memo');
			// memoText.setValue(value);
			// win.show();
			// })
			// }
			// }
			// }
			// })
			// }
			, {
				header : '顺序号',
				dataIndex : 'displayNo',
				align : 'left',
				sortable : false,
				hidden : true,
				editor : new Ext.form.NumberField({
					allowBlank : false
				})
			}]);
	// checkStepCm.defaultSortable = true;
	var checkStepDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getChecksteps.action'
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
		tbar : checkSteptbar,
		// bbar : workbbar,
		frame : false,
		border : false,
		width : 400,
		autoScroll : true,
		clicksToEdit : 1
	});
	checkStepGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = checkStepGrid.getStore();
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
			iconCls : 'reflesh',
			handler : function() {
				stepCheckWin.hide();
			}
		}]
	});
	if (operateCode != "") {
		checkStepDs.load({
			params : {
				opticketCode : operateCode
			}
		})
	} else {
		Ext.Msg.alert("提示信息", "请先选择一个操作票！");
	}
	var viewport = new Ext.Viewport({
		layout : 'fit',
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