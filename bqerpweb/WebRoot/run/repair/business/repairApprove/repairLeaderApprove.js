Ext.ns("repairLeaderMaint.leaderApproveList");
repairLeaderMaint.leaderApproveList = function() {
	var mainId = null;
	var workFlowNo = null;
	var workflowStatus = null;
	var tasklistId = null;
	var specialityId = null;
	var status = null;

	var myRecord = Ext.data.Record.create([{
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
		name : 'situation',
		mapping : 10
	}, {
		name : 'reason',
		mapping : 11
	}, {
		name : 'startDate',
		mapping : 12
	}, {
		name : 'endDate',
		mapping : 13
	}, {
		name : 'specialityId',
		mapping : 14
	}, {
		name : 'specialityName',
		mapping : 15
	}, {
		name : 'workflowNo',
		mapping : 16
	}, {
		name : 'workflowStatus',
		mapping : 17
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true

	});

	var store = new Ext.data.JsonStore({
		url : 'manageplan/getLeaderApproveDetail.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : myRecord
	});

	function repairProSelect() {
		var url = "../repairRegister/repairProjectTree.jsp?";
		url += "op=single";
		var mate = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(mate) != "undefined") {
			var repairProject = mate.code;
			var repairProjectName = mate.name;
			var workingCharge = mate.workingCharge;
			var workingMenbers = mate.workingMenbers;
			var workingTime = mate.workingTime;
			var workingChargeCode = mate.workingChargeCode;

			var record = approveGrid.selModel.getSelected();
			record.set('repairProjectName', repairProjectName);
			record.set('repairProjectId', repairProject);
			record.set('workingCharge', workingChargeCode);
			record.set('workingChargeName', workingCharge);
			record.set('workingMenbers', workingMenbers);
			record.set('workingTime', workingTime);

			approveGrid.getView().refresh();

		}
	};

	// 删除记录
	var ids = [];
	function deleteDetail() {
		var sm = approveGrid.getSelectionModel();
		var selected = sm.getSelections();

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.projectDetailId) {
					ids.push(member.projectDetailId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'manageplan/deleteRepairDetail.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.rejectChanges();
									store.load({
										params : {
											projectMainId : mainId
										}
									});
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}

	// 保存
	function saveDetail() {
		approveGrid.stopEditing();
		var modifyRec = approveGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'manageplan/saveOrUpdateRecord.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(form, options) {
							var obj = Ext.util.JSON.decode(form.responseText)
							Ext.MessageBox.alert('提示信息', '保存成功！')
							store.rejectChanges();
							store.load({
								params : {
									projectMainId : mainId
								}
							});
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	function cancerDetail() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			ids = [];
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	function approve() {

		if (store.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行审批！');
			return;
		}
		var url = "leaderApproveSign.jsp";
		var flag = "";
		if (approveGrid.getSelectionModel().hasSelection()) {
			var rec = approveGrid.getSelectionModel().getSelected();
			var projectMainId = rec.get("projectMainId");
			var workflowNo = rec.get("workflowNo");
			flag = "single";
		} else {
			var projectMainId = mainId;
			var workflowNo = workFlowNo;

		}
		var args = new Object();
		args.entryId = workflowNo;
		args.projectMainId = projectMainId;
		args.flag = flag;

		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			//add by drdu 20100528
			store.reload({
				params : {
					projectMainId : 0
				}
			});

			Ext.getCmp("div_tabs").setActiveTab(0);
			Ext.getCmp("div_grid").getStore().reload();

			// Ext.Ajax.request({
			//				url : 'manageplan/getRepairStatusMain.action',
			//				method : 'post',
			//				params : {
			//					repairMainId : mainId,
			//					selectMainId:flag == ""?"":rec.get("projectMainId"),
			//					flag:"many"
			//				},
			//				success : function(response, options) {
			//					var res = response.responseText;
			//
			//					if (res.toString() == '') {
			//						;
			//					} else {
			//						status = res.toString();
			//
			//						if (status == 7) {
			//							Ext.getCmp("div_tabs").setActiveTab(0);
			//
			//							Ext.getCmp("div_grid").getStore().reload();
			//						} else if (status == 8) {
			//							Ext.getCmp("div_tabs").setActiveTab(0);
			//
			//							Ext.getCmp("div_grid").getStore().reload();
			//						}
			//					}
			//				}
			//			});
		}

	}

	var contbar = new Ext.Toolbar({
		items : [{
			id : 'btnSave',
			iconCls : 'save',
			text : "保存",
			disabled : true,
			handler : saveDetail
		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			disabled : true,
			text : "删除",
			handler : deleteDetail
		}, '-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			disabled : true,
			text : "取消",
			handler : cancerDetail
		}, '-', {
			id : 'btnApprove',
			iconCls : 'approve',
			disabled : true,
			text : "审批",
			handler : approve
		}]

	});

	var approveGrid = new Ext.grid.EditorGridPanel({
		border : false,
		autoScroll : true,
		sm : sm,
		enableColumnMove : false,
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 50,
			align : 'center'
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
			header : '专业',
			dataIndex : 'specialityName',
			align : 'center',
			width : 100
		}, {
			header : '项目ID',
			dataIndex : 'repairProjectId',
			align : 'center',
			hidden : true,
			renderer : function(value) {
				if (value == null)
					value = "";
				return "<span style='color:gray;'>" + value + "</span>";

			}
		}, {
			header : '项目名称',
			dataIndex : 'repairProjectName',
			align : 'center',
			width : 150,
			editor : new Ext.form.TriggerField({
				width : 320,
				allowBlank : false,
				readOnly : true,
				onTriggerClick : repairProSelect,
				listeners : {
					render : function(f) {
						f.el.on('keyup', function(e) {
							approveGrid.getSelectionModel().getSelected().set(
									"repairProjectId", 'temp');
						});
					}
				}
			})
		}, {
			header : '工作负责人ID',
			dataIndex : 'workingCharge',
			align : 'center',
			hidden : true
		}, {
			header : '工作负责人',
			dataIndex : 'workingChargeName',
			width : 80,
			align : 'center',
			editor : new Ext.form.TriggerField({
				width : 320,
				allowBlank : false,
				readOnly : true,
				onTriggerClick : workingChargeSelect,
				listeners : {
					render : function(f) {
						f.el.on('keyup', function(e) {
							annexGrid.getSelectionModel().getSelected().set(
									"workingCharge", 'temp');
						});
					}
				}
			})
		}, {
			header : '工作成员',
			dataIndex : 'workingMenbers',
			align : 'center',
			width : 80,
			editor : new Ext.form.TextField()
		}, {
			header : '工作时间',
			dataIndex : 'workingTime',
			align : 'center',
			hidden : true,
			editor : new Ext.form.TextField()
		}, {
			header : '计划开始日期',
			dataIndex : 'startDate',
			width : 90,
			align : 'center',
			editor : new Ext.form.TextField({
				allowBlank : false,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onpicked : checkTime1
						});

					}
				}
			})

		}, {
			header : '计划结束日期',
			dataIndex : 'endDate',
			readOnly : true,
			width : 90,
			align : 'center',
			editor : new Ext.form.TextField({
				allowBlank : false,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d ',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onpicked : checkTime2
						});

					}
				}
			})

		}, {
			header : '验收标准',
			dataIndex : 'standard',
			align : 'center',
			width : 70,
			renderer : function(v) {
				if (v == '2') {
					return '二级';
				} else {
					return '三级';
				}
			},
			editor : new Ext.form.ComboBox({
				readOnly : true,
				name : 'standard',
				hiddenName : 'standard',
				mode : 'local',
				width : 70,
				value : '2',
				fieldLabel : '验收标准',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
					}
				},
				store : new Ext.data.SimpleStore({
					fields : ['name', 'value'],
					data : [['二级', '2'], ['三级', '3']]
				}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
					}
				}
			})
		}, {
			header : '是否落实材料',
			dataIndex : 'material',
			align : 'center',
			width : 80,
			renderer : function(v) {
				if (v == 'Y') {
					return '是';
				} else {
					return '否';
				}
			},
			editor : new Ext.form.ComboBox({
				readOnly : true,
				name : 'material',
				hiddenName : 'material',
				mode : 'local',
				width : 70,
				value : 'Y',
				fieldLabel : '是否落实材料',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
					}
				},
				store : new Ext.data.SimpleStore({
					fields : ['name', 'value'],
					data : [['是', 'Y'], ['否', 'N']]
				}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
					}
				}
			})
		}, {
			header : '落实材料情况',
			dataIndex : 'situation',
			align : 'center',
			width : 80,
			renderer : function(v) {
				if (v == 'Y') {
					return '是';
				} else {
					return '否';
				}
			},
			editor : new Ext.form.ComboBox({
				readOnly : true,
				name : 'situation',
				hiddenName : 'situation',
				mode : 'local',
				width : 70,
				value : 'Y',
				fieldLabel : '落实材料情况',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
					}
				},
				store : new Ext.data.SimpleStore({
					fields : ['name', 'value'],
					data : [['是', 'Y'], ['否', 'N']]
				}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
					}
				}
			})
		}, {
			header : '未落实原因',
			dataIndex : 'reason',
			align : 'center',
			width : 120,
			editor : new Ext.form.TextField()
		}],
		tbar : contbar,
		clicksToEdit : 1,
		autoSizeColumns : true
	});

	return {
		grid : approveGrid,
		setTasklistId : function(_tasklistId) {
			tasklistId = _tasklistId;

			Ext.Ajax.request({
				method : 'post',
				url : 'manageplan/getLeaderApproveInfo.action',
				params : {
					tasklist : tasklistId
				},
				success : function(action) {
					var result = eval("(" + action.responseText + ")");

					if (result && result.list && result.list.length > 0) {
						var projectMainId = "";
						var workflowNo = "";
						for (var i = 0; i < result.list.length; i++) {
							var ob = result.list[i];
							//								
							projectMainId += ob[0] + ",";
							workflowNo += ob[1] + ",";

						}
					}
					mainId = projectMainId.substring(0, projectMainId.length
							- 1);
					workFlowNo = workflowNo.substring(0, workflowNo.length - 1);

					store.load({
						params : {
							projectMainId : mainId
						}
					});
				}
			});
		},
		setRepairDetailBtn : function(b) {
			if (b) {
				Ext.getCmp("btnSave").enable();
				Ext.getCmp("btnDelete").enable();
				Ext.getCmp("btnCancer").enable();
				Ext.getCmp("btnApprove").enable();
			} else {
				Ext.getCmp("btnSave").disable();
				Ext.getCmp("btnDelete").disable();
				Ext.getCmp("btnCancer").disable();
				Ext.getCmp("btnApprove").disable();
			}
		}
	}

	function workingChargeSelect() {
		var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
		var args = {
			selectModel : 'single',
			notIn : "'999999'",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var emp = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			var record = approveGrid.selModel.getSelected()
			record.set("workingChargeName", emp.workerName);
			record.set("workingCharge", emp.workerCode);
			approveGrid.getView().refresh();
		}
	}

	function checkTime1() {
		var startdate1 = this.value;
		startdate2 = startdate1.substring(0, 10);
		var enddate1 = approveGrid.getSelectionModel().getSelected()
				.get("endDate");
		if (enddate1 != null && enddate1 != "") {
			enddate2 = enddate1.substring(0, 10);
			if (startdate2 != "") {
				if (startdate2 > enddate2 && enddate2 != "") {
					Ext.Msg.alert("提示", "开始日期必须早于结束日期");
					return;
				}
			}
		}
		approveGrid.getSelectionModel().getSelected().set("startDate",
				startdate2);
	}

	function checkTime2() {
		var endtime1 = this.value;
		var endtime2 = endtime1.substring(0, 10);
		var beginTime1 = approveGrid.getSelectionModel().getSelected()
				.get("startDate");
		if (beginTime1 != null && beginTime1 != "") {
			beginTime2 = beginTime1.substring(0, 10);
			if (endtime2 != "" && beginTime2 != "") {
				if (endtime2 < beginTime2 && endtime2 != "") {
					Ext.Msg.alert("提示", "结束日期必须晚于开始日期");
					return;
				}
			}
		}
		approveGrid.getSelectionModel().getSelected().set("endDate", endtime2);
	}
};