Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

    //↓↓**********************角色选择********************↓↓//
    // 角色列表组合框store
	var roleListStore = new Ext.data.JsonStore({
		url:"workticket/getroleList.action",
		root : 'list',
		fields:[{name:"roleName"},{name:"roleId"}]
	});
	var deptId = new Ext.ux.ComboBoxTree({
			fieldLabel : '所属部门',
			id : 'deptId',
			displayField : 'text',
			valueField : 'id',
			hiddenName : 'empinfo.deptId',
			blankText : '请选择',
			emptyText : '请选择',
			width : 100,
			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
			tree : {
				xtype : 'treepanel',
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'empInfoManage.action?method=getDep'
				}),
				root : new Ext.tree.AsyncTreeNode({
					id : '0',
					name : '大唐灞桥热电厂',
					text : '大唐灞桥热电厂'
				})
			},
			selectNodeModel : 'all'
		})
    // 载入数据
	roleListStore.load({
		params:{
			rolename:"票负责人"
		}
	});

    // 角色列表组合框
	var roleListCbo = new Ext.form.ComboBox({
		id : "role_list",
		allowBlank : false,
		triggerAction : 'all',
		store :roleListStore,
		displayField : 'roleName',
        valueField : 'roleId',
        mode : 'local',
        emptyText : '角色列表...',
        blankText : '角色列表',
        readOnly : true ,
        listeners : {
            // 选择角色之后加载相应用户信息
			select  : function(combo, record, index) {
				Ext.Msg.wait("请等候", "加载中", "操作进行中...");
				// 载入已选用户信息
                user_ds.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
                // 刷新
                alreadyRoleGrid.getView().refresh();
                // 载入未选用户信息
				wait_user_ds.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
                // 刷新
				waitRoleGrid.getView().refresh();
				// 超时时间
                setTimeout(function() {
					Ext.Msg.hide();
				}, 250);
			}
		}
	});
    // 默认选择处理
   roleListStore.on('load',function(store, records, options){
                                        if(records.length > 0){
                                            roleListCbo.setValue(records[0].data.roleId);
                                            roleListCbo.fireEvent('select',roleListCbo,records[0],0);
                                        }

            });
    // 包含角色列表组合框的头部
	var header = new Ext.Panel({
		renderTo:"header",
		labelAlign:"left",
        autoScroll : false,
		height: 30,
		border: false,
		tbar:['票负责人:', roleListCbo]
	});
    //↑↑**********************角色选择********************↑↑//

	//用户记录格式
	var User = Ext.data.Record.create([{
		name : 'workerId'
	}, {
		name : 'workerCode'
	}, {
		name : 'workerName'
	}, {
		name : 'isUse'
	}, {
		name : 'style'
	}, {
		name : 'email'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}]);
	// 设置每一行的选择框
	var user_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	// grid列模式
	var user_cm = new Ext.grid.ColumnModel([user_sm, {
		header : '工号',
		dataIndex : 'workerCode',
		align : 'left'
	}, {
		header : '姓名',
		dataIndex : 'workerName',
		align : 'left'
	}]);
	user_cm.defaultSortable = true;


	//↓↓**********************已选用户Grid************↓↓//

	// 数据源
	var user_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getUserList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, User)
	});
	// 工具栏
	var atbar = new Ext.Toolbar({
		items : ['<font color="blue">已选用户</font>', {
			id : 'auserlike',
			xtype : 'textfield',
			width : 80
		}, '-', {
			id : 'btnQuery',
			text : "模糊查询",
			iconCls:'query',
			handler : function() {
                // 如果未选择角色弹出提示信息
				if(typeof(roleListCbo.value) == 'undefined'){
					Ext.Msg.alert("警告","请选择一个票负责人");
					return;
				}
                // 请求已选用户
				Ext.Ajax.request({
					url : 'workticket/getUserList.action',
					method : 'post',
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						roleId : roleListCbo.value,
						iswait: false,
						userlike : Ext.get("auserlike").dom.value
					},
					waitMsg : '正在查询数据...',
					// 请求成功加载数据
                    success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						user_ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert("查询失败！");
					}
				})
			}
		}]
	});
	// 分页栏
	var abbar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : user_ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	// grid
	var alreadyRoleGrid = new Ext.grid.GridPanel({
		el : 'already-role-div',
		ds : user_ds,
		cm : user_cm,
		sm : user_sm,
		bbar : abbar,
		tbar : atbar,
		width : 300,
		border : true,
		fitToFrame : true,
		viewConfig : {
			forceFit : true
		}
	});
    // 绑定到div
	alreadyRoleGrid.render();
	//↑↑**********************已选用户Grid************↑↑//

	//↓↓**********************未选用户Grid************↓↓//

    // 工具栏
	var wtbar = new Ext.Toolbar({
		items : ['<font color="blue">未选用户</font>', {
			id : 'wuserlike',
			xtype : 'textfield',
			width : 40
		}, '-','部门', deptId,{
			id : 'btnQuery',
			text : "模糊查询",
			iconCls:'query',
			handler : function() {
                // 如果未选择角色弹出提示信息
				if(typeof(roleListCbo.value) == 'undefined'){
					Ext.Msg.alert(Constants.NOTICE,"请选择一个票负责人");
					return;
				}
                // 请求数据
				Ext.Ajax.request({
					url : 'workticket/getUserList.action',
					method : 'post',
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						roleId : roleListCbo.value,
						iswait: true,
						userlike : Ext.get("wuserlike").dom.value,
						deptId : deptId.getValue()==""?null:deptId.getValue()
					},
					waitMsg : '正在查询数据...',
					// 加载数据
                    success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						wait_user_ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert("查询失败！");
					}
				})
			}
		}]
	});

    // 选择模式
	var wait_user_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	// 数据源
	var wait_user_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getUserList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, User)
	});
    // 分页栏
	var wbbar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : wait_user_ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});

    // 待选用户grid
	var waitRoleGrid = new Ext.grid.EditorGridPanel({
		el : 'wait-role-div',
		ds : wait_user_ds,
		cm : user_cm,
		sm : wait_user_sm,
		width : 300,
		border : true,
		bbar : wbbar,
		tbar : wtbar,
		fitToFrame : true,
		selModel : new Ext.grid.RowSelectionModel({
			// 设置多选
            singleSelect : false
		}),
		viewConfig : {
			forceFit : true
		}
	});
    // 绑定到div
	waitRoleGrid.render();
	//↑↑**********************未选用户Grid************↑↑//

	//↓↓**********************中间按钮************↓↓//
	// 添加用户button
    var grantBar = new Ext.Button({
		id : 'btnGrant',
		text : "<<<",
		handler : function() {
            // 获得选中的id组
			var selectedRows = waitRoleGrid.getSelectionModel().getSelections();
			var ids = "";
			for (i = 0; i < selectedRows.length; i++) {
				ids += selectedRows[i].data.workerId + ",";
			}
            // 如果有选择记录
			if (ids.length > 0) {
				ids = ids.substring(0, ids.length - 1);
				// 增加用户
                Ext.Ajax.request({
							url : 'workticket/addSelectedUser.action',
							method : 'post',
							params : {
								ids : ids,
								'roleuser.roleId' : roleListCbo.value
							},
							success : function(result, request) {
                               user_ds.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                alreadyRoleGrid.getView().refresh();
                                // 未选用户grid数据加载
                                wait_user_ds.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                waitRoleGrid.getView().refresh();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
							}
						});
			} else {
				Ext.MessageBox.alert(Constants.SYS_REMIND_MSG, '请从右边选择人员!');
			}
		}

	});
    // 绑定到div
	grantBar.render("grant-div");

    // 去除用户button
	var revokeBar = new Ext.Button({
			id : 'btnRevoke',
			text : ">>>",
			handler : function() {
                // 获得选中的id组
				var selectedRows = alreadyRoleGrid.getSelectionModel().getSelections();
				var ids = "";
				for (i = 0; i < selectedRows.length; i++) {
					ids += selectedRows[i].data.workerId + ",";
				}
                // 如果有选择记录
				if (ids.length > 0) {
					ids = ids.substring(0, ids.length - 1);
					// 移除用户
                    Ext.Ajax.request({
						url : 'workticket/delSelectedUser.action',
						method : 'post',
						params : {
							ids : ids,
							'roleuser.roleId' : roleListCbo.value
						},
						waitMsg : '正在处理...',
						success : function(result, request) {
                          user_ds.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                alreadyRoleGrid.getView().refresh();
                                // 未选用户grid数据加载
                                wait_user_ds.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                waitRoleGrid.getView().refresh();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
						}
					});
				} else {
					Ext.MessageBox.alert(Constants.SYS_REMIND_MSG, '请从左边选择人员!');
				}
			}
	});
    // 绑定到div
	revokeBar.render("revoke-div");
    // storeload处理
    user_ds.on('beforeload',function(){
        Ext.apply(this.baseParams, {
               roleId : roleListCbo.value,
               iswait: false,
               userlike : Ext.get("auserlike").dom.value
            })           
      });
    wait_user_ds.on('beforeload',function(){
        Ext.apply(this.baseParams, {
               roleId : roleListCbo.value,
               iswait: true,
               userlike : Ext.get("wuserlike").dom.value,
               deptId : deptId.getValue()==""?null:deptId.getValue()
            })
      });
	//↑↑**********************中间按钮************↑↑//
});
