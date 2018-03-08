Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 开工日期
	var start_date = new Ext.form.TextField({
		id : 'start_date',
		fieldLabel : '开工时间',
		style : 'cursor:pointer',
		name : 'model.startDate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true

				});

			}
		}
	})

	// 竣工日期
	var end_date = new Ext.form.TextField({
		id : 'end_date',
		fieldLabel : '竣工日期',
		style : 'cursor:pointer',
		name : 'model.endDate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true

				});

			}
		}
	})

	var projectName = new Ext.form.TextField({
		id : 'projectName',
		fieldLabel : '项目名称',
		name : 'projectName',
		width : 100
	})

	// 查询按钮
	var queryButton = new Ext.Button({
		id : 'query',
		text : '查询',
		iconCls : 'query',
		handler : queryRecord
	})

	var updateButton = new Ext.Button({
		id : 'update',
		text : '修改',
		iconCls : 'update',
		handler : function() {

			if (repGrid.selModel.hasSelection()) {
				var rec = repGrid.getSelectionModel().getSelections();
				if (rec.length > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行修改！");
				} else {
					var record = repGrid.getSelectionModel().getSelected();
					tabs.setActiveTab(1);
					addSp.updateForm(record);
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}

		}

	})

	var deleteButton = new Ext.Button({
		id : 'delete',
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			deleteRecord();
		}

	})
	// 删除记录
	function deleteRecord() {
		var records = repGrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("checkId")) {
					ids.push(member.get("checkId"));
				} else {

					repStore.remove(repStore.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'manageproject/deleteCheck.action', {
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
	var printButton = new Ext.Button({
		id : 'print',
		text : '打印开工报告书',
		iconCls : 'print',
		handler : function() {

		}

	})
	var saveButton = new Ext.Button({
		id : 'save',
		text : '增加',
		iconCls : 'save',
		handler : function() {
			tabs.setActiveTab(1);
			addSp.resetform();
		}

	})

	var checkButton = new Ext.Button({
		id : 'check',
		text : '开工报告书回录',
		iconCls : 'check',
		handler : function() {

		}

	})

	var prj_type = new Ext.ux.ComboBoxTree({
		fieldLabel : "工程类别",
		allowBlank : false,
		displayField : 'text',
		width : '60%',
		valueField : 'id',
		hiddenName : 'model.prjTypeId',
		readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'manageproject/findByPId.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥电厂',
				text : '灞桥电厂'
			}),
			listeners : {
				click : function(node, rec) {
					typeId = node.id;
					typeName = node.text
				}
			}
		},
		selectNodeModel : 'all',
		listeners : {
			select : function(newNode, oldNode) {
				this.setValue(node.id);

			}
		}

	})

	var datalist = new Ext.data.Record.create([{
		// 开工报告书id
		name : 'checkId',
		mapping : 0
	}, {
		// 工程合同编号
		name : 'contractName',
		mapping : 1
	}, {
		// 项目名称
		name : 'conId',
		mapping : 2
	}, {
		// 编号
		name : 'reportCode',
		mapping : 3
	}, {
		// 开工日期
		name : 'startDate',
		mapping : 4
	}, {
		// 竣工日期
		name : 'endDate',
		mapping : 5
	}, {
		// 承包单位名称
		name : 'contractorName',
		mapping : 6
	}, {
		// 承包单位ID
		name : 'contractorId',
		mapping : 7
	}, {
		// 承包负责人
		name : 'chargeBy',
		mapping : 8
	}, {
		// 发包部门负责人
		name : 'deptChargeBy',
		mapping : 9
	}, {
		// 发包部门工程方负责人验收评价
		name : 'checkAppraise',
		mapping : 10
	}, {
		// 填写人
		name : 'entryBy',
		mapping : 11
	}, {
		// 填写时间
		name : 'entryDate',
		mapping : 12
	}, {
		// 企业编码
		name : 'enterpriseCode',
		mapping : 13
	}, {
		// 发包部门负责人姓名
		name : 'deptChargeByName',
		mapping : 14
	}, {
		// 立项id
		name : 'prjId',
		mapping : 16
	}, {
		// 立项名称
		name : 'prjName',
		mapping : 17
	}])

	var repStore = new Ext.data.JsonStore({
		url : 'manageproject/queryNoReportCheck.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var repGrid = new Ext.grid.GridPanel({
		sm : sm,
		ds : repStore,
		listeners : {
			'rowdblclick' : function() {
				var rec = repGrid.getSelectionModel().getSelected();
				tabs.setActiveTab(1);
				addSp.updateForm(rec);
			}
		},
		columns : [sm, new Ext.grid.RowNumberer(), {
			header : "立项名称",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'prjName'
		},{
			header : "项目名称",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'contractName'
		}, {
			header : "工程开始时间",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'startDate',
			renderer : function(v) {
				if (v != null) {
					return v.substring(0, 10);
				} else {
					return v;
				}
			}
		}, {
			header : "工程结束时间",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'endDate',
			renderer : function(v) {
				if (v != null) {
					return v.substring(0, 10);
				} else {
					return v;
				}
			}
		}, {
			header : "承包单位名称",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'contractorName'
		}, {
			header : "承包方负责人",
			width : 75,
			align : "center",
			sortable : true,
			dataIndex : 'chargeBy'
				//								}, {
				//									header : "竣工日期",
				//									width : 75,
				//									align : "center",
				//									sortable : true,
				//									dataIndex : 'endDate',
				}],
		viewConfig : {
			forceFit : true
		},
		frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : repStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		tbar : [// '开工时间：',start_date,'竣工时间：',end_date,'工程类别：',prj_type,
		'项目名称:', projectName, queryButton, '-', saveButton, '-', updateButton,
				'-', deleteButton, '-'//, printButton, '-',checkButton
		],
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	function queryRecord() {
		repStore.baseParams = {
			status : '0',
			projectName : projectName.getValue(),
			flag : 'fillQuery'
		}
		repStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	var addSp = new addStartReport();

	var tabs = new Ext.TabPanel({
		activeTab : 1,
		tabPosition : 'bottom',
		plain : false,
		defaults : {
			autoScroll : true
		},
		items : [{
			id : 'checkGrid',
			title : '项目验收列表',
			autoScroll : true,
			items : repGrid,
			layout : 'fit'
		}, {
			id : 'checkPanelEntry',
			title : '项目验收填写',
			items : addSp.panel,
			autoScroll : true,
			layout : 'fit'
		}]
	});
	queryRecord();
	tabs.on('tabchange', function(thisTab, newTab) {
		var Id = newTab.getId();
		if (Id == 'checkGrid') {
			queryRecord();
		}
	})

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '1 0 1 1',
			collapsible : true,
			items : [tabs]

		}]
	});

});