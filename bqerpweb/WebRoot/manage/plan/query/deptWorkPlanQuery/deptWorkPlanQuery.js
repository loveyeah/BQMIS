Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	// 部门工作计划主表id
	var depMainId;

	// 取当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";

		return s;
	}
	
		// 系统下个月时间
	function getToMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 2;
		s += (t > 9 ? "" : "0") + t + "";

		return s;
	}
	//add by sychen 20100414
		// 从session取登录人部门编码
	function getDepCodeOnly() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							var DeptCode = result.deptCode;
							var workerCode = result.workerCode;
							editBy.setValue(workerCode);
						}
					}
				});
				query();
	}
//update by sychen 20100426
//	function init() {
//		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobDepMain.action',
//				{
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//                        
//						if (result.baseInfo != null) {
//
//							workFlowNo = result.baseInfo.workFlowNo;
//							signStatus = result.baseInfo.signStatus;
//							depMainId = result.baseInfo.depMainId;
//							//update by sychen 20100414
//							status.setValue(signStatus == '0'
//									? '未上报'
//									: (signStatus == '1'
//											? '已上报'
//											: (signStatus == '2'
//													? '部门领导汇总已审批'
//											 : (signStatus == '3'
//													 ? '总公司汇总已审批'
//											  : (signStatus == '4'
//													  ? '多经副总经理审批（阮）已审批'
//											   : (signStatus == '5'
//													   ? '多经副总经理审批（刘）已审批'
//											    : (signStatus == '6'
//													    ? '多经总经理审批已审批'
//											     : (signStatus == '7'
//													     ? '生产计划汇总已审批'
//											      : (signStatus == '8'
//													      ? '设备部已审批'
//											       : (signStatus == '9'
//													       ? '总工已审批'
//											        : (signStatus == '10'
//													        ? '生产厂长已审批'
//										            : (signStatus == '11'
//													         ? '审批通过'
//											         : (signStatus == '12'
//													          ? '已退回'
//													  : (signStatus == '13'
//															   ? '已汇总'
//															: '状态错误'))))))))))))));
//							//store.load({
//							//			params : {
//							//				depMainId : depMainId,
//							//				start : 0,
//							//				limit : 18
//							//			}
//							//		});
//
//						} else {
//
//							signStatus = undefined;
//
//						}
//
//					}
//				}, 'planTime=' + planTime.getValue() + '&editDepcode='
//						+ editDepcode.getValue()+ '&flag='
//						+ "query"+ '&editBy='
//						+ editBy.getValue());
//	}

	// 计划时间
	var planTime = new Ext.form.TextField({

				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"

								});
						this.blur();
					}

				}

			});
	planTime.setValue(getToMonth());
	
	// 编辑人编码//add by sychen 20100414
	var editBy = new Ext.form.TextField({
				name : 'editBy'
			});
	
	// 编辑人所在部门名称
	var editDepName = new Ext.form.ComboBox({
		fieldLabel : '编辑部门',

		mode : 'remote',

		editable : false,
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '灞桥热电厂'
				}
			}
			var url = "/power/comm/jsp/hr/dept/dept.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				editDepcode.setValue(rvo.codes);
				editDepName.setValue(rvo.names);

			}
		}

	});

	// 编辑人所在部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});
	function query() {
		//update by sychen 20100427
//		if (editDepcode.getValue() != null) {
//			approveQuery.setDisabled(false);
//			init();
		store.baseParams = {
			editDepcode : editDepcode.getValue(),
			planTime : planTime.getValue()
		}
		store.load()
//		} else {
//			Ext.Msg.alert('提示', '请选择部门！');
//		}

	}
	//add by sychen 20100426
	function approveQuery() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = sm.getSelected();
			var workflowType = 'bqDeptWorkPlanApprove';
			var entryId = rec.get('workFlowNo');
			var url;
			if (entryId == null || entryId == "")
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ workflowType;
			else
				url = url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			window.open(url);

		}
	}
	
//	function approveQuery() {
//
//		var url;
//		var entryId = workFlowNo;
//
////		var workflowType = 'bqDeptJobplan';//update by sychen 20100414
//		var workflowType = 'bqDeptWorkPlanApprove';
//		if (entryId == null || entryId == "")
//			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
//					+ workflowType;
//		else
//			url = url = "/power/workflow/manager/show/show.jsp?entryId="
//					+ entryId;
//		window.open(url);
//
//	}
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : "查询",
				iconCls : 'query',
				handler : query

			})
	var approveQuery = new Ext.Button({
				text : '审批查询',
				disabled : true,
				iconCls : 'write',
				handler : approveQuery

			})
	var status = new Ext.form.TextField({
				fieldLabel : '计划状态',
				readOnly : true

			})

	// var myform = new Ext.form.FormPanel({
	// bodyStyle : "padding: 20,10,20,20",
	// layout : 'column',
	// items : [{
	// columnWidth : '.2',
	// layout : 'form',
	// border : false,
	// labelWidth : 60,
	// items : [planTime]
	// }, {
	// columnWidth : '.3',
	// labelWidth : 60,
	// layout : 'form',
	// border : false,
	// items : [editDepName]
	// },
	//
	// {
	// columnWidth : '.1',
	// layout : 'form',
	// border : false,
	// labelWidth : 60,
	// items : [query]
	// }, {
	// columnWidth : '.1',
	// layout : 'form',
	// border : false,
	// labelWidth : 60,
	// items : [approveQuery]
	// }, editDepcode
	//
	// ]
	//
	// })

