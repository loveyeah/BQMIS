Ext.QuickTips.init();
Ext.onReady(function() {

    // 人员编码
    var txtUserCode = new Ext.form.TextField({
                id : "right.userCode",
                name : "right.userCode",
                width : 120,
                fieldLabel : "人员编码<font color='red'>*</font>",
                allowBlank : false,
                readOnly : true
            });
    txtUserCode.onClick(selectUserCode);
    // 人员姓名
    var txtName = new Ext.form.TextField({
                width : 120,
                fieldLabel : "人员姓名",
                readOnly : true,
                name : "txtName",
                id : "txtName"
            });
    // 部门名称
    var txtDepName = new Ext.form.TextField({
                width : 120,
                fieldLabel : "部门名称",
                readOnly : true,
                name : "txtDepName",
                id : "txtDepName"
            });
    var fs = new Ext.Panel({
        height : "100%",
        layout : "form",
        style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    border : false,
                    layout : "column",
                    items : [{
                                columnWidth : 0.5,
                                border : false,
                                layout : "form",
                                items : [txtUserCode, {
                                            border : false,
                                            height : 10
                                        }, txtName]
                            }, {
                                columnWidth : 0.5,
                                border : false,
                                layout : "form",
                                items : [{
                                            border : false,
                                            height : 35
                                        }, txtDepName]
                            }]
                }]
    });

    var fp = new Ext.form.FormPanel({
                id : "form",
                region : "center",
                labelAlign : "right",
                labelWidth : 70,
                frame : true,
                autoHeight : true,
                items : [fs]
            });
    function selectUserCode() {
		var args = {
			selectModel : 'single',
			// modify by liuyi 为测试用，将管理员可以选到
//			notIn : "'999999'",
			notIn : "'888888'",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		// 根据返回值设置画面的值
		if (object) {
			if (typeof(object.workerName) != "undefined") {
				txtName.setValue(object.workerName);
			}
			if (typeof(object.workerCode) != "undefined") {
				txtUserCode.setValue(object.workerCode);
			}
			if (typeof(object.deptName) != "undefined") {
				txtDepName.setValue(object.deptName);
			}
		}
	}
    // 保存按钮处理函数
    function save() {
        // 人员编码不能为空
        if (txtUserCode.getValue() == null || txtUserCode.getValue() == "") {
            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                            MessageConstants.COM_E_002, "人员编码"));
            return;
        }
        var isOnly;
        Ext.Ajax.request({
            url : "administration/checkRegularWorkRight.action",
            method : "post",
            params : {
                strUserCode : txtUserCode.getValue()
            },
            success : function(result, request) {
                var data = eval('(' + result.responseText + ')');
                isOnly = data.isOnly;
                if (isOnly) {
                    Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
                            MessageConstants.COM_C_001, function(button, text) {
                                if (button == "yes") {
                                    fp.getForm().submit({
                                        url : "administration/addRegularWorkRight.action",
                                        methos : "post",
                                        params : {
                                            "right.worktypeCode" : drpWorkTypeCbx.getValue()
                                        },
                                        success : function(form, action) {
                                            store.baseParams.strWorkTypeCode = drpWorkTypeCbx.getValue();
                                            Ext.Msg.alert(
                                                            MessageConstants.SYS_REMIND_MSG,
                                                            MessageConstants.COM_I_004,
                                                            function() {
                                                            	win.hide();
                                                                store.load({
                                                                    params : {
                                                                        start : 0,
                                                                        limit : Constants.PAGE_SIZE
                                                                    }
                                                                });
                                                            });

                                        },
                                        failure : function(form, action) {}
                                    });
                                }
                            });
                } else {
                    Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.AW001_E_001);
                }
            },
            failure : function(result, request) {}
        });

    }
    // 取消按钮处理函数
    function cancel() {
    	Ext.MessageBox.confirm(Constants.NOTICE_CONFIRM,
				MessageConstants.COM_C_005, function(button, text) {
					if (button == "yes") {
						win.hide();
					}
				});
    }
    // 增加窗口
    var win = new Ext.Window({
    	        buttonAlign : "center",
				modal : true,
				width : 500,
				autoHeight : true,
				closeAction : "hide",
				resizable : false,
				items : [fp],
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : save
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : cancel
						}]
			});

    // 工作类别名
    var drpWorkTypeCbx = new Ext.form.CmbWorkType({
                value : '01',
                width : 80
            });
    // 工具栏
    var tbar = new Ext.Toolbar({
                items : ["工作类别:", drpWorkTypeCbx, "-", {
                            id : "add",
                            text : Constants.BTN_ADD,
                            iconCls : Constants.CLS_ADD,
                            handler : add
                        }, {
                            id : "deleteIt",
                            text : Constants.BTN_DELETE,
                            iconCls : Constants.CLS_DELETE,
                            handler : deleteIt
                        }]
            });
    // 新增按钮处理函数
    function add() {
        win.setTitle("新增权限人员");
        fp.getForm().reset();
        win.show();
    }
    // 删除按钮处理函数
    function deleteIt() {
        var selectedRows = grid.getSelectionModel().getSelections();
        if (selectedRows.length < 1) {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
            return;
        }
        var lastSelected = selectedRows[selectedRows.length - 1];
        Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
                MessageConstants.COM_C_002, function(button, text) {
                    if (button == "yes") {
                        Ext.Ajax.request({
                            url : "administration/deleteRegularWorkRight.action",
                            method : 'post',
							params : {
								"right.id" : lastSelected.get("id"),
								"updateTime" : lastSelected.get("updateTime")
							},
							success : function(result, request) {
								var result = eval('(' + result.responseText + ')');
								// 排他
								if (result.msg == Constants.DATA_USING) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
									return;
								}
								if (result.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								store.baseParams.strWorkTypeCode = drpWorkTypeCbx.getValue();
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, function() {
											store.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										});
							},
							failure : function(result, request) {}
						});
                    }
                });
    }
    // 工作类别名注册选择事件
    drpWorkTypeCbx.on("select", function(combo, record, index) {
    	        store.baseParams.strWorkTypeCode = drpWorkTypeCbx.getValue();
                Ext.Ajax.request({
                            url : "administration/regularWorkRight.action",
                            method : "post",
                            params : {
                                start : 0,
                                limit : Constants.PAGE_SIZE,
                                strWorkTypeCode : this.getValue()
                            },
                            success : function(result, request) {
                                var gridData = eval('(' + result.responseText + ')');
                                store.loadData(gridData);
                            },
                            failure : function(result, request) {}
                        });
            });
    // grid选择模式设为单行选择模式
    var sm = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
    // grid中的列定义
    var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
                        header : "行号",
                        width : 35
                    }), {
                header : "部门名称",
                dataIndex : "depName",
                width : 200
            }, {
                header : "人员编码",
                dataIndex : "userCode",
                align : "left"
            }, {
                header : "人员姓名",
                dataIndex : "name",
                align : "left"
            }]);
    cm.defaultSortable = true;
    // grid中的数据
    var recordWorkRight = Ext.data.Record.create([{
                name : "id"
            }, {
                name : "depName"
            }, {
                name : "userCode"
            }, {
                name : "name"
            }, {
                name : "updateTime"
            }]);
    // grid中的store
    var store = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : "administration/regularWorkRight.action"
                        }),
                reader : new Ext.data.JsonReader({
                            totalProperty : "totalCount",
                            root : "list"
                        }, recordWorkRight)
            });
    store.baseParams = ({
        strWorkTypeCode : drpWorkTypeCbx.getValue()
    });
    store.load({
                params : {
                    start : 0,
                    limit : Constants.PAGE_SIZE
                }
            });
    // 底部工具栏
    var bbar = new Ext.PagingToolbar({
                pageSize : Constants.PAGE_SIZE,
                store : store,
                displayInfo : true,
                displayMsg : MessageConstants.DISPLAY_MSG,
                emptyMsg : MessageConstants.EMPTY_MSG
            })
    // grid主体
    var grid = new Ext.grid.GridPanel({
                region : "center",
                layout : "fit",
                colModel : cm,
                sm : sm,
                tbar : tbar,
                bbar : bbar,
                enableColumnMove : false,
                store : store
            });
    // 设定布局器及面板
    var layout = new Ext.Viewport({
                layout : "border",
                border : false,
                enableTabScroll : true,
                items : [grid]
            });
})