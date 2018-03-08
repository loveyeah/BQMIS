Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    //==============  定义grid ===============
    var MyRecord = Ext.data.Record.create([
        {name : 'conditionId'},
        {name : 'conditionName'},
        {name : 'orderBy'}
    ]);

    var dataProxy = new Ext.data.HttpProxy({
        // 工作条件信息
        url:'workticket/getWorkConditionList.action'
    });
	// json reader
    var theReader = new Ext.data.JsonReader({
        root : "list"
    }, MyRecord);
	// 工作条件列表
    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
    // 获取数据
    store.load();
    // 查询参数
    var fuuzy = new Ext.form.TextField({
        id : "fuzzy",
        name : "fuzzy"
    });
    // CheckBox多选框
    var sm = new Ext.grid.CheckboxSelectionModel();
    // 列表
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,
		// 数据列
        columns : [
            sm, {
                header : "编码",
                width : 75,
                sortable : true,
                dataIndex : 'conditionId'
            },
    
            {
                header : "名称",
                width : 75,
                sortable : true,
                dataIndex : 'conditionName'
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
	// 双击进入修改页面
    grid.on("dblclick", updateRecord);
	// 容器
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });

    //=============  定义弹出画面  ===============
    var wd = 180;
	// 编码
    var conditionId = {
        id : "conditionId",
        xtype : "textfield",
        fieldLabel : '编码',
        value : Constants.AUTO_CREATE,
        width : wd,
        readOnly : true
    }
    // 隐藏编码
    var hiddenContentId = {
        id : "hiddenContentId",
        xtype : "hidden",
        value: '0',
        name : 'workCondition.conditionId'
    }
    // 名称
    var conditionName = {
        id : "conditionName",
        xtype : "textfield",
        fieldLabel : "名称<font color='red'>*</font>",
        allowBlank : false,
        width : wd,
        name : 'workCondition.conditionName',
        maxLength : 30
    };
	// 显示排序
    var orderByShow = {
        id : "orderBy",
        xtype : "numberfield",
        fieldLabel : '显示顺序',
        width : wd,
        name : 'workCondition.orderBy',
        maxLength : 10
    }
    
    var myaddpanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        labelWidth : 80,
        items : [conditionId, hiddenContentId, conditionName, orderByShow]
 
    });
	// 弹出画面
    var win = new Ext.Window({
    	resizable : false,
        title : '工作条件维护',
        width : 320,
        height : 160,
        buttonAlign : "center",
        items : [myaddpanel],
        layout : 'fit',
        closeAction : 'hide',
        modal:true,
        // 保存按钮
        buttons : [{
            text : Constants.BTN_SAVE,
            iconCls : Constants.CLS_SAVE,
            handler : function() {
                if (!myaddpanel.getForm().isValid()) {
                    return false;
                }
                var myurl="";
                var contentIdValue = Ext.get("conditionId").dom.value;

                if (contentIdValue == Constants.AUTO_CREATE) {
                    myurl = "workticket/addWorkCondition.action";
                    Ext.get("hiddenContentId").dom.value = "-1";
                } else {
                    myurl = "workticket/updateWorkCondition.action";
                    // 用于后台查询
                    Ext.get("hiddenContentId").dom.value = contentIdValue;
                }

                myaddpanel.getForm().submit({
                    method : Constants.POST,
                    waitMsg : Constants.DATA_SAVING,
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
        	// 取消按钮
            text : Constants.BTN_CANCEL,
            iconCls : Constants.CLS_CANCEL,
            handler : function() {
                win.hide();
            }
        }]
    });
    
    // 查询
    function queryRecord() {
    	// 查询参数
        var fuzzytext = fuuzy.getValue();
        store.baseParams = {
            fuzzy : fuzzytext
        };
        store.load();
    }
    
    // 增加
    function addRecord()
    {
        myaddpanel.getForm().reset();

        win.show();
    }
    // 修改记录
    function updateRecord()
    {
    	// 选择记录与否
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            // 选择多行
            if (recordslen > 1) {
            	// 请选择其中一项进行编辑！"
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
            } else {
                var record = grid.getSelectionModel().getSelected();

                win.show();
                // 显示该行记录
                myaddpanel.getForm().loadRecord(record);
            }
        } else {
        	// 请选择要删除的记录！
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
        }
    }
    // 删除记录
    function deleteRecord()
    {
    	// 多选框
        var sm = grid.getSelectionModel();
        // 取得选择项
        var selected = sm.getSelections();
        var ids = [];
        if (selected.length == 0) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
        } else {

            for (var i = 0; i < selected.length; i += 1) {
                var member = selected[i].data;
                ids.push(member.conditionId);
            }
            // 是否确定删除id为" + Ids + "的记录？
            Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'workticket/deleteWorkCondition.action',
                        success : function(action) {
                        	// 删除成功！
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                               store.load();
                             
                        },
                        failure : function() {
                        	// 删除时出现未知错误.
                            Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
                        },
                        params : {ids : ids.join(',')}
                    });
                }
            });
        }
    }
});