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

    var recordMain = new Ext.data.Record.create([{
            name : 'outTypeId'
        }, {
            name : 'outTypeType'
        }, {
            name : 'orderBy'
        }])

    // store
    var outTypeStore = new Ext.data.JsonStore({
        url : 'hr/getOutType.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : recordMain
    });

    // grid中的列
    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '离职类别ID',
            dataIndex : 'outTypeId',
            sortable : true
        }, {
            header : '离职类别',
            dataIndex : 'outTypeType',
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
    var outTypeGrid = new Ext.grid.GridPanel({
        border : true,
        sm : sm,
        fitToFrame : true,
        border : true,
        cm : colms,
        layout : 'fit',
        store : outTypeStore,
        tbar : headTbar,
        enableColumnMove : false,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : PAGE_SIZE,
            store : outTypeStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });

    // 注册双击事件
    outTypeGrid.on("rowdblclick", recordUpdate);

    outTypeStore.load({
        params : {
            start : 0,
            limit : PAGE_SIZE
        }
    });

    // 定义form中的单位名称
    // 离职类别ID
    var typeIDField = new Ext.form.TextField({
        id : "outTypeId",
        name : 'outTypeId',
        fieldLabel : "离职类别ID",
        allowBlank : false,
        disabled : true,
        width : 180
    });

    // 离职类别
    var typeField = new Ext.form.TextField({
        id : "outTypeType",
        name : 'outTypeType',
        fieldLabel : "离职类别<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 20,
        width : 180
    });

    // 显示顺序
    var orderField = new Ext.form.NaturalNumberField({
        id : "orderBy",
        name : 'orderBy',
        fieldLabel : "显示顺序",
        maxLength : 10,
        width : 180
    });

    var upPanel = new Ext.FormPanel({
        labelAlign : 'right',
        frame : true,
        items : [typeIDField, typeField, orderField]
    });

    var win = new Ext.Window({
        width : 350,
        height : 160,
        modal : true,
        title : '',
        buttonAlign : "center",
        resizable : false,
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
        layout : 'fit',
        closeAction : 'hide'
    });
    // 保存button压下的操作
    function confirmRecord() {
        if (isNotNull()) {
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_001, function(buttonobj) {
                if (buttonobj == 'yes') {
                    if (!typeField.isValid() || !orderField.isValid()) {
                        return;
                    } else {

                        if (flag == "0") {
                            recordAdd();
                        }
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
        var myurl = 'hr/updateOutType.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                if ("notfind" == result.responseText) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                } else {
                    outTypeStore.load({
                        params : {
                            start : 0,
                            limit : PAGE_SIZE
                        }
                    });
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                        win.hide();
                    });
                }
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                win.hide();
            },
            params : {
                // 向后台传递参数
                outId : typeIDField.getValue(),
                type : typeField.getValue(),
                order : orderField.getValue()
            }
        });
    }

    // 保存按钮后的增加处理
    function recordAdd() {
        var myurl = 'hr/addOutType.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                outTypeStore.load({
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
                type : typeField.getValue(),
                order : orderField.getValue()
            }
        });
    }

    // 修改信息
    function recordUpdate() {
        if (outTypeGrid.selModel.hasSelection()) {
            flag = '1';
            var record = outTypeGrid.getSelectionModel().getSelected();
            win.setTitle("修改离职类别");
            // 弹出编辑窗口
            win.x = undefined;
            win.y = undefined;
            win.show();
            // form表单初始化
            upPanel.getForm().loadRecord(record);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 删除信息
    function recordDelete() {
        if (outTypeGrid.selModel.hasSelection()) {
            var record = outTypeGrid.getSelectionModel().getSelected();
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002, function(buttonobj) {
                if (buttonobj == 'yes') {
                    var myurl = 'hr/deleteOutType.action';
                    Ext.Ajax.request({
                        method : 'POST',
                        url : myurl,
                        success : function(result, request) {
                            if ("notfind" == result.responseText) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                     	       } else {
                                outTypeStore.load({
                                    params : {
                                        start : 0,
                                        limit : PAGE_SIZE
                                    }
                                });
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        },
                        params : {
                            // 向后台传递参数
                            outId : record.get("outTypeId")
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
        // 离职类别判断数据是否为空
        if (typeField.getValue() == "") {
            msg += String.format(MessageConstants.COM_E_002, "离职类别");
        }
        // msg是否为空判断
        if (msg != "") {
            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
            return false;
        } else {
            return true;
        }

    }
    // ↑↑********弹出窗口*********↑↑//

    // 点新增弹出窗口
    function addRecord() {
        flag = '0';
        upPanel.getForm().reset();
        win.x = undefined;
        win.y = undefined;
        win.show();
        typeIDField.setValue(Constants.AUTO_CREATE);
        win.setTitle("新增离职类别");
    }

    var layout = new Ext.Viewport({
        layout : 'fit',
        enableTabScroll : true,
        border : false,
        items : [outTypeGrid]
    });

})
