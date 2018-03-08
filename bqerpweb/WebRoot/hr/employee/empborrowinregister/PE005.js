Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
    this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
    Ext.QuickTips.init();
    var FLAG_SUB_ADD = '0';
    var FLAG_SUB_MODIFY = '1';
    var flag;

    // 定义借调开始日期
    var startDate = new Ext.form.TextField({
        width : 90,
        // name : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });

    // 定义借调结束日期
    var endDate = new Ext.form.TextField({
        width : 90,
        // id : 'endDate',
        // name : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 单据状态
    var cmbStatus = new Ext.form.CmbHRCode({
        id : "cmbStatus",
        type : "单据状态",
        width : 70,
        value : "0",
        name : 'dcmStatus'
    });
    // 所属部门
    // 主页面显示Store
    var storeEmpMove = new Ext.data.JsonStore({
        fields : ['ssDeptId', 'ssDeptName', 'jdDeptId', 'jdDeptName']
    })
    storeEmpMove.insert(0, new Ext.data.Record({
        'ssDeptId' : '',
        'ssDeptName' : '',
        'jdDeptId' : '',
        'jdDeptName' : ''
    }))
    var txtSSDept = new Ext.form.ComboBox({
        fieldLabel : '所属部门',
        width : 110,
        hiddenName : 'mrDept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        store : storeEmpMove,
        displayField : "ssDeptName",
        valueField : "ssDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    txtSSDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeEmpMove.getAt(0).data['ssDeptId'] = dept.ids;
            storeEmpMove.getAt(0).data['ssDeptName'] = dept.names;
            txtSSDept.setValue(dept.ids);
        }
    });
    // 借调部门
    var txtJDDept = new Ext.form.ComboBox({
        fieldLabel : '借调部门',
        width : 110,
        hiddenName : 'mrDept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        store : storeEmpMove,
        displayField : "jdDeptName",
        valueField : "jdDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 借调部门的onClick事件
    txtJDDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeEmpMove.getAt(0).data['jdDeptId'] = dept.ids;
            storeEmpMove.getAt(0).data['jdDeptName'] = dept.names;
            txtJDDept.setValue(dept.ids);
        }
    })
    // 查询按钮
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryEmpMoveData
    });
    // 新增按钮
    var btnAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addEmpBorrowInData
    });

    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : modifyData
    });

    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delRecord
    });
    // 上报按钮
    var btnReport = new Ext.Button({
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        handler : reportData
    });

    // 批处理按钮
    var btnBatch = new Ext.Button({
        text : Constants.BTN_BAT,
        iconCls : Constants.CLS_BAT,
        handler : batchData
    });

    function batchData() {
        winBatch.x = undefined;
        winBatch.y = undefined;
        winBatch.show();
        storeBorrowIn.removeAll();
        storeBorrowOut.removeAll();
        panelBorrowIn.getForm().reset();
        panelBorrowOut.getForm().reset();
        // btnInDelete.setDisabled(true);
        btnInSubmit.setDisabled(true);
        btnOutSubmit.setDisabled(true);
        tabPanel.setActiveTab(tabBorrowIn);
    }
    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
            /** 员工借调ID */
            name : 'hrJEmployeeborrowInId'
        }, {
            /** 员工ID */
            name : 'empId'
        }, {
            /** 员工Code */
            name : 'empCode'
        }, {
            /** 中文名称 */
            name : 'chsName'
        }, {
            /** 所属部门ID */
            name : 'ssDeptId'
        }, {
            /** 所属部门名称 */
            name : 'ssDeptName'
        }, {
            /** 岗位ID */
            name : 'stationId'
        }, {
            /** 岗位名称 */
            name : 'stationName'
        }, {
            /** 借调部门ID */
            name : 'jdDeptId'
        }, {
            /** 借调部门名称 */
            name : 'jdDeptName'
        }, {
            /** 开始日期 */
            name : 'startDate'
        }, {
            /** 结束日期 */
            name : 'endDate'
        }, {
            /** 单据状态 */
            name : 'dcmStatus'
        }, {
            /** 备注 */
            name : 'memo'
        }, {
            /** 上次修改时间 */
            name : 'lastModifiedDate'
        }, {
            /** 是否已回 */
            name : 'ifBack'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/getEmpBorrowInRegisterInfo.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "startDate",
            direction : "ASC"
        }
    }, MyRecord);
    // 定义封装缓存数据的对象
    var queryStore = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ['借调日期:', startDate, '~', endDate, "-", '单据状态:', cmbStatus, "-", '所属部门:', txtSSDept, "-", '借调部门:',
            txtJDDept]
    });
    var gridTbar = new Ext.Toolbar({
        border : false,
        height : 25,
        items : [btnQuery, btnAdd, btnModify, btnDelete, btnReport, btnBatch]
    });
    var recordGrid = new Ext.grid.GridPanel({
        region : "center",
        store : queryStore,
        border : false,
        columns : [
            // 自动行号
            new Ext.grid.RowNumberer({
                header : "行号",
                width : 31
            }), {
                header : "员工工号",
                sortable : true,
                dataIndex : 'empCode'
            }, {
                header : '员工姓名',
                sortable : true,
                dataIndex : 'chsName'

            }, {
                header : "所属部门",
                sortable : true,
                dataIndex : 'ssDeptName'
            }, {
                header : "所在岗位",
                sortable : true,
                dataIndex : 'stationName'
            }, {
                header : "借调部门",
                sortable : true,
                dataIndex : 'jdDeptName'
            }, {
                header : "开始日期",
                sortable : true,
                dataIndex : 'startDate'
            }, {

                header : "单据状态",
                sortable : true,
                dataIndex : 'dcmStatus',
                renderer : function(value) {
                    if (value == "0")
                        return '未上报';
                    else if (value == "1")
                        return '已上报'
                    else if (value == "2")
                        return '已终结'
                    else if (value == "3")
                        return '已退回'
                }
            }, {
                header : "备注",
                sortable : true,
                dataIndex : 'memo'
            }],
        sm : sm,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        },
        // 头部工具栏
        tbar : gridTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : queryStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });
    queryStore.on('beforeload', function() {
        this.baseParams = {
            startDate : startDate.getValue(),
            endDate : endDate.getValue(),
            ssDeptId : txtSSDept.getValue(),
            jdDeptId : txtJDDept.getValue(),
            dcmStatus : cmbStatus.getValue()
        };
    })
    // 双击进入登记tab
    recordGrid.on("rowdblclick", modifyData);
    selectData(false);

    // 子页面显示Store
    var storeSubShow = new Ext.data.JsonStore({
        fields : ['empId', 'chsName', 'stationId', 'stationName', 'ssDeptId', 'ssDeptName', 'jdDeptId', 'jdDeptName']
    })
    storeSubShow.insert(0, new Ext.data.Record({
        'empId' : '',
        'chsName' : '',
        'stationId' : '',
        'stationName' : '',
        'ssDeptId' : '',
        'ssDeptName' : '',
        'jdDeptId' : '',
        'jdDeptName' : ''
    }))

    var labelTextName = new Ext.form.Label({
        text : '员工姓名:',
        height : 15,
        labelAlign : 'right',
        width : 59,
        style : {
            'fontSize' : 12,
            'padding-top' : '10%'

        }
    })
    // 员工姓名
    var textName = new Ext.form.ComboBox({
        hideLabel : true,
        valueField : 'id',
        readOnly : true,
        allowBlank : false,
        width : 125,
        store : storeSubShow,
        name : "empId",
        displayField : "chsName",
        valueField : "empId",
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 员工姓名的onClick事件
    textName.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            storeSubShow.getAt(0).data['empId'] = person.empId;
            storeSubShow.getAt(0).data['chsName'] = person.workerName;
            textName.setValue(person.empId);
            txtSubNameCode.setValue(person.workerCode);
            storeSubShow.getAt(0).data['stationId'] = person.stationId;
            storeSubShow.getAt(0).data['stationName'] = person.stationName;
            txtSubStation.setValue(person.stationId);
            storeSubShow.getAt(0).data['ssDeptId'] = person.deptId;
            storeSubShow.getAt(0).data['ssDeptName'] = person.deptName;
            txtSubSSdept.setValue(person.deptId);
        }
    })
    // 员工工号
    var txtSubNameCode = new Ext.form.TextField({
        fieldLabel : '员工工号',
        id : 'empCode',
        disabled : true
    })
    // 所在岗位
    var txtSubStation = new Ext.form.ComboBox({
        fieldLabel : '所在岗位',
        id : "stationId",
        width : 125,
        store : storeSubShow,
        name : "stationId",
        displayField : "stationName",
        valueField : "stationId",
        mode : 'local',
        triggerAction : 'all',
        disabled : true,
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 所属部门
    var txtSubSSdept = new Ext.form.ComboBox({
        fieldLabel : '所属部门',
        id : "ssDeptId",
        width : 125,
        store : storeSubShow,
        name : "ssDeptId",
        displayField : "ssDeptName",
        valueField : "ssDeptId",
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        },
        disabled : true
    });

    // 借调部门
    var txtSubJDDept = new Ext.form.ComboBox({
        fieldLabel : '借调部门<font color ="red">*</font>',
        valueField : 'id',
        width : 125,
        readOnly : true,
        allowBlank : false,
        store : storeSubShow,
        displayField : "jdDeptName",
        valueField : "jdDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    txtSubJDDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeSubShow.getAt(0).data['jdDeptId'] = dept.ids;
            storeSubShow.getAt(0).data['jdDeptName'] = dept.names;
            txtSubJDDept.setValue(dept.ids);
        }
    })

    var labelTextBack = new Ext.form.Label({
        text : '是否已回:',
        labelAlign : 'right',
        style : {
            'fontSize' : 12,
            'padding-top' : '10%'

        }
    })
    var isLeafRadio = new Ext.form.Radio({
        id : 'Y',
        width : 100,
        boxLabel : '是',
        name : 'pressboard.isLeaf',
        inputValue : 'Y'
    })
    var isLeafRadioNot = new Ext.form.Radio({
        id : 'N',
        boxLabel : '否',
        name : 'pressboard.isLeaf',
        inputValue : 'N',
        checked : true
    })
    var randioPanel = new Ext.Panel({
        border : false,
        style : 'font-size:13;',
        layout : 'column',
        width : 300,
        items : [{
                columnWidth : .15,
                border : false,
                items : [isLeafRadio]
            }, {
                columnWidth : .15,
                border : false,
                items : [isLeafRadioNot]
            }]
    })

    // 开始日期
    var startSubDate = new Ext.form.TextField({
        fieldLabel : '开始日期<font color ="red">*</font>',
        id : 'startDate',
        // name : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }
        }
    });

    // 结束日期
    var endSubDate = new Ext.form.TextField({
        fieldLabel : '结束日期<font color ="red">*</font>',
        id : 'endDate',
        // name : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }

        }
    });

    // 备注
    var areaSubMomo = new Ext.form.TextArea({
        id : "memo",
        width : 360,
        maxLength : 128,
        height : 60,
        fieldLabel : '备注'
    })

    var hiddenBorrowInId = new Ext.form.Hidden({
        id : 'hiddenBorrowInId'
    })

    var hiddenLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate'
    })

    var formPanelSub = new Ext.form.FormPanel({
        border : false,
        labelAlign : 'right',
        labelWidth : 85,
        frame : true,
        layout : 'form',
        items : [{
                layout : "column",
                border : false,
                height : 5
            }, {
                layout : "column",
                border : false,
                labelAlign : "right",
                items : [{
                        columnWidth : 0.0455,
                        layout : "form",
                        border : false
                    }, {
                        columnWidth : 0.15,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [labelTextName, hiddenBorrowInId, hiddenLastModifiedDate]
                    }, {
                        columnWidth : 0.308,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [textName]
                    }, {
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtSubNameCode]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtSubStation]
                    }, {
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [txtSubSSdept]
                    }]
            }, {
                layout : "column",
                border : false,
                labelAlign : 'right',
                items : [{
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [txtSubJDDept]
                    }, {
                        columnWidth : 0.062,
                        border : false
                    }, {
                        columnWidth : 0.13,
                        layout : "form",
                        border : false,
                        labelAlign : "right",
                        items : [labelTextBack]
                    }, {
                        columnWidth : 0.275,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [randioPanel]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [startSubDate]
                    }, {
                        columnWidth : 0.5,
                        layout : "form",
                        border : false,
                        height : 30,
                        labelAlign : "right",
                        items : [endSubDate]
                    }]
            }, {
                layout : "form",
                border : false,
                items : [areaSubMomo]
            }]
    });

    // 保存按钮
    var btnSubSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveData
    });

    // 取消按钮
    var btnSubCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : cancelSubWin
    });

    // 弹出画面
    var winSub = new Ext.Window({
        // height : 320,
        width : 500,
        modal : true,
        resizable : false,
        closeAction : 'hide',
        items : [formPanelSub],
        buttonAlign : "center",
        title : '员工借调登记',
        buttons : [btnSubSave, btnSubCancel],
        closeAction : 'hide'
    });

    // 主页面显示Store
    var storeInJD = new Ext.data.JsonStore({
        fields : ['jdDeptId', 'jdDeptName']
    })
    storeInJD.insert(0, new Ext.data.Record({
        'jdDeptId' : '',
        'jdDeptName' : ''
    }))
    // 调动后部门
    var txtInJDDept = new Ext.form.ComboBox({
        fieldLabel : '借调单位<font color ="red">*</font>',
        width : 125,
        hiddenName : 'mrDept',
        allowBlank : false,
        readOnly : true,
        store : storeInJD,
        displayField : "jdDeptName",
        valueField : "jdDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    txtInJDDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeInJD.getAt(0).data['jdDeptId'] = dept.ids;
            storeInJD.getAt(0).data['jdDeptName'] = dept.names;
            txtInJDDept.setValue(dept.ids);
        }
    })
    // 批处理开始日期
    var startInDate = new Ext.form.TextField({
        fieldLabel : '开始日期<font color ="red">*</font>',
        name : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }
        }
    });

    // 批处理结束日期
    var endInDate = new Ext.form.TextField({
        fieldLabel : '结束日期<font color ="red">*</font>',
        name : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }
        }
    });

    // 新增按钮
    var btnInAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addBatchBorrowInData
    });

    /**
     * 点击新增按钮时
     */
    function addBatchBorrowInData() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            var exist = "";
            for (i = 0; i < storeBorrowIn.getCount(); i++) {
                if (storeBorrowIn.getAt(0).get('empId') == person.empId) {
                    exist = person.workerName;
                    break;
                }
            }
            if (exist != "") {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_007, exist));
            } else {
                var data = new recordBorrowIn({
                    'empId' : person.empId,
                    'chsName' : person.workerName,
                    'ssDeptId' : person.deptId,
                    'ssDeptName' : person.deptName
                })
                storeBorrowIn.add(data);
                gridBorrowIn.getView().refresh();
                if (storeBorrowIn.getCount() > 0) {
                    // btnInDelete.setDisabled(false);
                    btnInSubmit.setDisabled(false);
                } else {
                    // btnInDelete.setDisabled(true);
                    btnInSubmit.setDisabled(true);
                }
            }
        }

    }

    // 删除按钮
    var btnInDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delBatchBorrowInData
    });

    // 取消按钮
    var btnInCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : cancelInWin
    });

    // 确认按钮
    var btnInSubmit = new Ext.Button({
        text : Constants.BTN_CONFIRM,
        iconCls : Constants.CLS_OK,
        disabled : true,
        handler : confirmInWin
    })

    // 右边窗口
    var panelBorrowIn = new Ext.FormPanel({
        border : false,
        labelWidth : 65,
        labelAlign : "right",
        style : 'color:#000;',
        items : [{
                height : 20,
                border : false
            }, {
                layout : "form",
                items : [txtInJDDept],
                border : false
            }, {
                height : 10,
                border : false
            }, {
                layout : "form",
                items : startInDate,
                border : false
            }, {
                height : 10,
                border : false
            }, {
                layout : "form",
                items : endInDate,
                border : false
            }]
    });

    // 数据源--------------------------------
    var recordBorrowIn = Ext.data.Record.create([{
            /** 员工Id */
            name : 'empId'
        }, {
            /** 中文名称 */
            name : 'chsName'
        }, {
            /** 所属部门ID */
            name : 'ssDeptId'
        }, {
            /** 所属部门名称 */
            name : 'ssDeptName'
        }]);

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, recordBorrowIn);
    // 定义封装缓存数据的对象
    var storeBorrowIn = new Ext.data.Store({
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var smIn = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    var gridBorrowIn = new Ext.grid.GridPanel({
        region : "center",
        store : storeBorrowIn,
        width : 240,
        height : 258,
        border : true,
        columns : [
            // 自动行号
            new Ext.grid.RowNumberer({
                header : "行号",
                width : 31
            }), {
                header : "员工Id",
                sortable : true,
                hidden : true,
                dataIndex : 'empId'
            }, {
                header : '员工姓名',
                sortable : true,
                dataIndex : 'chsName'

            }, {
                header : "所属部门",
                sortable : true,
                dataIndex : 'ssDeptName'
            }, {
                header : "所属部门Id",
                sortable : true,
                hidden : true,
                dataIndex : 'ssDeptId'
            }],
        sm : smIn,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        }

    });

    // 借调员工登记Panel
    var tabBorrowIn = new Ext.Panel({
        title : '借调员工登记',
        enableTabScroll : true,
        activeTab : 0,
        autoScroll : true,
        border : false,
        tbar : [btnInAdd, btnInDelete, btnInCancel, btnInSubmit],
        layout : 'column',
        width : 500,
        items : [{
                columnWidth : .55,
                border : false,
                items : [gridBorrowIn]
            }, {
                columnWidth : .45,
                border : false,
                items : [panelBorrowIn]
            }]
    })
    // 主页面显示Store
    var storeOutJD = new Ext.data.JsonStore({
        fields : ['jdDeptId', 'jdDeptName']
    })
    storeOutJD.insert(0, new Ext.data.Record({
        'jdDeptId' : '',
        'jdDeptName' : ''
    }))
    // 调动后部门
    var txtOutJDDept = new Ext.form.ComboBox({
        fieldLabel : '借调单位<font color ="red">*</font>',
        width : 125,
        hiddenName : 'mrDept',
        allowBlank : false,
        readOnly : true,
        store : storeOutJD,
        displayField : "jdDeptName",
        valueField : "jdDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    txtOutJDDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeOutJD.getAt(0).data['jdDeptId'] = dept.ids;
            storeOutJD.getAt(0).data['jdDeptName'] = dept.names;
            txtOutJDDept.setValue(dept.ids);
            storeBorrowOut.load({
                params : {
                    jdDeptId : txtOutJDDept.getValue()
                }
            })
        }
    })
    // 批处理开始日期
    var startOutDate = new Ext.form.TextField({
        fieldLabel : '开始日期',
        name : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        disabled : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });

    // 批处理结束日期
    var endOutDate = new Ext.form.TextField({
        fieldLabel : '结束日期<font color ="red">*</font>',
        name : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
            }
        }
    });

    // 删除按钮
    var btnOutDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delOutRecord
    });

    // 取消按钮
    var btnOutCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : cancelOutWin
    });

    // 确定按钮
    var btnOutSubmit = new Ext.Button({
        text : Constants.CONFIRM,
        iconCls : Constants.CLS_OK,
        disabled : true,
        handler : confirmOutWin
    })

    // 右边窗口
    var panelBorrowOut = new Ext.FormPanel({
        border : false,
        labelWidth : 65,
        labelAlign : "right",
        style : 'color:#000;',
        items : [{
                height : 20,
                border : false
            }, {
                layout : "form",
                items : [txtOutJDDept],
                border : false
            }, {
                height : 10,
                border : false
            }, {
                layout : "form",
                items : startOutDate,
                border : false
            }, {
                height : 10,
                border : false
            }, {
                layout : "form",
                items : endOutDate,
                border : false
            }]
    });

    // 数据源--------------------------------
    var recordBorrowOut = Ext.data.Record.create([{
            name : 'hrJEmployeeborrowInId'
        }, {
            /** 员工Code */
            name : 'empCode'
        }, {
            name : 'empId'
        }, {
            /** 中文名称 */
            name : 'chsName'
        }, {
            name : 'startDate'
        }, {
            /** 所属部门ID */
            name : 'jdDeptId'
        }, {
            /** 所属部门名称 */
            name : 'jdDeptName'
        }, {
            name : 'lastModifiedDate'
        }]);

    // 定义获取数据源
    var dataOutProxy = new Ext.data.HttpProxy({
        url : 'hr/getEmpBorrowInRegisterByDeptId.action'
    });

    // 定义格式化数据
    var theOutReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "startDate",
            direction : "ASC"
        }
    }, recordBorrowOut);
    // 定义封装缓存数据的对象
    var storeBorrowOut = new Ext.data.Store({
        // 访问的对象
        proxy : dataOutProxy,
        // 处理数据的对象
        reader : theOutReader
    });
    storeBorrowOut.on('load', function() {
        if (storeBorrowOut.getCount() > 0) {
            // btnOutDelete.setDisabled(false);
            btnOutSubmit.setDisabled(false);
        } else if (storeBorrowOut.getCount() == 0) {
            // btnOutDelete.setDisabled(true);
            btnOutSubmit.setDisabled(true);
        }
    })
    // 定义选择列
    var smOut = new Ext.grid.RowSelectionModel({
        singleSelect : false
    });
    var gridBorrowOut = new Ext.grid.GridPanel({
        region : "center",
        store : storeBorrowOut,
        width : 240,
        height : 258,
        frame : false,
        // border : true,
        columns : [
            // 自动行号
            new Ext.grid.RowNumberer({
                header : "行号",
                width : 31
            }), {
                header : "员工工号",
                sortable : true,
                hidden : true,
                dataIndex : 'empCode'
            }, {
                header : '员工姓名',
                sortable : true,
                dataIndex : 'chsName'

            }, {
                header : '开始日期',
                sortable : true,
                hidden : true,
                dataIndex : 'startDate'
            }, {
                header : "所属部门",
                sortable : true,
                dataIndex : 'jdDeptName'
            }, {
                header : "所属部门Id",
                sortable : true,
                hidden : true,
                dataIndex : 'jdDeptId'
            }],
        sm : smOut,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        }
    });

    gridBorrowOut.on("rowclick", getStartData);
    // 单击gird时
    function getStartData() {
        var record = gridBorrowOut.getSelectionModel().getSelected();
        if (record != null)
            startOutDate.setValue(record.get('startDate'));
        else
            startOutDate.setValue("");
    }

    // 借回员工处理Panel
    var tabBorrowOut = new Ext.Panel({
        title : '借回员工处理',
        enableTabScroll : true,
        activeTab : 1,
        autoScroll : true,
        border : false,
        layout : "border",
        tbar : [btnOutDelete, btnOutCancel, btnOutSubmit],
        layout : 'column',
        width : 500,
        items : [{
                columnWidth : .55,
                border : false,
                items : [gridBorrowOut]
            }, {
                columnWidth : .45,
                border : false,
                items : [panelBorrowOut]
            }]
    })

    // **********子画面主tabPanel********** //
    var tabPanel = new Ext.TabPanel({
        // activeTab : 0,
        autoScroll : true,
        layoutOnTabChange : true,
        tabPosition : 'bottom',
        border : false,
        items : [tabBorrowIn, tabBorrowOut]
    })

    // **********批处理子画面********** //
    var winBatch = new Ext.Window({
        width : 500,
        height : 340,
        modal : true,
        resizable : false,
        title : '员工借调登记批处理',
        enableTabScroll : true,
        layout : "fit",
        border : false,
        items : [{
                layout : 'fit',
                border : false,
                margins : '0 0 0 0',
                region : 'center',
                autoScroll : true,
                items : [tabPanel]
            }],
        closeAction : 'hide'

    });

    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'border',
        autoHeight : true,
        items : [headTbar, recordGrid]
    });

    /**
     * 点击查询按钮时执行的操作
     */
    function queryEmpMoveData() {
        selectData(true);
    }

    /**
     * 点击新增按钮时
     */
    function addEmpBorrowInData() {
        flag = FLAG_SUB_ADD;
        winSub.x = undefined;
        winSub.y = undefined;
        winSub.show();
        // 设置子窗口标题
        winSub.setTitle("新增员工借调登记");
        formPanelSub.getForm().reset();
        isLeafRadio.setDisabled(true);
        isLeafRadioNot.setDisabled(true);
        textName.setDisabled(false);
        labelTextName.setText('员工姓名<font color ="red">*</font>:');
    }

    /**
     * 点击修改按钮时
     */
    function modifyData() {
        formPanelSub.getForm().reset();
        textName.setDisabled(true);
        flag = FLAG_SUB_MODIFY;
        if (recordGrid.selModel.hasSelection()) {
            var record = recordGrid.getSelectionModel().getSelected();
            hiddenBorrowInId.setValue(record.get('hrJEmployeeborrowInId'));
            // 显示主窗口
            winSub.x = undefined;
            winSub.y = undefined;
            winSub.show();
            isLeafRadio.setDisabled(false);
        	isLeafRadioNot.setDisabled(false);
            // 加载该行记录
            formPanelSub.getForm().loadRecord(record);
            // // 设置子窗口标题
            winSub.setTitle("修改员工借调登记");
            labelTextName.setText('员工姓名:');
            storeSubShow.getAt(0).data['empId'] = record.get('empId');
            storeSubShow.getAt(0).data['chsName'] = record.get('chsName');
            textName.setValue(record.get('empId'));
            storeSubShow.getAt(0).data['stationId'] = record.get('stationId');
            storeSubShow.getAt(0).data['stationName'] = record.get('stationName');
            txtSubStation.setValue(record.get('stationId'));
            storeSubShow.getAt(0).data['ssDeptId'] = record.get('ssDeptId');
            storeSubShow.getAt(0).data['ssDeptName'] = record.get('ssDeptName');
            txtSubSSdept.setValue(record.get('ssDeptId'));
            storeSubShow.getAt(0).data['jdDeptId'] = record.get('jdDeptId');
            storeSubShow.getAt(0).data['jdDeptName'] = record.get('jdDeptName');
            txtSubJDDept.setValue(record.get('jdDeptId'),true);
            startSubDate.setValue(record.get('startDate'));
            endSubDate.setValue(record.get('endDate'));
            if (record.get('ifBack') == '1') {
                isLeafRadio.checked = true;
            } else if (record.get('ifBack') == '0')
                isLeafRadioNot.checked = true;
            hiddenLastModifiedDate.setValue(record.get('lastModifiedDate'));
            markValid(false);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    function markValid(flag) {
        if (flag) {
            if (txtSubJDDept.getValue() == "")
                txtSubJDDept.markInvalid();
            if (startSubDate.getValue() == "")
                startSubDate.markInvalid();
            if (endSubDate.getValue() == "")
                endSubDate.markInvalid();
        } else {
            txtSubJDDept.clearInvalid();
            startSubDate.clearInvalid();
            endSubDate.clearInvalid();
        }
    }

    /**
     * 点击取消按钮时
     */
    function cancelWin() {

        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 隐藏弹出窗口
                winSub.hide();
            }
        })
    }

    /**
     * 点击取消按钮时
     */
    function cancelSubWin() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // alert(startDate.getValue());
                // alert(endDate.getValue());
                // 隐藏弹出窗口
                winSub.hide();
            }
        })
    }
    /**
     * 点击删除按钮时
     */
    function delRecord() {
        // 如果选择了数据的话，先弹出提示
        if (recordGrid.selModel.hasSelection()) {
            var record = recordGrid.getSelectionModel().getSelected();
            if (record.get('dcmStatus') == '1') {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "已上报", "删除"));
            } else if (record.get('dcmStatus') == '2') {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "已终结", "删除"));
            } else {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        Ext.Ajax.request({
                            method : Constants.POST,
                            url : 'hr/deleteEmpBorrowInRegisterInfo.action',
                            params : {
                                hrJEmployeeborrowInId : record.get('hrJEmployeeborrowInId'),
                                lastModifiedDate : record.get('lastModifiedDate')
                            },
                            success : function(result, request) {
                                if (result.responseText) {
                                    var o = eval("(" + result.responseText + ")");
                                    var suc = o.msg;
                                    // 如果成功，弹出删除成功
                                    if (suc == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (suc == Constants.DATA_USING) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                    } else {
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005)
                                        // 成功,则重新加载grid里的数据
                                        // 重新加载数据
                                        selectData(false);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        } else {
            // 如果没有选择数据，弹出错误提示框
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    /**
     * 点击保存数据时
     */
    function saveData() {
        var message = "";
        var messageLength = "";
        if (txtSubJDDept.getValue() != null && txtSubJDDept.getValue() != ""){
        }else {
			message += String.format(Constants.COM_E_003, "借调部门") + "</br>";        	
        }
        if (startSubDate.getValue() == "")
            message += String.format(Constants.COM_E_003, "开始日期") + "</br>";
        if (endSubDate.getValue() == "")
            message += String.format(Constants.COM_E_003, "结束日期") + "</br>";
        if (areaSubMomo.getValue().toString().length > 128)
            messageLength += "1";
        if (FLAG_SUB_ADD == flag) {
            if (textName.getValue() == "")
                message = String.format(Constants.COM_E_003, "员工姓名") + "</br>" + message;
        }
        if (message != "")
            Ext.Msg.alert(Constants.ERROR, message);
        else {
            if (FLAG_SUB_ADD == flag) {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        if (messageLength != "") {
                        } else if (Date.parseDate(startSubDate.getValue(), 'Y-m-d').getTime() > Date.parseDate(
                        endSubDate.getValue(), 'Y-m-d').getTime()) {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
                        } else {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'hr/checkEmpIsBorrowIn.action',
                                params : {
                                    empId : textName.getValue()
                                },
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText + ")");
                                    var succ = o.msg;
                                    // 如果更新失败，弹出提示
                                    if (succ == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (succ == "Y") {
                                        Ext.Msg.alert(Constants.ERROR, String.format(Constants.PE005_E_001, textName
                                        .getRawValue()));
                                    } else if (succ == "N") {
                                        Ext.Ajax.request({
                                            method : Constants.POST,
                                            url : 'hr/addEmpBorrowInRegister.action',
                                            params : {
                                                empId : textName.getValue(),
                                                jdDeptId : txtSubJDDept.getValue(),
                                                startDate : startSubDate.getValue(),
                                                endDate : endSubDate.getValue(),
                                                memo : areaSubMomo.getValue()
                                            },
                                            success : function(result, request) {
                                                var o = eval("(" + result.responseText + ")");
                                                var succ = o.msg;
                                                // 如果更新失败，弹出提示
                                                if (succ == Constants.SQL_FAILURE) {
                                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                                } else {
                                                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004,function() {
	                                                    selectData(false);
	                                                    // 重新加载数据
	                                                    winSub.hide();
                                                    })
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    }
                })
            } else if (FLAG_SUB_MODIFY = flag) {
                if (isLeafRadio.checked == true)
                    isBackValue = "1";
                if (isLeafRadioNot.checked == true)
                    isBackValue = "0";
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        if (messageLength != "") {
                        } else if (Date.parseDate(startSubDate.getValue(), 'Y-m-d').getTime() > Date.parseDate(
                        endSubDate.getValue(), 'Y-m-d').getTime()) {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
                        } else {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'hr/updateEmpBorrowInRegister.action',
                                params : {
                                    hrJEmployeeborrowInId : hiddenBorrowInId.getValue(),
                                    jdDeptId : txtSubJDDept.getValue(),
                                    lastModifiedDate : hiddenLastModifiedDate.getValue(),
                                    ifBack : isBackValue,
                                    startDate : startSubDate.getValue(),
                                    endDate : endSubDate.getValue(),
                                    memo : areaSubMomo.getValue()
                                },
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText + ")");
                                    var succ = o.msg;
                                    // 如果更新失败，弹出提示
                                    if (succ == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (succ == Constants.DATA_USING) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                    } else {
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004,function() {
                                        	 selectData(false);
		                                        winSub.hide();
		                                        // 重新加载数据
                                        })
                                       
                                    }
                                }
                            });
                        }
                    }
                })
            }
        }

    }

    /**
     * 点击上报时
     */
    function reportData() {
        // 如果选择了数据的话，先弹出提示
        if (recordGrid.selModel.hasSelection()) {
            var record = recordGrid.getSelectionModel().getSelected();
            if (record.get('dcmStatus') == '1') {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "已上报", "上报"));
            } else if (record.get('dcmStatus') == '2') {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "已终结", "上报"));
            } else {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_006, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        Ext.Ajax.request({
                            method : Constants.POST,
                            url : 'hr/reportEmpBorrowInRegisterInfo.action',
                            params : {
                                hrJEmployeeborrowInId : record.get('hrJEmployeeborrowInId'),
                                lastModifiedDate : record.get('lastModifiedDate')
                            },
                            success : function(result, request) {
                                if (result.responseText) {
                                    var o = eval("(" + result.responseText + ")");
                                    var suc = o.msg;
                                    if (suc == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (suc == Constants.DATA_USING) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                    } else {
                                        // 重新加载数据
                                        selectData(false);
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_007)
                                    }
                                }
                            }
                        });
                    }
                });

            }
        } else {
            // 如果没有选择数据，弹出错误提示框
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    /**
     * 点击删除按钮时
     */
    function delBatchBorrowInData() {
        // 如果选择了数据的话，先弹出提示
        if (gridBorrowIn.selModel.hasSelection()) {

            var record = gridBorrowIn.getSelectionModel().getSelected();
            storeBorrowIn.remove(record);
            gridBorrowIn.getView().refresh();
            if (storeBorrowIn.getCount() > 0) {
                // btnInDelete.setDisabled(false);
                btnInSubmit.setDisabled(false);
            } else {
                // btnInDelete.setDisabled(true);
                btnInSubmit.setDisabled(true);
            }
        } else {
            // 如果没有选择数据，弹出错误提示框
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    function cancelInWin() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 隐藏弹出窗口
                winBatch.hide();
            }
        })
    }

    /**
     * 点击确认按钮时
     */
    function confirmInWin() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(buttonobj) {
            // 确认保存
            if (buttonobj == "yes") {
                var message = "";
                var empidArray = "";
                if (txtInJDDept.getValue() == "")
                    message += String.format(Constants.COM_E_003, "借调单位") + "</br>";
                if (startInDate.getValue() == "")
                    message += String.format(Constants.COM_E_003, "开始日期") + "</br>";
                if (endInDate.getValue() == "")
                    message += String.format(Constants.COM_E_003, "结束日期") + "</br>";
                if (message != "")
                    Ext.Msg.alert(Constants.ERROR, message);
                else if (Date.parseDate(startInDate.getValue(), 'Y-m-d').getTime() > Date.parseDate(
                endInDate.getValue(), 'Y-m-d').getTime()) {
                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
                } else {
                    for (i = 0; i < storeBorrowIn.getCount(); i++) {
                        if (i > 0)
                            empidArray += ",";
                        empidArray += storeBorrowIn.getAt(i).get('empId');
                    }
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/batchAddEmpBorrowInRegister.action',
                        params : {
                            empidArray : empidArray,
                            jdDeptId : txtInJDDept.getValue(),
                            startDate : startInDate.getValue(),
                            endDate : endInDate.getValue()
                        },
                        success : function(result, request) {
                            var o = eval("(" + result.responseText + ")");
                            var succ = o.msg;
                            // 如果更新失败，弹出提示
                            if (succ == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else if (succ == "success") {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    winBatch.hide();
                                });
                                // 重新加载数据
                                selectData(false);
                            } else {
                                var arr = succ.split(",");
                                var messa = "";
                                for (j = 0; j < arr.length; j++) {
                                    for (i = 0; i < storeBorrowIn.getCount(); i++) {
                                        if (storeBorrowIn.getAt(i).get('empId') == arr[j]) {
                                            messa += String.format(Constants.PE005_E_001, storeBorrowIn.getAt(i)
                                            .get('chsName'))
                                            + "</br>";
                                            break;
                                        }
                                    }
                                }
                                Ext.Msg.alert(Constants.ERROR, messa);
                            }
                        }
                    });
                }
            }
        })
    }

    function delOutRecord() {
        // 如果选择了数据的话，先弹出提示
        if (gridBorrowOut.selModel.hasSelection()) {
            var records = gridBorrowOut.selModel.getSelections()
            var record = gridBorrowOut.getSelectionModel().getSelected();
            for (i = 0; i < records.length; i++) {
                storeBorrowOut.remove(records[i]);
            }
            gridBorrowOut.getView().refresh();
        } else {
            // 如果没有选择数据，弹出错误提示框
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    /**
     * 点击取消按钮时
     */
    function cancelOutWin() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 隐藏弹出窗口
                winBatch.hide();
            }
        })
    }

    function confirmOutWin() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(buttonobj) {
            // 确认保存
            if (buttonobj == "yes") {
                var message = "";
                if (txtOutJDDept.getValue() == "")
                    message += String.format(Constants.COM_E_003, "借调部门") + "</br>";
                if (endOutDate.getValue() == "")
                    message += String.format(Constants.COM_E_003, "结束日期") + "</br>";
                if (message != "") {
                    Ext.Msg.alert(Constants.ERROR, message);
                    return;
                }
                for (i = 0; i < storeBorrowOut.getCount(); i++) {
                    var data = storeBorrowOut.getAt(i);
                    if (data.get('startDate') != null && data.get('startDate') != "") {
                        if (Date.parseDate(data.get('startDate'), 'Y-m-d').getTime() > Date.parseDate(
                        endOutDate.getValue(), 'Y-m-d').getTime()) {
                            var name = "员工姓名为" + data.get('chsName') + "的数据的开始日期";
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, name, "结束日期"));
                            return;
                        }
                    }
                }
                var borrowInArray = "";
                for (i = 0; i < storeBorrowOut.getCount(); i++) {
                    if (i > 0) {
                        borrowInArray += ",";
                    }
                    borrowInArray += storeBorrowOut.getAt(i).get('hrJEmployeeborrowInId');
                    borrowInArray += ",";
                    if (storeBorrowOut.getAt(i).get('lastModifiedDate') != "") {
                        borrowInArray += storeBorrowOut.getAt(i).get('lastModifiedDate');
                    } else {
                        borrowInArray += "";
                    }
                }
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'hr/batchUpdateEmpBorrowInRegister.action',
                    params : {
                        borrowInArray : borrowInArray,
                        endDate : endOutDate.getValue()
                    },
                    success : function(result, request) {
                        var o = eval("(" + result.responseText + ")");
                        var succ = o.msg;
                        // 如果更新失败，弹出提示
                        if (succ == Constants.SQL_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        } else if (succ == Constants.DATE_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                        } else if (succ == Constants.DATA_USING) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                        } else {
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004,
                            function() {
                            	winBatch.hide();
                            })
                            selectData(false);
                            // 重新加载数据
                        }
                    }

                });
            }
        })
    }

    function selectData(flag) {
        queryStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE,
                startDate : startDate.getValue()
                // endDate : endDate.getValue(),
                // ssDeptId : txtSSDept.getValue(),
                // jdDeptId : txtJDDept.getValue(),
                // dcmStatus : cmbStatus.getValue()
            },
            callback : function() {
                if (flag) {
                    if (queryStore.getCount() == 0) {
                        // btnModify.setDisabled(true);
                        // btnReport.setDisabled(true);
                        // btnDelete.setDisabled(true);
                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                    } else {
                        // btnModify.setDisabled(false);
                        // btnReport.setDisabled(false);
                        // btnDelete.setDisabled(false);
                    }
                } else {
                    if (queryStore.getCount() == 0) {
                        // btnModify.setDisabled(true);
                        // btnReport.setDisabled(true);
                        // btnDelete.setDisabled(true);
                    } else {
                        // btnModify.setDisabled(false);
                        // btnReport.setDisabled(false);
                        // btnDelete.setDisabled(false);
                    }
                }
            }
        });
    }

    // 画面上的开始日期和结束日期的check
    function checkDate() {
        var strStartDate = Ext.get("startDate").dom.value;
        var strEndDate = Ext.get("endDate").dom.value;
        if (Date.parseDate(strStartDate, 'Y-m-d').getTime() > Date.parseDate(strEndDate, 'Y-m-d').getTime())
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
        if (strStartDate != "" && strEndDate != "") {
            var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
            var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
            if (dateStart.getTime() > dateEnd.getTime()) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
})