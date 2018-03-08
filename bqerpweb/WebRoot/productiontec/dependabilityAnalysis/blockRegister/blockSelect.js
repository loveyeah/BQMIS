Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'blockId'
	}, {
		name : 'blockCode'
	}, {
		name : 'blockName'
	}, {
		name : 'nameplateCapability'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findKkxBlockList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'blockId',
			hidden : true
		}, {
			header : "机组编码",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'blockCode'
		}, {
			header : "机组名称",
			width : 100,
			sortable : true,
			dataIndex : 'blockName',
			align : 'center'
		}, {
			header : "铭牌容量（MW）",
			width : 100,
			sortable : true,
			dataIndex : 'nameplateCapability',
			align : 'center'
		}],
		sm : sm,
		tbar : [{
			text : "确定",
			iconCls : 'update',
			handler : getRecord
		}, {
			text : "关闭",
			iconCls : 'delete',
			handler : function() {
				window.close();
			}
		}]
	});
	grid.on("dblclick", getRecord);
	function queryRecord() {
		store.load();
	}

	function getRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				var block = new Object();
				//id 
				block.blockId = record.get("blockId");
				//code
				block.blockCode = record.get("blockCode");
				//name
				block.blockName = record.get("blockName");
				//铭牌容量
				block.nameplateCapability = record.get("nameplateCapability");
				window.returnValue = block;
				window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择行!");
		}
	}
	//---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});