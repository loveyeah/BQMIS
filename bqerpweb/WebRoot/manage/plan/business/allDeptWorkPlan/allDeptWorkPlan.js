Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	// 计划时间(部门工作计划汇总表主键)
	// var planTime;

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		return s;
	}

	function removeStore() {
		con_ds.baseParams = {
				planTime : planTime.getValue(),
				finish:'finish'
			};
			con_ds.load({
						params : {
							start : 0,
							limit : 18
						}
					});
		
	
		store.removeAll();
	}
	function btnSet(flg) {
		if (flg) {
			Ext.getCmp("btnadd").setDisabled(true);
			Ext.getCmp("btndelete").setDisabled(true);
			Ext.getCmp("btnsave").setDisabled(true);
			Ext.getCmp("btncancer").setDisabled(true);
//		    Ext.getCmp("btnupcommit").setDisabled(true);
			Ext.getCmp("btnread").setDisabled(true);
		} else {
			Ext.getCmp("btnadd").setDisabled(false);
			Ext.getCmp("btndelete").setDisabled(false);
			Ext.getCmp("btnsave").setDisabled(false);
			Ext.getCmp("btncancer").setDisabled(false);
//			Ext.getCmp("btnupcommit").setDisabled(false);
			Ext.getCmp("btnread").setDisabled(false);
		}
	}
	function init() {
		//update by sychen 20100414
//		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobMain.action', {
//					success : function(action) {
//						var result =  action.responseText ;
//								if(result=="1")
//							{
//							       btnSet(false);
//							    	removeStore();
//											return;
//							}
//								if(result=="2")
//							{
//							       btnSet(true);
//							       con_ds.removeAll();
//							       store.removeAll();
//								   return;
//							}
						
						btnSet(false);
						removeStore();

//					}
//				}, 'planTime=' + planTime.getValue()+'&flag=1');
	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'editDepName'
			}, {
				name : 'editByName'
			}, {
				name : 'editDate'
			}, {
				name : 'baseInfo.depMainId'
			}, {
				name : 'baseInfo.workFlowNo'
			}, {
				name : 'baseInfo.signStatus'
			}, {//add by sychen 20100408
				name : 'baseInfo.ifRead'
			}, {//add by sychen 20100408
				name : 'level1DeptName'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getBpJPlanJobDepMainList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,

	{
				header : '部门',
				dataIndex : 'level1DeptName',
				align : 'center'

			}, {
				header : '编辑人员',
				dataIndex : 'editByName',
				align : 'center'
			}, {
				header : '编辑时间',
				dataIndex : 'editDate',
				align : 'center'
			}, {
				header : '审阅状态',
				dataIndex : 'baseInfo.ifRead',
				align : 'center',
				renderer:function(value)
				{//update by sychen 20100408
					if(value=="Y")
					{
						return "已读";
					}
					else
					{
						return "未读";
					}
				}
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
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
	planTime.setValue(getDate());

	function wquery() {
		init();
	}

	function showAllInfo() {
		window.showModalDialog('allDeptWorkPlanShow.jsp?planTime='
						+ planTime.getValue(), null,
				'status:no;dialogWidth=530px;dialogHeight=700px');
		// query();

	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : wquery

			});

	var showAll = new Ext.Button({
				id : 'btnShowAll',
				text : '整体展示',
				iconCls : 'list',
				hidden:false,
				handler : showAllInfo
			});

	var contbar = new Ext.Toolbar({
				items : [planTime, query, showAll]

			});
	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true
			});

	Grid.on('click', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			store.baseParams = {
				depMainId : recL.get("baseInfo.depMainId")
			};
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					});
			// add by sychen 20100408
			Ext.getCmp("btnadd").setDisabled(false);
			Ext.getCmp("btndelete").setDisabled(false);
			Ext.getCmp("btnsave").setDisabled(false);
			Ext.getCmp("btncancer").setDisabled(false);
