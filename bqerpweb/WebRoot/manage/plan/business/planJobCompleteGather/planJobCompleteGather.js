Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var finishWorkFlowNo;
	// 工作流状态
	var signStatus;
	// 部门主表Id
	var deptMainId;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() ;
		s += (t > 9 ? "" : "0") + t + "";
		return s;
	}

	function removeStore() {
		con_ds.baseParams = {
				planTime : planTime.getValue(),
				flag : 'finish'
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
			Ext.getCmp("btnsave").setDisabled(true);
		} else {
			Ext.getCmp("btnsave").setDisabled(false);
		}
	}

	function init() {
				btnSet(false);
				removeStore();
	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'deptMainId',
				mapping : 0
			}, {
				name : 'planTime',
				mapping : 1
			}, {
				name : 'editDepcode',
				mapping : 2
			}, {
				name : 'deptName',
				mapping : 3
			}, {
				name : 'finishEditBy',
				mapping : 4
			}, {
				name : 'editName',
				mapping : 5
			}, {
				name : 'finishEditDate',
				mapping : 6
			}, {
				name : 'finishWorkFlowNo',
				mapping : 7
			}, {
				name : 'finishSignStatus',
				mapping : 8
			}, {
				name : 'finishIfRead',
				mapping : 9
			}, {
				name : 'levelDeptName',
				mapping : 10
			},{
				name : 'orderBy',
				mapping : 11
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanJobCompleteMainList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '部门',
				dataIndex : 'levelDeptName',
				align : 'center'

			}, {
				header : '编辑人员',
				dataIndex : 'editName',
				align : 'center'
			}, {
				header : '编辑时间',
				dataIndex : 'finishEditDate',
				align : 'center'

			}, {
				header : '审阅状态',
				dataIndex : 'finishIfRead',
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

	
	var contbar = new Ext.Toolbar({
		items : ["计划时间:", planTime, '-', {
					text : '查询',
					iconCls : 'query',
					handler : queryLoad

				}, '-', {
					text : '整体展示',
					id : 'revelation',
					hidden : false,
					iconCls : 'list',
					handler : function() {
						var url = "completeRevelation.jsp";
						var month = planTime.getValue();
						var args = new Object();
						args.month = month;
						window
								.showModalDialog(url, args,
										'dialogWidth=600px;dialogHeight=500px;status=no');
					}
				}]

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

	Grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			deptMainId = recL.get("deptMainId");
			store.baseParams = {
				deptMainId : deptMainId
			};
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					});
			if(recL.get("deptMainId")!=null||recL.get("deptMainId")!=""){
			     	Ext.getCmp("btnadd").setDisabled(false);
			     	Ext.getCmp("btndelete").setDisabled(false);
			     	Ext.getCmp("btnsave").setDisabled(false);
			     	Ext.getCmp("btncancer").setDisabled(false);
			}
			else{
			     	Ext.getCmp("btnadd").setDisabled(true);
			     	Ext.getCmp("btndelete").setDisabled(true);
			     	Ext.getCmp("btnsave").setDisabled(true);
			     	Ext.getCmp("btncancer").setDisabled(true);
			}
			if (recL.get("finishIfRead") == 'Y') {
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
				name : 'deptMainId',
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
			},{
				name : 'chargeBy',
				mapping : 6
			},{
				name : 'orderBy',
				mapping : 7
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getPlanJobCompleteDetailList.action'
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
					'deptMainId' : recL.get("deptMainId"),
					'jobContent' : '',
					'completeData' : '0',
					'ifComplete' : '0',
					'completeDesc':'',
					'orderBy':'' 
				});
		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 2);

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

				if (member.get("jobId") != null) {

					ids.push(member.get("jobId"));

				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	// 保存
	function saveModifies() {
		grid.stopEditing();
		var recL = Grid.getSelectionModel().getSelected();
		var modifyRec = store.getModifiedRecords();
		if (!confirm("确定要保存修改吗?"))
			return;
		var updateData = new Array();
		for (var i = 0; i < modifyRec.length; i++) {
			updateData.push(modifyRec[i].data);
		}
		Ext.Ajax.request({
					url : 'manageplan/savePlanJobCompleteGather.action',
					method : 'post',
					params : {
						flag : 'finish',
						planTime : planTime.getValue(),
						isUpdate : Ext.util.JSON.encode(updateData),
						isDelete : ids.join(","),
						deptMainId:recL.get("deptMainId")
					},
					success : function(request, action) {
						var o = eval('(' + request.responseText + ')');
						Ext.Msg.alert('提示信息', "保存成功");
						store.rejectChanges();
						ids = [];
						store.reload();
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
	
	//已读 
     function readRecord(){
		var readflag;
		var recL = Grid.getSelectionModel().getSelected();
		if (recL == undefined) {
			Ext.Msg.alert("提示", "请选择左边一条记录！");
			return;
		}
		readflag = recL.get("finishIfRead");
		if (readflag == "N") {
			Ext.Msg.confirm("提示", "确认阅读此记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Msg.wait("操作进行中...", "请稍候");
							Ext.Ajax.request({
										method : 'post',
										url : 'manageplan/modifyFinishIfRead.action',
										params : {
											deptMainId : recL
													.get("deptMainId")
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
	

	// 弹出画面
	
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});
  var compleDesc = new Ext.form.TextArea({
				id : "compDesc",
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
	var winCom = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [compleDesc],
		buttonAlign : "center",
		title : '考核说明录入窗口',
		buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!compleDesc.isValid()) {
							return;
						}
						var record = grid.selModel.getSelected();
						record.set("completeDesc", Ext.get("compDesc").dom.value);
						winCom.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						winCom.hide();
					}
				}]
	});

	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [sm,{
					header : '序号',
					width : 40,
					dataIndex : 'orderBy',
					align : 'center',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})

				}, {
					header : '工作内容',
					width : 250,
					dataIndex : 'jobContent',
					align : 'left',
					renderer:function(value, metadata, record){ //modify by wpzhu 
					
			           metadata.attr = 'style="white-space:normal;word-break:break-all;"'; 
			           return value;  
	             },
					editor : new Ext.form.TextField({
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
								var record = grid
										.getSelectionModel().getSelected();
										
								var value = record.get('jobContent');
								
								memoText.setValue(value);
								win.show();
							})
						}
					}
				})

				}, {
							header : '完成时间',
							dataIndex : 'completeData',
							align : 'center',
							editor : new Ext.form.ComboBox({
										name : 'name111',
										hiddenName : 'hiddenName111',
										mode : 'local',
										triggerAction : 'all',
										store : new Ext.data.SimpleStore({
													fields : ['name', 'value'],
													data : [['当月', 0],
															['跨月', 1],
															['长期', 2],
															['全年', 3]]
												}),
										displayField : 'name',
										valueField : 'value'

									}),
							renderer : function(v) {
								if (v == '0') {
									return '当月';
								} else if (v == '1') {
									return '跨月';
								} else if (v == '2') {
									return '长期';
								} else
									return '全年';
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
					sortable : true,
					align : 'center',
					width : 100,
					renderer:function(value, metadata, record){ 
					
			           metadata.attr = 'style="white-space:normal;word-break:break-all;"'; 
			           return value;  
	             },
	             editor : new Ext.form.TextField({
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
								var record = grid
										.getSelectionModel().getSelected();
										
								var value = record.get('completeDesc');
								
								compleDesc.setValue(value);
								winCom.show();
							})
						}
					}
				})
			       
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
						}, {
							text : "已读",
							disabled : true,
							id : 'btnread',
							iconCls : 'write',
							handler : readRecord
						}],
				sm : sm, // 选择框的选择 Shorthand for
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
							width : '40%',
							region : "west",
							items : [Grid]
						}, {

							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [grid]
						}]
			});

	// 页面载入未选时间时所有按钮不可用
	queryLoad();
	btnSet(true);
})
