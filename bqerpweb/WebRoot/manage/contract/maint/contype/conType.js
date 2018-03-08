Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;
	var date = new Date();
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		text : "合同类别"
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
			dataUrl : "managecontract/findByPId.action"
		})
	});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function initForm() {
		Ext.get("contype.contypeId").dom.value = "";
		Ext.get("contype.conTypeDesc").dom.value = "";
		Ext.get("contype.markCode").dom.value = "";
		Ext.get("contype.lastModifiedBy").dom.value = "";
		Ext.get("lastModifiedName").dom.value =date.getYear().toString()+"-"+date.getMonth().toString()+"-"+date.getDate().toString();
		Ext.get("contype.lastModifiedDate").dom.value = "";
		Ext.get("contype.memo").dom.value = "";
	}
	// ---------树的click事件
	function clickTree(node) {
		currentNode = node;
		if (node.id != "0") {
			currentType = '';
			pNode = node.parentNode;
			Ext.get("PContypeName").dom.value = pNode.text;
			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
		} else {
			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
			add();
			return false;
		}
		Ext.Ajax.request({
			url : 'managecontract/findInfoById.action',
			params : {
				node : currentNode.id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				Ext.get("contype.contypeId").dom.value = json.contypeId;
				Ext.get("contype.PContypeId").dom.value = json.PContypeId;
				Ext.get("contype.conTypeDesc").dom.value = json.conTypeDesc;
				Ext.get("contype.markCode").dom.value = json.markCode;
				isUse.setValue(json.isUse);
				Ext.get("contype.lastModifiedBy").dom.value = json.lastModifiedBy;
				Ext.get("contype.lastModifiedDate").dom.value = json.lastModifiedDate;
				Ext.get("lastModifiedName").dom.value = json.lastModifiedName;
				if(json.memo == null){
				Ext.get("contype.memo").dom.value = "";
				}else
				{
					Ext.get("contype.memo").dom.value = json.memo;
				}
				if (json.isUse == 'Y') {
					check();
				} else {
					Ext.get("contype.conTypeDesc").dom.readOnly = false;
					Ext.get("contype.markCode").dom.readOnly = false;
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};

	// -----------
	var contypeId = {
		id : "contype.contypeId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '自动生成',
		name : 'contype.contypeId'
	}
	var PContypeId = {
		id : "contype.PContypeId",
		xtype : 'hidden'
	}
	var PContypeName = {
		id : "PContypeName",
		xtype : "textfield",
		fieldLabel : '父类别名称',
		readOnly : true
	}
	var conTypeDesc = {
		id : "contype.conTypeDesc",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '类别名称',
		name : 'contype.conTypeDesc'
	}
	var markCode = {
		id : "contype.markCode",
		xtype : "textfield",
		fieldLabel : '识别码',
		allowBlank : false,
		name : 'contype.markCode'
	}
	var isUse = new Ext.form.ComboBox({
		fieldLabel : '是否使用',
		store : [['Y', '是'], ['N', '否']],
		// id : 'shift.isShift',
		name : 'contype.isUse',
		valueField : "value",
		displayField : "text",
		value : 'Y',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'contype.isUse',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		width : 200
	});
	var lastModifiedBy = {
		id : "contype.lastModifiedBy",
		xtype : "hidden"
	}
	var lastModifiedName = {
		id : "lastModifiedName",
		xtype : "textfield",
		fieldLabel : '修改人',
		readOnly : true
	}
	var lastModifiedDate = new Ext.form.DateField({
		fieldLabel : '修改日期',
		format : 'Y-m-d H:i:s',
		name : 'contype.lastModifiedDate',
		// value : '2008-11',
		id : 'lastModifiedDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		editable : false,
		value : date,
		readOnly : true
			// anchor : '90%'
	});
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'contype.memo'
	}
	// ------------

	function check() {
		Ext.get("contype.conTypeDesc").dom.readOnly = true;
		Ext.get("contype.markCode").dom.readOnly = true;
	}
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '在目录<<合同类别>>下增加',
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
			items : [contypeId, PContypeId, PContypeName, conTypeDesc,
					markCode, isUse, lastModifiedBy, lastModifiedName,
					lastModifiedDate, memo],
			buttons : [{
				id : 'btnAdd',
				text : "增加",
				// iconCls : 'add',
				handler : add
			}, {
				text : '删除',
				id : 'mydelete',
				handler : function() {
					//if (currentNode.id != null && currentNode.id.length > 0) {					
						myaddpanel.form.submit({
							url : 'managecontract/deleteConType.action',
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
				handler : function() {
					var url = ""
					if (currentType != "add") {
						url = "managecontract/updateConType.action"
					} else
						url = "managecontract/addConType.action"
					myaddpanel.getForm().submit({
						method : 'POST',
						url : url,
						success : function(form, action) {
							if (currentType == "add") {
								var o = eval('(' + action.response.responseText
										+ ')');	
								
								var id = o.type.contypeId;
								var text = o.type.conTypeDesc;
								var markCode = action.markCode;
								var newNode = new Ext.tree.AsyncTreeNode({
									id : id,
									leaf : true,
									text : text,
									markCode : markCode
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
									_node.setText(Ext.get("contype.conTypeDesc").dom.value);
									if (Ext.get("contype.isUse").dom.value == "Y") {
										check();
									} else {
										Ext.get("contype.conTypeDesc").dom.readOnly = false;
										Ext.get("contype.markCode").dom.readOnly = false;
									}
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
		Ext.get("contype.PContypeId").dom.value = currentNode.id;
		Ext.get("PContypeName").dom.value = currentNode.text;
		Ext.get("contype.conTypeDesc").dom.readOnly = false;
		Ext.get("contype.markCode").dom.readOnly = false;
		isUse.setValue("Y");
		initForm();
		myaddpanel.setTitle("在目录<<" + currentNode.text + ">>下增加");
	}
	// ------- 布局add
	var panelleft = new Ext.Panel({
		title: '合同类别树',
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
	Ext.get("contype.conTypeDesc").on('click', function() {
		if (currentNode == null) {
			Ext.Msg.alert("提示","请选择节点在增加");
			return false
		} else {
			if (Ext.get("contype.isUse").dom.value == "Y"
					&& currentType != "add") {
				Ext.Msg.alert("提示","在使用状态的合同不允许修改名称！");
			}
		}
	})
	Ext.get("contype.markCode").on('click', function() {
		if (currentNode == null) {
			Ext.Msg.alert("提示","请选择节点在增加");
			return false
		} else {
			if (Ext.get("contype.isUse").dom.value == "Y"
					&& currentType != "add") {
				Ext.Msg.alert("提示","在使用状态的合同不允许修改识别码！");
			}
		}
	})
});