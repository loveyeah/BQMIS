Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function renderDate(value) {
	return value ? value.dateFormat('Y-m-d H:i:s') : '';
}
var operateCode = "";
Ext.onReady(function() {
	operateCode = parent.currentOpCode;
	var deleteOpstep = new Array();
	function resetLine() {
		for (var j = 0; j < operateStepDs.getCount(); j++) {
			var temp = operateStepDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
	var checkColumn = new Ext.grid.CheckColumn({
		header : '执行情况',
		dataIndex : 'execStatus',
		id : 'execStatus',
		width : 70,
		onMouseDown : function(e, t) {
			if (t.className
					&& t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
				e.stopEvent();
				var index = this.grid.getView().findRowIndex(t);
				var record = this.grid.store.getAt(index);
				record.set(this.dataIndex, !record.data[this.dataIndex]);
			}
		},
		renderer : function(v, p, record) {
			if (v != null) {
				if (v.toString() == "1") {
					v = true;
				} else if (v.toString() == "0") {
					v = false;
				}
			} else {
				v = false;
			}
			p.css += ' x-grid3-check-col-td';
			return '<div class="x-grid3-check-col' + (v ? '-on' : '')
					+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
		}

	});
	var operateSteptbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				var currentRecord = operateStepGrid.getSelectionModel()
						.getSelected();
				var count = operateStepDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new operateStep({
					operateStepId : '',
					opticketCode : operateCode,
					operateStepName : '',
					execStatus : '',
					runAddFlag : 'Y',
					finishTime : '',
					displayNo : currentIndex,
					memo : ''
				});
				operateStepGrid.stopEditing();
				operateStepDs.insert(currentIndex, o);
				operateStepSm.selectRow(currentIndex);
				operateStepGrid.startEditing(currentIndex,2);
				resetLine();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = operateStepGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("operateStepId") != "") {
							deleteOpstep.push(member.get("operateStepId"));
						}
						operateStepGrid.getStore().remove(member);
						operateStepGrid.getStore().getModifiedRecords()
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
				var modifyRec = operateStepGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0 || deleteOpstep.length > 0) {
					if (!confirm("确定要保存修改吗?"))
						return;
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if(modifyRec[i].get('operateStepName')!=''){
						if (modifyRec[i].get("operateStepId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}}else{
							Ext.Msg.alert('提示信息','操作步骤不能为空');
							return
						}
					}
					Ext.Ajax.request({
						url : 'opticket/updateOPContentList.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : deleteOpstep.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							operateStepDs.rejectChanges();
							deleteOpstep = [];
							operateStepDs.reload();
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
			iconCls : 'cancer',
			handler : function() {
				var modifyRec = operateStepDs.getModifiedRecords();
				if (modifyRec.length > 0 || deleteOpstep.length > 0) {
					if (!confirm("确定要放弃修改吗?"))
						return;
					operateStepDs.reload();
					operateStepDs.rejectChanges();
					deleteOpstep = []; 
				} else {
					operateStepDs.reload();
				    operateStepDs.rejectChanges();
				    deleteOpstep = []; 
				} 
			}
		}]
	})
	var operateStep = Ext.data.Record.create([{
		name : 'operateStepId'
	}, {
		name : 'opticketCode'
	}, {
		name : 'operateStepName'
	}, {
		name : 'execStatus'
	}, {
		name : 'runAddFlag'
	}, {
		name : 'finishTime'
	}, {
		name : 'displayNo'
	}, {
		name : 'memo'
	}]);
	var operateStepSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var operateStepCm = new Ext.grid.ColumnModel([operateStepSm,
			new Ext.grid.RowNumberer({
				header : '',
				hidden : true,
				width : 25
			}), {
				header : '内容',
				dataIndex : 'operateStepName',
				width : 600,
				sortable : false,
				renderer : function(value, metadata, record) {
					metadata.attr = 'style="white-space:normal;"';
					return value;
				}, 
				editor : new Ext.form.TextField({
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
								var record = operateStepGrid
										.getSelectionModel().getSelected();
								var value = record.get('operateStepName');
								operateStepText.setValue(value);
								operateStepWin.show();
							})
						}
					}
				})
			}, {
				header : '是否运行时补充',
				dataIndex : 'runAddFlag',
				align : 'center',
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
			}, {
				header : "完成时间",
				sortable : true,
				hidden : true,
				sortable : false,
				dataIndex : 'finishTime',
				renderer : function(value) {
					if (!value)
						return '';
					if (value instanceof Date)
						return renderDate(value);
					var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
					var reTime = /\d{2}:\d{2}:\d{2}/gi;
					var strDate = value.match(reDate);
					var strTime = value.match(reTime);
					if (!strDate)
						return "";
					strTime = strTime ? strTime : '00:00:00';
					return strDate + " " + strTime;
				},
				editor : new Ext.form.NumberField({
					readOnly : true,
					id : "temp",
					listeners : {
						focus : function() {
							WdatePicker({
								startDate : '%y-%M-%d 00:00:00',
								dateFmt : 'yyyy-MM-dd HH:mm:ss',
								alwaysUseStartDate : true,
								onpicked : function() {
									if (Ext.get("temp").dom.value < new Date()
											.dateFormat('Y-m-d 00:00:00')) {
										Ext.Msg.alert(Constants.ERROR,
												"完成时间不能小于当前时间！");
										return false;
									}
									operateStepGrid.getSelectionModel()
											.getSelected().set("finishTime",
													Ext.get("temp").dom.value);
								}
							});
						}
					}
				})
			}, {
				header : '排序',
				dataIndex : 'displayNo',
				align : 'center',
				width:80 
			}
			// , {
			// header : '备注',
			// dataIndex : 'memo',
			// align : 'center',
			// width :300,
			// editor : new Ext.form.TextArea({
			// listeners : {
			// "render" : function() {
			// this.el.on("dblclick", function() {
			// var record = operateStepGrid.getSelectionModel()
			// .getSelected();
			// var value = record.get('memo');
			// memoText.setValue(value);
			// win.show();
			// })
			// }
			// }
			// })
			// }
			// , checkColumn
	]);
	// operateStepCm.defaultSortable = true;
	var operateStepDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getContentOpticketsteps.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, operateStep)
	});
	// workDs.load();
	operateStepbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : operateStepDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var operateStepGrid = new Ext.grid.EditorGridPanel({
		store : operateStepDs,
		cm : operateStepCm,
		sm : operateStepSm,
		tbar : operateSteptbar, 
		frame : false,
		border : false,
		width : 400,
		autoScroll : true,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		}
	});
	operateStepGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = operateStepGrid.getStore();
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
	var operateStepText = new Ext.form.TextArea({
		id : "operateStepText",
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
					var record = operateStepGrid.selModel.getSelected()
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
	var operateStepWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [operateStepText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("operateStepText").dom.value.length <= 100) {
					var record = operateStepGrid.selModel.getSelected()
					record.set("operateStepName",
							Ext.get("operateStepText").dom.value);
					operateStepWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				operateStepWin.hide();
			}
		}]
	});
	function searchOpticketsteps(argOpticketCode) {
		operateStepDs.baseParams = {
			opticketCode : argOpticketCode
		};

		operateStepDs.load();
	}
	var viewport = new Ext.Viewport({
		layout : 'fit',
		// autoWidth : true,
		// autoHeight : true,
		fitToFrame : true,
		items : [operateStepGrid]
	});
	// searchOpticketsteps(register.opticketCode);
	searchOpticketsteps(operateCode);
})
Ext.grid.CheckColumn = function(config) {
	Ext.apply(this, config);
	if (!this.id) {
		this.id = Ext.id();
	}
	this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
	init : function(grid) {
		this.grid = grid;
		this.grid.on('render', function() {
			var view = this.grid.getView();
			view.mainBody.on('mousedown', this.onMouseDown, this);
		}, this);
	}
};