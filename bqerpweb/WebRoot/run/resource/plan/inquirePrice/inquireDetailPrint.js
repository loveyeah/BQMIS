Ext.onReady(function() {
var gatherIds=window.dialogArguments.gatherIds;
var workerCode="";
var workerName = ""
var officeTel1= '';
var officeTel2= '';
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
	{name:'specNo'}
	// add by liuyi 20100406 
	,{name:'sbMemo'},
	{name:'sbDeptName'}
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
			dataIndex : 'materialName'
		},
		{
			header : "供应商",
			width : 75,
			sortable : true,
			dataIndex : 'supplyName'
		},
		{
			header : "规格型号",
			width : 75,
			sortable : true,
			dataIndex : 'specNo'
		},
		{
			header : "采购数量",
			width : 75,
			sortable : true,
			dataIndex : 'inquireQty'
		},
		{
			header : "单价",
			width : 75,
			sortable : true,
			dataIndex : 'unitPrice'
		},
		{
			header : "总价",
			width : 75,
			sortable : true,
			dataIndex : 'totalPrice'
		}
		// add by liuyi 20100406
		,
		{
			header : "需求备注",
			sortable : true,
			hidden : true,
			dataIndex : 'sbMemo'
		},
		{
			header : "申报部门",
			sortable : true,
			hidden : true,
			dataIndex : 'sbDeptName'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		}
		//modified by liuyi 091127 去掉分页
//		,
//		//分页
//		bbar : new Ext.PagingToolbar({
//			pageSize : 18,
//			store : store,
//			displayInfo : true,
//			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//			emptyMsg : "没有记录"
//		})
	});

grid.on("dblclick", printRecord);
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
		store.load(
		// modified by liuyi 091127 取消分页
//			{
//			params : {
//				start : 0,
//				limit : 18
//			}
//		}
		);
	}
	queryRecord();
	
	function printRecord()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (grid.selModel.hasSelection()) {
		 var data= selected[0];
		 var detailId=data.get("inquireDetailId");
		 var supplyName = data.get("supplyName");
//		 alert(detailId);
//		 alert(gatherIds);
//	     alert(workerName);
		 strReportAdds="/powerrpt/report/webfile/InquirePrice.jsp?detailId="+detailId+"&gatherIds="+gatherIds+"&supplyName="+supplyName+"&inquirer="+workerName
		 	+"&inquirerPhone="+officeTel1+"&inquirerFax="+officeTel2;
//		 alert(encodeURI(strReportAdds));
		 window.open(encodeURI(strReportAdds));
//		 window.open(strReportAdds);
		} else {
			Ext.Msg.alert("提示", "请选择要打印的物资!");
		}
	}
	
function getWorkCode()
    {
     Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workerCode=result.workerCode;
                          	workerName= result.workerName;
                          	Ext.Ajax.request({
                          		url : 'resource/getWorkerInfoByWorkerId.action',
                          		method : 'post',
                          		params : {
                          			empId : result.workerId
                          		},
                          		success : function(response,options){
                          			var obj = Ext.util.JSON.decode(response.responseText);
                          			officeTel1 = obj.officeTel1;
                          			officeTel2 = obj.officeTel2;
                          		}
                          	})
                        }
                    }
                });
    }
    getWorkCode();

});