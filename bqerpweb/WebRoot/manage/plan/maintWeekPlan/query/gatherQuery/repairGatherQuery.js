Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	
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
	var weekStartTime = new Ext.form.TextField({
				id : 'weekStartTime',
				style : 'cursor:pointer',
				readOnly : true,
				width : 80,
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
				width : 80,
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
	function approveQuery() {
		var url = '';
		if (store.getTotalCount() > 0) {
			var rec = store.getAt(0);
			if (rec.get('workFlowNo') == null || rec.get('workFlowNo') == '') {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ "bqMaintWeekPlanGather";
				window.open(url);
			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ rec.get('workFlowNo');
				window.open(url);
			}
		} else {
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ "bqMaintWeekPlanGather";
			window.open(url);
		}
	}
	
	
	// 会签票面浏览
	function CheckRptPreview() {
		var selected = grid.getSelectionModel().getSelections();
		var gatherId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			gatherId = menber.get('gatherId');
			var url = "/powerrpt/report/webfile/bqmis/repairGather.jsp?gatherId="
					+ gatherId;
			window.open(url);

		}
	};
	
	var tbar = new Ext.Toolbar({
				items : ["周计划时间:", planTime, '-',"计划周:", planWeek, '-', "周开始时间:", weekStartTime, "周结束时间",
						weekEndTime, '-', {
							text : "查询",
							iconCls : 'query',
							handler : query
						}, '-', {
							id : 'btnapprove',
							text : "审批信息",
							iconCls : 'approve',
							handler : approveQuery
						}, '-', {
							id : 'btnpdfview',
							text : "会签表",
							iconCls : 'pdfview',
							handler :CheckRptPreview
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
						
						} 
						else{
						    weekStartTime.setValue(null);
							weekEndTime.setValue(null);
						}
					}
				});
	}
			
	getWorkCode()
	query();
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