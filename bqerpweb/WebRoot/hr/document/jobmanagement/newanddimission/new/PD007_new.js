Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

    // 自定义函数
    var contractQuery = parent.Ext.getCmp('tabPanel').contractQuery;
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
    startDate.setValue(new Date().format("Y"));

    // 所属部门
    var deptTxt = new Ext.form.TextField({
        id : 'deptTxt',
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

    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
            // 员工编号
            name : 'workerCode'
        }, {
            // 员工姓名
            name : 'workerName'
        }, {
            // 性别
            name : 'sex'
        }, {
            // 员工类别
            name : 'workerType'
        }, {
            // 进厂类别
            name : 'missionType'
        }, {
            // 进厂日期
            name : 'missionDate'
        }, {
            // 身份证号
            name : 'idCard'
        }, {
            // 试用期开始
            name : 'tryStartDate'
        }, {
            // 试用期结束
            name : 'tryEndDate'
        }, {
            // 所属部门
            name : 'department'
        }, {
            // 岗位名称
            name : 'jobName'
        }, {
            // 备注
            name : 'memo'
        }
        ,{
        	//	新工号
        	name : 'newEmpCode'
        }]);

    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getNewEmployInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {
            field : "workerCode",
            direction : "ASC"
        },
        fields : gridData
    });
    var titleLabel= new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region:'north',
        height:35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text:'新进员工花名册',
            style:'font-size:22px;'
        })]
    })

    // 一览grid
    var grid = new Ext.grid.GridPanel({
        autoWidth : true,
        store : gridStore,
        region : 'center',
        border:true,
        sm : new Ext.grid.RowSelectionModel({
            // 单选
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }),/* {
                header : '员工工号',
                width : 80,
                sortable : true,
                hidden : true,
                dataIndex : 'workerCode'
            },{
                header : '员工工号',
                width : 80,
                sortable : true,
                dataIndex : 'newEmpCode'
            },*/ {
                header : '员工姓名',
                width : 120,
                sortable : true,
                dataIndex : 'workerName'
            },/* {
                header : '性别',
                width : 80,
                sortable : true,
                dataIndex : 'sex'
            }, {
                header : '员工类别',
                width : 80,
                sortable : true,
                dataIndex : 'workerType'
            }, {
                header : '进厂类别',
                width : 120,
                sortable : true,
                dataIndex : 'missionType'
            }, */{
                header : '进厂日期',
                width : 80,
                sortable : true,
                dataIndex : 'missionDate'
            },/* {
                header : '身份证号',
                width : 120,
                sortable : true,
                dataIndex : 'idCard'
            }, {
                header : '试用期开始',
                width : 80,
                sortable : true,
                dataIndex : 'tryStartDate'
            }, {
                header : '试用期结束',
                width : 80,
                sortable : true,
                dataIndex : 'tryEndDate'
            }, */{
                header : '工作部门',
                width : 120,
                sortable : true,
                dataIndex : 'department'
            }, {
                header : '岗位名称',
                width : 120,
                sortable : true,
                dataIndex : 'jobName'
            }, {
                header : '备注',
                width : 150,
                sortable : true,
                dataIndex : 'memo'
            }],

        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        frame : false,
        enableColumnHide : true,
        enableColumnMove : false
    });
    // 单元格双击事件
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
          contractQuery.showWin(record.get("memo"));
        }
    }
    
    var fullPanel = new Ext.Panel({
        tbar : ['年度:', startDate, '-', '部门:', deptTxt, hiddenDeptTxt, '-', queryBtn, printBtn, exportBtn],
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
        contractQuery.deptSelect();
        // 根据返回值设置画面的值
        deptTxt.setValue(contractQuery.name);
        Ext.get("deptId").dom.value = contractQuery.id;
    }

    /**
     * 查询处理
     */
    function queryHandler() {
        // 查询基本条件
        gridStore.baseParams = {
            startDate : startDate.getValue(),
            deptId : Ext.get("deptId").dom.value
        };
        // 保存查询条件
        queryObject.startDate = startDate.getValue();
        queryObject.deptId = Ext.get("deptId").dom.value;
        // 查询
        gridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
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
        window.open("../../../../../report/webfile/hr/PD007_newStaff.jsp?year="
        + queryObject.startDate
        + "&dept=" + queryObject.deptId);
    }

    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
                document.all.blankFrame.src = "hr/exportNewEmployFile.action";
            }
        });
    }
    queryHandler();
});