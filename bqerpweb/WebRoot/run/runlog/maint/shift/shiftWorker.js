Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var shiftId = getParameter('id');
	var shiftWorkerId;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '-1',
						text : '灞桥电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
				// var url = "deptEmpSelect.jsp?shiftId=" + shiftId;
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:550px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					Ext.Ajax.request({
						url : 'runlog/addShiftWorker.action',
						params : {
							ids : rvo.codes,
							deps : rvo.deptIds,
							shiftId : shiftId
						},
						method : 'post',
						waitMsg : '正在保存数据...',
						success : function(result, request) {
							Ext.Msg.alert('提示', '增加成功！');
							ds.load({
								params : {
									shiftId : shiftId
								}
							});
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							window.close();
						}
					});
				}
			}
		}, '-', {
			id : 'btnDel',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selectedRows = Grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							ids = selectedRows[0].data.shift_worker_id;
							Ext.Ajax.request({
								url : 'runlog/deleteShiftWorker.action',
								params : {
									shiftworkerid : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									for (i = 0; i < selectedRows.length; i++) {
										ds.remove(selectedRows[i]);
									}
									stat_ds.load();
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
			id : 'btnReflesh',
			text : "保存设置",
			iconCls : 'update',
			handler : function() {
				saveReason();
			}
		}, '-', {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.reload();
			}
		}]
	});
	function saveReason() {
		var str = "[";
		var record = Grid.getStore().getModifiedRecords();
		if (record.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				str = str + "{'shiftworkerid':" + data.get("shift_worker_id")
						+ ",'ishand':'" + data.get("isHand") + "' },"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";
			Ext.Ajax.request({
				url : 'runlog/updateShiftWorker.action',
				params : {
					data : str
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			alert("没有对数据进行任何修改！");
		}

	}
	var item = Ext.data.Record.create([{
		name : 'shift_worker_id'
	}, {
		name : 'emp_code'
	}, {
		name : 'emp_name'
	}, {
		name : 'isHand'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				shiftWorkerId = rec.data.shift_worker_id, stat_ds.load({
					params : {
						shiftWorkerId : rec.data.shift_worker_id
					}
				});
			}
		}
	});
	var item_cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer(), {
		header : '员工编号',
		dataIndex : 'emp_code',
		align : 'center'
	}, {
		header : '员工名称',
		dataIndex : 'emp_name',
		align : 'center'
	}, {
		header : '能否交接班',
		dataIndex : 'isHand',
		align : 'center',
		renderer : function(v) {
			return (v == 'Y' ? '能' : '否')
		},
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			store : [['Y', '能'], ['N', '否']]
		})
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftWorkerList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});
	ds.load({
		params : {
			shiftId : shiftId
		}
	});
	var Grid = new Ext.grid.EditorGridPanel({
		title : '值班人员设置',
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoScroll : true,
		tbar : tbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});

	var stat_tbar = new Ext.Toolbar({
		items : [{
			id : 'btnsAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (shiftWorkerId == null) {
					Ext.Msg.alert('提示', '请先选择值班人员！!');
				} else {
					addwin.show();
				}
			}
		}, '-', {
			id : 'btnsDel',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selectedRows = stat_Grid.getSelectionModel()
						.getSelections();
				if (selectedRows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							ids = selectedRows[0].data.workerStationId;
							Ext.Ajax.request({
								url : 'runlog/deleteShiftWorkerStation.action',
								params : {
									shiftWorkerStationId : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									for (i = 0; i < selectedRows.length; i++) {
										stat_ds.remove(selectedRows[i]);
									}
									stat_ds.commitChanges();
									stat_Grid.getView().refresh();
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
	var stat_item = Ext.data.Record.create([{
		name : 'workerStationId'
	}, {
		name : 'shiftWorkerId'
	}, {
		name : 'stationId'
	}, {
		name : 'stationName'
	}]);

	var stat_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var stat_item_cm = new Ext.grid.ColumnModel([stat_sm,
			new Ext.grid.RowNumberer(), {
				header : '岗位名称',
				dataIndex : 'stationName',
				align : 'center'
			}]);
	stat_item_cm.defaultSortable = true;
	var stat_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftWorkerStationList.action'
		}),
		reader : new Ext.data.JsonReader({}, stat_item)
	});

	var stat_Grid = new Ext.grid.EditorGridPanel({
		title : '值班人员岗位设置',
		ds : stat_ds,
		cm : stat_item_cm,
		sm : stat_sm,
		autoScroll : true,
		tbar : stat_tbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});

	var layout = new Ext.Viewport({
		layout : 'border',
		border : true,
		items : [{
			region : "center",
			layout : 'fit',
			width : 200,
			border : true,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [Grid]
		}, {
			region : "east",
			layout : 'fit',
			width : 200,
			border : true,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [stat_Grid]
		}]
	});
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var add_item = Ext.data.Record.create([{
		name : 'stationId'
	}, {
		name : 'stationName'
	}]);

	var add_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var add_item_cm = new Ext.grid.ColumnModel([add_sm, {
		header : '岗位ID',
		dataIndex : 'stationId',
		align : 'center'
	}, {
		header : '岗位名称',
		dataIndex : 'stationName',
		align : 'center'
	}]);
	add_item_cm.defaultSortable = true;
	var add_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findAllStationList.action'
		}),
		reader : new Ext.data.JsonReader({}, add_item)
	});
	var add_Grid = new Ext.grid.EditorGridPanel({
		ds : add_ds,
		cm : add_item_cm,
		sm : add_sm,
		tbar : [fuuzy, {
			text : "查询",
			iconCls : 'query',
			handler : function() {
				add_ds.load({
					params : {
						fuzzy : Ext.get('fuzzy').dom.value
					}
				})
			}
		}],
		width : 280,
		autoScroll : true,
		height : 325,
		border : true,
		viewConfig : {
			forceFit : false
		},
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var selectedRows = add_Grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = [];
					var names = [];
					for (var i = 0; i < selectedRows.length; i += 1) {
						var member = selectedRows[i].data;
						if (member.stationId) {
							ids.push(member.stationId);
							names.push(member.stationName);
						}
					}
					Ext.Ajax.request({
						url : 'runlog/addShiftWorkerStation.action',
						params : {
							ids : ids.join(","),
							names : names.join(","),
							shiftWorkerId : shiftWorkerId
						},
						method : 'post',
						waitMsg : '正在保存数据...',
						success : function(result, request) {
							stat_ds.reload();
							addwin.hide();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							stat_ds.reload();
							addwin.hide();
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要增加的岗位! ');
				}
			}
		}]
	});
	add_ds.load();
	var addwin = new Ext.Window({
		title : '增加值班人员岗位',
		el : 'win',
		// autoHeight : true,
		height : 350,
		width : 300,
		resizable : false,
		closeAction : 'hide',
		items : [add_Grid]
	})
});