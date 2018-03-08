Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'tradeId'},
    {name : 'tradeName'},
	{name : 'memo'},
	{name : 'lastModifiedName'},
	{name : 'lastModifiedDate'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'managecontract/findTradeList.action'
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
//分页
	store.load();

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,

		columns : [
		sm, {
			
			header : "ID",
			sortable : true,
			dataIndex : 'tradeId',
			hidden:true
		},

		{
			header : "行业名称",
			width : 250,
			sortable : true,
			dataIndex : 'tradeName',
			align:'center'
		},

		{
			header : "备注",
			width : 250,
			sortable : true,
			dataIndex : 'memo',
			align:'center'
		},

		{
			header : "修改人",
			width : 100,
			sortable : true,
			dataIndex : 'lastModifiedName',
			align:'center'
		},
		{
			header : "修改时间",
			width : 120,
			sortable : true,
			dataIndex : 'lastModifiedDate',
			align:'center'
		}
		],
		sm : sm,
		tbar : [
		{
			text : "新增",
            iconCls : 'add',
			handler :addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}]
	});

grid.on("dblclick", updateRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 250;

	var tradeId = {
		id : "tradeId",
		xtype : "hidden",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		name : 'trade.tradeId'

	}
	var tradeName =new Ext.form.TextField( {
		id : "tradeName",
		xtype : "textfield",
		fieldLabel : '行业名称',
		allowBlank : false,
		width : wd,
		name : 'trade.tradeName'

	});
	


	var lastModifiedName = {
		id : "lastModifiedName",
		xtype : "textfield",
		fieldLabel : '修改人',
		readOnly : true,
		name : 'trade.lastModifiedName',
		width : wd
	}

	var lastModifiedDate = {
		id : "lastModifiedDate",
		xtype : "datefield",
		fieldLabel : '修改日期',
		readOnly : true,
		name : 'trade.lastModifiedDate',
		width : wd,
		format : 'Y-m-d h:i:s'
	}
	
		var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		width : wd,
		name : 'trade.memo'

	}


	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		title : '行业字典增加/修改',
		items : [tradeId,tradeName,lastModifiedName,lastModifiedDate,memo]

	});
		

	var win = new Ext.Window({
		width : 450,
		height : 250,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true, 
		modal : true,
		buttons : [{
			text : '保存',
			 iconCls : 'save',
			handler : function() {
				var myurl="";
				if (Ext.get("tradeId").dom.value == "") {
					myurl="managecontract/addTrade.action";
				} else {
						myurl="managecontract/updateTrade.action";
				}
			
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						store.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			 iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]

	});
	
	
		  

	
	function addRecord()
	{
		myaddpanel.getForm().reset();
		win.setPosition(200, 100);
		win.show(); 
		myaddpanel.setTitle("增加行业字典");
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改行业字典");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		
			var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.tradeId) {
					ids.push(member.tradeId); 
					names.push(member.tradeName);
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'managecontract/deleteTrade.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   		store.load();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
	
	
});