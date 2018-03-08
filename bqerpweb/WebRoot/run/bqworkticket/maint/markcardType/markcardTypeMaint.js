Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    //==============  定义grid ===============
    var MyRecord = Ext.data.Record.create([
        {name : 'markcardTypeId'},
        {name : 'markcardTypeName'},
        {name : 'modifyBy'},
        {name : 'modifyDate'}
    ]);

    var dataProxy = new Ext.data.HttpProxy({
        // 查询标识牌信息
        url:'workticket/getMarkcardTypeList.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
    
    store.load();

    var sm = new Ext.grid.CheckboxSelectionModel();

    function renderDate(value) {
        if (!value) return "";
        
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var strDate = value.match(reDate);
        return strDate;
    }
    
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,

        columns : [
            sm, {
                header : "标识牌类型ID",
                width : 75,
                sortable : true,
                dataIndex : 'markcardTypeId'
            },
    
            {
                header : "标识牌类型名称",
                width : 75,
                sortable : true,
                dataIndex : 'markcardTypeName'
            },
    
            {
                header : "填写日期",
                width : 75,
                sortable : true,
                dataIndex : 'modifyDate',
                renderer : renderDate
            },
    
            {
                header : "填写人",
                hidden : true,
                dataIndex : 'modifyBy'
            }
        ],
        sm : sm,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },

        tbar : [{
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

    grid.on("rowdblclick", updateRecord);

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });

    //=============  定义弹出画面  ===============
    var wd = 180;

    var markcardTypeId = {
        id : "markcardTypeId",
        xtype : "textfield",
        fieldLabel : '标识牌ID',
        value : Constants.AUTO_CREATE,
        width : wd,
        readOnly : true
    }
    
    var hiddenTypeId = {
        id : "hiddenTypeId",
        xtype : "hidden",
        value: '0',
        name : 'cardType.markcardTypeId'
    }
    
    var markcardTypeName = {
        id : "markcardTypeName",
        xtype : "textfield",
        fieldLabel : "标识牌名称<font color='red'>*</font>",
        allowBlank : false,
        width : wd,
        name : 'cardType.markcardTypeName',
        maxLength : 30
    };

    var modifyBy = {
        id : "modifyBy",
        xtype : "hidden",
        name : 'cardType.modifyBy'
    }

    var modifyShowDate = {
        id : "modifyShowDate",
        xtype : "textfield",
        format : 'Y-m-d H:i:s',
        fieldLabel : '填写日期',
        name : 'cardType.modifyDate',
        width : wd,
        readOnly : true
    }
    
    var hiddenDate = {
        id : 'modifyDate',
        xtype : 'hidden'
    }

    var myaddpanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        items : [markcardTypeId, hiddenTypeId, markcardTypeName,
            modifyBy, modifyShowDate, hiddenDate]

    });

    var win = new Ext.Window({
        width : 350,
        height : 160,
        buttonAlign : "center",
        items : [myaddpanel],
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        buttons : [{
            text : Constants.BTN_SAVE,
            iconCls : 'save',
            handler : function() {
                var myurl="";
                var cardTypeId = Ext.get("markcardTypeId").dom.value;

                if (cardTypeId == Constants.AUTO_CREATE) {
                    myurl = "workticket/addMarkcardType.action";
                    Ext.get("hiddenTypeId").dom.value = "-1";
                } else {
                    myurl = "workticket/updateMarkcardType.action";
                    // 用于后台查询
                    Ext.get("hiddenTypeId").dom.value = cardTypeId;
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
            iconCls: Constants.CLS_CANCEL,
            handler : function() {
                win.hide();
            }
        }]
    });

    // 增加
    function addRecord()
    {
        myaddpanel.getForm().reset();
		win.setTitle("增加标识牌信息");
        win.show();
    }
    
    function updateRecord()
    {
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
            } else {
                var record = grid.getSelectionModel().getSelected();
                win.setTitle("修改标识牌信息");
				win.show();
                // 显示该行记录
                myaddpanel.getForm().loadRecord(record);
                // 设置日期数据
                Ext.get("modifyShowDate").dom.value = renderDate(Ext.get("modifyDate").dom.value);
                }
        } else {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
        }
    }
    
    function deleteRecord()
    {
        var sm = grid.getSelectionModel();
        var selected = sm.getSelections();
        var ids = [];
        var names = [];
        if (selected.length == 0) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
        } else {

            for (var i = 0; i < selected.length; i += 1) {
                var member = selected[i].data;
                ids.push(member.markcardTypeId);
                names.push(member.markcardTypeName);
            }
            
            Ext.Msg.confirm(Constants.BTN_DELETE, "是否确定删除名称为" + names + "的记录？", function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        method : 'POST',
                        url : 'workticket/deleteMarkcardType.action',
                        success : function(action) {
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
							store.load();
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
                        },
                        params : {ids : ids.join(',')}
                    });
                }
            });
        }
    }
});