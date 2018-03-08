Ext.onReady(function() {
			// 定义选择行
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : true
					});

			// grid中的数据Record
			var gridRecord = new Ext.data.Record.create([
				{
						name : 'bigRewardId',
						mapping : 0
					}, {
						name : 'bigRewardMonth',
						mapping : 1
					}, {
						name : 'bigAwardId',
						mapping : 2
					}, {
						name : 'bigAwardName',
						mapping : 3
					}, {
						name : 'bigRewardBase',
						mapping : 4
					},{
						name : 'handedDate',
						mapping : 5
					}, {
						name : 'fillBy',
						mapping : 6
					},{
						name : 'fillByName',
						mapping : 7
					},  {
						name : 'fillDate',
						mapping : 8
					}, {
						name : 'workFlowState',
						mapping : 9
					}, {
						name : 'workFlowNo',
						mapping : 10
					},{
						name : 'awardName',
						mapping : 11
					}]);

			// grid的store
			var queryStore = new Ext.data.JsonStore({
						url : 'hr/getBigReward.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : gridRecord
					});

			// 分页工具栏
			var pagebar = new Ext.PagingToolbar({
						pageSize : 18,
						store : queryStore,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					});

			// 页面的Grid主体
					

			var grid = new Ext.grid.GridPanel({
			        	id:'div_grid',
						region : 'north',
						layout : 'column',
						store : queryStore,
						height : 300,
						columns : [new Ext.grid.RowNumberer({
											header : '行号',
											width : 35
										}), {
									header : "ID",
									hidden : true,
									width : 100,
									dataIndex : 'bigRewardId'
								}, {
									header : "月份",
									sortable : true,
									width : 150,
									dataIndex : 'bigRewardMonth'
								}, {
									header : "大奖名称",
									sortable : true,
									width : 150,
									dataIndex : 'bigAwardName'
								}, {
									header : "大奖基数",
									sortable : true,
									width : 150,
									dataIndex : 'bigRewardBase'
								}, {
									header : "填写人",
									width : 200,
									dataIndex : 'fillByName'
								}, {
									header : "填写时间",
									width : 150,
									dataIndex : 'fillDate'
								}],
						tbar : [ {
									id : '审批处理',
									text : "审批",
									iconCls : 'add',
									handler : approve
								}],
						bbar : pagebar,
						sm : sm,
						frame : false,
						border : false,
						enableColumnMove : false
					});
 
			var bigRewardList = new bigReward.approveList();
		  function approve()
		  {
		  	var record = grid.getSelectionModel()
								.getSelected();
		   if(record==null)	
		   {
		   		Ext.MessageBox.alert('提示信息', '请选择一行记录！')
		   		return;
		   }
		  	tabs.setActiveTab(1);
		  	bigRewardList.setFormRec(record);
						
		  	
		  }
			grid.on('rowdblclick', function(grid, rowIndex, e) {
						var record = grid.getSelectionModel()
								.getSelected();
						bigRewardList.setFormRec(record);
						tabs.setActiveTab(1);
					});


			function query(start) {
				queryStore.baseParams = {
					workflowStatus : 1
				};
				queryStore.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}
			query();
			var tabs = new Ext.TabPanel({
						id : 'div_tabs',
						activeTab : 1,
						tabPosition : 'bottom',
						plain : false,
						defaults : {
							autoScroll : true
						},
						items : [{
									id : 'list',
									title : '大奖发放列表',
									items : grid,
									autoScroll : true,
									layout : 'fit'
								}, {
									id : 'approve',
									title : '大奖发放审批',
									autoScroll : true,
									items : bigRewardList.layout,
									layout : 'fit'
								}]
					});
			tabs.on('tabchange',function(tab,newtab){
				if(newtab.getId() == 'list'){
					queryStore.reload()
				}
			})
			var view = new Ext.Viewport({
						layout : 'fit',
						items : [tabs]
					});
			tabs.setActiveTab(0);

		});