Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
	var ifDiversify; 
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
							dept.setValue(result.deptName);
					        editName.setValue(result.workerName);
					        editDate.setValue(finishEditDate.getValue());

							getRecord();
						}
					}
				});
	}

	function getRecord() {
		Ext.Ajax.request({
					url : 'manageplan/getPlanJobCompleteInfo.action',
					method : 'post',
					params : {
						planTime : planTime.getValue(),
						flag : 'approve'
					},
						success : function(action) {
						var result = eval("(" + action.responseText + ")");

						if (result && result.length > 0) {
							var mainId = "";
							var finishWorkflowNo = "";
							for (var i = 0; i < result.length; i++) {
								var ob = result[i];
								mainId += ob[0] + ",";
								finishWorkflowNo += ob[3] + ",";
								
							}
							deptMainId = mainId.substring(0, mainId.length - 1);
							finishWorkNo = finishWorkflowNo.substring(0,finishWorkflowNo.length - 1)

							finishStatus = ob[4];

							ifDiversify = ob[5];
							ifHeating = ob[6];

						}

					}
				})
	}
	
	var con_item = Ext.data.Record.create([{
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
			}, {
				name : 'chargeBy',
				mapping : 6
			}, {
				name : 'orderBy',
				mapping : 7
			}, {
				name : 'editDepcode',
				mapping : 8
			}, {
				name : 'deptName',
				mapping : 9
			}, {
				name : 'finishEditBy',
				mapping : 10
			},{
				name : 'editName',
				mapping : 11
			},{
				name:'finishEditDate',
				mapping : 12
			},{
				name : 'finishSignStatus',
				mapping : 13
			},{
				name : 'finishWorkFlowNo',
				mapping : 14
			},{
				name : 'linkJobId',
				mapping : 15
			},{
			   name:'level1DeptName',
			   mapping : 16
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanJobCompleteApproveList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,{
				width : 150,
				header : "部门",
				sortable : false,
				dataIndex : 'level1DeptName'
			},	{
				width : 80,
				header : "序号",
				align : "center",
				sortable : false,
				dataIndex : 'orderBy'
			}, {
				header : '工作内容',
				dataIndex : 'jobContent',
				width : 430,
				align : 'left'

			}, {
				header : '完成时间',
				dataIndex : 'completeData',
				align : 'center',
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
		con_ds.baseParams = {
			planTime : planTime.getValue(),
			flag:'approve'
		}
		con_ds.load();
		
		getRecord();
	}

	var contbar = new Ext.Toolbar({
				items : ["计划时间:", planTime, '-',{
							text : '查询',
							iconCls : 'query',
							handler : query
						}, '-',{
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
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1
			});

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

		var url = "approveSign.jsp";
		var args = new Object();
		//单个退回
		if (Grid.getSelectionModel().hasSelection()) {
			 var rec = Grid.getSelectionModel().getSelected();
			 args.deptMainId=rec.get("deptMainId");
			 args.entryId=rec.get("finishWorkFlowNo");
			 selectFlag="single";
		}
		else {
			selectFlag = "many";
//			args.deptMainId = deptMainId;// update  by sychen 20100707
			args.entryId = finishWorkNo;
		}
		   
		args.deptMainIds = deptMainId;// add by sychen 20100707
		args.selectFlag = selectFlag;
		args.workflowType = "bqDeptPlanFinishApprove";
		args.finishStatus = finishStatus;
		args.ifDiversify = ifDiversify;
		args.ifHeating = ifHeating;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			query();
		}
	}

		function init() {
		Ext.Ajax.request({
			url : 'manageplan/getPlanJobCompleteMaxPlanTime.action',
					method : 'post',
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '')
						{
							;
						}else{
							var year = res.toString().substring(0,4);
							var month = res.toString().substring(5,7);
							planTime.setValue(year +"-" + month)
						}
						
						query();
					}
				});
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
	init()
})
