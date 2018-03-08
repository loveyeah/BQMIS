// 员工岗位维护
// author:zhaozhijie
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();

    var temp = [];
    // 左边data
    var dataLeft = Ext.data.Record.create([
        // 职工岗位ID
        {
        name : "empStationId"
    },
        // 岗位id
        {
            name : "stationId"
        },
        // 岗位名称
        {
            name : "stationName"
        },
        // 岗位级别
        {
            name : "stationLevel"
        },
        // 上次修改时间
        {
            name : "lastModifyDate"
        },
        // 判断更新,插入,删除的flag
        {
            name : "existFlag"
        },
        // 判断DB中是否存在数据的flag
        {
            name : "dbFlag"
        }])
    // 左边Store
    var dsLeft = new Ext.data.JsonStore({
        url : "hr/getStationMaintain.action",
        root : "list",
        totalProperty : 'totalCount',
        fields : dataLeft,
        listeners : {
            loadexception : function(ds, records, o) {
                var o = eval("(" + o.responseText + ")");
                // 判断exception
                if (o.msg != null) {
                    var succ = o.msg;
                    // sql失败
                    if (succ == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        // io失败
                    } else if (succ == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        }
    });
    dsLeft.setDefaultSort("stationId", "ASC");
    // 左边grid
    var leftGrid = new Ext.grid.GridPanel({
        layout : 'fit',
        border : false,
        autoWidth : true,
        autoScroll : true,
        height : 520,
        // 标题不可以移动
        enableColumnMove : false,
        listeners : {
            columnresize : function() {
                this.getView().onLayout();

            }
        },
        store : dsLeft,
        // 单选
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                align : 'right',
                width : 35
            }),
            // 岗位名称
            {
                header : "岗位名称",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'stationName'
            }]
    })

    // 右边data
    var dataRight = Ext.data.Record.create([
        // 职工岗位ID
        {
        name : "empStationId"
    },
        // 岗位名称
        {
            name : "stationName"
        },
        // 岗位id
        {
            name : "stationId"
        },
        // 岗位级别
        {
            name : "stationLevel"
        },
        // 主岗位
        {
            name : "mainStation"
        },
        // 备注
        {
            name : "memo"
        },
        // 上次修改时间
        {
            name : "lastModifyDate"
        },
        // 判断更新,插入,删除的flag
        {
            name : "existFlag"
        },
        // 判断DB中是否存在数据的flag
        {
            name : "dbFlag"
        }])
    // 右边Store
    var dsRight = new Ext.data.JsonStore({
        url : "hr/getEmpStationInfo.action",
        root : "list",
        totalProperty : 'totalCount',
        fields : dataRight,
        listeners : {
            loadexception : function(ds, records, o) {
                var o = eval("(" + o.responseText + ")");
                // 判断exception
                if (o.msg != null) {
                    var succ = o.msg;
                    // sql失败
                    if (succ == Constants.SQL_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        // io失败
                    } else if (succ == Constants.IO_FAILURE) {
                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    }
                }
            }
        }
    });
    dsRight.setDefaultSort("stationId", "ASC");
    // 主岗位 显示为Checkbox
    var ckMainStation = new Ext.grid.CheckColumn({
        header : "主岗位",
        dataIndex : 'mainStation',
        align : 'left',
        width : 60
    })
    // 备注
    var txtNote = new Ext.form.TextField({
        id : 'note',
        maxLength : 250,
        listeners : {
            "render" : function() {
                this.el.on("dblclick", cellClickHandler);
            }
        }
    })
    // 备注-弹出窗口
    var txaMemo = new Ext.form.TextArea({
        id : "txaMemo",
        maxLength : 250,
        width : 180
    });

    // 弹出画面
    var winEnter = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [txaMemo],
        buttonAlign : "center",
        title : '详细信息录入窗口',
        buttons : [{
                text : Constants.BTN_CONFIRM,
                iconCls : Constants.CLS_OK,
                handler : function() {
                    var rec = rightGrid.getSelectionModel().getSelections();
                    if (Ext.get("txaMemo").dom.value.length <= 250 && txaMemo.isValid()) {
                        rec[0].set("memo", Ext.get("txaMemo").dom.value);
                        winEnter.hide();
                    }
                }
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            winEnter.hide();
                        }
                    })
                }
            }]
    });
    // 右边grid
    var rightGrid = new Ext.grid.EditorGridPanel({
        layout : 'fit',
        border : false,
        autoWidth : true,
        autoScroll : true,
        height : 520,
        listeners : {
            columnresize : function() {
                this.getView().onLayout();

            }
        },
        // 标题不可以移动
        enableColumnMove : false,
        store : dsRight,
        // 单选
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        // 单击修改
        clicksToEdit : 1,
        plugins : [ckMainStation],
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                align : 'right',
                width : 35
            }),
            // 岗位名称
            {
                header : "岗位名称",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'stationName'
            },
            // 岗位级别
            {
                header : "岗位级别",
                width : 100,
                sortable : true,
                align : 'left',
                dataIndex : 'stationLevel'
            },
            // 主岗位
            ckMainStation,
            // 备注
            {
                header : "备注",
                width : 100,
                sortable : true,
                editor : txtNote,
                align : 'left',
                dataIndex : 'memo'
            }]
    })

    // 第一次load时
    if (window.dialogArguments.first) {
        // load左边的Store
        dsLeft.load({
            params : {
                deptId : window.dialogArguments.deptId,
                empId : window.dialogArguments.empId
            }
        })
        // load右边的Store
        dsRight.load({
            params : {
                empId : window.dialogArguments.empId
            }
        })
        dsRight.on("load", function() {
            var record = dsRight.getRange(0, dsRight.getCount());
            for (var i = 0; i < dsRight.getCount(); i++) {
                temp[i] = {};
                // 主岗位
                temp[i]["mainStation"] = record[i].data["mainStation"];
                // 备注
                temp[i]["memo"] = record[i].data["memo"];
                // 职工岗位ID
                temp[i]["empStationId"] = record[i].data["empStationId"];
            }
        })
        // 除了第一次以外load
    } else {
        // 清空左边的Store
        dsLeft.removeAll();
        // 清空右边的Store
        dsRight.removeAll();
        // 接受到的上一个状态
        var objects = window.dialogArguments.objects;
        for (var i = 0; i < objects.length; i++) {
            // set左边的Store
            if (objects[i]["isLeft"] == "true") {
                // 原数据个数
                var count = dsLeft.getCount();
                if (count == null) {
                    count = 0;
                }
                // 新增一条记录
                var newRecord = new dataLeft({
                    empStationId : objects[i]["empStationId"],
                    stationId : objects[i]["stationId"],
                    stationName : objects[i]["stationName"],
                    stationLevel : objects[i]["stationLevel"],
                    lastModifyDate : objects[i]["lastModifyDate"],
                    existFlag : objects[i]["existFlag"],
                    dbFlag : objects[i]["dbFlag"]
                });

                // 插入新数据
                dsLeft.insert(count, newRecord);
                dsLeft.totalLength = dsLeft.getTotalCount() + 1;
                dsLeft.commitChanges();
                // set右边的Store
            } else {
                // 原数据个数
                var count = dsRight.getCount();
                if (count == null) {
                    count = 0;
                }
                // 新增一条记录
                var newRecord = new dataRight({
                    stationId : objects[i]["stationId"],
                    stationName : objects[i]["stationName"],
                    stationLevel : objects[i]["stationLevel"],
                    mainStation : objects[i]["mainStation"],
                    memo : objects[i]["memo"],
                    empStationId : objects[i]["empStationId"],
                    lastModifyDate : objects[i]["lastModifyDate"],
                    existFlag : objects[i]["existFlag"],
                    dbFlag : objects[i]["dbFlag"]
                });
                // 插入新数据
                dsRight.insert(count, newRecord);
                dsRight.totalLength = dsRight.getTotalCount() + 1;
                temp[i - dsLeft.getCount()] = {};
                // 主岗位
                temp[i - dsLeft.getCount()]["mainStation"] = newRecord.data["mainStation"];
                // 备注
                temp[i - dsLeft.getCount()]["memo"] = newRecord.data["memo"];
                // 职工岗位ID
                temp[i - dsLeft.getCount()]["empStationId"] = newRecord.data["empStationId"];
                dsRight.commitChanges();
            }
        }
    }
    // 添加成员button
    var btnGrant = new Ext.Button({
        id : 'btnGrant',
        height : 80,
        minWidth : 58,
        iconCls : Constants.CLS_RIGHT_MOVE,
        layout : 'form',
        handler : grantHandler
    })
    // 去除成员button
    var btnRevoke = new Ext.Button({
        id : 'btnRevoke',
        iconCls : Constants.CLS_LEFT_MOVE,
        minWidth : 58,
        border : true,
        style : {
            'margin-top' : '30'
        },
        handler : revokeHandler
    })
    // 确定按钮
    var btnSubmit = new Ext.Button({
        style : {
            'margin-top' : '30'
        },
        text : Constants.BTN_CONFIRM,
        iconCls : Constants.CLS_OK,
        handler : submitHandler
    })
    // 取消按钮
    var btnCancel = new Ext.Button({
        style : {
            'margin-top' : '30'
        },
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : cancelHandler
    })
    // panel
    var intop = new Ext.Panel({
        region : 'north',
        layout : 'fit',
        items : "",
        height : 140,
        border : false
    })

    // panel
    var incenter = new Ext.Panel({
        region : 'center',
        layout : 'form',
        style : 'padding-left:20px',
        items : [btnGrant, btnRevoke, btnSubmit, btnCancel],
        border : false
    })

    // panel
    var inwest = new Ext.Panel({
        region : 'west',
        layout : 'form',
        width : 8,
        items : "",
        border : false
    })

    // panel
    var mid = new Ext.Panel({
        width : 100,
        height : 520,
        frame : false,
        border : false,
        items : [intop, inwest, incenter]
    });

    // panel
    var top = new Ext.Panel({
        region : 'north',
        border : false,
        items : ""
    })

    // 主panel
    var panelMain = new Ext.Panel({
        layout : 'column',
        border : false,
        items : [{
                columnWidth : .26,
                border : false,
                layout : 'fit',
                items : [leftGrid]
            }, {
                width : 100,
                border : false,
                style : "border-top:0px;border-left:1px solid;border-right:1px solid",
                layout : 'anchor',
                items : [mid]
            }, {
                columnWidth : .74,
                layout : 'fit',
                border : false,
                items : [rightGrid]
            }]
    })

    // Viewport
    new Ext.Viewport({
        enableTabScroll : true,
        frame : false,
        border : false,
        items : [panelMain]
    });

    /**
     * 备注双击处理
     */
    function cellClickHandler() {
        var record = rightGrid.selModel.getSelected();
        winEnter.x = undefined;
        winEnter.y = undefined;
        winEnter.show();
        txaMemo.setValue(record.get("memo"));
    }
    /**
     * 从左往右操作
     */
    function grantHandler() {
        // 报message
        if (!leftGrid.selModel.hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        } else {
            // 选中的数据
            var record = leftGrid.getSelectionModel().getSelected();
            if (record.data["existFlag"] == "D") {
                record.data["existFlag"] = "U"
            } else {
                record.data["existFlag"] = "I"
            }
            // 插入新数据
            addDsRight(record);
            dsRight.totalLength = dsRight.getTotalCount() + 1;
            // 重新刷新
            rightGrid.getView().refresh();
            // 移出选中的数据
            dsLeft.remove(record);
            dsLeft.totalLength = dsLeft.getTotalCount() - 1;
            // 重新刷新
            leftGrid.getView().refresh();
        }
    }

    /**
     * 从右往左操作
     */
    function revokeHandler() {
        // 报message
        if (!rightGrid.selModel.hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        } else {
            // 选中的数据
            var record = rightGrid.getSelectionModel().getSelected();
            if (record.data["existFlag"] == "U") {
                record.data["existFlag"] = "D"
            } else if (record.data["existFlag"] == "I") {
                record.data["existFlag"] = ""
            } else {
                record.data["existFlag"] = "D"
            }
            // 插入新数据
            addDsLeft(record);
            dsLeft.totalLength = dsLeft.getTotalCount() + 1;
            // 重新刷新
            leftGrid.getView().refresh();
            // 移出选中的数据
            dsRight.remove(record);
            dsRight.totalLength = dsRight.getTotalCount() - 1;
            // 重新刷新
            rightGrid.getView().refresh();
        }
    }

    /**
     * 确认操作
     */
    function submitHandler() {
        // CHECK
        var check = false;
        // 左边grid中数据的长度
        var leftLength = dsLeft.getCount();
        // 左边grid中的数据
        var leftRecords = dsLeft.getRange(0, leftLength);
        // 所需传回的数据
        var objects = [];
        // 右边grid中的数据
        var rightRecords = dsRight.getRange(0, dsRight.getCount());
        // 选中checkbox的个数
        var count = 0;
        // 判断checkbox是否选择了
        for (var i = 0; i < dsRight.getCount(); i++) {
            if (rightRecords[i].data["mainStation"] === "Y") {
                count++;
            }
        }
        if (count == 1) {
            check = true;
        }
        if (check) {
            // 将左边数据插入返回值中
            for (var i = 0; i < leftLength; i++) {
                objects[i] = {};
                // 职工岗位ID
                objects[i]["empStationId"] = leftRecords[i].data["empStationId"];
                // 岗位id
                objects[i]["stationId"] = leftRecords[i].data["stationId"];
                // 岗位名称
                objects[i]["stationName"] = leftRecords[i].data["stationName"];
                // 岗位级别
                objects[i]["stationLevel"] = leftRecords[i].data["stationLevel"];
                // 判断更新,插入,删除的flag
                objects[i]["existFlag"] = leftRecords[i].data["existFlag"];
                // 上次修改时间
                objects[i]["lastModifyDate"] = leftRecords[i].data["lastModifyDate"];
                // 判断DB中是否存在数据的flag
                objects[i]["dbFlag"] = leftRecords[i].data["dbFlag"];
                // 是否为左边的grid
                objects[i]["isLeft"] = "true";
            }
            // 将右边的数据插入返回值中
            for (var i = 0; i < dsRight.getCount(); i++) {
                objects[i + leftLength] = {};
                var initFlag = false;
                // 岗位id
                objects[i + leftLength]["stationId"] = rightRecords[i].data["stationId"];
                // 岗位名称
                objects[i + leftLength]["stationName"] = rightRecords[i].data["stationName"];
                // 岗位级别
                objects[i + leftLength]["stationLevel"] = rightRecords[i].data["stationLevel"];
                // 上次修改时间
                objects[i + leftLength]["lastModifyDate"] = rightRecords[i].data["lastModifyDate"];
                // 判断更新,插入,删除的flag
                objects[i + leftLength]["existFlag"] = rightRecords[i].data["existFlag"];
                if (rightRecords[i].data["existFlag"] == "") {
                    for (var j = 0; j < temp.length; j++) {
                        if (rightRecords[i].data["empStationId"] == temp[j]["empStationId"]) {
                            if (rightRecords[i].data["mainStation"] != temp[j]["mainStation"]
                            || rightRecords[i].data["memo"] || temp[j]["memo"]) {
                                initFlag = true;
                            }
                        }
                        if (initFlag) {
                            objects[i + leftLength]["existFlag"] = "U";
                        }
                    }
                }
                // 主岗位
                objects[i + leftLength]["mainStation"] = rightRecords[i].data["mainStation"];
                // 备注
                objects[i + leftLength]["memo"] = rightRecords[i].data["memo"];
                // 职工岗位ID
                objects[i + leftLength]["empStationId"] = rightRecords[i].data["empStationId"];
                // 判断DB中是否存在数据的flag
                objects[i + leftLength]["dbFlag"] = rightRecords[i].data["dbFlag"];
                // 是否为左边的grid
                objects[i + leftLength]["isLeft"] = "false";
            }
            // 返回主页面
            window.returnValue = objects;
            window.close();
        } else {
            Ext.Msg.alert(Constants.ERROR, Constants.PD003_E_001);
        }
    }

    /**
     * 取消操作
     */
    function cancelHandler() {
        // 确认取消信息
        Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
            if (button == "yes") {
                window.close();
            }
        })
    }

    /**
     * 右边增加一条记录
     */
    function addDsRight(record) {
        // 新规一条记录，设定默认值
        var newRecord = new dataRight({
            // 岗位id
            stationId : record.data.stationId,
            // 岗位名称
            stationName : record.data.stationName,
            // 岗位级别
            stationLevel : record.data.stationLevel,
            // 主岗位
            mainStation : "",
            // 备注
            memo : "",
            // 职工岗位ID
            empStationId : record.data.empStationId,
            // 上次修改时间
            lastModifyDate : record.data.lastModifyDate,
            // 判断更新,插入,删除的flag
            existFlag : record.data.existFlag,
            // 判断DB中是否存在数据的flag
            dbFlag : record.data.dbFlag
        });
        dsRight.add(newRecord);
    }

    /**
     * 左边增加一条记录
     */
    function addDsLeft(record) {
        // 新规一条记录，设定默认值
        var newRecord = new dataLeft({
            // 职工岗位ID
            empStationId : record.data.empStationId,
            // 岗位id
            stationId : record.data.stationId,
            // 岗位名称
            stationName : record.data.stationName,
            // 岗位级别
            stationLevel : record.data.stationLevel,
            // 上次修改时间
            lastModifyDate : record.data.lastModifyDate,
            // 判断更新,插入,删除的flag
            existFlag : record.data.existFlag,
            // 判断DB中是否存在数据的flag
            dbFlag : record.data.dbFlag
        });
        dsLeft.add(newRecord);
    }
})
// ↓↓********************grid插件，用来显示一行checkbox********************↓↓ //
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
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {
                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
            } else {
                for (var i = 0; i < this.grid.store.getCount(); i++) {
                    var recordTotal = this.grid.store.getAt(i);
                    if (recordTotal.data[this.dataIndex] == Constants.CHECKED_VALUE) {
                        recordTotal.set(this.dataIndex, Constants.UNCHECKED_VALUE);
                    }
                }
                record.set(this.dataIndex, Constants.CHECKED_VALUE);
            }
        }
    },

    renderer : function(v, p, record) {
        p.css += ' x-grid3-check-col-td';
        return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '') + ' x-grid3-cc-' + this.id + '">&#160;</div>';
    }
}