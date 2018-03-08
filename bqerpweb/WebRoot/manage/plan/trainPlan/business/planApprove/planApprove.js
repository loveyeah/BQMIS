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

	var deptCode;
	var deptName;
	var status;
//	function getWorkCode() {
//		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result.workerCode) {
//
//							// 设定默认工号
//							workerCode = result.workerCode;
//							workerName = result.workerName;
//							// 默认部门
//							deptCode = result.deptCode;
//							deptName = result.deptName;
//							planDept.setValue(deptName);
//						}
//					}
//
//				});
//
//	}
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
	var workStatus;
	var reportby;
	var reportime

	var planDate = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				value : ChangeDateToString(startdate),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"
									// add by liuyi 091214 清空按钮不可见
									,isShowClear : false

								});
						this.blur();
					}

				}

			});
	// 计划部门

	var planDept = new Ext.form.TextField({
		id : 'planDept',
		xtype : "textfield",
		fieldLabel : "计划部门",
		readOnly : true,
		name : 'planDept',
		anchor : '100%'
			// ,
			// listeners : getWorkCode()

		});
	// 审批状态

	var approveStatus = new Ext.form.TextField({
				id : 'workflowStatus',
				name : 'workflowStatus',
				fieldLabel : "审批状态",
				readOnly : true,
				xtype : "textfield",
				anchor : '80%'

			});
	// 上报人
	var entrtyName = new Ext.form.TextField({
				name : 'reportByName',
				id : 'reportByName',
				fieldLabel : '上报人',
				mode : 'remote',
				readOnly : true,
				anchor : '100%'
			});
	// 上报时间
	var entrtyDate = new Ext.form.TextField({
				fieldLabel : '上报时间',
				id : 'reportTime',
				name : 'reportTime',
				readOnly : true,
				anchor : '80%'
			});

	function query() {
		// modified by liuyi 091214 解决页面刷新问题
		con_ds.baseParams = {
			approve : 'approve', 
//			status,
			planType : '',
			planDept : deptCode,
			planTime : planDate.getValue()
		}
		con_ds.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '查 询',
				iconCls : 'query',
				minWidth : 70,
				handler : function() {
					init();
				}

			});
	var btnapprove = new Ext.Button({
				id : 'btnapprove',
				text : '审批',
				iconCls : 'approve',
				minWidth : 70,
				handler : function() {
					// modified by liuyi 20100429 审批之前先判断该部门能否进行审批
					if (approveStatus.getValue() == "审批中") {
				Ext.Ajax.request({
					url : 'manageplan/judgeDeptCanApprove.action',
					method : 'post',
					params : {
						planDept : deptCode,
						planTime : planDate.getValue()
					},
					success : function(response, options) {
						var result = Ext.decode(response.responseText)
						if (result.msg == null) {
							Ext.Msg.confirm("提示", "确定该部门下的计划已全部上报？", function(buttonobj) {

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
			});
	function approve() {
		var url = "sign.jsp";
		if (con_ds.getCount() > 0) {
			var mainId = con_ds.getAt(0).get("trainDetail.trainingMainId");
			var workFlowNo = con_ds.getAt(0).get("workflowNo");
			var args = new Object();
			args.entryId = workFlowNo;
			args.mainId = mainId;
			// args.workflowType = "bqDeptJobplan";
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');
		}
		if (obj) {
			init();
		}
	}

	var content = new Ext.form.FieldSet({
				title : '培训计划信息',
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
						// modified by liuyi 20100427
//						, {
//							layout : 'column',
//							border : false,
//							items : [{
//										border : false,
//										columnWidth : 0.3,
//										layout : 'form',
//										items : [entrtyName]
//									}, {
//										border : false,
//										columnWidth : 0.4,
//										layout : 'form',
//										items : [entrtyDate]
//									}]
//						}
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
	var con_item = Ext.data.Record.create([{
				name : 'trainDetail.trainingLevel'
			}, {
				name : 'trainDetail.trainingName'
			}, {
				name : 'trainDetail.trainingNumber'
			}, {
				name : 'trainDetail.trainingHours'
			}, {
				name : 'trainDetail.chargeBy'
			}, {
				name : 'chargeName'
			}, {
				name : 'trainDetail.trainingMainId'
			}, {
				name : 'workflowNo'
			}, {
				name : 'planTypeName'
			}
			// add by liuyi 20100427 
			,{
				name : 'fillBy'
			},{
				name : 'fillName'
			},{
				name : 'deptName' //add by fyyang 20100517
			},{name:'workflowStatus'}
			]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						// header : '序号',
						width : 20,
						align : 'center'
					}), 
					{
				header : '部门',
				dataIndex : 'deptName',
				align : 'center'
			},{
				header : '项目类别',
				dataIndex : 'planTypeName',
				align : 'center'
			}, {
				header : '计划培训项目',
				dataIndex : 'trainDetail.trainingName',
				width : 400,
				align : 'left'
			}, {
				header : '计划人数',
				dataIndex : 'trainDetail.trainingNumber',
				align : 'center'
			}, {
				header : '培训课时',
				dataIndex : 'trainDetail.trainingHours',
				align : 'center'
			}, {
				header : '负责人',
				dataIndex : 'trainDetail.chargeBy',
				align : 'center'
			},{
				header : '填写人',
				dataIndex : 'fillName'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getTrainPlanApplyList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, con_item)
			});

	query();
	con_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {

				})
			});
			
	con_ds.on("load",function(){
	 if(con_ds.getTotalCount()>0)
	 {
	    var myStatus=con_ds.getAt(0).get("workflowStatus");
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
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePaext : '页',
				afterPaext : "共{0}"
			});

	var Grid = new Ext.grid.GridPanel({
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				width : 200,
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				// tbar : contbar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	/* WorkCode(); */
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

	// Ext.getCmp("btnapprove").setDisabled(true);

	function init() {
//		Ext.Ajax.request({
//					method : 'post',
//					url : 'manageplan/getPlanMainObj.action',
//					params : {
//						planDate : planDate.getValue()
//					},
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result.model != null) {
//							planDept.setValue(result.model.deptName);
//							approveStatus.setValue(result.model.workflowStatus);
////							if (result.model.workflowStatus == '审批中'||result.model.workflowStatus == '部门培训员已汇总') {
////							//	Ext.getCmp("btnapprove").setDisabled(false);
////								status ='approve'
////							} else {
////								//Ext.getCmp("btnapprove").setDisabled(true);
////								status = ""
////							}
//							entrtyName.setValue(result.model.reportBy);
//							entrtyDate.setValue(result.model.reportTime);
//							// con_ds.loadData(result.list);
//							/*
//							 * workStatus = result.model.workflowStatus;
//							 * reportby = result.model.reportByName; reportime =
//							 * result.model.eportBy;
//							 */
//							// workflowStatus.setValue(workStatus);
//							// reportByName.setValue(reportby);
//							// reportime.setValue(reportime);
//						}
//						query();
//					}
//				});
		
		query();
	}
	init();
})