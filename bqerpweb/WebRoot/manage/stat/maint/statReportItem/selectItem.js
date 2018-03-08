Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 自动生成显示顺序
	var j = 1;
	var args = window.dialogArguments;

	var accountCode = args ? args.accountCode : '33';
	var accountType = args ? args.accountType : '1';

	var MyRecord = Ext.data.Record.create([{
				name : 'model.id.reportCode'
			}, {
				name : 'model.id.itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'model.displayNo'

			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/findStatReportItemList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	store.load({
				params : {
					reportCode : accountCode,
					start : 0,
					limit : 18
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);
			temp.set("model.displayNo", temp.get("displayNo"));
		}
	}

	// 删除记录
	var ids = new Array();
	var reportcode = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();
				var code = new Array();

				if (member.get("model.id.itemCode") != null
						&& member.get("model.id.reportCode") != null) {
					id.push("'" + member.get("model.id.itemCode") + "'");
					reportcode.push(member.get("model.id.reportCode"));
					// id.push(member.get("model.id.itemCode") + ","
					// + member.get("model.id.reportCode"));
					var idstr = '';
					idstr = id.join(",");
					ids.push(idstr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
				resetLine();
			}
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			// alert(ids.join(";"));
			Ext.Ajax.request({
						url : 'manager/saveStatReportItem.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(","),
							isCode : accountCode
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
	// // 确定返回
	// function saveModifies() {
	// grid.stopEditing();
	// var selectNodes = store.getRange(0, store.getTotalCount());
	// var ros = new Array();
	// var i;
	// alert(Ext.encode(ros))
	// for (i = 0; i < selectNodes.length; i++) {
	// var obj = {
	// accountCode : selectNodes[i].get('model.id.reportCode'),
	// itemCode : selectNodes[i].get('model.id.itemCode'),
	// displayNo : selectNodes[i].get('model.displayNo'),
	// itemName : selectNodes[i].get('itemName')
	// }
	// ros.push(obj);
	// }
	// window.returnValue = ros;
	//
	// window.close();
	// }

	// 关闭
	function close() {
		if (!confirm("确定已保存，并关闭吗?"))
			return;
		else {
			window.close();
		}
	}

	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				layout : 'fit',
				columns : [
						sm, // 选择框
						number, {
							header : "指标编码",
							sortable : false,
							dataIndex : 'model.id.itemCode',
							width : 70,//modify by ywliu 20090911
							renderer:function(value, metadata, record){ 
								metadata.attr = 'style="white-space:normal;"'; 
								return value;  
							}
						}, {
							header : "指标名称",
							align : "center",
							sortable : true,
							dataIndex : 'itemName',
							width : 140,//modify by ywliu 20090911
							renderer:function(value, metadata, record){ 
								metadata.attr = 'style="white-space:normal;"'; 
								return value;  
							}
						}, {
							header : "显示顺序",
							align : "center",
							sortable : true,
							dataIndex : 'model.displayNo',
							width : 70,
							editor : new Ext.form.NumberField()
						}
//						, {
//							header : "显示顺序",
//							align : "center",
//							sortable : true,
//							dataIndex : 'displayNo',
//							editor : new Ext.form.NumberField()
//						}
						],
				tbar : [{
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
						}, '-', {
							text : "关闭",
							iconCls : 'exit',
							handler : close

						}],
				sm : sm, // 选择框的选择 Shorthand for
				// selModel（selectModel）
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	/* 模糊查询的grid */

	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "模糊查询",
				hideLabel : true,
				emptyText : '指标编码、指标名称',
				name : 'argFuzzy',
				value : '',
				anchor : '75%'
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
	// add by drdu 20090605
	var btnUt = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : fuzzyQuery
			});

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();
			// document.getElementById('btnSign').click();
		}
	}

	var con_item = Ext.data.Record.create([{
				name : 'itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'unitCode'
			}, {
				name : 'dataTimeType'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getAllStatItemaList.action'
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
	var con_item_cm = new Ext.grid.ColumnModel([

	{
				header : '指标编码',
				dataIndex : 'itemCode',
				align : 'center'
			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				align : 'center',
				width : 170//modify by ywliu 20090911
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var Grid = new Ext.grid.GridPanel({
				ds : con_ds,
				cm : con_item_cm,
				height : 315,
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				border : true
			});

	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {

		var recR = Grid.getSelectionModel().getSelected();

		// var recs1 = Oldstore.getRange(0, Oldstore.getTotalCount());
		// for (i = 0; i < recs1.length; i++) {
		// if (recs1[i].get("baseInfo.id.itemCode") ==
		// recR.data.itemCode) {
		// Ext.Msg.alert("提示", "该台帐已有此指标！");
		// return;
		// }
		// }
		if (recR) {
			var recs = store.getRange(0, store.getCount());
			if (recs.length > 0) {
				for (i = 0; i < recs.length; i++) {
					if (recs[i].get("model.id.itemCode") == recR.data.itemCode) {
						Ext.Msg.alert("提示", "该报表已有此指标！");
						return;
					}
				}
			}

			var currentRecord = grid.getSelectionModel().getSelected();
			var rowNo = store.indexOf(currentRecord);
			var count = store.getCount();
			var currentIndex = currentRecord ? (rowNo + 1) : count;
			var o = new MyRecord({
						'model.id.reportCode' : accountCode,
						'model.id.itemCode' : recR.data.itemCode,
						'model.displayNo' : "",
						'itemName' : recR.data.itemName
					});

			grid.stopEditing();
			store.insert(currentIndex, o);
			sm.selectRow(currentIndex);
			resetLine();

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	// 指标体系树
	var rootNode = {
		id : '0',
		text : '指标编码体系',
		attributes : {
			"isItem" : "N"
		}
	};
	var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var deptTree = new Ext.tree.TreePanel({

				loader : new Ext.tree.TreeLoader({
							dataUrl : 'manager/findStatTree.action'
						}),
				root : rootNodeObj,
				autoWidth : true,
				layout : 'fit',
				autoScroll : true,
				animate : true,
				enableDD : false,
				border : false,
				hidden : true,
				rootVisible : true,
				containerScroll : true
			});
	deptTree.on('beforeload', function(node) {
				deptTree.loader.dataUrl = 'manager/findStatTree.action?pid='
						+ node.id;
			});

	deptTree.on('click', function(node, e) {
				e.stopEvent(node.attributes.code);

				var recs = store.getRange(0, store.getCount());
				for (i = 0; i < recs.length; i++) {
					if (recs[i].get("model.id.itemCode") == node.id) {
						Ext.Msg.alert("提示", "该报表已有此指标！");
						return;
					}
				}
				// deptIdChoose = node.id;
				if (node.attributes.description == 'Y') {
					// if (node.attributes.openType == '1') {
					// if (node.attributes.code == accountType) {
					var currentRecord = grid.getSelectionModel().getSelected();
					var rowNo = store.indexOf(currentRecord);
					var count = store.getCount();
					var currentIndex = currentRecord ? (rowNo + 1) : count;
					var o = new MyRecord({
								'model.id.reportCode' : accountCode,
								'model.id.itemCode' : node.id,
								'model.displayNo' : '',
								'itemName' : node.text
							});

					grid.stopEditing();
					store.insert(currentIndex, o);
					sm.selectRow(currentIndex);
					resetLine();

					// } else {
					// Ext.Msg.alert("提示", "该指标时间点类型与录入报表类型不同！")
					// }
					// } else {
					// Ext.Msg.alert("提示", "该指标不是录入型指标!")
					// }
				} else {
					Ext.Msg.alert("提示", "该数据不是指标！")
				}
			});
	rootNodeObj.expand();

	var innerPanel1 = new Ext.Panel({
				layout : "fit",
				hidden : true,
				border : false,
				frame : false,
				items : [{
							// bodyStyle : "padding: 20,30,20,30",
							border : false,
							frame : true,
							labelAlign : 'center',
							layout : 'column',
							items : [query, btnUt]
						}, {
							border : false,
							frame : false,
							layout : 'fit',
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
						}],
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							items : [deptTree, innerPanel1]
						}]
			})

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '55%',
							items : [grid]
						}, {
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [rightPanel]
						}]
			});
	innerPanel1.show();
	function prjChose() {
		innerPanel1.hide();
		deptTree.show();
	}
	function Query() {
		deptTree.hide();
		innerPanel1.show();
	}

})