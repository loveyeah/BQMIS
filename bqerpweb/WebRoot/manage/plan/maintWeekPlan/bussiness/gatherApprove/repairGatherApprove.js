Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	var method = "add";
	var ids = new Array();
	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	
	// 根据开始时间和结束时间计算安全天数
	function DateDiff(sDate1, sDate2) {
		// sDate1和sDate2是年-月-日格式
		var arrDate, objDate1, objDate2, intDays;
		arrDate = sDate1.split("-");
		objDate1 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);// 转换为月-日-年格式
		arrDate = sDate2.split("-");
		objDate2 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);
		intDays = parseInt(Math.abs(objDate1 - objDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
		return intDays;
	}

	function checkTime1() {
		var date = this.value;
		var rec = grid.getSelectionModel().getSelected();
		grid.getSelectionModel().getSelected().set("beginTime", date);
		if (rec.get("endTime") != null && rec.get("endTime") != "") {
			var days = DateDiff(rec.get("endTime"), date) + 1;
			grid.getSelectionModel().getSelected().set("days", days);
		}
	}
	function checkTime2() {
		var date = this.value;
		var rec = grid.getSelectionModel().getSelected();
		var days = DateDiff(date, rec.get("beginTime")) + 1;
		grid.getSelectionModel().getSelected().set("endTime", date);
		grid.getSelectionModel().getSelected().set("days", days);
	}
	
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
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	var enddate2 = enddate.add(Date.DAY, +6);
	// // 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	function getMon() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	
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
							var DeptCode = result.deptCode;
							var DeptName = result.deptName

							depMainId.setValue(0);
							gatherDep.setValue(DeptName);
							editBy.setValue(workerCode);
							editDepcode.setValue(DeptCode);

							store.rejectChanges();

						}
					}
				});
	}

	// 部门
	var gatherDep = new Ext.form.TextField({
				readOnly : true,
				name : 'gatherDep'
			});

	// 制表人
	var gatherBy = new Ext.form.TextField({
				readOnly : true,
				name : 'gatherBy'
			});

	// 制表时间

	var gatherDate = new Ext.form.TextField({
				readOnly : true,
				name : 'gatherDate'
			});

	// 编辑人编码
	var editBy = new Ext.form.Hidden({
				name : 'editBy'
			});
	// 编辑人所在部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});
	// 部门id
	var depMainId = new Ext.form.Hidden({
				name : 'depMainId'
			});
			
	var planTime = new Ext.form.TextField({
				id : 'planTime',
				fieldLabel : '周计划时间',
				style : 'cursor:pointer',
				value : getMon(),
				readOnly : true,
				width : 80,
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
	var planWeek = new Ext.form.ComboBox({
				name : 'planWeek',
				hiddenName : 'planWeek',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['第1周', '1'], ['第2周', '2'], ['第3周', '3'],
									['第4周', '4'], ['第5周', '5'], ['第6周', '6']]
						}),
				value : "1",
				fieldLabel : '计划周',
				triggerAction : 'all',
				readOnly : true,
				valueField : 'value',
				displayField : 'name',
				mode : 'local',
				width : 80,
				listeners : {
					"select" : function() {
						query();
					}
				}
			})

	var planDetailId = new Ext.form.Hidden({
				id : 'planDetailId',
				name : 'planDetailId'
			});

	var gatherId = new Ext.form.Hidden({
				id : 'gatherId',
				name : 'gatherId'
			});

	var MyRecord = Ext.data.Record.create([{
				name : 'planDetailId',
				mapping : 0
			}, {
				name : 'gatherId',
				mapping : 1
			}, {
				name : 'content',
				mapping : 2
			}, {
				name : 'chargeDep',
				mapping : 3
			}, {
				name : 'chargeDeptName',
				mapping : 4
			}, {
				name : 'assortDep',
				mapping : 5
			}, {
				name : 'assortDeptName',
				mapping : 6
			}, {
				name : 'beginTime',
				mapping : 7
			}, {
				name : 'endTime',
				mapping : 8
			}, {
				name : 'days',
				mapping : 9
			}, {
				name : 'memo',
				mapping : 10
			}, {
				name : 'planTime',
				mapping : 11
			}, {
				name : 'gatherDep',
				mapping : 12
			}, {
				name : 'gatherDeptName',
				mapping : 13
			}, {
				name : 'gatherBy',
				mapping : 14
			}, {
				name : 'gatherName',
				mapping : 15
			}, {
				name : 'gatherDate',
				mapping : 16
			}, {
				name : 'workFlowNo',
				mapping : 17
			}, {
				name : 'signStatus',
				mapping : 18
			}, {
				name : 'memo',
				mapping : 19
			}]);
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managemaintweek/getRepairGatherDetailList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : 0,
							totalProperty : 'totalCount',
							root : 'list'
						}, MyRecord)
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});

	function query() {
		store.baseParams = {
			approve : 'Y',
			planTime : planTime.getValue(),
			planWeek : planWeek.getValue()
		}
		store.load({
					start : 0,
					limit : 18
				})
		initStatus()
	}
	
		// 增加
	function addRecord() {
	
		var count = store.getCount();
		var currentIndex = count;
		var o = new MyRecord({
					'content' : '',
					'chargeDep' : editDepcode.getValue(),
					'chargeDeptName' : gatherDep.getValue(),
					'assortDep' : '',
					'assortDeptName' : '',
					'beginTime' : '',
					'endTime' : '',
					'days' : '',
					'memo' : ''

				});
		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 1);

	}
	
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var alertMsg = "";
		var modifyRec = grid.getStore().getModifiedRecords();
		var days;
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("content") == null
								|| modifyRec[i].get("content") == "") {
							alertMsg += "设备名称及检修内容不能为空</br>";
						}
						if (modifyRec[i].get("chargeDeptName") == null
								|| modifyRec[i].get("chargeDeptName") == "") {
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
						if (modifyRec[i].get("beginTime") > modifyRec[i]
								.get("endTime")) {
							alertMsg += "“计划结束日期”不能小于“计划开工日期”";
						}
						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'managemaintweek/addRepairGather.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(","),
									planDate : planTime.getValue(),
									planWeek : planWeek.getValue(),
									gatherDept : editDepcode.getValue(),
									'gatherBy' : gatherBy.getValue(),
									'gatherDate' : gatherDate.getValue()
								},
								success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
									}
									store.rejectChanges();
									ids = [];
									query();
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
	
	// 取消
	function cancerRecords() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			query();
			store.rejectChanges();
			ids = [];
		} else {
			query();
			store.rejectChanges();
			ids = [];
		}
	}

	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			Ext.Msg.confirm('提示', '确认删除此检修周计划吗?', function(response) {
						if (response == 'yes') {
							for (var i = 0; i < selected.length; i += 1) {
								var member = selected[i];
								if (member.get("planDetailId") != null) {
									ids.push(member.get("planDetailId"));
								}
								grid.getStore().remove(member);
								grid.getStore().getModifiedRecords()
										.remove(member);
							}
						}
					});
		}

	}
	
	function approveFun() {
		if (store.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行审批！');
			return;
		}
		var url = "repairSign.jsp";
		var gatherId = store.getAt(0).get("gatherId");
		var workFlowNo = store.getAt(0).get("workFlowNo");
		var args = new Object();
		args.entryId = workFlowNo;
		args.gatherId = gatherId;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			query();
		}
	}
	var tbar = new Ext.Toolbar({
				items : ["周计划时间:", planTime, '-',"计划周:", planWeek, '-',{
							text : "查询",
							iconCls : 'query',
							handler : query
						},{
							text : "增加",
							id : 'btnAdd',
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							id : 'btnsave',
							text : "保存",
							iconCls : 'save',
							handler : saveModifies
						}, '-', {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerRecords

						}, '-', {
							text : "删除",
							id : 'btndelete',
							iconCls : 'delete',
							handler : deleteRecords
						}, '-', {
							id : 'approveBtu',
							text : '审批',
							iconCls : 'approve',
							handler : approveFun
						}]
			});

	var bbar = new Ext.Toolbar({
				items : ["部门:", gatherDep, '-', "制表人:", gatherBy, '-', "制表时间:",
						gatherDate]
			});
	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}), {
					header : "ID",
					sortable : true,
					dataIndex : 'planDetailId',
					hidden : true
				}, {
					width : 200,
					header : "设备名称及检修内容",
					sortable : false,
					dataIndex : 'content',
					editor : new Ext.form.TextField({
								id : 'content',
								allowBlank : false
							})
				}, {
					header : "负责单位",
					width : 100,
					sortable : true,
					dataIndex : 'chargeDeptName'
				}, {
					header : "配合单位",
					width : 100,
					sortable : true,
					dataIndex : 'assortDep',
						editor : new Ext.form.ComboBox({
						mode : 'remote',
//						forceSelection : true,
						editable : true,
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
								var record = grid.getSelectionModel().getSelected();
								record.set("assortDep", rvo.names);
//								record.set("assortDeptName", rvo.names);
							}
						}
					})
				}, {
					header : '计划开工日期',
					dataIndex : 'beginTime',
					width : 150,
					align : 'left',
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
					width : 150,
					align : 'left',
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
													onpicked : checkTime2
												});

									}
								}
							})
				}, {
					header : '天数',
					dataIndex : 'days',
					align : 'center',
					width : 100
				}, {
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'memo'
							})
				}],
