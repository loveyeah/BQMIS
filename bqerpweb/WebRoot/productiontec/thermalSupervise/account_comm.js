Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			var arg = window.dialogArguments;
			// 取得监督专业id

			
			
			var jdzyId =  arg.jdzyId ;

			var westsm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
			// 左边列表中的数据
			var datalist = new Ext.data.Record.create([

			{
						name : 'model.regulatorId'
					}, {
						name : 'model.names'
					}, {
						name : 'model.yqyblbId'
					}, {
						name : 'model.yqybdjId'
					}, {
						name : 'model.yqybjdId'
					}, {
						name : 'model.userRange'
					}, {
						name : 'model.sizes'
					}, {
						name : 'model.testCycle'
					}, {
						name : 'model.factory'
					}, {
						name : 'model.outFactoryDate'
					}, {
						name : 'model.outFactoryNo'
					}, {
						name : 'model.buyDate'
					}, {
						name : 'model.useDate'
					}, {
						name : 'model.depCode'
					}, {
						name : 'model.charger'
					}, {
						name : 'model.ifUsed'
					}, {
						name : 'model.memo'
					}, {
						name : 'model.lastCheckDate'
					}, {
						name : 'model.nextCheckDate'
					}, {
						name : 'model.jdzyId'
					}, {
						name : 'depName'

					}, {
						name : 'chargerName'
					}, {
						name : 'nameOfSort'
					}]);

			var westgrids = new Ext.data.JsonStore({
						url : 'productionrec/findAccountByNames.action',
						root : 'list',
						totalCount : 'totalCount',
						fields : datalist
					});

			var query = new Ext.form.TextField({
						id : 'argFuzzy',
						fieldLabel : "模糊查询",
						hideLabel : false,
						emptyText : '仪器名称..',
						name : 'argFuzzy',
						width : 150,
						value : ''
					});
			function fuzzyQuery() {
				westgrids.load({
							params : {
								start : 0,
								limit : 18,
								fuzzy : query.getValue(),
								jdzyId : jdzyId
							}
						});
			};
			fuzzyQuery();

			// 查询时Enter
			document.onkeydown = function() {
				if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
					fuzzyQuery();

				}
			}
			var querybtn = new Ext.Button({
						iconCls : 'query',
						text : '查询',
						handler : function() {
							fuzzyQuery();
						}
					})
			var confirm = new Ext.Button({
						iconCls : 'confirm',
						text : '确定',
						handler : function() {
							chooseAccount();
						}
					})
			// 左边列表
			var westgrid = new Ext.grid.GridPanel({
						ds : westgrids,
						columns : [westsm, new Ext.grid.RowNumberer(), {
									header : "仪表名称",

									// align : "center",
									sortable : true,

									dataIndex : 'model.names'
								}, {
									header : "类别",

									sortable : false,

									dataIndex : 'nameOfSort'
								}],
						tbar : [query, querybtn],
						sm : westsm,
						viewConfig : {
							forceFit : true
						},
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : westgrids,
									displayInfo : true,
									displayMsg : "{0} 到 {1} /共 {2} 条",
									emptyMsg : "没有记录"
								}),
						border : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});

			function chooseAccount() {
				var rec = westgrid.getSelectionModel().getSelected();
				if (rec) {
					var obj = {
						regulatorId : rec.get("model.regulatorId"),
						names : rec.get("model.names"),
						testCycle:rec.get("model.testCycle")

					}

					window.returnValue = obj;
					window.close();
				}

			}
			// westgrid 的事件
			westgrid.on('rowdblclick', chooseAccount);

			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									title : "",
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '1 0 1 1',
									split : true,
									collapsible : true,

									items : [westgrid]

								}]
					});
		});