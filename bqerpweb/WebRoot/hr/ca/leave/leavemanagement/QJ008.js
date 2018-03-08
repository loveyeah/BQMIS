Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    // 系统时间
    var systemTime = "";
    // 判断按下的是保存还是上报按钮
    var btnKind = "";
    // 记录查询后的实际换休时间的值
    var change = "0";
    // 初始化加班换休数据
    var initData = "";
    // 修改后加班换休数据
    var updateData = "";
    //是否按下查询按钮
    var flag ="1";
    //查询日期
    var searchDate;
    // 年月
    var dteYearMonth = new Ext.form.TextField({
        id : 'dteYearMonth',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    // 时间格式
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
                this.blur();
            }
        }
    });

    // 部门
    var txtTreDept = new Ext.form.TextField({
        id : 'txtTreDept',
        readOnly : true
    });

    // 部门ID
    var hiddenDeptId = new Ext.form.Hidden({
        id : ''
    })

    // 选择部门
    txtTreDept.onClick(deptSelect);

    // 查询
    var btnSearch = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : search
    })

    // 保存
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : function() {
            btnKind = '0';
            saveOrReport();
        },
        disabled : true
    })

    // 上报
    var btnReport = new Ext.Button({
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        disabled : true,
        handler : function() {
            btnKind = '1';
            saveOrReport();
        }
    })

    // 数据解析
    var recordMain = new Ext.data.Record.create([{
            name : 'empId'
        }, {
            name : 'empName'
        }, {
            name : 'department'
        }, {
            name : 'deptId'
        }, {
            name : 'sumOverTime',
            sortType : "asFloat"
        }, {
            name : 'actualTime'
        }, {
            name : 'approvalStates'
        }, {
            name : 'standardTime'
        }, {
            name : 'sumDays'
        }, {
            name : 'exchangHours',
            sortType : "asFloat"
        }, {
            name : 'lastModifyTime'
        }, {
            name : 'attendanceYear'
        }, {
            name : 'attendanceMonth'
        }])

    // store
    var overTimeStore = new Ext.data.JsonStore({
        url : 'ca/searchLastMonth.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : recordMain
    });

    // 实际换休时间编辑区
    var txtExchageHours = new Powererp.form.NumField({
        allowNegative : false,
        maxLength : 15,
        decimalPrecision : 1,
        maxValue : 99999999999.9,
        addComma : true,
        style : 'text-align:right'
    });

    // grid中的列
    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '员工姓名',
            sortable : true,
            dataIndex : 'empName'
        }, {
            header : '所属部门',
            sortable : true,
            dataIndex : 'department'

        }, {
            header : '月累计加班时间',
            dataIndex : 'sumOverTime',
            align : 'right',
            sortable : true,
            renderer : numberFormat

        }, {
            header : '实际换休时间',
            dataIndex : 'exchangHours',
            align : 'right',
            editor : txtExchageHours,
            sortable : true,
            renderer : numberFormat
        }, {
            header : '审批状态',
            dataIndex : 'approvalStates',
            sortable : true,
            renderer : statesFormat
        }])

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

    // 选择模式
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : ['年月:<font color="red">*</font>', dteYearMonth, '-', '部门:', txtTreDept, '-', btnSearch, btnSave,
            btnReport]
    });

    // grid
    var overTimeGrid = new Ext.grid.EditorGridPanel({
        sm : sm,
        fitToFrame : true,
        cm : colms,
        store : overTimeStore,
        tbar : headTbar,
        frame : false,
        border : false,
        clicksToEdit:1,
        autoScroll : true,
        region : "center",
        layout : "fit",
        enableColumnMove : false,
        autoSizeColumns : true,
        autoSizeHeaders : false
    });
    init();

    overTimeGrid.on("beforeedit", function(obj) {
        var record = obj.record;
        // 根据日期和上报状态决定换休时间是否可编辑
        if (compareDate(searchDate) || record.get('approvalStates') == '1'
        || record.get('approvalStates') == '2') {
            obj.cancel = true;
            return;
        }
    })

    overTimeGrid.on('afteredit', function() {
        updateData = "";
        for (var i = 0; i < overTimeStore.getCount(); i++) {
            var record = overTimeStore.getAt(i).data;
            var temp = record['exchangHours'] + "";
            if (temp.indexOf(".") == -1)
                temp += ".0";
            updateData += temp;
        }
    })

    /**
     * 初始化
     */
    function init() {
        systemTime = new Date();
        var year = systemTime.getYear();
        var month = systemTime.getMonth();
        if (month < 1) {
            month = 12;
            year = year - 1;
        }
        if (month < 10)
            month = "0" + month;
        var lastMonth = year + "-" + month;
        dteYearMonth.setValue(lastMonth);
    }
    overTimeStore.on('load', function() {
        if (overTimeStore.getCount() == 0&&flag =="0") {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
        }
        if (compareDate(dteYearMonth.getValue()) || overTimeStore.getCount() == 0) {
            btnReport.setDisabled(true);
            btnSave.setDisabled(true);
        } else {
            btnReport.setDisabled(false);
            btnSave.setDisabled(false);
        }
        initData = "";
        updateData = "";
        for (var i = 0; i < overTimeStore.getCount(); i++) {
            var record = overTimeStore.getAt(i).data;
            initData += record['exchangHours'];
            updateData += record['exchangHours'];
        }
        flag ="1";
    })

    /**
     * 查询按钮按下
     */
    function search() {
    	searchDate = dteYearMonth.getValue();
    	flag ="0";
    	overTimeStore.sortInfo = null;
        overTimeGrid.getView().removeSortIcon();
        if (updateData == initData)
            change = '0';
        else
            change = '1';
        if (change == '1') {
            Ext.Msg.confirm(Constants.REMIND, Constants.COM_C_004, function(obj) {
                if (obj == "yes") {
                    overTimeStore.load({
                        params : {
                            yearMonth : dteYearMonth.getValue(),
                            deptId : hiddenDeptId.getValue()
                        }
                    })
                    change = '0';
                }
            })
        } else {
            overTimeStore.load({
                params : {
                    yearMonth : dteYearMonth.getValue(),
                    deptId : hiddenDeptId.getValue()
                }
            })
        }
    }

    /**
     * 存放到加班换休登记表中的数据
     */
    function cloneLactionData(record) {
        var objClone = new Object();
        objClone['attendanceYear'] = record['attendanceYear'];
        objClone['attendanceMonth'] = record['attendanceMonth'];
        objClone['empId'] = record['empId'];
        objClone['exchangerestHours'] = record['exchangHours'];
        objClone['signState'] = record['approvalStates'];
        objClone['lastModifyTime'] = record['lastModifyTime'];
        return objClone;
    }

    /**
     * 实际换休时间与月累计时间比较
     */
    function compareValue() {
        for (index = 0; index < overTimeStore.getCount(); index++) {
            var record = overTimeStore.getAt(index).data;
            var sumTime = record['sumOverTime'] * 1;
            var breakTime = record['exchangHours'] * 1;
            if (breakTime > sumTime) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_006, Constants.ACTUAL_BREAK_TIME,
                Constants.CUMULATIVE_TIME));
                return false;
            }
        }
        return true;
    }

    /**
     * 保存或上报按钮按下
     */
    function saveOrReport() {
        if (compareValue()) {
            var remindInfo = '';
            if (btnKind == '0')
                remindInfo = Constants.COM_C_001;
            else
                remindInfo = Constants.COM_C_006;

            Ext.Msg.confirm(Constants.REMIND, remindInfo, function(buttonobj) {
                if (buttonobj == "yes") {
                    var records = new Array();
                    // 循环收集gird值
                    for (index = 0; index < overTimeStore.getCount(); index++) {
                        var record = overTimeStore.getAt(index).data;
                        records.push(cloneLactionData(record))
                    }
                    Ext.Ajax.request({
                        method : 'POST',
                        url : 'ca/saveLastMonth.action',
                        success : function(result, request) {
                            var o = result.responseText;
                            if (o == "U") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            if (o == "E") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            // 页面刷新
                            overTimeStore.load({
                                params : {
                                    yearMonth : dteYearMonth.getValue(),
                                    deptId : hiddenDeptId.getValue()
                                }
                            })
                            if (btnKind == '1') {
                                change = '0';
                                Ext.Msg.alert(Constants.REMIND, String.format(Constants.REPORT_SUCCESS));
                                return;
                            }
                            if (btnKind == '0') {
                                change = '0';
                                Ext.Msg.alert(Constants.REMIND, String.format(Constants.COM_I_004));
                                return;
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        },
                        params : {
                            overTimeStore : Ext.util.JSON.encode(records),
                            btnKind : btnKind
                        }
                    });
                }
            })
        }
    }

    /**
     * 与系统日期比较
     */
    function compareDate(date) {
        var start = Date.parseDate(date, 'Y-m');
        var systemDate = new Date().format('Y-m');
        systemDate = Date.parseDate(systemDate, 'Y-m');
        if (start.getTime() >= systemDate.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 选择部门
     */
    function deptSelect() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : Constants.POWER_NAME
            }
        };
        // 调用画面
        var object = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth='
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT
        + 'px;center=yes;help=no;resizable=no;status=no;');
        // 根据返回值设置画面的值
        if (object) {
            if (typeof(object.names) != "undefined") {
                txtTreDept.setValue(object.names);
            }
            if (typeof(object.ids) != "undefined") {
                hiddenDeptId.setValue(object.ids);
            }
        }
    }
    /**
     * 大数字中间用','分隔 小数点后3位
     */
    function numberFormat(value) {
        if ((value == "" || value == null) && value != "0") {
            return null;
        }
        value = value * 1;
        value = String(value);
        // 整数部分
        var whole = value;
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, '$1' + ',' + '$2');
        }
        var dom = whole.indexOf(".");
        if (dom == -1)
            whole += ".0";
        return whole;
    }

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        items : [overTimeGrid]
    });
})