Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function renderDate(value) {
	return value ? value.dateFormat('Y-m-d H:i:s') : '';
}
Ext.onReady(function() {
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var deleteOpstep = new Array();
	var opPerson;
	var prPerson;
	function resetLine() {
		for (var j = 0; j < operateStepDs.getCount(); j++) {
			var temp = operateStepDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
//	var label = new Ext.form.Label({
//		text : '监护人:'
//	});
	var oprateMan = new Ext.form.TriggerField({ // TriggerField
		fieldLabel : '操作人',
		blankText : '请选择操作人',
		emptyText : '请选择操作人',
		name : 'operatorName',
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single'
			}
			opPerson = window
					.showModalDialog(
							"../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp",
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (opPerson != null) {
				oprateMan.setValue(opPerson.workerName);
				var rec = operateStepGrid.getSelectionModel().getSelections();
				if (rec.length > 0) {
					for (i = 0; i < rec.length; i++) {
						rec[i].set("execName", opPerson.workerName);
						rec[i].set("execMan", opPerson.workerCode);
					}
					var s=parent.app.document.getElementById("rem").value;
					if(s.indexOf(opPerson.workerName)==-1){
						s+=opPerson.workerName;
					}
					parent.app.document.getElementById("rem").value=s;
				}
			}
		}
	})
//	var protectMan = new Ext.form.TriggerField({ // TriggerField
//		fieldLabel : '监护人',
//		blankText : '请选择监护人',
//		emptyText : '请选择监护人',
//		name : 'protectName',
//		onTriggerClick : function(e) {
//			var args = {
//				selectModel : 'single'
//			}
//			prPerson = window
//					.showModalDialog(
//							"../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp",
//							args,
//							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//			if (prPerson != null) {
//				protectMan.setValue(prPerson.workerName);
//				var rec = operateStepGrid.getSelectionModel().getSelections();
//				for (i = 0; i < rec.length; i++) {
//					rec[i].set("proName", prPerson.workerName);
//					rec[i].set("proMan", opPerson.workerCode);
//				}
//			}
//		}
//	})
	var checkAll = new Ext.form.Checkbox({
		id : 'checkAll',
		name : 'checkAll',
		boxLabel : '全选',
		listeners : {
			check : function(box, checked) {
				var _totalCount = operateStepGrid.getStore().getCount();
				var ischeck = "0";
				if (checked) {
					ischeck = "1"
				} else {
					ischeck = "0"
				}
				for (var i = 0; i < _totalCount; i++) {
					var r = operateStepGrid.getStore().getAt(i);
					if (r.get("execStatus") != ischeck)
						r.set("execStatus", ischeck);
				}
			}
		}
	})
	var checkColumn = new Ext.grid.CheckColumn({
		header : '执行情况',
		dataIndex : 'execStatus',
		id : 'execStatus',
		name : 'execStatus',
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
	// 执行情况检索
	var storeCbx = new Ext.data.SimpleStore({
		fields : ['name', 'id'],
		data : [["已执行", "1"], ["未执行", "0"]]
	})
	// 执行情况组合框
	var execStatusCbo = new Ext.form.ComboBox({
		id : "execStatusCbo",
		fieldLabel : '执行情况',
		store : storeCbx,
		triggerAction : 'all',
		displayField : "name",
		valueField : "id",
		mode : 'local',
		selectOnFocus : true,
		readOnly : true,
		emptyText : '所有',
		blankText : '所有',
		hiddenName : 'operateStep.execStatus'
	});
	var operateSteptbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (opticketCode == "") {
					Ext.Msg.alert('提示信息', '请选择操作任务！');
					return;
				}
				var currentRecord = operateStepGrid.getSelectionModel()
						.getSelected();
				var count = operateStepDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new operateStep({
					operateStepId : '',
					opticketCode : opticketCode,
					operateStepName : '',
					execStatus : '',
					runAddFlag : '',
					finishTime : '',
					displayNo : currentIndex,
					memo : '',
					execMan : '',
					proMan : '',
					execName : '',
					proName : ''
				});
				operateStepGrid.stopEditing();
				operateStepDs.insert(currentIndex, o);
				operateStepSm.selectRow(currentIndex);
				operateStepGrid.startEditing(currentIndex, 2);
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
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("operateStepId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
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
			iconCls : 'reflesh',
			handler : function() {
				operateStepDs.reload();
				operateStepDs.rejectChanges();
			}
		}, '-', checkAll, '-', '操作人:', oprateMan]
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
	}, {
		name : 'execMan'
	}, {
		name : 'proMan'
	}, {
		name : 'execName'
	}, {
		name : 'proName'
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
				width : 30,
				hidden : true,
				sortable : false,
				align : 'left'
			}), {
				header : '内容',
				dataIndex : 'operateStepName',
				align : 'left',
				width : 300,
				anchor : "90%",
				sortable : false,
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
				header : "完成时间",
				sortable : false,
				dataIndex : 'finishTime',
				hidden : false,
				renderer : function(value) {
					if (!value)
						return ''
					if (value instanceof Date)
						return renderDate(value);
					var reTime = /\d{2}:\d{2}/gi;
					var strTime = value.match(reTime);
					strTime = strTime ? strTime : '00:00';
					return strTime;
				},
				editor : new Ext.form.TextField({
					readOnly : true,
					id : "temp",
					listeners : {
						focus : function() {
							WdatePicker({
								startDate : '00:00',
								dateFmt : 'HH:mm',
								alwaysUseStartDate : true,
								onpicked : function() {
									// if (Ext.get("temp").dom.value < new
									// Date()
									// .dateFormat('Y-m-d 00:00:00')) {
									// Ext.Msg.alert(Constants.ERROR,
									// "完成时间不能小于当前时间！");
									// return false;
									// }
									operateStepGrid.getSelectionModel()
											.getSelected().set("finishTime",
													Ext.get("temp").dom.value);
								}
							});
						}
					}
				})
			}, {
				header : '顺序号',
				dataIndex : 'displayNo',
				align : 'center',
				hidden : true,
				sortable : false,
				editor : new Ext.form.TextField({
					allowBlank : false
				})
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'left',
				hidden : true,
				editor : new Ext.form.TextArea({
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
								var record = operateStepGrid
										.getSelectionModel().getSelected();
								var value = record.get('memo');
								memoText.setValue(value);
								win.show();
							})
						}
					}
				})
			}, {
				header : '操作人',
				dataIndex : 'execName',
				align : 'center',
				sortable : false
			}, {
				header : '监护人',
				id : 'proName',
				dataIndex : 'proName',
				align : 'center',
				sortable : false
			}, {
				dataIndex : 'execMan',
				hidden : true,
				align : 'center',
				sortable : false
			}, {
				header : '监护人',
				hidden : true,
				dataIndex : 'proMan',
				sortable : false
			}, checkColumn]);
	// operateStepCm.defaultSortable = true;
	var operateStepDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getFillOpSteps.action'
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
		plugins : [checkColumn],
		// bbar : workbbar,
		frame : false,
		border : false,
		width : 680,
		autoScroll : true,
		clicksToEdit : 1
	});
	if (isSingle == "Y") {
		var index = operateStepGrid.getColumnModel().getIndexById('proName');
		operateStepGrid.getColumnModel().setHidden(index, true);
	}
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
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [operateStepGrid]
	});
//	if (isSingle == "N") {
//		operateSteptbar.addSeparator();
//		operateSteptbar.addField(label);
//		operateSteptbar.addField(protectMan);
//	}
	searchOpticketsteps(opticketCode);
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
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