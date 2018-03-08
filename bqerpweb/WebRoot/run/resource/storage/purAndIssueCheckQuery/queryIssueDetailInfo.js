Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var arg = window.dialogArguments;
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([
		{name : 'issueHeadId',mapping : 0},
	    {name : 'issueNo',mapping : 1},
		{name : 'getRealPerson',mapping : 2},
		{name : 'receiptByName',mapping :3},
		{name : 'receiptDept',mapping :4},
		{name : 'totalPrice',mapping :5},
		{name : 'memo',mapping :9},
		{name : 'lastModifyDate',mapping :15}
	
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/queryCheckedIssueDetailList.action'
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
		
	//------------------------------------------------------------
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
//        renderTo:"mygrid",
		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 35}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'issueHeadId',
			hidden:true
		},
		{
			header : "审核时间",
			width : 90,
			sortable : true,
		//	hidden:true,
			dataIndex : 'lastModifyDate'
		},{
			header : "领料单号",
			width : 60,
			sortable : true,
			dataIndex : 'issueNo'
		},
		{
			header : "实际领料人",
			width : 60,
			sortable : true,
			dataIndex : 'getRealPerson'
		},
		{
			header : "申请领料人",
			width : 60,
			sortable : true,
			dataIndex : 'receiptByName'
		},
		{
			header : "申请领用部门",
			width : 100,
			sortable : true,
			dataIndex : 'receiptDept'
		},
		{
			header : "单据金额",
			width : 75,
			sortable : true,
			dataIndex : 'totalPrice',
			renderer:function (value)
			{
				if(value==null) return value;
			    else	return value.toFixed(2);
			}
			
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}
		],
		sm : sm,
		//autoSizeColumns : true,
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

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	
	
		  
	// 查询
	function queryRecord() {
		store.baseParams = {
			sdate : arg.sdate,
			edate:arg.edate,
			issueNo:arg.no,
			receiptBy:arg.receiptBy,
			receiptDept:arg.receiptDept,
			billType:arg.billType
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