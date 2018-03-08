Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'notice.dztzdId'
	}, {
		name : 'notice.deviceId'
	}, {
		name : 'equName'
	}, {
		name : 'notice.dzjssmId'
	}, {
		name : 'jssmName'
	}, {
		name : 'notice.dztzdCode'
	}, {
		name : 'notice.ctCode'
	}, {
		name : 'notice.ptCode'
	}, {
		name : 'notice.memo'
	}, {
		name : 'effectiveDate'
	}, {
		name : 'notice.fillBy'
	}, {
		name : 'fillName'
	}, {
		name : 'notice.useStatus'
	}, {
		name : 'notice.saveBy'
	}, {
		name : 'saveName'
	}, {
		name : 'notice.saveMark'
	}, {
		name : 'protectedDevice'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findRelayProtectionNoticeList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var deviceName = new Ext.form.TextField({
		id : 'deviceName',
		fieldLabel : '保护装置',
		readOnly : true,
		anchor : "90%",
		listeners : {
			focus : deviceSelect
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		autoScroll : true,
		viewConfig : {
			forceFit : false
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'left'
		}), {
			header : "通知单ID",
			width : 100,
			sortable : true,
			dataIndex : 'notice.dztzdId',
			hidden : true
		}, {
			header : "通知单号",
			width : 130,
			sortable : true,
			dataIndex : 'notice.dztzdCode'
		}, {
			header : "装置ID",
			width : 80,
			sortable : true,
			hidden: true,
			dataIndex : 'notice.deviceId'
		}, {
			header : "保护装置",
			width : 130,
			sortable : true,
			dataIndex : 'equName'
		}, {
			header : "计算说明ID",
			width : 80,
			sortable : true,
			hidden:true,
			dataIndex : 'notice.dzjssmId'
		}, {
			header : "计算说明",
			width : 130,
			sortable : true,
			dataIndex : 'jssmName'
		}, {
			header : "CT变化",
			width : 130,
			sortable : true,
			dataIndex : 'notice.ctCode'
		}, {
			header : "PT变化",
			width : 130,
			sortable : true,
			dataIndex : 'notice.ptCode'
		}, {
			header : "生效时间",
			width : 130,
			sortable : true,
			dataIndex : 'effectiveDate'
		}, {
			header : "使用状态",
			width : 130,
			sortable : true,
			dataIndex : 'notice.useStatus',
			renderer : function(v) {
				if (v == '1') {
					return "作废";
				}
				if (v == '2') {
					return "执行";
				}
				if (v == '3') {
					return "未执行";
				}
			}
		}, {
			header : "存档人",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'saveName'
		}, {
			header : "存档状态",
			width : 130,
			sortable : true,
			dataIndex : 'notice.saveMark',
			renderer : function(v) {
				if (v == 'N') {
					return "未存档";
				}
				if (v == 'Y') {
					return "已存档";
				}
			}
		}, {
			header : "填报人",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'fillName'
		}],
		sm : sm,
		tbar : ['保护装置:', deviceName, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "查看",
			iconCls : 'list',
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
	grid.on('rowdblclick', updateRecord);
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "productiontec/relayProtection/query/protectionNoticeQuery/protectionNoticeQueryBase.jsp";			
				parent.document.all.iframe2.src = url;		
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的行!");
		}
	}
	
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				equName : deviceName.getValue()
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	queryRecord();

	function deviceSelect() {
		var url = "../../business/deviceSelect.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			deviceName.setValue(equ.deviceCode);
			deviceName.setValue(equ.deviceName);
		}
	}
});