Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var currentNode = '';
	var currnetType = '';
	function treeClick(node, e) {
		Ext.Msg.wait("正在加载数据,请等待..."); 
		if(e)
		{
			e.stopEvent();
		}
		if (node.id == '0') {
			form.getForm().reset();
			content.hide();
			currentNode = node;
		} else {
			content.show();
			Ext.Ajax.request({
				url : 'manager/getItemInfo.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.get('bpItem.itemCode').dom.value = o[0];
					Ext.get("bpItem.itemName").dom.value = o[1];
					if (o[2] == "Y") {
						Ext.getCmp('ay').setValue(true);
					} else if(o[2] == "N"){
						Ext.getCmp('an').setValue(true);
					}
					
					if (o[3] != null) {
						Ext.get("pItemName").dom.value = o[3];
					}
					else
					{
						Ext.get("pItemName").dom.value = root.text;
					}
					if (o[4] != null) {
						Ext.get("unitCode").dom.value = o[5];
					}
					if (o[6] != null) {
						Ext.get("bpItem.itemMemo").dom.value = o[6];
					}else{
						Ext.get("bpItem.itemMemo").dom.value == "";
					}
					Ext.get("bpItem.retrieveCode").dom.value = o[7];
					
					if(o[8] != null){
					Ext.get("bpItem.orderBy").dom.value = o[8];
					}else{
						Ext.get("bpItem.orderBy").dom.value == "";
					}
					currentNode = node;
					currnetType = "update";
					itemName.on("change", function(filed, newValue, oldValue) {
							Ext.Ajax.request({
								url : 'manager/getRetrieveCode.action',
								params : {
									name : newValue
								},
								method : 'post',
								success : function(result, request) {
									var json = result.responseText;
									var o = eval("(" + json + ")");
									Ext.get("bpItem.retrieveCode").dom.value = o.substring(0,8);
								}
							});
					})
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				}
			});
		}
		Ext.Msg.hide();
	};

	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({ 
		autoScroll : true,
		root : root, 
		animate : true,// 是否有动画效果
		enableDD : false,// 是否可以拖放节点
		useArrows : false, // 文件夹前显示的图标改变了不在是+号了
		border : false,
		rootVisible : true,
		containerScroll : true,// 是否含滚动条
		lines : true,
		tbar:new Ext.Panel({
			id:'treetbar',
			layout:'column',
			items : [{
				xtype : 'textfield', 
				id : 'searchKey',
				width : '100%',
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) { 
							root.reload();
						}
					}
				}  
			}  
			] 
		}),
		loader : new Tree.TreeLoader({
			dataUrl : 'manager/getTreeList.action',
			 listeners: {
                "beforeload": function(treeloader, node) {
                	var key = Ext.getCmp("searchKey").getValue();
					if(typeof(key) == "undefined")
					key = ''; 
                    treeloader.baseParams = {
                        pid: node.id,
                        method: 'POST',
                        searchKey:key
                    };
                }
            } 
		})
	});
 
	var root = new Tree.AsyncTreeNode({
		text : '经营指标编码体系', 
		iconCls : 'x-tree-node-icon',
		draggable : false,
		id : '0'
	});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick); 
	tree.on('contextmenu', function(node, e) {
		e.stopEvent();
		currentNode = node;
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '添加',
				iconCls : 'add',
				handler : function() {
					itemName.focus();
					if (currentNode == "") {
						Ext.Msg.alert('', '请先选择指标节点！');
					} else {
						itemName.focus();

						Ext.get("bpItem.itemMemo").dom.value = "";
						Ext.get("unitCode").dom.value = "";
						Ext.get("bpItem.itemMemo").dom.parentNode.parentNode.style.display = '';
						Ext.get("unitCode").dom.parentNode.parentNode.parentNode.style.display = '';
						content.show();
						currnetType = "add";
						form.getForm().reset();
						Ext.get("pItemName").dom.value = currentNode.text;

						// 给radio赋值
						Ext.get("ay").dom.checked = true;
					}

				}
			}), new Ext.menu.Item({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					currentNode = node;
					if (currentNode.id == "0") {
						Ext.Msg.alert('', '根节点不可删除！')
					} else {
						Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(
								response) {
							if (response == 'yes') {
								Ext.Msg.wait("正在删除.....");
								Ext.Ajax.request({
									url : 'manager/deleteItem.action',
									params : {
										id : currentNode.id
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										var removeNode = tree.getNodeById(currentNode.id);
										form.getForm().reset();
										var nd = tree.getNodeById(currentNode.parentNode.id);
										var path = nd.getPath();
										tree.getRootNode().reload();
										tree.expandPath(path, false);
										Ext.Msg.alert("提示", o.msg);
									},
									failure : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.Msg.alert("错误", o.msg);
									}
								});
								Ext.Msg.hide();
							}
						});
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);
	var btnAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : function() {
			if (currentNode == "") {
				Ext.Msg.alert('', '请先选择指标节点！');
			} else {
				itemName.focus();

				Ext.get("bpItem.itemMemo").dom.value = "";
				Ext.get("unitCode").dom.value = "";
				Ext.get("bpItem.itemMemo").dom.parentNode.parentNode.style.display = '';
				Ext.get("unitCode").dom.parentNode.parentNode.parentNode.style.display = '';
				content.show();
				currnetType = "add";
				form.getForm().reset();
				Ext.get("pItemName").dom.value = currentNode.text;

				// 给radio赋值
				Ext.get("ay").dom.checked = true;
						{
							 Ext.get("bpItem.itemMemo").dom.value="";
							 Ext.get("unitCode").dom.value="";
							 Ext.get("bpItem.itemMemo").dom.parentNode.parentNode.style.display='';
							 Ext.get("unitCode").dom.parentNode.parentNode.parentNode.style.display='';
							}
			}
		}
	});
	var btnCancel = new Ext.Button({
		text : '取消',
		iconCls:'cancer',
		handler : function()
		{ 
			treeClick(currentNode);
		}
	});
	var btnUdt = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			if (currentNode == "") {
				Ext.Msg.alert('', '请选择要修改的指标节点！')
			} else if (currentNode.id == '0' && currnetType == 'update') {
				Ext.Msg.alert('', '根节点不可编辑！')
			} else { 
				if (form.getForm().isValid()) {
					Ext.Msg.wait("正在保存数据,请等待...");
					var isItem = Ext.get("ay").dom.checked;
					var idItemValue = (isItem ? "Y" : "N");
					form.getForm().submit({
						method : 'post',
						url : 'manager/saveItem.action',
						params : {
							method : currnetType,
							'bpItem.isItem' : idItemValue,
							//itemName : Ext.get('itemName').dom.value,
							id : currentNode.id
						},
						success : function(form, action) {
							if (currnetType == 'add') {
								var o = eval('(' + action.response.responseText
										+ ')');
								var id = o.msg;
								var newNode = new Ext.tree.TreeNode({
									id : id,
									text : Ext.get('bpItem.itemName').dom.value,
									leaf : true,
									iconCls : "file"
								});
								var nd = tree.getNodeById(currentNode.id);
								var path = nd.getPath();
								tree.getRootNode().reload();
								tree.expandPath(path, false);
								currentNode.id = id;
								currnetType = 'update';
								Ext.Msg.alert('提示', '数据保存成功！')

							} else if (currnetType == 'update') {
									var o = eval('(' + action.response.responseText
										+ ')');
								var id = o.msg;
								unitCode.setValue(id);
								var node = tree.getNodeById(currentNode.id);
								var path = node.getPath();
								tree.getRootNode().reload();
								tree.expandPath(path, false);
                                 currentNode.id=id;
								Ext.Msg.alert('提示', '数据修改成功！')
							}
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.MessageBox.alert('提示', o.msg);
						} 
					})
				} 
			}
		}
	});
	var btnDlt = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (currentNode == "") {
				Ext.Msg.alert('', '请选择要删除的指标节点！')
			} else if (currentNode.id == "0") {
				Ext.Msg.alert('', '根节点不可删除！')
			} else {
				Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(response) {
					if (response == 'yes') {
						Ext.Msg.wait("正在删除.....");
						Ext.Ajax.request({
							url : 'manager/deleteItem.action',
							params : {
								id : currentNode.id
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								var removeNode = tree.getNodeById(currentNode.id);
								form.getForm().reset();
								var nd = tree.getNodeById(currentNode.parentNode.id);
								var path = nd.getPath();
								tree.getRootNode().reload();
								tree.expandPath(path, false);
								Ext.Msg.alert("提示", o.msg);
							},
							failure : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.Msg.alert("错误", o.msg);
							}
						});
						Ext.Msg.hide();
					}
				});
			}
		}
	});
	var itemName = new Ext.form.TextField({
		//id : 'itemName',
		name : 'bpItem.itemName',
		xtype : 'textfield',
		fieldLabel : '指标名称',
		readOnly : false,
		anchor : '85%',
		allowBlank : false,
		blankText : '不可为空！'
	
	});

	//计量单位下拉框数据源
	var unitData = Ext.data.Record.create([{
		name : 'unitName'
	}, {
		name : 'unitId'
	}]);
	var allUnitStore = new Ext.data.JsonStore({
		url : 'resource/getAllUnitList.action',
		root : 'list',
		fields : unitData
	});
	allUnitStore.load();

	var unitCombobox = new Ext.form.ComboBox({
		fieldLabel : '计量单位',
		id : 'unitCode',
		allowBlank : true,
		style : "border-bottom:solid 2px",
		triggerAction : 'all',
		editable:false,
		store : allUnitStore,
		blankText : '',
		emptyText : '',
		valueField : 'unitId',
		displayField : 'unitName',
		mode : 'local',
		hiddenName : 'bpItem.unitCode',
		anchor : '85%'
	})
	
	var unitCode = new Ext.form.TextField({ 
		name : 'bpItem.itemCode',
		xtype : 'textfield',
		fieldLabel : '指标编码',
		cls: 'disable',
		readOnly : true,
		anchor : '85%',
		blankText : '不可为空！' 
	});
	var retrieveCode = new Ext.form.TextField({
		name : 'bpItem.retrieveCode',
		xtype : 'textfield',
		fieldLabel : '检索码',
		readOnly : true,
		anchor : '85%',
		maxLength : 8,
		maxLengthText : '最多输入8个字符！'
//		,
//		vtype : 'alpha'
	});

	var pItemName = new Ext.form.TextField({
		id : 'pItemName',
		xtype : 'textfield',
		cls: 'disable',
		fieldLabel : '父指标名称',
		readOnly : true,
		anchor : '85%'
	});

	var orderBy = new Ext.form.NumberField({
		id : 'bpItem.orderBy',
		xtype : 'numberfield',
		fieldLabel : '显示顺序',
		readOnly : false,
		anchor : '42.5%'
	});

	var itemMemo = new Ext.form.TextField({
		id : 'bpItem.itemMemo',
		xtype : 'textarea',
		fieldLabel : '指标说明',
		readOnly : false,
		height : 120,
		anchor : '92.5%'
	});
	
	var content = new Ext.form.FieldSet({
		title : '指标信息',
		height : '100%',
		layout : 'form',
		buttonAlign:'center',
		buttons:[btnUdt,btnCancel],
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [unitCode, itemName,retrieveCode]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 100,
				border : false,
				items : [pItemName,unitCombobox, 
				{ 
					layout : 'column',
					isFormField : true,
					fieldLabel : '是否指标',
					style:{cursor:'hand'},
					anchor : '80%',
					border : false,
					items : [{
						columnWidth : .4,
						border : false,
						items : new Ext.form.Radio({
							id : 'ay',
							boxLabel : '是',
							name : 'as',
							readOnly : true,
							inputValue : 'Y',
						//	checked : true,
							listeners:{
						   'check' : function(radio,check){
							if(check)
							{
							 Ext.get("bpItem.itemMemo").dom.value="";
							 Ext.get("unitCode").dom.value="";
							 Ext.get("bpItem.itemMemo").dom.parentNode.parentNode.style.display='';
							 Ext.get("unitCode").dom.parentNode.parentNode.parentNode.style.display='';
							}
							
						}	 
					}
						})
					}, {
						columnWidth : .4,
						border : false,
						items : new Ext.form.Radio({
							id : 'an',
							boxLabel : '否',
							readOnly : true,
							name : 'as',
							inputValue : 'N',
							listeners:{
						'check' : function(radio,check){
							if(check)
							{
								Ext.get("bpItem.itemMemo").dom.parentNode.parentNode.style.display='none';
							 	Ext.get("unitCode").dom.parentNode.parentNode.parentNode.style.display='none';
							}
						}	 
					}
						})
					}]
				}]
			}]

		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.3,
			labelWidth : 100,
			items : [orderBy]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 100,
			items : [itemMemo]
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 70,
		autoHeight : true, 
		border : false,
		tbar : [btnAdd, {
			xtype : "tbseparator"
		}, btnDlt, {
			xtype : "tbseparator"
		}],
		items : [content]
		
	});
	var panel = new Ext.Panel({
		border : false,
		collapsible : false,
		title : '指标信息维护',
		items : [form]
	});

	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [panel]

		}, {
			title : "经营指标",
			region : 'west',
			margins : '0 0 0 0',
			split : true,
			collapsible : true,
			titleCollapse : true,
			width : 200,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [tree]
		}]
	})
	content.hide();
});
