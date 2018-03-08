Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();
    // 新增或修改,flag='0'更新;flag='1'增加
    var flag = '0';
    // 分页时每页显示记录条数
    var PAGE_SIZE = Constants.PAGE_SIZE;

    // 新增按钮
    var btnAdd = new Ext.Toolbar.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });

    // 修改按钮
    var btnUpdate = new Ext.Toolbar.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : recordUpdate
    });

    // 删除按钮
    var btnDelete = new Ext.Toolbar.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : recordDelete
    });

    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : [btnAdd, btnUpdate, btnDelete]
    });

    // 数据解析
    var recordMain = new Ext.data.Record.create([{
            name : 'inTypeId'
        }, {
            name : 'inType'
        }, {
            name : 'orderBy'
        }])

    // store
    var inTypeStore = new Ext.data.JsonStore({
        url : 'hr/getInTypeList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : recordMain
    });

    // grid中的列
    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '进厂类别ID',
            dataIndex : 'inTypeId',
            sortable : true

        }, {
            header : '进厂类别',
            dataIndex : 'inType',
            sortable : true

        }, {
            header : '显示顺序',
            dataIndex : 'orderBy',
            sortable : true
        }])

    // 选择模式
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // grid
    var inTypeGrid = new Ext.grid.GridPanel({
        border : true,
        sm : sm,
        fitToFrame : true,
        cm : colms,
        store : inTypeStore,
        tbar : headTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : PAGE_SIZE,
            store : inTypeStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        enableColumnMove : false
    });

    // 注册双击事件
    inTypeGrid.on("rowdblclick", recordUpdate);

    // 页面初期化数据加载
    inTypeStore.load({
        params : {
            start : 0,
            limit : PAGE_SIZE
        }
    });

    // 定义form中的单位名称
    // 进厂类别ID
    var txtInTypeId = new Ext.form.TextField({
        id : "InTypeId",
        name : 'inTypeId',
        fieldLabel : "进厂类别ID",
        allowBlank : false,
        disabled : true,
        width : 180
    });

    // 进厂类别
    var txtInType = new Ext.form.TextField({
        id : "InType",
        name : 'inType',
        fieldLabel : "进厂类别<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 10,
        width : 180
    });

    // 显示顺序
    var txtOrderBy = new Ext.form.NaturalNumberField({
        id : "OrderBy",
        name : 'orderBy',
        fieldLabel : "显示顺序",
        maxLength : 10,
        width : 180
    });

    // 弹出窗口的panel
    var upPanel = new Ext.FormPanel({
        labelAlign : 'right',
        frame : true,
        items : [txtInTypeId, txtInType, txtOrderBy]
    });

    // 弹出窗口
    var win = new Ext.Window({
        width : 350,
        height : 160,
        modal : true,
        title : '',
        buttonAlign : "center",
        resizable : false,
        closeAction : 'hide',
        items : [upPanel],
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : confirmRecord
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    // 确认取消信息
                    Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    })
                }
            }],
        layout : 'fit'
    });

    // 保存button按下的操作
    function confirmRecord() {
        // 判断进厂类别是否为空
        if (isNotNull()) {
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_001, function(buttonobj) {
                if (buttonobj == 'yes') {
                    // 按下“yes”按钮后，检查form里的textfield的长度是否超过最大长度
                    if (!txtInType.isValid() || !txtOrderBy.isValid()) {
                        return;
                    } else {
                        // 新增按钮后的操作
                        if (flag == "0") {
                            recordAdd();
                        }
                        // 修改按钮后的操作
                        if (flag == "1") {
                            recordEdit();
                        }
                    }
                }
            });
        }
    }

    // 保存按钮后的修改处理
    function recordEdit() {
        var myurl = 'hr/updateInType.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                inTypeStore.load({
                    params : {
                        start : 0,
                        limit : PAGE_SIZE
                    }
                });
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                    win.hide();
                });
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                win.hide();
            },
            params : {
                // 向后台传递参数
                inTypeId : txtInTypeId.getValue(),
                inType : txtInType.getValue(),
                orderBy : txtOrderBy.getValue()
            }
        });
    }

    // 保存按钮后的增加处理
    function recordAdd() {
        var myurl = 'hr/addInType.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                inTypeStore.load({
                    params : {
                        start : 0,
                        limit : PAGE_SIZE
                    }
                });
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                    win.hide();
                });
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                win.hide();
            },
            params : {
                // 向后台传递参数
                inType : txtInType.getValue(),
                orderBy : txtOrderBy.getValue()
            }
        });
    }

    // 新增按钮按下
    function addRecord() {
        flag = '0';
        upPanel.getForm().reset();
        // 弹出编辑窗口
        win.x = undefined;
        win.y = undefined;
        txtInTypeId.setValue(Constants.AUTO_CREATE);
        win.show();
        win.setTitle("新增进厂类别");
    }

    // 修改按钮按下
    function recordUpdate() {
        // 是否选择一行
        if (inTypeGrid.selModel.hasSelection()) {
            flag = '1';
            var record = inTypeGrid.getSelectionModel().getSelected();
            win.setTitle("修改进厂类别");
            // 弹出编辑窗口
            win.x = undefined;
            win.y = undefined;
            win.show();
            // form表单初始化
            upPanel.getForm().loadRecord(record);
            change = txtInTypeId.getValue();
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 删除信息
    function recordDelete() {
        // 是否选择一行
        if (inTypeGrid.selModel.hasSelection()) {
            var record = inTypeGrid.getSelectionModel().getSelected();
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002, function(buttonobj) {
                if (buttonobj == 'yes') {
                    var myurl = 'hr/deleteInType.action';
                    Ext.Ajax.request({
                        method : 'POST',
                        url : myurl,
                        success : function(result, request) {
                            inTypeStore.load({
                                params : {
                                    start : 0,
                                    limit : PAGE_SIZE
                                }
                            });
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG, MessageConstants.DEL_SUCCESS);
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        },
                        params : {
                            // 向后台传递参数
                            inTypeId : record.get("inTypeId")
                        }
                    });
                }
            });
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 非空check
    function isNotNull() {
        var msg = "";
        // 姓名判断数据是否为空
        if (txtInType.getValue() == "") {
            msg += String.format(MessageConstants.COM_E_002, "进厂类别");
        }
        // 错误有无判断
        if (msg != "") {
            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
            return false;
        } else {
            return true;
        }
    }
    // ↑↑********弹出窗口*********↑↑//

    var layout = new Ext.Viewport({
        layout : 'fit',
        enableTabScroll : true,
        border : false,
        items : [inTypeGrid]
    });
})
