Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var method;
	var hrSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var impInfo = Ext.data.Record.create([{
		name : 'empCode'
	}, {
		name : 'chsName'
	}])
	var hrStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findUsersFromHrByName.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, impInfo)
	});
	hrStore.load({
		params : {
			start : 0,
			limit : 18,
			hrlike : ''
		}
	});
	var hrCm = new Ext.grid.ColumnModel([hrSm, {
		header : "员工工号",
		width : 75,
		sortable : true,
		dataIndex : 'empCode'
	},

	{
		header : "员工名称",
		width : 75,
		sortable : true,
		dataIndex : 'chsName'
	}]);
	var hrtbar = new Ext.Toolbar({
		items : ['工号/姓名', {
			name : 'hruserlike',
			xtype : 'textfield'
		}, {
			id : 'hrquery',
			text : "查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findUsersFromHrByName.action',
					method : 'post',
					params : {
						start : 0,
						limit : 18,
						hrlike : Ext.get("hruserlike").dom.value
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						hrStore.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert("失败，请联系管理员！");
					}
				})
			}
		}]
	})
	var hrbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : hrStore,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})

	var hrGrid = new Ext.grid.EditorGridPanel({
		region : 'center',
		ds : hrStore,
		cm : hrCm,
		sm : hrSm,
		bbar : hrbbar,
		tbar : hrtbar,
		title : '人员列表',
		fitToFrame : true,
		border : false
	});
	var styleComboBox = new Ext.form.ComboBox({
		fieldLabel : '显示风格',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['xtheme-black', '典雅黑'], ['xtheme-darkgray', '深灰'],
					['xtheme-galdaka', '紫灰'], ['xtheme-gray', '灰'],
					['xtheme-purple', '紫色'], ['xtheme-slate', '蓝色']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择显示风格',
		emptyText : '选择显示风格',
		hiddenName : 'user.style',
		value : 'xtheme-gray',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'user.style',
		anchor : '50%'
	});
	var form = new Ext.form.FormPanel({
		id : 'user-form',
		el : 'form',
		defaultType : 'textfield',
		labelAlign : 'right',
		title : '增加/修改用户',
		labelWidth : 120,
		frame : true,
		layout : 'form',
		autoWidth : true,
		autoHeight : true,
		waitMsgTarget : true,
		items : [{
			name : "user.workerId",
			xtype : 'hidden'
		}, {
			id : 'workerCode',
			xtype : 'trigger',
			allowBlank : false,
			readOnly : true,
			fieldLabel : '工号<font color="red">*</font>',
			onTriggerClick : function(e) {
				if (method == 'add') {
					var args = {
							selectModel : 'single', 
							rootNode : {
								id : '-1',
								text : '合肥电厂'
							}
						} 
					var rvo = window.showModalDialog('../../../comm/jsp/hr/workerByDept/workerByDept2.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
					if(typeof(rvo) !="undefined")
					{
//						alert(rvo.newEmpCode)
						// 显示新工号
//						document.getElementById("workerCode").value = rvo.workerCode;
						document.getElementById("workerCode").value = rvo.newEmpCode;
						document.getElementById("user.workerCode").value = rvo.workerCode;
						document.getElementById("workerName").value = rvo.workerName;
					}
					
//					var emp = window
//							.showModalDialog(
//									"../../../comm/jsp/hr/findEmpByDept.jsp?select=many",
//									"",
//									"dialogHeight:600px;dialogWidth:800px;status:no;scroll:yes;help:no");
//					if (emp != null) {
//						document.getElementById("workerCode").value = emp.codes;
//						document.getElementById("user.workerCode").value = emp.codes;
//						document.getElementById("workerName").value = emp.names;
//					}
				} else {
					Ext.Msg.alert("提示", "工号不允许修改！");
				}
			}
		}, {
			xtype : 'hidden',
			name : "user.workerCode"
		}, {
			fieldLabel : '姓名<font color="red">*</font>',
			name : "user.workerName",
			id : 'workerName',
			readOnly : true,
			allowBlank : false
		}, {
			fieldLabel : '邮箱',
			name : "user.email"
		}, styleComboBox, {
			xtype : 'hidden',
			name : 'roleIds'
		}, {
			id : 'roles',
			xtype : 'trigger',
			fieldLabel : '角色名称',
			name : "roleNames",
			width : 120,
			readOnly : true,
			onTriggerClick : function(e) {
				if (method == 'add') {
					rolewin.show();
					query();
				} else {
					Ext.Msg.alert("提示", "修改时不允许修改角色！");
				}
			}
		}],
		buttons : [{
			text : '保存',
			handler : function() {
				if (document.getElementById("user.email").value.trim(" ") != "") {
					var r = /^[a-zA-Z0-9_\.]+@[a-zA-Z0-9-]+[\.a-zA-Z]+$/;
					if (r.test(document.getElementById("user.email").value) == false) {
						Ext.Msg.alert('提示信息', "请输入合法的Email");
						return false;
					}
				}
				if (Ext.get("roleNames").dom.value == null
						&& Ext.get("roleNames").dom.value.length < 1)
					Ext.get("roleIds").dom.value = '';
				if (form.getForm().isValid()) {
					var act = (Ext.get('user.workerId').dom.value == '')
							? 'system/addUser.action'
							: 'system/updateUser.action';
					form.getForm().submit({
						url : act,
						params : {
							newEmpCodes : document.getElementById("workerCode").value
						},
						method : 'post',
						waitMsg : '正在保存数据...',
						success : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							if (act == 'system/addUser.action') {
								ds.load({
									params : {
										start : 0,
										limit : 18,
										userlike : Ext.get("userlike").dom.value
									}
								});
							} else {
								ds.load({
									params : {
										start : 0,
										limit : 18,
										userlike : Ext.get("userlike").dom.value
									}
								});
							}
							win.hide();
							grid.getView().refresh();
						},
						failure : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert('错误提示', o.errMsg);
						}
					});
					win.hide();
				}
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
				// form.getForm().reset();
			}
		}]
	});
	form.render();
	var hrWin = new Ext.Window({
		closeAction : 'hide',
		width : 300,
		height : 400,
		plain : true,
		layout : 'border',
		closeAction : 'hide',
		modal : true,
		items : [hrGrid]
	})
	var User = Ext.data.Record.create([{
		name : 'workerId'
	}, {
		name : 'workerCode'
	}, {
		name : 'ifUse'
	}, {
		name : 'style'

	}, {
		name : 'workerName'
	}, {
		name : 'email'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
			// ,
			// type: 'date'
			}, {
				name : 'enterpriseCode'
			}, {
				name : 'loginCode'
			}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findUsersByProperty.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, User)
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				Ext.getCmp("user-form").getForm().loadRecord(rec);
			}
		}
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			userlike : ''
		}
	});

	var cm = new Ext.grid.ColumnModel([sm, {
		header : '编号',
		dataIndex : 'workerId',
		align : 'left',
		width : 50
	}, {
		header : '老工号',
		dataIndex : 'workerCode',
		align : 'left',
		hidden : true,
		width : 150
	}, {
		header : '工号',
		dataIndex : 'loginCode',
		align : 'left',
		width : 150
	}, {
		header : '姓名',
		dataIndex : 'workerName',
		// style : 'display="none"',
		align : 'left',
		width : 150
	}, {
		header : '邮箱',
		dataIndex : 'email',
		align : 'left',
		width : 150
	}, {
		header : '修改人',
		dataIndex : 'modifyBy',
		align : 'left'

	}, {
		header : '修改时间',
		dataIndex : 'modifyDate',
		renderer : function formatDate(value) {
			return value ? value.substr(0, 10) : '';
		},
		align : 'left'
	}]);

	cm.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	var tbar = new Ext.Toolbar({
		items : ['工号/姓名', {
			name : 'userlike',
			xtype : 'textfield'
		}, {
			id : 'query',
			iconCls : 'query',
			text : "模糊查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findUsersByProperty.action',
					method : 'post',
					params : {
						start : 0,
						limit : 18,
						userlike : Ext.get("userlike").dom.value
					},
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						ds.loadData(gridData);
					},
					failure : function(result, request) {

					}
				})
			}
		}, '-', {
			id : 'btnAdd',
			iconCls : 'add',
			text : "增加",
			handler : function() {
				form.getForm().reset();
				method = 'add';
				form.setTitle("增加用户");
				win.show(Ext.get('btnAdd'));
				Ext.get("roleNames").isVisible(true);
				Ext.get("user.workerCode").dom.readOnly = false;
			}
		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : function() {
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var userIds = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							for (i = 0; i < selectedRows.length; i++) {
								userIds += selectedRows[i].data.workerId + ",";
							}
							if (userIds.length > 0) {
								userIds = userIds.substring(0, userIds.length
										- 1);
							}
							Ext.Ajax.request({
								url : 'system/deleteUser.action',
								params : {
									userIds : userIds
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									for (i = 0; i < selectedRows.length; i++) {
										ds.remove(selectedRows[i]);
									}
									ds.commitChanges();
									grid.getView().refresh();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的用户!');
				}
			}
		}, '-', {
			id : 'btnUpdate',
			iconCls : 'update',
			text : "修改",
			handler : function() {
				method = "update";
				form.setTitle("修改用户信息");
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length < 1) {
					Ext.Msg.alert('提示信息', "请选择一行!");
					return;
				}
				var lastSelected = selectedRows[selectedRows.length - 1];

				Ext.get("user.workerId").dom.value = lastSelected
						.get("workerId");
				Ext.get("user.workerCode").dom.value = lastSelected
						.get("workerCode");
			// modified by liuyi 20100406  显示新工号
//				Ext.get("workerCode").dom.value = lastSelected
//						.get("workerCode");
				Ext.get("workerCode").dom.value = lastSelected
						.get("loginCode");
				Ext.get("user.workerName").dom.value = lastSelected
						.get("workerName") == null ? '' : lastSelected
						.get("workerName");
				styleComboBox.setValue(lastSelected.data.style);
				Ext.get("user.email").dom.value = lastSelected.get("email") == null
						? ''
						: lastSelected.get("email");
				win.show(Ext.get('btnUpdate'));
				Ext.get("user.workerCode").dom.readOnly = true;
			}
		}, '-', {
			id : 'btnReflesh',
			iconCls : 'reflesh',
			text : "刷新",
			handler : function() {
				window.location = window.location;
			}
		}, '-', '<font color="red">右击表格中的用户可以查询用户角色与权限!</font>']
	})

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		// el : 'siteTeam',
		ds : ds,
		cm : cm,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		// title : '用户列表',
		autoWidth : true,
		fitToFrame : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		}),// 设置单行选中模式, 否则将无法删除数据
		viewConfig : {
			forceFit : false
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			userlike : Ext.get("userlike").dom.value
		});
	});
	grid.enableColumnHide = false;
	// grid.render();
	grid.on("dblclick", function() {
		Ext.get("btnUpdate").dom.click();

	});

	grid.addListener('rowcontextmenu', function(_grid, rowIndex, e) {
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '查看用户角色',
				iconCls : 'personp',
				handler : function() {
					var Role = Ext.data.Record.create([{
						name : 'roelId'
					}, {
						name : 'roleName'
					}])
					var role_store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : 'system/findRoleBywId.action'
						}),
						reader : new Ext.data.JsonReader({
							root : 'rolesList'
						}, Role)
					});

					var role_cm = new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(), {
								header : '角色名称',
								dataIndex : 'roleName',
								align : 'left',
								width : 200
							}]);
					var rolegrid = new Ext.grid.EditorGridPanel({
						ds : role_store,
						cm : role_cm,
						region : 'center',
						title : '所属角色列表',
						fitToFrame : true,
						border : false
					})

					var rolewin = new Ext.Window({
						closeAction : 'hide',
						width : 300,
						height : 400,
						plain : true,
						modal : true,
						layout : 'border',
						items : [rolegrid]
					});
					rolewin.show();
					role_store.load({
						params : {
							workerId : grid.getStore().getAt(rowIndex).data.workerId
						}
					})
				}
			}), new Ext.menu.Item({
				text : '查看用户权限',
				iconCls : 'filep',
				handler : function() {
					var File = Ext.data.Record.create([{
						name : 'fileId'
					}, {
						name : 'fileName'
					}])
					var file_store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : 'system/findFileBywId.action'
						}),
						reader : new Ext.data.JsonReader({
							root : 'list'
						}, File)
					});
					var file_cm = new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(), {
								header : '权限名称',
								dataIndex : 'fileName',
								align : 'left',
								width : 200
							}]);
					var filegrid = new Ext.grid.EditorGridPanel({
						ds : file_store,
						cm : file_cm,
						region : 'center',
						title : '所有权限列表',
						fitToFrame : true,
						border : false
					})
					var filewin = new Ext.Window({
						closeAction : 'hide',
						width : 300,
						height : 400,
						plain : true,
						layout : 'border',
						items : [filegrid]
					});
					filewin.show();
					file_store.load({
						params : {
							workerId : grid.getStore().getAt(rowIndex).data.workerId
						}
					})
				}
			})]
		});
		e.preventDefault();
		menu.showAt(e.getXY());
	});
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 250,
		closeAction : 'hide',
		modal : true,
		items : [form],
		buttons : []
	});
	var Role = Ext.data.Record.create([{
		name : 'roleId'
	}, {
		name : 'roleName'
	}])
	var role_store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findAllRole.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.total',
			root : 'data.rolesList'
		}, Role)
	});
	role_store.load();
	var role_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var role_cm = new Ext.grid.ColumnModel([role_sm, {
		header : '角色名称',
		dataIndex : 'roleName',
		align : 'left',
		width : 200
	}]);
	var roletbar = new Ext.Toolbar({
		items : ['角色编号/名称', {
			name : 'like',
			xtype : 'textfield'
		}, {
			id : 'query',
			text : "模糊查询",
			// handler : function() {
			// Ext.Ajax.request({
			// url : 'system/findRole.action',
			// method : 'post',
			// waitMsg : '正在查询数据...',
			// params : {
			// start : 0,
			// limit : 30,
			// likename : Ext.get("like").dom.value
			// },
			// success : function(result, request) {
			// var gridData = eval('(' + result.responseText +
			// ')');
			// role_store.loadData(gridData);
			// },
			// failure : function(result, request) {
			// alert();
			// }
			// })
			// }
			handler : query
		}]
	})
	function query() {
		var likeName = "%";
		try {
			var likeName = Ext.get("like").dom.value;
		} catch (err) {
		}
		Ext.Ajax.request({
			url : 'system/findRole.action',
			method : 'post',
			waitMsg : '正在查询数据...',
			params : {
				start : 0,
				limit : 18,
				likename : likeName
			},
			success : function(result, request) {
				var gridData = eval('(' + result.responseText + ')');
				role_store.loadData(gridData);
			}
		})
	}
	var rolegrid = new Ext.grid.EditorGridPanel({
		ds : role_store,
		cm : role_cm,
		sm : role_sm,
		tbar : roletbar,
		region : 'center',
		title : '所属角色列表',
		fitToFrame : true,
		border : false
	})
	var rolewin = new Ext.Window({
		closeAction : 'hide',
		width : 300,
		height : 400,
		// border : false,
		plain : true,
		layout : 'border',
		items : [rolegrid],
		buttons : [{
			text : '确定',
			handler : function() {
				var selectedRows = rolegrid.getSelectionModel().getSelections();
				var roleIds = "";
				var roleNames = "";
				if (selectedRows.length < 1)
					Ext.Msg.alert('提示信息', "请选择一行！");
				else {
					for (i = 0; i < selectedRows.length; i++) {
						roleIds = roleIds + selectedRows[i].data.roleId + ",";
						roleNames = roleNames + selectedRows[i].data.roleName
								+ ",";
					}
				}
				if (roleIds.length > 0) {
					roleIds = roleIds.substring(0, roleIds.length - 1);
					roleNames = roleNames.substring(0, roleNames.length - 1);
				}
				Ext.get("roleIds").dom.value = roleIds;
				Ext.get("roleNames").dom.value = roleNames;
				rolewin.hide();
			}
		}, {
			text : '取消',
			handler : function() {
				rolewin.hide();
			}
		}]
	});
	/* 创建登录是需要使用的表单 */
	var store = new Ext.data.SimpleStore({
		fields : ['id', 'ifUseText'],
		data : [['Y', '是'], ['N', '否']]
	});

	/* 在用户校验失败的时候判断是否是服务器连接失败 */
	var onFailure = function(form, action) {
		if (action.failureType == "connect") {
			Ext.Msg.alert('服务器异常', '服务器异常，请与管理员联系！');
		}
	}
	/* 登录是需要使用的表单放在这个弹出窗口中 */

	// 工号
	var person_store = new Ext.data.SimpleStore({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findUsersByProperty.action'
		}),
		fields : [{
			name : 'workerId'
		}, {
			name : 'workerCode'
		}, {
			name : 'ifUse'
		}, {
			name : 'workerName'
		}, {
			name : 'email'
		}, {
			name : 'modifyBy'
		}, {
			name : 'modifyDate'
		}, {
			name : 'enterpriseCode'
		}, {
			name : 'loginCode'
		}]
	});

	var person_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var person_cm = new Ext.grid.ColumnModel([person_sm,
			new Ext.grid.RowNumberer(), {
				header : '编号',
				dataIndex : 'workerId',
				align : 'left',
				width : 50
			}, {
				header : '工号',
				dataIndex : 'workerCode',
				align : 'left',
				width : 150
			}, {
				header : '姓名',
				dataIndex : 'workerName',
				style : 'display="none"',
				align : 'left',
				width : 150
			}, {
				header : '邮箱',
				dataIndex : 'email',
				align : 'left',
				width : 300
			}]);
	person_cm.defaultSortable = true;
	/* 创建表格 */
	var gridWin = new Ext.grid.EditorGridPanel({
		// el : 'gridDiv',
		store : person_store,
		cm : person_cm,
		sm : person_sm,
		// tbar : person_tbar,
		width : 650,
		height : 380,
		fitToFrame : true,
		border : false
	});

	gridWin.enableColumnHide = false;

	gridWin.on("dblclick", function() {
		var recordtoedit = gridWin.getSelectionModel().getSelected();
		Ext.get("user.workerCode").dom.value = recordtoedit.data.workerCode;
		Ext.get("user.workerName").dom.value = recordtoedit.data.name;
		gridShowWin.hide();

	});
	var gridShowWin = new Ext.Window({
		title : '请选择员工',
		el : 'gridShowWin',
		width : 680,
		autoScroll : true,
		height : 420,
		items : [gridWin],
		closeAction : 'hide'
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});
});