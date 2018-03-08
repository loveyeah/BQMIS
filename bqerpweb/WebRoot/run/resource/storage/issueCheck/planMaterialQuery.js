Ext.onReady(function() {
var orderNo=window.dialogArguments.orderNo;
var flag=window.dialogArguments.flag;
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
	{name:'factory'},
	{name:'wfNo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/findMaterialDetailByPurOrIssue.action'
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

		// 查看流程按钮
	var btnView = new Ext.Toolbar.Button({
		text : "流程",
		iconCls : 'view',
		handler : function() {

			if (grid.selModel.hasSelection()) {
				var record = grid.getSelectionModel().getSelected();
				var entryId = record.get("wfNo");
				var planSource = record.get("planOriginalId");
				var flowCode = getFlowCode(planSource);
				if (flowCode == "") {
					Ext.Msg.alert("提示", "该流程不存在！");
					return;
				}
				if (entryId == null || entryId == "") {
					url = application_base_path + "workflow/manager/flowshow/flowshow.jsp?flowCode="
							+ flowCode;
				} else {
					url = application_base_path+"workflow/manager/show/show.jsp?entryId="
							+ entryId;
				}
				window.open(url);
			} else {
				Ext.MessageBox.alert("提示", "请选择要查看的记录");
			}
		}

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
        tbar:[btnView],
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
			orderNo : orderNo,
			flag:flag
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