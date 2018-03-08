Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    //==============  定义grid ===============
    var MyRecord = Ext.data.Record.create([
    	// 编码
        {name : 'firecontentId'},
        // 名称
        {name : 'firecontentName'},
        // 显示顺序
        {name : 'orderBy'}
    ]);

    var dataProxy = new Ext.data.HttpProxy({
        // 动火票内容
        url:'workticket/getFireContentList.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    // grid的store
    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
    
    // 加载数据
    store.load();
    
    // 输入框
    var fuuzy = new Ext.form.TextField({
        id : "fuzzy",
        name : "fuzzy"
    });
    
    // grid选择模式
    var sm = new Ext.grid.CheckboxSelectionModel();
    
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,

        columns : [
            sm, {
                header : "编码",
                width : 75,
                sortable : true,
                dataIndex : 'firecontentId'
            },
    
            {
                header : "名称",
                width : 75,
                sortable : true,
                dataIndex : 'firecontentName'
            },
    
            {
                header : "显示顺序",
                width : 75,
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
            }]
    });

    // 双击编辑
    grid.on("rowdblclick", updateRecord);

    // 显示区域
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });

    //=============  定义弹出画面  ===============
    var wd = 180;

    // 编码
    var firecontentId = {
        id : "firecontentId",
        xtype : "textfield",
        fieldLabel : '编码',
        value : Constants.AUTO_CREATE,
        width : wd,
        readOnly : true
    }
    
    var hiddenContentId = {
        id : "hiddenContentId",
        xtype : "hidden",
        value: '0',
        name : 'fireContent.firecontentId'
    }
    
    // 名称
    var firecontentName = {
        id : "firecontentName",
        xtype : "textfield",
        fieldLabel : '名称<font color="red">*</font>',
        allowBlank : false,
        width : wd,
        name : 'fireContent.firecontentName',
        maxLength : 30
    };

    // 显示顺序
    var orderByShow = {
        id : "orderBy",
        xtype : "numberfield",
        fieldLabel : '显示顺序',
        width : wd,
        name : 'fireContent.orderBy',
        maxLength : 10
    }
    
    // 弹出窗口panel
    var myaddpanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        defaults : {width : 300},
        items : [firecontentId, hiddenContentId, firecontentName, orderByShow]
    });

    // 弹出窗口
    var win = new Ext.Window({
        width : 350,
        height : 160,
        title : '动火票内容维护',
        buttonAlign : "center",
        resizable : false,
        modal : true,
        items : [myaddpanel],
        buttons : [{
            text : Constants.BTN_SAVE,
            iconCls : Constants.CLS_SAVE,
            handler : function() {
                if (!myaddpanel.getForm().isValid()) {
                    return false;
                }
                var myurl="";
                var contentIdValue = Ext.get("firecontentId").dom.value;

                if (contentIdValue == Constants.AUTO_CREATE) {
                	// 增加
                    myurl = "workticket/addFireContent.action";
                    Ext.get("hiddenContentId").dom.value = "-1";
                } else {
                	// 修改
                    myurl = "workticket/updateFireContent.action";
                    // 用于后台查询
                    Ext.get("hiddenContentId").dom.value = contentIdValue;
                }

                // 提交表单
                myaddpanel.getForm().submit({
                    method : Constants.POST,
                    waitMsg : Constants.DATA_SAVING,
                    url : myurl,
                    success : function(form, action) {
                        var o = eval("(" + action.response.responseText + ")");
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
                        // 刷新grid
                        store.load();
                        // 隐藏窗口
                        win.hide(); 
                    },
                    faliue : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                    }
                });
            }
        }, {
            text : Constants.BTN_CANCEL,
            iconCls:Constants.CLS_CANCEL,
            handler : function() {
                win.hide();
            }
        }],
        layout : 'fit',
        closeAction : 'hide'
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
     * 增加
	 */
    function addRecord()
    {
    	// 重置表单
        myaddpanel.getForm().reset();
		// 显示增加窗口
        win.show();
    }
    
    /**
     * 修改
	 */
    function updateRecord()
    {
    	// 判断有无选择记录
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
            } else {
                var record = grid.getSelectionModel().getSelected();

                win.show();
                // 显示该行记录
                myaddpanel.getForm().loadRecord(record);
            }
        } else {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
        }
    }
    
    /**
     * 删除
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
                ids.push(member.firecontentId);
            }
            
            Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        method : Constants.POST,
                        waitMsg : Constants.DATA_DEL,
                        url : 'workticket/deleteFireContent.action',
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