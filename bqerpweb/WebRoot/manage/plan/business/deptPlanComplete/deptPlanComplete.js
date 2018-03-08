Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
//	var isEqu = null;
	
	var actionId;
	var deptId;
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 2;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

		// 系统当前时间
	function getToDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	//update by sychen 20100409
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
							
							deptH.setValue(DeptCode);
							dept.setValue(DeptName);//add by sychen 20100507
					        editName.setValue(workerName);//add by sychen 20100507
					        editDate.setValue(finishEditDate.getValue());//add by sychen 20100507
							workercode.setValue(workerCode);
							
							deptId = result.deptId;
							getRecord();
						}
					}
				});
	}
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							deptH.setValue(result.deptCode);
							dept.setValue(result.deptName);
							workercode.setValue(result.workerCode);
							editName.setValue(result.workerName);
							deptId = result.deptId;
							getRecord();
						}
					}
				});
	}
    
	// update  by sychen 20100505
//	function getRecord() {
////		Ext.Msg.wait('正在查询数据……');
//		Ext.Ajax.request({
//					url : 'manageplan/getBpPlanStatusByDeptCode.action',
//					method : 'post',
//					params : {
//						deptH : workercode.getValue(),
//						planTime : planTime.getValue()
//					},
//					success : function(response, options) {
////						Ext.Msg.hide();
//						var res = Ext.util.JSON.decode(response.responseText);
//						if (res && res[0]) {
//							var resu = res[0].toString().split(',');
//							if (resu[0]) {
//								con_ds.baseParams = {
//									deptMainId : resu[0]
//								}
//
//								con_ds.load();
//								dept.setValue(resu[3])
//								editName.setValue(resu[4]);
//								editDate.setValue(resu[7]);
//								Ext.getCmp('btnsave').setDisabled(false);
//								Ext.getCmp('btnreport').setDisabled(false);
//								deptMainId = resu[0];
//								finishWorkNo = resu[9];
//								finishStatus = resu[10];
//								if (finishStatus == '1' || finishStatus == '2') {
//									Ext.getCmp('btnsave').setDisabled(true);
//									Ext.getCmp('btnreport').setDisabled(true);
//								}
////								isEqu = resu[10];
//							}
//						} else {
//							con_ds.removeAll();
//							// Ext.Msg.alert('提示','该月份的工作计划汇总审批未结束，不能提前填写完成情况！');
////							Ext.Msg.alert('提示', '该月份的工作计划审批未结束，不能提前填写完成情况！');
//							editName.setValue(null);
//							editDate.setValue(null);
//							Ext.getCmp('btnsave').setDisabled(true);
//							Ext.getCmp('btnreport').setDisabled(true);
//						}
//
//					}
//				})
//	}

