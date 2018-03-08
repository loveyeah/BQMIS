Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var arg = window.dialogArguments;
	var itemId = arg.itemId;
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
	
	var wwName = new Ext.form.TextField({
		fieldLabel:'外委名称'
	})
	var applyBy = new Ext.form.TextField({
		fieldLabel:'申请人'
	})
	
	var ApproveQuery = new Ext.Button({
		text:'审批查询',
		handler:function(){
			var records = grid.getSelectionModel().getSelections();
			
			if(records.length!=1){
				Ext.Msg.alert('提示','请选择一条记录查看！');
			}else{
				var record = grid.getSelectionModel().getSelected();
				var url = "/power/workflow/manager/show/show.jsp?entryId="+record.data.entryId;
				window.open(url);
			}
			
		}
	
	})
//查询按钮
var queryButton = new Ext.Button({
	id:'query',
	text:'查询',
	iconCls:'query',
	handler:function(){
			store.baseParams = {
				flag:'ww',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemId:itemId,
				wwName:wwName.getValue(),
				applyBy:applyBy.getValue()
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
		// 外委名称
		name : 'assignName',
		mapping:0
	},{
		//费用来源
		name : 'itemName',
		mapping:1
	},{
		//估算金额
		name : 'estimateMoney',
		mapping:2
	},{
		//用途
		name : 'assignFunction',
		mapping:3
	},{
		// 申请人
		name : 'applyBy',
		mapping:4
	},{
		// 工作流编号
		name : 'entryId',
		mapping:4
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
							header : "外委名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'assignName'
						},{
							header : "费用来源",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'itemName'
						},{
							header : "估算金额",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'estimateMoney'
						},{
							header : "用途",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'assignFunction'
						},{
							header : "申请人",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'applyBy'
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
				tbar:['外委名称：',wwName,'申请人：',applyBy,'申请时间：',start_date,'~',end_date,queryButton,'-',ApproveQuery] ,
				border : true,
				iconCls : 'icon-grid'
			});
	function queryRecord(){
		store.baseParams = {
				flag:'ww',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemId:itemId,
				wwName:wwName.getValue(),
				applyBy:applyBy.getValue()
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