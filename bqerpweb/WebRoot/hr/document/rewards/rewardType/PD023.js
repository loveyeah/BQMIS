Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    // 数据源--------------------------------
    var flag = 0;
    var width = 200;
    // 奖惩类别ID
    var rewardsPunishIdField = new Ext.form.TextField({
        fieldLabel : "奖惩类别ID",
        id : "rewardsPunishId",
        name : "rewardsPunishId",
        value : Constants.AUTO_CREATE,
        width : width
    });

    // 奖惩类别名称
    var rewardsPunishField = new Ext.form.TextField({
        fieldLabel : "奖惩类别名称<font color='red'>*</font>",
        id : "rewardsPunish",
        name : "rewardsPunish",
        allowBlank : false,
        width : width,
        maxLength : 25
    });

    // 奖惩类别
    var rewardsPunishTypeField = new Ext.form.CmbHRCode({
        fieldLabel : '奖惩类别<font color ="red">*</font>',
        type : "奖惩类别",
        id : 'rewardsPunishType',
        allowBlank : false,
        width : width,
        name : 'rewardsPunishType'
    });

    // 显示顺序
    var orderByField = new Ext.form.NaturalNumberField({
        allowNegative : false,
        allowDecimals : false,
        width : width,
        fieldLabel : "显示顺序",
        maxLength : 10,
        // maxValue : 9999999999,
        minValue : 0,
        id : 'orderBy',
        name : "orderBy",
        allowBlank : true
    });

    // FormPanel:放置各控件
    var myPanel = new Ext.FormPanel({
        frame : true,
        labelWidth : 100,
        labelAlign : 'right',
        defaultType : "textfield",
        items : [rewardsPunishIdField, rewardsPunishField, rewardsPunishTypeField, orderByField]
    });
    // 新增/修改弹出窗口
    var win = new Ext.Window({
        width : 350,
        height : 190,
        modal : true,
        buttonAlign : "center",
        resizable : false,
        items : [myPanel],
        title : '',
        buttons : [{
                // 保存按钮
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : function() {
                    // check必须入力项是否为空
                    if (checkIsNull()) {
                        // check通过后进行保存
                        Ext.Msg.confirm(Constants.BTN_CONFIRM, Constants.COM_C_001, function(buttonObj) {
                            if (buttonObj == "yes") {
                                // 判断数据是否长度溢出
                                if (!rewardsPunishField.isValid() || !orderByField.isValid()) {
                                    return;
                                }
                                var myurl = "";
                                var rewardsPunishId = Ext.get("rewardsPunishId").dom.value;
                                var rewardsPunish = Ext.get("rewardsPunish").dom.value;
                                var rewardsPunishType = rewardsPunishTypeField.value;
                                var orderBy = Ext.get("orderBy").dom.value;
                                // 更新数据操作
                                if (flag == '0') {
                                    myurl = "hr/updateReward.action";
                                } else {
                                    // 添加数据操作
                                    myurl = "hr/addReward.action";
                                }
                                Ext.Ajax.request({
                                    method : 'POST',
                                    url : myurl,
                                    success : function(result, request) {
                                        // 重新加载数据
                                        queryStore.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                            // 隐藏弹出窗口
                                            win.hide();
                                        });
                                    },
                                    failure : function() {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014, function() {
                                        });
                                    },
                                    params : {
                                        // 向后台传递参数
                                        rewardsPunishId : rewardsPunishId,
                                        rewardsPunish : rewardsPunish,
                                        rewardsPunishType : rewardsPunishType,
                                        orderBy : orderBy

                                    }
                                });
                            }
                        });
                    }
                }
            }, {
                // 取消按钮
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.MessageBox.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    });
                }
            }],
        layout : 'fit',
        closeAction : 'hide'
    });
    // ↑↑********弹出窗口*********↑↑//

    var MyRecord = Ext.data.Record.create([{
            name : 'rewardsPunishId'
        }, {
            name : 'rewardsPunish'
        }, {
            name : 'rewardsPunishType'
        }, {
            name : 'orderBy'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/searchRewards.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);
    // 定义封装缓存数据的对象
    var queryStore = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 增加按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addHander
    });

    // 修改按钮
    var editBtn = new Ext.Button({
        id : 'edit',
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateHander
        // editMsg
}   );

    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : function() {
        }
    });

    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : queryStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });
    // 初始化grid
    queryStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    // 运行执行的Grid主体
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : queryStore,
        autoScroll : true,
        enableColumnMove : false,
        enableColumnHide : true,
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {

                header : "奖惩类别ID",
                sortable : true,
                width : 100,
                dataIndex : 'rewardsPunishId'
            }, {
                header : "奖惩类别名称",
                width : 200,
                sortable : true,
                dataIndex : 'rewardsPunish'
            }, {
                header : "奖惩类别",
                width : 150,
                sortable : true,
                dataIndex : 'rewardsPunishType',
                renderer : function(value) {
                    if (value == "1") {
                        value = "奖励";
                    } else if (value == "2") {
                        value = "处罚";
                    }
                    return value;
                }
            }, {
                header : "显示顺序",
                width : 150,
                sortable : true,
                dataIndex : 'orderBy'
            }

        ],
        // 增加，修改，删除栏
        tbar : [addBtn, editBtn, deleteBtn],
        // 分页
        bbar : pagebar
    });
    // 注册双击事件
    grid.on("rowdblclick", updateHander);
    // ----------主画面-----------------

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    // -------------------------------

    // 增加处理
    function addHander() {
        flag = '1'
        myPanel.getForm().reset();
        win.setTitle("新增奖惩类别");
        win.x = undefined;
        win.y = undefined;
        win.show();
        Ext.get("rewardsPunishId").dom.value = "自动生成";
        Ext.get("rewardsPunishId").dom.disabled = true;
    }
    // 编辑处理
    function updateHander() {
        flag = '0';
        // 判断是否已选择了数据
        if (grid.selModel.hasSelection()) {
            var record = grid.getSelectionModel().getSelected();
            win.setTitle("修改奖惩类别");
            // 弹出编辑窗口
            win.x = undefined;
            win.y = undefined;
            win.show();
            // 设置隐藏域的值
            Ext.get("rewardsPunishId").dom.disabled = true;
            // form表单初始化
            myPanel.getForm().loadRecord(record);
        } else {
            // 没有选择时提示信息
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    // 删除处理
    deleteBtn.handler = function() {
        // 判断是否已选择了数据
        if (grid.selModel.hasSelection()) {
            var record = grid.getSelectionModel().getSelected();
            var rewardsPunishIdValue = record.get("rewardsPunishId");
            // 是否确认删除对话框
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(buttonObj) {
                // 删除数据
                if (buttonObj == "yes") {
                    Ext.lib.Ajax.request('POST', 'hr/deleteReward.action', {
                        success : function(action) {
                            // 刷新页面
                            queryStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                            // 删除成功后显示提示信息，并重新初始化grid
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005, function() {
                            })
                        },
                        failure : function() {
                            // 删除失败
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        }
                    },
                    // 参数信息
                    'rewardsPunishId=' + rewardsPunishIdValue);
                }
            });
        } else {
            // 没有选择数据时的提示信息
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }

    }
    // 必须输入项check
    function checkIsNull() {
        var nullMsg = "";
        // 判断奖惩类别名称是否为空
        if (Ext.get("rewardsPunish").dom.value == null || Ext.get("rewardsPunish").dom.value == "") {
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "奖惩类别名称");
            // rewardsPunishField.markInvalid();
        }
        // 判断奖惩类别是否为空
        if (Ext.get("rewardsPunishType").dom.value == null || Ext.get("rewardsPunishType").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "奖惩类别");
            // rewardsPunishTypeField.markInvalid();
        }
        // 如果为空显示错误信息，并返回false,不为空则返回true
        if (nullMsg != "") {
            Ext.Msg.alert(Constants.ERROR, nullMsg);
            return false;
        } else
            return true;
    }
});
