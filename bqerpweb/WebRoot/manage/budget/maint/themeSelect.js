Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
//	var args = window.dialogArguments;
//	var topicId = args.topicId;
//	var topicCode = args.topicCode;
//	var topicName = args.topicName;
	
	var item = Ext.data.Record.create([{
		name : 'topicId'
	},{
		name : 'topicCode'
	}, {
		name : 'topicName'
	}, {
		name : 'budgetType'
	}, {
		name : 'dataType'
	}, {
		name : 'timeType'
	},{
		name : 'isUse'
	}
	]);

	// 预算类别store
	var budgetTypeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','公司汇总'],['2','部门预算']]
	})
	//数据类别store
	var dataTypeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','经营预算'],['2','现金预算'],['3','财务预算']]
	})
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getThemeList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})

	// 事件状态
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
	}),{
		dataIndex : 'topicId',
		hidden : true
	}, {
		header : '主题编码',
		dataIndex : 'topicCode',
		align : 'right',
		width : 100
	}, {
		header : '主题名称',
		dataIndex : 'topicName',
		align : 'right',
		width : 110
	}, {
		header : '预算类别',
		dataIndex : 'budgetType',
		align : 'center',
		width : 110,
		renderer : function(v){
			if(v == 1)
			{
				return '公司汇总';
			}
			else if(v == 2)
			{
				return '部门预算';
			}
		}
	}, {
		header : "数据类别",
		width : 100,
		dataIndex : 'dataType',
		renderer : function(v){
			if(v == 1)
			{
				return '经营预算';
			}
			else if(v == 2)
			{
				return '现金预算';
			}
			else if(v == 3)
			{
				return '财务预算';
			}
		}
	}
	]);
		
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	function fuzzyQuery() {
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	};
	// tbar
	var contbar = new Ext.Toolbar({
		items : [{
			id : 'btnSure',
			iconCls : 'ok',
			text : "确定",
			handler : selectRecord
		}, {
			id : 'btnCancel',
			iconCls : 'cancer',
			text : "取消",
			handler : cancelHandler

		}]

	});
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		title : '预算主题选择',
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : true,
		viewConfig : {
		// forceFit : true
		}
	});
	
	Grid.on('rowdblclick',selectRecord);
	
	function selectRecord()
	{
		if(Grid.selModel.hasSelection()){
			var records = Grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if(recordslen > 1)
			{
				Ext.Msg.alert('提示信息','请选择其中一项！');
			}
			else
			{
				var record = Grid.getSelectionModel().getSelected();
				var client = new Object();
				client.topicId = record.get('topicId');
				client.topicCode = record.get('topicCode');
				client.topicName = record.get('topicName');
				client.budgetType = record.get('budgetType');
				client.dataType = record.get('dataType');
				window.returnValue = client;
				window.close();
			}
		}
		else
		{
			Ext.Msg.alert('提示信息','请先选择预算主题！')
		}
	}
	
	function cancelHandler()
	{
		var obj = new Object();
		window.returnValue = obj;
		window.close();
	}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});


})