// add by sychen 20100505
	function getRecord() {
		Ext.Ajax.request({
					url : 'manageplan/getBpPlanStatusByChargeBy.action',
					method : 'post',
					params : {
						deptH : workercode.getValue(),
						planTime : planTime.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result.list.length > 0) {
						        var mainId = "";
	                            var finishworkflowNo="";
	                            var finishsignstatus="";
							for(var i=0;i<result.list.length;i++){
							var ob=result.list[i];
                                mainId += ob[0]+ ",";
                                finishworkflowNo += ob[9]+ ",";
                                finishsignstatus += ob[10]+ ",";
							}
						 deptMainId=mainId.substring(0,mainId.length-1);
						 finishStatus=ob[10];
						 finishWorkNo=finishworkflowNo.substring(0,finishworkflowNo.length-1)
						 finishStatus=finishsignstatus.substring(0,finishsignstatus.length-1)	
						 con_ds.baseParams = {
									deptMainId : deptMainId
							}	
						 con_ds.load();	
						 Ext.getCmp('btnsave').setDisabled(false);
						 Ext.getCmp('btnreport').setDisabled(false);
						}
						
						else {
							con_ds.removeAll();
//							Ext.Msg.alert('提示', '该月份的工作计划审批未结束，不能提前填写完成情况！');
//							
							Ext.getCmp('btnsave').setDisabled(true);
							Ext.getCmp('btnreport').setDisabled(true);
						}

					}
				})
	}
	
	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
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
				name : 'deptName',
				mapping : 7
			}, {
				name : 'editbyname',
				mapping : 10
			}, {
				name : 'editDate',
				mapping : 11
			}, {
				name : 'finishWorkFlowNo',
				mapping : 12
			}, {
				name : 'finishSignStatus',
				mapping : 13
			},{
				name : 'orderBy',
				mapping : 14
			},{
				name : 'signStatus',
				mapping : 15
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanJobCompleteDetialList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	//add by sychen 20100613
			
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
						var record = Grid.selModel.getSelected();
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
			
    // add by sychen 20100613 end
			
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '部门',
				dataIndex : 'deptName',
				width : 150,
				align : 'left'

			},{//add by wpzhu 2010/05/25
				    width : 80,
					header : "序号",
					align : 'center',
					sortable : false,
					dataIndex : 'orderBy',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})
				}, {
				header : '工作内容',
				dataIndex : 'jobContent',
				width : 430,
				align : 'left',
				editor : new Ext.form.TextArea({
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
													Grid.stopEditing();
													var record =Grid
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
											data : [['当月', 0], ['跨月', 1],
													['长期', 2], ['全年', 3]]
										}),
								displayField : 'name',
								valueField : 'value'

							}),
				renderer : function(v) {
					if (v == '0') {
						return '当月';
					} else if (v == '1') {
						return '跨越';
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
										data : [['0', '未完成'], ['1', '进行中'],
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

			}]);
	con_item_cm.defaultSortable = true;
	var deptH = new Ext.form.Hidden({
				id : 'deptH',
				name : 'deptH'
			})
	var workercode = new Ext.form.Hidden({
				id : 'workercode',
				name : 'workercode'
			})
	var dept = new Ext.form.TextField({
				id : 'dept',
				name : 'dept',
				readOnly : true
			})
	var editName = new Ext.form.TextField({
				id : 'editName',
				readOnly : true
			})
	var editDate = new Ext.form.TextField({
				id : 'editDate',
				readOnly : true
			})
	var gridbbar = new Ext.Toolbar({
				items : ['编辑部门：', deptH, dept, '填写人：', editName, '填写时间：',
						editDate]
			})
			
	var finishEditDate = new Ext.form.TextField({
				name : 'finishEditDate',
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
	finishEditDate.setValue(getToDate());	
			
	// 计划时间
	var planTime = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM",
									isShowClear : false
								});
						this.blur();
					}
				}

			});
	planTime.setValue(getDate());

	function query() {
		getRecord()

	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query

			})

	var contbar = new Ext.Toolbar({
				items : [planTime, query, '-', {
					text : "新增",
					id : 'btnadd',
					iconCls : 'add',
					handler : addRecord
				}, '-', {
					text : "删除",
					id : 'btndelete',
					iconCls : 'delete',
					handler : deleteRecords
				}, {
							text : "保存",
							id : 'btnsave',
							iconCls : 'save',
							handler : saveFun
						}, {
							text : "上报",
							id : 'btnreport',
							iconCls : 'upcommit',
							handler : reportFun
						}]
			});

	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				// height : 425,
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1
			});
  // add by sychen 20100505
	Grid.on("rowclick", function() {

				if (Grid.getSelectionModel().hasSelection()) {
					var rec = Grid.getSelectionModel().getSelected();
					dept.setValue(rec.get("deptName"));
					editName.setValue(rec.get("editbyname"));
					editDate.setValue(rec.get("editDate"));

				} else {
					dept.setValue(null);
					editName.setValue(null);
					editDate.setValue(null);
					
				}
			})		
			
	Grid.on('beforeedit', function() {
				if (Ext.getCmp('btnsave').disabled == true)
					return false;
			})

