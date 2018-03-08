//评价记录信息
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var cliendId = parent.cliendId;
var clendName = parent.cliendName;
Ext.onReady(function() {
	if(cliendId == "" ) {
		Ext.MessageBox.alert('提示', '请从列表中选择一条数据！');
		return;
	}
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'appraisalId'
	}, {
		name : 'intervalId'
	}, {
		name : 'cliendId'
	}, {
		name : 'totalScore'
	}, {
		name : 'intervalDate'
	}, {
		name : 'gatherFlag'
	}, {
		name : 'clientName'
	}, {
		name : 'appraiseBy'
	}, {
		name : 'appraiseName'
	}, {
		name : 'appraiseDate'
	}, {
		name : 'beginDate'
	}, {
		name : 'endDate'
	}, {
		name : 'gatherBy'
	}, {
		name : 'gatherName'
	}, {
		name : 'gatherDate'
	}, {
		name : 'appraisalResult'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'clients/findAppraiseGatherList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// ===========================查询条件=========================================

	var cbxClient = new Ext.form.TextField({
		name : 'cbxClient',
		id : 'cbxClient',
		readOnly : true,
		anchor : "80%",
		value : clendName
	});

	// ============================================================================
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit : 1,
		columns : [sm,new Ext.grid.RowNumberer({
			 header : '序号',
			width : 35
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'appraisalId',
			hidden : true
		}, {
			header : "合作伙伴",
			width : 75,
			sortable : true,
			hidden :true,
			dataIndex : 'clientName'
		}, {
			header : "评价区间",
			width : 75,
			sortable : true,
			dataIndex : 'intervalDate'
		}, {
			header : "评价分数",
			width : 75,
			sortable : true,
			dataIndex : 'totalScore'
		}, {
			header : "评价人",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseName'
		}, {
			header : "评价日期",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseDate'
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['合作伙伴：', cbxClient],
			bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});
		store.load({
		params : {
			clientId : cliendId
		}
	});
});