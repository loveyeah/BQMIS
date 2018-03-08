function getYear() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10);
	return s;
}
Ext.onReady(function() {

		Ext.form.Field.prototype.msgTarget = 'title';

		var projectMainYear = new Ext.form.TextField({
			id : 'projectMainYear',
			fieldLabel : '年度',
			readOnly : true,
			width : 60,
			value : getYear(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy',
						isShowClear : false,
						onpicked : function(v) {
							this.blur();
						}
					});
				}
			}
		});
		var storeRepairType = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'manageplan/getRepairType.action'
			}),
			reader : new Ext.data.JsonReader({
				root : 'list'
			}, [{
				name : 'repairTypeId',
				mapping : 0
			}, {
				name : 'repairTypeName',
				mapping : 1
			}])
		});

		var cbxRepairType = new Ext.form.ComboBox({
			id : 'cbxRepairType',
			fieldLabel : "检修类别",
			store : storeRepairType,
			displayField : "repairTypeName",
			valueField : "repairTypeId",
			hiddenName : 'main.repairTypeId',
			mode : 'remote',
			triggerAction : 'all',
			value : '',
			readOnly : true,
			width : 90
		})

		var storeRepairSpecail = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'manageplan/getRepairSpecialityType.action'
			}),
			reader : new Ext.data.JsonReader({
				root : 'list'
			}, [{
				name : 'specialityId',
				mapping : 'specialityId'
			}, {
				name : 'specialityName',
				mapping : 'specialityName'
			}])
		});
		var cbxRepairSpecail = new Ext.form.ComboBox({
			id : 'specialityId',
			fieldLabel : "检修专业",
			store : storeRepairSpecail,
			displayField : "specialityName",
			valueField : "specialityId",
			hiddenName : 'main.specialityId',
			mode : 'remote',
			triggerAction : 'all',
			value : '',
			readOnly : true,
			width : 90
		})

		var tasklistId = {
			fieldLabel : '任务单',
			name : 'tasklistId',
			xtype : 'combo',
			id : 'tasklistId',
			store : new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [[]]
			}),
			mode : 'remote',
			hiddenName : 'main.tasklistId',
			allowBlank : false,
			editable : false,
			width : 90,
			triggerAction : 'all',
			onTriggerClick : function() {
				var url = "selectRepair.jsp";
				var args = {
					selectModel : 'single',
					notIn : "'999999'",
					rootNode : {
						id : '-1',
						text : '合肥电厂'
					}
				}
				var emp = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

				if (typeof(emp) != "undefined") {
					Ext.getCmp('tasklistId').setValue(emp.id);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('tasklistId'), emp.name);
				}
			}
		};

		// 定义选择行
		var sm = new Ext.grid.RowSelectionModel({
			singleSelect : true
		});

		// grid中的数据Record
		var gridRecord = new Ext.data.Record.create([{
			name : 'projectMainId',
			mapping : 0
		}, {
			name : 'projectMainYear',
			mapping : 1
		}, {
			name : 'repairTypeId',
			mapping : 2
		}, {
			name : 'repairTypeName',
			mapping : 3
		}, {
			name : 'fillBy',
			mapping : 4
		}, {
			name : 'fillName',
			mapping : 5
		}, {
			name : 'fillTime',
			mapping : 6
		}, {
			name : 'specialityId',
			mapping : 7
		}, {
			name : 'specialityName',
			mapping : 8
		}, {
			name : 'tasklistId',
			mapping : 9
		}, {
			name : 'tasklistName',
			mapping : 10
		}, {
			name : 'memo',
			mapping : 11
		}, {
			name : 'workflowNo',
			mapping : 13
		}, {
			name : 'workflowStatus',
			mapping : 14
		}, {
			name : 'situationProject',
			mapping : 16
		}]);

		// grid的store
		var queryStore = new Ext.data.JsonStore({
			url : 'manageplan/findRepairRecordList.action',
			root : 'list',
			totalProperty : 'totalCount',
			fields : gridRecord
		});

		// 分页工具栏
		var pagebar = new Ext.PagingToolbar({
			pageSize : 18,
			store : queryStore,
			displayInfo : true,
			displayMsg : "一共 {2} 条",
			emptyMsg : "没有记录"
		});

		// 页面的Grid主体
		var gridOrder = new Ext.grid.GridPanel({
			region : 'north',
			layout : 'column',
			store : queryStore,
			height : 300,
			columns : [new Ext.grid.RowNumberer({
				header : '行号',
				width : 35
			}), {
				header : "ID",
				hidden : true,
				width : 100,
				dataIndex : 'projectMainId'
			}, {
				header : "年度",
				sortable : true,
				width : 100,
				dataIndex : 'projectMainYear'
			}, {
				header : "检修类别",
				sortable : true,
				width : 100,
				dataIndex : 'repairTypeName'
			}, {
				header : "专业",
				sortable : true,
				width : 80,
				dataIndex : 'specialityName'
			}, {
				header : "任务单",
				width : 150,
				dataIndex : 'tasklistName'
			}, {
				header : "填写人",
				width : 100,
				dataIndex : 'fillName'
			}, {
				header : "填写日期",
				sortable : true,
				width : 100,
				dataIndex : 'fillTime'
			}, {
				header : "备注",
				sortable : true,
				width : 100,
				dataIndex : 'memo'
			}],
			tbar : [{
				text : '年度'
			}, projectMainYear, {
				text : '检修类别'
			}, cbxRepairType, {
				text : '检修专业'
			}, cbxRepairSpecail, {
				text : '任务单'
			}, tasklistId, {
				id : 'btnSubmit',
				text : "查询",
				iconCls : 'query',
				handler : findFuzzy
			}],
			bbar : pagebar,
			sm : sm,
			frame : false,
			border : false,
			enableColumnMove : false
		});

		gridOrder.on('rowclick', showDetail);
		gridOrder.on('render', function() {
			gridFresh();
		});
		gridOrder.on('rowdblclick', function(grid, rowIndex, e) {
		var record = gridOrder.getSelectionModel().getSelected();
		var revoke;
		if (record.data.workflowStatus == '1') {
			revoke = 'revoke';
		} else {
			revoke = "";
		}
		id = record.data.projectMainId;
		var situationProject = record.data.situationProject;
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
		var url = "run/repair/business/repairRegister/repairRegister.jsp";
		parent.document.all.iframe2.src = url + "?id=" + id + "&revoke="+ revoke+ "&situationProject="+ situationProject;
	});

		var detailRecord = new Ext.data.Record.create([{
			name : 'projectDetailId',
			mapping : 0
		}, {
			name : 'projectMainId',
			mapping : 1
		}, {
			name : 'repairProjectId',
			mapping : 2
		}, {
			name : 'repairProjectName',
			mapping : 3
		}, {
			name : 'workingCharge',
			mapping : 4
		}, {
			name : 'workingChargeName',
			mapping : 5
		}, {
			name : 'workingMenbers',
			mapping : 6
		}, {
			name : 'workingTime',
			mapping : 7
		}, {
			name : 'standard',
			mapping : 8
		}, {
			name : 'material',
			mapping : 9
		}, {
		name : 'startDate',
		mapping : 12
	}, {
		name : 'endDate',
		mapping : 13
	}]);
		var detailStore = new Ext.data.JsonStore({
			url : 'manageplan/repairDetailList.action',
			root : 'list',
			totalProperty : 'totalCount',
			fields : detailRecord
		});

		var materialGrid = new Ext.grid.GridPanel({
			region : 'center',
			border : false,
			autoScroll : true,
			enableColumnMove : false,
			store : detailStore,
			columns : [new Ext.grid.RowNumberer({
				header : "行号",
				width : 35
			}), {
				header : '明细ID',
				dataIndex : 'projectDetailId',
				align : 'center',
				hidden : true
			}, {
				header : '主表ID',
				dataIndex : 'projectMainId',
				align : 'center',
				hidden : true
			}, {
				header : '项目ID',
				dataIndex : 'repairProjectId',
				align : 'center',
				hidden : true
			}, {
				header : '项目名称',
				dataIndex : 'repairProjectName',
				align : 'center'
			}, {
				header : '工作负责人',
				dataIndex : 'workingChargeName',
				width : 120,
				align : 'center'
			}, {
				header : '工作成员',
				dataIndex : 'workingMenbers',
				align : 'center'
			}, {
				header : '工作时间',
				dataIndex : 'workingTime',
				align : 'center',
				hidden : true
			}, {
				header : '计划开始日期',
				dataIndex : 'startDate',
				align : 'center'
			}, {
				header : '计划结束日期',
				dataIndex : 'endDate',
				align : 'center'
			}, {
				header : '验收标准',
				dataIndex : 'standard',
				align : 'center',
				renderer : function(v)
				{
					if(v=='2')
					{
						return '二级';
					}else{
						return '三级';
					}
				}
			}, {
				header : '是否落实材料',
				dataIndex : 'material',
				align : 'center',
				renderer : function(v) {
					if (v == 'Y') {
						return '是';
					} else {
						return '否';
					}
				}
			}]
		});

		new Ext.Viewport({
			layout : 'border',
			margins : '0 0 0 0',
			border : false,
			items : [gridOrder, materialGrid]
		});

		function findFuzzy(start) {
			gridFresh();
			detailStore.removeAll();
		}

		/**
		 * 当单击主grid中一行时，下面的grid显示详细信息
		 */
		function showDetail() {
			if (gridOrder.getSelectionModel().getSelected() != null) {
				var record = gridOrder.getSelectionModel().getSelected();
				detailStore.load({
					params : {
						repairMainId : record.get('projectMainId')
					}
				});
			}
		}

		function gridFresh() {
			queryStore.baseParams = {
				year : projectMainYear.getValue(),
				repairType : cbxRepairType.getValue(),
				speciality : cbxRepairSpecail.getValue(),
				tastlist : Ext.get("tasklistId").dom.value,
				flag : "status"
			};
			queryStore.load({
				params : {
					start : 0,
					limit : 18
				}
			});
		}

});