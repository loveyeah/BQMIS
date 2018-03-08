Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var budgetItemId = getParameter("budgetItemId");
			var changeId = getParameter("changeId");

			var Myrecord = new Ext.data.Record.create([{
						name : 'centerName'
					}, {
						name : 'topicName'
					}, {
						name : 'budgetTime'
					}, {
						name : 'itemName'
					}, {
						name : 'originBudget'
					}, {
						name : 'newBudget'
					}, {
						name : 'budgetChange'
					}, {
						name : 'changeReason'
					}, {
						name : 'changeDate'
					}, {
						name : 'changeName'
					}, {
						name : 'btnChange'
					}, {
						name : 'changeStatus'
					}]);

			var store = new Ext.data.JsonStore({
						url : 'managebudget/getBudgetModifyByItemId.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : Myrecord
					});
			var sm = new Ext.grid.CheckboxSelectionModel();

			// 左边列表
			var grid = new Ext.grid.GridPanel({
						split : true,
						autoScroll : true,
						ds : store,
						columns : [new Ext.grid.RowNumberer({
											header : "行号",
											width : 31
										}), {
									header : "预算部门",
									width : 120,
									align : "center",
									sortable : true,
									dataIndex : 'centerName'
								}, {
									header : "预算主题",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'topicName'
								}, {
									header : "预算时间",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'budgetTime'
								}, {
									header : "预算指标",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'itemName'
								}, {
									header : "原预算值",
									width : 60,
									align : "center",
									sortable : true,
									dataIndex : 'originBudget'
								}, {
									header : "现预算值",
									width : 60,
									align : "center",
									sortable : true,
									dataIndex : 'newBudget'
								}, {
									header : "变更方式",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'btnChange',
									renderer : function(v) {
										if (v > 0)
											return '变更增加';
										else if (v < 0)
											return '变更减少';
									}
								}, {
									header : "预算变更",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'budgetChange'
								}, {
									header : "变更状态",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'changeStatus',
									renderer : function(v) {
										if (v == 2)
											return '已确认';
										else if (v == 0)
											return '未确认';
									}
								}, {
									header : "变更事由",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'changeReason'
								}, {
									header : "变更申请人",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'changeName'
								}, {
									header : "变更时间",
									width : 70,
									align : "center",
									sortable : true,
									dataIndex : 'changeDate'
								}],
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : store,
									displayInfo : true,
									displayMsg : "共 {2} 条",
									emptyMsg : "没有记录"
								}),
						border : true,
						sm : sm
					});
			function queryData() {
				store.baseParams = {
					budgetItemId : budgetItemId
				};
				store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
				store.rejectChanges();

			}
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									region : "center",
									layout : 'fit',
									collapsible : true,
									split : true,
									border : false,
									margins : '0 0 0 0',
									// 注入表格
									items : [grid]
								}]
					})
			queryData();
		})
