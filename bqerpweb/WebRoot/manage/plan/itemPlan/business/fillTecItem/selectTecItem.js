Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			var args = window.dialogArguments;
			var con_item = Ext.data.Record.create([{
						name : 'technologyItemId',
						mapping : 0
					}, {
						name : 'itemCode',
						mapping : 1
					}, {
						name : 'itemName',
						mapping : 2
					}, {
						name : 'alias',
						mapping : 3
					}, {
						name : 'unitId',
						mapping : 4
					}, {
						name : 'displayNo',
						mapping : 5
					}, {
						name : 'unitName',
						mapping : 8
					}]);

			var con_sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false
					});
			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'manageitemplan/getTecItemList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : "list"
								}, con_item)
					});
			con_ds.load()

			var con_item_cm = new Ext.grid.ColumnModel([con_sm,

					new Ext.grid.RowNumberer(), {
						header : '指标别名', 
						width : 250,
						dataIndex : 'alias',
						align : 'center'
					}/*, {
						header : '计量单位',
						dataIndex : 'unitId',
						hidden : true,
						align : 'center'
					}, {
						header : '指标编码',
						dataIndex : 'itemCode',
						align : 'center'
					}*/, {
						
						header : '指标名称',
						width : 200,
						dataIndex : 'itemName',
						align : 'center'
					}, {
						header : '计量单位',
						dataIndex : 'unitName',
						align : 'center'
					}, {
						header : '显示顺序',
						dataIndex : 'displayNo',
						align : 'center'
					}]);

			function addTopic() {
				var records = Grid.getSelectionModel().getSelections();
				var recordslen = records.length;
				if (recordslen > 0) {
					var temparr = new Array();
					for (var i = 0; i <= recordslen - 1; i++) {
						temparr.push(records[i].data)
					}
					window.returnValue = temparr;
					window.close()
				}
			}

			var contbar = new Ext.Toolbar({
						items : [{
									id : 'btnAdd',
									iconCls : 'add',
									text : "确定",
									handler : addTopic

								}]

					});
			var Grid = new Ext.grid.GridPanel({
						sm : con_sm,
						ds : con_ds,
						cm : con_item_cm,
						height : 425,
						autoScroll : true,
						tbar : contbar,
						border : true,
						viewConfig : {
							forceFit : true
						}
					});

			if (typeof(args) != "undefined" && typeof(args) == 'object') {
				contbar.setDisabled(true)
				Grid.on("click", function() {
							var rec = Grid.selModel.getSelected();
							window.returnValue = rec;
							window.close();
						})
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

		})