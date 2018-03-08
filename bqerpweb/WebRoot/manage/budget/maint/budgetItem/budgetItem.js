Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var itemFCode;
	var NoleafAddOrUpdate='update';
	function treeClick(node, e) {
		e.stopEvent();
		panel.setTitle("当前选择指标:[" + node.text + "]");

		if (node.attributes.code == '000') {
			form.getForm().reset();
			form.addOrUpdate = "add";
			iyRadio.enable();
			inRadio.enable();
		} else {
			var isItem = node.attributes.description;
			form.addOrUpdate = "update";
			NoleafAddOrUpdate = "update";
			var isItem = (isItem == "Y" ? true : false);
			iyRadio.setValue(isItem);
			inRadio.setValue(!isItem);
			iyRadio.disable();
			inRadio.disable();
			if (isItem) {
				Ext.Msg.wait("正在加载数据,请等待...");
				Ext.Ajax.request({
							url : 'managebudget/getbudgetItemInfo.action',
							params : {
								id : node.id
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var str = result.responseText;
								var o = eval("("
										+ str.substring(1, str.length - 1)
										+ ")");
										
								formPositon2.show();
								itemName.setValue(o.zbbmtxName);
								itemCode.setValue(o.item.itemCode);
								retrieveCode.setValue(o.item.retrieveCode);
//								// 是否累计
//								var isTotal = (o.item.ifTotal == "Y"
//										? true
//										: false);
//								ryRadio.setValue(isTotal);
//								rnRadio.setValue(!isTotal);
								
								itemId.setValue(o.item.itemId);
								// 计量单位
								if (o.item.unitCode != null)
									statItemUnit.setValue(o.item.unitCode);
								else
									statItemUnit.clearValue();
								// 预算编制
								if (o.item.comeFrom != null)
									comeTypeBox.setValue(o.item.comeFrom);
								else
									comeTypeBox.clearValue();
									
								// 指标类别
								if (o.item.itemType != null)
									itemType.setValue(o.item.itemType);
								else
									itemType.clearValue();
									
								// 实际来源
								if (o.item.factFrom != null)
									factWayBox.setValue(o.item.factFrom);
								else
									factWayBox.clearValue();
								
								// 一流值、挖潜值
								if (o.item.firstclassValue != null)
									yiliuzhi.setValue(o.item.firstclassValue);
								else
									yiliuzhi.setValue("");//clearValue();
								
								//创造值、挖潜值
								if (o.item.createValue != null)
									chuangzaozhi.setValue(o.item.createValue);
								else
									chuangzaozhi.setValue("");
								//费用管理部门ID
								if (o.item.centerId != null)
									centerId.setValue(o.item.centerId);
								else
									centerId.setValue("");
								//费用管理部门名称
								if (o.deptName != null)
									deptName.setValue(o.deptName);
								else
									deptName.setValue("");
								//排序
								if (o.item.orderBy != null)
									orderBy.setValue(o.item.orderBy);
								else
									orderBy.setValue("");
								// 计算方法
//								if (o.item.computeMethod)
//									computeMethodBox
//											.setValue(o.item.computeMethod);
//								else
//									computeMethodBox.clearValue();
									
								// 指标性质
//								if (o.item.dataAttribute != null)
//									dataAttributeBox
//											.setValue(o.item.dataAttribute);
//								else
//									dataAttributeBox.clearValue();
								// 指标分解
//								if (o.item.ifDispart != null)
//									itemDispart.setValue(o.item.ifDispart);
//								else
//									itemDispart.clearValue();

//								// 借方贷方
//								if (o.debitCredit != null)
//
//									debitCredit.setValue(o.debitCredit);
//								else
//									debitCredit.clearValue();
								// 财务对应
//								if (o.finaceName != null) {
//									financeItem.setValue(o.finaceName)
//									financeItem.setDisabled(false)
//								} else {
//									financeItem.clearValue();
//								}
//								if (o.finaceId != null)
//									hidfinanceItem.setValue(o.finaceId)
//								else
//									hidfinanceItem.setValue("");
//									
								// 预算公式
								if (o.item.comeFrom == 2) {
									itemExplain.setValue(o.item.itemExplain);
									budgetFormula.setDisabled(false);
									btnAccountOrder.setDisabled(false);
								} else {
									itemExplain.setValue("");
									budgetFormula.setDisabled(true);
									btnAccountOrder.setDisabled(true)
								}
								// 实际公式
								if (o.item.factFrom == 2) {
									factExplain.setValue(o.item.factExplain);
									factFormula.setDisabled(false);
									btnAccountOrder.setDisabled(false)
								} else {
									factExplain.setValue("");
									factFormula.setDisabled(true);
									btnAccountOrder.setDisabled(true);
								}
								// 费用使用部门
								//add by ypan 20100907
								if (o.item.itemId !=null) {
									//factExplain.setValue(o.item.factExplain);
									centerUse.setDisabled(false);
									//btnAccountOrder.setDisabled(false)
								} else {
									//factExplain.setValue("");
									centerUse.setDisabled(true);
									//btnAccountOrder.setDisabled(true);
								}
								// 指标类别
								if (o.item.itemType == 2) {
									//debitCredit.setDisabled(false);
									//financeItem.setDisabled(false);
								} else {
									//debitCredit.setDisabled(true);
									//financeItem.setDisabled(true);
								}
								if(o.item.comeFrom == 2 || o.item.factFrom == 2){
								btnAccountOrder.setDisabled(false);
								}
								Ext.Msg.hide();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败!');
							}
						});
			} else {
				systemName.setValue(node.attributes.text);
				systemId.setValue(node.id);
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
							dataUrl : 'managebudget/findBudgetTree.action?pid=000'
						})
			});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/findBudgetTree.action?pid='
						+ node.attributes.code;
			});
	var root = new Tree.AsyncTreeNode({
				text : '预算指标编码体系',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				code : '000'
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
		NoleafAddOrUpdate = "add";
		// btnUdt.setText('保存');
		iyRadio.enable();
		inRadio.enable();
		form.getForm().reset();
		itemType.setValue(1);
		computeMethodBox.setValue(1);
		factWayBox.setValue(1);
		dataAttributeBox.setValue(1);
		comeTypeBox.setValue(1);
		itemDispart.setValue(1);
		budgetFormula.disable();
		factFormula.disable();
		//debitCredit.disable();

	};
	// 树↑
	// form↓
	var itemId = new Ext.form.Hidden({
				id : 'bubgetItem.itemId',
				name : 'bubgetItem.itemId'
			});

	var itemName = new Ext.form.ComboBox({
		id : "bubgetItem.itemName",
		name : "bubgetItem.itemName",
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
					if (rvo.openType != "null")
						retrieveCode.setValue(rvo.openType);
					factFormula.disable();
					budgetFormula.disable();
				}
			}
		}

	});
	var itemCode = new Ext.form.TextField({
				name : 'bubgetItem.itemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '85%',
				allowBlank : true,
				blankText : '不可为空！'
			});
	var yiliuzhi = new Ext.form.NumberField({
				name : 'bubgetItem.firstclassValue',
				//xtype : 'textfield',
				fieldLabel : '一流值/挖潜值',
				//readOnly : true,
				anchor : '85%',
				//allowBlank : true,
				blankText : '不可为空！'
			});
	var orderBy = new Ext.form.NumberField({
				name : 'bubgetItem.orderBy',
				//xtype : 'textfield',
				fieldLabel : '排序',
				//readOnly : true,
				anchor : '85%'
				//,allowBlank : true,
				//blankText : '不可为空！'
			});
	var chuangzaozhi = new Ext.form.NumberField({
				name : 'bubgetItem.createValue',
				//xtype : 'textfield',
				fieldLabel : '创造值/挖潜值',
				//readOnly : true,
				anchor : '85%',
				//allowBlank : true,
				blankText : '不可为空！'
			});

	var retrieveCode = new Ext.form.TextField({
				name : 'bubgetItem.retrieveCode',
				xtype : 'textfield',
				fieldLabel : '检索码',
				readOnly : true,
				anchor : '85%',
				maxLength : 8,
				maxLengthText : '最多输入8个字符！',
				vtypeText : '只能输入字母！'
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
				hiddenName : 'bubgetItem.unitCode',
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

	// 指标类别
	var itemType = new Ext.form.ComboBox({
				fieldLabel : '指标类别',
				store : [['1', '生产指标'], ['2', '财务指标'], ['3', '预测指标'],
						['4', '其它指标']],
				id : 'itemType',
				name : 'bubgetItem.itemType',
				hiddenName : 'bubgetItem.itemType',
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
						var getvalue = index.value;
						if (getvalue == "2") {
							//debitCredit.setDisabled(false);
							//financeItem.setDisabled(false);
						} else {
							//debitCredit.setDisabled(true);
							//financeItem.setDisabled(true);
							//financeItem.clearValue();
							//debitCredit.clearValue();
						}
					}
				}
			});

