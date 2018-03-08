Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() - 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();

	// 计划时间
	var planTime = new Ext.form.TextField({
				id : 'planTime',
				fieldLabel : '计划时间',
				style : 'cursor:pointer',
				value : ChangeDateToString(startdate),
				readOnly : true,
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var Storelist = new Ext.data.Record.create([sm, {
				name : 'trainingMainId'
			}, {
				name : 'trainingDetailId'
			}, {
				name : 'trainingTypeName'
			}, {
				name : 'trainingName'
			}, {
				name : 'chargeName'
			}, {
				name : 'trainingHours'
			}, {
				name : 'trainingNumber'
			}, {
				name : 'finishNumber'
			}, {
				name : 'backfillWorkflowNo'
			}, {
				name : 'backfillWorkflowStatus'
			}, {
				name : 'backfillName'
			}, {
				name : 'backfillDate'
			}, {
				name : 'trainingTypeId'
			}, {
				name : 'trainingMonth'
			}, {
				name : 'trainingDep'
			}, {
				name : 'trainingLevel'
			}]);

	var store = new Ext.data.JsonStore({
				url : 'manageplan/findTrainPlanBackfillList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Storelist
			});

	// add by sychen 20100518
	store.on("load", function() {
				if (store.getTotalCount() > 0) {
					var myStatus = store.getAt(0).get("backfillWorkflowStatus");

					if (myStatus == null) {
						Ext.getCmp("btnSave").setDisabled(false);
						Ext.getCmp("btnupcommit").setDisabled(true);
					} else if (myStatus == "1" || myStatus == "2"
							|| myStatus == "4") {
						Ext.getCmp("btnSave").setDisabled(true);
						Ext.getCmp("btnupcommit").setDisabled(true);
					} else if (myStatus == "0" || myStatus == "3") {
						Ext.getCmp("btnSave").setDisabled(false);
						Ext.getCmp("btnupcommit").setDisabled(false);
					}
				}
			});
	// add end

	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [new Ext.grid.RowNumberer(), {
							header : "培训类别",
							width : 80,
							sortable : false,
							align : 'center',
							dataIndex : 'trainingTypeName'
						}, {
							header : "计划培训项目",
							width : 450,
							sortable : false,
							dataIndex : 'trainingName'
						}, {
							header : "项目级别",
							width : 100,
							sortable : false,
							hidden : true,
							dataIndex : 'trainingLevel'
						}, {
							header : "负责人",
							width : 100,
							sortable : false,
							align : 'center',
							dataIndex : 'chargeName'
						}, {
							header : "培训课时",
							width : 100,
							sortable : false,
							align : 'center',
							dataIndex : 'trainingHours'
						}, {
							header : "计划人数",
							width : 100,
							align : 'center',
							sortable : false,
							dataIndex : 'trainingNumber'
						}, {
							header : "完成人数",
							width : 100,
							id : 'finishNumber',
							sortable : false,
							align : 'center',
							css : CSS_GRID_INPUT_COL,
							dataIndex : 'finishNumber',
							editor : new Ext.form.NumberField({
										maxLength : 100,
										allowDecimals : false
									})
						}],

				tbar : ["计划时间:", planTime, '-', {
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}, '-', {
							id : 'btnSave',
							text : "保存",
							iconCls : 'save',
							handler : saveRecord
						}, '-', {
							text : "上报",
							id : 'btnupcommit',
							iconCls : 'upcommit',
							handler : upcommit
						}],
				sm : sm,
				viewConfig : {
					forceFit : true
				}
			});
	var ids = new Array();
	function saveRecord() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			var mainId;
			mainId = grid.getStore().getAt(0).get("trainingMainId");
			for (var i = 0; i < grid.getStore().getCount(); i++) {
				var rec = grid.getStore().getAt(i);
				// if (rec.get("trainingLevel") != 1) {//过滤掉合计行 add by drdu
				// 20100112
				if (rec.get("finishNumber") == null
						|| (rec.get("finishNumber") == "" && rec
								.get("finishNumber") != "0")) {
					Ext.MessageBox.alert('提示', '完成人数不能为空！')
					return
				}
				updateData.push({
							id : rec.get("trainingDetailId"),
							level : rec.get("trainingLevel"),
							fn : rec.get("finishNumber")
						});
			}
			// }
			Ext.Ajax.request({
						url : 'manageplan/saveFinishNumber.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							id : mainId,
							month : planTime.getValue()
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							queryRecord();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 上报
	function upcommit() {
		// ----------上报前保存确认-----------------
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (confirm("信息已修改，是否要保存修改吗?")) {
				var updateData = new Array();
				var mainId;
				mainId = grid.getStore().getAt(0).get("trainingMainId");
				for (var i = 0; i < grid.getStore().getCount(); i++) {
					var rec = grid.getStore().getAt(i);
					if (rec.get("finishNumber") == null
							|| (rec.get("finishNumber") == "" && rec
									.get("finishNumber") != "0")) {
						Ext.MessageBox.alert('提示', '完成人数不能为空！')
						return
					}
					updateData.push({
								id : rec.get("trainingDetailId"),
								level : rec.get("trainingLevel"),
								fn : rec.get("finishNumber")
							});
				}
				Ext.Ajax.request({
							url : 'manageplan/saveFinishNumber.action',
							method : 'post',
							params : {
								isUpdate : Ext.util.JSON.encode(updateData),
								id : mainId
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '未知错误！')
								return;
							}
						})
			}
		}
		// ----------------------------------------
		if (store.getTotalCount() > 0) {
			var url = "reportSign.jsp";
			var mainId = store.getAt(0).get("trainingMainId");
			var workFlowNo = store.getAt(0).get("backfillWorkflowNo");
			var args = new Object();
			args.entryId = workFlowNo;
			args.mainId = mainId;
			args.planDate = planTime.getValue();
			args.flowCode = "bqDeptplanBack";
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');

			if (obj) {
				// init();
				queryRecord();
			}
		}
	}

	store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							planDate : planTime.getValue(),
							flag : 'N'
						});
			});

	function queryRecord() {

		// store.reload({
		// params : {
		// start : 0,
		// limit : 18
		// }
		// })
		store.load();
		// init();
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							height : '50%',
							items : [grid]
						}]
			});

	// function init() {
	// Ext.Ajax.request({
	// method : 'post',
	// // url : 'manageplan/getPlanMainBackObj.action',
	// url : 'manageplan/findTrainPlanBackfillList.action',
	// params : {
	// // planDate : planTime.getValue()
	// planDate : planTime.getValue(),
	// flag : 'N'
	// },
	// success : function(action) {
	// var result = eval("(" + action.responseText + ")");
	//				
	// if (result != null) {
	// if (result.model == null
	// || result.model.workflowStatus == null) {
	// Ext.getCmp("btnSave").setDisabled(false);
	// Ext.getCmp("btnupcommit").setDisabled(true);
	// // } else if (result.model.workflowStatus == '审批中'
	// // || result.model.workflowStatus == '审批已通过'
	// // || result.model.workflowStatus == '部门培训员已汇总') {
	// } else if (result.model.workflowStatus == 1
	// || result.model.workflowStatus == 2
	// || result.model.workflowStatus == 4) {
	// Ext.getCmp("btnSave").setDisabled(true);
	// Ext.getCmp("btnupcommit").setDisabled(true);
	//
	// } else if (result.model.workflowStatus == 0
	// || result.model.workflowStatus == 3) {
	// Ext.getCmp("btnSave").setDisabled(false);
	// Ext.getCmp("btnupcommit").setDisabled(false);
	// }
	//
	// }
	//
	// }
	// });
	// }

	queryRecord();
})
