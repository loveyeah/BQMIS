Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 是否销假0（否）
Constants.IFCLEAR_0 = "0";
// 是否销假1(是）
Constants.IFCLEAR_1 = "1";
Ext.onReady(function() {
    Ext.QuickTips.init();
    // 系统当前日期
    var sysDate = new Date();
    sysDate = dateFormat(sysDate)
    // 初期化标志
    initFlagW = true;

    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHander
    });

    // 打印按钮
    var printBtn = new Ext.Button({
        id : 'print',
        text : Constants.BTN_PRINT,
        iconCls : Constants.CLS_PRINT,
        handler : printHander
    });

    // 导出按钮
    var exportBtn = new Ext.Button({
        id : 'export',
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        handler : exportHander
    });
    // 请假开始时间
    var startTime = new Ext.form.TextField({
        id : 'vacationTimeS',
        name : 'vacationTimeS',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    startTime.onClick(function() {
        WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd'
        })
    });

    // 请假结束时间
    var endTime = new Ext.form.TextField({
        id : 'vacationTimeE',
        name : 'vacationTimeE',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    endTime.onClick(function() {
        WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd'
        })
    });
    // 备注-弹出窗口
    var memoText = new Ext.form.TextArea({
        id : "memoTextId",
        name : "memoTextId",
        maxLength : 127,
        disabled:true,
        width : 180
    });
    
    // 弹出画面
    var win = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [memoText],
        buttonAlign : "center",
        title : '详细信息查看窗口',
        buttons : [{
                text : Constants.BTN_CLOSE,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    win.hide();
                }
            }]
    });
    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
            // 请假登记流水号
            name : 'vacationid'
        }, {
            // 员工姓名
            name : 'chsName'
        }, {
            // 部门
            name : 'deptName'
        }, {
            // 假别
            name : 'vacationType'
        }, {
            // 请假开始时间
            name : 'startTime'
        }, {
            // 请假结束时间
            name : 'endTime'
        }, {
            // 请假天数
            name : 'vacationDays',
            sortType : 'asFloat'
        }, {
            // 请假时长
            name : 'vacationTime',
            sortType : 'asFloat'
        }, {
            // 请假原因
            name : 'reason'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 去向
            name : 'whither'
        }, {
            // 是否销假
            name : 'ifClear'
        }, {
            // 销假时长
            name : 'clearDate'
        }, {
            // 审批状态
            name : 'signState'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'ca/searchVacationList.action'
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
    // 头标题
    var headTbar = new Ext.Toolbar({
        items : ["请假开始日期:", startTime, "~", endTime,'-', queryBtn, printBtn, exportBtn]
    });

    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : queryStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });

    // beforeload事件,传递查询字符串作为参数
    queryStore.on('beforeload', function() {
        Ext.apply(this.baseParams, {
            startTime : startTime.getValue(),
            endTime : endTime.getValue()
        });
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
                header : "审批状态",
                width : 100,
                sortable : true,
                dataIndex : 'signState',
                renderer : statesFormat
            }, {
                header : "部门",
                width : 100,
                sortable : true,
                dataIndex : 'deptName'
            }, {
                header : "姓名",
                width : 100,
                sortable : true,
                dataIndex : 'chsName'
            }, {
                header : "请假开始时间",
                width : 120,
                sortable : true,
                dataIndex : 'startTime'

            }, {
                header : "请假结束时间",
                width : 120,
                sortable : true,
                dataIndex : 'endTime'
            }, {
                header : "假别",
                width : 100,
                sortable : true,
                dataIndex : 'vacationType'
            }, {
                header : "请假天数",
                width : 80,
                sortable : true,
                align : 'right',
                renderer : numberFormat2,
                dataIndex : 'vacationDays'
            }, {
                header : "请假时长",
                width : 80,
                align : 'right',
                sortable : true,
                renderer : numberFormat1,
                dataIndex : 'vacationTime'
            }, {
                header : "请假原因",
                width : 150,
                sortable : true,
                dataIndex : 'reason'
            }, {
                header : "是否销假",
                width : 65,
                sortable : true,
                dataIndex : 'ifClear',
                renderer : function(value) {
                    if (value == Constants.IFCLEAR_1) {
                        // 是
                        value = caCodeDefine['是否销假'][1].text;
                    } else if (value == Constants.IFCLEAR_0) {
                        // 否
                        value = caCodeDefine['是否销假'][2].text;
                    }
                    return value;
                }
            }, {
                header : "销假时间",
                width : 120,
                sortable : true,
                dataIndex : 'clearDate'
            }, {
                header : "去向",
                width : 100,
                sortable : true,
                dataIndex : 'whither'
            }, {
                header : "备注",
                width : 100,
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : "请假登记流水号",
                hidden : true,
                dataIndex : 'vacationId'
            }

        ],
        // 请假日期范围，查询btn，打印btn，导出btn
        tbar : headTbar,
        // 分页
        bbar : pagebar
    });
    // 单元格双击事件
    grid.on('celldblclick', dbClickHandler);
    queryStore.on('load', function() {
        if (queryStore.getCount() < 1) {
            Ext.get('print').dom.disabled = true;
            Ext.get('export').dom.disabled = true;
            // 判断是初期化，还是点击的查询按钮
            if(!initFlagW) {
            	Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003)
            }
            if(initFlagW) {
                initFlagW = false;
            }
        } else {
            Ext.get('print').dom.disabled = false;
            Ext.get('export').dom.disabled = false;
        }
    })
    // ----------主画面-----------------

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    // -------------------------------
    /**
     * 格式日期
     */
    function dateFormat(value) {
        var year;
        var month;
        var day;
        day = value.getDate();
        if (day < 10) {
            day = '0' + day;
        }
        month = value.getMonth() + 1;
        if (month < 10) {
            month = '0' + month;
        }
        year = value.getYear();
        value = year + "-" + month + "-" + day;
        return value;
    }
    /**
     * celldblclick事件处理函数
     */
    function dbClickHandler(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = queryStore.getAt(rowIndex);
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        if ("memo" == fieldName) {
            memoText.setValue(queryStore.getAt(rowIndex).get('memo'));
            win.x = undefined;
            win.y = undefined;
            win.show();
        }
        if ("reason" == fieldName) {
            memoText.setValue(queryStore.getAt(rowIndex).get('reason'));
            win.x = undefined;
            win.y = undefined;
            win.show();
        }
    }
    /**
     * 查询处理
     */
    function queryHander() {
    	queryStore.sortInfo = null;
        grid.getView().removeSortIcon();
        // 初始化grid
        queryStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
    }

    /**
     * 打印处理
     */
    function printHander() {
    	window.open("../../../../report/webfile/hr/ca/QJ006_vacationRegisterQuery.jsp?fromDate="+
        startTime.getValue() +"&toDate="+endTime.getValue());
    }
    /**
     * 导出处理
     */
    function exportHander() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
                var url = "ca/exportVacationExcel.action?startTime=" + startTime.getValue() + "&endTime="
                + endTime.getValue()
                document.all.blankFrame.src = url;
            }
        });
    }

    /**
     * 转化审批状态
     */
    function statesFormat(value) {
        switch (value) {
            case '0' : {
                return Constants.NOT_REPORT_MESSAGE;
                break;
            }
            case '1' : {
                return Constants.ALREADY_REPORT_MESSAGE;
                break;
            }
            case '2' : {
                return Constants.ALREADY_OVER_MESSAGE;
                break;
            }
            case '3' : {
                return Constants.ALREADY_RETURN_MESSAGE;
                break;
            }
            default : {
                return;
            }
        }
    }
    /**
     * 三位一隔，并保留2位小数
     */
    function numberFormat2(value) {
        value = String(value);
        // 整数部分
        var whole = value;
        if (whole == null || whole == "") {
            whole = "0";
        }
        // 小数部分
        var sub = ".00";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "00";
            if (sub.length > 3) {
                sub = sub.substring(0, 3);
            }
        }
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return v;
    }
    /**
     * 三位一隔，并保留1位小数
     */
    function numberFormat1(value) {
        value = String(value);
        // 整数部分
        var whole = value;
        if (whole == null || whole == "") {
            whole = "0";
        }
        // 小数部分
        var sub = ".0";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "0";
            if (sub.length > 2) {
                sub = sub.substring(0, 2);
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
