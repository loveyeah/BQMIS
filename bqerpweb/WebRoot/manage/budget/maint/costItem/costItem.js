Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var itemFCode;
	var addOrUpdate;
	function treeClick(node, e) {
		e.stopEvent();
		panel.setTitle("当前选择指标:[" + node.text + "]");
		var isItem = node.attributes.code;
		addOrUpdate = "update";
		var isItem = (isItem == null ? false : true);
		iyRadio.setValue(isItem);
		inRadio.setValue(!isItem);
		iyRadio.disable();
		inRadio.disable();
		Ext.Msg.wait("正在加载数据,请等待...");
		Ext.Ajax.request({
					url : 'managebudget/getCostItemInfo.action',
					params : {
						id : node.id
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var str = result.responseText;
						var o = eval("(" + str.substring(1, str.length - 1)
								+ ")");
						// formPositon2.show();
						itemName.setValue(o.itemName);
						itemName1.setValue(o.itemName);
						itemCode.setValue(o.itemCode);
						orderBy.setValue(o.orderBy);
						itemId.setValue(o.itemId);
						// 计量单位
						if (o.unitCode != null)
							statItemUnit.setValue(o.unitCode);
						else
							statItemUnit.clearValue();
						// 产生方式
						if (o.comeFrom != null)
							comeTypeBox.setValue(o.comeFrom);
						else
							comeTypeBox.clearValue();
						// 指标类型
						if (o.itemType != null)
							itemType.setValue(o.itemType);
						else
							itemType.clearValue();
						// 计算级别
						if (o.accountOrder != null)
							accountOrder.setValue(o.accountOrder);
						else
							accountOrder.setValue("");
						// 指标说明
						if (o.itemMemo != null) {
							itemMemo.setValue(o.itemMemo);
						} else {
							itemMemo.setValue("");
						}
						// 计算公式
						if (o.itemExplain != null) {
							itemExplain.setValue(o.itemExplain);
						} else {
							itemExplain.setValue("");
						}
						if (o.comeFrom == 3) {
							btnAccountOrder.setDisabled(false);
							budgetFormula.setDisabled(false);
						} else {
							btnAccountOrder.setDisabled(true);
							budgetFormula.setDisabled(true);
						}
						Ext.Msg.hide();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});
	};

	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				autoScroll : true,
				root : root,
				layout : 'fit',
				autoHeight : false,
				animate : true,// 是否有动画效果
				enableDD : false,// 是否可以拖放节点
				useArrows : false, // 文件夹前显示的图标改变了不在是+号了
				border : false,
				rootVisible : true,
				containerScroll : true,// 是否含滚动条
				lines : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'managebudget/getCostItemTree.action?pid=0'
						})
			});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/getCostItemTree.action?pid='
						+ node.id;
			});
	var root = new Tree.AsyncTreeNode({
				text : ' 成本指标编码体系',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				id : '0'
			});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);
	tree.on('contextmenu', function(node, e) {
				node.select();// 很重要
				e.stopEvent();
				var menu = new Ext.menu.Menu({
							id : 'mainMenu',
							items : [new Ext.menu.Item({
												text : '添加',
												iconCls : 'add',
												handler : addStatItem
											}), new Ext.menu.Item({
												text : '删除',
												iconCls : 'delete',
												handler : delStatItem
											})]
						});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			}, this);

	function addStatItem() {
		form.getForm().reset();
		itemName.focus();
		iyRadio.enable();
		inRadio.enable();
		addOrUpdate = "add";
		// btnUdt.setText('保存');
		iyRadio.enable();
		inRadio.enable();
		form.getForm().reset();
		accountOrder.setValue(1);
		comeTypeBox.setValue(1);
		budgetFormula.disable();

	};
	// 树↑

	// form↓
	var itemId = new Ext.form.Hidden({
				id : 'itemId',
				name : 'cost.itemId'
			});

	var itemName = new Ext.form.ComboBox({
		id : "itemName",
		name : "cost.itemName",
		fieldLabel : "指标名称",
		readOnly : true,
		anchor : '92.5%',
		allowBlank : false,
		emptyText : '选择指标',
		onTriggerClick : function(e) {
			if (addOrUpdate == "add") {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '指标名称'
					}
				}
				var url = "../../../system/selItem/selectItem.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					itemName.setValue(rvo.text);
					itemCode.setValue(rvo.id);
					if (rvo.href != "null" && rvo.href != null) {
						statItemUnit.setValue(rvo.href);
					} else {
						statItemUnit.setValue("");
					}

					if (rvo.openType != "null")
						budgetFormula.disable();
				}
			}
		}
	});
	var itemCode = new Ext.form.TextField({
				name : 'cost.itemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '85%',
				allowBlank : true,
				blankText : '不可为空！'
			});

	var itemName1 = new Ext.form.TextField({
				id : "itemName1",
				name : "cost.itemName",
				allowBlank : false,
				fieldLabel : "指标名称",
				anchor : '92.5%'
			})

	var orderBy = new Ext.form.NumberField({
				name : 'cost.orderBy',
				fieldLabel : '显示顺序',
				anchor : '85%',
				allowDecimals : false,
				allowNegative : false
			});

	// 计量单位
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

	var statItemUnit = new Ext.form.ComboBox({
				fieldLabel : '计量单位',
				store : allUnitStore,
				id : 'unitId',
				name : 'unitId',
				valueField : "unitId",
				displayField : "unitName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'cost.unitCode',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					render : function() {
						this.clearInvalid();
					}
				}
			});

	// 指标类型
	var itemType = new Ext.form.ComboBox({
				fieldLabel : '指标类型',
				store : [['1', '年'], ['2', '月'], ['3', '日']],
				id : 'itemType',
				hiddenName : 'cost.itemType',
				valueField : "itemType",
				displayField : "itemName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
					}
				}
			});

	// 产生方式
	var comeTypeBox = new Ext.form.ComboBox({
				fieldLabel : '产生方式',
				store : [['1', '录入'], ['2', '采集'], ['3', '计算']],
				id : 'comeFrom',
				name : 'comeFrom',
				valueField : "comeFrom",
				displayField : "comeName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'cost.comeFrom',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
						var comevalue = comeTypeBox.getValue();
						if (comevalue == 3) {
							budgetFormula.setDisabled(false);
							btnAccountOrder.setDisabled(false);
						} else {
							itemMemo.setValue("");
							budgetFormula.setDisabled(true);
							btnAccountOrder.setDisabled(true);
						}
					}
				}
			});

	// 计算级别
	var accountOrder = new Ext.form.TextField({
				fieldLabel : '计算级别',
				id : 'accountOrder',
				readOnly : true,
				name : 'cost.accountOrder',
				anchor : '85%'
			});

	var itemMemo = new Ext.form.TextArea({
				id : 'itemMemo',
				name : 'cost.itemMemo',
				fieldLabel : '指标说明',
				readOnly : false,
				height : 70,
				anchor : '92.5%'
			});
	var itemExplain = new Ext.form.TextArea({
				id : 'itemExplain',
				name : 'cost.itemExplain',
				fieldLabel : '计算公式',
				readOnly : true,
				height : 70,
				anchor : '92.5%'
			});

	function resetEL() {
		itemName.setValue("");
		itemName1.setValue("");
		itemCode.setValue("");
		orderBy.setValue("");
		statItemUnit.setValue("");
		comeTypeBox.setValue(1);
		itemType.setValue("");
		accountOrder.setValue(1);
		itemMemo.setValue("");
		itemExplain.setValue("");
	}
	// 指标对应
	var iyRadio = new Ext.form.Radio({
		id : 'iy',
		boxLabel : '有',
		name : 'ii',
		readOnly : true,
		inputValue : 'Y',
		checked : true,
		listeners : {
			'check' : function(radio, check) {
				if (check) {
					resetEL();
					Ext.get("itemName").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("itemName1").dom.parentNode.parentNode.style.display = 'none';
					itemName1.setDisabled(true);
					itemName.setDisabled(false);
				}
			}
		}
	});

	var inRadio = new Ext.form.Radio({
		id : 'in',
		boxLabel : '无',
		readOnly : true,
		name : 'ii',
		inputValue : 'N',
		listeners : {
			'check' : function(radio, check) {
				if (check) {
					resetEL();
					Ext.get("itemName1").dom.parentNode.parentNode.style.display = '';
					itemName.el.dom.parentNode.parentNode.parentNode.style.display = 'none';
					itemName.setDisabled(true);
					itemName1.setDisabled(false);
				}
			}
		}
	});

	var isItem = {
		id : 'isItem',
		layout : 'column',
		isFormField : true,
		fieldLabel : '指标对应',
		style : 'cursor:hand',
		labelWidth : 100,
		border : false,
		items : [{
					columnWidth : .1,
					border : false,
					items : [iyRadio]
				}, {
					columnWidth : .1,
					border : false,
					items : [inRadio]
				}]
	};

	// 子页面按钮
	var budgetFormula = new Ext.Button({
		text : '计算公式',
		handler : function() {
			if (addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}

			if (itemCode.getValue() == "" || itemName.getValue() == ""
					|| accountOrder.getValue() == "") {

				Ext.MessageBox.alert('提示信息', '请选择指标项！');
				return false;
			}
			var node = tree.getSelectionModel().getSelectedNode();
			var args = new Object();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			args.itemId = itemId.getValue();
			args.itemType = itemType.getValue();
			var url;
			if (comeTypeBox.getValue() == "3") {
				url = "costFormula.jsp";
			} else
				Ext.Msg.alert("提示", "公式类型异常！")
			var o = window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
			itemExplain.setValue(o);
		}
	});

	var btnAccountOrder = new Ext.Button({
				text : '计算等级',
				id : 'orderId',
				handler : function() {
					Ext.Ajax.request({
								url : 'managebudget/geAccountCostOrder.action',
								params : {
									itemId : itemId.getValue()
								},
								method : 'post',
								waitMsg : '正在加载数据...',
								success : function(result, request) {
									var str = result.responseText;
									var o = eval("(" + str + ")");
									accountOrder.setValue(o.account);
									Ext.MessageBox.alert('提示', '操作成功!');
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '操作失败!');
								}
							});

				}
			});

	var formPositon2 = new Ext.form.FieldSet({
				title : '详细信息',
				border : true,
				layout : 'column',
				items : [{
							columnWidth : .1,
							layout : 'form',
							border : false
						}, {
							columnWidth : .15,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [budgetFormula]
						}, {
							columnWidth : .15,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [btnAccountOrder]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [isItem, itemId]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemName]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemName1]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemCode, statItemUnit, itemType]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [orderBy, comeTypeBox, accountOrder]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 100,
							items : [itemMemo]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 100,
							items : [itemExplain]
						}]
			});
	// 按钮
	var btnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addStatItem,
				minWidth : 65
			});
	/**
	 * 删除预算指标
	 */
	function delStatItem() {
		if (!confirm("指标删除后,相关的表数据也将级联删除,\n并且不能恢复,确定要删除吗?"))
			return;
		var curNode = tree.getSelectionModel().getSelectedNode();
		if (curNode.leaf) {
			Ext.Ajax.request({
						method : 'post',
						url : 'managebudget/costItemDelete.action',
						params : {
							"cost.itemId" : curNode.id,
							isItem : curNode.attributes.description
						},
						success : function(result, request) {
							var nd = tree.getSelectionModel().getSelectedNode();
							var path = nd.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);
							tree.getRootNode().select();
							addStatItem();
							// Ext.MessageBox.alert('提示信息', "操作成功");
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('错误', '请加载节点信息后删除！');
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '父节点不可删除！');
		}

	};

	var btndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : delStatItem,
				minWidth : 65
			});
	var btnUdt = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		minWidth : 65,
		handler : function() {
			var cn = tree.getSelectionModel().getSelectedNode();
			if (cn == null) {
				Ext.Msg.alert("提示", "请选择要修改的树");
				return
			}
			var isItem = tree.getSelectionModel().getSelectedNode().attributes.description;
			var itemFCode = tree.getSelectionModel().getSelectedNode().attributes.id;
			// alert(itemFCode)
			// alert(itemName.getValue())
			// alert(itemName1.getValue())
			// return;
			if (form.getForm().isValid()) {
				form.getForm().submit({
					method : 'post',
					url : 'managebudget/costItemSaveorUpdate.action',
					params : {
						method : addOrUpdate,
						"cost.PItemId" : itemFCode
					},
					success : function(form, action) {
						if (addOrUpdate == 'add') {
							var cn = tree.getSelectionModel().getSelectedNode();
							var path = cn.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, true);
							addOrUpdate = "update";
						} else {
							var cn = tree.getSelectionModel().getSelectedNode();
							cn.attributes.openType = orderBy.getValue();
						}
						var obj = eval("(" + action.response.responseText+")")
						Ext.MessageBox.alert('提示', obj.msg);
					},
					failure : function(form, action) {
						// var o = eval("(" + action.response.responseText
						// + ")");
						Ext.Msg.alert("注意", "保存失败!");
					}
				});
			}

		}
	});
	Ext.form.FormPanel.prototype.addOrUpdate = "add";
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [formPositon2]
			});
	var mytbar = new Ext.Toolbar({
				items : [btnAdd, '-', btndel, '-', btnUdt]
			})
	var panel = new Ext.Panel({
				border : false,
				collapsible : true,
				title : '预算信息维护',
				tbar : mytbar,
				items : [form]
			});
	budgetFormula.disable();
	btnAccountOrder.setDisabled(true);
	addStatItem();
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [panel]
						}, {
							title : "预算指标",
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
			});
	root.select();

});
