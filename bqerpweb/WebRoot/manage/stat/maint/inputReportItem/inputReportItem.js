Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'model.id.reportCode'
			}, {
				name : 'model.id.itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'model.itemAlias'
			}, {
				name : 'model.dataType'
			}, {
				name : 'model.displayNo'
			}, {
				name : 'model.itemBaseName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/findReprotItemList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			});

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	var dataTypeBox = new Ext.form.ComboBox({
				// fieldLabel : '数据类型',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['0', '整数'], ['1', '一位小数'], ['2', '二位小数'],
									['3', '三位小数'], ['4', '四位小数']]
						}),
				id : 'dataType',
				name : 'dataTypeText',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				value : "0",
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'model.dataType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [sm, number, {
							header : "一级名称",
							sortable : false,
							dataIndex : 'model.itemBaseName',
							width : 100,
							editor : new Ext.form.TextField({}),
							renderer:function(value, metadata, record){ 
								metadata.attr = 'style="white-space:normal;"'; 
								return value;  
							}//modify by ywliu 20090911
						}, {
							header : "指标编码",
							sortable : false,
							dataIndex : 'model.id.itemCode'
						}, {
							header : "指标名称",
//							align : "center",
							sortable : true,
							dataIndex : 'itemName',
							width : 170,//modify by ywliu 20090911
							renderer:function(value, metadata, record){ 
								metadata.attr = 'style="white-space:normal;"'; 
								return value;  
							}
						}, {
							header : "指标别名",
//							align : "center",
							sortable : true,
							dataIndex : 'model.itemAlias',
							width : 170,//modify by ywliu 20090911
							editor : new Ext.form.TextField({}),
							renderer:function(value, metadata, record){ 
								metadata.attr = 'style="white-space:normal;"'; 
								return value;  
							}
						}, {
							header : "数据类型",
							align : "center",
							sortable : true,
							dataIndex : 'model.dataType',
							editor : dataTypeBox,
							width : 60,
							renderer : function idByType(type) {
								if (type == 0) {
									return "整数"
								} else if (type == 1) {
									return "一位小数"
								} else if (type == 2) {
									return "二位小数"
								} else if (type == 3) {
									return "三位小数"
								} else if (type == 4) {
									return "四位小数"
								}
							}
						}, {
							header : "显示顺序",
							align : "center",
							sortable : true,
							hidden : false,
							dataIndex : 'model.displayNo',
							editor : new Ext.form.NumberField({
										maxLength : 10
									})
						}],
				tbar : [{
							text : "指标选择",
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecords
						}, '-', {
							text : "保存",
							iconCls : 'save',
							handler : saveModifies
						}, '-', {
							text : "取消",
							iconCls : 'cancer',
							handler : cancel
						}],
				sm : sm,
