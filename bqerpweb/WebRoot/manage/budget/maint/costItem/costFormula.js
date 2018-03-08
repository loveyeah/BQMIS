/* 预算指标维护中的公式设置(预算公式) */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var args = window.dialogArguments;
	var itemId = args.itemId;
	var itemCode = args.itemCode;
	var itemType = args.itemType;

	var MyRecord = Ext.data.Record.create([{
				name : 'formulaId'
			}, {
				name : 'itemId'
			}, {
				name : 'formulaNo'
			}, {
				name : 'rowItemCode'
			}, {
				name : 'formulaContent'
			}, {
				name : 'fornulaType'
			}, {
				name : 'isUse'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'managebudget/getCbmCostFormulaList.action'
			});

	var theReader = new Ext.data.JsonReader({}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	store.load({
				params : {
					itemId : itemId
				}
			});
	store.on('update', function(store) {
				spellExtendFormualByStore(store);
			});

	store.on('load', function(store) {
				spellExtendFormualByStore(store);
			});

	function checkFormula() {
		try {
			if (store.getCount() == 0)
				return true;
			var strTemp = "";
			for (var i = 0; i < store.getCount(); i++) {
				var rec = store.getAt(i);
				if (rec.get("fornulaType") == itemBaseAttr.formulaContentType.item)
					strTemp += " " + i + " ";
				else
					strTemp += " " + rec.get("formulaContent") + " ";
			}
			Ext.util.JSON.decode(strTemp);
			return true;
		} catch (err) {
			Ext.Msg.alert("错误", "公式维护错误!");
			return false;
		}
	}

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);

		}
	}
	// 增加
	function addRecord() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = store.indexOf(currentRecord);
		var count = store.getCount();
		var currentIndex = currentRecord ? rowNo + 1 : count;
		var o = new MyRecord({
					'formulaId' : '',
					'itemId' : itemId,
					'formulaNo' : currentIndex,
					'rowItemCode' : '',
					'formulaContent' : '',
					'isUse' : '',
					'fornulaType' : '1'
				});
		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
		resetLine();
	}

	// 删除记录

	var isDeletes = false;
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
				isDeletes = true;
			}
		}
	}

	// 保存
	function saveModifies() {
		grid.stopEditing();
		// 验证公式正确性
		if (!checkFormula())
			return;

		var modifyRec = grid.getStore().getRange(0, store.getCount());
		if (modifyRec.length > 0 || isDeletes) {
			var updateData = new Array();
			for (var i = 0; i < grid.getStore().getCount(); i++) {
				var obj = store.getAt(i);
				var rec = new Object();
				rec.formulaNo = obj.get("formulaNo");
				rec.formulaContent = (obj.get("rowItemCode") == null || obj
						.get("rowItemCode") == '')
						? obj.get("formulaContent")
						: obj.get("rowItemCode");
				if (rec.formulaContent == null || rec.formulaContent == '') {
					Ext.Msg.alert('提示', '第' + (i + 1) + '行公式内容不能为空');
					return;
				}
				rec.fornulaType = obj.get("fornulaType");
				rec.itemId = obj.get("itemId");
				updateData.push(rec);
			}
			Ext.Ajax.request({
						url : 'managebudget/saveCbmCostFormula.action',
						method : 'post',
						params : {
							itemId : itemId,
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							isDeletes = false;
							store.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	
	// 取消
	function cancel() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || isDeletes) {
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			isDeletes = false;
		} else {
			store.reload();
			store.rejectChanges();
			isDeletes = false;
		}
	}
	// 关闭
	function close() {
		var object = Ext.get("winspace").dom.value;
		// alert(object)
		// return
		window.returnValue = object;
		window.close();
	}
	// 时间类型
	var Operate = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);

	var operateStore = new Ext.data.JsonStore({
				url : '',
				fields : Operate
			});
	operateStore.loadData(itemBaseAttr.operates);

	var formulaContentTypeBox = new Ext.form.ComboBox({
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['1', '指标'], ['2', '数值'], ['3', '操作符']]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				mode : 'local',
				forceSelection : true,
				blankText : '工具',
				emptyText : '工具',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '99%',
				listeners : {
					'change' : function() {
						var rec = grid.getSelectionModel().getSelected();
						rec.set("formulaContent", '');
						rec.set("rowItemCode", '');
					}
				}
			});

	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({

		store : store,
		layout : 'fit',
		columns : [sm, // 选择框
				number, {
					header : "公式内容",
					sortable : false,
					dataIndex : 'formulaContent',
					editor : new Ext.form.NumberField({
								listeners : {
									change : function() {
										alert()
										var rec = grid.getSelectionModel()
												.getSelected();
										rec.set("rowItemCode", this.value);
									}
								}
							})
				}, {
					header : "内容类别",
					align : "center",
					sortable : true,
					dataIndex : 'fornulaType',
					renderer : function changeIt(val) {
						if (val == "1") {
							return "指标";
						} else if (val == "2") {
							return "数值";
						} else if (val == "3") {
							return "操作符";
						} else {
							return "内容类别异常";
						}
					},
					editor : formulaContentTypeBox
				}],
		tbar : [{
					text : "新增",
					iconCls : 'add',
					handler : addRecord
				}, {
					text : "删除",
					iconCls : 'delete',
					handler : deleteRecords
				}, {
					text : "保存",
					iconCls : 'save',
					handler : saveModifies
				}, {
					text : "取消",
					iconCls : 'reflesh',
					handler : cancel
				}, {
					text : "关闭",
					iconCls : 'cancer',
					handler : close
				}],
		sm : sm,
		clicksToEdit : 1,
		listeners : {
			'beforeedit' : function(e) {
				if (e.field == "formulaContent") {
					var type = e.record.get('fornulaType');
					if (type == itemBaseAttr.formulaContentType.item) {
						Ext.Msg.alert("提示", "请从右边面板中选择指标!");
						return false;
					} else if (type == itemBaseAttr.formulaContentType.operate) {
						grid.getColumnModel().setEditor(2,
								new Ext.grid.GridEditor(new Ext.form.ComboBox({
											store : operateStore,
											id : 'operateBox',
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
												'select' : function(combo,
														record, index) {
													var rec = grid
															.getSelectionModel()
															.getSelected();
													rec.set("rowItemCode",
															record.data.key);
												}
											}
										})));
					} else if (type == itemBaseAttr.formulaContentType.data) {
						grid
								.getColumnModel()
								.setEditor(
										2,
										new Ext.grid.GridEditor(new Ext.form.NumberField()));
					}
				}
			}
		}
	});

	/* 模糊查询的grid */

	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "模糊查询",
				hideLabel : true,
				emptyText : '指标编码/名称模糊查询',
				name : 'argFuzzy',
				value : '',
				width : 250
			});
	function fuzzyQuery() {
		con_ds.reload({
					params : {
						start : 0,
						limit : 18,
						argFuzzy : query.getValue()
					}
				});
	};
	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();
			// document.getElementById('btnSign').click();
		}
	}

	var con_item = Ext.data.Record.create([{
				name : 'itemId'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'unitCode'
			}, {
				name : 'itemType'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managebudget/getAllCostItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					argFuzzy : query.getValue(),
					start : 0,
					limit : 18
				}
			})
	con_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							argFuzzy : query.getValue()
						});
			});
	var con_item_cm = new Ext.grid.ColumnModel([{
				header : '指标编码',
				dataIndex : 'itemCode',
				align : 'center'

			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				align : 'center'
			}, {
				header : '指标类别',
				dataIndex : 'itemType',
				align : 'center',
				renderer : function(n) {
					if (n == 1)
						return "年";
					if (n == 2)
						return "月";
					if (n == 3)
						return "日";
				}
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				beforePageText : '',
				afterPageText : '',
				emptyMsg : "没有记录"
			});
	var Grid = new Ext.grid.GridPanel({
				layout : 'fit',
				ds : con_ds,
				cm : con_item_cm,
				height : 320,
				autoScroll : true,
				split : true,
				// containerScroll : true,
				bbar : gridbbar,
				border : true
			});
	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {
		var recR = Grid.getSelectionModel().getSelected();

		if (recR.get('itemId') == itemId) {
			Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");
			return;
		}
		if (recR.get("itemType") != itemType) {
			Ext.Msg.alert("提示", "请选择同类型的指标!");
			return;
		}
		var recL = grid.getSelectionModel().getSelected();
		if (recL) {
			recL.set("rowItemCode", recR.get('itemCode'));
			recL.set("formulaContent", recR.get('itemName'));

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	// 指标体系树
	var rootNode = {
		id : '0',
		text : '成本编码体系',
		attributes : {
			"isItem" : "N"
		}
	};
	var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var deptTree = new Ext.tree.TreePanel({

				loader : new Ext.tree.TreeLoader({
							dataUrl : 'managebudget/getCostItemTree.action?pid=0'
						}),
				root : rootNodeObj,
				autoWidth : true,
				layout : 'fit',
				autoScroll : true,
				animate : true,
				enableDD : false,
				border : false,
				rootVisible : true,
				containerScroll : true

			});
	deptTree.on('beforeload', function(node) {
				deptTree.loader.dataUrl = 'managebudget/getCostItemTree.action?pid='
						+ node.id;
			});
	deptTree.on('dblclick', function(node, e) {
				e.stopEvent();
				Ext.Ajax.request({
							url : 'managebudget/getCostItemInfo.action',
							params : {
								id : node.id
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var str = result.responseText;
								var o = eval("("
										+ str.substring(1, str.length - 1)
										+ ")")
								// 是否预算指标
								var isItem = (o.itemCode == null ? false : true);
								if (isItem) {
									if (o.itemCode == itemCode) {
										Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");
										return;
									}
									if (o.itemType != itemType) {
										Ext.Msg.alert("提示", "请选择指标同类型");
										return;
									}
									var recL = grid.getSelectionModel()
											.getSelected();
									if (recL && recL.get("fornulaType") == 1) {
										recL.set("rowItemCode", o.itemCode);
										recL.set("formulaContent", node.text);
									} else {
										Ext.Msg.alert("提示",
												"请从左边选择一条[内容类别]为[指标]的记录！")
									}
								} else {
									Ext.Msg.alert("提示", "非成本指标");
								}
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示', '操作失败!');
							}
						});
			});
	rootNodeObj.expand();

	var winspace = new Ext.form.TextArea({
				id : 'winspace',
				readOnly : true,
				hideLabel : true
			});

	var innerPanel2 = new Ext.Panel({
				hidden : true,
				layout : "fit",
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							items : [winspace]
						}]
			});

	var innerPanel1 = new Ext.Panel({
				layout : "fit",
				hidden : true,
				border : false,
				tbar : [query, new Ext.Button({
									iconCls : 'query',
									text : '查询',
									handler : function() {
										con_ds.reload();
									}
								})],
				items : [{
							border : false,
							frame : false,
							layout : 'fit',
							autoScroll : true,
							items : [Grid]
						}]
			});

	var rightPanel = new Ext.Panel({
				layout : "fit",
				tbar : [{
							text : "项目选择",
							id : "prjChose",
							handler : prjChose
						}, {
							text : "模糊查询",
							id : "Query",
							handler : Query
						}
				// , {
				// text : "公式生成",
				// id : "formulaMade",
				// handler : formulaMade
				//
				// }
				],
				items : [{
							layout : 'fit',
							autoScroll : true,
							border : false,
							frame : false,
							items : [

									innerPanel2, deptTree, innerPanel1]
						}]
			})

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							// bodyStyle : "padding: 20,30,20,20",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '50%',
							items : [grid]
						}, {
							// bodyStyle : "padding: 20,20,20,20",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							// width : '50%',
							items : [rightPanel]
						}]
			});
	deptTree.show();
	function prjChose() {
		innerPanel1.hide();
		innerPanel2.hide();
		deptTree.show();

	}
	function Query() {

		innerPanel2.hide();
		deptTree.hide();
		innerPanel1.show();

		con_ds.reload();

	}
	// function formulaMade() {
	// innerPanel1.hide();
	// deptTree.hide();
	// // innerPanel2.show();

	// };
	window.onbeforeunload = onbeforeunload_handler;

	function onbeforeunload_handler() {
		var object = Ext.get("winspace").dom.value;
		// alert(object)
		window.returnValue = object;

	}

})