Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 
Ext.onReady(function() {
	var taskId = "";
	var deleteDanger = new Array();
	var url = "opticket/findCheckStatus.action?";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	function resetLine() {
		for (var j = 0; j < measureDs.getCount(); j++) {
			var temp = measureDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
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
	var measuretbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				
				var currentRecord = measureGrid.getSelectionModel()
						.getSelected();
				var count = measureDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo"): count;
				var o = new conMeasure({
					dangerId : '',
					dangerName : '',
					operateTaskId : 0,
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
				if (modifyRec.length > 0 || deleteDanger.length>0 ) {
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
						url : 'opticket/updateConMeasureList.action',
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
							Ext.MessageBox.alert('提示信息', '未知错误！');
						}
					})
				}
				else
				{
					Ext.MessageBox.alert('提示', '您没有做任何修改！');
				}

			}
		}, '-', {
			id : 'btnCanse',
			text : "取消",
			iconCls : 'cancer',
			handler : function() {
				measureDs.reload();
				measureDs.rejectChanges();
			}
		}]
	})
	var conMeasure = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'operateTaskId'
	}, {
		name : 'dangerName'
	}, {
		name : 'measureContent'
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
	var measureCm = new Ext.grid.ColumnModel([measureSm,
	new Ext.grid.RowNumberer({
		header : '行号',
		width : 40,
		hidden : true,
		sortable : false,
		align : 'left'
	}), {
		header : '危险点',
		width : 200,
		dataIndex : 'dangerName',
		sortable : false,
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '控制措施内容',
		width : 300,
		sortable : false,
		dataIndex : 'measureContent',
		editor : new Ext.form.TextArea({
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
			}
		})
	}, {
		header : '顺序号',
		dataIndex : 'displayNo',
		align : 'center',
		sortable : false,
		editor : new Ext.form.NumberField({
			allowBlank : false
		})
	}, {
		header : '备注',
		dataIndex : 'memo',
		sortable : false,
		width : 300,
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
			}
		})
	}]);
	var measureDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/findDangerMeasure.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, conMeasure)
	});
	// workDs.load();
	measurebbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : measureDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var measureGrid = new Ext.grid.EditorGridPanel({
		store : measureDs,
		cm : measureCm,
		sm : measureSm,
		tbar : measuretbar,
		// bbar : workbbar,
		frame : false,
		border : false,
		autoWidth : true,
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
			text : "确定",
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
			text : "确定",
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
		layout : 'border',
		items : [
		// {
		// region : 'west',
		// layout : 'fit',
		// split : true,
		// width : 200,
		// autoScroll : true,
		// collapsible : true,
		// border : false,
		// items : [treePanel]
		// },
		{
			region : 'center',
			layout : 'fit',
			items : [measureGrid]
		}]
	});
	measureDs.load({
		params : {
			operateTaskId : 0
		}
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})