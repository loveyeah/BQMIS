Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 定义时间格式
	 function renderDate(value) {
        if (!value) return "";
        
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        return strTime ? strDate + " " + strTime : strDate;
    }
	//==============  定义grid ===============
	var MyRecord = Ext.data.Record.create([
		{name : 'contentKeyId'},
	    {name : 'contentKeyName'},
		{name : 'modifyBy'},
		{name : 'modifyDate'},
		{name : 'orderBy'},
		{name : 'workticketTypeCode'},
		{name : 'keyType'}
	]);
	
	var dataProxy = new Ext.data.HttpProxy({
		// 工作票内容关键词查询
		url:'workticket/getContentKeyList.action'
	});
	
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	
	
	var storeCbx = new Ext.data.JsonStore({
		root: 'list',
		url:"workticket/getTicketTypeCodeList.action",
		fields:['workticketTypeCode','workticketTypeName']
	})
	storeCbx.load();
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var frontRadio = new Ext.form.Radio({
		id : 'front',
		boxLabel : '前置词',
		name : 'contentKey.keyType',
		inputValue : '1',
		checked : true
	})
	var backRadio = new Ext.form.Radio({
		id : 'back',
		boxLabel : '后置词',
		name : 'contentKey.keyType',
		inputValue : '2'
	})

	var workTicketTypeCodeComboBox =  new Ext.form.ComboBox({
   		store :storeCbx,
   		displayField:"workticketTypeName",
   		valueField:"workticketTypeCode",
   		mode : 'local', 
   		triggerAction : 'all',
   		allowBlank : false,
   		readOnly : true 
    });
   // 通过store的装载初始化工作票类型下拉框的默认选项为store的第一项
    storeCbx.on("load", function(e, records, o) {
		workTicketTypeCodeComboBox.setValue(records[0].data.workticketTypeCode);
		store.load({
			params : {
				keyType : '1',
				typeCode : workTicketTypeCodeComboBox.value,
				start : 0,
				limit : Constants.PAGE_SIZE
			}
    	});
	});
	wd = 180;
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		
		columns : [
			sm, 
			{
				header : "ID",
				width : 75,
				sortable : true,
				dataIndex : 'contentKeyId'
			},
			{
				header : "关键词",
				width : 75,
				sortable : true,
				dataIndex : 'contentKeyName'
			},
			{
				header : "填写人",
				width : 75,
				sortable : true,
				hidden : true,
				dataIndex : 'modifyBy'
			},
			{
				header : "填写日期",
				width : 75,
				sortable : true,
				dataIndex : 'modifyDate',
				renderer : renderDate
			},
			{
				header : "显示顺序",
				width : 0,
				sortable : true,
				dataIndex : 'orderBy',
				hidden : true
			},
			{
				header : "工作票类型编码",
				width : 0,
				sortable : true,
				dataIndex : 'workticketTypeCode',
				hidden : true
			},
			{
				header : "关键词标识",
				width : 0,
				sortable : true,
				dataIndex : 'keyType',
				hidden : true
			}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [
		frontRadio,backRadio,
		'&nbsp;','工作票类型:',
		workTicketTypeCodeComboBox,
		{
			text : Constants.BTN_QUERY,
			iconCls : Constants.CLS_QUERY,
			handler : queryRecord
		}, {
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
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : store,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		enableColumnMove : false
	});

	grid.on("rowdblclick", updateRecord);
	
	new Ext.Viewport({
		title : '工作票内容关键词维护',
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});
	
	//=============  定义弹出画面  ===============
	var contentKeyName = {
		id : "contentKeyName",
		xtype : "textfield",
		fieldLabel : "名称<font color='red'>*</font>",
		allowBlank : false,
		width : wd,
		name : 'contentKey.contentKeyName',
        maxLength : 100
	}
	
	var orderBy = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '显示顺序',
		width : wd,
		name : 'contentKey.orderBy',
        maxLength : 10
	}
	
	var workticketTypeCodeHidden = {
		id : "workticketTypeCode",
		xtype : "hidden",
		fieldLabel : '工作票类型编码',
		width : wd,
		name : 'contentKey.workticketTypeCode'
	}
	
	var keyType = {
		id : "keyType",
		xtype : "hidden",
		fieldLabel : '关键词标识',
		width : wd,
		name : 'contentKey.keyType'
	}
	
	// 增加，更新Flag
	var addUpdateFlag = {
		id : "addUpdateFlag",
		xtype : "hidden",
		value:"",
		name : "addUpdateFlag"
	}
	
	var contentKeyId = {
		id : "contentKeyId",
		xtype : "hidden",
		value:"",
		name : "contentKey.contentKeyId"
	}
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		items : [addUpdateFlag, contentKeyId, contentKeyName, orderBy, workticketTypeCodeHidden, keyType]

	});
	
	 store.on('beforeload',function(){
	  	var keyType = "";
		if(frontRadio.checked) {
			keyType = "1";
		} else {
			keyType = "2";
		}
        Ext.apply(this.baseParams, {
               	typeCode : workTicketTypeCodeComboBox.value,
				keyType : keyType
            })           
      });

	var win = new Ext.Window({
		resizable : false,
		width : 350,
		height : 130,
//		title : '修改关键词',
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal:true,
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				if (!myaddpanel.getForm().isValid()) {
					return false;
				}
				var myurl="";
				if (Ext.get("addUpdateFlag").dom.value == "0") {
					// 增加工作票内容关键词
					myurl="workticket/addContentKey.action";
				} else {
					// 更新工作票内容关键词
					myurl="workticket/updateContentKey.action";
				}
				myaddpanel.getForm().submit({
					method : Constants.POST,
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
						store.load({
                                	params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                    }
                                  });
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
		}
		]
	});
	
	/**
	 * 
	 */
	function queryRecord() {
		var workTicketTypeCode = workTicketTypeCodeComboBox.value;
		var keyType = "";
		if(frontRadio.checked) {
			keyType = "1";
		} else {
			keyType = "2";
		}
		store.baseParams = {
			typeCode : workTicketTypeCode,
			keyType : keyType
		};
		store.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}
	/**
	 * 增加工作票内容关键词
	 */
	function addRecord()
	{
		myaddpanel.getForm().reset();
		win.setTitle("增加关键词");
		win.show();
		// 
		Ext.get("addUpdateFlag").dom.value = "0";
		var workTicketTypeCode = workTicketTypeCodeComboBox.value;
		var keyType = "";
		if(frontRadio.checked) {
			keyType = "1";
		} else {
			keyType = "2";
		}
		Ext.get("workticketTypeCode").dom.value = workTicketTypeCode;
		Ext.get("keyType").dom.value = keyType;
	}
	
	/**
	 * 更新工作票内容关键词
	 */
	function updateRecord() {
		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setTitle("修改关键词");
				win.show();
				myaddpanel.getForm().loadRecord(record);
				Ext.get("addUpdateFlag").dom.value = "1";
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	
	/**
	 * 删除工作票内容关键词
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
				if (member.contentKeyId) {
					ids.push(member.contentKeyId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request(Constants.POST,
							'workticket/deleteContentKey.action', {
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
									Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
});