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
	
	var contractName = new Ext.form.TextField({
		fieldLabel:'合同名称'
	})
//查询按钮
var queryButton = new Ext.Button({
	id:'query',
	text:'查询',
	iconCls:'query',
	handler:function(){
			store.baseParams = {
				flag:'contract',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemCode:itemCode,
				contractName:contractName.getValue()
			}
		store.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	
})


var datalist = new Ext.data.Record.create([{
		// 合同编码
		name : 'conttreesNo',
		mapping:0
	},{
		//合同名称
		name : 'contractName',
		mapping:1
	},{
		//总金额
		name : 'actAmount',
		mapping:2
	},{
		//费用来源
		name : 'itemName',
		mapping:3
	},{
		// 经办人
		name : 'operateBy',
		mapping:4
	},{
		// 合同开始时间
		name : 'startDate',
		mapping:5
	},{
		//合同结束时间
		name : 'endDate',
		mapping:6
	}])
	
	var store = new Ext.data.JsonStore({
				url : 'managebudget/findFeeDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var grid = new Ext.grid.GridPanel({
				sm:sm,
				ds : store,
				columns : [sm,new Ext.grid.RowNumberer(),{
							header : "合同编码",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'conttreesNo'
						},{
							header : "合同名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'contractName'
						},{
							header : "总金额",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'actAmount'
						},{
							header : "费用来源",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'itemName'
						},{
							header : "经办人",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'operateBy'
						},{
							header : "签订时间",
							width : 75,
							align : "itemName",
							sortable : true,
							dataIndex : '',
							renderer:function(value, cellmeta, record, rowIndex,
		                            columnIndex, store){
								var sign_time = record.get('startDate')+'~'+record.get('endDate');
								return sign_time;
							}
							
						}],
				viewConfig : {
			                 forceFit : true
		           },
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				tbar:['合同名称：',contractName,'签订时间：',start_date,'~',end_date,queryButton] ,
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	function queryRecord(){
		store.baseParams = {
				flag:'contract',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemCode:itemCode,
				contractName:contractName.getValue()
			}
		store.load({
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
							items : [grid]

						}]
			});
			
});