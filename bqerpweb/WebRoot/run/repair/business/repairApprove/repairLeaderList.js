Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
            var tastlist;

			
			var MyRecord = Ext.data.Record.create([{
				name : 'tasklistId',
				mapping:0
			}, {
				name : 'tasklistYear',
				mapping:1
			}, {
				name : 'tasklistName',
				mapping:2
			}, {
				name : 'entryBy',
				mapping:3
			}, {
				name : 'entryByName',
				mapping:4
			}, {
				name : 'entryDate',
				mapping:5
			}]);

			var DataProxy = new Ext.data.HttpProxy({
						url : 'manageplan/getRepairTastListApprove.action'
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

			var editTime = new Ext.form.TextField({
				        id:'editTime',
						width : 120,
						readOnly : true
					});

			function query() {
				listStore.load({
							start : 0,
							limit : 18
						})
			}

			var listGrid = new Ext.grid.GridPanel({
						id:'div_grid',
						region : "center",
						layout : 'fit',
						store : listStore,
						listeners : {
							'rowdblclick' : function() {
								var rec = listGrid.getSelectionModel()
										.getSelected();
								tabs.setActiveTab(1);
								leaderApproveList.setTasklistId(rec.get("tasklistId"));
								leaderApproveList.setRepairDetailBtn(true);
							}
						},
						columns : [sm,new Ext.grid.RowNumberer({
											header : '行号',
											width : 35
										}), {
									header : "ID",
									hidden : true,
									width : 100,
									dataIndex : 'projectMainId'
								}, {
									header : "检修任务单",
									width : 300,
									dataIndex : 'tasklistName'
								}, {
									header : "填写人",
									width : 150,
									dataIndex : 'entryByName'
								}, {
									header : "填写日期",
									sortable : true,
									width : 150,
									dataIndex : 'entryDate'
								}],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						tbar : ["填写时间:", editTime,'-', {
									text : "查询",
									iconCls : 'query',
									minWidth : 70,
									handler : query
								}],
						// 分页
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : listStore,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								})
					});
            
			listGrid.on("rowclick", function() {

				if (listGrid.getSelectionModel().hasSelection()) {
					var rec = listGrid.getSelectionModel().getSelected();
					var entryDate=(rec.get("entryDate"));
					if (entryDate != null && entryDate != "") {
								var year = entryDate.substring(0, 4);
								var month = entryDate.substring(5, 7);
								if (month.substring(0, 1) == '0')
									month = entryDate.substring(6, 7);
								editTime.setValue(year + "年" + month + "月");
							}

				} else {
					entryDate.setValue(null);
				}
				
			})		
					
            query();	
            
			var leaderApproveList = new repairLeaderMaint.leaderApproveList();
			var tabs = new Ext.TabPanel({
						id:'div_tabs',
						activeTab : 0,
						tabPosition : 'bottom',
						plain : false,
						defaults : {
							autoScroll : true
						},
						items : [{
									id : 'repairList',
									title : '检修项目列表',
									items : listGrid,
									autoScroll : true,
									layout : 'fit'
								}, {
									id : 'repairApprove',
									title : '检修项目审批',
									autoScroll : true,
									items : leaderApproveList.grid,
									layout : 'fit'
								}]
					});
            
			var view = new Ext.Viewport({
						layout : 'fit',
						items : [tabs]
					});				
					
		});
