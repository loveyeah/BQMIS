Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'bookId'},
	{name :'bookNo'}
	]);
	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'resource/findBookNoSelectList.action'
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
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'bookId',
			hidden:true
		},{			
			header : "盘点单号",
			width : 80,
			sortable : true,
			dataIndex : 'bookNo'
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
				var book=new Object();
				//id 
				book.bookId=record.get("bookId");  
				//申请单编号
				book.bookNo=record.get("bookNo");
				window.returnValue = book;
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