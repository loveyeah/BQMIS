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
//add by wpzhu 20100727--------------
 function getMaxmonth() {
		Ext.Ajax.request({
			url : 'hr/getMaxMonth.action',
					method : 'post',
					params : {
						workFlowState : "'2','3'"
						
					},
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == ''||res.toString() ==0)
						{
							
						}else{
							var  month=res.toString();
							trainStartdate.setValue(month);
						}
				          query();
					}
					
				});
	}	
	getMaxmonth();
	//----------------------------------------
	function query() {
		
		listStore.load({
					params : {
						workFlowState : "'2','3'",
						rewardMonth : trainStartdate.getValue(),
						start : 0,
						limit : 18
					}
				})
	}

	var grid = new Ext.grid.GridPanel({
				layout : 'fit',
				autoHeight : true,
				autoScrolla : true,
				enableColumnMove : false,
				store : listStore,
				listeners : {
					'rowdblclick' : function() {
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
						},{
							text : "查阅",
							iconCls : 'write',
							minWidth : 70,
							handler : function() {
								if (grid.getSelectionModel().hasSelection()) {
									var record = grid.getSelectionModel().getSelected();
									var args = new Object();
									args.detailId = record.data.detailId;
									var url = "shfitSign.jsp";
									var obj=window.showModalDialog(url, args,
										'dialogWidth=800px;dialogHeight=600px;status=no');
									if(obj) {
										listStore.reload();
									}
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
							}
						}]
			});
			
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});


	});