//				bbar : new Ext.PagingToolbar({
//							pageSize : 100,
//							store : store,
//							displayInfo : true,
//							displayMsg : "显示第{0}条到{1}条，共{2}条",
//							emptyMsg : "没有记录",
//							beforePageText : '',
//							afterPageText : ""
//						}),
				clicksToEdit : 1
			});

	var con_item = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCInputReportList.action' // 运行报表指标名称
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					start : 0,
					limit : 18
				}
			})

	var con_item_cm = new Ext.grid.ColumnModel([{
				header : '运行报表编码',
				dataIndex : 'reportCode',
				align : 'center',
				hidden : 'true'
			}, {
				header : '运行报表名称',
				dataIndex : 'reportName',
				align : 'left',
				width : 180,//modify by ywliu 20090911
				renderer:function(value, metadata, record){ 
						metadata.attr = 'style="white-space:normal;"'; 
						return value;  
				}
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});

	var Grid = new Ext.grid.GridPanel({
				title : '运行报表列表',
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				border : true
			});
	Grid.on('rowclick', modifyBtn);
	grid.on("rowcontextmenu", function(g, i, e) {
				e.stopEvent();
				var _store = grid.getStore();
				var menu = new Ext.menu.Menu({
							id : 'fuctionMenu',
							items : [new Ext.menu.Item({
												text : '移至顶行',
												iconCls : 'priorStep',
												handler : function() {
													if (i != 0) {
														var record = _store
																.getAt(i);
														_store.remove(record);
														_store
																.insert(0,
																		record);
														resetLine();
													}
												}
											}), new Ext.menu.Item({
												text : '上移',
												iconCls : 'priorStep',
												handler : function() {
													if (i != 0) {
														var record = _store
																.getAt(i);
														_store.remove(record);
														_store.insert(i - 1,
																record);
														resetLine();
													}
												}
											}), new Ext.menu.Item({
												text : '下移',
												iconCls : 'nextStep',
												handler : function() {
													if ((i + 1) != _store
															.getCount()) {
														var record = _store
																.getAt(i);
														_store.remove(record);
														_store.insert(i + 1,
																record);
														resetLine();
													}
												}
											}), new Ext.menu.Item({
												text : '移至最后',
												iconCls : 'nextStep',
												handler : function() {
													if ((i + 1) != _store
															.getCount()) {
														var record = _store
																.getAt(i);
														_store.remove(record);
														_store
																.insert(
																		_store
																				.getCount(),
																		record);
														resetLine();
													}
												}
											})]
						});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			});
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("model.displayNo", j + 1);
		}
	}
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			store.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									reportCode : recL.get("reportCode")
								});
					})
			store.load({
						params : {
							reportCode : recL.get("reportCode"),
							start : 0,
							limit : 999999999
						}
					});
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			var args = {
				accountType : recL.get("reportType"),
				accountCode : recL.get("reportCode")
			}
			var url = "selectItem.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(rvo) != "undefined") {
				var i;
				for (i = 0; i < rvo.length; i++) {
					var count = store.getCount();
					var currentIndex = count;
					var checkSame = true;
					for (var j = 0; j < store.getCount(); j++) {
						if (store.getAt(j).get("model.id.itemCode") == rvo[i].itemCode) {
							checkSame = false;
							break;
						}
					}

					if (checkSame) {
						var o = new MyRecord({
									'model.itemBaseName' : '',
									'model.id.reportCode' : '',
									'model.id.itemCode' : '',
									'model.displayNo' : '',
									'itemName' : '',
									'model.itemAlias' : '',
									'model.dataType' : '0'
								});
						grid.stopEditing();
						store.insert(currentIndex, o);
						sm.selectRow(currentIndex);
						o.set("model.id.reportCode", rvo[i].accountCode);
						o.set("model.id.itemCode", rvo[i].itemCode);
						o.set("model.displayNo", rvo[i].displayNo);
						o.set("model.itemAlias", rvo[i].itemName);
						o.set("itemName", rvo[i].itemName);
						o.set("model.dataType", '0');

					}
				}
			}
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	// 删除记录
	var ids = new Array();
	var reportcode;
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			reportcode = selections[0].get("model.id.reportCode");
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();
				var code = new Array();

				if (member.get("model.id.itemCode") != null
						&& member.get("model.id.reportCode") != null) {
					id.push("'" + member.get("model.id.itemCode") + "'");

					// id.push(member.get("model.id.itemCode") + ","
					// + member.get("model.id.reportCode"));
					var idstr = '';
					idstr = id.join(",");
					ids.push(idstr);

					// var codestr ='';
					// codestr = reportcode.join(",");
					// reportcode.push(codestr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveModifies() {

		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			// alert(ids.join(";"));
			Ext.Ajax.request({
						url : 'manager/addReprotItem.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(","),
							isCode : reportcode
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							ids = [];
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
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			ids = [];
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 2,2,2,2",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '25%',
							items : [Grid]
						}, {
							bodyStyle : "padding: 2,2,2,2",
							region : "center",
							border : false,
							autoScroll : true,
							frame : false,
							layout : 'fit',
							items : [grid]
						}]
			});
})