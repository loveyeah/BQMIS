Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

    // 考勤部门Store
    var deptStore = new Ext.data.JsonStore({
        url : 'ca/getDataByWriterCheckerId.action',
        root : 'list',
        fields : ['attendanceDeptId', 'attendanceDeptName', 'lastModifiyBy', 'attendWriterId', 'attendCheckerId']
    })

    // 考勤部门
    var txtAttendanceDept = new Ext.form.ComboBox({
        fieldLabel : '考勤部门<font color ="red">*</font>',
        width : 180,
        allowBlank : false,
        readOnly : true,
        store : deptStore,
        disabled : true,
        mode : 'local',
        triggerAction : 'all',
        displayField : "attendanceDeptName",
        valueField : "attendanceDeptId",
        listeners : {
            select : function() {
            	if(txtAttendanceDept.getValue() != null &&　txtAttendanceDept.getValue() != "") {
                storeAttendanceDept.load({
                    params : {
                        deptId : txtAttendanceDept.getValue()
                    },
                    callback : function() {
                        for (i = 0; i < deptStore.getCount(); i++) {
                            if (deptStore.getAt(i).get('attendanceDeptId') == txtAttendanceDept.getValue()) {
                                // 如果当前用户是该部门的考勤登记人，就设置考勤登记人可用
                                if (deptStore.getAt(i).get('lastModifiyBy') == deptStore.getAt(i).get('attendWriterId')) {
									txtAttendWriter.setDisabled(false);
                                }else {
                                	// 否则只是不可用
                                	txtAttendWriter.setDisabled(true);
                                }
                              	// 如果当前用户是该部门的考勤审核人，就设置考勤审核人可用
                                if (deptStore.getAt(i).get('lastModifiyBy') == deptStore.getAt(i).get('attendCheckerId')) {
									txtAttendChecker.setDisabled(false);
                                }else {
                                	txtAttendChecker.setDisabled(true);
                                }
                                break;
                            }
                        }
                    }
                })
            	}else {
            		// 如果选择空值，就设置所有值为空
            		clearValue();
            		// 考勤登记人
            		txtAttendWriter.setDisabled(true);
            		// 考勤审核人
            		txtAttendChecker.setDisabled(true);
            		txtAttendanceDept.markInvalid();
            	}
            }
        }
    })

    deptStore.on('load', function() {
        if (deptStore.getCount() <= 0) {
            // 设置考勤部门不可使用
            txtAttendanceDept.setDisabled(true);
            // 考勤登记人
            txtAttendWriter.setDisabled(true);
            // 考勤审核人
            txtAttendChecker.setDisabled(true);
            // 保存按钮不可用
            btnDeptSave.setDisabled(true);
            // 取消按钮不可用
            btnDeptCancel.setDisabled(true);
        } else {
            // 设置考勤部门不可使用
            txtAttendanceDept.setDisabled(false);
            // 考勤登记人
            txtAttendWriter.setDisabled(true);
            // 考勤审核人
            txtAttendChecker.setDisabled(true);
            // 保存按钮不可用
            btnDeptSave.setDisabled(false);
            // 取消按钮不可用
            btnDeptCancel.setDisabled(false);
            deptStore.insert(0, new Ext.data.Record({
				attendanceDeptId : '',
				attendanceDeptName : ''
			}));
        }
    })

    // 查询考勤部门信息
    deptStore.load();

    // 存储窗口信息
    var storeAttendanceDept = new Ext.data.JsonStore({
        url : 'ca/getAttendanceDeptSingleInfo.action',
        root : 'list',
        fields : ['id', 'attendanceDeptId', 'attendanceDeptName', 'attendDepType', 'topCheckDepId', 'topCheckDepName',
        'replaceDepId', 'replaceDepName', 'attendWriterId', 'attendWriterName', 'attendCheckerId', 'attendCheckerName']
    })
    storeAttendanceDept.on('load', function() {
        if (storeAttendanceDept.getCount() > 0) {
            hiddenId.setValue(storeAttendanceDept.getAt(0).get('id'));
            // 考勤部门类别
            txtAttendanceKind.setValue(storeAttendanceDept.getAt(0).get('attendDepType'));
            if (txtAttendanceKind.getValue() == "3") {
                // 代考勤部门
                storeDept.getAt(0).data['replaceDepId'] = storeAttendanceDept.getAt(0).get('replaceDepId');
                storeDept.getAt(0).data['replaceDepName'] = storeAttendanceDept.getAt(0).get('replaceDepName');
                txtReplaceDep.setValue(storeAttendanceDept.getAt(0).get('replaceDepId'));
                // 考勤审核人
                txtAttendChecker.setValue("");
                txtAttendChecker.setDisabled(true);
            } else {
                if (txtAttendanceKind.getValue() == "2") {
                    // 考勤审核人
                    storeDept.getAt(0).data['attendCheckerId'] = storeAttendanceDept.getAt(0).get('attendCheckerId');
                    storeDept.getAt(0).data['attendCheckerName'] = storeAttendanceDept.getAt(0)
                    .get('attendCheckerName');
                    txtAttendChecker.setValue(storeAttendanceDept.getAt(0).get('attendCheckerId'));
                    txtAttendChecker.setDisabled(false);
                } else {
                    // 考勤审核人
                    txtAttendChecker.setValue("");
                    txtAttendChecker.setDisabled(true);
                }
                // 代考勤部门
                txtReplaceDep.setValue("");
            }
            // 上级审核部门
            storeDept.getAt(0).data['topCheckDepId'] = storeAttendanceDept.getAt(0).get('topCheckDepId');
            storeDept.getAt(0).data['topCheckDepName'] = storeAttendanceDept.getAt(0).get('topCheckDepName');
            txtTopCheckDep.setValue(storeAttendanceDept.getAt(0).get('topCheckDepId'));
            // 考勤登记人
            storeDept.getAt(0).data['attendWriterId'] = storeAttendanceDept.getAt(0).get('attendWriterId');
            storeDept.getAt(0).data['attendWriterName'] = storeAttendanceDept.getAt(0).get('attendWriterName');
            txtAttendWriter.setValue(storeAttendanceDept.getAt(0).get('attendWriterId'));
        }
         // 去掉红线
     clearWinInvalid();
	})

    // 考勤部门维护表ID
    var hiddenId = new Ext.form.Hidden({
        id : 'hiddenId'
    })

    // 考勤类别
    var txtAttendanceKind = new Ext.form.CmbCACode({
        type : "考勤部门类别",
        width : 180,
        allowBlank : false,
        disabled : true,
        fieldLabel : '考勤类别'
    });

    // 主页面显示Store
    var storeDept = new Ext.data.JsonStore({
        fields : ['replaceDepId', 'replaceDepName', 'topCheckDepId', 'topCheckDepName', 'attendWriterId',
        'attendCheckerName', 'attendCheckerId', 'attendCheckerName']
    })
    storeDept.insert(0, new Ext.data.Record({
        'replaceDepId' : '',
        'replaceDepName' : '',
        'topCheckDepId' : '',
        'topCheckDepName' : '',
        'attendWriterId' : '',
        'attendCheckerName' : '',
        'attendCheckerId' : '',
        'attendCheckerName' : ''
    }))
    // 代考勤部门
    var txtReplaceDep = new Ext.form.ComboBox({
        fieldLabel : '代考勤部门',
        hiddenName : 'replaceDepId',
        width : 180,
        readOnly : true,
        store : storeDept,
        disabled : true,
        displayField : "replaceDepName",
        valueField : "replaceDepId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 上级审核部门
    var txtTopCheckDep = new Ext.form.ComboBox({
        fieldLabel : '上级审核部门',
        hiddenName : 'topCheckDepId',
        width : 180,
        disabled : true,
        store : storeDept,
        displayField : "topCheckDepName",
        valueField : "topCheckDepId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤登记人
    var txtAttendWriter = new Ext.form.ComboBox({
        fieldLabel : '考勤登记人<font color ="red">*</font>',
        readOnly : true,
        allowBlank : false,
        store : storeDept,
        disabled : true,
        width : 180,
        displayField : "attendWriterName",
        valueField : 'attendWriterId',
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤登记人的onClick事件
    txtAttendWriter.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
        'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            storeDept.getAt(0).data['attendWriterId'] = person.empId;
            storeDept.getAt(0).data['attendWriterName'] = person.workerName;
            txtAttendWriter.setValue(person.empId);
        }
    })

    // 考勤审核人
    var txtAttendChecker = new Ext.form.ComboBox({
        fieldLabel : '考勤审核人<font color ="red">*</font>',
        readOnly : true,
        width : 180,
        allowBlank : false,
        disabled : true,
        store : storeDept,
        displayField : "attendCheckerName",
        valueField : 'attendCheckerId',
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤审核人的onClick事件
    txtAttendChecker.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
        'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            storeDept.getAt(0).data['attendCheckerId'] = person.empId;
            storeDept.getAt(0).data['attendCheckerName'] = person.workerName;
            txtAttendChecker.setValue(person.empId);
        }
    })

    // 保存按钮
    var btnDeptSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
         handler : saveDeptData
	});

    // 取消按钮
    var btnDeptCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
         handler : cancel
	});

    // 第一行
    var fldSetFirstLine = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        width : 600,
        style : "padding-top:30px;padding-bottom:0px;border:0px",
        items : [{
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtAttendanceDept]
        }, {
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtAttendanceKind, hiddenId]
        }]
    })

    // 第二行
    var fldSetSecondLine = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        width : 600,
        style : "padding-top:0px;padding-bottom:0px;border:0px",
        items : [{
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtReplaceDep]
        }, {
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtTopCheckDep]
        }]
    })

    // 第三行
    var fldSetThirdLine = new Ext.form.FieldSet({
        border : false,
        layout : 'column',
        width : 600,
        style : "padding-top:0px;padding-bottom:0px;border:0px",
        items : [{
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtAttendWriter]
        }, {
            columnWidth : 0.5,
            border : false,
            layout : 'form',
            items : [txtAttendChecker]
        }]
    })
    // 部门基本信息fldSet
    var fldSetWebpage = new Ext.Panel({
        frame : false,
        layout : 'form',
        tbar : [btnDeptSave, btnDeptCancel],
//     	style : "padding-top:45px;padding-left:60px;padding-right:60px;border:0px",
//        width : 750,
//        height : 250,
        width:640,
        height:200,
        border:false,
        style : "border-right:1px solid;border-bottom:1px solid",
        labelAlign : 'right',
        items : [fldSetFirstLine, fldSetSecondLine, fldSetThirdLine]
    })
    // 考勤部门设置FormPanel
    var formDept = new Ext.form.FormPanel({
        border : false,
        labelAlign : 'right',
        frame : false,
         autoScroll : true,
        enableTabScroll : true,
        containerScroll : true,
        layout : 'form',
        items : [fldSetWebpage]
    })

    // 显示区域
    new Ext.Viewport({
        layout : "border",
        border : false,
        items : [{
            region : 'center',
            layout : 'fit',
            border : false,
            frame:false,
            margins : '0 0 0 0',
            split : true,
            collapsible : false,
            items : [formDept]
        }]
    });
    
    /**
     * 去掉红线提示
     */
    function clearWinInvalid() {
        // 考勤部门
        txtAttendanceDept.clearInvalid();
        // 考勤部门类别
        txtAttendanceKind.clearInvalid();
        // 代考勤部门
        txtReplaceDep.clearInvalid();
        // 上级审核部门
        txtTopCheckDep.clearInvalid();
        // 考勤登记人
        txtAttendWriter.clearInvalid();
        // 考勤审核人
        txtAttendChecker.clearInvalid();
    }
    
    /**
     * 保存考勤部门
     */
    function saveDeptData() {
    	// 空mesage信息
    	 var message = "";
    	 // check考勤部门是否为空
    	if (txtAttendanceDept.getValue() != null && txtAttendanceDept.getValue() != "") {
        } else {
            message += String.format(Constants.COM_E_003, "考勤部门") + "</br>";
        }
        // 如果考勤审核人是可以输入的
        if(!txtAttendChecker.disabled) {
        	// check考勤审核人是否为空
        	 if (txtAttendChecker.getValue() != null && txtAttendChecker.getValue() != "") {
                } else {
                    message += String.format(Constants.COM_E_003, "考勤审核人") + "</br>";
                }
        }
        //　如果考勤审核人是可输入的
        if(!txtAttendWriter.disabled){
        	// check考勤登记人是否为空
        	 if (txtAttendWriter.getValue() != null && txtAttendWriter.getValue() != "") {
			    } else {
			        message += String.format(Constants.COM_E_003, "考勤登记人") + "</br>";
			    }
        }
        // 如果空message非空，弹出提示
        if (message != "")
            Ext.Msg.alert(Constants.ERROR, message);
        else {
        	Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        Ext.Ajax.request({
                            method : Constants.POST,
                            url : 'ca/modifyWriterChecker.action',
                            params : {
                                id:hiddenId.getValue(),
                                attendWriterId : txtAttendWriter.getValue(),
                                attendCheckerId : txtAttendChecker.getValue()
                            },
                            success : function(result, request) {
                                var o = eval("(" + result.responseText + ")");
                                var succ = o.msg;
                                // 如果更新失败，弹出提示
                                if (succ == Constants.SQL_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                } else {
                                		
									for (i = 0; i < deptStore.getCount(); i++) {
				                        if (deptStore.getAt(i).get('attendanceDeptId') == txtAttendanceDept.getValue()) {
				                            // 如果当前用户是该部门的考勤登记人，就设置考勤登记人可用
				                        	var flag = 0;
				                            if (deptStore.getAt(i).get('lastModifiyBy') != txtAttendWriter.getValue()) {
												flag += 1;
												txtAttendWriter.setDisabled(true);
												deptStore.getAt(i).data['attendWriterId']= "";
				                            }
				                          	// 如果当前用户是该部门的考勤审核人，就设置考勤审核人可用
				                            if (deptStore.getAt(i).get('lastModifiyBy') != txtAttendChecker.getValue()) {
												flag +=1;
												txtAttendChecker.setDisabled(true);
												deptStore.getAt(i).data['attendCheckerId'] = "";
				                            }
				                            // 如果当前登记人和当前审核人都不是当前用户就初始化画面
				                            if(flag == 2){
				                            	// 查询考勤部门信息
					                        	clearValue();
												deptStore.load();
				                            }
				                        }
				                    }
                                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                    })
                                }
                            }
                        });
                    }
                })
        }
    }
    /**
	 * 点击取消按钮时
	 */
	function cancel() {
		 Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
            	clearValue();
            	deptStore.load();
            }
        })
	}
	
	/**
	 * 清空值
	 */
	function clearValue() {
        // 考勤部门
        txtAttendanceDept.setValue("");
        // 考勤部门类别
        txtAttendanceKind.setValue("");
        // 代考勤部门
        txtReplaceDep.setValue("");
        // 上级审核部门
        txtTopCheckDep.setValue("");
        // 考勤登记人
        txtAttendWriter.setValue("");
        // 考勤审核人
        txtAttendChecker.setValue("");
        clearWinInvalid();
	}
})