//	// 借方贷方
//	var debitCredit = new Ext.form.ComboBox({
//				fieldLabel : '借方贷方',
//				store : [['1', '借方'], ['2', '贷方']],
//				id : 'debitCredit1',
//				name : 'debitCredit1',
//				valueField : "key",
//				displayField : "value",
//				mode : 'local',
//				typeAhead : true,
//				forceSelection : true,
//				hiddenName : 'debitCredit',
//				editable : false,
//				triggerAction : 'all',
//				selectOnFocus : true,
//				allowBlank : true,
//				emptyText : '请选择',
//				disabled : true,
//				anchor : '85%'
//			});

	// 计算方法
	var computeMethodBox = new Ext.form.ComboBox({
				fieldLabel : '计算方法',
				store : [['1', '求平均'], ['2', '求合计'], ['3', '求最大'], ['4', '求最小']],
				id : 'computeMethod',
				name : 'computeMethod',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				hidden : true,
				hideLabel : true,
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'bubgetItem.computeMethod',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	// 指标分解
	var itemDispart = new Ext.form.ComboBox({
				fieldLabel : '指标分解',
				store : [['1', '部门级'], ['2', '公司级']],
				id : 'ifDispart',
				name : 'ifDispart',
				valueField : 'key',
				displayField : 'value',
				mode : 'local',
				typeAhead : true,
				triggerAction : 'all',
				forceSelection : true,
				hiddenName : 'bubgetItem.ifDispart',
				editable : false,
				allowBlank : true,
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			})
	// 是否累计
	var ryRadio = new Ext.form.Radio({
				id : 'ry',
				boxLabel : '是',
				name : 'rs',
				inputValue : '1',
				checked : true
			});

	var rnRadio = new Ext.form.Radio({
				id : 'rn',
				boxLabel : '否',
				name : 'rs',
				inputValue : '0'
			});

	var ifAdd = {
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否累计',
		style : 'cursor:hand',
		anchor : '80%',
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : [ryRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [rnRadio]
				}]
	};

	// 预算编制
	var comeTypeBox = new Ext.form.ComboBox({
				fieldLabel : '预算编制',
				store : [['1', '手工编制'], ['2', '公式计算']],
				id : 'comeFrom',
				name : 'comeFrom',
				valueField : "comeFrom",
				displayField : "comeName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'bubgetItem.comeFrom',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
						var comevalue = comeTypeBox.getValue();
						if (comevalue == 2) {
							budgetFormula.setDisabled(false);
							btnAccountOrder.setDisabled(false);
						} else {
							itemExplain.setValue("");
							budgetFormula.setDisabled(true);
							btnAccountOrder.setDisabled(true);
						}
					}
				}
			});

	// 实际来源
	var factWayBox = new Ext.form.ComboBox({
				fieldLabel : '实际来源',
				store : [['1', '数据采集'], ['2', '公式计算'], ['3', '手工录入']],
				id : 'factFrom',
				name : 'factFrom',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				hiddenName : 'bubgetItem.factFrom',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
						var getvalue = index.value;
						if (getvalue == 2) {
							factFormula.setDisabled(false);
							btnAccountOrder.setDisabled(false);
						} else {
							factExplain.setValue("");
							factFormula.setDisabled(true);
							btnAccountOrder.setDisabled(true);
						}
					}
				}
			});

	// 指标性质
	var dataAttributeBox = new Ext.form.ComboBox({
				fieldLabel : '指标性质',
				store : [['1', '数量指标'], ['2', '质量指标']],
				id : 'dataAttribute',
				name : 'dataAttribute',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				hidden : true,
				hideLabel : true,
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'bubgetItem.dataAttribute',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择'
//				,
//				anchor : '85%'
			});

