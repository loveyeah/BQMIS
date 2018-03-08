Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
    var actionId;
	// deng lu buid
	var deptId;
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		// t = d.getHours();
		// s += (t > 9 ? "" : "0") + t + ":";
		// t = d.getMinutes();
		// s += (t > 9 ? "" : "0") + t
		// + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}
	// 系统当前时间
	function getToMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 2;
		s += (t > 9 ? "" : "0") + t + "";

		return s;
	}
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'manageplan/getSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						var arr = new Array()
						arr = result.split(",");
						if (result) {
							// 设定默认工号
							var workerCode = arr[2];
							var workerName = arr[3];
							var DeptCode = arr[0];
							
							var DeptName = arr[1];
							depMainId.setValue(0);
							editByName.setValue(workerName);
							editDepName.setValue(DeptName);
							editBy.setValue(workerCode);
							editDepcode.setValue(DeptCode);
							editDate.setValue(getDate());
							store.rejectChanges();
							store.load({
										params : {

											start : 0,
											limit : 18
										}
									});
						}
					}
				});
	}
	// 从session取登录人部门编码
	function getDepCodeOnly() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result.workerCode) {
							var DeptCode = result.deptCode;
							var workerCode = result.workerCode;
							editDepcode.setValue(DeptCode);
							editBy.setValue(workerCode);
							deptId = result.deptId;
							
							editByName.setValue(result.workerName);//add by sychen 20100507
							editDepName.setValue(result.deptName);//add by sychen 20100507
							editDate.setValue(editTime.getValue());//add by sychen 20100507
						}
						init();
					}
				});
	}
	
	
	function init() {
		//add by sychen 20100329
		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobDepMainApprove.action',
				{
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result && result.list &&result.list.length > 0) {
							var ob=result.list[0];
							if (ob[8] =='1'||ob[8] =='2'||ob[8] =='3'||ob[8] =='4'||
							    ob[8] =='5'||ob[8] =='6'||ob[8] =='7'||ob[8] =='8'||
							    ob[8] =='9'||ob[8] =='10'|| ob[8] =='11') {
								signStatus = ob[8];
								store.rejectChanges();
								store.load();
							} else {
								// 设定默认工号
								var mainId = ob[0];
								var workerName = ob[13];
								var date = ob[3];
								var DeptName = ob[14];

								workFlowNo = ob[7];
								signStatus = ob[8];
								depMainId.setValue(mainId);
								editByName.setValue(workerName);
								editDate.setValue(date);
								editDepName.setValue(DeptName);
								store.rejectChanges();

								store.load({
											params : {
												depMainId : depMainId
														.getValue()
											}
										});
							}
						} else {
							getWorkCode();
							signStatus = undefined;

						}

						// 选时间后所有按钮先全部可用
						Ext.getCmp("btnupcommit").setDisabled(false);

						// 页面载入时根据工作流状态控制哪些按钮可用
						if (signStatus == undefined) {
							Ext.getCmp("btnupcommit").setDisabled(true);
							store.removeAll();
							 Ext.Msg.alert('提示', '此计划未编辑!')
						} else if (signStatus == "0" || signStatus == "12") {
							 Ext.Msg.alert('提示', '此计划未上报!')
							store.removeAll();
						} else if (signStatus == "1"||signStatus =="2"||signStatus =="3"||signStatus =="4"||
							signStatus =="5"||signStatus =="6"|| signStatus =="7"||signStatus =="8"||
							signStatus =="9"||signStatus =="10") {
							Ext.getCmp("btnupcommit").setDisabled(true);
							store.removeAll();
							 Ext.Msg.alert('提示', '此计划已上报!')
						}

						else if (signStatus == "11"||signStatus =="13") {
							Ext.getCmp("btnupcommit").setDisabled(true);
							store.removeAll();
							 Ext.Msg.alert('提示', '此计划已审批!')

						} else {
							 Ext.Msg.alert('提示', '此计划未编辑!')
							store.removeAll();
						};

					}
				}, 'planTime=' + planTime.getValue() + '&editBy='
						+ editBy.getValue());
	}
	
