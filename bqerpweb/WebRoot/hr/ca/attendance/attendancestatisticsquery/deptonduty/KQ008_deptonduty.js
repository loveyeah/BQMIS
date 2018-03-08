Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

    // 自定义函数
    var attendanceQuery = parent.Ext.getCmp('tabPanel').attendanceQuery;
    // 查询条件
    var queryObject = new Object();

    // 年度
    var startDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y',
                    dateFmt : 'yyyy',
                    alwaysUseStartDate : false,
					isShowToday : false,
                    isShowClear : true
                });
                this.blur();
            }
        }
    });
  
    // 考勤月份
    var startMonthDate = new Ext.form.TextField({
        id : 'startMonthDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%M',
                    dateFmt : 'MM',
                    alwaysUseStartDate : false,
					isShowToday : false,
                    isShowClear : true
                });
                this.blur();
            }
        }
    });
    

    // 所属部门
    var deptTxt = new Ext.form.TextField({
        id : 'deptTxt',
        style : 'cursor:pointer',
        width : 120,
        allowBlank : true,
        readOnly : true
    });

    // 所属部门Code
    var hiddenDeptTxt = {
        id : 'deptId',
        xtype : 'hidden',
        value : '',
        readOnly : true,
        hidden : true
    };

    // 选择部门
    deptTxt.onClick(deptSelect);

    // 查询按钮
    var queryBtn = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHandler
    });

    // 打印按钮
    var printBtn = new Ext.Button({
        text : Constants.BTN_PRINT,
        iconCls : Constants.CLS_PRINT,
        disabled : true,
        handler : printHandler
    });

    // 导出按钮
    var exportBtn = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        disabled : true,
        handler : exportHandler
    });
    var titleLabel= new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region:'north',
        height:35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text:'部门出勤统计',
            style:'font-size:22px;'
        })]
    })
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
            // 考勤年份
            name : 'attendanceYear'
        }, {
            // 考勤月份
            name : 'attendanceMonth'
        }, {
            // 人员ID
            name : 'empId'
        }, {
            // 出勤类别ID
            name : 'attendanceTypeId'
        }, {
            // 部门id
            name : 'deptId'
        }, {
            // 中文名称
            name : 'chsName'
        }, {
            // 部门名称
            name : 'deptName'
        }, {
            // 出勤
            name : 'days0',
            sortType : 'asFloat'
        }, {
            // 休息
            name : 'days1',
            sortType : 'asFloat'
        }, {
            // 旷工
            name : 'days2',
            sortType : 'asFloat'
        }, {
            // 迟到
            name : 'days3',
            sortType : 'asFloat'
        }, {
            // 早退
            name : 'days4',
            sortType : 'asFloat'
        }, {
            // 外借
            name : 'days5',
            sortType : 'asFloat'
        }, {
            // 出差
            name : 'days6',
            sortType : 'asFloat'
        }]);

    var gridStore = new Ext.data.JsonStore({
        url : 'ca/getDeptOndutyStatisticsQueryInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
    });

    // 一览grid
    var grid = new Ext.grid.GridPanel({
        autoWidth : true,
        store : gridStore,
        region : 'center',
        sm : new Ext.grid.RowSelectionModel({
            // 单选
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : '考勤年份',
                width : 80,
                sortable : true,
                dataIndex : 'attendanceYear'
            }, {
                header : '考勤月份',
                width : 80,
                sortable : true,
                dataIndex : 'attendanceMonth'
            }, {
                header : '部门名称',
                width : 120,
                sortable : true,
                dataIndex : 'deptName'
            }, {
                header : '姓名',
                width : 80,
                sortable : true,
                dataIndex : 'chsName'
            }, {
                header : '出勤天数',
                width : 80,
                align :'right',
                sortable : true,
                dataIndex : 'days0'
            }, {
                header : '休息天数',
                width : 80,
                sortable : true,
                align :'right',
                dataIndex : 'days1'
            }, {
                header : '旷工天数',
                width : 80,
                sortable : true,
                align :'right',
                dataIndex : 'days2'
            }, {
                header : '迟到天数',
                width : 80,
                align :'right',
                sortable : true,
                dataIndex : 'days3'
            }, {
                header : '早退天数',
                width : 80,
                align :'right',
                sortable : true,
                dataIndex : 'days4'
            }, {
                header : '外借天数',
                width : 80,
                align :'right',
                sortable : true,
                dataIndex : 'days5'
            } , {
                header : '出差天数',
                width : 80,
                align :'right',
                sortable : true,
                dataIndex : 'days6'
            } ],
        frame : false,
        border : true,
        enableColumnHide : true,
        enableColumnMove : false
    });
   
    var fullPanel = new Ext.Panel({
        tbar : ['选择部门:', deptTxt, hiddenDeptTxt, '-','考勤年份:', startDate,  '考勤月份:', startMonthDate, '-', queryBtn, printBtn, exportBtn],
        layout:'border',
        border:false,
        items:[titleLabel,grid]
    })
    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border:false,
        items : [fullPanel]
    });


    /**
     * 选择部门
     */
    function deptSelect() {
        attendanceQuery.deptSelect();
        // 根据返回值设置画面的值
        deptTxt.setValue(attendanceQuery.name);
        Ext.get("deptId").dom.value = attendanceQuery.id;
    }

    /**
     * 查询处理
     */
    function queryHandler() {
        // 查询基本条件
        gridStore.baseParams = {
             year: startDate.getValue(),
             month : startMonthDate.getValue(),
             deptId : Ext.get("deptId").dom.value
        };
        // 保存查询条件
        queryObject.year = startDate.getValue();
        queryObject.deptId = Ext.get("deptId").dom.value;
        queryObject.month = startMonthDate.getValue();
        gridStore.sortInfo=null;
        grid.getView().removeSortIcon();
        // 查询
        gridStore.load({
            params : {
            },
            callback : function() {
                if (gridStore.getCount() == 0) {
                    printBtn.setDisabled(true);
                    exportBtn.setDisabled(true);
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                } else {
                    printBtn.setDisabled(false);
                    exportBtn.setDisabled(false);
                }
            }
        });
    }

    /**
     * 打印处理
     */
    function printHandler() {
        window.open("../../../../../report/webfile/hr/ca/KQ016_deptOndutyStatistics.jsp?deptId="+
        queryObject.deptId +"&year="+queryObject.year+
        "&month="+queryObject.month);
    }

    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
                document.all.blankFrame.src = "ca/exportDeptOndutyStatisticsQueryInfo.action?deptId="+
                queryObject.deptId+"&year="+queryObject.year+"&month="+queryObject.month;
            }
        });
    }
});