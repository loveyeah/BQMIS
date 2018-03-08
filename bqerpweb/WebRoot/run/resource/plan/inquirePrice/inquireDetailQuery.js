Ext.onReady(function() {
var gatherIds=window.dialogArguments.gatherIds;

	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'inquireDetailId'},
    {name : 'gatherId'},
	{name : 'inquireSupplier'},
	{name:'materialId'},
	{name:'materialName'},
	{name:'supplyName'},
	{name:'inquireQty'},
	{name:'unitPrice'},
	{name:'totalPrice'},
	{name:'qualityTime'},
	{name:'offerCycle'},
	{name:'specNo'},
	{name:'isSelectSupplier'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/findPlanInquireListForBill.action'
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
////分页
//	store.load({
//			params : {
//				start : 0,
//				limit : 18			
//			}
//		});
//	



	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		// region:"east",
		store : store,

		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, {
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'inquireDetailId',
			hidden:true
		},

		{
			header : "汇总ID",
			width : 75,
			sortable : true,
			dataIndex : 'gatherId',
			hidden:true
		},
		{
			header : "物料名称",
			width : 75,
			sortable : true,
			dataIndex : 'materialName',
			renderer:showColor
		},
		{
			header : "供应商",
			width : 75,
			sortable : true,
			dataIndex : 'supplyName',
			renderer:showColor
		},
		{
			header : "规格型号",
			width : 75,
			sortable : true,
			dataIndex : 'specNo',
			renderer:showColor
		},
		{
			header : "采购数量",
			width : 75,
			sortable : true,
			dataIndex : 'inquireQty',
			renderer:showColor
		},
		{
			header : "单价",
			width : 75,
			sortable : true,
			dataIndex : 'unitPrice',
			renderer:showColor
		},
		{
			header : "总价",
			width : 75,
			sortable : true,
			dataIndex : 'totalPrice',
			renderer:showColor
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


	
	 function showColor(value, cellmeta, record, rowIndex, columnIndex, store)
	 {
	 	if(value==null)
	 	{
	 	  return "";	
	 	}
	 	
	 	if(record.get("isSelectSupplier")=="Y")
	 	{
	 		return "<font color='red'>" + value + "</font>";
	 	}
	 	else
	 	{
	 	 return 	value;
	 	}
	 	
	 }
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	// -------------------

	// 查询
	function queryRecord() {
		
		store.baseParams = {
			gatherIds : gatherIds
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