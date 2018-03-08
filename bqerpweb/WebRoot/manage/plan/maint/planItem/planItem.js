Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 产生方式
	// var currnetGetMethod = '';
	// 时间类型
	var currentDateType = '';
	// 数据类型
	var currentDataType = '';
	// 指标编码
	// var itemCodeValue = '';
	// 控制另外2个按钮显隐
	var otherIf = false;
	// 是否是叶子
	// var leafIf;
	// 开始是否是指标
	// var oldIfItem = 'N';
	var firstItemCode = "";
	// 判断是否修改，以控制指标编码输入控件
	var isUpdate = false;

	// 原itemcode
	var id;

	function treeClick(node, e) {
		e.stopEvent();
		// leafIf = node.leaf;
		if (node.id == '0') {
		} else {
			Ext.Msg.wait("正在加载数据,请等待...");
			Ext.Ajax.request({
						url : 'manageplan/getPlanItemModel.action',
						params : {
							id : node.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							form.addOrUpdate = "update";
							// 是否指标
							var isItem = (o.isItem == "Y" ? true : false);
							if (isItem) {
								id = o.itemCode;
								notItemFormPanel.getForm().reset();
								ItemFormPanel.getForm().reset();
								form.getForm().reset();
								itemCode.setValue(o.itemCode);
								itemName.setValue(o.itemName);
								itemNameNo.allowBlank = true;
								iyRadio.setValue(isItem);
								inRadio.setValue(!isItem);
								displayNo.setValue(o.orderBy);
								statItemUnit.setValue(o.unitCode == null
										? ''
										: o.unitCode);
								dataCollectWayBox.setValue(o.collectWay == null
										? ''
										: o.collectWay);
								computeMethodBox
										.setValue(o.computeMethod == null
												? ''
												: o.computeMethod);
								var iFaddC = (o.ifTotal == "1" ? true : false);
								ayRadio.setValue(iFaddC);
								anRadio.setValue(!iFaddC);

								if (o.collectWay == 2) {

									btnFormulaBotton.enable();
									Ext.getCmp("btnCLevel").setDisabled(false);

								} else {
									btnFormulaBotton.disable();
									Ext.getCmp("btnCLevel").setDisabled(true);
								}
							} else {
								id = o.itemCode;
								notItemFormPanel.getForm().reset();
								ItemFormPanel.getForm().reset();
								form.getForm().reset();
								iyRadio.setValue(false);
								inRadio.setValue(true);
								itemCode.allowBlank = true;
								itemName.allowBlank = true;
								itemNameNo.setValue(o.itemName);
								displayNo1.setValue(o.orderBy);
								btnFormulaBotton.disable();
							}
							Ext.Msg.hide();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
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
							dataUrl : 'manageplan/getPlanItemTreeNode.action?pid'
						})
			});

	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'manageplan/getPlanItemTreeNode.action?pid='
						+ node.id;
			});

	var root = new Tree.AsyncTreeNode({
				text : '计划指标编码体系',
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
												handler : btnAddMethord
											}), new Ext.menu.Item({
												text : '删除',
												iconCls : 'delete',
												handler : btnDelMethord
											})]
						});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			}, this);

	// 树↑
	// form↓

	var itemCode = new Ext.form.TextField({
				name : 'planItemInfo.itemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '95%',
				allowBlank : false,
				blankText : '不可为空！'
			});

	var itemName = new Ext.form.ComboBox({
		id : "planItemInfo.itemName",
		name : "planItemInfo.itemName",
		fieldLabel : '指标名称',
		readOnly : true,
		anchor : '95%',
		emptyText : '选择指标',
		allowBlank : false,
		onTriggerClick : function(e) {
			if (isUpdate) {
			} else {
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
					if (rvo.href != "null")
						statItemUnit.setValue(rvo.href);
				}
			}
		}
	});

	// 是否指标数据
	var iyRadio = new Ext.form.Radio({
				id : 'iy',
				boxLabel : '是',
				name : 'is',
				inputValue : 'Y',
				checked : true,
				listeners : {
					'check' : function(radio, check) {
						if (check) {
							formPositon1.hide();
							formPositon2.show();
						}
					}
				}
			});

	var inRadio = new Ext.form.Radio({
				id : 'in',
				boxLabel : '否',
				name : 'is',
				inputValue : 'N',
				listeners : {
					'check' : function(radio, check) {
						if (check) {
							formPositon1.show();
							formPositon2.hide();
						}
					}
				}
			});

	var isItem = {
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否指标数据',
		style : 'cursor:hand',
		anchor : '95%',
		border : false,
		items : [{
					columnWidth : .3,
					border : false,
					items : [iyRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [inRadio]
				}]
	};

	var displayNo = new Ext.form.NumberField({
				name : 'planItemInfo.orderBy',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '显示顺序',
				anchor : '95%'
			});

	var displayNo1 = new Ext.form.NumberField({
				name : 'planItemInfo.orderBy',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '显示顺序',
				anchor : '95%'
			});

	var itemNameNo = new Ext.form.TextField({
				id : 'itemNameNo',
				name : 'planItemInfo.itemName',
				xtype : 'textfield',
				fieldLabel : '节点名称',
				readOnly : false,
				anchor : '95%',
				allowBlank : false,
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
				hiddenName : 'planItemInfo.unitCode',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '95%',
				listeners : {
					render : function() {
						this.clearInvalid();
					}
				}
			});

	// 产生方式
	var dataCollectWayStore = [["手工编制", "1"], ["公式计算", "2"]];

	var dataCollectWayBox = new Ext.form.ComboBox({
				fieldLabel : '产生方式',
				store : new Ext.data.SimpleStore({
							fields : ['key', 'value'],
							data : dataCollectWayStore
						}),
				valueField : 'value',
				displayField : 'key',
				id : 'dataCollectWay',
				name : 'dataCollectWay',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'planItemInfo.collectWay',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '95%',
				value : '1',
				listeners : {
					select : function(index) {
						if (index.value == '2') {
							btnFormulaBotton.enable();
							Ext.getCmp("btnCLevel").setDisabled(false);
						} else {
							btnFormulaBotton.disable();
							Ext.getCmp("btnCLevel").setDisabled(true);
						}
					}
				}
			});

	var calLevel = new Ext.form.TextField({
				name : 'planItemInfo.accountOrder',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '计算等级',
				anchor : '95%',
				value : '1'
			});

	// 计算方法
	var computeMethodStore = [["求平均", "1"], ["求合计", "2"]];

	var computeMethodBox = new Ext.form.ComboBox({
				fieldLabel : '计算方法',
				store : new Ext.data.SimpleStore({
							fields : ['key', 'value'],
							data : computeMethodStore
						}),
				id : 'computeMethod',
				name : 'computeMethod',
				valueField : "value",
				displayField : "key",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'planItemInfo.computeMethod',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '95%',
				value : '1'
			});

	// 是否累计
	var ayRadio = new Ext.form.Radio({
				id : 'ay',
				boxLabel : '是',
				name : 'as',
				inputValue : '1',
				checked : true
			});

	var anRadio = new Ext.form.Radio({
				id : 'an',
				boxLabel : '否',
				name : 'as',
				inputValue : '0'
			});

	var ifAdd = {
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否累计',
		style : 'cursor:hand',
		anchor : '95%',
		border : false,
		items : [{
					columnWidth : .3,
					border : false,
					items : [ayRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [anRadio]
				}]
	};

	var formPosition = new Ext.form.FieldSet({
				bodyStyle : "padding:10px 0px 0",
				title : '统计指标基础信息',
				border : true,
				height : '100%',
				layout : 'column',
				items : [{
							columnWidth : 1,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							border : false,
							labelWidth : 60,
							items : [isItem]
						}]
			});

	var formPositon1 = new Ext.form.FieldSet({
				border : true,
				height : '100%',
				title : '指标体系',
				layout : 'column',
				items : [{
							columnWidth : .5,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							border : false,
							labelWidth : 100,
							items : [itemNameNo]
						}, {
							columnWidth : .5,
							bodyStyle : "padding:10px 0px 0",
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [displayNo1]
						}]
			});

	var formPositon2 = new Ext.form.FieldSet({
				bodyStyle : "padding:10px 0px 0",
				title : '统计指标',
				border : true,
				height : '100%',
				layout : 'column',
				items : [{
							columnWidth : .5,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							border : false,
							labelWidth : 60,
							items : [itemCode]
						}, {
							columnWidth : .5,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							border : false,
							labelWidth : 60,
							items : [itemName]
						}, {
							columnWidth : .5,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							border : false,
							labelWidth : 60,
							items : [displayNo]
						}, {
							columnWidth : .5,
							bodyStyle : "padding:10px 0px 0",
							layout : 'form',
							labelWidth : 60,
							border : false,
							items : [statItemUnit]
						}, {
							columnWidth : .5,
							bodyStyle : "padding:10px 0px 0",
							layout : 'form',
							labelWidth : 60,
							border : false,
							items : [dataCollectWayBox]
						}, {
							columnWidth : .5,
							bodyStyle : "padding:10px 0px 0",
							layout : 'form',
							labelWidth : 60,
							border : false,
							items : [computeMethodBox]
						}, {
							columnWidth : .5,
							bodyStyle : "padding:10px 0px 0",
							layout : 'form',
							labelWidth : 60,
							border : false,
							items : [calLevel]
						}, {
							columnWidth : 1,
							layout : 'form',
							bodyStyle : "padding:10px 0px 0",
							labelWidth : 60,
							border : false,
							items : [ifAdd]
						}]
			});
	// 按钮
	var btnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : btnAddMethord
			});
	/**
	 * 删除指标
	 */
	function delStatItem() {
		var curNode = tree.getSelectionModel().getSelectedNode();
		//		
		if (curNode.leaf)
			form.getForm().submit({
						method : 'post',
						url : 'manageplan/deletePlanItemModel.action',
						params : {
							id : curNode.id
						},
						success : function(form, action) {
							otherIf = false;
							var o = eval('(' + action.response.responseText
									+ ')');
							var nd = tree.getSelectionModel().getSelectedNode();
							var path = nd.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);

							tree.getRootNode().select();
							btnAddMethord();
							Ext.MessageBox.alert('提示信息', o.msg);
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('错误', '请加载节点信息后删除！');
						}
					})
		else
			Ext.MessageBox.alert('提示信息', '父节点不可删除！');

	};

	Ext.form.FormPanel.prototype.addOrUpdate = "add";

	function btnAddMethord() {
		notItemFormPanel.getForm().reset();
		ItemFormPanel.getForm().reset();
		form.getForm().reset();
		itemName.focus();
		iyRadio.enable();
		inRadio.enable();
		form.addOrUpdate = "add";
		computeMethodBox.setValue(1);
		dataCollectWayBox.setValue(1);
	}

	function btnDelMethord() {
		delStatItem()

	}

	function btnSavMethord() {
		var temp = tree.getSelectionModel().getSelectedNode();
		if (temp == null) {
			Ext.Msg.alert("提示", "请选择要修改指标！");
			return false;
		}
		var isItem = temp.attributes.description;
		if (form.addOrUpdate == "add") {
			isItem = Ext.get("iy").dom.checked ? "Y" : "N";
		}
		// 是指标
		if (isItem == "Y") {
			if (form.getForm().isValid()) {
				var idadd = (Ext.get("ay").dom.checked ? "1" : "0");
				ItemFormPanel.getForm().submit({
					method : 'post',
					url : 'manageplan/savePlanItemModel.action',
					params : {
						'planItemInfo.isItem' : 'Y',
						method : form.addOrUpdate,
						id : id,
						'planItemInfo.FItemCode' : tree.getSelectionModel()
								.getSelectedNode().id,
						'planItemInfo.ifTotal' : idadd,
						firstItemCode : firstItemCode
					},
					success : function(ItemFormPanel, action) {
						if (form.addOrUpdate == 'add') {
							var cn = tree.getSelectionModel().getSelectedNode();
							var path = cn.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);
							form.addOrUpdate = "update";
						} else {
							var cn = tree.getSelectionModel().getSelectedNode();
							// modify by fyyang 090824
							// cn.attributes.code = dataTimeTypeBox.getValue();
//							cn.attributes.openType = dataCollectWayBox
//									.getValue();
							tree.getRootNode().reload();
						}
					},
					failure : function(ItemFormPanel, action) {
						var o = eval('(' + action.response.responseText + ')');
						Ext.MessageBox.alert('错误', o.msg);
					}
				});
			}
		}
		// 非指标
		else {
			if (notItemFormPanel.getForm().isValid()) {
				notItemFormPanel.getForm().submit({
					method : 'post',
					url : 'manageplan/savePlanItemModel.action',
					params : {
						'planItemInfo.isItem' : 'N',
						method : form.addOrUpdate,
						id : id,
						'planItemInfo.FItemCode' : tree.getSelectionModel()
								.getSelectedNode().id
					},
					success : function(form, action) {
						if (form.addOrUpdate == 'add') {
							var cn = tree.getSelectionModel().getSelectedNode();
							var path = cn.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);
							form.addOrUpdate = "update";
						} else {
							var cn = tree.getSelectionModel().getSelectedNode();
							// modify by fyyang 090824
							// cn.attributes.code = dataTimeTypeBox.getValue();
//							cn.attributes.openType = dataCollectWayBox
//									.getValue();
							tree.getRootNode().reload();
						}
					},
					failure : function(form, action) {
						var o = eval('(' + action.response.responseText + ')');
						Ext.MessageBox.alert('提示', o.msg);
					}
				});
			}
		}
	}

	function btnFormulaMethord() {
		var args = {
			itemCode : itemCode.getValue()

		}
		var url = "formula/formula.jsp";
		var rvo = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:400px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		// if (typeof(rvo) != "undefined") {
		// if (rvo.itemCode == ArgItemCode) {
		// Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");
		//
		// } else {
		// ylZbbm.setValue(rvo.itemCode);
		// ylZbbmName.setValue(rvo.itemName);
		// }
		// }

	}

	function btnCLevelMethord() {
		Ext.Ajax.request({
					url : 'manageplan/getAccountOrder.action',
					params : {
						itemCode : itemCode.getValue()
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = result.responseText;
						calLevel.setValue(o);

					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});

	}

	var btnFormulaBotton = new Ext.Button({
				id : 'btnFormula',
				text : '公式',
				disabled : true,
				iconCls : 'add',
				handler : btnFormulaMethord
			});

	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:10px 10px 0",
				labelAlign : 'right',
				region : 'center',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				tbar : [{
							id : 'btnAdd',
							text : '增加',
							iconCls : 'add',
							handler : btnAddMethord
						}, '-', {
							id : 'btnDel',
							text : '删除',
							iconCls : 'delete',
							handler : btnDelMethord
						}, '-', {
							id : 'btnSave',
							text : '保存',
							iconCls : 'save',
							handler : btnSavMethord
						}, '-', btnFormulaBotton, '-', {
							id : 'btnCLevel',
							text : '计算等级',
							iconCls : 'add',
							disabled : true,
							handler : btnCLevelMethord
						}],
				items : [formPosition]
			});

	var ItemFormPanel = new Ext.FormPanel({
				bodyStyle : "padding:0px 10px 0",
				labelAlign : 'right',
				region : 'center',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				items : [formPositon2]
			});

	var notItemFormPanel = new Ext.FormPanel({
				bodyStyle : "padding:0px 10px 0",
				labelAlign : 'right',
				region : 'center',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				items : [formPositon1]
			});

	var panel = new Ext.Panel({
				border : false,
				collapsible : true,
				title : '指标信息维护',
				items : [form, ItemFormPanel, notItemFormPanel]
			});

	// form↑
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
							title : "统计指标",
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
	btnFormulaBotton.disable();
});
