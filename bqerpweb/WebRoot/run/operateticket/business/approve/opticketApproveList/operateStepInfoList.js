Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 渲染时间格式
function renderDate(value) {
	return value ? value.dateFormat('Y-m-d H:i:s') : '';
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 暂时自己定义的操作票项目号
	var operateTaskId = 1;
	// 暂时自己定义的操作票号
	// var opticketCode = 'S2008120005';
	// var opticketCode = getParameter("opticketCode");
	var arg = window.dialogArguments;

	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var wd = 180;
	// Ext.Msg.alert("list", opticketCode);
	// ============= 定义弹出画面 ===============
	// 操作项目名称
	var tfOperateStepName = new Ext.form.TextField({
		id : "tfOperateStepName",
		fieldLabel : '操作项目名称<font color="red">*</font>',
		allowBlank : false,
		width : wd + 200,
		name : 'operateStep.operateStepName',
		maxLength : 255
	});

	// 操作票编号
	var hideOpticketCode = new Ext.form.Hidden({
		id : 'opticketCode',
		name : 'operateStep.opticketCode'
	});

	// 完成时间
	var dfFinishTime = new Ext.form.TextField({
		id : 'dfFinishTime',
		fieldLabel : "完成时间",
		name : 'operateStep.finishTime',
		style : 'cursor:pointer',
		anchor : "95%",
		readOnly : true,
		value : new Date().dateFormat('Y-m-d H:i:s'),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	})

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
		width : wd,
		triggerAction : 'all',
		displayField : "name",
		valueField : "id",
		value : '0',
		emptyText : '未执行',
		blankText : '未执行',
		mode : 'local',
		selectOnFocus : true,
		readOnly : true,
		hiddenName : 'operateStep.execStatus'
	});

	// 显示顺序
	var nfDisplayNo = new Ext.form.NumberField({
		id : "nfDisplayNo",
		fieldLabel : '显示顺序',
		width : wd,
		name : 'operateStep.displayNo',
		maxLength : 10
	});

	// 弹出窗口panel
	var formPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		defaults : {
			width : 300
		},
		items : [tfOperateStepName, {
			layout : 'form',
			items : [dfFinishTime, hideOpticketCode, execStatusCbo, nfDisplayNo]
		}]
	});

	// 弹出窗口
	var win = new Ext.Window({
		width : 500,
		height : 200,
		title : '增加操作项目',
		buttonAlign : "center",
		resizable : false,
		items : [formPanel],
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : saveRecord
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}],
		layout : 'fit',
		closeAction : 'hide'
	});
	// ↓↓********************主画面************************
	// 三个button
	var btnAdd = new Ext.Button({
		text : Constants.BTN_ADD,
		iconCls : Constants.CLS_ADD,
		handler : function() {
			// 初始化窗口中的控件
			formPanel.getForm().reset();
			win.show();
		}
	})
	var btnDel = new Ext.Button({
		text : Constants.BTN_DELETE,
		iconCls : Constants.CLS_DELETE,
		handler : function() {
			var ids = "";
			// 获得grid中被选中的记录
			var selectedRows = grid.getSelectionModel().getSelections();
			// 取得该记录的实体id
			for (i = 0; i < selectedRows.length; i++) {
				ids += selectedRows[i].get("operateStepId") + ",";
			}
			// 执行删除action
			if (ids.length > 0) {
				// 去掉最后一个多余的逗号
				ids = ids.substring(0, ids.length - 1);
				Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg,
						function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									url : 'opticket/deleteStepList.action',
									method : Constants.POST,
									params : {
										// 删除的记录的ids
										ids : ids
									},
									success : function(result, request) {
										// 成功，显示删除成功信息
										var o = eval("(" + result.responseText
												+ ")");
										Ext.Msg.alert(Constants.NOTICE, o.msg);
										// 更新信息
										ds.load({
											params : {
												opticketCode : opticketCode
											}
										});
									},
									// 删除失败
									failure : function() {
										Ext.Msg.alert(Constants.ERROR,
												Constants.UNKNOWN_ERR);
									}
								});
							}
						})
			} else {
				Ext.MessageBox.alert('提示', '请选择要删除的记录！');
			}
		}
	})
	var btnSave = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		handler : function() {
			var allc = ds.getTotalCount();
			// var sm = grid.getSelectionModel();
			// var selected = sm.getSelections();
			// var saveInfo = [];
			// for (var i = 0; i < selected.length; i++) {
			// saveInfo.push(selected[i].data);
			// }
			var saveInfo = [];
			for (var i = 0; i < allc; i++) {
				saveInfo.push(ds.getAt(i).data);
			}
			if (saveInfo.length > 0) {
				Ext.Ajax.request({
					method : Constants.POST,
					url : 'opticket/updateStepList.action',
					success : function(result, request) {
						// 成功，显示修改成功信息
						var o = eval("(" + result.responseText + ")");
						Ext.Msg.alert(Constants.NOTICE, o.msg);
						// 更新信息
						ds.load({
							params : {
								opticketCode : opticketCode
							}
						});
					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					},
					params : {
						saveInfo : saveInfo.toJSONString()
					}
				});
			}

			else {
				Ext.MessageBox.alert('提示', '请选择要修改的记录！');
			}
		}
	})
	var btnField = new Ext.form.FieldSet({
		layout : 'column',
		style : "border:0",
		height : 20,
		items : [{
			columnWidth : .15,
			layout : 'form',
			items : [btnAdd]
		}, {
			columnWidth : .15,
			layout : 'form',
			items : [btnDel]
		}, {
			columnWidth : .15,
			layout : 'form',
			items : [btnSave]
		}]
	})
	// grid中的数据Record
	var rungridRecord = new Ext.data.Record.create([{
		// 操作项目
		name : 'operateStepName'
	}, {
		// 完成时间
		name : 'finishTime'
	}, {
		// 执行情况
		name : 'execStatus'
	}, {
		// 操作项目ID
		name : 'operateStepId'
	}, {
		name : 'displayNo'
	}, {
		name : 'runAddFlag'
	}, {
		name : 'memo'
	}]);
	// grid中的store
	var ds = new Ext.data.JsonStore({
		root : 'list',
		url : 'opticket/getStepList.action',
		fields : rungridRecord
	});
	// 载入数据
	ds.load({
		params : {
			opticketCode : opticketCode
		}
	});
	// ds.setDefaultSort('operateStepName', 'asc');
	// 单选列
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				r = row;
				// sm.selectRow(r);
			}
		}
	});
	var checkColumn = new Ext.grid.CheckColumn({
		header : '执行情况',
		dataIndex : 'execStatus',
		width : 70
	});
	var dateColumn = ({
		header : "完成时间",
		sortable : true,
		width : 180,
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
		editor : new Ext.form.TextField({
			readOnly : true,
			id : "temp",
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M-01 00:00:00',
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						alwaysUseStartDate : true,
						onpicked : function() {
							grid.getSelectionModel().getSelected().set(
									"finishTime", Ext.get("temp").dom.value);
						}
					});
				}
			}
		})
	})
	// 列内容
	var item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		sortable : false,
		align : 'left'
	}), {
		header : '操作项目',
		dataIndex : 'operateStepName',
		align : 'left',
		width : 500,
		sortable : false,
		editor : new Ext.form.TextArea({
			listeners : {
				focus : function() {
					var record = grid.getSelectionModel().getSelected();
					if (record.get("runAddFlag") == 'N') {
						this.blur();
					}
				}
			}
		})
	},
			// dateColumn,
			// checkColumn,
			{
				header : 'id',
				dataIndex : 'operateStepId',
				hidden : true
			}, {
				header : '是否运行时补充',
				dataIndex : 'runAddFlag',
				align : 'center',
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
				width : 70,
				align : 'center',
				sortable : false,
				editor : new Ext.form.NumberField({
					allowBlank : false,
					listeners : {
						focus : function() {
							var record = grid.getSelectionModel().getSelected();
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
				sortable : true,
				xtype : 'textfield'
			}]);

	// EditorGridPanel,可在单元格里编辑
	var grid = new Ext.grid.EditorGridPanel({
		clicksToEdit : 1,
		enableColumnHide : true,
		enableColumnMove : false,
		layout : 'fit',
		ds : ds,
		cm : item_cm,
		sm : sm,
		width : 680,
		autoScroll : true,
		border : false
	});
	// // 主框架
	// var listPanel = new Ext.FormPanel({
	// layout : "form",
	// frame : false,
	// autoScroll : true,
	// items : [grid]
	// });
	new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [grid]
	})
	var opStepText = new Ext.form.TextArea({
		id : "opStepText",
		maxLength : 100,
		width : 180
	});
	var opStepWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [opStepText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("opStepText").dom.value.length <= 100) {
					var record = grid.selModel.getSelected()
					record.set("operateStepName",
							Ext.get("opStepText").dom.value);
					opStepWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				opStepWin.hide();
			}
		}]
	});
	// 保存新增的记录
	function saveRecord() {
		if (!formPanel.getForm().isValid()) {
			return false;
		}
		if (Ext.get("dfFinishTime").dom.value < new Date()
				.dateFormat('Y-m-d 00:00:00')) {
			Ext.Msg.alert(Constants.ERROR, "完成时间不能小于当前时间！");
			return false;
		}

		// 设置操作项目的操作票编号
		hideOpticketCode.setValue(opticketCode);
		// 提交表单
		formPanel.getForm().submit({
			method : Constants.POST,
			waitMsg : Constants.DATA_SAVING,
			url : 'opticket/addContentOpticketstep.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
				// 刷新grid
				ds.reload();
				// 隐藏窗口
				win.hide();
			},
			faliue : function() {
				Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
			}
		});
	}
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});
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
	},

	onMouseDown : function(e, t) {
		if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
			e.stopEvent();
			var index = this.grid.getView().findRowIndex(t);
			var record = this.grid.store.getAt(index);
			record.set(this.dataIndex, !record.data[this.dataIndex]);
			if (record.data[this.dataIndex]) {
				// this.grid.selModel.selectRow(index);
			}
		}
	},

	renderer : function(v, p, record) {
		v = v ? v : false;
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
		var sm = this.grid.getSelectionModel();
		return '<div class="x-grid3-check-col' + (v ? '-on' : '')
				+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
	}
};