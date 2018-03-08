Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var arg = window.dialogArguments;
	var itemCode = arg.itemCode;
//开始时间
var start_date = new Ext.form.TextField({
		id : 'start_date',
		fieldLabel : '开始时间',
		style : 'cursor:pointer',
		name:'model.startDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		},
		value:new Date().getYear()+'-01-01'
	})
	
//结束时间
	var end_date = new Ext.form.TextField({
		id : 'end_date',
		fieldLabel : '结束时间',
		style : 'cursor:pointer',
		name:'model.endDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		},
		value:new Date().getYear()+'-12-31'
	})
	
	var materialName = new Ext.form.TextField({
		fieldLabel:'物资名称'
	})
//查询按钮
var queryButton = new Ext.Button({
	id:'query',
	text:'查询',
	iconCls:'query',
	handler:function(){
			llStore.baseParams = {
				flag:'ll',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemCode:itemCode,
				wlName:materialName.getValue()
			}
		llStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	
})


var datalist = new Ext.data.Record.create([{
		// 交易号
		name : 'transId',
		mapping:0
	},{
		//流水单号
		name : 'orderNo',
		mapping:1
	},{
		//领料费用
		name : 'llFee',
		mapping:2
	},{
		//物料编码
		name : 'materialNo',
		mapping:3
	},{
		// 物料名称
		name : 'materialName',
		mapping:4
	},{
		// 规格型号
		name : 'specNo',
		mapping:5
	},{
		//申请数量
		name : 'appliedCount',
		mapping:6
	},{
		//实际数量
		name : 'actCount',
		mapping:7
	},{
		//费用来源
		name : 'itemName',
		mapping:8
	},{
		//领料人
		name : 'llBy',
		mapping:9
	},{
		//领料时间
		name : 'llDate',
		mapping:10
	}])
	
	var llStore = new Ext.data.JsonStore({
				url : 'managebudget/findFeeDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var llGrid = new Ext.grid.GridPanel({
				sm:sm,
				ds : llStore,
				columns : [sm,new Ext.grid.RowNumberer(),{
							header : "物资编码",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'materialNo'
						},{
							header : "物资名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'materialName'
						},{
							header : "型号规格",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'specNo'
						},{
							header : "申请数量",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'appliedCount'
						},{
							header : "实发数量",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'actCount'
						},{
							header : "费用来源",
							width : 75,
							align : "itemName",
							sortable : true,
							dataIndex : 'endDate'
						},{
							header : "领料人",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'llBy'
						},{
							header : "领料时间",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'llDate'
						}],
				viewConfig : {
			                 forceFit : true
		           },
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : llStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				tbar:['物资名称：',materialName,'领料时间：',start_date,'~',end_date,queryButton] ,
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	function queryRecord(){
		llStore.baseParams = {
				flag:'ll',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemCode:itemCode,
				wlName:materialName.getValue()
			}
		llStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}

			queryRecord()
	
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
							items : [llGrid]

						}]
			});
			
});