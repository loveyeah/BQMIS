// 员工请假登记
// author:zhaozhijie
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

    Ext.QuickTips.init();
    // 新增/修改flag
    var newFlag = true;
    var ifWeekend;
    var startSysDate = "";
    var endSysDate = "";
    var startOldDate = "";
    var endOldDate = "";
    var oldObject = [];
    var startOldDateArray = [];
    var endOldDateArray = [];
    var HOLIDAY_TYPE_NO = "1";
    var HOLIDAY_TYPE_YES = "2";

    // 员工姓名
    var txtEmp = new Ext.form.TextField({
        width : 100,
        displayField : 'text',
        valueField : 'id',
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : Constants.POWER_NAME
                    },
                    onlyLeaf : false
                };
                this.blur();
                var winEmp = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
                'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
                + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(winEmp) != "undefined") {
                    txtEmp.setValue(winEmp.workerName);
                    hdnEmp.setValue(winEmp.empId);
                }
            }
        }
    });
    // 员工id
    var hdnEmp = new Ext.form.Hidden({
        id : 'empID',
        name : 'empID'
    })
    // 所属部门
    var txtTreeDept = new Ext.form.TextField({
        width : 100,
        readOnly : true,
        listeners : {
            render : function() {
                var click = function() {
                    // 弹出部门选择树
                    var objReturn = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp', null,
                    'dialogWidth=' + Constants.WIDTH_COM_DEPT + 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT
                    + 'px;center=yes;help=no;resizable=no;status=no;')
                    // 将部门名称显示到画面
                    if (objReturn) {
                        objDeptName = objReturn;
                        txtTreeDept.setValue(objDeptName.names);
                        hdnDept.setValue(objDeptName.ids);
                    }
                }
                this.el.on('click', click);
            }
        }
    })
    // 部门id
    var hdnDept = new Ext.form.Hidden({
        id : 'deptID',
        name : 'deptID'
    })
    var vacationStart = new Date();
    vacationStart = vacationStart.format('Y-01-01');

    // 请假开始日
    var txtVacationStart = new Ext.form.TextField({
        width : 100,
        style : 'cursor:pointer',
        allowBlank : true,
        readOnly : true,
        value : vacationStart,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : true,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    })
    var vacationEnd = new Date();
    vacationEnd = vacationEnd.format('Y-m-d');
    // 请假结束日
    var txtVacationEnd = new Ext.form.TextField({
        width : 100,
        style : 'cursor:pointer',
        allowBlank : true,
        value : vacationEnd,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : true,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    })

    // 查询button
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHandler
    })
    // 确认button
    var btnSubmit = new Ext.Button({
        text : Constants.BTN_CONFIRM,
        iconCls : Constants.CLS_OK,
        handler : submitHandler
    })
    // 审批查询button
    var btnApprove = new Ext.Button({
        text : '审批查询',
        iconCls : Constants.CLS_CHECK_QUERY,
        handler : approveHandler
    })
    // Gridtbar
    var tbarGrid = new Ext.Toolbar({
        border : false,
        height : 25,
        items : ["员工姓名:", txtEmp, '-', '所属部门:', txtTreeDept, '-', '请假开始日:', txtVacationStart, '~', txtVacationEnd, '-',
            btnQuery, btnSubmit, btnApprove]
    })
    // store的data
    var dataVacation = Ext.data.Record.create([
        // 审批状态
        {
        name : "signState"
    },
        // 请假id
        {
            name : "vacationId"
        },
        // 员工姓名
        {
            name : "empName"
        },
        // 员工ID
        {
            name : "empId"
        },
        // 所属部门
        {
            name : "deptName"
        },
        // 所属部门ID
        {
            name : "deptId"
        },
        // 请假开始时间
        {
            name : "startTime"
        },
        // 请假结束时间
        {
            name : "endTime"
        },
        // 请假类别
        {
            name : "vacationType"
        },
        // 假别ID
        {
            name : "vacationTypeId"
        },
        // 是否包括周末
        {
            name : "ifWeekend"
        },
        // 请假天数
        {
            name : "vacationDays",
            sortType : "asFloat"
        },
        // 请假时长
        {
            name : "vacationHours",
            sortType : "asFloat"
        },
        // 原因
        {
            name : "reason"
        },
        // 去向
        {
            name : "whither"
        },
        // 备注
        {
            name : "memo"
        },
        // 上次修改时间
        {
            name : "lastModifyDate"
        }])
    // store
    var dsVacation = new Ext.data.JsonStore({
        url : 'ca/getVacationInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : dataVacation,
        listeners : {
            loadexception : function(ds, records, o) {
                var o = eval("(" + o.responseText + ")");
                if (o.msg != null) {
                    var succ = o.msg;
                    if (succ == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    } else if (succ == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        }
    })

    dsVacation.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE,
            startTime : vacationStart,
            endTime : vacationEnd
        }
    })
    dsVacation.on('beforeload', function() {
        Ext.apply(this.baseParams, {
            startTime : txtVacationStart.getValue(),
            endTime : txtVacationEnd.getValue(),
            deptId : hdnDept.getValue(),
            empId : hdnEmp.getValue()
        });
        // 页面刷新后grid排序状态恢复到画面初始化状态
        dsVacation.sortInfo = null;
        gridVacation.getView().removeSortIcon();
    });
    var gridVacation = new Ext.grid.GridPanel({
        layout : 'fit',
        border : false,
        autoWidth : true,
        autoScroll : true,
        // 标题不可以移动
        enableColumnMove : false,
        store : dsVacation,
        tbar : tbarGrid,
        // 单选
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                align : 'right',
                width : 35
            }),
            // 审批状态
            {
                header : "审批状态",
                width : 100,
                sortable : true,
                align : 'left',
                renderer : renderStates,
                dataIndex : 'signState'
            },
            // 员工姓名
            {
                header : "员工姓名",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'empName'
            },
            // 所属部门
            {
                header : "所属部门",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'deptName'
            },
            // 请假开始时间
            {
                header : "请假开始时间",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'startTime'
            },
            // 请假结束时间
            {
                header : "请假结束时间",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'endTime'
            },
            // 请假类别
            {
                header : "请假类别",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'vacationType'
            },
            // 请假天数
            {
                header : "请假天数",
                width : 100,
                sortable : true,
                align : 'right',
                dataIndex : 'vacationDays',
                renderer : divide
            },
            // 请假时长
            {
                header : "请假时长",
                width : 100,
                sortable : true,
                renderer : divideHour,
                align : 'right',
                dataIndex : 'vacationHours'
            }],
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : dsVacation,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    })

    // 双击处理
    gridVacation.on("rowdblclick", doubleMouseHandler);

    // 新增按钮
    var btnNew = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : newHandler
    })
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveHandler
    })
    // 删除按钮
    var btnDel = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        disabled : true,
        handler : deleteHandler
    })
    // 上报按钮
    var btnReport = new Ext.Button({
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        disabled : true,
        handler : reportHandler
    })
    // 按钮Toolbar
    var btnTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : [btnNew, btnSave, btnDel, btnReport]
    });
    // 员工姓名
    var txtEmpName = new Ext.form.TextField({
        width : 120,
        fieldLabel : "员工姓名<font color='red'>*</font>",
        id : "empVacationInfo.empName",
        displayField : 'text',
        readOnly : true,
        validateOnBlur : false,
        valueField : 'id',
        allowBlank : false
    });
    txtEmpName.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : Constants.POWER_NAME
            },
            onlyLeaf : false
        };
        this.blur();
        var winEmp = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
        'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        txtEmpName.clearInvalid();
        if (typeof(winEmp) != "undefined") {
            // 新增的场合
            if (newFlag) {
                // 人员id不为空的场合
                if (winEmp.empId) {
                    // 设值
                    setAttendDept(winEmp.empId);
                    // 开始时间
                    var startDate = new Date();
                    startDate = startDate.format('Y-m-d');
                    // 结束时间
                    var endDate = new Date();
                    endDate = endDate.format('Y-m-d');
                    // 得到本年度的上，下班时间
                    Ext.Ajax.request({
                        url : 'ca/getOnDutyTime.action',
                        method : Constants.POST,
                        params : {
                            empId : winEmp.empId
                        },
                        success : function(action) {
                            var result = eval("(" + action.responseText + ")");
                            if (result.list) {
                                if (result.list[0]) {
                                    if (result.list[0].amBegingTime) {
                                        startSysDate = startDate + " " + result.list[0].amBegingTime;
                                        endSysDate = endDate + " " + result.list[0].pmEndTime;
                                        // 开始时间
                                        txtStartTime.setValue(startSysDate);
                                        // 结束时间
                                        txtEndTime.setValue(endSysDate);
                                        startOldDateArray.push(startSysDate);
                                        endOldDateArray.push(endSysDate);
                                        hdnEmpId.setValue(winEmp.empId);
                                        txtEmpName.setValue(winEmp.workerName);
                                        hdnDeptId.setValue(winEmp.deptId);
                                        txtTreeDeptName.setValue(winEmp.deptName);
                                        calculateHandler();
                                    } else {
                                        Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_I_029,
                                        result.list[0].attendanceDeptName), function() {
                                            formPanel.getForm().reset();
                                            cmbVacationType.setValue(null);
                                            btnDel.setDisabled(true);
                                            cmbVacationType.clearInvalid();
                                            cmbVacationType.setDisabled(true);
                                            btnReport.setDisabled(true);
                                            txtStartTime.setDisabled(true);
                                            txtEndTime.setDisabled(true);
                                        });
                                    }
                                }
//                                Ext.Msg.alert('提示','此人所属考勤部门未指定！');
                            } else {
                                if (result.msg == Constants.SQL_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                } else if (result.msg == Constants.IO_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                                } 
                            }
                        }
                    })

                    // 设置控件可用
                    controlUseHandler();
                    // 人员id为空的场合
                } else {
                    newNullHandler();
                }
                // 修改的场合
            } else {
                // 人员id不为空的场合
                if (winEmp.empId) {
                    // 设值
                    setAttendDept(winEmp.empId);
                    txtEmpName.setValue(winEmp.workerName);
                    hdnDeptId.setValue(winEmp.deptId);
                    txtTreeDeptName.setValue(winEmp.deptName);
                    hdnEmpId.setValue(winEmp.empId);
                    calculateHandler();

                    // 设置控件可用
                    controlUseHandler();
                    // 人员id为空的场合
                } else {
                    modifyNullHandler();
                }
            }

        }
    })
    txtEmpName.on("change", calculateHandler);
    // 员工id
    var hdnEmpId = new Ext.form.Hidden({
        id : 'empVacationInfo.empId',
        name : 'empVacationInfo.empId'
    })
    // 节假日Store
    var dsHoliday = new Ext.data.JsonStore({
        url : 'ca/getHoliday.action',
        root : 'list',
        fields : ['holidayId', 'holidayDate', 'holidayType'],
        listeners : {
            loadexception : function(ds, records, o) {
                var o = eval("(" + o.responseText + ")");
                if (o.msg != null) {
                    var succ = o.msg;
                    // sql失败
                    if (succ == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        // io失败
                    } else if (succ == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        }
    })
    dsHoliday.load();
    // 所属部门
    var txtTreeDeptName = new Ext.form.TextField({
        id : 'empVacationInfo.deptName',
        fieldLabel : "所属部门",
        width : 120,
        disabled : true
    })
    // 部门id
    var hdnDeptId = new Ext.form.Hidden({
        id : 'empVacationInfo.deptId',
        name : 'empVacationInfo.deptId'
    })
    // 考勤部门
    var txtAttendDeptName = new Ext.form.TextField({
        id : 'empVacationInfo.attendanceDeptName',
        fieldLabel : "考勤部门",
        width : 135,
        disabled : true
    })
    // 考勤部门id
    var hdnAttendDeptId = new Ext.form.Hidden({
        id : 'empVacationInfo.attendanceDeptId',
        name : 'empVacationInfo.attendanceDeptId'
    })
    // 请假类别Store
    var dsVacationType = new Ext.data.JsonStore({
        url : 'ca/getVacationType.action',
        root : 'list',
        fields : ['vacationType', 'vacationTypeId', 'ifWeekend'],
        listeners : {
            loadexception : function(ds, records, o) {
                var o = eval("(" + o.responseText + ")");
                if (o.msg != null) {
                    var succ = o.msg;
                    // sql失败
                    if (succ == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        // io失败
                    } else if (succ == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        }
    })
    dsVacationType.load();
    // 请假类别
    var cmbVacationType = new Ext.form.ComboBox({
        width : 135,
        allowBlank : false,
        id : "cmbVacationType",
        forceSelection : true,
        fieldLabel : "请假类别<font color='red'>*</font>",
        triggerAction : 'all',
        mode : 'local',
        displayField : 'vacationType',
        valueField : 'vacationTypeId',
        store : dsVacationType,
        readOnly : true,
        disabled : true
    })
    cmbVacationType.on("collapse", function() {
        // 请假类别为空的时候
        if (cmbVacationType.getValue() == null) {
            txtVacationDays.setValue("");
            txtVacationHours.setValue("");
        } else {
            if (txtStartTime.getValue() <= txtEndTime.getValue()) {
                calculateHandler();
            } else {
                // 清空请假时长和天数
                txtVacationDays.setValue("");
                txtVacationHours.setValue("");
                hdnVacationDays.setValue("");
                hdnVacationHours.setValue("");
            }
        }
    });
    cmbVacationType.on("beforeselect", function(t, record, index) {
        ifWeekend = record.get("ifWeekend");
    });

    // 第一行
    var fldFirst = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '100%',
        items : [{
                border : false,
                columnWidth : 0.33,
                layout : 'form',
                items : [txtEmpName]
            }, {
                border : false,
                columnWidth : 0.33,
                layout : 'form',
                items : [txtTreeDeptName]
            }, {
                border : false,
                columnWidth : 0.33,
                layout : 'form',
                items : [txtAttendDeptName]
            }]
    })
    // 开始时间
    var txtStartTime = new Ext.form.TextField({
        id : 'empVacationInfo.startTime',
        name : 'empVacationInfo.startTime',
        fieldLabel : "开始时间<font color='red'>*</font>",
        width : 120,
        style : 'cursor:pointer',
        allowBlank : false,
        readOnly : true,
        disabled : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : false,
                    startDate : '%y-%M-%d %H-%m',
                    dateFmt : 'yyyy-MM-dd HH:mm',
                    onpicked : timeChangeHandler
                });
                this.blur();
            }

        }
    })
    // 结束时间
    var txtEndTime = new Ext.form.TextField({
        id : 'empVacationInfo.endTime',
        name : 'empVacationInfo.endTime',
        fieldLabel : "结束时间<font color='red'>*</font>",
        width : 120,
        style : 'cursor:pointer',
        allowBlank : false,
        readOnly : true,
        disabled : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : false,
                    startDate : '%y-%M-%d %H-%m',
                    dateFmt : 'yyyy-MM-dd HH:mm',
                    onpicked : timeChangeHandler
                });
                this.blur();
            }
        }
    })
    txtEndTime.on("change", timeChangeHandler);
    // 第二行
    var fldSecond = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '100%',
        items : [{
                columnWidth : 0.33,
                border : false,
                layout : 'form',
                items : [txtStartTime]
            }, {
                columnWidth : 0.33,
                border : false,
                layout : 'form',
                items : [txtEndTime]
            }, {
                columnWidth : 0.33,
                border : false,
                layout : 'form',
                items : [cmbVacationType]
            }]
    })
    // 请假天数
    var txtVacationDays = new Powererp.form.NumField({
        fieldLabel : '请假天数',
        style : "text-align:right",
        width : 120,
        padding : 2,
        disabled : true
    })
    var hdnVacationDays = new Ext.form.Hidden({
        id : 'empVacationInfo.vacationDays',
        name : 'empVacationInfo.vacationDays'
    })
    var lblVacationDays = new Ext.form.Label({
        width : 12,
        text : "天"
    })
    // 请假时长
    var txtVacationHours = new Powererp.form.NumField({
        fieldLabel : '请假时长',
        padding : 1,
        width : 120,
        style : "text-align:right",
        disabled : true
    })
    var hdnVacationHours = new Ext.form.Hidden({
        id : 'empVacationInfo.vacationHours',
        name : 'empVacationInfo.vacationHours'
    })
    var lblVacationHours = new Ext.form.Label({
        width : 26,
        text : "小时"
    })
    // 第三行
    var fldThree = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '100%',
        items : [{
                width : 210,
                border : false,
                layout : 'form',
                items : [txtVacationDays]
            }, {
                width : 30,
                border : false,
                layout : 'form',
                style : 'padding-top:5;font-size:12;',
                items : [lblVacationDays]
            }, {
                width : 0,
                border : false,
                layout : 'form',
                style : 'padding-top:5;font-size:12;',
                items : ""
            }, {
                width : 210,
                border : false,
                layout : 'form',
                items : [txtVacationHours]
            }, {
                width : 60,
                border : false,
                layout : 'form',
                style : 'padding-top:5;font-size:12;',
                items : [lblVacationHours]
            }]
    })
    // 原因
    var txaReason = new Ext.form.TextArea({
        id : 'empVacationInfo.reason',
        name : 'empVacationInfo.reason',
        height : 47,
        anchor : '98.5%',
        maxLength : 200,
        fieldLabel : '原因'
    })
    // 第四行
    var fldFour = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '100%',
        items : [{
                columnWidth : 0.97,
                border : false,
                layout : 'form',
                items : [txaReason]
            }]
    })
    // 去向
    var txtGo = new Ext.form.TextField({
        id : 'empVacationInfo.whither',
        name : 'empVacationInfo.whither',
        anchor : '100%',
        maxLength : 50,
        fieldLabel : '去向'
    })
    // 第五行
    var fldFive = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '99%',
        items : [{
                columnWidth : 0.97,
                border : false,
                layout : 'form',
                items : [txtGo]
            }]
    })
    // 备注
    var txaMemo = new Ext.form.TextArea({
        id : 'empVacationInfo.memo',
        name : 'empVacationInfo.memo',
        anchor : '98.5%',
        height : 47,
        maxLength : 128,
        fieldLabel : '备注'
    })
    // 第六行
    var fldSix = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        style : "padding-top:4px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        anchor : '100%',
        items : [{
                columnWidth : 0.97,
                border : false,
                layout : 'form',
                items : [txaMemo]
            }]
    })

    // 上次修改时间
    var hdnLastDate = new Ext.form.Hidden({
        id : 'empVacationInfo.lastModifyDate',
        name : 'empVacationInfo.lastModifyDate'
    })
    // 请假id
    var hdnVacationId = new Ext.form.Hidden({
        id : 'empVacationInfo.vacationId',
        name : 'empVacationInfo.vacationId'
    })
    // 假别id
    var hdnVacationTypeId = new Ext.form.Hidden({
        id : 'empVacationInfo.vacationTypeId',
        name : 'empVacationInfo.vacationTypeId'
    })
    // formPanel
    var formPanel = new Ext.FormPanel({
        labelAlign : 'right',
        labelWidth : 80,
        width : 740,
        border : false,
        layout : 'form',
        autoScroll : true,
        items : [fldFirst, fldSecond, fldThree, fldFour, fldFive, fldSix, hdnLastDate, hdnVacationId,
            hdnVacationTypeId, hdnEmpId, hdnDeptId, hdnVacationHours, hdnVacationDays, hdnAttendDeptId]
    })
    // searchPanel
    var searchPanel = new Ext.Panel({
        layout : 'fit',
        autoScroll : false,
        border : false,
        id : 'searchPanel',
        title : '查询',
        items : [gridVacation]
    })

    // registerPanel
    var registerPanel = new Ext.Panel({
        layout : 'border',
        border : false,
        id : 'registerPanel',
        title : '登记',
        items : [btnTbar, {
                region : 'center',
                layout : 'form',
                frame : true,
                border : false,
                autoScroll : true,
                items : [formPanel]
            }]
    })
    // tabPanel
    var tabPanel = new Ext.TabPanel({
        activeTab : 0,
        layoutOnTabChange : true,
        tabPosition : 'bottom',
        autoScroll : false,
        items : [searchPanel, registerPanel]
    });
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'fit',
        margins : '0 0 0 0',
        region : 'center',
        border : true,
        items : [tabPanel]
    });
    // --------------------------------------------------------------事件处理-------------------------------
    var originalData = null;
    /**
     * 查询操作
     */
    function queryHandler() {
        vacationEnd = txtVacationEnd.getValue();
        vacationStart = txtVacationStart.getValue();
        dsVacation.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE,
                startTime : txtVacationStart.getValue(),
                endTime : txtVacationEnd.getValue(),
                deptId : hdnDept.getValue(),
                empId : hdnEmp.getValue()
            },
            callback : function() {
                // 没有检索到数据的情况
                if (dsVacation.getCount() < 1) {
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                }
            }
        })
    }
    /**
     * 确认操作
     */
    function submitHandler() {
        if (!gridVacation.selModel.hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
            return;
        }
        doubleMouseHandler();
    }
    /**
     * 审批查询
     */
    function approveHandler() {
        // TODO
    	alert('审批查询未做')
    }
    /**
     * 新增操作
     */
    function newHandler() {
        // 画面有改变
        if (isFormChange()) {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_004, function(button) {
                if (button == 'yes') {
                    formPanel.getForm().reset();
                    cmbVacationType.setValue(null);
                    btnDel.setDisabled(true);
                    btnSave.setDisabled(false);
                    cmbVacationType.clearInvalid();
                    cmbVacationType.setDisabled(true);
                    txtStartTime.setDisabled(true);
                    txtEndTime.setDisabled(true);
                    txtEmpName.setDisabled(false);
                    txtGo.setDisabled(false);
                    txaMemo.setDisabled(false);
                    txaReason.setDisabled(false);
                    originalData = formPanel.getForm().getValues();
                } else {
                    return;
                }
            })
        } else {
            formPanel.getForm().reset();
            cmbVacationType.setValue(null);
            btnDel.setDisabled(true);
            btnSave.setDisabled(false);
            cmbVacationType.clearInvalid();
            cmbVacationType.setDisabled(true);
            txtStartTime.setDisabled(true);
            txtEndTime.setDisabled(true);
            txtEmpName.setDisabled(false);
            txtGo.setDisabled(false);
            txaMemo.setDisabled(false);
            txaReason.setDisabled(false);
            originalData = formPanel.getForm().getValues();
        }
        newFlag = true;

    }
    /**
     * 保存操作
     */
    function saveHandler() {
        // 画面为空check
        if (!inputCheck()) {
            return;
        }

        // 画面修改check
        if (!isFormChange()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_006);
            return;
        }

        // 编码为空标志
        var flagCode = false;
        var msg = "";
        var retSFlag = false;
        var retEFlag = false;
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(button) {
            if (button == 'yes') {
                // 事务check
                // 开始时间不能大于结束时间的场合check
                if (txtStartTime.getValue() > txtEndTime.getValue()) {
                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_005, "结束时间", "开始时间"));
                    return;
                }
                var record = dsHoliday.getRange(0, dsHoliday.getCount());
                // 开始时间不为空的情况
                if (txtStartTime.getValue()) {
                    // 开始时间
                    var startDay = Date.parseDate(txtStartTime.getValue(), 'Y-m-d h:m');
                    // 开始时间是节假日的check
                    for (var i = 0; i < dsHoliday.getCount(); i++) {
                        if (startDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
                        && record[i].data["holidayType"] != HOLIDAY_TYPE_YES) {
                            if (msg != '') {
                                msg += "<br>";
                            }
                            msg += String.format(Constants.COM_E_054, "开始时间");
                            flagCode = true;
                            retSFlag = true;
                        }
                    }
                    // 开始时间是周末的check
                    var startDayWeek = startDay.getDay();
                    if (startDayWeek == 6 || startDayWeek == 0) {
                        var startFlag = false;
                        // 开始时间是周末上班时间的check
                        for (var i = 0; i < dsHoliday.getCount(); i++) {
                            if (startDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
                            && record[i].data["holidayType"] != HOLIDAY_TYPE_NO) {
                                startFlag = true;
                            }
                        }
                        if (!startFlag) {
                            if (msg != '') {
                                msg += "<br>";
                            }
                            msg += String.format(Constants.COM_E_054, "开始时间");
                            flagCode = true;
                            retSFlag = true;
                        }
                    }
                }
                // 结束时间不为空的情况
                if (txtEndTime.getValue()) {
                    // 结束时间
                    var endDay = Date.parseDate(txtEndTime.getValue(), 'Y-m-d h:m');

                    // 结束时间是节假日的check
                    for (var i = 0; i < dsHoliday.getCount(); i++) {
                        if (endDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
                        && record[i].data["holidayType"] != HOLIDAY_TYPE_YES) {
                            if (msg != '') {
                                msg += "<br>";
                            }
                            msg += String.format(Constants.COM_E_054, "结束时间");
                            flagCode = true;
                            retEFlag = true;
                        }
                    }
                    // 结束时间是周末的check
                    var endDayWeek = endDay.getDay();
                    if (endDayWeek == 6 || endDayWeek == 0) {
                        var endFlag = false;
                        // 结束时间是周末上班的check
                        for (var i = 0; i < dsHoliday.getCount(); i++) {
                            if (endDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
                            && record[i].data["holidayType"] != HOLIDAY_TYPE_NO) {
                                endFlag = true;
                            }
                        }
                        if (!endFlag) {
                            if (msg != '') {
                                msg += "<br>";
                            }
                            msg += String.format(Constants.COM_E_054, "结束时间");
                            flagCode = true;
                            retEFlag = true;
                        }
                    }
                }
                // 输入必要数据提示信息
                if (flagCode) {
                    Ext.Msg.alert(Constants.ERROR, msg);
                    return;
                }
                // 时长为0check
                if (hdnVacationHours.getValue() == 0) {
                    Ext.Msg.alert(Constants.ERROR, Constants.QJ003_E_004);
                    return;
                }
                formPanel.getForm().submit({
                    method : Constants.POST,
                    url : "ca/saveVacation.action",
                    params : {
                        newFlag : newFlag,
                        flag : false,
                        ifWeekend : ifWeekend
                    },
                    success : function(from, action) {
                        var o = eval("(" + action.response.responseText + ")");
                        // sql失败
                        if (o.msg == Constants.SQL_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            return;
                        }
                        // 排他处理
                        if (o.msg == Constants.DATA_USING) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                            return;
                        }
                        // 计划check
                        if (o.msg == 'TIME') {
                            var startYear = txtStartTime.getValue();
                            startYear = startYear.substring(0, 4);
                            var vacationTypeName = Ext.get("cmbVacationType").dom.value;
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.QJ003_E_003, startYear,
                            vacationTypeName, o.data));
                            returnTime(true, true);
                            calculateHandler();
                            return;
                        }
                        // 重复check
                        if (o.msg == 'REPEAT') {
                            Ext.Msg.alert(Constants.ERROR, Constants.QJ003_E_001);
                            returnTime(true, true);
                            calculateHandler();
                            return;
                            // 保存成功
                        } else {
                            if (o.list[0].attendanceDeptName) {
                                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_I_029,
                                o.list[0].attendanceDeptName), function() {
                                    var startLength = startOldDateArray.length;
                                    var endLength = endOldDateArray.length;
                                    if (startLength > 1) {
                                        txtStartTime.setValue(startOldDateArray[startLength - 2]);
                                    } else if (startLength == 1) {
                                        txtStartTime.setValue(startOldDateArray[0]);
                                    }
                                    if (endLength > 1) {
                                        txtEndTime.setValue(endOldDateArray[endLength - 2]);
                                    } else if (endLength == 1) {
                                        txtEndTime.setValue(endOldDateArray[0]);
                                    }
                                    startOldDateArray.push(txtStartTime.getValue());
                                    endOldDateArray.push(txtEndTime.getValue());
                                    calculateHandler();
                                    return;
                                });
                            }
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                btnDel.setDisabled(false);
                                btnReport.setDisabled(false);
                                startOldDateArray = [];
                                endOldDateArray = [];
                                setSaveData(o.list[0]);
                                newFlag = false;
                                originalData = formPanel.getForm().getValues();
                            });
                            dsVacation.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE,
                                    startTime : vacationStart,
                                    endTime : vacationEnd
                                }
                            });

                        }
                    },
                    faliue : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    }
                });
            }
        })
    }

    /**
     * 删除操作
     */
    function deleteHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(button) {
            if (button == 'yes') {
                formPanel.getForm().submit({
                    method : Constants.POST,
                    url : "ca/saveVacation.action",
                    params : {
                        flag : true
                    },
                    success : function(form, action) {
                        var o = eval("(" + action.response.responseText + ")");
                        // sql失败
                        if (o.msg == Constants.SQL_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            return;
                        }
                        // 排他处理
                        if (o.msg == Constants.DATA_USING) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                            return;
                            // 删除成功
                        } else {
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005, function() {
                                formPanel.getForm().reset();
                                btnDel.setDisabled(true);
                                cmbVacationType.setValue(null);
                                cmbVacationType.clearInvalid();
                                cmbVacationType.setDisabled(true);
                                txtStartTime.setDisabled(true);
                                txtEndTime.setDisabled(true);
                                originalData = formPanel.getForm().getValues();
                                newFlag = true;
                                tabPanel.setActiveTab("searchPanel");
                            });
                            dsVacation.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE,
                                    startTime : vacationStart,
                                    endTime : vacationEnd
                                }
                            });
                        }
                    },
                    faliue : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    }
                });
            }
        })
    }
    /**
     * 上报操作
     */
    function reportHandler() {
        // TODO
    	alert('上报未处理')
    }

    /**
     * 重新load保存后的数据
     */
    function setSaveData(value) {
        // 人员id
        hdnEmpId.setValue(value.empId);
        var startValue = value.startTime;
        var endValue = value.endTime;
        startValue = fromDateRender(startValue);
        endValue = fromDateRender(endValue);
        // 设置考勤部门名称
        setAttendDept(value.empId);
        // 开始时间
        txtStartTime.setValue(startValue.substring(0, startValue.length - 3));
        // 结束时间
        txtEndTime.setValue(endValue.substring(0, endValue.length - 3));
        startOldDateArray.push(txtStartTime.getValue());
        endOldDateArray.push(txtEndTime.getValue());
        // 部门id
        hdnDeptId.setValue(value.deptId);
        // 原因
        txaReason.setValue(value.reason);
        // 备注
        txaMemo.setValue(value.memo);
        // 请假天数
        txtVacationDays.setValue(value.vacationDays);
        hdnVacationDays.setValue(value.vacationDays);
        // 请假时长
        txtVacationHours.setValue(value.vacationTime);
        hdnVacationHours.setValue(value.vacationTime);
        // 去向
        txtGo.setValue(value.whither);
        // 请假类别
        cmbVacationType.setValue(value.vacationTypeId);
        // 上次修改时间
        var lastModifyDate = fromDateRender(value.lastModifiyDate);
        hdnLastDate.setValue(lastModifyDate);
        // 请假id
        hdnVacationId.setValue(value.vacationid);
        // 假别id
        hdnVacationTypeId.setValue(value.vacationTypeId);
    }

    /**
     * 设置考勤部门名称
     */
    function setAttendDept(value) {
        Ext.Ajax.request({
            url : 'ca/getAttendanceDept.action',
            method : Constants.POST,
            params : {
                empId : value
            },
            success : function(action) {
                var result = eval("(" + action.responseText + ")");
                if (result.list) {
                    if (result.list[0]) {
                        txtAttendDeptName.setValue(result.list[0].attendanceDeptName);
                        hdnAttendDeptId.setValue(result.list[0].attendanceDeptId);
                    }
                } else {
                    if (result.msg == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    } else if (result.msg == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        })
    }

    /**
     * 设置值
     */
    function setValue() {
        var record = gridVacation.selModel.getSelected();
        // 设置登记tab的值
        if (record) {
            // 人员姓名
            txtEmpName.setValue(record.get("empName"));
            // 人员id
            hdnEmpId.setValue(record.get("empId"));
            // 部门名称
            txtTreeDeptName.setValue(record.get("deptName"));
            // 开始时间
            txtStartTime.setValue(record.get("startTime"));
            startOldDate = txtStartTime.getValue();
            // 结束时间
            txtEndTime.setValue(record.get("endTime"));
            endOldDate = txtEndTime.getValue();
            startOldDateArray.push(txtStartTime.getValue());
            endOldDateArray.push(txtEndTime.getValue());
            // 部门id
            hdnDeptId.setValue(record.get("deptId"));
            // 原因
            txaReason.setValue(record.get("reason"));
            // 备注
            txaMemo.setValue(record.get("memo"));
            // 请假天数
            txtVacationDays.setValue(record.get("vacationDays"));
            hdnVacationDays.setValue(record.get("vacationDays"));
            // 请假时长
            txtVacationHours.setValue(record.get("vacationHours"));
            hdnVacationHours.setValue(record.get("vacationHours"));
            // 去向
            txtGo.setValue(record.get("whither"));
            // 请假类别
            cmbVacationType.setValue(record.get("vacationTypeId"));
            // 上次修改时间
            hdnLastDate.setValue(record.get("lastModifyDate"));
            // 请假id
            hdnVacationId.setValue(record.get("vacationId"));
            // 假别id
            hdnVacationTypeId.setValue(record.get("vacationTypeId"));
            ifWeekend = record.get("ifWeekend");
            // button可用
            btnNew.setDisabled(false);
            btnDel.setDisabled(false);
            btnSave.setDisabled(false);
            btnReport.setDisabled(false);
            // 设置控件可用
            if (txtEmpName.getValue() != null) {
                cmbVacationType.setDisabled(false);
                txtStartTime.setDisabled(false);
                txtEndTime.setDisabled(false);
            }
            // 设置考勤部门名称
            Ext.Ajax.request({
                url : 'ca/getAttendanceDept.action',
                method : Constants.POST,
                params : {
                    empId : record.get("empId")
                },
                success : function(action) {
                    var result = eval("(" + action.responseText + ")");
                    if (result.list) {
                        if (result.list[0]) {
                            txtAttendDeptName.setValue(result.list[0].attendanceDeptName);
                            hdnAttendDeptId.setValue(result.list[0].attendanceDeptId);
                            originalData = formPanel.getForm().getValues();
                        }
                    } else {
                        if (result.msg == Constants.SQL_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        } else if (result.msg == Constants.IO_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                        }
                    }
                }
            })
            // 已退回或已上报
            if (record.get("signState") == Constants.ALREADY_OVER
            || record.get("signState") == Constants.ALREADY_REPORT) {
                setAllDisabled();
            } else {
                txtGo.setDisabled(false);
                txaMemo.setDisabled(false);
                txaReason.setDisabled(false);
                txtEmpName.setDisabled(false);
            }
        }
        originalData = formPanel.getForm().getValues();
    }
    /**
     * 设置所有控件不可用
     */
    function setAllDisabled() {
        txtEmpName.setDisabled(true);
        txtStartTime.setDisabled(true);
        txtEndTime.setDisabled(true);
        cmbVacationType.setDisabled(true);
        txtGo.setDisabled(true);
        txaMemo.setDisabled(true);
        txaReason.setDisabled(true);
        btnSave.setDisabled(true);
        btnDel.setDisabled(true);
        btnReport.setDisabled(true);
        cmbVacationType.clearInvalid();
    }
    /**
     * 输入必须项check
     */
    function inputCheck() {
        // 编码为空标志
        var flagCode = false;
        var msg = "";
        // 员工姓名
        if (!txtEmpName.getValue()) {
            msg += String.format(Constants.COM_E_003, "员工姓名");
            flagCode = true;
        }
        // 请假类别
        if (cmbVacationType.getValue() != "0") {
            if (!cmbVacationType.getValue()) {
                if (msg != '') {
                    msg += "<br>";
                }
                msg += String.format(Constants.COM_E_003, "请假类别");
                flagCode = true;
            }
        }
        // 开始时间
        if (!txtStartTime.getValue()) {
            if (msg != '') {
                msg += "<br>";
            }
            msg += String.format(Constants.COM_E_003, "开始时间");
            flagCode = true;
        }
        // 结束时间
        if (!txtEndTime.getValue()) {
            if (msg != '') {
                msg += "<br>";
            }
            msg += String.format(Constants.COM_E_003, "结束时间");
            flagCode = true;
        }

        // 输入必要数据提示信息
        if (flagCode) {
            Ext.Msg.alert(Constants.ERROR, msg);
            return false;
        }

        return true;
    }

    /**
     * 判断表单是否改变
     */
    function isFormChange() {
        // 获取现在的表单值
        var objForm = formPanel.getForm().getValues();
        // 循环判断
        for (var prop in originalData) {
            if (objForm[prop] != originalData[prop]) {
                return true;
            }
        }

        var flag = false
        // 新增时判断是否有值输入
        if (originalData == null) {
            formPanel.getForm().items.each(function(f) {
                var data = f.getValue();
                if (data) {
                    flag = true;
                }
            })
        }
        return flag;
    }
    /**
     * 双击grid操作
     */
    function doubleMouseHandler() {
        // 调到登记tab
        tabPanel.setActiveTab("registerPanel");
        newFlag = false;

        // 画面修改check
        if (isFormChange()) {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_004, function(obj) {
                if (obj == "yes") {
                    setValue();
                } else {
                    return;
                }
            });
        } else {
            setValue();
        }

    }
    /**
     * 新增为空时的操作
     */
    function newNullHandler() {
        // 设置控件不可用
        // 请假类别
        cmbVacationType.setDisabled(true);
        // 开始时间
        txtStartTime.setDisabled(true);
        // 结束时间
        txtEndTime.setDisabled(true);
        oldObject = formPanel.getForm().getValues();
        // 设置值为空
        formPanel.getForm().reset();
        // 请假类别
        cmbVacationType.setValue(null);
        cmbVacationType.clearInvalid();
        // 原因
        txaReason.setValue(oldObject["empVacationInfo.reason"]);
        // 去向
        txtGo.setValue(oldObject["empVacationInfo.whither"]);
        // 备注
        txaMemo.setValue(oldObject["empVacationInfo.memo"]);
    }

    /**
     * 修改为空的操作
     */
    function modifyNullHandler() {
        // 设置控件不可用
        // 请假类别
        cmbVacationType.setDisabled(true);
        // 开始时间
        txtStartTime.setDisabled(true);
        // 结束时间
        txtEndTime.setDisabled(true);
        // 设置值为空
        // 部门名称
        txtTreeDeptName.setValue("");
        // 考勤部门
        txtAttendDeptName.setValue("");
        txtEmpName.setValue("");
        // 请假天数
        txtVacationDays.setValue("");
        // 请假时长
        txtVacationHours.setValue("");
    }
    /**
     * 设置控件可用
     */
    function controlUseHandler() {
        // 请假类别
        cmbVacationType.setDisabled(false);
        // 开始时间
        txtStartTime.setDisabled(false);
        // 结束时间
        txtEndTime.setDisabled(false);
    }
    /**
     * 计算时长处理
     */
    function calculateHandler() {
        if (cmbVacationType.getValue() != "0") {
            if (!txtStartTime.getValue() || !txtEndTime.getValue() || !cmbVacationType.getValue()
            || !txtEmpName.getValue()) {
                return;
            }
        }
        hdnVacationTypeId.setValue(cmbVacationType.getValue());
        Ext.Ajax.request({
            url : 'ca/calculate.action',
            method : Constants.POST,
            params : {
                empId : hdnEmpId.getValue(),
                startTime : txtStartTime.getValue(),
                endTime : txtEndTime.getValue(),
                ifWeekend : ifWeekend
            },
            success : function(action) {
                var o = eval("(" + action.responseText + ")");
                if (o.msg == Constants.SQL_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                } else if (o.msg == Constants.IO_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    return;
                } else if (o.list) {
                    if (o.list[0].attendanceDeptName) {
                        Ext.Msg.alert(Constants.ERROR,
                        String.format(Constants.COM_I_029, o.list[0].attendanceDeptName), function() {
                            if (newFlag) {
                                formPanel.getForm().reset();
                                cmbVacationType.setValue(null);
                                btnDel.setDisabled(true);
                                cmbVacationType.clearInvalid();
                                cmbVacationType.setDisabled(true);
                                btnReport.setDisabled(true);
                                txtStartTime.setDisabled(true);
                                txtEndTime.setDisabled(true);
                            } else {
                                var startLength = startOldDateArray.length;
                                var endLength = endOldDateArray.length;
                                if (startLength > 1) {
                                    txtStartTime.setValue(startOldDateArray[startLength - 2]);
                                } else if (startLength == 1) {
                                    txtStartTime.setValue(startOldDateArray[0]);
                                }
                                if (endLength > 1) {
                                    txtEndTime.setValue(endOldDateArray[endLength - 2]);
                                } else if (endLength == 1) {
                                    txtEndTime.setValue(endOldDateArray[0]);
                                }
                                startOldDateArray.push(txtStartTime.getValue());
                                endOldDateArray.push(txtEndTime.getValue());
                                calculateHandler();
                            }
                            return;
                        });
                    }
                }
                txtVacationDays.setValue(o.vacationDays);
                hdnVacationDays.setValue(o.vacationDays);
                txtVacationHours.setValue(o.vacationHours);
                hdnVacationHours.setValue(o.vacationHours);
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
            }
        })
    }
    /**
     * 开始，结束时间的变更处理
     */
    function timeChangeHandler() {
        // 编码为空标志
        var flagCode = false;
        var msg = "";
        var retSFlag = false;
        var retEFlag = false;
        if (startOldDate != txtStartTime.getValue() || endOldDate != txtEndTime.getValue()) {
            if (!txtStartTime.getValue()) {
                msg += String.format(Constants.COM_E_003, "开始时间");
                flagCode = true;
                retSFlag = true;
            }
            if (!txtEndTime.getValue()) {
                if (msg != '') {
                    msg += "<br>";
                }
                msg += String.format(Constants.COM_E_003, "结束时间");
                flagCode = true;
                retEFlag = true;
            }
        }
        // 输入必要数据提示信息
        if (flagCode) {
            Ext.Msg.alert(Constants.ERROR, msg, function() {
                returnTime(retSFlag, retEFlag);
                // 清空请假时长和天数
                txtVacationDays.setValue("");
                txtVacationHours.setValue("");
                hdnVacationDays.setValue("");
                hdnVacationHours.setValue("");
            });
            return;
        }
        // 开始时间不能大于结束时间的场合check
        if (txtStartTime.getValue() <= txtEndTime.getValue()) {
            // 计算时长
            calculateHandler();
            startOldDate = txtStartTime.getValue();
            endOldDate = txtEndTime.getValue();
            startOldDateArray.push(txtStartTime.getValue());
            endOldDateArray.push(txtEndTime.getValue());
        } else {
            // 清空请假时长和天数
            txtVacationDays.setValue("");
            txtVacationHours.setValue("");
            hdnVacationDays.setValue("");
            hdnVacationHours.setValue("");
        }
    }
    /**
     * 返回上次的值
     */
    function returnTime(argStartFlag, argEndFlag) {
        var startLength = startOldDateArray.length;
        var endLength = endOldDateArray.length;
        if (argStartFlag) {
            if (startLength > 1) {
                txtStartTime.setValue(startOldDateArray[startLength - 1]);
            } else if (startLength == 1) {
                txtStartTime.setValue(startOldDateArray[0]);
            }
        }

        if (argEndFlag) {
            if (endLength > 1) {
                txtEndTime.setValue(endOldDateArray[endLength - 1]);
            } else if (endLength == 1) {
                txtEndTime.setValue(endOldDateArray[0]);
            }
        }
    }
    /**
     * 渲染请假天数
     */
    function divide(v) {
        if (v) {
            v = Number(v).toFixed(2);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        }
        if (v == 0) {
            return 0;
        } else
            return '';
    }
    /**
     * 渲染请假时长
     */
    function divideHour(v) {
        if (v) {
            v = Number(v).toFixed(1);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        }
        if (v == 0) {
            return 0;
        } else
            return '';
    }
    /**
     * 调整审批状态
     */
    function renderStates(value) {
        if (value == 0) {
            return Constants.NOT_REPORT_MESSAGE;
        }
        if (value == 1) {
            return Constants.ALREADY_REPORT_MESSAGE;
        }
        if (value == 2) {
            return Constants.ALREADY_OVER_MESSAGE;
        }
        if (value == 3) {
            return Constants.ALREADY_RETURN_MESSAGE;
        }
    }

    /**
     * 渲染日期
     */
    function fromDateRender(value) {
        if (!value)
            return '';
        if (value instanceof Date)
            return renderDate(value);
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate)
            return "";
        return strTime ? strDate + " " + strTime : strDate;
    }
})