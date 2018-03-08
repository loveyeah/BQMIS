Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	var ifDiversify; 
	var ifHeating;
	var deptMainId;
	var strIdAndWoNo;
	var flag;
	var selectFlag;
	var caller='';
	var callerName='';
	// 系统当前时间
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
							var DeptCode = arr[0];
							editDepcode.setValue(DeptCode);
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
							var workerCode=result.workerCode;
							editDepcode.setValue(DeptCode);
							editBy.setValue(workerCode);
							
							editByName.setValue(result.workerName);//add by sychen 20100507
							editDepName.setValue(result.deptName);//add by sychen 20100507
							editDate.setValue(editTime.getValue());//add by sychen 20100507
						}
						init();
					}
				});
	}

	function init() {
		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobDepMainApprove.action',
				{
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result && result.list && result.list.length > 0) {
						        var mainId = "";
	                            var workflowNo="";
							for(var i=0;i<result.list.length;i++){
							var ob=result.list[i];
//								
                                mainId += ob[0]+ ",";
								workflowNo += ob[7]+ ",";
								var arr = new Array()
						        arr = workflowNo.split(",");
						        entryId2=arr[0];
							}
							if (ob[8] =='1'||ob[8] =='2'||ob[8] =='3'||ob[8] =='4'||
							    ob[8] =='5'||ob[8] =='6'||ob[8] =='7'||ob[8] =='8'||
							    ob[8] =='9'||ob[8] =='10') {
								// 设定默认工号
							    deptMainId=mainId.substring(0,mainId.length-1);
							    workFlowNo=workflowNo.substring(0,workflowNo.length-1)
								
								signStatus = ob[8];

								if(ob[8] =='2'||ob[8] =='3'||ob[8] =='4'||ob[8] =='5'||ob[8] =='6'
								   || ob[8] == '7'|| ob[8] == '8' || ob[8] == '9'|| ob[8] == '10') {
								
									Ext.Ajax.request({
													    url : 'manageplan/getBpJPlanJobDepMainCaller.action',
														method : 'POST',
														params : {
															entryId : entryId2
														},
														success : function(
																action) {
															var result = eval("("+ action.responseText+ ")");
															var arr = new Array()
															arr = result.split(",");
															if (result) {
																caller = arr[0];
																callerName = arr[1];
															}
														}
													});
								}
                                ifDiversify = ob[11];
								ifHeating = ob[12];
								store.rejectChanges();
								store.load({
											params : {
												depMainId:deptMainId,
												start : 0,
												limit : 18
											}
										});
							   getMainId();			
							} 
							else {
								signStatus = ob[8];
								store.rejectChanges();
								store.load({
											params : {
												// 传0，使store清空
												depMainId : 0,
												start : 0,
												limit : 18
											}
										});
							}
						
						} else {
							store.rejectChanges();
							store.load({
										params : {
											// 传0，使store清空
											depMainId : 0,
											start : 0,
											limit : 18
										}
									});
							signStatus = undefined;

						}

						// 选时间后所有按钮先全部不可用
						Ext.getCmp("btnapprove").setDisabled(true);
						Ext.getCmp("btnadd").setDisabled(true);
						Ext.getCmp("btndelete").setDisabled(true);
						Ext.getCmp("btnsave").setDisabled(true);
						Ext.getCmp("btncancer").setDisabled(true);

						// 页面载入时根据工作流状态控制哪些按钮可用
						if (signStatus == "1"||signStatus =="2"||signStatus =="3"||signStatus =="4"||
							signStatus =="5"||signStatus =="6"|| signStatus =="7"||signStatus =="8"||
							signStatus =="9"||signStatus =="10") {
							Ext.getCmp("btnapprove").setDisabled(false);
							Ext.getCmp("btnadd").setDisabled(false);
							Ext.getCmp("btndelete").setDisabled(false);
							Ext.getCmp("btnsave").setDisabled(false);
							Ext.getCmp("btncancer").setDisabled(false);
						}

						else if (signStatus == "11") {
							Ext.getCmp("btnapprove").setDisabled(true);
							Ext.getCmp("btnadd").setDisabled(true);
							Ext.getCmp("btndelete").setDisabled(true);
							Ext.getCmp("btnsave").setDisabled(true);
							Ext.getCmp("btncancer").setDisabled(true);
//							Ext.Msg.alert('提示', '此计划已审批!')

						} else if (signStatus == "12" || signStatus == "0") {
							Ext.getCmp("btnapprove").setDisabled(true);
							Ext.getCmp("btnadd").setDisabled(true);
							Ext.getCmp("btndelete").setDisabled(true);
							Ext.getCmp("btnsave").setDisabled(true);
							Ext.getCmp("btncancer").setDisabled(true);
//							Ext.Msg.alert('提示', '此计划未上报!')

						} else {
							Ext.getCmp("btnapprove").setDisabled(true);
							Ext.getCmp("btnadd").setDisabled(true);
							Ext.getCmp("btndelete").setDisabled(true);
							Ext.getCmp("btnsave").setDisabled(true);
							Ext.getCmp("btncancer").setDisabled(true);
//							Ext.Msg.alert('提示', '无数据审批!')

						}

					}
				}, 'planTime=' + planTime.getValue() + '&editDepcode='
						+ editDepcode.getValue() + '&approve=approve');
	}
	
	function getMainId() {
		Ext.Ajax.request({
			url : 'manageplan/getBpJPlanJobDeptMainId.action',
					method : 'post',
					params : {
					         planTime:planTime.getValue()
					},
					success :function(response,options) {
						var res = response.responseText;
						
						if(res.toString() == '')
						{
							depMainId.setValue(0);
							getWorkCode();
						}else{
						    var mainId = res.toString();
							depMainId.setValue(mainId);
							getWorkCode();
						}
					}
				});
	}
	
	
	var deptRec = new Ext.data.Record.create([{
    	name  : 'editDepcode',
    	mapping : 0
    },{
    	name : 'deptname',
    	mapping : 1
    }])
    // 部门store
    var deptStore = new Ext.data.Store({
    	proxy : new Ext.data.HttpProxy({
    		url : 'manageplan/getBpJPlanJobDept.action'
    	}),
    	reader : new Ext.data.ArrayReader({},deptRec)
    })
    deptStore.load();
    
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
			
