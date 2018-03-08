Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var method = "add";
	var Ids = new Array();
	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	// 类别
	var typeId;

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
	//	
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
	// 从session取登录人编码姓名部门相关信息
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
							var DeptCode = arr[0];
							var DeptName = arr[1]

							depMainId.setValue(0);
							trainingDep.setValue(DeptName);
							editBy.setValue(workerCode);
							editDepcode.setValue(DeptCode);

							store.rejectChanges();

						}
					}
				});
	}

	function getCurrentMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
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

	// 填写人
	var fillBy = new Ext.form.TextField({
				readOnly : true,
				name : 'fillBy'
			});

	// 填写时间

	var fillDate = new Ext.form.TextField({
				readOnly : true,
				name : 'fillDate'
			});

	// 计划部门
	var trainingDep = new Ext.form.TextField({
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
	// 计划部门id
	var depMainId = new Ext.form.Hidden({
				name : 'depMainId'
			});

	var trainingDetailId = new Ext.form.Hidden({
				id : 'trainDetail.trainingDetailId',
				name : 'trainDetail.trainingDetailId'
			});

	var trainingMainId = new Ext.form.Hidden({
				id : 'trainDetail.trainingMainId',
				name : 'trainDetail.trainingMainId'
			});

	var trainingTypeId = new Ext.form.Hidden({
				id : 'trainDetail.trainingTypeId',
				name : 'trainDetail.trainingTypeId'
			});

	// 培训计划类别数据源
	var planType = Ext.data.Record.create([{
				name : 'trainingTypeName'
			}, {
				name : 'trainingTypeId'
			}]);

	var allPlanTypeStore = new Ext.data.JsonStore({
				url : 'manageplan/getAllTypeList.action',
				root : 'list',
				fields : planType
			});
	var typeSm = new Ext.grid.CheckboxSelectionModel();
	allPlanTypeStore.load(
	// modified by liuyi 091214 不要分页栏
//		{
//				params : {
//					start : 0,
//					limit : 18
//				}
//			}
			);
	// grid列表数据源
	var Storelist = new Ext.data.Record.create([sm, {
				name : 'trainDetail.trainingDetailId'
			}, {
				name : 'trainDetail.trainingMainId'
			}, {
				name : 'trainDetail.trainingTypeId'
			}, {
				name : 'trainDetail.trainingName'
			}, {
				name : 'trainDetail.trainingLevel'
			}, {
				name : 'trainDetail.trainingNumber'
			}, {
				name : 'trainDetail.trainingHours'
			}, {
				name : 'trainDetail.chargeBy'
			}, {
				name : 'trainingDep'
			}, {
				name : 'fillBy'
			}, {
				name : 'trainingTypeName'
			}, {
				name : 'fillDate'
			}, {
				name : 'chargeName'
			}, {
				name : 'billName'
			}]);
	var store = new Ext.data.JsonStore({
				url : 'manageplan/getTrainPlanApplyList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Storelist
			});
	store.load(
		{
				params : {
					planTime : planTime.getValue()
					// add by liuyi 20100427 增加人员过滤，一管理部门下可以有多条主数据
						,isApply : 1
					// modified by liuyi 091214 不要分页栏
//					,
//					start : 0,
//					limit : 18
				}
			})

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 增加
	function addRecord() {
		if (depMainId.getValue() == null) {
			Ext.Msg.alert("提示", "请先选择计划时间！");
			return;
		}
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示', "请选择项目计划类别！");
			return;
		}

		typeId = rec[0].get("trainingTypeId");

		var count = store.getCount();
		var currentIndex = count;
		var o = new Storelist({
					'trainDetail.trainingLevel' : '2',
					'trainDetail.trainingName' : '',
					'trainDetail.trainingNumber' : '',
					'trainDetail.trainingHours' : '',
					'trainDetail.chargeBy' : '',
					'trainDetail.fillBy' : '',
					'trainDetail.fillDate' : ''

				});
		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 1);

	}

	// add by liuyi 091214 判断输入的页面数据不会有两个合计
	function checkUnicityOfFirst() {
		for (var i = 0; i < store.getCount(); i++) {
			for (var j = i + 1; j < store.getCount(); j++) {
				if (store.getAt(i).get('trainDetail.trainingLevel') == 1 
				&& store.getAt(i).get('trainDetail.trainingLevel') == store
						.getAt(j).get('trainDetail.trainingLevel')
				&& store.getAt(i).get('trainDetail.trainingTypeId') == store
						.getAt(j).get('trainDetail.trainingTypeId')) {
							var str = '不能有多个合计在同一类别下！'
					if (store.getAt(i).get('trainDetail.trainingTypeId') != null
							&& store.getAt(i).get('trainDetail.trainingTypeId') != '') {
						allPlanTypeStore.each(function(re) {
									if (re.get('trainingTypeId') == store
											.getAt(i)
											.get('trainDetail.trainingTypeId')) {
										str = '有多个合计在'
																+ re
																		.get('trainingTypeName')
																+ '类别下！'									
									}
								})
					}
					Ext.Msg.alert('提示', str);
					return false;
					
				}
			}
		}
		return true;
	}
	// add by liuyi 091214 数据查询后，填写人，填写时间初始化
	store.on('load',function(){
		fillBy.setValue(null);
		fillDate.setValue(null);
		
	})
	// 保存
	function saveModifies() {
		grid.stopEditing();
		// add by liuyi 091214 判断输入的页面数据不会有两个合计
//		if(!checkUnicityOfFirst())  //modify by fyyang 20100630 合计已去掉
//			return;
		var alertMsg = "";
		var modifyRec = grid.getStore().getModifiedRecords();
		var planDate = planTime.getValue();
		var planDeptCode = editDepcode.getValue();
		if (modifyRec.length > 0 || Ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						
//						if (modifyRec[i].get("trainDetail.trainingLevel") == 1) {
//							if (modifyRec[i].get("trainDetail.trainingNumber") == null
//									|| (modifyRec[i]
//											.get("trainDetail.trainingNumber") == ""
//											&& modifyRec[i]
//											.get("trainDetail.trainingNumber") != "0")) {
//								alertMsg += "计划人数不能为空</br>";
//								alert(modifyRec[i].get("trainDetail.chargeBy"));
//								if (modifyRec[i].get("trainDetail.chargeBy") == null
//										|| modifyRec[i]
//												.get("trainDetail.chargeBy") == "") {
//									alertMsg += "负责人不能为空";
//								}
//								if (alertMsg != "") {
//									Ext.Msg.alert("提示", alertMsg);
//									return;
//								}
//							}
//						}
//						else {
							
							if (modifyRec[i].get("trainDetail.trainingLevel") == null
									|| modifyRec[i]
											.get("trainDetail.trainingLevel") == "") {
								alertMsg += "项目级别不能为空</br>";
							}
							if (modifyRec[i].get("trainDetail.trainingName") == null
									|| modifyRec[i]
											.get("trainDetail.trainingName") == "") {
								alertMsg += "计划培训项目不能为空</br>";
							}
							if (modifyRec[i].get("trainDetail.trainingNumber") == null
									|| (modifyRec[i]
											.get("trainDetail.trainingNumber") == ""
											&&modifyRec[i]
											.get("trainDetail.trainingNumber") != "0")) {
								alertMsg += "计划人数不能为空</br>";
							}
							/*if (modifyRec[i].get("trainDetail.trainingHours") == null
									|| modifyRec[i]
											.get("trainDetail.trainingHours") == "") {
								alertMsg += "培训课时不能为空</br>";
							}*/
							
							if (modifyRec[i].get("trainDetail.chargeBy") == null
									|| modifyRec[i].get("trainDetail.chargeBy") == "") {
								alertMsg += "负责人不能为空</br>";
							}
							var leftSelect=westgrid.getSelectionModel().getSelections();
							var typeId=leftSelect[0].get("trainingTypeId");
							  if (typeId == 2
								|| typeId == 7
								|| typeId == 9|| typeId == 10) {
                                
							if (modifyRec[i].get("trainDetail.trainingHours") == null
									|| (modifyRec[i]
											.get("trainDetail.trainingHours") == "" && modifyRec[i]
											.get("trainDetail.trainingHours") != "0")) {
								alertMsg += "培训课时不能为空";
							}
						}
							  
							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
//						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'manageplan/addTrainPlanApply.action',
								method : 'post',
								params : {
									// isAdd : Ext.util.JSON.encode(newData),
									isUpdate : Ext.util.JSON.encode(updateData),
									typeId : typeId,
									isDelete : Ids.join(","),
									planDate : planDate,
									planDeptCode : planDeptCode,
									'trainDetail.fillBy' : editBy.getValue(),
									'trainDetail.fillDate' : fillDate
											.getValue()
								},
								success : function(form, options) {

									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
										if (obj.msg.indexOf('已经存在') != -1)
											return;
									}
									store.rejectChanges();
									Ids = [];
									store.reload();
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

	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			Ext.Msg.confirm('提示', '确认删除此培训计划项目?', function(response) {
				if (response == 'yes') {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("trainDetail.trainingDetailId") != null) {
							Ids
									.push(member
											.get("trainDetail.trainingDetailId"));
						}
						grid.getStore().remove(member);
						grid.getStore().getModifiedRecords().remove(member);
					}
				}
			});
		}

	}

	// 上报
	function upcommit() {
		var url = "reportsign.jsp";
		var mainId = store.getAt(0).get("trainDetail.trainingMainId");
		var args = new Object();

		args.entryId = workFlowNo;
		args.mainId = mainId;
		args.workflowType = "bqTrainPlan";
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		store.load({
					params : {
						depMainId : depMainId.getValue(),
						planTime : planTime.getValue(),
						start : 0,
						limit : 18
					}
				});
		// // 上报后增删存取报按钮设为不可用，签字可用
		init();
	}

	function query() {
		store.rejectChanges();
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec[0]) {
			typeId = rec[0].get("trainingTypeId");
			// alert()
		}
		if (planTime.getValue() == null || planTime.getValue() == "") {
			Ext.Msg.alert('提示', '请选择计划时间！')
		} else {
			store.load({
						params : {
							planTime : planTime.getValue(),
							planType : rec[0]
									? rec[0].get("trainingTypeId")
									: ""
							// add by liuyi 20100427 增加人员过滤，一管理部门下可以有多条主数据
						,isApply : 1
							// modified by liuyi 091214 不进行分页
//									,
//							start : 0,
//							limit : 18
						}
					})
		}

		init();
	}
	var tbar = new Ext.Toolbar({
				items : ["计划时间:", planTime, '-', {
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : query
						}, '-', {
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
							text : "删除",
							id : 'btndelete',
							iconCls : 'delete',
							handler : deleteRecords
						}, '-', {
							text : "上报",
							id : 'btnupcommit',
							iconCls : 'upcommit',
							handler : upcommit
						}

				]
			});

	var bbar = new Ext.Toolbar({
				items : ["计划部门:", trainingDep, '-', "填写人:", fillBy, '-',
						"填写时间:", fillDate]
			});

	var westgrid = new Ext.grid.EditorGridPanel({
				store : allPlanTypeStore,
				layout : 'fit',
				columns : [sm, {
							header : "项目计划类别",
							width : 100,
							sortable : false,
							dataIndex : 'trainingTypeName'
						}],

				sm : typeSm, // 选择框的选择 Shorthand for
				// 分页
				viewConfig : {
					forceFit : true
				}
			});
	westgrid.on("rowclick", function() {
				var rec = westgrid.getSelectionModel().getSelections();
				if (planTime.getValue() == null || planTime.getValue() == "") {
					Ext.Msg.alert('提示', '请选择计划时间！')
				} else {
					store.reload({
								params : {
									planTime : planTime.getValue(),
									planType : rec[0] ? rec[0]
											.get("trainingTypeId") : ''
							// add by liuyi 20100427 增加人员过滤，一管理部门下可以有多条主数据
						,isApply : 1
									// modified by liuyi 091214 不进行分页
//									,
//							start : 0,
//							limit : 18
								}
							})
				}
				init();
			})

	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [sm,
				new Ext.grid.RowNumberer({
				header : '行号',
				width : 35
				}),// 选择框
						{
							width : 20,
							header : "项目级别",
							sortable : false,
							hidden:true,
							dataIndex : 'trainDetail.trainingLevel',
							editor : new Ext.form.ComboBox({
										store : new Ext.data.SimpleStore({
													fields : ["retrunValue",
															"displayText"],
													data : [['1', '合计'],
															['2', '明细']]
												}),
										displayField : "displayText",
										valueField : "retrunValue",
										hiddenName : 'trainDetail.trainingLeve',
										id : 'trainingLevel',
										allowBlank : false,
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										value:'2'
									}),
							renderer : function(v) {
								if (v == 1) {
									return "合计";
								}
								if (v == 2) {
									return "明细";
								}
							}
						}, {
							header : '计划培训项目',
							dataIndex : 'trainDetail.trainingName',
							align : 'left',
							width : 40,
							editor : new Ext.form.TextField({
										allowBlank : false,
										id : 'trainingName'
									})
						}, {
							header : '计划人数',
							dataIndex : 'trainDetail.trainingNumber',
							align : 'center',
							width : 10,
							editor : new Ext.form.NumberField({
										allowBlank : false,
										id : 'trainingNumber'
										// add by liuyi 091214 输入限制
										,allowDecimals : false,
										allowNegative : false
									})
						}, {
							header : '培训课时',
							dataIndex : 'trainDetail.trainingHours',
							align : 'center',
							width : 10,
							editor : new Ext.form.NumberField({
										allowBlank : false,
										id : 'trainingHours'
										// add by liuyi 091214 输入限制
										,allowNegative : false
									})
						}, {
							header : '负责人',
							dataIndex : 'trainDetail.chargeBy',
							align : 'center',
							width : 20,
							editor : new Ext.form.TextField({
										allowBlank : false,
										id : 'chargeBy'
									})
						}],

				sm : sm, // 选择框的选择 Shorthand for
				// 分页
				clicksToEdit : 1,
				viewConfig : {
					forceFit : true
				}
			});
	grid.on("rowclick", function() {
		// modified by liuyi 091214 增加判断明细是否被选中
				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					fillBy.setValue(rec.get("billName"));
					fillDate.setValue(rec.get("fillDate"));

				}else{
					fillBy.setValue(null);
					fillDate.setValue(null);
				}
			})

			// add by liuyi 091214 对已上报的数据，列表中不允许修改
	grid.on('beforeedit',function(e){
		// 使用新增按钮的可用状态判断
		if(Ext.get("btnAdd").dom.disabled)
			return false;
	});
	getWorkCode()
	/** 右边的grid * */

	function init() {
		Ext.Ajax.request({
					method : 'post',
					url : 'manageplan/getTrainPlanApplyList.action',
					params : {
						planTime : planTime.getValue()
						// planType : typeId
						// add by liuyi 20100427 增加人员过滤，一管理部门下可以有多条主数据
						,isApply : 1
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// alert(action.responseText)
						if (result.list.length > 0) {
							var str = Ext.encode(result.list);
							var ob = eval("("
									+ str.substring(1, str.length - 1) + ")")
							if (ob.workflowStatus == 0
									|| ob.workflowStatus == 3) {
								Ext.get("btnAdd").dom.disabled = false;
								Ext.get("btnupcommit").dom.disabled = false;
								Ext.get("btndelete").dom.disabled = false;
								Ext.get("btnsave").dom.disabled = false;
							} else {
								Ext.get("btnAdd").dom.disabled = true;
								Ext.get("btnupcommit").dom.disabled = true;
								Ext.get("btndelete").dom.disabled = true;
								Ext.get("btnsave").dom.disabled = true;
							}
						} else {
							Ext.get("btnAdd").dom.disabled = false;
							Ext.get("btnupcommit").dom.disabled = false;
							Ext.get("btndelete").dom.disabled = false;
							Ext.get("btnsave").dom.disabled = false;
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
							region : 'north',
							items : [tbar],
							height : 25,
							style : 'padding-bottom:0.8px'
						}, {
							bodyStyle : "padding: 1,1,1,1",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '25%',
							items : [westgrid]
						}, {
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [grid]
						}, {
							region : 'south',
							items : [bbar],
							height : 25,
							style : 'padding-bottom:0.8px'
						}]
			});
})
