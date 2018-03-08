Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
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
		// add by ywliu 20100607
		name : 'trainingDepName'
	}]);

	var store = new Ext.data.JsonStore({
		url : 'manageplan/findTrainPlanBackfillList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Storelist
	});

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, {
			// add by ywliu 20100607
			header : "部门",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'trainingDepName'
		}, {
			header : "培训类别",
			width : 50,
			sortable : false,
			align : 'center',
			dataIndex : 'trainingTypeName'
		}, {
			header : "计划培训项目",
			width : 200,
			sortable : false,
			dataIndex : 'trainingName'
		}, {
			header : "负责人",
			width : 50,
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
			width : 50,
			sortable : false,
			align : 'center',
			dataIndex : 'trainingNumber'
		}, {
			header : "完成人数",
			width : 50,
			id : 'finishNumber',
			sortable : false,
			align : 'center',
			dataIndex : 'finishNumber'
		}, {
			// add by mgxia 20100607
			header : "填写人",
			width : 50,
			sortable : false,
			align : 'center',
			dataIndex : 'backfillName'
		}],

		tbar : ["计划时间:", planTime, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "审批信息",
			id : 'btnupcommit',
			iconCls : 'approve',
			handler : approveQuery
		}],
		sm : sm,
		viewConfig : {
			forceFit : true
		}
	});

	function approveQuery() {
		
		
		if(sm.hasSelection()){
			if(sm.getSelections().length >1){
				Ext.Msg.alert('提示','请选择一条数据！');
				return;
			}
			var selected = sm.getSelected();
			var url = '';
		
		var entryId = selected.get("backfillWorkflowNo");;
		if (entryId == null || entryId == "") {
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ "bqDeptplanBack";
			window.open(url);
		} else {
			var url = "/power/workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);

			init();
		}
		}else{
			Ext.Msg.alert('提示','请选择要查看审批信息的数据！')
		} 
	}

	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			planDate : planTime.getValue(),
			flag : "dept"
		});
	});

	function queryRecord() {
		// modified by liuyi 091215
//		store.reload({
//			params : {
//				start : 0,
//				limit : 18
//			}
//		})
		store.load()
		init();
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

	function init() {
		Ext.Ajax.request({
			method : 'post',
			url : 'manageplan/getPlanMainBackObj.action',
			params : {
				planDate : planTime.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result != null) {
//					if (result.model.workflowStatus == null) {
//						Ext.getCmp("btnupcommit").setDisabled(false);
//					} else {
//						Ext.getCmp("btnupcommit").setDisabled(false);
//					}
				}
			}
		});
	}
	queryRecord();
})
