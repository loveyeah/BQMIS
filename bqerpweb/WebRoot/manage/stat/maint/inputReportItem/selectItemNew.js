Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			//班组指标
	var	groupTemp;
			var args = window.dialogArguments;

			var reportId = args.reportId;
			var reportType = args.reportType;
			if(reportType == '4'){
			 groupTemp = '2';
			}
			var MyRecord = Ext.data.Record.create([{
						name : 'baseInfo.reportId'
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
			// 数据库中已存在的数据，即父页面的指标记录。取出已判断选择是否重复。

			var RptRecord = Ext.data.Record.create([{
						name : 'id'
					}, {
						name : 'reportId'
					}, {
						name : 'itemCode'
					}, {
						name : 'itemAlias'
					}, {
						name : 'dataType'
					}, {
						name : 'orderBy'
					}, {
						name : 'bpCSmallitemRowtype.rowDatatypeId'
					}, {
						name : 'bpCSmallitemRowtype.rowDatatypeName'
					}]);

			var RptReader = new Ext.data.JsonReader({
					// root : "list",
					// totalProperty : "totalCount"
					}, RptRecord);

			var Oldstore = new Ext.data.Store({

						proxy : new Ext.data.HttpProxy({
									url : 'manager/getRelationItems.action'
								}),

						reader : RptReader

					});
			Oldstore.load({
						params : {
							reportId : reportId,
							start : 0,
							limit : 999999
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
				}
			}

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

			
			var qingkong = "";

			// 保存
			function saveModifies() {
				grid.stopEditing();
				var selectNodes = grid.getStore().getModifiedRecords();
				var ros = new Array();
				var i;
				for (i = 0; i < selectNodes.length; i++) {
					var obj = {
						reportId : selectNodes[i].get('baseInfo.reportId'),
						itemCode : selectNodes[i].get('baseInfo.itemCode'),
						displayNo : selectNodes[i].get('baseInfo.displayNo'),
						itemName : selectNodes[i].get('itemName')
					}
					ros.push(obj);
				}
				if (qingkong == "qk") {
					ros = null
				}
				window.returnValue = ros;
				window.close();
			}
			// 清空
			function cancel() {
				store.removeAll();
				qingkong = "qk";
			}
			// 关闭
			function close() {
				window.close();
			}

			// 定义grid
			var grid = new Ext.grid.EditorGridPanel({
						store : store,
						layout : 'fit',
						columns : [sm, // 选择框
								number, {
									header : "指标编码",
									sortable : false,
									dataIndex : 'baseInfo.itemCode',
									width : 100,
									renderer : function(value, metadata, record) {
										metadata.attr = 'style="white-space:normal;"';
										return value;
									}// modify by ywliu 20090911
								}, {
									header : "指标名称",
									align : "center",
									sortable : true,
									dataIndex : 'itemName',
									width : 200,
									renderer : function(value, metadata, record) {
										metadata.attr = 'style="white-space:normal;"';
										return value;
									}// modify by ywliu 20090911
								}, {
									header : "显示顺序",
									align : "center",
									sortable : true,
									dataIndex : 'baseInfo.displayNo',
									editor : new Ext.form.NumberField({
												maxLength : 10
											})
								}],
						tbar : [{
									text : "删除",
									iconCls : 'delete',
									handler : deleteRecords

								}, {
									text : "清空",
									iconCls : 'reflesh',
									handler : cancel
								}, '-', {
									text : "确定",
									iconCls : 'confirm',
									handler : saveModifies
								}, '-', {
									text : "关闭",
									iconCls : 'cancer',
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
								}),
						viewConfig : {
							forceFit : true
						}
					});

			/* 模糊查询的grid */

			var query = new Ext.form.TextField({
						id : 'argFuzzy',
						fieldLabel : "模糊查询",
						hideLabel : true,
						emptyText : '指标编码、指标名称',
						name : 'argFuzzy',
						value : ''
					});
			function fuzzyQuery() {
				con_ds.reload({
							params : {
								start : 0,
								limit : 18,
								argFuzzy : query.getValue(),
								 dataTimeType : groupTemp
							}
						});
			};
			var btnUt = new Ext.Button({
						text : '查询',
						iconCls : 'query',
						handler : fuzzyQuery
					});

			// Enter
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
			con_ds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									 dataTimeType : groupTemp,
									argFuzzy : query.getValue()
								});
					});
			con_ds.load({
						params : {
							 dataTimeType : groupTemp,
							argFuzzy : query.getValue(),
							start : 0,
							limit : 18
						}
					})
			var con_item_cm = new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(), {
						header : '指标编码',
						dataIndex : 'itemCode',
						align : 'center',
						width : 100,
						renderer : function(value, metadata, record) {
							metadata.attr = 'style="white-space:normal;"';
							return value;
						}// modify by ywliu 20090911
					}, {
						header : '指标名称',
						dataIndex : 'itemName',
						align : 'center',
						width : 180,
						renderer : function(value, metadata, record) {
							metadata.attr = 'style="white-space:normal;"';
							return value;
						}// modify by ywliu 20090911
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
						id : 'Grid',
						ds : con_ds,
						cm : con_item_cm,
						height : 295,
						split : true,
						autoScroll : true,
						bbar : gridbbar,
						border : false
					});

			Grid.on('rowdblclick', modifyBtn);
			function modifyBtn() {
				var recR = Grid.getSelectionModel().getSelected();
				var recs = store.getModifiedRecords();
				// for (i = 0; i < recs.length; i++) {
				// if (recs[i].get("baseInfo.itemCode") == recR.data.itemCode) {
				// Ext.Msg.alert("提示", "该报表已有此指标！");
				// return;
				// }
				// }
				var recs1 = Oldstore.getRange(0, Oldstore.getTotalCount());
				// for (i = 0; i < recs1.length; i++) {
				// if (recs1[i].get("itemCode") == recR.data.itemCode) {
				// Ext.Msg.alert("提示", "该报表已有此指标！");
				// return;
				// }
				// }
				if (recR) {
					var currentRecord = grid.getSelectionModel().getSelected();
					var rowNo = store.indexOf(currentRecord);
					var count = store.getCount();
					var currentIndex = currentRecord ? rowNo : count;
					var o = new MyRecord({
								'baseInfo.reportId' : reportId,
								'baseInfo.itemCode' : recR.data.itemCode,
								'baseInfo.displayNo' : '',
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

						var recs = store.getModifiedRecords();
						// for (i = 0; i < recs.length; i++) {
						// if (recs[i].get("baseInfo.itemCode") == node.id) {
						// Ext.Msg.alert("提示", "该报表已有此指标！");
						// return;
						// }
						// }
						var recs1 = Oldstore.getRange(0, Oldstore
										.getTotalCount());
						// for (i = 0; i < recs1.length; i++) {
						// if (recs1[i].get("itemCode") == node.id) {
						// Ext.Msg.alert("提示", "该报表已有此指标！");
						// return;
						// }
						// }

						if (node.attributes.description == 'Y') {
							var currentRecord = grid.getSelectionModel()
									.getSelected();
							var rowNo = store.indexOf(currentRecord);
							var count = store.getCount();
							var currentIndex = currentRecord ? rowNo : count;
							var o = new MyRecord({
										'baseInfo.reportId' : reportId,
										'baseInfo.itemCode' : node.id,
										'baseInfo.displayNo' : '',
										'itemName' : node.text
									});
							grid.stopEditing();
							store.insert(currentIndex, o);
							sm.selectRow(currentIndex);
							resetLine();

						} else {
							Ext.Msg.alert("提示", "该指标无数据！")
						}
					});
			rootNodeObj.expand();

			var innerPanel1 = new Ext.Panel({
						layout : "fit",
						id : 'innerPanel1',
						hidden : true,
						border : false,
						frame : false,
						tbar : [query, ' ', btnUt],
						items : [Grid]
					});

			var rightPanel = new Ext.Panel({
						layout : "fit",
						border : true,
						tbar : [{
									text : "项目选择",
									id : "prjChose",
									handler : prjChose
								}, '-', {
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
									width : '50%',
									items : [grid]
								}, {
									region : "center",
									border : false,
									bodyStyle : "padding: 0,0,0,5",
									frame : false,
									layout : 'fit',
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
				fuzzyQuery();
			}

		})