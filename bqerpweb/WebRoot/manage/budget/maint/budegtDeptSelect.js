Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 120,
		emptyText : '部门名称'
	})

	var queryButton = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			westgrids.load({
				params : {
					fuzzytext : fuzzyText.getValue(),
					start : 0,
					limit : 18
				}
			});
		}
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([{
		name : 'centerId'
	}, {
		name : 'depCode'
	}, {
		name : 'depName'
	}, {
		name : 'manager'
	}, {
		name : 'ifDuty'
	}, {
		name : 'manageName'
	}]);

	var westgrids = new Ext.data.JsonStore({
		url : 'managebudget/findBudgetDeptList.action',
		root : "list",
		totalProperty : "totalCount",
		fields : datalist
	});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
		autoScroll : true,
		ds : westgrids,
		columns : [westsm, new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "centerId",
			width : 75,
			sortable : true,
			hidden : true,
			dataIndex : 'centerId'
		}, {
			header : "部门编码",
			width : 75,
			sortable : true,
			hidden : true,
			dataIndex : 'depCode'
		}, {
			header : "部门名称",
			width : 150,
			sortable : true,
			dataIndex : 'depName'
		}, {
			header : "部门负责人编码",
			width : 75,
			sortable : true,
			hidden : true,
			dataIndex : 'manager'
		}, {
			header : "部门负责人",
			width : 75,
			sortable : true,
			dataIndex : 'manageName'
		}, {
			header : "是否责任部门",
			width : 75,
			sortable : true,
			dataIndex : 'ifDuty',
			renderer : function(v) {
				if (v == 'Y') {
					return "是";
				}
				if (v == 'N') {
					return "否";
				}
			}
		}],
		tbar : ['模糊查询 ：', fuzzyText, queryButton, {
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
			displayMsg : "{0} 到 {1} /共 {2} 条",
			emptyMsg : "没有记录"
		}),
		border : true
	});

	westgrids.load({
		params : {
			start : 0,
			limit : 18,
			name : fuzzyText.getValue()
		}
	});
	westgrid.on('rowdblclick', selectRecord);

	function selectRecord() {
		if (westgrid.selModel.hasSelection()) {

			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {

				var record = westgrid.getSelectionModel().getSelected();
				var client = new Object();
				client.centerId = record.get("centerId");
				client.depCode = record.get("depCode");
				client.depName = record.get("depName");
				window.returnValue = client;
				window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择预算部门!");
		}
	}
	function cancelHandler() {
		var object = new Object();
		window.returnValue = object;
		window.close();
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '1 0 1 1',
			collapsible : true,
			items : [westgrid]

		}]
	});
});