//	var editDepcodeA= new Ext.form.TextField({
//	          name:'editDepcodeA'
//	})		
	// 计划部门id
	var depMainId = new Ext.form.Hidden({
				name : 'depMainId'
			});

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
				name:'signStatus',
				mapping:12
			},{
				name:'orderBy',
				mapping:13
			},{
				name:'level1DeptName',
				mapping:14
			},{
				name:'workFlowNo',
				mapping:15
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
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

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
					width : 100,
					header : "部门",
					sortable : false,
					dataIndex : 'editDepcode',
					editor : new Ext.form.ComboBox({
								hiddenName : 'heDept',
								name : 'eDept',
								id:'eDept',
								store : deptStore,
								displayField : 'deptname',
								valueField : 'editDepcode',
								mode : 'local',
								editable : false,
								triggerAction : 'all',
								listeners : {
									"change" : function() {
									//	alert(Ext.get("eDept").dom.value);										
										grid.getSelectionModel().getSelected()
									.set("level1DeptName",
											Ext.get("eDept").dom.value);
									}
								}
							}),
							renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store)  {
								return record.get("level1DeptName");
							}
				},{
					header : "责任人",
					width : 100,
					sortable : true,
					dataIndex : 'chargeName',
						editor : new Ext.form.ComboBox({
						mode : 'remote',
						editable : false,
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
				}, 
						// add by sychen 20100505
				  {
				    width : 60,
					header : "序号",
					align :"center",
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
					editor : new Ext.form.TextField({
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
					text : "审批",
					id : 'btnapprove',
					iconCls : 'approve',
					handler : approve
				}],
		sm : sm, // 选择框的选择 Shorthand for
		clicksToEdit : 1,
		// selModel（selectModel）
		// 分页
		bbar : ["编辑部门:", editDepName, '-', "编辑人:", editByName, '-', "编辑时间:",
				editDate],
		viewConfig : {
			forceFit : true
		}
	});
	grid.on("rowclick", function() {

				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					editDepName.setValue(rec.get("deptName"));
					editByName.setValue(rec.get("editbyname"));
					editDate.setValue(rec.get("editDate"));

				} else {
					editDepName.setValue(null);
					editByName.setValue(null);
					editDate.setValue(null);
					
				}
			})
		//-----add by sychen 20100428------------	
		grid.on("beforeedit", function(obj) {
				var record = obj.record;
				var field = obj.field;
					
				if(field=="editDepcode")
				{
					if(record.get("depMainId")!=null &&record.get("depMainId")!='0')
					{
						return false;
					}
				}
				if(field=="chargeName")
				{
					if(record.get("jobId")!=null)
					{
						return false;
					}
					else{
					   if(signStatus!='1')
					{
						return false;
					}
					}
				}
			});
