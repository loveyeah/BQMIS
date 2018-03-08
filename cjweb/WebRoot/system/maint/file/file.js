// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var ddjson = '';
Ext.onReady(function() {
	var currentType = 'add';
	var currentNode;
	var isDispComboBox = new Ext.form.ComboBox({
		fieldLabel : '是否显示',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['Y', '显示'], ['N', '不显示']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择是否显示',
		emptyText : '选择是否显示',
		hiddenName : 'fls.isDisp',
		value : 'Y',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'fls.isDisp',
		anchor : '50%'
	});
	// var fileTypeComboBox = new Ext.form.ComboBox({
	// fieldLabel : '类型',
	// store : new Ext.data.SimpleStore({
	// fields : ["retrunValue", "displayText"],
	// data : [['1', '管理模块'], ['0', '业务模块'], ['2', '共享模块']]
	// }),
	// valueField : "retrunValue",
	// displayField : "displayText",
	// mode : 'local',
	// forceSelection : true,
	// hiddenName : 'fls.fileType',
	// value : '0',
	// editable : false,
	// triggerAction : 'all',
	// selectOnFocus : true,
	// name : 'fls.fileType',
	// anchor : '50%'
	// });
	var isFileComboBox = new Ext.form.ComboBox({
		fieldLabel : '是否网页<font color="red">*</font>',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['Y', '网页'], ['N', '目录']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'fls.isFile',
		value : 'Y',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'fls.isFile',
		anchor : '50%'
	});
	function treeClick(node, e) {
		e.stopEvent();
		if (node.id == "1") {
			currentType = 'add';
			webpageform.setTitle('在目录<<漕泾热电厂>>下增加');
			webpageform.getForm().reset();
			return false;
		}
		Ext.Ajax.request({
			url : 'system/findFile.action',
			method : 'post',
			params : {
				node : node.id
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.get('fls.fileId').dom.value = o.fileId;
				Ext.get('fls.fileName').dom.value = o.fileName;
				Ext.get("fls.line").dom.value = o.line == null ? '' : o.line;
				Ext.get("fls.memo").dom.value = o.memo == null ? '' : o.memo;
				Ext.get("fls.fileAddr").dom.value = o.fileAddr == null
						? ''
						: o.fileAddr;
				Ext.get("fls.modifyDate").dom.value = o.modifyDate == null
						? ''
						: o.modifyDate.substring(0, 10);
				Ext.get("fls.modifyBy").dom.value = o.modifyBy == null
						? ''
						: o.modifyBy;
				isDispComboBox.setValue(o.isDisp);
				// fileTypeComboBox.setValue(o.fileType);
				isFileComboBox.setValue(o.isFile);
				currentType = 'update';
				currentNode = node;
				webpageform.setTitle("修改<<" + o.fileName + ">>基本信息");
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};
	var webpageform = new Ext.FormPanel({
		id : 'webpage-form',
		title : '在目录<<漕泾热电厂>>下增加',
		frame : true,
		labelAlign : 'left',
		width : 600,
		height : 500,
		bodyStyle : 'padding:30px',
		items : [{
			xtype : 'fieldset',
			id : 'formSet',
			labelAlign : 'right',
			labelWidth : 60,
			title : '模块详细信息',
			defaults : {
				width : 300
			},
			defaultType : 'textfield',
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},
			items : [{
				name : 'fls.parentFileId',
				value : '1',
				xtype : 'hidden'
			}, {
				fieldLabel : '模块编号',
				name : 'fls.fileId',
				xtype : 'hidden'
			}, {
				fieldLabel : '模块名称<font color="red">*</font>',
				allowBlank : false,
				name : 'fls.fileName'
			}, isFileComboBox,
					// fileTypeComboBox,
					{
						id : 'fls.fileAddr',
						width : 300,
						fieldLabel : '链接地址<font color="red">*</font>',
						allowBlank : false,
						name : 'fls.fileAddr'
					}, {
						width : 300,
						xtype : 'textarea',
						fieldLabel : '备注',
						height : 120,
						name : 'fls.memo'
					}, isDispComboBox, {
						xtype : 'numberfield',
						fieldLabel : '排序',
						name : 'fls.line'

					}, {
						id : 'fls.modifyBy',
						fieldLabel : '修改人',
						name : 'fls.modifyBy',
						readOnly : true
					}, {
						id : 'fls.modifyDate',
						fieldLabel : '修改时间',
						name : 'fls.modifyDate',
						readOnly : true
					}],
			buttons : [{
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(
							response) {
						if (response == 'yes') {
							webpageform.getForm().submit({
								method : 'post',
								url : 'system/deleteFile.action',
								waitMsg : '正在删除数据,请等待...',
								success : function(form, action) {
									ddjson = '';
									var removeNode = tree
											.getNodeById(currentNode.id);
									// tree.getNodeById(currentNode.parentNode.id)
									// .reload();
									// removeNode.parentNode
									// .removeChild(removeNode);
									webpageform.getForm().reset();
									var nd = tree
											.getNodeById(currentNode.parentNode.id);
									var path = nd.getPath();
									tree.getRootNode().reload();
									tree.expandPath(path, false);
									Ext.Msg.alert('提示信息', '删除模块成功！');
								},
								failure : function(form, action) {
									ddjson = '';
									var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert('错误', o.Errmsg);
								}
							});
						}
					});
				}
			}, {
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (webpageform.getForm().isValid()) {
						// 增加
						if (Ext.get("fls.fileName").dom.value.trim(" ") == "") {
							Ext.Msg.alert('注意', '模块名称不能全是空格!');
							return false;
						}
						if (Ext.get("fls.fileAddr").dom.value.trim(" ") == "") {
							Ext.Msg.alert('注意', '地址不能全是空格!');
							return false;
						}
						Ext.get("fls.fileName").dom.value = Ext
								.get("fls.fileName").dom.value.trim(" ");
						Ext.get("fls.fileAddr").dom.value = Ext
								.get("fls.fileAddr").dom.value.trim(" ");
						if (currentType == "add") {
							if (currentNode != null) {
								webpageform.getForm().submit({
									method : 'post',
									url : 'system/addFile.action',
									waitMsg : '正在保存数据,请等待...',
									success : function(form, action) {
										var id = action.result.id;
										var url = action.result.url;
										var newNode = new Ext.tree.AsyncTreeNode({
											id : id,
											leaf : Ext.get("fls.isFile").dom.value == "Y"
													? true
													: false,
											text : Ext.get("fls.fileName").dom.value
												// url : url
										});
										var addnode = tree
												.getNodeById(currentNode.id);
										if (!addnode.isLeaf()) {
											var fc = addnode.firstChild;
											if (fc == null)
												addnode.appendChild(newNode);
											else
												addnode.insertBefore(newNode,
														fc);
										} else {
											var rid = addnode.id;
											var rtext = addnode.text;
											// var rurl =
											// node.attributes['url'];
											var rnode = new Ext.tree.AsyncTreeNode({
												id : rid,
												text : rtext,
												leaf : false
													// url : rurl
											});
											var rsibling = addnode.nextSibling;
											var pnode = addnode.parentNode;
											addnode.remove();
											if (rsibling != null)
												pnode.insertBefore(rnode,
														rsibling);
											else
												pnode.appendChild(rnode);
											rnode.appendChild(newNode);
											rnode.expand();
										}
										newNode.select();
										// webpageform.getForm().load({
										// url :
										// 'system/findFile.action?node='+nodeId
										// // url : url
										// });
										Ext.Msg.alert('提示信息', '添加模块成功！');

									},
									failure : function() {
										Ext.Msg.alert('保存失败，请联系管理员！');
									}
								});
							} else {
								Ext.Msg.alert('提示信息', "请先选择节点！");
							}
						}

						// 修改
						else {
							if (currentNode == null) {
								Ext.Msg.alert('提示信息', "请先选择节点！");

							} else {
								webpageform.getForm().submit({
									method : 'post',
									url : 'system/updateFile.action',
									waitMsg : '正在保存数据,请等待...',
									success : function(form, action) {
										Ext.Msg.alert('提示信息', '修改模块成功！');
										var node = tree
												.getNodeById(currentNode.id);
										node
												.setText(Ext
														.get("fls.fileName").dom.value);
										 tree.getNodeById(node.parentNode.id)
										 .reload();
									},
									failure : function(form, action) {
										Ext.Msg.alert('保存失败，请联系管理员！');
									}
								});
							}

						}
					}
				}
			}]
		}]
	});

	// layout

	// 设置树形面板
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
		el : 'file-tree-div',
		rootVisible : true,
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : true,
		border : false,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'system/findFiles.action'
		})
	});

	var root = new Tree.AsyncTreeNode({
		text : '漕泾热电厂',
		draggable : false,
		id : '1',
		leaf : false
	});
	tree.setRootNode(root);
	tree.render();
	root.expand();
	root.select();
	currentNode = root;
	tree.on('click', treeClick);
	tree.on("beforenodedrop", function(e) {
		var sid = e.dropNode.id;
		var tid = e.target.leaf ? e.target.parentNode.id : e.target.id;
		if (ddjson != '') {
			ddjson += ',';
		}
		ddjson += '{"sid":' + sid + ',"tid":' + tid + '}';
	});
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			layout : 'fit',
			el : 'west-div',
			split : true,
			width : 200,
			autoScroll : true,
			collapsible : true,
			border : false,
			items : [tree]
		}, {
			region : 'center',
			id : 'center-panel',
			layout : 'fit',
			items : [webpageform]
		}]
	});
	tree.on('contextmenu', function(node, e) {
		e.stopEvent();
		if (node.id == "1") {
			return false;
		}
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '添加',
				iconCls : 'add',
				style : 'display:' + (node.isLeaf() ? 'none' : ''),
				handler : function() {
					webpageform.getForm().reset();
					currentNode = node;
					currentType = "add";
					Ext.get("fls.parentFileId").dom.value = node.id;
					Ext.get("fls.fileName").dom.focus();
					webpageform.setTitle("在目录<<" + node.text + ">>下增加");
				}
			}), new Ext.menu.Item({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					currentNode = node;
					Ext.Msg.confirm("提示", '确定要删除吗！', function(b) {
						if (b == 'yes') {
							Ext.Ajax.request({
								method : 'post',
								url : 'system/deleteFile.action',
								waitMsg : '正在删除数据...',
								params : {
									'fls.fileId' : currentNode.id
								},
								success : function(result, request) {
									var responseArray = Ext.util.JSON
											.decode(result.responseText);
									if (responseArray.success == true) {
										ddjson = '';
										currentNode.parentNode
												.removeChild(currentNode);
										webpageform.getForm().reset();
									} else {
										var o = eval("(" + result.responseText
												+ ")");
										Ext.Msg.alert('错误', o.Errmsg);
									}
								}
							})
						}
					});
				}
			}), new Ext.menu.Item({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					treeClick(node, e);
				}
			}), new Ext.menu.Item({
				text : '查看该权限的角色',
				iconCls : 'rolep',
				handler : function() {
					var Role = Ext.data.Record.create([{
						name : 'roelId'
					}, {
						name : 'roleName'
					}])
					var role_store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : 'system/findRolesByfileId.action'
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
						closable : true,
						width : 300,
						height : 400,
						// border : false,
						plain : true,
						layout : 'border',
						items : [rolegrid]
					});
					rolewin.show();
					role_store.load({
						params : {
							fileId : node.id
						}
					})
				}
			}), new Ext.menu.Item({
				text : '查看该权限的用户',
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
							url : 'system/findRolesByfileId.action'
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
						// border : false,
						plain : true,
						layout : 'border',
						items : [usergrid]
					});

					userwin.show();
					Ext.Ajax.request({
						url : 'system/findUsersByfileId.action',
						method : 'post',
						waitMsg : '正在查询数据...',
						params : {
							fileId : node.id
						},
						success : function(result, request) {
							var gridData = eval('(' + result.responseText + ')');
							user_store.loadData(gridData);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '失败！');
						}
					})
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);

	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnSave',
			text : '保存目录结构',
			iconCls : 'save',
			handler : function() {
				if (ddjson == '') {
					return false;
				} else {
					if (confirm('确定要保存树形结构吗?')) {
						ddjson = '[' + ddjson + ']';
						Ext.Ajax.request({
							url : 'system/saveCatalog.action',
							waitMsg : '正在保存数据..',
							params : {
								catalogStr : ddjson
							},
							success : function() {
								ddjson = '';
								Ext.Msg.alert("成功", '保存成功！');
							},
							failure : function() {
								ddjson = '';
								Ext.Msg.alert("错误", '保存失败！');
							}

						});
					}
				}
			}
		}]
	})
	tbar.render('tree-button-div');
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});