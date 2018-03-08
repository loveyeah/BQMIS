Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;

	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				code : 0,
				text : "辅机设备状态"
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
				loader : new Tree.TreeLoader({})
			});
	mytree.on('beforeload', function(node) {

				mytree.loader.dataUrl = 'productionrec/findFjStatusNodeByPNode.action?pid='
						+ node.attributes.code;
			});

	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
		Ext.get("model.jzztId").dom.value = "";
		Ext.get("model.jzztCode").dom.value = "";
		Ext.get("model.jzztName").dom.value = "";
		Ext.get("model.daddyCode").dom.value = "";
		Ext.get("model.jzztDescription").dom.value = "";
		Ext.get("model.displayNo").dom.value = "";
	}
	// ---------树的click事件
	function clickTree(node) {
		currentNode = node;

		if (node.id != "0") {
			currentType = '';
			pNode = node.parentNode;
			// Ext.get("model.jzztName").dom.value = pNode.text;
			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
		} else {

			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
			add();
			return false;
		}
		Ext.Ajax.request({
			url : 'productionrec/findFjStatusNodeInfo.action',
			params : {
				nodeid : currentNode.id
			},
			method : 'post',
			waitMsg : '正在加载数据...',

			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');

				Ext.get("model.jzztId").dom.value = json.jzztId;
				Ext.get("model.jzztCode").dom.value = json.jzztCode;
				Ext.get("model.jzztName").dom.value = json.jzztName;
				Ext.get("model.daddyCode").dom.value = json.daddyCode;
				if (json.jzztDescription == null)
					Ext.get("model.jzztDescription").dom.value = "";
				else
					Ext.get("model.jzztDescription").dom.value = json.jzztDescription;
				if (json.displayNo == null)
					Ext.get("model.displayNo").dom.value = "";
				else
					Ext.get("model.displayNo").dom.value = json.displayNo;

			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};

	// -----------
	var jzztId = {
		id : "model.jzztId",
		xtype : "hidden",
		fieldLabel : 'ID',
		// value : '自动生成',
		name : 'model.jzztId'
	}

	var jzztName = {
		id : "model.jzztName",
		xtype : "textfield",
		fieldLabel : '状态名称'
		// readOnly : true
	}
	var jzztCode = {
		id : "model.jzztCode",
		xtype : "textfield",
		fieldLabel : '状态编码'
		// readOnly : true
	}
	var daddyCode = {
		id : "model.daddyCode",
		xtype : "textfield",
		fieldLabel : '上级编码',
		allowBlank : false,

		readOnly : true,
		name : 'model.daddyCode'
	}
	var displayNo = {
		id : "model.displayNo",
		xtype : "textfield",
		allowBlank : true,

		name : 'model.displayNo',
		fieldLabel : '显示顺序'
	}

	var jzztDescription = {
		id : "model.jzztDescription",
		xtype : "textarea",
		fieldLabel : '状态描述',
		name : 'model.jzztDescription'
	}
	// ------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		labelWidth : 70,
		title : '在目录<<状态>>下增加',
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
			items : [jzztId, jzztCode, jzztName, daddyCode, jzztDescription,
					displayNo],

			buttons : [{
						id : 'btnAdd',
						text : "增加",
					    iconCls : 'add',
						handler : add
					}, {
						text : '删除',
						id : 'mydelete',
						 iconCls : 'delete',
						handler : function() {
						
							if (Ext.get("model.jzztId").dom.value == ""
									|| Ext.get("model.jzztId").dom.value == null) {
								Ext.Msg.alert("提示", "请先选择删除的节点！")
								return;
							}

							Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?',
									function(response) {
										if (response == 'yes') {
											myaddpanel.getForm().submit({
												method : 'post',
												url : 'productionrec/deleteFjStatusNode.action',
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
					    iconCls : 'save',
						handler : function() {

							var url = ""
							if (currentType == "add") {
								url = "productionrec/saveFjStatusNode.action"
							} else
								url = "productionrec/updateFjStatusNode.action"
							if (myaddpanel.getForm().isValid()) {
								myaddpanel.getForm().submit({
									method : 'POST',
									url : url,
									success : function(form, action) {
										if (currentType == "add") {
											var o = eval('('
													+ action.response.responseText
													+ ')');
											//alert(Ext.encode(o))
											var jzztId = o.baseInfo.jzztId;
											var jzztCode = o.baseInfo.jzztCode;
											var jzztName = o.baseInfo.jzztName;

											var newNode = new Ext.tree.AsyncTreeNode(
													{
														id : jzztId,
														leaf : true,
														text : jzztName,
														code : jzztCode
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
															code : jzztCode,

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
														
//												_node
//														.setText(Ext
//																.get("baseInfo.jzztName").dom.value);

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
		// Ext.get("model.jzztId").dom.value = currentNode.id;
		initForm();
		Ext.get("model.daddyCode").dom.value = currentNode.attributes.code;

		// Ext.get("prjtype.prjTypeName").dom.readOnly = false;
		// Ext.get("prjtype.markCode").dom.readOnly = false;

		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
				title : '辅机设备状态树',
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
	
});