Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = 'add';
	var currentNode = new Object();
	currentNode.id = "root";
	currentNode.text = "安装点树";
	//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "安装点树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		border : false,
		loader : new Ext.tree.TreeLoader({
			url : "equ/getInstallTreeList.action",
			baseParams : {
				id : 'root'
			}

		})
	});

	// ---------------------------------------
	//-----------树的事件----------------------
	//树的单击事件
	root.expand();//展开根节点
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'equ/getInstallTreeList.action?id=' + node.id;
	}, this);

	//为树加右键菜单
	mytree.on('contextmenu', function(node, e) {
		e.stopEvent();
		if (node.id == "root") {
			return false;
		}
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '新增',
				iconCls : 'add',
				style : 'display:' + (node.id.length > 8 ? 'none' : ''),
				handler : function() {
					myaddpanel.setTitle("在目录【" + node.text + "】下增加");
					currentNode = node;
					currentType = 'add';
					initForm();
					Ext.get("installId").dom.value = "自动生成";
					Ext.get("fatherCode").dom.value = node.id;
					// grid.setVisible(false);
					grid.getTopToolbar().items.get('mygridadd')
							.setVisible(false);
					grid.getTopToolbar().items.get('mygriddelete')
							.setVisible(false);
					store.baseParams = {
						code : 'root'
					};
					store.load({
						params : {
							start : 0,
							limit : 10
						}
					});

					if (Ext.get("fatherCode").dom.value == "root"
							|| Ext.get("fatherCode").dom.value.length == 2) {
						Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
					} else {
						Ext.get("equNum").dom.parentNode.parentNode.style.display = '';
					}

					if (Ext.get("fatherCode").dom.value.length == 2) {
						Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = '';
					} else {
						Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';
					}

					if (Ext.get("fatherCode").dom.value.length == 6) {
						Ext.get("installationCode").dom.parentNode.parentNode.parentNode.style.display = 'none';

						Ext.get("installationDesc").dom.value = node.text
								.substring(node.text.indexOf(' ') + 1,
										node.text.length);
					} else {
						Ext.get("installationCode").dom.parentNode.parentNode.parentNode.style.display = '';
					}

					if (Ext.get("fatherCode").dom.value.length >= 8) {
						Ext.getCmp("installationCode").setEditable(true);
						Ext.get("installationDesc").dom.value = node.text
								.substring(node.text.indexOf(' ') + 1,
										node.text.length);
					}

				}
			}), new Ext.menu.Item({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					currentNode = node;
					Ext.Msg.confirm("删除", "是否确定删除" + node.text + "吗？",
							function(buttonobj) {
								if (buttonobj == "yes") {

									Ext.Ajax.request({
										url : 'equ/deleteInstall.action',
										params : {
											id : node.id,
											method : 'code'
										},
										method : 'post',
										waitMsg : '正在加载数据...',
										success : function(result, request) {
											var json = eval('('
													+ result.responseText + ')');
											Ext.Msg.alert("注意", json.msg);
											if (json.msg == "删除成功！") {
												var rnode = currentNode.parentNode;
												rnode.removeChild(currentNode);
												currentNode.id = "root";
												currentNode.text = "安装点树";
												clickTree(currentNode);
												root.select();
											}

										}
									});

								}

							});

				}
			}), new Ext.menu.Item({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					clickTree(node);
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);
	//-----------

	//
	// //add for tree add

	// ------------
	function initForm() {

		Ext.get("installId").dom.value = "";
		Ext.get("fatherCode").dom.value = "";
		Ext.get("installationCode").dom.value = "";
		Ext.get("equNum").dom.value = "";
		//		Ext.get("classStructureId").dom.value = "";
		Ext.get("installationDesc").dom.value = "";
		Ext.get("changeBy").dom.value = "";
		Ext.get("changeDate").dom.value = "";

	}
	// ---------树的click事件
	function clickTree(node) {

		Ext.get("installationCode").dom.parentNode.parentNode.parentNode.style.display = '';
		if (node.id == "root") {
			//根节点
			currentType = 'add';
			currentNode = node;
			initForm();
			//grid.setVisible(false);
			grid.getTopToolbar().items.get('mygridadd').setVisible(false);
			grid.getTopToolbar().items.get('mygriddelete').setVisible(false);
			store.baseParams = {
				code : 'root'
			};
			store.load({
				params : {
					start : 0,
					limit : 10
				}
			});
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			Ext.get("fatherCode").dom.value = "root";
			Ext.get("installId").dom.value = "自动生成";
			Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
			Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';

		} else {
			currentNode = node;
			currentType = "update";
			initForm();
			//grid.setVisible(true);
			grid.getTopToolbar().items.get('mygridadd').setVisible(true);
			grid.getTopToolbar().items.get('mygriddelete').setVisible(true);

			//显示树对应的位置信息
			store.baseParams = {
				code : node.id
			};
			store.load({
				params : {
					start : 0,
					limit : 10
				}
			});
			//----
			myaddpanel.setTitle("修改【" + node.text + "】基本信息");
			Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
			Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';
			//Ext.get("mydelete").dom.parentNode.style.display = '';

			Ext.Ajax.request({
				url : 'equ/getInstallByCode.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');

					var obj = json.data;
					Ext.get("installId").dom.value = obj.id;
					Ext.get("fatherCode").dom.value = obj.fatherCode;
					Ext.get("installationCode").dom.value = obj.installationCode;
					Ext.get("installationDesc").dom.value = obj.installationDesc;
					//					if (obj.classStructureId != null) {
					//						Ext.get("classStructureId").dom.value = obj.classStructureId;
					//					}
					if (obj.changeBy != null) {
						Ext.get("changeBy").dom.value = json.workName;
					}
					if (obj.changeDate != null) {
						Ext.get("changeDate").dom.value = obj.changeDate
								.substring(0, 10);

					}

				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

		};
	};

	// -----------
	//ID
	var installId = {
		id : "installId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		readOnly : true,
		allowBlank : false,
		name : 'installId'
	}

	//位置码
	var installationCode = {
		fieldLabel : '安装点码',
		name : 'equInstall.installationCode',
		xtype : 'combo',
		id : 'installationCode',
		//	store : ,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
		//	allowBlank : false,
		//	hiddenName : 'equLoc.locationCode',
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			if (currentType == "add") {
				var url = "";
				if (Ext.get("fatherCode").dom.value == "root") {
					url = "blockSelect.jsp";
				}
				if (Ext.get("fatherCode").dom.value.length == 2) {
					url = "sysSelect.jsp?code=all";
				}
				if (url != "") {
					var install = window.showModalDialog(url, '',
							'dialogWidth=500px;dialogHeight=400px;status=no');

					if (typeof(install) != "undefined") {

						if (currentNode != null) {
							if (currentNode.text != ""
									&& currentNode.text != "安装点树") {
								var myname = currentNode.text.substring(
										currentNode.text.indexOf(' ') + 1,
										currentNode.text.length);

								Ext.get("installationDesc").dom.value = myname
										+ install.name;
							} else {

								Ext.get("installationDesc").dom.value = install.name;
							}
							if (Ext.get("fatherCode").dom.value == "root") {
								Ext.get("installationCode").dom.value = "+"
										+ install.code;
							} else {
								Ext.get("installationCode").dom.value = install.code;
							}
						}
					}
				}

			}
		}

	};
	// 父位置码
	var fatherCode = {
		id : "fatherCode",
		xtype : "textfield",
		fieldLabel : '父编码',
		allowBlank : false,
		readOnly : true,
		value : 'root',
		name : 'equInstall.fatherCode'
	}

	var StartSysCode = {
		id : "StartSysCode",
		xtype : "textfield",
		fieldLabel : '前置编码',
		value : '0',
		allowBlank : false,
		name : 'StartSysCode',
		maxLength : 1
	}

	var equNum = {
		id : "equNum",
		xtype : "textfield",
		fieldLabel : '编号',
		name : 'equNum',
		maxLength : 3
	}
	var installationDesc = {
		id : "installationDesc",
		xtype : "textfield",
		fieldLabel : '名称',
		name : 'equInstall.installationDesc'
	}

	var changeBy = {
		id : "changeBy",
		xtype : "textfield",
		fieldLabel : '更改人',
		name : 'equInstall.changeBy',
		readOnly : true
	}

	var changeDate = {
		id : 'changeDate',
		xtype : "datefield",
		fieldLabel : '更改时间',
		name : 'equInstall.changeDate',
		format : 'Y-m-d',

		readOnly : true
			// anchor:'30%'
	};

	var classStructureId = {
		id : "classStructureId",
		xtype : "hidden",
		fieldLabel : '技术规范分类编号',
		name : 'classStructureId'
	}

	//------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '在目录【安装点树】下增加',
		// width : 300,
		//	 height : 300,
		bodyStyle : 'padding:5px',

		items : [{
			xtype : 'fieldset',
			id : 'formSet',
			title : '基本信息',
			//			width : 400,
			labelAlign : 'right',
			labelWidth : 60,
			defaults : {
				width : 250
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
			items : [installId, fatherCode, StartSysCode, installationCode,
					equNum, installationDesc, changeBy, changeDate,
					classStructureId],
			buttons : [{
				text : '删除',
				id : 'mydelete',
				iconCls : 'delete',
				handler : function() {
					if (currentType == "add") {
						Ext.Msg.alert("注意", "请选择要删除的位置信息");
					}
					if (currentType == "update") {

						Ext.Msg.confirm("删除", "是否确定删除"
								+ Ext.get("installationDesc").dom.value + "吗？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										myaddpanel.form.submit({
											method : 'POST',
											url : 'equ/deleteInstall.action?method=id',
											success : function(form, action) {
												var o = eval("("
														+ action.response.responseText
														+ ")");
												Ext.Msg.alert("注意", o.msg);
												if (o.msg == "删除成功！") {
													var rnode = currentNode.parentNode;
													rnode
															.removeChild(currentNode);
													currentNode.id = "root";
													currentNode.text = "安装点树";
													clickTree(currentNode);
													root.select();
												}
											}
										})
									}

								});

					}

				}
			}, {
				text : '保存',
				id : 'mysave',
				iconCls : 'save',
				handler : function() {
					//----增加时检查输入
					var check = true;
					if (currentType == "add") {
						var r = /^[0-9]*[0-9][0-9]*$/;
						if (Ext.get("fatherCode").dom.value.length != 6) {
							if (Ext.get("installationCode").dom.value == "") {
								check = false;
								alert("请输入安装点码");
							} else {
								if (Ext.get("fatherCode").dom.value.length == 8) {
									if (Ext.get("installationCode").dom.value.length > 2) {
										check = false;
										alert("安装点码不能超过两位");
									} else {
										if (document.getElementById("equNum").value == "") {
											check = false;
											alert("编号不能为空");
										} else {
											if (r
													.test(document
															.getElementById("equNum").value) == false) {
												check = false;
												alert("编号请输入数字");
											} else {
												if (document
														.getElementById("equNum").value.length > 3) {
													check = false;
													alert("编号不能超过3位数字");
												}
											}
										}
									}

								}
							}

						} else {
							if (document.getElementById("equNum").value == "") {
								check = false;
								alert("编号请输入两位数字");
							} else {
								if (r
										.test(document.getElementById("equNum").value) == false) {
									check = false;
									alert("编号请输入两位数字");
								} else {
									if (document.getElementById("equNum").value.length > 2) {
										check = false;
										alert("编号请输入两位数字");
									}
								}
							}

						}

						if (Ext.get("fatherCode").dom.value.length = 2) {
							if (r
									.test(document
											.getElementById("StartSysCode").value) == false) {
								check = false;
								alert("前缀编号请输入数字");
							}
						}

					}

					//---------保存-------
					if (check) {
						myaddpanel.form.submit({
							method : 'POST',
							url : 'equ/saveInstallCode.action?method='
									+ currentType,
							success : function(form, action) {

								var o = eval("(" + action.response.responseText
										+ ")");

								Ext.Msg.alert("注意", o.msg);
								// ----------
								if (currentType == "add") {

									if (o.id != -1) {
										//---add
										var newNode = new Ext.tree.AsyncTreeNode({
											id : o.id,
											leaf : true,
											text : o.id
													+ " "
													+ Ext
															.get("installationDesc").dom.value
										});

										var pnode = mytree
												.getNodeById(currentNode.id);
										if (!pnode.isLeaf()) {
											var fc = pnode.firstChild;

											if (fc == null)
												pnode.appendChild(newNode);
											else
												pnode.insertBefore(newNode, fc);
											newNode.select();
										} else {
											var rnode = new Ext.tree.AsyncTreeNode({
												id : pnode.id,
												text : pnode.text,
												leaf : false
											});
											var rsibling = pnode.nextSibling;
											var ppnode = pnode.parentNode;
											pnode.remove();
											if (rsibling != null)
												ppnode.insertBefore(rnode,
														rsibling);
											else
												ppnode.appendChild(rnode);
											rnode.appendChild(newNode);
											rnode.expand();
											if (rnode.firstChild != null) {
												rnode.firstChild.select();
											}

										}
										//------
										currentNode = newNode;
										currentType = "update";
										clickTree(newNode);
									}
								} else {
									if (o.msg == "修改成功！") {
										var cnode = mytree
												.getNodeById(currentNode.id);
										cnode
												.setText(currentNode.id
														+ " "
														+ Ext
																.get("installationDesc").dom.value);
										mytree.getNodeById(cnode.parentNode.id)
												.reload();
									}
								}
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
						// -------------------------

					}
				}

			}]

		}]

	});

	//--------------grid----------------
	var MyRecord = Ext.data.Record.create([{
		name : 'equId'
	}, {
		name : 'equName'
	}, {
		name : 'attributeCode'
	}, {
		name : 'locationCode'
	}, {
		name : 'installationCode'
	}, {
		name : 'assetnum'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'equ/findListByInstallationCode.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
	//分页
	//	store.load({
	//			params : {
	//				start : 0,
	//				limit : 10				
	//			}
	//		});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [sm, {

			header : "ID",
			sortable : true,
			dataIndex : 'equId',
			hidden : true
		}, {
			header : "设备名称",
			width : 150,
			sortable : true,
			dataIndex : 'equName'
		}, {
			header : "设备功能码",
			width : 100,
			sortable : true,
			dataIndex : 'attributeCode'
		}, {
			header : "设备位置码",
			width : 75,
			sortable : true,
			dataIndex : 'locationCode'
		}, {
			header : "安装点码",
			width : 75,
			sortable : true,
			dataIndex : 'installationCode'
		}, {
			header : "物资码",
			width : 50,
			sortable : true,
			dataIndex : 'assetnum'
		}],
		sm : sm,
		tbar : ['对应设备   ', {
			text : "增加",
			id : 'mygridadd',
			iconCls : 'add',
			handler : addRecord
		}, {
			text : '删除',
			id : "mygriddelete",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	//----grid增加删除事件
	function addRecord() {
		var url = "../kksselect/selectAttribute.jsp?op=many";
		var equ = window.showModalDialog(url, '',
				'dialogWidth=600px;dialogHeight=400px;status=no');

		if (typeof(equ) != "undefined") {

			// addEquRLocation

			Ext.Ajax.request({
				url : 'equ/addEquRInstall.action',
				params : {
					codes : equ.code,
					installCode : currentNode.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
                    Ext.Msg.alert("注意", json.msg);
					//alert(json.msg);
					//刷新grid
					store.baseParams = {
						code : currentNode.id
					};
					store.load({
						params : {
							start : 0,
							limit : 10
						}
					});

				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

		}

	}

	function deleteRecord() {
		var mysm = grid.getSelectionModel();
		var selected = mysm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择设备！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.equId) {
					ids.push(member.equId);
				} else {

					store.remove(store.getAt(i));
				}
			}

			//deleteEquRLocation
			Ext.Msg.confirm("删除", "确定删除选择的记录吗？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'equ/deleteEquRInstall.action',
						params : {
							ids : ids.toString()

						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
                              Ext.Msg.alert("注意", json.msg);
							//alert(json.msg);
							//刷新grid
							store.baseParams = {
								code : currentNode.id
							};
							store.load({
								params : {
									start : 0,
									limit : 10
								}
							});

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
				}
			})
		}

	}

	// --------------endgrid-------------

	// ------- 布局add

	//------------

	var right = new Ext.Panel({
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'border',
		items : [{
			region : 'north',
			height : 290,
			items : [myaddpanel]
		}, {
			region : 'center',
			layout : 'fit',
			items : [grid]
		}]

	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			split : true,
			width : 250,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		}, right// 初始标签页
		]
	});

	// -------------------
	Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
	Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';
	// grid.setVisible(false);
	grid.getTopToolbar().items.get('mygridadd').setVisible(false);
	grid.getTopToolbar().items.get('mygriddelete').setVisible(false);

		//Ext.getCmp("locationCode").setEditable(false);
		// Ext.get("mygrid").dom.style.display = 'none';

});