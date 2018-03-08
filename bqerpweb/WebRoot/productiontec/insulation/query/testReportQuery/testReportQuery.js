Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var bview;
	
	var equNameQuery = new Ext.form.TextField({
		id : 'equNameQuery',
		fieldLabel : '设备名称',
		readOnly : true,
		anchor : "85%",
		listeners : {
			focus : equNameSelect
		}
	});
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'reportId'
	}, {
		name : 'equId'
	}, {
		name : 'equName'
	}, {
		name : 'content'
	}, {
		name : 'memo'
	}]);
	
	var dataProxy = new Ext.data.HttpProxy(
			{
				url : 'productionrec/findSybgAllList.action'
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

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 50
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'reportId',
			hidden : true
		}, {
			header : "equId",
			width : 75,
			sortable : true,
			hidden : true,
			dataIndex : 'equId'
		}, {
			header : "设备名称",
			width : 75,
			sortable : true,
			dataIndex : 'equName'
		}, {
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}, {
			header : "内容",
			width : 75,
			sortable : true,
			dataIndex : 'content',
			renderer : function(v) {
				if (v != null && v != '') {
					var s = '<a href="#" onclick="window.open(\'' + v
							+ '\');return  false;">[查看]</a>';
					return s;
				}
			}
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['设备名称：', equNameQuery, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "查看",
			iconCls : 'list',
			handler : updateRecord
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

	grid.on("rowdblclick", updateRecord);
	//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	// -------------------
	
	var reportId = new Ext.form.Hidden({
		id : "reportId",
		fieldLabel : 'ID',
		anchor : "95%",
		readOnly : true,
		value : '',
		name : 'report.reportId'
	});
	
	var equId = new Ext.form.TextField({
		id : "equId",
		fieldLabel : 'equId',
		xtype : "textfield",
		readOnly : true,
		name : 'report.equId',
		anchor : "85%"
	});

	var equName = new Ext.form.TextField({
		id : 'equName',
		fieldLabel : '设备名称',
		readOnly : true,
		anchor : "85%",
		name : 'report.equName'
	});
	// 附件
	var annex = {
		id : "annex",
		xtype : "textfield",
		fieldLabel : '内容',
		readOnly : true,
		height : 21,
		anchor : "100%"
	}
	// 附件 内容
//	var annex = {
//		id : "annex",
//		xtype : 'fileuploadfield',
//		isFormField : true,
//		readOnly : true,
//		name : "annex",
//		fieldLabel : '内容',
//		height : 21,
//		anchor : "100%"
////		buttonCfg : {
////			text : '浏览...',
////			iconCls : 'upload-icon'
////		}
//	}

	// 查看
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			window.open(bview);
		}
	});
	btnView.setVisible(false);

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		anchor : "85%",
		name : 'report.memo',
		readOnly : true,
		height : 80
	});

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		fileUpload : true,
		closeAction : 'hide',
		title : '增加/修改绝缘监督试验报告',
		layout : 'column',
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [reportId]
		}, {
			columnWidth : 1,
			border : false,
			hidden : true,
			layout : 'form',
			items : [equId]
		}, {
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [equName]
		}, {
			columnWidth : 0.78,
			border : false,
			layout : 'form',
			items : [annex]
		}, {
			columnWidth : 0.22,
			border : false,
			layout : 'form',
			items : [btnView]
		}, {
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [memo]
		}]
	});
	
	var win = new Ext.Window({
		width : 500,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	function queryRecord() {
		store.baseParams = {
			equName : equNameQuery.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().reset();
				myaddpanel.getForm().loadRecord(record);

				if (record.get("content") != null && record.get("content") != "") {
					bview = record.get("content");
					btnView.setVisible(true);
					Ext.get("annex").dom.value = bview.replace('/power/upload_dir/productionrec/','');
				} else {
					btnView.setVisible(false);
				}
				myaddpanel.setTitle("修改绝缘监督试验报告");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的行!");
		}
	}

	
	/** 设备选择处理(查询条件） */
	function equNameSelect() {
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		//url += "op=many";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			equNameQuery.setValue(equ.name);
		}
	};

	queryRecord();

});