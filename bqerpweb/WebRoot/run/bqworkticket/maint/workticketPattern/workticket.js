Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	// 定义grid 
	var MyRecord = Ext.data.Record.create([
		{name : 'workticketTypeId'},
		{name : 'workticketTypeCode'},
	    {name : 'workticketTypeName'},
	    {name : 'modifyBy'},
	    {name : 'modifyDate'}
		]);
		
	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'workticket/getWorkTicket.action'
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
	store.load(
	{
		// start jincong 2008-12-27 增加“公用”
		callback : function() {
			// 新记录
	        var record = new MyRecord({
	        	workticketTypeId : '',
	        	workticketTypeCode : 'C',
	        	workticketTypeName : '公用',
	        	modifyBy : '',
	        	modifyDate : ''
	        });
	        var count = store.getCount();
	        store.insert(count, record);
		}
		// end jincong 2008-12-27 增加“公用”
	}
	);	
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [
			sm,				
			{
				header : "编码",
				width : 40,
				sortable : true,
				dataIndex : 'workticketTypeCode'
			},
	
			{
				header : "类别",
				width : 40,
				sortable : true,
				dataIndex : 'workticketTypeName'
			}
		],
		sm : sm,
		autoSizeColumns : true,
		enableColumnHide : false,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},
		tbar : [
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
	grid.on("rowdblclick", updateRecord);
		new Ext.Viewport({
			enableTabScroll : true,
			layout : "border",
			items : [grid]
		});
	// 设定textfield的宽度
	var wd = 180;
	// 设定隐藏域
	var flag = {
		id : "flag",
		xtype : "hidden",
		value:"",
		name : "flag"
	}
	// 设定id输入框
	var workticketTypeId = {
		id : "workticketTypeId",
		xtype : "hidden",
		name : 'workticketTypeId',
		readOnly:true

	}
	// 工作票类型编输入框
	var workticketTypeCode = {
		id : "workticketTypeCode",
		xtype : "textfield",
		fieldLabel : '工作票类型编码<font color="red">*</font>',
		value : '',
		allowBlank : false,
		maxLength: 1,
		width : wd,
		name : 'bean.workticketTypeCode'
	};
	
	// 工作票类型名称输入框
	var workticketTypeName =new Ext.form.TextField( {
		id : "workticketTypeName",
		xtype : "textfield",
		fieldLabel : '工作票类型名称',
		width : wd,
		name : 'bean.workticketTypeName',
        maxLength : 20
	});

	// panel
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		items : [flag,workticketTypeId,workticketTypeCode,workticketTypeName]

	});
		
	// 弹出的window
	var win = new Ext.Window({
		width : 350,
		height : 150,
		modal:true,
		title : '工作票类型维护',
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				// start jincong 2008-12-27 编码验证
				if(validateCode(Ext.get("workticketTypeCode").dom.value) 
					|| !(Ext.get("flag").dom.value == "1")) {
					var myurl="";
					if (Ext.get("flag").dom.value == "1") {
						myurl="workticket/addWorkTicket.action";
					} else {
							myurl="workticket/updateWorkTicket.action";
						
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
						failure : function() {
							Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
						}
					});
				}
			}
			// end jincong 2008-12-27 编码验证
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]

	});
	
	/**
	 *  添加处理
	 */
	function addRecord()
	{
			
		myaddpanel.getForm().reset();
		win.show(); 
		Ext.get("flag").dom.value ="1";  		
		Ext.get("workticketTypeCode").dom.readOnly = false;
	}
	
	/**
	 * 更新处理
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
				Ext.get("flag").dom.value = "0";
				Ext.get("workticketTypeCode").dom.readOnly=true;
				
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	
	/**
	 * 删除处理
	 */
	function deleteRecord()
	{
		
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var codes =[];
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.workticketTypeId) {
					ids.push(member.workticketTypeId); 
					codes.push(member.workticketTypeCode);
				} else {					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm(Constants.BTN_DELETE , Constants.DelMsg, function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'workticket/deleteWorkTicket.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                                   		store.load();						         	
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