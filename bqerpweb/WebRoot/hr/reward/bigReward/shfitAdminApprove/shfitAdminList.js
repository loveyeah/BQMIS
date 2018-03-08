Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var bigRewardId = "";

	var MyRecord = Ext.data.Record.create([{
				name : 'bigDetailId',
				mapping : 0
			}, {
				name : 'deptId',
				mapping : 1
			}, {
				name : 'deptName',
				mapping : 2
			}, {
				name : 'bigRewardId',
				mapping : 3
			}, {
				name : 'bigAwardName',
				mapping : 4
			}, {
				name : 'bigRewardMonth',
				mapping : 5
			}, {
				name : 'empCount',
				mapping : 6
			}, {
				name : 'bigRewardNum',
				mapping : 7
			}, {
				name : 'memo',
				mapping : 8
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/bigRewardAdministratorQuery.action'
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

	function query() {
		listStore.load({
					params : {
						workFlowState : "'2','3'",
						bigRewardId : selectName.combo.getValue(),
						start : 0,
						limit : 18
					}
				})
	}
	
	var selectName = new bigReward.selectName();
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
							dataIndex : 'bigDetailId',
							hidden : true
						}, {
							header : "rewardId",
							sortable : true,
							dataIndex : 'bigRewardId',
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
							dataIndex : 'deptName'
						}, {
							header : "大奖名称",
							width : 100,
							sortable : true,
							dataIndex : 'bigAwardName'
					   	}, {
							header : "月度",
							width : 100,
							sortable : true,
							dataIndex : 'bigRewardMonth'
						}, {
							header : "受奖人数",
							width : 100,
							sortable : true,
							dataIndex : 'empCount'
						}, {
							header : "金额",
							width : 100,
							sortable : true,
							dataIndex : 'bigRewardNum'
						}, {

							header : "备注",
							width : 120,
							sortable : true,
							dataIndex : 'memo'
						}],
				sm : sm,
				clicksToEdit : 1,
				autoSizeColumns : true,
				tbar : ["大奖名称:", selectName.combo, {
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
									var url = "/powerrpt/report/webfile/hr/bigRewardNotice.jsp?detailId="+record.data.bigDetailId;
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
									args.detailId = record.data.bigDetailId;
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
	query();

	});
