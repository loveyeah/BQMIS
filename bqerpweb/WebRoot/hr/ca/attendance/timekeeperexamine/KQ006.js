var checkFlag;
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
// 部门考勤画面关联处理
DeptAttendance = function() {
    // 画面关联项排斥数组
//    this.checkArray = new Array([false, false, false, false, true, false, false, true, true, true, true], [false,
//        false, false, false, true, false, false, true, true, true, true], [false, false, false, false, true, false,
//        false, true, true, true, true], [false, false, false, false, true, false, false, true, true, true, true], [
//        true, true, true, true, false, false, false, true, true, true, true], [false, false, false, false, false,
//        false, true, true, true, true, true], [false, false, false, false, false, true, false, true, true, true, true],
//    [true, true, true, true, true, true, true, false, true, true, true], [true, true, true, true, true, true, true,
//        true, false, true, true], [true, true, true, true, true, true, true, true, true, false, true], [true, true,
//        true, true, true, true, true, true, true, true, false]);

     /** modified by liuyi 20100202
	 * 画面关联项排斥数组
	 * true: 排斥
	 * false: 不排斥
	 */
	this.checkArray = new Array(
//		[false, false, false, false, true, false, false, true, true, true, true],
	[false, false, false, false, true, false, false, true, false, true, true],
//		[false, false, false, false, true, false, false, true, true, true, true],
	[false, false, false, false, true, false, false, true, false, true, true],
//		[false, false, false, false, true, false, false, true, true, true, true],
	[false, false, false, false, true, false, false, true, false, true, true],
//		[false, false, false, false, true, false, false, true, true, true, true],
	[false, false, false, false, true, false, false, true, false, true, true],
//		[true, true, true, true, false, false, false, true, true, true, true],
	[true, true, true, true, false, false, false, true, false, true, true],
//		[false, false, false, false, false, false, true, true, true, true, true],
	[false, false, false, false, false, false, false, true, false, true, true],
//		[false, false, false, false, false, true, false, true, true, true, true],
	[false, false, false, false, false, true, false, true, false, true, true],
//		[true, true, true, true, true, true, true, false, true, true, true],
	[true, true, true, true, true, true, false, false, false, true, true],
		// 旷工检查
//		[true, true, true, true, true, true, true, true, false, true, true],
		[false, false, false, false, true, false, false, false, false, false, false],
//		[true, true, true, true, true, true, true, true, true, false, true],
		[true, true, true, true, true, true, false, true, false, false, true],
//		[true, true, true, true, true, true, true, true, true, true, false]
		[true, true, true, true, true, true, false, true, false, true, false]
	);

    // 画面关联项名称
    this.nameArray = new Array('amBegingTime', 'amEndTime', 'pmBegingTime', 'pmEndTime', 'workShiftId',
    'vacationTypeId', 'overtimeTypeId', 'restType', 'absentWork', 'evectionType', 'outWork');

    // 关联处理
    DeptAttendance.prototype.check = function(record, name) {
        var index = -1;
        for (var i = 0; i < this.nameArray.length; i++) {
            if (name == this.nameArray[i]) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            var array = this.checkArray[index];
            for (var j = 0; j < array.length; j++) {
                if (array[j] === true) {
                    if (record.get(this.nameArray[j]) != '' && record.get(this.nameArray[j]) != null) {
                        record.set(this.nameArray[j], '');
                    }
                }
            }
        }
    }
}

