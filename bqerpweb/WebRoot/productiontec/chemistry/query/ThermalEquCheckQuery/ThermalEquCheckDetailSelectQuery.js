Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	var rlsbjcId = getParameter("rlsbjcId");

	var rec = Ext.data.Record.create([{
		name : 'rlsbjcDetailId'
	}, {
		name : 'rlsbjcId'
	}, {
		name : 'equCode'
	}, {
		name : 'equName'
	}, {
		name : 'repairDate'
	}, {
		name : 'courseNumber'
	}, {
		name : 'repairType'
	}, {
		name : 'repairNumber'
	}, {
		name : 'checkHigh'
	}, {
		name : 'checkName'
	}, {
		name : 'checkPart'
	}, {
		name : 'dirtyCapacity'
	}, {
		name : 'sedimentQuantity'
	}, {
		name : 'memo'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/findEquCheckDetailList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				// Ext.getCmp("form").getForm().loadRecord(rec);
			}
		}
	});

	var rn = new Ext.grid.RowNumberer({});

	var cm = new Ext.grid.ColumnModel([sm, rn, {
		header : "设备功能编码",
		width : 100,
		dataIndex : 'equCode',
		renderer : function(value) {
			if (value == null)
				value = "";
			return "<span style='color:gray;'>" + value + "</span>";

		}
	}, {
		header : "工作设备",
		width : 140,
		dataIndex : 'equName'
	}, {
		header : "检修时间",
		sortable : true,
		width : 120,
		dataIndex : 'repairDate',
		renderer : function(value) {
			if (!value) {
				return '';
			} else {
				return value;
			}
		}
	}, {
		header : '运行时数',
		dataIndex : 'courseNumber',
		align : 'left'
	}, {
		header : '维修类别',
		dataIndex : 'repairType',
		align : 'left'
	}, {
		header : '维修次数',
		dataIndex : 'repairNumber',
		align : 'left'
	}, {
		header : '标高',
		dataIndex : 'checkHigh',
		align : 'left'
	}, {
		header : '名称',
		dataIndex : 'checkName',
		align : 'left'
	}, {
		header : '部位',
		dataIndex : 'checkPart',
		align : 'left'
	}, {
		header : '垢量',
		dataIndex : 'dirtyCapacity',
		align : 'left'
	}, {
		header : '沉积量',
		dataIndex : 'sedimentQuantity',
		align : 'left'
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'left'
	}]);

	function queryRecord() {

		ds.baseParams = {
			rlsbjcId : rlsbjcId
		};
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		autoWidth : true,
		fitToFrame : true,
		border : true,
		frame : true,
		clicksToEdit : 1,// 单击一次编辑
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : "显示第{0}条到{1}条，共{2}条",
			emptyMsg : "没有记录",
			beforePageText : '',
			afterPageText : ""
		}),
		viewConfig : {
			forceFit : false
		}
	});

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
		layout : 'border',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : {
			region : 'center',
			layout : 'fit',
			items : [grid]
		}
	});

	queryRecord();
})