Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'power.noticeId'},
	{name:'power.noticeNo'},
    {name : 'power.contactDept'},
	{name : 'power.contactMonitor'},
	{name : 'power.contactContent'},
	{name : 'power.memo'},
	{name : 'deptName'},
	{name : 'monitorName'},
	{name : 'busiStateName'},
	{name : 'power.busiState'},
	{name : 'power.workFlowNo'},
	{name:'contactDate'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'powernotice/findPowerNoticeApproveList.action'
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
//分页

	
		var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'center'
	}),{
			
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.noticeId',
			hidden:true
		},{
			
			header : "状态ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.busiState',
			hidden:true
		},{
			
			header : "通知单号",
			width : 80,
			sortable : true,
			dataIndex : 'power.noticeNo'
		},{
			
			header : "状态",
			width : 70,
			sortable : true,
			align : 'center',
			dataIndex : 'busiStateName'
//				renderer : function changeIt(val) {
//				
//				
//			}
		},

		{
			header : "联系班组编号",
			width : 120,
			sortable : true,
			dataIndex : 'power.contactDept',
			align:'center',
			hidden:true
		},

		{
			header : "联系班组",
			width : 100,
			sortable : true,
			dataIndex : 'deptName',
			align:'center'
		},
		{
			header : "联系班长工号",
			width : 100,
			sortable : true,
			dataIndex : 'power.contactMonitor',
			align:'center',
			hidden:true
		},
		{
			header : "联系班长",
			width : 70,
			sortable : true,
			dataIndex : 'monitorName',
			align:'center'
		},
		{
			header : "联系时间",
			width : 110,
			sortable : true,
			dataIndex : 'contactDate',
			align:'center'
		},
		{
			header : "联系内容",
			width : 140,
			sortable : true,
			dataIndex : 'power.contactContent',
			align:'center'
		},
		{
			header : "备注",
			width : 100,
			sortable : true,
			dataIndex : 'power.memo',
			align:'center'
		},
		{
			header : "工作流实例",
			width : 100,
			sortable : true,
			dataIndex : 'power.workFlowNo',
			align:'center'
		}
		
		],
		sm : sm,
		tbar : [
		{
			text : "查询",
            iconCls : 'query',
            handler:queryRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	
	grid.on('rowdblclick',edit);
	
	
	/**
	 * 双击grid事件
	 */
	function edit() {
		var record = grid.getSelectionModel().getSelected();
		var workFlowNo = record.get('power.workFlowNo');
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var obj = eval("(" + result.responseText + ")");
		       
				if (obj[0].url==null||obj[0].url=="") {
					Ext.Msg.alert("提示","无地址");
				} else {
					
					var url = "../../../../" + obj[0].url; 
					
					var args = new Object();
					args.busiNo = record.get('power.noticeNo');
					args.entryId = record.get("power.workFlowNo"); 
					args.flowCode="";
					
					args.title="值班负责人审批";
					window.showModalDialog(url, args,
							'status:no;dialogWidth=750px;dialogHeight=550px');
					queryRecord();
				}
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});
	}
	
     function queryRecord() {
	
		store.load({
			params : {
				start : 0,
				limit : 18
			
			}
		});
	}
	
	
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();

});