//                tbar : gridTbar,
				sm : sm, // 选择框的选择 Shorthand for
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				viewConfig : {
					forceFit : true
				}
			});
			
			grid.on("rowclick", function() {

				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					gatherBy.setValue(rec.get("gatherName"));
					gatherDate.setValue(rec.get("gatherDate"));

				} else {
					gatherBy.setValue(null);
					gatherDate.setValue(null);
				}
			})
	var memoText = "\n1.各部门应严格按照周计划进行设备检修工作。2.周计划的检修项目完成后须经设备部、检修公司相关点检、高管的验收。3.周计划项目的改变须经检修计划专工的认可\n";
	var totalMemo = new Ext.form.TextArea({
				id : "totalMemo",
				fieldLabel : '备注',
				allowBlank : true,
				readOnly : true,
				value : memoText,
				width : 980,
				name : 'totalMemo'
			});

	var Form = new Ext.Toolbar({
				items : ["备注:", totalMemo]
			});
	
	function initStatus() {
		Ext.Ajax.request({
					method : 'post',
					url : 'managemaintweek/getRepairApproveGatherId.action',
					params : {
						planTime : planTime.getValue(),
						planWeek : planWeek.getValue()
					},
					success : function(response,options) {
						var res = response.responseText;
						if(res.toString() == '')
						{
							Ext.get("btnAdd").dom.disabled = true;
							Ext.get("approveBtu").dom.disabled = true;
							Ext.get("btndelete").dom.disabled = true;
							Ext.get("btnsave").dom.disabled = true;
							Ext.get("btnCancer").dom.disabled = true;
						}else{
							Ext.get("btnAdd").dom.disabled = false;
							Ext.get("approveBtu").dom.disabled = false;
							Ext.get("btndelete").dom.disabled = false;
							Ext.get("btnsave").dom.disabled = false;
							Ext.get("btnCancer").dom.disabled = false;
						}
					}
				});
	}		
			
	function init() {
		Ext.Ajax.request({
			url : 'managemaintweek/getRepairGatherInfo.action',
					method : 'post',
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '')
						{
							;
						}else{
							var year = res.toString().substring(0,4);
							var month = res.toString().substring(4,6);
							var week = res.toString().substring(6);
							planTime.setValue(year +"-" + month)
							planWeek.setValue(week);
						}
						
						query();
					}
				});
	}	
			
	getWorkCode()
	init();	
	new Ext.Viewport({
				layout : 'border',
				auotHeight : true,
				items : [{
							region : 'north',
							layout : 'fit',
							height : 25,
							items : [tbar]
						}, {
							region : 'center',
							layout : 'fit',
							items : [grid]
						}, {
							region : 'south',
							border : false,
							height : 110,
							items : [{
										xtype : 'panel',
										border : false,
										items : [{
													border : false,
													height : 75,
													items : [Form]
												}]
									}, {
										region : 'south',
										border : false,
										height : 25,
										items : [bbar]
									}]
						}]

			});

})