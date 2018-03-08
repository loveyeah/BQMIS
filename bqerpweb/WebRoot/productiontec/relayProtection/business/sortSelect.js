Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'sylbId'},
    {name : 'sylbName'},
	{name : 'displayNo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findExperimentSortList.action'
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
//分页
	store.load({
			params : {
				start : 0,
				limit : 18				
			}
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
									dataIndex : 'sylbId',
									hidden : true
								},
		{
			header : "类别名称",
			width : 75,
			sortable : true,
			dataIndex : 'sylbName'
		},

		{
			header : "显示顺序",
			width : 75,
			sortable : true,
			dataIndex : 'displayNo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [
				{ text:'确定',
				  iconCls:'ok',
				  handler:selectRecord
				},
				{
				 text:'取消',
				 iconCls:'cancer',
				 handler:cancelHandler
				}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("rowdblclick", selectRecord);
function selectRecord()
	{
		if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				 
				var record = grid.getSelectionModel().getSelected();
		         var client=new Object();
		         client.sylbId=record.get("sylbId");
		         client.sylbName=record.get("sylbName");
		         client.displayNo=record.get("displayNo");
		         window.returnValue=client;
		         window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择试验类型名称!");
		}
	}
	function cancelHandler(){
				var object = new Object();	
				window.returnValue = object;
				window.close();
			}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});
	
});