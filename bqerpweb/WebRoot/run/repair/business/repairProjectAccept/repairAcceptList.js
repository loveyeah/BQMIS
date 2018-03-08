Ext.onReady(function() {

			var projectName = new Ext.form.TextField({
						id : 'projectName',
						width : 120
					})
			// 定义选择行
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : true
					});

			// grid中的数据Record
			var gridRecord = new Ext.data.Record.create([{
						name : 'acceptId',
						mapping : 0
					}, {
						name : 'repairProjectName',
						mapping : 1
					}, {
						name : 'workingChargeName',
						mapping : 2
					}, {
						name : 'workingCharge',
						mapping : 3
					}, {
						name : 'workingMenbers',
						mapping : 4
					}, {
						name : 'startTime',
						mapping : 5
					}, {
						name : 'endTime',
						mapping : 6
					}, {
						name : 'workTime',
						mapping : 7
					}, {
						name : 'specialityName',
						mapping : 8
					}, {
						name : 'specialityId',
						mapping : 9
					}, {
						name : 'memo',
						mapping : 10
					}, {
						name : 'completion',
						mapping : 11
					}, {
						name : 'workflowNo',
						mapping : 13
					}]);

			// grid的store
			var queryStore = new Ext.data.JsonStore({
						url : 'manageplan/getRepairAcceptList.action',
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
			var gridOrder = new Ext.grid.GridPanel({
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
									dataIndex : 'acceptId'
								}, {
									header : "项目名称",
									sortable : true,
									width : 150,
									dataIndex : 'repairProjectName'
								}, {
									header : "工作负责人",
									sortable : true,
									width : 150,
									dataIndex : 'workingChargeName'
								}, {
									header : "工作成员",
									sortable : true,
									width : 150,
									dataIndex : 'workingMenbers'
								}, {
									header : "实际工作时间",
									width : 200,
									dataIndex : 'workTime'
								}, {
									header : "填写专业",
									width : 150,
									dataIndex : 'specialityName'
								}],
						tbar : [{
									text : '项目名称'
								}, projectName, '-', {
									id : 'btnSubmit',
									text : "查询",
									iconCls : 'query',
									handler : findFuzzy
								}],
						bbar : pagebar,
						sm : sm,
						frame : false,
						border : false,
						enableColumnMove : false
					});

			var approveList = new repairMaint.approveList();
			gridOrder.on('rowdblclick', function(grid, rowIndex, e) {
						var record = gridOrder.getSelectionModel()
								.getSelected();
						approveList.setFormRec(record);
						tabs.setActiveTab(1);
					});

			// new Ext.Viewport({
			// layout : 'fit',
			// margins : '0 0 0 0',
			// border : false,
			// items : [gridOrder]
			// });

			function findFuzzy(start) {
				queryStore.baseParams = {
					projectName : projectName.getValue(),
					workflowStatus : 0,
					isFillBy : 1
				};
				queryStore.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}
			findFuzzy();
			var tabs = new Ext.TabPanel({
						id : 'div_tabs',
						activeTab : 1,
						tabPosition : 'bottom',
						plain : false,
						defaults : {
							autoScroll : true
						},
						items : [{
									id : 'repairList',
									title : '项目验收列表',
									items : gridOrder,
									autoScroll : true,
									layout : 'fit'
								}, {
									id : 'repairApprove',
									title : '项目验收填写',
									autoScroll : true,
									items : approveList.grid,
									layout : 'fit'
								}]
					});
			tabs.on('tabchange',function(tab,newtab){
				if(newtab.getId() == 'repairList'){
					queryStore.reload()
				}
			})
			var view = new Ext.Viewport({
						layout : 'fit',
						items : [tabs]
					});
			tabs.setActiveTab(0);

		});