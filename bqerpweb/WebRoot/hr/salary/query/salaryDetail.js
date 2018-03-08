Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var arg = window.dialogArguments;
var idparam = arg.ids;
var ids ='';
for(i=0;i<idparam.length;i++){
	ids = ids+idparam[i]+',';
}
ids=ids.substring(0,ids.length-1);
var month = arg.month;

Ext.onReady(function() {
	var grid;
	var ds ;
	// 创建一个对象
	var datalist = new Ext.data.Record.create([{
		// 人员id
		name : 'empId',
		mapping:0
	},{
		// 人员姓名
		name : 'empName',
		mapping:1
	},{
		// 薪点
		name : 'salaryPoint',
		mapping:2
	},{
		// 点值
		name : 'pointValue',
		mapping:3
	},{
		//薪点工资
		name : 'pointWage',
		mapping:4
	},{
		// 工龄
		name : 'workAge',
		mapping:5
	},{
		// 工龄工资
		name : 'ageWage',
		mapping:6
	},{
		// 运龄
		name : 'runningAge',
		mapping:7
	},{
		// 运行津贴
		name : 'operationWage',
		mapping:8
	},{
		// 加班情况
		name : 'overTime',
		mapping:9
	},{
		// 加班工资
		name : 'overtimeWage',
		mapping:0
	},{
		// 缺勤情况
		name : 'absence',
		mapping:10
	},{
		// 扣除工资
		name : 'deductionWage',
		mapping:11
	}])
	
	var wageStore = new Ext.data.JsonStore({
				url : 'com/getWageDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	
	var wageGrid = new Ext.grid.GridPanel({
				ds : wageStore,
				columns : [new Ext.grid.RowNumberer(),{
							header : "员工名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'empName'
						},{
							header : "薪点",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'salaryPoint'
						},{
							header : "点值",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'pointValue'
						},{
							header : "薪点工资",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'pointWage'
						},{
							header : "工龄",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'workAge'
						},{
							header : "工龄工资",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'ageWage'
						},{
							header : "运龄",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'runningAge'
						},{
							header : "运行津贴",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'operationWage'
						},{
							header : "加班情况",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'overTime'
						},{
							header : "加班工资",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'overtimeWage'
						},{
							header : "缺勤情况",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'absence'
						},{
							header : "扣除工资",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'deductionWage'
						}],
				viewConfig : {
			                 forceFit : true
		           },
				
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : wageStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
			
			wageStore.baseParams = {
				ids:ids,
				month:month
			}
		wageStore.load({
			params : {
				ids:ids,
				month:month,
				start : 0,
				limit : 18				
			}
		});
			//布局器面板
			var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [wageGrid]

						}]
			});
	
});