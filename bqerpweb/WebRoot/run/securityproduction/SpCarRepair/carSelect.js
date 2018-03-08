Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'carId',
		mapping : 0
	}, {
		name : 'carNo',
		mapping : 1
	}]);
	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'security/getSpCcarfileList.action?flag=F'
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
	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,
		columns : [
		sm, new Ext.grid.RowNumberer({
		header : '序号',
		width : 35,
		align : 'left'
	}),{			
			header : "车辆ID",
			width : 100,
			sortable : true,
			dataIndex : 'carId',
			hidden:true
		},{			
			header : "牌照号码",
			width : 80,
			sortable : true,
			dataIndex : 'carNo'
		}],
		sm : sm,
		tbar : [
		 {
			text : "确定",
			iconCls : 'update',
			handler:getRecord
		}, {
			text : "关闭",
			iconCls : 'delete',
			handler : function(){
				window.close();
			}
		}]
	});
	grid.on("dblclick", getRecord);
	function queryRecord() {
		store.load();
	}
	
	function getRecord()
	{
		if (grid.selModel.hasSelection()) {		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				var car=new Object();
				car.carId=record.get("carId");  
				car.carNo=record.get("carNo");
				window.returnValue = car;
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