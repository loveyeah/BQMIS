Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	function treeClick(node, e) {
		e.stopEvent();
		panel.setTitle("当前选择指标:[" + node.text + "]");

		if (node.id == '0') {
			form.getForm().reset();
			form.addOrUpdate = "add";
			iyRadio.enable();
			inRadio.enable();

		} else {
			Ext.Msg.wait("正在加载数据,请等待...");
			Ext.Ajax.request({
				url : 'manager/getStatItemInfo.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var o = Ext.util.JSON.decode(result.responseText);
					form.addOrUpdate = "update";
					// 是否指标
					var isItem = (o.isItem == "Y" ? true : false);
					iyRadio.setValue(isItem);
					inRadio.setValue(!isItem);
					iyRadio.disable();
					inRadio.disable();
					if (isItem) {
						formPositon2.show();
						itemName.setValue(o.itemName);
						itemCode.setValue(o.itemCode);
						displayNo.setValue(o.orderBy);
						retrieveCode.setValue(o.retrieveCode);
						calLevel.setValue(o.accountOrder);

						// 是否累计
						var isTotal = (o.totalDataType == "1" ? true : false);
						ryRadio.setValue(isTotal);
						rnRadio.setValue(!isTotal);
						// 是否忽略零
						var isZero = (o.ignoreZero == "1" ? true : false);
						ayRadio.setValue(isZero);
						anRadio.setValue(!isZero);
						// 计量单位
						if (o.unitCode != null)
							statItemUnit.setValue(o.unitCode);
						else
							statItemUnit.clearValue();
						// 时间类型
						if (o.dataTimeType != null) {
							if (o.dataTimeType == '2') {
								groupNature.enable();
							} else {
								groupNature.disable();
							}
							dataTimeTypeBox.setValue(o.dataTimeType);
						} else
						{
							groupNature.disable();
							dataTimeTypeBox.clearValue();
						}
						// 数据类型
						if (o.itemType != null)
							itemTypeBox.setValue(o.itemType);
						else
							itemTypeBox.clearValue();
						// 产生方式
						if (o.dataCollectWay != null) {
							dataCollectWayBox.setValue(o.dataCollectWay);
							if (o.dataCollectWay == '2') {
								btnTimeType.enable();
							} else {
								btnTimeType.disable();
							}
							if ((o.dataCollectWay == '1' || o.dataCollectWay == '2')
									&& o.itemType == '2') {
								btnMultiple.setDisabled(false);
							} else {
								btnMultiple.setDisabled(true);
							}
						} else {
							dataCollectWayBox.clearValue();
							btnTimeType.disable();

						}

						if (o.dataCollectWay == '3') {
							// 产生方式为"派生计算",并且公式类型为"扩展指标公式"时
							if (o.deriveDataType == "3") {
								Ext.Ajax.request({
									url : 'manager/getBpCExtendFormulaList.action',
									params : {
										itemCode : node.id
									},
									method : 'post',
									success : function(result, request) {

										var list = Ext.util.JSON
												.decode(result.responseText);
										var strTemp = spellExtendFormual(list);
										calMethod.setValue(strTemp);
									}
								});
							}
							// 焓值公式
							else if (o.deriveDataType == '6') {
								Ext.Ajax.request({
									url : 'manager/getBpCEnthalpyFormula.action',
									params : {
										itemCode : node.id
									},
									method : 'post',
									success : function(result, request) {

										var list = eval('('
												+ result.responseText + ')');
										if (list.itemCode != null) {
											var strTemp = spellEnthalpyFormual(list);
											calMethod.setValue(strTemp);
										} else
											calMethod.setValue("");
									}
								});
							}
							// 表值公式
							else if (o.deriveDataType == '2') {
								Ext.Ajax.request({
									url : 'manager/getRunFormulaList.action',
									params : {
										deriveDataType : o.deriveDataType,
										itemCode : node.id
									},
									method : 'post',
									success : function(result, request) {

										var list = eval('('
												+ result.responseText + ')').list;

										var strTemp = spellTableFormual(list);

										calMethod.setValue(strTemp);
									}
								});
							}
							// 时段公式
							else if (o.deriveDataType == '5') {
								Ext.Ajax.request({
									url : 'manager/getRunFormulaList.action ',
									params : {
										deriveDataType : o.deriveDataType,
										itemCode : node.id
									},
									method : 'post',
									success : function(result, request) {

										var list = eval('('
												+ result.responseText + ')').list;;
										var strTemp = spellTimeFormual(list);
										calMethod.setValue(strTemp);
									}
								});
							}
							deriveDataTypeBox.enable();
							btnCalmethod.enable();
							btnAccountOrder.setDisabled(false);
						} else {
							calMethod.setValue("");
							btnCalmethod.disable();
							btnAccountOrder.setDisabled(true);
						}
						// 公式类型
						if (o.deriveDataType != null) {
							deriveDataTypeBox.setValue(o.deriveDataType);
							btnCalmethod.enable();//add by sychen 20100429
						} else {
							deriveDataTypeBox.clearValue();
							btnCalmethod.disable();//add by sychen 20100429
						}
						// 指标性质
						if (o.dataAttribute != null)
							dataAttributeBox.setValue(o.dataAttribute);
						else
							dataAttributeBox.clearValue();
						// 计算方法
						if (o.computeMethod != null)
							computeMethodBox.setValue(o.computeMethod);
						else
							computeMethodBox.clearValue();

						// 指标类型
						if (o.itemStatType != null)
							itemStatType.setValue(o.itemStatType);
						
						else
							itemStatType.clearValue();
						// handleBtnRateSet();
						// 计算公式

					} else {
						displayNo1.setValue(o.orderBy);
						itemNameNo.setValue(o.itemName);
						itemCode.setValue(o.itemCode);
					}
					btnTimepoint.enable();
						//班组性质
						if(o.groupNature !=null){
							groupNature.setValue(o.groupNature);
							groupNature.enable();
							btnTimepoint.disable();
						}else{
							groupNature.clearValue();
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
		tbar : new Ext.Panel({
					id : 'treetbar',
					layout : 'column',
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
							}]
				}),
		loader : new Tree.TreeLoader({
					dataUrl : 'manager/findStatTree.action',
					listeners : {
						"beforeload" : function(treeloader, node) {
							var key = Ext.getCmp("searchKey").getValue();
							if (typeof(key) == "undefined")
								key = '';
							treeloader.baseParams = {
								pid : node.id,
								method : 'POST',
								searchKey : key
							};
						}
					}
				})
	});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'manager/findStatTree.action?pid='
						+ node.id;
			});
	var root = new Tree.AsyncTreeNode({
				text : '统计指标编码体系',
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
				btnTimepoint.enable();
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
		btnUdt.setText('保存');
		iyRadio.enable();
		inRadio.enable();
		form.getForm().reset();
		itemTypeBox.setValue(1);
		computeMethodBox.setValue(2);
		dataTimeTypeBox.setValue(1);
		dataCollectWayBox.setValue(1);
		dataAttributeBox.setValue(1);
		btnCalmethod.disable();
		btnMultiple.disable();
		deriveDataTypeBox.disable();
		groupNature.disable();
	};
	// 树↑
	// form↓
	var displayNo = new Ext.form.NumberField({
				name : 'statItem.orderBy',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '显示顺序',
				anchor : '85%'
			});
	var displayNo1 = new Ext.form.NumberField({
				name : 'displayNo',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '显示顺序',
				anchor : '85%'
			});
	var itemNameNo = new Ext.form.TextField({
				id : 'itemNameNo',
				name : 'itemNameNo',
				xtype : 'textfield',
				fieldLabel : '节点名称',
				readOnly : false,
				anchor : '95%',
				allowBlank : false,
				blankText : '不可为空！'
			});
	var itemName = new Ext.form.ComboBox({
		id : "statItem.itemName",
		name : "statItem.itemName",
		fieldLabel : '指标名称',
		readOnly : true,
		anchor : '85%',
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
				var url = "../../system/selItem/selectItem.jsp";
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
					btnMultiple.disable();
					btnCalmethod.disable();
				}
			}
		}

	});
	var itemCode = new Ext.form.TextField({
				name : 'statItem.itemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '85%',
				allowBlank : true,
				blankText : '不可为空！'
			});

	var retrieveCode = new Ext.form.TextField({
				name : 'statItem.retrieveCode',
				xtype : 'textfield',
				fieldLabel : '检索码',
				readOnly : true,
				anchor : '85%',
				maxLength : 8,
				maxLengthText : '最多输入8个字符！',
				// vtype : 'alpha',
				vtypeText : '只能输入字母！'
			});

	// 计量单位
	var unitData = Ext.data.Record.create([{
				name : 'unitName'
			}, {
				name : 'unitId'
			}]);

	var allUnitStore = new Ext.data.JsonStore({
				url : 'manager/getUnitList.action',
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
				hiddenName : 'statItem.unitCode',
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

	// 数据类型
	var itemTypeData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var itemTypeStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=ITEM_TYPE',
				fields : itemTypeData
			});

	itemTypeStore.on('load', function(e, records) {
				itemTypeBox.setValue(1);
			});

	itemTypeStore.load();

	var itemTypeBox = new Ext.form.ComboBox({
				fieldLabel : '数据类型',
				store : itemTypeStore,
				id : 'itemType',
				name : 'itemType',
				hiddenName : 'statItem.itemType',
				valueField : "key",
				displayField : "value",
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
						var dcw = dataCollectWayBox.getValue();
						if ((dcw == "1" || dcw == "2") && getvalue == "2") {
							btnMultiple.enable();
						} else {
							btnMultiple.disable();
						}
					}
				}
			});

	// 公式类型
	var deriveDataTypeData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var deriveDataTypeStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=DERIVE_DATA_TYPE',
				fields : deriveDataTypeData
			});

	deriveDataTypeStore.load();

	var deriveDataTypeBox = new Ext.form.ComboBox({
				fieldLabel : '公式类型',
				store : deriveDataTypeStore,
				id : 'deriveDataType',
				name : 'deriveDataType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.deriveDataType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',//add by sychen 20100429
				listeners : {
					select : function(index, scrollIntoView) {
						var getvalue = index.value;
						switch (getvalue) {
							case '2' :
								btnCalmethod.enable();
								break;
							case '3' :
							    btnCalmethod.enable();
								break;
							case '5' :
								btnCalmethod.enable();
								break;
							case '6' :
								btnCalmethod.enable();
								break;
						}
					}
				}
				//------------add end--------------------//
			});

	// 计算方法
	var computeMethodData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var computeMethodStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=COMPUTE_METHOD',
				fields : computeMethodData
			});

	computeMethodStore.load();

	computeMethodStore.on('load', function(e, records) {
				computeMethodBox.setValue(2);
			});

	var computeMethodBox = new Ext.form.ComboBox({
				fieldLabel : '计算方法',
				store : computeMethodStore,
				id : 'computeMethod',
				name : 'computeMethod',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.computeMethod',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});

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

	// 时间类型
	var dataTimeTypeData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var dataTimeTypeStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=DATA_TIME_TYPE',
				fields : dataTimeTypeData
			});

	dataTimeTypeStore.on('load', function(e, records) {
				dataTimeTypeBox.setValue(1);
			});

	dataTimeTypeStore.load();

	var dataTimeTypeBox = new Ext.form.ComboBox({
				fieldLabel : '时间类型',
				store : dataTimeTypeStore,
				id : 'dataTimeType',
				name : 'dataTimeType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.dataTimeType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
						var getvalue = index.value;
						var itemType = dataTimeTypeBox.getValue();
						if (itemType == "2") {
							groupNature.enable();
							groupNature.setValue("1")
							btnTimepoint.disable();
						} else {
							groupNature.disable();
							groupNature.clearValue();
							btnTimepoint.enable();
						}
					}
				}
			});

	// 产生方式
	var dataCollectWayData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var dataCollectWayStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=DATA_COLLECT_WAY',
				fields : dataCollectWayData
			});

	dataCollectWayStore.on('load', function(e, records) {
				dataCollectWayBox.setValue(1);
			});

	dataCollectWayStore.load();
	
	var dataCollectWayBox = new Ext.form.ComboBox({
				fieldLabel : '产生方式',
				store : dataCollectWayStore,
				id : 'dataCollectWay',
				name : 'dataCollectWay',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.dataCollectWay',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				listeners : {
					select : function(index, scrollIntoView) {
						var getvalue = index.value;
						var itemType = itemTypeBox.getValue();
						switch (getvalue) {
							// 实时录入
							case '1' :
								deriveDataTypeBox.clearValue();
								deriveDataTypeBox.disable();
								btnCalmethod.disable();
								if (itemType == '2') {
									btnMultiple.enable();
								} else {
									btnMultiple.disable();
								}
								btnTimeType.disable();
								break;
							// 实时采集
							case '2' :
								deriveDataTypeBox.clearValue();
								deriveDataTypeBox.disable();
								btnCalmethod.disable();
								if (itemType == '2') {
									btnMultiple.enable();
								} else {
									btnMultiple.disable();
								}
								if(dataTimeTypeBox.getValue() == '2'){
										btnTimeType.disable();
									}else{
									  btnTimeType.enable();
									}
								deriveDataTypeBox.clearValue();
								break;
							// 派生计算
							case '3' :
								deriveDataTypeBox.enable();
								// 设置默认公式类型为表值指标公式，编码为2
								deriveDataTypeBox.setValue(3);
								btnCalmethod.enable();
								btnMultiple.disable();
								btnTimeType.disable();
								btnAccountOrder.setDisabled(false);
								break;
						}
					}
				}
			});

	// 指标性质
	var dataAttributeData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var dataAttributeStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=DATA_ATTRIBUTE',
				fields : dataAttributeData
			});

	dataAttributeStore.on('load', function(e, records) {
				dataAttributeBox.setValue(1);
			});

	dataAttributeStore.load();

	var dataAttributeBox = new Ext.form.ComboBox({
				fieldLabel : '指标性质',
				store : dataAttributeStore,
				id : 'dataAttribute',
				name : 'dataAttribute',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.dataAttribute',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});

	var calLevel = new Ext.form.TextField({
				name : 'accountOrder',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '计算等级',
				anchor : '85%',
				value : '1'
			});

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

	var ifGivezero = {
		// id : 'isItem',
		layout : 'column',
		isFormField : true,
		fieldLabel : '忽略零值',
		style : 'cursor:hand',
		anchor : '80%',
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : [ayRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [anRadio]
				}]
	};

	var calMethod = new Ext.form.TextArea({
				id : 'formula',
				xtype : 'textarea',
				fieldLabel : '计算公式',
				readOnly : true,
				height : 120,
				anchor : '92.5%'
			});

	var iyRadio = new Ext.form.Radio({
		id : 'iy',
		boxLabel : '是',
		name : 'ii',
		readOnly : true,
		inputValue : 'Y',
		checked : true,
		listeners : {
			'check' : function(radio, check) {
				if (check) {
					// formPositon1.hide();
					Ext.get("itemNameNo").dom.parentNode.parentNode.style.display = 'none';
					Ext.get("displayNo").dom.parentNode.parentNode.style.display = 'none';
					formPositon2.show();
					// formPositon4.show();
					itemNameNo.allowBlank = true;
					itemName.allowBlank = false;
				}
			}
		}
	});

	var inRadio = new Ext.form.Radio({
		id : 'in',
		boxLabel : '否',
		readOnly : true,
		name : 'ii',
		inputValue : 'N',
		listeners : {
			'check' : function(radio, check) {
				if (check) {
					formPositon2.hide();
					Ext.get("itemNameNo").dom.parentNode.parentNode.style.display = '';
					Ext.get("displayNo").dom.parentNode.parentNode.style.display = '';
					itemNameNo.allowBlank = false;
					itemName.allowBlank = true;
					itemNameNo.focus();
				}
			}
		}
	});
	var isItem = {
		id : 'isItem',
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否指标',
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

	// 指标类型 1大指标 2小指标

	var itemStatType = new Ext.form.ComboBox({
				fieldLabel : '指标类型',
				store : [['1', '大指标'], ['2', '小指标']],
				id : 'itemStatType',
				name : 'itemStatType',
				valueField : "id",
				displayField : "name",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemStatType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%',
				value : '2'
			})

	// 班组性质
	var groupNature = new Ext.form.ComboBox({
		fieldLabel : '班组性质',
		store : [['1', '后夜班'],['2', '上午班'],['4', '下午班'], ['5', '前夜班']],
		id : 'groupNature',
		name : 'groupNature',
		valueField : "id",
		displayField : "name",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'statItem.groupNature',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		anchor : '85%'
			// value : '1'
		})

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
							items : [isItem]
						}, {
							columnWidth : .7,
							layout : 'form',
							border : false,
							labelWidth : 100,
							items : [itemNameNo]
						}, {
							columnWidth : .3,
							layout : 'form',
							labelWidth : 100,
							border : false,
							items : [displayNo1]
						}]
			});

	// 子页面按钮
	var btnTimeType = new Ext.Button({
		text : '采集点设置',
		disabled : true,
		handler : function() {
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}
			var args = new Object();
			args.dateType = dataTimeTypeBox.getValue();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			args.isCollectItem = ((dataCollectWayBox.getValue() == 2)
					? true
					: false);
			args.ischange = false;
			var url = "itemDetailSet/itemDetailSet.jsp";
			// var url = "itemDetailSet/collectionSet/collectionSet.jsp";
			window
					.showModalDialog(
							url,
							args,
							"dialogHeight:300px;dialogWidth:600px;center:yes;help:no;resizable:no;status:no;");
		}
	});

	//********有效时点公式设置 add by liuyi 20100521 开始******************************
	// 数据库中时候存在该指标的有效时点公式 0：不存在，1：存在
	var validExist = new Ext.form.Hidden({
		id : 'valieExist',
		value : 0
	})
	var validItemCode = new Ext.form.Hidden({
		id : 'validItemCode'
	})
	var validItemName = new Ext.form.TextField({
		id : 'validItemName',
		fieldLabel : '指标名称<font color="red">*</font>',
		readOnly : true,
		allowBlank : false
	})
	var validMin = new Ext.form.NumberField({
		id : 'validMin',
		fieldLabel : '最小值<font color="red">*</font>',
		allowBlank : false,
		allowDecimals : true,
		allowNegative : true,
		decimalPrecision : 4,
		maxValue : 99999999999.9999
	})
	var validMax = new Ext.form.NumberField({
		id : 'validMax',
		fieldLabel : '最大值<font color="red">*</font>',
		allowBlank : false,
		allowDecimals : true,
		allowNegative : true,
		decimalPrecision : 4,
		maxValue : 99999999999.9999
	})
	var validConnItem = new Power.item({dataTimeType:'1'},true,{fieldLabel : '关联指标<font color="red">*</font>',width : 120,allowBlank:false});
	var validForm = new Ext.form.FormPanel({
		id : 'validForm',
		border : false,
		frame : true,
		labelWidth : 65,
		layout : 'column',
		defaults : {
			layout : 'form',
			columnWidth : .5,
			border: false,
			frame : false
		},items : [{
			items : [validItemCode,validItemName,validMin]
		},{
			items : [validConnItem.combo,validMax]
		}]
	})
	var validWin = new Ext.Window({
		id : 'validWin',
		frame : true,
		title : '有效时点公式设置',
		closeAction : 'hide',
		layout : 'fit',
		width : 450,
		height : 200,
		modal : true,
		items : [validForm],
		buttonAlign : 'center',
		buttons : [new Ext.Button({
			text : '保存',
			iconCls : 'save',
			id : 'validWinSave',
			handler : function(){
				if(validForm.getForm().isValid()){
					Ext.Msg.confirm('提示','确认要保存吗？',function(buttonId){
						if(buttonId == 'yes'){
							Ext.Msg.wait("正在保存中……")
								Ext.Ajax.request({
											url : 'manager/addOrUpdateValidFormulaEntity.action',
											method : 'post',
											params : {
												itemCode : validItemCode.getValue(),
												connItemCode : validConnItem.combo.getValue(),
												min : validMin.getValue(),
												max : validMax.getValue()
											},
											success : function(response,
													options) {
												Ext.Msg.hide();
												var res = Ext.decode(response.responseText);
												if (res && res.msg) {
													Ext.Msg.alert('提示',res.msg);
													validWin.hide();
												}
											},failure : function(response,
													options) {
														Ext.Msg.alert('提示','公式保存失败！');
													}
										})
							}
					})			
				}
			}
		}),new Ext.Button({
			text : '删除',
			id : 'validWinDelete',
			iconCls : 'delete',
			handler : function(){
				if(validExist.getValue() == 0){
					Ext.Msg.alert('提示','公式尚未保存！');
					return;
				}
				Ext.Msg.confirm("提示",'确认要删除公式吗？',function(buttonId){
					if (buttonId == 'yes') {
								Ext.Ajax.request({
											url : 'manager/deleteValidFormula.action',
											method : 'post',
											params : {
												itemCode : validItemCode.getValue()
											},
											success : function(response,
													options) {
												var res = Ext.decode(response.responseText);
												if (res && res.msg) {
													Ext.Msg.alert('提示',res.msg);
													validWin.hide();
												}
											},failure : function(response,
													options) {
														Ext.Msg.alert('提示','公式删除失败！');
													}
										})
							}
				})
			}
		}),new Ext.Button({
			text : '取消',
			iconCls : 'cancer',
			id : 'validWinCancel',
			handler : function(){
				validWin.hide()
			}
		})]
	})
	//********有效时点公式设置 add by liuyi 20100521 结束******************************
	var btnCalmethod = new Ext.Button({
		text : '公式设置',
		handler : function() {
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}

			if (dataTimeTypeBox.getValue() == "" || itemCode.getValue() == ""
					|| itemName.getValue() == ""
					|| dataCollectWayBox.getValue() == "") {

				Ext.MessageBox.alert('提示信息', '请选择指标项！');
				return false;
			}
			
			// add by liuyi 20100521 对有效时点公式进行维护
			if(deriveDataTypeBox.getValue() == 7){
				if(dataTimeTypeBox.getValue() != 3){
					Ext.Msg.alert('提示','有效时点公式的时间类型为日指标！');
					return;
				}
				validWin.show();
				validForm.getForm().reset()
				Ext.Msg.wait('正在查询公式……')
				Ext.Ajax.request({
					url : 'manager/queryValidFormulaEntity.action',
					method : 'post',
					params : {
						itemCode : itemCode.getValue()
					},success: function(response,options){
						Ext.Msg.hide()
						var resu = Ext.decode(response.responseText);
						if(resu){
							if(resu.length == 0){
								validExist.setValue(0);
								validItemCode.setValue(itemCode.getValue());
								validItemName.setValue(itemName.getValue());
							}else{
								validExist.setValue(1);
								var arr = resu[0].toString().split(',');
								validItemCode.setValue(arr[0]);
								validItemName.setValue(arr[1]);
								validConnItem.setValue(arr[2],arr[3]);
								validMin.setValue(arr[4]);
								validMax.setValue(arr[5])
							}
						}
					},failur: function(response,options){
						Ext.Msg.alert('提示','查询有效时点公式失败！')
					}
				})
			} else {

				var args = new Object();
				// 指标编码
				args.itemCode = itemCode.getValue();
				// 指标名称
				args.itemName = itemName.getValue();
				// 公式类型
				args.deriveDataType = deriveDataTypeBox.getValue();
				// 时间类型
				args.itemType = dataTimeTypeBox.getValue();
				var url;
				if (args.deriveDataType == '3') {
					url = "./formula/formula.jsp";
				} else if (args.deriveDataType == '6') {
					url = "./formula/enthalpyFormula.jsp";
				} else if (args.deriveDataType == '2'
						|| args.deriveDataType == '5') {
					url = "./formula/timeFormula.jsp";
				} else
					Ext.Msg.alert("错误", "公式类型异常！")
				var o = window
						.showModalDialog(
								url,
								args,
								"dialogHeight:2400px;dialogWidth:2000px;center:yes;help:no;resizable:no;status:no;");
				calMethod.setValue(o);
			}
		
		}
	});

	var btnMultiple = new Ext.Button({
		text : '倍率设置',
		handler : function() {
			var args = new Object();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}
			// if (dataTimeTypeBox.getValue() == "" || itemCode.getValue() == ""
			// || itemName.getValue() == ""
			// || dataCollectWayBox.getValue() == "") {
			// Ext.MessageBox.alert('提示信息', '请选择指标项！');
			// return false;
			// }
			var url = "./multipleSet/multipleSet.jsp";
			window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
		}
	});
	var btnAccountOrder = new Ext.Button({
				text : '计算等级',
				handler : function() {
					Ext.Ajax.request({
								url : 'manager/getAccountOrder.action',
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
			});

	var btnTimepoint = new Ext.Button({
		text : '时间点设置',
		disabled : true,
		handler : function() {
			
			// add by liuyi 20100521 
			if(deriveDataTypeBox.getValue() == 7){
				Ext.Msg.alert('提示','有效时点公式无时间点设置!')
				return;
			}
			
			var args = new Object();
			args.itemCode = itemCode.getValue();
			args.itemName = itemName.getValue();
			args.dateType = dataTimeTypeBox.getValue();
			if (form.addOrUpdate == "add") {
				Ext.Msg.alert("提示", "请先保存!");
				return;
			}

			var url = "./itemDetailSet/timeDotSet/timeDotSet.jsp";
			window
					.showModalDialog(
							url,
							args,
							"dialogHeight:400px;dialogWidth:800px;center:yes;help:no;resizable:no;status:no;");
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
						// ,
						// labelWidth : 100
						// ,
						// items : [btnTimeType]
					}, {
					columnWidth : .15,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [btnTimeType]
				}, {
					columnWidth : .15,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [btnCalmethod]
				}, {
					columnWidth : .15,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [btnMultiple]
				}, {
					columnWidth : .15,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [btnAccountOrder]
				}, {
					columnWidth : .15,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [btnTimepoint]
				}, {
					columnWidth : 1,
					layout : 'form',
					border : false
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [itemName, itemCode, statItemUnit, itemTypeBox,
							deriveDataTypeBox, computeMethodBox, ifAdd,
							itemStatType]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [displayNo, retrieveCode, dataTimeTypeBox,
							groupNature, dataCollectWayBox, dataAttributeBox,
							calLevel, ifGivezero]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 100,
					items : [calMethod]
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
	 * 删除指标
	 */
	function delStatItem() {
		if (!confirm("指标删除后,相关的表数据也将级联删除,\n并且不能恢复,确定要删除吗?"))
			return;
		var curNode = tree.getSelectionModel().getSelectedNode();
		if (curNode.leaf)
			form.getForm().submit({
						method : 'post',
						url : 'manager/deleteStatItem.action',
						params : {
							id : curNode.id
						},
						success : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							var nd = tree.getSelectionModel().getSelectedNode();
							var path = nd.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);

							tree.getRootNode().select();
							addStatItem();
							Ext.MessageBox.alert('提示信息', o.msg);

						},
						failure : function(form, action) {
							Ext.MessageBox.alert('错误', '请加载节点信息后删除！');
						}
					})
		else
			Ext.MessageBox.alert('提示信息', '父节点不可删除！');

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
			var isItem = tree.getSelectionModel().getSelectedNode().attributes.description;
			if (form.addOrUpdate == "add") {
				isItem = Ext.get("iy").dom.checked ? "Y" : "N";
			}
			// 是指标
			if (isItem == "Y") {
				// alert(firstItemCode);
				// return;
				if (form.getForm().isValid()) {
					var idzero = (Ext.get("ay").dom.checked ? "1" : "2");
					var idadd = (Ext.get("ry").dom.checked ? "1" : "2");
					form.getForm().submit({
						method : 'post',
						url : 'manager/updateStatItem.action',
						params : {
							isItem : 'Y',
							method : form.addOrUpdate,
							'statItem.parentItemCode' : tree
									.getSelectionModel().getSelectedNode().id,
							'statItem.totalDataType' : idadd,
							'statItem.ignoreZero' : idzero
							// ,
							// firstItemCode : firstItemCode

						},
						success : function(form1, action) {
							if (form.addOrUpdate == 'add') {
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								var path = cn.getPath();
								tree.getRootNode().reload();
							    form.getForm().reset();//add by wpzhu 
								tree.expandPath(path, false);
								// if(o.dataCollectWay == '2')
								// {
								// btnTimeType.enable();
								// }
								form.addOrUpdate = "update";
							} else {
								var cn = tree.getSelectionModel()
										.getSelectedNode();
								cn.attributes.code = dataTimeTypeBox.getValue();
								cn.attributes.openType = dataCollectWayBox
										.getValue();
							}
							Ext.MessageBox.alert('提示', '操作成功!');
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
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
						url : 'manager/updateStatItem.action',
						params : {
							isItem : 'N',
							method : form.addOrUpdate,
							itemCode : tree.getSelectionModel()
									.getSelectedNode().id,
							itemName : itemNameNo.getValue(),
							displayNo : displayNo1.getValue()
						},
						success : function(form, action) {
							var cn = tree.getSelectionModel().getSelectedNode();
							var path = cn.getPath();
							tree.getRootNode().reload();
							tree.expandPath(path, false);
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.MessageBox.alert('提示', o.msg);
						}
					});
				}
			}
		}
	});
	Ext.form.FormPanel.prototype.addOrUpdate = "add";

	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:10px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [formPositon2]
			});
	var notItemFormPanel = new Ext.FormPanel({
				bodyStyle : "padding:10px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				items : [formPositon1],
				buttons : [btnAdd, btndel, btnUdt]
			});
	var panel = new Ext.Panel({
				border : false,
				collapsible : true,
				title : '指标信息维护',
				items : [notItemFormPanel, form]
			});
	btnCalmethod.disable();
	btnMultiple.disable();
	deriveDataTypeBox.disable();
	groupNature.disable();
	btnAccountOrder.setDisabled(true);
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
							title : "统计指标",
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
