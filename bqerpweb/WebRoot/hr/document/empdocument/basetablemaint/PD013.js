/**
 * 基本表维护
 * @since 2009-02-09
 * @author 黄维杰
 * @version 1.0
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 选中的表名
    var tableName = "";
    // 要维护的表
    var TABLE1 = "称谓编码表";
    var TABLE2 = "学位编码表";
    var TABLE3 = "学历编码表";
    var TABLE4 = "语种编码表";
    var TABLE5 = "民族编码表";
    var TABLE6 = "籍贯编码表";
    var TABLE7 = "政治面貌表";
    var TABLE8 = "学校名称表";
    var TABLE9 = "学习类别表";
    var TABLEa = "学习专业表";
    var TABLEb = "技术等级表";
    var TABLEc = "员工身份表";
    var TABLEd = "员工类别表";
    // 各表的名字字段和检索码字段长度
    var TABLE_1589A_NAME = 15;
    var TABLE_1589A_CODE = 8;

    var TABLE_2367BCD_NAME = 50;
    var TABLE_2367BCD_CODE = 20;

    var TABLE_4_NAME = 10;
    var TABLE_4_CODE = 8;
    /**************弹出窗口*****************/
    // 定义弹出窗体
    var TXTWIDTH = 180;
    // 保存按钮
    var btnSave = new Ext.Button({
        text : "保存",
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    // 取消按钮
    var btnCancel = new Ext.Button({
        text : "取消",
        iconCls : Constants.CLS_CANCEL,
        handler : onCancle
    });

    // ID
    var txtID = new Ext.form.TextField({
        id : 'recordId',
        fieldLabel : 'ID',
        disabled : true,
        maxLength : 10,
        width : TXTWIDTH
    })
    // 名称
    var txtName = new Ext.form.TextField({
        id : 'recordName',
        fieldLabel : "名称<font color='red'>*</font>",
        allowBlank : false,
        width : TXTWIDTH
    })
    // 检索码
    var txtCode = new Ext.form.CodeField({
        id : 'retrieveCode',
        fieldLabel : '检索码',
        width : TXTWIDTH
    })
    // 显示顺序
    var txtOrder = new Ext.form.NaturalNumberField({
        id : 'orderBy',
        fieldLabel : '显示顺序',
        width : TXTWIDTH,
        maxLength : 10
    })
    // 定义弹出窗体中的form
    var myPanel = new Ext.FormPanel({
        autoScroll : true,
        labelAlign : 'right',
        border : false,
        frame : true,
        labelWidth : 100,
        items : [txtID, txtName, txtCode, txtOrder]
    });
    // 弹出编辑窗口
    var winEdit = new Ext.Window({
        width : 350,
        height : 190,
        title : "",
        buttonAlign : "center",
        items : [myPanel],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [btnSave, btnCancel]
    });
    /**************弹出窗口结束*****************/
    // 表名数据源
    var myData = [
    ["称谓编码表"], ["学位编码表"], ["学历编码表"], 
    ["语种编码表"], ["民族编码表"], ["籍贯编码表"], 
    ["政治面貌表"], ["学校名称表"], ["学习类别表"],
    ["学习专业表"], ["技术等级表"], ["员工身份表"], 
    ["员工类别表"]];
    // create the data store
    var myStore = new Ext.data.SimpleStore({
        fields : [{
                name : 'tableName'
            }]
    });
    myStore.loadData(myData);

    // 创建grid
    var leftGrid = new Ext.grid.GridPanel({
        store : myStore,
        region : 'west',
        title : "基础表一览",
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : '行号',
                width : 35,
                align : 'left'
            }), {
                id : 'tableName',
                header : "表名称",
                width : 109,
                dataIndex : 'tableName'
            }],
        viewConfig : {
            forceFit : false
        },
        border : true,
        frame : false,
        enableColumnHide : true,
        enableColumnMove : false,
        style : {
            'margin-top' : '0px',
            'padding' : 0,
            'padding-top' : '0px'
        },
        height : 350,
        width : 150
    });
    // 设置响应单击行事件
    leftGrid.on('rowclick', onShow);
    // 左边grid的fieldset
    var leftSet = new Ext.form.FieldSet({
        width : 150,
        region : 'west',
        title : "基础表一览",
        layout : 'fit',
        border : true,
        frame : false,
        style : {
            'margin-top' : '0px',
            'padding' : 0,
            'padding-top' : '2px'
        },
        items : [{
                layout : 'fit',
                items : [leftGrid]
            }]
    });

    var runGridList = new Ext.data.Record.create([{
            name : 'recordId'
        }, {
            name : 'recordName'
        }, {
            name : 'retrieveCode'
        }, {
            name : 'orderBy'
        }]);
    // grid的store
    var searchStore = new Ext.data.JsonStore({
        url : 'hr/getRecordList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : runGridList
    });
    var myColumn = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : '行号',
            sortable : true,
            width : 35,
            align : 'left'
        }), {
            header : "ID",
            width : 70,
            sortable : true,
            dataIndex : 'recordId'
        }, {
            header : "名称",
            width : 200,
            sortable : true,
            dataIndex : 'recordName'
        }, {
            header : "检索码",
            width : 200,
            sortable : true,
            dataIndex : 'retrieveCode'
        }, {
            header : "显示顺序",
            width : 85,
            sortable : true,
            dataIndex : 'orderBy'
        }]);
    // 创建grid
    var rightGrid = new Ext.grid.GridPanel({
        store : searchStore,
        region : 'center',
        title : "称谓编码表",
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        cm : myColumn,
        viewConfig : {
            forceFit : false
        },
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : searchStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        border : true,
        frame : false,
        enableColumnHide : true,
        enableColumnMove : false,
        height : 350,
        width : 600
    });
    rightGrid.on('rowdblclick', onModify);
    // 初始化为称谓编码表
    searchStore.baseParams = {
        tableName : TABLE1
    }
    searchStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    txtName.maxLength = TABLE_1589A_NAME;
    txtCode.maxLength = TABLE_1589A_CODE;
    
    // tbar 的button
    // 新增按钮
    var addBtn = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : onAdd
    });
    // 修改按钮
    var modifyBtn = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : onModify
    });
    // 删除按钮
    var deleteBtn = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : onDelete
    });
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : 'fit',
        border : false,
        frame : true,
        items : [{
                layout : 'border',
                border : false,
                tbar : [addBtn, modifyBtn, deleteBtn],
                items : [leftGrid, rightGrid]
            }]
    });

    /*******************响应函数**************************/
    /**
     * 点击左边grid，右边grid打开某个表
     */
    function onShow() {
        // 打开某个表
        if (leftGrid.selModel.hasSelection()) {
            var selectedRow = leftGrid.selModel.getSelected();
            var tName = selectedRow.get("tableName");
            rightGrid.setTitle(tName);

            if (tName == TABLE1 || tName == TABLE5 || tName == TABLE8 || tName == TABLE9 || tName == TABLEa) {
                txtName.maxLength = TABLE_1589A_NAME;
                txtCode.maxLength = TABLE_1589A_CODE;
            } else if (tName == TABLE2 || tName == TABLE3 || tName == TABLE6 || tName == TABLE7 || tName == TABLEb
            || tName == TABLEc || tName == TABLEd) {
                txtName.maxLength = TABLE_2367BCD_NAME;
                txtCode.maxLength = TABLE_2367BCD_CODE;
            } else if (tName == TABLE4) {
                txtName.maxLength = TABLE_4_NAME;
                txtCode.maxLength = TABLE_4_CODE;
            }
            tableName = selectedRow.get("tableName");
            searchStore.baseParams = {
                tableName : tableName
            }
            searchStore.load({
                params : {
                    start : 0,
                    limit : Constants.PAGE_SIZE
                }
            });
        }
    }

    /**
     * 增加记录
     */
    function onAdd() {
        myPanel.getForm().reset();
        var str = "新增";
        // 加上表名
        str += rightGrid.title;
        // 把“表”字去掉
        str = str.substring(0, str.length - 1);
        txtID.setValue("自动生成");
        winEdit.setTitle(str);
        // 把弹出窗口的位置初始化到中间
        winEdit.x = undefined;
        winEdit.y = undefined;
        winEdit.show();
    }
    /**
     * 修改记录
     */
    function onModify() {
        if (rightGrid.selModel.hasSelection()) {
            var selectedRow = rightGrid.selModel.getSelected();
            var str = "修改";
            str += rightGrid.title;
            str = str.substring(0, str.length - 1);
            winEdit.setTitle(str);
            // 把弹出窗口的位置初始化到中间
            winEdit.x = undefined;
            winEdit.y = undefined;
            winEdit.show();
            myPanel.getForm().loadRecord(selectedRow);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
        }
    }
    /**
     * 删除记录
     */
    function onDelete() {
        if (rightGrid.selModel.hasSelection()) {
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = rightGrid.selModel.getSelected();
                    // 获得记录的申请单号
                    var recordId = record.get("recordId");
                    // 获得表名
                    var tableName = rightGrid.title;
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteBaseTableRecord.action',
                        success : function(result, request) {
                            // 成功，显示删除成功信息
                            var o = eval("(" + result.responseText + ")");
                            // 数据库异常
                            if (o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                // 删除成功
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                                searchStore.load({
	                                    params : {
	                                        start : 0,
	                                        limit : Constants.PAGE_SIZE
	                                    }
	                                });
                            }
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            searchStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                        },
                        params : {
                            recordId : recordId,
                            tableName : tableName
                        }
                    });
                }
            });
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
        }
    }
    /**
     * 保存记录
     */
    function onSave() {
        // 检查输入是否合法
        // 检查必须输入项
        if (txtName.getValue() !== "") {
            var str = "";
            str = txtOrder.getValue() + "";
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(buttonobj) {
                if ("yes" == buttonobj) {
                    // 长度检查
                    if (txtName.isValid() && txtID.isValid() && txtCode.isValid() && str.length <= 10) {
                        var type = winEdit.title;
                        // 新增记录
                        if ("新增" == type.substring(0, 2)) {
                            // 提交
                            Ext.Ajax.request({
                                url : 'hr/addBaseTableRecord.action',
                                method : Constants.POST,
                                params : {
                                    // 操作表名
                                    tableName : rightGrid.title,
                                    recordName : txtName.getValue(),
                                    retrieveCode : txtCode.getValue(),
                                    orderBy : txtOrder.getValue()
                                },
                                success : function(action) {
                                    var result = eval("(" + action.responseText + ")");
                                    if (result.success == true) {
                                        // 数据库异常
                                        if (result.msg == "SQL") {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                            // 修改成功
                                        } else {
                                        	searchStore.load({
                                                    params : {
                                                        start : 0,
                                                        limit : Constants.PAGE_SIZE
                                                    }
                                                });
                                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                                winEdit.hide();
                                            });
                                        }
                                    }
                                },
                                failure : function() {
                                    // 保存失败
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                }
                            })
                            // 修改记录
                        } else if ("修改" == type.substring(0, 2)) {
                            // 获得选中的记录
                            var record = rightGrid.selModel.getSelected();
                            // 提交表单
                            myPanel.getForm().submit({
                                method : Constants.POST,
                                url : 'hr/modifyBaseTableRecord.action',
                                params : {
                                    // 操作表名
                                    tableName : rightGrid.title,
                                    // ID
                                    recordId : txtID.getValue()
                                },
                                success : function(form, action) {
                                    var result = eval("(" + action.response.responseText + ")");
                                    if (result.success == true) {
                                        // 数据库异常
                                        if (result.msg == "SQL") {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                            // 修改成功
                                        } else {
                                        	searchStore.load({
                                                    params : {
                                                        start : 0,
                                                        limit : Constants.PAGE_SIZE
                                                    }
                                                });
                                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {                                                
                                                winEdit.hide();
                                            });
                                        }
                                    }
                                },
                                failure : function() {
                                    // 保存失败
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                }
                            });
                        }
                    }
                }
            })

            // 名称
        } else if ("" === txtName.getValue()) {
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_002, "名称"));
            return;
        }
    }
    /**
     * 取消
     */
    function onCancle() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_005, function(buttonobj) {
            if ("yes" == buttonobj) {
                winEdit.hide();
            }
        });
    }

});