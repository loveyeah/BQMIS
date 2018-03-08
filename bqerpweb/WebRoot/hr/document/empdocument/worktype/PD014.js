// 画面：档案管理/员工档案/工种维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
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
    // grid中的数据 工种ID,工种名称,工种类别,检索码,显示顺序
    var runGridList = new Ext.data.Record.create([{
            name : "typeOfWorkId"
        }, {
            name : "typeOfWorkName"
        }, {
            name : "typeOfWorkType"
        }, {
            name : "retrieveCode"
        }, {
            name : "orderBy"
        }]);

    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'hr/getWortTypeList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
//    runGridStore.setDefaultSort('typeOfWorkId', 'ASC');
    // 初始化时,显示数据
    runGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });

    // 运行执行的Grid主体 (行号,工种ID,工种名称,工种类别,检索码,显示顺序)
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        columns : [
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : '工种ID',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'typeOfWorkId'
            }, {
                header : '工种名称',
                width : 200,
                align : 'left',
                sortable : true,
                dataIndex : 'typeOfWorkName'
            }, {
                header : '工种类别',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'typeOfWorkType',
                renderer : function(value) {
                    if (value == "1") {
                        value = "特殊工种";
                    } else if (value == "2") {
                        value = "行业工种";
                    } else if (value == "3") {
                        value = "社会工种";
                    } else
                        value = "";
                    return value;
                }
            }, {
                header : '检索码',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'retrieveCode'
            }, {
                header : '显示顺序',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'orderBy'
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
    runGrid.on("rowdblclick", updateRecord);

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

    // 弹出窗口项目
    var THEWIDTH = 180;
    // 定义form中的工种ID: 新增: "自动生成" 修改: 动态加载
    var txtTypeID = new Ext.form.TextField({
        fieldLabel : '工种ID',
        value : Constants.AUTO_CREATE,
        width : THEWIDTH,
        // readOnly : true
        disabled : true
    })
    var hidTypeID = {
        id : "typeOfWorkId",
        xtype : "hidden",
        name : "workTypeBeen.typeOfWorkId"
    }
    // 定义form中的工种名称: 新增: "" 修改: 动态加载
    var txtTypeNAME = new Ext.form.TextField({
        id : "typeOfWorkName",
        fieldLabel : '工种名称<font color ="red">*</font>',
        maxLength : 50,
        name : 'workTypeBeen.typeOfWorkName',
        allowBlank : false,
        width : THEWIDTH
    })
    // 定义form中的工种类别: 新增: "" 修改: 动态加载
    var drpTypeTYPE = new Ext.form.CmbHRCode({
        fieldLabel : '工种类别<font color ="red">*</font>',
        type : "工种类别",
        allowBlank : false,
        id : 'typeOfWorkType',
        hiddenName : 'workTypeBeen.typeOfWorkType'
    })
    // 定义form中的检索码: 新增: "" 修改: 动态加载
    var txtRetrieveCode = new Ext.form.CodeField({
        id : "retrieveCode",
        xtype : "textfield",
        fieldLabel : '检索码',
        name : 'workTypeBeen.retrieveCode',
        maxLength : 20,
        width : THEWIDTH
    })
    // 定义form中的显示顺序: 新增: "" 修改: 动态加载
    var txtOrderBy = new Ext.form.NaturalNumberField({
        allowNegative : false,
        allowDecimals : false,
        allowZero : false,
        width : THEWIDTH,
        fieldLabel : "显示顺序",
        maxLength : 10,
        minValue : 0,
        id : 'orderBy',
        allowBlank : true,
        name : 'workTypeBeen.orderBy'
    })
    // 定义form中的addFlag:新增：1 修改：0
    var addFlag = {
        id : "addFlag",
        xtype : "hidden",
        value : "",
        name : "addFlag"
    }
    // 定义弹出窗体中的form
    var mypanel = new Ext.FormPanel({
        labelAlign : 'right',
        autoHeight : true,
        layout : "form",
        frame : true,
        items : [addFlag, txtTypeID, hidTypeID, txtTypeNAME, drpTypeTYPE, txtRetrieveCode, txtOrderBy]

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
                handler : function() {
                    var myurl = "";
                    if (Ext.get("addFlag").dom.value == "1") {
                        // 新增url
                        myurl = "hr/addWorkType.action";
                    } else {
                        // 修改url
                        myurl = "hr/updateWorkType.action";
                    }
                    // 画面check
                    // 必须输入项check
                    var nullMsg = "";
                    if (Ext.get("typeOfWorkName").dom.value == null || Ext.get("typeOfWorkName").dom.value == "") {
                        nullMsg = nullMsg + String.format(Constants.COM_E_002, "工种名称");
                        txtTypeNAME.markInvalid();
                    }
                    if (Ext.get("typeOfWorkType").dom.value == null || Ext.get("typeOfWorkType").dom.value == "") {
                        if (nullMsg != "") {
                            nullMsg = nullMsg + "<br/>";
                        }
                        nullMsg = nullMsg + String.format(Constants.COM_E_003, "工种类别");
                        drpTypeTYPE.markInvalid();
                    }
                    if (nullMsg != "") {
                        Ext.Msg.alert(Constants.ERROR, nullMsg);
                        return;
                    }
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                        if (buttonobj == "yes") {
                            // 画面控件是否红线
                            if (!mypanel.getForm().isValid()) {
                                return;
                            }
                            mypanel.getForm().submit({
                                method : 'POST',
                                url : myurl,
                                success : function(form, action) {
                                    var result = eval("(" + action.response.responseText + ")");
                                    if (result.success) {
                                        if (result.flag == 1) {
                                            // 保存成功
                                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                                // 隐藏弹出窗口
                                                win.hide();
                                            });
                                            // 刷新父画面
                                            runGridStore.load({
                                                params : {
                                                    start : 0,
                                                    limit : Constants.PAGE_SIZE
                                                }
                                            });
                                        } else if (result.flag == 0) {
                                            // 保存失败
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                        }
                                        // else if(result.flag == 2){
                                        // // 保存失败
                                        // Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                        // "工种名称已经存在!");
                                        // }
                                    }
                                },
                                failure : function() {
                                    // 保存失败
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                }
                            });
                        }
                    })
                }
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
        mypanel.getForm().reset();
        win.setTitle("新增工种");
        win.x = undefined;
        win.y = undefined;
        win.show();
        Ext.get("addFlag").dom.value = "1";
    }
    /**
     * 修改函数
     */
    function updateRecord() {
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
            var record = runGrid.getSelectionModel().getSelected();
            win.setTitle("修改工种");
            win.x = undefined;
            win.y = undefined;
            win.show();
            mypanel.getForm().loadRecord(record);
            Ext.get("addFlag").dom.value = "0";
            txtTypeID.setValue(record.get("typeOfWorkId"));
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
                    Ext.lib.Ajax.request(Constants.POST, 'hr/deleteWorkType.action', {
                        success : function(action) {
                            // 删除成功
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
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
                    }, 'workTypeBeen.typeOfWorkId=' + record.get('typeOfWorkId'));
                }
            })
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
});