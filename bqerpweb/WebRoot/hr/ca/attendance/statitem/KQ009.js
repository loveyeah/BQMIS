// 画面：考勤管理/考勤合计项维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 取消排序flag(仅在新增，修改，删除，查询后，初始化时取消排序)
    var cancelSortFlag = false;
    // 标记：新增/选择/成功
    FLAG_1 = "1";
    // 标记：修改/修改初始化/失败
    FLAG_0 = "0";
    // 新增/修改标记
    var addFlag = "";
    // 选择/修改初始化flag
    var selectFlag = "";
    var strStatId = "";
    var strStatItemName = "";
    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
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

    // grid中的数据 流水号,ID,合计项名称,合计项别名,合计项类型,单位,表示顺序,是否使用
    var runGridList = new Ext.data.Record.create([{
            name : "statId"
        }, {
            name : "statItemId",
            type : 'float'
        }, {
            name : "statItemName"
        }, {
            name : "statItemByname"
        }, {
            name : "statItemType"
        }, {
            name : "orderBy"
        }, {
            name : "useFlg"
        }, {
            name : "removeSort"
        }]);

    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'ca/getStatItemList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList),
        listeners : {
            loadexception : function(ds, records, o) {
                var result = eval("(" + o.responseText + ")");
                var succ = result.msg;
                if (succ == Constants.SQL_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                } else if (succ == Constants.DATE_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                }
            }

        }
    });
    // 初始化时,显示数据
    runGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    runGridStore.setDefaultSort('orderBy')
    runGridStore.on("beforeload", function() {
        // 仅在新增，修改，删除，查询后，初始化时取消排序
        if (cancelSortFlag) {
            // 页面刷新后grid排序状态恢复到画面初始化状态
//            runGridStore.sortInfo = null;
            runGrid.getView().removeSortIcon();
            cancelSortFlag = false;
        }
    })
    // 运行执行的Grid主体 (ID,合计项名称,合计项别名,合计项类型,单位,表示顺序,是否使用)
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        columns : [
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35,
                dataIndex : 'removeSort'
            }), {
                header : '对应合计项ID',
                width : 80,
                align : 'left',
                sortable : true,
                hidden : true,
                dataIndex : 'statItemId'
            }, {
                header : '合计项名称',
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'statItemName'
            }, {
                header : '合计项别名',
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'statItemByname'
            }, {
                header : '合计项类型',
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'statItemType',
                renderer : function(value) {
                    if (value == "1") {
                        value = caCodeDefine['考勤合计项类型'][1].text;
                    } else if (value == "2") {
                        value = caCodeDefine['考勤合计项类型'][2].text;
                    } else if (value == "3") {
                        value = caCodeDefine['考勤合计项类型'][3].text;
                    } else if (value == "4") {
                        value = caCodeDefine['考勤合计项类型'][4].text;
                    } else
                        value = "";
                    return value;
                }
            }, {
                header : '显示顺序',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'orderBy'
            }, {
                header : '是否使用',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'useFlg',
                renderer : function(value) {
                    if (value == "1") {
                        value = caCodeDefine['合计项是否使用标志'][0].text;
                    } else if (value == "0") {
                        value = caCodeDefine['合计项是否使用标志'][1].text;
                    } else
                        value = "";
                    return value;
                }
            }],
        viewConfig : {
            forceFit : false
        },
        tbar : [addBtn, updateBtn, deleteBtn],
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
    });

    // 注册双击事件
    runGrid.on("rowdblclick", dbUpdateRecord);
    /**
     * 双击事件选中则进入修改页面
     */
    function dbUpdateRecord() {
        if (runGrid.selModel.hasSelection()) {
            updateRecord();
        }
    }

    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [{
                xtype : "panel",
                region : 'center',
                layout : 'fit',
                border : false,
                items : [runGrid]
            }]
    });

    // 弹出窗口项目 合计项类型,合计项名称 ,合计项别名,单位,表示顺序,是否使用
    var THEWIDTH = 180;
    // 定义form中的合计项类型:
    var drpType = new Ext.form.CmbCACode({
        fieldLabel : '合计项类型<font color ="red">*</font>',
        type : "考勤合计项类型",
        allowBlank : false,
        id : 'statItemType',
        listeners : {
            select : drpTypeSelected
        }
    })

    // 合计项名称下拉框数据源from DB
    var statItemNameStore = new Ext.data.JsonStore({
        url : 'ca/getStatNameListByTypeCode.action',
        root : 'list',
        fields : [{
                name : 'statItemName'
            }, {
                name : 'statItemId'
            }]
    });
    // 合计项名称下拉框数据源from code定义书
    var staticData = {
        list : [{
                statItemId : '',
                statItemName : ''
            }, {
                statItemId : '0',
                statItemName : caCodeDefine['出勤类别'][1].text
            }, {
                statItemId : '1',
                statItemName : caCodeDefine['出勤类别'][2].text
            }, {
                statItemId : '2',
                statItemName : caCodeDefine['出勤类别'][3].text
            }, {
                statItemId : '3',
                statItemName : caCodeDefine['出勤类别'][4].text
            }, {
                statItemId : '4',
                statItemName : caCodeDefine['出勤类别'][5].text
            }, {
                statItemId : '5',
                statItemName : caCodeDefine['出勤类别'][6].text
            }, {
                statItemId : '6',
                statItemName : caCodeDefine['出勤类别'][7].text
            }]
    };

    // 定义form中的合计项名称:
    var drpName = new Ext.form.ComboBox({
        fieldLabel : '合计项名称<font color ="red">*</font>',
        width : THEWIDTH,
        id : 'statItemId',
        allowBlank : false,
        triggerAction : 'all',
        store : statItemNameStore,
        displayField : 'statItemName',
        valueField : 'statItemId',
        mode : 'local',
        readOnly : true,
        listeners : {
            select : drpNameSelected
        }
    })
    // 定义form中的合计项别名:
    var txtByName = new Ext.form.TextField({
        id : "statItemByname",
        fieldLabel : '合计项别名',
        maxLength : 10,
        allowBlank : true,
        width : THEWIDTH
    })
    // 定义form中的表示顺序: 新增: "" 修改: 动态加载
    var txtOrderBy = new Ext.form.NaturalNumberField({
        allowNegative : false,
        allowDecimals : false,
        allowZero : false,
        width : THEWIDTH,
        fieldLabel : "显示顺序",
        maxLength : 10,
        minValue : 0,
        id : 'orderBy',
        allowBlank : true
    })
    // 定义form中的是否使用:新增(默认：否，无空选项)
    var drpUseFlg = new Ext.form.CmbCACode({
        fieldLabel : '是否使用',
        type : "合计项是否使用标志",
        value : 0,
        allowBlank : true,
        id : 'useFlg'
    })
    // 隐藏ID
    var hidStatId = {
        id : "statId",
        xtype : "hidden"
    }
    // 定义弹出窗体中的form
    var mypanel = new Ext.FormPanel({
        labelAlign : 'right',
        autoHeight : true,
        layout : "form",
        frame : true,
        items : [hidStatId, drpType, drpName, txtByName, txtOrderBy, drpUseFlg]
    });
    // 定义弹出窗体
    var win = new Ext.Window({
        width : 350,
        autoHeight : 100,
        buttonAlign : "center",
        items : [mypanel],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : saveRecord
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    });
                }
            }]
    });
    /**
     * 增加函数
     */
    function addRecord() {
        // 去除合计项名称store
        statItemNameStore.removeAll();
        mypanel.getForm().reset();
        win.setTitle("新增考勤合计项");
        win.x = undefined;
        win.y = undefined;
        win.show();
        addFlag = FLAG_1;
    }
    /**
     * 修改函数
     */
    function updateRecord() {
        selectFlag = FLAG_0;
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
            var record = runGrid.getSelectionModel().getSelected();
            win.setTitle("修改考勤合计项");
            win.x = undefined;
            win.y = undefined;
            win.show();
            var statTypeCode = record.get('statItemType');
            var id = record.get('statItemId');
            if (statTypeCode == "4") {
                statItemNameStore.loadData(staticData, false);
                mypanel.getForm().loadRecord(record);
                drpName.setValue(id, true);
                if (id == "0") {
                    drpName.setValue("0", true);
                }
                strStatItemName = record.get('statItemName');
                drpName.clearInvalid();
            } else {
                statItemNameStore.load({
                    params : {
                        typeCode : record.get('statItemType')
                    }
                });
            }
            statItemNameStore.on("load", function(e, records, o) {
                if (selectFlag == FLAG_0) {
                    drpType.setValue(record.get('statItemType'), true);
                    drpName.setValue(id + "", true);
                    strStatItemName = record.get('statItemName');
                    drpType.clearInvalid();
                    drpName.clearInvalid();
                }
            });
            addFlag = FLAG_0;
            // 加载数据
            mypanel.getForm().loadRecord(record);
            drpType.setValue(record.get('statItemType'), true);
            drpName.setValue(id + "", true);
            strStatItemName = record.get('statItemName');
            drpType.clearInvalid();
            drpName.clearInvalid();
            txtByName.setValue(record.get('statItemByname'));
            txtOrderBy.setValue(record.get('orderBy'));
            drpUseFlg.setValue(record.get('useFlg'), true);
            mypanel.getForm().clearInvalid();
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 删除函数
     */
    function deleteRecord() {
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
            var record = runGrid.getSelectionModel().getSelected();
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.lib.Ajax.request(Constants.POST, 'ca/deleteStatItem.action', {
                        success : function(action) {
                            // 删除成功
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                            // 取消排序
                            cancelSortFlag = true;
                            runGridStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });

                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        }
                    }, 'statId=' + record.get('statId'));
                }
            })
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 保存处理
     */
    function saveRecord() {
        // 画面check
        if (!check()) {
            return;
        }
        if (addFlag == FLAG_0) {
            // 修改
            strStatId = Ext.get("statId").dom.value;
        } else if (addFlag == FLAG_1) {
            // 新增
            strStatId = "";
        }
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 画面控件是否红线
                if (!mypanel.getForm().isValid()) {
                    return;
                };
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'ca/saveOrUpdateStatItem.action',
                    params : {
                        // 流水号,合计项类型,合计项名称id,合计项名称 ,合计项别名,单位,表示顺序,是否使用
                        statId : strStatId,
                        statItemType : drpType.getValue(),
                        statItemId : drpName.getValue(),
                        statItemName : strStatItemName,
                        statItemByname : txtByName.getValue(),
                        orderBy : txtOrderBy.getValue(),
                        useFlg : drpUseFlg.getValue(),
                        addOrUpdate : addFlag
                    },
                    success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var suc = o.flag;
                            // 如果成功
                            if (suc == FLAG_0) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else if (suc == FLAG_1) {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    // 隐藏弹出窗口
                                    win.hide();
                                });
                                // 成功,则重新加载grid里的数据
                                // 取消排序
                                cancelSortFlag = true;
                                // 重新加载数据
                                runGridStore.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE
                                    }
                                });
                            } else {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014)
                            }
                        }
                    }
                });
            }
        })
    }
    /**
     * 画面check
     */
    function check() {
        // 必须输入项check
        var nullMsg = "";
        // 合计项类型 必须输入项check
        if (drpType.getValue() == null || drpType.getValue() == "") {
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "合计项类型");
        }
        // 合计项名称 必须输入项check
        if (drpName.getValue() == null || drpName.getValue() == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "合计项名称");
        }
        if (nullMsg != "") {
            Ext.Msg.alert(Constants.ERROR, nullMsg);
            return false;
        }
        return true;
    }
    // 合计项名称选择后处理
    function drpNameSelected(combo, record) {
        strStatItemName = record.get('statItemName');
    }

    /**
     * 合计项类型选择后处理：动态加载合计项名称
     */
    function drpTypeSelected() {
        selectFlag = FLAG_1;
        // 获得选择的合计项类型
        var statTypeCode = drpType.getValue();
        if (statTypeCode == "") {
            // 去除合计项名称store
            drpName.store.removeAll();
            drpName.setValue("");
            drpName.clearInvalid();
            return;
        }
        // 选择“出勤”时
        if (statTypeCode == "4") {
            statItemNameStore.loadData(staticData, false);
            drpName.setValue("");
            strStatItemName = "";
            drpName.clearInvalid();
        } else {
            drpName.store = statItemNameStore;
            statItemNameStore.load({
                params : {
                    typeCode : statTypeCode
                }
            });
            statItemNameStore.on("load", function(e, records, o) {
                if (selectFlag == FLAG_1) {
                    drpName.setValue("");
                    strStatItemName = "";
                    drpName.clearInvalid();
                }
            });
        }
    }
});