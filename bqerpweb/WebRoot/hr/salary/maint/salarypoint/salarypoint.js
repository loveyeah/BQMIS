Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id = "";
	// 薪点点值
	var salaryPointid = new Ext.form.Hidden({
		id : 'salaryPointid',
		name : 'spoint.salaryPointId'
	})
	var salaryPoint = new Ext.form.NumberField({
		fieldLabel : "薪点点值",
		id : 'salaryPoint',
		name : 'spoint.salaryPoint',
		decimalPrecision : 2,
		maxLength : 10,
		anchor : "85%"
	})
	// 生效开始时间
	var effectStartTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "effectStartTime",
		// name : 'spoint.effectStartTime',
		readOnly : true,
		anchor : "85%",
		fieldLabel : '生效开始时间',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : " ",
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
						if (effectEndTime.getValue() != "") {
							if (effectStartTime.getValue() == ""
									|| effectStartTime.getValue() > effectEndTime
											.getValue()
									|| effectEndTime.getValue() == effectStartTime
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于计划结束时间");
								effectStartTime.setValue("");
								return;
							}
						}
					},
					onclearing : function() {
						effectStartTime.markInvalid();
					}
				});
				this.blur();
			}
		}
	});

	// 生效结束时间
	var effectEndTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "effectEndTime",
		// name : 'spoint.effectEndTime',
		readOnly : true,
		anchor : "85%",
		allowBlank : false,
		fieldLabel : '生效结束时间',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : " ",
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
						if (effectStartTime.getValue() == ""
								|| effectStartTime.getValue() > effectEndTime
										.getValue()
								|| effectEndTime.getValue() == effectStartTime
										.getValue()) {
							Ext.Msg.alert("提示", "必须大于计划开始时间");
							effectEndTime.setValue("")
							return;
						}
					},
					onclearing : function() {
						effectEndTime.markInvalid();
					}
				});
				this.blur();
			}
		}
	})

	// 备注
	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		name : 'spoint.memo',
		anchor : "85%"
	})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
		labelAlign : 'left',
		frame : true,
		labelWidth : 90,
		items : [salaryPointid, salaryPoint, effectStartTime, effectEndTime,
				memo]
	});
	// 弹出窗体
	var win = new Ext.Window({
		width : 330,
		height : 210,
		buttonAlign : "center",
		items : [blockForm],
		layout : 'fit',
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (salaryPointid.getValue() == "") {
					myurl = "com/saveSalaryPoint.action";
				} else {
					myurl = "com/updateSalaryPoint.action";
				}
				blockForm.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						"startTime" : effectStartTime.getValue(),
						"endTime" : effectEndTime.getValue()
					},
					success : function(form, action) {
						Ext.Msg.alert('错误', '操作成功!');
						queryRecord();
						id = "";
						win.hide();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '操作失败!');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]

	});

	var fuzzy = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "fuzzy",
		name : 'fuzzy',
		readOnly : true,
		anchor : "80%",
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM',
					onpicked : function() {
					},
					onclearing : function() {
						fuzzy.markInvalid();
					}
				});
			}
		}
	});

	// 新建按钮
	var westbtnAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : addRecord
	});

	// 修改按钮
	var westbtnedit = new Ext.Button({
		text : '修改',
		iconCls : 'update',
		handler : updateRecord
	});

	// 刷新按钮
	var westbtnref = new Ext.Button({
		text : '刷新',
		iconCls : 'reflesh',
		handler : function() {
			queryRecord();
		}
	});
	var westbtndelete = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteRecord
	});
	var westsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var datalist = new Ext.data.Record.create([

	{
		name : 'spoint.salaryPointId'
	}, {
		name : 'spoint.salaryPoint'
	}, {
		name : 'spoint.effectStartTime'
	}, {
		name : 'spoint.effectEndTime'
	}, {
		name : 'spoint.memo'
	}, {
		name : 'startTime'
	}, {
		name : 'endTime'
	}, {
		name : 'status'
	}]);

	var westgrids = new Ext.data.JsonStore({
		url : 'com/getSalaryPointList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
		ds : westgrids,
		columns : [westsm, new Ext.grid.RowNumberer(), {
			header : "薪点点值",
			width : 40,
			sortable : true,
			dataIndex : 'spoint.salaryPoint'
		}, {
			header : "生效开始时间",
			width : 40,
			sortable : false,
			dataIndex : 'startTime'
		}, {
			header : "生效结束时间",
			width : 40,
			sortable : false,
			dataIndex : 'endTime'
		}, {
			header : "备注",
			width : 40,
			sortable : true,
			dataIndex : 'spoint.memo'
		}],
		viewConfig : {
			forceFit : true
		},
		tbar : ['执行时间：', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, westbtnAdd, {
			xtype : "tbseparator"
		}, westbtnedit, {
			xtype : "tbseparator"
		}, westbtnref, {
			xtype : "tbseparator"
		}, westbtndelete],
		sm : westsm,
		frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : westgrids,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// westgrid.on("rowdblclick", updateRecord);
	function queryRecord() {
		westgrids.baseParams = {
			sDate : fuzzy.getValue()
		}
		westgrids.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	function addRecord() {
		blockForm.getForm().reset();
		win.show();
		var count = westgrids.getCount();
		if (westgrids.getCount() > 0) {
			effectStartTime.setDisabled(true);
			var date = westgrids.getAt(count - 1).get("endTime");
			effectStartTime.setValue(date);
		} else {
			effectStartTime.setDisabled(false);
		}

		win.setTitle("增加薪点点值维护");

	}
	function updateRecord() {
		if (westgrid.selModel.hasSelection()) {
			var records = westgrid.getSelectionModel().getSelections();
			id = records[0].data.salaryPointId;
			if (records[0].data.status == 1) {
				win.show();
				var record = westgrid.getSelectionModel().getSelected();
				blockForm.getForm().reset();
				blockForm.form.loadRecord(record);
				effectStartTime.setDisabled(true);
				effectStartTime.setValue(record.get("startTime"));
				effectEndTime.setValue(record.get("endTime"))
				win.setTitle("修改薪点点值维护");
			} else {
				Ext.Msg.alert("提示", "请先选择要可编辑的行!");
				return false;
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var records = westgrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("status") == 0) {
					Ext.Msg.alert('提示', '该行不可删除!');
					return;
				} else if (member.get("spoint.salaryPointId")) {
					ids.push(member.get("spoint.salaryPointId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteSalaryPoint.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '1 0 2 1',
			collapsible : true,
			items : [westgrid]
		}]
	});

	queryRecord();
});