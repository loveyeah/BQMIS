Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var args = window.dialogArguments;

	var accountCode = args ? args.accountCode : '33';
	var accountType = args ? args.accountType : '1';

	var MyRecord = Ext.data.Record.create([{
				name : 'baseInfo.accountCode'
			}, {
				name : 'baseInfo.itemCode'
			}, {
				name : 'baseInfo.displayNo'
			}, {
				name : 'itemName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : ''
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

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

		}
	}
	// // 增加
	// function addRecord() {
	//
	// var currentRecord = grid.getSelectionModel().getSelected();
	// var rowNo = store.indexOf(currentRecord);
	//
	// var count = store.getCount();
	//
	// var currentIndex = currentRecord ? rowNo : count;
	//
	// var o = new MyRecord({
	// 'id.itemCode' : itemCode,
	// // 'id.formulaNo' : '',
	// 'formulaContent' : '',
	// 'fornulaType' : '1'
	// });
	//
	// grid.stopEditing();
	// store.insert(currentIndex, o);
	// sm.selectRow(currentIndex);
	// grid.startEditing(currentIndex, 3);
	// resetLine();
	// }

	// 删除记录

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
			}
			resetLine();
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();

		var selectNodes = grid.getStore().getModifiedRecords();
		var ros = new Array();
		var i;

		for (i = 0; i < selectNodes.length; i++) {

			var obj = {
				accountCode : selectNodes[i].get('baseInfo.accountCode'),
				itemCode : selectNodes[i].get('baseInfo.itemCode'),
				displayNo : selectNodes[i].get('baseInfo.displayNo'),
				itemName : selectNodes[i].get('itemName')
			}

			ros.push(obj);
		}
		window.returnValue = ros;
		window.close();

	}
	// 清空
	function cancel() {
		store.removeAll();

	}
	// 关闭
	function close() {

		window.close();
	}

	// 定义grid

	var grid = new Ext.grid.EditorGridPanel({
				// region : "west",
				store : store,
				layout : 'fit',
				// width:'0.5',
				columns : [
						sm, // 选择框
						number, {
							header : "指标编码",

							sortable : false,
							dataIndex : 'baseInfo.itemCode'
						}, {
							header : "指标名称",

							align : "center",
							sortable : true,
							dataIndex : 'itemName'
						}, {
							header : "显示顺序",

							align : "center",
							sortable : true,
							dataIndex : 'baseInfo.displayNo',
							editor : new Ext.form.NumberField()
						}],
				tbar : [
						// {
						// text : "新增",
						// iconCls : 'add',
						//
						// handler : addRecord
						// }
						//
						// ,
						{
					text : "删除",
					iconCls : 'delete',
					handler : deleteRecords

				}, {
					text : "清空",
					iconCls : 'cancer',
					handler : cancel
				}, '-', {
					text : "确定",
					iconCls : 'save',
					handler : saveModifies
				}, '-', {
					text : "关闭",
					iconCls : 'close',
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
				emptyText : '根据工单编码、KKS编码、申请人',
				name : 'argFuzzy',
				value : '',
				// anchor : '90%'
				width : '94%'
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
							url : 'manager/getAllStatItemaList.action'
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
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var Grid = new Ext.grid.GridPanel({

		ds : con_ds,
		cm : con_item_cm,
		height : 256,
		// // title : '合同列表',
		// width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		// tbar : contbar,
		border : true
			// ,
			// viewConfig : {
			// forceFit : true
			// }
		});

	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {

		var recR = Grid.getSelectionModel().getSelected();

		if (recR) {
			if (recR.data.itemType == accountType) {

				var currentRecord = grid.getSelectionModel().getSelected();
				var rowNo = store.indexOf(currentRecord);

				var count = store.getCount();

				var currentIndex = currentRecord ? rowNo : count;

				var o = new MyRecord({
							'baseInfo.accountCode' : accountCode,
							'baseInfo.itemCode' : recR.data.itemCode,
							'baseInfo.displayNo' : '',
							'itemName' : recR.data.itemName
						});

				grid.stopEditing();
				store.insert(currentIndex, o);
				sm.selectRow(currentIndex);
				// grid.startEditing(currentIndex, 3);
				resetLine();
				// var i = store.indexOf(currentRecord)
				// grid.getStore().getAt(i)
				// .set("formulaContent", recR.get('itemCode'));
			} else {
				Ext.Msg.alert("提示", "该指标时间点类型与台帐不同！")
			}
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
				e.stopEvent();
				// deptIdChoose = node.id;

				if (node.attributes.attributes.isItem == 'Y') {
					if (node.attributes.attributes.dataTimeType == accountType) {
						var currentRecord = grid.getSelectionModel()
								.getSelected();
						var rowNo = store.indexOf(currentRecord);

						var count = store.getCount();

						var currentIndex = currentRecord ? rowNo : count;

						var o = new MyRecord({
									'baseInfo.accountCode' : accountCode,
									'baseInfo.itemCode' : node.id,
									'baseInfo.displayNo' : '',
									'itemName' : node.text
								});

						grid.stopEditing();
						store.insert(currentIndex, o);
						sm.selectRow(currentIndex);
						// grid.startEditing(currentIndex, 3);
						resetLine();
						// var i = store.indexOf(currentRecord)
						// grid.getStore().getAt(i)
						// .set("formulaContent", recR.get('itemCode'));
					} else {
						Ext.Msg.alert("提示", "该指标时间点类型与台帐不同！")
					}
				} else {
					Ext.Msg.alert("提示", "该指标无数据！")
				}
			});

	rootNodeObj.expand();

	var innerPanel1 = new Ext.Panel({

				layout : "fit",
				hidden : true,
				border : false,
				items : [{
							bodyStyle : "padding: 20,30,20,30",
							//									
							border : false,
							frame : false,
							// region:"north",
							layout : 'form',
							// height : '40%',
							items : [query]
						}, {
							// bodyStyle : "padding: 20,20,20,20",

							border : false,
							frame : false,
							// region:"center",
							layout : 'fit',
							// height:700,
							items : [Grid]
						}]

			});

	var rightPanel = new Ext.Panel({

				layout : "fit",
				tbar : [{
							text : "项目选择",
							id : "prjChose",
							// iconCls : 'add',

							handler : prjChose
						}, {
							text : "模糊查询",
							id : "Query",
							// iconCls : 'add',

							handler : Query
						}],
				items : [

				{

							layout : 'fit',
							border : false,
							frame : false,
							// region : "center",

							items : [deptTree, innerPanel1]
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
							width : '55%',
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

	function prjChose() {
		innerPanel1.hide();

		deptTree.show();

	}
	function Query() {

		deptTree.hide();
		innerPanel1.show();

	}

})