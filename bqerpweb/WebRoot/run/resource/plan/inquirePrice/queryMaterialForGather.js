Ext.onReady(function() {
var gatherId=window.dialogArguments.gatherId;

	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'requirementDetailId'},
    {name : 'materialNo'},
	{name : 'materialName'},
	{name:'materSize'},
	{name:'appliedQty'},
	{name:'approvedQty'},
	{name:'stockUmName'},
	{name:'applyByName'},
	{name:'applyDeptName'},
	{name:'applyReason'},
	{name:'planOriginalId'},
	{name:'estimatedPrice'},
	{name:'dueDate'},
	{name:'supplier'},
	{name:'factory'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/findMaterialDetailByGatherId.action'
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
			dataIndex : 'requirementDetailId',
			hidden:true
		},

		{
			header : "物资编码",
			width : 75,
			sortable : true,
			dataIndex : 'materialNo'
		},
		{
			header : "物料名称",
			width : 80,
			sortable : true,
			dataIndex : 'materialName'
		},
		{
			header : "规格型号",
			width : 60,
			sortable : true,
			dataIndex : 'materSize'
		},
		{
			header : "计量单位",
			width : 60,
			sortable : true,
			dataIndex : 'stockUmName'
		},
			{
			header : "估计单价",
			width : 75,
			sortable : true,
			dataIndex : 'estimatedPrice'
		},
		{
			header : "生产厂家",
			width : 75,
			sortable : true,
			dataIndex : 'factory'
		},
		{
			header : "申请人",
			width : 60,
			sortable : true,
			dataIndex : 'applyByName'
		},
		{
			header : "申请单位",
			width : 75,
			sortable : true,
			dataIndex : 'applyDeptName'
		},
		{
			header : "申请数量",
			width : 60,
			sortable : true,
			dataIndex : 'appliedQty'
		},
		{
			header : "核准数量",
			width : 60,
			sortable : true,
			dataIndex : 'approvedQty'
		},
		{
			header : "申请理由",
			width : 75,
			sortable : true,
			dataIndex : 'applyReason'
		},
		{
			header : "计划种类",
			width : 90,
			sortable : true,
			dataIndex : 'planOriginalId',
			renderer : function(value) {
						return planOriginalName(value);
					}
		},
	
		{
			header : "需求日期",
			width : 75,
			sortable : true,
			dataIndex : 'dueDate'
		},
		{
			header : "建议供应商",
			width : 75,
			sortable : true,
			dataIndex : 'supplier'
		}
		],
		sm : sm,
		autoSizeColumns : true,
//		viewConfig : {
//			forceFit : true
//		},

		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});


	
	
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
			gatherId : gatherId
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