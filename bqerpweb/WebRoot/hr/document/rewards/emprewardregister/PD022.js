// 员工奖惩登记
// author:zhaozhijie
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();

    var dteReward = new Date();
    dteReward = dteReward.format('Y-m');

    // 奖惩月份
    var txtRewardMonth = new Ext.form.TextField({
        id : 'startDate',
        name : 'startDate',
        width : 100,
        style : 'cursor:pointer',
        value : dteReward,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : true,
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM'
                });
                this.blur();
            }
        }
    })
    // 奖惩数据形式
    var rewardData = Ext.data.Record.create([
        // 员工姓名
        {
        name : 'empName'
    },
        // 员工ID
        {
            name : 'empID'
        },
        // 员工工号
        {
            name : 'empCode'
        },
        // 所属部门
        {
            name : 'deptName'
        },
        // 奖惩类别名称
        {
            name : 'rewardsPunish'
        },
        // 奖惩类别
        {
            name : 'rewardsPunishType'
        },
        // 奖惩日期
        {
            name : 'rewardsDate'
        },
        // 奖惩原因
        {
            name : 'rewardsReason'
        },
        // 备注
        {
            name : 'memo'
        },
        // 职工奖惩ID
        {
            name : 'rewardsPunishID'
        },
        // 修改时间
        {
            name : 'lastModifyDate'
        },
        // 判断新增，更新，删除
        {
            name : 'flag'
        }])
    // 奖惩store
    var dsReward = new Ext.data.JsonStore({
        url : 'hr/getEmpRewardInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : rewardData,
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
    dsReward.on("beforeload", function() {
        Ext.apply(this.baseParams, {
            rewardMonth : txtRewardMonth.getValue()
        });
    })
    dsReward.setDefaultSort('empCode', 'ASC');
    dsReward.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE,
            rewardMonth : dteReward
        }
    })

    // 查询button
    var btnQuery = new Ext.Button({
        iconCls : Constants.CLS_QUERY,
        text : Constants.BTN_QUERY,
        handler : queryHandler
    })
    // 新增button
    var btnAdd = new Ext.Button({
        iconCls : Constants.CLS_ADD,
        text : Constants.BTN_ADD,
        handler : addHandler
    })
    // 修改button
    var btnModify = new Ext.Button({
        iconCls : Constants.CLS_UPDATE,
        text : Constants.BTN_UPDATE,
        handler : modifyHandler
    })
    // 删除button
    var btnDelete = new Ext.Button({
        iconCls : Constants.CLS_DELETE,
        text : Constants.BTN_DELETE,
        handler : deleteHandler
    })
    // 工具栏
    var tbarReward = new Ext.Toolbar({
        height : 25,
        items : ["奖惩月份:", txtRewardMonth, '-', btnQuery, btnAdd, btnModify, btnDelete]
    })
    // 奖惩grid
    var gridReward = new Ext.grid.EditorGridPanel({
        layout : 'fit',
        autoWidth : true,
        autoScroll : true,
        enableColumnMove : false,
        clicksToEdit : 1,
        store : dsReward,
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : false
        }),
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                align : 'right',
                width : 35
            }),
            // 员工姓名
            {
                header : "员工姓名",
                width : 75,
                align : 'left',
                sortable : true,
                dataIndex : 'empName'
            },
            // 员工工号
            {
                header : "员工工号",
                width : 75,
                align : 'left',
                sortable : true,
                dataIndex : 'empCode'
            },
            // 所属部门
            {
                header : "所属部门",
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'deptName'
            },
            // 奖惩类别名称
            {
                header : "奖惩类别名称",
                width : 120,
                align : 'left',
                sortable : true,
                dataIndex : 'rewardsPunish'
            },
            // 奖惩类别
            {
                header : "奖惩类别",
                width : 75,
                align : 'left',
                sortable : true,
                dataIndex : 'rewardsPunishType'
            },
            // 奖惩日期
            {
                header : "奖惩日期",
                width : 75,
                align : 'left',
                sortable : true,
                dataIndex : 'rewardsDate'
            },
            // 奖惩原因
            {
                header : "奖惩原因",
                width : 75,
                align : 'left',
                sortable : true,
                dataIndex : 'rewardsReason'
            },
            // 备注
            {
                header : "备注",
                width : 110,
                align : 'left',
                sortable : true,
                dataIndex : 'memo'
            }],
        tbar : tbarReward,
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : dsReward,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        autoSizeColumns : true
    })
    gridReward.on("rowdblclick", modifyHandler);
    // 显示区域
    var view = new Ext.Viewport({
        layout : 'fit',
        autoScroll : true,
        items : [gridReward]
    })
    // ============= 定义弹出画面 ===============
    // 员工姓名
    var txtEmpName = new Ext.form.TextField({
        fieldLabel : "员工姓名<font color='red'>*</font>",
        width : 180,
        id : "empReward.empName",
        displayField : 'text',
        valueField : 'id',
//        allowBlank : false,
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
                    txtEmpCode.setValue(winEmp.workerCode);
                    txtDept.setValue(winEmp.deptName);
                    hdnEmpId.setValue(winEmp.empId);
                }
            }
        }
    });
    // 员工id
    var hdnEmpId = new Ext.form.Hidden({
        id : 'empReward.empID'
    })
    // 职工奖惩id
    var hdnRewardPunishId = new Ext.form.Hidden({
        id : 'empReward.rewardsPunishID '
    })
    // 上次修改时间
    var hdnLastModifyDate = new Ext.form.Hidden({
        id : 'empReward.lastModifyDate'
    })
    // 员工工号
    var txtEmpCode = new Ext.form.TextField({
        id : 'empReward.empCode',
        name : 'empReward.empCode',
        fieldLabel : "员工工号",
        width : 180,
        disabled : true
//        allowBlank : false
    });
    // 所属部门
    var txtDept = new Ext.form.TextField({
        id : 'empReward.deptName',
        name : 'empReward.deptName',
        fieldLabel : "所属部门",
        width : 180,
        disabled : true
//        allowBlank : false
    })
    // 奖惩类别名称Store
    var dsRewardTypeName = new Ext.data.JsonStore({
        url : 'hr/getEmpRewardsMainInfo.action',
        root : 'list',
        fields : ['rewardsPunishId', 'rewardsPunish', 'rewardsPunishType'],
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
    dsRewardTypeName.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    })
    // 奖惩类别名称
    var cmbRewardTypeName = new Ext.form.ComboBox({
        width : 180,
//        allowBlank : false,
        forceSelection : true,
        fieldLabel : "奖惩类别名称<font color='red'>*</font>",
        triggerAction : 'all',
        mode : 'local',
        displayField : 'rewardsPunish',
        valueField : 'rewardsPunishId',
        store : dsRewardTypeName,
        readOnly : true
    })
    cmbRewardTypeName.on("select", function(t, record, index) {
        // 奖励条件下
        if (record.get("rewardsPunishType") == "1") {
            txtRewardType.setValue("奖励");
            // 处罚条件下
        } else if (record.get("rewardsPunishType") == "2") {
            txtRewardType.setValue("处罚");
        } else {
            txtRewardType.setValue("");
        }
        hdnRewardTypeName.setValue(cmbRewardTypeName.getValue());
    });
    // 奖惩类别名称隐藏域
    var hdnRewardTypeName = new Ext.form.Hidden({
        id : 'empReward.rewardsPunishId'
    })
    // 奖惩类别
    var txtRewardType = new Ext.form.TextField({
        id : 'empReward.rewardsPunishType',
        name : 'empReward.rewardsPunishType',
        fieldLabel : "奖惩类别",
        disabled : true,
        width : 180
    })
    // 奖惩日期
    var txtRewardDate = new Ext.form.TextField({
        id : 'empReward.rewardsDate',
        name : 'empReward.rewardsDate',
        width : 180,
        fieldLabel : "奖惩日期<font color='red'>*</font>",
        style : 'cursor:pointer',
//        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    isShowClear : false,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    })
    // 奖惩原因
    var txaRewardReason = new Ext.form.TextArea({
        id : 'empReward.rewardsReason',
        name : 'empReward.rewardsReason',
        fieldLabel : "奖惩原因",
        width : 177,
        maxLength : 250
    })
    // 备注
    var txaMemo = new Ext.form.TextArea({
        id : 'empReward.memo',
        name : 'empReward.memo',
        fieldLabel : "备注",
        width : 177,
        maxLength : 128
    })
    // 判断新增，更新，删除
    var hdnFlag = new Ext.form.Hidden({
        id : 'empReward.flag'
    })
    // 添加panel
    var addPanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        items : [txtEmpName, txtEmpCode, txtDept, cmbRewardTypeName, txtRewardType, txtRewardDate, txaRewardReason,
            txaMemo, hdnFlag, hdnEmpId, hdnRewardPunishId, hdnLastModifyDate, hdnRewardTypeName]
    })
    // 窗口
    var win = new Ext.Window({
        width : 350,
        height : 350,
        buttonAlign : "center",
        items : [addPanel],
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : saveHandler
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : cancelHandler
            }]
    });

    /**
     * 查询操作
     */
    function queryHandler() {
        dsReward.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE,
                rewardMonth : txtRewardMonth.getValue()
            }
        })
        // 没有检索到数据时
        dsReward.on("load", function() {
            if (dsReward.getTotalCount() == 0) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            }
        })
    }

    /**
     * 新增操作
     */
    function addHandler() {
        addPanel.getForm().reset();
        win.x = undefined;
        win.y = undefined;
        win.show();
        txtEmpName.setDisabled(false);
        // 判断新增，更新
        hdnFlag.setValue("0");
        win.setTitle("新增员工奖惩");
    }

    /**
     * 修改操作
     */
    function modifyHandler() {
        if (!gridReward.selModel.hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        } else {
            var record = gridReward.selModel.getSelected();
            win.x = undefined;
            win.y = undefined;
            win.show();
            // 员工姓名
            txtEmpName.setValue(record.get("empName"));
            txtEmpName.setDisabled(true);
            // 员工工号
            txtEmpCode.setValue(record.get("empCode"));
            // 所属部门
            txtDept.setValue(record.get("deptName"));
            // 员工id
            hdnEmpId.setValue(record.get("empID"));
            // 上次修改时间
            hdnLastModifyDate.setValue(record.get("lastModifyDate"));
            // 职工奖惩id
            hdnRewardPunishId.setValue(record.get("rewardsPunishID"));
            // 奖惩类别名称
            cmbRewardTypeName.setValue(record.get("rewardsPunish"));
            hdnRewardTypeName.setValue(record.get("rewardsPunishId"))
            // 奖惩类别
            txtRewardType.setValue(record.get("rewardsPunishType"));
            // 奖惩日期
            txtRewardDate.setValue(record.get("rewardsDate"));
            // 备注
            txaMemo.setValue(record.get("memo"));
            // 判断新增，更新
            hdnFlag.setValue("1");
            // 奖惩原因
            txaRewardReason.setValue(record.get("rewardsReason"));
            win.setTitle("修改员工奖惩");
        }
    }

    /**
     * 删除操作
     */
    function deleteHandler() {
        if (!gridReward.selModel.hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        } else {
            // 确认删除信息
            Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_002, function(button, text) {
                if (button == "yes") {
                    var record = gridReward.selModel.getSelected();
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : "hr/saveEmpRewards.action",
                        params : {
                            id : record.get("rewardsPunishID"),
                            flag : "1"
                        },
                        success : function(result, request) {
                            var o = eval('(' + result.responseText + ')');
                            // 排他处理
                            if (o.msg == Constants.DATA_USING) {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_002);
                                return;
                            }
                            // sql失败
                            if (o.msg == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                                // 删除成功message
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                                dsReward.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE,
                                        rewardMonth : txtRewardMonth.getValue()
                                    }
                                });
                            }
                        },
                        faliue : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        }
                    })
                }
            })
        }
    }

    /**
     * 保存操作
     */
    function saveHandler() {
        // 编码为空标志
        var flagCode = false;
        var msg = "";
        // 员工姓名为空check
        if (txtEmpName.getValue() == null || txtEmpName.getValue() == "") {
            msg = String.format(Constants.COM_E_003, "员工姓名");
            flagCode = true;
        }

        // 奖惩类别名称为空check
        if (cmbRewardTypeName.getValue() == null || cmbRewardTypeName.getValue() == "") {
            msg += "<br>" + String.format(Constants.COM_E_003, "奖惩类别名称");
            flagCode = true;
        }

        // 奖惩日期为空check
        if (txtRewardDate.getValue() == null || txtRewardDate.getValue() == "") {
            msg += "<br>" + String.format(Constants.COM_E_003, "奖惩日期");
            flagCode = true;
        }

        if (flagCode) {
            Ext.Msg.alert(Constants.ERROR, msg);
            return;
        } else {
            // 确认保存信息
            Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_001, function(button, text) {
                if (button == "yes") {
                    addPanel.getForm().submit({
                        method : Constants.POST,
                        url : "hr/saveEmpRewards.action",
                        success : function(form, action) {
                            var o = eval("(" + action.response.responseText + ")");
                            // 排他处理
                            if (o.msg == Constants.DATA_USING) {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_002);
                                return;
                            }
                            // sql失败
                            if (o.msg == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                                // 保存成功
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    win.hide();
                                });
                                dsReward.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE,
                                        rewardMonth : txtRewardMonth.getValue()
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
    }
    /**
     * 取消操作
     */
    function cancelHandler() {
        // 确认取消信息
        Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
            if (button == "yes") {
                win.hide();
            }
        })
    }
})