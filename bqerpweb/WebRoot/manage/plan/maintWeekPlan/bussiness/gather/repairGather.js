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
	// ---------------------从各部门已审批的页面中选择页面开始------------------------//
	var planTime1 = new Ext.form.TextField({
				id : 'planTime1',
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
	var planWeek1 = new Ext.form.ComboBox({
				name : 'planWeek1',
				hiddenName : 'planWeek1',
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
						copyQuery();
					}
				}
			})
	var startTime1 = new Ext.form.TextField({
				id : 'startTime1',
				style : 'cursor:pointer',
				readOnly : true,
				width : 100,
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

	var endTime1 = new Ext.form.TextField({
				id : 'endTime1',
				style : 'cursor:pointer',
				readOnly : true,
				width : 100,
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

	var copyRecord = Ext.data.Record.create([{
				name : 'planDetailId',
				mapping : 0
			}, {
				name : 'planDepId',
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
				name : 'editDep',
				mapping : 12
			}, {
				name : 'editDeptName',
				mapping : 13
			}, {
				name : 'editBy',
				mapping : 14
			}, {
				name : 'editrName',
				mapping : 15
			}, {
				name : 'editDate',
				mapping : 16
			}, {
				name : 'workFlowNo',
				mapping : 17
			}, {
				name : 'status',
				mapping : 18
			}]);
	var copyStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managemaintweek/getRepairApproveList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : 0,
							totalProperty : 'totalCount',
							root : 'list'
						}, copyRecord)
			});

	var copySm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});

	function copyQuery() {
		copyStore.baseParams = {
			status : "'2'",
			planTime : planTime1.getValue(),
			planWeek : planWeek1.getValue()
		}
		copyStore.load({
					start : 0,
					limit : 18
				})
	}
	function copyConfirm() {
		var array = new Array();
		if (copySm.hasSelection()) {
			var records = copyGrid.selModel.getSelections();
			var record;
			var recordArray = [];
			var isExist;
			for (var i = 0; i < records.length; i++) {
				storeAdd(records[i])
			}
			grid.getView().refresh();
			for (var i = 0; i < records.length; i++) {
				record = records[i];
				// copyStore.remove(record);
				copyGrid.getView().refresh();
			}
			copyWin.hide();
		} else {
			Ext.Msg.alert('提示', '请先选择要增加的数据！')
		}

	}
	var copyTbar = new Ext.Toolbar({
				items : ["周计划时间:", planTime1, '-', "计划周:", planWeek1, '-',
						"周开始时间:", startTime1, "周结束时间", endTime1, '-', {
							text : "查询",
							iconCls : 'query',
							handler : copyQuery
						}, '-', {
							id : "cofirmBtu",
							text : "确定",
							iconCls : 'confirm',
							handler : copyConfirm
						}]
			});

	var copyGrid = new Ext.grid.EditorGridPanel({
				store : copyStore,
				tbar : copyTbar,
				layout : 'fit',
				columns : [copySm, new Ext.grid.RowNumberer({
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
							dataIndex : 'content'
						}, {
							header : "负责单位",
							width : 100,
							sortable : true,
							dataIndex : 'chargeDep',
							renderer : function(value, metadata, record) {
								if (value != null)
									return record.get('chargeDeptName')
							}
						}, {
							header : "配合单位",
							width : 100,
							sortable : true,
							dataIndex : 'assortDep'
						}, {
							header : '计划开工日期',
							dataIndex : 'beginTime',
							width : 150,
							align : 'left'
						}, {
							header : '计划结束日期',
							dataIndex : 'endTime',
							width : 150,
							align : 'left'
						}, {
							header : '天数',
							dataIndex : 'days',
							align : 'center',
							width : 100
						}, {
							header : '备注',
							dataIndex : 'memo',
							align : 'center',
							width : 100
						}],

				sm : copySm, // 选择框的选择 Shorthand for
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : copyStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				viewConfig : {
					forceFit : true
				}
			});

	// ---------------------从各部门已审批的页面中选择页面结束------------------------//

	// ---------------------从历史记录选择页面开始------------------------//

	var planTime2 = new Ext.form.TextField({
				id : 'planTime2',
				fieldLabel : '周计划时间',
				style : 'cursor:pointer',
				value : getMon(),
				readOnly : true,
				width : 58,
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
	var planWeek2 = new Ext.form.ComboBox({
				name : 'planWeek2',
				hiddenName : 'planWeek2',
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
				width : 55,
				listeners : {
					"select" : function() {
						selectQuery();
					}
				}
			})
	var startTime2 = new Ext.form.TextField({
				id : 'startTime2',
				style : 'cursor:pointer',
				readOnly : true,
				width : 70,
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

	var endTime2 = new Ext.form.TextField({
				id : 'endTime2',
				style : 'cursor:pointer',
				readOnly : true,
				width : 70,
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
	var queryKey = new Ext.form.TextField({
				id : 'queryKey',
				name : 'queryKey',
				width : 85
			});

	var planDetailId = new Ext.form.Hidden({
				id : 'planDetailId',
				name : 'planDetailId'
			});

	var gatherId = new Ext.form.Hidden({
				id : 'gatherId',
				name : 'gatherId'
			});

	var selectRecord = Ext.data.Record.create([{
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
	var selectStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managemaintweek/getRepairGatherDetailList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : 0,
							totalProperty : 'totalCount',
							root : 'list'
						}, selectRecord)
			});

	var selectSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	function selectQuery() {
		selectStore.baseParams = {
			planTime : planTime2.getValue(),
			planWeek : planWeek2.getValue(),
			queryKey : queryKey.getValue()
		}
		selectStore.load({
					start : 0,
					limit : 18
				})
	}

	function storeAdd(record) {

		var o = new MyRecord({
					'content' : '',
					'chargeDep' : '',
					'chargeDeptName' : '',
					'assortDep' : '',
					'assortDeptName' : '',
					'beginTime' : '',
					'endTime' : '',
					'days' : '',
					'memo' : ''
				});
		store.insert(store.getCount(), o);
		o.set('content', record.data.content), o.set('chargeDep',
				record.data.chargeDep), o.set('chargeDeptName',
				record.data.chargeDeptName), o.set('assortDep',
				record.data.assortDep), o.set('assortDeptName',
				record.data.assortDeptName), o.set('beginTime',
				record.data.beginTime), o.set('endTime', record.data.endTime), o
				.set('days', record.data.days), o.set('memo', record.data.memo)
	}

	function selectConfirm() {
		var array = new Array();
		if (selectSm.hasSelection()) {
			var records = selectGrid.selModel.getSelections();
			var record;
			var recordArray = [];
			var isExist;
			for (var i = 0; i < records.length; i++) {
				storeAdd(records[i])
			}
			grid.getView().refresh();
			for (var i = 0; i < records.length; i++) {
				record = records[i];
				// selectStore.remove(record);
				selectGrid.getView().refresh();
			}
			selectWin.hide();
		} else {
			Ext.Msg.alert('提示', '请先选择要增加的数据！')
		}
	}

	var selectTbar = new Ext.Toolbar({
				items : ["周计划时间:", planTime2, '-', "计划周:", planWeek2, '-', '设备名称及检修内容:',
						queryKey,'-',"周开始时间:", startTime2, "周结束时间", endTime2,'-', {
							text : "查询",
							iconCls : 'query',
							handler : selectQuery
						}, '-', {
							id : "cofirmBtu",
							text : "确定",
							iconCls : 'confirm',
							handler : selectConfirm
						}]
			});

	var selectGrid = new Ext.grid.EditorGridPanel({
				store : selectStore,
				layout : 'fit',
				tbar : selectTbar,
				columns : [selectSm, new Ext.grid.RowNumberer({
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
							dataIndex : 'content'
						}, {
							header : "负责单位",
							width : 100,
							sortable : true,
							dataIndex : 'chargeDep',
							renderer : function(value, metadata, record) {
								if (value != null)
									return record.get('chargeDeptName')
							}
						}, {
							header : "配合单位",
							width : 100,
							sortable : true,
							dataIndex : 'assortDep'
						}, {
							header : '计划开工日期',
							dataIndex : 'beginTime',
							width : 150,
							align : 'left'
						}, {
							header : '计划结束日期',
							dataIndex : 'endTime',
							width : 150,
							align : 'left'
						}, {
							header : '天数',
							dataIndex : 'days',
							align : 'center',
							width : 100
						}, {
							header : '备注',
							dataIndex : 'memo',
							align : 'center',
							width : 100
						}],

				sm : selectSm, // 选择框的选择 Shorthand for
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : selectStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				viewConfig : {
					forceFit : true
				}
			});

	// ---------------------从历史记录选择页面结束------------------------//

	// ---------------------汇总填写页面开始------------------------//
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
	var weekStartTime = new Ext.form.TextField({
				id : 'weekStartTime',
				style : 'cursor:pointer',
				readOnly : true,
				width : 100,
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

	var weekEndTime = new Ext.form.TextField({
				id : 'weekEndTime',
				style : 'cursor:pointer',
				readOnly : true,
				width : 100,
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
			}, {
				name : 'weekStartTime',
				mapping : 20
			}, {
				name : 'weekEndTime',
				mapping : 21
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

	store.on('load', function() {
				gatherBy.setValue(null);
				gatherDate.setValue(null);

			})
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var alertMsg = "";
		var modifyRec = grid.getStore().getModifiedRecords();
		var days;
		if (store.getCount() == modifyRec.length) {
			if (weekStartTime.getValue() == null
					|| weekStartTime.getValue() == "") {
				Ext.Msg.alert("提示", "请先选择周计划开始时间！");
				return;
			}
			if (weekEndTime.getValue() == null || weekEndTime.getValue() == "") {
				Ext.Msg.alert("提示", "请先选择周计划结束时间！");
				return;
			}
			if (weekEndTime.getValue() < weekStartTime.getValue()) {
				Ext.Msg.alert("提示", "周计划结束时间应大于周计划开始时间！");
				return;
			}
		}
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
									weekStartTime : weekStartTime.getValue(),
									weekEndTime : weekEndTime.getValue(),
									totalMemo : totalMemo.getValue(),
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

	// 上报
	function upcommit() {
		if (store.getCount() == 0) {
			Ext.Msg.alert("提示", "无上报数据");
			return false;
		}
		var url = "reportsign.jsp";
		var gatherId = store.getAt(0).get("gatherId");
		var workFlowNo = store.getAt(0).get("workFlowNo");
		var args = new Object();
		args.entryId = workFlowNo;
		args.gatherId = gatherId;
		args.workflowType = "bqMaintWeekPlanGather";
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			query();
		}
	}
	function query() {
		store.baseParams = {
			approve : 'N',
			planTime : planTime.getValue(),
			planWeek : planWeek.getValue()
		}
		store.load({
					start : 0,
					limit : 18
				})
	}
	store.on('load', function() {
				init()
			})
	function copyRecords() {
		copyWin.show();
		copyQuery()
	}

	function selectRecords() {
		selectWin.show();
		selectQuery()
	}

	var copyWin = new Ext.Window({
				id : 'copyWin',
				width : 800,
				height : 500,
				modal : true,
				items : [copyGrid],
				closable : true,
				closeAction : 'hide',
				layout : 'fit',
				autoHeight : false,
				buttonAlign : 'center',
				buttons : [{
							text : '关闭',
							iconCls : 'cancer',
							id : 'cancelBtu',
							handler : function() {
								copyWin.hide()
							}
						}]
			})

	var selectWin = new Ext.Window({
				id : 'selectWin',
				width : 870,
				height : 560,
				modal : true,
				items : [selectGrid],
				closable : true,
				closeAction : 'hide',
				layout : 'fit',
				autoHeight : false,
				buttonAlign : 'center',
				buttons : [{
							text : '关闭',
							iconCls : 'cancer',
							id : 'cancelBtu',
							handler : function() {
								selectWin.hide()
							}
						}]
			})

	var tbar = new Ext.Toolbar({
				items : ["周计划时间:", planTime, '-', "计划周:", planWeek, '-', {
							text : "查询",
							iconCls : 'query',
							handler : query
						}, '-', "周开始时间:",
						weekStartTime, "周结束时间", weekEndTime]
			});
    var gridTbar= new Ext.Toolbar({
    	        items:[{
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
							text : "上报",
							id : 'btnupcommit',
							iconCls : 'upcommit',
							handler : upcommit
						}, '-', {
							text : "从各部门已审批的计划中选择",
							id : 'btncopy',
							iconCls : 'add',
							handler : copyRecords
						}, '-', {
							text : "从历史计划中选择",
							id : 'btnselect',
							iconCls : 'add',
							handler : selectRecords
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
        
		tbar : gridTbar,
		sm : sm, // 选择框的选择 Shorthand for
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				}),
		clicksToEdit : 1,
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
				readOnly : false,
				value : memoText,
				width : 980,
				name : 'totalMemo'
			});

	var Form = new Ext.Toolbar({
				items : ["备注:", totalMemo]
			});

	grid.on('beforeedit', function(e) {
				// 使用新增按钮的可用状态判断
				if (Ext.get("btnAdd").dom.disabled)
					return false;
			});
	getWorkCode()
	/** 右边的grid * */

	function init() {
		Ext.Ajax.request({
					method : 'post',
					url : 'managemaintweek/getRepairGatherDetailList.action',
					params : {
						planTime : planTime.getValue(),
						planWeek : planWeek.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result.list.length > 0) {
							var str = Ext.encode(result.list);
							var ob = eval("("
									+ str.substring(1, str.length - 1) + ")")
									
							weekStartTime.setValue(ob[20]);
							weekEndTime.setValue(ob[21]);
							if (ob[18] == 0 || ob[18] == 3) {
								Ext.get("btnAdd").dom.disabled = false;
								Ext.get("btnupcommit").dom.disabled = false;
								Ext.get("btndelete").dom.disabled = false;
								Ext.get("btnsave").dom.disabled = false;
								Ext.get("btnCancer").dom.disabled = false;
								Ext.get("btncopy").dom.disabled = false;
								Ext.get("btnselect").dom.disabled = false;
								totalMemo.el.dom.readOnly = true; 
							} else {
								Ext.get("btnAdd").dom.disabled = true;
								Ext.get("btnupcommit").dom.disabled = true;
								Ext.get("btndelete").dom.disabled = true;
								Ext.get("btnsave").dom.disabled = true;
								Ext.get("btnCancer").dom.disabled = true;
								Ext.get("btncopy").dom.disabled = true;
								Ext.get("btnselect").dom.disabled = true;
								totalMemo.el.dom.readOnly = true; 
							}
						} else {
							Ext.get("btnAdd").dom.disabled = false;
							Ext.get("btnupcommit").dom.disabled = false;
							Ext.get("btndelete").dom.disabled = false;
							Ext.get("btnsave").dom.disabled = false;
							Ext.get("btnCancer").dom.disabled = false;
							Ext.get("btncopy").dom.disabled = false;
							Ext.get("btnselect").dom.disabled = false;
							totalMemo.el.dom.readOnly = false;  
							
							weekStartTime.setValue(null);
							weekEndTime.setValue(null);
						}
					}
				});
	}
	query()
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
		// ---------------------汇总填写结结束------------------------//
})