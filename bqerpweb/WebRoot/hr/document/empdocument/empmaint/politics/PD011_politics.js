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
    
    
    var politicsRecord = Ext.data.Record.create([{
            // 政治面貌
            name : 'politicsName'
        }, {
            // 参加日期
            name : 'joinDate'
        }, {
            // 退出日期
            name : 'exitDate'
        }, {
            // 参加单位
            name : 'joinUnit'
        }, {
            // 参加地点
            name : 'joinPlace'
        }, {
            // 所属组织
            name : 'belongUnit'
        }, {
            // 介绍人
            name : 'introducer'
        }, {
            // 介绍人单位
            name : 'introducerUnit'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 是否最新标志
            name : 'isNewMark'
        }, {
            // 政治面貌Id
            name : 'politicsId'
        }, {
            // 政治面貌登记Id
            name : 'politicsid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    }]);
    
    // grid列模式
    var politicsCM = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '政治面貌',
            width : 80,
            dataIndex : 'politicsName'
        }, {
            header : '参加日期',
            width : 100,
            dataIndex : 'joinDate'
        }, {
            header : '退出日期',
            width : 100,
            dataIndex : 'exitDate'
        }, {
            header : '参加单位',
            width : 120,
            dataIndex : 'joinUnit'
        }, {
            header : '参加地点',
            width : 120,
            dataIndex : 'joinPlace'
        }, {
            header : '所属组织',
            width : 120,
            dataIndex : 'belongUnit'
        }, {
            header : '介绍人',
            width : 60,
            dataIndex : 'introducer'
        }, {
            header : '介绍人单位',
            width : 100,
            dataIndex : 'introducerUnit'
        }, {
            header : '备注',
            width : 150,
            dataIndex : 'memo'
    }]);
    politicsCM.defaultSortable = true;
    
    
    // 数据源
    var politicsStore = new Ext.data.JsonStore({
        url : 'hr/getEmpPoliticsInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : politicsRecord
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : politicsStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
    
    // 政治面貌Grid
    var politicsGrid = new Ext.grid.GridPanel({
        store : politicsStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : politicsCM,
        // 分页
        bbar : pagebar,
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    politicsGrid.on('rowdblclick', onModify);
    politicsGrid.on('celldblclick', showWindow);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [politicsGrid]
    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 150;
    Ext.form.CmbHRBussiness.prototype.width = 150;
    var twoWd = 387;
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'chsName',
        fieldLabel : "员工姓名",
        readOnly : true,
        value : employee.chsName
    });
    
    // 政治面貌
    var cbPoliticsId = new Ext.form.CmbHRBussiness({
         id    :'politicsId',
         hiddenName  :'politics.politicsId',
         fieldLabel :"政治面貌<font color='red'>*</font>",
         allowBlank : false,
         type :drpTypes[2]
    });
    
    //第一面板
    var politicsFirstPnl = new Ext.Panel({
         border : false,
         layout : 'form',
         style  :'padding-top:2px',
         items  : [{
             border : false,
             layout : "column",
             items  :[{
                     columnWidth : 0.5,
                     border : false,
                     layout : "form",
                     items : [tfChsName]
                  }, {
                     columnWidth : 0.5,
                     border : false,
                     layout : "form",
                     items : [cbPoliticsId]
             }]
       }]
    });
    
    // 参加日期
    var tfJoinDate = new Ext.form.TextField({
        id : 'joinDate',
        name : 'politics.joinDate',
        style : 'cursor:pointer',
        fieldLabel : "参加日期<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfJoinDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfJoinDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 所属组织
    var tfBelongUnit = new Ext.form.TextField({
        id : 'belongUnit',
        name  :'politics.belongUnit',
        fieldLabel : "所属组织",
        maxLength : 15
    });
        
     //第二行面板
     var politicsSecondPnl = new Ext.Panel({
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
                   items : [tfJoinDate]
                   },{
                   columnWidth : 0.5,
                   border :false,
                   layout :'form',
                   items :[tfBelongUnit]
                   }
                   ]
               }]
        });
        
    // 参加单位
    var tfJoinUnit = new Ext.form.TextField({
         id    :'joinUnit',
         name  :'politics.joinUnit',
         fieldLabel :'参加单位',
         maxLength :25,
         width:  twoWd
    });
        
     // 第三行面板
     var politicsThirdPnl = new Ext.Panel({
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
                           items : [tfJoinUnit]
                        }
                   ]
               }]
    });
    
     // 参加地点
     var tfJoinPlace = new Ext.form.TextField({
           id    :'joinPlace',
           name  :'politics.joinPlace',
           fieldLabel :'参加地点',
           maxLength : 15,
           width:  twoWd
    });
        
      // 第四行面板
      var politicsFourthPnl = new Ext.Panel({
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
                           items : [tfJoinPlace]
                    }]
               }]
    });
    
    // 介绍人
    var tfIntroducer = new Ext.form.TextField({
        id   :'introducer',
        name :'politics.introducer',
        fieldLabel :'介绍人',
        maxLength : 10
    });
    
    //介绍人单位
    var tfIntroducerUnit = new Ext.form.TextField({
       id    :'introducerUnit',
       name  :'politics.introducerUnit',
       fieldLabel :'介绍人单位',
       maxLength : 15
    });

    //第五个面板
    var politicsFifththPnl = new Ext.Panel({
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
                           items : [tfIntroducer]
                        }, {
                           columnWidth : 0.5,
                           border : false,
                           layout : "form",
                           items : [tfIntroducerUnit]
                 }]
           }]
    });
    
     // 退出日期
     var tfExitDate = new Ext.form.TextField({
           id : 'exitDate',
           name : 'politics.exitDate',
           fieldLabel : "退出日期",
           style : 'cursor:pointer',
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
    
    //最新标志
    var rdIsNewMark = {
        id : 'isNewMark',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '最新标志',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'isNewMark1',
                boxLabel : '是',
                name : 'politics.isNewMark',
                inputValue : '1',
                checked : true
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'isNewMark2',
                boxLabel : '否',
                name : 'politics.isNewMark',
                inputValue : '0'
            })
        }]
    };

    //备注
    var taMemo = new Ext.form.TextArea({
       id     : 'memo',
       name   : 'politics.memo',
       maxLength :128,
       width  : 150 - 2,
       fieldLabel :'备注'
    });
    
    //第六个面板
    var politicsSixthPnl = new Ext.Panel({
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
                       items : [tfExitDate,rdIsNewMark]
                    }, {
                       columnWidth : 0.5,
                       border : false,
                       layout : "form",
                       items : [taMemo]
            }]
       }]
    });
    
    // 政治面貌ID
    var hidePoliticsId = new Ext.form.Hidden({
        id : 'politicsid',
        name : 'politics.politicsid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'politics.lastModifiedDate'
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
        items : [politicsFirstPnl,politicsSecondPnl,
            politicsThirdPnl,politicsFourthPnl,
            politicsFifththPnl,politicsSixthPnl,
            hidePoliticsId, hideLastModifiedDate]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 500,
        height : 300,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmpPoliticsInfo);
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
    
    var pageFields = ['politicsId',
        	'joinDate',
        	'belongUnit',
        	'joinUnit',
        	'joinPlace',
        	'introducer',
        	'introducerUnit',
        	'exitDate',
        	'isNewMark',
        	'memo'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !hidePoliticsId.getValue();
    	var record = isAdd ? {} : politicsGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['politics.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && (prop === 'isNewMark')) {
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
    
    // 加载员工政治面貌信息 
    function loadEmpPoliticsInfo() {
    	// 隐藏弹出画面
    	employee.closeWin('politics', win);
    	
        politicsStore.baseParams = {
            empId : employee.empId
        };
        
        politicsStore.load({
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
        politicsStore.reload(options);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增政治面貌
        win.setTitle('新增政治面貌');
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
        if (!politicsGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改政治面貌
        win.setTitle('修改政治面貌');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = politicsGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 政治面貌
        cbPoliticsId.setValue(record.get('politicsId'), true);
        
        // 最新标志
        if (record.get('isNewMark') == '0') {
            Ext.getCmp('isNewMark2').setValue(true);
        }
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!politicsGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = politicsGrid.getSelectionModel().getSelected();
                       
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteEmpPoliticsInfo.action',
                        params : {
                            'politics.politicsid' : record.get('politicsid'),
                            'politics.lastModifiedDate' : record.get('lastModifiedDate')
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
        if (!cbPoliticsId.getValue() && cbPoliticsId.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "政治面貌") + '<br/>';
        }
        if (tfJoinDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "参加日期") + '<br/>';
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
    	if (tfExitDate.getValue() !== '') {
            if (!compareDateStr(tfJoinDate.getValue(), tfExitDate.getValue())) {
                msg += String.format(Constants.COM_E_006, "参加日期", "退出日期") + '<br/>';
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
			        
                    var isAddFlag = !hidePoliticsId.getValue();
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpPoliticsInfo.action',
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
    loadEmpPoliticsInfo();
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