//-----add by end--------------------
			
	// 增加
	function addRecord() {
//		if (depMainId.getValue() == null) {
//			Ext.Msg.alert("提示", "请先选择计划时间！");
//			return;
//		}
		var count = store.getCount();
		var currentIndex = count;
		var o = new MyRecord({
			        'editDepcode':'',
					'ifComplete' : '0',
//					'depMainId' : depMainId.getValue(),
					'depMainId' : '0',
					'chargeBy':caller,
					'chargeName':callerName,
					'jobContent' : '',
					'completeData' : '0',
					'orderBy':'' // add by sychen 20100505
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
			   if (modifyRec[i].get("chargeBy") == null
								|| modifyRec[i].get("chargeBy") == "") {
				Ext.Msg.alert("提示", "请选择责任人！");
				return;
			}
			   if (modifyRec[i].get("orderBy") == null
								|| modifyRec[i].get("orderBy") == "") {
							Ext.Msg.alert("提示", "序号不能为空！");
							return;
						}
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
						url : 'manageplan/saveBpJPlanJobDepApprove.action',
						method : 'post',
						params : {
							planTime : planTime.getValue(),
							mainId : depMainId.getValue(),
							deptCode : editDepcode.getValue(),
//							deptCodeA : editDepcodeA.getValue(),
							flag:"approve",//add by sychen 20100408
							workflowType:"bqDeptWorkPlanApprove",//add by sychen 20100408
							signStatus:signStatus,
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(";")
						},
						success : function(form, action) {
							var o = eval('(' + form.responseText + ')');
							depMainId.setValue(o.obj);
							Ext.MessageBox.alert('提示信息', o.msg);
							if (o.msg.indexOf("已存在") != -1) {
							return;
						   }
							store.rejectChanges();
							ids = [];
							store.load({
										params : {
											depMainId : o.obj,
											start : 0,
											limit : 18
										}
									});
							query();
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

	// 签字
	function approve() {
		
		var url = "../SignWorkPlan/approveDeptWorkPlan.jsp";
		var args = new Object();
		
		//add by sychen 20100628 单个退回
		if (grid.getSelectionModel().hasSelection()) {
			 var rec = grid.getSelectionModel().getSelected();
			 args.prjNo=rec.get("depMainId");
			 args.entryId=rec.get("workFlowNo");
			 selectFlag="single";
		}
		else {
			selectFlag = "many";
			args.entryId = workFlowNo;
//		    args.prjNo = deptMainId;// update  by sychen 20100707
		}
		args.prjNos = deptMainId;// add by sychen 20100707
		
        //add by sychen 20100628 end
		
//		args.prjNo = deptMainId;
//		args.entryId = workFlowNo;
		args.selectFlag = selectFlag;//add by sychen 20100628
		
		args.workflowType = "bqDeptWorkPlanApprove";
		args.prjTypeId = "";
		args.prjStatus = signStatus;
		args.ifDiversify = ifDiversify;
		args.ifHeating = ifHeating;
		
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		// 按钮设为不可用
		if (obj) {
			init()
		}

	}

	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							height : '50%',
							items : [grid]
						}]
			});
	// 页面载入未选时间时所有按钮不可用
	Ext.getCmp("btnapprove").setDisabled(true);
	Ext.getCmp("btnadd").setDisabled(true);
	Ext.getCmp("btndelete").setDisabled(true);
	Ext.getCmp("btnsave").setDisabled(true);
	Ext.getCmp("btncancer").setDisabled(true);
	query()

})
