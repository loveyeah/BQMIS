Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var finishWorkFlowNo;
	// 工作流状态
	var signStatus;
	// 计划时间(部门工作计划汇总表主键)
	// var planTime;
	// 部门主表Id
	var depMainId;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		return s;
	}

	function removeStore() {
		con_ds.load({
					params : {
						planTime : planTime.getValue(),
						finish : 'finish',
						start : 0,
						limit : 18
					}
				})
		store.removeAll();
	}
	function btnSet(flg) {
		if (flg) {
			Ext.getCmp("btnsave").setDisabled(true);
//			Ext.getCmp("btncancer").setDisabled(true);
//			 Ext.getCmp("btnupcommit").setDisabled(true);
		} else {
			Ext.getCmp("btnsave").setDisabled(false);
//			Ext.getCmp("btncancer").setDisabled(false);
//			 Ext.getCmp("btnupcommit").setDisabled(false);
		}
	}

	function init() {
	//update by sychen 20100414
				btnSet(false);
				removeStore();
//		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobMain.action', {
//					success : function(action) {
//						var result =  action.responseText ;
						
//						if (result=="1") {
//							btnSet(false);
//								removeStore();
//								return;
//						}
//						else
//						{
//							btnSet(true);
//							con_ds.removeAll();
//							store.removeAll();
//								return;
//						}
//					}
//				}, 'planTime=' + planTime.getValue()+'&flag=2');
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
				name : 'baseInfo.finishSignStatus'
			}, {//add by sychen 20100409
				name : 'baseInfo.finishIfRead'
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
	con_ds.baseParams = {
		finish : 'finish1'
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '部门',
				dataIndex : 'editDepName',
				align : 'center'

			}, {
				header : '编辑人员',
				dataIndex : 'editByName',
				align : 'center'
			}, {
				header : '编辑时间',
				dataIndex : 'editDate',
				align : 'center'

			}, {//update by sychen 20100409
				header : '审阅状态',
				dataIndex : 'baseInfo.finishIfRead',
				align : 'center',
				renderer:function(value)
				{
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

	function queryLoad() {
		init();
	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : queryLoad

			})
	var revelation = new Ext.Button({
				text : '整体展示',
				id : 'revelation',
				hidden:false,
				iconCls : 'list',
				handler : function() {
					var url = "revelation.jsp";
					var month = planTime.getValue();
					var args = new Object();
					args.month = month;
					window.showModalDialog(url, args,
							'dialogWidth=600px;dialogHeight=500px;status=no');
				}
			})
	var contbar = new Ext.Toolbar({
				items : [planTime, '-', query, '-', revelation]

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

	// modified by liuyi 20100525 
//	Grid.on('click', modifyBtn);
	Grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			depMainId = recL.get("baseInfo.depMainId");
			// modified by liuyi 20100525 
			store.baseParams = {
				deptMainId : depMainId,
				isApprove:'gather'
			};
			store.load({
						params : {
//							deptMainId : depMainId,
							start : 0,
							limit : 18
							// add by liuyi 20100525 
//							,isApprove:'gather'
						}
					});
			// add by sychen 20100409
			if (recL.get("baseInfo.finishIfRead") == 'Y') {
				Ext.getCmp("btnread").setDisabled(true);
			} else {
				Ext.getCmp("btnread").setDisabled(false);
			}
		}

	}
	/** you边的grid * */

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
				name : 'completeDate',
				mapping : 5
			}
			// add by liuyi 20100525 
			,{
				name : 'orderBy',
				mapping : 14
			}
			]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getPlanJobCompleteDetialList.action'
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
	// 删除记录

	var ids = new Array();
	function deleteRecords() {
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
						finish : 'finish',
						planTime : planTime.getValue(),
						isUpdate : Ext.util.JSON.encode(updateData),
						isDelete : ids.join(",")
					},
					success : function(request, action) {
						var o = eval('(' + request.responseText + ')');
						// 保存后上报按钮可用
//						 Ext.getCmp("btnupcommit").setDisabled(false);
						Ext.Msg.alert('提示信息', "保存成功");
					},
					failure : function(request, action) {
						Ext.Msg.alert('提示信息', '未知错误！')
					}
				})

	}
	// 上报
	function upcommit() {
		if(depMainId==null||depMainId=="")
		{
			Ext.Msg.alert("提示","请选择一条左边列表记录！");
			return;
		}
		Ext.Ajax.request({
					url : 'manageplan/setDeptMainRead.action',
					params : {
						depMainId : depMainId
					},
					method : 'post',
					success : function(result, request) {
						var tt = eval('(' + result.responseText + ')');
						Ext.Msg.alert('提示', tt.msg);
						removeStore();
//						Ext.getCmp("btnupcommit").setDisabled(true);
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	}
	
	//已读 add sychen 20100409
     function readRecord(){
		var readflag;
		var recL = Grid.getSelectionModel().getSelected();
		if (recL == undefined) {
			Ext.Msg.alert("提示", "请选择左边一条记录！");
			return;
		}
		readflag = recL.get("baseInfo.finishIfRead");
		if (readflag == "N") {
			Ext.Msg.confirm("提示", "确认阅读此记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Msg.wait("操作进行中...", "请稍候");
							Ext.Ajax.request({
										method : 'post',
										url : 'manageplan/updateFinishIfRead.action',
										params : {
											depMainId : recL
													.get("baseInfo.depMainId")
										},
										success : function(action) {
											Ext.Msg.alert("提示", "操作成功！");
											con_ds.reload();
										},
										failure : function() {
											Ext.Msg.alert('提示信息', '未知错误！');
										}
									});
						}
					})
		} else  {
			Ext.Msg.alert("提示", "该记录已阅读或不存在！");
			return false;
		}
	
     	
     }
	
	// // 上报
	// function upcommit() {
	// var url = "reportsign.jsp";
	// var args = new Object();
	// args.planTime = planTime.getValue();
	// args.entryId = finishWorkFlowNo;
	// args.workflowType = "bqDeptJobFinishGather";
	// var obj = window.showModalDialog(url, args,
	// 'status:no;dialogWidth=750px;dialogHeight=550px');
	// if (obj) {
	// // 上报后增删存取报按钮设为不可用，签字可用
	// Ext.getCmp("btnsave").setDisabled(true);
	// Ext.getCmp("btncancer").setDisabled(true);
	// Ext.getCmp("btnupcommit").setDisabled(true);
	// }
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
				columns : [{
							header : '序号',
							dataIndex : 'orderBy',
							align : 'center'

						},{
							header : '工作内容',
							dataIndex : 'jobContent',
							align : 'center'

						}, {
							header : '完成时间',
							dataIndex : 'completeDate',
							align : 'center',
							renderer : function(v) {
								if (v == '0') {
									return '当月';
								} else if (v == '1') {
									return '跨越';
								} else if (v == '2') {
									return '长期';
								} else
									return '';
							}

						}, {
							header : '完成情况',
							dataIndex : 'ifComplete',
							align : 'center',
							editor : new Ext.form.ComboBox({
										store : new Ext.data.SimpleStore({
													data : [['0', '未完成'],
															['1', '进行中'],
															['2', '已完成']],
													fields : ['value', 'text']
												}),
										displayField : 'text',
										valueField : 'value',
										mode : 'local',
										triggerAction : 'all'
									}),
							renderer : function(v) {
								if (v == '0')
									return '未完成';
								else if (v == '1')
									return '进行中';
								else if (v == '2')
									return '已完成';
								else
									return '';

							}
						}, {
							header : '考核说明',
							dataIndex : 'completeDesc',
							align : 'center',
							editor : new Ext.form.TextField()

						}],
				tbar : [{
							text : "保存",
							disabled : true,
							id : 'btnsave',
							iconCls : 'save',
							handler : saveModifies
						},
//						{
//							text : "上报",
//							disabled : true,
//							id : 'btnupcommit',
//							iconCls : 'upcommit',
//							handler : upcommit
//						}, 
							{// 已读 add sychen 20100409
							text : "已读",
							disabled : true,
							id : 'btnread',
							iconCls : 'write',
							handler : readRecord
						}
				// , {
				// text : "导出",
				// id : 'btnupcommit',
				// iconCls : 'upcommit',
				// handler : upcommit
				// }
				],
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
				clicksToEdit : 1

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
							// autoScroll: true,
							width : '40%',
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
	queryLoad();
	btnSet(true);
})
