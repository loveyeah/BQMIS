Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

			var label1 = new Ext.form.Label({
						text : '名称：'
					})
			var queryName = new Ext.form.TextField({
						id : 'queryName'
					})
			var label2 = new Ext.form.Label({
						text : '规格型号：'
					})
			var queryModel = new Ext.form.TextField({
						id : 'queryModel'
					})

			var label3 = new Ext.form.Label({
						text : '类别：'
					})
			// 类别
			var typeStoreNew = new Ext.data.SimpleStore({
						fields : ['value', 'text'],
						data : [['1', '电气安全用具'], ['2', '电动工具'], ['3', '安全带'],
								['0', '全部']]
					})
			var toolTypeNew = new Ext.form.ComboBox({
						id : 'toolTypeNew',
						hiddenName : 'hToolTypeNew',
						store : typeStoreNew,
						displayField : 'text',
						valueField : 'value',
						mode : 'local',
						triggerAction : 'all',
						selectOnFocus : true,
						value : 0,
						fieldLabel : '类别',
						anchor : '90%'
					})

			var queryBtu = new Ext.Button({
						id : 'queryBtu',
						text : '查询',
						iconCls : 'query',
						handler : queryFun
					})
			function queryFun() {
				toolStore.load({
							params : {
								start : 0,
								limit : 18,
								toolName : queryName.getValue(),
								toolModel : queryModel.getValue(),
								toolType : toolTypeNew.getValue()

							}
						})
			}
			var tbar = new Ext.Toolbar({
						items : [label1, queryName, label2, queryModel, label3,
								toolTypeNew, queryBtu]
					})
			var ToolRecord = new Ext.data.Record.create([{
						name : 'toolId',
						mapping : 0
					}, {
						name : 'toolCode',
						mapping : 1
					}, {
						name : 'toolName',
						mapping : 2
					}, {
						name : 'toolType',
						mapping : 3
					}, {
						name : 'toolModel',
						mapping : 4
					}, {
						name : 'factoryDate',
						mapping : 5
					}, {
						name : 'memo',
						mapping : 6
					}, {
						name : 'chargeBy',
						mapping : 7
					}, {
						name : 'chargeName',
						mapping : 8
					}])

			var toolStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'security/findToolsByCondi.action'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list',
									totalProperty : 'totalCount'
								}, ToolRecord)

					})
			var sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false
					})

			var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
								header : '行号',
								width : 35
							}), {
						header : '编号',
						dataIndex : 'toolCode'
					}, {
						header : '名称',
						dataIndex : 'toolName'
					}, {
						header : '规格型号',
						dataIndex : 'toolModel'
					}, {
						header : '类别',
						dataIndex : 'toolType',
						renderer : function(v) {
							if (v == 1)
								return '电气安全用具';
							else if (v == 2)
								return '电动工具';
							else if (v == 3)
								return '安全带';
							else
								return '';
						}
					}, {
						header : '出厂日期',
						dataIndex : 'factoryDate'
					}, {
						header : '备注',
						dataIndex : 'memo'
					}])

			var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : toolStore,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					})
			var grid = new Ext.grid.GridPanel({
						id : 'grid',
						frame : 'true',
						border : false,
						layout : 'fit',
						autoScroll : true,
						sm : sm,
						store : toolStore,
						cm : cm,
						tbar : tbar,
						bbar : bbar
					})

			new Ext.Viewport({
						id : 'viewPort',
						layout : 'fit',
						items : [grid]
					})
			queryFun();
		})