Ext.onReady(function() {
	var workFlowNo;

	var actionId;
	var deptId;
	function getDate1() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	function getDate2() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate() + 6;
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	function getMon() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t /*
									 * + "-"; t = d.getDate(); s += (t > 9 ? "" :
									 * "0") + t + " ";
									 */
		return s;
	}
	var weekSelect = new Ext.form.ComboBox({
				readOnly : true,
				name : 'name',
				hiddenName : 'week',
				mode : 'local',
				width : 70,
				value : "1",
				fieldLabel : '完成情况',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						store.reload();
						init();

					}
				},

				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['第一周', '1'], ['第二周', '2'], ['第三周', '3'],
									['第四周', '4'], ['第五周', '5'], ['第六周', '6']]
						}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
						queryRepair();
					}
				}
			})

	var planStart = new Ext.form.TextField({
				style : 'cursor:pointer',
				name : 'time',
				fieldLabel : '计划时间',
				readOnly : true,
				anchor : "80%",
				value : getMon(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM',
									isShowClear : false,
									onpicked : function(v) {
										store.reload();
										init();
										this.blur();
									}
								});
					}
				}
			});

	var weekStart = new Ext.form.TextField({
				name : "weekStartTime",
				fieldLabel : '周起始日期',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : true,
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							isShowClear : false,
							alwaysUseStartDate : true
/*
 * , isShowClear : false
 */
							});
						this.blur();
					}
				}
			});
	var weekEnd = new Ext.form.TextField({
				name : "weekEndTime",
				fieldLabel : '周结束日期',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : true,
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源
	var Record = new Ext.data.Record.create([sm, {

				name : 'planDetailId'
			}, {
				name : 'planDepId'
			}, {
				name : 'content'
			}, {
				name : 'chargeDep'
			}, {
				name : 'chargeDepName'
			}, {
				name : 'assortDep'
			}, {
				name : 'assortDepName'
			}, {
				name : 'beginTime'
			}, {
				name : 'endTime'
			}, {
				name : 'days'
			}, {
				name : 'memo'
			}, {
				name : 'isUse'
			}, {
				name : 'enterpriseCode'
			}, {
				name : 'DepId'
			}, {
				name : 'planTime'
			}, {
				name : 'editDep'
			}, {
				name : 'editDepName'
			}, {
				name : 'editBy'
			}, {
				name : 'editByName'
			}, {
				name : 'editDate'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'status'
			}, {
				name : 'weekStartTime'
			}, {
				name : 'weekEndTime'
			}]);
	// 填写人
	var fillByName = new Ext.form.TextField({
				readOnly : true,
				name : 'editByName'

			});
	var fillByCode = new Ext.form.Hidden({
				name : 'editBy'
			});

	// 填写时间

	var fillDate = new Ext.form.TextField({
				readOnly : true,
				name : 'editDate'
			});

	// 登陆人部门
	var logDep = new Ext.form.TextField({
				readOnly : true

			});
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							var workerCode = result.workerCode;
							var workerName = result.workerName;
							// 设定默认部门
							DeptCode = result.deptCode;
							DeptName = result.deptName

							deptId = result.deptId;
							logDep.setValue(DeptName);
							editDepcode.setValue(DeptCode);

						}
					}
				});
	}
	getWorkCode();
	var bbar = new Ext.Toolbar({
				items : ["所在部门:", logDep, '-', "填写人:", fillByName, fillByCode,
						'-', "填写时间:", fillDate]
			});

	var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'add',
							text : "增加",
							iconCls : 'add',
							handler : addRepair
						}, '-', {
							id : 'delete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteRepair
						}, '-', {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveRepair
						}, '-', {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}, '-', {
							id : 'export',
							text : "上报",
							iconCls : 'upcommit',
							handler : reportRepair
						}]
			});

	function cancerChange() {

		queryRepair();
		store.rejectChanges();

	}
	function accPlanTime(start) {
		if (!start) {
			start = planStart.getValue();
		}
		var plantime = start.substr(0, 4) + start.substr(5, 2);
		plantime += weekSelect.getValue();
		return plantime;
	}

	function queryRepair() {
		if (planStart.getValue() == null || planStart.getValue() == ""
				|| weekSelect.getValue() == null || weekSelect.getValue() == "") {
			Ext.Msg.alert("提示", "请先选择计划时间和计划周！");
		}

		store.load({
					params : {
						start : 0,
						limit : 18,
						planTime : accPlanTime(),
						deptCode : editDepcode.getValue()
					}
				})

	}

	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : queryRepair
			});

	var headerTbar = new Ext.Toolbar({
				items : ["计划时间", planStart, "计划周", weekSelect, btnQuery, '-',
						"周起始时间", weekStart, "周结束时间", weekEnd]
			});
	var tbarPanel = new Ext.Panel({
				layout : 'form',
				items : [headerTbar, gridTbar]
			});
	function addRepair() {
		if (accPlanTime() == null) {
			Ext.Msg.alert("提示", "请先选择计划时间！");
			return;
		}
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
					'content' : '',
					'chargeDep' : editDepcode.getValue(),// add by sychen
															// 20100319
					'chargeDepName' : logDep.getValue(),// add by sychen
														// 20100319
					'assortDep' : '',
					'beginTime' : '',
					'endTime' : '',
					'days' : '',
					'memo' : ''

				});
		repairgrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		repairgrid.startEditing(currentIndex, 1);

	}

	function saveRepair() {
		var alertMsg = "";
		repairgrid.stopEditing();
		var modifyRec = repairgrid.getStore().getModifiedRecords();
		if (store.getCount() == modifyRec.length) {
			if (weekStart.getValue() == null || weekStart.getValue() == "") {
				Ext.Msg.alert("提示", "请先选择周计划起始时间！");
				return;
			}
			if (weekEnd.getValue() == null || weekEnd.getValue() == "") {
				Ext.Msg.alert("提示", "请先选择周计划结束时间！");
				return;
			}
			if (weekEnd.getValue() < weekStart.getValue()) {
				Ext.Msg.alert("提示", "周计划结束时间应大于周计划起始时间！");
				return;
			}
		}
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("content") == null
									|| modifyRec[i].get("content") == "") {
								alertMsg += "设备名称及检修内容不能为空</br>";
							}
							if (modifyRec[i].get("chargeDepName") == null
									|| modifyRec[i].get("chargeDepName") == "") {
								alertMsg += "负责单位不能为空</br>";
							}
							if (modifyRec[i].get("beginTime") == null
									|| modifyRec[i].get("beginTime") == "") {
								alertMsg += "计划开工日期不能为空</br>";
							}
							if (modifyRec[i].get("endTime") == null
									|| modifyRec[i].get("endTime") == "") {
								alertMsg += "计划结束日期不能为空</br>";
							}

							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'manageplan/saveRepair.action',
									method : 'post',
									params : {
										weekStart : weekStart.getValue(),
										weekEnd : weekEnd.getValue(),
										isUpdate : Ext.util.JSON
												.encode(updateData),
										planDate : accPlanTime(),
										planDeptCode : DeptCode,
										approve:''
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！')
										store.rejectChanges();
										store.reload();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
	function deleteRepair() {
		var sm = repairgrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.planDetailId) {
					ids.push(member.planDetailId);
				}
				store.remove(selected[i]);
			}

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'manageplan/deleteRepair.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												store.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								store.reload();
							}
						});
			}
		}
	}

	function reportRepair() {
		if (store.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可进行上报！');
			return;
		}
		if (store.getCount() > 0) {
			var workflowStatus = store.getAt(0).get("status");
			if (workflowStatus == 0 || workflowStatus == 3) {
				Ext.Msg.confirm('提示', '确认要进行上报吗？', function(id) {
					if (id == 'yes') {
						var url = "reportSign.jsp";
						var depmainid = store.getAt(0).get("planDepId");
						var workFlowNo = store.getAt(0).get("workFlowNo");
						var args = new Object();
						args.entryId = workFlowNo;

						args.actionId = actionId;
						args.deptId = deptId;
						args.depmainid = depmainid;
						args.flowCode = "bqRepair";
						var obj = window
								.showModalDialog(url, args,
										'status:no;dialogWidth=770px;dialogHeight=550px');

						if (obj) {
							queryRepair();
						}
					}
				})
			} else {
				Ext.Msg.alert("提示", "请先填写数据！");
				return false;
			}
		}
	}

	var planDetailId = new Ext.form.Hidden({
				name : 'planDetailId'
			});

	var store = new Ext.data.JsonStore({
				url : 'manageplan/getAllRepair.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	store.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							planTime : accPlanTime(this.value),
							deptCode : editDepcode.getValue()

						});

			});
			

	store.on('load', function() {
				if (store.getAt(0) != null && store.getAt(0) != "") {
					fillByCode.setValue(store.getAt(0).data.editBy);
					fillByName.setValue(store.getAt(0).data.editByName);
					fillDate.setValue(store.getAt(0).data.editDate.substring(0,10));
							/*+ " "
							+ store.getAt(0).data.editDate.substring(11, 20));*/
					if (store.getAt(0).data.status == "1"
							|| store.getAt(0).data.status == "2") {
						repairgrid.stopEditing(true);
						store.rejectChanges();
						Ext.get("add").dom.disabled = true;
						Ext.get("delete").dom.disabled = true;
						Ext.get("save").dom.disabled = true;
						Ext.get("cancer").dom.disabled = true;
						Ext.get("export").dom.disabled = true;
					} else {
						Ext.get("add").dom.disabled = false;
						Ext.get("delete").dom.disabled = false;
						Ext.get("save").dom.disabled = false;
						Ext.get("cancer").dom.disabled = false;
						Ext.get("export").dom.disabled = false;
					}

				} else {

					Ext.get("add").dom.disabled = false;
					Ext.get("delete").dom.disabled = false;
					Ext.get("save").dom.disabled = false;
					Ext.get("cancer").dom.disabled = false;
					Ext.get("export").dom.disabled = false;

					fillByCode.setValue(null);
					fillByName.setValue(null);
					fillDate.setValue(null);
				}
			})

	function checkTime1() {
		var startdate1 = this.value;
		startdate2 = startdate1.substring(0, 10);

		var enddate1 = repairgrid.getSelectionModel().getSelected()
				.get("endTime");
		if (enddate1 != null && enddate1 != "") {
			enddate2 = enddate1.substring(0, 10);

			if (startdate2 != "") {
				if (startdate2 > enddate2 && enddate2 != "") {
					Ext.Msg.alert("提示", "开始日期必须早于结束日期");
					return;
				}
			}
		}

		repairgrid.getSelectionModel().getSelected().set("beginTime",
				startdate2);
	}
	function checkTime2() {
		var endtime1 = this.value;
		var endtime2 = endtime1.substring(0, 10);

		var beginTime1 = repairgrid.getSelectionModel().getSelected()
				.get("beginTime");
		if (beginTime1 != null && beginTime1 != "") {
			beginTime2 = beginTime1.substring(0, 10);

			if (endtime2 != "" && beginTime2 != "") {
				if (endtime2 < beginTime2 && endtime2 != "") {
					Ext.Msg.alert("提示", "结束日期必须晚于开始日期");
					return;
				}
			}
		}
		repairgrid.getSelectionModel().getSelected().set("endTime", endtime2);
	}

	// 页面的Grid主体
	var repairgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '设备名称及检修内容',
					dataIndex : 'content',
					align : 'left',
					width : 220,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'content'
							})
				}, {
					header : "负责单位",
					width : 100,
					align : 'center',
					sortable : true,
					dataIndex : 'chargeDepName'

				}, {
					header : "配合单位",
					width : 100,
					align : 'center',
					value : "",
					sortable : true,
					dataIndex : 'assortDep',
					editor : new Ext.form.ComboBox({
						fieldLabel : '配合单位',
						value : "请选择...",
						mode : 'remote',
						editable : false,
						readOnly : true,
						width : 100,
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

								var record = repairgrid.getSelectionModel()
										.getSelected();
								record.set("assortDep", rvo.names);

								editDepcode.setValue(rvo.codes);

							}
						}
					})

				}, {
					header : '计划开工日期',
					dataIndex : 'beginTime',
					width : 120,
					align : 'center',
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
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
					dataIndex : 'endTime',
					readOnly:true,
					width : 120,
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
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
					header : '天数',
					dataIndex : 'days',
					align : 'center',
					width : 80,
					editor : new Ext.form.TextField({
								id : 'chargeBy',
								readOnly : true
							})
				}, {
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 180,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'memo'
							})

				}],

		sm : sm, // 选择框的选择
		tbar : tbarPanel,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	// 进入页面时执行一次查询
	queryRepair();
	repairgrid.on('beforeedit', function() {
				if (Ext.get("save").dom.disabled == true) {
					return false;
				}
			})
	// add by sychen 20100319
	function init() {

		Ext.Ajax.request({
					method : 'post',
					url : 'manageplan/getRepairDeptObj.action',
					params : {
						planTime : accPlanTime(),
						deptCode : editDepcode.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.list[0] != null) {
							weekStart.setValue(result.list[0].weekStartTime);
							weekEnd.setValue(result.list[0].weekEndTime);
						} else {
							weekStart.setValue(null);
							weekEnd.setValue(null);
						}
					}
				});
	}
	init();

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [repairgrid]
						}, {
							region : 'south',
							items : [bbar],
							height : 25,
							style : 'padding-bottom:0.8px'
						}]
			});
})