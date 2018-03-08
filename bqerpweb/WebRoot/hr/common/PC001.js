Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    var _deptId = window.dialogArguments.deptId;
    var _empId = window.dialogArguments.empId;
    // var _deptId =1;
    var empOrderByData = Ext.data.Record.create([{
            name : 'empId'
        }, {
            name : 'deptId'
        }, {
            name : 'chsName'
        }, {
            name : 'orderBy'
        }, {
            name : 'orderByBak'
        }, {
            name : 'flag'
        }]);
    var empOrderByStore = new Ext.data.JsonStore({
        url : 'hr/getEmpOrderbyList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : empOrderByData
    });
    empOrderByStore.load({
        params : {
            deptId : _deptId
        }
    });
    var confirmBtn = new Ext.Button({
        text : "确认",
        iconCls : Constants.CLS_OK,
        handler : function() {
            confirmBtnOperation();
        }
    });
    var cancelBtn = new Ext.Button({
        text : "取消",
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
                if (buttonobj == "yes") {
                    window.close();
                }
            });
        }
    });
    empOrderByStore.on('load', afterStoreLoad);
    var empOrderByGrid = new Ext.grid.EditorGridPanel({// 
        region : 'center',
        border : false,
        autoScroll : true,
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        enableColumnMove : false,
        clicksToEdit : 1,
        store : empOrderByStore,
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                width : 35
            }),
            // 岗位名称
            {
                header : "员工姓名",
                width : 200,
                sortable : false,
                dataIndex : 'chsName'
            }],
        bbar : new Ext.Toolbar({
            style : "text-align:center;",
            items : [{
                    text : "上移",
                    iconCls : Constants.CLS_UP_MOVE,
                    handler : function() {
                        upMoveBtnOperation();
                    }
                }, new Ext.Toolbar.Spacer(), {
                    text : "下移",
                    iconCls : Constants.CLS_DOWN_MOVE,
                    handler : function() {
                        downMoveBtnOperation();
                    }
                }, new Ext.Toolbar.Spacer(), confirmBtn, new Ext.Toolbar.Spacer(), cancelBtn]
        }),
        autoSizeColumns : true
    });

    // 显示区域
    var view = new Ext.Viewport({
        enableTabScroll : true,
        autoScroll : true,
        layout : "fit",
        items : [empOrderByGrid]
    })
    /**
     * 上移处理
     */
    function upMoveBtnOperation() {
        if (empOrderByGrid.selModel.hasSelection()) {
            var record = empOrderByGrid.getSelectionModel().getSelected();
            var index = findOpposition(record);
            if (index == 0) {
                return;
            } else {
                var orderBy = record.data.orderBy;
                var beforeOrderBy = empOrderByStore.getAt(index - 1).data.orderBy;
                record.set("orderBy", beforeOrderBy);
                record.set("flag", "1");
                empOrderByStore.getAt(index - 1).set("orderBy", orderBy);
                empOrderByStore.getAt(index - 1).set("flag", "1");
                // 显示顺相同，交换二者顺序
                if (orderBy == beforeOrderBy) {
                    empOrderByStore.remove(record);
                    empOrderByStore.insert(index - 1, record);
                    empOrderByGrid.getSelectionModel().selectRow(index - 1);
                }
                empOrderByStore.sort("orderBy", "ASC");
                empOrderByGrid.getView().refresh();
            }
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 下移处理
     */
    function downMoveBtnOperation() {
        if (empOrderByGrid.selModel.hasSelection()) {
            var record = empOrderByGrid.getSelectionModel().getSelected();
            var index = findOpposition(record);
            if (index == empOrderByStore.getCount() - 1) {
                return;
            } else {
                var orderBy = record.data.orderBy;
                var afterOrderBy = empOrderByStore.getAt(index + 1).data.orderBy;
                record.set("orderBy", afterOrderBy);
                record.set("flag", "1");
                empOrderByStore.getAt(index + 1).set("orderBy", orderBy);
                empOrderByStore.getAt(index + 1).set("flag", "1");
                // 显示顺相同，交换二者顺序
                if (orderBy == afterOrderBy) {
                    empOrderByStore.remove(record);
                    empOrderByStore.insert(index + 1, record);
                    empOrderByGrid.getSelectionModel().selectRow(index + 1);
                }
                empOrderByStore.sort("orderBy", "ASC");
                empOrderByGrid.getView().refresh();
            }
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 确认处理
     */
    function confirmBtnOperation() {
        var data = [];
        var record;
        var currentEmpRecord;
        for (var i = 0; i < empOrderByStore.getCount(); i++) {
            record = empOrderByStore.getAt(i);
            if (_empId == record.get('empId')) {
                currentEmpRecord = record;
            }
            if (record.data.flag === "1") {
                if (record.data.orderBy === record.data.orderByBak) {
                    continue;
                } else {
                    data.push(record.data);
                }
            }
        }
        // 画面没有变化，不做后台操作
        if (data.length < 1) {
            if (currentEmpRecord) {
                window.returnValue = currentEmpRecord.get('orderBy');
            } else {
                window.returnValue = null;
            }
            window.close();
        }
        Ext.Ajax.request({
            method : 'POST',
            url : 'hr/saveEmpOrderbyMaintenList.action',
            params : {
                data : Ext.util.JSON.encode(data),
                empId : _empId
            },
            success : function(action) {

                var result = eval('(' + action.responseText + ')');

                if (result.msg == Constants.SQL_FAILURE) {
                    Ext.Msg.alert(MessageConstants.ERROR, Constants.COM_E_014);
                    return;
                }
                if (result.msg == Constants.DATE_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                    return;
                }
                var lastModifiyDate = result.msg;
                if (currentEmpRecord) {
                    window.returnValue = {
                        orderBy :currentEmpRecord.get('orderBy'),
                        lastModifiyDate : lastModifiyDate
                    }
                } else {
                    window.returnValue = null;
                }
                window.close();
            }
        });
    }
    /**
     * 该记录在Store中的位置
     */
    function findOpposition(record) {
        var empId = record.data.empId;
        var index;
        for (var i = 0; i < empOrderByStore.getCount(); i++) {
            if (empId === empOrderByStore.getAt(i).data.empId) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * store load 事件处理
     */
    function afterStoreLoad(store, records) {
        for (var i = 0; i < records.length; i++) {
            if (_empId == records[i].get('empId')) {
                empOrderByGrid.getSelectionModel().selectRow(i);
            }
        }
    }
});
