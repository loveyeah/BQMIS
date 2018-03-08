Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    pageInit();
    Ext.QuickTips.init();
    // add by liuyi 091201 标记刷新
    var reflesh = true;
    // 新增或修改,flag='0'更新;flag='1'增加
    var flag = '0';
    var strMethod;
    // 文件路径
    var filePath;
    // 人员工号是否修改
    var change = '0';
    // 双击grid是否能用
    var dbclick = '0';

    // 新增按钮
    var btnAdd = new Ext.Toolbar.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });

    // 修改按钮
    var btnUpdate = new Ext.Toolbar.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : recordUpdate
    });

    // 删除按钮
    var btnDelete = new Ext.Toolbar.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : recordDelete
    });
    
 

    // 附件
    var tfAppend = new Ext.form.TextField({
        id : "xlsFile",
        name : 'xlsFile',
        inputType : 'file',
        width : 200
    });

    // 导入按钮
    var btnInport = new Ext.Toolbar.Button({
        text : '导入',
        handler : uploadQuestFile,
        iconCls : Constants.CLS_UPLOAD
    });

    // 确认按钮
    var btnYes = new Ext.Toolbar.Button({
        text : Constants.BTN_CONFIRM,
        iconCls : Constants.CLS_OK,
        disabled : true,
        handler : importToDatabase
    });

    // **************add by liuyi 091201 查询开始**************************
    var queryText = new Ext.form.TextField({
    	id : 'queryText',
    	width : 120,
    	emptyText : '员工姓名'
    })
     var queryRecords = function(){
     empInfoStore.baseParams={
      chsName:queryText.getValue()
     };
    	
      empInfoStore.load({
    	params : {
    		start : 0,
    		limit : 18
    	}
    })
    }
    var queryBtn = new Ext.Toolbar.Button({
   		text : '查询',
   		iconCls : 'query',
   		handler : queryRecords
    })
    // **************add by liuyi 091201 查询结束**************************
    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : [
        queryText,queryBtn,'-',
        btnAdd, btnUpdate, btnDelete, '-', tfAppend, btnInport, btnYes]
    });

    // 数据解析
    var recordMain = new Ext.data.Record.create([{
            name : 'empCode'
        }, {
            name : 'chsName'
        }, {
            name : 'retrieveCode'
        }, {
            name : 'empState'
        }, {
            name : 'empId'
        }// add by liuyi 20100406 显示新员工编码 
        ,{
        	name : 'newEmpCode'
        }
        ])

    // store
    var empInfoStore = new Ext.data.JsonStore({
        url : 'hr/getEmpInfoList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : recordMain
//        ,
//        baseParams : {
//        	queryText : queryText.getValue()
//        }
    });

    // grid中的列
    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '人员ID',
            dataIndex : 'empId',
            hidden : true

        }, {
            header : '员工状态',
            dataIndex : 'empState',
            hidden : true

        }, {
            header : '员工工号',
            hidden : true,
            dataIndex : 'empCode',
            sortable : true 
        }
        , {
            header : '员工工号',
            dataIndex : 'newEmpCode',
            sortable : true 
        }, {
            header : '姓名',
            dataIndex : 'chsName',
            sortable : true
        }, {
            header : '检索码',
            dataIndex : 'retrieveCode',
            sortable : true
        }])

    // 选择模式
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    var headForm = new Ext.form.FormPanel({
        region : 'north',
        id : 'center-panel',
        height : 28,
        frame : false,
        fileUpload : true,
        labelWidth : 70,
        labelAlign : 'right',
        layout : 'form',
        items : [headTbar]
    });

    // grid
    var empInfoGrid = new Ext.grid.GridPanel({
        border : true,
        sm : sm,
        fitToFrame : true,
        cm : colms,
        store : empInfoStore,
        // tbar : headForm,

        frame : false,
        border : false,
        autoScroll : true,
        region : "center",
        layout : "fit",
        enableColumnMove : false,
        autoSizeColumns : true,
        autoSizeHeaders : false,
         // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : 18,
            store : empInfoStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });

    // 注册双击事件
    empInfoGrid.on("rowdblclick", function() {
        if (dbclick == '0') {
            recordUpdate();
        } else {
        }
    });

   
    
    // 定义form中的单位名称
    // 员工工号
    var txtEmpCode = new Ext.form.CodeField({
        id : "EmpCode",
//        name : 'empCode',
        name : 'newEmpCode',
        fieldLabel : "员工工号<font color='red'>*</font>",
        allowBlank : false,
//        maxLength : 6,
        maxLength : 20,
        codeField : "yes",
        style : {
            'ime-mode' : 'disabled'
        },
        width : 180
    });

    // 姓名
    var txtEmpName = new Ext.form.TextField({
        id : "EmpName",
        name : 'chsName',
        fieldLabel : "姓名<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 12,
        width : 180
    });

    // 检索码
    var txtSearchCode = new Ext.form.CodeField({
        id : "SearchCode",
        name : 'retrieveCode',
        fieldLabel : "检索码",
        maxLength : 10,
        align : 'right',
        codeField : "yes",
        style : {
            'ime-mode' : 'disabled'
        },
        width : 180
    });

    // 人员ID
    var hdEmpId = new Ext.form.Hidden({
        id : "empId"
    });

    // 弹出窗口的panel
    var upPanel = new Ext.FormPanel({
        labelAlign : 'right',
        frame : true,
        items : [txtEmpCode, txtEmpName, txtSearchCode, hdEmpId]
    });

    // 弹出窗口
    var win = new Ext.Window({
        width : 350,
        height : 150,
        modal : true,
        title : '',
        buttonAlign : "center",
        resizable : false,
        closeAction : 'hide',
        items : [upPanel],
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : confirmRecord
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    // 确认取消信息
                    Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    })
                }
            }],
        layout : 'fit'
    });

    // 保存button按下的操作
    function confirmRecord() {
        // 判断人员工号和姓名是否为空
        if (isNotNull()) {
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_001, function(buttonobj) {
                if (buttonobj == 'yes') {
                    // 按下“yes”按钮后，检查form里的textfield的长度是否超过最大长度
                    if (!txtEmpCode.isValid() || !txtEmpName.isValid() || !txtSearchCode.isValid()) {
                        return;
                    } else {
                        // 新增按钮后的操作
                        if (flag == "0") {
                            recordAdd();
                        }
                        // 修改按钮后的操作
                        if (flag == "1") {
                            recordEdit();
                        }
                    }
                }
            });
        }
    }

    // 保存按钮后的修改处理
    function recordEdit() {
        // 检查人员工号是否修改了
        if (change == txtEmpCode.getValue())
            change = '0';
        else
            change = '1';
        var myurl = 'hr/updateEmpInfo.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                // 人员工号是否重复
                if (result.responseText == 'codeRepeat') {
                    Ext.Msg
                    .alert(Constants.SYS_REMIND_MSG, String.format(Constants.PD001_I_019, txtEmpCode.getValue()));
                } else {
                   // empInfoStore.load();
                	queryRecords();
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                        win.hide();
                    });

                }
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                win.hide();
            },
            params : {
                // 向后台传递参数
                empCode : txtEmpCode.getValue(),
                empName : txtEmpName.getValue(),
                searchCode : txtSearchCode.getValue(),
                empId : hdEmpId.getValue(),
                change : change
            }
        });
    }

    // 保存按钮后的增加处理
    function recordAdd() {
        var myurl = 'hr/addEmpInfo.action';
        Ext.Ajax.request({
            method : 'POST',
            url : myurl,
            success : function(result, request) {
                // 人员工号是否重复
                if (result.responseText == 'codeRepeat') {
                    Ext.Msg.alert(Constants.REMIND, String.format(Constants.PD001_I_019, txtEmpCode.getValue()));
                } else {
                   // empInfoStore.load();
                	queryRecords();
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                        win.hide();
                    });

                }
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                win.hide();
            },
            params : {
                // 向后台传递参数
                empCode : txtEmpCode.getValue(),
                empName : txtEmpName.getValue(),
                searchCode : txtSearchCode.getValue(),
                empId : hdEmpId.getValue()
            }
        });
    }

    // 新增按钮按下
    function addRecord() {
        flag = '0';
        upPanel.getForm().reset();
        win.x = undefined;
        win.y = undefined;
        win.show();
        win.setTitle("新增员工工号");

    }

    // 修改按钮按下
    function recordUpdate() {
        // 是否选择一行
        if (empInfoGrid.selModel.hasSelection()) {
            var record = empInfoGrid.getSelectionModel().getSelected();
            var empState = record.get("empState");
            // 不能删除员工状态为'2'的信息
//            if (empState == '2') {
//                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_034);
//                return;
//            }
            flag = '1';
            var record = empInfoGrid.getSelectionModel().getSelected();
            // 弹出编辑窗口
            win.x = undefined;
            win.y = undefined;
            win.show();
            win.setTitle("修改员工工号");

            // form表单初始化
            upPanel.getForm().loadRecord(record);
            change = txtEmpCode.getValue();
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 删除信息
    function recordDelete() {
        // 是否选择一行
        if (empInfoGrid.selModel.hasSelection()) {
            var record = empInfoGrid.getSelectionModel().getSelected();
            var empState = record.get("empState");
            // 不能删除员工状态为'2'的信息
            if (empState == '2')
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_035);
            else {
                Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002, function(buttonobj) {
                    if (buttonobj == 'yes') {
                        var myurl = 'hr/deleteEmpInfo.action';
                        Ext.Ajax.request({
                            method : 'POST',
                            url : myurl,
                            success : function(result, request) {
                               // empInfoStore.load();
                            	queryRecords();
                                Ext.Msg.alert(Constants.REMIND, MessageConstants.DEL_SUCCESS);
                            },
                            failure : function() {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            },
                            params : {
                                // 向后台传递参数
                                empId : record.get("empId")
                            }
                        });
                    }
                });
            }
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    // 页面初始化
    function pageInit() {
        Ext.Ajax.request({
            method : Constants.POST,
            url : 'hr/clearEmpInfoSession.action'
        })
    }

    // 非空check
    function isNotNull() {
        var msg = "";
        // 人员工号判断数据是否为空
        if (txtEmpCode.getValue() == "") {
            msg += String.format(MessageConstants.COM_E_002, "员工工号");
        }
        // 姓名判断数据是否为空
        if (txtEmpName.getValue() == "") {
            if (msg != "") {
                msg += '<br/>' + String.format(MessageConstants.COM_E_002, "姓名");
            } else {
                msg += String.format(MessageConstants.COM_E_002, "姓名");
            }
        }
        // 错误有无判断
        if (msg != "") {
            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
            return false;
        } else {
            return true;
        }
    }

    // 上传附件
    function uploadQuestFile() {
        filePath = tfAppend.getValue();
        // 文件路径为空的情况
        if (filePath == "") {
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_025, tfAppend.getValue()));
            return;
        } else {
            // 取得后缀名并小写
            var suffix = filePath.substring(filePath.length - 3, filePath.length);
            if (suffix.toLowerCase() != 'xls')
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_039);
            else {
                headForm.getForm().submit({
                    method : 'POST',
                    url : 'hr/importFileCheck.action',
                    success : function(form, action) {
                        var o = eval("(" + action.response.responseText + ")");
                        // 是否为空
                        if (o.type == "E") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_028, o.param1, o.param2));
                            return;
                        }
                        // 长度过长
                        if (o.type == "L") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_029, o.param1, o.param2));
                            return;
                        }
                        // 是否字符
                        if (o.type == "G") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_042, o.param1, o.param2));
                            return;
                        }
                        // 是否半角英数
                        if (o.type == "H") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_031, o.param1, o.param2));
                            return;
                        }
                        // 员工编码是否重复
                        if (o.type == "D") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_046, o.param1, o.param2));
                            return;
                        }
                        // excel中员工工号是否重复
                        if (o.type == "S") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_047, o.param1, o.param2,
                            o.param3));
                            return;
                        }
                        // excel文件内容格式不正确
                        if (o.type == "W") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_041));
                            return;
                        }
                        // 文件不存在
                        if (o.type == "U") {
                            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_025, tfAppend.getValue()));
                            return;
                        }
                        // 文件内容为空
                         if (o.type == "O") {
                            Ext.Msg.alert(Constants.REMIND, String.format(Constants.COM_I_015));
                            return;
                        }
                        // 某个数字大于最大值
			             if (o.type == "A") {
			                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_044, o.param1, o.param2));
			                return;
			            }
			             // 某个数字不是整数
			            if (o.type == "B") {
			                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_043, o.param1, o.param2));
			                return;
			            }
                        if (o.msg == Constants.IO_FAILURE) {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_041);
                            return;
                        } else {
                           // empInfoStore.load();
                        	queryRecords();
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_013);
                            btnAdd.setDisabled(true);
                            btnDelete.setDisabled(true);
                            btnUpdate.setDisabled(true);
                            tfAppend.setDisabled(true);
                            btnInport.setDisabled(true);
                            btnYes.setDisabled(false);
                            // 双击grid不能使用
                            dbclick = '1';
                        }
                    },
                    failture : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    }
                })
            }
        }
    }

    // 导入数据库
    function importToDatabase() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
            if (buttonobj == "yes") {
                Ext.Ajax.request({
                    method : 'POST',
                    url : "hr/importToDatabase.action",
                    success : function() {
                    	//empInfoStore.load();// modify by ywliu 2009/9/7
                    	queryRecords();
                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014, function() {
                            //empInfoStore.load();
                        	queryRecords();
                        });
                    }
                });
                // 双击grid可以使用
                dbclick = '0';
                btnAdd.setDisabled(false);
                btnDelete.setDisabled(false);
                btnUpdate.setDisabled(false);
                tfAppend.setDisabled(false);
                btnInport.setDisabled(false);
                btnYes.setDisabled(true);
            }
        })
    }

    // ↑↑********弹出窗口*********↑↑//

    var layout = new Ext.Viewport({
        layout : 'border',
        border : false,
        enableTabScroll : true,
        border : false,
        items : [{
                region : 'north',
                layout : 'fit',
                border : false,
                margins : '0 0 0 0',
                height : 25,
                items : [headForm]
            }, {
                title : "",
                region : 'center',
                layout : 'fit',
                border : false,
                margins : '0 0 0 0',
                split : true,
                collapsible : false,
                items : [empInfoGrid]
            }]
    });
    
    // 页面初期化数据加载
    
    Ext.Msg.wait('正在加载数据，请稍候……','提示');
//    empInfoStore.load();
    
    empInfoStore.load({
    	params : {
    		start : 0,
    		limit : 18
    	}
    })
	empInfoStore.on('load',function(){
		if(reflesh)
		{
			Ext.Msg.hide();
			reflesh = false;
		}
//		win.hide();
	})
})
