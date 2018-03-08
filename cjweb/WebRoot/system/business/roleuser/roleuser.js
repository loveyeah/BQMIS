// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var w = screen.availWidth - 500;
	var h = screen.availHeight - 290;
	var roleId="";
	var scrwidth = document.body.clientWidth;
//	var deptId = new Ext.ux.ComboBoxTree({
//			fieldLabel : '所属部门',
//			id : 'deptId',
//			displayField : 'text',
//			valueField : 'id',
//			hiddenName : 'empinfo.deptId',
//			blankText : '请选择',
//			emptyText : '请选择',
//			width : 250,
//			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
//			tree : {
//				xtype : 'treepanel',
//				autoScroll : false,
//				loader : new Ext.tree.TreeLoader({
//					dataUrl : 'empInfoManage.action?method=getDep'
//				}),
//				root : new Ext.tree.AsyncTreeNode({
//					id : '0',
//					name : '合肥电厂',
//					text : '合肥电厂'
//				})
//			},
//			selectNodeModel : 'all'
//		})
//		var gwtbar = new Ext.Toolbar({
//			items : ['部门:',deptId]
//		})
	var wtbar = new Ext.Toolbar({
		items : ['<font color="blue">未选用户</font>',
			 {
				id : 'wuserlike',
				xtype : 'textfield',
				width : 80
//				hidden: true
			}, '-', {
			id : 'btnQuery',
			text : "查询",
			handler : function() {
				if(roleId==""){
					Ext.Msg.alert('提示信息','请选择角色！');
					return;
				};
				Ext.Ajax.request({
					url : 'system/findWaitByDeptIdAndRoleId.action',
					method : 'post',
					params : {
						start : 0,
						limit : 100,
						roleId : roleId,
//						deptId : deptId.getValue()==""?null:deptId.getValue(),
						userlike : Ext.get('wuserlike').dom.value

					},
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						wait_user_ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息',"查询失败！");
					}
				})
			}
		}]
	});
	var atbar = new Ext.Toolbar({
		items : ['<font color="blue">已选用户</font>', {
			id : 'auserlike',
			xtype : 'textfield',
			width : 80
		}, '-', {
			id : 'btnQuery',
			// iconCls : 'reflesh',
			text : "模糊查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findUserByRP.action',
					method : 'post',
					params : {
						start : 0,
						limit : 18,
						roleId : roleId,
						userlike : Ext.get("auserlike").dom.value
					},
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						user_ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息',"查询失败！");
					}
				})
			}
		}]
	});
	var rtbar = new Ext.Toolbar({
		items : ['角色查询:', {
			id : 'rlikeName',
			xtype : 'textfield',
			width : 80
		}, '-', {
			id : 'btnQuery',
			// iconCls : 'reflesh',
			text : "模糊查询",
			width : 80,
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findRole.action',
					params : {
						start : 0,
						limit : 18,
						likename : Ext.get("rlikeName").dom.value
					},
					method : 'post',
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息',"查询失败！");
					}
				})
			}
		}]
	});
	var win = new Ext.Window({
		el : 'window-win',
		title : '增加管理员',
		width : 400,
		height : 250,
		closeAction : 'hide'
			// buttons : [tbar_add]

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findRole.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.total',
			root : 'data.rolesList'
		}, [{
			name : 'roleId',
			hidden : true
		}, {
			name : 'roleName'
		}])
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			likename : ''
		}
	});
	
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				
				Ext.Msg.wait("请等候", "加载中", "操作进行中...");
				roleId = rec.data.roleId;
				user_ds.load({
					params : {
						start : 0,
						limit : 100,
						roleId : roleId,
						userlike : Ext.get("auserlike").dom.value
					}
				});
				alreadyRoleGrid.getView().refresh();
				wait_user_ds.load({
					params : {
//						start : 0,
//						limit : 100,
//						roleId : roleId,
//						userlike : Ext.get("wuserlike").dom.value
						start : 0,
						limit : 100,
						roleId : roleId,
//						deptId : deptId.getValue()==""?null:deptId.getValue(),
						userlike : Ext.get('wuserlike').dom.value
					}
				});
				waitRoleGrid.getView().refresh();
				setTimeout(function() {
					Ext.Msg.hide();
				}, 250);
			}
		}
	});
	var colModel = new Ext.grid.ColumnModel([{
		header : '角色名称',
		dataIndex : 'roleName',
		width : 170
	}]);
	colModel.defaultSortable = true;
	var rbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var roleGrid = new Ext.grid.GridPanel({
		id : 'role-grid',
		el : 'role-div',
		ds : ds,
		cm : colModel,
		sm : sm,
		bbar : rbbar,
		tbar : rtbar,
		border : true,
		width : 220,
		fitToFrame : true,
		listeners : {
			render : function(g) {
				g.selModel.selectFirstRow();
			},
			delay : 10
		}
	});
	roleGrid.render();
	roleGrid.addListener('rowcontextmenu', function(_grid, rowIndex, e) {
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '查看角色权限',
				iconCls : 'filep',
				handler : function() {
					var roleId = roleGrid.getStore().getAt(rowIndex).data.roleId;
					var File = Ext.data.Record.create([{
						name : 'fileId'
					}, {
						name : 'fileName'
					}])
					var file_store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : 'system/findgridFilesByRoleId.action'
						}),
						reader : new Ext.data.JsonReader({
							totalCount : 'data.totalCount',
							root : 'data.list'
						}, File)
					});
					var file_cm = new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(), {
								header : '权限名称',
								dataIndex : 'fileName',
								align : 'left',
								width : 200
							}]);
					file_cm.defaultSortable = true;
					var filegrid = new Ext.grid.EditorGridPanel({
						ds : file_store,
						cm : file_cm,
						region : 'center',
						title : '所有权限列表',
						fitToFrame : true,
						border : false
					})

					var filewin = new Ext.Window({
						closable : true,
						width : 300,
						height : 400,
						plain : true,
						layout : 'border',
						items : [filegrid]
					});
					filewin.show();
					Ext.Ajax.request({
						url : 'system/findgridFilesByRoleId.action',
						method : 'post',
						params : {
							roleId : roleGrid.getStore().getAt(rowIndex).data.roleId,
							filelike : ''
						},
						success : function(result, request) {
							var gridData = eval('(' + result.responseText + ')');
							file_store.loadData(gridData);
						},
						failure : function(result, request) {
							Ext.Msg.alert('查询失败，请联系管理员！');
						}
					})
				}
			})]
		});
		e.preventDefault();
		menu.showAt(e.getXY());
	});
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
	var user_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findUserByRP.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, User)
	});

	var wait_user_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findWaitByDeptIdAndRoleId.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, User)
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			likename : Ext.get("rlikeName").dom.value
		});
	});
	wait_user_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			userlike : Ext.get("wuserlike").dom.value,
			roleId : roleId
		});