//	var MyRecord = Ext.data.Record.create([{
//				name : 'baseInfo.depMainId'
//			}, {
//				name : 'editDate'
//			}, {
//				name : 'editDepName'
//			}, {
//				name : 'editByName'
//			}, {
//				name : 'jobContent'
//			}, {
//				name : 'completeData'
//			}, {
//				name : 'ifComplete'
//			}, {
//				name : 'completeDesc'
//			}]);
// add by sychen 20100426
	var MyRecord = Ext.data.Record.create([{
				name : 'depMainId',
				mapping : 0
			}, {
				name : 'planTime',
				mapping : 1
			}, {
				name : 'editBy',
				mapping : 2
			}, {
				name : 'editName',
				mapping : 3
			}, {
				name : 'editDate',
				mapping : 4
			}, {
				name : 'workFlowNo',
				mapping : 5
			}, {
				name : 'signStatus',
				mapping : 6
			},{
				name : 'jobId',
				mapping : 7
			},{
				name:'jobContent',
				mapping: 8
			},{
				name:'completeData',
				mapping:9
			},{
				name:'editDepcode',
				mapping: 10
			},{
				name:'deptName',
				mapping:11
			},{
				name:'chargeBy',//add by sychen 20100504
				mapping:12
			},{
				name:'chargeName',//add by sychen 20100504
				mapping:13
			},{
				name:'orderBy',//add by sychen 20100505
				mapping:14
			},{
				name:'deptId',//add by sychen 20100519
				mapping:15
			},{
				name:'level1DeptName',//add by sychen 20100519
				mapping:16
			}]);
	var dataProxy = new Ext.data.HttpProxy({
//				url : 'manageplan/queryBpJPlanJobDepDetail.action '
				//update by sychen 20100423
				url : 'manageplan/getBpJPlanJobDepMainQuery.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 定义grid

	var grid = new Ext.grid.GridPanel({
				// region : "west",
				store : store,
				layout : 'fit',
				// width:'0.5',
				columns : [

						sm, // 选择框
						// number, // add by sychen 20100505
						 {

							header : "部门",

							sortable : false,
							dataIndex : 'level1DeptName'

						}, {

							header : "编辑人",

							sortable : false,
							dataIndex : 'editName'

						}, {
							// add by sychen 20100504
							header : "责任人",

							sortable : false,
							dataIndex : 'chargeName'

						}, {

							header : "编辑时间",

							sortable : false,
							dataIndex : 'editDate'

						}, {
							width : 60,
							header : "序号",
							align : 'center',
							sortable : false,
							dataIndex : 'orderBy',
							editor : new Ext.form.TextField({
										id : 'orderBy',
										allowBlank : false
									})
						},{
							width : 300,
							header : "工作内容",

							sortable : false,
							dataIndex : 'jobContent'

						}, {

							header : "完成时间",

							sortable : false,
							dataIndex : 'completeData',
							renderer : function changeIt(val) {
								if (val == "0") {
									return "当月";
								} else if (val == "1") {
									return "跨月";
								} else if (val == "2") {
									return "长期";
								} else if (val == "3") {
									return "全年";
								} else {
									return "";
								}
							}
						}, {

							header : "完成情况",

							sortable : false,
							hidden : true,
							dataIndex : 'ifComplete',
							renderer : function changeIt(val) {
								if (val == "0") {
									return "未完成";
								} else if (val == "1") {
									return "进行中";
								} else if (val == "2") {
									return "已完成";
								} else {
									return "";
								}
							}
						}, {
							width : 300,
							header : "完成说明",
							hidden : true,
							sortable : false,
							dataIndex : 'completeDesc'
						}],

				sm : sm, // 选择框的选择 Shorthand for
				// selModel（selectModel）
				// 顶部工具栏
				tbar : ["计划时间:", planTime, '-', "编辑部门:", editDepName, '-',
						btnQuery, '-', approveQuery/*, '-', "计划状态:", status*/]
			});
	/** 右边的grid * */
   // add by sychen 20100426
	grid.on("rowclick", function() {

				if (grid.getSelectionModel().hasSelection()) {
					
			        approveQuery.setDisabled(false);
			        
					var rec = grid.getSelectionModel().getSelected();
					var signStatus=rec.get("signStatus");
					status.setValue(signStatus == '0'
									? '未上报'
									: (signStatus == '1'
											? '已上报'
											: (signStatus == '2'
													? '部门领导汇总已审批'
											 : (signStatus == '3'
													 ? '总公司汇总已审批'
											  : (signStatus == '4'
													  ? '多经副总经理审批（阮）已审批'
											   : (signStatus == '5'
													   ? '多经副总经理审批（刘）已审批'
											    : (signStatus == '6'
													    ? '多经总经理审批已审批'
											     : (signStatus == '7'
													     ? '生产计划汇总已审批'
											      : (signStatus == '8'
													      ? '设备部已审批'
											       : (signStatus == '9'
													       ? '总工已审批'
											        : (signStatus == '10'
													        ? '生产厂长已审批'
										            : (signStatus == '11'
													         ? '审批通过'
											         : (signStatus == '12'
													          ? '已退回'
													  : (signStatus == '13'
															   ? '已汇总'
															: '状态错误'))))))))))))));

				} else {
					status.setValue(null);
					
				}
			})		
		
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [
						// {
						//
						// layout : 'fit',
						// border : false,
						// frame : false,
						// region : "north",
						// height : 80,
						// items : [myform]
						// },
						{
					// bodyStyle : "padding: 20,20,20,0",
					region : "center",
					border : false,
					frame : false,
					layout : 'fit',
					height : '50%',
					// width : '50%',
					items : [grid]
				}]
			});
getDepCodeOnly();//add by sychen 20100414
})
