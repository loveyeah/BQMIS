Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {


	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'conId'},
	{name : 'conttreesNo'},
    {name : 'contractName'},
	{name : 'clientName'},
	{name : 'operateName'},
	{name : 'operateDeptName'},
	{name:'operateBy'},
	{name:'operateDepCode'},
	{name:'operateLeadBy'},
	{name:'operateLeadName'},
	{name:'currencyType'},
	{name:'actAmount'},
	{name:'startDate'},
	{name:'endDate'},
	{name:'deptId'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'managecontract/findConListForSelect.action'
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
	store.load({
			params : {
				conTypeId : 2,
				start : 0,
				limit : 18			
			}
		});
	
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
			header : "合同ID",
			width : 100,
			sortable : true,
			dataIndex : 'conId',
			hidden:true
		},{
			
			header : "合同编号",
			width : 100,
			sortable : true,
			dataIndex : 'conttreesNo'
		},

		{
			header : "合同名称",
			width : 120,
			sortable : true,
			dataIndex : 'contractName',
			align:'center'
		},

		{
			header : "供应商",
			width : 100,
			sortable : true,
			dataIndex : 'clientName',
			align:'center'
		},

		{
			header : "经办人",
			width : 100,
			sortable : true,
			dataIndex : 'operateName',
			align:'center'
		},
		{
			header : "申请部门",
			width : 100,
			sortable : true,
			dataIndex : 'operateDeptName',
			align:'center'
		},
		{
			header : "经办人编码",
			sortable : true,
			hidden:true,
			dataIndex : 'operateBy',
			align:'center'
		},
		{
			header : "申请部门编码",
			sortable : true,
			hidden:true,
			dataIndex : 'operateDepCode',
			align:'center'
		},
		{
			header : "经办部门负责人",
			hidden:true,
			sortable : true,
			dataIndex : 'operateLeadBy',
			align:'center'
		},
		{
			header : "经办部门负责人名称",
			hidden:true,
			sortable : true,
			dataIndex : 'operateLeadName',
			align:'center'
		}
		],
		sm : sm,
		tbar : [fuzzy,
		{
			text : "查询",
            iconCls : 'add',
            handler:queryRecord
		}, {
			text : "确定",
			iconCls : 'update',
			handler:getRecord
		}, {
			text : "关闭",
			iconCls : 'delete',
			handler : function(){
				window.close();
			}
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

		grid.on("dblclick", getRecord);
function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				conTypeId : 2,
				start : 0,
				limit : 18
			}
		});
	}
	
	function getRecord()
	{
		if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				var con=new Object();
				//合同id
				con.conId=record.get("conId");  
				//合同编号
				con.conNo=record.get("conttreesNo");
				//合同名称
				con.conName=record.get("contractName");
				//经办人
				con.operateBy=record.get("operateBy");
				con.operateName=record.get("operateName");
				//经办部门
				con.operateDepCode=record.get("operateDepCode");
				con.operateDeptName=record.get("operateDeptName");
				//经办部门负责人
				con.operateLeadBy=record.get("operateLeadBy");
				con.operateLeadName=record.get("operateLeadName");
				//合同金额
				con.actAmount=record.get("actAmount");
				//币别
				con.currencyType=record.get("currencyType");
				//开始时间
				   con.startDate=record.get("startDate");
	         //结束时间
	              con.endDate=record.get("endDate");
	                //经办人部门ID
	              con.deptId = record.get("deptId");
				//-----------------------------------
//				con.operateDepCode="XXX";
//     			con.operateDeptName="临时数据";
//				con.operateLeadBy="999999";
//				con.operateLeadName="测试员";
//				con.startDate="2008-01-01";
//				 con.endDate="2008-12-31";
				window.returnValue = con;
				window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择行!");
		}
	}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

});