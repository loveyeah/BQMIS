Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var cliendId = parent.cliendId;

Ext.onReady(function() {
	if(cliendId == "" ) {
		Ext.MessageBox.alert('提示', '请从列表中选择一条数据！');
		return;
	}
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'qualificationId'
	}, {
		name : 'cliendId'
	}, {
		name : 'clientName'
	}, {
		name : 'aptitudeName'
	}, {
		name : 'qualificationOrg'
	}, {
		name : 'sendPaperDate'
	}, {
		name : 'beginDate'
	}, {
		name : 'endDate'
	}, {
		name : 'memo'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'clients/findClientsQualificationList.action'
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
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'qualificationId',
			hidden : true
		}, {
			header : "资质材料名称",
			width : 110,
			sortable : true,
			dataIndex : 'aptitudeName'
		}, {
			header : "合作伙伴",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'cliendId'
		}, {
			header : "合作伙伴名称",
			width : 130,
			sortable : true,
			dataIndex : 'clientName'
		}, {
			header : "发证机构",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'qualificationOrg'
		}, {
			header : "发证日期",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'sendPaperDate'
		}, {
			header : "证书生效日期",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'beginDate'
		}, {
			header : "证书失效日期",
			width : 80,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'endDate'
		}, {
			header : "备注",
			width : 120,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'memo'
		}, {
			header : "登记人",
			width : 120,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'lastModifiedName'
		}, {
			header : "登记日期",
			width : 120,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'lastModifiedDate'
		}],
		sm : sm,
		tbar : ['双击查看资质信息',{
			handler : queryRecord
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
		var record = grid.getSelectionModel().getSelected();
		var qualificationId = record.data.qualificationId;
		var url = "clientQualificationInfo.jsp?qualificationId="
				+ qualificationId;
		var o = window.showModalDialog(url, '',
				'dialogWidth=650px;dialogHeight=400px;status=no');
	}
	
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				clientId : cliendId
			}
		});
	}
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});