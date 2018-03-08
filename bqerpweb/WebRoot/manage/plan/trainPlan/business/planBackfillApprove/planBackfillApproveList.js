Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var deptCode;
	var deptName;
	var workStatus;
	var reportby;
	var reportime

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

//	function getWorkCode() {
//		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
//			success : function(action) {
//				var result = eval("(" + action.responseText + ")");
//				alert( action.responseText)
//				if (result.workerCode) {
//					// 设定默认工号
//					workerCode = result.workerCode;
//					workerName = result.workerName;
//					// 默认部门
//					deptCode = result.deptCode;
//					deptName = result.deptName;
//					planDept.setValue(deptName);
//				}
//			}
//		});
//	}
	// 计划部门显示为一级部门 add by sychen 20100518
		function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'manageplan/getSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						var arr =new Array()
 						arr = result.split(",");
						if (result) {
							// 设定默认工号
							var workerCode = arr[2];
							var workerName = arr[3];
							// 设定默认部门
							deptCode = arr[0];
							deptName = arr[1]
							planDept.setValue(deptName);

						}
					}
				});
	}
	getWorkCode();

	var planDate = new Ext.form.TextField({
		name : 'planTime',
		fieldLabel : '计划时间',
		value : ChangeDateToString(startdate),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '',
					alwaysUseStartDate : true,
					dateFmt : "yyyy-MM",
					isShowClear : false//不在时间控件上显示清除按钮
				});
				this.blur();
			}
		}
	});

	var planDept = new Ext.form.TextField({
		id : 'planDept',
		fieldLabel : "计划部门",
		readOnly : true,
		name : 'planDept',
		anchor : '100%'
	});
	
	var approveStatus = new Ext.form.TextField({
		id : 'backfillWorkflowStatus',
		name : 'backfillWorkflowStatus',
		fieldLabel : "审批状态",
		readOnly : true,
		anchor : '80%'

	});

//	var entrtyName = new Ext.form.TextField({
//		name : 'backfillName',
//		id : 'backfillName',
//		fieldLabel : '上报人',
//		mode : 'remote',
//		readOnly : true,
//		anchor : '100%'
//	});
//	
//	var entrtyDate = new Ext.form.TextField({
//		fieldLabel : '上报时间',
//		id : 'backfillDate',
//		name : 'backfillDate',
//		typeAhead : true,
//		forceSelection : true,
//		readOnly : true,
//		selectOnFocus : true,
//		anchor : '80%'
//	});

//	store.on('beforeload', function() {
//		Ext.apply(this.baseParams, {
//			planDate : planDate.getValue(),
//			flag : 'Y'
//		});
//	});
	
	function query(){
		store.baseParams = {
		planDate : planDate.getValue(),
			flag : 'Y'
	}
			store.reload({
				params : {
//					planDate : planDate.getValue(),
//					flag : 'Y',
					start : 0,
					limit : 18
				}
			})
//			init();
		}
		
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查 询',
		iconCls : 'query',
		minWidth : 70,
		handler : query
	});
	var btnapprove = new Ext.Button({
		id : 'btnapprove',
		text : '审批',
		iconCls : 'approve',
		minWidth : 70,
//		handler : approve
		handler : approveJudge
	});
	
	// add by sychen  20100518审批之前先判断该部门能否进行审批
		function approveJudge() {
		if (approveStatus.getValue() == "审批中") {
			Ext.Ajax.request({
						url : 'manageplan/judgeDeptCanApprove.action',
						method : 'post',
						params : {
							planDept : deptCode,
							planTime : planDate.getValue(),
							flag:"finish"
						},
						success : function(response, options) {
							var result = Ext.decode(response.responseText)
							if (result.msg == null) {
								Ext.Msg.confirm("提示", "确定该部门下的计划已全部上报？",
										function(buttonobj) {
											if (buttonobj == "yes") {
												approve();
											}
										});
							} else {
								Ext.Msg.alert('提示', result.msg)
							}
						},
						failure : function(response, options) {
							Ext.Msg.alert('提示', '该部门尚不可进行审批!')
						}
					})
		} else {
			approve();
		}
	}
	
	function approve() {
		var url = "sign.jsp";
		if (store.getCount() > 0) {
			var mainId = store.getAt(0).get("trainingMainId");
			var workFlowNo = store.getAt(0).get("backfillWorkflowNo");
			var tempDate = planDate.getValue();
			var args = new Object();
			args.entryId = workFlowNo;
			args.mainId = mainId;
			args.planDate = tempDate;
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');
		}
		if (obj) {
//			init();
			query();
		}
	}

	var content = new Ext.form.FieldSet({
		title : '培训计划回填信息',
		height : '100%',
		collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [planDept]
			}, {
				border : false,
				columnWidth : 0.4,
				layout : 'form',
				items : [approveStatus]
			}]
		}
