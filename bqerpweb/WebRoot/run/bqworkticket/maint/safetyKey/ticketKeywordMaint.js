Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    // 定义时间格式
    function renderDate(value) {
        if (!value)
            return "";

        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;

        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate)
            return "";
        return strTime ? strDate + " " + strTime : strDate;
    }

    // ============== 定义查询画面 ===============
    var MyRecord = Ext.data.Record.create([{
            name : 'safetyKeyId'
        }, {
            name : 'safetyKeyName'
        }, {
            name : 'workticketTypeCode'
        }, {
            name : 'modifyBy'
        }, {
            name : 'modifyDate'
        }, {
            name : 'orderBy'
        }, {
            name : 'reverseKeyId'
    }]);

    var dataProxy = new Ext.data.HttpProxy({
        // 安全关键词内容
        url : 'workticket/getDataList.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    var storeSafetyKey = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });

    var storeCbx = new Ext.data.JsonStore({
        root : 'list',
        url : "workticket/getTicketKeywordList.action",
        fields : ['workticketTypeCode', 'workticketTypeName']
    })
    storeCbx.load();

    /* 设置每一行的选择框 */
    var sm = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false
    });
    var ticketComboBox = new Ext.form.ComboBox({
        id : "ticketType",
        store : storeCbx,
        displayField : "workticketTypeName",
        valueField : "workticketTypeCode",
        mode : 'local',
        value : '',
        triggerAction : 'all',
        readOnly : true
    });
    // 通过store的装载初始化所属系统下拉框的默认选项为store的第一项
    storeCbx.on("load", function(e, records, o) {
        ticketComboBox.setValue(records[0].data.workticketTypeCode);
        var workticketType = records[0].data.workticketTypeCode;
        storeSafetyKey.load({
            params : {
                typeCode : workticketType,
                keyType : '1',
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
    });

    var radioFront = new Ext.form.Radio({
        id : 'front',
        boxLabel : '前置词',
        name : 'keyType',
        inputValue : '1',
        checked : true
    })
    var radioBack = new Ext.form.Radio({
        id : 'back',
        boxLabel : '后置词',
        name : 'keyType',
        inputValue : '2'
    })

    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : storeSafetyKey,
        columns : [sm, {
                    header : "ID",
                    width : 75,
                    sortable : true,
                    dataIndex : 'safetyKeyId'
                }, {
                    header : "关键词",
                    width : 75,
                    sortable : true,
                    dataIndex : 'safetyKeyName'
                }, {
                    header : "填写人",
                    width : 75,
                    sortable : true,
                    hidden : true,
                    dataIndex : 'modifyBy'
                }, {
                    header : "填写日期",
                    width : 75,
                    sortable : true,
                    dataIndex : 'modifyDate',
                    renderer : renderDate
                }, {
                    dataIndex : 'reverseKeyId',
                    hidden : true
                }],
        sm : sm,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },

        tbar : [radioFront, radioBack, '&nbsp;', '工作票类型:', ticketComboBox, {
                    text : Constants.BTN_QUERY,
                    iconCls : Constants.CLS_QUERY,
                    handler : queryRecord
                }, {
                    text : Constants.BTN_ADD,
                    iconCls : Constants.CLS_ADD,
                    handler : addSafetyKey
                }, {
                    text : Constants.BTN_UPDATE,
                    iconCls : Constants.CLS_UPDATE,
                    handler : updateSafetyKey
                }, {
                    text : Constants.BTN_DELETE,
                    iconCls : Constants.CLS_DELETE,
                    handler : deleteSafetyKey
                }],
        // 分页
        bbar : new Ext.PagingToolbar({
                    pageSize : Constants.PAGE_SIZE,
                    store : storeSafetyKey,
                    displayInfo : true,
                    displayMsg : Constants.DISPLAY_MSG,
                    emptyMsg : Constants.EMPTY_MSG
                }),
        enableColumnMove : false
    });

    grid.on("dblclick", updateSafetyKey);
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [grid]
    });

    // ============== 定义增加,修改画面 ===============
    var workticketTypeCode = {
        name : "entity.workticketTypeCode",
        xtype : 'hidden'
    }

    var keyType = {
        name : "entity.keyType",
        xtype : 'hidden'
    }

    var safetyKeyId = {
        id : 'safetyKeyId',
        name : "entity.safetyKeyId",
        value : '',
        xtype : 'hidden'
    }

    var keywordName = {
        id : 'safetyKeyName',
        xtype : 'textfield',
        fieldLabel : '名称<font color="red">*</font>',
        name : 'entity.safetyKeyName',
        allowBlank : false,
        maxLength : 30
    }

    var orderBy = {
        id : 'orderBy',
        xtype : 'numberfield',
        fieldLabel : '显示顺序',
        name : 'entity.orderBy',
        maxLength : 10
    }

    var antonym = {
        id : 'reverseKeyName',
        xtype : 'trigger',
        fieldLabel : '反义词',
        onTriggerClick : antonymSelect,
        readOnly : true
    }

    var reverseKeyId = {
        id : 'reverseKeyId',
        name : 'entity.reverseKeyId',
        xtype : 'hidden'
    }

    var myaddpanel = new Ext.FormPanel({
        id : 'myaddpanel',
        frame : true,
        labelAlign : 'right',
        defaults : {
            width : 180
        },
        items : [keyType, workticketTypeCode, safetyKeyId, keywordName,
                orderBy, antonym, reverseKeyId]

    });

