Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			// 定义grid
			var monthDate = window.dialogArguments.rootNode.monthDate;
			var problemKind = window.dialogArguments.rootNode.problemKind;
			var deptCode = window.dialogArguments.rootNode.deptCode;

			var MyRecord = Ext.data.Record.create([{
						name : 'mainId',
						mapping : 0
					}, {
						name : 'year',
						mapping : 1
					}, {
						name : 'season',
						mapping : 2
					}, {
						name : 'detailId',
						mapping : 3
					}, {
						name : 'existQuestion',
						mapping : 4
					}, {
						name : 'wholeStep',
						mapping : 5
					}, {
						name : 'avoidStep',
						mapping : 6
					}, {
						name : 'planDate',
						mapping : 7
					}, {
						name : 'actualDate',
						mapping : 8
					}, {
						name : 'dutyDeptCode',
						mapping : 9
					}, {
						name : 'dutyDeptName',
						mapping : 10
					}, {
						name : 'dutyBy',
						mapping : 11
					}, {
						name : 'dutyName',
						mapping : 12
					}, {
						name : 'superDeptCode',
						mapping : 13
					}, {
						name : 'superDeptName',
						mapping : 14
					}, {
						name : 'superBy',
						mapping : 15
					}, {
						name : 'superName',
						mapping : 16
					}, {
						name : 'noReason',
						mapping : 17
					}, {
						name : 'issueProerty',
						mapping : 18
					}, {
						name : 'noFinishNum',
						mapping : 19
					}, {
						name : 'finishNum',
						mapping : 20
					}, {
						name : 'totalNum',
						mapping : 21
					}, {
						name : 'percent',
						mapping : 22
					}, {
						name : 'isFinish',
						mapping : 23
					}]);

			var dataProxy = new Ext.data.HttpProxy(

			{
						url : 'security/findMatterGatherQuery.action'
					}

			);

			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"

					}, MyRecord);

			var store = new Ext.data.Store({

						proxy : dataProxy,

						reader : theReader

					});

			var sm = new Ext.grid.CheckboxSelectionModel();

			var grid = new Ext.grid.GridPanel({
						region : "center",
						layout : 'fit',
						store : store,

						columns : [sm, new Ext.grid.RowNumberer({
											header : '序号',
											width : 50
										}), {

									header : "ID",
									width : 75,
									sortable : true,
									dataIndex : 'detailId',
									hidden : true
								}, {
									header : "ID",
									width : 75,
									sortable : true,
									hidden : true,
									dataIndex : 'mainId'

								}, {
									header : "存在问题",
									width : 200,
									sortable : true,
									dataIndex : 'existQuestion',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								},

								{
									header : "整改计划",
									width : 200,
									sortable : true,
									dataIndex : 'wholeStep',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "计划完成时间",
									width : 110,
									sortable : true,
									dataIndex : 'planDate',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								},  {
									header : "整改责任人",
									width : 110,
									sortable : true,
									dataIndex : 'dutyName',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "整改责任部门",
									width : 120,
									sortable : true,
									dataIndex : 'dutyDeptName',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "整改监督人",
									width : 100,
									sortable : true,
									dataIndex : 'superName',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "整改监督部门",
									width : 120,
									sortable : true,
									dataIndex : 'superDeptName',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "问题性质",
									width : 75,
									sortable : true,
									dataIndex : 'issueProerty',
									renderer : function(v, cellmeta, record,
											rowIndex, columnIndex, store) {
										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'
										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'
										if (v == '1') {
											return "一般性质";
										} else if (v == '2') {
											return "重大性质";
										} else {
											return v;
										}
									}

								}, {
									header : "整改前防范措施",
									width : 80,
									sortable : true,
									dataIndex : 'avoidStep',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								},  {
									header : "未整改原因",
									width : 80,
									sortable : true,
									dataIndex : 'noReason',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {

										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}, {
									header : "整改完成时间",
									width : 80,
									sortable : true,
									dataIndex : 'actualDate',
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (record.get('isFinish') == '3')
											cellmeta.attr = 'style=color:"green"'

										else if (record.get('isFinish') == '2')
											cellmeta.attr = 'style=color:"red"'

										return value;
									}
								}],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : store,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								})
					});

			// ---------------------------------------

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						// layout : "fit",
						items : [grid]
					});

			// 查询

			function queryRecord() {

				store.baseParams = {
					monthDate : monthDate,
					problemKind : problemKind,
					deptCode : deptCode
				};
				store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}

			queryRecord();

		});