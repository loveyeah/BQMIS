// 画面：考勤管理/节假日维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 时间重复
	DATE_REPEAT = "DATE_REPEAT";
    // 取消排序flag(左右grid:仅在新增，修改，删除，查询后，初始化时取消排序)
    var cancelLeftSortFlag = false;
    var cancelRightSortFlag = false;
    // 流水号
    var holidayId = "";
    // 刷新flag（除去初始化和查询事件：false）
    var refreshFlag = false;
    // 左右grid操作时，只需刷新修改的grid
    var refreshLeftGrid = true;
    // grid显示提示信息
    DISPLAY_MSG = "共 {2} 条";
    // grid最小宽度
    var MIN_WIDTH = 320;
    // client宽度，高度
    var CLIENT_WIDTH = document.body.clientWidth;
    var CLIENT_HEIGHT = document.body.clientHeight;
    // 左grid宽度
    var LEFT_WIDTH;
    // 右grid的X坐标
    var RIGHT_GRID_X;
    // 余白高度(分页栏高度)
    var THE_BLANK_HEIGHT_26 = 26;
    // 余白高度(分页栏高度+滚动条高度)
    var THE_BLANK_HEIGHT_43 = 43;
    // 右grid：宽度
    var RIGHT_WIDTH;
    // 左grid，右grid：高度
    var GRID_HEIGHT;
    initGridSize(CLIENT_WIDTH, CLIENT_HEIGHT);
    // 初始化左右grid高宽
    function initGridSize(clientWidth, clientHeight) {
        // 左grid宽度
        LEFT_WIDTH = MIN_WIDTH;
        // 右grid的X坐标
        RIGHT_GRID_X = LEFT_WIDTH;
        // 当容器宽度大于grid最小宽度2倍时取其一半作为grid宽度
        if (clientWidth > MIN_WIDTH * 2) {
            if (clientWidth % 2 == 1) {
                LEFT_WIDTH = (clientWidth - 1) / 2;
            } else {
                LEFT_WIDTH = clientWidth / 2;
            }
        }
        RIGHT_GRID_X = LEFT_WIDTH;
        // 右grid：宽度
        RIGHT_WIDTH = LEFT_WIDTH;
        if (clientWidth % 2 == 1) {
            RIGHT_WIDTH = LEFT_WIDTH + 1;
        }
        if (clientWidth < LEFT_WIDTH + RIGHT_WIDTH) {
            // 左grid，右grid：高度
            GRID_HEIGHT = clientHeight - THE_BLANK_HEIGHT_43;
        } else {
            // 左grid，右grid：高度
            GRID_HEIGHT = clientHeight - THE_BLANK_HEIGHT_26;
        }
    }
    // 标记：新增/选择
    FLAG_1 = "1";
    // 标记：修改/修改初始化
    FLAG_0 = "0";
    // 新增/修改标记
    var addFlag = "";
    // 是否是查询操作
    var queryFlag = false;
    // 系统当前日期
    var sysDate = new Date();
    sysDate = sysDate.format('Y');
    // 年份选择
    var txtYear = new Ext.form.TextField({
        id : 'txtYear',
        name : 'year',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    txtYear.onClick(function() {
        WdatePicker({
            startDate : '%y',
            dateFmt : 'yyyy',
            alwaysUseStartDate : false,
            isShowClear : true,
            isShowToday : false,
            onpicked : function() {
                txtYear.blur();
            }
        });
    })
    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryRecord
    });
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

    // grid中的数据 节假日ID, 节假日, 节假日类别
    var runGridList = new Ext.data.Record.create([{
            name : "holidayId"
        }, {
            name : "holidayDate"
        }, {
            name : "holidayType"
        }, {
            name : "removeSort"
        }]);

    // 左grid：非周末休息日期 中的store
    var leftGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'ca/getRestDateList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList),
        listeners : {
            loadexception : showLoadMsg
        }
    });
    leftGridStore.baseParams = {
        year : txtYear.getValue()
    };
    // 初始化时,显示数据
    leftGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    // 右grid：周末上班日期 中的store
    var rightGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'ca/getWorkDateList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList),
        listeners : {
            loadexception : showLoadMsg
        }
    });

    leftGridStore.on("load", function() {
        // 只当初始化和查询的时候联动加载右grid
        if (refreshFlag == false) {
            rightGridStore.baseParams = {
                year : txtYear.getValue()
            };
            // 初始化时,显示数据
            rightGridStore.load({
                params : {
                    start : 0,
                    limit : Constants.PAGE_SIZE
                }
            });
            refreshFlag = true;
        }
    });
    rightGridStore.on("load", function() {
        if (leftGridStore.getCount() == 0 && rightGridStore.getCount() == 0) {
            // 没有检索到任何信息(仅在查询处理后显示)
            if (queryFlag) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            }
        }
    })
    leftGridStore.on("beforeload", function() {
        // 仅在新增，修改，删除，查询后，初始化时取消排序
        if (cancelLeftSortFlag) {
            // 页面刷新后grid排序状态恢复到画面初始化状态
            leftGridStore.sortInfo = null;
            leftGrid.getView().removeSortIcon();
            cancelLeftSortFlag = false;
        }
    })
    rightGridStore.on("beforeload", function() {
        // 仅在新增，修改，删除，查询后，初始化时取消排序
        if (cancelRightSortFlag) {
            // 页面刷新后grid排序状态恢复到画面初始化状态
            rightGridStore.sortInfo = null;
            rightGrid.getView().removeSortIcon();
            cancelRightSortFlag = false;
        }
    })
    // "非周末休息日期"Grid主体 (节假日ID, 节假日, 节假日类别)
    var leftGrid = new Ext.grid.GridPanel({
        x : 0,
        y : 0,
        height : GRID_HEIGHT,
        width : LEFT_WIDTH,
        store : leftGridStore,
        columns : [
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35,
                dataIndex : 'removeSort'
            }), {
                header : '非周末休息日期',
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'holidayDate',
                renderer : function(value) {
                    return value.substring(0, 10);
                }
            }],
        viewConfig : {
            forceFit : false
        },
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : leftGridStore,
            displayInfo : true,
            displayMsg : DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        frame : false,
        border : true,
        enableColumnHide : true,
        enableColumnMove : false
    });

    // "周末上班日期" Grid主体 (节假日ID, 节假日, 节假日类别)
    var rightGrid = new Ext.grid.GridPanel({
        x : RIGHT_GRID_X,
        y : 0,
        height : GRID_HEIGHT,
        width : RIGHT_WIDTH,
        store : rightGridStore,
        columns : [
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35,
                dataIndex : 'removeSort'
            }), {
                header : '周末上班日期',
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'holidayDate',
                renderer : function(value) {
                    return value.substring(0, 10);
                }
            }],
        viewConfig : {
            forceFit : false
        },
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : rightGridStore,
            displayInfo : true,
            displayMsg : DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        frame : false,
        border : true,
        enableColumnHide : true,
        enableColumnMove : false
    });
    // 注册选择行事件
    leftGrid.on("rowclick", cancelRight);
    rightGrid.on("rowclick", cancelLeft);
    // 注册双击事件
    leftGrid.on("rowdblclick", dbLUpdateRecord);
    rightGrid.on("rowdblclick", dbRUpdateRecord);
    /**
     * 双击事件选中则进入修改页面
     */
    function dbLUpdateRecord() {
        if (leftGrid.selModel.hasSelection()) {
            updateRecord();
        }
    }
    function dbRUpdateRecord() {
        if (rightGrid.selModel.hasSelection()) {
            updateRecord();
        }
    }

    var panelForTwoGrid = new Ext.form.FormPanel({
        id : "panelForTwoGrid",
        border : false,
        autoScroll : true,
        frame : false,
        layout : 'absolute',
        items : [leftGrid, rightGrid]
    });
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : 'fit',
        border : false,
        frame : false,
        items : [{
                layout : 'fit',
                margins : '0 0 0 0',
                border : false,
                tbar : ["年份:", txtYear, "-", queryBtn, addBtn, updateBtn, deleteBtn],
                items : [panelForTwoGrid]
            }]
    });
    // 动态设置左右grid高宽及右grid的x坐标，使其适应容器大小
    layout.on('resize', function(vp, adjWidth, adjHeight, rawWidth, rawHeight) {
        clientWidth = document.body.clientWidth;
        clientHeight = document.body.clientHeight;
        initGridSize(clientWidth, clientHeight);
        // 设置左右grid高宽及右grid的x坐标
        leftGrid.setWidth(LEFT_WIDTH);
        rightGrid.setPosition(RIGHT_GRID_X, 0);
        rightGrid.setWidth(RIGHT_WIDTH);
        leftGrid.setHeight(GRID_HEIGHT);
        rightGrid.setHeight(GRID_HEIGHT);
    })
    // 弹出窗口项目
    var THEWIDTH = 180;
    // 定义form中的类别:
    var drpType = new Ext.form.CmbCACode({
        id : 'holidayType',
        fieldLabel : '类别',
        type : "节假日类别",
        allowBlank : true,
        value : 1
    })
    // 定义form中的 日期
    var txtDate = new Ext.form.TextField({
        fieldLabel : '日期<font color ="red">*</font>',
        id : 'holidayDate',
        style : 'cursor:pointer',
        width : THEWIDTH,
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false,
                    onpicked : function() {
                        txtDate.clearInvalid();
                    }
                });
            }
        }
    });
    // 隐藏ID
    var hidHolidayId = {
        id : "holidayId",
        xtype : "hidden"
    }
    // 定义弹出窗体中的form
    var mypanel = new Ext.FormPanel({
        labelAlign : 'right',
        autoHeight : true,
        layout : "form",
        frame : true,
        items : [hidHolidayId, txtDate, drpType]
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
        mypanel.getForm().reset();
        txtDate.setValue("");
        // 流水号
        holidayId = "";
        drpType.setDisabled(false);
        win.setTitle("新增");
        win.x = undefined;
        win.y = undefined;
        win.show();
        mypanel.getForm().clearInvalid();
        addFlag = FLAG_1;
    }
    /**
     * 修改函数
     */
    function updateRecord() {
        // 是否有被选项
        if (leftGrid.selModel.hasSelection() || rightGrid.selModel.hasSelection()) {
            // 左grid是否有选中一行
            if (leftGrid.selModel.hasSelection()) {
                var record = leftGrid.getSelectionModel().getSelected();
                refreshLeftGrid = true;
            } else {
                // 右grid有选中一行
                var record = rightGrid.getSelectionModel().getSelected();
                refreshLeftGrid = false;
            }
            // 将数据传入修改页面
            mypanel.getForm().loadRecord(record);
            // 流水号
            holidayId = record.get('holidayId');
            txtDate.setValue(record.get('holidayDate').substring(0, 10));
            drpType.setValue(record.get('holidayType'), true);
            drpType.setDisabled(true);
            mypanel.getForm().clearInvalid();
            win.setTitle("修改");
            win.x = undefined;
            win.y = undefined;
            win.show();
            addFlag = FLAG_0;
        } else {
            // message提示：请选择一行
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 删除函数
     */
    function deleteRecord() {
        // 是否有被选项
        if (leftGrid.selModel.hasSelection() || rightGrid.selModel.hasSelection()) {
            if (leftGrid.selModel.hasSelection()) {
                var record = leftGrid.getSelectionModel().getSelected();
                refreshLeftGrid = true;
                // 取消左grid排序
                cancelLeftSortFlag = true;
            } else {
                var record = rightGrid.getSelectionModel().getSelected();
                refreshLeftGrid = false;
                // 取消右grid排序
                cancelRightSortFlag = true;
            }
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'ca/deleteHoliday.action',
                        params : {
                            // 节假日id
                            holidayId : record.get('holidayId')
                        },
                        success : function(result, request) {
                            // 成功，显示删除成功信息
                            var o = eval("(" + result.responseText + ")");
                            // 数据库异常
                            if (o.msg == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else {
                                // 删除成功
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                                queryFlag = false;
                                refreshRecords();
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            queryFlag = false;
                            refreshRecords();
                        }
                    })
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
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 画面控件是否红线
                if (!mypanel.getForm().isValid()) {
                    return;
                };
                // 画面逻辑check,是否周末
                if (!checkLogic()) {
                    return;
                }
                // 选非周末休息日
                if (drpType.getValue() == "1") {
                    refreshLeftGrid = true;
                    // 取消左grid排序
                    cancelLeftSortFlag = true;
                } else {
                    refreshLeftGrid = false;
                    // 取消右grid排序
                    cancelRightSortFlag = true;
                }
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'ca/saveOrUpdateHoliday.action',
                    params : {
                        // 节假日id,节假日日期,节假日类型,新增修改flag
                        holidayId : holidayId,
                        holidayDate : txtDate.getValue(),
                        holidayType : drpType.getValue(),
                        addOrUpdate : addFlag
                    },
                    success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var suc = o.msg;
                            // 如果成功
                            if (suc == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else if(suc == DATE_REPEAT) { 
                            	// 2009-3-19 增加日期重复性check
                            	Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_007,"输入日期"));
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    // 隐藏弹出窗口
                                    win.hide();
                                });
                                // 成功,则重新加载grid里的数据
                                // 重新加载数据
                                queryFlag = false;
                                refreshRecords();
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
        // 合计项类型 必须输入项check
        if (txtDate.getValue() == null || txtDate.getValue() == "") {
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_003, "日期"));
            return false;
        }
        return true;
    }
    /**
     * 逻辑check
     */
    function checkLogic() {
        var isWeekDay = checkWeekDay(txtDate.getValue().substring(0, 10));
        var type = drpType.getValue();
        // 节假日类别:1 非周末休息日期
        if ("1" == type && isWeekDay) {
            Ext.Msg.alert(Constants.ERROR, Constants.KQ010_E_002);
            return false;
        } else if ("2" == type && !isWeekDay) {
            // 节假日类别:2 周末上班日期
            Ext.Msg.alert(Constants.ERROR, Constants.KQ010_E_001);
            return false;
        }
        return true;
    }
    /**
     * 查询处理
     */
    function queryRecord() {
        // 刷新时为ture
        refreshFlag = false;
        // 查询时为ture
        queryFlag = true;
        // 取消左grid排序
        cancelLeftSortFlag = true;
        // 取消右grid排序
        cancelRightSortFlag = true;
        refreshRecords();
    }
    /**
     * 刷新画面
     */
    function refreshRecords() {
        // 只刷新左
        // if (refreshLeftGrid == true) {
        leftGridStore.baseParams = {
            year : txtYear.getValue()
        };
        leftGridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        // } else {
        // 刷新右
        rightGridStore.baseParams = {
            year : txtYear.getValue()
        };
        rightGridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        // }
    }
    /**
     * grid加载数据时出错处理
     */
    function showLoadMsg(ds, records, o) {
        var result = eval("(" + o.responseText + ")");
        var succ = result.msg;
        // 数据库操作异常终了
        if (succ == Constants.SQL_FAILURE) {
            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
        } else if (succ == Constants.DATE_FAILURE) {
            // 日期格式转化错误
            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
        }
    }
    /**
     * 对String型校验 判断是不是周末
     */
    function checkWeekDay(sDate) {
        aDate = sDate.split("-")
        // 转换为11-19-2009格式
        oDate = new Date(aDate[1] + "-" + aDate[2] + "-" + aDate[0])
        day = oDate.getDay();
        if (day == 0 || day == 6) {
            return true;
        }
        return false;
    }
    /**
     * 取消右grid选中行
     */
    function cancelRight(grid, rowIndex, e) {
        if (rightGrid.selModel.hasSelection()) {
            var record = rightGrid.getSelectionModel().getSelected();
            var rowindex = rightGridStore.indexOf(record);
            rightGrid.selModel.deselectRow(rowindex);
        }
    }
    /**
     * 取消左grid选中行
     */
    function cancelLeft(grid, rowIndex, e) {
        if (leftGrid.selModel.hasSelection()) {
            var record = leftGrid.getSelectionModel().getSelected();
            var rowindex = leftGridStore.indexOf(record);
            leftGrid.selModel.deselectRow(rowindex);
        }
    }
});