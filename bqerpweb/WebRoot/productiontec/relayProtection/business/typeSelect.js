Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 继电保护类型名称
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 120,
		emptyText : '继电保护类型名称'
	})		
	

	var queryButton = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westgrids.load({
								params : {
									name : fuzzyText.getValue(),
									start : 0,
									limit : 18
								}
							});
				}
			});


	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([
	{
				name : 'protectTypeId'
			}, {
				name : 'protectTypeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findRelayProtectionList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 60
								}), {
							header : "继电保护类型编号",
							width : 140,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'protectTypeId'
						}, {
							header : "名称",
							width : 170,
							align : "center",
							sortable : false,
							dataIndex : 'protectTypeName'
						}],
				tbar : ['模糊查询 ：', fuzzyText, queryButton,
				{ text:'确定',
				  iconCls:'ok',
				  handler:selectRecord
				},
				{
				 text:'取消',
				 iconCls:'cancer',
				 handler:cancelHandler
				}],
				sm : westsm,
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
			
	westgrids.load({
				params : {
					start : 0,
					limit : 18,
					name : fuzzyText.getValue()
				}
			});
	westgrid.on('rowdblclick',selectRecord);
	
	function selectRecord()
	{
		if (westgrid.selModel.hasSelection()) {
		
			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				 
				var record = westgrid.getSelectionModel().getSelected();
		         var client=new Object();
		         client.protectTypeId=record.get("protectTypeId");
		         client.protectTypeName=record.get("protectTypeName");
		         window.returnValue=client;
		         window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择继电保护类型名称!");
		}
	}
	function cancelHandler(){
				var object = new Object();	
				window.returnValue = object;
				window.close();
			}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
});