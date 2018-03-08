Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'intervalId'},
    {name : 'beginDate'},
	{name : 'endDate'},
	{name : 'evaluationDays'},
	{name : 'memo'},
	{name : 'recordBy'},
	{name : 'recordDate'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'clients/findClientIntervalList.action'
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
				limit : 10				
			}
		});
	



	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect:true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'intervalId',
			hidden:true
		},
		{
			header : "评价周期开始时间",
			width : 75,
			sortable : true,
			dataIndex : 'beginDate',
			renderer:function(value)
			{
				return value.substring(0,10);
			}
		},
		{
			header : "评价周期结束时间",
			width : 75,
			sortable : true,
			dataIndex : 'endDate',
			renderer:function(value)
			{
				return value.substring(0,10);
			}
		},
		{
			header : "评分天数",
			width : 75,
			sortable : true,
			dataIndex : 'evaluationDays'
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("rowdblclick", updateRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});


	
	
	

	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				 
				var record = grid.getSelectionModel().getSelected();
		        var obj=new Object();
		        obj.beginDate=record.get("beginDate").substring(0,10);
		        obj.endDate=record.get("endDate").substring(0,10);
		        obj.intervalId=record.get("intervalId");
		        obj.evaluationDays=record.get("evaluationDays");//modify by ywliu 2009/6/25
		        window.returnValue=obj;
		        window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	

	
	
});