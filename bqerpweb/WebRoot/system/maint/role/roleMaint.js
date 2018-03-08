// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var method = "";
	var Tree = Ext.tree;
	// var wroot = new Tree.AsyncTreeNode({
	// text : '合肥电厂',
	// draggable : false,
	// id : '1'
	// });
	// var wtree = new Tree.TreePanel({
	// title : '未选功能模块树',
	// checkModel : 'cascade', // 对树的级联多选
	// onlyLeafCheckable : false,// 对树所有结点都可选
	// autoScroll : true,
	// autoHeight : true,
	// split : true,
	// root : wroot,
	// animate : true,
	// enableDD : false,
	// border : false,
	// rootVisible : true,
	// containerScroll : true,
	// loader : new Tree.TreeLoader({
	// dataUrl : 'system/findFiles.action',
	// baseAttrs : {
	// uiProvider : Ext.tree.TreeCheckNodeUI
	// }
	// })
	// })
	// wtree.on('beforeload', function(node) {
	// wtree.loader.dataUrl = 'system/findFiles.action'
	// });
	// wtree.setRootNode(wroot);
	// var aids = "";
	// var anames = "";
	// wtree.on("check", function(node, checked) {
	// aids += node.id;
	// aids += ",";
	// anames += node.text;
	// anames += ",";
	// });
	// if (aids.length > 0) {
	// aids = aids.substring(0, ids.length - 1);
	// anames = anames.substring(0, anames.length - 1);
	// }
	// var wtreeWin = new Ext.Window({
	// el : 'wait-tree-div',
	// autoScroll : true,
	// width : 300,
	// // height : 300,
	// autoHeight : true,
	// items : [wtree],
	// closeAction : 'hide',
	// buttons : [{
	// text : '保存',
	// iconCls : 'filesave',
	// handler : function() {
	// // 取得以选择节点的id与text
	//
	// var selectNodes = wtree.getChecked();
	// var aids = "";
	// var anames = "";
	// if (selectNodes.length > 0) {
	// for (i = 0; i < selectNodes.length; i++) {
	// aids = aids + selectNodes[i].id + ","
	// anames = anames + selectNodes[i].text + ","
	// }
	// }
	// Ext.get("fileIds").dom.value = aids;
	// Ext.get("fileNames").dom.value = anames;
	// wtreeWin.hide();
	// // aids = "";
	// // anames = "";
	// }
	// }, {
	// text : '取消',
	// iconCls : 'filecanse',
	// handler : function() {
	// wtreeWin.hide();
	// aids = "";
	// anames = "";
	// }
	// }]
	// });

	var deptId = new Ext.ux.ComboBoxTree({
				fieldLabel : '授权部门',
				id : 'deptName',
				displayField : 'text',
				valueField : 'id',
				// hiddenName : 'role.accreditSection',
				blankText : '请选择',
				emptyText : '请选择',
				tree : {
					xtype : 'treepanel',
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'empInfoManage.action?method=getDep'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '-1',
								name : '灞桥电厂',
								text : '灞桥电厂'
							}),
					listeners : {
						click : setDeptId

					}
				},

				selectNodeModel : 'all'
			})

	function setDeptId(node) {
		Ext.get("deptId").dom.value = node.id;
	}
	var gridForm = new Ext.FormPanel({
		id : 'role-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '角色详细信息',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			defaults : {
				width : 240
			},
			items : [{
						name : 'role.roleId',
						xtype : 'hidden'
					}, {
						name : 'role.roleType',
						xtype : 'hidden'
					}, {
						fieldLabel : '角色名称<font color="red">*</font>',
						allowBlank : false,
						name : 'role.roleName'
					}, {
						xtype : 'textarea',
						fieldLabel : '备注',
						name : 'role.memo'
					}, {
						xtype : 'numberfield',
						fieldLabel : '排序',
						name : 'role.line'
					}, {
						xtype : 'hidden',
						name : 'fileIds'
					}, {
						xtype : 'trigger',
						fieldLabel : '配置权限',
						name : "fileNames",
						readOnly : true,
						onTriggerClick : function(e) {
							if (method == 'add') {
								// wtreeWin.show();
								// wroot.expand();
								var o = window
										.showModalDialog("module.jsp", "",
												"dialogHeight:400px;dialogWidth:200px;status:no;scroll:yes;help:no");
								if (o != null) {
									Ext.get("fileIds").dom.value = o.aids;
									Ext.get("fileNames").dom.value = o.anames;
								}
							} else {
								Ext.Msg.alert('提示信息', "请到角色对应模块页面修改！");
							}
						}
					}, {
						xtype : 'hidden',
						id : 'deptId',
						name : 'role.accreditSection'
					}, deptId],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					// alert(Ext.get("deptId").dom.value)
					//					
					// return;
					if (Ext.get("fileNames").dom.value == null
							&& Ext.get("fileNames").dom.value.length < 1)
						Ext.get("fileIds").dom.value = "";
					if (gridForm.getForm().isValid()) {
						if (Ext.get("role.roleName").dom.value.trim(" ") == "") {
							Ext.Msg.alert('注意', '角色名称不能全是空格!');
							return false;
						}
						gridForm.getForm().submit({
							url : 'system/'
									+ (Ext.get("role.roleId").dom.value == ""
											? "addRole"
											: "updateRole") + '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								ds.load({
											params : {
												start : 0,
												limit : 18,
												likename : Ext.get("like").dom.value
											}
										});
								win.hide();
								gridForm.getForm().reset();
								roleGrid.getView().refresh();
							},
							failure : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert('错误', o.errMsg);
							}
						});
						win.hide();
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.hide();
					gridForm.getForm().reset();
				}
			}]
		}]
	});
	var Role = new Ext.data.Record.create([{
				name : 'enterpriseCode'
			}, {
				name : 'roleId'
			}, {
				name : 'roleName'
			}, {
				name : 'memo'
			}, {
				name : 'line'
			}, {
				name : 'modifyBy'
			}, {
				name : 'modifyDate'
			}, {
				name : 'isUse'
			}, {
				name : 'accreditSection'
			}, {
				name : 'accreditSectionname'
			}]);
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'system/findRole.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'data.total',
							root : 'data.rolesList'
						}, Role)
			});
	ds.load({
				params : {
					start : 0,
					limit : 18,
					likename : ''
				}
			});
	var rolesbox = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
					}
				}
			});
	var colModel = new Ext.grid.ColumnModel([rolesbox, {
				id : 'roleId',
				header : '角色编号',
				dataIndex : 'roleId',
				sortable : true
			}, {
				header : '角色名称',
				dataIndex : 'roleName',
				width : 130
			}, {
				header : '备注',
				dataIndex : 'memo',
				width : 275
			}, {
				header : '最后修改人',
				dataIndex : 'modifyBy'
			}, {
				header : '最后修改时间',
				dataIndex : 'modifyDate',
				width : 120
			}, {
				header : '授权部门',
				dataIndex : 'accreditSectionname',
				width : 150
			}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : ['角色编号/名称', {
					name : 'like',
					xtype : 'textfield'
				}, {
					id : 'query',
					text : "模糊查询",
					handler : function() {
						Ext.Ajax.request({
									url : 'system/findRole.action',
									method : 'post',
									waitMsg : '正在查询数据...',
									params : {
										start : 0,
										limit : 18,
										likename : Ext.get("like").dom.value
									},
									success : function(result, request) {
										var gridData = eval('('
												+ result.responseText + ')');
										ds.loadData(gridData);
									},
									failure : function(result, request) {
										Ext.Msg.alert('失败，请联系管理员！');
									}
								})
					}
				}, '-', {
					id : 'btnAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						method = "add";
						win.show();
						gridForm.getForm().reset();
						Ext.get("role.roleName").dom.focus();
					}
				}, '-', {
					id : 'btnDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var rec = roleGrid.getSelectionModel().getSelections();
						var allroleNames = "";
						for (i = 0; i < rec.length; i++) {
							allroleNames += rec[i].data.roleName + ",";
						}
						if (allroleNames.length > 0) {
							allroleNames = allroleNames.substring(0,
									allroleNames.length - 1);
						} else
							Ext.Msg.alert('提示信息', "请选择你要删除的行！");
						if (confirm("确定要删除\"" + allroleNames + "\"角色吗？")) {
							for (i = 0; i < rec.length; i++) {
								Ext.Ajax.request({
											url : 'system/delRole.action?role.roleId='
													+ rec[i].get("roleId"),
											method : 'post',
											waitMsg : '正在删除数据...',
											success : function(result, request) {
											},
											failure : function(result, request) {
												Ext.MessageBox.alert('提示信息',
														'删除失败！');
											}
										})
							};
							ds.load({
										params : {
											start : 0,
											limit : 18,
											likename : Ext.get("like").dom.value
										}
									});
						}
					}
				}, '-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : modify
				}, '-', {
					id : 'btnReflesh',
					text : "刷新",
					iconCls : 'reflesh',
					handler : function() {
						ds.load({
									params : {
										start : 0,
										limit : 18,
										likename : ''
									}
								});
					}
				}, '-', '<font color="red">右击表格中的角色可以查询用户与权限!</font>']
	});
	function modify() {
		var rec = roleGrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			win.show();
			fillFormValue(rec[0]);
		}

	}
	function fillFormValue(rec) {
		Ext.get("role.roleId").dom.value = rec.get("roleId");
		Ext.get("role.roleName").dom.value = rec.get("roleName");
		Ext.get("role.memo").dom.value = (rec.get("memo") == null
				? ""
				: rec.data.memo);
		Ext.get("role.line").dom.value = (rec.get("line") == null
				? ""
				: rec.data.line);
		Ext.get("role.roleType").dom.value = rec.get("roleType");
		if (rec.get("accreditSection") == null
				&& rec.get("accreditSectionname") == null) {
			Ext.get("deptId").dom.value = "";
			Ext.get("deptName").dom.value = "";
		} else {
			Ext.get("deptId").dom.value = rec.get("accreditSection");
			Ext.get("deptName").dom.value = rec.get("accreditSectionname");
		}
	}
	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : '共 {2} 条',
				emptyMsg : "没有记录"
			});
	var roleGrid = new Ext.grid.GridPanel({
				id : 'role-grid',
				ds : ds,
				cm : colModel,
				sm : rolesbox,
				bbar : bbar,
				tbar : tbar,
				border : false,
				autoWidth : true,
				// autoHeight : true,
				fitToFrame : true
			});
	ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							likename : Ext.get("like").dom.value
						});
			});
	roleGrid.on('rowdblclick', function(grid, rowIndex, e) {
				win.show();
				method = "";
				var rec = roleGrid.getStore().getAt(rowIndex);
				fillFormValue(rec);
			});
	roleGrid.addListener('rowcontextmenu', function(_grid, rowIndex, e) {
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '查看用户',
				iconCls : 'personp',
				handler : function() {
					var User = Ext.data.Record.create([{
								name : 'workerId'
							}, {
								name : 'workerCode'
							}, {
								name : 'workerName'
							}])
					var user_store = new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
											url : 'system/findUserByRP.action'
										}),
								reader : new Ext.data.JsonReader({
											totalCount : 'data.totalCount',
											root : 'data.list'
										}, User)
							});
					var user_cm = new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(), {
								header : '工号',
								dataIndex : 'workerCode',
								align : 'left',
								width : 200
							}, {
								header : '姓名',
								dataIndex : 'workerName',
								align : 'left',
								width : 200
							}]);
					user_cm.defaultSortable = true;
					var usergrid = new Ext.grid.EditorGridPanel({
								ds : user_store,
								cm : user_cm,
								region : 'center',
								title : '用户列表',
								fitToFrame : true,
								border : false
							})

					var userwin = new Ext.Window({
								closable : true,
								width : 300,
								height : 400,
								plain : true,
								layout : 'border',
								items : [usergrid]
							});
					Ext.Ajax.request({
						url : 'system/findUserByRP.action',
						method : 'post',
						waitMsg : '正在查询数据...',
						params : {
							roleId : roleGrid.getStore().getAt(rowIndex).data.roleId,
							userlike : ''
						},
						success : function(result, request) {
							var gridData = eval('(' + result.responseText + ')');
							user_store.loadData(gridData);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '失败！');
						}
					})
					userwin.show();
				}
			}), new Ext.menu.Item({
				text : '查看角色权限',
				iconCls : 'filep',
				handler : function() {
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
							Ext.Msg.alert('失败，请联系管理员！');
						}
					})
				}
			})]
		});
		e.preventDefault();
		menu.showAt(e.getXY());
	});
	function changeIt(val) {
		return (val == "Y") ? "是" : "否";
	}
	var win = new Ext.Window({
				el : 'form-div',
				width : 400,
				height : 300,
				modal : true,
				closeAction : 'hide',
				items : [gridForm]
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true,
				items : [roleGrid]
			});
	setTimeout(function() {
				Ext.get('loading').remove();
				Ext.get('loading-mask').fadeOut({
							remove : true
						});
			}, 250);
});