//	// 财务对应
//	function clickbyfinance() {
//		// if (form.addOrUpdate == "add") {
//		var args = {
//			selectModel : 'single',
//			rootNode : {
//				id : "0",
//				text : '财务科目'
//			}
//		}
//		var url = "financeItem.jsp";
//		var rvo = window
//				.showModalDialog(
//						url,
//						args,
//						'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//		if (typeof(rvo) != "undefined") {
//			financeItem.setValue(rvo.finaceName)
//			hidfinanceItem.setValue(rvo.finaceId)
//		}
//		// }
//	}
//
//	var financeItem = new Ext.form.ComboBox({
//				id : "financeItem",
//				name : "financeItem",
//				fieldLabel : '财务对应',
//				readOnly : true,
//				anchor : '85%',
//				disabled : true,
//				emptyText : '选择财务科目',
//				mode : 'local',
//				triggerAction : 'all',
//				forceSelection : true,
//				editable : false,
//				allowBlank : false,
//				selectOnFocus : true,
//				onTriggerClick : function(e) {
//					if (!this.disabled) {
//						clickbyfinance()
//					}
//					this.blur();
//				}
//			});

//	var hidfinanceItem = new Ext.form.Hidden({
//				id : 'hidfinanceItem',
//				name : 'financeItemId'
//			})
	//费用管理部门Id
	var centerId = new Ext.form.Hidden({
				id : 'centerId',
				name : 'bubgetItem.centerId'
			});
	//费用管理部门名称
	var deptName = new Ext.form.TextField({
		fieldLabel : "费用管理部门",
		emptyText : '请选择...',
		anchor : '85%',
		//width:'91.2%',
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '灞桥电厂'
					}
				}
				var url = "../../../../comm/jsp/hr/dept/dept.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					centerId.setValue(rvo.ids);
					deptName.setValue(rvo.names);
				}
				this.blur();
			}
		},
		readOnly : true,
		allowBlank : true
	});
	
	var itemExplain = new Ext.form.TextArea({
				id : 'itemExplain',
				xtype : 'textarea',
				name : 'bubgetItem.itemExplain',
				fieldLabel : '预算公式',
				readOnly : true,
				height : 70,
				anchor : '92.5%'
			});
	var factExplain = new Ext.form.TextArea({
				id : 'factExplain',
				xtype : 'textarea',
				name : 'bubgetItem.factExplain',
				fieldLabel : '实际公式',
				readOnly : true,
				height : 70,
				anchor : '92.5%'
			});

	// 有无数据
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
					Ext.get("systemName").dom.parentNode.parentNode.style.display = 'none';
					// Ext.get("displayNo").dom.parentNode.parentNode.style.display
					// = 'none';
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
					Ext.get("systemName").dom.parentNode.parentNode.style.display = '';
					itemName.allowBlank = true;
					itemName.focus();
				}
			}
		}
	});
	var systemName = new Ext.form.TextField({
				id : 'systemName',
				name : 'zbbmtxName',
				allowBlank : false,
				fieldLabel : '体系名称'
			})
	var systemId = new Ext.form.Hidden({
				id : 'systemId',
				name : 'systemId'
			})

	var isItem = {
		id : 'isItem',
		layout : 'column',
		isFormField : true,
		fieldLabel : '有无数据',
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
				title : '指标体系',
				autoHeight : true,
				layout : 'column',
				items : [{
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [systemName, systemId]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [isItem]
						}]
			});

	// 子页面按钮

	var budgetFormula = new Ext.Button({
		text : '预算公式',
		handler : function() {
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}

			if (itemCode.getValue() == "" || itemName.getValue() == ""
					|| factWayBox.getValue() == "") {
				Ext.MessageBox.alert('提示信息', '请选择指标项！');
				return false;
			}
			var node = tree.getSelectionModel().getSelectedNode();
			var args = new Object();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			args.itemId = itemId.getValue();
			var url;
			if (comeTypeBox.getValue() == "2") {
				url = "../../../../manage/budget/maint/formula/budgetFormula.jsp";
			} else
				Ext.Msg.alert("提示", "公式类型异常！")
			var o = window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
			var beforechange=itemExplain.getValue();
			if(o!=' [] '){
				if (beforechange!=o) {
					itemExplain.setValue(o);				
					accountOrder();
				}
			}
		}
	});

	var factFormula = new Ext.Button({
		text : '实际公式',
		handler : function() {
			var node = tree.getSelectionModel().getSelectedNode();
			var args = new Object();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			args.itemId = itemId.getValue();
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}
			if (factWayBox.getValue() == "2") {
				var url = "../../../../manage/budget/maint/formula/factFormula.jsp";
			} else
				Ext.Msg.alert("提示", "公式类型异常！");
			var o = window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
			var beforechange=factExplain.getValue();
			if(o!=' [] '){
				if (beforechange!=o) {
					factExplain.setValue(o);
					accountOrder();
				}
			}
		}
	});
	function accountOrder(){
		Ext.Ajax.request({
								url : 'managebudget/geAccountorFactOrder.action',
								params : {
									itemId : itemId.getValue()
								},
								method : 'post',
								waitMsg : '正在加载数据...',
								success : function(result, request) {
									//Ext.MessageBox.alert('提示', '计算等级成功!');
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '计算等级失败!');
								}
							});
	}
	var btnAccountOrder = new Ext.Button({
				text : '计算等级',
				id : 'orderId',
				handler : function() {
					Ext.Ajax.request({
								url : 'managebudget/geAccountorFactOrder.action',
								params : {
									itemId : itemId.getValue()
								},
								method : 'post',
								waitMsg : '正在加载数据...',
								success : function(result, request) {
									Ext.MessageBox.alert('提示', '操作成功!');
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '操作失败!');
								}
							});

				}
			});
			//add by ypan 20100906
 var centerUse = new Ext.Button({
		id : 'centerUse',
		text : '费用使用部门',
		disabled:true,
		//iconCls : 'centerUse',
		handler : function() {
				var url= "centerUse.jsp";
				var args = new Object();
				args.itemId = itemId.getValue();
				var emp = window
						.showModalDialog(
								url,
								args,
								'dialogHeight:400px;dialogWidth:500px;center:yes;help:no;resizable:yes;status:no;');
		}

	})		
			
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
							columnWidth : .15,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [budgetFormula]
						}
						, {
							columnWidth : .15,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [factFormula]
						}
						//隐藏计算等级按钮 
