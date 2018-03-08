Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
//---code定义书 是否设置周末，是否包含周末 begin
// 是否设置周末周期
Constants.ISCYCLE_OK = "1";
// 是否设置周末周期
Constants.ISCYCLE_NO = "0";
// 是否包含周末(标志位)
Constants.ISWEEKEND_OK = "1";
// 是否包含周末(标志位)
Constants.ISWEEKEND_NO = "0";
//---code定义书 end
Ext.onReady(function() {
    Ext.QuickTips.init();
    // 数据源--------------------------------
    var width = 180;
    // 假别id
    var vacationTypeIdField = new Ext.form.Hidden({
        id : "vacationTypeId",
        name : "vacationTypeId"
    });
    // 假别
    var vacationTypeField = new Ext.form.TextField({
        fieldLabel : "假别<font color='red'>*</font>",
        id : "vacationType",
        name : "vacationType",
        maxLength : 10,
        width : width,
        allowBlank : false
    });

    // 是否设置周期
    var periodCom = new Ext.form.CmbCACode({
        fieldLabel : '是否设置周期',
        type : "是否设置周期",
        id : 'ifCycle',
        value :0,
        name : 'ifCycle'
    });

    // 是否包含周末
    var weekendCom = new Ext.form.CmbCACode({
        fieldLabel : '是否包含周末',
        type : "是否包含周末",
        id : 'ifWeekend',
        value :1,
        name : 'ifWeekend'
    });

    // 考勤标志
    var attendanceMarkField = new Ext.form.TextField({
        fieldLabel : "考勤标志<font color='red'>*</font>",
        id : "vacationMark",
        name : "vacationMark",
        allowBlank : false,
        width : width,
        maxLength : 5
    });

    // FormPanel:放置各控件
    var myPanel = new Ext.FormPanel({
        frame : true,
        labelWidth : 100,
        labelAlign : 'right',
        defaultType : "textfield",
        items : [vacationTypeIdField, vacationTypeField, periodCom, weekendCom, attendanceMarkField]
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
                                if (!vacationTypeField.isValid() || !attendanceMarkField.isValid()) {
                                    return;
                                }
                                var myurl = "";
                                // 根据假别id是否有值判断是新增还是修改
                                var flag = Ext.get("vacationTypeId").dom.value;
                                // 得到假别值
                                var vacationTypeFieldValue = Ext.get("vacationType").dom.value;
                                // 得到是否设置周期值
                                var periodComValue = periodCom.value;
                                // 得到是否包含周末值
                                var weekendComValue = weekendCom.value;
                                // 得到考勤标志值
                                var attendanceMarkFieldValue = Ext.get("vacationMark").dom.value;
                                // 添加数据操作
                                if (flag == "" || flag == null) {
                                    myurl = "ca/addVacationType.action";
                                } else {
                                    // 更新数据操作
                                    myurl = "ca/updateVacationType.action";
                                }
                                Ext.Ajax.request({
                                    method : 'POST',
                                    url : myurl,
                                    success : function(result, request) {
                                    	queryStore.sortInfo = null;
                                        grid.getView().removeSortIcon();
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
                                        // 假别id
                                        vacationTypeId : vacationTypeIdField.value,
                                        // 假别
                                        vacationType : vacationTypeFieldValue,
                                        // 是否设置周期
                                        ifCycle : periodComValue,
                                        // 是否包含周末
                                        ifWeekend : weekendComValue,
                                        // 考勤标志
                                        vacationMark : attendanceMarkFieldValue

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
            // 假别流水号
            name : 'vacationTypeId'
        }, {
            // 假别
            name : 'vacationType'
        }, {
            // 是否设置周期
            name : 'ifCycle'
        }, {
            // 是否包含周末
            name : 'ifWeekend'
        }, {
            // 考勤标志
            name : 'vacationMark'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'ca/searchVacationTypeList.action'
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
    });

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

                header : "假别ID",
                width : 100,
                sortable : true,
                dataIndex : 'vacationTypeId'
            }, {
                header : "假别",
                width : 100,
                sortable : true,
                dataIndex : 'vacationType'
            }, {
                header : "是否设置周期",
                width : 100,
                sortable : true,
                dataIndex : 'ifCycle',
                renderer : function(value) {
                    if (value == Constants.ISCYCLE_OK) { 
                        value = caCodeDefine['是否设置周期'][0].text;
                    } else if (value == Constants.ISCYCLE_NO) {
                        value = caCodeDefine['是否设置周期'][1].text;
                    }
                    return value;
                }

            }, {
                header : "是否包含周末",
                width : 100,
                sortable : true,
                dataIndex : 'ifWeekend',
                renderer : function(value) {
                    if (value == Constants.ISWEEKEND_OK) {
                        value = caCodeDefine['是否包含周末'][0].text;
                    } else if (value == Constants.ISWEEKEND_NO) {
                        value = caCodeDefine['是否包含周末'][1].text;
                    }
                    return value;
                }

            }, {
                header : "考勤标志",
                width : 100,
                sortable : true,
                dataIndex : 'vacationMark'
            }

        ],
        // 增加，修改，删除栏
        tbar : [addBtn, editBtn, deleteBtn],
        // 分页
        bbar : pagebar
    });
    // 注册双击事件
    grid.on("rowdblclick", updateHanderTwo);
    // ----------主画面-----------------

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    // -------------------------------

    // 增加处理
    function addHander() {
        myPanel.getForm().reset();
        win.setTitle("新增假别");
        win.x = undefined;
        win.y = undefined;
        win.show();
    }
    // 双击grid时只有选中数据时才执行修改操作
    function updateHanderTwo() {
    	if(grid.selModel.hasSelection()){
    		updateHander();
    	} 
    }
    // 编辑处理
    function updateHander() {
        // 判断是否已选择了数据
        if (grid.selModel.hasSelection()) {
            var record = grid.getSelectionModel().getSelected();
            win.setTitle("修改假别");
            // 弹出编辑窗口
            win.x = undefined;
            win.y = undefined;
            win.show();
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
            var vacationTypeId = record.get("vacationTypeId");
            // 是否确认删除对话框
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(buttonObj) {
                // 删除数据
                if (buttonObj == "yes") {
                    Ext.lib.Ajax.request('POST', 'ca/deleteVacationType.action', {
                        success : function(action) {
                        	queryStore.sortInfo = null;
                            grid.getView().removeSortIcon();
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
                    'vacationTypeId=' + vacationTypeId);
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
        // 判断假别是否为空
        if (Ext.get("vacationType").dom.value == null || Ext.get("vacationType").dom.value == "") {
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "假别");
            // rewardsPunishField.markInvalid();
        }
        // 判断考勤标志是否为空
        if (Ext.get("vacationMark").dom.value == null || Ext.get("vacationMark").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "考勤标志");
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
