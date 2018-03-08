Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 定义数据格式
    var MyRecord = Ext.data.Record.create([{
            name : 'contractTermId'
        }, {
            // 合同有效期
            name : 'contractTerm'
        }, {
            // 显示顺序
            name : 'contractDisplayNo'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/getContractTerm.action'
    });

    // 定义格式化数据方式
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    // 定义封装缓存数据的对象
    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader,
        sortInfo : {//modify by drdu 090925
            field : "contractDisplayNo",
            direction : "ASC"
        }

    });
    // 加载store
    store.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    var pageBar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : store,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });
    // 定义gird
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : "合同有效期",
                dataIndex : 'contractTerm',
                sortable : true,
                width : 120
            }, {
                header : "显示顺序",
                dataIndex : 'contractDisplayNo',
                sortable : true,
                width : 100
            }, {
                hidden : true,
                sortable : true,
                dataIndex : "contractTermId"
            }],
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),

        // 头部工具栏
        tbar : [{
                text : Constants.BTN_ADD,
                iconCls : Constants.CLS_ADD,
                handler : addRecord
            }, {
                text : Constants.BTN_UPDATE,
                iconCls : Constants.CLS_UPDATE,
                handler : updateRecord
            }, {
                text : Constants.BTN_DELETE,
                iconCls : Constants.CLS_DELETE,
                handler : deleteRecord
            }],
        // 底部工具栏
        bbar : pageBar,
        enableColumnMove : false
    });

    // 定义合同有效期textbox
    var txtContractTerm = new Ext.form.TextField({
        fieldLabel : "合同有效期<font color='red'>*</font>",
        name : "contractTerm",
        maxLength : 5,
        anchor : '90%',
        allowBlank : false
    });
    // 定义显示顺序textbox
    var txtContractDisplayNo = new Ext.form.NaturalNumberField({
        fieldLabel : "显示顺序",
        name : "contractDisplayNo",
        anchor : '90%',
        maxLength : 10
    })
    var hidContractTermId = new Ext.form.Hidden({
        name : "contractTermId"
    });
    // 定义弹出窗口内部控件panel
    var myPanel = new Ext.FormPanel({
//        layout : 'form',
        height : 135,
        width : 350,
        labelAlign : 'right',
        frame : true,
        items : [txtContractTerm, txtContractDisplayNo, hidContractTermId]

    });
    // 定义增加、更新和删除的Action url
    var urlSend = "";
    // 定义“增加”和“修改”弹出窗体
    var win = new Ext.Window({
        width : 350,
        height : 135,
        title : "增加/修改合同有效期",
        buttonAlign : "center",
        items : [myPanel],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : function() {
                    // 判断是否为空
                    if (txtContractTerm.getValue().trim() == "") {
                        Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_002, "合同有效期"));

                        // 如果判断长度是否小于15
                    } else {
                        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                            if (buttonobj == "yes") {
                                if (myPanel.getForm().isValid()) {
                                    saveData();
                                }
                            }
                        });
                    }
                }

            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
                        if (buttonobj == "yes") {
                            win.hide();
                        }
                    });
                }
            }]
    });

    // 注册双击事件
    grid.on("rowdblclick", updateRecord);

    // 修改合同有效期
    function updateRecord() {
        // 是否有被选项
        if (grid.selModel.hasSelection()) {
            var record = grid.getSelectionModel().getSelected();
            win.x = undefined;
            win.y = undefined;
            win.show();
            // 重命名弹出窗口名称
            win.setTitle("修改合同有效期");
            urlSend = 'hr/updateContractTerm.action';
            // 将被选择的第一条数据加载给form
            myPanel.getForm().loadRecord(record);
            // 没有记录被选择，弹出提示框
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    // 新增合同有效期
    function addRecord() {
        win.x = undefined;
        win.y = undefined;
        win.show();
        win.setTitle("新增合同有效期");
        urlSend = 'hr/addContractTerm.action';
        myPanel.getForm().reset();
    }
    // 删除合同有效期
    function deleteRecord() {
        // 是否有被选项
        if (grid.selModel.hasSelection()) {
            // 获取被选记录的数据
            var record = grid.getSelectionModel().getSelected();
            // 获取数据中的合同有效期id
            var id = record.data.contractTermId;
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        url : 'hr/deleteContractTerm.action',
                        method : Constants.POST,
                        params : {
                            // 修改记录的id
                            'bean.contractTermId' : id
                        },
                        success : function(action) {
                            var result = eval("(" + action.responseText + ")");
                            if (result.msg == "S") {
                                // 如果删除成功，弹出删除成功信息
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                            } else if (result.msg == Constants.SQL_FAILURE) {
                                // 失败时，弹出提示
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            }
                            // 更新信息
                            store.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                        }
                    });
                }
            });
            // 没有记录被选择，弹出提示框
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 发送数据
    function saveData() {

        Ext.Ajax.request({
            url : urlSend,
            method : Constants.POST,
            params : {
                // 修改记录的id
                'bean.contractTermId' : hidContractTermId.getValue(),
                'bean.contractTerm' : txtContractTerm.getValue().trim(),
                'bean.contractDisplayNo' : txtContractDisplayNo.getValue()
            },
            success : function(action) {
                var result = eval("(" + action.responseText + ")");
                if (result.msg == 'S') {
                    // 如果增加成功，弹出保存成功信息
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                                // 隐藏弹出窗口
                                                win.hide();
                                            });
                } else if (result.msg == Constants.SQL_FAILURE) {
                    // 失败时，弹出提示
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                }
                // 更新信息
                store.load({
                    params : {
                        start : 0,
                        limit : Constants.PAGE_SIZE
                    }
                });
            }
        });
    }
    // 页面加载显示数据
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
});