// 协作单位维护
// author:zhaozhijie
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();

    // 协作单位信息
    var dataTeam = Ext.data.Record.create([
        // 协作单位id
        {
        name : "cooperateUnitId"
    },
        // 协作单位名称
        {
            name : "cooperateUnit"
        },
        // 显示顺序
        {
            name : "orderBy"
        }])
    // 协作单位store
    var dsTeam = new Ext.data.JsonStore({
        url : "hr/getCooperateUnitList.action",
        root : "list",
        totalProperty : 'totalCount',
        fields : dataTeam,
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
    dsTeam.load({
    		params : {
					start : 0,
					limit : Constants.PAGE_SIZE
			}
    })
    dsTeam.setDefaultSort("orderBy");
    // 新增
    var btnAdd = new Ext.Button({
        iconCls : Constants.CLS_ADD,
        text : Constants.BTN_ADD,
        handler : addHandler
    })
    // 修改
    var btnUpd = new Ext.Button({
        iconCls : Constants.CLS_UPDATE,
        text : Constants.BTN_UPDATE,
        handler : updateHandler
    })
    // 删除
    var btnDel = new Ext.Button({
        iconCls : Constants.CLS_DELETE,
        text : Constants.BTN_DELETE,
        handler : deleteHandler
    })
    var teamtbar = new Ext.Toolbar({
        height : 25,
        items : [btnAdd, btnUpd, btnDel]
    })
    // 协作单位grid
    var teamGrid = new Ext.grid.GridPanel({
        layout : 'fit',
        border : true,
        autoWidth : true,
        // 标题不可以移动
        enableColumnMove : false,
        store : dsTeam,
        // 单选
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns : [new Ext.grid.RowNumberer({
                header : "行号",
                align : 'right',
                width : 35
            }),
            // 协作单位ID
            {
                header : "协作单位ID",
                width : 100,
                sortable : true,
                align : 'left',
                defaultSortable : true,
                dataIndex : 'cooperateUnitId'
            },
            // 协作单位名称
            {
                header : "协作单位名称",
                width : 350,
                sortable : true,
                align : 'left',
                dataIndex : 'cooperateUnit'
            },
            // 显示顺序
            {
                header : "显示顺序",
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'orderBy'
            }],
        tbar : teamtbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : dsTeam,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    })
    teamGrid.on("rowdblclick",updateHandler);
    // 显示区域
    var view = new Ext.Viewport({
        enableTabScroll : true,
        autoScroll : true,
        layout : "fit",
        items : [teamGrid]
    })
    // ============= 定义弹出画面 ===============
    // 协作单位ID
    var txtTeamId = new Ext.form.TextField({
    	id : "teamUnitId",
        fieldLabel : '协作单位ID',
        value : Constants.AUTO_CREATE,
        width : 180,
        disabled : true
    })
    var hdnTeamId = new Ext.form.Hidden({
        id : "cooperateUnit.cooperateUnitId",
        name : "cooperateUnit.cooperateUnitId"
    })
    // 协作单位名称
    var txtTeamName = new Ext.form.TextField({
        id : "cooperateUnit.cooperateUnit ",
        fieldLabel : "协作单位名称<font color='red'>*</font>",
        width : 180,
        maxLength : 50,
        allowBlank : false
    })
    // 显示顺序
    var txtOrderBy = new Ext.form.NaturalNumberField({
    	id : "cooperateUnit.orderBy",
        fieldLabel : '显示顺序',
        value : "",
        width : 180,
        maxLength : 10
     })
    // 添加panel
    var addPanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        items : [txtTeamId, hdnTeamId, txtTeamName, txtOrderBy]
    });
    // 窗口
    var win = new Ext.Window({
        width : 350,
        height : 150,
        buttonAlign : "center",
        items : [addPanel],
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : saveHandler
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : cancelHandler
            }]
    });
    // 新增操作
    function addHandler() {
    	addPanel.getForm().reset();
    	win.x=undefined;
		win.y=undefined;
        win.show();
        win.setTitle("新增协作单位");
    }
    // 修改操作
    function updateHandler() {
    	if (!teamGrid.selModel.hasSelection()) {
    		Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
    	} else {
    		var record = teamGrid.selModel.getSelected();
    		win.x=undefined;
			win.y=undefined;
    		win.show();
	    	txtTeamId.setValue(record.get("cooperateUnitId"));
	    	hdnTeamId.setValue(record.get("cooperateUnitId"));
	    	txtTeamName.setValue(record.get("cooperateUnit"));
	    	txtOrderBy.setValue(record.get("orderBy"));
	        win.setTitle("修改协作单位");
	        
    	}
    }
    // 删除操作
    function deleteHandler() {
    	if (!teamGrid.selModel.hasSelection()) {
    		Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
    	} else {
    		// 确认删除信息
			Ext.MessageBox.confirm(Constants.CONFIRM,
				Constants.COM_C_002, function(button, text) {
					if (button == "yes") {
						var record = teamGrid.selModel.getSelected();
						Ext.Ajax.request({
							method : Constants.POST,
         					url : "hr/deleteCooperateUnit.action",
         					params : {
         						id : record.get("cooperateUnitId")
         					},
         					success: function(result, request) {
         						var o = eval('(' + result.responseText + ')');
         						// sql失败
         						if (o.msg == Constants.SQL_FAILURE) {
					         		Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
					         		return;
					         	// 删除成功message
					         	} else {
         			         		Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
						         	dsTeam.load({
						         		params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										}
						         	});
					         	}
         					},
         					faliue : function() {
	         					Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
	         				}
						})
					}
			})
    	}
    }
    // 保存操作
    function saveHandler() {
         var teamId = Ext.get("teamUnitId").dom.value;
         if (teamId == Constants.AUTO_CREATE) {
        	hdnTeamId.setValue("-1");
         } else {
         // 用于后台查询
         	hdnTeamId.setValue(teamId);
         }
         // 不为空check
         if (txtTeamName.getValue() != null && txtTeamName.getValue() != "") {
         	 // 确认保存信息
			Ext.MessageBox.confirm(Constants.CONFIRM,
				Constants.COM_C_001, function(button, text) {
					if (button == "yes") {
						    addPanel.getForm().submit({
						     	method : Constants.POST,
						     	url : "hr/saveCooperateUnit.action",
						     	success : function(form, action) {
						         	var o = eval("(" + action.response.responseText + ")");
						         	// sql失败
						         	if (o.msg == Constants.SQL_FAILURE) {
						         		Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						         		return;
						         	// 保存成功
						         	} else {
						         		Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
								         	win.hide();
						         		});
						         		dsTeam.load({
							         		params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
							         	});

						         	}
						     	},
						     	faliue : function() {
						         	Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						         }
						     });
					}
			})
	     // 报message
         } else {
         	Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_002,"协作单位名称"));
         }

    }
    // 取消操作
    function cancelHandler() {
    	// 确认取消信息
    	Ext.MessageBox.confirm(Constants.CONFIRM,
				Constants.COM_C_005, function(button, text) {
					if (button == "yes") {
					     win.hide();
					}
		})
    }
})