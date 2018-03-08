Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	function treeClick(node, e) {
		e.stopEvent();
		var temp = node.attributes.id;
		if (temp == '00') {
			panel.hide();
			formPositon2.hide();
			formPositon1.hide();
		}
		if (temp == '6666666666' || temp == '7777777777'
				|| temp == '8888888888' || temp == '9999999999') {
			panel.setTitle("当前选择指标:[" + node.text + "]");
			form.getForm().reset();
			notItemFormPanel.getForm().reset();
			form.addOrUpdate = "add";
			iyRadio.enable();
			inRadio.enable();
			panel.show();
			formPositon2.show();
			formPositon1.show();
		} else {
			panel.setTitle("当前选择指标:[" + node.text + "]");
			var isItem = node.attributes.description;
			form.addOrUpdate = "update";
			var isItem = (isItem == "Y" ? true : false);
			iyRadio.setValue(isItem);
			inRadio.setValue(!isItem);
			iyRadio.disable();
			inRadio.disable();
			if (isItem) {
				Ext.Msg.wait("正在加载数据,请等待...");
				Ext.Ajax.request({
							url : 'managebudget/getModelItemInfo.action',
							params : {
								id : node.id
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var str = result.responseText;
								var o = eval("(" + str + ")");
								formPositon2.show();
								itemName.setValue(o.modelItemName);
								itemCode.setValue(o.modelItemCode);
								itemId.setValue(o.modelItemId);
								// 计量单位
								if (o.unitId != null)
									statItemUnit.setValue(o.unitId);
								else
									statItemUnit.clearValue();
								// 预算编制
								if (o.comeFrom != null)
									comeTypeBox.setValue(o.comeFrom);
								else
									comeTypeBox.clearValue();
								// 显示顺序
								if (o.displayNo != null)
									displayNo1.setValue(o.displayNo)
								else
									displayNo1.setValue("");
								// 预算公式
								if (o.comeFrom == 2) {
									modelFormula.setDisabled(false);
								} else {
									modelFormula.setDisabled(true);
								}
								// 公式
								if (o.modelItemExplain != null) {
									modelItemExplain
											.setValue(o.modelItemExplain);
								} else {
									modelItemExplain.setValue("");
								}
								Ext.Msg.hide();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败!');
							}
						});
			} else {
				modelItemName.setValue(node.attributes.text);
				modelItemId.setValue(node.id);
				if (node.attributes.openType != null) {
					displayNo.setValue(node.attributes.openType);
				} else {
					displayNo.setValue("");
				}
			}

		}
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
							dataUrl : 'managebudget/findModelTreeList.action?pid=00'
						})
			});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/findModelTreeList.action?pid='
						+ node.id;
			});

	var root = new Tree.AsyncTreeNode({
				text : '预算指标编码体系',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				id : '00'
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
		notItemFormPanel.getForm().reset();
		form.getForm().reset();
		itemName.focus();
		iyRadio.enable();
		inRadio.enable();
		form.addOrUpdate = "add";
		// btnUdt.setText('保存');
		iyRadio.enable();
		inRadio.enable();
		form.getForm().reset();
		comeTypeBox.setValue(1);
		modelFormula.disable();

	};
	// 树↑

	// form↓

	// 显示顺序
	var displayNo = new Ext.form.NumberField({
				id : 'displayNo',
				name : 'cbModel.displayNo',
				fieldLabel : '显示顺序',
				anchor : '92.5%'
			})

	var displayNo1 = new Ext.form.NumberField({
				id : 'displayNo1',
				name : 'cbModel.displayNo',
				fieldLabel : '显示顺序',
				anchor : '92.5%'
			})

	var itemId = new Ext.form.Hidden({
				id : 'cbModel.modelItemId',
				name : 'cbModel.modelItemId'
			});

	var itemName = new Ext.form.ComboBox({
		id : "cbModel.itemName",
		name : "cbModel.modelItemName",
		fieldLabel : '指标名称',
		 readOnly : true,
		anchor : '92.5%',
		allowBlank : false,
		emptyText : '选择指标',
		onTriggerClick : function(e) {
			if (form.addOrUpdate == "add") {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '指标名称'
					}
				}
				var url = "../../../../manage/budget/maint/budgetItem/budgetItemTree.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					itemName.setValue(rvo.itemName);
					itemCode.setValue(rvo.itemCode);
					statItemUnit.setValue(rvo.unitId);
					modelFormula.disable();
				}
			}
		}
	});
	var itemCode = new Ext.form.TextField({
				name : 'cbModel.modelItemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '92.5%',
				allowBlank : true,
				blankText : '不可为空！'
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
				hiddenName : 'cbModel.unitId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '92.5%',
				listeners : {
					render : function() {
						this.clearInvalid();
					}
				}
			});

	// 产生方式
	var comeTypeBox = new Ext.form.ComboBox({
				fieldLabel : '产生方式',
				store : [['1', '手工编制'], ['2', '公式计算']],
				id : 'comeFrom',
				name : 'comeFrom',
				valueField : "comeFrom",
				displayField : "comeName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'cbModel.comeFrom',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '92.5%',
				listeners : {
					select : function(index, scrollIntoView) {
						var comevalue = comeTypeBox.getValue();
						if (comevalue == 2) {
							modelFormula.setDisabled(false);
						} else {
							modelFormula.setDisabled(true);
						}
					}
				}
			});

	var modelItemExplain = new Ext.form.TextArea({
				id : 'modelItemExplain',
				xtype : 'textarea',
				name : 'cbModel.modelItemExplain',
				fieldLabel : '实际公式',
				readOnly : true,
				height : 90,
				anchor : '92.5%'
			});

	// 预算对应
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
					Ext.get("modelItemName").dom.parentNode.parentNode.style.display = 'none';
					Ext.get("displayNo").dom.parentNode.parentNode.style.display = 'none';
					formPositon2.show();
					itemName.allowBlank = false;
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
					formPositon2.hide();
					Ext.get("modelItemName").dom.parentNode.parentNode.style.display = '';
					Ext.get("displayNo").dom.parentNode.parentNode.style.display = '';
					itemName.allowBlank = true;
					itemName.focus();
				}
			}
		}
	});
	var modelItemName = new Ext.form.TextField({
				id : 'modelItemName',
				name : 'cbModel.modelItemName',
				allowBlank : false,
				fieldLabel : '节点名称',
				anchor : '92.5%'
			})
	var modelItemId = new Ext.form.Hidden({
				id : 'modelItemId',
				name : 'cbModel.modelItemId'
			})

	var isItem = {
		id : 'isItem',
		layout : 'column',
		isFormField : true,
		fieldLabel : '预算对应',
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

	var formPositon1 = new Ext.form.FieldSet({
				border : true,
				title : '预算对应',
				autoHeight : true,
				layout : 'column',
				items : [{
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [isItem]
						}, {

							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [modelItemName, displayNo, modelItemId]

						}]
			});

	// 子页面按钮

	var modelFormula = new Ext.Button({
		text : '公式设置',
		handler : function() {
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}

			var node = tree.getSelectionModel().getSelectedNode();
			var args = new Object();
			args.modelItemId = itemId.getValue();
			var url;
			if (comeTypeBox.getValue() == "2") {
				url = "modelFormula.jsp";
			} else
				Ext.Msg.alert("提示", "公式类型异常！")
			var o = window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
			modelItemExplain.setValue(o);
		}

	});

	var formPositon2 = new Ext.form.FieldSet({
				title : '详细信息',
				border : true,
				height : '100%',
				layout : 'column',
				items : [{
							columnWidth : .1,
							layout : 'form',
							border : false
						}, {
							columnWidth : .75,
							bodyStyle : "padding:0px 0px 0px 150px",
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [modelFormula]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemId, itemName]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemCode]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [statItemUnit]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [comeTypeBox]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [displayNo1]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 100,
							items : [modelItemExplain]
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
						url : 'managebudget/delModelItem.action',
						params : {
							id : curNode.id
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
			var modelType = tree.getSelectionModel().getSelectedNode().attributes.code;
			var daddyItemId = tree.getSelectionModel().getSelectedNode().attributes.id;
			if (form.addOrUpdate == "add") {
				isItem = Ext.get("iy").dom.checked ? "Y" : "N";
			}
			// 是指标
			if (isItem == "Y") {
				if (form.getForm().isValid()) {
					form.getForm().submit({
						method : 'post',
						url : 'managebudget/saveOrupdateModelItem.action',
						params : {
							isItem : 'Y',
							method : form.addOrUpdate,
							'cbModel.modelType' : modelType,
							DaddyItemId : daddyItemId,
							itemName : itemName.getValue()
						},
						success : function(form1, action) {
							if (form.addOrUpdate == 'add') {
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								var path = cn.getPath();
								tree.getRootNode().reload();
								tree.expandPath(path, false);
								form.addOrUpdate = "update";
							} else {
								// var cn =
								// tree.getSelectionModel().getSelectedNode();
								// var path = cn.getPath();
								tree.getRootNode().reload();
								// tree.expandPath(path, false);
								// form.addOrUpdate = "update";
							}
							Ext.MessageBox.alert('提示', "操作成功");
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示', "操作失败");
						}
					});
				}
			}
			// 非指标
			else {
				if (notItemFormPanel.getForm().isValid()) {
					notItemFormPanel.getForm().submit({
						method : 'post',
						url : 'managebudget/saveOrupdateModelItem.action',
						params : {
							isItem : 'N',
							method : form.addOrUpdate,
							'cbModel.modelType' : modelType,
							DaddyItemId : daddyItemId
						},
						success : function(form, action) {
							if (form.addOrUpdate == 'add') {
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								var path = cn.getPath();
								tree.getRootNode().reload();
								tree.expandPath(path, false);
								form.addOrUpdate = "update";
							} else {
								// var cn =
								// tree.getSelectionModel().getSelectedNode();
								// var path = cn.getPath();
								tree.getRootNode().reload();
								// tree.expandPath(path, false);
								// form.addOrUpdate = "update";
							}
							Ext.MessageBox.alert('提示', "操作成功");
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示', "操作失败！");
						}
					});
				}
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
	var notItemFormPanel = new Ext.FormPanel({
				bodyStyle : "padding:5px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				items : [formPositon1]
			});

	var mytbar = new Ext.Toolbar({
				items : [btnAdd, '-', btndel, '-', btnUdt]
			})
	var panel = new Ext.Panel({
				border : false,
				collapsible : true,
				title : '预算信息维护',
				tbar : mytbar,
				items : [notItemFormPanel, form]
			});
	modelFormula.disable();
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
							width : 300,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [tree]
						}]
			});
	root.select();

});
