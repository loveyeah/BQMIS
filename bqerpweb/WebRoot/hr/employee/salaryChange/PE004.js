Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    var labelLength = 10;
    var label1 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });
    var label2 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });

    var label3 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });

    var label4 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });

    var label5 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });

    var label6 = new Ext.form.Label({
        text : "级",
        style : "padding-top:8px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        width : labelLength
    });

    var MyRecord = Ext.data.Record.create([{
            // 薪酬变动单id
            name : 'salayradjustid'
        }, {
            // 员工姓名
            name : 'chsName'
        }, {
            // 起薪日期
            name : 'doDate'
        }, {
            // 所属部门
            name : 'deptName'
        }, {
            // 变更前执行岗级
            name : 'oldCheckStationGrade'
        }, {
            // 变更后执行岗级
            name : 'newCheckStationGrade'
        }, {
            // 变更前标准岗级
            name : 'oldStationGrade'
        }, {
            // 变更后标准岗级
            name : 'newStationGrade'
        }, {
            // 变更前薪级
            name : 'oldSalaryGrade'
        }, {
            // 变更后薪级
            name : 'newSalaryGrade'
        }, {
            // 变动类别
            name : 'adjustType'
        }, {
            // 岗薪变化类别
            name : 'stationChangeType'
        }, {
            // 单据状态
            name : 'dcmState'
        }, {
            // 原因
            name : 'reason'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 更新时间，排他用
            name : 'updateTime'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/getSalaryChangeList.action'
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
    // 隐藏域员工id
    var hiddenId = new Ext.form.Hidden({
        hiddenName : 'empId'
    })
    // 隐藏部门id
    var hiddenMrDept = new Ext.form.Hidden({
        hiddenName : 'deptId'
    })
    // 部门选择
    var txtDept = new Ext.form.TextField({
        fieldLabel : '所属部门',
//        width : 100,
        valueField : 'id',
        hiddenName : 'dept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '合肥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args,
                'dialogWidth:500px;dialogHeight:320px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    txtDept.setValue(dept.names);
                    hiddenMrDept.setValue(dept.ids);
                }
            }
        }
    });

    // 弹出页面起薪日期初始值设置为系统当前日期
    var endDate = new Date();
    // 系统当前日期
    endDate.setDate(endDate.getDate());
    endDate = endDate.format('Y-m-d');

    // 起薪开始日期
    var txtStartDate = new Ext.form.TextField({
        id : 'startDate',
        name : 'startDate',
        style : 'cursor:pointer',
        width : 110,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
            }
        }
    });

    // 起薪结束时间
    var txtEndDate = new Ext.form.TextField({
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        width : 110,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
            }
        }
    });

    // 单据状态
    var statusField = new Ext.form.CmbHRCode({
        fieldLabel : '单据状态<font color ="red">*</font>',
        type : "单据状态",
        width : 100,
        id : 'rewardsPunishType',
        name : 'rewardsPunishType',
        value : 0
    });
    statusField.setValue("0");
    // 隐藏ID
    var hdnId = new Ext.form.Hidden({
        id : "salayradjustid",
        name : "salaryBean.salayradjustid",
        value : ""
    });
    // 隐藏修改时间
    var hdnUpdateTime = new Ext.form.Hidden({
        id : "updateTime",
        name : "updateTime",
        value : ""
    });

    var width = 85;

    // 员工姓名
    var staffName = new Ext.form.TextField({
        id : "chsName",
        name : "chsName",
        fieldLabel : "员工姓名<font color='red'>*</font>",
        maxLength : 12,
        readOnly : true,
        width : 101
    });
    staffName.onClick(selectName2);

    // 起薪日期
    var txtDoDate = new Ext.form.TextField({
        id : 'doDate',
        name : 'salaryBean.doDate',
        fieldLabel : "起薪日期<font color='red'>*</font>",
        width : 100,
        value : endDate,
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false
                });
            }
        }
    });
    // 所在部门名称
    var txtDepName = new Ext.form.TextField({
        id : "depName",
        name : "deptName",
        readOnly : true,
        fieldLabel : "所属部门",
        width : 325,
        disabled : true
    });
    
    // 岗位级别:
	var stationLestore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getStationlevel'
						}),
				reader : new Ext.data.JsonReader({
						}, [{
							name : 'id'
						}, {
							name : 'text'
						}])
			});
	stationLestore.load();
    // 变更前执行岗级
	var txtOldCheckS = new Ext.form.ComboBox({
		fieldLabel : '变更前执行岗级',
		name : 'oldCheckStationGrade',
		id : 'oldCheckStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'salaryBean.oldCheckStationGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		selectOnFocus : true,
		disabled : true,
		hideTrigger : true
	});

    // 变更后执行岗级
    var txtNewCheckS = new Ext.form.ComboBox({
		fieldLabel : "变更后执行岗级<font color='red'>*</font>",
		name : 'newCheckStationGrade',
		id : 'newCheckStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'salaryBean.newCheckStationGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true,
		allowBlank : false
	});
    // 变更前标准岗级
    var txtOldStationG = new Ext.form.ComboBox({
		fieldLabel : '变更前标准岗级',
		name : 'oldStationGrade',
		id : 'oldStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'salaryBean.oldStationGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		selectOnFocus : true,
		disabled : true,
		hideTrigger : true
	});

    // 变更后标准岗级
    var txtNewStationG = new Ext.form.ComboBox({
		fieldLabel : "变更后标准岗级<font color='red'>*</font>",
		name : 'newStationGrade',
		id : 'newStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'salaryBean.newStationGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true,
		allowBlank : false
	});
    
    // 薪级store 
	var salaryLevelStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getSalaryLevelStore'
						}),
				reader : new Ext.data.JsonReader({
						}, [{
							name : 'id'
						}, {
							name : 'text'
						}])
			});
	salaryLevelStore.load();
	// 变更前薪级
	var txtOldSalaryG = new Ext.form.ComboBox({
		fieldLabel : '变更前薪级',
		name : 'oldSalaryGrade',
		id : 'oldSalaryGrade',
		store : salaryLevelStore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'salaryBean.oldSalaryGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		selectOnFocus : true,
		disabled : true,
		hideTrigger : true
	});
    

    // 变更后薪级
    var txtNewSalaryG = new Ext.form.ComboBox({
		fieldLabel : "变更后薪级<font color='red'>*</font>",
		name : 'newSalaryGrade',
		id : 'newSalaryGrade',
		store : salaryLevelStore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		hiddenName : 'salaryBean.newSalaryGrade',
		editable : false,
		triggerAction : 'all',
		width : 85,
		blankText : '请选择',
		emptyText : '请选择',
		allowBlank : false
	});
    // 变动类别
    var adjustTypeCm = new Ext.form.CmbHRCode({
        fieldLabel : '变动类别<font color ="red">*</font>',
        type : "变动类别",
        width : 102,
        id : 'adjustType',
        name : 'salaryBean.adjustType',
        value : '1',
        allowBlank : false
    });

    // 岗薪变化类别
    var stationChangeTypeCm = new Ext.form.CmbHRCode({
        fieldLabel : '岗薪变化类别<font color ="red">*</font>',
        type : "岗薪变化类别",
        width : 100,
        id : 'stationChangeType',
        name : 'salaryBean.stationChangeType',
        value : '1',
        allowBlank : false
    });
    // 原因
    var txaReasonAddr = new Ext.form.TextArea({
        id : "reason",
        name : "salaryBean.reason",
        fieldLabel : "原因",
        maxLength : 200,
        height : 44,
        width : 323
    });
    // 备注
    var txaMemoAddr = new Ext.form.TextArea({
        id : "memo",
        name : "salaryBean.memo",
        fieldLabel : "备注",
        maxLength : 127,
        height : 44,
        width : 323
    });

    // 新增修改窗口布局
    var fs = new Ext.Panel({
        height : "100%",
        width : 450,
        layout : "form",
        border : false,
        autoScroll : true,
        buttonAlign : "center",
        style : "padding-top:3px;padding-right:0px;padding-bottom:0px;margin-bottom:3px",
        items : [
            // 第一行
            {
            border : false,
            layout : "column",
            items : [{
                    columnWidth : 0.5,
                    border : false,
                    layout : "form",
                    items : [hdnId, hdnUpdateTime, staffName]
                }, {
                    columnWidth : 0.5,
                    border : false,
                    layout : "form",
                    items : [txtDoDate]
                }]
        },
            // 第二行
            txtDepName,
            // 第三行
            {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtOldCheckS]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label1]
                    }, {
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtNewCheckS]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label2]
                    }]
            },
            // 第三行
            {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtOldStationG]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label3]
                    }, {
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtNewStationG]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label4]
                    }]
            },
            // 第四行
            {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtOldSalaryG]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label5]
                    }, {
                        columnWidth : 0.45,
                        border : false,
                        layout : "form",
                        items : [txtNewSalaryG]
                    }, {
                        columnWidth : 0.05,
                        border : false,
                        layout : "form",
                        items : [label6]
                    }]
            },
            // 第五行
            {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.5,
                        border : false,
                        layout : "form",
                        items : [adjustTypeCm]
                    }, {
                        columnWidth : 0.5,
                        border : false,
                        layout : "form",
                        items : [stationChangeTypeCm]
                    }]
            }, txaReasonAddr, txaMemoAddr]
    });

    // 增加或修改面板
    var fp = new Ext.form.FormPanel({
        autoScroll : true,
        id : "form",
        labelAlign : "right",
        labelWidth : 110,
        frame : true,
        items : [fs]
    });
    // 编辑窗口
    var win = new Ext.Window({
        modal : true,
        width : 500,
        height : 320,
        layout : 'fit',
        closeAction : "hide",
        resizable : false,
        autoScroll : true,
        buttonAlign : 'center',
        items : [fp],
        buttons : [{
                // 保存按钮
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : save
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                // 取消按钮直接关闭窗口
                handler : function() {
                    Ext.MessageBox.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    });
                }
            }]
    });
    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHander
    });
    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addHander
    });

    // 修改按钮
    var editBtn = new Ext.Button({
        id : 'edit',
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateHander
    });

    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : deleteHander
    });
    // 上报按钮
    var repoetBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        handler : repoetHander
    });
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : queryStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });
    // load前传查询条件
    queryStore.on('beforeload', function() {
        Ext.apply(this.baseParams, {
            strStartDate : txtStartDate.getValue(),
            // 结束日期
            strEndDate : txtEndDate.getValue(),
            // 部门id
            strDeptCode : hiddenMrDept.getValue(),
            // 单据状态
            strDcmStatus : statusField.getValue()
        });
    });
    var headerTbar = new Ext.Toolbar({
        region : "north",
        border : false,
        height : 25,
        items: ['所属部门:', txtDept, '-', "起薪日期:", txtStartDate, " ~ ", txtEndDate, '-', '单据状态:', statusField]
    });
    // 初始化grid
    myLoad();
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
                header : "薪酬变动单id",
                hidden : true,
                sortable : true,
                dataIndex : 'salayradjustid'
            }, {
                header : "员工姓名",
                sortable : true,
                width : 60,
                dataIndex : 'chsName'
            }, {
                header : "起薪日期",
                width : 75,
                sortable : true,
                dataIndex : 'doDate'
            }, {
                header : "所属部门",
                width : 100,
                sortable : true,
                dataIndex : 'deptName'
            }, {
                header : "变更前执行岗级",
                width : 120,
                align : 'right',
                sortable : true,
                dataIndex : 'oldCheckStationGrade',
                renderer : function(value) {
                	return stationLevelFormat(value);
                }
            }, {
                header : "变更后执行岗级",
                width : 120,
                align : 'right',
                sortable : true,
                dataIndex : 'newCheckStationGrade',
                renderer : function(value) {
                    return stationLevelFormat(value);
                }
            }, {
                header : "变更前标准岗级",
                width : 120,
                sortable : true,
                align : 'right',
                dataIndex : 'oldStationGrade',
                renderer : function(value) {
                    return stationLevelFormat(value);
                }
            }, {
                header : "变更后标准岗级",
                width : 120,
                sortable : true,
                align : 'right',
                dataIndex : 'newStationGrade',
                renderer : function(value) {
                    return stationLevelFormat(value);
                }
            }, {
                header : "变更前薪级",
                width : 100,
                sortable : true,
                align : 'right',
                dataIndex : 'oldSalaryGrade',
                renderer : function(value) {
                    return salaryLevelFormat(value);
                }
            }, {
                header : "变更后薪级",
                width : 100,
                sortable : true,
                align : 'right',
                dataIndex : 'newSalaryGrade',
                renderer : function(value) {
                    return salaryLevelFormat(value);
                }
            }, {
                header : "变动类别",
                width : 100,
                sortable : true,
                dataIndex : 'adjustType',
                renderer : function(value) {
                    if (value == Constants.ADJUST_TYPE_TRANSFER) {
                        // 变动类别 (调动)
                        value = Constants.ADJUST_TYPE_TRANSFER_MESSAGE;
                    } else if (value == Constants.ADJUST_TYPE_PERFORM) {
                        // 变动类别 (绩效变动)
                        value = Constants.ADJUST_TYPE_PERFORM_MESSAGE;
                    } else if (value == Constants.ADJUST_TYPE_REWARDS_PUNISH) {
                        // 变动类别 (奖惩违纪)
                        value = Constants.ADJUST_TYPE_REWARDS_PUNISH_MESSAGE;
                    } else if (value == Constants.ADJUST_TYPE_ELSE) {
                        // 变动类别 (其它)
                        value = Constants.ADJUST_TYPE_ELSE_MESSAGE
                    }
                    return value;
                }

            }, {
                header : "岗薪变化类别",
                width : 100,
                sortable : true,
                dataIndex : 'stationChangeType',
                renderer : function(value) {
                    if (value == Constants.STATION_MOVE_TYPE_UP) {
                        // 岗位调动类别 (低岗至高岗)
                        value = Constants.STATION_MOVE_TYPE_UP_MESSAGE;
                    } else if (value == Constants.STATION_MOVE_TYPE_DOWN) {
                        // 岗位调动类别 (高岗至低岗)
                        value = Constants.STATION_MOVE_TYPE_DOWN_MESSAGE;
                    } else if (value == Constants.STATION_MOVE_TYPE_LATERAL) {
                        // 岗位调动类别 (平级调动)
                        value = Constants.STATION_MOVE_TYPE_LATERAL_MESSAGE;
                    }
                    return value;
                }
            }, {
                header : "单据状态",
                width : 100,
                sortable : true,
                dataIndex : 'dcmState',
                renderer : function(value) {
                    if (value == Constants.NOT_REPORT) {
                        // 未上报
                        value = Constants.NOT_REPORT_MESSAGE;
                    } else if (value == Constants.ALREADY_REPORT) {
                        // 已上报
                        value = Constants.ALREADY_REPORT_MESSAGE;
                    } else if (value == Constants.ALREADY_OVER) {
                        // 已终结
                        value = Constants.ALREADY_OVER_MESSAGE;
                    } else if (value == Constants.ALREADY_RETURN) {
                        // 已退回
                        value = Constants.ALREADY_RETURN_MESSAGE
                    }
                    return value;
                }
            }, {
                header : "原因",
                width : 100,
                sortable : true,
                dataIndex : 'reason'
            }, {
                header : "备注",
                width : 100,
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : "上次修改时间",
                width : 100,
                sortable : true,
                dataIndex : 'updateTime',
                hidden : true
            }],
        // 增加，修改，删除栏
        tbar : [queryBtn, addBtn, editBtn, deleteBtn, repoetBtn],
        // 分页
        bbar : pagebar
    });
    // 双击处理
    grid.on('rowdblclick', updateHander);
    // ----------主画面-----------------

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [headerTbar,grid]
    });
    // -------------------------------
    /**
     * 必须输入项check
     * 
     */
    function checkIsNull() {
        var nullMsg = "";
        // 判断员工姓名是否为空
        if (Ext.get("chsName").dom.value == null || Ext.get("chsName").dom.value == "") {
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "员工姓名");
            // staffName.markInvalid();
        }

        // 判断起薪日期是否为空
        if (Ext.get("doDate").dom.value == null || Ext.get("doDate").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "起薪日期");
            // txtDoDate.markInvalid();
        }

        // 判断变更后执行岗级是否为空
        if (Ext.get("salaryBean.newCheckStationGrade").dom.value == null
        || Ext.get("salaryBean.newCheckStationGrade").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "变更后执行岗级");
            // txtNewCheckS.markInvalid();
        }

        // 判断变更后标准岗级是否为空
        if (Ext.get("newStationGrade").dom.value == null || Ext.get("newStationGrade").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "变更后标准岗级");
            // txtNewStationG.markInvalid();
        }

        // 判断变更后薪级是否为空
        if (Ext.get("newSalaryGrade").dom.value == null || Ext.get("newSalaryGrade").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_002, "变更后薪级");
            // txtNewSalaryG.markInvalid();
        }

        // 判断变动类别是否选择
        if (Ext.get("adjustType").dom.value == null || Ext.get("adjustType").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "变动类别");
            // adjustTypeCm.markInvalid();
        }

        // 判断岗薪变化类别是否选择
        if (Ext.get("stationChangeType").dom.value == null || Ext.get("stationChangeType").dom.value == "") {
            if (nullMsg != "") {
                nullMsg = nullMsg + "<br/>";
            }
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "岗薪变化类别");
            // stationChangeTypeCm.markInvalid();
        }
        // 如果为空显示错误信息，并返回false,不为空则返回true
        if (nullMsg != "") {
            Ext.Msg.alert(Constants.ERROR, nullMsg);
            return false;
        } else
            return true;
    }
    /**
     * 保存按钮处理函数
     * 
     */
    function save() {
        if (checkIsNull()) {
            Ext.MessageBox.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(button, text) {
                if (button == "yes") {
                    if (!checkLength())
                        return;
                    // 为几个渲染的字段取值，然后以param方式传给后台
                    /** 员工id */
                    var empId = hiddenId.getValue();
                    /** 起薪日期 */
                    var doDate = txtDoDate.getValue();
                    /** 变更前执行岗级 */
                    var oldCheckStationGrade = txtOldCheckS.getValue();
                    /** 变更后执行岗级 */
                    var newCheckStationGrade = txtNewCheckS.getValue();
                    /** 变更前标准岗级 */
                    var oldStationGrade = txtOldStationG.getValue();
                    /** 变更后标准岗级 */
                    var newStationGrade = txtNewStationG.getValue();
                    /** 变更前薪级 */
                    var oldSalaryGrade = txtOldSalaryG.getValue();
                    /** 变更后薪级 */
                    var newSalaryGrade = txtNewSalaryG.getValue();
                    /** 变动类别 */
                    var adjustType = adjustTypeCm.getValue();
                    /** 岗薪变化类别 */
                    var stationChangeType = stationChangeTypeCm.getValue();
                    /** 原因 */
                    var reason = txaReasonAddr.getValue();
                    /** 备注 */
                    var memo = txaMemoAddr.getValue();

                    fp.getForm().submit({
                        url : "hr/" + (hdnId.getValue() == "" ? "addSalaryRecord" : "updateSalaryRecord") + ".action",
                        method : "post",
                        params : {
                            /** 员工id */
                            empId : empId,
                            /** 变更前执行岗级 */
                            oldCheckStationGrade : oldCheckStationGrade,
                            /** 变更后执行岗级 */
                            newCheckStationGrade : newCheckStationGrade,
                            /** 变更前标准岗级 */
                            oldStationGrade : oldStationGrade,
                            /** 变更后标准岗级 */
                            newStationGrade : newStationGrade,
                            /** 变更前薪级 */
                            oldSalaryGrade : oldSalaryGrade,
                            /** 变更后薪级 */
                            newSalaryGrade : newSalaryGrade,
                            /** 变动类别 */
                            adjustType : adjustType,
                            /** 岗薪变化类别 */
                            stationChangeType : stationChangeType,
                            /** 设置岗位调动单ID */
                            stationremoveid : 0
                        },
                        success : function(form, action) {
                            var result = eval('(' + action.response.responseText + ')');
                            if ((result.msg != null) && (result.msg == Constants.SQL_FAILURE)) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            if ((result.msg != null) && (result.msg == 'addSuccessful')) {
                                myLoad();
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    win.hide();
                                });
                                grid.getView().refresh();
                                return;
                            }
                            // 员工重复的时候
                            if ((result.msg != null) && (result.msg == 'empRepeat')) {
                                Ext.Msg.alert(Constants.ERROR, String.format(Constants.PE004_E_001, staffName
                                .getValue()));
                                return;
                            }
                            if ((result.msg != null) && (result.msg == 'report')) {
                                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "单据状态"));
                                return;
                            }
                            if ((result.msg != null) && (result.msg == 'changeSuccessful')) {
                                myLoad();
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    win.hide();
                                });
                                grid.getView().refresh();
                                return;
                            }
                            // 排他异常
                            if (result.msg == "U") {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_E_015);
                            }

                        },
                        failure : function(form, action) {
                        }
                    });
                }
            });
        }
    }
    /**
     * 新增按钮处理函数
     */
    function addHander() {
        win.setTitle("新增薪酬变动单");
        fp.getForm().reset();
        win.x = undefined;
        win.y = undefined;
        win.show();
        staffName.flag = true;
        staffName.disabled = false;
        Ext.get("chsName").dom.disabled = false;
        hdnId.setValue("");

    }

    /**
     * 修改按钮处理函数
     */
    function updateHander() {
        // 判断是否选择了一行
        if (grid.selModel.hasSelection()) {
            // 已选择
            var selectedRow = grid.selModel.getSelected();
            hdnId.setValue(selectedRow.get("salayradjustid"));
            // check 是否已经上报
            var dcmState = selectedRow.get("dcmState");
            if (dcmState == Constants.ALREADY_REPORT) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_REPORT_MESSAGE,
                "修改"));
                return;
            }
            if (dcmState == Constants.ALREADY_OVER) {
                Ext.Msg
                .alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_OVER_MESSAGE, "修改"));
                return;
            }
            // 为不能submit的字段亲自赋值
            /** 员工Id */
            hiddenId.setValue(selectedRow.get("empId"));
            /** 薪酬申报单id */
            hdnId.setValue(selectedRow.get("salayradjustid"));
            win.x = undefined;
            win.y = undefined;
            win.show();
            fp.getForm().loadRecord(selectedRow);
            win.setTitle("修改薪酬变动单");
            staffName.flag = false;
            Ext.get("chsName").dom.disabled = true;

        } else {
            // 为选择时报提示message
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }

    }

    /**
     * 删除按钮处理函数
     */
    function deleteHander() {
        // 判断是否选择了一行
        if (grid.selModel.hasSelection()) {
            // check 是否已经上报
            var selectedRow = grid.selModel.getSelected();
            var dcmState = selectedRow.get("dcmState");
            // 如果已上报，不允许删除
            if (dcmState == Constants.ALREADY_REPORT) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_REPORT_MESSAGE,
                "删除"));
                return;
            }
            if (dcmState == Constants.ALREADY_OVER) {
                Ext.Msg
                .alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_OVER_MESSAGE, "删除"));
                return;
            }
            // 已选择
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = grid.selModel.getSelected();
                    // 获得记录的申请单号
                    var salaryId = record.get("salayradjustid");
                    // 获得该记录被读出时的修改时间
                    var updateTime = record.get("updateTime");
                    Ext.Ajax.request({
                        method : "post",
                        url : 'hr/deleteSalaryRecord.action',
                        success : function(result, request) {
                            // 成功，显示删除成功信息
                            var o = eval("(" + result.responseText + ")");
                            // 排他异常
                            if (o.msg == "U") {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_E_015);
                                // 数据库异常
                            } else if (o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                // 已上报或已终结
                            } else if (o.msg == "report") {
                                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "单据状态", "删除"));
                                // 删除成功
                            } else {
                                myLoad();
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        },
                        params : {
                            salaryId : salaryId,
                            updateTime : updateTime
                        }
                    });
                }
            });
        } else {
            // 为选择一行时，报提示message
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 上报按钮处理函数
     */
    function repoetHander() {
        // 判断是否选择了一行
        if (grid.selModel.hasSelection()) {
            // check 是否已经上报
            var selectedRow = grid.selModel.getSelected();
            var dcmState = selectedRow.get("dcmState");
            // 如果已上报，不允许删除
            if (dcmState == Constants.ALREADY_REPORT) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_REPORT_MESSAGE,
                "上报"));
                return;
            }
            if (dcmState == Constants.ALREADY_OVER) {
                Ext.Msg
                .alert(Constants.ERROR, String.format(Constants.COM_E_032, Constants.ALREADY_OVER_MESSAGE, "上报"));
                return;
            }
            // 已选择
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_006, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = grid.selModel.getSelected();
                    // 获得记录的申请单号
                    var salaryId = record.get("salayradjustid");
                    // 获得该记录被读出时的修改时间
                    var updateTime = record.get("updateTime");
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/reportSalaryRecord.action',
                        success : function(result, request) {
                            // 成功，显示上报成功信息
                            var o = eval("(" + result.responseText + ")");
                            // 排他异常
                            if (o.msg == "U") {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_E_015);
                                // 数据库异常
                            } else if (o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                // 已上报或已终结
                            } else if (o.msg == "report") {
                                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_032, "单据状态", "上报"));
                                // 上报成功
                            } else {
                                myLoad();
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_007);
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        },
                        params : {
                            salaryId : salaryId,
                            updateTime : updateTime
                        }
                    });
                }
            });
        } else {
            // 未选择
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 查询按钮处理函数
     */
    function queryHander() {
        queryStore.load({
            params : {
                // 条件查询标志
                strflag : 1,
                // 起薪日期
                strStartDate : txtStartDate.getValue(),
                // 结束日期
                strEndDate : txtEndDate.getValue(),
                // 部门id
                strDeptCode : hiddenMrDept.getValue(),
                // 单据状态
                strDcmStatus : statusField.getValue(),
                // 分页信息
                start : 0,
                limit : Constants.PAGE_SIZE
            },
            callback : function() {
                if (queryStore.getCount() > 0) {
                    // 设置为可用
                } else {
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                    // 如果检索数据为空，设置导出按钮不可用

                }
            }
        })
    }

    /**
     * 人员选择页面
     */
    function selectName2() {
        if (staffName.flag) {
            var args = {
                selectModel : 'single',
                notIn : "'999999'",
                rootNode : {
                    id : '0',
                    text : '合肥电厂'
                }
            }
            var object = window.showModalDialog('../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
            'dialogWidth:550px;dialogHeight:320px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
            // 根据返回值设置画面的值
            if (object) {
                if (typeof(object.empId) != "undefined") {
                    // 员工code不为空时，搜索此员工相关其他信息
                    if (object.empId != "") {
                        // 根据code搜索此人员的相关信息
                        Ext.Ajax.request({
                            url : "hr/searchSalaryByEmployeeQuery.action",
                            method : "post",
                            params : {
                                // 员工code
                                empId : object.empId
                            },
                            success : function(result, request) {
                                var data = eval("(" + result.responseText + ")");
                                if ((result.msg != null) && (result.msg == Constants.SQL_FAILURE)) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    return;
                                }
                                // 原执行岗级
                                txtOldCheckS.setValue(data.checkStationGrade);
                                // 原标准岗级
                                txtOldStationG.setValue(data.stationGrade);
                                // 员薪级
                                txtOldSalaryG.setValue(data.salaryGrade);
                            },
                            failure : function(result, request) {
                            }
                        });
                        // 员工信息为空时，各控件附空串
                    } else {
                        txtOldCheckS.setValue("");
                        txtOldStationG.setValue("");
                        txtOldSalaryG.setValue("");
                        hiddenId.setValue("");
                    }
                    // 设置员工名称
                    staffName.setValue(object.workerName);
                    // 员工id
                    hiddenId.setValue(object.empId)
                }
                if (typeof(object.deptName) != "undefined") {
                    // 设置员工部门
                    txtDepName.setValue(object.deptName);
                }
            }
        }
    }

    /** 检查数据长度 */
    function checkLength() {
        // 如果长度益处,则返回false,否则返回true
        if (txaReasonAddr.isValid() && txaMemoAddr.isValid() && txaMemoAddr.isValid() && txtNewCheckS.isValid()
        && txtNewStationG.isValid() && txtNewSalaryG.isValid()) {
            return true;
        } else
            return false;

    }
    /** 重载数据 */
    function myLoad() {
        // 刷新grid数据
        queryStore.load({
            params : {
                // 条件查询标志
                // strflag : 1,
                // 起薪日期
                strStartDate : txtStartDate.getValue(),
                // 结束日期
                strEndDate : txtEndDate.getValue(),
                // 部门id
                strDeptCode : hiddenMrDept.getValue(),
                // 单据状态
                strDcmStatus : statusField.getValue(),
                // 分页信息
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
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
        v = whole;
        return v;
    }
    
    function stationLevelFormat(value)
    {
    	if(value != null && value != '')
    	{
    		if(stationLestore.getTotalCount() > 0)
    		{
    			for(var i = 0; i <= stationLestore.getTotalCount() -1 ;i++)
    			{
    				if(stationLestore.getAt(i).get('id') == value)
    				return stationLestore.getAt(i).get('text')
    			}
    		}
    	}
    }
 	function salaryLevelFormat(value)
 	{
 		if(value != null && value != '')
    	{
    		if(salaryLevelStore.getTotalCount() > 0)
    		{
    			for(var i = 0; i <= salaryLevelStore.getTotalCount() -1 ;i++)
    			{
    				if(salaryLevelStore.getAt(i).get('id') == value)
    				return salaryLevelStore.getAt(i).get('text')
    			}
    		}
    	}
 	}
});