//		, {
//			layout : 'column',
//			border : false,
//			items : [{
//				border : false,
//				columnWidth : 0.3,
//				layout : 'form',
//				items : [entrtyName]
//			}, {
//				border : false,
//				columnWidth : 0.4,
//				layout : 'form',
//				items : [entrtyDate]
//			}]
//		}
		]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		tbar : ["计划时间:", planDate, '-', btnQuery, '-', btnapprove],
		items : [content]
	});
	/*-----------------------------------------------------------------*/
	var con_item = Ext.data.Record.create([con_sm,{
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
		name : 'backfillBy'
	}, {
		name : 'trainingDepName'//部门 add by sychen 20100517
	}
	]);
	var con_sm = new Ext.grid.CheckboxSelectionModel();
	var con_item_cm = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {//部门 add by sychen 20100517
			header : "部门",
			width : 100,
			sortable : false,
			dataIndex : 'trainingDepName',
			align : 'center'
		}, {
			header : "培训类别",
			width : 100,
			sortable : false,
			dataIndex : 'trainingTypeName',
			align : 'center'
		},{
			header : "计划培训项目",
			width : 250,
			sortable : false,
			dataIndex : 'trainingName'
		}, {
			header : "负责人",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'chargeName'
		}, {
			header : "培训课时",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'trainingHours'
		}, {
			header : "计划人数",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'trainingNumber'
		}, {
			header : "完成人数",
			width : 80,
			id : 'finishNumber',
			sortable : false,
			align : 'center',
			dataIndex : 'finishNumber'
		},  {
			header : "上报人",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'backfillName'
		}, {
			header : "上报时间",
			width : 80,
			sortable : false,
			align : 'center',
			dataIndex : 'backfillDate'
		},{
			header : "backfillWorkflowNo",
			width : 100,
			id : 'finishNumber',
			hidden :true,
			sortable : false,
			dataIndex : 'backfillWorkflowNo'
		}]);

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/findTrainPlanBackfillList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, con_item)
	});
	
	//add by sychen 20100518
	store.on("load",function(){
	 if(store.getTotalCount()>0)
	 {
	    var myStatus=store.getAt(0).get("backfillWorkflowStatus");
	    
	    if(myStatus=="1") {myStatus="审批中";}
	    else if(myStatus=="4"){myStatus="部门培训员已汇总"}
	    else myStatus= "";
	 	approveStatus.setValue(myStatus);
	 }
	 else
	 {
	 	approveStatus.setValue("");
	 }
		
	});	

	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePaext : '页',
		afterPaext : "共{0}"
	});

	var Grid = new Ext.grid.GridPanel({
		ds : store,
		cm : con_item_cm,
		sm : con_sm,
		width : 200,
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 120,
			border : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			border : false,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [Grid]
		}]
	});

//	function init() {
//		Ext.Ajax.request({
//			method : 'post',
//			url : 'manageplan/getPlanMainBackObj.action',
//			params : {
//				planDate : planDate.getValue()
//			},
//			success : function(action) {
//				var result = eval("(" + action.responseText + ")");

//				if (result != null && result.model != null) {
//							planDept.setValue(result.model.deptName);
//							approveStatus.setValue(result.model.workflowStatus);
//							
//							if (result.model.workflowStatus == '审批中'||
//							    result.model.workflowStatus == '部门培训员已汇总') {
//
//								Ext.getCmp("btnapprove").setDisabled(false);
//							} else {
//								Ext.getCmp("btnapprove").setDisabled(true);
//							}
//							entrtyName.setValue(result.model.reportBy);
//							entrtyDate.setValue(result.model.reportTime);
//				}else{
//					Ext.Msg.alert('提示','该月数据未上报！');
//					return ;
//				}
//			}
//		});
//	}
	
	query();
})