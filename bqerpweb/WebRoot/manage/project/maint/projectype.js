Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;
	var date = new Date();
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				text : "项目类别"
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
							dataUrl : "manageproject/findByPId.action"
						})
			});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
		Ext.get("prjtype.prjTypeId").dom.value = "";
		Ext.get("prjtype.prjTypeName").dom.value = "";
		Ext.get("prjtype.markCode").dom.value = "";

		Ext.get("prjtype.memo").dom.value = "";
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
					url : 'manageproject/findInfoById.action',
					params : {
						node : currentNode.id
					},
					method : 'post',
					waitMsg : '正在加载数据...',

					success : function(result, request) {
						var json = eval('(' + result.responseText + ')');
						Ext.get("prjtype.prjTypeId").dom.value = json.prjTypeId;
						Ext.get("prjtype.prjPTypeId").dom.value = json.prjPTypeId;
						Ext.get("prjtype.prjTypeName").dom.value = json.prjTypeName;
						Ext.get("prjtype.markCode").dom.value = json.markCode;
						// isUse.setValue(json.isUse);
						// Ext.get("contype.lastModifiedBy").dom.value =
						// json.lastModifiedBy;
						// Ext.get("contype.lastModifiedDate").dom.value =
						// json.lastModifiedDate;
						// Ext.get("lastModifiedName").dom.value =
						// json.lastModifiedName;
						if (json.memo == null) {
							Ext.get("prjtype.memo").dom.value = "";
						} else {
							Ext.get("prjtype.memo").dom.value = json.memo;
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
		id : "prjtype.prjTypeId",
		xtype : "hidden",
		fieldLabel : 'ID',
		// value : '自动生成',
		name : 'prjtype.prjTypeId'
	}
	var PPrjtypeId = {
		id : "prjtype.prjPTypeId",
		xtype : 'hidden'
	}
	var PPrjTypeName = {
		id : "PPrjTypeName",
		xtype : "textfield",
		fieldLabel : '父类别名称',
		readOnly : true
	}
	var prjTypeName = {
		id : "prjtype.prjTypeName",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '类别名称',
			listeners:{
					change:function(){ 
						if(Ext.get("prjtype.prjTypeName").dom.value =="")
						return ; 
						var arrRslt = makePy(Ext.get("prjtype.prjTypeName").dom.value);
						Ext.get("prjtype.markCode").dom.value = arrRslt;
					}
				},
		name : 'prjtype.prjTypeName'
	}

	var markCode = {
		id : "prjtype.markCode",
		xtype : "textfield",
		fieldLabel : '识别码',
		allowBlank : false,
		readOnly : true,
		name : 'prjtype.markCode'
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
		name : 'prjtype.memo'
	}
	// ------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		labelWidth : 70,
		title : '在目录<<合同类别>>下增加',
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
							Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?',
									function(response) {
										if (response == 'yes') {
											myaddpanel.getForm().submit({
												method : 'post',
												url : 'manageproject/deletePrjType.action',
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
								url = "manageproject/updatePrjType.action"
							} else
								url = "manageproject/addPrjType.action"
							if (myaddpanel.getForm().isValid()) {
								myaddpanel.getForm().submit({
									method : 'POST',
									url : url,
									success : function(form, action) {
										if (currentType == "add") {
											var o = eval('('
													+ action.response.responseText
													+ ')');

											var id = o.type.prjTypeId;
											var text = o.type.prjTypeName;
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
																.get("prjtype.prjTypeName").dom.value);

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
		Ext.get("prjtype.prjPTypeId").dom.value = currentNode.id;
		Ext.get("PPrjTypeName").dom.value = currentNode.text;
		Ext.get("prjtype.prjTypeName").dom.readOnly = false;
		Ext.get("prjtype.markCode").dom.readOnly = false;

		initForm();
		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
				title : '合同类别树',
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
	Ext.get("prjtype.prjTypeName").on('click', function() {
				if (currentNode == null) {
					Ext.Msg.alert("提示", "请选择节点在增加");
					return false
				}

			})
	Ext.get("prjtype.markCode").on('click', function() {
				if (currentNode == null) {
					Ext.Msg.alert("提示", "请选择节点在增加");
					return false
				}

			})
});