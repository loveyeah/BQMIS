Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'detailId'
			}, {
				name : 'rewardId'
			}, {
				name : 'deptId'
			}, {
				name : 'empCount'
			}, {
				name : 'lastMonthNum'
			}, {
				name : 'monthRewardNum'
			}, {
				name : 'quantifyCash'
			}, {
				name : 'extraAddNum'
			}, {
				name : 'monthAssessNum'
			}, {
				name : 'otherNum'
			}, {
				name : 'totalNum'
			}, {
				name : 'memo'
			}, {
				name : 'workFlowState'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'deptName'
			}, {
				name : 'isUse'
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/administratorQuery.action'
			});

	var TheReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var listStore = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var trainStart = new Date();
	trainStart = trainStart.format('Y-m');
	// 培训开始时间
	var trainStartdate = new Ext.form.TextField({
				width : 100,
				style : 'cursor:pointer',
				allowBlank : true,
				readOnly : true,
				value : trainStart,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM'
								});
					}
				}
			});

	function query() {
		listStore.load({
					params : {
						workFlowState : '1,2,3,4',
						rewardMonth : trainStartdate.getValue(),
						start : 0,
						limit : 18
					}
				})
	}
	//查阅按钮
	var signB=new Ext.Button({
		text : "查阅",
							iconCls : 'write',
							minWidth : 70,
							handler : function() {
								if (grid.getSelectionModel().hasSelection()) {
									var record = grid.getSelectionModel().getSelected();
									var args = new Object();
									args.detailId = record.data.detailId;
									var url = "sign.jsp";
									var obj=window.showModalDialog(url, args,
										'dialogWidth=800px;dialogHeight=600px;status=no');
									if(obj) {
										listStore.reload();
									}
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
							}
	})
	var grid = new Ext.grid.GridPanel({
				layout : 'fit',
				autoHeight : true,
				autoScrolla : true,
				enableColumnMove : false,
				store : listStore,
				listeners : {
					'rowdblclick' : function() {
					},'click' : function() {
						var record = grid.getSelectionModel().getSelected();
						if (record!=null) {
							if (record.data.workFlowState=="1") {
								signB.setDisabled(false);
							}else{
								signB.setDisabled(true);
							}
						}else{
								signB.setDisabled(true);
						}
					}
				},
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "ID",
							sortable : true,
							dataIndex : 'detailId',
							hidden : true
						}, {
							header : "rewardId",
							sortable : true,
							dataIndex : 'rewardId',
							hidden : true
						}, {
							header : "deptId",
							sortable : true,
							dataIndex : 'deptId',
							hidden : true
						}, {
							header : "部门",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'workFlowNo'
						}, {
							header : "人数",
							width : 100,
							sortable : true,
							dataIndex : 'empCount'
					   	}, {
							header : "上月结余",
							width : 100,
							sortable : true,
							dataIndex : 'lastMonthNum'
						}, {
							header : "月奖金额",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum'
						}, {
							header : "量化兑现",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyCash'
						}, {

							header : "工会主席技师增加值",
							width : 120,
							sortable : true,
							dataIndex : 'extraAddNum'
						}, {
							header : "月度考核",
							width : 80,
							sortable : true,
							dataIndex : 'monthAssessNum'
						}, {
							header : "其他",
							width : 80,
							sortable : true,
							dataIndex : 'otherNum'
						}, {
							header : "金额",
							width : 80,
							sortable : true,
							dataIndex : 'totalNum'
						}, {
							header : "备注",
							width : 180,
							sortable : true,
							dataIndex : 'memo'
						}, {
							header : "状态",
							width : 180,
							dataIndex : 'workFlowState',
							renderer:function(v){
								if(v=='1'){
									return "未查阅";
								}else if(v=='2'){
									return "已查阅";
								}else if(v=='3'){
									return "班组已查阅";
								}else if(v=='4'){
									return "部门已同意";
								}else{
									return "";
								}
							}
						}],
				sm : sm,
				clicksToEdit : 1,
				autoSizeColumns : true,
				tbar : ["月度:", trainStartdate, {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						},{
							text : "详细查看",
//							iconCls : 'query',
							minWidth : 70,
							handler : function() {
								if (grid.getSelectionModel().hasSelection()) {
									var record = grid.getSelectionModel().getSelected();
									var url = "/powerrpt/report/webfile/hr/monthRewardNotice.jsp?detailId="+record.data.detailId;
									window.open(url);
//									var obj=window.showModalDialog(url, args,
//										'dialogWidth=800px;dialogHeight=600px;status=no');
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}	
							}
						},signB,'-',new Ext.Button({
							text:'查看部门明细',
							iconCls : 'detail',
							handler:function(){
								var record = grid.getSelectionModel().getSelected();
									if (record == null) {
										Ext.Msg.alert("提示", "请选择一条记录！");
									} else {
										var arg = new Object();
										arg.deptName=record.get("workFlowNo");
										arg.deptId = record.get("deptId");
										arg.rewardId =record.data.rewardId  //  月奖主表Id
										
										
										
										window.showModalDialog('deptDetailQuery.jsp', arg,
												'status:no;dialogWidth=750px;dialogHeight=450px');
		}
							}
							
						})]
			});
	query();	
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});


	});
