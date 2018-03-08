Ext.BLANK_IMAGE_URL="comm/ext/resources/images/default/s.gif";

Ext.onReady(function(){
	var westsm = new Ext.grid.CheckboxSelectionModel();
	
	// 列表中的数据
	var datalist = new Ext.data.Record.create([
		{
			name : 'dzjssm.dzjssmId'
		}, {
			name : 'dzjssm.jssmName'
		}, {
			name : 'dzjssm.content'
		}, {
			name : 'dzjssm.memo'
		}, {
			name : 'dzjssm.fillBy'
		}, {
			name : 'dzjssm.fillDate'
		}, {
			name : 'fillName'
		},{
			name : 'fillDate'
		}
		]);
	var westgrids = new Ext.data.JsonStore({
		url : 'productionrec/getAllDzjssm.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});
	
	// 列表
	var westgrid = new Ext.grid.GridPanel({
		autoScroll : true,
		ds : westgrids,
		columns : [ westsm, 
					new Ext.grid.RowNumberer({
											header : "行号",
											width : 31
										}),
			{
				header : '定值计算说明编号',
				width : 120,
				align : 'center',
				sortable : true,
				hidden : true,
				dataIndex : 'dzjssm.dzjssmId'
			},{
				header : '定值计算说明主题',
				width : 120,
				align : 'center',
				sortable : true,
				dataIndex : 'dzjssm.jssmName'
			},{
				header : '计算说明',
				width : 120,
				align : 'center',
				sortable : true,
				dataIndex : 'dzjssm.content'
			}],
		tbar : [{
									text : '确定',
									iconCls : 'ok',
									handler : selectRecord
								}, {
									text : '取消',
									iconCls : 'cancer',
									handler : cancelHandler
								}],
		sm : westsm,
		frame : true,
		bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : westgrids,
									displayInfo : true,
									displayMsg : "{0}到{1}/共{2}条",
									emptyMsg : "没有记录"
								}),
		border : true
	})
	westgrids.load({
		params : {
			start : 0,
			limit : 18
		}
	})
	
	westgrid.on("rowdblclick",selectRecord);
	
	function selectRecord()
	{
		if(westgrid.selModel.hasSelection())
		{
			var records = westgrid.getSelectionModel().getSelections()
			var recordslen = records.length;
			if(recordslen > 1)
			{
				Ext.Msg.alert("提示信息","请选择其中一项！");
			}
			else
			{
				var record = westgrid.getSelectionModel().getSelected();
				  var dzj=new Object();
		         dzj.dzjssmId=record.get("dzjssm.dzjssmId");
		         dzj.jssmName=record.get("dzjssm.jssmName");
		         dzj.content=record.get("dzjssm.content");
		        
		         window.returnValue=dzj;
		         window.close();
			}
		}
		else
		{
			Ext.Msg.alert("提示信息","请先选择计算说明主题");
		}
	}
	
	function cancelHandler()
	{
		var object = new Object();	
				window.returnValue = object;
				window.close();
	}
	
	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : 'border',
		border : true,
		items : [
			{
				region : 'center',
				layout : 'fit',
				border : false,
				margins : '1 0 1 1',
				collapsible : true,
				items : [westgrid]
			}
		]
	})
})		