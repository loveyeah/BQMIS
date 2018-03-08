Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;
	var date = new Date();
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		text : "费用来源"
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
			dataUrl : "managecontract/findByPItemId.action"
		})
	});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
//		Ext.get("source.itemId").dom.value = "";
		Ext.get("source.itemId").dom.value = 0;
		Ext.get("source.itemName").dom.value = "";
		Ext.get("source.itemCode").dom.value = "";
		Ext.get("source.memo").dom.value = "";
	}
	// ---------树的click事件
	function clickTree(node) {		
		currentNode = node;
		if (node.id != "0") {
			currentType = '';
			pNode = node.parentNode;
			Ext.get("PItemName").dom.value = pNode.text;
			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
		} else {
			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
			add();
			return false;
		}
		Ext.Ajax.request({
			url : 'managecontract/findInfoByItemId.action',
			params : {
				node : currentNode.id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				Ext.get("source.itemId").dom.value = json.itemId;
				Ext.get("source.PItemId").dom.value = json.PItemId;
				Ext.get("source.itemCode").dom.value = json.itemCode;
				Ext.get("source.itemName").dom.value = json.itemName;
				isUse.setValue(json.isUse);
				if(json.memo == null){
				Ext.get("source.memo").dom.value = "";
				}else
				{
					Ext.get("source.memo").dom.value = json.memo;
				}
				if (json.isUse == 'Y') {
					check();
				} else {
					Ext.get("source.itemCode").dom.readOnly = false;
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};

	// -----------
	var itemId = {
		id : "source.itemId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : 0,
		name : 'source.itemId'
	}
	var PItemId = {
		id : "source.PItemId"
		,
		xtype : 'hidden'
	}
	var PItemName = {
		id : "PItemName",
		xtype : "textfield",
		fieldLabel : '父费用来源名称',
		readOnly : true
	}
	var itemName = {
		id : "source.itemName",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '费用来源名称',
		name : 'source.itemName'
	}
	var itemCode = {
		id : "source.itemCode",
		xtype : "textfield",
		fieldLabel : '费用来源编码',
		allowBlank : false,
		name : 'source.itemCode'
	}
	var isUse = new Ext.form.ComboBox({
		hidden : true,
		hideLabel : true,
		fieldLabel : '是否使用',
		store : [['Y', '是'], ['N', '否']],
		name : 'source.isUse',
		valueField : "value",
		displayField : "text",
		value : 'Y',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
//		hiddenName : 'source.isUse',
		hiddenName : 'sourceisUse',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		width : 200
	});
	
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'source.memo'
	}
	// ------------

	function check() {
		Ext.get("source.itemCode").dom.readOnly = true;
	}
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '在目录<<费用来源>>下增加',
		bodyStyle : 'padding:30px',
		items : [{
			xtype : 'fieldset',
			buttonAlign:'center',
			id : 'formSet',
			title : '基本信息',
			width : 600,
			labelAlign : 'right',
			labelWidth : 100,
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
			items : [itemId, PItemId, PItemName, itemCode,
					itemName,  memo],
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
					//if (currentNode.id != null && currentNode.id.length > 0) {					
						myaddpanel.form.submit({
							url : 'managecontract/deleteItemSource.action',
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(form, action) {
							Ext.MessageBox.alert('提示', '删除成功!');
							},
							failure : function(form, action) {
								Ext.MessageBox.alert('错误', '操作失败!');
							}
						});
					//}
				}
			}, {
				text : '保存',
				id : 'mysave',
				iconCls : 'save',
				handler : function() {
					var url = ""
					if (currentType != "add") {
						url = "managecontract/updateItemSource.action"
					} else
						url = "managecontract/addItemSource.action"
					myaddpanel.getForm().submit({
						method : 'POST',
						url : url,
						success : function(form, action) {
							if (currentType == "add") {
								var o = eval('(' + action.response.responseText
										+ ')');	
								var id = o.source.itemId;
								var code = o.source.itemCode;
								var text = o.source.itemName;								
								var newNode = new Ext.tree.AsyncTreeNode({
									id : id,
									leaf : true,
									text : text,
									code : code
								});
								var addnode = mytree.getNodeById(currentNode.id);
								if (!addnode.isLeaf()) {
									
									var fc = addnode.firstChild;
									if (fc == null)
										addnode.appendChild(newNode);
									else
										addnode.insertBefore(newNode);
									//	Ext.MessageBox.alert('提示', '增加成功!');
								} else {
									var rid = addnode.id;
									var rtext = addnode.text;
									var rnode = new Ext.tree.AsyncTreeNode({
										id : rid,
										text : rtext,
										leaf : false
									});
									var rsibling = addnode.nextSibling;
									var pnode = addnode.parentNode;
									addnode.remove();
									rnode.appendChild(newNode)
									if (rsibling != null){
										pnode.insertBefore(rnode, rsibling);
										}
									else{
										pnode.appendChild(rnode);}				
								pnode.reload();
									
								}
//								newNode.select();
								Ext.MessageBox.alert('提示', '增加成功!');
							} else {

								if (currentNode == null) {
									Ext.Msg.alert("提示","请先选择节点！");

								} else {
									var _node = mytree.getNodeById(currentNode.id);
									_node.setText(Ext.get("source.itemName").dom.value);
//									if (Ext.get("source.isUse").dom.value == "Y") {
//										check();
//									} else {
//										Ext.get("source.itemName").dom.readOnly = false;
//									}
									Ext.get("source.itemCode").dom.readOnly = true;
									Ext.Msg.alert("提示","修改成功！");
								}

							}
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});

				}
			}

			]

		}]

	});
	function add() {
		if (currentNode == null) {
			Ext.Msg.alert("提示","请选择节点在增加");
			return false;
		}
		currentType = "add";
		myaddpanel.getForm().reset();
		Ext.get("source.PItemId").dom.value = currentNode.id;
		Ext.get("PItemName").dom.value = currentNode.text;
		Ext.get("source.itemCode").dom.value = currentNode.code;
		Ext.get("source.itemCode").dom.readOnly = false;
		isUse.setValue("Y");
//		initForm();
		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
		title: '费用来源树',
		region : 'west',
		layout : 'fit',
		width : 250,
		autoScroll:true,
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
	Ext.get("source.itemCode").on('click', function() {
		if (currentNode == null) {
			Ext.Msg.alert("提示","请选择节点在增加");
			return false
		} else {
//			if (currentType != "add") {
//				Ext.Msg.alert("提示","在使用状态的费用来源不允许修改编码！");
//			}
		}
	})
});