//		params : {
////						start : 0,
////						limit : 100,
////						roleId : roleId,
////						userlike : Ext.get("wuserlike").dom.value
//						start : 0,
//						limit : 100,
//						roleId : roleId,
//						deptId : deptId.getValue()==""?null:deptId.getValue(),
//						userlike : Ext.get('wuserlike').dom.value
//					}
	});
	user_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			userlike : Ext.get("auserlike").dom.value,
			roleId : roleId
		});
	});
	/* 设置每一行的选择框 */
	var user_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});
	var wait_user_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				// Ext.getCmp("user-form").getForm().loadRecord(rec);
			}
		}
	});
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
	// //////////////////////////////////////////////////////////////////////////////不包含用户
	var wbbar = new Ext.PagingToolbar({
		pageSize : 100,
		store : wait_user_ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var waitRoleGrid = new Ext.grid.EditorGridPanel({
		el : 'wait-role-div',
		ds : wait_user_ds,
		cm : user_cm,
		sm : wait_user_sm,
		width : 260,
		border : true,
		bbar : wbbar,
		tbar : wtbar,
		fitToFrame : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		}),
		viewConfig : {
			forceFit : true
		}
	});
	waitRoleGrid.render();
	var abbar = new Ext.PagingToolbar({
		pageSize : 100,
		store : user_ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	var alreadyRoleGrid = new Ext.grid.GridPanel({
		el : 'already-role-div',
		ds : user_ds,
		cm : user_cm,
		sm : user_sm,
		bbar : abbar,
		tbar : atbar,
		width : 220,
		border : true,
		fitToFrame : true,
		viewConfig : {
			forceFit : true
		}
	});
	alreadyRoleGrid.render();
	var grantBar = new Ext.Toolbar({
		items : [{
			id : 'btnGrant',
			text : "<<<",
			handler : function() {
				var selectedRows = waitRoleGrid.getSelectionModel()
						.getSelections();
				var ids = "";
				for (i = 0; i < selectedRows.length; i++) {
					ids += selectedRows[i].data.workerId + ",";
				}
				if (ids.length > 0) {
					ids = ids.substring(0, ids.length - 1);
					var currentRole = roleGrid.getSelectionModel()
							.getSelected();
					var roleId = currentRole.data.roleId;
					Ext.Ajax.request({
						url : 'system/addUserInRolesList.action',
						method : 'post',
						params : {
							ids : ids,
							'roleuser.roleId' : roleId
						},
						success : function(result, request) {
							for (i = 0; i < selectedRows.length; i++) {
								var wr = selectedRows[i];
								var wp = new User({
									workerId : wr.data.workerId,
									workerName : wr.data.workerName,
									workerCode : wr.data.workerCode,
									ifUse : wr.data.ifUse,
									style : wr.data.style,
									email : wr.data.email,
									modifyBy : wr.data.modifyBy,
									modifyDate : wr.data.modifyDate
								});
								user_ds.insert(0, wp);
								wait_user_ds.remove(wr);
							}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				} else {
					Ext.MessageBox.alert('提示', '请从右边选择人员!');
				}
			}
		}]
	});
	grantBar.render("grant-div");
	var revokeBar = new Ext.Toolbar({
		items : [{
			id : 'btnRevoke',
			text : ">>>",
			handler : function() {

				var selectedRows = alreadyRoleGrid.getSelectionModel()
						.getSelections();
				var ids = "";
				for (i = 0; i < selectedRows.length; i++) {
					ids += selectedRows[i].data.workerId + ",";
				}
				if (ids.length > 0) {
					ids = ids.substring(0, ids.length - 1);
					var currentRole = roleGrid.getSelectionModel()
							.getSelected();
					var roleId = currentRole.data.roleId;
					Ext.Ajax.request({
						url : 'system/delUserInRolesList.action',
						method : 'post',
						params : {
							ids : ids,
							'roleuser.roleId' : roleId
						},
						waitMsg : '正在处理...',
						success : function(result, request) {
							for (i = 0; i < selectedRows.length; i++) {
								var r = selectedRows[i];
								var p = new User({
									workerId : r.data.workerId,
									workerCode : r.data.workerCode,
									workerName : r.data.workerName,
									ifUse : r.data.ifUse,
									style : r.data.style,
									email : r.data.email,
									modifyBy : r.data.modifyBy,
									modifyDate : r.data.modifyDate
								});
								wait_user_ds.insert(0, p);
								user_ds.remove(r);
							}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				} else {
					Ext.MessageBox.alert('提示', '请从左边选择人员!');
				}
			}
		}]
	});
	revokeBar.render("revoke-div");
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
	var layout=new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : 'center',
			layout : 'border',
			border : false,
			split : true,
			items : [{
				region : 'center',
				layout : 'fit',
				border : true,
				margins : '0 0 0 0',
				split : true,
				collapsible : false,
				items : [alreadyRoleGrid]
			},{
				region : 'east',
				layout : 'border',
				border : false,
				width : 40,
				items :[{
					region : 'north',
					layout : 'fit',
					border : true,
					margins : '200 0 0 0',
//					margins : '300 0 0 0',
					split : false,
					height : 30,
					items : [grantBar]
				},{
					region : 'center',
					layout : 'fit',
					border : false,
					margins : '0 0 0 0',
					split : false
//					height : 20,
//					items : [grantBar]
				},{
					region : 'south',
					layout : 'fit',
					border : true,
					margins : '0 0 300 0',
//					margins : '0 0 400 0',
					split : false,
					height : 30,
					items : [revokeBar]
				}]
			}]
		}, {
			region : "west",
			collapsible : true,
//			title : '角色列表',
			layout : 'fit',
			border : true,
			margins : '0 0 0 50',
//			width : 230,
			width : scrwidth/11*3,
			split : true,
			items : [roleGrid]
		},{
			region : "east",
			layout : 'fit',
			border : true,
			margins : '0 50 0 0',
			width : scrwidth/11*3,
			items : [waitRoleGrid]
//			[{
//					region : 'center',
//					layout : 'fit',
//					border : false,
//					margins : '0 0 0 0',
//					split : false,
//					items : [waitRoleGrid]
//				},{
//					region : 'north',
//					layout : 'fit',
//					border : true,
//					split : false,
//					height : 20,
//					items : [gwtbar]
//				}
//			]
		}]
	
	})
});