//	function init() {
//		//update by sychen 20100328
//		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobDepMain.action',
//				{
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result.baseInfo != null) {
//							if (result.baseInfo.signStatus =='1'||result.baseInfo.signStatus =='2'||
//							    result.baseInfo.signStatus =='3'||result.baseInfo.signStatus =='4'||
//							    result.baseInfo.signStatus =='5'||result.baseInfo.signStatus =='6'||
//							    result.baseInfo.signStatus =='7'||result.baseInfo.signStatus =='8'||
//							    result.baseInfo.signStatus =='9'||result.baseInfo.signStatus =='10'||
//							    result.baseInfo.signStatus == '11'||result.baseInfo.signStatus == '13') {
//								// 已上报的记录，为使不能重复，只赋signStatus为2，下面判断为所有按钮不可用
//								signStatus = result.baseInfo.signStatus;
//								store.rejectChanges();
//								store.load({
//											params : {
//												// depMainId : depMainId
//												// .getValue(),
//												start : 0,
//												limit : 18
//											}
//										});
//							} else {
//								// 设定默认工号
//								var mainId = result.baseInfo.depMainId;
//								var workerName = result.editByName;
//								var date = result.editDate;
//								var DeptName = result.editDepName;
//
//								workFlowNo = result.baseInfo.workFlowNo;
//								signStatus = result.baseInfo.signStatus;
//								depMainId.setValue(mainId);
//								editByName.setValue(workerName);
//								editDate.setValue(date);
//								editDepName.setValue(DeptName);
//								store.rejectChanges();
//
//								store.load({
//											params : {
//												depMainId : depMainId
//														.getValue(),
//												start : 0,
//												limit : 18
//											}
//										});
//							}
//						} else {
//							getWorkCode();
//							signStatus = undefined;
//
//						}
//
//						// 选时间后所有按钮先全部可用
//						// Ext.getCmp("btnapprove").setDisabled(false);
//
////						Ext.getCmp("btnadd").setDisabled(false);
////						Ext.getCmp("btndelete").setDisabled(false);
////						Ext.getCmp("btnsave").setDisabled(false);
////						Ext.getCmp("btncancer").setDisabled(false);
//						Ext.getCmp("btnupcommit").setDisabled(false);
//
//						// 页面载入时根据工作流状态控制哪些按钮可用
//						if (signStatus == undefined) {
//							Ext.getCmp("btnupcommit").setDisabled(true);
//							store.removeAll();
//							 Ext.Msg.alert('提示', '此计划未编辑!')
//						} else if (signStatus == "0" || signStatus == "12") {
//							 Ext.Msg.alert('提示', '此计划未上报!')
//							store.removeAll();
//						} else if (signStatus == "1"||signStatus =="2"||signStatus =="3"||signStatus =="4"||
//							signStatus =="5"||signStatus =="6"|| signStatus =="7"||signStatus =="8"||
//							signStatus =="9"||signStatus =="10") {
//							// 此时无用，因为此时signStatus不会被赋值为1。
////							Ext.getCmp("btnadd").setDisabled(true);
////							Ext.getCmp("btndelete").setDisabled(true);
////							Ext.getCmp("btnsave").setDisabled(true);
////							Ext.getCmp("btncancer").setDisabled(true);
//							Ext.getCmp("btnupcommit").setDisabled(true);
//							store.removeAll();
//							 Ext.Msg.alert('提示', '此计划已上报!')
//						}
//
//						else if (signStatus == "11"||signStatus =="13") {
//							// 此时无用，因为此时signStatus不会被赋值为2。
////							Ext.getCmp("btnadd").setDisabled(true);
////							Ext.getCmp("btndelete").setDisabled(true);
////							Ext.getCmp("btnsave").setDisabled(true);
////							Ext.getCmp("btncancer").setDisabled(true);
//							Ext.getCmp("btnupcommit").setDisabled(true);
//							store.removeAll();
//							// Ext.getCmp("btnapprove").setDisabled(true);
//							 Ext.Msg.alert('提示', '此计划已审批!')
//
//						} else {
//							 Ext.Msg.alert('提示', '此计划未编辑!')
//							store.removeAll();
//							// alert(signStatus)
//						};
//
//					}
//				}, 'planTime=' + planTime.getValue() + '&editBy='
//						+ editBy.getValue());
//	}
	
	var editTime = new Ext.form.TextField({
				name : 'editTime',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM-dd"
								});
						this.blur();
					}
				}
			});		
			
	editTime.setValue(getDate());	
	
	// 计划时间
	var planTime = new Ext.form.TextField({

				name : 'planTime',
				fieldLabel : '计划时间',
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '',
							alwaysUseStartDate : true,
							dateFmt : "yyyy-MM"
								// ,
								// onpicked : function() {
								// year.clearInvalid();
								//
								// },
								// onclearing : function() {
								// year.markInvalid();
								//
								// }
							});
						this.blur();
					}
				}

			});
	planTime.setValue(getToMonth());

	function query() {
		getDepCodeOnly();

	}
	// 编辑人名称
	var editByName = new Ext.form.TextField({
		readOnly : true
			// fieldLabel : '编辑人'
		});

	// 编辑时间
	var editDate = new Ext.form.TextField({
		readOnly : true,
		name : 'editDate'
			// fieldLabel : '编辑时间'
		});

	// 编辑人所在部门名称
	var editDepName = new Ext.form.TextField({
				// fieldLabel : '编辑部门',
				readOnly : true

			});

	// 编辑人编码
	var editBy = new Ext.form.Hidden({
				name : 'editBy'
			});
	// 编辑人所在部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});
	// 编辑人所在部门一级部门编码
	// var editDepcdeone = new Ext.from.Hidden({
	// name : 'editDepcodeone'
	// });
	// 计划部门id
	var depMainId = new Ext.form.Hidden({
				name : 'depMainId'
			});

	//