//			Ext.getCmp("btnupcommit").setDisabled(false);
			
			//add by sychen 20100408
		 if (recL.get("baseInfo.ifRead") == 'Y') {
			Ext.getCmp("btnread").setDisabled(true);
		} else {
			Ext.getCmp("btnread").setDisabled(false);
		}
		}
		

	}
	/** 右边的grid * */

	var MyRecord = Ext.data.Record.create([{
				name : 'jobId',
				mapping :0
			}, {
				name : 'depMainId',
				mapping :1
			}, {
				name : 'jobContent',
				mapping :2
			}, {
				name : 'completeData',
				mapping :3
			}, {
				name : 'ifComplete',
				mapping :4
			}, {
				name : 'completeDesc',
				mapping :5
			}//add by sychen 20100506
			, {
				name : 'orderBy',
				mapping :6
			}
			]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getBpJPlanJobDepDetail.action '
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
		var recL = Grid.getSelectionModel().getSelected();
		if (recL == undefined) {
			Ext.Msg.alert("提示", "请选择左边一条记录！");
			return;
		}
		var count = store.getCount();
		var currentIndex = count;
		var o = new MyRecord({
					'depMainId' : recL.get("baseInfo.depMainId"),
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

		if (!confirm("确定要保存修改吗?"))
			return;

		var updateData = new Array();
		for (var i = 0; i < modifyRec.length; i++) {

			updateData.push(modifyRec[i].data);

		}
		Ext.Ajax.request({
					url : 'manageplan/saveBpJPlanJob.action',
					method : 'post',
					params : {
						planTime : planTime.getValue(),
						isUpdate : Ext.util.JSON.encode(updateData),
						isDelete : ids.join(";")
					},
					success : function(request, action) {
						var o = eval('(' + request.responseText + ')');
						workFlowNo = o.workFlowNo;
						signStatus = o.signStatus;
						// 保存后上报按钮可用
//						 Ext.getCmp("btnupcommit").setDisabled(false);
						Ext.Msg.alert('提示信息', "保存成功");
						store.rejectChanges();
						ids = [];
						var recL = Grid.getSelectionModel().getSelected();
						store.load({

									params : {
										depMainId : recL
												.get("baseInfo.depMainId"),
										start : 0,
										limit : 18
									}
								});
					},
					failure : function(request, action) {
						Ext.Msg.alert('提示信息', '未知错误！')
					}
				})

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

	// 上报add by drdu 20100106
	function upcommitRecord() {
		var readflag;
		var recL = Grid.getSelectionModel().getSelected();
		if (recL == undefined) {
			Ext.Msg.alert("提示", "请选择左边一条记录！");
			return;
		}

		readflag = recL.get("baseInfo.signStatus");
		if (readflag == "11") {
			Ext.Msg.confirm("提示", "生产月度计划确认已完善？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Msg.wait("操作进行中...", "请稍候");
							Ext.Ajax.request({
										method : 'post',
										url : 'manageplan/updateMainreportTo.action',
										params : {
											depMainId : recL
													.get("baseInfo.depMainId")
										},
										success : function(action) {
											Ext.Msg.alert("提示", "操作成功！");
											con_ds.reload();
											store.removeAll();
											//update by sychen 20100408
											Ext.getCmp("btnadd").setDisabled(true);
									        Ext.getCmp("btndelete").setDisabled(true);
									        Ext.getCmp("btnsave").setDisabled(true);
									        Ext.getCmp("btncancer").setDisabled(true);
//								          	Ext.getCmp("btnupcommit").setDisabled(true);
								        	Ext.getCmp("btnread").setDisabled(true);
										},
										failure : function() {
											Ext.Msg.alert('提示信息', '未知错误！');
										}
									});
						}
					})
		} else /*if (readflag == "13")*/ {
			Ext.Msg.alert("提示", "该记录已阅读或不存在！");
			return false;
		}
	}
	
	//已读 add sychen 20100408
     function readRecord(){
		var readflag;
		var recL = Grid.getSelectionModel().getSelected();
		if (recL == undefined) {
			Ext.Msg.alert("提示", "请选择左边一条记录！");
			return;
		}
		readflag = recL.get("baseInfo.ifRead");
		if (readflag == "N") {
			Ext.Msg.confirm("提示", "确认阅读此记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Msg.wait("操作进行中...", "请稍候");
							Ext.Ajax.request({
										method : 'post',
										url : 'manageplan/updateIfRead.action',
										params : {
											depMainId : recL
													.get("baseInfo.depMainId")
										},
										success : function(action) {
											Ext.Msg.alert("提示", "操作成功！");
											con_ds.reload();
//											store.removeAll();
										},
										failure : function() {
											Ext.Msg.alert('提示信息', '未知错误！');
										}
									});
						}
					})
		} else /*if (readflag == "N")*/ {
			Ext.Msg.alert("提示", "该记录已阅读或不存在！");
			return false;
		}
	
     	
     }
	
	
	// 上报
	function upcommit() {
		var url = "../SignWorkPlan/commitDeptWorkPlan.jsp";
		var args = new Object();
		args.prjNo = planTime.getValue();
		args.entryId = workFlowNo;
		args.workflowType = "bqDeptJobplangather";
		args.prjTypeId = "";
		args.prjStatus = signStatus;

		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');

		if (obj) {
			workFlowNo = obj.workFlowNo;
			signStatus = obj.signStatus;
			// 上报后增删存取报按钮设为不可用，签字可用
			Ext.getCmp("btnadd").setDisabled(true);
			Ext.getCmp("btndelete").setDisabled(true);
			Ext.getCmp("btnsave").setDisabled(true);
			Ext.getCmp("btncancer").setDisabled(true);
//			 Ext.getCmp("btnupcommit").setDisabled(true);
			Ext.getCmp("btnread").setDisabled(true);
			// Ext.getCmp("btnapprove").setDisabled(false);
		}

	}

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
					{
					width : 300,
					header : "depMainId",
					sortable : false,
					hidden : true,
					dataIndex : 'depMainId'
				},// add by sychen 20100505
				  {
				    width : 60,
					header : "序号",
					align:'center',
					sortable : false,
					dataIndex : 'orderBy',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})
				}, {
					width : 300,
					header : "工作内容",

					sortable : false,
					dataIndex : 'jobContent',
					editor : new Ext.form.TextArea({
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
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
		tbar : [{
					text : "新增",
					id : 'btnadd',
					iconCls : 'add',
					disabled : true,
					handler : addRecord
				}, {
					text : "删除",
					disabled : true,
					id : 'btndelete',
					iconCls : 'delete',
					handler : deleteRecords

				}, {
					text : "保存",
					disabled : true,
					id : 'btnsave',
					iconCls : 'save',
					handler : saveModifies
				}, {
					text : "取消",
					disabled : true,
					id : 'btncancer',
					iconCls : 'cancer',
					handler : cancel
				},
//				{
//					text : "上报",
//					disabled : true,
//					id : 'btnupcommit',
//					iconCls : 'upcommit',
//					handler : upcommitRecord
//				}, 
					{//已读 add sychen 20100408
					text : "已读",
					disabled : true,
					id : 'btnread',
					iconCls : 'write',
					handler : readRecord 
				}/*
					 * , { text : "上报", id : 'btnupcommit', iconCls :
					 * 'upcommit',
					 * 
					 * handler : upcommit }
					 */],
		sm : sm, // 选择框的选择 Shorthand for
		// selModel（selectModel）
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第{0}条到{1}条，共{2}条",
					emptyMsg : "没有记录",
					beforePageText : '',
					afterPageText : ""
				}),
		clicksToEdit : 2

	});
	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{

							layout : 'fit',
							border : false,
							frame : false,
							width : '50%',
							region : "west",
							items : [Grid]
						}, {

							region : "center",
							border : false,
							frame : false,
							layout : 'fit',// width : 400,
							items : [grid]
						}]
			});

	// 页面载入未选时间时所有按钮不可用
	btnSet(true);
	wquery()
})