// 添加，修改窗口
    var win = new Ext.Window({
        width : 400,
        buttonAlign : "center",
        modal : true,
        items : [myaddpanel],
        resizable : false,
        layout : 'fit',
        closeAction : 'hide',
        buttons : [{
            text : Constants.BTN_SAVE,
            iconCls : Constants.CLS_SAVE,
            handler : function() {
                if (!myaddpanel.getForm().isValid()) {
                    return false;
                }
                var myurl = "";
                if (Ext.get("entity.safetyKeyId").dom.value == '') {
                    myurl = "workticket/addSafetyKey.action";
                } else {
                    myurl = "workticket/updateSafetyKey.action";
                }
                myaddpanel.getForm().submit({
                    method : Constants.POST,
                    url : myurl,
                    success : function(form, action) {
                        var o = eval("(" + action.response.responseText
                                + ")");
                        var ticketType = ticketComboBox.value;
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
                        storeSafetyKey.load({
                                    params : {
                                        typeCode : ticketType,
                                        keyType : "1",
                                        start : 0,
                                        limit : Constants.PAGE_SIZE
                                    }
                                });
                        win.hide();
                    },
                    faliue : function() {
                        Ext.Msg.alert(Constants.ERROR,
                                Constants.UNKNOWN_ERR);
                    }
                });
            }
        }, {
            text : Constants.BTN_CANCEL,
            iconCls : Constants.CLS_CANCEL,
            handler : function() {
                win.hide();
            }
        }]
    });

    var queryRecord = Ext.data.Record.create([{
            name : 'safetyKeyId'
        }, {
            name : 'safetyKeyName'
        }, {
            name : 'workticketTypeCode'
        }, {
            name : 'modifyBy'
        }, {
            name : 'modifyDate'
        }, {
            name : 'reverseKeyId'
        }, {
            name : 'orderBy'
    }]);

    var dataProvide = new Ext.data.HttpProxy({
        // 安全关键词内容
        url : 'workticket/getDataList.action'
    });

    var reader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, queryRecord);

    var storeQuery = new Ext.data.Store({
        proxy : dataProvide,
        reader : reader
    });

    var storeQueryCbx = new Ext.data.JsonStore({
        root : 'list',
        url : "workticket/getTicketKeywordList.action",
        fields : ['workticketTypeCode', 'workticketTypeName']
    })

    // ============== 定义反义词选择画面 ===============
    /* 设置每一行的选择框 */
    var smQuery = new Ext.grid.CheckboxSelectionModel({
        singleSelect : true
    });

    var cbxTypeCode = new Ext.form.ComboBox({
        id : 'cbxTypeCode',
        store : storeQueryCbx,
        displayField : "workticketTypeName",
        valueField : "workticketTypeCode",
        mode : 'local',
        triggerAction : 'all',
        emptyText : '',
        blankText : '',
        value : '',
        readOnly : true
    });
    storeQueryCbx.on("load", function(e, records, o) {
        cbxTypeCode.setValue(ticketComboBox.getValue());
        workticketTypeQuery();

    });
    var gridQuery = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        width : 600,
        height : 400,
        store : storeQuery,
        columns : [smQuery, {
                    header : "ID",
                    width : 75,
                    sortable : true,
                    dataIndex : 'safetyKeyId'
                }, {
                    header : "关键词",
                    width : 75,
                    sortable : true,
                    dataIndex : 'safetyKeyName'
                }, {
                    header : "填写人",
                    width : 75,
                    sortable : true,
                    hidden : true,
                    dataIndex : 'modifyBy'
                }, {
                    header : "填写日期",
                    width : 75,
                    sortable : true,
                    dataIndex : 'modifyDate',
                    renderer : renderDate
                }, {
                    id : 'reverseKeyId',
                    hidden : true
                }],
        sm : smQuery,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },
        tbar : [new Ext.form.Radio({
                            id : 'frontWord',
                            boxLabel : '前置词',
                            inputValue : '1',
                            checked : true
                        }), '&nbsp;', '工作票类型:', cbxTypeCode, {
                    text : Constants.BTN_QUERY,
                    iconCls : Constants.CLS_QUERY,
                    handler : workticketTypeQuery
                }, '&nbsp;', '双击选择']
    });

    var queryForm = new Ext.FormPanel({
        id : 'queryForm',
        frame : true,
        labelAlign : 'center',
        items : [gridQuery]
    });
    var queryWin = new Ext.Window({
        width : 600,
        height : 400,
        resizable : false,
        modal : true,
        layout : 'fit',
        labelAlign : 'center',
        closeAction : 'hide',
        items : [queryForm]
    });

    gridQuery.on("dblclick", reverseKeySelected);

    // 前置词的时候，反义词选择处理
    function antonymSelect() {
        if (radioBack.checked) {
            Ext.Msg.alert(Constants.NOTICE, "只有前置词有反义词！");
            return;
        } else {
            storeQuery.removeAll();
            queryForm.getForm().reset();
            storeQueryCbx.load();
            queryWin.show();
            cbxTypeCode.reset();
        }
    }
    // 反义词选择页面查询处理
    function workticketTypeQuery() {
        var ticketType = "";
        ticketType = cbxTypeCode.value;
        if (ticketType == "") {
            Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_WORKTICKET_TYPE);
            return;
        }
        storeQuery.baseParams = {
            typeCode : ticketType,
            keyType : "1"
        }
        storeQuery.load({
                    params : {
                        start : 0,
                        limit : Constants.PAGE_SIZE
                    }
                })
    }

    // 反义词选择页面双击处理
    function reverseKeySelected() {
        var selectedRow = gridQuery.getSelectionModel().getSelected();
        if (selectedRow) {
            var safetyKeyID = selectedRow.data.safetyKeyId;
            Ext.lib.Ajax.request('POST', 'workticket/getReverseWorkType.action', {
                        success : function(action) {
                            var obj = Ext.util.JSON.decode(action.responseText);
                            Ext.get("reverseKeyName").dom.value = obj.safetyKeyName;
                            Ext.get("entity.reverseKeyId").dom.value = obj.safetyKeyId
                            queryWin.hide();
                        }
    
                    }, 'id=' + safetyKeyID)
        }
    }

    // 查询主页记录
    function queryRecord() {
        var ticketType = ticketComboBox.value;
        if (ticketType == "") {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.SELECT_WORKTICKET_TYPE);
            return;
        }
        var keyType = "";
        if (radioFront.checked) {
            keyType = "1";
        } else {
            keyType = "2";
        }
        storeSafetyKey.baseParams = {
            typeCode : ticketType,
            keyType : keyType
        };
        storeSafetyKey.load({
                    params : {
                        start : 0,
                        limit : Constants.PAGE_SIZE
                    }
                });
    }

    // 增加关键字
    function addSafetyKey() {
        if (ticketComboBox.value == "") {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.SELECT_WORKTICKET_TYPE);
            return;
        }
        myaddpanel.getForm().reset();
        win.setTitle("增加关键词");
        win.show();
        var keyType = "1";
        if (radioBack.checked) {
            keyType = "2";
        }
        Ext.get("entity.keyType").dom.value = keyType;
        Ext.get("entity.workticketTypeCode").dom.value = ticketComboBox.value;
    }

    // 修改关键字
    function updateSafetyKey() {
        myaddpanel.getForm().reset();
        var selectedRows = grid.getSelectionModel().getSelections();
        recordslen = selectedRows.length
        if (selectedRows.length == 0 || selectedRows.length > 1) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.SELECT_COMPLEX_MSG);
            return;
        } else {
            win.setTitle("修改关键词");
            win.show();
            var record = grid.getSelectionModel().getSelected();
            myaddpanel.getForm().loadRecord(record);
            var reverseKeyID = Ext.get('reverseKeyId').dom.value;
            var myurl = "workticket/getReverseKeyById.action";
            Ext.Ajax.request({
                            method : 'POST',
                            url : myurl,
                            params : {
                                id : reverseKeyID
                            },
                            success : function(action) {
                                var obj = Ext.util.JSON.decode(action.responseText);
                                Ext.get("reverseKeyName").dom.value = obj.safetyKeyName;
            }});

        }
    }

    // 删除关键字
    function deleteSafetyKey() {

        var sm = grid.getSelectionModel();
        var selectedRows = sm.getSelections();
        if (selectedRows.length == 0) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.SELECT_NULL_DEL_MSG);
        } else {
            var ids = "";
            for (i = 0; i < selectedRows.length; i++) {
                ids += selectedRows[i].data.safetyKeyId;
                if (i < selectedRows.length - 1) {
                    ids += ",";
                }
            }
            Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg,
                function(b) {
                    if (b == 'yes') {
                        Ext.lib.Ajax.request('POST',
                            'workticket/deleteSafetyKey.action', {
                                success : function(action) {
                                    Ext.Msg.alert(
                                            Constants.SYS_REMIND_MSG,
                                            Constants.DEL_SUCCESS)
                                    var ticketType = ticketComboBox.value;
                                    storeSafetyKey.load({
                                                params : {
                                                    typeCode : ticketType,
                                                    keyType : "1",
                                                    start : 0,
                                                    limit : Constants.PAGE_SIZE
                                                }
                                            });

                                },
                                failure : function() {
                                    Ext.Msg.alert(Constants.ERROR,
                                            Constants.DEL_ERROR);
                                }
                            }, 'ids=' + ids);
                        }

                });
        }
    }
});