//	var MyRecord = Ext.data.Record.create([{
//				name : 'jobId'
//			}, {
//				name : 'depMainId'
//			}, {
//				name : 'jobContent'
//			}, {
//				name : 'completeData'
//			}, {
//				name : 'ifComplete'
//			}, {
//				name : 'completeDesc'
//			}]);
//
//	var dataProxy = new Ext.data.HttpProxy({
//				url : 'manageplan/getBpJPlanJobDepDetail.action '
//			});
//
//	var theReader = new Ext.data.JsonReader({
//				root : "",
//				totalProperty : ""
//
//			}, MyRecord);
	//update by sychen 20100504		
	var MyRecord = Ext.data.Record.create([{
				name : 'jobId',
				mapping : 0
			}, {
				name : 'depMainId',
				mapping : 1
			}, {
				name : 'jobContent',
				mapping : 2
			}, {
				name : 'ifComplete',
				mapping : 3
			}, {
				name : 'completeDesc',
				mapping : 4
			}, {
				name : 'completeData',
				mapping : 5
			}, {
				name : 'editDepcode',
				mapping : 6
			},{
				name : 'deptName',
				mapping : 7
			},{
				name:'editbyname',
				mapping: 8
			},{
				name:'chargeBy',
				mapping: 9
			},{
				name:'chargeName',
				mapping: 10
			},{
				name:'editDate',
				mapping:11
			},{
				name:'orderBy',
				mapping:13
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getBpJPlanJobDepDetailApprove.action '
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

	// 增加
	function addRecord() {
//		if (depMainId.getValue() == null) {
//			Ext.Msg.alert("提示", "请先选择计划时间！");
//			return;
//		}
		var count = store.getCount();

		var currentIndex = count;

		var o = new MyRecord({
                    'orderBy':'',// add by sychen 20100505
					'ifComplete' : '0',
					'depMainId' : depMainId.getValue(),
					'chargeBy':editBy.getValue(),
					'chargeName':editByName.getValue(),
					'jobContent' : '',
					'completeData' : '0'

				});

		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 2);
		// resetLine();
	}
	// 删除记录

	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();

				if (member.get("jobId") != null) {

					ids.push(member.get("jobId"));

				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}

	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;

			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
                if (modifyRec[i].get("orderBy") == null
								|| modifyRec[i].get("orderBy") == "") {
							Ext.Msg.alert("提示", "序号不能为空！");
							return;
						}
				updateData.push(modifyRec[i].data);

			}
			Ext.Ajax.request({
						url : 'manageplan/saveBpJPlanJobDep.action',
						method : 'post',
						params : {
							planTime : planTime.getValue(),
							mainId : depMainId.getValue(),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(";"),
							deptCode : editDepcode.getValue()
						},
						success : function(form, action) {
							var o = eval('(' + form.responseText + ')');
							depMainId.setValue(o.obj);
							// 保存后上报按钮可用
							Ext.getCmp("btnupcommit").setDisabled(false);
							Ext.MessageBox.alert('提示信息', "保存成功");
							store.rejectChanges();
							ids = [];
							store.load({

										params : {
											depMainId : o.obj,
											start : 0,
											limit : 18
										}
									});
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancel() {
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
	// 上报
	function upcommit() {
		var url = "../SignWorkPlan/commitDeptWorkPlan.jsp";
		var args = new Object();
		args.prjNo = depMainId.getValue();
		args.entryId = workFlowNo;
//		args.workflowType = "bqDeptJobplan";//update by sychen 20100326
		args.workflowType = "bqDeptWorkPlanApprove";
		args.actionId = actionId;
		args.prjTypeId = "";
		args.prjStatus = signStatus;
		args.roles = true;
		args.deptId = deptId;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');
		if (obj) {
			workFlowNo = obj.workFlowNo;
			signStatus = obj.signStatus;
			store.load({
						params : {
							depMainId : depMainId.getValue()
						}
					});
			// 上报后增删存取报按钮设为不可用，签字可用
			Ext.getCmp("btnadd").setDisabled(true);
			Ext.getCmp("btndelete").setDisabled(true);
			Ext.getCmp("btnsave").setDisabled(true);
			Ext.getCmp("btncancer").setDisabled(true);
			Ext.getCmp("btnupcommit").setDisabled(true);
		}
		// Ext.getCmp("btnapprove").setDisabled(false);

	}
	// // 签字
	// function approve() {
	//
	// var url = "../SignWorkPlan/approveDeptWorkPlan.jsp";
	//
	// var args = new Object();
	// args.prjNo = depMainId.getValue();
	// args.entryId = workFlowNo;
	// args.workflowType = "bqDeptJobplan";
	// args.prjTypeId = "";
	// args.prjStatus = signStatus;
	//
	// window.showModalDialog(url, args,
	// 'status:no;dialogWidth=750px;dialogHeight=550px');
	// // 按钮设为不可用
	//
	// }

	// 定义grid
	// 弹出画面

	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});

	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = grid.selModel.getSelected();
						record.set("jobContent", Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]
	});
	var grid = new Ext.grid.EditorGridPanel({
		// region : "west",
		store : store,
		layout : 'fit',
		// width:'0.5',
		columns : [

				sm, // 选择框
//				number, 
				// add by sychen 20100505
				  {
				    width : 40,
					header : "序号",
					sortable : false,
					dataIndex : 'orderBy',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})
				},
					{
					width : 300,
					header : "工作内容",

					sortable : false,
					dataIndex : 'jobContent',
					editor : new Ext.form.TextArea({
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
													grid.stopEditing();
													var record = grid
															.getSelectionModel()
															.getSelected();
													var value = record
															.get('jobContent');
													memoText.setValue(value);
													win.x = undefined;
													win.y = undefined;
													win.show();
												})
									}
								}
							})

				}, {
					header : "责任人",
					width : 100,
					sortable : true,
					dataIndex : 'chargeName',
						editor : new Ext.form.ComboBox({
						mode : 'remote',
						editable : true,
						onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:550px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {
								var record = grid.getSelectionModel().getSelected();
								record.set("chargeBy", rvo.workerCode);
								record.set("chargeName", rvo.workerName);
							}
						}
					})
				}, {
					header : "完成时间",
					sortable : false,
					dataIndex : 'completeData',
					editor : new Ext.form.ComboBox({
								name : 'name111',
								hiddenName : 'hiddenName111',
								mode : 'local',
								triggerAction : 'all',
								store : new Ext.data.SimpleStore({
											fields : ['name', 'value'],
											data : [['当月', 0], ['跨月', 1],
													['长期', 2], ['全年', 3]]
										}),
								displayField : 'name',
								valueField : 'value'

							}),
					renderer : function changeIt(val) {
						if (val == '0') {
							return "当月";
						} else if (val == '1') {
							return "跨月";
						} else if (val == '2') {
							return "长期";
						} else if (val == '3') {
							return "全年";
						} else {
							return "";
						}
					}
				}],
		tbar : ["计划时间:", planTime, '-', {
					id : 'btnQuery',
					text : "查询",
					iconCls : 'query',
					handler : function() {
						query();
					}
				}, '-', {
					text : "新增",
					id : 'btnadd',
					iconCls : 'add',

					handler : addRecord
				}, '-', {
					text : "删除",
					id : 'btndelete',
					iconCls : 'delete',
					handler : deleteRecords

				}, '-', {
					text : "保存",
					id : 'btnsave',
					iconCls : 'save',
					handler : saveModifies
				}, '-', {
					text : "取消",
					id : 'btncancer',
					iconCls : 'cancer',
					handler : cancel
				}, '-', {
					text : "上报",
					id : 'btnupcommit',
					iconCls : 'upcommit',
					handler : upcommit
				}
		// ,'-',{
		// text : "审批",
		// id : 'btnapprove',
		// iconCls : 'approve',
		// handler : approve
		// }
		],
		sm : sm, // 选择框的选择 Shorthand for
		// selModel（selectModel）
		// 分页
		bbar : ["编辑部门:", editDepName, '-', "编辑人:", editByName, '-', "编辑时间:",
				editDate],
		viewConfig : {
			forceFit : true
		}
	});
	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [

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
	query();
		// 页面载入未选时间时所有按钮不可用
		// Ext.getCmp("btnapprove").setDisabled(true);
		// Ext.getCmp("btnadd").setDisabled(true);
		// Ext.getCmp("btndelete").setDisabled(true);
		// Ext.getCmp("btnsave").setDisabled(true);
		// Ext.getCmp("btncancer").setDisabled(true);
		// Ext.getCmp("btnupcommit").setDisabled(true);

})
