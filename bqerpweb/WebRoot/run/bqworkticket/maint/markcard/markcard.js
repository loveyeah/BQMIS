Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 标识牌类型检索
	var markcardStore = new Ext.data.JsonStore({
		url : 'workticket/getMarkcard.action',
		root : 'list',				
		fields : [{
            name : 'markcardTypeName'},
            {name : 'markcardTypeId'}]		
	});
	markcardStore.load();
	markcardStore.on("load", function(ds, records, o) {
		markcardTypeCbo.setValue(records[0].data.markcardTypeId);
		markcardTypeidCbo.setValue(records[0].data.markcardTypeId);
		searchStore.baseParams = {
			fuzzy : fuuzy.getValue(),
			markcardTypeID : markcardTypeCbo.value
		};
		searchStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	});

	//***************************弹出窗口**************************//
	
	// ID输入框
	var ID = {
   		id : 'markcardId',
        xtype : 'hidden',
        value : Constants.AUTO_CREATE
	};
	
	// 隐藏的ID
	var hiddenID = {
   		id : 'hiddenID',
   		xtype : 'hidden',
   		readOnly : true,
   		hidden : true,
   		value : '0',
   		name : 'markcard.markcardId'
	};
	
	// 标识牌类型下拉框
	var markcardTypeidCbo = new Ext.form.ComboBox({  
		id : 'markcardTypeidCbo',
		fieldLabel : '标识牌类型',
		allowBlank : true,
        triggerAction : 'all',
        store : markcardStore,
        displayField : 'markcardTypeName',
        valueField : 'markcardTypeId',
        mode : 'local',
        width : 180,
        emptyText : '标识牌类型...',
        blankText : '标识牌类型',
        readOnly : true
	});
	
	// 隐藏的标识牌类型
	var hiddenMarkcardCbo = {
		id : 'hiddenMarkcardCbo',
		name : 'markcard.markcardTypeId',
		xtype : 'hidden'
	}
	
	// 标识牌编号显示框
	var markcardCode = new Ext.form.TextField({
		fieldLabel : "标识牌编号<font color='red'>*</font>",
		width : 180,
   		id : 'markcardCode',
   		name : 'markcard.markcardCode',
   		allowBlank : false,
        maxLength : 20
	});
	
	// 排序号输入框
	var sortNumber = new Ext.form.NumberField({
		fieldLabel : '显示顺序',
		width : 180,
   		id : 'orderBy',
   		name : 'markcard.orderBy',
   		allowBlank : true,
        maxLength : 10
	});

	// Form
	var addPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		items : [ID,hiddenID,markcardTypeidCbo,hiddenMarkcardCbo,markcardCode,sortNumber]
	});
	
	// 弹出窗口
	var win = new Ext.Window({
		width : 380,
		height : 180,
		modal : true,
		title : '标识牌维护',
		buttonAlign : 'center',
		items : [addPanel],
		layout : 'fit',
		closeAction : 'hide',
        resizable : false,
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
            buttonAlign : 'center',
			handler : function() {
				// start jincong 2008-12-27 编码验证
				if(validateCode(markcardCode.getValue())) {
					var myurl="";
					if (Ext.get("markcardId").dom.value == Constants.AUTO_CREATE) {
						myurl="workticket/addWorkticketMarkcard.action";
						Ext.get("hiddenID").dom.value = "-1";
						Ext.get("hiddenMarkcardCbo").dom.value = markcardTypeidCbo.value;
					} else {
						myurl="workticket/updateWorkticketMarkcard.action";	
						Ext.get("hiddenID").dom.value = Ext.get("markcardId").dom.value;
	                    Ext.get("hiddenMarkcardCbo").dom.value = markcardTypeidCbo.value;
					}
					addPanel.getForm().submit({
						method : Constants.POST,
						url : myurl,
						success : function(form, action) {
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
								queryRecord();
							win.hide(); 
						},
						failure : function() {
							Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
						}
					});
				}
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
            buttonAlign : 'center',
			handler : function() {
				win.hide();
			}
		}]
	});
	
	//***************************主画面**************************//

	// 标识牌类型下拉框
	var markcardTypeCbo = new Ext.form.ComboBox({  
		id : 'markcardTypeCbo',
		name : 'markcardTypeCbo',
		allowBlank : true,
        triggerAction : 'all',
        store : markcardStore,
        displayField : 'markcardTypeName',
        valueField : 'markcardTypeId',
        mode : 'local',
        emptyText : '标识牌类型...',
        blankText : '标识牌类型',
        readOnly : true
	});
	
	// 编号模糊查询框
	var fuuzy = new Ext.form.TextField({
		id : 'fuzzy',
		name : 'fuzzy'
	});
	
	// 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryRecord
    });
            
    // 增加按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : function() {
        	addPanel.getForm().reset();
        	markcardTypeidCbo.setDisabled(false);
			win.show();
        }
    });
            
    // 修改按钮
    var updateBtn = new Ext.Button({
        id : 'update',
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateRecord
    });
            
    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : deleteRecord
    });  
    
    // grid中的数据
	var runGridList = new Ext.data.Record.create([  {
		name : 'markcardId'
	}, {
		name : 'markcardCode'
	}, {
		name : 'markcardTypeId'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'orderBy'
	}]);
			
    // 选择列
    var sm = new Ext.grid.CheckboxSelectionModel(); 
    
    var dataProxy = new Ext.data.HttpProxy({
		url : 'workticket/getWorkticketMarkcard.action'
	});
	
    var theReader = new Ext.data.JsonReader({
		root : 'list',
		totalProperty : 'totalCount'
	}, runGridList);
	
    // grid的store
    var searchStore = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	searchStore.setDefaultSort('markcardCode', 'asc');
    
	 function renderDate(value) {
        if (!value) return "";
        
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        return strTime ? strDate + " " + strTime : strDate;
    }
    
  	// 运行执行的Grid主体
    var runGrid = new Ext.grid.GridPanel({
        store : searchStore,
        columns : [ sm, {
                    header : '标识牌编号',
                    width : 60,
                    sortable : true,
                    dataIndex : 'markcardCode'
                }, {
                    header : '填写人',
                    width : 60,
                    sortable : true,
                    dataIndex : 'modifyBy',
                    hidden : true
                }, {
                    header : '填写日期',
                    width : 60,
                    sortable : true,
                    dataIndex : 'modifyDate',
                    renderer : renderDate
                    
                }],
        viewConfig : {
            forceFit : true
        },
        tbar : ['标识牌类型：', markcardTypeCbo,
        		{xtype : "tbseparator"}, '编码模糊查询：', fuuzy,
        		{xtype : "tbseparator"}, queryBtn,
        		{xtype : "tbseparator"}, addBtn,
        		{xtype : "tbseparator"}, updateBtn,
        		{xtype : "tbseparator"}, deleteBtn],
        //分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : searchStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
       sm : sm,
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false,
       autoExpandColumn:2
    });
    
    // 双击一行，弹出修改画面        
  	runGrid.on("rowdblclick", updateRecord);
  	
  	// 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : 'border',
        border : false,
        items : [{
            title : '',
            region : 'center',
            layout : 'fit',
            border : false,
            margins : '0 0 0 0',
            split : true,
            collapsible : false,
            items : [runGrid]
        }]
    });
                
   	//***************************处理**************************//
    
    // 查询处理        
    function  queryRecord() {
    	var valueStr = markcardTypeCbo.value;
		if(valueStr == undefined || valueStr==""){
			Ext.MessageBox.alert(Constants.NOTICE,"请选择一个标识牌类型");
			return;
		}
    	searchStore.baseParams = {
			fuzzy : fuuzy.getValue(),
			markcardTypeID : markcardTypeCbo.value
		};
		searchStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
    }
                
    // 修改处理
  	function updateRecord()
	{
		if (runGrid.selModel.hasSelection()) {
			var records = runGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_COMPLEX_MSG);
			} else {
				addPanel.getForm().reset();
				var record = runGrid.getSelectionModel().getSelected();
				markcardTypeidCbo.setValue(record.get("markcardTypeId"));
				markcardTypeidCbo.setDisabled(true);
				win.show();
			    // 显示该行记录
				addPanel.getForm().loadRecord(record);
			}
		} else {
			Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	
	// 删除处理
	function deleteRecord()
	{
	    var smodel = runGrid.getSelectionModel();
		var selected = smodel.getSelections();
		var ids = [];
		var codes = [];
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_NULL_DEL_MSG);
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.markcardId) {
					ids.push(member.markcardId); 
					codes.push(member.markcardCode);
				} else {
				}
			}
			
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request(Constants.POST,
							'workticket/deleteMultiMackcard.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                                   	queryRecord();
						         	
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