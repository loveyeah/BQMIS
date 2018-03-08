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
    
    
    var studyRecord = Ext.data.Record.create([{
            // 入学日期
            name : 'enrollmentDate'
        }, {
            // 毕业日期
            name : 'graduateDate'
        }, {
            // 学校编码Id
            name : 'schoolCodeId'
        }, {
            // 学校名称
            name : 'schoolName'
        }, {
            // 学习专业Id
            name : 'specialtyCodeId'
        }, {
            // 学习专业
            name : 'specialtyName'
        }, {
            // 学号
            name : 'studyCode'
        }, {
            // 学历Id
            name : 'educationId'
        }, {
            // 学历
            name : 'educationName'
        }, {
            // 学位Id
            name : 'degreeId'
        }, {
            // 学位
            name : 'degreeName'
        }, {
            // 语种编码Id
            name : 'languageCodeId'
        }, {
            // 语种
            name : 'languageName'
        }, {
            // 学习类别编码Id
            name : 'studyTypeCodeId'
        }, {
            // 学习类别
            name : 'studyType'
        }, {
            // 学制(年)
            name : 'studyLimit'
        }, {
            // 是否毕业
            name : 'ifGraduate'
        }, {
            // 培训费用
            name : 'studyMoney'
        }, {
            // 教育结果
            name : 'educationResult'
        }, {
            // 证书号码
            name : 'certificateCode'
        }, {
            // 是否原始学历
            name : 'ifOriginality'
        }, {
            // 是否最高学历
            name : 'ifHightestXl'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 学历教育Id
            name : 'educationid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    }]);
    
    // grid列模式
    var studyCM = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '入学日期',
            width : 80,
            dataIndex : 'enrollmentDate'
        }, {
            header : '毕业日期',
            width : 80,
            dataIndex : 'graduateDate'
        }, {
            header : '学校名称',
            width : 100,
            dataIndex : 'schoolName'
        }, {
            header : '学习专业',
            width : 80,
            dataIndex : 'specialtyName'
        }, {
            header : '学号',
            width : 80,
            dataIndex : 'studyCode'
        }, {
            header : '学历',
            width : 50,
            dataIndex : 'educationName'
        }, {
            header : '学位',
            width : 50,
            dataIndex : 'degreeName'
        }, {
            header : '语种',
            width : 40,
            dataIndex : 'languageName'
        }, {
            header : '学习类别',
            width : 60,
            dataIndex : 'studyType'
        }, {
            header : '学制(年)',
            width : 60,
            dataIndex : 'studyLimit'
        }, {
            header : '是否毕业',
            width : 60,
            renderer : renderYesNo,
            dataIndex : 'ifGraduate'
        }, {
            header : '培训费用',
            width : 80,
            align : 'right',
            renderer : renderNumber,
            dataIndex : 'studyMoney'
        }, {
            header : '教育结果',
            width : 80,
            renderer : renderEduResult,
            dataIndex : 'educationResult'
        }, {
            header : '证书号码',
            width : 80,
            dataIndex : 'certificateCode'
        }, {
            header : '是否原始学历',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'ifOriginality'
        }, {
            header : '是否最高学历',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'ifHightestXl'
        }, {
            header : '备注',
            width : 150,
            dataIndex : 'memo'
    }]);
    studyCM.defaultSortable = true;
    
    
    // 数据源
    var studyStore = new Ext.data.JsonStore({
        url : 'hr/getEmpStudyResumeInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : studyRecord
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : studyStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
    
    // 学习简历Grid
    var studyGrid = new Ext.grid.GridPanel({
        store : studyStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : studyCM,
        // 分页
        bbar : pagebar,
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    studyGrid.on('rowdblclick', onModify);
    studyGrid.on('celldblclick', showWindow);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [studyGrid]
    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 105;
    Ext.form.CmbHRBussiness.prototype.width = 105;
    Ext.form.CmbHRCode.prototype.width = 105;
    var twoWd = 294;
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'chsName',
        fieldLabel : "员工姓名",
        readOnly : true,
        value : employee.chsName
    });
    
    // 学校名称
    var cbSchoolCodeId = new Ext.form.CmbHRBussiness({
        id : "schoolCodeId",
        hiddenName : 'studyResume.schoolCodeId',
        fieldLabel : "学校名称<font color='red'>*</font>",
        allowBlank : false,
        selectOnFocus : true,
        type : '学校',
        width : twoWd
    });
    
    // 入学日期
    var tfEnrollmentDate = new Ext.form.TextField({
        id : 'enrollmentDate',
        name : 'studyResume.enrollmentDate',
        style : 'cursor:pointer',
        fieldLabel : "入学日期<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfEnrollmentDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfEnrollmentDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 学习专业
    var cbSpecialtyCodeId = new Ext.form.CmbHRBussiness({
        id : "specialtyCodeId",
        hiddenName : 'studyResume.specialtyCodeId',
        fieldLabel : '学习专业',
        selectOnFocus : true,
        type : '学习专业',
        width : twoWd
    });
    
    // 毕业日期
    var tfGraduateDate = new Ext.form.TextField({
        id : 'graduateDate',
        name : 'studyResume.graduateDate',
        style : 'cursor:pointer',
        fieldLabel : "毕业日期<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfGraduateDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfGraduateDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 学历
    var cbEducationId = new Ext.form.CmbHRBussiness({
        id : "educationId",
        hiddenName : 'studyResume.educationId',
        fieldLabel : "学历",
        selectOnFocus : true,
        type : '学历'
    });
    
    // 学位
    var cbDegreeId = new Ext.form.CmbHRBussiness({
        id : "degreeId",
        hiddenName : 'studyResume.degreeId',
        fieldLabel : "学位",
        selectOnFocus : true,
        type : '学位'
    });
    
    // 语种
    var cbLanguageCodeId = new Ext.form.CmbHRBussiness({
        id : "languageCodeId",
        hiddenName : 'studyResume.languageCodeId',
        fieldLabel : '语种',
        selectOnFocus : true,
        type : '语种'
    });
    
    // 学习类别
    var cbStudyTypeCodeId = new Ext.form.CmbHRBussiness({
        id : "studyTypeCodeId",
        hiddenName : 'studyResume.studyTypeCodeId',
        fieldLabel : '学习类别',
        selectOnFocus : true,
        type : '学习类别'
    });
    
    // 学制（年）
    var cbStudyLimit = new Ext.form.TextField({
        id : "studyLimit",
        name : 'studyResume.studyLimit',
        fieldLabel : '学制（年）',
        maxLength : 8
    });
    
    // 学号
    var tfStudyCode = new Ext.form.TextField({
        id : 'studyCode',
        name : 'studyResume.studyCode',
        fieldLabel : "学号",
        maxLength : 10,
        codeField : 'yes',
        style: {
            'ime-mode' : 'disabled'
        }
    });
    
    // 教育结果
    var cbEducationResult = new Ext.form.CmbHRCode({
        id : "educationResult",
        hiddenName : 'studyResume.educationResult',
        fieldLabel : '教育结果',
        selectOnFocus : true,
        type : '教育结果'
    });
    
    // 培训费用
    var cbStudyMoney = new Powererp.form.NumField({
        id : "studyMoney",
        name : 'studyResume.studyMoney',
        fieldLabel : '培训费用',
        style : 'text-align:right',
        maxLength: 16,
        allowNegative : false,
        decimalPrecision : 4,
        padding : 4
    });
    
    // 是否毕业
    var raIfGraduate = {
        id : 'ifGraduate',
        layout : 'column',
        isFormField : true,
        fieldLabel : '是否毕业',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifGraduate1',
                boxLabel : '是',
                name : 'studyResume.ifGraduate',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifGraduate2',
                boxLabel : '否',
                name : 'studyResume.ifGraduate',
                inputValue : '0'
            })
        }]
    };
    
    // 证书号码
    var tfCertificateCode = new Ext.form.TextField({
        id : 'certificateCode',
        name : 'studyResume.certificateCode',
        fieldLabel : "证书号码",
        maxLength : 20,
        width : twoWd,
        codeField : 'yes',
        style: {
            'ime-mode' : 'disabled'
        }
    });
    
    // 原始学历
    var raIfOriginality = {
        id : 'ifOriginality',
        layout : 'column',
        isFormField : true,
        fieldLabel : '原始学历',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifOriginality1',
                boxLabel : '是',
                name : 'studyResume.ifOriginality',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifOriginality2',
                boxLabel : '否',
                name : 'studyResume.ifOriginality',
                inputValue : '0'
            })
        }]
    };
    
    // 最高学历
    var raIfHightestXl = {
        id : 'ifHightestXl',
        layout : 'column',
        isFormField : true,
        fieldLabel : '最高学历',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifHightestXl1',
                boxLabel : '是',
                name : 'studyResume.ifHightestXl',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifHightestXl2',
                boxLabel : '否',
                name : 'studyResume.ifHightestXl',
                inputValue : '0'
            })
        }]
    };
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'memo',
        name : 'studyResume.memo',
        fieldLabel : "备注",
        maxLength : 128,
        width : twoWd - 2
    });
    
    // 学习简历ID
    var hideStudyResumeId = new Ext.form.Hidden({
        id : 'educationid',
        name : 'studyResume.educationid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'studyResume.lastModifiedDate'
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
    
    var panel1 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfChsName]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [cbSchoolCodeId]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfEnrollmentDate]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [cbSpecialtyCodeId]
                    }]
            }]
    });
    
    var panel2 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfGraduateDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbEducationId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbDegreeId]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbLanguageCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyTypeCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyLimit]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfStudyCode]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbEducationResult]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyMoney]
                    }]
            }]
    });
    
    var panel3 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [raIfGraduate]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [tfCertificateCode]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [raIfOriginality, raIfHightestXl]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [tfMemo]
                    }]
            }]
    });
    
    var formPanel = new Ext.FormPanel({
        labelAlign : 'right',
        labelWidth : 70,
        frame : true,
        border : false,
        items : [panel1, panel2, panel3, 
            hideStudyResumeId, hideLastModifiedDate
        ]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 600,
        height : 300,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmpStudyInfo);
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
            return !checkPageChanged();
        }
        
        return true;
    }
    
    var pageFields = ['schoolCodeId',
        	'enrollmentDate',
        	'specialtyCodeId',
        	'graduateDate',
        	'educationId',
        	'degreeId',
        	'languageCodeId',
        	'studyTypeCodeId',
        	'studyLimit',
        	'studyCode',
        	'educationResult',
        	'studyMoney',
        	'certificateCode',
        	'ifGraduate',
        	'ifOriginality',
        	'ifHightestXl',
        	'memo'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !hideStudyResumeId.getValue();
    	var record = isAdd ? {} : studyGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['studyResume.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && (
            	prop === 'ifGraduate'
                	|| prop === 'ifOriginality'
                	|| prop === 'ifHightestXl')) {
        		origialV = "1";
        	}
    		if (currentV === 'undefined'
                || currentV === 'null'
                || currentV == null) {
                currentV = "";
            }
            // 培训费用
            if (prop === 'studyMoney') {
            	// 删除逗号
            	currentV = String(currentV).replace(/,/g, '');
            	if (currentV.length > 0) {
            		currentV = Number(currentV);
            	}
        	}
                    
    		if (origialV != currentV) {
    			return true;
    		}
    	}
    	return false;
    }
    
    function renderYesNo(value) {
        if (value == '1') {
            return '是';
        } else if (value == '0') {
            return '否';
        }
        return '';
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 4;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
	
    // 教育结果
    function renderEduResult(value) {
        if (value == '1') {
            return '毕业';
        } else if (value == '2') {
            return '结业';
        } else if (value == '3') {
            return '未结业';
        } else if (value == '4') {
            return '肆业';
        }
        return '';
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
    
    // 加载员工学习简历信息 
    function loadEmpStudyInfo() {
    	// 隐藏弹出画面
    	employee.closeWin('studyResume', win);
    	
        studyStore.baseParams = {
            empId : employee.empId
        };
        
        studyStore.load({
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
        studyStore.reload(options);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增学习简历
        win.setTitle('新增学习简历');
        win.show();
        win.center();
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
        if (!studyGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改学习简历
        win.setTitle('修改学习简历');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = studyGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 学校名称
        cbSchoolCodeId.setValue(record.get('schoolCodeId'), true);
        // 学习专业
        cbSpecialtyCodeId.setValue(record.get('specialtyCodeId'), true);
        // 学历
        cbEducationId.setValue(record.get('educationId'), true);
        // 学位
        cbDegreeId.setValue(record.get('degreeId'), true);
        // 语种
        cbLanguageCodeId.setValue(record.get('languageCodeId'), true);
        // 学习类别
        cbStudyTypeCodeId.setValue(record.get('studyTypeCodeId'), true);
        // 教育结果
        cbEducationResult.setValue(record.get('educationResult'), true);
        
        // 是否毕业
        if (record.get('ifGraduate') == '0') {
            Ext.getCmp('ifGraduate2').setValue(true);
        }
        // 是否原始学历
        if (record.get('ifOriginality') == '0') {
            Ext.getCmp('ifOriginality2').setValue(true);
        }
        // 是否最高学历
        if (record.get('ifHightestXl') == '0') {
            Ext.getCmp('ifHightestXl2').setValue(true);
        }
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!studyGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = studyGrid.getSelectionModel().getSelected();
                    
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteEmpStudyResumeInfo.action',
                        params : {
                            'studyResume.educationid' : record.get('educationid'),
                            'studyResume.lastModifiedDate' : record.get('lastModifiedDate')
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
        if (!cbSchoolCodeId.getValue() && cbSchoolCodeId.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "学校名称") + '<br/>';
        }
        if (tfEnrollmentDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "入学日期") + '<br/>';
        }
        if (tfGraduateDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "毕业日期") + '<br/>';
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
    	if (tfEnrollmentDate.getValue() !== '') {
            if (!compareDateStr(tfEnrollmentDate.getValue(), tfGraduateDate.getValue())) {
                msg += String.format(Constants.COM_E_006, "入学日期", "毕业日期") + '<br/>';
            }
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 学校名称
        if (!cbSchoolCodeId.getValue()) {
            cbSchoolCodeId.setValue('');
        }
        // 学习专业
        if (!cbSpecialtyCodeId.getValue()) {
            cbSpecialtyCodeId.setValue('');
        }
        // 学历
        if (!cbEducationId.getValue()) {
            cbEducationId.setValue('');
        }
        // 学位
        if (!cbDegreeId.getValue()) {
            cbDegreeId.setValue('');
        }
        // 语种
        if (!cbLanguageCodeId.getValue()) {
            cbLanguageCodeId.setValue('');
        }
        // 学习类别
        if (!cbStudyTypeCodeId.getValue()) {
            cbStudyTypeCodeId.setValue('');
        }
        // 教育结果
        if (!cbEducationResult.getValue()) {
            cbEducationResult.setValue('');
        }
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
			        
                    var isAddFlag = !hideStudyResumeId.getValue();
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpStudyResumeInfo.action',
                        params : {
                            'emp.empId' : employee.empId,
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
    // 打印按钮不可用
    btnPrint.setDisabled(true);
    // 加载员工基本信息
    loadEmpStudyInfo();
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