//add by sychen 20100613 
			
		Grid.on("beforeedit", function(obj) {
				var record = obj.record;
				var field = obj.field;
				if(field=="orderBy"||field=="jobContent"||field=="completeData")
				{
					if(record.get("jobId")!=null){
						return false;
					}
				}
			});	
			
	function addRecord() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var o = new con_item({
			        'deptName':dept.getValue(),
					'ifComplete' : '0',
					'depMainId' : '0',
					'chargeBy':workercode.getValue(),
					'chargeName':editName.getValue(),
					'jobContent' : '',
					'completeData' : '0',
					'completeDesc':'',
					'orderBy':'' 
				});
		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);
	}
	// 删除记录

	var ids = new Array();
	function deleteRecords() {
		var sm = Grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("signStatus")=='11') {
					Ext.Msg.alert('提示', '此计划非新增的计划，不可删除！');
					return;
				}
				else {
					var id = new Array();
					if (member.get("jobId") != null) {
						ids.push(member.get("jobId"));
					}
					Grid.getStore().remove(member);
					Grid.getStore().getModifiedRecords().remove(member);
				}
			}
		}
	}			
// add by sychen 20100613 end	
	
	function saveFun() {
		if (con_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行保存！');
			return;
		}
		var modifieds = con_ds.getModifiedRecords();
		if (modifieds.length == 0) {
			Ext.Msg.alert('提示', '没有任何修改！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
					if (id == 'yes') {
						var updates = new Array();
						for (var i = 0; i < modifieds.length; i++) {
							updates.push(modifieds[i].data)
						}
						Ext.Ajax.request({
									url : 'manageplan/saveDeptPlanComplete.action',
									method : 'post',
									params : {
//										updates : Ext.util.JSON.encode(updates)
							             planTime : planTime.getValue(),
							             isUpdate : Ext.util.JSON.encode(updates),
							             isDelete : ids.join(";")
									},
									success : function(response, options) {
										Ext.Msg.alert('提示', '数据保存成功！');
										con_ds.rejectChanges();
										con_ds.reload()
										getRecord()
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '数据保存失败！');
									}
								})
					}
				}

		)

	}
	//add by sychen 20100505
   function reportFun() {
		if (con_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行上报！');
			return;
		}
		var arr = new Array()
		arr = finishStatus.split(",");
		for(i=0;i<arr.length;i++){
		 if(arr[i]==null||arr[i]=='null'||arr[i]==''){
		  Ext.Msg.alert('提示', '请先保存数据！');
			return;
		 }
		}
		var url = "reportsign.jsp";
		var args = new Object();
		args.entryId = finishWorkNo;
		args.deptMainId = deptMainId;
		args.workflowType = "bqDeptPlanFinishApprove";
		args.actionId = actionId;
		args.deptId = deptId;
		
		args.roles = true;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			getRecord()
		}
	}
	
//	function reportFun() {
//		if (con_ds.getTotalCount() == 0) {
//			Ext.Msg.alert('提示', '无数据进行上报！');
//			return;
//		}
//		if (finishStatus == '1' || finishStatus == '2') {
//			Ext.Msg.alert('提示', '该月份数据已上报！');
//			return;
//		} else if (finishStatus == null || finishStatus == '') {
//			Ext.Msg.alert('提示', '请先保存数据！');
//			return;
//		}
//		var url = "reportsign.jsp";
//		var args = new Object();
//		args.entryId = finishWorkNo;
//		args.deptMainId = deptMainId;
////		args.isEqu = isEqu; //update by sychen 20100329
////		args.workflowType = "bqDeptJobFinish";
//		args.workflowType = "bqDeptPlanFinishApprove";
//		args.actionId = actionId;
//		args.deptId = deptId;
//		
//		args.roles = true;
//		var obj = window.showModalDialog(url, args,
//				'status:no;dialogWidth=770px;dialogHeight=550px');
//		if (obj) {
//			getRecord()
//		}
//	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});

	getWorkCode();
})
