Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	function treeClick(node, e) {
		document.getElementById("deptCode").value = node.attributes.deptCode;
		document.getElementById("deptName").value = node.text;
		e.stopEvent();
		ds.load({
					params : {
						deptId : node.id
					}
				});
	};
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				el : 'tree',
				title : '选择部门',
				root : root,
				autoHeight : true,
				animate : true,// 是否有动画效果
				enableDD : false,// 是否可以拖放节点
				useArrows : false, // 文件夹前显示的图标改变了不在是+号了
				border : false,
				rootVisible : true,
				containerScroll : true,// 是否含滚动条
				lines : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'equfailure/deptTree.action'
						})
			});

	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'equfailure/deptTree.action?pid='
						+ node.id;
			});

	var root = new Tree.AsyncTreeNode({
				text : '部门体系',
				iconCls : 'home',
				draggable : false,
				id : '0'
			});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);
	tree.render();

	var item = Ext.data.Record.create([{
				name : 'empCode'
			}, {
				name : 'chsName'
			}, {
				name : 'deptId'
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
					}
				}
			});
	var item_cm = new Ext.grid.ColumnModel([sm, {
				header : '员工编号',
				dataIndex : 'empCode',
				align : 'center'
			}, {
				header : '员工名称',
				dataIndex : 'chsName',
				align : 'center'
			}, {
				header : 'deptId',
				dataIndex : 'deptId',
				align : 'center',
				hidden : true
			}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'equfailure/empbyList.action'
						}),
				reader : new Ext.data.JsonReader({}, item)
			});

	var Grid = new Ext.grid.EditorGridPanel({
				title : '人员选择',
				ds : ds,
				cm : item_cm,
				sm : sm,
				autoWidth : true,
				autoScroll : true,
				// tbar : tbar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	Grid.on("rowdblclick", function(grid, rowIndex, e) {
		var rec = Grid.getStore().getAt(rowIndex);
		Ext.Msg.confirm("选择", "是否确定选择" + rec.get("chsName") + "？", function(buttonobj) {
					if (buttonobj == "yes") {
						var obj = new Object();
						obj.workerCode = rec.get("empCode");
						obj.workerName = rec.get("chsName");
						obj.deptCode = document.getElementById("deptCode").value;
						obj.deptName = document.getElementById("deptName").value;
						window.returnValue = obj;
						window.close();
					}
				});
	});

	var left = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				border : true,
				width : 200,
				collapsible : true,
				items : [tree]
			});
	var right = new Ext.Panel({
				region : "center",
				layout : 'fit',
				width : 200,
				collapsible : true,
				items : [Grid]
			});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [left, right]
			});
})