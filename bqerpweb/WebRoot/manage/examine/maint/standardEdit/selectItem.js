Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			var args = window.dialogArguments;

			var MyRecord = Ext.data.Record.create([{
						name : 'itemCode'
					}, {
						name : 'itemName'
					}, {
						name : 'itemId'
					}]);

			var dataProxy = new Ext.data.HttpProxy({
						url : 'managexam/getExamItemList.action'
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
							start : 0,
							limit : 18
						}
					});

			// 数据库中已存在的数据，即父页面的指标记录。取出已判断选择是否重复。
			var RptRecord = Ext.data.Record.create([{
						name : 'itemCode'
					},{
						name : 'itemName'
					}, {
						name : 'itemId'
					}]);

			var RptReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
					}, RptRecord);

			var Oldstore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'managexam/getExamItemList.action'
								}),
						reader : RptReader
					});

			Oldstore.load({
						params : {
							start : 0,
							limit : 999999
						}
					});

			var sm = new Ext.grid.CheckboxSelectionModel();

			number = new Ext.grid.RowNumberer({
						header : "",
						align : 'left'
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
								searchKey : query.getValue()
							}
						});
			};

			var btnUt = new Ext.Button({
						text : '查询',
						iconCls : 'query',
						handler : fuzzyQuery
					});

			// 签字时的Enter
			document.onkeydown = function() {
				if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
					fuzzyQuery();
				
				}
			}

			var con_item = Ext.data.Record.create([{
						name : 'itemCode'
					}, {
						name : 'itemName'
					}, {
						name : 'itemId'
					}, {
						name : 'isItem'
					}]);

			var con_sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true

					});

			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'managexam/findItemList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : "list"
								}, con_item)
					});

			con_ds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									argFuzzy : query.getValue()
								});
					});

			con_ds.load({
						params : {
							argFuzzy : query.getValue(),
							start : 0,
							limit : 18
						}
					});

			var con_item_cm = new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(), {
						header : '指标编码',
						dataIndex : 'itemCode',
						align : 'center',
						width : 90
						// modify by ywliu 20090911
				}	, {
						header : '指标名称',
						dataIndex : 'itemName',
						align : 'center',
						width : 160,// modify by ywliu 20090911
						renderer : function(value, metadata, record) {
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
						beforePageText : '第',
						afterPageText : "共{0}"
					});

			var Grid = new Ext.grid.GridPanel({
						id : 'Grid',
						ds : con_ds,
						cm : con_item_cm,
						height : 320,
						split : true,
						autoScroll : true,
						bbar : gridbbar,
						border : false
					});

			Grid.on('rowdblclick', modifyBtn);

			function modifyBtn() {
				var recR = Grid.getSelectionModel().getSelected();
				var recs = store.getModifiedRecords();
				if (recR) {
					if (recR.data.isItem != 'Y') {
						Ext.Msg.alert("提示", "该选项不是指标！")
						return false;
					}
				   else
		            {
		  		     if(!confirm("确定选择["+recR.data.itemName+"]吗?"))
		  		     return ;
		  	         window.returnValue ={id:recR.get("itemId"),code:recR.get("itemCode"),name:recR.get("itemName")}; // recR.data;
		             window.close();
		            } 
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
									dataUrl : 'managexam/findItemTree.action'
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
						deptTree.loader.dataUrl = 'managexam/findItemTree.action?pid='
								+ node.id;
					});
			deptTree.on('click', function(node, e) {
				e.stopEvent(node.attributes.code);
				var recs = store.getModifiedRecords();
//				
				if (node.attributes.description != "Y") {
					Ext.Msg.alert("提示", "该选项不是指标！");
					return;
				}
				else
		  {
		  		if(!confirm("确定选择["+node.text+"]吗?"))
		  		return ;
		  	    window.returnValue = {id:node.attributes.id,code:node.attributes.code,name:node.text};//node.attributes;
		        window.close();
		  } 
				
				resetLine();
					
				});

			rootNodeObj.expand();

			var innerPanel1 = new Ext.Panel({
						layout : "fit",
						id : 'innerPanel1',
						hidden : true,
						autoScroll : true,
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
					});

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						border : false,
						frame : false,
						items : [
									{
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