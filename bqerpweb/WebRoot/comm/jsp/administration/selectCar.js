Ext.onReady(function() {
            // 画面布局
            // 车俩选择查询DataStore
            var storeQuery = new Ext.data.JsonStore({
                        url : 'administration/getCarInfoList.action',
                        root : 'list',
                        totalProperty : 'totalCount',
                        fields : ['carNo', 'carName', 'carKind', 'carType',
                                'runMile', 'driver','id','updateTime','driverName']
                    })

            // 初期化数据读入
            storeQuery.load({
                        params : {
                            start : 0,
                            limit : Constants.PAGE_SIZE
                        }
                    });

            // 设置默认排序
            storeQuery.setDefaultSort('carNo');

            // 车俩选择查询Grid列内容定义
            var cmQuery = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
                                header : "行号",
                                width : 35
                            }), {
                        sortable : true,
                        header : '车种',
                        dataIndex : 'carKind',
                        align : 'left'
                    }, {
                        sortable : true,
                        header : '车型',
                        dataIndex : 'carType',
                        align : 'left'
                    }, {
                        sortable : true,
                        header : '车名',
                        dataIndex : 'carName',
                        align : 'left'
                    }, {
                        sortable : true,
                        header : '司机',
                        dataIndex : 'driverName',
                        align : 'left'
                    }, {
                        sortable : true,
                        header : '车牌号',
                        dataIndex : 'carNo',
                        align : 'left'
                    }, {
                        sortable : true,
                        header : '行车里程',
                        dataIndex : 'runMile',
                        align : 'left'
                    }])

            // 确认Button
            var btnSubmit = new Ext.Button({
                        text : Constants.BTN_CONFIRM,
                        iconCls : Constants.CLS_OK,
                        handler : submit
                    })

            // 取消Button
            var btnCancel = new Ext.Button({
                        text : Constants.BTN_CANCEL,
                        iconCls : Constants.CLS_CANCEL,
                        handler : cancel
                    })

            // 车俩选择查询ToolBar
            var tbarQuery = new Ext.Toolbar({
                        items : [btnSubmit, "-", btnCancel]
                    })

            // 选择模式
            var sm = new Ext.grid.RowSelectionModel({
                        singleSelect : true
                    })

            // 分页
            var pb = new Ext.PagingToolbar({
                        pageSize : Constants.PAGE_SIZE,
                        store : storeQuery,
                        displayInfo : true,
                        displayMsg : MessageConstants.DISPLAY_MSG,
                        emptyMsg : MessageConstants.EMPTY_MSG
                    })

            // 车俩选择查询 Grid
            var gridQuery = new Ext.grid.GridPanel({
                        layout : 'fit',
                        ds : storeQuery,
                        cm : cmQuery,
                        tbar : tbarQuery,
                        sm : sm,
                        autoScroll : true,
                        enableColumnMove : false,
                        enableColumnHide : true,
                        border : false,

                        bbar : pb,
                        viewConfig : {
                            autoFill : true
                        }
                    })

            // 车俩选择查询双击事件
            gridQuery.on("rowdblclick", submit);

            // 车俩选择查询 Panel
            var tabQuery = new Ext.Panel({
                        layout : 'fit',
                        border : true,
                        items : [gridQuery]
                    })

            new Ext.Viewport({
                        width : 550,
                        height : 500,
                        enableTabScroll : true,
                        layout : "fit",
                        border : false,
                        items : [{
                                    layout : 'fit',
                                    border : false,
                                    margins : '0 0 0 0',
                                    region : 'center',
                                    autoScroll : true,
                                    items : [tabQuery]
                                }]
                    })

            // 画面处理
            /**
             * 车俩选择查询画面确认BUTTON
             */
            function submit() {
                // 如果没有选择，弹出提示信息
                if (!gridQuery.selModel.hasSelection()) {
                    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_E_016);
                    return;
                }
                var record = gridQuery.getSelectionModel().getSelected();
                var object = new Object();
                if (record != null) {
                    object.carKind = record.data.carKind;
                    object.carType = record.data.carType;
                    object.carName = record.data.carName;
                    object.carNo = record.data.carNo;
                    object.runMile = record.data.runMile;
                    object.driver = record.data.driver;
                    object.id = record.data.id;
                    object.updateTime = record.data.updateTime;
                    object.driverName = record.data.driverName
                    window.returnValue = object;
                    window.close();
                }
            }
            /**
             * 车俩选择查询画面取消BUTTON
             */
            function cancel() {
                window.close();
            }
        })