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
                    isShowClear : false
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
            // 所属部门
            name : 'deptName'
        }, {
            // 岗位名称
            name : 'stationName'
        }, {
            // 岗位标准人数
            name : 'stationNum'
        }, {
            // 现人数
            name : 'nowNum'
        }, {
            // 新进人数
            name : 'newNum'
        }, {
            // 离职人数
            name : 'dimissionNum'
        }]);

    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getTotalInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
//        sortInfo : {
//            field : "deptName",
//            direction : "ASC"
//        },
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
                header : '所属部门',
                width : 120,
                sortable : false,
                dataIndex : 'deptName',
                renderer : contractQuery.checkName

            }, {
                header : '岗位名称',
                width : 120,
                sortable : false,
                dataIndex : 'stationName'
            }, {
                header : '岗位标准人数',
                width : 100,
                align : 'right',
                sortable : false,
                dataIndex : 'stationNum',
                renderer : addcom
            }, {
                header : '现人数',
                width : 100,
                align : 'right',
                sortable : false,
                dataIndex : 'nowNum',
                renderer : addcom
            }, {
                header : '新进人数',
                width : 100,
                align : 'right',
                sortable : false,
                dataIndex : 'newNum',
                renderer : addcom
            }, {
                header : '离职人数',
                width : 100,
                align : 'right',
                sortable : false,
                dataIndex : 'dimissionNum',
                renderer : addcom
            }],
        tbar : ['年度<font color="red">*</font>:', startDate, '-',
        // modified by liuyi 091210 不需部门查询，默认显示一级部门
//        		'部门:', deptTxt, hiddenDeptTxt, '-', 
        		queryBtn,
            printBtn, exportBtn],
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        frame : false,
        border : true,
        enableColumnHide : true,
        enableColumnMove : false
    });

    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
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
        if (startDate.getValue() != "") {
            // 查询基本条件
            gridStore.baseParams = {
                startDate : startDate.getValue()
                // modified by liuyi 091210 不需按部门查询
//                ,
//                deptId : Ext.get("deptId").dom.value
            };
            // 保存查询条件
            queryObject.startDate = startDate.getValue();
             // modified by liuyi 091210 不需按部门查询
//            queryObject.deptId = Ext.get("deptId").dom.value;
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
        } else {

        }
    }

    /**
     * 打印处理
     */
    function printHandler() {
        window.open("../../../../../report/webfile/hr/PD007_gotAndLoseTotal.jsp?year="
        + queryObject.startDate
        + "&dept=" + queryObject.deptId);
    }

    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
                document.all.blankFrame.src = "hr/exportTotalEmployFile.action";
            }
        });
    }
    
    /**
     * 用于每隔3位加一个逗号
     */
    function addcom(v) {
    	if (v) {
			var t = '';
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			return v;
		} else
			return '';
    }
    
    queryHandler();
});