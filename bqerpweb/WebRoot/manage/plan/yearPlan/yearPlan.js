Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
BpYearPlan = function(isQuery) {
	var yearPlanId = new Ext.form.Hidden({
		id : 'yearPlanId',
		name : 'yearPlan.yearPlanId'
	})
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}

	var year = new Ext.form.TextField({
		id : 'strYear',
		name : 'yearPlan.strYear',
		fieldLabel : '年度',
		style : 'cursor:pointer',
		anchor : '80%',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy'
				})
			}
		}
	})
	var yeartbar = new Ext.form.TextField({
		id : 'Year',
		name : 'tbarYear',
		fieldLabel : '年度',
		style : 'cursor:pointer',
		anchor : '95%',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy'
				})
			}
		}
	})
	var title = new Ext.form.TextField({
		id : "title",
		name : 'yearPlan.title',
		fieldLabel : '标题',
		anchor : '90%',
		allowBlank : false
	})
	var contentPath = {
		id : "contentPath",
		xtype : "fileuploadfield",
		fieldLabel : '内容',
		readOnly : true,
		anchor : "90%",
		height : 33,
		name : "contentPath",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	var entryBy = new Ext.form.Hidden({
		id : "entryBy",
		name : 'yearPlan.entryBy'
	});

	var entryByName = new Ext.form.TextField({
		id : "entryBy",
		name : 'yearPlan.entryByName',
		fieldLabel : '填写人',
		anchor : '80%',
		readOnly : true,
		allowBlank : false
	})
	var entryTime = new Ext.form.TextField({
		id : "entryDate",
		name : 'yearPlan.entryDate',
		fieldLabel : '填写时间',
		anchor : '80%',
		readOnly : true,
		allowBlank : false
	})
	//增加时填写时间为当前时间
	entryTime.setValue(getDate());
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					var workerCode = result.workerCode;
					var workerName = result.workerName;
					entryBy.setValue(workerCode);
					entryByName.setValue(workerName);
				}
			}
		});
	}
	getWorkCode();
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'yearPlan.memo',
		fieldLabel : '备注',
		anchor : '90%',
		height : 75
	})

	var saveBtu = new Ext.Button({
		id : 'saveButton',
		text : '保存',
		iconCls : 'save',
		handler : saveYearPlan
	})
	var cancelBtu = new Ext.Button({
		id : 'canButton',
		text : '取消',
		iconCls : 'cancer',
		handler : function() {
			win.hide();
		}
	})

	var formPanel = new Ext.form.FormPanel({
		id : 'yearPlan',
		autoWidth : true,
		autoHeight : true,
		frame : true,
		border : true,
		fileUpload : true,
		layout : 'column',
		buttons : [saveBtu, cancelBtu],
		buttonAlign : 'center',
		labelAlign : 'right',
		labelWidth : 100,
		defaults : {
			layout : 'form',
			frame : false,
			border : false
		},
		items : [{
			columnWidth : 1,
			items : [yearPlanId, title]
		}, {
			columnWidth : 0.5,
			items : [year]
		}, {
			columnWidth : 1,
			items : [contentPath]
		}, {
			columnWidth : 1,
			items : [memo]
		}, {
			columnWidth : 0.5,
			items : [entryTime]
		}, {
			columnWidth : 0.5,
			items : [entryByName]
		}]
	})

	var win = new Ext.Window({
		width : 600,
		height : 300,
		modal : true,
		closeAction : 'hide',
		items : [formPanel]
	});

	var yearPlaNRecord = new Ext.data.Record.create([{
		name : 'yearPlanId',
		mapping : 0
	}, {
		name : 'strYear',
		mapping : 1
	}, {
		name : 'title',
		mapping : 2
	}, {
		name : 'contentPath',
		mapping : 3
	}, {
		name : 'memo',
		mapping : 4
	}, {
		name : 'entryBy',
		mapping : 5
	}, {
		name : 'entryByName',
		mapping : 6
	}, {
		name : 'entryDate',
		mapping : 7
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/findYearPlan.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, yearPlaNRecord)
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var colModel = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}), {
		id : 'yearPlanId',
		header : 'ID',
		hidden : true,
		dataIndex : 'yearPlanId',
		sortable : true,
		width : 100
	}, {
		id : 'strYear',
		header : '年度',
		align : 'center',
		dataIndex : 'strYear',
		width : 150,
		sortable : true
	}, {
		header : '标题',
		dataIndex : 'title',
		align : 'center',
		width : 150
	}, {
		dataIndex : "contentPath",
		header : '内容',
		align : 'center',
		renderer : function(v) {
			if (v != null || v != '') {
				return '<a href="' + v + '" target="_blank">查看</a>'
			}
		},
		width : 150
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'center',
		width : 150

	}]);
	function updateYearPlan() {
		if (sm.hasSelection()) {
			var record = grid.getSelectionModel().getSelected();
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				win.setTitle('修改年度计划')
				win.show();
				formPanel.getForm().loadRecord(sm.getSelected());
				entryByName.setValue(record.get('entryByName'))
			}
		} else
			Ext.Msg.alert('提示', '请先选择要修改的数据！')
	}
	function saveYearPlan() {
		{
			var myurl = "";
			if (yearPlanId.getValue() == "" || yearPlanId.getValue() == null) {
				method = 'add';
			} else {
				method = 'update';
			}
			myurl = "manageplan/addOrUpdateYearPlan.action";
		}
		formPanel.getForm().submit({
			method : 'POST',
			url : myurl,
			params : {
				year : year.getValue(),
				filePath : Ext.get('contentPath').dom.value,
				method : method
			},
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				if (o.msg.indexOf("成功") != -1) {
					win.hide();

					ds.reload();
				}
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	function deleYearPlan() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('yearPlanId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
							url : 'manageplan/delYearPlan.action',
							method : 'post',
							params : {
								ids : ids.join(",")
							},
							success : function(response, options) {
								if (response && response.responseText) {
									var res = Ext.decode(response.responseText)
									Ext.Msg.alert('提示', res.msg);
									ds.reload();
								}
							},
							failure : function(response, options) {
								Ext.Msg.alert('提示', '删除数据出现异常！')
							}
						})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	}
	colModel.defaultSortable = true;
	function query() {
		ds.reload();
	}
	var addBtu = new Ext.Button({
		id : 'addBtu',
		text : '新增',
		iconCls : 'add',
		handler : function() {
			win.setTitle('新增年度计划');
			win.show();
			formPanel.getForm().reset();
		}
	})
	var updateBtu = new Ext.Button({
		id : 'updateBtu',
		text : '修改',
		iconCls : 'update',
		handler : updateYearPlan
	})
	var deleteBtu = new Ext.Button({
		id : 'deleteBtu',
		text : '删除',
		iconCls : 'delete',
		handler : deleYearPlan
	})
	var tbar = new Ext.Toolbar({
		items : ['年度', yeartbar, {
			id : 'query2',
			text : "查询",
			iconCls : 'query',
			handler : function() {

				query();
			}
		}, '-', addBtu, updateBtu, deleteBtu]
	});
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录"
	})

	var grid = new Ext.grid.GridPanel({
		id : 'yearplan-grid',
		labelAlign : 'center',
		autoScroll : true,
		ds : ds,
		cm : colModel,
		sm : sm,
		tbar : tbar,
		bbar : bbar,
		border : true
	});
	grid.on('rowdblclick', updateYearPlan);
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			Year : yeartbar.getValue()

		})
	});
	ds.load({
		params : {
			start : 0,
			limit : 18

		}
	})
	this.grid = grid;
	this.init = function() {
		if (isQuery != null && isQuery) {

			addBtu.setVisible(false);
			updateBtu.setVisible(false);
			deleteBtu.setVisible(false);
			grid.purgeListeners()
		} else {
			grid.on('rowdblclick', updateYearPlan)
		}
	};

};