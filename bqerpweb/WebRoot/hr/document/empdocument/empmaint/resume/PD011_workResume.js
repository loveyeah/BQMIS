Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var employee = parent.Ext.getCmp('tabPanel').employee;
    
    // 新增按钮
    var btnAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : onAdd
    });
    
    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : onModify
    });
    
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : onDelete
    });
    
    // 打印按钮
    var btnPrint = new Ext.Button({
        text : '打印员工履历表',
        iconCls : Constants.CLS_PRINT,
        handler : onPrint
    });
    
    
    var workRecord = Ext.data.Record.create([{
            // 工作单位
            name : 'unit'
        }, {
            // 开始日期
            name : 'startDate'
        }, {
            // 结束日期
            name : 'endDate'
        }, {
            // 岗位名称
            name : 'stationName'
        }, {
            // 职务名称
            name : 'headshipName'
        }, {
            // 证明人
            name : 'witness'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 工作简历ID
            name : 'workresumeid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    }]);
    
    // grid列模式
    var workCM = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '工作单位',
            width : 120,
            dataIndex : 'unit'
        }, {
            header : '开始日期',
            width : 100,
            renderer : renderDate,
            dataIndex : 'startDate'
        }, {
            header : '结束日期',
            renderer : renderDate,
            width : 100,
            dataIndex : 'endDate'
        }, {
            header : '岗位名称',
            width : 80,
            dataIndex : 'stationName'
        }, {
            header : '职务名称',
            width : 80,
            dataIndex : 'headshipName'
        }, {
            header : '证明人',
            width : 60,
            dataIndex : 'witness'
        }, {
            header : '备注',
            width : 200,
            dataIndex : 'memo'
    }]);
    workCM.defaultSortable = true;
    
    // 数据源
    var workStore = new Ext.data.JsonStore({
        url : 'hr/getEmpWorkresumeInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : workRecord
    });
    
    // 工作简历Grid
    var workGrid = new Ext.grid.GridPanel({
        store : workStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : workCM,
        // 分页
        bbar : new Ext.PagingToolbar({
                pageSize : Constants.PAGE_SIZE,
                store : workStore,
                displayInfo : true,
                displayMsg : Constants.DISPLAY_MSG,
                emptyMsg : Constants.EMPTY_MSG
            }),
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    workGrid.on('rowdblclick', onModify);
    workGrid.on('celldblclick', showWindow);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [workGrid]
    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 180;
    var twoWd = 350;
    var height = 100;
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'chsName',
        fieldLabel : "员工姓名",
        readOnly : true,
        value : employee.chsName
    });
    
    // 开始日期
    var tfStartDate = new Ext.form.TextField({
        id : 'startDate',
        name : 'workResume.startDate',
        style : 'cursor:pointer',
        fieldLabel : "开始日期<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfStartDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfStartDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 结束日期
    var tfEndDate = new Ext.form.TextField({
        id : 'endDate',
        name : 'workResume.endDate',
        style : 'cursor:pointer',
        fieldLabel : "结束日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    });
    
    // 证明人
    var tfWitness = new Ext.form.TextField({
        id : 'witness',
        name : 'workResume.witness',
        fieldLabel : "证明人",
        maxLength : 6
    });
    
    // 工作单位
    var tfUnit = new Ext.form.TextField({
        id : 'unit',
        name : 'workResume.unit',
        fieldLabel : "工作单位<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 15,
        width : twoWd
    });
    
    // 岗位名称
    var tfStationName = new Ext.form.TextField({
        id : 'stationName',
        name : 'workResume.stationName',
        fieldLabel : "岗位名称<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 30
    });
    
    // 职务名称
    var tfHeadshipName = new Ext.form.TextField({
        id : 'headshipName',
        name : 'workResume.headshipName',
        fieldLabel : "职务名称<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 15
    });
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'memo',
        name : 'workResume.memo',
        fieldLabel : "备注",
        maxLength : 127,
        width : twoWd,
        height : height
    });
    
    // 工作简历ID
    var hideWorkResumeId = new Ext.form.Hidden({
        id : 'workresumeid',
        name : 'workResume.workresumeid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'workResume.lastModifiedDate'
    });
    
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    
    // 取消按钮
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            employee.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                if (button == "yes") {
                    win.hide();
                }
            });
        }
    });
    
    var formPanel = new Ext.FormPanel({
        labelAlign : 'right',
        labelWidth : 80,
        frame : true,
        border : false,
        trackResetOnLoad : true,
        items : [tfChsName, tfStartDate, tfEndDate,
            tfWitness, tfUnit, tfStationName,
            tfHeadshipName, tfMemo,
            hideWorkResumeId, hideLastModifiedDate
        ]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 500,
        height : 370,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmpWorkInfo);
    // 添加更改员工名字时的监听器
    employee.addNameChangeHandler(function() {
        tfChsName.setValue(employee.chsName);
    });
    // 添加加载员工综合信息前的监听器
    employee.addBeforeLoadEmpHandler(beforLoadEmp);
    
    // 加载员工前的处理
    function beforLoadEmp(argEmpCode) {
        if (win.rendered && !win.hidden && !win.inValid) {
	    	formPanel.getForm().trim();
            return !formPanel.getForm().isDirty();
        }
        
        return true;
    }
    
    // 加载员工工作简历信息 
    function loadEmpWorkInfo() {
    	// 隐藏弹出画面
    	employee.closeWin('workResume', win);
    	
        workStore.baseParams = {
            empId : employee.empId
        };
        
        workStore.load({
            params:{
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        
        var enableFlag = employee.hasEmpId();
        // 新增按钮可用设置
        btnAdd.setDisabled(!enableFlag);
        // 修改按钮可用设置
        btnModify.setDisabled(!enableFlag);
        // 删除按钮可用设置
        btnDelete.setDisabled(!enableFlag);
        // 打印按钮可用设置
        btnPrint.setDisabled(!enableFlag);
    }
    
    // 重新加载Grid
    function reloadGrid(options) {
        workStore.reload(options);
    }
    
    function renderDate(value) {
        if (value instanceof Date) {
            return value.dateFormat('Y-m-d');
        }
        value = value ? value.match(/\d{4}-\d{2}-\d{2}/gi) : '';
        return value ? value[0] : '';
    }
    
    function renderModifyDate(value) {
        if (!value) return "";
        if (value instanceof Date) {
            return value.dateFormat('Y-m-d H:i:s');
        }
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        strTime = strTime ? strTime[0] : '00:00:00';
        return strDate[0] + " " + strTime;
    }
    
    // 显示时间比较方法
    function compareDate(argDate1, argDate2) {
        return argDate1.getTime() < argDate2.getTime();
    }
    
    // textField显示时间比较方法
    function compareDateStr(argDateStr1, argDateStr2){
        var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
        var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
        return compareDate(date1, date2);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增工作简历
        win.setTitle('新增工作简历');
        win.show();
        win.center();
        
        formPanel.getForm().setValues({
        	workresumeid: '',
        	lastModifiedDate: '',
        	startDate: '',
        	endDate: '',
        	witness: '',
        	unit: '',
        	stationName: '',
        	headshipName: '',
        	memo: ''
        });
        formPanel.getForm().clearInvalid();
    }
    
    // 显示弹出备注查看对话框
    function showWindow(grid, row, col) {
    	if (!btnModify.hidden) {
            return;
        }
    	var dataIndex = grid.getColumnModel().getDataIndex(col);
    	if (dataIndex === 'memo') {
    		employee.showMemoWin(grid.getStore().getAt(row).get(dataIndex));
    	}
    }
    
    // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!workGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改工作简历
        win.setTitle('修改工作简历');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = workGrid.getSelectionModel().getSelected();
        // 开始日期
        record.data.startDate = renderDate(record.get('startDate'));
        // 结束日期
        record.data.endDate = renderDate(record.get('endDate'));
        // 上次修改时间
        record.data.lastModifiedDate = renderDate(record.get('lastModifiedDate'));
        formPanel.getForm().loadRecord(record);
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!workGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = workGrid.getSelectionModel().getSelected();
                    
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteEmpWorkresumeInfo.action',
                        params : {
                            'workResume.workresumeid' : record.get('workresumeid'),
                            'workResume.lastModifiedDate' : record.get('lastModifiedDate')
                            },
                        success : function(result, request) {
                            var o = eval('(' + result.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                employee.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                employee.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid({
					            params:{
					                start : 0,
					                limit : Constants.PAGE_SIZE
					            }
					        });
                            employee.alert(Constants.REMIND, Constants.COM_I_005);
                        },
                        failure : function() {
                            employee.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        }
                    });
                }
            }
        );
    }
    
    // 打印按钮处理
    function onPrint() {
        employee.print();
    }
    
    // Check处理
    function checkFields() {
        var msg = '';
        if (tfStartDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "开始日期") + '<br/>';
        }
        if (tfUnit.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "工作单位") + '<br/>';
        }
        if (tfStationName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "岗位名称") + '<br/>';
        }
        if (tfHeadshipName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "职务名称") + '<br/>';
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 关联Check处理
    function checkRefFields() {
    	var msg = '';
    	if (tfEndDate.getValue() !== '') {
            if (!compareDateStr(tfStartDate.getValue(), tfEndDate.getValue())) {
                msg += String.format(Constants.COM_E_006, "开始日期", "结束日期") + '<br/>';
            }
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 保存按钮处理
    function onSave() {
        if (!checkFields()) {
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_001,
            function(buttonobj) {
                if (buttonobj == "yes") {
                	if (!checkRefFields()) {
			            return;
			        }
			        
                    var isAddFlag = !hideWorkResumeId.getValue();
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpWorkresumeInfo.action',
                        params : {
                            'workResume.empId' : employee.empId,
                            'isAdd' : isAddFlag
                            },
                        success : function(form, action) {
                            var o = eval('(' + action.response.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                employee.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                employee.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid();
                            employee.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                win.hide();
                            });
                        }
                    });
                }
            }
        );
    }
    // ==========       处理结束       =============
    
    
    // ==========       初期化处理        ===========
    // 打印按钮
    btnPrint.setDisabled(true);
    // 加载员工基本信息
    loadEmpWorkInfo();
    if (employee.editable) {
        // 打印按钮不可用
        btnPrint.setVisible(false);
        // 新增按钮可用设置
        btnAdd.setVisible(true);
        // 修改按钮可用
        btnModify.setVisible(true);
        // 保存按钮可用
        btnDelete.setVisible(true);
    } else {
        // 打印按钮可用
        btnPrint.setVisible(true);
        // 新增按钮可用设置
        btnAdd.setVisible(false);
        // 修改按钮不可用
        btnModify.setVisible(false);
        // 保存按钮不可用
        btnDelete.setVisible(false);
    }
    
    // 员工姓名不可用
    tfChsName.setDisabled(true);
    
     // 右键禁用
     document.onkeydown = function()
 {
          if(event.keyCode==116) {
          event.keyCode=0;
          event.returnValue = false;
          }
}
document.oncontextmenu = function() {event.returnValue = false;} 
});
