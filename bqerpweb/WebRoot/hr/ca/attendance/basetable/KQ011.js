// 考勤管理： 基本表维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 取消排序flag(仅在新增，修改，删除，查询后，初始化时取消排序)
    var cancelSortFlag = false;
    // 要维护的表
    var TABLE1 = "加班类别表";
    var TABLE2 = "运行班类别表";
    // 选中的表名
    var selectedTableName = TABLE1;
    // 标记：新增
    FLAG_1 = "1";
    // 标记：修改
    FLAG_0 = "0";
    // 新增/修改标记
    var addFlag = "";
    // 修改项的流水号
    var updateId;
    // 加班类别 or 运行班类别
    var updateTypename;
    // 是否发放费用 or 津贴标准
    var updateIfFeeOrShiftFee;
    // 考勤标志
    var updatemark;
    // tbar 的button
    // 新增按钮
    var addBtn = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : onAdd
    });
    // 修改按钮
    var modifyBtn = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : onModify
    });
    // 删除按钮
    var deleteBtn = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : onDelete
    });
    // 表名数据源
    var myData = [[TABLE1], [TABLE2]];
    // 创建 data store
    var myStore = new Ext.data.SimpleStore({
        fields : [{
                name : 'tableName'
            }]
    });
    myStore.loadData(myData);
    // 初始化设置选中第一项表名
    myStore.on('load', function() {
        leftGrid.getSelectionModel().selectFirstRow();
    });

    // 创建grid
    var leftGrid = new Ext.grid.GridPanel({
        store : myStore,
        region : 'west',
        title : "基础表一览",
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                id : 'tableName',
                header : "表名称",
                width : 109,
                dataIndex : 'tableName'
            }],
        viewConfig : {
            forceFit : false
        },
        border : true,
        frame : false,
        enableColumnHide : true,
        enableColumnMove : false,
        style : {
            'margin-top' : '0px',
            'padding' : 0,
            'padding-top' : '0px'
        },
        height : 350,
        width : 150
    });

    // 设置响应单击行事件
    leftGrid.on('rowclick', changeTable);
    leftGrid.on('keydown', changeTable);

    // ID 加班类别 是否发放费用 运行班类别 津贴标准 考勤标志
    var runGridList = new Ext.data.Record.create([{
            name : 'recordId',
            type : 'float'
        }, {
            name : 'recordOverTimeName'
        }, {
            name : 'recordIfOverTimeFee'
        }, {
            name : 'recordShiftName'
        }, {
            name : 'recordShiftFee',
            type : 'float'
        }, {
            name : 'recordMark'
        }, {
            name : 'removeSort'
        }]);
    // grid的store
    var searchStore = new Ext.data.JsonStore({
        url : 'ca/getBaseTableRecordList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : runGridList,
        listeners : {
            // 出错信息提示
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
    // 初始化为(默认:加班类别表)
    searchStore.baseParams = {
        tableName : TABLE1
    }
    searchStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    searchStore.on("beforeload", function() {
        // 仅在新增，修改，删除，查询后，初始化时取消排序
        if (cancelSortFlag) {
        	// 页面刷新后grid排序状态恢复到画面初始化状态
            searchStore.sortInfo = null;
            rightGrid.getView().removeSortIcon();
            cancelSortFlag = false;
        }
    })
    var myColumn = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : '行号',
            width : 35,
            dataIndex : 'removeSort'
        }), {
            header : "ＩＤ",
            width : 70,
            sortable : true,
            hidden : true,
            dataIndex : 'recordId'
        }, {
            header : "加班类别",
            width : 150,
            sortable : true,
            dataIndex : 'recordOverTimeName'
        }, {
            header : "是否发放费用",
            width : 150,
            sortable : true,
            dataIndex : 'recordIfOverTimeFee',
            renderer : function(value) {
                if (value == "1") {
                    value = caCodeDefine['是否发放费用'][0].text;
                } else if (value == "0") {
                    value = caCodeDefine['是否发放费用'][1].text;
                } else
                    value = "";
                return value;
            }
        }, {
            header : "运行班类别",
            width : 150,
            sortable : true,
            dataIndex : 'recordShiftName',
            hidden : true
        }, {
            header : "津贴标准",
            width : 150,
            sortable : true,
            align : 'right',
            dataIndex : 'recordShiftFee',
            hidden : true,
            renderer : function(value) {
                if (value != null) {
                    return numberFormat(value);
                }
            }
        }, {
            header : "考勤标志",
            width : 100,
            sortable : true,
            dataIndex : 'recordMark'
        }]);
    // 创建grid
    var rightGrid = new Ext.grid.GridPanel({
        store : searchStore,
        region : 'center',
        title : "加班类别表",
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        cm : myColumn,
        viewConfig : {
            forceFit : false
        },
        // loadMask : true,
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : searchStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        border : true,
        frame : false,
        enableColumnHide : true,
        enableColumnMove : false,
        height : 350,
        width : 600
    });
    rightGrid.on('rowdblclick', dbToModify);
    /**
     * 双击事件选中则进入修改页面
     */
    function dbToModify() {
        if (rightGrid.selModel.hasSelection()) {
            onModify();
        }
    }

    /** ************弹出窗口**************** */
    // 定义弹出窗体
    var TXTWIDTH = 180;
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    // 取消按钮
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : onCancle
    });
    // 保存按钮
    var btnSave2 = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    // 取消按钮
    var btnCancel2 = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : onCancle
    });
    // 加班类别
    var txtOverTimeTypeName = new Ext.form.TextField({
        id : 'recordOverTimeName',
        fieldLabel : '加班类别<font color ="red">*</font>',
        maxLength : 10,
        name : 'baseTableBeen.recordOverTimeName',
        allowBlank : false,
        width : TXTWIDTH
    })
    // 是否发放费用
    var drpIfFee = new Ext.form.CmbCACode({
        fieldLabel : '是否发放费用',
        type : "是否发放费用",
        allowBlank : false,
        value : 1,
        hiddenName : 'baseTableBeen.holidayType',
        id : 'holidayType'
    })
    // 运行班类别
    var txtShiftTypeName = new Ext.form.TextField({
        id : 'recordShiftName',
        fieldLabel : '运行班类别<font color ="red">*</font>',
        maxLength : 10,
        allowBlank : false,
        name : 'baseTableBeen.recordShiftName',
        width : TXTWIDTH
    })
    // 津贴标准
    var txtShiftFee = new Powererp.form.NumField({
        id : 'recordShiftFee',
        fieldLabel : "津贴标准",
        width : TXTWIDTH,
        allowNegative : false,
        allowBlank : true,
        maxLength : 16,
        addComma : true,
        style : 'text-align:right',
        decimalPrecision : 4,
        padding : 4
    });
    // 考勤标志
    var txtOverTimeMark = new Ext.form.TextField({
        id : 'txtOverTimeMark',
        fieldLabel : '考勤标志<font color ="red">*</font>',
        maxLength : 5,
        allowBlank : false,
        width : TXTWIDTH
    })
    var txtWorkShiftMark = new Ext.form.TextField({
        id : 'txtWorkShiftMark',
        fieldLabel : '考勤标志<font color ="red">*</font>',
        maxLength : 5,
        allowBlank : false,
        width : TXTWIDTH
    })
    // 定义弹出窗体中的form
    var panelOverTime = new Ext.FormPanel({
        autoScroll : true,
        labelAlign : 'right',
        border : false,
        frame : true,
        labelWidth : 100,
        items : [txtOverTimeTypeName, drpIfFee, txtOverTimeMark]
    });
    var panelWorkShift = new Ext.FormPanel({
        autoScroll : true,
        labelAlign : 'right',
        border : false,
        frame : true,
        labelWidth : 100,
        items : [txtShiftTypeName, txtShiftFee, txtWorkShiftMark]
    });
    // 弹出编辑窗口
    var winOverTime = new Ext.Window({
        id : 'winOverTime',
        width : 350,
        title : "",
        buttonAlign : "center",
        items : [panelOverTime],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [btnSave, btnCancel]
    });
    var winWorkShift = new Ext.Window({
        id : 'winWorkShift',
        width : 350,
        title : "",
        buttonAlign : "center",
        items : [panelWorkShift],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [btnSave2, btnCancel2]
    });
    // /**************弹出窗口结束*****************/

    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : 'fit',
        border : false,
        frame : true,
        items : [{
                layout : 'border',
                border : false,
                tbar : [addBtn, modifyBtn, deleteBtn],
                items : [leftGrid, rightGrid]
            }]
    });
    // 选中第一行，并使其获得焦点
    leftGrid.getSelectionModel().selectFirstRow();
    var row = leftGrid.getView().getRow(0);
    Ext.get(row).focus();
    /** *****************响应函数************************* */
    /**
     * 点击左边grid，右边grid打开某个表
     */
    function changeTable() {
        // 打开某个表
        if (leftGrid.selModel.hasSelection()) {
            var selectedRow = leftGrid.selModel.getSelected();
            var tName = selectedRow.get("tableName");
            if (tName == selectedTableName) {
                return;
            }
            // 设定grid的标题
            rightGrid.setTitle(tName);
            // 定义变量：是否是第一个表
            var isTableFrist = true;
            if (tName == TABLE1) {
                // 如果为表1时
                selectedTableName = TABLE1;
                isTableFrist = true;
            } else if (tName == TABLE2) {
                // 如果为表2时
                selectedTableName = TABLE2;
                isTableFrist = false
            }
            // 根据表名设置隐藏与显示
            searchStore.removeAll();
            rightGrid.getColumnModel().setHidden(2, !isTableFrist);
            rightGrid.getColumnModel().setHidden(3, !isTableFrist)
            rightGrid.getColumnModel().setHidden(4, isTableFrist)
            rightGrid.getColumnModel().setHidden(5, isTableFrist)
            // 取消排序与初始状态相同
            cancelSortFlag = true;
            searchStore.baseParams = {
                // 参数：表名
                tableName : tName
            }
            searchStore.load({
                params : {
                    start : 0,
                    limit : Constants.PAGE_SIZE
                }
            });
        }
    }

    /**
     * 增加记录
     */
    function onAdd() {
        // 设置新增flag为1
        addFlag = FLAG_1;
        // 设置修改项流水号
        updateId = "";
        var strTitle = "新增";
        // 加上表名
        strTitle += rightGrid.title;
        // 把“表”字去掉
        strTitle = strTitle.substring(0, strTitle.length - 1);
        // 表1：加班类别表
        if (selectedTableName == TABLE1) {
            panelOverTime.getForm().reset();
            // 设置标题，居中显示
            winOverTime.setTitle(strTitle);
            winOverTime.x = undefined;
            winOverTime.y = undefined;
            winOverTime.show();
        } else if (selectedTableName == TABLE2) {
            // 表2：运行班类别表
            panelWorkShift.getForm().reset();
            // 设置标题，居中显示
            winWorkShift.setTitle(strTitle);
            winWorkShift.x = undefined;
            winWorkShift.y = undefined;
            winWorkShift.show();
        }
    }
    /**
     * 修改记录
     */
    function onModify() {
        // 设置新增flag为0
        addFlag = FLAG_0;
        if (rightGrid.selModel.hasSelection()) {
            // 选中行记录
            var selectedRowRecord = rightGrid.selModel.getSelected();
            var strTitle = "修改";
            strTitle += rightGrid.title;
            strTitle = strTitle.substring(0, strTitle.length - 1);
            if (selectedTableName == TABLE1) {
                // 表1：加班类别表
                // 设置标题，居中显示
                winOverTime.setTitle(strTitle);
                winOverTime.x = undefined;
                winOverTime.y = undefined;
                winOverTime.show();
                // 加载选中数据
                txtOverTimeTypeName.setValue(selectedRowRecord.get('recordOverTimeName'));
                drpIfFee.setValue(selectedRowRecord.get('recordIfOverTimeFee'), true);
                drpIfFee.clearInvalid();
                txtOverTimeMark.setValue(selectedRowRecord.get('recordMark'));
            } else if (selectedTableName == TABLE2) {
                // 表2：运行班类别表
                // 设置标题，居中显示
                winWorkShift.setTitle(strTitle);
                winWorkShift.x = undefined;
                winWorkShift.y = undefined;
                winWorkShift.show();
                // 加载选中数据
                txtShiftTypeName.setValue(selectedRowRecord.get('recordShiftName'));
                txtShiftFee.setValue(selectedRowRecord.get('recordShiftFee'));
                txtWorkShiftMark.setValue(selectedRowRecord.get('recordMark'));
            }
            // 设置修改项流水号
            updateId = selectedRowRecord.get('recordId');
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
        }
    }
    /**
     * 删除记录
     */
    function onDelete() {
        if (rightGrid.selModel.hasSelection()) {
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = rightGrid.selModel.getSelected();
                    // 获得记录的流水号
                    var id = record.get("recordId");
                    // 获得表名
                    var titleName = rightGrid.title;
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'ca/deleteBaseTableRecord.action',
                        success : function(result, request) {
                            // 成功，显示删除成功信息
                            var o = eval("(" + result.responseText + ")");
                            // 数据库异常
                            if (o.msg == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                // 删除成功
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                                // 取消排序与初始状态相同
                                cancelSortFlag = true;
                                searchStore.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE
                                    }
                                });
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            searchStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                        },
                        params : {
                            // 记录流水号，表名
                            recordId : id,
                            tableName : titleName
                        }
                    });
                }
            });
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
        }
    }
    /**
     * 画面check
     */
    function check() {
        // 必须输入项check
        var nullMsg = "";
        if (selectedTableName == TABLE1) {
            // 表1：加班类别表
            // 加班类别必须输入项check
            if (txtOverTimeTypeName.getValue() == null || txtOverTimeTypeName.getValue() == "") {
                nullMsg = nullMsg + String.format(Constants.COM_E_002, "加班类别");
            }
            // 是否发放费用必须输入项check
            if (drpIfFee.getValue() == null || drpIfFee.getValue() == "") {
                if (nullMsg != "") {
                    nullMsg = nullMsg + "<br/>";
                }
                nullMsg = nullMsg + String.format(Constants.COM_E_003, "是否发放费用");
            }
            // 考勤标志必须输入项check
            if (txtOverTimeMark.getValue() == null || txtOverTimeMark.getValue() == "") {
                if (nullMsg != "") {
                    nullMsg = nullMsg + "<br/>";
                }
                nullMsg = nullMsg + String.format(Constants.COM_E_002, "考勤标志");
            }
        } else if (selectedTableName == TABLE2) {
            // 表2：运行班类别表
            // 运行班类别必须输入项check
            if (txtShiftTypeName.getValue() == null || txtShiftTypeName.getValue() == "") {
                nullMsg = nullMsg + String.format(Constants.COM_E_002, "运行班类别");
            }
            // 考勤标志必须输入项check
            if (txtWorkShiftMark.getValue() == null || txtWorkShiftMark.getValue() == "") {
                if (nullMsg != "") {
                    nullMsg = nullMsg + "<br/>";
                }
                nullMsg = nullMsg + String.format(Constants.COM_E_002, "考勤标志");
            }
        }
        if (nullMsg != "") {
            Ext.Msg.alert(Constants.ERROR, nullMsg);
            return false;
        }
        return true;
    }
    /**
     * 保存记录
     */
    function onSave() {
        // 画面check
        if (!check()) {
            return;
        }
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                if (selectedTableName == TABLE1) {
                    // 画面控件是否红线
                    if (!panelOverTime.getForm().isValid()) {
                        return;
                    }
                    // 设置更新值
                    updateTypename = txtOverTimeTypeName.getValue();
                    updateIfFeeOrShiftFee = drpIfFee.getValue();
                    updatemark = txtOverTimeMark.getValue();
                } else if (selectedTableName == TABLE2) {
                    // 画面控件是否红线
                    if (!panelWorkShift.getForm().isValid()) {
                        return;
                    }
                    // 设置更新值
                    updateTypename = txtShiftTypeName.getValue();
                    updateIfFeeOrShiftFee = txtShiftFee.getValue();
                    updatemark = txtWorkShiftMark.getValue();
                }
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'ca/saveOrUpdateBaseTable.action',
                    params : {
                        // 基础表ID,加班类别 or 运行班类别,是否发放费用 or 津贴标准,考勤标志,新增修改flag
                        recordId : updateId,
                        recordTypeName : updateTypename,
                        ifFeeOrShiftFee : updateIfFeeOrShiftFee,
                        recordMark : updatemark,
                        addOrUpdate : addFlag,
                        tableName : selectedTableName
                    },
                    success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var suc = o.msg;
                            // 如果成功
                            if (suc == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    if (selectedTableName == TABLE1) {
                                        // 隐藏弹出窗口
                                        winOverTime.hide();
                                    } else if (selectedTableName == TABLE2) {
                                        // 隐藏弹出窗口
                                        winWorkShift.hide();
                                    }
                                });
                                // 成功,则重新加载grid里的数据
                                // 取消排序与初始状态相同
                                cancelSortFlag = true;
                                searchStore.baseParams = {
                                    tableName : selectedTableName
                                }
                                searchStore.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE
                                    }
                                });
                            }
                        }
                    },
                    // 异常终了
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    }
                });
            }
        })
    }
    /**
     * 取消
     */
    function onCancle() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_005, function(buttonobj) {
            if ("yes" == buttonobj) {
                // 选择“是”，隐藏弹出窗口
                if (selectedTableName == TABLE1) {
                    winOverTime.hide();
                } else if (selectedTableName == TABLE2) {
                    winWorkShift.hide();
                }
            }
        });
    }
    /**
     * 大数字中间用','分隔 小数点后4位
     */
    function numberFormat(value) {
        value = String(value);
        // 整数部分
        var whole = value;
        if(whole == ''){
        	return "";
        }
        // 小数部分
        var sub = ".0000";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "0000";
            if (sub.length > 5) {
                sub = sub.substring(0, 5);
            }
        }
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return v;
    }
});