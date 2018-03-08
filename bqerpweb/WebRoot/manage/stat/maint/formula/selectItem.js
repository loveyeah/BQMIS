Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			var args = window.dialogArguments;

			// var accountCode = args ? args.accountCode : '33';
			// var accountType = args ? args.accountType : '1';

			/* 模糊查询的grid */

			var query = new Ext.form.TextField({
						id : 'argFuzzy',
						fieldLabel : "模糊查询",
						hideLabel : true,
						emptyText : '指标编码、指标名称',
						name : 'argFuzzy',
						value : '',
						anchor : '100%'
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
			con_ds.reload({
						params : {
							start : 0,
							limit : 18,
							argFuzzy : query.getValue()
						}
					});
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
				if (recR) {

					returndata(recR.data.itemCode, recR.data.itemName)
				} else {
					Ext.Msg.alert("提示", "请选择右边的一条记录！")
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

			// 确定返回数据
			function returndata(itemCode, itemName) {

				var obj = {
					itemCode : itemCode,
					itemName : itemName

				}

				window.returnValue = obj;
				window.close();
			};

			deptTree.on('click', function(node, e) {
						e.stopEvent();
						// deptIdChoose = node.id;

						if (node.attributes.description == 'Y') {

							returndata(node.id, node.text);
						} else {
							Ext.Msg.alert("提示", "该指标无数据！")
						}
					});
			rootNodeObj.expand();

			var innerPanel1 = new Ext.Panel({
						layout : "fit",
						hidden : true,
						border : false,
						frame : false,
						items : [{
									bodyStyle : "padding: 5,0,0,0",
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
									autoScroll : true,
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
						items : [

						{
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