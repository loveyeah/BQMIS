Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([
		{name : 'firelevelId'},
	    {name : 'firelevelName'},
		{name : 'orderBy'}
		]);

	var dataProxy = new Ext.data.HttpProxy(
    
			{
				url:'workticket/getWorkTicketFireLevel.action'
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
	store.load();

	// 查询textfield输入框
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [
			sm, {				
				header : "编码",
				width : 40,
				sortable : true,
				dataIndex : 'firelevelId'
			},	
			{
				header : "名称",
				width : 40,
				sortable : true,
				dataIndex : 'firelevelName'
			},	
			{
				header : "显示顺序",
				width : 40,
				sortable : true,
				dataIndex : 'orderBy'
			}
		],
		sm : sm,
		autoSizeColumns : true,
		enableColumnHide : false,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},		
		tbar : ['编码或名称:', fuuzy, {
			text : Constants.BTN_QUERY,
			iconCls : Constants.CLS_QUERY,
			handler : queryRecord
		},
		{
			text : Constants.BTN_ADD,
            iconCls : Constants.CLS_ADD,
			handler :addRecord
		}, {
			text : Constants.BTN_UPDATE,
			iconCls : Constants.CLS_UPDATE,
			handler : updateRecord
		}, {
			text : Constants.BTN_DELETE,
			iconCls : Constants.CLS_DELETE,
			handler : deleteRecord
		}]
});
	// 添加grid的事件
	grid.on("dblclick", updateRecord);

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	var wd = 180;
	var unitId = {
		id : "firelevelId",
		xtype : "textfield",
		fieldLabel : '编码',
		value : Constants.AUTO_CREATE,
		width : wd,
		readOnly : true,
		name : 'firelevelId'

	}
	var unitName =new Ext.form.TextField( {
		id : "firelevelName",
		xtype : "textfield",
		fieldLabel : '名称<font color="red">*</font>',
		allowBlank : false,
		width : wd,
		name : 'bean.firelevelName',
        maxLength : 30
	});
	
	var unitAlias = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '显示顺序',
		name : 'bean.orderBy',
		width : wd,
        maxLength : 10
	}
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		items :[unitId,unitName,unitAlias]
	});
		

	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		items : [myaddpanel],
		buttonAlign : "center",
		modal:true,
		title : '动火票级别维护',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var myurl="";
				if (Ext.get("firelevelId").dom.value == Constants.AUTO_CREATE) {
					myurl="workticket/addWorkTicketFireLevel.action";
				} else {
						myurl="workticket/updateWorkTicketFireLevel.action";
				}
		
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert(Constants.NOTICE, o.msg);
							store.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}
				});
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
			  
	/**
	 * 查询
	 */
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load();
	}
	
	/**
	 *  添加一条记录
	 */  
	function addRecord()
	{
			
		myaddpanel.getForm().reset();	
		win.show(); 	        
	}
	
	/** 
	 * 更新一条记录
	 */
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = grid.getSelectionModel().getSelected();		
				win.show();
				myaddpanel.getForm().loadRecord(record);
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	
	/** 
	 * 删除记录
	 */
	function deleteRecord()
	{		
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.firelevelId) {
					ids.push(member.firelevelId); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}			
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg , function(
					buttonobj) {
				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'workticket/deleteWorkTicketFireLevel.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                                   		store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : Constants.PAGE_SIZE
			                                        }
		                                          });						         	
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
								}
							}, 'ids=' + ids);
				}
			});
		}
	}		
});