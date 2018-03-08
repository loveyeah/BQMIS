Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'tool.toolsId'
	}, {
		name : 'tool.toolsNames'
	}, {
		name : 'tool.toolsCode'
	}, {
		name : 'tool.toolsSizes'
	}, {
		name : 'tool.factory'
	}, {
		name : 'strManuDate'
	}, {
		name : 'chargeName'
	}, {
		name : 'tool.chargeMan'
	}, {
		name : 'strCheckDate'
	}, {
		name : 'tool.state'
	}, {
		name : 'tool.memo'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/findSafeToolList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 10
		}
	});

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsId',
			hidden : true
		}, {
			header : "工具名称",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsNames'
		}, {
			header : "工具编号",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsCode'
		}, {
			header : "负责人",
			width : 75,
			sortable : true,
			dataIndex : 'chargeName'
		}, {
			header : "规格型号",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsSizes'
		}, {
			header : "状态",
			width : 75,
			sortable : true,
			dataIndex : 'tool.state',
			renderer : function(value) {
				if (value == "0")
					return "不合格";
				else if (value == "1")
					return "合格";
				else
					return "";
			}
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['工具名称或负责人：', fuzzy, {
			text : "查询",
			handler : queryRecord
		}, {
			text : "查看详细信息",
			iconCls : 'update',
			handler : updateRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 220;
	var toolsId = new Ext.form.Hidden({
		id : "toolsId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value : '',
		name : 'tool.toolsId'
	});

	var toolsNames = new Ext.form.TextField({
		id : "toolsNames",
		fieldLabel : '工具名称',
		allowBlank : false,
		readOnly : true,
		width : wd,
		name : 'tool.toolsNames'
	});

	var toolsCode = new Ext.form.TextField({
		id : "toolsCode",
		fieldLabel : '工具编号',
		allowBlank : false,
		width : wd,
		readOnly : true,
		name : 'tool.toolsCode'
	});

	var toolsSizes = new Ext.form.TextField({
		id : "toolsSizes",
		fieldLabel : '规格型号',
		width : wd,
		readOnly : true,
		name : 'tool.toolsSizes'
	});

	var factory = new Ext.form.TextField({
		id : "factory",
		fieldLabel : '生产厂家',
		width : wd,
		readOnly : true,
		name : 'tool.factory'
	});

	var manuDate = new Ext.form.TextField({
		id : "manuDate",
		fieldLabel : '生产日期',
		readOnly : true,
		width : wd,
		name : 'tool.manuDate',
		format : 'Y-m-d'
	});

	var chargeMan = new Ext.form.TextField({
		fieldLabel : '负责人',
		id : 'chargeMan',
		readOnly : true,
		name : 'tool.chargeMan',
		width : wd
	});

	var checkDate = new Ext.form.TextField({
		id : 'checkDate',
		fieldLabel : '检修日期',
		readOnly : true,
		width : wd,
		name : 'tool.checkDate',
		format : 'Y-m-d'
	});
	
	var state = new Ext.form.ComboBox({
		fieldLabel : '状态',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['0', '不合格'], ['1', '合格']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		hiddenName : 'tool.state',
		value : '',
		editable : false,
		selectOnFocus : true,
		triggerAction : 'all',
		name : 'state',
		width : wd

	});
	state.on('beforequery', function() {
		return false
	});
	
	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		width : 260,
		name : 'tool.memo',
		readOnly : true,
		height : 80

	});
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '工器具信息',
		items : [toolsId, toolsNames, toolsCode, toolsSizes, factory, manuDate,
				chargeMan, checkDate, state, memo]
	});

	var win = new Ext.Window({
		width : 400,
		height : 400,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});
	// 查询
	function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			toolOrMan : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			win.show();
			var record = grid.getSelectionModel().getSelected();
			myaddpanel.getForm().reset();
			toolsId.setValue(record.get("tool.toolsId"));
			toolsNames.setValue(record.get("tool.toolsNames"));
			toolsCode.setValue(record.get("tool.toolsCode"));
			toolsSizes.setValue(record.get("tool.toolsSizes"));
			factory.setValue(record.get("tool.factory"));
			manuDate.setValue(record.get("strManuDate"));
			checkDate.setValue(record.get("strCheckDate"));
			state.setValue(record.get("tool.state"));
			memo.setValue(record.get("tool.memo"));
			Ext.getCmp('chargeMan').setValue(record.get("tool.chargeMan"));
			Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('chargeMan'),
					record.get("chargeName"));
			myaddpanel.setTitle("详细信息");
		}
	}
});