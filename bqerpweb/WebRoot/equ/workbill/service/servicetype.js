Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;
	var date = new Date();
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				text : "服务类别"
			});
	var Tree = Ext.tree;
	var mytree = new Tree.TreePanel({
				rootVisible : true,
				autoHeight : true,
				root : root,
				animate : true,
				enableDD : true,
				border : false,
				containerScroll : true,
				loader : new Tree.TreeLoader({
							dataUrl : "workbill/findBySPId.action"
						})
			});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
		Ext.get("sertype.Id").dom.value = "";
		Ext.get("sertype.TypeName").dom.value = "";
		Ext.get("sertype.markCode").dom.value = "";

		Ext.get("sertype.memo").dom.value = "";
	}
	// ---------树的click事件
	function clickTree(node) {
		currentNode = node;

		if (node.id != "0") {
			currentType = '';
			pNode = node.parentNode;
			Ext.get("PPrjtypeName").dom.value = pNode.text;
			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
		} else {
			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
			add();
			return false;
		}
		Ext.Ajax.request({
					url : 'workbill/findInfoBySId.action',
					params : {
						node : currentNode.id
					},
					method : 'post',
					waitMsg : '正在加载数据...',

					success : function(result, request) {
						var json = eval('(' + result.responseText + ')');
						Ext.get("sertype.id").dom.value = json.id;
						Ext.get("sertype.pid").dom.value = json.pid;
						Ext.get("sertype.typename").dom.value = json.typename;
						Ext.get("sertype.markCode").dom.value = json.markCode;
						// isUse.setValue(json.isUse);
						// Ext.get("contype.lastModifiedBy").dom.value =
						// json.lastModifiedBy;
						// Ext.get("contype.lastModifiedDate").dom.value =
						// json.lastModifiedDate;
						// Ext.get("lastModifiedName").dom.value =
						// json.lastModifiedName;
						if (json.memo == null) {
							Ext.get("sertype.memo").dom.value = "";
						} else {
							Ext.get("sertype.memo").dom.value = json.memo;
						}
						// if (json.isUse == 'Y') {
						// check();
						// } else {
						// Ext.get("contype.conTypeDesc").dom.readOnly = false;
						// Ext.get("contype.markCode").dom.readOnly = false;
						// }
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});
	};

	// -----------
	var prjtypeId = {
		id : "sertype.id",
		xtype : "hidden",
		fieldLabel : 'ID',
		// value : '自动生成',
		name : 'sertype.id'
	}
	var PPrjtypeId = {
		id : "sertype.pid",
		xtype : 'hidden'
	}
	var PPrjTypeName = {
		id : "PPrjTypeName",
		xtype : "textfield",
		fieldLabel : '父类别名称',
		readOnly : true
	}
	var prjTypeName = {
		id : "sertype.typename",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '类别名称',
			listeners:{
					change:function(){ 
						if(Ext.get("sertype.typename").dom.value =="")
						return ; 
						var arrRslt = makePy(Ext.get("sertype.typename").dom.value);
						Ext.get("sertype.markCode").dom.value = arrRslt;
					}
				},
		name : 'sertype.typename'
	}

	var markCode = {
		id : "sertype.markCode",
		xtype : "textfield",
		fieldLabel : '识别码',
		allowBlank : false,
//		readOnly : true,
		name : 'sertype.markCode'
	}

	// var lastModifiedBy = {
	// id : "prjtype.lastModifiedBy",
	// xtype : "hidden"
	// }
	// var lastModifiedName = {
	// id : "lastModifiedName",
	// xtype : "textfield",
	// fieldLabel : '修改人',
	// readOnly : true
	// }
	// var lastModifiedDate = new Ext.form.DateField({
	// fieldLabel : '修改日期',
	// format : 'Y-m-d H:i:s',
	// name : 'prjtype.lastModifiedDate',
	// // value : '2008-11',
	// id : 'lastModifiedDate',
	// itemCls : 'sex-left',
	// clearCls : 'allow-float',
	// checked : true,
	// editable : false,
	// value : date,
	// readOnly : true
	// // anchor : '90%'
	// });
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'sertype.memo'
	}
	// ------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		labelWidth : 70,
		title : '在目录<<服务类别>>下增加',
		bodyStyle : 'padding:60px ',
		items : [{
			xtype : 'fieldset',
			buttonAlign : 'center',

			id : 'formSet',
			title : '基本信息',
			width : '100%',

			labelAlign : 'right',
			// labelWidth : '5%',
			defaults : {
				width : 270
			},
			defaultType : 'textfield',
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:20 15 25px 25px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},
			items : [prjtypeId, PPrjtypeId, PPrjTypeName, prjTypeName,
					markCode, memo],
			buttons : [{
						id : 'btnAdd',
						text : "增加",
						// iconCls : 'add',
						handler : add
					}, {
						text : '删除',
						id : 'mydelete',
						handler : function() {
							if (currentNode == null)// add by wpzhu
					{
						Ext.Msg.alert('提示信息', '请选择要删除的节点！');
						return;

					}
					if (!myaddpanel.getForm().isValid()) {
						Ext.Msg.alert('提示信息', '请选择要删除的节点！');
						return;
					}
							Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?',
									function(response) {
										if (response == 'yes') {
											myaddpanel.getForm().submit({
												method : 'post',
												url : 'workbill/deleteServiceType.action',
												// waitMsg : '正在删除数据,请等待...',
												success : function(form, action) {
													// ddjson = '';
													var removeNode = mytree
															.getNodeById(currentNode.id);
													// tree.getNodeById(currentNode.parentNode.id)
													// .reload();
													// removeNode.parentNode
													// .removeChild(removeNode);
													myaddpanel.getForm()
															.reset();
													var nd = mytree
															.getNodeById(currentNode.parentNode.id);
													var path = nd.getPath();
													mytree.getRootNode()
															.reload();
													mytree.expandPath(path,
															false);
													Ext.Msg.alert('提示信息',
															'删除模块成功！');
												},
												failure : function(form, action) {
													// ddjson = '';
													var o = eval("("
															+ action.response.responseText
															+ ")");
													Ext.Msg.alert('错误',
															o.Errmsg);
												}
											});
										}
									});

						}
					}, {

						text : '保存',
						id : 'mysave',
						handler : function() {

							var url = ""
							if (currentType != "add") {
								url = "workbill/updateServiceType.action"
							} else
								url = "workbill/addServiceType.action"
							if (myaddpanel.getForm().isValid()) {
								myaddpanel.getForm().submit({
									method : 'POST',
									url : url,
									success : function(form, action) {
										if (currentType == "add") {
											var o = eval('('
													+ action.response.responseText
													+ ')');

											var id = o.type.id;
											var text = o.type.typename;
											var markCode = action.markCode;
											var newNode = new Ext.tree.AsyncTreeNode(
													{
														id : id,
														leaf : true,
														text : text,
														markCode : markCode
													});
											var addnode = mytree
													.getNodeById(currentNode.id);
											if (!addnode.isLeaf()) {

												var fc = addnode.firstChild;
												if (fc == null)
													addnode
															.appendChild(newNode);
												else
													addnode
															.insertBefore(newNode);
												// Ext.MessageBox.alert('提示',
												// '增加成功!');
											} else {
												var rid = addnode.id;
												var rtext = addnode.text;
												var rnode = new Ext.tree.AsyncTreeNode(
														{
															id : rid,
															text : rtext,
															leaf : false
														});
												var rsibling = addnode.nextSibling;
												var pnode = addnode.parentNode;
												addnode.remove();
												rnode.appendChild(newNode)
												if (rsibling != null) {
													pnode.insertBefore(rnode,
															rsibling);
												} else {
													pnode.appendChild(rnode);
												}
												pnode.reload();

											}
											// newNode.select();
											Ext.MessageBox.alert('提示', '增加成功!');
										} else {

											if (currentNode == null) {
												Ext.Msg.alert("提示", "请先选择节点！");

											} else {
												var _node = mytree
														.getNodeById(currentNode.id);
												_node
														.setText(Ext
																.get("sertype.TypeName").dom.value);

												Ext.Msg.alert("提示", "修改成功！");
											}

										}
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert('错误', o.msg);
									}
								});
							}
						}
					}

			]

		}]

	});

	function add() {
		if (currentNode == null) {
			Ext.Msg.alert("提示", "请选择节点再增加");
			return false;
		}
		currentType = "add";
		Ext.get("sertype.pId").dom.value = currentNode.id;
		Ext.get("PPrjTypeName").dom.value = currentNode.text;
		Ext.get("sertype.TypeName").dom.readOnly = false;
		Ext.get("sertype.markCode").dom.readOnly = false;

		initForm();
		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
				title : '服务类别树',
				region : 'west',
				layout : 'fit',
				width : '25%',
				autoScroll : true,
				collapsible : true,
				split : true,
				items : [mytree]
			});
	var right = new Ext.Panel({
				region : "center",
				layout : 'fit',
				collapsible : true,
				items : [myaddpanel]
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [panelleft, right]
			});
	Ext.get("sertype.TypeName").on('click', function() {
				if (currentNode == null) {
					Ext.Msg.alert("提示", "请选择节点在增加");
					return false
				}

			})
	Ext.get("sertype.markCode").on('click', function() {
				if (currentNode == null) {
					Ext.Msg.alert("提示", "请选择节点在增加");
					return false
				}

			})
});