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
	
	var reportBy = new Ext.form.TextField({
		fieldLabel:'报销人'
	})
	
//查询按钮
var queryButton = new Ext.Button({
	id:'query',
	text:'查询',
	iconCls:'query',
	handler:function(){
			store.baseParams = {
				flag:'zb',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemId:itemId,
				reportBy:reportBy.getValue()
			}
		store.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	
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

var datalist = new Ext.data.Record.create([{
		//报销金额
		name : 'reportMoney',
		mapping:0
	},{
		//用途
		name : 'reportUse',
		mapping:1
	},{
		//备注
		name : 'memo',
		mapping:2
	},{
		//报销人
		name : 'reportBy',
		mapping:3
	},{
		// 报销时间
		name : 'reportDate',
		mapping:4
	},{
		// 审批状态
		name : 'workFlowStatus',
		mapping:5
	},{
		//工作流编号
		name : 'entryId',
		mapping:5
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
							header : "报销金额",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'reportMoney'
						},{
							header : "用途",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'reportUse'
						},{
							header : "备注",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'memo'
						},{
							header : "报销人",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'reportBy'
						},{
							header : "报销时间",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'reportDate'
						},{
							header : "审批状态",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'workFlowStatus',
							renderer:function(v){
								if(v=='0'){
									return '已填写';
								}else if(v=='1'){
									return '已上报';
								}else if(v=='2'){
									return '部门主任已审核';
								}
								else if(v=='3'){
									return '部门主任已审核';
								}
								else if(v=='4'){
									return '部门主任已审核';
								}
								else if(v=='5'){
									return '生产厂长已审核';
								}
								else if(v=='6'){
									return '经营厂长已审核';
								}else if(v=='7'){
									return '经营厂长/厂长已审核';
								}
								else if(v=='8'){
									return '厂长已审核';
								}
								else if(v=='9'){
									return '财务审核';
								}else if(v=='10'){
									return '已退回';
								}
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
				tbar:['报销人：',reportBy,'报销时间：',start_date,'~',end_date,queryButton,'-',ApproveQuery] ,
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	function queryRecord(){
		store.baseParams = {
				flag:'zb',
				startTime:start_date.getValue(),
				endTime:end_date.getValue(),
				itemId:itemId,
				reportBy:reportBy.getValue()
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