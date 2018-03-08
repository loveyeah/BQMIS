Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();

// checkbox控件
Ext.grid.CheckColumn = function(config) {
    Ext.apply(this, config);
    if (!this.id) {
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
    init : function(grid) {
        this.grid = grid;
        this.grid.on('render', function() {
            var view = this.grid.getView();
        }, this);
    },
    // 是则选中，否则不选中
    renderer : function(v, p, record) {
        v = v ? v : false;
        if (v != null) {
            if (v.toString() == "1") {
                v = true;
            } else if (v.toString() == "0") {
                v = false;
            }
        } else {
            v = false;
        }
        p.css += ' x-grid3-check-col-td';
        var sm = this.grid.getSelectionModel();
        return '<div class="x-grid3-check-col' + (v ? '-on' : '') + ' x-grid3-cc-' + this.id + '">&#160;</div>';
    }
};
Ext.onReady(function() {
    /** 考勤检索件数为0时，报错 */
    var attStdNull = "E";
    /**
     * 开始日期
     */
    var strStartDate;
    /**
     * 结束日期
     */
    var strEndDate;
    /**
     * 画面flag
     */
    var checkFlag;
    /**
     * 上班休息flag
     */
    var workOrRest;
    /**
     * 考勤部门ID
     */
    var attendanceDeptId;
    /**
     * 颜色
     */
    var color = '';
    /**
     * 查询条件保存对象
     */
    var queryObj = new Object();
    /** 审核年月 */
    var examineDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        value : new Date().format("Y-m"),
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM',
                    alwaysUseStartDate : true,
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
                this.blur();
            }
        }
    });
    var storeDept = new Ext.data.JsonStore({
        root : 'list',
        url : "ca/getAttendanceDeptInfoAll.action",
        fields : ['attendanceDeptId', 'attendanceDeptName']
    })
    storeDept.load();
    // 审核部门
    var examineDept = new Ext.form.ComboBox({
        readOnly : true,
        triggerAction : 'all',
        allowBlank : false,
        store : storeDept,
        mode : 'local',
        valueNotFoundText : '',
        displayField : 'attendanceDeptName',
        valueField : 'attendanceDeptId'
    })
    /** 查询按钮 */
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : function() {
            queryAll();
        }
    });
    /** 审核记录查询 */
    var btnExamineQuery = new Ext.Button({
        text : "审核记录查询",
        iconCls : Constants.CLS_RECORD_QUERY,
        handler : function() {
            stoAudite.sortInfo = null;
            stoAudite.removeAll();
            if (gridAudite.rendered) {
                gridAudite.getView().removeSortIcon();
            }
            stoAudite.load({
                params : {
                    examineDate : examineDate.getValue(),
                    examineDept : examineDept.getValue()
                },
                callback : function() {
                    winAudite.show();
                }
            })
        }
    });
    /** 导出 */
    var btnExport = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        handler : function() {
            Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_007, function(button, text) {
                if (button == "yes") {
                    document.all.blankFrame.src = "ca/exportFileAllAttendance.action";
                }
            })
        }
    });
    // 打印
    var btnPrint = new Ext.Button({
        text : Constants.BTN_PRINT,
        iconCls : Constants.CLS_PRINT,
        handler : function() {
            window.open("/power/report/webfile/hr/ca/KQ012_allAttendanceQuery.jsp?checkDate=" + queryObj.queryDate
             + "&checkDeptId=" + queryObj.queryDept);
        }
    });
    // 取消
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
        	Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 职工考勤记录明细为空
		            storeMain.removeAll();
		            // 审核部门，审核人，考勤员，审核年月清空
		            gExamineDept.setValue("");
		            gExamineEmp.setValue("");
		            gExamineDate.setValue("");
		            gTimeKeeper.setValue("");
		            // 查询按钮可用，审核记录查询、导出按钮、打印按钮不可用，审核年月和审核部门可用
		            btnQuery.enable();
		            btnExamineQuery.disable();
		            btnExport.disable();
		            btnPrint.disable();
		            examineDate.enable();
		            examineDept.enable();
		            gridMain.getColumnModel().setConfig([]);
                }
            });
            
        }
    });
    // 头部工具栏
    var topBar = new Ext.Toolbar({

        items : ["审核年月<font color='red'>*</font>:", examineDate, "-", "审核部门<font color='red'>*</font>:", examineDept,
            "-", btnQuery, btnExamineQuery, btnExport, btnPrint, btnCancel]
    })

    /** 定义主画面store */
    var storeMain = new Ext.data.JsonStore({
        root : 'list',
        url : ''
    });
    // 定义考勤审核store
    var storeCheck = new Ext.data.JsonStore({
        root : 'list',
        url : '',
        fields : ['examineADept', 'examineMan1', 'examineDate1', 'examineMan2', 'examineDate2', 'examineType',
            'examineDeptTop', 'examineBDeptName', 'examineBDeptId', 'examineRegisterMan', 'man1ChsName', 'examineMan',
            'man2ChsName', 'cancelFlag']
    })
    storeMain.on('metachange', function(store, meta) {
        var item;
        for (var i = 0; i < meta.fields.length; i++) {
            item = meta.fields[i];
            if (item.renderer) {
                // 解析renderer
                item.renderer = eval('(' + item.renderer + ')');
            }
            if (item.sortable) {
                // 解析renderer
                item.sortable = eval('(' + item.sortable + ')');
            }
        }
        meta.fields.splice(0, 1, new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }));
        gridMain.getColumnModel().setConfig(meta.fields);
    }   );
    var titleLabel = new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region : 'north',
        height : 35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text : '职工考勤记录表',
            style : 'font-size:22px;font-family: 黑体'
        })]
    });
    var gExamineDept = new Ext.form.TextField({
        name : 'deptName',
        disabled : true,
        width : 120
    })
    var gExamineEmp = new Ext.form.TextField({
        name : 'examineEmpName',
        disabled : true,
        width : 120
    })
    var gTimeKeeper = new Ext.form.TextField({
        name : 'timeEmpName',
        disabled : true,
        width : 120
    })
    var gExamineDate = new Ext.form.TextField({
        name : 'examineDate',
        disabled : true,
        width : 120
    })
    var gridBar = new Ext.Toolbar({
        items : ["审核部门:", gExamineDept, "-", "审核人:", gExamineEmp, "-", "考勤员:", gTimeKeeper, "-", "审核年月:", gExamineDate]

    })
    // 定义gird
    var gridMain = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : storeMain,
        cm : new Ext.grid.ColumnModel([]),
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),

        // 头部工具栏
        tbar : gridBar,
        enableColumnMove : false,
        enableColumnHide : true
    });

    gridMain.on('rowdblclick', function() {
        // 如果有行选中
        if (gridMain.selModel.hasSelection()) {
            var record = gridMain.getSelectionModel().getSelected();
            // 清空原store
            gridStore.removeAll();
            // 清空审核人，审核日期
            txtCheckMan.setValue("");
            txtCheckDate.setValue("");
            empAttendanceQueryWin.x = undefined;
            empAttendanceQueryWin.y = undefined;
            empAttendanceQueryWin.show();

            // 设置员工名称
            txtEmpName.setValue(record.data['empName']);
            empId = record.data['empId'];
            // 设置部门名称
            txtEmpDept.setValue(record.data['deptName']);
            for (i = 0; i < storeCheck.getCount(); i++) {
                // 设置审核人，审核日期
                var r = storeCheck.getAt(i);
                if (r.data['examineADept'] == record.data['attendanceDeptId']) {
                    if (r.data['man2ChsName'] != null && r.data['man2ChsName'] != '') {
                        txtCheckMan.setValue(r.data['man2ChsName']);
                        txtCheckDate.setValue(r.data['examineDate2']);
                    } else {
                        txtCheckMan.setValue(r.data['man1ChsName']);
                        txtCheckDate.setValue(r.data['examineDate1']);
                    }
                    break;
                } else {
                    continue;
                }
            }
            // 加载数据
            loadDetail(record);
        }
    });

    var fullPanel = new Ext.Panel({
        tbar : topBar,
        layout : 'border',
        border : false,
        items : [titleLabel, gridMain]
    });

    // 设置审核记录查询、导出和打印按钮不可用
    btnExamineQuery.disable();
    btnExport.disable();
    btnPrint.disable();
    // <<<<<<<<<<<<<<职工考勤记录查询
    // 控件类型: 选择
    var TYPE_SELECT = "S";
    // 控件类型: 输入
    var TYPE_INPUT = "I";

    // 保存按钮
    var saveBtn = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        disabled : true
    });

    // 退出按钮
    var quitBtn = new Ext.Button({
        text : "退出",
        iconCls : Constants.CLS_EXIT,
        handler : function() {
            empAttendanceQueryWin.hide();
        }
    });

    // 头部信息: 员工姓名
    var txtEmpName = new Ext.form.TextField({
        fieldLabel : '员工姓名',
        width : 100,
        disabled : true
    });

    // 头部信息: 所属部门
    var txtEmpDept = new Ext.form.TextField({
        fieldLabel : '所属部门',
        width : 100,
        disabled : true
    });
    // 头部信息: 审核人
    var txtCheckMan = new Ext.form.TextField({
        fieldLabel : '审核人',
        width : 100,
        disabled : true
    });
    // 头部信息: 审核日期
    var txtCheckDate = new Ext.form.TextField({
        fieldLabel : '审核日期',
        width : 100,
        disabled : true
    });

    // 头部信息
    var infoFs = new Ext.form.FormPanel({
        border : false,
        region : 'north',
        labelAlign : 'right',
        labelWidth : 70,
        height : 35,
        layout : 'form',
        items : [{
                xtype : 'panel',
                style : "padding-top:8px;padding-bottom:8px;border:0px;margin:0px;text-align:center;",
                border : false,
                items : [new Ext.form.Label({
                    text : '职工考勤记录表',
                    style : 'font-size:22px;font-family: 黑体'
                })]
            }]
    });

    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([
        {
        // 考勤日期
        name : 'id.attendanceDate'
        },
        // 考勤登记表
        {
            // 上午上班时间
            name : 'amBegingTime'
        }, {
            // 上午下班时间
            name : 'amEndTime'
        }, {
            // 下午上班时间
            name : 'pmBegingTime'
        }, {
            // 下午下班时间
            name : 'pmEndTime'
        }, {
            // 运行班类别ID
            name : 'workShiftId'
        }, {
            // 假别ID
            name : 'vacationTypeId'
        }, {
            // 加班类别ID
            name : 'overtimeTypeId'
        }, {
            // 休息
            name : 'restType'
        }, {
            // 旷工
            name : 'absentWork'
        }, {
            // 出差
            name : 'evectionType'
        }, {
            // 外借
            name : 'outWork'
        }, {
            // 备注
            name : 'memo'
        }
        // add by liuyi 20100203
        // 加班时间id 
    ,{
    	name : 'overtimeTimeId'
    }
    //病假时间Id
    ,{
    	name : 'sickLeaveTimeId'
    }
    // 事假时间Id
    ,{
    	name : 'eventTimeId'
    }
    // 旷工时间Id
    ,{
    	name : 'absentTimeId'
    } // 其他假时间id
    ,{
    	name : 'otherTimeId'
    }
    ]);

    // 定义运行班种类的store
    var workshiftStore = new Ext.data.JsonStore({
        root : 'list',
        url : 'ca/getWorkshiftTypeCommon.action',
        fields : ['workShiftId', 'workShift']
    });

    // 定义请假种类的store
    var vacationStore = new Ext.data.JsonStore({
        root : 'list',
        url : 'ca/getVacationTypeCommon.action',
        fields : ['vacationTypeId', 'vacationType']
    });

     // add by liuyi 20100202 定义基础天数store
    var basicDaysStore = new Ext.data.JsonStore({
    	root : 'list',
    	url : 'ca/getBasicDaysCommon.action',
    	fields : ['id','baseDays']
    })
    
    // 定义加班种类的store
    var overtimeStore = new Ext.data.JsonStore({
        root : 'list',
        url : 'ca/getOvertimeTypeCommon.action',
        fields : ['overtimeTypeId', 'overtimeType']
    });

    // 休息checkbox
    var resetCheckColumn = new Ext.grid.CheckColumn({
        header : '休息',
        dataIndex : 'restType',
        width : 35
    });

    // 旷工checkbox
    var absentCheckColumn = new Ext.grid.CheckColumn({
        header : '旷工',
        dataIndex : 'absentWork',
        width : 35
    });

    // 出差checkbox
    var evectionCheckColumn = new Ext.grid.CheckColumn({
        header : '出差',
        dataIndex : 'evectionType',
        width : 35
    });

    // 外借checkbox
    var outCheckColumn = new Ext.grid.CheckColumn({
        header : '外借',
        dataIndex : 'outWork',
        width : 35
    });

    // grid store
    var gridStore = new Ext.data.JsonStore({
        url : 'ca/getEmpAttendance.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
    });

    // 一览grid
    var gridDetail = new Ext.grid.EditorGridPanel({
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
            }),
            {
                header : '考勤日期',
                width : 70,
                sortable : true,
                dataIndex : 'id.attendanceDate',
                renderer : function(value) {
                    value = value + "";
                    var reg = /(\d{4}-\d{2}-\d{2})T\d{2}:\d{2}:\d{2}/gi;
                    return value.replace(reg, "$1");
                }
            }, {
                header : '上午上班',
                width : 60,
                sortable : true,
                dataIndex : 'amBegingTime'
            }, {
                header : '上午下班',
                width : 60,
                sortable : true,
                dataIndex : 'amEndTime'
            }, {
                header : '下午上班',
                width : 60,
                sortable : true,
                dataIndex : 'pmBegingTime'
            }, {
                header : '下午下班',
                width : 60,
                sortable : true,
                dataIndex : 'pmEndTime'
            }, {
                header : '运行班',
                width : 70,
                sortable : true,
                dataIndex : 'workShiftId',
                editor : new Ext.form.ComboBox({
                    readOnly : true,
                    triggerAction : 'all',
                    store : workshiftStore,
                    mode : 'local',
                    valueNotFoundText : '',
                    displayField : 'workShift',
                    valueField : 'workShiftId',
                    listClass : 'x-combo-list-small'
                }),
                renderer : getWorkShift
            }, {
                header : '加班',
                width : 70,
                sortable : true,
                dataIndex : 'overtimeTypeId',
                editor : new Ext.form.ComboBox({
                    readOnly : true,
                    triggerAction : 'all',
                    store : overtimeStore,
                    mode : 'local',
                    valueNotFoundText : '',
                    displayField : 'overtimeType',
                    valueField : 'overtimeTypeId',
                    listClass : 'x-combo-list-small'
                }),
                renderer : getOvertimeType
            }
            // add by liuyi 20100202 
  			, {
  				header : '加班时间',
  				width : 80,
                sortable : true,
                dataIndex : 'overtimeTimeId',
                editor : new Ext.form.ComboBox({
			        readOnly : true,
			        triggerAction : 'all',
			        store : basicDaysStore,
			        mode : 'local',
			        valueNotFoundText : '',
			        displayField : 'baseDays',
			        valueField : 'id',
			        listClass: 'x-combo-list-small'
			    }),
			    renderer : getBasicDayJB
  			},
  			// modified by liuyi 20100202
  			absentCheckColumn,
  			// add by liuyi 20100202 
  			{
  				header : '旷工时间',
  				width : 80,
                sortable : true,
                dataIndex : 'absentTimeId',
                editor : new Ext.form.ComboBox({
			        readOnly : true,
			        triggerAction : 'all',
			        store : basicDaysStore,
			        mode : 'local',
			        valueNotFoundText : '',
			        displayField : 'baseDays',
			        valueField : 'id',
			        listClass: 'x-combo-list-small'
			    }),
			    renderer : getBasicDayKG
  			},
  			// add by liuyi 20100202 
  			{
                header : '请假',
                width : 70,
                sortable : true,
                dataIndex : 'vacationTypeId',
                editor : new Ext.form.ComboBox({
                    disabled : true,
                    triggerAction : 'all',
                    store : vacationStore,
                    mode : 'local',
                    valueNotFoundText : '',
                    displayField : 'vacationType',
                    valueField : 'vacationTypeId',
                    listClass : 'x-combo-list-small'
                }),
                renderer : getVacationType
            },
  			 {
  				header : '病假时间',
  				width : 80,
                sortable : true,
                dataIndex : 'sickLeaveTimeId',
                editor : new Ext.form.ComboBox({
			        readOnly : true,
			        triggerAction : 'all',
			        store : basicDaysStore,
			        mode : 'local',
			        valueNotFoundText : '',
			        displayField : 'baseDays',
			        valueField : 'id',
			        listClass: 'x-combo-list-small'
			    }),
			    renderer : getBasicDayBJ
  			},
  			// add by liuyi 20100202 
  			 {
  				header : '事假时间',
  				width : 80,
                sortable : true,
                dataIndex : 'eventTimeId',
                editor : new Ext.form.ComboBox({
			        readOnly : true,
			        triggerAction : 'all',
			        store : basicDaysStore,
			        mode : 'local',
			        valueNotFoundText : '',
			        displayField : 'baseDays',
			        valueField : 'id',
			        listClass: 'x-combo-list-small'
			    }),
			    renderer : getBasicDaySJ
  			},
  			// add by liuyi 20100202 
  			 {
  				header : '其他请假时间',
  				width : 80,
                sortable : true,
                dataIndex : 'otherTimeId',
                editor : new Ext.form.ComboBox({
			        readOnly : true,
			        triggerAction : 'all',
			        store : basicDaysStore,
			        mode : 'local',
			        valueNotFoundText : '',
			        displayField : 'baseDays',
			        valueField : 'id',
			        listClass: 'x-combo-list-small'
			    }),
			    renderer : getBasicDayQT
  			},
            resetCheckColumn,// absentCheckColumn, 
            evectionCheckColumn, outCheckColumn, {
                header : '备注',
                width : 100,
                sortable : true,
                dataIndex : 'memo'
            }],
        plugins : [new Ext.ux.plugins.GroupHeaderGrid({
                rows : [[{}, {}, {
                        header : '行政班',
                        colspan : 4,
                        align : 'center',
                        dataIndex : 'amBegingTime'
                    }, {}, {
//                        colspan : 6,
                    	 colspan : 11,
                        align : 'center',
                        dataIndex : 'vacationTypeId'
                    }, {}]],
                hierarchicalColMenu : true
            }), resetCheckColumn, absentCheckColumn, evectionCheckColumn, outCheckColumn],
        frame : false,
        border : true,
        tbar : ['员工姓名:', txtEmpName, '-', '所属部门:', txtEmpDept, '-', '审核人:', txtCheckMan, '-', '审核日期:', txtCheckDate],
        enableColumnHide : true,
        enableColumnMove : false
    });

    // panel
    var panel = new Ext.Panel({
        tbar : [saveBtn, quitBtn],
        border : false,
        frame : true,
        layout : 'border',
        items : [infoFs, gridDetail]
    });

    // 设定布局器及面板
    var empAttendanceQueryWin = new Ext.Window({
        enableTabScroll : true,
        height : 400,
        width : 720,
        modal : true,
        closeAction : 'hide',
        title : "全公司考勤查询",
        resizable : false,
        layout : 'fit',
        border : false,
        items : [panel]
    });

    // 单元格双击事件
    gridDetail.on('celldblclick', dbClickHandler);
    /**
     * celldblclick事件处理函数
     */
    function dbClickHandler(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = gridStore.getAt(rowIndex);
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        if ("memo" == fieldName) {
            taShowMemo.setValue(record.get("memo"));
            win.x = undefined;
            win.y = undefined;
            win.show();
        }
    }
    
    // 设置grid可否编辑
    gridDetail.on('beforeedit', function() {
        return false;
    });
    
    gridDetail.getView().on('refresh', function() {
        var columns = gridDetail.getColumnModel().config.length;
        var xView = gridDetail.getView();
        var height = xView.mainHd.getComputedHeight();
        for (var c = 0; c < columns; c++) {
            var validcol = gridDetail.getColumnModel().config[c].id;
            if (validcol === 'numberer' || validcol === 'checker') {
                // skip this column
            } else {
                var headerCellObj = Ext.get(xView.getHeaderCell([c]).firstChild);
                headerCellObj.setStyle({
                    'padding-top' : '3px',
                    'padding-bottom' : '4px'
                });
            }
        }
    })

    // 备注查看
    var taShowMemo = new Ext.form.TextArea({
        disabled : true
    });

    // 弹出画面
    var win = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [taShowMemo],
        buttonAlign : "center",
        title : '详细信息查看窗口',
        buttons : [{
                text : Constants.BTN_CLOSE,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    win.hide();
                }
            }],
        listeners : {
            hide : function() {
                taShowMemo.setValue("");
            }
        }
    });
    // -----------职工考勤记录查询>>>>>>>>>>>>>>>>

    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border : false,
        items : [fullPanel]
    });

    // --------------------------------审核记录查询窗口
    /**
     * 退出button
     */
    var btnQuit = new Ext.Button({
        text : "退出",
        iconCls : Constants.CLS_EXIT,
        handler : function() {
            winAudite.hide();
        }
    })

    /**
     * 审核记录grid
     */
    var rcdAudite = new Ext.data.Record.create([{
            name : 'examineBDeptName'
        }, {
            name : 'examineMan2'
        }, {
            name : 'examineDate2'
        }, {
            name : 'examineMan1'
        }, {
            name : 'examineDate1'
        }]);
    /**
     * 审核记录查询的store
     */
    var stoAudite = new Ext.data.JsonStore({
        url : 'ca/getAuditeDetail.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : rcdAudite
    });

    /**
     * 审核记录查询的grid
     */
    var gridAudite = new Ext.grid.GridPanel({
        autoWidth : true,
        store : stoAudite,
        sm : new Ext.grid.RowSelectionModel({
            // 单选
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : '审核部门',
                width : 100,
                sortable : true,
                dataIndex : 'examineBDeptName'
            }, {
                header : '审核人',
                width : 70,
                sortable : true,
                dataIndex : 'examineMan2'
            }, {
                header : '审核日期',
                width : 80,
                sortable : true,
                dataIndex : 'examineDate2',
                renderer : function(value) {
                    if ("" != value && null != value) {
                        var reg = /(\d{4}-\d{2}-\d{2})T\d{2}:\d{2}:\d{2}/gi;
                        return value.replace(reg, "$1");
                    } else {
                        return "";
                    }
                }
            }, {
                header : '申报人',
                width : 80,
                sortable : true,
                dataIndex : 'examineMan1'
            }, {
                header : '申报日期',
                width : 80,
                sortable : true,
                dataIndex : 'examineDate1',
                renderer : function(value) {
                    if ("" != value && null != value) {
                        var reg = /(\d{4}-\d{2}-\d{2})T\d{2}:\d{2}:\d{2}/gi;
                        return value.replace(reg, "$1");
                    } else {
                        return "";
                    }
                }
            }],
        frame : false,
        border : false,
        tbar : [btnQuit],
        enableColumnHide : true,
        enableColumnMove : false
    });
    
    gridAudite.getView().on('refresh', function() {
        var columns = gridAudite.getColumnModel().config.length;
        var xView = gridAudite.getView();
        var height = xView.mainHd.getComputedHeight();
        for (var c = 0; c < columns; c++) {
            var validcol = gridAudite.getColumnModel().config[c].id;
            if (validcol === 'numberer' || validcol === 'checker') {
                // skip this column
            } else {
                var headerCellObj = Ext.get(xView.getHeaderCell([c]).firstChild);
                headerCellObj.setStyle({
                    'padding-top' : '3px',
                    'padding-bottom' : '4px'
                });
            }
        }
    })

    /**
     * 审核记录查询window
     */
    var winAudite = new Ext.Window({
        height : 300,
        width : 500,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [gridAudite],
        buttonAlign : "center",
        title : '审核记录查询',
        autoScroll : true
    })
    // ========================= 处理 ======================= //

    /**
     * 加载数据
     */
    function loadDetail(record) {
        vacationStore.load({
            callback : function() {
                vacationStore.insert(0, new Ext.data.Record({
                    vacationTypeId : '',
                    vacationType : ''
                }));
            }
        });
        // 加载加班数据
        overtimeStore.load({
            callback : function() {
                overtimeStore.insert(0, new Ext.data.Record({
                    overtimeTypeId : '',
                    overtimeType : ''
                }));
            }
        });
        // 加载基本天数数据 add by liuyi 20100202
	    basicDaysStore.load({
	    	callback : function(){
	    		basicDaysStore.each(function(obj){
	    			obj.set('baseDays',obj.get('baseDays') + '天')
	    		})
	    		basicDaysStore.insert(0,new Ext.data.Record({
	    			id : '',
	    			baseDays : ''
	    		}))
	    	}
	    })
        // 加载运行班数据
        workshiftStore.load({
            callback : function() {
                workshiftStore.insert(0, new Ext.data.Record({
                    workShiftId : '',
                    workShift : ''
                }));
                // 加载明细部数据
                gridStore.sortInfo = null;
                if (gridDetail.rendered) {
                    gridDetail.getView().removeSortIcon();
                }
                // 重新加载数据
                gridStore.load({
                    params : {
                        empId : record.data["empId"],
                        strYearMonth : examineDate.getValue(),
                        workOrRest : Ext.encode(workOrRest),
                        strStartDate : strStartDate,
                        strEndDate : strEndDate,
                        strExamineDeptId : strExamineDeptId,
                        checkFlag : checkFlag
                    },
                    callback : function() {
                        empAttendanceQueryWin.show();
                        Ext.Msg.hide();
                    }
                });
            }
        });
    }

    /**
     * 检查日期是否合法
     */
    function checkDate() {
        var nowDate = new Date().format('Y-m-d');
        if (attendanceDate.getValue() > nowDate) {
            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_040);
            return false;
        }
        return true;
    }

    /**
     * 检查查询条件是否为空
     */
    function checkQuery() {
        var msg = "";
        msg += checkInput(attendanceDate, "考勤日期", TYPE_SELECT);
        msg += checkInput(attendanceDeptCmb, "考勤部门", TYPE_SELECT);
        if (msg != "") {
            Ext.Msg.alert(Constants.ERROR, msg);
            return false;
        }
        return true;
    }

    /**
     * 检查必须输入项
     */
    function checkInput(component, name, type) {
        var message = "";
        if (component.getValue() == "" || component.getValue() == null) {
            if (type == TYPE_INPUT) {
                message += String.format(Constants.COM_E_002, name);
            } else if (type == TYPE_SELECT) {
                message += String.format(Constants.COM_E_003, name);
            }
            message += "</BR>";
        }
        return message;
    }

    /**
     * 取得对应的运行班类别
     */
    function getWorkShift(value) {
        for (var i = 0; i < workshiftStore.getCount(); i++) {
            if (workshiftStore.getAt(i).get("workShiftId") == value) {
                return workshiftStore.getAt(i).get("workShift");
            }
        }
        return "";
    }

    /**
     * 取得对应的请假类别
     */
    function getVacationType(value) {
        for (var i = 0; i < vacationStore.getCount(); i++) {
            if (vacationStore.getAt(i).get("vacationTypeId") == value) {
                return vacationStore.getAt(i).get("vacationType");
            }
        }
        return "";
    }

    /**
     * 取得对应的加班类别
     */
    function getOvertimeType(value) {
        for (var i = 0; i < overtimeStore.getCount(); i++) {
            if (overtimeStore.getAt(i).get("overtimeTypeId") == value) {
                return overtimeStore.getAt(i).get("overtimeType");
            }
        }
        return "";
    }

    /**
     * 单元格双击事件处理
     */
    function showWin(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = gridStore.getAt(rowIndex);
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        // 如果是备注列
        if ('memo' == fieldName) {
            taShowMemo.setValue(record.get('memo'));
            win.x = undefined;
            win.y = undefined;
            win.show();
        }
    }
    
    /**
     * 取得对应的基本天数   加班
     * add by liuyi 20100202 
     */
    function getBasicDayJB(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(value != '' && value != null) {
	    	for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('overtimeTimeId', '');
			record.modified = {};
			record.dirty = false;
    	}
		return "";
    }
    
     /**
     * 取得对应的基本天数   旷工
     * add by liuyi 20100202 
     */
    function getBasicDayKG(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(value != '' && value != null) {
	    	for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('absentTimeId', '');
			record.modified = {};
			record.dirty = false;
    	}
		return "";
    }
    
     /**
     * 取得对应的基本天数   病假
     * add by liuyi 20100202 
     */
    function getBasicDayBJ(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(value != '' && value != null) {
	    	for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('sickLeaveTimeId', '');
			record.modified = {};
			record.dirty = false;
    	}
		return "";
    }
    
    /**
     * 取得对应的基本天数   事假
     * add by liuyi 20100202 
     */
    function getBasicDaySJ(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(value != '' && value != null) {
	    	for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('eventTimeId', '');
			record.modified = {};
			record.dirty = false;
    	}
		return "";
    }
     /**
     * 取得对应的基本天数   其他假
     * add by liuyi 20100202 
     */
    function getBasicDayQT(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(value != '' && value != null) {
	    	for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('otherTimeId', '');
			record.modified = {};
			record.dirty = false;
    	}
		return "";
    }
    
    /**
     * 查询函数
     */
    function queryAll() {
        queryObj.queryDate = examineDate.getValue();
        queryObj.queryDept = examineDept.getValue();
        var msg = '';
        if (examineDate.getValue() == '') {
            msg += String.format(Constants.COM_E_003, '审核年月') + '<br />'
        };
        // modify by liuyi 091027 增加判断部门为0时情况
        if (examineDept.getValue() != 0 && examineDept.getValue() == '') {
            msg += String.format(Constants.COM_E_003, '审核部门') + '<br />'
        }
        msg = msg.substring(0, msg.lastIndexOf("<br />"));
        // 必须项都不为空
        if (msg == '') {
            // 日期小于当前日期
            if (!compareDateStr(examineDate.getValue(), new Date().format("Y-m"))) {
                Ext.Ajax.request({
                    url : 'ca/getAllAttendanceList.action',
                    method : Constants.POST,
                    params : {
                        examineDate : examineDate.getValue(),
                        examineDept : examineDept.getValue()
                    },
                    success : function(action) {
                        var result = eval("(" + action.responseText + ")");
                        var o = eval("(" + action.responseText + ")");
                        if (o.msg == Constants.DATE_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                            return;
                        } else if (o.msg == attStdNull) {
                            // 考勤标准为空的场合
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_025);
                            // 审核记录查询按钮设为可用
                            btnExamineQuery.enable();
                            return;
                        } else if (undefined != o.msg) {
                            // 考勤标准为空的场合
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            // 审核记录查询按钮设为可用
                            btnExamineQuery.enable();
                            return;
                        } else {
                            loadData(o);
                            btnExamineQuery.enable();
                        }
                    }
                });
            } else {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_053, "审核年月"));
            }
        } else {
            Ext.Msg.alert(Constants.ERROR, msg);
        }
    }

    /**
     * textField显示时间比较方法
     */
    function compareDate(argDate1, argDate2) {
        return argDate1.getTime() > argDate2.getTime();
    }
    /**
     * textField显示时间比较方法 date1>date2 返回ture
     */
    function compareDateStr(argDateStr1, argDateStr2) {
        var date1 = Date.parseDate(argDateStr1, 'Y-m');
        var date2 = Date.parseDate(argDateStr2, 'Y-m');
        return compareDate(date1, date2);
    }

    /**
     * 设置假日颜色
     */
    function setColor(value, cellmeta, record, rowIndex, columnIndex, store) {
        if (workOrRest != null) {
            if (workOrRest[columnIndex - 6].workOrRest == '1') {
                return "<font color='" + color + "'>" + value + "</font>";
            } else {
                return value;
            }
        } else {
            return value;
        }
    }

    /**
     * 数据加载
     */
    function loadData(obj) {
        strExamineDeptId = obj.strExamineDeptId;
        checkFlag = obj.checkFlag;
        workOrRest = obj.workOrRestList;
        color = obj.strColor;
        if (obj.store != null) {
        	storeMain.loadData(obj.store);
        }
        if (obj.deptAllStore != null) {
        	storeCheck.loadData(obj.deptAllStore);
        }
        if (storeMain.getCount() > 0 && storeCheck.getCount() > 0) {
            gExamineDept.setValue(obj.strExamineDeptName);
            gExamineEmp.setValue(obj.strExamine);
            gTimeKeeper.setValue(obj.strAttendance);
            gExamineDate.setValue(examineDate.getValue());
            strStartDate = obj.strStartDate;
            strEndDate = obj.strEndDate;
            checkFlag = obj.checkFlag;
            workOrRest = obj.workOrRestList;
            color = obj.strColor;
            
            // 导出按钮、审核记录查询按钮、打印按钮可用
            btnExport.enable();
            btnExamineQuery.enable();
            btnPrint.enable();
            // 审核年月，审核部门控件不可用
            examineDate.disable();
            examineDept.disable();
			gExamineDate.setValue(examineDate.getValue());
			gExamineDept.setValue(examineDept.getRawValue());
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
        }

    }
     /**
     * 合计项数据格式化
     */
    function setTotalCountNumber(v, argDecimal) {
        if (v === 0)
            return "0.0";
        if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 2;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        } else
            return "";
    }
    
    /**
     * 合计项数据格式化
     */
    function setTotalCountNumber1(v, cellmeta, record, rowIndex, columnIndex, store) {
        if (v) {
            v = Number(v).toFixed(1);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        } else
            return "0.0";
    }
    function setTotalCountNumber2(v, cellmeta, record, rowIndex, columnIndex, store) {
        if (v) {
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;
            return v;
        } else
            return "0";
    }
});