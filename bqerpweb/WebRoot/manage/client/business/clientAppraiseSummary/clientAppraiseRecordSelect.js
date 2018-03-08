Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var cliendId = getParameter("cliendId");
	var intervalId = getParameter("intervalId");
	var clientName = getParameter("clientName");
	var beginDate = getParameter("beginDate");
	var endDate = getParameter("endDate");

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'recordId'
	}, {
		name : 'eventId'
	}, {
		name : 'intervalId'
	}, {
		name : 'cliendId'
	}, {
		name : 'appraisePoint'
	}, {
		name : 'memo'
	}, {
		name : 'eventName'
	}, {
		name : 'gatherFlag'
	}, {
		name : 'clientName'
	}, {
		name : 'appraiseMark'
	}, {
		name : 'appraiseCriterion'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'clients/findAppraiseRecordList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// -----------查询条件-----------------------------------------
	var cbxClient = new Ext.form.TextField({
		name : 'cbxClient',
		id : 'cbxClient',
		readOnly : true,
		anchor : "80%",
		value : clientName
	});

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		fieldLabel : '评价周期开始时间',
		name : 'beginDate',
		readOnly : true,
		value : beginDate
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : '评价周期结束时间',
		name : 'endDate',
		readOnly : true,
		value : endDate
	});
	var hideIntervalId = new Ext.form.Hidden({
		id : 'hideIntervalId'
	});

	// ----------------------------------------------
	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit : 1,
		columns : [new Ext.grid.RowNumberer({
			header : '序号',
			width : 50
		}), {

			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'recordId',
			hidden : true
		}, {
			header : "评价项目名称",
			width : 75,
			sortable : true,
			dataIndex : 'eventName'
		}, {
			header : "标准分数",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseMark'
		}, {
			header : "评分标准",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseCriterion'
		}, {
			header : "评价分数",
			width : 75,
			sortable : true,
			dataIndex : 'appraisePoint'
		}, {
			header : "评分备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}, {

			header : "是否汇总",
			width : 75,
			sortable : true,
			dataIndex : 'gatherFlag',
			renderer : function(value) {
				if (value == "N")
					return "否";
				if (value == "Y")
					return "是";
			}
		}],
		tbar : ['合作伙伴：', cbxClient, '评价区间：', beginDate, '~', endDate,
				hideIntervalId]
	});
	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});
	store.load({
		params : {
			clientId : cliendId,
			intervalId : intervalId
		}
	});
});