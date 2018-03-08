Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
//	var isEqu = null;
	var ifDiversify; // add by sychen 20100329
	var ifHeating;
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
	
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							deptH.setValue(result.deptCode);
							dept.setValue(result.deptName)//add by sychen 20100507
					        editName.setValue(result.workerName);//add by sychen 20100507
					        editDate.setValue(finishEditDate.getValue());//add by sychen 20100507
//                            workercode.setValue(result.workerCode);
							getRecord();
						}
					}
				});
	}

	function getRecord() {
//		Ext.Msg.wait('正在查询数据……');
		Ext.Ajax.request({
					url : 'manageplan/getBpPlanStatusByDeptCode.action',
					method : 'post',
					params : {
//						 deptH : workercode.getValue(),
						planTime : planTime.getValue()

					},//update by sychen 20100408
						success : function(action) {
						var result = eval("(" + action.responseText + ")");
                        
						if (result && result.length > 0) {
						        var mainId = "";
	                            var finishWorkflowNo="";
							for(var i=0;i<result.length;i++){
							var ob=result[i];
                                mainId += ob[0]+ ",";
								finishWorkflowNo += ob[9]+ ",";
							}
							if (ob[10] =='1'||ob[10] =='2'||ob[10] =='3'||ob[10] =='4'||
							    ob[10] =='5'||ob[10] =='6'||ob[10] =='7'||ob[10] =='8'||
							    ob[10] =='9'||ob[10] =='10') {
								// 设定默认工号
							    deptMainId=mainId.substring(0,mainId.length-1);
							    finishWorkNo=finishWorkflowNo.substring(0,finishWorkflowNo.length-1)
								
								finishStatus = ob[10];

                                ifDiversify = ob[12];
								ifHeating = ob[13];
								con_ds.rejectChanges();
								con_ds.load({
											params : {
												// modified by liuyi 20100525 
//												depMainId:deptMainId,
												deptMainId:deptMainId,
												isApprove : 'Y'
											}
										});		
								Ext.getCmp('btnapprove').setDisabled(false);
								if (finishStatus == '11') {
									Ext.getCmp('btnapprove').setDisabled(true);
									Ext.Msg.alert('提示', '该部门工作计划完成情况审批已通过！');
								}		
							} 
						
						}
//					success : function(response, options) {
//						Ext.Msg.hide();
//						var res = Ext.util.JSON.decode(response.responseText);
//						
//						if (res && res[0]) {
//							var resu = res[0].toString().split(',');
//							if (resu[0]) {
//								con_ds.baseParams = {
//									deptMainId : resu[0],
//									isApprove : 'Y'
//								}
//								con_ds.load();
//								dept.setValue(resu[3]);
//								editName.setValue(resu[4]);
//								editDate.setValue(resu[6]);
//								deptMainId = resu[0];
//								finishWorkNo = resu[9];
//								finishStatus = resu[10];
//								Ext.getCmp('btnapprove').setDisabled(false);
//								if (finishStatus == '11') {
//									Ext.getCmp('btnapprove').setDisabled(true);
//									Ext.Msg.alert('提示', '该部门工作计划完成情况审批已通过！');
//								}
////								isEqu = resu[11];
//								ifDiversify = resu[12];
//								ifHeating = resu[13];
//							}
//						} 
						else {
//							editName.setValue(null);
//							editDate.setValue(null);
							Ext.getCmp('btnapprove').setDisabled(true);
							con_ds.removeAll();
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
				name : 'completeDate',
				mapping : 5
			}, {//add by sychen 20100408
				name : 'editDepcode',
				mapping : 6
			},{
				name : 'deptName',
				mapping : 7
			},{
				name:'finishEditbyname',
				mapping: 8
			},{
				name:'finishEditDate',
				mapping:9
			},{
				name:'orderBy',
				mapping:14
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
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

	var con_item_cm = new Ext.grid.ColumnModel([con_sm, 
		      // add by sychen 20100408
		      	{
				width : 150,
				header : "部门",
				sortable : false,
				dataIndex : 'deptName'
			},	{
				width : 80,
				header : "序号",
				sortable : false,
				dataIndex : 'orderBy'
			}, {
				header : '工作内容',
				dataIndex : 'jobContent',
				width : 430,
				align : 'left'

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
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	
	var workercode = new Ext.form.Hidden({
				id : 'workercode',
				name : 'workercode'
			})
	var deptH = new Ext.form.Hidden({
				id : 'deptH',
				name : 'deptH'
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
				items : [planTime, query, {
							text : "审批",
							id : 'btnapprove',
							iconCls : 'approve',
							handler : approveFun
						}]
			});

	var Grid = new Ext.grid.GridPanel({
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
      // add by sychen 20100408
		Grid.on("rowclick", function() {
				if (Grid.getSelectionModel().hasSelection()) {
					var rec = Grid.getSelectionModel().getSelected();
					dept.setValue(rec.get("deptName"));
					editName.setValue(rec.get("finishEditbyname"));
					editDate.setValue(rec.get("finishEditDate"));

				} else {
					dept.setValue(null);
					editName.setValue(null);
					editDate.setValue(null);
					
				}
			})		
			
	function approveFun() {
		if (con_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行审批！');
			return;
		}

		var url = "deptPlanCompleteApprove.jsp";
		var args = new Object();
		args.entryId = finishWorkNo;
		args.deptMainId = deptMainId;
//		args.isEqu = isEqu; //update by sychen 20100329
//		args.workflowType = "bqDeptJobFinish";
		args.workflowType = "bqDeptPlanFinishApprove";
		args.finishStatus = finishStatus;
		args.ifDiversify = ifDiversify;
		args.ifHeating = ifHeating;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			getRecord();
		}
	}

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
