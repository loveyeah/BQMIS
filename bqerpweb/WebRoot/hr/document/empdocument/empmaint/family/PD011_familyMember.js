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
    
    
    var familyRecord = Ext.data.Record.create([{
            // 称谓编码Id
            name : 'callsCodeId'
        }, {
            // 称谓
            name : 'callsName'
        }, {
            // 姓名
            name : 'name'
        }, {
            // 性别
            name : 'sex'
        }, {
            // 出生日期
            name : 'birthday'
        }, {
            // 婚否
            name : 'ifMarried'
        }, {
            // 籍贯Id
            name : 'nativePlaceId'
        }, {
            // 籍贯
            name : 'nativePlaceName'
        }, {
            // 民族Id
            name : 'nationCodeId'
        }, {
            // 民族
            name : 'nationName'
        }, {
            // 学历Id
            name : 'educationId'
        }, {
            // 学历
            name : 'educationName'
        }, {
            // 单位
            name : 'unit'
        }, {
            // 政治面貌Id
            name : 'politicsId'
        }, {
            // 政治面貌
            name : 'politicsName'
        }, {
            // 职务名称
            name : 'headshipName'
        }, {
            // 是否直系
            name : 'zxqsMark'
        }, {
            // 是否供养
            name : 'ifLineally'
        }, {
            // 家庭成员Id
            name : 'familymemberid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    }]);
    
    // grid列模式
    var familyCM = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '称谓',
            width : 60,
            dataIndex : 'callsName'
        }, {
            header : '姓名',
            width : 70,
            dataIndex : 'name'
        }, {
            header : '性别',
            width : 40,
            renderer : renderSex,
            dataIndex : 'sex'
        }, {
            header : '出生日期',
            width : 100,
            dataIndex : 'birthday'
        }, {
            header : '婚否',
            width : 60,
            renderer : renderYesNo,
            dataIndex : 'ifMarried'
        }, {
            header : '籍贯',
            width : 100,
            dataIndex : 'nativePlaceName'
        }, {
            header : '民族',
            width : 60,
            dataIndex : 'nationName'
        }, {
            header : '学历',
            width : 80,
            dataIndex : 'educationName'
        }, {
            header : '单位',
            width : 120,
            dataIndex : 'unit'
        }, {
            header : '政治面貌',
            width : 60,
            dataIndex : 'politicsName'
        }, {
            header : '职务名称',
            width : 60,
            dataIndex : 'headshipName'
        }, {
            header : '是否直系',
            width : 60,
            renderer : renderYesNo,
            dataIndex : 'zxqsMark'
        }, {
            header : '是否供养',
            width : 60,
            renderer : renderYesNo,
            dataIndex : 'ifLineally'
    }]);
    familyCM.defaultSortable = true;
    
    
    // 数据源
    var familyStore = new Ext.data.JsonStore({
        url : 'hr/getEmpFamilyInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : familyRecord
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : familyStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
    
    // 家庭成员Grid
    var familyGrid = new Ext.grid.GridPanel({
        store : familyStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : familyCM,
        // 分页
        bbar : pagebar,
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    familyGrid.on('rowdblclick', onModify);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [familyGrid]
    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 150;
    Ext.form.CmbHRBussiness.prototype.width = 150;
    Ext.form.CmbHRCode.prototype.width = 150;
    var twoWd = 387;
    
    // 姓名
    var tfName = new Ext.form.TextField({
         id   :'name',
         name :'member.name',
         allowBlank : false,
         fieldLabel :"姓名<font color='red'>*</font>",
         maxLength  :4 
    });
    
    // 称谓
    var cbCallsCodeId = new Ext.form.CmbHRBussiness({
         id    :'callsCodeId',
         hiddenName  :'member.callsCodeId',
         fieldLabel :"称谓<font color='red'>*</font>",
         allowBlank : false,
         type :'称谓'
    });
    
    //第一面板
    var familyMemberFirstPnl = new Ext.Panel({
             border : false,
             layout : 'form',
             style  :'padding-top:12px',
             items  : [{
                 border : false,
                 layout : "column",
                 items  :[{
                         columnWidth : 0.5,
                         border : false,
                         layout : "form",
                         items : [tfName]
                          }, {
                         columnWidth : 0.5,
                         border : false,
                         layout : "form",
                         items : [cbCallsCodeId]
                  }]
             }]
    });
    
    // 出生日期
    var tfBirthday  = new Ext.form.TextField({
       id : 'birthday',
       name : 'member.birthday',
       style : 'cursor:pointer',
       fieldLabel : "出生日期",
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
       
    // 性别
    var cbSex = new Ext.form.CmbHRCode({
         id    :'sex',
         hiddenName  :'member.sex',
         allowBlank : false,
         fieldLabel :"性别<font color='red'>*</font>",
         type : '性别'
    });
    
     // 第二行面板
     var familyMemberSecondPnl = new Ext.Panel({
          border : false,
          layout : 'form',
          style  :'padding-top:5px',
          items  : [{
              border : false,
              layout : "column",
              items  :[{
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [tfBirthday]
                       },{
                       columnWidth : 0.5,
                       border :false,
                       layout :'form',
                       items :[cbSex]
               }]
           }]
    });
    
    // 籍贯
    var cbNativePlaceId = new Ext.form.CmbHRBussiness({
         id    :'nativePlaceId',
         hiddenName  :'member.nativePlaceId',
         fieldLabel :'籍贯',
         type   :'籍贯'
    });
    
    // 民族
    var cbNationCodeId = new Ext.form.CmbHRBussiness({
         id    :'nationCodeId',
         hiddenName  :'member.nationCodeId',
         fieldLabel :'民族',
         type   :'民族'
    });
    
     //第三行面板
     var familyMemberThirdPnl = new Ext.Panel({
               border : false,
               layout : 'form',
               style  :'padding-top:5px',
               items  : [{
               border : false,
               layout : "column",
               items  :[{
                           columnWidth : 0.5,
                           border : false,
                           layout : "form",
                           items : [cbNativePlaceId]
                        },{
                           columnWidth : 0.5,
                           border : false,
                           layout : "form",
                           items : [cbNationCodeId]
                        }
                   ]
               }]
        });
        
    //学历
    var cbEducationId = new Ext.form.CmbHRBussiness({
         id    :'educationId',
         hiddenName  :'member.educationId',
         fieldLabel :'学历',
         type   :'学历'
    });
        
    //政治面貌
    var cbPoliticsId = new Ext.form.CmbHRBussiness({
         id    :'politicsId',
         hiddenName  :'member.politicsId',
         fieldLabel :'政治面貌',
         type   :'政治面貌'
    });
    
    //第四行面板
    var familyMemberFourthPnl = new Ext.Panel({
        border : false,
        layout : 'form',
        style  :'padding-top:5px',
        items  : [{
        border : false,
        layout : "column",
        items  :[{
                 columnWidth : 0.5,
                 border : false,
                 layout : "form",
                 items : [cbEducationId]
                 },{
                 columnWidth : 0.5,
                 border : false,
                 layout : "form",
                 items : [cbPoliticsId]
                 }
                   ]
               }]
        });
        
  // 单位
  var tfUnit = new Ext.form.TextField({
       id    :'unit',
       name  :'member.unit',
       fieldLabel :'单位',
       width      : twoWd,
       maxLength : 15
    });
    
    // 第五个面板
    var familyMemberFifththPnl = new Ext.Panel({
           border : false,
           layout : 'form',
           style  :'padding-top:5px',
           items  : [{
                   border : false,
                   layout : "column",
                   items  :[{
                           columnWidth : 1.00,
                           border : false,
                           layout : "form",
                           items : [tfUnit]
                }]
           }]
    });
    
    // 婚否
    var rdIfMarried = {
        id : 'ifMarried',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '婚否',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifMarried1',
                boxLabel : '是',
                name : 'member.ifMarried',
                inputValue : '1',
                checked : true
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'ifMarried2',
                boxLabel : '否',
                name : 'member.ifMarried',
                inputValue : '0'
            })
        }]
    };
    
    // 职务名称
    var tfHeadshipName = new Ext.form.TextField({
       id     : 'headshipName',
       name   : 'member.headshipName',
       maxLength :15,
       fieldLabel :'职务名称'
    });
    
    //第六个面板
    var familyMemberSixthPnl = new Ext.Panel({
       border : false,
       layout : 'form',
       style  :'padding-top:5px',
       items  : [{
               border : false,
               layout : "column",
               items  :[{
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [rdIfMarried]
                    }, {
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [tfHeadshipName]
            }]
       }]
    });
    
     //是否直系
    var rdZxqsMark = {
        id : 'zxqsMark',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '是否直系',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'zxqsMark1',
                boxLabel : '是',
                name : 'member.zxqsMark',
                inputValue : '1',
                checked : true
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'zxqsMark2',
                boxLabel : '否',
                name : 'member.zxqsMark',
                inputValue : '0'
            })
        }]
    };
    
    //是否供养
    var rdIfLineally= {
        id : 'ifLineally',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '是否供养',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifLineally1',
                boxLabel : '是',
                name : 'member.ifLineally',
                inputValue : '1',
                checked : true
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'ifLineally2',
                boxLabel : '否',
                name : 'member.ifLineally',
                inputValue : '0'
            })
        }]
    };
    
    //第七个面板
    var familyMemberSeventhPnl = new Ext.Panel({
       border : false,
       layout : 'form',
       style  :'padding-top:5px',
       items  : [{
               border : false,
               layout : "column",
               items  :[{
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [rdZxqsMark]
                    }, {
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [rdIfLineally]
            }]
       }]
    });
    
    // 家庭成员ID
    var hideFamilyId = new Ext.form.Hidden({
        id : 'familymemberid',
        name : 'member.familymemberid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'member.lastModifiedDate'
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
    
    var formPanel = new Ext.form.FormPanel({
        labelAlign :'right',
        labelWidth : 70,
        border : false,
        frame : true,
        items : [familyMemberFirstPnl,familyMemberSecondPnl,
               familyMemberThirdPnl,familyMemberFourthPnl,
               familyMemberFifththPnl,familyMemberSixthPnl,
               familyMemberSeventhPnl, hideFamilyId, hideLastModifiedDate]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 500,
        height : 320,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmpFamilyInfo);
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
    
    var pageFields = ['name',
        	'callsCodeId',
        	'birthday',
        	'sex',
        	'nativePlaceId',
        	'nationCodeId',
        	'educationId',
        	'politicsId',
        	'unit',
        	'headshipName',
        	'ifMarried',
        	'zxqsMark',
        	'ifLineally'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !hideFamilyId.getValue();
    	var record = isAdd ? {} : familyGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['member.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && (
            	prop === 'ifMarried'
                	|| prop === 'zxqsMark'
                	|| prop === 'ifLineally')) {
        		origialV = "1";
        	}
    		if (currentV === 'undefined'
                || currentV === 'null'
                || currentV == null) {
                currentV = "";
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
    
    // 性别
    function renderSex(value) {
        if (value == 'M') {
            return '男';
        } else if (value == 'W') {
            return '女';
        }
        return '';
    }
    
    // 加载员工家庭成员信息 
    function loadEmpFamilyInfo() {
    	// 隐藏弹出画面
    	employee.closeWin('familyMember', win);
    	
        familyStore.baseParams = {
            empId : employee.empId
        };
        
        familyStore.load({
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
        familyStore.reload(options);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增家庭成员
        win.setTitle('新增家庭成员');
        win.show();
        win.center();
    }
    
    // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!familyGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改家庭成员
        win.setTitle('修改家庭成员');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = familyGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 称谓
        cbCallsCodeId.setValue(record.get('callsCodeId'), true);
        // 性别
        cbSex.setValue(record.get('sex'), true);
        // 籍贯
        cbNativePlaceId.setValue(record.get('nativePlaceId'), true);
        // 民族
        cbNationCodeId.setValue(record.get('nationCodeId'), true);
        // 学历
        cbEducationId.setValue(record.get('educationId'), true);
        // 政治面貌
        cbPoliticsId.setValue(record.get('politicsId'), true);
        
        // 婚否
        if (record.get('ifMarried') == '0') {
            Ext.getCmp('ifMarried2').setValue(true);
        }
        // 是否直系
        if (record.get('zxqsMark') == '0') {
            Ext.getCmp('zxqsMark2').setValue(true);
        }
        // 是否供养
        if (record.get('ifLineally') == '0') {
            Ext.getCmp('ifLineally2').setValue(true);
        }
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!familyGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = familyGrid.getSelectionModel().getSelected();
                          
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteEmpFamilyInfo.action',
                        params : {
                            'member.familymemberid' : record.get('familymemberid'),
                            'member.lastModifiedDate' : record.get('lastModifiedDate')
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
        if (tfName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "姓名") + '<br/>';
        }
        if (!cbCallsCodeId.getValue() && cbCallsCodeId.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "称谓") + '<br/>';
        }
        if (!cbSex.getValue() && cbSex.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "性别") + '<br/>';
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 称谓
        if (!cbCallsCodeId.getValue()) {
            cbCallsCodeId.setValue('');
        }
        // 性别
        if (!cbSex.getValue()) {
            cbSex.setValue('');
        }
        // 籍贯
        if (!cbNativePlaceId.getValue()) {
            cbNativePlaceId.setValue('');
        }
        // 民族
        if (!cbNationCodeId.getValue()) {
            cbNationCodeId.setValue('');
        }
        // 学历
        if (!cbEducationId.getValue()) {
            cbEducationId.setValue('');
        }
        // 政治面貌
        if (!cbPoliticsId.getValue()) {
            cbPoliticsId.setValue('');
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
                    var isAddFlag = !hideFamilyId.getValue();
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpFamilyInfo.action',
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
    loadEmpFamilyInfo();
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
