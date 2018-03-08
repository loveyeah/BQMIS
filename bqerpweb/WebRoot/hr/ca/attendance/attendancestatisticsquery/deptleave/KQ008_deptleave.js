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
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
                this.blur();
            }
        }
    });
    
     // 审批状态
    var cmbStatus = new Ext.form.CmbCACode({
        id : "signState",
        type : "签字状态",
        width : 100,
        value : '',
        name : 'signState'
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
            text:'部门请假单',
            style:'font-size:22px;'
        })]
    })
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
            // 请假id
            name : 'vacationId'
        }, {
            // 部门id
            name : 'deptId'
        }, {
            // 部门名称
            name : 'deptName'
        }, {
            // 员工id
            name : 'empId'
        }, {
            // 员工名称
            name : 'chsName'
        }, {
            // 假别ID
            name : 'vacationTypeId'
        }, {
            // 假别
            name : 'vacationType'
        }, {
            // 开始时间
            name : 'startTime'
        }, {
            // 结束时间
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
            // 原因
            name : 'reason'
        }, {
            // 去向
            name : 'whither'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 是否销假
            name : 'ifClear'
        }, {
            // 销假时间
            name : 'clearDate'
        }, {
            // 审批状态
            name : 'signState'
        }]);

    var gridStore = new Ext.data.JsonStore({
        url : 'ca/getDeptleaveStatisticsQueryInfo.action',
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
                header : '部门名称',
                width : 120,
                sortable : true,
                dataIndex : 'deptName'
            }, {
                header : '姓名',
                width : 120,
                sortable : true,
                dataIndex : 'chsName'
            }, {
                header : '假别',
                width : 100,
                sortable : true,
                dataIndex : 'vacationType'
            }, {
                header : '开始时间',
                width : 120,
                sortable : true,
                dataIndex : 'startTime'
            }, {
                header : '结束时间',
                width : 120,
                sortable : true,
                dataIndex : 'endTime'
            }, {
                header : '请假天数',
                width : 100,
                align :'right',
                sortable : true,
                dataIndex : 'vacationDays'
            }, {
                header : '请假时长',
                width : 100,
                align :'right',
                sortable : true,
                dataIndex : 'vacationTime'
            }, {
                header : '原因',
                width : 100,
                sortable : true,
                dataIndex : 'reason'
            }, {
                header : '去向',
                width : 100,
                sortable : true,
                dataIndex : 'whither'
            }, {
                header : '备注',
                width : 150,
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : '是否销假',
                width : 100,
                sortable : true,
                dataIndex : 'ifClear'
            }, {
                header : '销假时间',
                width : 120,
                sortable : true,
                dataIndex : 'clearDate'
            }, {
                header : '审批状态',
                width : 100,
                sortable : true,
                dataIndex : 'signState'
            }],
       
        frame : false,
        border : true,
        enableColumnHide : true,
        enableColumnMove : false
    });
    
     grid.on('celldblclick', dbClickHandler);
    /**
     * celldblclick事件处理函数
     */
    function dbClickHandler(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = gridStore.getAt(rowIndex); 
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        if("memo" == fieldName) {
          attendanceQuery.showWin(record.get("memo"));
        }
         if("reason" == fieldName) {
          attendanceQuery.showWin(record.get("reason"));
        }
    }
    
    var fullPanel = new Ext.Panel({
        tbar : ['选择部门:', deptTxt, hiddenDeptTxt, '-','开始年月:', startDate, '-',"审批状态:", cmbStatus,"-" ,queryBtn, printBtn, exportBtn],
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
            yearMonth : startDate.getValue(),
            signState : cmbStatus.getValue(),
            deptId : Ext.get("deptId").dom.value
        };
        queryObject.yearMonth = startDate.getValue();
        queryObject.signState = cmbStatus.getValue();
        queryObject.deptId = Ext.get("deptId").dom.value;
        gridStore.sortInfo=null;
        grid.getView().removeSortIcon();
        // 查询
        gridStore.load({
            params : {},
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
        window.open("../../../../../report/webfile/hr/ca/KQ017_deptleaveStatistics.jsp?deptId="+
        queryObject.deptId +"&yearMonth="+queryObject.yearMonth+
        "&signState="+queryObject.signState);
    }

    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
                document.all.blankFrame.src = "ca/exportDeptleaveStatisticsQueryInfo.action?yearMonth="+
                queryObject.yearMonth+"&deptId="+queryObject.deptId+"&signState="+queryObject.signState;
            }
        });
    }
});