//						, {
//							columnWidth : .15,
//							layout : 'form',
//							border : false,
//							labelWidth : 100,
//							items : [btnAccountOrder]
//						}
						, {
							columnWidth : .22,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [centerUse]
						}
						, {
							columnWidth : 1,
							layout : 'form',
							border : false
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemName]
						}, 
							{
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemCode, statItemUnit, itemType
//									,financeItem,
//									hidfinanceItem
									]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [retrieveCode, comeTypeBox, factWayBox
//							,debitCredit
							]
						}, 
							{
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [yiliuzhi]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [chuangzaozhi]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [deptName,centerId]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [orderBy]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 100,
							items : [itemExplain]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 100,
							items : [factExplain]
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
		var cn = tree.getSelectionModel().getSelectedNode();
			if (cn == null) {
				Ext.Msg.alert("提示", "请选择要修改的树");
				return
			};
		if ((itemCode.getValue() == "" || itemName.getValue() == ""
					|| factWayBox.getValue() == "")&&systemName.getValue()=="") {
				Ext.MessageBox.alert('提示信息', '请选择要删除的记录！');
				return false;
		};
		Ext.Msg.confirm("删除", "指标删除后,相关的表数据也将级联删除,\n并且不能恢复,确定要删除吗?", function(buttonobj) {
		if (buttonobj == "yes") {
		var curNode = tree.getSelectionModel().getSelectedNode();
				if (curNode.leaf) {
					Ext.Ajax.request({
								method : 'post',
								url : 'managebudget/deleteBudgetItem.action',
								params : {
									id : curNode.id,
									isItem : curNode.attributes.description
									//hidfinanceItem.getValue()
		
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
		}
		});
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
			};
			var isItem = tree.getSelectionModel().getSelectedNode().attributes.description;
			var itemFCode = tree.getSelectionModel().getSelectedNode().attributes.code;
			if (form.addOrUpdate == "add") {
				isItem = Ext.get("iy").dom.checked ? "Y" : "N";
			}
			// 是指标
			if (isItem == "Y") {
				if (form.getForm().isValid()) {
//					var idadd = (Ext.get("ry").dom.checked ? "Y" : "N");
					form.getForm().submit({
						method : 'post',
						url : 'managebudget/saveOrupdateBudgetItem.action',
						params : {
							isItem : 'Y',
							method : form.addOrUpdate,
							itemFCode : itemFCode,
//							'bubgetItem.ifTotal' : idadd,
							itemId : itemId.getValue()
						},
						success : function(form1, action) {
							if (form.addOrUpdate == 'add') {
								var nd = tree.getSelectionModel()
										.getSelectedNode();
								var path = nd.getPath();
									tree.getRootNode().reload();
									tree.expandPath(path, false);
									tree.getRootNode().select();
								form.addOrUpdate = "update";
							} else if (form.addOrUpdate == 'update'){
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								cn.attributes.openType = factWayBox.getValue();
								
							}else{
							}
							Ext.MessageBox.alert('提示', '操作成功!');
						},
						failure : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert("注意", o.msg);
							// Ext.MessageBox.alert('提示', "操作失败");
						}
					});
				}
			}
			// 非指标
			else {
				if (notItemFormPanel.getForm().isValid()) {
					notItemFormPanel.getForm().submit({
						method : 'post',
						url : 'managebudget/saveOrupdateBudgetItem.action',
						params : {
							isItem : 'N',
							method : NoleafAddOrUpdate,
							itemFCode : itemFCode,
							systemId : systemId.getValue()
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText + ")");
							if (NoleafAddOrUpdate == 'add') {
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								var path = cn.getPath();
								var node=cn;
								tree.loader.load(node, function(){
								tree.expandPath(path, false);
								tree.getNodeById(o.id).select();
								});
								systemId.setValue(o.id);
								NoleafAddOrUpdate = 'update';
							}else{
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								var path = cn.getPath();
								var node=cn.parentNode;
								tree.loader.load(node, function(){
								tree.expandPath(path, false);
								tree.getNodeById(cn.id).select();
								});
							}
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
	budgetFormula.disable();
	factFormula.disable();
	//debitCredit.disable();
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
