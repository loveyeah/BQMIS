Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	var rlsbjcId = getParameter("rlsbjcId");

	var rec = Ext.data.Record.create([{
		name : 'rlsbjcDetailId'
	}, {
		name : 'rlsbjcId'
	}, {
		name : 'equCode'
	}, {
		name : 'equName'
	}, {
		name : 'repairDate'
	}, {
		name : 'courseNumber'
	}, {
		name : 'repairType'
	}, {
		name : 'repairNumber'
	}, {
		name : 'checkHigh'
	}, {
		name : 'checkName'
	}, {
		name : 'checkPart'
	}, {
		name : 'dirtyCapacity'
	}, {
		name : 'sedimentQuantity'
	}, {
		name : 'memo'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/findEquCheckDetailList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				// Ext.getCmp("form").getForm().loadRecord(rec);
			}
		}
	});

	var rn = new Ext.grid.RowNumberer({});

	var equCodeH = new Ext.form.Hidden({
		hidden : false,
		id : 'equCode',
		name : 'equCode'
	});

	var equName = new Ext.form.TextField({
		id : 'equName',
		fieldLabel : '设备',
		readOnly : true,
		allowBlank : false,
		anchor : "100%",
		name : 'equName',
		listeners : {
			focus : equSelect
		}
	});

	var repairType = new Ext.form.TextArea({
		id : 'repairType',
		fieldLabel : '维修类别',
		allowBlank : false,
		height : 100
	});

	var checkHigh = new Ext.form.TextArea({
		id : 'checkHigh',
		fieldLabel : '标高',
		maxLength : 100
	});

	var dirtyCapacity = new Ext.form.TextArea({
		id : 'dirtyCapacity',
		fieldLabel : '垢量',
		maxLength : 100
	});

	var sedimentQuantity = new Ext.form.TextArea({
		id : 'sedimentQuantity',
		fieldLabel : '沉积量',
		maxLength : 100
	});

	var checkName = new Ext.form.TextArea({
		id : 'checkName',
		fieldLabel : '名称',
		maxLength : 100
	});

	var checkPart = new Ext.form.TextArea({
		id : 'checkPart',
		fieldLabel : '部位',
		maxLength : 100
	});

	// 备注
	var taShowMemo = new Ext.form.TextArea({
		id : "taShowMemo",
		maxLength : 256,
		width : 180
	});

	// 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		modal : true,
		closeAction : 'hide',
		items : [taShowMemo],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var record = grid.getSelectionModel().getSelected();
				record.set("memo", taShowMemo.getValue());
				win.hide();
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
	win.on('show', function() {
		taShowMemo.focus(true, 100);
	});

	var cm = new Ext.grid.ColumnModel([sm, rn, {
		header : "设备功能编码",
		width : 100,
		dataIndex : 'equCode',
		renderer : function(value) {
			if (value == null)
				value = "";
			return "<span style='color:gray;'>" + value + "</span>";

		}
	}, {
		header : "工作设备",
		width : 140,
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'equName',
		editor : new Ext.form.TriggerField({
			width : 320,
			allowBlank : false,
			onTriggerClick : equSelect,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {
						grid.getSelectionModel().getSelected().set("equCode",
								'temp');
					});
				}
			}
		})
	}, {
		header : "检修时间",
		sortable : true,
		width : 120,
		dataIndex : 'repairDate',
		renderer : function(value) {
			if (!value) {
				return '';
			} else {
				return value;
			}
		},
		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			id : "temp",
			readOnly : true,
			allowBlank : false,
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M-%d 00:00:00',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						onpicked : function() {
							grid.getSelectionModel().getSelected().set(
									"repairDate", Ext.get("temp").dom.value);
						},
						onclearing : function() {
							grid.getSelectionModel().getSelected().set(
									"repairDate", null);
						}
					});
				}
			}
		})
	}, {
		header : '运行时数',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'courseNumber',
		align : 'left',
		editor : new Ext.form.NumberField({
			maxLength : 10,
			decimalPrecision: 2, // 默认的小数点位数  
			maxLengthText : '最多输入10个数字！'
		})
	}, {
		header : '维修类别',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'repairType',
		align : 'left',
		editor : repairType
	}, {
		header : '维修次数',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'repairNumber',
		align : 'left',
		editor : new Ext.form.NumberField({
			maxLength : 4,
			minValue : 0,
			allowDecimals : false,
			allowNegative : false,
			allwoBlank : false
		})
	}, {
		header : '标高',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'checkHigh',
		align : 'left',
		editor : checkHigh
	}, {
		header : '名称',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'checkName',
		align : 'left',
		editor : checkName
	}, {
		header : '部位',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'checkPart',
		align : 'left',
		editor : checkPart
	}, {
		header : '垢量',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'dirtyCapacity',
		align : 'left',
		editor : dirtyCapacity
	}, {
		header : '沉积量',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'sedimentQuantity',
		align : 'left',
		editor : sedimentQuantity
	}, {
		header : '备注',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'memo',
		align : 'left',
		editor : new Ext.form.TextArea({
			maxLength : 256,
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = grid.getSelectionModel().getSelected();
						grid.stopEditing();
						taShowMemo.setValue(record.get("memo"));
						win.show();
					})
				}
			}
		})
	}]);

	// 增加
	function addRecords() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);
		var count = ds.getCount();
		var o = new rec({
			'rlsbjcDetailId' : '',
			'rlsbjcId' : rlsbjcId,
			'equName' : '',
			'repairDate' : '',
			'courseNumber' : '',
			'repairType' : '',
			'repairNumber' : '',
			'checkHigh' : '',
			'checkName' : '',
			'checkPart' : '',
			'dirtyCapacity' : '',
			'sedimentQuantity' : '',
			'memo' : ''
		});
		grid.stopEditing();
		ds.insert(count, o);
		sm.selectRow(count);
		grid.startEditing(count, 2);
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("rlsbjcDetailId") != null) {
					ids.push(member.get("rlsbjcDetailId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.equCode == "") {
					Ext.MessageBox.alert('提示信息', '设备不能为空！')
					return
				}
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
				url : 'productionrec/saveAndUpdateRecord.action',
				method : 'post',
				params : {
					isUpdate : Ext.util.JSON.encode(updateData),
					isDelete : ids.join(",")
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);

					ds.rejectChanges();
					ids = [];
					ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancer() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			ds.reload();
			ds.rejectChanges();
			ids = [];
		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}

	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addRecords
		}, {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteRecords
		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存",
			handler : save
		}, {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancer
		}]
	});

	function queryRecord() {

		ds.baseParams = {
			rlsbjcId : rlsbjcId
		};
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		tbar : tbar,
		autoWidth : true,
		fitToFrame : true,
		border : true,
		frame : true,
		clicksToEdit : 1,// 单击一次编辑
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : "显示第{0}条到{1}条，共{2}条",
			emptyMsg : "没有记录",
			beforePageText : '',
			afterPageText : ""
		}),
		viewConfig : {
			forceFit : false
		}
	});

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
		layout : 'border',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : {
			region : 'center',
			layout : 'fit',
			items : [grid]
		}
	});

	/** 设备选择处理 */
	function equSelect() {
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		url += "op=one";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			var record = grid.selModel.getSelected()
			var names = equ.name.split(",");
			var codes = equ.code.split(",");
			if (names.length > 1) {
				for (var i = 1; i < names.length; i++) {
					var currentRecord = grid.getSelectionModel().getSelected();
					var count = ds.getCount();
					addRecord(count, names[i], codes[i]);
				}
			}
			record.set("equName", names[0]);
			record.set("equCode", codes[0]);
			grid.getView().refresh();
		}
	};
	queryRecord();
})