deptAttendance = new DeptAttendance();

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
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    onMouseDown : function(e, t) {
        if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
            // e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            if (checkFlag == 'N') {
                record.set(this.dataIndex, !(record.data[this.dataIndex] * 1) ? '1' : '0');
                deptAttendance.check(record, this.dataIndex);
            }

        }
    },

    renderer : function(v, p, record) {
        p.css += ' x-grid3-check-col-td';
        return '<div class="x-grid3-check-col' + ((v === '1') ? '-on' : '') + ' x-grid3-cc-' + this.id
        + '">&#160;</div>';
    }
};
Ext.onReady(function() {
    // 开始日期
    var strStartDate;
    // 结束日期
    var strEndDate;
    // 颜色
    var color = '';
    // 上班休息flag
    var workOrRest = null;
    // 控件类型: 选择
    var TYPE_SELECT = "S";
    // 控件类型: 输入
    var TYPE_INPUT = "I";
    // 标识: 0
    var FLAG_0 = "0";
    // 标识: 1
    var FLAG_1 = "1";
    // 标准上午上班时间
    var amBegingTime = '';
    // 标准上午下班时间
    var amEndTime = '';
    // 标准下午上班时间
    var pmBegingTime = '';
    // 标准下午下班时间
    var pmEndTime = '';
    // 数据加载标记
    var countLoad = 0;
    // 员工id
    var empId = '';
    // 审核年月
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
        url : "ca/getAttendanceDeptType2Info.action",
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

    // 查询按钮
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : function() {
            queryAll();
        }
    });
    // 考勤审核
    var btnExamine = new Ext.Button({
        text : "考勤审核",
        disabled : true,
        iconCls : Constants.CLS_ATTEND_CHECK,
        handler : function() {
            btnAttendExamineWin.setVisible(true);
            btnCancelExamineWin.setVisible(true);
            examineWin.setTitle('考勤审核');
            examineWin.x = undefined;
            examineWin.y = undefined;
            examineWin.show();
            storeCheckAllRight.sortInfo = null;
            gridAttendExamineWin.getView().removeSortIcon();
        }
    });
    // 审核记录查询
    var btnExamineQuery = new Ext.Button({
        text : "审核记录查询",
        disabled : true,
        iconCls : Constants.CLS_RECORD_QUERY,
        handler : function() {
            btnAttendExamineWin.setVisible(false);
            btnCancelExamineWin.setVisible(false);
            examineWin.setTitle('审核记录查询');
            examineWin.x = undefined;
            examineWin.y = undefined;
            examineWin.show();
            storeCheckAllRight.sortInfo = null;
            gridAttendExamineWin.getView().removeSortIcon();
        }
    });
    // 导出
    var btnExport = new Ext.Button({
        text : Constants.BTN_EXPORT,
        disabled : true,
        iconCls : Constants.CLS_EXPORT,
        handler : function() {
            Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_007, function(button, text) {
                if (button == "yes") {
                    document.all.blankFrame.src = "ca/exportAttendanceQueryFile.action";
                }
            })
        }
    });
    // 取消
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
                if (buttonobj == "yes") {
                    cls();
                }
            });
        }
    });
    // 头部工具栏
    var topBar = new Ext.Toolbar({

        items : ["审核年月<font color='red'>*</font>:", examineDate, "-", "审核部门<font color='red'>*</font>:", examineDept,
            "-", btnQuery, btnExamine, btnExamineQuery, btnExport, btnCancel]
    })

    // 定义主画面store
    var storeMain = new Ext.data.JsonStore({
        root : 'list',
        url : ''
    });
    // 定义考勤审核store
    var storeCheck = new Ext.data.JsonStore({
        root : 'list',
        url : '',
        fields : ['examineADept', 'attendanceYear', 'attendanceMonth', 'examineMan1', 'examineDate1', 'examineMan2',
            'examineDate2', 'examineType', 'examineDeptTop', 'examineBDeptName', 'examineBDeptId',
            'examineRegisterMan', 'man1ChsName', 'examineMan', 'man2ChsName', 'cancelFlag', 'lastModifiyDate']
    });
    var storeCheckAllRight = new Ext.data.JsonStore({
        root : 'list',
        url : '',
        fields : ['examineADept', 'attendanceYear', 'attendanceMonth', 'examineMan1', 'examineDate1', 'examineMan2',
            'examineDate2', 'examineType', 'examineDeptTop', 'examineBDeptName', 'examineBDeptId',
            'examineRegisterMan', 'man1ChsName', 'examineMan', 'man2ChsName', 'cancelFlag', 'lastModifiyDate']
    });
    var storeEmpAttendance = new Ext.data.JsonStore({
        root : 'list',
        url : 'ca/getEmpAttendanceInfo.action',
        totalProperty : 'totalCount',
        fields : ['id.empId', 'id.attendanceDate', 'deptId', 'attendanceDeptId', 'amBegingTime', 'amEndTime',
            'pmBegingTime', 'pmEndTime', 'workShiftId', 'vacationTypeId', 'overtimeTypeId', 'work', 'restType',
            'absentWork', 'lateWork', 'leaveEarly', 'outWork', 'evectionType', 'memo', 'insertby', 'insertdate',
            'lastModifiyBy', 'lastModifiyDate', 'isUse', 'enterpriseCode'
            // add by liuyi 20100203 
            ,'overtimeTimeId','sickLeaveTimeId','eventTimeId','absentTimeId','otherTimeId'
            ]
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
    });

    var titleLabel = new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region : 'north',
        height : 35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text : '职工考勤记录申报表',
            style : 'font-size:22px;font-family: 黑体;'
        })]
    });
    var gExamineDept = new Ext.form.TextField({
        name : 'deptName',
        disabled : true,
        width : 120
    });
    var gExamineEmp = new Ext.form.TextField({
        name : 'examineEmpName',
        disabled : true,
        width : 120
    });
    var gTimeKeeper = new Ext.form.TextField({
        name : 'timeEmpName',
        disabled : true,
        width : 120
    });
    var gExamineDate = new Ext.form.TextField({
        name : 'examineDate',
        disabled : true,
        width : 120
    });

    var gridBar = new Ext.Toolbar({
        items : ["审核部门:", gExamineDept, "-", "审核人:", gExamineEmp, "-", "考勤员:", gTimeKeeper, "-", "审核年月:", gExamineDate]

    });
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
        enableColumnMove : false
    });
    
    /**
     * 设置职工考勤记录弹出窗口的审核人
     */
    function setCheckManWin(empId, attendanceTopDeptId) {
        var intCount = storeCheck.getCount();
        for (i = 0; i < intCount; i++) {
            var r = storeCheck.getAt(i);
            if (r.data['examineBDeptId'] == attendanceTopDeptId) {
                if (r.data['examineType'] == '2' && r.data['man2ChsName'] != null) {
                    txtCheckMan.setValue(r.data['man2ChsName']);
                    txtCheckDate.setValue(r.data['examineDate2']);
                    break;
                } else {
                    attendanceTopDeptId = r.data['examineDeptTop'];
                    continue;
                }
            }
        }
    }
    var fullPanel = new Ext.Panel({
        tbar : topBar,
        layout : 'border',
        border : false,
        items : [titleLabel, gridMain]
    });

    // <<<<<<<<<<<<<<职工考勤记录查询

    // 保存按钮
    var saveBtn = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        disabled : true,
        handler : saveHandler
    });

    // 取消按钮
    var quitBtn = new Ext.Button({
        text : "退出",
        iconCls : Constants.CLS_EXIT,
        handler : quitHandler
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
    var infoFs = new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region : 'north',
        height : 35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text : "职工考勤记录表",
            style : 'font-size:22px;font-family: 黑体;color : #000000;'
        })]
    });

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

     // add by liuyi 20100203 定义基础天数store
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

    // 一览grid
    var grid = new Ext.grid.EditorGridPanel({
        autoWidth : true,
        store : storeEmpAttendance,
        region : 'center',
        sm : new Ext.grid.RowSelectionModel({
            // 单选
            singleSelect : true
        }),
        clicksToEdit : 1,
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : '考勤日期',
                width : 70,
                sortable : true,
                dataIndex : 'id.attendanceDate',
                renderer : function(value) {
                    return formatTime(value);
                }
            }, {
                header : '上午上班',
                width : 60,
                sortable : true,
                dataIndex : 'amBegingTime',
                editor : new Ext.form.TextField({
                    id : 'amBegingTime',
                    style : 'cursor:pointer',
                    readOnly : true,
                    listeners : {
                        focus : function() {
                            this.blur();
                            WdatePicker({
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowToday : false,
                                onpicked : function() {
                                    grid.getSelectionModel().getSelected().set('amBegingTime',
                                    Ext.get('amBegingTime').dom.value);
                                    deptAttendance.check(grid.getSelectionModel().getSelected(), 'amBegingTime');
                                    this.blur();
                                },
                                onclearing : function() {
                                    grid.getSelectionModel().getSelected().set('amBegingTime', '');
                                }
                            });
                        }
                    }
                })
            }, {
                header : '上午下班',
                width : 60,
                sortable : true,
                dataIndex : 'amEndTime',
                editor : new Ext.form.TextField({
                    id : 'amEndTime',
                    style : 'cursor:pointer',
                    readOnly : true,
                    listeners : {
                        focus : function() {
                            this.blur();
                            WdatePicker({
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowToday : false,
                                onpicked : function() {
                                    grid.getSelectionModel().getSelected().set('amEndTime',
                                    Ext.get('amEndTime').dom.value);
                                    deptAttendance.check(grid.getSelectionModel().getSelected(), 'amEndTime');
                                    this.blur();
                                },
                                onclearing : function() {
                                    grid.getSelectionModel().getSelected().set('amEndTime', '');
                                }
                            });
                        }
                    }
                })
            }, {
                header : '下午上班',
                width : 60,
                sortable : true,
                dataIndex : 'pmBegingTime',
                editor : new Ext.form.TextField({
                    id : 'pmBegingTime',
                    style : 'cursor:pointer',
                    readOnly : true,
                    listeners : {
                        focus : function() {
                            this.blur();
                            WdatePicker({
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowToday : false,
                                onpicked : function() {
                                    grid.getSelectionModel().getSelected().set('pmBegingTime',
                                    Ext.get('pmBegingTime').dom.value);
                                    deptAttendance.check(grid.getSelectionModel().getSelected(), 'pmBegingTime');
                                    this.blur();
                                },
                                onclearing : function() {
                                    grid.getSelectionModel().getSelected().set('pmBegingTime', '');
                                }
                            });
                        }
                    }
                })
            }, {
                header : '下午下班',
                width : 60,
                sortable : true,
                dataIndex : 'pmEndTime',
                editor : new Ext.form.TextField({
                    id : 'pmEndTime',
                    style : 'cursor:pointer',
                    readOnly : true,
                    listeners : {
                        focus : function() {
                            this.blur();
                            WdatePicker({
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowToday : false,
                                onpicked : function() {
                                    grid.getSelectionModel().getSelected().set('pmEndTime',
                                    Ext.get('pmEndTime').dom.value);
                                    deptAttendance.check(grid.getSelectionModel().getSelected(), 'pmEndTime');
                                    this.blur();
                                },
                                onclearing : function() {
                                    grid.getSelectionModel().getSelected().set('pmEndTime', '');
                                }
                            });
                        }
                    }
                })
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
            },  {
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
            // add by liuyi 20100203
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
  			}
  			// modified by liuyi 20100203
  			,
  			absentCheckColumn
  			,// add by liuyi 20100202 
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
                    readOnly : true,
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
  			 resetCheckColumn, //absentCheckColumn,
  			evectionCheckColumn, outCheckColumn, {
                header : '备注',
                width : 100,
                sortable : true,
                dataIndex : 'memo',
                editor : new Ext.form.TextArea({
                    maxLength : 128,
                    id : 'test',
                    grow : true,
                    listeners : {
                        'render' : function() {
                            this.el.on('dblclick', function() {
                                var record = grid.getSelectionModel().getSelected();
                                grid.stopEditing();
                                taShowMemo.setValue(record.get('memo'));
                                taShowMemo.setDisabled(false);
                                win.x = undefined;
                                win.y = undefined;
                                win.setTitle('详细信息录入窗口');
                                btnMemoSave.setVisible(true);
                                btnMemoCancel.setVisible(true);
                                btnMemoClose.setVisible(false);
                                win.show();
                            })
                        }
                    }
                })
            }],
        plugins : [new Ext.ux.plugins.GroupHeaderGrid({
                rows : [[{}, {}, {
                        header : '行政班',
                        colspan : 4,
                        align : 'center',
                        dataIndex : 'amBegingTime'
                    }, {}, {
                    	// modified by liuyi 20100203
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

    // 单元格双击事件
    grid.on('celldblclick', dbClickHandler);
    // 设置grid可否编辑
    grid.on('beforeedit', function() {
        if (storeEmpAttendance.getCount() > 0) {
            // 如果画面已审核，则不可编辑
            if (checkFlag == "Y") {
                return false;
            }
        }
        return true;
    });
    /**
     * 单元格双击处理
     */
    function dbClickHandler(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = storeEmpAttendance.getAt(rowIndex);
        // 判断是否审核
        if (checkFlag == "Y") {
            // 编辑列的字段名
            var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
            // 如果是备注列
            if ('memo' == fieldName) {
                taShowMemo.setValue(record.get('memo'));
                taShowMemo.setDisabled(true);
                win.x = undefined;
                win.y = undefined;
                win.setTitle('详细信息查看窗口');
                win.show();
                btnMemoSave.setVisible(false);
                btnMemoCancel.setVisible(false);
                btnMemoClose.setVisible(true);
            }
        }
    }
    // 关联处理，check互斥项
    grid.on('afteredit', function(object) {
        if ((storeEmpAttendance.getCount() > 0) && object.field != 'amBegingTime' && object.field != 'amEndTime'
        && object.field != 'pmBegingTime' && object.field != 'pmEndTime') {
            if (object.record.get(object.field) != '') {
                deptAttendance.check(object.record, object.field);
            }
        }
    });
    grid.getView().on('refresh', function() {
        var columns = grid.getColumnModel().config.length;
        var xView = grid.getView();
        var height = xView.mainHd.getComputedHeight();
        for (var c = 0; c < columns; c++) {
            var validcol = grid.getColumnModel().config[c].id;
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
    // panel
    var panel = new Ext.Panel({
        tbar : [saveBtn, quitBtn],
        border : false,
        frame : true,
        layout : 'border',
        items : [infoFs, grid]
    });

    // 设定布局器及面板
    var empAttendanceQueryWin = new Ext.Window({
        enableTabScroll : true,
        title : '职工考勤记录查询',
        height : 400,
        width : 720,
        modal : true,
        closeAction : 'hide',
        // closable : false,
        resizable : false,
        layout : 'fit',
        border : false,
        items : [panel]
    });

    // 注册行的双击事件
    gridMain.on('rowdblclick', function() {

        // 如果有行选中
        if (gridMain.selModel.hasSelection()) {
            var record = gridMain.getSelectionModel().getSelected();
            // 清空原store
            storeEmpAttendance.removeAll();
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
                if (r.data['examineBDeptId'] == record.data['attendanceDeptId']) {
                    if (r.data['examineType'] == '2') {
                        if (r.data['examineMan2'] != null && r.data['examineMan2'] != '') {
                            txtCheckMan.setValue(r.data['man2ChsName']);
                            txtCheckDate.setValue(r.data['examineDate2']);
                            break;
                        } else {
                            txtCheckMan.setValue(r.data['man1ChsName']);
                            txtCheckDate.setValue(r.data['examineDate1']);
                            break;
                        }
                    } else {
                        topId = r.data['examineDeptTop'];
                        setCheckManWin(empId, topId);
                        break;
                    }
                } else {
                    continue;
                }
            }
            // 已审核保存按钮不可用
            if (checkFlag == "Y") {
                saveBtn.setDisabled(true);
            } else {
                saveBtn.setDisabled(false);
            }

            // 加载数据
            loadEmpAttendanceList(record);
        }

    });
    
    // 备注查看
    var taShowMemo = new Ext.form.TextArea({
        maxLength : 128
    });

    var btnMemoSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : function() {
            if (taShowMemo.isValid()) {
                var record = grid.getSelectionModel().getSelected();
                record.set('memo', taShowMemo.getValue());
                win.hide();
            }
        }
    });
    var btnMemoCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            win.hide();
        }
    })
    var btnMemoClose = new Ext.Button({
        text : Constants.BTN_CLOSE,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            win.hide();
        }
    })
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
        buttons : [btnMemoSave, btnMemoCancel, btnMemoClose],
        listeners : {
            show : function() {
                taShowMemo.focus(true, 100);
            },
            hide : function() {
                taShowMemo.setValue("");
            }
        }
    });
    // -----------职工考勤记录查询>>>>>>>>>>>>>>>>
    // <---------考勤审核画面
    // 考勤审核按钮
    var btnAttendExamineWin = new Ext.Button({
        text : "考勤审核",
        iconCls : Constants.CLS_ATTEND_CHECK,
        handler : function() {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_011, function(buttonobj) {
                if (buttonobj == 'yes') {
                    var deptIds = [];
                    for (i = 0; i < storeCheck.getCount(); i++) {
                        record = storeCheck.getAt(i);
                        deptIds.push(record.data['examineBDeptId']);

                    }
                    Ext.Ajax.request({
                        url : 'ca/setAttendanceCheck.action',
                        method : Constants.POST,
                        params : {
                            deptIds : Ext.encode(deptIds),
                            attendanceDeptId : examineDept.getValue(),
                            strStartDate : strStartDate,
                            strEndDate : strEndDate
                        },
                        success : function(result, request) {
                            if (result.responseText) {
                                var o = eval("(" + result.responseText + ")");
                                var succ = o.msg;
                                if (succ == 'S') {
                                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_026, function(obj) {
                                        if (obj == 'ok') {
                                            examineWin.hide();
                                            // 刷新grid中的数据, 并且不弹出确认对话框
                                            queryAll(true);
                                            checkFlag = 'Y';
                                        }
                                    });
                                } else if (succ == Constants.DATE_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                                } else if (succ == Constants.SQL_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                } else if (succ == Constants.DATA_USING) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                }
                            }
                        }
                    })
                }
            })
        }
    });

    // 撤销前回审核按钮
    var btnCancelExamineWin = new Ext.Button({
        text : "撤销前回审核",
        disabled : true,
        iconCls : Constants.CLS_CHECK_ABOLISH,
        handler : function() {
            if (gridAttendExamineWin.selModel.hasSelection()) {
                var records = gridAttendExamineWin.getSelectionModel().getSelections();
                var cancelDeptInfos = [];
                for (i = 0; i < records.length; i++) {
                    cancelDeptInfos.push(records[i].data);
                }
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_012, function(buttonobj) {
                    if (buttonobj == 'yes') {
                        Ext.Ajax.request({
                            url : 'ca/cancelLastCheck.action',
                            method : Constants.POST,
                            params : {
                                cancelDeptInfos : Ext.encode(cancelDeptInfos)
                            },
                            success : function(result, request) {
                                if (result.responseText) {
                                    var o = eval("(" + result.responseText + ")");
                                    var succ = o.msg;
                                    if (succ == 'S') {
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_027, function(obj) {
                                            if (obj == 'ok') {
                                                examineWin.hide();
                                                // 刷新grid中的数据, 并且不弹出确认对话框
                                                cls();
                                            }
                                        });
                                    } else if (succ == Constants.DATE_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                                    } else if (succ == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (succ == Constants.DATA_USING) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                    }
                                }
                            }
                        })
                    }
                })
            }
        }
    });

    // 退出按钮
    var btnExitExamineWin = new Ext.Button({
        text : "退出",
        iconCls : Constants.CLS_EXIT,
        handler : function() {
            examineWin.hide();
        }
    });

    // 审核记录grid
    var gridAttendExamineWin = new Ext.grid.GridPanel({
        autoWidth : true,
        store : storeCheckAllRight,
        region : 'center',
        sm : new Ext.grid.RowSelectionModel({
            // 单选
            singleSelect : false,
            listeners : {
                selectionchange : function() {
                    if (gridAttendExamineWin.selModel.hasSelection()) {
                        var records = gridAttendExamineWin.getSelectionModel().getSelections();
                        for (i = 0; i < records.length; i++) {
                            r = records[i];
                            if (r.data['cancelFlag'] == 'Y') {
                                if (btnCancelExamineWin.isVisible())
                                    btnCancelExamineWin.setDisabled(false);
                            } else {
                                btnCancelExamineWin.setDisabled(true);
                                break;
                            }
                        }
                    } else {
                        btnCancelExamineWin.setDisabled(true);
                    }
                }
            }
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
                dataIndex : 'man2ChsName'
            }, {
                header : '审核日期',
                width : 80,
                sortable : true,
                dataIndex : 'examineDate2'
            }, {
                header : '申报人',
                width : 80,
                sortable : true,
                dataIndex : 'man1ChsName'
            }, {
                header : '申报日期',
                width : 80,
                sortable : true,
                dataIndex : 'examineDate1'
            }],
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
    });

    gridAttendExamineWin.getView().on('refresh', function() {
        var columns = gridAttendExamineWin.getColumnModel().config.length;
        var xView = gridAttendExamineWin.getView();
        var height = xView.mainHd.getComputedHeight();
        for (var c = 0; c < columns; c++) {
            var validcol = gridAttendExamineWin.getColumnModel().config[c].id;
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

    // 审核记录列表Win
    var examineWin = new Ext.Window({
        tbar : [btnAttendExamineWin, btnCancelExamineWin, btnExitExamineWin],
        height : 300,
        width : 500,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [gridAttendExamineWin],
        buttonAlign : "center",
        title : '考勤审核'
    });
    examineWin.on('beforehide', function() {
        gridAttendExamineWin.selModel.clearSelections();
    })

    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border : false,
        items : [fullPanel]
    });
    // ========================= 处理 ======================= //

    /**
     * 保存处理
     */
    function saveHandler() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(buttonobj) {
            if (buttonobj == 'yes') {
                if (checkGridTime()) {
                    // 设置出勤标识
                    setAttendanceFlag();
                    // 设置迟到早退标识
                    setLateEarlyFlag();
                    // 定义记录数组
                    var recordArray = new Array();
                    // 将grid中的记录转化为数组
                    for (var i = 0; i < storeEmpAttendance.getCount(); i++) {
                        recordArray.push(storeEmpAttendance.getAt(i).data);
                    }
                    // 转化为字符串
                    var records = Ext.util.JSON.encode(recordArray);
                    Ext.Ajax.request({
                        url : 'ca/saveEmpAttendanceInfo.action',
                        method : Constants.POST,
                        params : {
                            records : records,
                            empId : empId
                        },
                        success : function(result, request) {
                            if (result.responseText) {
                                var o = eval("(" + result.responseText + ")");
                                var succ = o.msg;
                                if (succ == 'S') {
                                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function(obj) {
                                        if (obj == 'ok') {
                                            empAttendanceQueryWin.hide();
                                            queryAll();
                                        }
                                    });
                                    // 刷新grid中的数据, 并且不弹出确认对话框
                                } else if (succ == Constants.DATE_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                                } else if (succ == Constants.SQL_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                } else if (succ == Constants.DATA_USING) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                }
                            }
                        }
                    })
                }
            }
        });
    }
    /**
     * 员工考勤记录查询退出
     */
    function quitHandler() {
        if (checkFlag == 'Y') {
            empAttendanceQueryWin.hide();
        } else {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
                if (buttonobj == "yes") {
                    empAttendanceQueryWin.hide();
                }
            });
        }
    }
    /**
     * 加载某个员工的考勤记录作为弹出画面数据（员工考勤记录查询）
     */
    function loadEmpAttendanceList(record) {
        Ext.Ajax.request({
            url : 'ca/getEmpAttendanceInfo.action',
            method : Constants.POST,
            params : {
                empId : record.data['empId'],
                strStartDate : strStartDate,
                strEndDate : strEndDate,
                checkFlag : checkFlag,
                workOrRest : Ext.encode(workOrRest),
                attendanceDeptId : record.data['attendanceDeptId'],
                deptId : record.data['deptId']
            },
            success : function(action) {
                var result = eval("(" + action.responseText + ")");
                var o = eval("(" + action.responseText + ")");
                if (o.msg == Constants.SQL_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                } else {
                    loadEmpAttendanceData(o);
                }
            }
        });
    }

    /**
     * 取消处理
     */
    function cancelHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            if (buttonobj == "yes") {
                // 登记按钮可用
                registerBtn.setDisabled(false);
                // 保存按钮不可用
                saveBtn.setDisabled(false);
                // 考勤日期初期化
                attendanceDate.setDisabled(false);
                attendanceDate.setValue(date);
                // 星期初期化
                weekdayTxt.setValue(weekday);
                // 考勤部门初期化
                attendanceDeptCmb.setDisabled(false);
                attendanceDeptCmb.setValue("");
                attendanceDeptCmb.clearInvalid();
                // 职工考勤登记明细部分初期化
                attendanceDateInfo.setValue("");
                attendanceDeptInfo.setValue("");
                // grid初期化
                storeEmpAttendance.removeAll();
                grid.getView().refresh();
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
     * 设置出勤标识
     */
    function setAttendanceFlag() {
        for (var i = 0; i < storeEmpAttendance.getCount(); i++) {
            var record = storeEmpAttendance.getAt(i);
            // 行政班和运行班只要有一个不为空
            if (!isEmpty(record.get('amBegingTime')) || !isEmpty(record.get('pmBegingTime'))
            || !isEmpty(record.get('workShiftId'))) {
                record.set('work', FLAG_1);
            } else {
                record.set('work', FLAG_0);
            }
        }
    }

    /**
     * 设置迟到早退标识
     */
    function setLateEarlyFlag() {
        for (var i = 0; i < storeEmpAttendance.getCount(); i++) {
            var record = storeEmpAttendance.getAt(i);
            // 出勤的情况下
            if ((record.get('work') == FLAG_1)) {
                // 标准时间不为空
                if (!isEmpty(amBegingTime)) {
                    var amEarly = false;
                    var amLate = false;
                    var pmEarly = false;
                    var pmLate = false;

                    // 上午上班
                    if (!isEmpty(record.get('amBegingTime'))) {
                        // 上午迟到
                        if (record.get('amBegingTime') > amBegingTime) {
                            amLate = true;
                        }
                        // 上午早退
                        if (record.get('amEndTime') < amEndTime) {
                            amEarly = true;
                        }
                    }

                    // 下午上班
                    if (!isEmpty(record.get('pmBegingTime'))) {
                        // 下午迟到
                        if (record.get('pmBegingTime') > pmBegingTime) {
                            pmLate = true;
                        }
                        // 下午早退
                        if (record.get('pmEndTime') < pmEndTime) {
                            pmEarly = true;
                        }
                    }

                    // 上午或下午迟到
                    if (amLate || pmLate) {
                        // 迟到
                        record.set('lateWork', FLAG_1);
                    } else {
                        record.set('lateWork', FLAG_0);
                    }

                    // 上午或下午早退
                    if (amEarly || pmEarly) {
                        // 早退
                        record.set('leaveEarly', FLAG_1);
                    } else {
                        record.set('leaveEarly', FLAG_0);
                    }
                } else {
                    record.set('lateWork', FLAG_0);
                    record.set('leaveEarly', FLAG_0);
                }
            } else {
                record.set('lateWork', FLAG_0);
                record.set('leaveEarly', FLAG_0);
            }
        }
    }
    /**
     * 检查上下班时间是否合法
     */
    function checkGridTime() {
        var msga = '';
        var msgb = '';
        for (var i = 0; i < storeEmpAttendance.getCount(); i++) {
            var record = storeEmpAttendance.getAt(i);
            // 上午上班和下班时间不都为空
            if (!(isEmpty(record.get('amBegingTime')) && isEmpty(record.get('amEndTime')))) {
                // 上午上班或下班时间为空
                if (isEmpty(record.get('amBegingTime')) || isEmpty(record.get('amEndTime'))) {
                    msga += String.format(Constants.COM_E_051, i + 1, '上午上班时间', '上午下班时间');
                    msga += '</BR>';
                    // 上午上班或下班时间都不为空
                } else if (record.get('amBegingTime') >= record.get('amEndTime')) {
                    msgb += String.format(Constants.COM_E_050, i + 1, '上午上班时间', '上午下班时间');
                    msgb += '</BR>';
                }
            }
            // 下午上班和下班时间不都为空
            if (!(isEmpty(record.get('pmBegingTime')) && isEmpty(record.get('pmEndTime')))) {
                // 下午上班或下班时间为空
                if (isEmpty(record.get('pmBegingTime')) || isEmpty(record.get('pmEndTime'))) {
                    msga += String.format(Constants.COM_E_051, i + 1, '下午上班时间', '下午下班时间');
                    msga += '</BR>';
                    // 下午上班或下班时间都不为空
                } else if (record.get('pmBegingTime') >= record.get('pmEndTime')) {
                    msgb += String.format(Constants.COM_E_050, i + 1, '下午上班时间', '下午下班时间');
                    msgb += '</BR>';
                }
            }
        }
        if (msga != '') {
            Ext.Msg.alert(Constants.ERROR, msga);
            return false;
        } else if (msgb != '') {
            Ext.Msg.alert(Constants.ERROR, msgb);
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
     * 单元格双击事件处理
     */
    function showWin(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = storeEmpAttendance.getAt(rowIndex);
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
     * 查询函数
     */
    function queryAll(flag) {
        var msg = '';
        if (examineDate.getValue() == '') {
            msg += String.format(Constants.COM_E_003, '审核年月') + '<br />'
        };
        if (examineDept.getValue() == '') {
            msg += String.format(Constants.COM_E_003, '审核部门') + '<br />'
        }
        // 必须项都不为空
        if (msg == '') {
            // 日期小于当前日期
            if (!compareDateStr(examineDate.getValue(), new Date().format("Y-m"))) {
                Ext.Ajax.request({
                    url : 'ca/getAttendanceList.action',
                    method : Constants.POST,
                    params : {
                        examineDate : examineDate.getValue(),
                        examineDept : examineDept.getValue()
                    },
                    success : function(action) {
                        var result = eval("(" + action.responseText + ")");
                        var o = eval("(" + action.responseText + ")");
                        cls();
                        if (o.msg == Constants.DATE_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                            return;
                        } else if (o.msg == 'X') {
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_025);
                            btnExamineQuery.setDisabled(false);
                            return;
                            // modify by liuyi 091027 action中未写N到前台
//                        } else if (o.msg == 'N') {
                            } else if (o.msg == 'U') {
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                            return;
                        } else if (o.unCheckDeptName != null) {
                            var dept = [];
                            dept = o.unCheckDeptName.split(",");
                            var msgc = "";
                            for (i = 0; i < dept.length; i++) {
                                msgc += String.format(Constants.KQ006_I_002, dept[i]) + "<br/>"
                            }
                            Ext.Msg.alert(Constants.REMIND, msgc);
                            storeCheck.loadData(o.deptAllStore);
                            storeCheckAllRight.removeAll();
                            for (i = 0; i < storeCheck.getCount(); i++) {
                                var r = storeCheck.getAt(i);
                                if (isEmpty(r.data['examineMan2']) && isEmpty(r.data['examineMan1'])) {
                                    continue;
                                    // storeCheckAllRight.add(r);
                                } else {
                                    storeCheckAllRight.add(r);
                                }
                            }
                            btnExamineQuery.setDisabled(false);
                            return;
                        } else {
                            loadMainData(o, flag);
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
     * 数据加载
     */
    function loadMainData(obj, flag) {
        examineDate.setDisabled(true);
        examineDept.setDisabled(true);
        checkFlag = obj.checkFlag;
        workOrRest = obj.workOrRestList;
        color = obj.strColor;
        storeMain.loadData(obj.store);
        storeCheck.loadData(obj.deptAllStore);
        storeCheckAllRight.removeAll();
        if (storeMain.getCount() > 0 && storeCheck.getCount() > 0) {

            for (i = 0; i < storeCheck.getCount(); i++) {
                var r = storeCheck.getAt(i);
                if (isEmpty(r.data['examineMan2']) && isEmpty(r.data['examineMan1'])) {
                    continue;
                    // storeCheckAllRight.add(r);
                } else {
                    storeCheckAllRight.add(r);
                }
            }
            gExamineDept.setValue(obj.strExamineDeptName);
            gExamineEmp.setValue(obj.strExamine);
            gTimeKeeper.setValue(obj.strAttendance);
            gExamineDate.setValue(examineDate.getValue());
            strStartDate = obj.strStartDate;
            strEndDate = obj.strEndDate;
            checkFlag = obj.checkFlag;
            workOrRest = obj.workOrRestList;
            color = obj.strColor;

            if (checkFlag == 'N') {
                btnExamine.setDisabled(false);
            } else {
                if (!flag) {
                    Ext.Msg.alert(Constants.REMIND, Constants.KQ006_I_001);
                }
                btnExamine.setDisabled(true);
            }
            controlButton(true);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            controlButton(false);
        }
    }
    /** 画面按钮控制 */
    function controlButton(flag) {

        btnExamineQuery.setDisabled(!flag);
        btnExport.setDisabled(!flag);
    }
    /**
     * 员工考勤记录查询
     */
    function loadEmpAttendanceData(obj) {
        // 加载完成计数器
        var count = 0;
        vacationStore.load({
            callback : function() {
                vacationStore.insert(0, new Ext.data.Record({
                    vacationTypeId : '',
                    vacationType : ''
                }));
                loadGridData(++count, obj);
            }
        });
        // 加载加班数据
        overtimeStore.load({
            callback : function() {
                overtimeStore.insert(0, new Ext.data.Record({
                    overtimeTypeId : '',
                    overtimeType : ''
                }));
                loadGridData(++count, obj);
            }
        });
        // 加载运行班数据
        workshiftStore.load({
            callback : function() {
                workshiftStore.insert(0, new Ext.data.Record({
                    workShiftId : '',
                    workShift : ''
                }));
                loadGridData(++count, obj);
            }
        });
        
        // 加载基本天数数据 add by liuyi 20100203
	    basicDaysStore.load({
	    	callback : function(){
	    		basicDaysStore.each(function(obj){
	    			obj.set('baseDays',obj.get('baseDays') + '天')
	    		})
	    		basicDaysStore.insert(0,new Ext.data.Record({
	    			id : '',
	    			baseDays : ''
	    		}))
	    	loadGridData(++count, obj);
	    	}
	    })
    };
    function loadGridData(count, obj) {
       // modified by liuyi 20100202 增加一个基本天数加载
//    	if(count == 3) {
    	if(count == 4) {
            // 请假DRP内容初期化
            // 加班DRP内容初期化
            // 运行班DRP内容初期化
            storeEmpAttendance.sortInfo = null;
            grid.getView().removeSortIcon();
            storeEmpAttendance.loadData(obj.pobj);
            // 标准上午上班时间
            amBegingTime = obj.amBeginTime;
            // 标准上午下班时间
            amEndTime = obj.amEndTime;
            // 标准下午上班时间
            pmBegingTime = obj.pmBeginTime;
            // 标准下午下班时间
            pmEndTime = obj.pmEndTime;
        }
    }
    /**
     * 去掉时间中T
     */
    function formatTime(value) {
        if (!value)
            return "";
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;

        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate)
            return "";
        return strDate;
    }
    /**
     * 判断是否为空,为空返回ture
     */
    function isEmpty(value) {
        return value == '' || value == null;
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
     * 取消事件，清空页面
     */
    function cls(flag) {
        btnExamine.setDisabled(true);
        controlButton(false);
        storeCheck.removeAll();
        storeCheckAllRight.removeAll();
        storeEmpAttendance.removeAll();
        storeMain.removeAll();
        gExamineDept.setValue('');
        gExamineEmp.setValue('');
        gTimeKeeper.setValue('');
        gExamineDate.setValue('');
        gridMain.getColumnModel().setConfig([]);
        examineDate.setDisabled(false);
        examineDept.setDisabled(false);
        examineDept.clearInvalid();
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