Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;

	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		code : 0,
		text : "财务科目"
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

		mytree.loader.dataUrl = 'managebudget/findFinanceTree.action?pid='
				+ node.attributes.code;
	});

	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
		Ext.get("model.financeId").dom.value = "";
		Ext.get("model.financeItem").dom.value = "";
		Ext.get("model.financeName").dom.value = "";
		Ext.get("model.upperItem").dom.value = "";
	}
	// ---------树的click事件
	function clickTree(node) {
		currentNode = node;

		if (node.id != "0") {
			currentType = '';
			pNode = node.parentNode;
			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
		} else {

			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
			add();
			return false;
		}
		Ext.Ajax.request({
			url : 'managebudget/findNodeInfo.action',
			params : {
				nodeid : currentNode.id
			},
			method : 'post',
			waitMsg : '正在加载数据...',

			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');

				Ext.get("model.financeId").dom.value = json.financeId;
				Ext.get("model.financeItem").dom.value = json.financeItem;
				Ext.get("model.financeName").dom.value = json.financeName;
				Ext.get("model.upperItem").dom.value = json.upperItem;

			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};

	// -----------
	var financeId = {
		id : "model.financeId",
		xtype : "hidden",
		fieldLabel : 'ID',
		// value : '自动生成',
		name : 'model.financeId'
	}

	var financeName = {
		id : "model.financeName",
		xtype : "textfield",
		fieldLabel : '科目名称'
	}
	//	var financeItem = new Ext.form.TextField({
	//		id : "model.financeItem",
	////		xtype : "textfield",
	//		fieldLabel : '科目编码',
	//		allowBlank : false,
	//		name : 'model.financeItem',
	//		vtype : 'alphanum'
	////		regex : /^^[\u0391-\uFFE5]+$/,
	////				regexText:"只能由数字和字母组成"
	////		listeners : {
	////			check:function(){
	////				
	////			}
	////		}	
	//	})
	var financeItem = {
		id : "model.financeItem",
		xtype : "textfield",
		fieldLabel : '科目编码',
		allowBlank : false,
		name : 'model.financeItem',
		vtype : 'alphanum'
	}
	var upperItem = {
		id : "model.upperItem",
		xtype : "textfield",
		fieldLabel : '上级科目',
		allowBlank : false,

		readOnly : true,
		name : 'model.upperItem'
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
			items : [financeId, financeItem, financeName, upperItem],

			buttons : [{
				id : 'btnAdd',
				text : "增加",
				// iconCls : 'add',
				handler : add
			}, {
				text : '删除',
				id : 'mydelete',
				handler : function() {

					if (Ext.get("model.financeId").dom.value == ""
							|| Ext.get("model.financeId").dom.value == null) {
						Ext.Msg.alert("提示", "请先选择删除的节点！")
						return;
					}

					Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(
							response) {
						if (response == 'yes') {
							myaddpanel.getForm().submit({
								method : 'post',
								url : 'managebudget/deleteNode.action',
								// waitMsg : '正在删除数据,请等待...',
								success : function(form, action) {
									// ddjson = '';
									var removeNode = mytree
											.getNodeById(currentNode.id);
									myaddpanel.getForm().reset();
									var nd = mytree
											.getNodeById(currentNode.parentNode.id);
									var path = nd.getPath();
									mytree.getRootNode().reload();
									mytree.expandPath(path, false);
									Ext.Msg.alert('提示信息', '删除模块成功！');
								},
								failure : function(form, action) {
									// ddjson = '';
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
				id : 'mysave',
				handler : function() {
					
					if(Ext.get('model.upperItem').dom.value == null || 
						Ext.get('model.upperItem').dom.value == "")
						{
							Ext.Msg.alert('提示信息','上级科目不能为空,请选择！');
							return;
						}
					var url = ""
					if (currentType == "add") {
						url = "managebudget/saveNode.action"
					} else
						url = "managebudget/updateNode.action"
					if (myaddpanel.getForm().isValid()) {
						myaddpanel.getForm().submit({
							method : 'POST',
							url : url,
							success : function(form, action) {
								if (currentType == "add") {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									if(o.msg != null)
									{
										Ext.Msg.alert('提示信息',o.msg);
										return;
									}
									var financeId = o.baseInfo.financeId;
									var financeItem = o.baseInfo.financeItem;
									var financeName = o.baseInfo.financeName;

									var newNode = new Ext.tree.AsyncTreeNode({
												id : financeId,
												leaf : true,
												text : financeName,
												code : financeItem
											});
									var addnode = mytree
											.getNodeById(currentNode.id);
									if (!addnode.isLeaf()) {

										var fc = addnode.firstChild;
										if (fc == null)
											addnode.appendChild(newNode);
										else
											addnode.insertBefore(newNode);
									} else {
										var rid = addnode.id;
										var rtext = addnode.text;
										var rnode = new Ext.tree.AsyncTreeNode({
													id : rid,
													text : rtext,
													code : financeItem,

													leaf : false
												});
										var rsibling = addnode.nextSibling;
										var pnode = addnode.parentNode;
										addnode.remove();
										rnode.appendChild(newNode)
										if (rsibling != null) {
											pnode.insertBefore(rnode, rsibling);
										} else {
											pnode.appendChild(rnode);
										}
										pnode.reload();

									}
									// newNode.select();
									Ext.MessageBox.alert('提示', '增 加 成 功！');
								} else {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									if(o.msg != null)
									{
										Ext.Msg.alert('提示信息',o.msg);
										return;
									}
						
									if (currentNode == null) {
										Ext.Msg.alert("提示", "请先选择节点！");

									} else {
										var _node = mytree
												.getNodeById(currentNode.id);
										_node
												.setText(Ext
														.get("model.financeName").dom.value);
										mytree.getRootNode().reload();
										currentNode = root;
										Ext.Msg.alert("提示", "修 改 成 功！");
									}

								}
							},
							failure : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.Msg.alert('错误', o.msg);
							}
						});
					}
					else
					{
						Ext.Msg.alert('提示信息','财务编码只能为字母与数字！');
						return;
					}

				}
			}]
		}]
	});

	function add() {
		if (currentNode == null) {
			Ext.Msg.alert("提示", "请选择节点再增加");
			return false;
		}
		currentType = "add";
		initForm();
		Ext.get("model.upperItem").dom.value = currentNode.attributes.code;
		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
		title : '财务科目',
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