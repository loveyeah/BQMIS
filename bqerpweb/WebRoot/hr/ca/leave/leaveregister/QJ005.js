Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();

    var monday = "星期一";
    var tuesday = "星期二";
    var wednesday = "星期三";
    var thursday = "星期四";
    var friday = "星期五";
    var saturday = "星期六";
    var sunday = "星期日";
    // 判断是初期化还是点击查询按钮后
    var flag = '0';

    // 选择模式
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // 员工姓名
    var txtEmpName = new Ext.form.TextField({
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
                    txtEmpName.setValue(winEmp.workerName);
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
    var txtDeptName = new Ext.form.TextField({
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
                        txtDeptName.setValue(objDeptName.names);
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

    // 查询button
    var btnSearch = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : function() {
            flag = '1';
            search(true);
        }
    });

    // 确认button
    var btnConfirm = new Ext.Button({
        text : Constants.CONFIRM,
        iconCls : Constants.CLS_OK,
        handler : confirm
    });

    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : ['&nbsp所属部门:', txtDeptName, '-', btnSearch, btnConfirm]
    });

    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '员工姓名',
            sortable : true,
            dataIndex : 'empName'
        }, {
            header : '请假开始时间',
            sortable : true,
            dataIndex : 'startTime'
        }]);

    // 数据解析
    var recordMain = new Ext.data.Record.create([{
            name : 'empId'
        }, {
            name : 'empName'
        }, {
            name : 'deptId'
        }, {
            name : 'deptName'
        }, {
            name : 'leaveTypeId'
        }, {
            name : 'leaveType'
        }, {
            name : 'startTime'
        }, {
            name : 'endTime'
        }, {
            name : 'leaveDays'
        }, {
            name : 'leaveTime'
        }, {
            name : 'reason'
        }, {
            name : 'goWhere'
        }, {
            name : 'memo'
        }, {
            name : 'vacationId'
        }, {
            name : 'lastModifyTime'
        }, {
            name : 'ifWeekend'
        }])

    // store
    var leaveInfoStore = new Ext.data.JsonStore({
        url : 'ca/getEmpLeave.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : recordMain
    });

    leaveInfoStore.on('beforeload', function() {
        this.baseParams = {
            empId : hdnEmp.getValue(),
            deptId : hdnDept.getValue()
        };
    })

    // 员工请假信息grid
    var leaveInfoGrid = new Ext.grid.GridPanel({
        sm : sm,
        layout : 'fit',
        region : 'center',
        border : false,
        cm : colms,
        store : leaveInfoStore,
        tbar : headTbar,
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : leaveInfoStore
        }),
        enableColumnMove : false
    });

    // grid双击事件
    leaveInfoGrid.on("rowdblclick", confirm);

    leaveInfoStore.on("load", function() {
        if (leaveInfoStore.getCount() == 0)
            if (flag != '0')
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
        flag = '0';
    });

    var headerTbar = new Ext.Toolbar({
        border : false,
        height : 20,
        region : 'north',
        items : ['员工姓名:', txtEmpName]
    });

    // 左边的panel
    var leftPanel = new Ext.Panel({
        style : "border-rigth:1px solid;",
        region : 'west',
        split : false,
        layout : 'border',
        width : 260,
        items : [headerTbar, leaveInfoGrid]
    });

    // 销假时间
    var txtOverTime = new Ext.form.TextField({
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    // 时间格式
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd HH:mm',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }
        }
    });

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

    // 销假button
    var btnClearLeave = new Ext.Button({
        text : '销假',
        disabled : true,
        iconCls : Constants.CLS_RESUME_LEAVE,
        handler : clearLeave
    });

    // 员工id
    var hdnEmpId = new Ext.form.Hidden({
        name : 'empId'
    })

    // 请假ID
    var hdnVacationID = new Ext.form.Hidden({
        name : 'vacationId'
    });
    // 上次修改时间
    var hdnModifyTime = new Ext.form.Hidden({
        name : 'lastModifyTime'
    });

    // 员工姓名
    var txtemployeeName = new Ext.form.TextField({
        name : 'empName',
        disabled : true,
        fieldLabel : '员工姓名'
    });
    // 所属部门
    var txtDepartName = new Ext.form.TextField({
        name : 'deptName',
        disabled : true,
        fieldLabel : '所属部门'
    });
    // 请假类别
    var txtLeaveType = new Ext.form.TextField({
        name : 'leaveType',
        disabled : true,
        fieldLabel : '请假类别'
    });
    // 开始时间
    var txtStartTime = new Ext.form.TextField({
        name : 'startTime',
        disabled : true,
        fieldLabel : '开始时间'
    });

    // 开始星期
    var txtStartWeek = new Ext.form.TextField({
        name : 'startWeek',
        width : '45',
        labelSeparator : '',
        disabled : true
    });

    // 结束星期
    var txtEndWeek = new Ext.form.TextField({
        name : 'endWeek',
        width : '45',
        labelSeparator : '',
        disabled : true
    });
    // 结束时间
    var txtEndTime = new Ext.form.TextField({
        name : 'endTime',
        disabled : true,
        fieldLabel : '结束时间'
    });

    var lblVacationDays = new Ext.form.Label({
        text : "天"
    });
    // 请假天数
    var txtLeaveDays = new Powererp.form.NumField({
        name : 'leaveDays',
        disabled : true,
        anchor : '99%',
        style : 'text-align:right',
        fieldLabel : '请假天数',
        padding : 2
    });
    var lblVacationHours = new Ext.form.Label({
        text : "小时"
    });
    // 请假时长
    var txtLeaveLong = new Powererp.form.NumField({
        name : 'leaveTime',
        disabled : true,
        anchor : '99%',
        style : 'text-align:right',
        fieldLabel : '请假时长',
        padding : 1
    });
    // 原因
    var txaReason = new Ext.form.TextArea({
        name : 'reason',
        height : '45',
        disabled : true,
        anchor : '95%',
        fieldLabel : '原因'
    });
    // 去向
    var txtGoWhere = new Ext.form.TextField({
        name : 'goWhere',
        disabled : true,
        anchor : '95.5%',
        fieldLabel : '去向'
    });
    // 备注
    var txaMemo = new Ext.form.TextArea({
        name : 'memo',
        disabled : true,
        height : '45',
        anchor : '95%',
        fieldLabel : '备注'
    });
    // 是否周末
    var hdnIfWeekend = new Ext.form.Hidden({
        name : 'ifWeekend'
    })

    var headForm = new Ext.form.FieldSet({
        border : true,
        labelAlign : 'right',
        layout : 'form',
        labelWidth : 70,
        width : 300,
        height : 440,
        items : [{
                layout : "column",
                style : "padding-top:20px",
                border : false,
                items : [{
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtemployeeName]
                    }, {
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtDepartName]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtLeaveType]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.45,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtStartTime]
                    }, {
                        columnWidth : 0.5,
                        style : 'margin-top:-1px',
                        border : false,
                        height : 30,
                        items : [txtStartWeek]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.45,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtEndTime]
                    }, {
                        columnWidth : 0.5,
                        border : false,
                        style : 'margin-top:-1px',
                        height : 30,
                        items : [txtEndWeek]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.42,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtLeaveDays]
                    }, {
                        columnWidth : 0.085,
                        border : false,
                        layout : 'form',
                        style : 'padding-top:5;font-size:12;',
                        items : [lblVacationDays]
                    }, {
                        columnWidth : 0.4,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtLeaveLong]
                    }, {
                        columnWidth : 0.1,
                        border : false,
                        layout : 'form',
                        style : 'padding-top:5;font-size:12;',
                        items : [lblVacationHours]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        height : 54,
                        labelAlign : "right",
                        items : [txaReason]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtGoWhere]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        height : 54,
                        labelAlign : "right",
                        items : [txaMemo]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [hdnVacationID]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [hdnModifyTime]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [hdnEmpId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 1,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [hdnIfWeekend]
                    }]
            }]

    });
    // 员工请假信息panel
    var leaveInfoPanel = new Ext.form.FormPanel({
        border : false,
        layout : 'fit',
        width : 500,
        height : 460,
        style : "padding:10px",
        labelAlign : 'right',
        items : [headForm]
    });

    // 右边的panel
    var rightPanel = new Ext.Panel({
        region : "center",
        autoScroll : true,
        border : false,
        style : "border-left:none",
        items : [leaveInfoPanel],
        tbar : ['销假时间<font color="red">*</font>:', txtOverTime, '-', btnClearLeave]
    });

    /**
     * 查询按钮按下
     */
    function search(flag) {
        init();
        leaveInfoStore.removeAll();
        if (flag) {
            leaveInfoStore.sortInfo = null;
            leaveInfoGrid.getView().removeSortIcon();
        }
        leaveInfoStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
    }

    /**
     * 确认按钮按下
     */
    function confirm() {
        // 是否选择一行
        if (leaveInfoGrid.selModel.hasSelection()) {
            var record = leaveInfoGrid.getSelectionModel().getSelected();
            // form表单初始化
            leaveInfoPanel.getForm().loadRecord(record);
            txtOverTime.setValue(record.get('endTime'));
            var sw = Date.parseDate(record.get('startTime'), 'Y-m-d H:i');
            var ew = Date.parseDate(record.get('endTime'), 'Y-m-d H:i');
            switch (sw.getDay()) {
                case 1 : {
                    txtStartWeek.setValue(monday);
                    break;
                }
                case 2 : {
                    txtStartWeek.setValue(tuesday);
                    break;
                }
                case 3 : {
                    txtStartWeek.setValue(wednesday);
                    break;
                }
                case 4 : {
                    txtStartWeek.setValue(thursday);
                    break;
                }
                case 5 : {
                    txtStartWeek.setValue(friday);
                    break;
                }
                case 6 : {
                    txtStartWeek.setValue(saturday);
                    break;
                }
                case 0 : {
                    txtStartWeek.setValue(sunday);
                    break;
                }
            };
            switch (ew.getDay()) {
                case 1 : {
                    txtEndWeek.setValue(monday);
                    break;
                }
                case 2 : {
                    txtEndWeek.setValue(tuesday);
                    break;
                }
                case 3 : {
                    txtEndWeek.setValue(wednesday);
                    break;
                }
                case 4 : {
                    txtEndWeek.setValue(thursday);
                    break;
                }
                case 5 : {
                    txtEndWeek.setValue(friday);
                    break;
                }
                case 6 : {
                    txtEndWeek.setValue(saturday);
                    break;
                }
                case 0 : {
                    txtEndWeek.setValue(sunday);
                    break;
                }
            }
            btnClearLeave.setDisabled(false);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    /**
     * 判断销假时间是否属于节假日
     */
    function judgeClearTime() {
        var record = dsHoliday.getRange(0, dsHoliday.getCount());
        // 销假时间
        var clearDay = Date.parseDate(txtOverTime.getValue(), 'Y-m-d h:m');
        // 销假时间是节假日的check
        for (var i = 0; i < dsHoliday.getCount(); i++) {
            if (clearDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
            && record[i].data["holidayType"] != "2") {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.QJ003_E_002, "销假时间"));
                return true;
            }
        }
        // 销假时间是周末的check
        var clearDayWeek = clearDay.getDay();
        if (clearDayWeek == 6 || clearDayWeek == 0) {
            var flag = false;
            // 开始时间是周末上班时间的check
            for (var i = 0; i < dsHoliday.getCount(); i++) {
                if (clearDay.format('Y-m-d') == record[i].data["holidayDate"].substring(0, 10)
                && record[i].data["holidayType"] != "1") {
                    flag = true;
                }
            }
            if (!flag) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.QJ003_E_002, "销假时间"));
                return true;
            }
        }
        return false;
    }

    /**
     * 销假按钮按下
     */
    function clearLeave() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_013, function(obj) {
            if (obj == "yes") {
                if (judgeClearTime())
                    return;
                var ct = Date.parseDate(txtOverTime.getValue(), 'Y-m-d h:i');
                var st = Date.parseDate(txtStartTime.getValue(), 'Y-m-d h:i');
                var et = Date.parseDate(txtEndTime.getValue(), 'Y-m-d h:i');
                // 销假时间小于开始时间
                if (ct < st) {
                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_027, '销假时间', '开始时间'));
                    return;
                }
                // 销假时间大于结束时间
                if (ct > et) {
                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_026, '销假时间', '结束时间'));
                    return;
                }

                // 销假处理
                Ext.Ajax.request({
                    method : 'POST',
                    url : 'ca/clearLeave.action',
                    success : function(result, request) {
                        var o = result.responseText;
                        // 销假成功
                        if (o == "S") {
                            init();
                            leaveInfoStore.removeAll();
                            leaveInfoStore.sortInfo = null;
                            leaveInfoGrid.getView().removeSortIcon();
                            leaveInfoStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
                            return;
                        }
                        // 他人使用中
                        if (o == "U") {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                            return;
                        }
                        // 失败
                        if (o == "E") {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            return;
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    },
                    params : {
                        // 向后台传递参数
                        startTime : txtStartTime.getValue(),
                        clearTime : txtOverTime.getValue(),
                        endTime : txtEndTime.getValue(),
                        leaveDays : txtLeaveDays.getValue(),
                        leaveLong : txtLeaveLong.getValue(),
                        vacationId : hdnVacationID.getValue(),
                        lastModifyTime : hdnModifyTime.getValue(),
                        empId : hdnEmpId.getValue(),
                        ifWeekend : hdnIfWeekend.getValue()
                    }
                });
            }
        })
    }

    // 总的panel
    var fullPanel = new Ext.Panel({
        layout : 'border',
        border : false,
        split : true,
        autoScroll : true,
        items : [leftPanel, {
                region : 'center',
                layout : 'border',
                frame : false,
                border : true,
                items : [rightPanel]
            }]
    });

    /**
     * 初始化
     */
    function init() {
        txtOverTime.setValue("");
        leaveInfoPanel.getForm().reset();
        btnClearLeave.setDisabled(true);
    }
    search();

    // 显示区域
    var view = new Ext.Viewport({
        enableTabScroll : true,
        autoScroll : true,
        layout : "fit",
        items : [fullPanel]
    })
})
